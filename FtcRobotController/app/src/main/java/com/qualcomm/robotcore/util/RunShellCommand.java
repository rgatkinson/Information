package com.qualcomm.robotcore.util;

import com.qualcomm.robotcore.exception.RobotCoreException;
import java.io.IOException;

public class RunShellCommand
{
    boolean a;
    
    public RunShellCommand() {
        this.a = false;
    }
    
    private String a(final String s, final boolean b) {
        final byte[] array = new byte[524288];
        String s2 = "";
        final ProcessBuilder processBuilder = new ProcessBuilder(new String[0]);
        Process start = null;
        Label_0116: {
            if (!b) {
                break Label_0116;
            }
            try {
                processBuilder.command("su", "-c", s).redirectErrorStream(true);
                start = processBuilder.start();
                start.waitFor();
                final int read = start.getInputStream().read(array);
                if (read > 0) {
                    s2 = new String(array, 0, read);
                }
                return s2;
                processBuilder.command("sh", "-c", s).redirectErrorStream(true);
            }
            catch (IOException ex) {
                RobotLog.logStacktrace(ex);
                return s2;
            }
            catch (InterruptedException ex2) {
                ex2.printStackTrace();
                return s2;
            }
            finally {
                if (start != null) {
                    start.destroy();
                }
            }
        }
    }
    
    public static int getSpawnedProcessPid(final String s, final String s2, final RunShellCommand runShellCommand) {
        int i = 0;
        final String run = runShellCommand.run("ps");
        String s3 = "invalid";
        for (final String s4 : run.split("\n")) {
            if (s4.contains(s2)) {
                s3 = s4.split("\\s+")[0];
                break;
            }
        }
        for (String[] split2 = run.split("\n"); i < split2.length; ++i) {
            final String s5 = split2[i];
            if (s5.contains(s) && s5.contains(s3)) {
                return Integer.parseInt(s5.split("\\s+")[1]);
            }
        }
        return -1;
    }
    
    public static void killSpawnedProcess(final String s, final String s2, final RunShellCommand runShellCommand) throws RobotCoreException {
        try {
            for (int i = getSpawnedProcessPid(s, s2, runShellCommand); i != -1; i = getSpawnedProcessPid(s, s2, runShellCommand)) {
                RobotLog.v("Killing PID " + i);
                runShellCommand.run(String.format("kill %d", i));
            }
        }
        catch (Exception ex) {
            throw new RobotCoreException(String.format("Failed to kill %s instances started by this app", s));
        }
    }
    
    public void enableLogging(final boolean a) {
        this.a = a;
    }
    
    public String run(final String s) {
        if (this.a) {
            RobotLog.v("running command: " + s);
        }
        final String a = this.a(s, false);
        if (this.a) {
            RobotLog.v("         output: " + a);
        }
        return a;
    }
    
    public String runAsRoot(final String s) {
        if (this.a) {
            RobotLog.v("running command: " + s);
        }
        final String a = this.a(s, true);
        if (this.a) {
            RobotLog.v("         output: " + a);
        }
        return a;
    }
}
