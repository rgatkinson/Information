package com.qualcomm.robotcore.util;

import com.qualcomm.robotcore.exception.RobotCoreException;

import java.io.IOException;
import java.io.InputStream;

public class RunShellCommand {
    boolean a = false;

    public static void killSpawnedProcess(String processName, String packageName, RunShellCommand shell) throws RobotCoreException {
        try {
            for (int var3 = getSpawnedProcessPid(processName, packageName, shell); var3 != -1; var3 = getSpawnedProcessPid(processName, packageName, shell)) {
                RobotLog.v("Killing PID " + var3);
                shell.run(String.format("kill %d", Integer.valueOf(var3)));
            }

        } catch (Exception var4) {
            throw new RobotCoreException(String.format("Failed to kill %s instances started by this app", processName));
        }
    }

    public static int getSpawnedProcessPid(String var0, String var1, RunShellCommand var2) {
        int var3 = 0;
        String var4 = var2.run("ps");
        String var5 = "invalid";
        String[] var6 = var4.split("\n");
        int var7 = var6.length;

        for (int var8 = 0; var8 < var7; ++var8) {
            String var12 = var6[var8];
            if (var12.contains(var1)) {
                var5 = var12.split("\\s+")[0];
                break;
            }
        }

        String[] var9 = var4.split("\n");

        for (int var10 = var9.length; var3 < var10; ++var3) {
            String var11 = var9[var3];
            if (var11.contains(var0) && var11.contains(var5)) {
                return Integer.parseInt(var11.split("\\s+")[1]);
            }
        }

        return -1;
    }

    private String a(String var1, boolean var2) {
        byte[] var3 = new byte[524288];
        boolean var4 = false;
        String var5 = "";
        ProcessBuilder var6 = new ProcessBuilder();
        Process var7 = null;

        try {
            if (var2) {
                var6.command("su", "-c", var1).redirectErrorStream(true);
            } else {
                var6.command("sh", "-c", var1).redirectErrorStream(true);
            }

            var7 = var6.start();
            var7.waitFor();
            InputStream var8 = var7.getInputStream();
            int var16 = var8.read(var3);
            if (var16 > 0) {
                var5 = new String(var3, 0, var16);
            }
        } catch (IOException var13) {
            RobotLog.logStacktrace(var13);
        } catch (InterruptedException var14) {
            var14.printStackTrace();
        } finally {
            if (var7 != null) {
                var7.destroy();
            }

        }

        return var5;
    }

    private String a(String var1, boolean var2) {
        byte[] var3 = new byte[524288];
        boolean var4 = false;
        String var5 = "";
        ProcessBuilder var6 = new ProcessBuilder();
        Process var7 = null;

        try {
            if (var2) {
                var6.command("su", "-c", var1).redirectErrorStream(true);
            } else {
                var6.command("sh", "-c", var1).redirectErrorStream(true);
            }

            var7 = var6.start();
            var7.waitFor();
            InputStream var8 = var7.getInputStream();
            int var16 = var8.read(var3);
            if (var16 > 0) {
                var5 = new String(var3, 0, var16);
            }
        } catch (IOException var13) {
            RobotLog.logStacktrace(var13);
        } catch (InterruptedException var14) {
            var14.printStackTrace();
        } finally {
            if (var7 != null) {
                var7.destroy();
            }

        }

        return var5;
    }

    public void enableLogging(boolean var1) {
        this.a = var1;
    }

    public String run(String var1) {
        if (this.a) {
            RobotLog.v("running command: " + var1);
        }

        String var2 = this.a(var1, false);
        if (this.a) {
            RobotLog.v("         output: " + var2);
        }

        return var2;
    }

    public String runAsRoot(String var1) {
        if (this.a) {
            RobotLog.v("running command: " + var1);
        }

        String var2 = this.a(var1, true);
        if (this.a) {
            RobotLog.v("         output: " + var2);
        }

        return var2;
    }
}
