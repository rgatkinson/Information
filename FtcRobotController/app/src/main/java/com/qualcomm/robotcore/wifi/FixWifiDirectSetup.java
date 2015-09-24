package com.qualcomm.robotcore.wifi;

import android.net.wifi.WifiManager;

public class FixWifiDirectSetup {
   public static final int WIFI_TOGGLE_DELAY = 2000;

   private static void a(boolean var0, WifiManager var1) throws InterruptedException {
      var1.setWifiEnabled(var0);
      Thread.sleep(2000L);
   }

   public static void fixWifiDirectSetup(WifiManager var0) throws InterruptedException {
      a(false, var0);
      a(true, var0);
   }
}
