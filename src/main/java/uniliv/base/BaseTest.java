package uniliv.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;

public class BaseTest {

    public WebDriver driver;
    public WebDriver invokedriver() throws InterruptedException {
        ChromeOptions browseroptions = new ChromeOptions();
        browseroptions.addArguments("--disable-notifications");
        WebDriver driver = new ChromeDriver(browseroptions);
        driver.manage().window().maximize();
        driver.get("https://stg-next.universityliving.com/");
        Thread.sleep(5000);
        return driver;
    }

    @BeforeMethod(alwaysRun = true)
    public void Launchapplication() throws InterruptedException {
        driver = invokedriver();
    }

    @AfterMethod(alwaysRun = true)
    public void quitbrowser(){
        driver.quit();
    }

}
