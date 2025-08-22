package uniliv.AbstractMethods;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReporterNG
{

    private static ExtentReports extent;

    public static ExtentReports getReporter() {
        if (extent == null) {
            ExtentSparkReporter Reporter = new ExtentSparkReporter (System.getProperty("user.dir") + "/Reports/Report.html");
            Reporter.config().setDocumentTitle("Automation Report");
            Reporter.config().setReportName("QA Execution Report");
            Reporter.config().setTheme(Theme.STANDARD);

            extent = new ExtentReports();
            extent.attachReporter(Reporter);
        }
        return extent;
    }

}
