package LoginTestCases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

import java.time.Duration;

public class BothCorrect {

    WebDriver driver;
    @BeforeMethod
    public void openPage(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }

    @Test

    @Parameters ({"username","password"})
    public void loginWithBothCorrect(String uName, String pass){


        WebElement username = driver.findElement(By.xpath("//input[@placeholder='Username']"));
        username.sendKeys(uName);

        WebElement password = driver.findElement(By.xpath("//input[@placeholder='Password']"));
        password.sendKeys(pass);

        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();

    }

    @AfterMethod
    public void closeBrowser(){
        driver.quit();
    }
}
