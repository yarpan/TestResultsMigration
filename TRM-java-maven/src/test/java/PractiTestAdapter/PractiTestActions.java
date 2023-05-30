package PractiTestAdapter;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PractiTestActions {

    public void addTestResult(String testCaseName, int testStatusId, String testResultText) {
        int instanceId = selectInstanceId(testCaseName);
        String requestUrl = PractiTestSettings.baseURL + PractiTestSettings.projectId + "/runs.json";
        String body = "{\"data\": { \"type\": \"instances\", \"attributes\": {\"instance-id\": \"" + instanceId
                + "\", \"exit-code\": " + testStatusId + ", \"automated-execution-output\": \"" + testResultText + "\" }}}";
        System.out.println("Adding Test Result to PractiTest");
        responseBuilderPost(requestUrl, body);
    }


    private int selectInstanceId(String testCaseName){
        if(PractiTestSettings.isCreateNewInstanceForResults){
            return createNewInstance(testCaseName);
        }else {
            int lastInstanceId = getLastInstance(testCaseName);
            if (lastInstanceId > 0){
                return lastInstanceId;
            }else{
                return createNewInstance(testCaseName);
            }
        }
    }


    private int selectTestId(String testName){
        int testId = getTestId(testName);
        if(testId > 0){
            return testId;
        }else {
            return createNewTest(testName);
        }
    }


    public void selectTestSetId(String executableSuiteName){
        int selectedTestSetId;
        if (PractiTestSettings.isTestSetUse_SuiteName) {
            int executableSuiteId = getTestSetId(executableSuiteName);
            if (executableSuiteId > 0) {
                selectedTestSetId = executableSuiteId;
            } else {
                selectedTestSetId = createNewTestSet(executableSuiteName);
            }
        }else if(PractiTestSettings.isTestSetUse_NewTimeStamp){
            selectedTestSetId = createNewTestSet(getTimeStamp("yyyy-MM-dd_HH-mm"));
        }else{
            selectedTestSetId = PractiTestSettings.testSetId_Default ;
        }
        PractiTestSettings.setSetId(selectedTestSetId);
    }


    private int getLastInstance(String testCaseName){
        String requestUrl = PractiTestSettings.baseURL + PractiTestSettings.projectId + "/instances.json?name_like=" + testCaseName;
        HttpResponse<JsonNode> response = responseBuilderGet(requestUrl);
        return parseResponse(response);
    }


    private int getTestId(String testName){
        System.out.println("testName: " + testName);
        String requestUrl = PractiTestSettings.baseURL + PractiTestSettings.projectId + "/tests.json?name_like=" + testName;
        HttpResponse<JsonNode> response = responseBuilderGet(requestUrl);
        return parseResponse(response);
    }


    private int getTestSetId(String testSetName){
        System.out.println("testSetName: " + testSetName);
        String requestUrl = PractiTestSettings.baseURL + PractiTestSettings.projectId + "/sets.json?name_like=" + testSetName;
        HttpResponse<JsonNode> response = responseBuilderGet(requestUrl);
        return parseResponse(response);
    }


    public int parseResponse (HttpResponse<JsonNode> response){
        int totalCount = Integer.parseInt(response.getBody().getObject().getJSONObject("meta").get("total-count").toString());
        if (totalCount > 0) {
            int lastId = Integer.parseInt(response.getBody().getObject().getJSONArray("data").getJSONObject(0).get("id").toString());
            return lastId;
        }else return 0;
    }


    private int createNewInstance(String testName){
        int testId = selectTestId(testName);
        int testSetId = PractiTestSettings.getSetId();
        String requestUrl = PractiTestSettings.baseURL + PractiTestSettings.projectId + "/instances.json";
        String body = "{\"data\": [{ \"type\": \"instances\", \"attributes\": {\"test-id\": " + testId + ", \"set-id\":" + testSetId + "}}]}";
        HttpResponse<JsonNode> response = responseBuilderPost(requestUrl, body);
        int newTestId = Integer.parseInt(response.getBody().getObject().getJSONObject("data").get("id").toString());
        return newTestId;
    }


    private int createNewTest(String testName){
        String requestUrl = PractiTestSettings.baseURL + PractiTestSettings.projectId + "/tests.json";
        String body = "{\"data\": { \"type\": \"tests\", \"attributes\": {\"name\": \"" + testName + "\", \"author-id\": "
                + PractiTestSettings.userId + ", \"priority\": \"high\", \"status\": \"Ready\"}}}";
        HttpResponse<JsonNode> response = responseBuilderPost(requestUrl, body);
        int newTestId = Integer.parseInt(response.getBody().getObject().getJSONObject("data").get("id").toString());
        System.out.println("newSetId: " + newTestId);
        return newTestId;
    }


    private int createNewTestSet(String testSetName){
        String requestUrl = PractiTestSettings.baseURL + PractiTestSettings.projectId + "/sets.json";
        String body = "{\"data\": { \"type\": \"sets\", \"attributes\": {\"name\": \"" + testSetName + "\", \"priority\": \"high\"}}}";
        HttpResponse<JsonNode> response = responseBuilderPost(requestUrl, body);
        int newSetId = Integer.parseInt(response.getBody().getObject().getJSONObject("data").get("id").toString());
        return newSetId;
    }


    private HttpResponse<JsonNode> responseBuilderGet(String requestUrl) {
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get(requestUrl)
                    .header("PTToken", PractiTestSettings.userToken)
                    .header("Content-Type", "application/json")
                    .asJson();
            System.out.println("response Status: " + response.getStatus() + " - " + response.getStatusText());
            if (!Integer.toString(response.getStatus()).startsWith("20")){
                System.out.println("response: " + response.getBody());
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return response;
    }


    private HttpResponse<JsonNode> responseBuilderPost(String requestUrl, String body) {
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.post(requestUrl)
                    .header("PTToken", PractiTestSettings.userToken)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .asJson();
            System.out.println("response Status: " + response.getStatus() + " - " + response.getStatusText());
            if (!Integer.toString(response.getStatus()).startsWith("20")){
                System.out.println("response: " + response.getBody());
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return response;
    }


    public static String getTimeStamp(String timeStampFormat) {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat(timeStampFormat);
        return dateFormat.format(date);
    }


}
