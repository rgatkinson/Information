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
import com.qualcomm.robotcore.util.RobotLog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
import java.util.Locale;
import java.util.UUID;
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
   public static int MAX_ENTRIES_SIZE = 100;
   public static final String RC_COMMAND_STRING = "update_rc";
   public static int TRIMMED_SIZE = 90;
   public static final String UUID_PATH = ".analytics_id";
   static String a = "https://ftcdc.qualcomm.com/DataApi";
   static long b;
   static UUID c = null;
   static String d;
   static String f = "";
   static final HostnameVerifier k = new HostnameVerifier() {
      public boolean verify(String var1, SSLSession var2) {
         return true;
      }
   };
   private static final Charset l = Charset.forName("UTF-8");
   String e;
   Context g;
   SharedPreferences h;
   boolean i;
   long j;

   public Analytics(Context param1, String param2) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   static String a(Analytics var0, String var1, String var2, String var3) {
      return var0.a(var1, var2, var3);
   }

   // $FF: synthetic method
   static String a(String var0, int var1) {
      return b(var0, var1);
   }

   private String a(String var1, String var2, String var3) {
      try {
         String var5 = URLEncoder.encode(var1, l.name()) + var2 + URLEncoder.encode(var3, l.name());
         return var5;
      } catch (UnsupportedEncodingException var6) {
         RobotLog.i("Analytics caught an UnsupportedEncodingException");
         return "";
      }
   }

   private void a() {
      Editor var1 = this.h.edit();
      var1.putLong("last_upload_date", this.j);
      var1.apply();
      (new File(EXTERNAL_STORAGE_DIRECTORY_PATH + ".ftcdc")).delete();
      (new File(EXTERNAL_STORAGE_DIRECTORY_PATH + ".analytics_id")).delete();
      this.i = false;
   }

   private String b() {
      // $FF: Couldn't be decompiled
   }

   private static String b(String var0, int var1) {
      return var0.substring(1 + findNthIndex(var0, var1, ']'));
   }

   private static Analytics.DateCount c(String var0, int var1) {
      String[] var2 = var0.substring(var1).replace("[", "").replace("]", "").split(" ");
      return new Analytics.DateCount(var2[0].trim(), var2[1].trim());
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

   public static int findNthIndex(String var0, int var1, char var2) {
      int var3;
      for(var3 = var0.indexOf(var2, 0); var1 > 0 && var3 != -1; --var1) {
         var3 = var0.indexOf(var2, var3 + 1);
      }

      return var3;
   }

   public static String getDateFromTime(long var0) {
      return (new SimpleDateFormat("yyyy-MM-dd", Locale.US)).format(new Date(var0));
   }

   protected static UUID getUuid() {
      return c;
   }

   protected static ArrayList<Analytics.DateCount> parseDateCountFile(String var0) {
      ArrayList var1;
      if(var0 != null && !var0.isEmpty()) {
         var1 = new ArrayList();
         String[] var2 = var0.split("]");
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String[] var5 = var2[var4].replace("[", "").trim().split(" ");
            if(var5.length == 2) {
               var1.add(new Analytics.DateCount(var5[0], var5[1]));
            }
         }
      } else {
         var1 = null;
      }

      return var1;
   }

   public static String ping(URL var0, String var1) {
      return call(var0, var1);
   }

   public static void setApplicationName(String var0) {
      d = var0;
   }

   public void communicateWithServer() {
      String[] var1 = new String[]{a};
      (new Analytics.a(null)).execute((Object[])var1);
   }

   protected void handleCreateNewFile(String param1, String param2) {
      // $FF: Couldn't be decompiled
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

   public String incrementAndSetCount(String var1, String var2) {
      File var3 = new File(var1);
      if(!var3.exists()) {
         return "[" + var2 + " 1] ";
      } else {
         String var4 = this.readFromFile(var3);
         if(findNthIndex(var4, MAX_ENTRIES_SIZE, ']') > 0) {
            var4 = b(var4, MAX_ENTRIES_SIZE - TRIMMED_SIZE);
         }

         int var5 = var4.lastIndexOf("[");
         if(var5 < 0) {
            return "[" + var2 + " 1] ";
         } else {
            Analytics.DateCount var6 = c(var4, var5);
            if(getDateFromTime(b).equals(var6.date())) {
               int var7 = 1 + Integer.parseInt(var6.count());
               String var8 = var4.substring(0, var5);
               return var8.trim() + " [" + var6.date() + " " + Integer.toString(var7) + "] ";
            } else {
               return var4 + " [" + var2 + " " + "1] ";
            }
         }
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

   public void register() {
      this.g.registerReceiver(this, new IntentFilter("android.net.wifi.STATE_CHANGE"));
   }

   public void unregister() {
      this.g.unregisterReceiver(this);
   }

   public String updateStats(String var1, ArrayList<Analytics.DateCount> var2, String var3) {
      String var4 = this.a("cmd", "=", var3) + "&" + this.a("uuid", "=", var1) + "&" + this.a("device_hw", "=", Build.MANUFACTURER) + "&" + this.a("device_ver", "=", Build.MODEL) + "&" + this.a("chip_type", "=", this.b()) + "&" + this.a("sw_ver", "=", f) + "&";
      String var5 = "";

      String var8;
      for(int var6 = 0; var6 < var2.size(); var5 = var8) {
         if(var6 > 0) {
            var5 = var5 + ",";
         }

         var8 = var5 + this.a(((Analytics.DateCount)var2.get(var6)).date(), ",", ((Analytics.DateCount)var2.get(var6)).count());
         ++var6;
      }

      String var7 = var4 + this.a("dc", "=", "");
      return var7 + var5;
   }

   public static class DateCount {
      private final String a;
      private final String b;

      public DateCount(String var1, String var2) {
         this.a = var1;
         this.b = var2;
      }

      public String count() {
         return this.b;
      }

      public String date() {
         return this.a;
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
