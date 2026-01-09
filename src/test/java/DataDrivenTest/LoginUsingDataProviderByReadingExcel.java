package DataDrivenTest;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

public class LoginUsingDataProviderByReadingExcel {

    // WebDriver instance used for test execution
    WebDriver driver;

    // Runs before each test
    // Launches browser and opens login page
    @BeforeMethod
    public void openPage() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Implicit wait to handle dynamic elements
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Navigate to OrangeHRM login page
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }

    // Utility method to read and print Excel data (for debugging/verification)
    // This method is NOT used in the test execution
    public void readExcel() throws IOException {

        // Load Excel file
        FileInputStream fileInputStream =
                new FileInputStream(System.getProperty("user.dir") + "\\testData\\Credentials.xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workbook.getSheet("Sheet1");

        // Get total number of rows and columns
        int rowCount = sheet.getLastRowNum();     // Excludes header row
        int columnCount = sheet.getRow(0).getLastCellNum();

        System.out.println("Total row count: " + rowCount);
        System.out.println("Total column count: " + columnCount);

        // Loop through rows and columns to print Excel data
        for (int r = 0; r <= rowCount; r++) {
            XSSFRow currentRow = sheet.getRow(r);

            for (int c = 0; c < columnCount; c++) {
                XSSFCell currentCell = currentRow.getCell(c);
                System.out.print(currentCell.toString() + "    ");
            }
            System.out.println();
        }

        workbook.close();
        fileInputStream.close();
    }

    // DataProvider method that reads login data from Excel
    // Supplies username, password, and expected validation to the test
    @DataProvider(name = "loginData")
    public String[][] getExcelData() throws IOException {

        // Load Excel file
        FileInputStream fileInputStream =
                new FileInputStream(System.getProperty("user.dir") + "\\testData\\Credentials.xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workbook.getSheet("Sheet1");

        // Get row and column counts
        int rowCount = sheet.getLastRowNum();       // Data rows (excluding header)
        int columnCount = sheet.getRow(0).getLastCellNum();

        // Create 2D array to store test data
        String[][] testData = new String[rowCount][columnCount];

        // Read data from Excel starting from row 1 (skip header)
        for (int r = 1; r <= rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                testData[r - 1][c] = sheet.getRow(r).getCell(c).toString();
            }
        }

        workbook.close();
        fileInputStream.close();

        return testData;
    }

    // Test method that runs once per row in Excel
    @Test(dataProvider = "loginData")
    public void loginTestScenario(String uName, String pass, String expValidation) {

        // Enter username
        WebElement username = driver.findElement(By.xpath("//input[@placeholder='Username']"));
        username.sendKeys(uName);

        // Enter password
        WebElement password = driver.findElement(By.xpath("//input[@placeholder='password']"));
        password.sendKeys(pass);

        // Click login button
        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();

        // Verify navigation to dashboard
        boolean dashboardURL = driver.getCurrentUrl().contains("dashboard");

        // Validate login result based on expected value from Excel
        if (expValidation.equals("valid")) {
            Assert.assertTrue(dashboardURL,
                    "Login was successful but user did not navigate to dashboard");
        } else {
            Assert.assertFalse(dashboardURL,
                    "Login should have failed but user navigated to dashboard");
        }
    }

    // Runs after each test
    // Closes browser
    @AfterMethod
    public void closeBrowser() {
        driver.quit();
    }
}
