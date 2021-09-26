package com.pragya.jain.pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PlainITTest{
    WebDriver driver;
    public static String Url ;
    public Properties testprop;

    @FindBy(how = How.XPATH, using = "//a[@href='#/contact']/i")
    public static WebElement lnkContact;

    @BeforeSuite
    public void initProp() {

        testprop = new Properties();

        try {
            FileInputStream fs = new FileInputStream(
                    System.getProperty("user.dir")
                            + "//src//test//resources//properties//project.properties");

            testprop.load(fs); // loading the Properties
            Url =testprop.getProperty("plainITurl");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @BeforeMethod
    public void setUpBrowser(){
        System.setProperty("webdriver.chrome.driver", "D:\\Sources\\src\\test\\resources\\drivers\\chromedriver.exe");
        driver=new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(Url);
    }

    @Test
    public void createContact() {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='#/contact']/i")));
        driver.findElement(By.xpath("//a[@href='#/contact']/i")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='forename']")));
        driver.findElement(By.xpath("//input[@name='forename']")).sendKeys("Rahul");
        driver.findElement(By.xpath("//input[@name='surname']")).sendKeys("Sharma");
        driver.findElement(By.xpath("//input[@name='email']")).sendKeys("rahul.sharma@gmail.com");
        driver.findElement(By.xpath("//input[@name='telephone']")).sendKeys("1234567890");
        driver.findElement(By.xpath("//textarea[@name='message']")).sendKeys("rahul.sharma@gmail.com");
        driver.findElement(By.xpath("//*[contains(text(), 'Submit')]")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@ui-if='contactValidSubmit']/div")));
        String successMsg = driver.findElement(By.xpath("//*[@ui-if='contactValidSubmit']/div")).getText();
        System.out.println("Success message is : " + successMsg);

        try {
            Assert.assertEquals(successMsg, "Thanks Rahul, we appreciate your feedback.");
        } catch (AssertionError e) {
            System.out.println("Success message displayed on screen : "+successMsg +", not matches with expected message");
            throw e;
        }
        System.out.println("Success message displayed on screen : "+successMsg +", matches with expected message");

    }

    @Test
    public void shop() {
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='#/shop']/i")));
        driver.findElement(By.xpath("//a[@href='#/shop']/i")).click();

        int totalFunnyCow= 2;
        for (int i=0; i<totalFunnyCow; i++) {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'Funny Cow')]/parent::div/p/a[@ng-click='add(item)']")));
            driver.findElement(By.xpath("//*[contains(text(),'Funny Cow')]/parent::div/p/a[@ng-click='add(item)']")).click();
        }

        int totalFluffyBunny= 1;
        for (int i=0; i<totalFluffyBunny; i++) {
            driver.findElement(By.xpath("//*[contains(text(), 'Fluffy Bunny')]/parent::div/p/a[@ng-click='add(item)']")).click();
        }
        driver.findElement(By.xpath("//a[@href='#/cart']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Funny Cow')]/following-sibling::td[2]/input")));
        String FunnyCowInCart = driver.findElement(By.xpath("//*[contains(text(), 'Funny Cow')]/following-sibling::td[2]/input")).getAttribute("value");
        String FluffyBunnyInCart = driver.findElement(By.xpath("//*[contains(text(), 'Fluffy Bunny')]/following-sibling::td[2]/input")).getAttribute("value");

        try {
            Assert.assertEquals(totalFunnyCow, Integer.parseInt(FunnyCowInCart));
            Assert.assertEquals(totalFluffyBunny, Integer.parseInt(FluffyBunnyInCart));
        } catch (AssertionError e) {
            System.out.println("Total Funny Cow in Cart : "+FluffyBunnyInCart +", not matches with added Funny Cow");
            System.out.println("Total Fluffy Bunny in Cart : "+FunnyCowInCart +", not matches with added Fluffy Bunny");
            throw e;
        }
        System.out.println("Total Funny Cow in Cart : "+FluffyBunnyInCart +", matches with added Funny Cow");
        System.out.println("Total Fluffy Bunny in Cart : "+FunnyCowInCart +", matches with added Fluffy Bunny");
    }

    @Test
    public void subTotalOfEachProduct() {

        double totalPriceStuffedFrog = 0.0;
        double totalPriceFluffyBunny = 0.0;
        double totalPriceValentineBear = 0.0;

        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='#/shop']/i")));
        driver.findElement(By.xpath("//a[@href='#/shop']/i")).click();

        int totalStuffedFrog= 2;
        for (int i=0; i<totalStuffedFrog; i++) {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(),'Stuffed Frog')]/parent::div/p/a[@ng-click='add(item)']")));
            driver.findElement(By.xpath("//*[contains(text(),'Stuffed Frog')]/parent::div/p/a[@ng-click='add(item)']")).click();
            String[] priceStuffedFrog = driver.findElement(By.xpath("//*[contains(text(), 'Stuffed Frog')]/parent::div/p/span")).getText().split("\\$");
            double priceStuffedFrogPart2 = Double.parseDouble(priceStuffedFrog[1]);
            totalPriceStuffedFrog = totalPriceStuffedFrog + priceStuffedFrogPart2;
        }

        int totalFluffyBunny= 5;
        for (int i=0; i<totalFluffyBunny; i++) {
            driver.findElement(By.xpath("//*[contains(text(), 'Fluffy Bunny')]/parent::div/p/a[@ng-click='add(item)']")).click();
            String[] priceFluffyBunny = driver.findElement(By.xpath("//*[contains(text(), 'Fluffy Bunny')]/parent::div/p/span")).getText().split("\\$");
            double priceFluffyBunnyPart2 = Double.parseDouble(priceFluffyBunny[1]);
            totalPriceFluffyBunny = totalPriceFluffyBunny + priceFluffyBunnyPart2;
        }

        int totalValentineBear= 3;
        for (int i=0; i<totalValentineBear; i++) {
            driver.findElement(By.xpath("//*[contains(text(), 'Valentine Bear')]/parent::div/p/a[@ng-click='add(item)']")).click();
            String[] priceValentineBear = driver.findElement(By.xpath("//*[contains(text(), 'Valentine Bear')]/parent::div/p/span")).getText().split("\\$");
            double priceValentineBearPart2 = Double.parseDouble(priceValentineBear[1]);
            totalPriceValentineBear = totalPriceValentineBear + priceValentineBearPart2;
        }

        String totalCalPriceStuffedFrog= "$"+ (totalPriceStuffedFrog);
        String totalCalPriceFluffyBunny = "$"+ (totalPriceFluffyBunny);
        String totalCalPriceValentineBear = "$"+ (totalPriceValentineBear);

        System.out.println("Total calculated price of Stuffed Frog : "+totalCalPriceStuffedFrog );
        System.out.println("Total calculated price of Fluffy Bunny : "+totalCalPriceFluffyBunny);
        System.out.println("Total calculated price of Valentine Bear : "+totalCalPriceValentineBear);

        driver.findElement(By.xpath("//a[@href='#/cart']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), 'Stuffed Frog')]/following-sibling::td[3]")));
        String totalPriceOfStuffedFrogInCart = driver.findElement(By.xpath("//*[contains(text(), 'Stuffed Frog')]/following-sibling::td[3]")).getText();
        String totalPriceOfFluffyBunnyInCart = driver.findElement(By.xpath("//*[contains(text(), 'Fluffy Bunny')]/following-sibling::td[3]")).getText();
        String totalPriceOfValentineBearInCart = driver.findElement(By.xpath("//*[contains(text(), 'Valentine Bear')]/following-sibling::td[3]")).getText();

       try {
            Assert.assertEquals(totalCalPriceStuffedFrog, totalPriceOfStuffedFrogInCart);
            Assert.assertEquals(totalCalPriceFluffyBunny, totalPriceOfFluffyBunnyInCart);
           Assert.assertEquals(totalCalPriceValentineBear, totalPriceOfValentineBearInCart);

       } catch (AssertionError e) {
           System.out.println("Total price of Stuffed Frog in Cart : "+totalCalPriceStuffedFrog +", not matches with expected total price");
           System.out.println("Total price of Fluffy Bunny in Cart : "+totalCalPriceFluffyBunny +", not matches with expected total price");
           System.out.println("Total price of Valentine Bear in Cart : "+totalCalPriceValentineBear +", not matches with expected total price");
            throw e;
        }
        System.out.println("Total price of Stuffed Frog in Cart : "+totalCalPriceStuffedFrog +", matches with expected total price");
        System.out.println("Total price of Fluffy Bunny in Cart : "+totalCalPriceFluffyBunny +", matches with expected total price");
        System.out.println("Total price of Valentine Bear in Cart : "+totalCalPriceValentineBear +", matches with expected total price");

    }



    @AfterMethod
    public void tearDown(){
        driver.quit();
    }

}
