//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.robotcore.util;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.IOException;
import java.io.InputStream;

public class RunShellCommand {
    boolean a = false;

    public RunShellCommand() {
    }

    public void enableLogging(boolean enable) {
        this.a = enable;
    }

    public String run(String cmd) {
        if(this.a) {
            RobotLog.v("running command: " + cmd);
        }

        String var2 = this.a(cmd, false);
        if(this.a) {
            RobotLog.v("         output: " + var2);
        }

        return var2;
    }

    public String runAsRoot(String cmd) {
        if(this.a) {
            RobotLog.v("running command: " + cmd);
        }

        String var2 = this.a(cmd, true);
        if(this.a) {
            RobotLog.v("         output: " + var2);
        }

        return var2;
    }

    private String a(String var1, boolean var2) {
        byte[] var3 = new byte[524288];
        boolean var4 = false;
        String var5 = "";
        ProcessBuilder var6 = new ProcessBuilder(new String[0]);
        Process var7 = null;

        try {
            if(var2) {
                var6.command(new String[]{"su", "-c", var1}).redirectErrorStream(true);
            } else {
                var6.command(new String[]{"sh", "-c", var1}).redirectErrorStream(true);
            }

            var7 = var6.start();
            var7.waitFor();
            InputStream var8 = var7.getInputStream();
            int var16 = var8.read(var3);
            if(var16 > 0) {
                var5 = new String(var3, 0, var16);
            }
        } catch (IOException var13) {
            RobotLog.logStacktrace(var13);
        } catch (InterruptedException var14) {
            var14.printStackTrace();
        } finally {
            if(var7 != null) {
                var7.destroy();
            }

        }

        return var5;
    }

    public static void killSpawnedProcess(String processName, String packageName, RunShellCommand shell) throws RobotCoreException {
        try {
            for(int var3 = getSpawnedProcessPid(processName, packageName, shell); var3 != -1; var3 = getSpawnedProcessPid(processName, packageName, shell)) {
                RobotLog.v("Killing PID " + var3);
                shell.run(String.format("kill %d", new Object[]{Integer.valueOf(var3)}));
            }

        } catch (Exception var4) {
            throw new RobotCoreException(String.format("Failed to kill %s instances started by this app", new Object[]{processName}));
        }
    }

    public static int getSpawnedProcessPid(String processName, String packageName, RunShellCommand shell) {
        String var3 = shell.run("ps");
        String var4 = "invalid";
        String[] var5 = var3.split("\n");
        int var6 = var5.length;

        int var7;
        String var8;
        String[] var9;
        for(var7 = 0; var7 < var6; ++var7) {
            var8 = var5[var7];
            if(var8.contains(packageName)) {
                var9 = var8.split("\\s+");
                var4 = var9[0];
                break;
            }
        }

        var5 = var3.split("\n");
        var6 = var5.length;

        for(var7 = 0; var7 < var6; ++var7) {
            var8 = var5[var7];
            if(var8.contains(processName) && var8.contains(var4)) {
                var9 = var8.split("\\s+");
                return Integer.parseInt(var9[1]);
            }
        }

        return -1;
    }
}
