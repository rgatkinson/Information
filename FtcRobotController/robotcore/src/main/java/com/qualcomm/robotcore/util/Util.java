package com.qualcomm.robotcore.util;

import android.widget.TextView;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Util {
    public static final String LOWERCASE_ALPHA_NUM_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
    public static String ASCII_RECORD_SEPARATOR = "\u001e";

    public static byte[] concatenateByteArrays(byte[] var0, byte[] var1) {
        byte[] var2 = new byte[var0.length + var1.length];
        System.arraycopy(var0, 0, var2, 0, var0.length);
        System.arraycopy(var1, 0, var2, var0.length, var1.length);
        return var2;
    }

    public static String getRandomString(int var0, String var1) {
        Random var2 = new Random();
        StringBuilder var3 = new StringBuilder();

        for (int var4 = 0; var4 < var0; ++var4) {
            var3.append(var1.charAt(var2.nextInt(var1.length())));
        }

        return var3.toString();
    }

    public static void sortFilesByName(File[] var0) {
        Arrays.sort(var0, new Comparator() {
            public int a(File var1, File var2) {
                return var1.getName().compareTo(var2.getName());
            }

            // $FF: synthetic method
            public int compare(Object var1, Object var2) {
                return this.a((File) var1, (File) var2);
            }
        });
    }

    public static void updateTextView(final TextView var0, final String var1) {
        if (var0 != null) {
            var0.post(new Runnable() {
                public void run() {
                    var0.setText(var1);
                }
            });
        }

    }
}
