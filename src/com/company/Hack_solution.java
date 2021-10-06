package com.company;

import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

public class Hack_solution extends JFrame {
    JLabel label1, label2, label3, label4, label5, label6;
    JEditorPane status;
    JTextField ac_solution, hack_solution, testCase_generator, timeLimit, link, number_of_testCases;
    JComboBox comboBox1, comboBox2;
    JButton button;
    JProgressBar progressBar;
    JCheckBox jCheckBox;
    Thread thread;

    class Hack_solution_last_data {
        String accepted_solution_file, hack_solution_file, testCase_generator, url, timeLimit, number_of_testCases;
        boolean jcheck_state;
        Hack_solution_last_data(String accepted_solution_file, String hack_solution_file, String testCase_generator, String url, String timeLimit, String number_of_testCases, boolean jcheck_state) {
            this.accepted_solution_file = accepted_solution_file;
            this.hack_solution_file = hack_solution_file;
            this.number_of_testCases = number_of_testCases;
            this.testCase_generator = testCase_generator;
            this.url = url;
            this.timeLimit = timeLimit;
            this.jcheck_state = jcheck_state;
        }
    }

    protected void run() {
        setTitle("Hack Solutions");
        setResizable(false);
        setSize(800, 700);
        int x = 0, y = 50;
        setLayout(null);
        label1 = new JLabel("Accepted solution:");
        label2 = new JLabel("Hacking solution:");
        label3 = new JLabel("Test cases generator:");
        label4 = new JLabel("Time limit:");
        label5 = new JLabel("Link of the problem:");
        label6 = new JLabel("Number of TestCases:");

        progressBar = new JProgressBar();
        //progressBar.setBackground(new Color(39,48,184));
        progressBar.setForeground(new Color(39,48,184));
        progressBar.setBounds(0, 660, 800, 10);
        progressBar.setValue(0);
        progressBar.setVisible(false);

        jCheckBox = new JCheckBox("Generate new testCases");
        jCheckBox.setSelected(false);
        jCheckBox.setBounds(x+30, y+195, 250, 30);
        jCheckBox.setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 20));
        jCheckBox.setForeground(Color.blue);

        link = new JTextField();
        status = new JEditorPane();
        status.setEditable(false);
        status.setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 18));
        status.setForeground(Color.black);

        DefaultCaret caret = (DefaultCaret) status.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane scroll = new JScrollPane (status,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(x+30, y+250, 740, 250);
        add(scroll);

        comboBox1 = new JComboBox();
        comboBox2 = new JComboBox();

        comboBox1.addItem("C++");
        comboBox1.addItem("Python");
        comboBox1.addItem("Python3");
        comboBox1.addItem("Java");
        comboBox2.addItem("C++");
        comboBox2.addItem("Python");
        comboBox2.addItem("Python3");
        comboBox2.addItem("Java");

        comboBox1.setBounds(x+110, 30, 150, 30);
        comboBox2.setBounds(x+480, 30, 150, 30);
        comboBox1.setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 22));
        comboBox2.setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 22));

        label1.setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 20));
        label1.setForeground(Color.blue);
        label2.setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 20));
        label2.setForeground(Color.blue);
        label3.setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 20));
        label3.setForeground(Color.blue);
        label4.setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 20));
        label4.setForeground(Color.blue);
        label5.setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 20));
        label5.setForeground(Color.blue);
        label6.setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 20));
        label6.setForeground(Color.blue);


        ac_solution = new JTextField();
        ac_solution.setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 20));
        hack_solution = new JTextField();
        hack_solution.setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 20));
        testCase_generator = new JTextField();
        testCase_generator.setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 20));
        timeLimit = new JTextField();
        timeLimit.setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 20));
        link = new JTextField();
        link.setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 20));
        number_of_testCases = new JTextField();
        number_of_testCases.setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 20));

        label1.setBounds(x+30, y+40, 220, 30);
        label4.setBounds(x+30, y+80, 220, 30);
        label2.setBounds(x+400, y+40, 220, 30);
        label3.setBounds(x+400, y+80, 220, 30);

        ac_solution.setBounds(x+210, y+40, 150, 30);
        timeLimit.setBounds(x+210, y+80, 150, 30);
        hack_solution.setBounds(x+605, y+40, 150, 30);
        testCase_generator.setBounds(x+605, y+80, 150, 30);

        label5.setBounds(x+30, y+130, 200, 30);
        link.setBounds(x+240, y+130, 515, 30);

        label6.setBounds(x+420, y+190, 200, 40);
        number_of_testCases.setBounds(x+635, y+190, 120, 40);

        button = new JButton("Start");
        button.setForeground(Color.white);
        button.setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 30));
        button.setBackground(new Color(39,48,184));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setBounds(x+320, y+550, 120, 40);

        add(label1); add(label2); add(label3); add(label4);
        add(ac_solution); add(hack_solution); add(testCase_generator); add(timeLimit);
        add(button);
        add(comboBox1); add(comboBox2);
        add(label5); add(link);
        add(jCheckBox);
        add(label6); add(number_of_testCases);
        add(progressBar);

        button.addActionListener(e -> {
            if(thread != null && thread.isAlive()) thread.interrupt();
            thread = new Thread(() -> {
                run_compile_unit.stop_process();
                run_compile_unit.kill_process();
                start();
            });
            thread.start();
        });

        number_of_testCases.setEditable(false);
        jCheckBox.addActionListener(e -> {
            number_of_testCases.setEditable(jCheckBox.isSelected());
        });

        if(Competitive_Programming.hack_solution_last_data != null) {
            Hack_solution_last_data tmp = Competitive_Programming.hack_solution_last_data;
            ac_solution.setText(tmp.accepted_solution_file.trim());
            hack_solution.setText(tmp.hack_solution_file.trim());
            testCase_generator.setText(tmp.testCase_generator.trim());
            link.setText(tmp.url);
            timeLimit.setText(String.valueOf(tmp.timeLimit));
            number_of_testCases.setText(String.valueOf(tmp.number_of_testCases));
            jCheckBox.setSelected(tmp.jcheck_state);
        }

        setLocationRelativeTo(null);
        setVisible(true);
    }

    Run_Compile_Unit run_compile_unit = Competitive_Programming.competitiveProgramming.run_compile_unit;


    protected void setDataToObject() {
        Competitive_Programming.hack_solution_last_data = new Hack_solution.Hack_solution_last_data(ac_solution.getText().trim(), hack_solution.getText().trim(), testCase_generator.getText().trim(), link.getText().trim(), timeLimit.getText().trim(), number_of_testCases.getText().trim(), jCheckBox.isSelected());
    }

    String acceptedFile, hackFile, url, testCase_generator_file;
    double time_limit;
    int number_of_generated_testCases = 10;

    private void start() {
        status.setText("");
        acceptedFile = ac_solution.getText().trim();
        hackFile = hack_solution.getText().trim();
        url = link.getText().trim();
        testCase_generator_file = testCase_generator.getText().trim();
        time_limit = Double.valueOf(timeLimit.getText().trim());
        if(jCheckBox.isSelected())
            number_of_generated_testCases = Integer.valueOf(number_of_testCases.getText().trim());
        try {
            URL my_url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            status.setText("Please add valid url\n");
            return;
        }
        String path = Competitive_Programming.folderPath + "/TestCases/" + Problem_Input_Output.convert_URL_to_Name(url);
        if (jCheckBox.isSelected() || !new File(path).exists()) {
            System.out.println("Generating testCases...\n");
            add_to_status("Generating testCase...\n");
            if(!generate_testCases(path, number_of_generated_testCases)) {
                add_to_status("Ended\n");
                return;
            }
        }
        System.out.println("Test Cases Generated successfully.\n");
        status.setText("");
        try {
            add_to_status( "Start Testing\n");
            check_solution(path);
        } catch (InterruptedException interruptedException){
            interruptedException.printStackTrace();
        } catch (Exception e) {
            add_to_status("ERROR: " + e.getMessage()+"\n");
            e.printStackTrace();
        }
    }

    private void check_solution(String path) throws Exception {
        if(run_compile_unit.compile(Competitive_Programming.competitiveProgramming.comboBox1.getSelectedIndex(), hackFile, false) == 1) {
            add_to_status("Can't Compile the file " + hackFile + "\n");
            return;
        }
        run_compile_unit.thread1.join();

        int cnt = new File(path + "/input/").list().length;
        for(int i = 1; i <= cnt; i++) {
            add_to_status("Test Case " + i + ": ");
            AtomicReference<StringBuilder> error = new AtomicReference<>();
            int finalI = i;
            Thread thread = new Thread(() -> {
                try {
                    error.set(run_compile_unit.run_file_to_file(hackFile, Competitive_Programming.competitiveProgramming.comboBox1.getSelectedIndex(),
                            path + "/input/" + finalI + ".txt", Competitive_Programming.folderPath + "/src/TEMPS/" + finalI + ".txt"));
                } catch (Exception e) {
                    add_to_status("ERROR: " + e.getMessage()+"\n");
                    e.printStackTrace();
                    return;
                }
            });
            thread.start();
            int time = 0;
            while (thread != null && thread.isAlive()) {
                Automation.sleep(500);
                time++;
                if(time == 10) {
                    add_to_status("Time Limit Exceeded at test case: " + i + ".txt\n");
                    return;
                }
            }
            if(error.get() != null) {
                add_to_status("Can't run the hack file at test case: " + i + "\n" + error + "\n");
                return;
            }
            if(Run_Compile_Unit.timeUsed > time_limit) {
                add_to_status("Time Limit Exceeded at test case: " + i + ".txt\n");
                return;
            }
            if(!compareTextFiles(Competitive_Programming.folderPath + "/src/TEMPS/" + i + ".txt", path + "/output/" + i + ".txt")) {
                add_to_status("Wrong answer at test case: " + i + "\n");
                return;
            }
            add_to_status("Accepted\n");
        }
        add_to_status("\nAccepted\n");
    }

    protected boolean compareTextFiles (String file1, String file2) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(file1)), br2 = new BufferedReader(new FileReader(file2));
        while (br.ready() && br2.ready()) {
            if(!br.readLine().trim().equals(br2.readLine().trim())) return false;
        }
        if(br.ready() || br2.ready()) return false;
        return true;
    }

    protected boolean generate_testCases(String path, int number_of_generated_testCases) {
        progressBar.setValue(0);
        progressBar.setVisible(true);

        try {
            if (run_compile_unit.compile(Competitive_Programming.competitiveProgramming.comboBox1.getSelectedIndex(), testCase_generator_file, false) == 1) {
                add_to_status("Can't Compile the file " + testCase_generator_file + "\n");
                return false;
            }

            if(run_compile_unit.thread1 != null && run_compile_unit.thread1.isAlive()) run_compile_unit.thread1.join();

            progressBar.setValue(10);

            if (run_compile_unit.compile(comboBox1.getSelectedIndex(), acceptedFile, false) == 1) {
                add_to_status("Can't Compile the file " + acceptedFile + "\n");
                return false;
            }

            progressBar.setValue(20);

            if(run_compile_unit.thread1 != null && run_compile_unit.thread1.isAlive()) run_compile_unit.thread1.join();

            File file = new File(path);
            if(file.exists() && file.isDirectory())
                FileUtils.deleteDirectory(new File(path));
            file.mkdir();
            new File(path + "/input/").mkdir();
            new File(path + "/output/").mkdir();

            for (int i = 1; i <= number_of_generated_testCases; i++) {
                int value = (int)Math.ceil(80.0/number_of_generated_testCases) * i;

                StringBuilder error = run_compile_unit.run_file_to_file(testCase_generator_file, Competitive_Programming.competitiveProgramming.comboBox1.getSelectedIndex(),
                        "", path + "/input/" + i + ".txt");
                if(error != null) {
                    add_to_status("Can't generate test cases.\n" + error + "\n");
                    return false;
                }

                progressBar.setValue(value/50);

                error = run_compile_unit.run_file_to_file(acceptedFile, Competitive_Programming.competitiveProgramming.comboBox1.getSelectedIndex(),
                        path + "/input/" + i + ".txt", path + "/output/" + i + ".txt");

                if(error != null) {
                    add_to_status("Can't generate test cases.\n" + error + "\n");
                    return false;
                }

                progressBar.setValue(value);
            }
            progressBar.setVisible(false);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        } catch (Exception exception) {
            add_to_status("ERROR: " + exception.getMessage()+"\n");
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    private void add_to_status(String text) {
        status.setText(status.getText() + text);
    }

}
