/*
 * Decompiled with CFR 0.150.
 */
package com.company;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Run_Compile_Unit {
    protected Thread thread1, thread2, thread3 ,thread4;
    private Input_Output_Unit input_output_unit;
    protected static long timeUsed;
    private long memoryUsed;
    private long time;
    private long lastMemoryUsed;
    ProcessBuilder processBuilder;
    Process process;


    protected Run_Compile_Unit(Input_Output_Unit input_output_unit) {
        this.input_output_unit = input_output_unit;
    }

    ArrayList<Competitive_Programming.Pair> inputs_for_compile_and_run;

    protected void compile_and_run(ArrayList<Competitive_Programming.Pair> inputs, int language) throws Exception {
        stop_process();
        kill_process();
        thread4 = new Thread(() -> {
                inputs_for_compile_and_run = inputs;
            try {
                compile(language, Competitive_Programming.get_fileName(), true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread4.start();
    }

    protected int compile(int language, String fileName, boolean run_runner) throws Exception {
        stop_process();
        kill_process();
        Competitive_Programming.competitiveProgramming.setWaitCursor(true);
        input_output_unit.clearOutput();

        processBuilder = new ProcessBuilder();
        String command = "";
        if(language == 1) {  // Python
            //runner(inputs, language, outputName);
            return 0;
        } else if(language == 0) {  // C++
            if( System.getProperty("os.name").charAt(0) == 'W') {
                command = "g++ \"" +  Competitive_Programming.fix_local_path(Competitive_Programming.folderPath + "/CODE/C++/") + fileName + ".cpp\" -std=gnu++17 -o " + Competitive_Programming.fix_local_path("\"" + Competitive_Programming.folderPath + "/src/TEMPS/" + fileName + ".out" + "\"");
            } else {
                command = "g++ \"" + Competitive_Programming.folderPath + "/CODE/C++/" + fileName + ".cpp\" -std=gnu++17 -o \"" + Competitive_Programming.folderPath + "/src/TEMPS/"+ fileName + ".out\"";
            }
        } else if(language == 3) {  // Java
            if( System.getProperty("os.name").charAt(0) == 'W') {
                command = "javac -d . \"" + Competitive_Programming.fix_local_path(Competitive_Programming.folderPath + "/CODE/Java/" + fileName + ".java" ) + "\" -d \"" + Competitive_Programming.fix_local_path(Competitive_Programming.folderPath + "/src/TEMPS") + "\"";
            } else {
                command = "javac -d . \"" + Competitive_Programming.folderPath + "/CODE/Java/" + fileName + ".java\" -d \"" + Competitive_Programming.folderPath + "/src/TEMPS/\"";
            }
        }
        processBuilder.command(Competitive_Programming.osCommand, Competitive_Programming.osCH, command);
        process = processBuilder.start();
        BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        AtomicInteger errors = new AtomicInteger(0);
        thread1 = new Thread(() -> {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                String s = stdError.readLine();
                if (s != null) {
                    stringBuilder.append(s);
                    while ((s = stdError.readLine()) != null) {
                        stringBuilder.append("\n" + s);
                    }
                    input_output_unit.setErrorFontSize();
                    input_output_unit.sendError(stringBuilder.toString(), 0);
                    Competitive_Programming.competitiveProgramming.setWaitCursor(false);
                    errors.set(1);
                } else if(run_runner) {
                    runner(inputs_for_compile_and_run, language, fileName);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        Competitive_Programming.competitiveProgramming.setWaitCursor(false);
        return errors.get();
    }

    protected void runner(ArrayList<Competitive_Programming.Pair> inputs, int language, String fileName) throws Exception {
        String output[];
        for(Competitive_Programming.Pair pair : inputs) {
            output = run_string_to_string(pair.input, language, fileName);
            thread2.join();
            thread3.join();
            if(output[1] != null)  {
                input_output_unit.sendError(output[1], pair.number_of_testCase);
                continue;
            }
            if(output[0] != null) input_output_unit.sendOutput(output[0], pair.number_of_testCase, timeUsed, memoryUsed);
        }
    }

    protected StringBuilder run_file_to_file(String file_name, int language, String input_file, String output_file) throws Exception {
        StringBuilder error = null;
        Competitive_Programming.competitiveProgramming.setWaitCursor(true);
        processBuilder = new ProcessBuilder();
        String command = "";
        String input_fixed = (input_file.isEmpty()? "" : " < " + input_file);
        if(language == 0) {  // C++
            if( System.getProperty("os.name").charAt(0) == 'W') {
                command = "\"" + Competitive_Programming.fix_local_path(Competitive_Programming.folderPath + "/src/TEMPS/" + file_name + ".out") + "\"" + input_fixed + " > " + output_file;
            } else {
                command = "cd \"" + Competitive_Programming.folderPath + "/src/TEMPS/\" && ./" + file_name + ".out" + input_fixed + " > " + output_file;
            }
        } else if(language == 1) { // Python
            if( System.getProperty("os.name").charAt(0) == 'W') {
                command = "python \"" + Competitive_Programming.fix_local_path(Competitive_Programming.folderPath + "/CODE/Python/") + file_name + ".py\"" + input_fixed + " > " + output_file;
            } else {
                command = "python \"" + Competitive_Programming.folderPath + "/CODE/Python/" + file_name + ".py" + input_fixed + " > " + output_file;
            }
        } else if(language == 2) { // Python3
            if( System.getProperty("os.name").charAt(0) == 'W') {
                command = "python3 \"" + Competitive_Programming.fix_local_path(Competitive_Programming.folderPath + "/CODE/Python/") + file_name + ".py\"" + input_fixed + " > " + output_file;
            } else {
                command = "python3 \"" + Competitive_Programming.folderPath + "/CODE/Python/" + file_name + ".py" + input_fixed + " > " + output_file;
            }
        } else if(language == 3) { // Java
            if( System.getProperty("os.name").charAt(0) == 'W') {
                command = "java -classpath \"" + Competitive_Programming.fix_local_path(Competitive_Programming.folderPath + "/src/") + "TEMPS\" " + file_name + input_fixed + " > " + output_file;
            } else {
                command = "cd \"" + Competitive_Programming.folderPath + "/src/TEMPS/\" && java " + file_name + input_fixed + " > " + output_file;
            }
        }

        processBuilder.command(Competitive_Programming.osCommand, Competitive_Programming.osCH, command);

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        runtime.gc();

        time = System.currentTimeMillis();
        lastMemoryUsed = runtime.totalMemory() - runtime.freeMemory();

        process = processBuilder.start();

        timeUsed = System.currentTimeMillis() - time;
        memoryUsed = runtime.totalMemory() - runtime.freeMemory() - lastMemoryUsed;
        memoryUsed = Math.abs(memoryUsed);

        BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        try {
            StringBuilder stringBuilder = new StringBuilder();
            String s = stdError.readLine();
            if (s != null) {
                stringBuilder.append(s + "\n");
                while ((s = stdError.readLine()) != null) {
                    stringBuilder.append(s + "\n");
                }
                 error = stringBuilder;
                // input_output_unit.sendError(stringBuilder.toString(), numberOfTestCase);
            }
        }
        catch (Exception e) {
            Competitive_Programming.competitiveProgramming.setWaitCursor(false);
            e.printStackTrace();
        }

        return error;
    }


    protected String[] run_string_to_string(String input, int language, String fileName) throws Exception {
        String output[] = new String[2];
        Competitive_Programming.competitiveProgramming.setWaitCursor(true);
        processBuilder = new ProcessBuilder();
        String command = "";
        if(language == 0) {  // C++
            if( System.getProperty("os.name").charAt(0) == 'W') {
                command = "\"" + Competitive_Programming.fix_local_path(Competitive_Programming.folderPath + "/src/TEMPS/" + fileName + ".out") + "\"";
            } else {
                command = "cd \"" + Competitive_Programming.folderPath + "/src/TEMPS/\" && ./" + fileName + ".out";
            }
        } else if(language == 1) { // Python
            if( System.getProperty("os.name").charAt(0) == 'W') {
                command = "python \"" + Competitive_Programming.fix_local_path(Competitive_Programming.folderPath + "/CODE/Python/") + fileName + ".py\"";
            } else {
                command = "python \"" + Competitive_Programming.folderPath + "/CODE/Python/" + fileName + ".py\"";
            }
        } else if(language == 2) { // Python3
            if( System.getProperty("os.name").charAt(0) == 'W') {
                command = "python3 \"" + Competitive_Programming.fix_local_path(Competitive_Programming.folderPath + "/CODE/Python/") + fileName + ".py\"";
            } else {
                command = "python3 \"" + Competitive_Programming.folderPath + "/CODE/Python/" + fileName + ".py\"";
            }
        } else if(language == 3) { // Java
            if( System.getProperty("os.name").charAt(0) == 'W') {
                command = "java -classpath \"" + Competitive_Programming.fix_local_path(Competitive_Programming.folderPath + "/src/") + "TEMPS\" " + fileName;
            } else {
                command = "cd \"" + Competitive_Programming.folderPath + "/src/TEMPS/\" && java " + fileName;
            }
        }

        processBuilder.command(Competitive_Programming.osCommand, Competitive_Programming.osCH, command);

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        runtime.gc();

        time = System.currentTimeMillis();
        lastMemoryUsed = runtime.totalMemory() - runtime.freeMemory();

        process = processBuilder.start();

        BufferedOutputStream writer = new BufferedOutputStream(process.getOutputStream());
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        if (!input.isEmpty()) {
            writer.write((input + "\n \u0000 \u001a").getBytes());
            writer.flush();
        }

        thread2 = new Thread(() -> {
            try {
                String s;
                StringBuilder stringBuilder = new StringBuilder();
                while ((s = stdInput.readLine()) != null) {
                    stringBuilder.append(s + "\n");
                }
                timeUsed = System.currentTimeMillis() - time;

                memoryUsed = runtime.totalMemory() - runtime.freeMemory() - lastMemoryUsed;
                memoryUsed = Math.abs(memoryUsed);
                output[0] = stringBuilder.substring(0, stringBuilder.length()-1);
                thread3.start();
            }
            catch (Exception e) {
                if(thread3 != null && !thread3.isAlive())
                    thread3.start();
                e.printStackTrace();
            }
            Competitive_Programming.competitiveProgramming.setWaitCursor(false);
        });
        thread2.start();

        thread3 = new Thread(() -> {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                String s = stdError.readLine();
                if (s != null) {
                    stringBuilder.append(s + "\n");
                    while ((s = stdError.readLine()) != null) {
                        stringBuilder.append(s + "\n");
                    }
                    output[1] = stringBuilder.toString().trim();
                   // input_output_unit.sendError(stringBuilder.toString(), numberOfTestCase);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            Competitive_Programming.competitiveProgramming.setWaitCursor(false);
        });
        return output;
    }

    protected void stop_process() {
        if (thread1 != null && thread1.isAlive()) {
            thread1.interrupt();
        }
        if (thread2 != null && thread2.isAlive()) {
            thread2.interrupt();
        }
        if (thread3 != null && thread3.isAlive()) {
            thread3.interrupt();
        }
        if (thread4 != null && thread4.isAlive()) {
            thread4.interrupt();
        }
    }

    protected void kill_process() {
        try {
            if(process != null && process.isAlive()) {
                process.destroy();
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}

