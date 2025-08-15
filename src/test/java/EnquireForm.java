import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import uniliv.base.BaseTest;
import uniliv.pageobjects.HomePage;

import java.util.HashMap;
import java.util.List;

public class EnquireForm extends BaseTest {


    @Test
    public void enquireform() throws InterruptedException {

        HomePage homepage = new HomePage(driver);
        homepage.searchAndSelectFirstOption();

    }

    @DataProvider
    public Object[][] getdata(){
        List<HashMap<String, String>> data=getjsondatatomap(System.getProperty("user.dir")+
                "src//main//java//uniliv//data//PropertiesName.json");

    }
}
