package com.qualcomm.robotcore.util;

import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Handler;
import android.content.Context;

public class BatteryChecker
{
    Runnable a;
    private Context b;
    protected Handler batteryHandler;
    private long c;
    private long d;
    private BatteryWatcher e;
    
    public BatteryChecker(final Context b, final BatteryWatcher e, final long c) {
        this.d = 5000L;
        this.a = new Runnable() {
            @Override
            public void run() {
                final float batteryLevel = BatteryChecker.this.getBatteryLevel();
                BatteryChecker.this.e.updateBatteryLevel(batteryLevel);
                RobotLog.i("Battery Checker, Level Remaining: " + batteryLevel);
                BatteryChecker.this.batteryHandler.postDelayed(BatteryChecker.this.a, BatteryChecker.this.c);
            }
        };
        this.b = b;
        this.e = e;
        this.c = c;
        this.batteryHandler = new Handler();
    }
    
    public void endBatteryMonitoring() {
        this.batteryHandler.removeCallbacks(this.a);
    }
    
    public float getBatteryLevel() {
        int n = -1;
        final Intent registerReceiver = this.b.registerReceiver((BroadcastReceiver)null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        final int intExtra = registerReceiver.getIntExtra("level", n);
        final int intExtra2 = registerReceiver.getIntExtra("scale", n);
        if (intExtra >= 0 && intExtra2 > 0) {
            n = intExtra * 100 / intExtra2;
        }
        return n;
    }
    
    public void startBatteryMonitoring() {
        this.batteryHandler.postDelayed(this.a, this.d);
    }
    
    public interface BatteryWatcher
    {
        void updateBatteryLevel(float p0);
    }
}
