package uniliv.AbstractMethods;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.testng.*;
import uniliv.base.BaseTest;

public class TestListener implements ITestListener, ISuiteListener {

	private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

	public static ExtentTest getCurrentTest() {
		return extentTest.get();
	}

	private ExtentReports getExtent() {
		return ExtentReporterNG.getReporter();
	}

	@Override
	public void onStart(ISuite suite) {
		getExtent();
	}

	@Override
	public void onFinish(ISuite suite) {
		getExtent().flush();
	}

	@Override
	public void onStart(ITestContext context) {
		// No-op
	}

	@Override
	public void onFinish(ITestContext context) {
		// No-op; flushing handled at suite level
	}

	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentTest test = getExtent().createTest(testName);
		extentTest.set(test);
		LogUtility.startTest(testName);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
        ExtentTest test = extentTest.get();
        if (test != null) {
            test.log(Status.PASS, "Test passed");
        }
        //LogUtility.pass("Test PASSED: " + result.getName());
        LogUtility.endTest(result.getName(), "PASSED");
        extentTest.remove();
    }

	@Override
	public void onTestFailure(ITestResult result) {
		ExtentTest test = extentTest.get();
		Throwable throwable = result.getThrowable();
		if (test != null && throwable != null) {
			test.fail(throwable);
		}

		WebDriver driver = null;
		Object instance = result.getInstance();
		if (instance instanceof BaseTest) {
			driver = ((BaseTest) instance).getDriver();
		}

		String screenshotPath = null;
		if (driver != null) {
			screenshotPath = ScreenshotUtility.takeScreenshot(driver, result.getName());
		}
		if (test != null && screenshotPath != null) {
			try {
				test.addScreenCaptureFromPath(screenshotPath);
			} catch (Exception e) {
				// Ignore attaching failures to avoid masking original failure
			}
		}

		LogUtility.fail("Test FAILED: " + result.getName());
		if (throwable != null) {
			LogUtility.error("Failure reason", new Exception(throwable));
		}
		LogUtility.endTest(result.getName(), "FAILED");
		extentTest.remove();
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		ExtentTest test = extentTest.get();
		if (test != null) {
			test.log(Status.SKIP, "Test skipped");
		}
		LogUtility.warn("Test SKIPPED: " + result.getName());
		LogUtility.endTest(result.getName(), "SKIPPED");
		extentTest.remove();
	}


}


