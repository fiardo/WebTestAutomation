import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import uniliv.AbstractMethods.LogUtility;
import uniliv.base.BaseTest;
import uniliv.pageobjects.DetailPage;
import uniliv.pageobjects.HomePage;
import uniliv.pageobjects.ListingPage;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.io.IOException;



public class EnquireForm extends BaseTest {


    @Test
    public void enquireform() throws InterruptedException {

        HomePage homepage = new HomePage(driver);
        ListingPage listingPage = new ListingPage(driver);
        DetailPage detailPage = new DetailPage(driver);

        LogUtility.step("Navigate to Home page");
        homepage.searchAndSelectFirstOption();
        LogUtility.step(" search and select the property/city/university");
        Thread.sleep(Long.parseLong("3000"));
        //listingPage.SelectPropertyCard_ListingPage();
        //LogUtility.step("Navigate to property detail page");
        Thread.sleep(Long.parseLong("3000"));
        //detailPage.CompareButton_DetailPage();
        LogUtility.step("Property Added to Compare list");
        //driver.navigate().back();
        //LogUtility.step("Navigated back to Listing page");
        driver.navigate().back();
        LogUtility.step("Navigated back to Homepage page");
        Thread.sleep(Long.parseLong("3000"));
        homepage.PreviouslyComparedProperty_HomePage();
        LogUtility.step("Select Previously Compared Tab");
        homepage.EnquireNowButton_HomePage();
        LogUtility.step("Clicked On Enquire Button of Previously Compared Property");
        Thread.sleep(Long.parseLong("3000"));
        homepage.FullName_Enquire();
        LogUtility.step("Entered Full Name " );
        homepage.Email_Enquire();
        LogUtility.step("Entered Email " );
        homepage.ContactNumber_Enquire();
        LogUtility.step("Entered Contact Number" );
        homepage.PlatformToReach_Enquire();
        LogUtility.step(("Clicked on Platform to reach"));
        homepage.SelectOptionFromPlatformToReach_Enquire();
        LogUtility.step("Selected Email option from PlatformToReach");
        homepage.SelectUniversity_Enquire();
        LogUtility.step("Clicked on Select University Dropdown");
        homepage.SelectUniversityFromUniversityDropdown_Enquire();
        LogUtility.step("Selected University from university Dropdown");
        homepage.SubmitButton_Enquire();
        LogUtility.step("Enquire Now Form Submitted");
        LogUtility.info("Enquire Now form Working Fine , please check the Contact Created in Crm with filled details");








    }



//    @DataProvider
//    public Object[][] getdata(){
//        List<HashMap<String, String>> data=getjsondatatomap(System.getProperty("user.dir")+
//                "src//main//java//uniliv//data//PropertiesName.json");
//
//    }
}
