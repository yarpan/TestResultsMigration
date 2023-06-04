package XrayAdapter;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.List;

public class XrayActions {

    public void createXrayToken() {
        String requestUrl = XraySettings.baseURL + XraySettings.endpointAauthenticate;
        String body = "{\"client_id\": \"" + XraySettings.xRayClient_id + "\", \"client_secret\": \"" + XraySettings.xRayClient_secret + "\" }";
        String newToken = responseBuilderPostString(requestUrl, body);
        XraySettings.setBearerToken(newToken.substring(1,newToken.length()-1));
    }


    public void addXrayTestResult(String testFullName, String testResult, String testResultMessage) {
        String jiraTestKey = getJiraIssueKey(testFullName);
        System.out.println("jiraIssueSearchResults: " + jiraTestKey);
        if (jiraTestKey.equals("0")){
            System.out.println("jiraTestKey not found, creating new Test...");
            jiraTestKey = createXrayTest(testFullName);
        }
        String[] testResCur = new String[] {jiraTestKey, testResult, testResultMessage};
        XraySettings.addTestResults(testResCur);
    }


    public String createXrayTest(String testFullName) {
        String requestUrl = XraySettings.baseURL + XraySettings.endpointImportTest;
        String body = "[{\"testtype\": \"" + XraySettings.testTypeDefault + "\",\"fields\": {\"summary\": \""
                + testFullName + "\",\"project\": { \"key\": \"" + XraySettings.jiraProjectKey + "\" }},\"xray_test_sets\": [\""
                + XraySettings.testSetDefaultForNewTests + "\"]}]";
        HttpResponse<JsonNode> response = responseBuilderPost(requestUrl, body);
        String jobId = response.getBody().getObject().get("jobId").toString();
        System.out.println("jobId: " + jobId);
        System.out.println("response: " + response.getBody().toString());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String requestUrl2 = XraySettings.baseURL + XraySettings.endpointImportTest + "/" + jobId + "/status";
        HttpResponse<JsonNode> response2 = responseBuilderGet(requestUrl2);
        String status = response2.getBody().getObject().get("status").toString();
        System.out.println("status: " + status);
        System.out.println("response2: " + response2.getBody().toString());
        if (!status.equals("successful")){
            return null;
        }
        String testKey = response2.getBody().getObject().getJSONObject("result").getJSONArray("issues").getJSONObject(0).get("key").toString();
        System.out.println("testKey: " + testKey);
        return testKey;
    }


    public String getJiraIssueKey(String issueSummary) {
        String requestUrl = "https://" + XraySettings.jiraSiteName + XraySettings.jiraEndpointSearch + issueSummary + XraySettings.jiraSearchCondition;
        System.out.println("requestUrl : " + requestUrl);
        HttpResponse<JsonNode> response = responseBuilderGetJira(requestUrl);
        int searchResultNumber = Integer.parseInt(response.getBody().getObject().get("total").toString());
        System.out.println("found issues with given summary: " + searchResultNumber);
        if (searchResultNumber == 0){
            return "0";
        }
        if (XraySettings.isUseLatestKeyForDuplicateTests){
            searchResultNumber = 1;
        }
        String issueKey = response.getBody().getObject().getJSONArray("issues").getJSONObject(searchResultNumber-1).get("key").toString();
        System.out.println("Issue Key = " + issueKey);
        return issueKey;
    }


    public void postTestResults(){
        List<String[]> testResults = XraySettings.getTestResults();
        String executionNumberIfShouldUseExisting = "";

        if (!XraySettings.isCreateNewTestExecution){
            executionNumberIfShouldUseExisting = "\"testExecutionKey\": \"" + XraySettings.testExecutionKeyDefault + "\",";
        }

        String requestBody = "{" + executionNumberIfShouldUseExisting + "\"info\" : {\"summary\" : \"" + XraySettings.executionSummary
                + "\",\"description\" : \"" + XraySettings.executionDescription+ "\",\"user\" : \"" + XraySettings.jiraUserName
                + "\",\"testPlanKey\" : \"" + XraySettings.testPlanDefaultForNewTests + "\" }, \"tests\" : [";

        String requestBody2 = "{\"info\" : {\"summary\" : \"" + XraySettings.executionSummary + "\",\"description\" : \""
                + XraySettings.executionDescription+ "\",\"user\" : \"" + XraySettings.jiraUserName + "\",\"testPlanKey\" : \"" + XraySettings.testPlanDefaultForNewTests + "\" }, \"tests\" : [";

        for (String[] testResCur : testResults) {
            String testResult = "{\"testKey\" : \"" + testResCur[0] + "\",\"status\" : \"" + testResCur[1] + "\",\"comment\" : \"" + testResCur[2] + "\"},";
            System.out.println("testResult: " + testResult);
            requestBody += testResult;
        }

        requestBody = requestBody.substring(0,requestBody.length()-1) + "]}";
        System.out.println("requestBody: " + requestBody);
        String requestUrl = XraySettings.baseURL + XraySettings.endpointImportExecution;
        HttpResponse<JsonNode> response = responseBuilderPost(requestUrl, requestBody);
        System.out.println("response: " + response.getBody().toString());

    }


    public HttpResponse<JsonNode> responseBuilderGetJira(String requestUrl){
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest
                    .get(requestUrl)
                    .basicAuth(XraySettings.jiraUserName, XraySettings.jiraUserToken)
                    .header("Content-Type", "application/json")
                    .asJson();
            System.out.println("response Status: " + response.getStatus());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return response;
    }


    public String responseBuilderPostString(String requestUrl, String body) {
        HttpResponse<String> response = null;
        try {
            response = Unirest
                    .post(requestUrl)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .asString();
            System.out.println("response Status: " + response.getStatus());
            return response.getBody();

        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }


    public HttpResponse<JsonNode> responseBuilderGet(String requestUrl){
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest
                    .get(requestUrl)
                    .header("Authorization", "Bearer " + XraySettings.getBearerToken())
                    .header("Content-Type", "application/json")
                    .asJson();
            System.out.println("response Status: " + response.getStatus());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return response;
    }


    public HttpResponse<JsonNode> responseBuilderPost(String requestUrl, String body){
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest
                    .post(requestUrl)
                    .header("Authorization", "Bearer " + XraySettings.getBearerToken())
                    .header("Content-Type", "application/json")
                    .body(body)
                    .asJson();
            System.out.println("response Status: " + response.getStatus());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return response;
    }



}