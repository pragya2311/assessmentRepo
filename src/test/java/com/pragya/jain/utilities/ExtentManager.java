package com.pragya.jain.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.pragya.jain.base.BaseUtil;

import java.io.File;
import java.util.Date;

public class ExtentManager {
    private static ExtentReports extent;
    public static String screenShotFolderPath;
    public ExtentTest scenario;

    public static ExtentReports getInstance(String reportPath, String scenarioName) {
        if (extent == null) {
            String fileName = "Report.html";
            Date d = new Date();
            String folderName = d.toString().replace(":", "_").replace(" ", "_") + " " + scenarioName;
            new File(reportPath + folderName + "//screenshots").mkdirs();
            reportPath = reportPath + folderName + "//";
            screenShotFolderPath = reportPath + "screenshots//";
            System.out.println(reportPath + fileName);
            createInstance(reportPath + fileName);
        }
        return extent;
    }

    public static ExtentReports getInstance(String path) {
        if (extent == null) {


            Date d = new Date();
            String folderName=d.toString().replace(":", "_").replace(" ","_");
            new File(path+folderName+"//log").mkdirs();
            BaseUtil.reportFolder=path+folderName;
            String fileName = path+folderName+"//report.html";


            createInstance(fileName);

        }
        return extent;
    }

    public static ExtentReports createInstance(String fileName) {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setDocumentTitle("Reports");
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName("Automation Test Reports");

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
    return extent;
    }
    }

