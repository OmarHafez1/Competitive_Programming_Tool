
package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WindowsProcessKiller  {
    private static final String TASKLIST = "tasklist";
    private static final String KILL = "taskkill /IM ";
    
    public boolean isProcessRunning(final String serviceName) {
        try {
            final Process pro = Runtime.getRuntime().exec("tasklist");
            final BufferedReader reader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(serviceName)) {
                    return true;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
