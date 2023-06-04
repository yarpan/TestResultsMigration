package TestRailAdapter;

public class TestRailSettings {


    /**
     * SECTION 1 - Project Settings
     **/
    public static String siteName = "trm";
    public static int projectId = 1;
    public static String userName = "testresmig@gmail.com";
    public static String userPassword = "whD2YwpHTuXOAei5k4AB-VuGI57rF80VBffUSIqih";
    public static String testResultCommentForSuccessTest = "Automation test passed successfully";
    public static boolean isUseLastRun = false; //If true then testRunIdDefault skipped in logic
    public static int testRunIdDefault = 1; // Test Run ID for test results
    public static int testSectionIdDefault = 1; // Test Suite where new Test Cases will be created if there are no present yet


    /**
     * SECTION 2 - TESTRAIL API SETTINGS
     **/
    public static int testStatusId_passed = 1;
    public static int testStatusId_failed = 5;
    public static String testrailEndpointBase = ".testrail.com/index.php?/api/v2/";
    public static String testrailEndpointAddResult = "add_result_for_case/";
    public static String testrailEndpointGetCases = "get_cases/";
    public static String testrailEndpointAddCase = "add_case/";
    public static String testrailEndpointGetRuns = "get_runs/";


}
