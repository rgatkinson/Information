package com.qualcomm.robotcore.util;

import android.os.Build;
import android.view.InputDevice;

import java.util.HashSet;
import java.util.Set;

public class Hardware {
    private static boolean a = CheckIfIFC();

    public static boolean CheckIfIFC() {
        String var0 = Build.BOARD;
        String var1 = Build.BRAND;
        String var2 = Build.DEVICE;
        String var3 = Build.HARDWARE;
        String var4 = Build.MANUFACTURER;
        String var5 = Build.MODEL;
        String var6 = Build.PRODUCT;
        RobotLog.d("Platform information: board = " + var0 + " brand = " + var1 + " device = " + var2 + " hardware = " + var3 + " manufacturer = " + var4 + " model = " + var5 + " product = " + var6);
        if (var0.equals("MSM8960") && var1.equals("qcom") && var2.equals("msm8960") && var3.equals("qcom") && var4.equals("unknown") && var5.equals("msm8960") && var6.equals("msm8960")) {
            RobotLog.d("Detected IFC6410 Device!");
            return true;
        } else {
            RobotLog.d("Detected regular SmartPhone Device!");
            return false;
        }
    }

    public static boolean IsIFC() {
        return a;
    }

    public static Set<Integer> getGameControllerIds() {
        HashSet var0 = new HashSet();
        int[] var1 = InputDevice.getDeviceIds();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            int var4 = var1[var3];
            int var5 = InputDevice.getDevice(var4).getSources();
            if ((var5 & 1025) == 1025 || (var5 & 16777232) == 16777232) {
                var0.add(Integer.valueOf(var4));
            }
        }

        return var0;
    }
}
