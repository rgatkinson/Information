package com.qualcomm.robotcore.util;

import android.widget.TextView;
import java.util.Arrays;
import java.util.Comparator;
import java.io.File;
import java.util.Random;

public class Util
{
    public static String ASCII_RECORD_SEPARATOR;
    public static final String LOWERCASE_ALPHA_NUM_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
    
    static {
        Util.ASCII_RECORD_SEPARATOR = "\u001e";
    }
    
    public static byte[] concatenateByteArrays(final byte[] array, final byte[] array2) {
        final byte[] array3 = new byte[array.length + array2.length];
        System.arraycopy(array, 0, array3, 0, array.length);
        System.arraycopy(array2, 0, array3, array.length, array2.length);
        return array3;
    }
    
    public static String getRandomString(final int n, final String s) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; ++i) {
            sb.append(s.charAt(random.nextInt(s.length())));
        }
        return sb.toString();
    }
    
    public static void sortFilesByName(final File[] array) {
        Arrays.sort(array, new Comparator<File>() {
            public int a(final File file, final File file2) {
                return file.getName().compareTo(file2.getName());
            }
        });
    }
    
    public static void updateTextView(final TextView textView, final String s) {
        if (textView != null) {
            textView.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    textView.setText((CharSequence)s);
                }
            });
        }
    }
}
