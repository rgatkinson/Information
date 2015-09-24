package com.qualcomm.robotcore.util;

import android.util.Log;
import com.qualcomm.robotcore.exception.RobotCoreException;
import java.io.File;
import android.os.Environment;
import android.content.Context;

public class RobotLog
{
    public static final String TAG = "RobotCore";
    private static String a;
    private static boolean b;
    
    static {
        RobotLog.a = "";
        RobotLog.b = false;
    }
    
    public static void cancelWriteLogcatToDisk(final Context context) {
        final String packageName = context.getPackageName();
        final String absolutePath = new File(Environment.getExternalStorageDirectory(), packageName).getAbsolutePath();
        RobotLog.b = false;
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000L);
                        try {
                            RobotLog.v("closing logcat file " + absolutePath);
                            RunShellCommand.killSpawnedProcess("logcat", packageName, new RunShellCommand());
                        }
                        catch (RobotCoreException ex) {
                            RobotLog.v("Unable to cancel writing log file to disk: " + ex.toString());
                        }
                    }
                    catch (InterruptedException ex2) {
                        continue;
                    }
                    break;
                }
            }
        }.start();
    }
    
    public static void clearGlobalErrorMsg() {
        RobotLog.a = "";
    }
    
    public static void d(final String s) {
        Log.d("RobotCore", s);
    }
    
    public static void e(final String s) {
        Log.e("RobotCore", s);
    }
    
    public static String getGlobalErrorMsg() {
        return RobotLog.a;
    }
    
    public static String getLogFilename(final Context context) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getPackageName() + ".logcat";
    }
    
    public static boolean hasGlobalErrorMsg() {
        return !RobotLog.a.isEmpty();
    }
    
    public static void i(final String s) {
        Log.i("RobotCore", s);
    }
    
    public static void logAndThrow(final String s) throws RobotCoreException {
        w(s);
        throw new RobotCoreException(s);
    }
    
    public static void logStacktrace(final RobotCoreException ex) {
        e(ex.toString());
        final StackTraceElement[] stackTrace = ex.getStackTrace();
        for (int length = stackTrace.length, i = 0; i < length; ++i) {
            e(stackTrace[i].toString());
        }
        if (ex.isChainedException()) {
            e("Exception chained from:");
            if (!(ex.getChainedException() instanceof RobotCoreException)) {
                logStacktrace(ex.getChainedException());
                return;
            }
            logStacktrace((RobotCoreException)ex.getChainedException());
        }
    }
    
    public static void logStacktrace(final Exception ex) {
        e(ex.toString());
        final StackTraceElement[] stackTrace = ex.getStackTrace();
        for (int length = stackTrace.length, i = 0; i < length; ++i) {
            e(stackTrace[i].toString());
        }
    }
    
    public static void setGlobalErrorMsg(final String s) {
        if (RobotLog.a.isEmpty()) {
            RobotLog.a += s;
        }
    }
    
    public static void setGlobalErrorMsgAndThrow(final String s, final RobotCoreException ex) throws RobotCoreException {
        setGlobalErrorMsg(s + "\n" + ex.getMessage());
        throw ex;
    }
    
    public static void v(final String s) {
        Log.v("RobotCore", s);
    }
    
    public static void w(final String s) {
        Log.w("RobotCore", s);
    }
    
    public static void writeLogcatToDisk(final Context context, final int n) {
        if (RobotLog.b) {
            return;
        }
        RobotLog.b = true;
        new Thread() {
            final /* synthetic */ String a = new File(getLogFilename(context)).getAbsolutePath();
            final /* synthetic */ String b = context.getPackageName();
            
            @Override
            public void run() {
                try {
                    RobotLog.v("saving logcat to " + this.a);
                    final RunShellCommand runShellCommand = new RunShellCommand();
                    RunShellCommand.killSpawnedProcess("logcat", this.b, runShellCommand);
                    runShellCommand.run(String.format("logcat -f %s -r%d -n%d -v time %s", this.a, n, 1, "UsbRequestJNI:S UsbRequest:S *:V"));
                }
                catch (RobotCoreException ex) {
                    RobotLog.v("Error while writing log file to disk: " + ex.toString());
                }
                finally {
                    RobotLog.b = false;
                }
            }
        }.start();
    }
}
