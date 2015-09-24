package com.qualcomm.ftccommon.configuration;

import com.qualcomm.robotcore.hardware.DeviceManager;

static class FtcConfigurationActivity$3 {
    static {
        a = new int[DeviceManager.DeviceType.values().length];
        while (true) {
            try {
                FtcConfigurationActivity$3.a[DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER.ordinal()] = 1;
                try {
                    FtcConfigurationActivity$3.a[DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER.ordinal()] = 2;
                    try {
                        FtcConfigurationActivity$3.a[DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE.ordinal()] = 3;
                        try {
                            FtcConfigurationActivity$3.a[DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE.ordinal()] = 4;
                        }
                        catch (NoSuchFieldError noSuchFieldError) {}
                    }
                    catch (NoSuchFieldError noSuchFieldError2) {}
                }
                catch (NoSuchFieldError noSuchFieldError3) {}
            }
            catch (NoSuchFieldError noSuchFieldError4) {
                continue;
            }
            break;
        }
    }
}