package com.company;

import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Competitive_Programming extends JFrame {
    private JPanel jpanel;
    private JTextPane textPane;
    protected JTextField textField;
    private JEditorPane jEditorPane;
    private JButton runButton;
    protected JComboBox comboBox1;
    protected JScrollPane input_JScrollPane;
    private JScrollPane output_scrollPane;
    private int[] minmum_window_size = {430, 915};
    private byte lastSelectedIndex = -1;
    protected static String folderPath = "";
    protected static boolean problem_input_output_ISOPEN = false, hack_solution_ISOPEN = false;
    protected Problem_Input_Output problem_input_output;
    protected Hack_solution hack_solution;
    protected static Hack_solution.Hack_solution_last_data hack_solution_last_data;
    protected static final String FONT_NAME = "Lato";

    protected static String osCommand = System.getProperty("os.name").charAt(0) == 'W' ? "cmd.exe" : "/bin/bash";
    protected static String osCH = System.getProperty("os.name").charAt(0) == 'W' ? "/C" : "-c";

    protected String problem_url = "";

    protected static Map<String, String[]> username_pass = new HashMap();

    protected static Competitive_Programming competitiveProgramming = new Competitive_Programming("Competitive Programming");
    protected static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    protected Input_Output_Unit input_output_unit = new Input_Output_Unit(textPane, jEditorPane, input_JScrollPane, output_scrollPane);
    protected Run_Compile_Unit run_compile_unit = new Run_Compile_Unit(input_output_unit);
    protected Automation automation = new Automation();
    protected Atcoder atcoder = new Atcoder();
    protected Codeforces codeforces = new Codeforces();
    protected Hakerearth hakerearth = new Hakerearth();

    private Competitive_Programming(String title) {
        super(title);
        try {
            String tmp = Competitive_Programming.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString();
            int last = tmp.length()-1;
            while(tmp.charAt(last) != '/') last--;
            if(System.getProperty("os.name").charAt(0) == 'W') {
                tmp = tmp.substring(6, last);
            } else {
                tmp = tmp.substring(5, last);
            }
            tmp = URLDecoder.decode(tmp, StandardCharsets.US_ASCII.name());
            folderPath = tmp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(jpanel);
        this.pack();
        input_output_unit.send_run_compile_object(run_compile_unit);
        automation.setter(competitiveProgramming, problem_input_output, input_output_unit);
        codeforces.setter(automation);
        atcoder.setter(automation);
        hakerearth.setter(automation);
        comboBox1.setBorder(null);
        textPane.setFocusable(true);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                run_compile_unit.stop_process();
                run_compile_unit.kill_process();
                automation.killProcess();
            }
        }));
        listeners();
    }

    public static void main(String[] args) {
        try {
            String path =  URLDecoder.decode(Competitive_Programming.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString()).trim().substring(5);
            int right = path.length()-1;
            if(path.contains("jar")) {
                while(path.charAt(right--) != '/');
            }
            path = path.substring(0, right+1);
            path += "/logs.txt";
            File myObj = new File(path);
            myObj.createNewFile();
            PrintStream out = new PrintStream(new FileOutputStream(path));
            System.setOut(out);
            System.setErr(out);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println("Started in " + dtf.format(now));
        } catch (Exception e) {
            e.printStackTrace();
        }
        check_login();
        competitiveProgramming.minmum_window_size[1] = (int)screenSize.getHeight() - 150;
        competitiveProgramming.setDefaultWindowSize();
        competitiveProgramming.setAlwaysOnTop(true);
        competitiveProgramming.setMinimumSize(new Dimension(competitiveProgramming.minmum_window_size[0], competitiveProgramming.minmum_window_size[1]));
        competitiveProgramming.setVisible(true);
    }

    public static String fix_local_path(String path) {
        if(System.getProperty("os.name").charAt(0) == 'W')
            return FilenameUtils.separatorsToSystem(path).trim();
        return path.trim();
    }

    private static void open_login() {
        Login login = new Login();
        login.login();
        login.setLocationRelativeTo(null);
        login.setVisible(true);
    }

    public static void check_login() {
        File file1 = new File(folderPath + "/src/logins/cf.txt");
        File file2 = new File(folderPath + "/src/logins/cff.txt");
        File file3 = new File(folderPath + "/src/logins/ac.txt");
        File file4 = new File(folderPath + "/src/logins/acc.txt");
        if(!file1.exists() || !file2.exists() || !file3.exists() || !file4.exists()) {
            Login login = new Login();
            login.login();
            login.setLocationRelativeTo(null);
            login.setVisible(true);
            while (login.isVisible()) {
                competitiveProgramming.automation.sleep(500);
            }
        }
        load_login();
    }

    public static void load_login() {
        try {
            String cf = competitiveProgramming.readFile(folderPath + "/src/logins/cf.txt");
            String cff = competitiveProgramming.readFile(folderPath + "/src/logins/cff.txt");
            String ac = competitiveProgramming.readFile(folderPath + "/src/logins/ac.txt");
            String acc = competitiveProgramming.readFile(folderPath + "/src/logins/acc.txt");

            String cf_decoded = AES.decrypt(cf), cff_decoded = AES.decrypt(cff);
            String ac_decoded = AES.decrypt(ac), acc_decoded = AES.decrypt(acc);
            username_pass.put("codeforces", new String[]{cf_decoded, cff_decoded});
            username_pass.put("atcoder", new String[]{ac_decoded, acc_decoded});

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    protected static String readFile(String path) {
        try {
            File file = new File(path);
            StringBuilder stringBuilder = new StringBuilder("");
            if(file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                while (br.ready()) {
                    stringBuilder.append(br.readLine()+"\n");
                }
                return stringBuilder.toString().trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void listeners() {
        try {
            input_output_unit.clearInput();

            runButton.addActionListener(actionEvent -> compile_run());

            jpanel.getActionMap().put("runIt", new AbstractAction("runIt") {
                public void actionPerformed(ActionEvent actionEvent) {
                    runButton.doClick();
                }
            });
            jpanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F5"), "runIt");

            jpanel.getActionMap().put("clear_output", new AbstractAction("clear_output") {
                public void actionPerformed(ActionEvent actionEvent) {
                    input_output_unit.clearOutput();
                }
            });
            jpanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("alt O"), "clear_output");

            jpanel.getActionMap().put("test", new AbstractAction("test") {
                public void actionPerformed(ActionEvent actionEvent) {
                    input_output_unit.add_new_testCase();
                }
            });
            jpanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control N"), "test");

            jpanel.getActionMap().put("reset", new AbstractAction("reset") {
                public void actionPerformed(ActionEvent actionEvent) {
                    reset_app();
                }
            });
            jpanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control R"), "reset");

            jpanel.getActionMap().put("stop_process", new AbstractAction("stop_process") {
                public void actionPerformed(ActionEvent actionEvent) {
                    run_compile_unit.stop_process();
                    run_compile_unit.kill_process();
                }
            });
            jpanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control B"), "stop_process");

            jpanel.getActionMap().put("reset_Driver", new AbstractAction("reset_Driver") {
                public void actionPerformed(ActionEvent actionEvent) {
                    automation.reset_driver();
                }
            });
            jpanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control D"), "reset_Driver");

            jpanel.getActionMap().put("restart", new AbstractAction("restart") {
                public void actionPerformed(ActionEvent actionEvent) {
                    try {
                        restartApplication();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            jpanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control shift R"), "restart");

            comboBox1.addActionListener(e -> {
                if(comboBox1.getSelectedIndex() == comboBox1.getItemCount()-1) {
                    comboBox1.setSelectedIndex((lastSelectedIndex == -1)? 0 : lastSelectedIndex);
                    open_login();
                }
                else {
                    lastSelectedIndex = (byte)comboBox1.getSelectedIndex();
                }
            });
        } catch (Exception e) {
            input_output_unit.sendError("\n" + e.toString(), -1);
            e.printStackTrace();
        }

        jpanel.getActionMap().put("automation", new AbstractAction("automation") {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    if (!competitiveProgramming.problem_input_output_ISOPEN) {
                        problem_input_output = new Problem_Input_Output("Problem setter");
                        automation.setter(competitiveProgramming, problem_input_output, input_output_unit);
                        problem_input_output.setter(competitiveProgramming, textPane);
                        problem_input_output.setSize(690, 360);
                        problem_input_output.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosing(WindowEvent e) {
                                problem_url = problem_input_output.get_problem_url();
                                problem_input_output_ISOPEN = false;
                            }
                        });
                        if(problem_url.equals("")) {
                            problem_input_output.paste_problem_url();
                            problem_url = problem_input_output.get_problem_url();
                        } else {
                            problem_input_output.set_problem_url(problem_url);
                        }
                        problem_input_output.setLocationRelativeTo(null);
                        problem_input_output.setResizable(false);
                        problem_input_output.setVisible(true);
                        problem_input_output_ISOPEN = true;
                    } else {
                        problem_input_output.setVisible(false);
                        problem_input_output.setVisible(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        jpanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"), "automation");

        // I didn't test this enough so i am commented this now

        jpanel.getActionMap().put("hack", new AbstractAction("hack") {
            public void actionPerformed(ActionEvent actionEvent) {
                if(!hack_solution_ISOPEN) {
                    hack_solution = new Hack_solution();
                    hack_solution.run();
                    hack_solution.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            Competitive_Programming.hack_solution_ISOPEN = false;
                            hack_solution.setDataToObject();
                        }
                    });
                    hack_solution_ISOPEN = true;
                } else {
                    hack_solution.setVisible(false);
                    hack_solution.setVisible(true);
                }
            }
        });
        jpanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F8"), "hack");

        jpanel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.isControlDown()) {
                    if (e.getWheelRotation() < 0) {
                        input_output_unit.change_input_fontsSize(1);
                        input_output_unit.change_output_fontsSize(1);
                    } else {
                        input_output_unit.change_input_fontsSize(-1);
                        input_output_unit.change_output_fontsSize(-1);
                    }
                }
            }
        });
    }

    protected void reset_app() {
        this.run_compile_unit.stop_process();
        this.run_compile_unit.kill_process();
        this.input_output_unit.clearOutput();
        for (JTextArea textArea : this.input_output_unit.inputTexts) {
            if (textArea == null) continue;
            textArea.setText("");
        }
        for (JTextArea textArea : this.input_output_unit.answersTexts) {
            if (textArea == null) continue;
            textArea.setText("");
        }
    }

    protected void restartApplication() throws Exception {
        automation.killProcess();
        new ProcessBuilder().command(osCommand, osCH, "java -jar \"" + fix_local_path(fix_local_path(folderPath + "/Competitive_Programming.jar\""))).start();
        System.exit(0);
    }

    public static class Pair {
        String input;
        int number_of_testCase;
        public Pair(String input, int number_of_testCase) {
            this.input = input;
            this.number_of_testCase = number_of_testCase;
        }
    }

    public static void exit() {
        System.exit(0);
    }

    private void compile_run() {
        Runtime.getRuntime().gc();
        try {
            ArrayList<Pair> inputs = new ArrayList<>();
            int numper_of_testCase = 1;
            for(JTextArea input : input_output_unit.inputTexts) {
                if(input != null && !input.getText().trim().isEmpty()) {
                    inputs.add(new Pair(input.getText(), numper_of_testCase));
                    numper_of_testCase++;
                }
            }
            run_compile_unit.compile_and_run(inputs, comboBox1.getSelectedIndex());
        } catch (Exception e) {
            if (e.getMessage().equals("Broken pipe"))
                compile_run();
        }
    }

    protected static String get_fileName() {
        return competitiveProgramming.textField.getText().trim();
    }

    private void setDefaultWindowSize() {
        competitiveProgramming.setSize(minmum_window_size[0], minmum_window_size[1]);
        int width = (int) screenSize.getWidth();
        competitiveProgramming.setLocation(width - competitiveProgramming.getWidth() - 12, 56);
    }

    protected void setWaitCursor(boolean state) {
        if(state) competitiveProgramming.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        else competitiveProgramming.setCursor(Cursor.getDefaultCursor());
    }
}