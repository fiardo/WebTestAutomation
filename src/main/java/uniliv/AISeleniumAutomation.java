package uniliv;

/*import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class AISeleniumAutomation {

    private WebDriver driver;
    private WebDriverWait wait;
    private AILocatorHelper aiHelper;
    private static final String CONFIG_FILE = "element_config.properties";
    private Properties elementConfig;

    public AISeleniumAutomation() {
        this.aiHelper = new AILocatorHelper();
        this.elementConfig = loadElementConfig();
    }

    public static void main(String[] args) {
        AISeleniumAutomation automation = new AISeleniumAutomation();
        automation.runAutomation();
    }

    public void runAutomation() {
        setupDriver();

        try {
            driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

            // Try to click "Forgot Password" with self-healing
            boolean success = clickElementWithSelfHealing("forgot_password", "Forgot your password?");

            if (success) {
                System.out.println("✅ Successfully clicked Forgot Password element");
            } else {
                System.out.println("❌ Failed to click Forgot Password element even with AI assistance");
            }

            Thread.sleep(3000); // Wait to see result

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    private void setupDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean clickElementWithSelfHealing(String elementKey, String expectedText) {
        // Step 1: Try saved locators
        List<String> savedLocators = getSavedLocators(elementKey);

        for (String locator : savedLocators) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
                element.click();
                System.out.println("✅ Found element using saved locator: " + locator);
                return true;
            } catch (Exception e) {
                System.out.println("⚠️ Saved locator failed: " + locator);
            }
        }

        // Step 2: Use AI to find new locator
        System.out.println("🤖 Using AI to find new locator...");
        String newLocator = aiHelper.findElementLocator(driver.getPageSource(), expectedText);

        if (newLocator != null && !newLocator.isEmpty()) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(newLocator)));
                element.click();

                // Save the new working locator
                saveNewLocator(elementKey, newLocator);
                System.out.println("✅ Found element using AI-generated locator: " + newLocator);
                return true;
            } catch (Exception e) {
                System.out.println("❌ AI-generated locator failed: " + newLocator);
            }
        }

        // Step 3: Fallback strategies
        return tryFallbackStrategies(expectedText);
    }

    private List<String> getSavedLocators(String elementKey) {
        List<String> locators = new ArrayList<>();

        // Add default known locators
        locators.add("//p[@class='oxd-text oxd--p orangehrm-login-forgot-header']");
        locators.add("//p[contains(text(), 'Forgot  password?')]");
        locators.add("//p[contains(@class, 'orangehrm-forgot-header')]");

        // Add any saved locators from config
        String savedLocators = elementConfig.getProperty(elementKey, "");
        if (!savedLocators.isEmpty()) {
            String[] locatorArray = savedLocators.split(";");
            locators.addAll(Arrays.asList(locatorArray));
        }

        return locators;
    }

    private void saveNewLocator(String elementKey, String locator) {
        try {
            String existingLocators = elementConfig.getProperty(elementKey, "");
            String newLocators = existingLocators.isEmpty() ? locator : existingLocators + ";" + locator;

            elementConfig.setProperty(elementKey, newLocators);

            // Save to file
            try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
                elementConfig.store(fos, "Updated locators - " + new Date());
            }

            System.out.println("💾 Saved new locator for " + elementKey + ": " + locator);
        } catch (IOException e) {
            System.err.println("Failed to save locator: " + e.getMessage());
        }
    }

    private Properties loadElementConfig() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            props.load(fis);
        } catch (FileNotFoundException e) {
            System.out.println("Config file not found, creating new one");
        } catch (IOException e) {
            System.err.println("Error loading config: " + e.getMessage());
        }
        return props;
    }

    private boolean tryFallbackStrategies(String expectedText) {
        System.out.println("🔄 Trying fallback strategies...");

        // Strategy 1: Text-based search
        try {
            List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(), '" + expectedText + "')]"));
            if (!elements.isEmpty()) {
                elements.get(0).click();
                System.out.println("✅ Found using text search");
                return true;
            }
        } catch (Exception e) {
            // Continue to next strategy
        }

        // Strategy 2: Partial text match
        try {
            String[] words = expectedText.split(" ");
            for (String word : words) {
                if (word.length() > 3) {
                    List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(), '" + word + "')]"));
                    if (!elements.isEmpty()) {
                        elements.get(0).click();
                        System.out.println("✅ Found using partial text: " + word);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // Continue
        }

        return false;
    }
}

// AI Helper class for generating locators
class AILocatorHelper {
    private static final String OPENAI_API_KEY = "Openai api key paste here ";
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    public String findElementLocator(String pageSource, String targetText) {
        try {
            // Parse HTML and find potential elements
            Document doc = Jsoup.parse(pageSource);
            Elements potentialElements = findPotentialElements(doc, targetText);

            if (potentialElements.isEmpty()) {
                return null;
            }

            // Use AI to generate best XPath
            return generateXPathWithAI(potentialElements, targetText);

        } catch (Exception e) {
            System.err.println("AI locator generation failed: " + e.getMessage());
            return null;
        }
    }

    private Elements findPotentialElements(Document doc, String targetText) {
        Elements elements = new Elements();

        // Find elements containing the target text
        elements.addAll(doc.getElementsContainingText(targetText));

        // Find elements with similar text
        String[] words = targetText.toLowerCase().split("\\s+");
        for (String word : words) {
            if (word.length() > 3) {
                elements.addAll(doc.getElementsContainingText(word));
            }
        }

        return elements;
    }

    private String generateXPathWithAI(Elements elements, String targetText) {
        try {
            StringBuilder prompt = new StringBuilder();
            prompt.append("Generate the best XPath selector for an element with text '").append(targetText).append("'.\n");
            prompt.append("Here are the potential HTML elements:\n\n");

            int count = 0;
            for (Element element : elements) {
                if (count++ > 5) break; // Limit to first 5 elements

                prompt.append("Element ").append(count).append(":\n");
                prompt.append("Tag: ").append(element.tagName()).append("\n");
                prompt.append("Classes: ").append(element.className()).append("\n");
                prompt.append("ID: ").append(element.id()).append("\n");
                prompt.append("Text: ").append(element.text()).append("\n");
                prompt.append("HTML: ").append(element.outerHtml()).append("\n\n");
            }

            prompt.append("Return only the XPath selector string, nothing else. Prefer specific class-based selectors over text-based ones when possible.");

            return callOpenAI(prompt.toString());

        } catch (Exception e) {
            System.err.println("AI XPath generation failed: " + e.getMessage());

            // Fallback: generate simple XPath
            for (Element element : elements) {
                if (element.text().toLowerCase().contains(targetText.toLowerCase())) {
                    if (!element.className().isEmpty()) {
                        return "//" + element.tagName() + "[@class='" + element.className() + "']";
                    } else if (!element.id().isEmpty()) {
                        return "//" + element.tagName() + "[@id='" + element.id() + "']";
                    } else {
                        return "//" + element.tagName() + "[contains(text(), '" + targetText + "')]";
                    }
                }
            }

            return null;
        }
    }

    private String callOpenAI(String prompt) {
        if (OPENAI_API_KEY == null || OPENAI_API_KEY.isEmpty() || OPENAI_API_KEY.equals("your-openai-api-key-here")) {
            System.out.println("⚠️ OpenAI API key not set, using fallback locator generation");
            return null;
        }

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(OPENAI_API_URL);
            post.setHeader("Authorization", "Bearer " + OPENAI_API_KEY);
            post.setHeader("Content-Type", "application/json");

            // Escape the prompt properly for JSON
            String escapedPrompt = prompt.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");

            String requestBody = String.format(
                    "{\n" +
                            "    \"model\": \"gpt-3.5-turbo\",\n" +
                            "    \"messages\": [\n" +
                            "        {\n" +
                            "            \"role\": \"user\",\n" +
                            "            \"content\": \"%s\"\n" +
                            "        }\n" +
                            "    ],\n" +
                            "    \"max_tokens\": 150,\n" +
                            "    \"temperature\": 0.3\n" +
                            "}", escapedPrompt);

            post.setEntity(new StringEntity(requestBody));

            org.apache.http.HttpResponse httpResponse = client.execute(post);
            String response = EntityUtils.toString(httpResponse.getEntity());

            System.out.println("🔍 OpenAI Response: " + response);

            // Parse JSON response with proper error handling
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            // Check for API errors first
            if (root.has("error")) {
                JsonNode error = root.get("error");
                String errorMessage = error.has("message") ? error.get("message").asText() : "Unknown error";
                System.err.println("❌ OpenAI API Error: " + errorMessage);
                return null;
            }

            // Check if choices array exists and has elements
            if (!root.has("choices") || root.get("choices").isEmpty()) {
                System.err.println("❌ No choices in OpenAI response");
                return null;
            }

            JsonNode choices = root.get("choices");
            JsonNode firstChoice = choices.get(0);

            if (firstChoice == null) {
                System.err.println("❌ First choice is null in OpenAI response");
                return null;
            }

            if (!firstChoice.has("message")) {
                System.err.println("❌ No message in first choice");
                return null;
            }

            JsonNode message = firstChoice.get("message");
            if (!message.has("content")) {
                System.err.println("❌ No content in message");
                return null;
            }

            String xpath = message.get("content").asText().trim();

            // Clean up the response
            xpath = xpath.replaceAll("```.*?\n", "").replaceAll("```", "").trim();

            System.out.println("🤖 AI Generated XPath: " + xpath);
            return xpath;

        } catch (Exception e) {
            System.err.println("OpenAI API call failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

// pom.xml dependencies needed:
/*
<dependencies>
    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>4.27.0</version>
    </dependency>
    <dependency>
        <groupId>io.github.bonigarcia</groupId>
        <artifactId>webdrivermanager</artifactId>
        <version>5.6.2</version>
    </dependency>
    <dependency>
        <groupId>org.apache.httpcomponents</groupId>
        <artifactId>httpclient</artifactId>
        <version>4.5.14</version>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.15.2</version>
    </dependency>
    <dependency>
        <groupId>org.jsoup</groupId>
        <artifactId>jsoup</artifactId>
        <version>1.17.2</version>
    </dependency>
</dependencies>
*/



import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class AISeleniumAutomation {

    private WebDriver driver;
    private WebDriverWait wait;
    private AILocatorHelper aiHelper;
    private static final String CONFIG_FILE = "element_config.properties";
    private Properties elementConfig;

    public AISeleniumAutomation() {
        this.aiHelper = new AILocatorHelper();
        this.elementConfig = loadElementConfig();
    }

    public static void main(String[] args) {
        AISeleniumAutomation automation = new AISeleniumAutomation();
        automation.runAutomation();
    }

    public void runAutomation() {
        setupDriver();

        try {
            driver.get("https://www.universityliving.com/");

            // Try to click "Forgot Password" with self-healing
            boolean success = clickElementWithSelfHealing("Login", "Login");

            if (success) {
                System.out.println("✅ Successfully clicked the element");
            } else {
                System.out.println("❌ Failed to click the element even with AI assistance");
            }

            Thread.sleep(3000); // Wait to see result

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    private void setupDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean clickElementWithSelfHealing(String elementKey, String expectedText) {
        // Step 1: Try saved locators
        List<String> savedLocators = getSavedLocators(elementKey);

        for (String locator : savedLocators) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
                element.click();
                System.out.println("✅ Found element using saved locator: " + locator);
                return true;
            } catch (Exception e) {
                System.out.println("⚠️ Saved locator failed: " + locator);
            }
        }

        // Step 2: Use AI to find new locator
        System.out.println("🤖 Using Gemini AI to find new locator...");
        String newLocator = aiHelper.findElementLocator(driver.getPageSource(), expectedText);

        if (newLocator != null && !newLocator.isEmpty()) {
            try {
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(newLocator)));
                element.click();

                // Save the new working locator
                saveNewLocator(elementKey, newLocator);
                System.out.println("✅ Found element using Gemini AI-generated locator: " + newLocator);
                return true;
            } catch (Exception e) {
                System.out.println("❌ Gemini AI-generated locator failed: " + newLocator);
            }
        }

        // Step 3: Fallback strategies
        return tryFallbackStrategies(expectedText);
    }

    private List<String> getSavedLocators(String elementKey) {
        List<String> locators = new ArrayList<>();

        // Add default known locators
        locators.add("//p[@class='oxd-text oxd-text--p orangehrm-login-forgot-header']");
        locators.add("//p[contains(text(), 'Forgot your password?')]");
        locators.add("//p[contains(@class, 'orangehrm-login-forgot-header')]");

        // Add any saved locators from config
        String savedLocators = elementConfig.getProperty(elementKey, "");
        if (!savedLocators.isEmpty()) {
            String[] locatorArray = savedLocators.split(";");
            locators.addAll(Arrays.asList(locatorArray));
        }

        return locators;
    }

    private void saveNewLocator(String elementKey, String locator) {
        try {
            String existingLocators = elementConfig.getProperty(elementKey, "");
            String newLocators = existingLocators.isEmpty() ? locator : existingLocators + ";" + locator;

            elementConfig.setProperty(elementKey, newLocators);

            // Save to file
            try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
                elementConfig.store(fos, "Updated locators - " + new Date());
            }

            System.out.println("💾 Saved new locator for " + elementKey + ": " + locator);
        } catch (IOException e) {
            System.err.println("Failed to save locator: " + e.getMessage());
        }
    }

    private Properties loadElementConfig() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            props.load(fis);
        } catch (FileNotFoundException e) {
            System.out.println("Config file not found, creating new one");
        } catch (IOException e) {
            System.err.println("Error loading config: " + e.getMessage());
        }
        return props;
    }

    private boolean tryFallbackStrategies(String expectedText) {
        System.out.println("🔄 Trying fallback strategies...");

        // Strategy 1: Text-based search
        try {
            List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(), '" + expectedText + "')]"));
            if (!elements.isEmpty()) {
                elements.get(0).click();
                System.out.println("✅ Found using text search");
                return true;
            }
        } catch (Exception e) {
            // Continue to next strategy
        }

        // Strategy 2: Partial text match
        try {
            String[] words = expectedText.split(" ");
            for (String word : words) {
                if (word.length() > 3) {
                    List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(), '" + word + "')]"));
                    if (!elements.isEmpty()) {
                        elements.get(0).click();
                        System.out.println("✅ Found using partial text: " + word);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // Continue
        }

        return false;
    }
}

// AI Helper class for generating locators using Google Gemini
class AILocatorHelper {
    // Replace with your actual Google Gemini API key
    private static final String GEMINI_API_KEY = "AIzaSyC2XL2_f0qk3vAyfFgjNFOKDDtK6BV4sqk";
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

    public String findElementLocator(String pageSource, String targetText) {
        try {
            // Parse HTML and find potential elements
            Document doc = Jsoup.parse(pageSource);
            Elements potentialElements = findPotentialElements(doc, targetText);

            if (potentialElements.isEmpty()) {
                return null;
            }

            // Use Gemini AI to generate best XPath
            return generateXPathWithGemini(potentialElements, targetText);

        } catch (Exception e) {
            System.err.println("Gemini AI locator generation failed: " + e.getMessage());
            return null;
        }
    }

    private Elements findPotentialElements(Document doc, String targetText) {
        Elements elements = new Elements();

        // Find elements containing the target text
        elements.addAll(doc.getElementsContainingText(targetText));

        // Find elements with similar text
        String[] words = targetText.toLowerCase().split("\\s+");
        for (String word : words) {
            if (word.length() > 3) {
                elements.addAll(doc.getElementsContainingText(word));
            }
        }

        return elements;
    }

    private String generateXPathWithGemini(Elements elements, String targetText) {
        try {
            StringBuilder prompt = new StringBuilder();
            prompt.append("Generate the best XPath selector for a web element with text '").append(targetText).append("'.\n\n");
            prompt.append("Context: I need to click on this element using Selenium WebDriver.\n");
            prompt.append("Here are the potential HTML elements found on the page:\n\n");

            int count = 0;
            for (Element element : elements) {
                if (count++ > 5) break; // Limit to first 5 elements

                prompt.append("Element ").append(count).append(":\n");
                prompt.append("- Tag: ").append(element.tagName()).append("\n");
                prompt.append("- Classes: ").append(element.className()).append("\n");
                prompt.append("- ID: ").append(element.id()).append("\n");
                prompt.append("- Text: ").append(element.text()).append("\n");
                prompt.append("- Parent: ").append(element.parent() != null ? element.parent().tagName() : "none").append("\n");
                if (element.hasAttr("data-v-")) {
                    prompt.append("- Has data-v attributes: yes\n");
                }
                prompt.append("- HTML: ").append(element.outerHtml().length() > 200 ?
                        element.outerHtml().substring(0, 200) + "..." : element.outerHtml()).append("\n\n");
            }

            prompt.append("Requirements:\n");
            prompt.append("1. Return ONLY the XPath selector string, nothing else\n");
            prompt.append("2. Prefer stable selectors (class-based or ID-based over text-based when possible)\n");
            prompt.append("3. Avoid using dynamic classes that might change\n");
            prompt.append("4. Make it as specific as needed to uniquely identify the element\n");
            prompt.append("5. The XPath should work reliably across page loads\n\n");
            prompt.append("XPath:");

            return callGeminiAPI(prompt.toString());

        } catch (Exception e) {
            System.err.println("Gemini XPath generation failed: " + e.getMessage());

            // Fallback: generate simple XPath
            for (Element element : elements) {
                if (element.text().toLowerCase().contains(targetText.toLowerCase())) {
                    if (!element.className().isEmpty()) {
                        return "//" + element.tagName() + "[@class='" + element.className() + "']";
                    } else if (!element.id().isEmpty()) {
                        return "//" + element.tagName() + "[@id='" + element.id() + "']";
                    } else {
                        return "//" + element.tagName() + "[contains(text(), '" + targetText + "')]";
                    }
                }
            }

            return null;
        }
    }

    private String callGeminiAPI(String prompt) {
        if (GEMINI_API_KEY == null || GEMINI_API_KEY.isEmpty() || GEMINI_API_KEY.equals("your-gemini-api-key-here")) {
            System.out.println("⚠️ Gemini API key not set, using fallback locator generation");
            return null;
        }

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String url = GEMINI_API_URL + "?key=" + GEMINI_API_KEY;
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/json");

            // Escape the prompt properly for JSON
            String escapedPrompt = prompt.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");

            // Gemini API request format
            String requestBody = String.format(
                    "{\n" +
                            "  \"contents\": [{\n" +
                            "    \"parts\":[{\n" +
                            "      \"text\": \"%s\"\n" +
                            "    }]\n" +
                            "  }],\n" +
                            "  \"generationConfig\": {\n" +
                            "    \"temperature\": 0.1,\n" +
                            "    \"topK\": 1,\n" +
                            "    \"topP\": 0.8,\n" +
                            "    \"maxOutputTokens\": 100,\n" +
                            "    \"stopSequences\": []\n" +
                            "  },\n" +
                            "  \"safetySettings\": [\n" +
                            "    {\n" +
                            "      \"category\": \"HARM_CATEGORY_HARASSMENT\",\n" +
                            "      \"threshold\": \"BLOCK_MEDIUM_AND_ABOVE\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"category\": \"HARM_CATEGORY_HATE_SPEECH\",\n" +
                            "      \"threshold\": \"BLOCK_MEDIUM_AND_ABOVE\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"category\": \"HARM_CATEGORY_SEXUALLY_EXPLICIT\",\n" +
                            "      \"threshold\": \"BLOCK_MEDIUM_AND_ABOVE\"\n" +
                            "    },\n" +
                            "    {\n" +
                            "      \"category\": \"HARM_CATEGORY_DANGEROUS_CONTENT\",\n" +
                            "      \"threshold\": \"BLOCK_MEDIUM_AND_ABOVE\"\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}", escapedPrompt);

            post.setEntity(new StringEntity(requestBody));

            org.apache.http.HttpResponse httpResponse = client.execute(post);
            String response = EntityUtils.toString(httpResponse.getEntity());

            System.out.println("🔍 Gemini API Response: " + response);

            // Parse JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            // Check for API errors
            if (root.has("error")) {
                JsonNode error = root.get("error");
                String errorMessage = error.has("message") ? error.get("message").asText() : "Unknown error";
                System.err.println("❌ Gemini API Error: " + errorMessage);
                return null;
            }

            // Parse Gemini response structure
            if (!root.has("candidates") || root.get("candidates").isEmpty()) {
                System.err.println("❌ No candidates in Gemini response");
                return null;
            }

            JsonNode candidates = root.get("candidates");
            JsonNode firstCandidate = candidates.get(0);

            if (firstCandidate == null || !firstCandidate.has("content")) {
                System.err.println("❌ No content in first candidate");
                return null;
            }

            JsonNode content = firstCandidate.get("content");
            if (!content.has("parts") || content.get("parts").isEmpty()) {
                System.err.println("❌ No parts in content");
                return null;
            }

            JsonNode parts = content.get("parts");
            JsonNode firstPart = parts.get(0);

            if (!firstPart.has("text")) {
                System.err.println("❌ No text in first part");
                return null;
            }

            String xpath = firstPart.get("text").asText().trim();

            // Clean up the response - remove any markdown or extra formatting
            xpath = xpath.replaceAll("```.*?\n", "").replaceAll("```", "");
            xpath = xpath.replaceAll("XPath:\\s*", "");
            xpath = xpath.replaceAll("^[^/]*//", "//"); // Remove any text before the actual XPath
            xpath = xpath.trim();

            // Validate that we have a proper XPath
            if (!xpath.startsWith("//") && !xpath.startsWith("/")) {
                System.err.println("❌ Invalid XPath format received: " + xpath);
                return null;
            }

            System.out.println("🤖 Gemini Generated XPath: " + xpath);
            return xpath;

        } catch (Exception e) {
            System.err.println("Gemini API call failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

