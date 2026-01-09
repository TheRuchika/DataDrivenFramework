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
    WebDriver driver;
    @BeforeMethod
    public void openPage(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }

    @Test

    @Parameters({"username","password","validation"})
    public void loginTestScenario (String uName, String pass, String expValidation) throws InterruptedException{


        WebElement username = driver.findElement(By.xpath("//input[@placeholder='Username']"));
        username.sendKeys(uName);

        WebElement password = driver.findElement(By.xpath("//input[@placeholder='Password']"));
        password.sendKeys(pass);

        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();

        boolean dashboardURL = driver.getCurrentUrl().contains("dashboard");

        if (expValidation.equals("valid")){
            Assert.assertTrue(dashboardURL,"Login success but not navigated to dashboard");
        }else
            Assert.assertFalse(dashboardURL,"Login success but not navigated to dashboard");
    }

    @AfterMethod
    public void closeBrowser(){
        driver.quit();
    }


}
