package com.pragya.jain.steps;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.pragya.jain.base.BaseUtil;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;

import java.io.IOException;

public class Hook extends BaseUtil {
    private BaseUtil base;

    public Hook(BaseUtil base1) {
        this.base = base1;
    }


    @Before(order = 0)
    public void before(Scenario scenario) {
        base.initReports(scenario.getName());
    }

    @Before(order = 1)
    public synchronized static void createExcelReport() throws IOException, InvalidFormatException {
//        String excelFilePath = SeleniumProps.getValue(SeleniumProps.RT_EXCELREPORTNAME);
    }

    public void setUpLocal(String browser, String url) throws IOException {
        if ("IE".equalsIgnoreCase(browser)) {
            System.setProperty("webdriver. ie .driver", "");
            InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
            internetExplorerOptions.ignoreZoomSettings();
            internetExplorerOptions.introduceFlakinessByIgnoringSecurityDomains();
            internetExplorerOptions.requireWindowFocus();
            internetExplorerOptions.enablePersistentHovering();
            base.Driver = new InternetExplorerDriver(internetExplorerOptions);
        }
    }
}