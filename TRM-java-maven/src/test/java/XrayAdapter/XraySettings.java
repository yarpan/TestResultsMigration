package XrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class XraySettings {


    /**
     * SECTION 1 - Project Settings
     * PART 1 - Xray Project Settings **/
    public static String jiraSiteName = "testresmig";
    public static String jiraProjectKey = "TRM";
    public static String jiraUserName = "testresmig@gmail.com";
    public static String jiraUserToken = "uSCAvyWz5pNRsXmlZ7eIB831";
    public static String xRayClient_id = "28984BAA666F4F4A87DB365E918DA03B";
    public static String xRayClient_secret = "df38857f67acc72dd600bf6338171664e7714c02cc31995832b887f0cc9fed35";
    public static String testPlanDefaultForNewTests = "TRM-59";
    public static String testSetDefaultForNewTests = "TRM-63";
    public static String testExecutionKeyDefault = "TRM-50"; //if not create new each time, than use this one
    public static boolean isCreateNewTestExecution = true; //true to create new TestExecution for each run, false to use Default
    public static boolean isUseLatestKeyForDuplicateTests = true; //if Jira has some Tests with same name, true to use the latest one, false to use first one
    public static String testTypeDefault = "Automation";
    public static String commentForSuccessTest = "Automation test passed successfully";
    public static String executionSummary = "Sample Summary for Automated execution";
    public static String executionDescription = "Sample Description of Automated execution";


    /**
     * SECTION 2 - Xray API Settings
     * do not change until global Xray API settings changed **/
    public static String baseURL = "https://xray.cloud.getxray.app/api/v2/";
    public static String endpointAauthenticate = "authenticate";
    public static String endpointImportExecution = "import/execution";
    public static String endpointImportTest = "import/test/bulk";
    /** Jira API Settings
     * do not change until global Jira API settings changed **/
    public static String jiraEndpointSearch = ".atlassian.net/rest/api/3/search?jql=summary~";
    public static String jiraSearchCondition = "%20AND%20issuetype=Test";


    /**
     * SECTION 3 - Runtime Parameters
     **/
    private static String bearerToken;
    public static String getBearerToken() {
        return bearerToken;
    }
    public static void setBearerToken(String newToken) {
        bearerToken = newToken;
    }
    private static List<String[]> testResults = new ArrayList<String[]>();
    public static void addTestResults(String[] testResCur) {
        testResults.add(testResCur);
    }
    public static List<String[]> getTestResults() {
        return testResults;
    }

}





