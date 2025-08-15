package uniliv.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import uniliv.AbstractMethods.AbstractComponents;

public class HomePage extends AbstractComponents {

    WebDriver driver;


    // Homepage method
    public HomePage(WebDriver driver){
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    @FindBy(xpath = "(//input[@placeholder='Search for City, University, Property'])[2]")
    WebElement searchbar;

    @FindBy(id = "async-pagination-example-item-0")
    WebElement searchbarselector;


    public void searchAndSelectFirstOption() throws InterruptedException {
        searchbar.sendKeys("iq hoxton");
        Thread.sleep(3000);
        searchbarselector.click();
    }




}
