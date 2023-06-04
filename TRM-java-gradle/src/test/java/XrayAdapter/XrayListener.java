package XrayAdapter;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class XrayListener extends TestListenerAdapter {

    @Override
    public void onTestSuccess(ITestResult result) {
        String testFullName = result.getInstanceName() + "." + result.getName();
        System.out.println("TestSuccess! testFullName = " + testFullName);
        XrayActions xrayActions = new XrayActions();
        xrayActions.addXrayTestResult(testFullName, "PASSED", XraySettings.commentForSuccessTest);
    }


    @Override
    public void onTestFailure(ITestResult result) {
        String testFullName = result.getInstanceName() + "." + result.getName();
        String testResultException = String.valueOf(result.getThrowable());
        System.out.println("TestFailure! \n testFullName: " + testFullName);
        System.out.println("testResult: \n" + testResultException);
        System.out.println("Seaching for opened Issue for Failed Test...");
        XrayActions xrayActions = new XrayActions();
        xrayActions.addXrayTestResult(testFullName, "FAILED", testResultException);
    }


    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("TestSkipped \n result.getSkipCausedBy() = " + result.getSkipCausedBy().toString());
    }

}
