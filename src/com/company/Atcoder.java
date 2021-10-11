package com.company;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Atcoder implements Websites {

    private Automation automation;
    protected void setter(Automation automation) {
        this.automation = automation;
    }

    private void login() {
        if(Competitive_Programming.username_pass.get("atcoder")[0] == null || Competitive_Programming.username_pass.get("atcoder")[1] == null) {
            System.out.println("Didn't store the logins to atcoder.jp");
            automation.problem_input_output.add_to_status("Please add atcoder username and password to login\n");
            File file = new File(Competitive_Programming.folderPath + "/src/logins/acc.txt");
            if(file.exists())
                file.delete();
            file = new File(Competitive_Programming.folderPath + "/src/logins/ac.txt");
            if(file.exists())
                file.delete();
            Automation.sleep(2000);
            Competitive_Programming.check_login();
            login();
            return;        }
        automation.setDriver();
        if(!automation.atcoder_loginedin) {
            automation.problem_input_output.add_to_status("Login to atcoder.jp\n");
            automation.get_driver().get("https://atcoder.jp/login?continue=https%3A%2F%2Fatcoder.jp%2Fhome");
            automation.wait_page_loading(By.id("username"));
            automation.get_driver().findElement(By.id("username")).sendKeys(Competitive_Programming.username_pass.get("atcoder")[0]);
            automation.get_driver().findElement(By.id("password")).sendKeys(Competitive_Programming.username_pass.get("atcoder")[1]);
            automation.get_driver().findElement(By.id("submit")).click();
            while (automation.driver.getCurrentUrl().equals("https://atcoder.jp/login?continue=https%3A%2F%2Fatcoder.jp%2Fhome")) {
                if(automation.driver.findElements(By.xpath("//*[@id=\"main-container\"]/div[1]/div")).size() > 0) {
                    if(automation.driver.findElement(By.xpath("//*[@id=\"main-container\"]/div[1]/div")).getText().contains("Username or Password is incorrect.")) {
                        automation.problem_input_output.add_to_status("Username or Password is incorrect.\nPlease enter the username and password of your account again...\n");
                        File file = new File(Competitive_Programming.folderPath + "/src/logins/acc.txt");
                        file.delete();
                        file = new File(Competitive_Programming.folderPath + "/src/logins/ac.txt");
                        file.delete();
                        Automation.sleep(2000);
                        Competitive_Programming.check_login();
                        login();
                        break;
                    }
                }
            }
            automation.atcoder_loginedin = true;
        }
    }

    private String get_textFile(String url) {
        automation.get_driver().get("file://" + url);
        return automation.get_driver().findElement(By.xpath("/html/body/pre")).getText();
    }

    public void submit(String url) {
        automation.input_output_unit.clearOutput();
        login();
        String textFile = get_textFile(automation.problem_input_output.get_sourceFile_path());
        automation.get_driver().get(url);
        automation.problem_input_output.add_to_status("Submitting...\n");
        automation.problem_input_output.exit();
        String start_string = "atcoder.jp problem: " + automation.competitiveProgramming.get_fileName() + "\n";
        automation.input_output_unit.setOutput(start_string);
        automation.wait_page_loading(By.id("submit"));
        JavascriptExecutor js = (JavascriptExecutor) automation.get_driver();
        js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
        automation.get_driver().findElement(By.id("select-lang")).click();
        String tmp = automation.competitiveProgramming.comboBox1.getSelectedItem().toString().trim().toLowerCase();
        int cnt = 1;
        if(tmp.contains("c++")) {
            automation.get_driver().findElement(By.xpath("/html/body/span/span/span[2]/ul/li[3]")).click();
        } else if(tmp.contains("python")) {
            automation.get_driver().findElement(By.xpath("/html/body/span/span/span[2]/ul/li[6]")).click();
        } else if(tmp.contains("java")){
            automation.get_driver().findElement(By.xpath("/html/body/span/span/span[2]/ul/li[5]")).click();
        } else {
            for(; cnt <= 67; cnt++) {
                if(automation.get_driver().findElement(By.xpath("/html/body/span/span/span[2]/ul/li[" + cnt + "]")).getText().toLowerCase().trim().contains(tmp)) {
                    automation.get_driver().findElement(By.xpath("/html/body/span/span/span[2]/ul/li[" + cnt + "]")).click();
                    cnt = 100;
                }
            }
        }
        if(cnt == 100) {
            JOptionPane.showMessageDialog(null, "Can't determine the used language", "Undefined language", 0);
            automation.input_output_unit.setOutput("Can't submit\n");
            return;
        }
        if(automation.get_driver().findElement(By.className("btn-toggle-editor")).getAttribute("aria-pressed").contains("false"))
            automation.get_driver().findElement(By.className("btn-toggle-editor")).click();

        automation.sendTextJS("//*[@id='sourceCode']/textarea", textFile);

        automation.get_driver().findElement(By.id("submit")).click();
        while (!automation.get_driver().getCurrentUrl().contains("submissions")) {
            automation.sleep(500);
        }
        load_results(0);
    }

    private String get_table_xpath() {
        int tmp = 2;
        String xpath = "//*[@id=\"main-container\"]/div[1]/div[3]/div[1]/div[2]/table";
        while (automation.get_driver().findElements(By.xpath(xpath)).size() < 1 && tmp < 4) {
            xpath = "//*[@id=\"main-container\"]/div[1]/div[3]/div[" + tmp + "]/div[2]/table";
            tmp++;
        }
        return xpath;
    }

    private String get_atcoder_table_result() throws Exception {
        String table_xpath = "//div[@class='table-responsive']/table";
        if(automation.get_driver().findElements(By.xpath(table_xpath)).size() < 1) {
            table_xpath = get_table_xpath();
        }
        automation .wait_page_loading(By.xpath(table_xpath));
        WebElement Webtable = automation.get_driver().findElement(By.xpath(table_xpath));
        List<WebElement> totalRowCount = Webtable.findElements(By.xpath(table_xpath + "/tbody/tr"));
        if(totalRowCount.size() < 1) {
            throw new Exception("Can't find results in the problem page");
        }
        return totalRowCount.get(0).findElements(By.xpath("td")).get(6).getText();
    }

    private void load_results(int tmp)  {
        try {
            String result = get_atcoder_table_result();
            String start_string = "atcoder.jp problem: " + automation.competitiveProgramming.get_fileName() + "\n";
            JavascriptExecutor js = (JavascriptExecutor) automation.get_driver();
            int cnt = 0;
            js.executeScript("window.scrollBy(0,(-1*document.body.scrollHeight))");
            while (result.equals(complete_status(result))) {
                cnt++;
                result = get_atcoder_table_result();
                automation.input_output_unit.setOutput(start_string + result);
                if (cnt == 360) break;
                automation.sleep(500);
            }
            result = get_atcoder_table_result();
            automation.input_output_unit.setOutput(start_string + complete_status(result));
            if (complete_status(result).contains("Accepted"))
                automation.problem_input_output.save_the_file();
            System.out.println("Done! Atcoder.jp submission");
        } catch (Exception exception) {
            if(tmp == 20) {
                exception.printStackTrace();
                automation.input_output_unit.addOutput("Please check the results in the problem page\n");
            } else {
                automation.driver.navigate().refresh();
                load_results(tmp + 1);
            }
        }
    }

    public List<List<String>> load_testCases(String url) throws Exception {
        ArrayList<String> input = new ArrayList<>();
        ArrayList<String> output = new ArrayList<>();
        login();
        automation.problem_input_output.add_to_status("connecting...");
        Thread thread = new Thread(() -> automation.get_driver().get(url));
        thread.start();
        int num_of_connection = 0;
        while (thread.isAlive()) {
            automation.problem_input_output.add_to_status(".");
            automation.sleep(800);
            if (++num_of_connection <= 120) continue;
            automation.problem_input_output.add_to_status("\ncouldn't connect to the server .. please check your url and check your internet connection\n");
            return null;
        }
        automation.problem_input_output.add_to_status("\nconnect Successfully!\nfetching the data from the server...\n");
        int start = 0;
        while(automation.get_driver().findElement(By.id("pre-sample" + start)).getText().isEmpty()) {
            start++;
        }
        while(automation.get_driver().findElements(By.id("pre-sample" + start)).size() != 0) {
            input.add(automation.get_driver().findElement(By.id("pre-sample" + start)).getText());
            start++;
            output.add(automation.get_driver().findElement(By.id("pre-sample" + start)).getText());
            start++;
        }
        List<List<String>> list = new ArrayList<>();
        list.add(input);
        list.add(output);
        automation.problem_input_output.exit();
        return list;
    }
    
    private String complete_status(String result) {
        if(result.equals("CE")) return "Compilation error";
        if(result.equals("TLE")) return "Time limit exceeded";
        if(result.equals("MLE")) return "Memory limit exceeded";
        if(result.equals("RE")) return "Runtime error";
        if(result.equals("AC")) return "Accepted";
        if(result.equals("WA")) return "Wrong answer";
        return result;
    }

}
