package DataDrivenTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LoginUsingDataProvider {

    // WebDriver instance for browser actions
    WebDriver driver;

    // Runs before each test execution
    // Opens browser and navigates to login page
    @BeforeMethod
    public void openPage() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Implicit wait for element synchronization
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Open OrangeHRM login page
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }

    // DataProvider supplying multiple login combinations
    // Each row represents one test case
    @DataProvider(name = "loginData")
    public Object[][] loginDataProvider() {

        String[][] data = {
                {"Admin", "admin1234", "valid"},     // Invalid password
                {"Dummy", "Dummypass", "invalid"},   // Invalid username & password
                {"Dummy", "admin1234", "invalid"},   // Invalid username
                {"Admin", "Dummypass", "invalid"}    // Invalid password
        };

        return data;
    }

    // Test method executed once for each data set
    @Test(dataProvider = "loginData")
    public void loginTestScenario(String uName, String pass, String expValidation) {

        // Enter username
        WebElement username = driver.findElement(By.xpath("//input[@placeholder='username']"));
        username.sendKeys(uName);

        // Enter password
        WebElement password = driver.findElement(By.xpath("//input[@placeholder='password']"));
        password.sendKeys(pass);

        // Click login button
        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();

        // Verify if dashboard URL is loaded
        boolean dashboardURL = driver.getCurrentUrl().contains("dashboard");

        // Validate result based on expected outcome
        if (expValidation.equals("valid")) {
            Assert.assertTrue(dashboardURL,
                    "Login was successful but user did not navigate to dashboard");
        } else {
            Assert.assertFalse(dashboardURL,
                    "Login should have failed but user navigated to dashboard");
        }
    }

    // Runs after each test method
    // Closes browser session
    @AfterMethod
    public void closeBrowser() {
        driver.quit();
    }
}
