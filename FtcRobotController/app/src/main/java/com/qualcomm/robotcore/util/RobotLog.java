package com.qualcomm.robotcore.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RunShellCommand;
import java.io.File;

public class RobotLog {
   public static final String TAG = "RobotCore";
   private static String a = "";
   private static boolean b = false;

   public static void cancelWriteLogcatToDisk(Context var0) {
      final String var1 = var0.getPackageName();
      final String var2 = (new File(Environment.getExternalStorageDirectory(), var1)).getAbsolutePath();
      b = false;
      (new Thread() {
         public void run() {
            try {
               Thread.sleep(1000L);
            } catch (InterruptedException var5) {
               ;
            }

            try {
               RobotLog.v("closing logcat file " + var2);
               RunShellCommand var3 = new RunShellCommand();
               RunShellCommand.killSpawnedProcess("logcat", var1, var3);
            } catch (RobotCoreException var4) {
               RobotLog.v("Unable to cancel writing log file to disk: " + var4.toString());
            }
         }
      }).start();
   }

   public static void clearGlobalErrorMsg() {
      a = "";
   }

   public static void d(String var0) {
      Log.d("RobotCore", var0);
   }

   public static void e(String var0) {
      Log.e("RobotCore", var0);
   }

   public static String getGlobalErrorMsg() {
      return a;
   }

   public static String getLogFilename(Context var0) {
      String var1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + var0.getPackageName();
      return var1 + ".logcat";
   }

   public static boolean hasGlobalErrorMsg() {
      return !a.isEmpty();
   }

   public static void i(String var0) {
      Log.i("RobotCore", var0);
   }

   public static void logAndThrow(String var0) throws RobotCoreException {
      w(var0);
      throw new RobotCoreException(var0);
   }

   public static void logStacktrace(RobotCoreException var0) {
      e(var0.toString());
      StackTraceElement[] var1 = var0.getStackTrace();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         e(var1[var3].toString());
      }

      if(var0.isChainedException()) {
         e("Exception chained from:");
         if(!(var0.getChainedException() instanceof RobotCoreException)) {
            logStacktrace(var0.getChainedException());
            return;
         }

         logStacktrace((RobotCoreException)var0.getChainedException());
      }

   }

   public static void logStacktrace(Exception var0) {
      e(var0.toString());
      StackTraceElement[] var1 = var0.getStackTrace();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         e(var1[var3].toString());
      }

   }

   public static void setGlobalErrorMsg(String var0) {
      if(a.isEmpty()) {
         a = a + var0;
      }

   }

   public static void setGlobalErrorMsgAndThrow(String var0, RobotCoreException var1) throws RobotCoreException {
      setGlobalErrorMsg(var0 + "\n" + var1.getMessage());
      throw var1;
   }

   public static void v(String var0) {
      Log.v("RobotCore", var0);
   }

   public static void w(String var0) {
      Log.w("RobotCore", var0);
   }

   public static void writeLogcatToDisk(Context var0, final int var1) {
      if(!b) {
         b = true;
         final String var2 = var0.getPackageName();
         (new Thread("Logging Thread") {
            // $FF: synthetic field
            final String a;

            {
               this.a = var2x;
            }

            public void run() {
               try {
                  RobotLog.v("saving logcat to " + this.a);
                  RunShellCommand var5 = new RunShellCommand();
                  RunShellCommand.killSpawnedProcess("logcat", var2, var5);
                  Object[] var6 = new Object[]{this.a, Integer.valueOf(var1), Integer.valueOf(1), "UsbRequestJNI:S UsbRequest:S *:V"};
                  var5.run(String.format("logcat -f %s -r%d -n%d -v time %s", var6));
                  return;
               } catch (RobotCoreException var9) {
                  RobotLog.v("Error while writing log file to disk: " + var9.toString());
               } finally {
                  RobotLog.b = false;
               }

            }
         }).start();
      }
   }
}
