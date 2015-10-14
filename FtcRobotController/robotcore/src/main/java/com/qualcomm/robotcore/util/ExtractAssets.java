package com.qualcomm.robotcore.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class ExtractAssets {
    private static final String a = ExtractAssets.class.getSimpleName();

    public static ArrayList<String> ExtractToStorage(Context var0, ArrayList<String> var1, boolean var2) throws IOException {
        if (!var2 && !"mounted".equals(Environment.getExternalStorageState())) {
            throw new IOException("External Storage not accessible");
        } else {
            ArrayList var3 = new ArrayList();
            Iterator var4 = var1.iterator();

            while (var4.hasNext()) {
                a(var0, (String) var4.next(), var2, var3);
                if (var3 != null) {
                    Log.d(a, "got " + var3.size() + " elements");
                }
            }

            return var3;
        }
    }

    private static ArrayList<String> a(Context var0, String var1, boolean var2, ArrayList<String> var3) {
        Log.d(a, "Extracting assests for " + var1);
        String[] var4 = null;
        AssetManager var5 = var0.getAssets();

        try {
            var4 = var5.list(var1);
        } catch (IOException var33) {
            var33.printStackTrace();
        }

        InputStream var6 = null;
        FileOutputStream var7 = null;
        String var8;
        int var11;
        String var12;
        String var13;
        if (var4.length == 0) {
            ArrayList var39;
            try {
                var6 = var5.open(var1);
                Log.d(a, "File: " + var1 + " opened for streaming");
                if (!var1.startsWith(File.separator)) {
                    var1 = File.separator + var1;
                }

                var8 = null;
                File var36;
                if (var2) {
                    var36 = var0.getFilesDir();
                } else {
                    var36 = var0.getExternalFilesDir(null);
                }

                String var37 = var36.getPath();
                String var38 = var37.concat(var1);
                if (var3 == null || !var3.contains(var38)) {
                    var11 = var38.lastIndexOf(File.separatorChar);
                    var12 = var38.substring(0, var11);
                    var13 = var38.substring(var11, var38.length());
                    File var14 = new File(var12);
                    if (var14.mkdirs()) {
                        Log.d(a, "Dir created " + var12);
                    }

                    File var15 = new File(var14, var13);
                    var7 = new FileOutputStream(var15);
                    if (var7 != null) {
                        byte[] var16 = new byte[1024];
                        boolean var17 = false;

                        int var40;
                        while ((var40 = var6.read(var16)) != -1) {
                            var7.write(var16, 0, var40);
                        }
                    }

                    var7.close();
                    if (var3 != null) {
                        var3.add(var38);
                    }

                    return var3;
                }

                Log.e(a, "Ignoring Duplicate entry for " + var38);
                var39 = var3;
            } catch (IOException var34) {
                Log.d(a, "File: " + var1 + " doesn\'t exist");
                return var3;
            } finally {
                if (var6 != null) {
                    try {
                        var6.close();
                    } catch (IOException var32) {
                        Log.d(a, "Unable to close in stream");
                        var32.printStackTrace();
                    }

                    if (var7 != null) {
                        try {
                            var7.close();
                        } catch (IOException var31) {
                            Log.d(a, "Unable to close out stream");
                            var31.printStackTrace();
                        }
                    }
                }

            }

            return var39;
        } else {
            var8 = var1;
            if (!var1.equals("") && !var1.endsWith(File.separator)) {
                var8 = var1.concat(File.separator);
            }

            String[] var9 = var4;
            int var10 = var4.length;

            for (var11 = 0; var11 < var10; ++var11) {
                var12 = var9[var11];
                var13 = var8.concat(var12);
                a(var0, var13, var2, var3);
            }

            return var3;
        }
    }
}
