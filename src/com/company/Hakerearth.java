package com.company;

import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

public class Hakerearth implements Websites{

    private Automation automation;
    protected void setter(Automation automation) {
        this.automation = automation;
    }

    @Override
    public void submit(String url) {

    }

    public List<List<String>> load_testCases (String url) {
        ArrayList<String> input = new ArrayList<>();
        ArrayList<String> output = new ArrayList<>();
        automation.setDriver();

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

        String input_xpath = "/html/body/div[7]/div/div/div/div[3]/div/div/div[1]/div/div[2]/div[1]/div[2]/pre";
        String output_xpath = "/html/body/div[7]/div/div/div/div[3]/div/div/div[1]/div/div[2]/div[2]/div[2]/pre";
        int cnt = 0;
        try {
            while(automation.get_driver().findElements(By.xpath("/html/body/div[7]/div/div/div/div[3]/div/div/div[1]/div/div[2]/div[1]/div[2]/pre")).size() == 0 && automation.get_driver().findElements(By.xpath("/html/body/div[8]/div/div/div/div[3]/div/div/div[1]/div/div[2]/div[1]/div[2]/pre")).size() == 0) {
                Thread.sleep(500);
                cnt++;
                if(cnt == 0) {
                    automation.problem_input_output.add_to_status("\ncouldn't connect to the server .. please check your url and check your internet connection\n");
                    return null;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if(automation.get_driver().findElements(By.xpath(input_xpath)).size() == 0) {
            input_xpath = "/html/body/div[8]/div/div/div/div[3]/div/div/div[1]/div/div[2]/div[1]/div[2]/pre";
            output_xpath = "/html/body/div[8]/div/div/div/div[3]/div/div/div[1]/div/div[2]/div[2]/div[2]/pre";
        }

        input.add(automation.get_driver().findElement(By.xpath(input_xpath)).getText());
        output.add(automation.get_driver().findElement(By.xpath(output_xpath)).getText());

        List<List<String>> list = new ArrayList<>();
        list.add(input);
        list.add(output);
        automation.problem_input_output.exit();
        return list;
    }
    
}
