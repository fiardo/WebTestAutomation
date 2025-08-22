package uniliv.AbstractMethods;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

public class ScreenshotUtility {

    public static String takeScreenshot(WebDriver driver, String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotDir = System.getProperty("user.dir") + "/screenshots";
        File dir = new File(screenshotDir);
        if (!dir.exists()) dir.mkdirs();

        String screenshotPath = screenshotDir + "/" + testName + "_" + timestamp + ".png";

        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(src, new File(screenshotPath));
            Reporter.log("<a href='" + screenshotPath + "' target='_blank'>Screenshot</a>");
        } catch (IOException e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
        }

        return screenshotPath;
    }
}

