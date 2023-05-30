package PractiTestAdapter;

public class PractiTestSettings {


    /**
     * SECTION 1 - Project Settings
     **/
    public static String baseURL = "https://eu1-prod-api.practitest.app/api/v2/projects/";
    public static int projectId = 13537;
    public static int userId = 28799;
    public static String userToken = "c7466f4a6fcc805ac4a74b47c96f6ba3ce75157d";
    public static String testResultCommentForSuccessTest = "Automation test passed successfully";
    public static boolean isCreateNewInstanceForResults = true; //if false - last Instance ID will be used for new Test Result Publishing
    public static boolean isTestSetUse_SuiteName = true; //if true, priority 1, TestSet name will be taken from automation Test Class Name
    public static boolean isTestSetUse_NewTimeStamp = false; //if true, priority 2, TestSet name will be created from current time stamp
    public static int testSetId_Default = 527617; //priority 3, default ID if 1 and 2 are false, default ID will be used for TestSet


    /**
     * SECTION 2 - PractiTest API Settings
     * do not change until global PractiTest API settings changed
     **/
    public static int testStatusId_passed = 0;
    public static int testStatusId_failed = 1;


    /**
     * SECTION 3 - Runtime Parameters
     * do not change
     **/
    private static int testSetId_Current;
    public static int getSetId() {
        return testSetId_Current;
    }
    public static void setSetId(int newSetId) {
        testSetId_Current = newSetId;
    }


}
