package TestRailAdapter;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class TestRailActions {

    public void addTestResult(String testCaseName, int testStatusId, String testResultText) {
        int testRunId = defineTestRunId();
        int testCaseId = defineTestCaseId(testCaseName);
        String requestUrl = "https://" + TestRailSettings.siteName + TestRailSettings.testrailEndpointBase
                + TestRailSettings.testrailEndpointAddResult + testRunId + "/" + testCaseId;
        String body = "{\"status_id\": " + testStatusId + ", \"comment\": \"" + testResultText + "\"}";
        System.out.println("Adding Test Result to TestRail");
        responseBuilderPost(requestUrl, body);
    }


    private int defineTestCaseId(String testCaseName) {
        int testCaseId = getTestCaseId(testCaseName);
        if (testCaseId > 0) {
            System.out.println("Test Case for this autotest is found. Case Id=" + testCaseId);
            return testCaseId;
        } else {
            return createTestCase(testCaseName);
        }
    }


    private int getTestCaseId(String testCaseName) {
        String requestUrl = "https://" + TestRailSettings.siteName + TestRailSettings.testrailEndpointBase
                + TestRailSettings.testrailEndpointGetCases + TestRailSettings.projectId + "&filter=" + testCaseName;
        HttpResponse<JsonNode> response = responseBuilderGet(requestUrl);
        int testCaseId = Integer.parseInt(response.getBody().getObject().get("size").toString());
        if (testCaseId > 0) {
            testCaseId = Integer.parseInt(response.getBody().getObject().getJSONArray("cases").getJSONObject(0).get("id").toString());
        }
        return testCaseId;
    }


    private int createTestCase(String testCaseName) {
        String requestUrl = "https://" + TestRailSettings.siteName + TestRailSettings.testrailEndpointBase
                + TestRailSettings.testrailEndpointAddCase + TestRailSettings.testSectionIdDefault;
        String body = "{\"title\": \"" + testCaseName + "\"}";
        HttpResponse<JsonNode> response = responseBuilderPost(requestUrl, body);
        int testCaseId = Integer.parseInt(response.getBody().getObject().get("id").toString());
        System.out.println("Test Case for this autotest NOT found. Creating new Test Case with Id=" + testCaseId);
        return testCaseId;
    }


    private int defineTestRunId() {
        if (TestRailSettings.isUseLastRun) {
            return getLastTestRunId();
        } else {
            System.out.println("Using Default Test Run ID from Settings for publishing Test Results. testRunIdDefault=" + TestRailSettings.testRunIdDefault);
            return TestRailSettings.testRunIdDefault;
        }
    }


    private int getLastTestRunId() {
        String requestUrl = "https://" + TestRailSettings.siteName + TestRailSettings.testrailEndpointBase
                + TestRailSettings.testrailEndpointGetRuns + TestRailSettings.projectId;
        HttpResponse<JsonNode> response = responseBuilderGet(requestUrl);
        int getLastRun = Integer.parseInt(response.getBody().getObject().get("size").toString());
        System.out.println("Using last existing Test Run ID for publishing Test Results. testRunId=" + getLastRun);
        return getLastRun;
    }


    private HttpResponse<JsonNode> responseBuilderGet(String requestUrl) {
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get(requestUrl)
                    .basicAuth(TestRailSettings.userName, TestRailSettings.userPassword)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .asJson();
            System.out.println("response Status: " + response.getStatus());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return response;
    }


    private HttpResponse<JsonNode> responseBuilderPost(String requestUrl, String body) {
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.post(requestUrl)
                    .basicAuth(TestRailSettings.userName, TestRailSettings.userPassword)
                    .header("Accept", "application/json")
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
