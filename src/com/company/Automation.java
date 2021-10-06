package com.company;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Automation {
    protected boolean atcoder_loginedin = false, codeforces_loginedin = false;
    protected WebDriver driver;

    Problem_Input_Output problem_input_output;
    protected Competitive_Programming competitiveProgramming;
    Input_Output_Unit input_output_unit;

    protected void setter(Competitive_Programming competitiveProgramming, Problem_Input_Output problem_input_output, Input_Output_Unit input_output_unit) {
        this.competitiveProgramming = competitiveProgramming;
        this.problem_input_output = problem_input_output;
        this.input_output_unit = input_output_unit;
    }

    protected WebDriver get_driver() {
        return driver;
    }

    protected void reset_driver() {
        driver = null;
        atcoder_loginedin = false;
        codeforces_loginedin = false;
        setDriver();
    }

    protected void setDriver() {
        try {
            if (driver != null) return;
            System.setProperty("webdriver.chrome.driver", Competitive_Programming.fix_local_path(Competitive_Programming.folderPath + "/src/chromedriver") + (System.getProperty("os.name").charAt(0) == 'W'? ".exe" : ""));
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("window-size=1920,1080");
            options.addArguments("--disable-gpu");
            options.setPageLoadStrategy(PageLoadStrategy.EAGER);
            driver = new ChromeDriver(options);
        } catch (Exception exception) {
            problem_input_output.add_to_status("Can't setup Chrome Driver\n");
        }
    }

    protected void wait_page_loading(By by) {
        int wait_timeout = 0;
        while(driver.findElements(by).size() == 0) {
            if(wait_timeout == 12) {
                System.out.println(by.toString() + "    Is not loaded");
                driver.navigate().refresh();
                wait_page_loading(by);
                return;
            }
            sleep(500);
            wait_timeout++;
        }
    }

    protected void sendTextJS(String xpath, String text) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        WebElement element = driver.findElement(By.xpath(xpath));
        jsExecutor.executeScript("arguments[1].value=arguments[0]",text, element);
    }

    protected static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    protected void killProcess() {
        try {
            if(driver != null) {
                driver.quit();
                driver = null;
                atcoder_loginedin = false;
                codeforces_loginedin = false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}

