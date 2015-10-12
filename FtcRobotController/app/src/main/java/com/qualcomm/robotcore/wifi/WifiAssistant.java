package com.qualcomm.robotcore.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import com.qualcomm.robotcore.util.RobotLog;

public class WifiAssistant {
   private final IntentFilter a;
   private final Context b;
   private final WifiAssistant.a c;

   public WifiAssistant(Context var1, WifiAssistant.WifiAssistantCallback var2) {
      this.b = var1;
      if(var2 == null) {
         RobotLog.v("WifiAssistantCallback is null");
      }

      this.c = new WifiAssistant.a(var2);
      this.a = new IntentFilter();
      this.a.addAction("android.net.wifi.STATE_CHANGE");
   }

   public void disable() {
      this.b.unregisterReceiver(this.c);
   }

   public void enable() {
      this.b.registerReceiver(this.c, this.a);
   }

   public interface WifiAssistantCallback {
      void wifiEventCallback(WifiAssistant.WifiState var1);
   }

   public static enum WifiState {
      CONNECTED,
      NOT_CONNECTED;

      static {
         WifiAssistant.WifiState[] var0 = new WifiAssistant.WifiState[]{CONNECTED, NOT_CONNECTED};
      }
   }

   private static class a extends BroadcastReceiver {
      private WifiAssistant.WifiState a = null;
      private final WifiAssistant.WifiAssistantCallback b;

      public a(WifiAssistant.WifiAssistantCallback var1) {
         this.b = var1;
      }

      private void a(WifiAssistant.WifiState var1) {
         if(this.a != var1) {
            this.a = var1;
            if(this.b != null) {
               this.b.wifiEventCallback(this.a);
               return;
            }
         }

      }

      public void onReceive(Context var1, Intent var2) {
         if(var2.getAction().equals("android.net.wifi.STATE_CHANGE")) {
            if(!((NetworkInfo)var2.getParcelableExtra("networkInfo")).isConnected()) {
               this.a(WifiAssistant.WifiState.NOT_CONNECTED);
               return;
            }

            this.a(WifiAssistant.WifiState.CONNECTED);
         }

      }
   }
}
