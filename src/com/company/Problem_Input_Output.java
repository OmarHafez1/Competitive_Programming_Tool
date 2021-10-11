package com.company;

import org.apache.commons.validator.routines.UrlValidator;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Problem_Input_Output extends JFrame {
    private JTextField url;
    private JButton load_testCases;
    private JPanel jpanel;
    private JButton saveButton;
    private JButton submitButton;
    private JTextPane textPane;
    private JTextPane status;
    private Run_Compile_Unit run_compile_unit;
    private Input_Output_Unit input_output_unit;
    private Competitive_Programming competitiveProgramming;
    Automation automation;
    private Codeforces codeforces;
    private Atcoder atcoder;
    private Hakerearth hakerearth;

    protected void setter(Competitive_Programming competitiveProgramming, JTextPane textPaneesdf) {
        this.competitiveProgramming = competitiveProgramming;
        this.input_output_unit = competitiveProgramming.input_output_unit;
        this.run_compile_unit = competitiveProgramming.run_compile_unit;
        this.url.setText(competitiveProgramming.problem_url);
        this.textPane = textPane;
        atcoder = competitiveProgramming.atcoder;
        codeforces = competitiveProgramming.codeforces;
        hakerearth = competitiveProgramming.hakerearth;
        automation = competitiveProgramming.automation;
    }

    protected String get_sourceFile_path() {
        if(competitiveProgramming.comboBox1.getSelectedIndex() == 0) { // C++
            return Competitive_Programming.folderPath + "/CODE/C++/" + Competitive_Programming.get_fileName() + ".cpp";
        }
        if(competitiveProgramming.comboBox1.getSelectedIndex() == 1) { //Python
            return Competitive_Programming.folderPath + "/CODE/Python/" + Competitive_Programming.get_fileName() + ".py";
        }
        if(competitiveProgramming.comboBox1.getSelectedIndex() == 2) { //Python3
            return Competitive_Programming.folderPath + "/CODE/Python/" + Competitive_Programming.get_fileName() + ".py";
        }
        // Java
        return Competitive_Programming.folderPath + "/CODE/Java" + Competitive_Programming.get_fileName() + ".java";
    }

    protected String get_problem_url () {
        return url.getText();
    }
    protected void set_problem_url(String url) {
        this.url.setText(url.trim());
    }
    protected void paste_problem_url() {
        url.setText("");
        url.paste();
        url.setText(url.getText().trim());
    }

    protected void exit() {
        automation.sleep(1500);
        competitiveProgramming.problem_input_output.dispatchEvent(new WindowEvent(competitiveProgramming.problem_input_output, WindowEvent.WINDOW_CLOSING));
    }

    protected Problem_Input_Output(String title) {
        super(title);
        this.setDefaultCloseOperation(2);
        this.setContentPane(this.jpanel);
        this.pack();
        url.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3) { // mean the user clicked the mouse right click
                    url.setText("");
                    url.paste();
                    url.setText(url.getText().trim());
                }
            }
        });
        this.load_testCases.addActionListener(e -> {
            load_testCases();
        });

        saveButton.addActionListener(e -> {
            save_the_file();
        });

        submitButton.addActionListener(e -> {
            submit();
        });
    }

    protected void submit() {
        status.setText("");
        if (!url_valid()) return;
        new Thread(() -> {
            try {
                Websites websites = get_websiteObject(get_problem_url());
                if (websites == null || websites == hakerearth) {
                    add_to_status(" Is not supported right now\n");
                    return;
                }
                URL myUrl = new URL(url.getText());
                set_status(myUrl.getHost());
                add_to_status("\n");
                websites.submit(get_problem_url());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    protected void load_testCases() {
        status.setText("");
        if(!url_valid()) return;
        Thread thread = new Thread(() -> {
            try {
                URL myUrl = new URL(get_problem_url());
                competitiveProgramming.problem_url = get_problem_url();
                set_status(myUrl.getHost());
                List<List<String>> list;
                Websites websites = get_websiteObject(get_problem_url());
                if(websites == null) {
                    add_to_status(" Is not supported right now\n");
                    return;
                }
                add_to_status("\n");
                competitiveProgramming.reset_app();
                list = websites.load_testCases(get_problem_url());
                send_testCases(list);
                exit();
            }
            catch (Exception exception) {
                set_status("An error occurred!");
                exception.printStackTrace();
            }
        });
        thread.start();

    }

    private boolean url_valid () {
        try {
            URL myUrl = new URL(get_problem_url());
            UrlValidator urlValidator = new UrlValidator();
            if (!urlValidator.isValid(myUrl.toString())) {
                throw new MalformedURLException();
            }
            if (get_problem_url().isEmpty()) {
                add_to_status("Please add the url of the problem !");
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            set_status("Please check your URL");
            return false;
        }
        return true;
    }

    protected Websites get_websiteObject(String url) {
        if (url.contains("codeforces.com")) {
            return codeforces;
        } else if (url.contains("atcoder.jp")) {
            return atcoder;
        } else if(url.contains("hackerearth.com")) {
            return hakerearth;
        } else {
            return null;
        }
    }

    private void send_testCases(List<List<String>> list) {
        for (int i = 0; i < list.get(0).size(); ++i) {
            input_output_unit.inputTexts.get(i).setText((list.get(0)).get(i));
            input_output_unit.answersTexts.get(i).setText((list.get(1)).get(i));
        }
        add_to_status("DONE!!\n");
        if(textPane != null)
            textPane.setCaretPosition(0);
    }

    private String get_type_of_file() {
        if(competitiveProgramming.comboBox1.getSelectedIndex() == 0) return ".cpp";
        if(competitiveProgramming.comboBox1.getSelectedIndex() == 1) return ".py";
        return ".java";
    }

    //save the file
    protected boolean save_the_file() {
        try {
            if(competitiveProgramming.problem_input_output_ISOPEN) status.setText("");
            competitiveProgramming.problem_url = get_problem_url();
            if (competitiveProgramming.problem_url.trim().equals("")) {
                if (competitiveProgramming.problem_input_output_ISOPEN)
                    add_to_status("Please add the url of the problem !\n");
                return false;
            }
            URL myUrl = new URL(competitiveProgramming.problem_url);
            set_status(myUrl.getHost() + "\n");
            String fileName = convert_URL_to_Name(competitiveProgramming.problem_url);
            String savePath = Competitive_Programming.folderPath + "/Solutions/" + myUrl.getHost();
            File file = new File(savePath);
            if(!file.exists()) {
                file.mkdir();
            }
            String source = Competitive_Programming.fix_local_path(get_sourceFile_path());
            String dest = Competitive_Programming.fix_local_path(Competitive_Programming.folderPath + "/Solutions/" + myUrl.getHost() + "/" + fileName + get_type_of_file());
            ProcessBuilder processBuilder = new ProcessBuilder();
            if( System.getProperty("os.name").charAt(0) == 'W') {
                processBuilder.command(Competitive_Programming.osCommand, Competitive_Programming.osCH, "copy \"" + source + "\" \"" + dest + "\"\n");
                System.out.println("copy \"" + source + "\" \"" + dest + "\"\n");
            } else {
                processBuilder.command(Competitive_Programming.osCommand, Competitive_Programming.osCH, "cp " + source + " " + dest + "\n");
            }
            processBuilder.start();
            if(competitiveProgramming.problem_input_output_ISOPEN)
                add_to_status("Done!");
            else
                input_output_unit.addOutput("\nFile saved\n");

            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    exit();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }).start();
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    protected static String convert_URL_to_Name(String url) {
        try {
            URL myUrl = new URL(url);
            String fileName = "";
            String tmp[] = myUrl.getFile().split("/");
            if (url.contains("codeforces.com")) {
                if (tmp[tmp.length - 2].equals("problem")) {
                    tmp[tmp.length - 2] = tmp[tmp.length - 3];
                }
                fileName += tmp[tmp.length - 2];
                fileName += "_" + tmp[tmp.length - 1];
            } else if (url.contains("atcoder.jp")) {
                fileName += tmp[tmp.length - 1];
            } else if (url.contains("projecteuler.net")) {
                fileName += "problem_" + tmp[1].split("=")[1];
            } else {
                fileName += tmp[tmp.length - 1];
            }
            return fileName;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "";
    }

    protected void set_status(String txt) {
        status.setText(txt);
    }

    protected void add_to_status(String str) {
        status.setText(this.status.getText() + str);
    }
}

