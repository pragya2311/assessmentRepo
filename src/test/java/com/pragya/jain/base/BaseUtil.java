package com.pragya.jain.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.pragya.jain.utilities.ExtentManager;
import com.pragya.jain.utilities.DataUtil;
import com.pragya.jain.utilities.Xls_Reader;
import io.restassured.RestAssured;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.WriterOutputStream;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.asserts.SoftAssert;

import java.io.*;
import java.util.Date;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseUtil {
    public WebDriver Driver;
    public ExtentReports rep;
    public ExtentTest scenario;

    public static String sessionid;

    public static String reportFolder;
    public int iteration;

    public Properties testprop;
    public Xls_Reader xls;
    public SoftAssert softAssert = new SoftAssert();
    public static StringWriter requestWritier;
    public static PrintStream requestCapture;


    public void flushReport(){
        if(rep!=null)
            rep.flush();
    }

    public void initReports(String scenarioName) {
        rep = ExtentManager.getInstance("D:\\sources\\Reports\\", scenarioName);
        scenario = rep.createTest(scenarioName);
        scenario.log(Status.INFO, "Starting " + scenarioName);
    }

    public void infoLog(String msg){
        scenario.log(Status.INFO,msg);
    }

    public void reportFailure(String errorMsg){
        scenario.log(Status.FAIL,errorMsg);
        takeScreenShot();
        assertThat(false);
    }

    public void takeScreenShot(){
        Date d = new Date();
        String screenshotFile = d.toString().replace(":","_").replace(" ","_")+".png";
        File srcFile = ((TakesScreenshot)Driver).getScreenshotAs(OutputType.FILE);
        try{
            FileUtils.copyFile(srcFile,new File(ExtentManager.screenShotFolderPath+screenshotFile));
            scenario.log(Status.FAIL,"Screenshot-> "+scenario.addScreenCaptureFromPath(ExtentManager.screenShotFolderPath+screenshotFile));

        }catch(IOException e){e.printStackTrace();}

    }

    @BeforeTest
    public void init() {

        testprop = new Properties();

        try {
            FileInputStream fs = new FileInputStream(
                    System.getProperty("user.dir")
                            + "//src//test//resources//project.properties");

            testprop.load(fs); // loading the Properties

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        xls = new Xls_Reader(testprop.getProperty("xlspath"));
        RestAssured.baseURI = testprop.getProperty("baseurl");
        String inputUserName = this.getClass().getSimpleName().toLowerCase();
//        RestAssured.basePath = testprop.getProperty(testname);

    }

    @BeforeMethod
    public void before() {
        iteration++;
        rep = ExtentManager.getInstance(testprop.getProperty("reportPath"));
        scenario = rep.createTest("Login");


        requestWritier = new StringWriter();
        requestCapture = new PrintStream(new WriterOutputStream(requestWritier),true);


    }

    @DataProvider
    public Object[][] getData() {

        return DataUtil.getData1(xls, this.getClass().getSimpleName());
    }

    public void reportFailure(String errMsg, boolean stop) {
        softAssert.fail(errMsg);

        if (stop)
            softAssert.assertAll();

    }




    @AfterMethod
    public void after(){
        rep.flush();
    }

    public void addReqResLinkToReport(String message,String fileName,String content){
        // file create
        try {
            System.out.println(reportFolder);
            System.out.println(reportFolder+"//log//"+fileName+".html");
            new File(reportFolder+"//log//"+fileName+".html").createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // write
        FileWriter fw;
        try {
            fw = new FileWriter(reportFolder+"//log//"+fileName+".html");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            fw.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // add to report
        scenario.log(Status.INFO, "<a href='log/"+fileName+".html' target='_blank'>Click Here for "+message+"</a>");

    }


   /* @DataProvider(name = "inputData")
    public static Iterator<Object[]> getData (Method M) throws Exception{
        return readExcelData(M,"D:\\Sources\\src\\test\\resources\\data\\testdata.xlsx","Sheet1");
    }

    private static Iterator<Object[]> readExcelData(Method M, String fileLocation, String sheetName) throws Exception{
        List<Hashtable><String, String>> a = ExcelUtils.readFile(System.getProperty("user.dir"))
    }*/


}
