import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import uniliv.AbstractMethods.LogUtility;
import uniliv.base.BaseTest;
import uniliv.pageobjects.HomePage;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.io.IOException;



public class EnquireForm extends BaseTest {


    @Test
    public void enquireform() throws InterruptedException {

        //LogUtility.startTest("enquireform");
        //LogUtility.info("started");
        //throw new SkipException("test skipped");
        LogUtility.step("Navigate to Home page");
        HomePage homepage = new HomePage(driver);
        LogUtility.step("click to search bar");
        homepage.searchAndSelectFirstOption();
        LogUtility.msg("text searched and submitted");




    }

    @Test
    public void simpleTest() {
        LogUtility.msg("This is a test entry");
    }

//    @DataProvider
//    public Object[][] getdata(){
//        List<HashMap<String, String>> data=getjsondatatomap(System.getProperty("user.dir")+
//                "src//main//java//uniliv//data//PropertiesName.json");
//
//    }
}
