package com.qualcomm.ftccommon;

import android.util.Log;

public class DbgLog
{
    public static final String ERROR_PREPEND = "### ERROR: ";
    public static final String TAG = "FIRST";
    
    public static void error(final String s) {
        Log.e("FIRST", "### ERROR: " + s);
    }
    
    public static void logStacktrace(final Exception ex) {
        msg(ex.toString());
        final StackTraceElement[] stackTrace = ex.getStackTrace();
        for (int length = stackTrace.length, i = 0; i < length; ++i) {
            msg(stackTrace[i].toString());
        }
    }
    
    public static void msg(final String s) {
        Log.i("FIRST", s);
    }
}
