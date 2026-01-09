package DataDrivenTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginUsingParameter {

    // WebDriver reference used across test methods
    WebDriver driver;

    // Runs before each test method
    // Launches browser and opens the login page
    @BeforeMethod
    public void openPage() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Implicit wait to handle dynamic elements
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Navigate to OrangeHRM login page
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }

    // Test method that receives values from testng.xml
    @Test
    @Parameters({"username", "password", "validation"})
    public void loginTestScenario(String uName, String pass, String expValidation) {

        // Locate username field and enter value from parameter
        WebElement username = driver.findElement(By.xpath("//input[@placeholder='username']"));
        username.sendKeys(uName);

        // Locate password field and enter value from parameter
        WebElement password = driver.findElement(By.xpath("//input[@placeholder='password']"));
        password.sendKeys(pass);

        // Click on login button
        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();

        // Check whether user is navigated to dashboard
        boolean dashboardURL = driver.getCurrentUrl().contains("dashboard");

        // Validation based on expected result passed from XML
        if (expValidation.equals("valid")) {
            Assert.assertTrue(dashboardURL,
                    "Login was successful but user did not navigate to dashboard");
        } else {
            Assert.assertFalse(dashboardURL,
                    "Login should have failed but user navigated to dashboard");
        }
    }

    // Runs after each test method
    // Closes the browser
    @AfterMethod
    public void closeBrowser() {
        driver.quit();
    }
}
