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
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
   public static final String DATA_COLLECTION_PATH = ".ftcdc";
   public static final String DS_COMMAND_STRING = "update_ds";
   public static final String EXTERNAL_STORAGE_DIRECTORY_PATH = Environment.getExternalStorageDirectory() + "/";
   public static final String LAST_UPLOAD_DATE = "last_upload_date";
   public static final String MAX_DEVICES = "max_usb_devices";
   public static int MAX_ENTRIES_SIZE = 100;
   public static final String RC_COMMAND_STRING = "update_rc";
   public static int TRIMMED_SIZE = 90;
   public static final String UUID_PATH = ".analytics_id";
   static String a = "https://ftcdc.qualcomm.com/DataApi";
   static long b;
   static UUID c = null;
   static String d;
   static String f = "";
   static final HostnameVerifier l = new HostnameVerifier() {
      public boolean verify(String var1, SSLSession var2) {
         return true;
      }
   };
   private static final Charset m = Charset.forName("UTF-8");
   String e;
   Context g;
   SharedPreferences h;
   boolean i;
   long j;
   int k;

   public Analytics(Context param1, String param2, HardwareMap param3) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   static String a(Analytics var0, String var1, String var2, String var3) {
      return var0.a(var1, var2, var3);
   }

   private String a(String var1, String var2, String var3) {
      try {
         String var5 = URLEncoder.encode(var1, m.name()) + var2 + URLEncoder.encode(var3, m.name());
         return var5;
      } catch (UnsupportedEncodingException var6) {
         RobotLog.i("Analytics caught an UnsupportedEncodingException");
         return "";
      }
   }

   private void a() {
      RobotLog.i("Analytics is starting with a clean slate.");
      Editor var1 = this.h.edit();
      var1.putLong("last_upload_date", this.j);
      var1.apply();
      var1.putInt("max_usb_devices", 0);
      var1.apply();
      (new File(EXTERNAL_STORAGE_DIRECTORY_PATH + ".ftcdc")).delete();
      (new File(EXTERNAL_STORAGE_DIRECTORY_PATH + ".analytics_id")).delete();
      this.i = false;
   }

   // $FF: synthetic method
   static void a(Analytics var0) {
      var0.a();
   }

   private String b() {
      // $FF: Couldn't be decompiled
   }

   private static void c() {
      TrustManager[] var0 = new TrustManager[]{new X509TrustManager() {
         public void checkClientTrusted(X509Certificate[] var1, String var2) throws CertificateException {
         }

         public void checkServerTrusted(X509Certificate[] var1, String var2) throws CertificateException {
         }

         public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
         }
      }};

      try {
         SSLContext var2 = SSLContext.getInstance("TLS");
         var2.init((KeyManager[])null, var0, new SecureRandom());
         HttpsURLConnection.setDefaultSSLSocketFactory(var2.getSocketFactory());
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   public static String call(URL param0, String param1) {
      // $FF: Couldn't be decompiled
   }

   public static String getDateFromTime(long var0) {
      return (new SimpleDateFormat("yyyy-MM-dd", Locale.US)).format(new Date(var0));
   }

   protected static UUID getUuid() {
      return c;
   }

   public static String ping(URL var0, String var1) {
      return call(var0, var1);
   }

   public static void setApplicationName(String var0) {
      d = var0;
   }

   protected int calculateUsbDevices(HardwareMap var1) {
      int var2 = 0 + var1.legacyModule.size() + var1.deviceInterfaceModule.size();
      Iterator var3 = var1.servoController.iterator();

      int var4;
      int var8;
      for(var4 = var2; var3.hasNext(); var4 = var8) {
         String var7 = ((ServoController)var3.next()).getDeviceName();
         if(Pattern.compile("(?i)usb").matcher(var7).matches()) {
            var8 = var4 + 1;
         } else {
            var8 = var4;
         }
      }

      Iterator var5 = var1.dcMotorController.iterator();

      while(var5.hasNext()) {
         String var6 = ((DcMotorController)var5.next()).getDeviceName();
         if(Pattern.compile("(?i)usb").matcher(var6).matches()) {
            ++var4;
         }
      }

      return var4;
   }

   public void communicateWithServer() {
      String[] var1 = new String[]{a};
      (new Analytics.a(null)).execute((Object[])var1);
   }

   protected void createInitialFile(String var1) throws IOException {
      Analytics.DataInfo var2 = new Analytics.DataInfo(getDateFromTime(b), 1);
      ArrayList var3 = new ArrayList();
      var3.add(var2);
      this.writeObjectsToFile(var1, var3);
   }

   protected void handleCreateNewFile(String param1, String param2) {
      // $FF: Couldn't be decompiled
   }

   protected void handleData() throws IOException, ClassNotFoundException {
      String var1 = EXTERNAL_STORAGE_DIRECTORY_PATH + ".ftcdc";
      if(!(new File(var1)).exists()) {
         this.createInitialFile(var1);
      } else {
         ArrayList var2 = this.updateExistingFile(var1, getDateFromTime(b));
         if(var2.size() >= MAX_ENTRIES_SIZE) {
            this.trimEntries(var2);
         }

         this.writeObjectsToFile(var1, var2);
      }
   }

   public void handleUUID(String var1) {
      File var2 = new File(EXTERNAL_STORAGE_DIRECTORY_PATH + var1);
      if(!var2.exists()) {
         c = UUID.randomUUID();
         this.handleCreateNewFile(EXTERNAL_STORAGE_DIRECTORY_PATH + var1, c.toString());
      }

      String var3 = this.readFromFile(var2);

      try {
         c = UUID.fromString(var3);
      } catch (IllegalArgumentException var5) {
         RobotLog.i("Analytics encountered an IllegalArgumentException");
         c = UUID.randomUUID();
         this.handleCreateNewFile(EXTERNAL_STORAGE_DIRECTORY_PATH + var1, c.toString());
      }
   }

   public boolean isConnected() {
      NetworkInfo var1 = ((ConnectivityManager)this.g.getSystemService("connectivity")).getActiveNetworkInfo();
      return var1 != null && var1.isConnected();
   }

   public void onReceive(Context var1, Intent var2) {
      Bundle var3 = var2.getExtras();
      if(var3 != null && var3.containsKey("networkInfo") && ((NetworkInfo)var3.get("networkInfo")).getState().equals(State.CONNECTED)) {
         RobotLog.i("Analytics detected NetworkInfo.State.CONNECTED");
         this.communicateWithServer();
      }

   }

   protected String readFromFile(File var1) {
      try {
         char[] var4 = new char[4096];
         FileReader var5 = new FileReader(var1);
         int var6 = var5.read(var4);
         var5.close();
         String var7 = (new String(var4, 0, var6)).trim();
         return var7;
      } catch (FileNotFoundException var8) {
         RobotLog.i("Analytics encountered a FileNotFoundException while trying to read a file.");
      } catch (IOException var9) {
         RobotLog.i("Analytics encountered an IOException while trying to read.");
      }

      return "";
   }

   protected ArrayList<Analytics.DataInfo> readObjectsFromFile(String var1) throws IOException, ClassNotFoundException {
      ObjectInputStream var2 = new ObjectInputStream(new FileInputStream(new File(var1)));
      ArrayList var3 = new ArrayList();
      boolean var4 = true;

      while(var4) {
         try {
            var3.add((Analytics.DataInfo)var2.readObject());
         } catch (EOFException var6) {
            var4 = false;
         }
      }

      var2.close();
      return var3;
   }

   public void register() {
      this.g.registerReceiver(this, new IntentFilter("android.net.wifi.STATE_CHANGE"));
   }

   protected void trimEntries(ArrayList<Analytics.DataInfo> var1) {
      var1.subList(TRIMMED_SIZE, var1.size()).clear();
   }

   public void unregister() {
      this.g.unregisterReceiver(this);
   }

   protected ArrayList<Analytics.DataInfo> updateExistingFile(String var1, String var2) throws ClassNotFoundException, IOException {
      ArrayList var3 = this.readObjectsFromFile(var1);
      Analytics.DataInfo var4 = (Analytics.DataInfo)var3.get(-1 + var3.size());
      if(var4.a.equalsIgnoreCase(var2)) {
         ++var4.numUsages;
         return var3;
      } else {
         var3.add(new Analytics.DataInfo(var2, 1));
         return var3;
      }
   }

   public String updateStats(String var1, ArrayList<Analytics.DataInfo> var2, String var3) {
      int var4 = this.h.getInt("max_usb_devices", this.k);
      String var5 = this.a("cmd", "=", var3) + "&" + this.a("uuid", "=", var1) + "&" + this.a("device_hw", "=", Build.MANUFACTURER) + "&" + this.a("device_ver", "=", Build.MODEL) + "&" + this.a("chip_type", "=", this.b()) + "&" + this.a("sw_ver", "=", f) + "&" + this.a("max_dev", "=", String.valueOf(var4)) + "&";
      String var6 = "";

      String var9;
      for(int var7 = 0; var7 < var2.size(); var6 = var9) {
         if(var7 > 0) {
            var6 = var6 + ",";
         }

         var9 = var6 + this.a(((Analytics.DataInfo)var2.get(var7)).date(), ",", String.valueOf(((Analytics.DataInfo)var2.get(var7)).numUsages()));
         ++var7;
      }

      String var8 = var5 + this.a("dc", "=", "");
      return var8 + var6;
   }

   protected void writeObjectsToFile(String var1, ArrayList<Analytics.DataInfo> var2) throws IOException {
      ObjectOutputStream var3 = new ObjectOutputStream(new FileOutputStream(var1));
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         var3.writeObject((Analytics.DataInfo)var4.next());
      }

      var3.close();
   }

   public static class DataInfo implements Serializable {
      private final String a;
      protected int numUsages;

      public DataInfo(String var1, int var2) {
         this.a = var1;
         this.numUsages = var2;
      }

      public String date() {
         return this.a;
      }

      public int numUsages() {
         return this.numUsages;
      }
   }

   private class a extends AsyncTask {
      private a() {
      }

      // $FF: synthetic method
      a(Object var2) {
         this();
      }

      protected Object doInBackground(Object[] param1) {
         // $FF: Couldn't be decompiled
      }
   }
}
