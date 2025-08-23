package uniliv.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import uniliv.AbstractMethods.AbstractComponents;

public class DetailPage extends AbstractComponents {

    WebDriver driver;
    public DetailPage(WebDriver driver){
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver,this);
        HomePage homepage = new HomePage(driver);
    }

    @FindBy(xpath = "")
    WebElement EnquireNowButton_DetailPage;

    public void DetailPage_EnquireNowButton(){
        EnquireNowButton_DetailPage.click();
    }

    @FindBy(id = "firstName")
    WebElement firstName;

    public void FirstName(){
        firstName.sendKeys("Test");
    }

  //  @FindBy(xpath = "//*[@id='__next']/main/div[1]/div[4]/div[1]/div[1]/div[2]/div/img")
   @FindBy(xpath = "//img[@title='Add to Compare']")
    WebElement  CompareButton;

    public void CompareButton_DetailPage(){
        Actions actions = new Actions(driver);
        actions.moveToElement(CompareButton).click().perform();

    }




}
