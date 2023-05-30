package PractiTestAdapter;

import org.testng.*;

public class PractiTestListener extends TestListenerAdapter {

    @Override
    public void onTestSuccess(ITestResult result) {
        String testFullName = result.getInstanceName() + "." + result.getName();
        System.out.println("TestSuccess! testFullName = " + testFullName);
        PractiTestActions practiTestActions = new PractiTestActions();
        practiTestActions.addTestResult(testFullName, PractiTestSettings.testStatusId_passed, PractiTestSettings.testResultCommentForSuccessTest);
    }


    @Override
    public void onTestFailure(ITestResult result) {
        String testFullName = result.getInstanceName() + "." + result.getName();
        String testResultException = String.valueOf(result.getThrowable());
        System.out.println("TestFailure! testFullName: " + testFullName);
        System.out.println("Validation message: " + testResultException);
        PractiTestActions practiTestActions = new PractiTestActions();
        practiTestActions.addTestResult(testFullName, PractiTestSettings.testStatusId_failed, testResultException);
    }


    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("TestSkipped \n result.getSkipCausedBy() = " + result.getSkipCausedBy().toString());
    }


}