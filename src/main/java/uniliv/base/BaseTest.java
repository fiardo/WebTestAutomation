package uniliv.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;
import org.testng.annotations.Listeners;
import uniliv.AbstractMethods.LogUtility;
import uniliv.AbstractMethods.TestListener;

@Listeners(TestListener.class)
public class BaseTest {

    protected WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    public WebDriver invokedriver() throws InterruptedException {
        ChromeOptions browseroptions = new ChromeOptions();
        browseroptions.addArguments("--disable-notifications");
        WebDriver driver = new ChromeDriver(browseroptions);
        driver.manage().window().maximize();
        driver.get("https://stg-next.universityliving.com/");
        Thread.sleep(3000);
        return driver;
    }

    @BeforeMethod(alwaysRun = true)
    public void Launchapplication() throws InterruptedException {
        driver = invokedriver();
    }

    @AfterMethod(alwaysRun = true)

    public void tearDown() {
        if (driver != null) {
            driver.quit();
            LogUtility.info("Browser closed");
        }
    }
}

