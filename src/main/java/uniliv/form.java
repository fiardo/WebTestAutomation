package uniliv;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class form {
    public static void main(String[] args) throws InterruptedException {

        WebDriverManager.chromedriver().setup();

        // chrome options
        ChromeOptions browseroptions = new ChromeOptions();
        browseroptions.addArguments("--disable-notifications");
        WebDriver driver = new ChromeDriver(browseroptions);
        driver.manage().window().maximize();
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        Thread.sleep(5000);

        driver.quit();

    }
}
