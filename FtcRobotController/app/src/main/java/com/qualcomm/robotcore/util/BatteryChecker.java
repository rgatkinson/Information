package com.qualcomm.robotcore.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import com.qualcomm.robotcore.util.RobotLog;

public class BatteryChecker {
   Runnable a = new Runnable() {
      public void run() {
         float var1 = BatteryChecker.this.getBatteryLevel();
         BatteryChecker.this.e.updateBatteryLevel(var1);
         RobotLog.i("Battery Checker, Level Remaining: " + var1);
         BatteryChecker.this.batteryHandler.postDelayed(BatteryChecker.this.a, BatteryChecker.this.c);
      }
   };
   private Context b;
   protected Handler batteryHandler;
   private long c;
   private long d = 5000L;
   private BatteryChecker.BatteryWatcher e;

   public BatteryChecker(Context var1, BatteryChecker.BatteryWatcher var2, long var3) {
      this.b = var1;
      this.e = var2;
      this.c = var3;
      this.batteryHandler = new Handler();
   }

   public void endBatteryMonitoring() {
      this.batteryHandler.removeCallbacks(this.a);
   }

   public float getBatteryLevel() {
      int var1 = -1;
      IntentFilter var2 = new IntentFilter("android.intent.action.BATTERY_CHANGED");
      Intent var3 = this.b.registerReceiver((BroadcastReceiver)null, var2);
      int var4 = var3.getIntExtra("level", var1);
      int var5 = var3.getIntExtra("scale", var1);
      if(var4 >= 0 && var5 > 0) {
         var1 = var4 * 100 / var5;
      }

      return (float)var1;
   }

   public void startBatteryMonitoring() {
      this.batteryHandler.postDelayed(this.a, this.d);
   }

   public interface BatteryWatcher {
      void updateBatteryLevel(float var1);
   }
}
