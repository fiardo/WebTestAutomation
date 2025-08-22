package uniliv.AbstractMethods;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.*;
import org.testng.Reporter;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;


public class LogUtility {

        private static final Logger logger = Logger.getLogger("MyLog");
        private static boolean isInitialized = false;

        public static Logger getLogger() {
            if (!isInitialized) {
                try {
                    // Create logs directory if not exists
                    String logDir = System.getProperty("user.dir") + "/logs";
                    File dir = new File(logDir);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    // File Handler for logging to file
                    FileHandler fh = new FileHandler(logDir + "/website.log", true); // append = true
                    fh.setFormatter(new SimpleFormatter());
                    logger.addHandler(fh);

                    // Console Handler for logging to console (in normal color)
                    ConsoleHandler ch = new ConsoleHandler();
                    ch.setFormatter(new SimpleFormatter());
                    ch.setLevel(Level.INFO);
                    logger.addHandler(ch);

                    // Remove default console handler to avoid duplicate logs
                    logger.setUseParentHandlers(false);

                    logger.setLevel(Level.INFO);
                    isInitialized = true;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return logger;
        }

        // Custom msg() method - General purpose message logging
        public static void msg(String message) {
            getLogger().info(message);
            Reporter.log("MSG: " + message, true);
            logToExtent(Status.INFO, message);
        }

        // Info level logging
        public static void info(String message) {
            getLogger().info(message);
            Reporter.log("INFO: " + message, false);
            logToExtent(Status.INFO, message);
        }

        // Error level logging
        public static void error(String message) {
            getLogger().severe(message);
            Reporter.log("ERROR: " + message, true);
            logToExtent(Status.FAIL, message);
        }

        // Error with exception
        public static void error(String message, Exception e) {
            getLogger().severe(message + " - Exception: " + e.getMessage());
            Reporter.log("ERROR: " + message + " - Exception: " + e.getMessage(), true);
            logToExtent(Status.FAIL, message + " - Exception: " + e.getMessage());
        }

        // Warning level logging
        public static void warn(String message) {
            getLogger().warning(message);
            Reporter.log("WARN: " + message, true);
            logToExtent(Status.WARNING, message);
        }

        // Debug level logging (will only show if level is set to FINE or lower)
        public static void debug(String message) {
            getLogger().fine(message);
            Reporter.log("DEBUG: " + message, true);
            // Extent may not have DEBUG status in all versions; map to INFO
            logToExtent(Status.INFO, "DEBUG: " + message);
        }

        // Step logging for test steps
        public static void step(String stepDescription) {
            String message = "STEP: " + stepDescription;
            getLogger().info(message);
            Reporter.log(message, false);
            logToExtent(Status.INFO, message);
        }

        // Test start logging
        public static void startTest(String testName) {
            String message = "========== Starting Test: " + testName + " ==========";
            getLogger().info(message);
            Reporter.log(message, false);
            logToExtent(Status.INFO, message);
        }

        // Test end logging
        public static void endTest(String testName, String status) {

            String message = "========== Test " + testName + " " + status + " ==========";
            getLogger().info(message);
            Reporter.log(message, false);
            logToExtent(Status.INFO, message);
        }

        // Pass logging
        public static void pass(String message) {
            getLogger().info("PASS: " + message);
            Reporter.log("PASS: " + message, false);
            logToExtent(Status.PASS, message);
        }

        // Fail logging
        public static void fail(String message) {
            getLogger().severe("FAIL: " + message);
            Reporter.log("FAIL: " + message, false);
            logToExtent(Status.FAIL, message);
        }

        // Custom formatting for different message types
        public static void logAction(String action) {
            msg("ACTION: " + action);
        }

        public static void logResult(String result) {
            msg("RESULT: " + result);
        }

        public static void logData(String dataDescription, String data) {
            msg("DATA: " + dataDescription + " = " + data);
        }

        // Internal: route to current Extent test if available
        private static void logToExtent(Status status, String message) {
            try {
                ExtentTest current = TestListener.getCurrentTest();
                if (current != null) {
                    current.log(status, message);
                }
            } catch (Throwable ignored) {
                // keep logging resilient; do not break tests if extent not ready
            }
        }

        // Convenience: attach screenshot to current Extent test
        public static void attachScreenshot(String path) {
            try {
                ExtentTest current = TestListener.getCurrentTest();
                if (current != null && path != null) {
                    current.addScreenCaptureFromPath(path);
                }
            } catch (Exception ignored) {
            }
        }
    }

// Example usage in test class:
/*
import java.util.logging.Logger;

public class LoginTest extends BaseTest {

    @Test
    public void loginTest() {
        LogUtility.startTest("loginTest");

        LogUtility.step("Navigate to login page");
        LogUtility.msg("Opening browser and navigating to application");
        // driver.get("https://example.com");

        LogUtility.step("Enter username");
        LogUtility.logData("Username", "testuser@example.com");
        LogUtility.logAction("Entering username in the username field");
        // usernameField.sendKeys("testuser@example.com");

        LogUtility.step("Enter password");
        LogUtility.logAction("Entering password");
        // passwordField.sendKeys("password123");

        LogUtility.step("Click login button");
        LogUtility.logAction("Clicking on login button");
        // loginButton.click();

        LogUtility.step("Verify successful login");
        LogUtility.logResult("User successfully logged in");
        LogUtility.pass("Login test completed successfully");

        LogUtility.endTest("loginTest", "PASSED");
    }

    @Test
    public void searchTest() {
        LogUtility.startTest("searchTest");

        LogUtility.step("Navigate to search page");
        LogUtility.msg("User is on the search page");

        LogUtility.step("Enter search term");
        LogUtility.logData("Search term", "selenium automation");

        LogUtility.step("Click search button");
        LogUtility.logAction("Performing search operation");

        LogUtility.step("Verify search results");
        try {
            // Verify search results logic here
            LogUtility.logResult("Search returned 15 results");
            LogUtility.pass("Search functionality working correctly");
        } catch (Exception e) {
            LogUtility.error("Search verification failed", e);
            LogUtility.fail("Search test failed");
        }

        LogUtility.endTest("searchTest", "PASSED");
    }
}
*/


