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

    WebDriver driver;
    @BeforeMethod
    public void openPage() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }

    public void readExcel() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(System.getProperty("user.dir")+ "\\testData\\Credentials.xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workbook.getSheet("Sheet1");
        int rowCount = sheet.getLastRowNum();
        int columnCount = sheet.getRow(0).getLastCellNum();
        System.out.println("Total row count: " +rowCount); //Row count is getting as 4 but actual value is 5
        System.out.println("Total column count: " +columnCount); //3

        for (int r=0; r<=rowCount; r++){
            XSSFRow currentRow = sheet.getRow(r);

            for (int c=0; c<columnCount; c++){
                XSSFCell currentCell = currentRow.getCell(c);
                String cellValue = currentCell.toString();
                System.out.print(cellValue+"         ");
            }
            System.out.println();
            System.out.println();
        }

    }

    @DataProvider(name="loginData")
    public String[][] getExcelData() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(System.getProperty("user.dir")+ "\\testData\\Credentials.xlsx");

        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workbook.getSheet("Sheet1");
        int rowCount = sheet.getLastRowNum();
        int columnCount = sheet.getRow(0).getLastCellNum();

        String [][] testData=new String[rowCount][columnCount];

        for (int r=1; r<=rowCount; r++){

            for(int c=0; c<columnCount; c++){
                testData[r-1][c] = sheet.getRow(r).getCell(c).toString();

            }
        }
            workbook.close();
        fileInputStream.close();

        return testData;

    }


    @Test(dataProvider = "loginData")

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
