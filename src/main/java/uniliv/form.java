package uniliv;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class form {
    public static void main(String[] args) throws InterruptedException {

        WebDriverManager.chromedriver().setup();

        // chrome options
        ChromeOptions browseroptions = new ChromeOptions();
        browseroptions.addArguments("--disable-notifications");
        WebDriver driver = new ChromeDriver(browseroptions);
        driver.manage().window().maximize();
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        // Add WebDriverWait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//p[@class='oxd-text oxd-text--p orangehrm-login-forgot-header']")
        ));
        driver.findElement(By.xpath("//p[@class='oxd-text oxd-text--p orangehrm-login-forgot-header']")).click();

        driver.quit();

    }
}