package uniliv.pageobjects;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jsoup.select.Evaluator;
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
        searchbar.sendKeys("London");
        Thread.sleep(3000);
        searchbarselector.click();
    }




                  @FindBy(xpath = "//span[normalize-space()='Previously compared']")
                  WebElement PreviouslyComparedProperty;
                  public void PreviouslyComparedProperty_HomePage()  {
                      PreviouslyComparedProperty.click();
                  }

                  @FindBy(xpath = "//div[normalize-space()='Enquire Now']")
                  WebElement EnquireNowButton;
                  public void EnquireNowButton_HomePage(){
                      EnquireNowButton.click();
                  }

                 @FindBy(id = "fullName")
                 WebElement FullName;
                  public void FullName_Enquire(){
                      FullName.sendKeys("Pravin kumar garg");
                  }
                 @FindBy(id = "email")
                 WebElement Email;
                  public void Email_Enquire(){
                      Email.sendKeys("pravin.garg@universityliving.com");
                  }
                 @FindBy(id = "contactNumber")
                 WebElement ContactNumber;
                  public void ContactNumber_Enquire(){
                      ContactNumber.sendKeys("8871555179");
                  }
                 @FindBy(name = "platformToReach")
                 WebElement PlatformToReach;
                  public void PlatformToReach_Enquire(){
                      PlatformToReach.click();
                  }
                 @FindBy(xpath = "//select[@name='platformToReach']//option[@value='Email']")
                 WebElement SelectOptionFromPlatformToReach;
                  public void SelectOptionFromPlatformToReach_Enquire(){
                      SelectOptionFromPlatformToReach.click();
                  }
                 @FindBy(xpath = "//input[@placeholder='Select University']")
                 WebElement SelectUniversity;
                  public void SelectUniversity_Enquire(){
                      SelectUniversity.click();
                  }
                 @FindBy(id = "university-search-item-0")
                 WebElement SelectUniversityFromUniversityDropdown;
                  public void SelectUniversityFromUniversityDropdown_Enquire(){
                      SelectUniversityFromUniversityDropdown.click();
                  }

                @FindBy(xpath = "//div[normalize-space()='Enquire now']")
                 WebElement SubmitButton;
                  public void SubmitButton_Enquire(){
                      SubmitButton.click();
                  }








}
