package com.company;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class Codeforces implements Websites {
    
    private Automation automation;
    protected void setter(Automation automation) {
        this.automation = automation;
    }

    private void login() {
        automation.setDriver();
        if(!automation.codeforces_loginedin) {
            if(automation.competitiveProgramming.username_pass.get("codeforces")[0] == null || automation.competitiveProgramming.username_pass.get("codeforces")[1] == null) {
                automation.problem_input_output.add_to_status("Please add codeforces username and password to login\n");
                File file = new File(Competitive_Programming.folderPath + "/src/logins/cff.txt");
                if(file.exists())
                    file.delete();
                file = new File(Competitive_Programming.folderPath + "/src/logins/cf.txt");
                if(file.exists())
                    file.delete();
                Automation.sleep(2000);
                Competitive_Programming.check_login();
                login();
                return;
            }
            automation.problem_input_output.add_to_status("Login to codeforces.com\n");
            automation.get_driver().get("https://codeforces.com/enter?back=%2F");
            automation.wait_page_loading(By.id("handleOrEmail"));
            automation.get_driver().findElement(By.id("handleOrEmail")).sendKeys(automation.competitiveProgramming.username_pass.get("codeforces")[0]);
            automation.get_driver().findElement(By.id("password")).sendKeys(automation.competitiveProgramming.username_pass.get("codeforces")[1]);
            automation.get_driver().findElement(By.id("remember")).click();
            automation.get_driver().findElement(By.className("submit")).click();
            while (automation.driver.getCurrentUrl().equals("https://codeforces.com/enter?back=%2F")) {
                if(automation.driver.findElements(By.className("shiftUp")).size() > 0) {
                    automation.problem_input_output.add_to_status("Invalid handle/email or password\nPlease enter the username and password of your account again...\n");
                    File file = new File(Competitive_Programming.folderPath + "/src/logins/cff.txt");
                    if(file.exists())
                        file.delete();
                    file = new File(Competitive_Programming.folderPath + "/src/logins/cf.txt");
                    if(file.exists())
                        file.delete();
                    Automation.sleep(2000);
                    Competitive_Programming.check_login();
                    login();
                    break;
                }
            }
            automation.codeforces_loginedin = true;
            while (!automation.get_driver().getCurrentUrl().equals("https://codeforces.com/"))
                automation.sleep(500);
        }
    }
    
    public List<List<String>> load_testCases (String url) throws Exception {
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
        List data = automation.get_driver().findElements(By.className("sample-test"));
        automation.problem_input_output.add_to_status("\nconnect Successfully!\n");
        automation.problem_input_output.add_to_status("fetching the data from the server...\n");
        if(data.size() == 0) {
            throw new MalformedURLException();
        }
        String temp = ((WebElement)data.get(0)).getText().trim();
        String[] strarr = temp.split("input\nCopy");
        for (int i = 1; i < strarr.length; ++i) {
            String[] input_output = strarr[i].split("output\nCopy");
            input.add(input_output[0].trim());
            output.add(input_output[1].trim());
        }
        if(input.size() == 0) {
            throw new MalformedURLException();
        }
        List<List<String>> list = new ArrayList<>();
        list.add(input);
        list.add(output);
        return list;
    }

    public void submit (String url) {
        automation.input_output_unit.clearOutput();
        login();
        automation.get_driver().get(url);
        automation.problem_input_output.add_to_status("Submitting...\n");
        automation.problem_input_output.exit();
        String start_string = "codeforces.com problem: " + automation.competitiveProgramming.get_fileName()+"\n";
        automation.input_output_unit.setOutput(start_string);
        automation.wait_page_loading(By.name("sourceFile"));
        automation.get_driver().findElement(By.name("sourceFile")).sendKeys(automation.problem_input_output.get_sourceFile_path());
        String tmp = automation.competitiveProgramming.comboBox1.getSelectedItem().toString().trim().toLowerCase();
        Select select = new Select(automation.driver.findElement(By.name("programTypeId")));
        if(tmp.contains("c++")) {
            select.selectByValue("54");
        } else if(tmp.contains("python3")) {
            select.selectByValue("31");
        } else if(tmp.contains("python")) {
            select.selectByValue("7");
        } else if(tmp.contains("java")){
            select.selectByValue("60");
        } else {
            JOptionPane.showMessageDialog(null, "Can't determine the used language", "Undefined language", 0);
            automation.input_output_unit.setOutput("Can't submit\n");
            return;
        }
        automation.get_driver().findElement(By.className("submit")).click();
        automation.sleep(2000);
        if(automation.get_driver().getCurrentUrl().contains(url)) {
            automation.input_output_unit.setOutput(start_string + "You have submitted exactly the same code before\n");
            automation.problem_input_output.exit();
            return;
        }
        automation.get_driver().get(url);
        JavascriptExecutor js = (JavascriptExecutor) automation.get_driver();
        js.executeScript("window.scrollBy(0, 500)");
        try {
            load_results(0, url);
        } catch (Exception exception) {
            exception.printStackTrace();
            automation.input_output_unit.addOutput("Please check the results in the problem page\n");
        }
    }

    private String get_taple_xpath() throws Exception {
        int tmp = 3;
        while(automation.get_driver().findElements(By.xpath("//*[@id='sidebar']/div[" + tmp + "]/table")).size() == 0 && tmp <= 12) tmp++;
        if(tmp == 13) throw new Exception("Can't find codeforces results table");
        return "//*[@id='sidebar']/div["+tmp+"]/table";
    }

    private String get_table_result(String table_xpath) throws Exception {
        WebElement Webtable = automation.get_driver().findElement(By.xpath(table_xpath));
        List<WebElement> totalRowCount = Webtable.findElements(By.xpath(table_xpath + "/tbody/tr"));
        if(totalRowCount.size() <= 1) {
            throw new Exception("Can't find results in the problem page");
        }
        return totalRowCount.get(1).findElements(By.xpath("td")).get(2).getText();
    }

    private void load_results(int count, String url) {
        try {
            String start_string = "codeforces.com problem: " + automation.competitiveProgramming.get_fileName() + "\n";
            automation.wait_page_loading(By.xpath(get_taple_xpath()));
            while (get_table_result(get_taple_xpath()).toLowerCase().contains("running") || get_table_result(get_taple_xpath()).toLowerCase().contains("queue")) {
                automation.get_driver().get(url);
                automation.wait_page_loading(By.xpath(get_taple_xpath()));
                automation.input_output_unit.setOutput(start_string + get_table_result(get_taple_xpath()));
                automation.sleep(500);
            }
            automation.input_output_unit.setOutput(start_string + get_table_result(get_taple_xpath()));
            if(get_table_result(get_taple_xpath()).contains("Accepted")) {
                automation.problem_input_output.save_the_file();
            }
        } catch (Exception exception) {
            if(count == 20) {
                exception.printStackTrace();
                automation.input_output_unit.addOutput("Please check the results in the problem page\n");
            } else {
                automation.driver.navigate().refresh();
                load_results(count++, url);
            }
        }
    }

}

