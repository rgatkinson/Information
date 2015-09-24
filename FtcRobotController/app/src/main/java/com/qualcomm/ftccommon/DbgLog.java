package com.qualcomm.ftccommon;

import android.util.Log;

public class DbgLog {
   public static final String ERROR_PREPEND = "### ERROR: ";
   public static final String TAG = "FIRST";

   public static void error(String var0) {
      Log.e("FIRST", "### ERROR: " + var0);
   }

   public static void logStacktrace(Exception var0) {
      msg(var0.toString());
      StackTraceElement[] var1 = var0.getStackTrace();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         msg(var1[var3].toString());
      }

   }

   public static void msg(String var0) {
      Log.i("FIRST", var0);
   }
}
