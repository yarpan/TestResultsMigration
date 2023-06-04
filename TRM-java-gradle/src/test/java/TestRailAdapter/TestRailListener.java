package TestRailAdapter;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class TestRailListener extends TestListenerAdapter {

    @Override
    public void onTestSuccess(ITestResult result) {
        String testFullName = result.getInstanceName() + "." + result.getName();
        System.out.println("TestSuccess! testFullName = " + testFullName);
        TestRailActions testRailActions = new TestRailActions();
        testRailActions.addTestResult(testFullName, TestRailSettings.testStatusId_passed, TestRailSettings.testResultCommentForSuccessTest);
    }


    @Override
    public void onTestFailure(ITestResult result) {
        String testFullName = result.getInstanceName() + "." + result.getName();
        String testResultException = String.valueOf(result.getThrowable());
        System.out.println("TestFailure! testFullName: " + testFullName);
        System.out.println("Validation message: " + testResultException);
        TestRailActions testRailActions = new TestRailActions();
        testRailActions.addTestResult(testFullName, TestRailSettings.testStatusId_failed, testResultException);
    }


    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("TestSkipped \n result.getSkipCausedBy() = " + result.getSkipCausedBy().toString());
    }









}
