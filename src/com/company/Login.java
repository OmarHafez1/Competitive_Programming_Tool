package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Login extends JFrame {
    JLabel label[] = new JLabel[6];
    JTextField textField[] = new JTextField[2];
    JPasswordField passwordField[] = new JPasswordField[2];
    JButton button;

    protected void login() {
        setTitle("Store Username and Passwords to login");
        setResizable(false);
        setSize(600, 525);
        setLayout(null);
        label[0] = new JLabel("Codeforces");
        label[0].setForeground(Color.blue);
        label[0].setFont(new Font(Competitive_Programming.FONT_NAME, Font.BOLD, 36));
        label[1] = new JLabel("Enter Email/Username:");
        label[2] = new JLabel("Enter Password:");
        label[1].setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 20));
        label[2].setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 20));
        textField[0] = new JTextField();
        textField[0].setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 20));
        passwordField[0] = new JPasswordField();
        passwordField[0].setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 20));
        label[0].setBounds(187, 25, 400, 30);
        label[1].setBounds(50, 80, 250, 30);
        label[2].setBounds(50, 120, 250, 30);
        textField[0].setBounds(300, 80, 250, 30);
        passwordField[0].setBounds(300, 120, 250, 30);

        label[3] = new JLabel("Atcoder");
        label[3].setForeground(Color.blue);
        label[3].setFont(new Font(Competitive_Programming.FONT_NAME, Font.BOLD, 36));
        label[4] = new JLabel("Enter Username:");
        label[5] = new JLabel("Enter Password:");
        label[4].setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 20));
        label[5].setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 20));
        textField[1] = new JTextField();
        textField[1].setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 20));
        passwordField[1] = new JPasswordField();
        passwordField[1].setFont(new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, 20));
        label[3].setBounds(218, 220, 400, 30);
        label[4].setBounds(60, 275, 250, 30);
        label[5].setBounds(60, 315, 250, 30);
        textField[1].setBounds(300, 275, 250, 30);
        passwordField[1].setBounds(300, 315, 250, 30);

        button = new JButton("Save");
        button.setForeground(Color.white);
        button.setFont(new Font(Competitive_Programming.FONT_NAME, Font.BOLD, 30));
        button.setBackground(new Color(39,48,184));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setBounds(225, 420, 120, 40);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                store();
            }
        } );

        for(JLabel label : label) {
            add(label);
        }
        for(JTextField textField : textField) {
            add(textField);
        }
        for(JPasswordField passwordField : passwordField) {
            add(passwordField);
        }


        add(button);
    }

    private void store() {
        try {
            String codeforces_username = textField[0].getText();
            String codeforces_password = new String(passwordField[0].getPassword());
            String atcoder_username = textField[1].getText();
            String atcoder_password = new String(passwordField[1].getPassword());

            String codeforces_username_encrypted = AES.encrypt(codeforces_username);
            String codeforces_password_encrypted = AES.encrypt(codeforces_password);
            String atcoder_username_encrypted = AES.encrypt(atcoder_username);
            String atcoder_password_encrypted = AES.encrypt(atcoder_password);

            if(!codeforces_username.trim().isEmpty() && !codeforces_password.trim().isEmpty()) {
                writeToFile(Competitive_Programming.fix_local_path(Competitive_Programming.folderPath + "/src/logins/cf.txt"), codeforces_username_encrypted);
                writeToFile(Competitive_Programming.fix_local_path(Competitive_Programming.folderPath + "/src/logins/cff.txt"), codeforces_password_encrypted);
            }
            if(!atcoder_username.trim().isEmpty() && !atcoder_password.trim().isEmpty()) {
                writeToFile(Competitive_Programming.fix_local_path(Competitive_Programming.folderPath + "/src/logins/ac.txt"), atcoder_username_encrypted);
                writeToFile(Competitive_Programming.fix_local_path(Competitive_Programming.folderPath + "/src/logins/acc.txt"), atcoder_password_encrypted);
            }

            dispose();
        } catch (Exception exception) {
            exception.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occured", "Can't encrypt and save your usernames and passwords", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void writeToFile(String path, String txt) {
        try {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(path), "utf-8"))) {
                writer.write(txt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
