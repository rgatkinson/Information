//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.analytics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.Version;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Analytics extends BroadcastReceiver {
    public static final String UUID_PATH = ".analytics_id";
    public static final String DATA_COLLECTION_PATH = ".ftcdc";
    static String a = "https://ftcdc.qualcomm.com/DataApi";
    public static final String RC_COMMAND_STRING = "update_rc";
    public static final String DS_COMMAND_STRING = "update_ds";
    public static final String EXTERNAL_STORAGE_DIRECTORY_PATH = Environment.getExternalStorageDirectory() + "/";
    public static final String LAST_UPLOAD_DATE = "last_upload_date";
    public static final String MAX_DEVICES = "max_usb_devices";
    public static int MAX_ENTRIES_SIZE = 100;
    public static int TRIMMED_SIZE = 90;
    private static final Charset m = Charset.forName("UTF-8");
    static long b;
    static UUID c = null;
    static String d;
    String e;
    static String f = "";
    Context g;
    SharedPreferences h;
    boolean i = false;
    long j = 0L;
    int k = 0;
    static final HostnameVerifier l = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    public void onReceive(Context context, Intent intent) {
        Bundle var3 = intent.getExtras();
        if(var3 != null && var3.containsKey("networkInfo")) {
            NetworkInfo var4 = (NetworkInfo)var3.get("networkInfo");
            State var5 = var4.getState();
            if(var5.equals(State.CONNECTED)) {
                RobotLog.i("Analytics detected NetworkInfo.State.CONNECTED");
                this.communicateWithServer();
            }
        }

    }

    public void unregister() {
        this.g.unregisterReceiver(this);
    }

    public void register() {
        this.g.registerReceiver(this, new IntentFilter("android.net.wifi.STATE_CHANGE"));
    }

    public Analytics(Context context, String commandString, HardwareMap map) {
        this.g = context;
        this.e = commandString;

        try {
            try {
                this.h = PreferenceManager.getDefaultSharedPreferences(context);
                b = System.currentTimeMillis();
                f = Version.getLibraryVersion();
                this.handleUUID(".analytics_id");
                int var4 = this.calculateUsbDevices(map);
                int var5 = this.h.getInt("max_usb_devices", this.k);
                if(var5 < var4) {
                    Editor var6 = this.h.edit();
                    var6.putInt("max_usb_devices", var4);
                    var6.apply();
                }

                setApplicationName(context.getApplicationInfo().loadLabel(context.getPackageManager()).toString());
                this.handleData();
                this.register();
                RobotLog.i("Analytics has completed initialization.");
            } catch (Exception var7) {
                this.i = true;
            }

            if(this.i) {
                this.a();
                this.i = false;
            }
        } catch (Exception var8) {
            RobotLog.i("Analytics encountered a problem during initialization");
            RobotLog.logStacktrace(var8);
        }

    }

    protected int calculateUsbDevices(HardwareMap map) {
        byte var2 = 0;
        int var7 = var2 + map.legacyModule.size();
        var7 += map.deviceInterfaceModule.size();
        Iterator var3 = map.servoController.iterator();

        String var5;
        Pattern var6;
        while(var3.hasNext()) {
            ServoController var4 = (ServoController)var3.next();
            var5 = var4.getDeviceName();
            var6 = Pattern.compile("(?i)usb");
            if(var6.matcher(var5).matches()) {
                ++var7;
            }
        }

        var3 = map.dcMotorController.iterator();

        while(var3.hasNext()) {
            DcMotorController var8 = (DcMotorController)var3.next();
            var5 = var8.getDeviceName();
            var6 = Pattern.compile("(?i)usb");
            if(var6.matcher(var5).matches()) {
                ++var7;
            }
        }

        return var7;
    }

    protected void handleData() throws IOException, ClassNotFoundException {
        String var1 = EXTERNAL_STORAGE_DIRECTORY_PATH + ".ftcdc";
        File var2 = new File(var1);
        if(!var2.exists()) {
            this.createInitialFile(var1);
        } else {
            ArrayList var3 = this.updateExistingFile(var1, getDateFromTime(b));
            if(var3.size() >= MAX_ENTRIES_SIZE) {
                this.trimEntries(var3);
            }

            this.writeObjectsToFile(var1, var3);
        }

    }

    protected void trimEntries(ArrayList<Analytics.DataInfo> dataInfoArrayList) {
        dataInfoArrayList.subList(TRIMMED_SIZE, dataInfoArrayList.size()).clear();
    }

    protected ArrayList<Analytics.DataInfo> updateExistingFile(String filepath, String date) throws ClassNotFoundException, IOException {
        ArrayList var3 = this.readObjectsFromFile(filepath);
        Analytics.DataInfo var4 = (Analytics.DataInfo)var3.get(var3.size() - 1);
        if(var4.a.equalsIgnoreCase(date)) {
            ++var4.numUsages;
        } else {
            Analytics.DataInfo var5 = new Analytics.DataInfo(date, 1);
            var3.add(var5);
        }

        return var3;
    }

    protected ArrayList<Analytics.DataInfo> readObjectsFromFile(String filepath) throws IOException, ClassNotFoundException {
        FileInputStream var2 = new FileInputStream(new File(filepath));
        ObjectInputStream var3 = new ObjectInputStream(var2);
        ArrayList var4 = new ArrayList();
        boolean var5 = true;

        while(var5) {
            try {
                Analytics.DataInfo var6 = (Analytics.DataInfo)var3.readObject();
                var4.add(var6);
            } catch (EOFException var7) {
                var5 = false;
            }
        }

        var3.close();
        return var4;
    }

    protected void createInitialFile(String filepath) throws IOException {
        Analytics.DataInfo var2 = new Analytics.DataInfo(getDateFromTime(b), 1);
        ArrayList var3 = new ArrayList();
        var3.add(var2);
        this.writeObjectsToFile(filepath, var3);
    }

    private void a() {
        RobotLog.i("Analytics is starting with a clean slate.");
        Editor var1 = this.h.edit();
        var1.putLong("last_upload_date", this.j);
        var1.apply();
        var1.putInt("max_usb_devices", 0);
        var1.apply();
        File var2 = new File(EXTERNAL_STORAGE_DIRECTORY_PATH + ".ftcdc");
        var2.delete();
        File var3 = new File(EXTERNAL_STORAGE_DIRECTORY_PATH + ".analytics_id");
        var3.delete();
        this.i = false;
    }

    public void communicateWithServer() {
        String[] var1 = new String[]{a};
        (new Analytics.a(null)).execute((Object[])var1);
    }

    public static void setApplicationName(String name) {
        d = name;
    }

    public void handleUUID(String filename) {
        File var2 = new File(EXTERNAL_STORAGE_DIRECTORY_PATH + filename);
        if(!var2.exists()) {
            c = UUID.randomUUID();
            this.handleCreateNewFile(EXTERNAL_STORAGE_DIRECTORY_PATH + filename, c.toString());
        }

        String var3 = this.readFromFile(var2);

        try {
            c = UUID.fromString(var3);
        } catch (IllegalArgumentException var5) {
            RobotLog.i("Analytics encountered an IllegalArgumentException");
            c = UUID.randomUUID();
            this.handleCreateNewFile(EXTERNAL_STORAGE_DIRECTORY_PATH + filename, c.toString());
        }

    }

    protected String readFromFile(File file) {
        try {
            char[] var2 = new char[4096];
            FileReader var3 = new FileReader(file);
            int var4 = var3.read(var2);
            var3.close();
            String var5 = new String(var2, 0, var4);
            return var5.trim();
        } catch (FileNotFoundException var6) {
            RobotLog.i("Analytics encountered a FileNotFoundException while trying to read a file.");
        } catch (IOException var7) {
            RobotLog.i("Analytics encountered an IOException while trying to read.");
        }

        return "";
    }

    protected void writeObjectsToFile(String filepath, ArrayList<Analytics.DataInfo> info) throws IOException {
        ObjectOutputStream var3 = new ObjectOutputStream(new FileOutputStream(filepath));
        Iterator var4 = info.iterator();

        while(var4.hasNext()) {
            Analytics.DataInfo var5 = (Analytics.DataInfo)var4.next();
            var3.writeObject(var5);
        }

        var3.close();
    }

    protected void handleCreateNewFile(String filepath, String data) {
        BufferedWriter var3 = null;

        try {
            File var4 = new File(filepath);
            FileOutputStream var5 = new FileOutputStream(var4);
            var3 = new BufferedWriter(new OutputStreamWriter(var5, "utf-8"));
            var3.write(data);
        } catch (IOException var14) {
            RobotLog.i("Analytics encountered an IOException: " + var14.toString());
        } finally {
            if(var3 != null) {
                try {
                    var3.close();
                } catch (IOException var13) {
                    ;
                }
            }

        }

    }

    public static String getDateFromTime(long time) {
        SimpleDateFormat var2 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String var3 = var2.format(new Date(time));
        return var3;
    }

    protected static UUID getUuid() {
        return c;
    }

    public String updateStats(String user, ArrayList<Analytics.DataInfo> dateInfo, String commandString) {
        int var4 = this.h.getInt("max_usb_devices", this.k);
        String var5 = this.a("cmd", "=", commandString) + "&" + this.a("uuid", "=", user) + "&" + this.a("device_hw", "=", Build.MANUFACTURER) + "&" + this.a("device_ver", "=", Build.MODEL) + "&" + this.a("chip_type", "=", this.b()) + "&" + this.a("sw_ver", "=", f) + "&" + this.a("max_dev", "=", String.valueOf(var4)) + "&";
        String var6 = "";

        for(int var7 = 0; var7 < dateInfo.size(); ++var7) {
            if(var7 > 0) {
                var6 = var6 + ",";
            }

            var6 = var6 + this.a(((Analytics.DataInfo)dateInfo.get(var7)).date(), ",", String.valueOf(((Analytics.DataInfo)dateInfo.get(var7)).numUsages()));
        }

        var5 = var5 + this.a("dc", "=", "");
        var5 = var5 + var6;
        return var5;
    }

    private String a(String var1, String var2, String var3) {
        String var4 = "";

        try {
            var4 = URLEncoder.encode(var1, m.name()) + var2 + URLEncoder.encode(var3, m.name());
        } catch (UnsupportedEncodingException var6) {
            RobotLog.i("Analytics caught an UnsupportedEncodingException");
        }

        return var4;
    }

    private String b() {
        String var1 = "UNKNOWN";
        String var2 = "/proc/cpuinfo";
        String[] var3 = new String[]{"CPU implementer", "Hardware"};
        HashMap var4 = new HashMap();

        try {
            BufferedReader var5 = new BufferedReader(new FileReader("/proc/cpuinfo"));

            for(String var6 = var5.readLine(); var6 != null; var6 = var5.readLine()) {
                var6 = var6.toLowerCase();
                String[] var7 = var6.split(":");
                if(var7.length >= 2) {
                    var4.put(var7[0].trim(), var7[1].trim());
                }
            }

            var5.close();
            String var14 = "";
            String[] var8 = var3;
            int var9 = var3.length;

            for(int var10 = 0; var10 < var9; ++var10) {
                String var11 = var8[var10];
                var14 = var14 + (String)var4.get(var11.toLowerCase()) + " ";
            }

            var14 = var14.trim();
            if(var14.isEmpty()) {
                return var1;
            }

            return var14;
        } catch (FileNotFoundException var12) {
            RobotLog.i("Analytics encountered a FileNotFoundException while looking for CPU info");
        } catch (IOException var13) {
            RobotLog.i("Analytics encountered an IOException while looking for CPU info");
        }

        return var1;
    }

    public boolean isConnected() {
        ConnectivityManager var1 = (ConnectivityManager)this.g.getSystemService("connectivity");
        NetworkInfo var2 = var1.getActiveNetworkInfo();
        return var2 != null && var2.isConnected();
    }

    public static String ping(URL baseUrl, String data) {
        String var2 = call(baseUrl, data);
        return var2;
    }

    public static String call(URL url, String data) {
        String var4 = null;
        if(url != null && data != null) {
            try {
                long var2 = System.currentTimeMillis();
                Object var5 = null;
                if(url.getProtocol().toLowerCase().equals("https")) {
                    c();
                    HttpsURLConnection var6 = (HttpsURLConnection)url.openConnection();
                    var6.setHostnameVerifier(l);
                    var5 = var6;
                } else {
                    var5 = (HttpURLConnection)url.openConnection();
                }

                ((HttpURLConnection)var5).setDoOutput(true);
                OutputStreamWriter var7 = new OutputStreamWriter(((HttpURLConnection)var5).getOutputStream());
                var7.write(data);
                var7.flush();
                var7.close();
                BufferedReader var10 = new BufferedReader(new InputStreamReader(((HttpURLConnection)var5).getInputStream()));

                String var8;
                for(var4 = new String(); (var8 = var10.readLine()) != null; var4 = var4 + var8) {
                    ;
                }

                var10.close();
                RobotLog.i("Analytics took: " + (System.currentTimeMillis() - var2) + "ms");
            } catch (IOException var9) {
                RobotLog.i("Analytics Failed to process command.");
            }
        }

        return var4;
    }

    private static void c() {
        TrustManager[] var0 = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        }};

        try {
            SSLContext var1 = SSLContext.getInstance("TLS");
            var1.init((KeyManager[])null, var0, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(var1.getSocketFactory());
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    private class a extends AsyncTask {
        private a() {
        }

        protected Object doInBackground(Object[] params) {
            if(Analytics.this.isConnected()) {
                try {
                    URL var2 = null;
                    var2 = new URL(Analytics.a);
                    long var3 = Analytics.this.h.getLong("last_upload_date", Analytics.this.j);
                    if(!Analytics.getDateFromTime(Analytics.b).equals(Analytics.getDateFromTime(var3))) {
                        label53: {
                            String var5 = Analytics.this.a("cmd", "=", "ping");
                            String var6 = Analytics.ping(var2, var5);
                            String var7 = "\"rc\": \"OK\"";
                            if(var6 != null && var6.contains(var7)) {
                                RobotLog.i("Analytics ping succeeded.");
                                String var8 = Analytics.EXTERNAL_STORAGE_DIRECTORY_PATH + ".ftcdc";
                                ArrayList var9 = Analytics.this.readObjectsFromFile(var8);
                                if(var9.size() >= Analytics.MAX_ENTRIES_SIZE) {
                                    Analytics.this.trimEntries(var9);
                                }

                                String var10 = Analytics.this.updateStats(Analytics.c.toString(), var9, Analytics.this.e);
                                String var11 = Analytics.call(var2, var10);
                                if(var11 != null && var11.contains(var7)) {
                                    RobotLog.i("Analytics: Upload succeeded.");
                                    Editor var12 = Analytics.this.h.edit();
                                    var12.putLong("last_upload_date", Analytics.b);
                                    var12.apply();
                                    var12.putInt("max_usb_devices", 0);
                                    var12.apply();
                                    File var13 = new File(var8);
                                    boolean var14 = var13.delete();
                                    break label53;
                                }

                                RobotLog.e("Analytics: Upload failed.");
                                return null;
                            }

                            RobotLog.e("Analytics: Ping failed.");
                            return null;
                        }
                    }
                } catch (MalformedURLException var15) {
                    RobotLog.e("Analytics encountered a malformed URL exception");
                } catch (Exception var16) {
                    Analytics.this.i = true;
                }

                if(Analytics.this.i) {
                    RobotLog.i("Analytics encountered a problem during communication");
                    Analytics.this.a();
                    Analytics.this.i = false;
                }
            }

            return null;
        }
    }

    public static class DataInfo implements Serializable {
        private final String a;
        protected int numUsages;

        public DataInfo(String adate, int numUsages) {
            this.a = adate;
            this.numUsages = numUsages;
        }

        public String date() {
            return this.a;
        }

        public int numUsages() {
            return this.numUsages;
        }
    }
}
