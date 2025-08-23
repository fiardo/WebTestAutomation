package uniliv.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import uniliv.AbstractMethods.AbstractComponents;

public class ListingPage extends AbstractComponents {

    WebDriver driver;
    public ListingPage(WebDriver driver){
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    @FindBy(xpath = "(//button[normalize-space()='Enquire now'])[1]")
    WebElement EnquireNowButton_Listing;

    public void ListingPage_EnquireNowButton(){
        EnquireNowButton_Listing.click();
    }

    @FindBy(xpath = "//div[contains(@href,'/united-kingdom/london/property/wood-green-hall')]")
    WebElement SelectProperty_Listing;

        public void SelectPropertyCard_ListingPage(){
        SelectProperty_Listing.click();

        }
}
