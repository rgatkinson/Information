package com.qualcomm.robotcore.util;

import android.view.InputDevice;
import java.util.HashSet;
import java.util.Set;
import android.os.Build;

public class Hardware
{
    private static boolean a;
    
    static {
        Hardware.a = CheckIfIFC();
    }
    
    public static boolean CheckIfIFC() {
        final String board = Build.BOARD;
        final String brand = Build.BRAND;
        final String device = Build.DEVICE;
        final String hardware = Build.HARDWARE;
        final String manufacturer = Build.MANUFACTURER;
        final String model = Build.MODEL;
        final String product = Build.PRODUCT;
        RobotLog.d("Platform information: board = " + board + " brand = " + brand + " device = " + device + " hardware = " + hardware + " manufacturer = " + manufacturer + " model = " + model + " product = " + product);
        if (board.equals("MSM8960") && brand.equals("qcom") && device.equals("msm8960") && hardware.equals("qcom") && manufacturer.equals("unknown") && model.equals("msm8960") && product.equals("msm8960")) {
            RobotLog.d("Detected IFC6410 Device!");
            return true;
        }
        RobotLog.d("Detected regular SmartPhone Device!");
        return false;
    }
    
    public static boolean IsIFC() {
        return Hardware.a;
    }
    
    public static Set<Integer> getGameControllerIds() {
        final HashSet<Integer> set = new HashSet<Integer>();
        for (final int n : InputDevice.getDeviceIds()) {
            final int sources = InputDevice.getDevice(n).getSources();
            if ((sources & 0x401) == 0x401 || (sources & 0x1000010) == 0x1000010) {
                set.add(n);
            }
        }
        return set;
    }
}
