package com.qualcomm.analytics;

import java.net.MalformedURLException;
import android.os.AsyncTask;
import android.os.Build;
import android.content.IntentFilter;
import android.os.Bundle;
import android.net.NetworkInfo$State;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import java.security.SecureRandom;
import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import android.content.SharedPreferences$Editor;
import java.io.File;
import java.io.UnsupportedEncodingException;
import com.qualcomm.robotcore.util.RobotLog;
import java.net.URLEncoder;
import javax.net.ssl.SSLSession;
import android.os.Environment;
import android.content.SharedPreferences;
import android.content.Context;
import java.nio.charset.Charset;
import javax.net.ssl.HostnameVerifier;
import java.util.UUID;
import android.content.BroadcastReceiver;

public class Analytics extends BroadcastReceiver
{
    public static final String DATA_COLLECTION_PATH = ".ftcdc";
    public static final String DS_COMMAND_STRING = "update_ds";
    public static final String EXTERNAL_STORAGE_DIRECTORY_PATH;
    public static final String LAST_UPLOAD_DATE = "last_upload_date";
    public static int MAX_ENTRIES_SIZE = 0;
    public static final String RC_COMMAND_STRING = "update_rc";
    public static int TRIMMED_SIZE = 0;
    public static final String UUID_PATH = ".analytics_id";
    static String a;
    static long b;
    static UUID c;
    static String d;
    static String f;
    static final HostnameVerifier k;
    private static final Charset l;
    String e;
    Context g;
    SharedPreferences h;
    boolean i;
    long j;
    
    static {
        Analytics.a = "https://ftcdc.qualcomm.com/DataApi";
        EXTERNAL_STORAGE_DIRECTORY_PATH = Environment.getExternalStorageDirectory() + "/";
        Analytics.MAX_ENTRIES_SIZE = 100;
        Analytics.TRIMMED_SIZE = 90;
        l = Charset.forName("UTF-8");
        Analytics.c = null;
        Analytics.f = "";
        k = new HostnameVerifier() {
            @Override
            public boolean verify(final String s, final SSLSession sslSession) {
                return true;
            }
        };
    }
    
    public Analytics(final Context p0, final String p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: invokespecial   android/content/BroadcastReceiver.<init>:()V
        //     4: aload_0        
        //     5: lconst_0       
        //     6: putfield        com/qualcomm/analytics/Analytics.j:J
        //     9: aload_0        
        //    10: aload_1        
        //    11: putfield        com/qualcomm/analytics/Analytics.g:Landroid/content/Context;
        //    14: aload_0        
        //    15: aload_2        
        //    16: putfield        com/qualcomm/analytics/Analytics.e:Ljava/lang/String;
        //    19: aload_0        
        //    20: aload_1        
        //    21: invokestatic    android/preference/PreferenceManager.getDefaultSharedPreferences:(Landroid/content/Context;)Landroid/content/SharedPreferences;
        //    24: putfield        com/qualcomm/analytics/Analytics.h:Landroid/content/SharedPreferences;
        //    27: invokestatic    java/lang/System.currentTimeMillis:()J
        //    30: putstatic       com/qualcomm/analytics/Analytics.b:J
        //    33: invokestatic    com/qualcomm/robotcore/util/Version.getLibraryVersion:()Ljava/lang/String;
        //    36: putstatic       com/qualcomm/analytics/Analytics.f:Ljava/lang/String;
        //    39: aload_0        
        //    40: ldc             ".analytics_id"
        //    42: invokevirtual   com/qualcomm/analytics/Analytics.handleUUID:(Ljava/lang/String;)V
        //    45: aload_1        
        //    46: invokevirtual   android/content/Context.getApplicationInfo:()Landroid/content/pm/ApplicationInfo;
        //    49: aload_1        
        //    50: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //    53: invokevirtual   android/content/pm/ApplicationInfo.loadLabel:(Landroid/content/pm/PackageManager;)Ljava/lang/CharSequence;
        //    56: invokeinterface java/lang/CharSequence.toString:()Ljava/lang/String;
        //    61: invokestatic    com/qualcomm/analytics/Analytics.setApplicationName:(Ljava/lang/String;)V
        //    64: new             Ljava/lang/StringBuilder;
        //    67: dup            
        //    68: invokespecial   java/lang/StringBuilder.<init>:()V
        //    71: getstatic       com/qualcomm/analytics/Analytics.EXTERNAL_STORAGE_DIRECTORY_PATH:Ljava/lang/String;
        //    74: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    77: ldc             ".ftcdc"
        //    79: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    82: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    85: astore          5
        //    87: aload_0        
        //    88: aload           5
        //    90: aload_0        
        //    91: aload           5
        //    93: getstatic       com/qualcomm/analytics/Analytics.b:J
        //    96: invokestatic    com/qualcomm/analytics/Analytics.getDateFromTime:(J)Ljava/lang/String;
        //    99: invokevirtual   com/qualcomm/analytics/Analytics.incrementAndSetCount:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   102: invokevirtual   com/qualcomm/analytics/Analytics.handleCreateNewFile:(Ljava/lang/String;Ljava/lang/String;)V
        //   105: aload_1        
        //   106: aload_0        
        //   107: new             Landroid/content/IntentFilter;
        //   110: dup            
        //   111: ldc             "android.net.wifi.STATE_CHANGE"
        //   113: invokespecial   android/content/IntentFilter.<init>:(Ljava/lang/String;)V
        //   116: invokevirtual   android/content/Context.registerReceiver:(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;)Landroid/content/Intent;
        //   119: pop            
        //   120: aload_0        
        //   121: getfield        com/qualcomm/analytics/Analytics.i:Z
        //   124: ifeq            153
        //   127: aload_0        
        //   128: invokespecial   com/qualcomm/analytics/Analytics.a:()V
        //   131: return         
        //   132: astore_3       
        //   133: aload_0        
        //   134: iconst_1       
        //   135: putfield        com/qualcomm/analytics/Analytics.i:Z
        //   138: goto            120
        //   141: astore          4
        //   143: ldc             "Analytics encountered a problem"
        //   145: invokestatic    com/qualcomm/robotcore/util/RobotLog.i:(Ljava/lang/String;)V
        //   148: aload           4
        //   150: invokestatic    com/qualcomm/robotcore/util/RobotLog.logStacktrace:(Ljava/lang/Exception;)V
        //   153: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  19     120    132    141    Ljava/lang/Exception;
        //  120    131    141    153    Ljava/lang/Exception;
        //  133    138    141    153    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0120:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:692)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:529)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at the.bytecode.club.bytecodeviewer.decompilers.ProcyonDecompiler.doSaveJarDecompiled(ProcyonDecompiler.java:194)
        //     at the.bytecode.club.bytecodeviewer.decompilers.ProcyonDecompiler.decompileToZip(ProcyonDecompiler.java:146)
        //     at the.bytecode.club.bytecodeviewer.gui.MainViewerGUI$18$1$2.run(MainViewerGUI.java:1093)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private String a(final String s, final String s2, final String s3) {
        try {
            return URLEncoder.encode(s, Analytics.l.name()) + s2 + URLEncoder.encode(s3, Analytics.l.name());
        }
        catch (UnsupportedEncodingException ex) {
            RobotLog.i("Analytics caught an UnsupportedEncodingException");
            return "";
        }
    }
    
    private void a() {
        final SharedPreferences$Editor edit = this.h.edit();
        edit.putLong("last_upload_date", this.j);
        edit.apply();
        new File(Analytics.EXTERNAL_STORAGE_DIRECTORY_PATH + ".ftcdc").delete();
        new File(Analytics.EXTERNAL_STORAGE_DIRECTORY_PATH + ".analytics_id").delete();
        this.i = false;
    }
    
    private String b() {
        final String[] array = { "CPU implementer", "Hardware" };
        final HashMap<Object, String> hashMap = new HashMap<Object, String>();
        try {
            final BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/cpuinfo"));
            for (String s = bufferedReader.readLine(); s != null; s = bufferedReader.readLine()) {
                final String[] split = s.toLowerCase().split(":");
                if (split.length >= 2) {
                    hashMap.put(split[0].trim(), split[1].trim());
                }
            }
            bufferedReader.close();
            final int length = array.length;
            String s2 = "";
            String string;
            for (int i = 0; i < length; ++i, s2 = string) {
                string = s2 + hashMap.get(array[i].toLowerCase()) + " ";
            }
            String trim = s2.trim();
            if (trim.isEmpty()) {
                trim = "UNKNOWN";
            }
            return trim;
        }
        catch (FileNotFoundException ex) {
            RobotLog.i("Analytics encountered a FileNotFoundException while looking for CPU info");
        }
        catch (IOException ex2) {
            RobotLog.i("Analytics encountered an IOException while looking for CPU info");
            goto Label_0215;
        }
    }
    
    private static String b(final String s, final int n) {
        return s.substring(1 + findNthIndex(s, n, ']'));
    }
    
    private static DateCount c(final String s, final int n) {
        final String[] split = s.substring(n).replace("[", "").replace("]", "").split(" ");
        return new DateCount(split[0].trim(), split[1].trim());
    }
    
    private static void c() {
        final TrustManager[] array = { new X509TrustManager() {
                @Override
                public void checkClientTrusted(final X509Certificate[] array, final String s) throws CertificateException {
                }
                
                @Override
                public void checkServerTrusted(final X509Certificate[] array, final String s) throws CertificateException {
                }
                
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            } };
        try {
            final SSLContext instance = SSLContext.getInstance("TLS");
            instance.init(null, array, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(instance.getSocketFactory());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static String call(final URL p0, final String p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: ifnull          222
        //     4: aload_1        
        //     5: ifnull          222
        //     8: invokestatic    java/lang/System.currentTimeMillis:()J
        //    11: lstore          4
        //    13: aload_0        
        //    14: invokevirtual   java/net/URL.getProtocol:()Ljava/lang/String;
        //    17: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //    20: ldc_w           "https"
        //    23: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    26: ifeq            153
        //    29: invokestatic    com/qualcomm/analytics/Analytics.c:()V
        //    32: aload_0        
        //    33: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
        //    36: checkcast       Ljavax/net/ssl/HttpsURLConnection;
        //    39: astore          6
        //    41: aload           6
        //    43: getstatic       com/qualcomm/analytics/Analytics.k:Ljavax/net/ssl/HostnameVerifier;
        //    46: invokevirtual   javax/net/ssl/HttpsURLConnection.setHostnameVerifier:(Ljavax/net/ssl/HostnameVerifier;)V
        //    49: aload           6
        //    51: iconst_1       
        //    52: invokevirtual   java/net/HttpURLConnection.setDoOutput:(Z)V
        //    55: new             Ljava/io/OutputStreamWriter;
        //    58: dup            
        //    59: aload           6
        //    61: invokevirtual   java/net/HttpURLConnection.getOutputStream:()Ljava/io/OutputStream;
        //    64: invokespecial   java/io/OutputStreamWriter.<init>:(Ljava/io/OutputStream;)V
        //    67: astore          7
        //    69: aload           7
        //    71: aload_1        
        //    72: invokevirtual   java/io/OutputStreamWriter.write:(Ljava/lang/String;)V
        //    75: aload           7
        //    77: invokevirtual   java/io/OutputStreamWriter.flush:()V
        //    80: aload           7
        //    82: invokevirtual   java/io/OutputStreamWriter.close:()V
        //    85: new             Ljava/io/BufferedReader;
        //    88: dup            
        //    89: new             Ljava/io/InputStreamReader;
        //    92: dup            
        //    93: aload           6
        //    95: invokevirtual   java/net/HttpURLConnection.getInputStream:()Ljava/io/InputStream;
        //    98: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
        //   101: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
        //   104: astore          8
        //   106: new             Ljava/lang/String;
        //   109: dup            
        //   110: invokespecial   java/lang/String.<init>:()V
        //   113: astore_3       
        //   114: aload           8
        //   116: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //   119: astore          10
        //   121: aload           10
        //   123: ifnull          165
        //   126: new             Ljava/lang/StringBuilder;
        //   129: dup            
        //   130: invokespecial   java/lang/StringBuilder.<init>:()V
        //   133: aload_3        
        //   134: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   137: aload           10
        //   139: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   142: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   145: astore          11
        //   147: aload           11
        //   149: astore_3       
        //   150: goto            114
        //   153: aload_0        
        //   154: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
        //   157: checkcast       Ljava/net/HttpURLConnection;
        //   160: astore          6
        //   162: goto            49
        //   165: aload           8
        //   167: invokevirtual   java/io/BufferedReader.close:()V
        //   170: new             Ljava/lang/StringBuilder;
        //   173: dup            
        //   174: invokespecial   java/lang/StringBuilder.<init>:()V
        //   177: ldc_w           "Analytics took: "
        //   180: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   183: invokestatic    java/lang/System.currentTimeMillis:()J
        //   186: lload           4
        //   188: lsub           
        //   189: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   192: ldc_w           "ms"
        //   195: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   198: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   201: invokestatic    com/qualcomm/robotcore/util/RobotLog.i:(Ljava/lang/String;)V
        //   204: aload_3        
        //   205: areturn        
        //   206: astore_2       
        //   207: aconst_null    
        //   208: astore_3       
        //   209: ldc_w           "Analytics Failed to process command."
        //   212: invokestatic    com/qualcomm/robotcore/util/RobotLog.i:(Ljava/lang/String;)V
        //   215: aload_3        
        //   216: areturn        
        //   217: astore          9
        //   219: goto            209
        //   222: aconst_null    
        //   223: areturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  8      49     206    209    Ljava/io/IOException;
        //  49     114    206    209    Ljava/io/IOException;
        //  114    121    217    222    Ljava/io/IOException;
        //  126    147    217    222    Ljava/io/IOException;
        //  153    162    206    209    Ljava/io/IOException;
        //  165    204    217    222    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0114:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at the.bytecode.club.bytecodeviewer.decompilers.ProcyonDecompiler.doSaveJarDecompiled(ProcyonDecompiler.java:194)
        //     at the.bytecode.club.bytecodeviewer.decompilers.ProcyonDecompiler.decompileToZip(ProcyonDecompiler.java:146)
        //     at the.bytecode.club.bytecodeviewer.gui.MainViewerGUI$18$1$2.run(MainViewerGUI.java:1093)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static int findNthIndex(final String s, int n, final char c) {
        int n2;
        for (n2 = s.indexOf(c, 0); n > 0 && n2 != -1; n2 = s.indexOf(c, n2 + 1), --n) {}
        return n2;
    }
    
    public static String getDateFromTime(final long n) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date(n));
    }
    
    protected static UUID getUuid() {
        return Analytics.c;
    }
    
    protected static ArrayList<DateCount> parseDateCountFile(final String s) {
        ArrayList<DateCount> list;
        if (s == null || s.isEmpty()) {
            list = null;
        }
        else {
            list = new ArrayList<DateCount>();
            final String[] split = s.split("]");
            for (int length = split.length, i = 0; i < length; ++i) {
                final String[] split2 = split[i].replace("[", "").trim().split(" ");
                if (split2.length == 2) {
                    list.add(new DateCount(split2[0], split2[1]));
                }
            }
        }
        return list;
    }
    
    public static String ping(final URL url, final String s) {
        return call(url, s);
    }
    
    public static void setApplicationName(final String d) {
        Analytics.d = d;
    }
    
    public void communicateWithServer() {
        new a().execute((Object[])new String[] { Analytics.a });
    }
    
    protected void handleCreateNewFile(final String p0, final String p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: new             Ljava/io/BufferedWriter;
        //     3: dup            
        //     4: new             Ljava/io/OutputStreamWriter;
        //     7: dup            
        //     8: new             Ljava/io/FileOutputStream;
        //    11: dup            
        //    12: new             Ljava/io/File;
        //    15: dup            
        //    16: aload_1        
        //    17: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //    20: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //    23: ldc_w           "utf-8"
        //    26: invokespecial   java/io/OutputStreamWriter.<init>:(Ljava/io/OutputStream;Ljava/lang/String;)V
        //    29: invokespecial   java/io/BufferedWriter.<init>:(Ljava/io/Writer;)V
        //    32: astore_3       
        //    33: aload_3        
        //    34: aload_2        
        //    35: invokevirtual   java/io/Writer.write:(Ljava/lang/String;)V
        //    38: aload_3        
        //    39: ifnull          46
        //    42: aload_3        
        //    43: invokevirtual   java/io/Writer.close:()V
        //    46: return         
        //    47: astore          4
        //    49: aconst_null    
        //    50: astore_3       
        //    51: new             Ljava/lang/StringBuilder;
        //    54: dup            
        //    55: invokespecial   java/lang/StringBuilder.<init>:()V
        //    58: ldc_w           "Analytics encountered an IOException: "
        //    61: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    64: aload           4
        //    66: invokevirtual   java/io/IOException.toString:()Ljava/lang/String;
        //    69: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    72: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    75: invokestatic    com/qualcomm/robotcore/util/RobotLog.i:(Ljava/lang/String;)V
        //    78: aload_3        
        //    79: ifnull          46
        //    82: aload_3        
        //    83: invokevirtual   java/io/Writer.close:()V
        //    86: return         
        //    87: astore          7
        //    89: return         
        //    90: astore          5
        //    92: aconst_null    
        //    93: astore_3       
        //    94: aload_3        
        //    95: ifnull          102
        //    98: aload_3        
        //    99: invokevirtual   java/io/Writer.close:()V
        //   102: aload           5
        //   104: athrow         
        //   105: astore          8
        //   107: return         
        //   108: astore          6
        //   110: goto            102
        //   113: astore          5
        //   115: goto            94
        //   118: astore          4
        //   120: goto            51
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  0      33     47     51     Ljava/io/IOException;
        //  0      33     90     94     Any
        //  33     38     118    123    Ljava/io/IOException;
        //  33     38     113    118    Any
        //  42     46     105    108    Ljava/io/IOException;
        //  51     78     113    118    Any
        //  82     86     87     90     Ljava/io/IOException;
        //  98     102    108    113    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0046:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at the.bytecode.club.bytecodeviewer.decompilers.ProcyonDecompiler.doSaveJarDecompiled(ProcyonDecompiler.java:194)
        //     at the.bytecode.club.bytecodeviewer.decompilers.ProcyonDecompiler.decompileToZip(ProcyonDecompiler.java:146)
        //     at the.bytecode.club.bytecodeviewer.gui.MainViewerGUI$18$1$2.run(MainViewerGUI.java:1093)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public void handleUUID(final String s) {
        final File file = new File(Analytics.EXTERNAL_STORAGE_DIRECTORY_PATH + s);
        if (!file.exists()) {
            Analytics.c = UUID.randomUUID();
            this.handleCreateNewFile(Analytics.EXTERNAL_STORAGE_DIRECTORY_PATH + s, Analytics.c.toString());
        }
        final String fromFile = this.readFromFile(file);
        try {
            Analytics.c = UUID.fromString(fromFile);
        }
        catch (IllegalArgumentException ex) {
            RobotLog.i("Analytics encountered an IllegalArgumentException");
            Analytics.c = UUID.randomUUID();
            this.handleCreateNewFile(Analytics.EXTERNAL_STORAGE_DIRECTORY_PATH + s, Analytics.c.toString());
        }
    }
    
    public String incrementAndSetCount(final String s, final String s2) {
        final File file = new File(s);
        if (!file.exists()) {
            return "[" + s2 + " 1] ";
        }
        String s3 = this.readFromFile(file);
        if (findNthIndex(s3, Analytics.MAX_ENTRIES_SIZE, ']') > 0) {
            s3 = b(s3, Analytics.MAX_ENTRIES_SIZE - Analytics.TRIMMED_SIZE);
        }
        final int lastIndex = s3.lastIndexOf("[");
        if (lastIndex < 0) {
            return "[" + s2 + " 1] ";
        }
        final DateCount c = c(s3, lastIndex);
        if (getDateFromTime(Analytics.b).equals(c.date())) {
            return s3.substring(0, lastIndex).trim() + " [" + c.date() + " " + Integer.toString(1 + Integer.parseInt(c.count())) + "] ";
        }
        return s3 + " [" + s2 + " " + "1] ";
    }
    
    public boolean isConnected() {
        final NetworkInfo activeNetworkInfo = ((ConnectivityManager)this.g.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    
    public void onReceive(final Context context, final Intent intent) {
        final Bundle extras = intent.getExtras();
        if (extras != null && extras.containsKey("networkInfo") && ((NetworkInfo)extras.get("networkInfo")).getState().equals((Object)NetworkInfo$State.CONNECTED)) {
            RobotLog.i("Analytics detected NetworkInfo.State.CONNECTED");
            this.communicateWithServer();
        }
    }
    
    protected String readFromFile(final File file) {
        try {
            final char[] array = new char[4096];
            final FileReader fileReader = new FileReader(file);
            final int read = fileReader.read(array);
            fileReader.close();
            return new String(array, 0, read).trim();
        }
        catch (FileNotFoundException ex) {
            RobotLog.i("Analytics encountered a FileNotFoundException while trying to read a file.");
        }
        catch (IOException ex2) {
            RobotLog.i("Analytics encountered an IOException while trying to read.");
            goto Label_0058;
        }
    }
    
    public void register() {
        this.g.registerReceiver((BroadcastReceiver)this, new IntentFilter("android.net.wifi.STATE_CHANGE"));
    }
    
    public void unregister() {
        this.g.unregisterReceiver((BroadcastReceiver)this);
    }
    
    public String updateStats(final String s, final ArrayList<DateCount> list, final String s2) {
        final String string = this.a("cmd", "=", s2) + "&" + this.a("uuid", "=", s) + "&" + this.a("device_hw", "=", Build.MANUFACTURER) + "&" + this.a("device_ver", "=", Build.MODEL) + "&" + this.a("chip_type", "=", this.b()) + "&" + this.a("sw_ver", "=", Analytics.f) + "&";
        String string2 = "";
        String string3;
        for (int i = 0; i < list.size(); ++i, string2 = string3) {
            if (i > 0) {
                string2 += ",";
            }
            string3 = string2 + this.a(list.get(i).date(), ",", list.get(i).count());
        }
        return string + this.a("dc", "=", "") + string2;
    }
    
    public static class DateCount
    {
        private final String a;
        private final String b;
        
        public DateCount(final String a, final String b) {
            this.a = a;
            this.b = b;
        }
        
        public String count() {
            return this.b;
        }
        
        public String date() {
            return this.a;
        }
    }
    
    private class a extends AsyncTask
    {
        protected Object doInBackground(final Object[] array) {
            if (Analytics.this.isConnected()) {
                File file;
                try {
                    final URL url = new URL(Analytics.a);
                    if (Analytics.getDateFromTime(Analytics.b).equals(Analytics.getDateFromTime(Analytics.this.h.getLong("last_upload_date", Analytics.this.j)))) {
                        return null;
                    }
                    final String ping = Analytics.ping(url, Analytics.this.a("cmd", "=", "ping"));
                    if (ping == null || !ping.contains("\"rc\": \"OK\"")) {
                        RobotLog.e("Analytics: Ping failed.");
                        return null;
                    }
                    RobotLog.i("Analytics ping succeeded.");
                    final String string = Analytics.EXTERNAL_STORAGE_DIRECTORY_PATH + ".ftcdc";
                    file = new File(string);
                    if (!file.exists()) {
                        Analytics.this.handleCreateNewFile(string, Analytics.this.incrementAndSetCount(string, Analytics.getDateFromTime(Analytics.b)));
                    }
                    final String fromFile = Analytics.this.readFromFile(file);
                    final ArrayList<DateCount> dateCountFile = Analytics.parseDateCountFile(fromFile);
                    final String call = Analytics.call(url, Analytics.this.updateStats(Analytics.c.toString(), dateCountFile, Analytics.this.e));
                    if (call == null || !call.contains("\"rc\": \"OK\"")) {
                        RobotLog.e("Analytics: Upload failed.");
                        if (dateCountFile.size() > Analytics.MAX_ENTRIES_SIZE) {
                            Analytics.this.handleCreateNewFile(string, b(fromFile, Analytics.MAX_ENTRIES_SIZE - Analytics.TRIMMED_SIZE));
                            RobotLog.i("Analytics trimmed the data file.");
                            return null;
                        }
                        return null;
                    }
                }
                catch (MalformedURLException ex) {
                    RobotLog.e("Analytics encountered a malformed URL exception");
                    return null;
                }
                RobotLog.i("Analytics: Upload succeeded.");
                final SharedPreferences$Editor edit = Analytics.this.h.edit();
                edit.putLong("last_upload_date", Analytics.b);
                edit.apply();
                file.delete();
            }
            return null;
        }
    }
}
