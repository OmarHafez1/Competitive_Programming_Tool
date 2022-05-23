package com.company;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Input_Output_Unit {
    private JEditorPane jEditorPane;
    private JTextPane textPane;
    protected final byte FONT_SIZE = 22, ERROR_FONT_SIZE = 16;
    protected byte RESENT_INPUT_FONT_SIZE = FONT_SIZE, RESENT_OUTPUT_FONT_SIZE = FONT_SIZE;
    protected final String FONT_NAME = Competitive_Programming.FONT_NAME;
    protected final int FONT_STYLE = Font.PLAIN;
    private DefaultStyledDocument document;
    private StyleContext styleContext;
    protected ArrayList<JTextArea> inputTexts, answersTexts;
    // private final Font font = new Font(Competitive_Programming.FONT_NAME, Font.PLAIN, RESENT_FONT_SIZE);
    private int currentSize = 0;
    private JScrollPane input_JScrollPane, output_scrollPane;
    private Run_Compile_Unit run_compile_unit;
    protected Input_Output_Unit(JTextPane textPane, JEditorPane jEditorPane, JScrollPane input_JScrollPane, JScrollPane output_scrollPane) {
        this.jEditorPane = jEditorPane;
        this.output_scrollPane = output_scrollPane;
        this.textPane = textPane;
        this.input_JScrollPane = input_JScrollPane;
        inputTexts = new ArrayList<>();
        answersTexts = new ArrayList<>();
        colorWords();
        add_change_output_font_size_listener(jEditorPane);
    }

    protected void send_run_compile_object(Run_Compile_Unit run_compile_unit) {
        this.run_compile_unit = run_compile_unit;
    }

    protected void setOutput(String output) {
        setResentFontSize();
        jEditorPane.setText(output);
    }

    protected void addOutput(String output) {
        jEditorPane.setText(jEditorPane.getText() + output);
    }

    protected void sendOutput(String outputMessage, int numberOfInput, long timeUsed, long memoryUsed) {
        String res = "Accepted\n\n";
        answersTexts.get(numberOfInput-1).setText(answersTexts.get(numberOfInput-1).getText());
        String output_lines[] = outputMessage.trim().split("\\r?\\n");
        String answer_lines[] = answersTexts.get(numberOfInput-1).getText().split("\\r?\\n");
        if(answersTexts.get(numberOfInput-1).getText().trim().isEmpty())
            res = "";
        else if(output_lines.length != answer_lines.length) {
            res = "Wrong Answer\n\n";
        } else {
            for (int i = 0; i < output_lines.length && i < answer_lines.length; i++) {
                if (!output_lines[i].trim().equals(answer_lines[i].trim())) {
                    res = "Wrong Answer\n\n";
                    break;
                }
            }
        }
        double memory = Math.round((memoryUsed / 1000.0) * 100.0) / 100.0;
        jEditorPane.setText(jEditorPane.getText() + "Test case " + numberOfInput + ":  >>" + timeUsed + "mS" +
                "  >>" + ((memory >= 1000.0)? (Math.round(memory/10.0)/100.0 + "MB\n") : (memory + "KB\n")) + outputMessage + "\n" + res);
        jEditorPane.setCaretPosition(0);
    }

    protected void sendError(String errorMessage, int numberOfInput) {
        setErrorFontSize();
        if (numberOfInput <= 0)
            jEditorPane.setText(jEditorPane.getText() + errorMessage + "\n");
        else
            jEditorPane.setText(jEditorPane.getText() + "Test case " + numberOfInput + ":\n" + errorMessage + "\n");
        jEditorPane.setCaretPosition(0);
    }

    protected void clearInput() {
        JLabel nextLabel = getLabel("Next test case >", null);
        textPane.insertComponent(nextLabel);
        MyMouseListener myMouseListener = new MyMouseListener();
        nextLabel.addMouseListener(myMouseListener);
    }

    protected void clearOutput() {
        jEditorPane.setText("");
        setResentFontSize();
    }

    private JLabel getLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(10000, 30));
        if(color == null)
            label.setBackground(new Color(0, 191, 3));
        else
            label.setBackground(color);
        label.setForeground(new Color(255, 255, 255));
        label.setFont(new Font(Competitive_Programming.FONT_NAME, Font.BOLD, 21));
        return label;
    }

    private JTextArea getTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setBackground(new Color(82, 82, 82));
        textArea.setForeground(new Color(255, 255, 255));
        textArea.setCaretColor(Color.WHITE);
        textArea.setFont(new Font("YaHei Consolas Hybrid", Font.PLAIN, FONT_SIZE-2));
        return textArea;
    }

    private void setResentFontSize() {
        jEditorPane.setFont(new Font("YaHei Consolas Hybrid", FONT_STYLE, RESENT_OUTPUT_FONT_SIZE));
    }

    protected void setErrorFontSize() {
        jEditorPane.setFont(new Font(FONT_NAME, FONT_STYLE, ERROR_FONT_SIZE));
    }

    private void colorWords() {
        styleContext = StyleContext.getDefaultStyleContext();
        document = new DefaultStyledDocument() {
            public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);
                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offset);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offset + str.length());
                int wordL = before;
                int wordR = before;

                String textSubstring;
                while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\s")) {
                        textSubstring = text.substring(wordL, wordR);
                        setAttriputes(textSubstring, wordL, wordR);
                        setUnderLinedWordAttripute(textSubstring, wordL, text);
                        wordL = wordR;
                    }
                    wordR++;
                }

                int start = 0, end = 0;
                String lines[] = text.split("\\r?\\n");
                for (String txt : lines) {
                    end += txt.length();
                    if(txt.matches("(Test case )[0-9]*(:( )*>>)[0-9]*(m)?(S( )*>>)[0-9]*(\\.)[0-9]*((K|M)?B)")) {
                        document.setCharacterAttributes(start,  13, getAttribute(styleContext, (new Color(4, 226, 0))), false);
                        document.setCharacterAttributes(start + 14, txt.length() - 14, getAttribute(styleContext, (new Color(57, 194, 255))), false);
                    } else {
                        setLineAttripute(txt, start, end);
                    }
                    end++;
                    start = end;
                }
                /*
                String lines[] = text.split("\\r?\\n");
                int start = 0, end = 0;
                for(String txt : lines) {
                    start = end-1;
                    end += txt.length() + 1;
                    if(special_lines(txt))
                        setLineAttripute(txt, start, end);
                }*/
            }

            public void remove(int offs, int len) throws BadLocationException {
                super.remove(offs, len);
                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offs);
                setAttriputes(text.substring(before, after), before, after);
            }
        };
        jEditorPane.setDocument(document);
        jEditorPane.setCaretPosition(0);
    }

    private int findLastNonWordChar(String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    private int findFirstNonWordChar(String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }

    private boolean special_lines(String text) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("(Wrong )(answer)*(Answer)*( on test)*( on pretest)*( )*[0-9]*");
        arrayList.add("(Time limit exceeded)( on test)*( on pretest)*( )*[0-9]*");
        arrayList.add("(Runtime error)( on test)*( on pretest)*( )*[0-9]*");
        arrayList.add("(Idleness limit exceeded)( on test)*( on pretest)*( )*[0-9]*");
        arrayList.add("(Idleness limit exceeded)( on test)*( on pretest)*( )*[0-9]*");
        arrayList.add("(Memory limit exceeded)( on test)*( on pretest)*( )*[0-9]*");
        arrayList.add("(Denial of judgement)( on test)*( on pretest)*( )*[0-9]*");
        arrayList.add("(Compilation error)");
        for(String str : arrayList) {
            if(text.trim().matches(str)) return true;
        }
        return false;
    }

    private void setAttriputes(String text, int wordL, int wordR) {
        if (text.matches("(\\W)*(((?i)(error):?)|(~+)|(\\^+))")) {
            document.setCharacterAttributes(wordL, wordR - wordL, getAttribute(styleContext, (new Color(255, 32, 0))), false);
        } else if (text.matches("(\\W)*((.*)((\\.java:?)|(\\.cpp:?))([0-9]?)+(:?)([0-9]?)+(:?))") || text.matches("(\\W)*(WJ)")) {
            document.setCharacterAttributes(wordL, wordR - wordL, getAttribute(styleContext, (new Color(255, 234, 0))), false);
        } else if (text.matches("(\\W)*((.*(((‘|\").*(’|\"))|'|\").*)|((?i)(note):?))")) {
            document.setCharacterAttributes(wordL, wordR - wordL, getAttribute(styleContext, (new Color(0, 234, 255))), false);
        } else if (text.matches("(\\W)*(([0-9][0-9]:[0-9][0-9]:[0-9][0-9])|([0-9][0-9][0-9][0-9]/[0-9][0-9]/[0-9][0-9]))")) {
            document.setCharacterAttributes(wordL, wordR - wordL, getAttribute(styleContext, (new Color(0, 182, 255))), false);
        } else if (text.matches("(\\W)*(Accepted)")) {
            try {
                document.setCharacterAttributes(wordL, wordR - wordL, getAttribute(styleContext, (new Color(229, 255, 234))), false);
                javax.swing.text.DefaultHighlighter.DefaultHighlightPainter highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(new Color(0, 187, 35));
                jEditorPane.getHighlighter().addHighlight(wordL+1, wordR, highlightPainter);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            document.setCharacterAttributes(wordL, wordR - wordL, getAttribute(styleContext, (new Color(255, 255, 255))), false);
        }
    }

    private void setUnderLinedWordAttripute(String textSubstring, int wordL, String text) {
        if (textSubstring.matches("(\\W)*((~+)|(\\^+))")) {
            int i = wordL;
            while (text.charAt(i) != '\n') {
                i--;
            }
            int x = i - 1;
            i = wordL - i;
            while (text.charAt(x) != '\n') {
                x--;
            }
            document.setCharacterAttributes(x + i, textSubstring.length(), getAttribute(styleContext, (new Color(255, 0, 17))), false);
        }
    }

    protected void setLineAttripute(String text, int start, int end) {
        try {
            if(special_lines(text)) {
                // document.setCharacterAttributes(start, end - start, getAttribute(styleContext, (new Color(255, 255, 255))), false);
                // jEditorPane.getHighlighter().removeAllHighlights();
                document.setCharacterAttributes(start, end - start+1, getAttribute(styleContext, (new Color(255, 228, 228))), false);
                javax.swing.text.DefaultHighlighter.DefaultHighlightPainter highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(new Color(123, 0, 0));
                jEditorPane.getHighlighter().addHighlight(start, end, highlightPainter);
            } else if(text.contains("Happy New Year!") || text.contains("Pretests passed")) {
                try {
                    document.setCharacterAttributes(start, end - start+1, getAttribute(styleContext, (new Color(229, 255, 234))), false);
                    javax.swing.text.DefaultHighlighter.DefaultHighlightPainter highlightPainter = new javax.swing.text.DefaultHighlighter.DefaultHighlightPainter(new Color(0, 187, 35));
                    jEditorPane.getHighlighter().addHighlight(start, end, highlightPainter);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private AttributeSet getAttribute(StyleContext styleContext, Color color) {
        return styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, color);
    }

    protected void add_new_testCase() {
        textPane.setCaretPosition(textPane.getText().length()-1);
        JTextArea textArea = getTextArea(), textArea1 = getTextArea();
        JLabel label = getLabel("Test case " + (currentSize+1) + ":\n", null), label1 = getLabel("Answer " + (currentSize+1) + ": \n" , new Color(39,48,184));
        inputTexts.add(textArea);
        answersTexts.add(textArea1);
        currentSize = inputTexts.size();
        try {
            textPane.insertComponent(label);
            textPane.insertComponent(textArea);
            textPane.insertComponent(label1);
            textPane.insertComponent(textArea1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        /*
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3) { // mean the user clicked the mouse right click
                    textArea.paste();
                    textArea.setText(textArea.getText().trim());
                }
            }
        });
        textArea1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3) { // mean the user clicked the mouse right click
                    textArea1.paste();
                    textArea1.setText(textArea1.getText().trim());
                }
            }
        });
        */
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                compile_run(textArea, label);
            }
        });
        textPane.setCaretPosition(0);
        enable_undo_redo(textArea);
        enable_undo_redo(textArea1);
        add_change_input_font_size_listener(textArea);
        add_change_input_font_size_listener(textArea1);
        add_change_input_font_size_listener(label);
    }


    protected void compile_run(JTextArea textArea, JLabel label) {
        Runtime.getRuntime().gc();
        try {
            if(!textArea.getText().trim().isEmpty()) {
                ArrayList<Competitive_Programming.Pair> arrayList = new ArrayList<>();
                arrayList.add(new Competitive_Programming.Pair(textArea.getText(), getNumberOfTestCase(label.getText())));
                run_compile_unit.compile_and_run(arrayList, Competitive_Programming.competitiveProgramming.comboBox1.getSelectedIndex());
            }
        } catch (Exception e) {
            if (e.getMessage().equals("Broken pipe"))
                compile_run(textArea, label);
            else e.printStackTrace();
        }
    }

    private class MyMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            for(int i = 0; i < 10; i++) {
                add_new_testCase();
            }
        }
    }

    private void enable_undo_redo(JTextArea textArea) {
        final UndoManager undo = new UndoManager();
        Document doc = textArea.getDocument();

        // Listen for undo and redo events
        doc.addUndoableEditListener(evt -> undo.addEdit(evt.getEdit()));

        // Create an undo action and add it to the text component
        textArea.getActionMap().put("Undo", new AbstractAction("Undo") {
            public void actionPerformed(ActionEvent evt) {
                if (undo.canUndo())
                    undo.undo();
            }
        });

        // Bind the undo action to ctl-Z
        textArea.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");

        // Create a redo action and add it to the text component
        textArea.getActionMap().put("Redo",
                new AbstractAction("Redo") {
                    public void actionPerformed(ActionEvent evt) {
                        if (undo.canRedo())
                            undo.redo();
                    }
                });

        // Bind the redo action to ctl-shift-Z
        textArea.getInputMap().put(KeyStroke.getKeyStroke("control shift Z"), "Redo");
    }

    private int getNumberOfTestCase(String str) {
        str = str.substring(str.length()-4, str.length()-2);
        str = str.trim();
        return Integer.valueOf(str);
    }

    private void add_change_input_font_size_listener(JComponent jComponent) {
        jComponent.addMouseWheelListener(e -> {
            if (e.isControlDown()) {
                if (e.getWheelRotation() < 0) {
                    change_input_fontsSize(1);
                } else {
                    change_input_fontsSize(-1);
                }
            } else {
                input_JScrollPane.dispatchEvent(e);
            }
        });
    }

    private void add_change_output_font_size_listener(JComponent jComponent) {
        jComponent.addMouseWheelListener(e -> {
            if (e.isControlDown()) {
                if (e.getWheelRotation() < 0) {
                    change_output_fontsSize(1);
                } else {
                    change_output_fontsSize(-1);
                }
            } else {
                // pass the event on to the scroll pane
                output_scrollPane.dispatchEvent(e);
            }
        });
    }

    protected void change_input_fontsSize(int value) {
        if(RESENT_INPUT_FONT_SIZE + value < 14 || RESENT_INPUT_FONT_SIZE + value > 50) return;
        RESENT_INPUT_FONT_SIZE += value;
        for(JTextArea textArea : inputTexts) textArea.setFont(new Font(textArea.getFont().getName(), textArea.getFont().getStyle(), RESENT_INPUT_FONT_SIZE));
        for(JTextArea textArea : answersTexts) textArea.setFont(new Font(textArea.getFont().getName(), textArea.getFont().getStyle(), RESENT_INPUT_FONT_SIZE));
    }

    protected void change_output_fontsSize(int value) {
        if(RESENT_OUTPUT_FONT_SIZE + value < 14 || RESENT_OUTPUT_FONT_SIZE + value > 50) return;
        RESENT_OUTPUT_FONT_SIZE += value;
        jEditorPane.setFont(new Font(jEditorPane.getFont().getName(), jEditorPane.getFont().getStyle(), RESENT_OUTPUT_FONT_SIZE));
    }

}