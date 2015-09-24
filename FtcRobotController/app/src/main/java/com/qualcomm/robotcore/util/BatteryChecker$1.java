package com.qualcomm.robotcore.util;

class BatteryChecker$1 implements Runnable {
    @Override
    public void run() {
        final float batteryLevel = BatteryChecker.this.getBatteryLevel();
        BatteryChecker.a(BatteryChecker.this).updateBatteryLevel(batteryLevel);
        RobotLog.i("Battery Checker, Level Remaining: " + batteryLevel);
        BatteryChecker.this.batteryHandler.postDelayed(BatteryChecker.this.a, BatteryChecker.b(BatteryChecker.this));
    }
}