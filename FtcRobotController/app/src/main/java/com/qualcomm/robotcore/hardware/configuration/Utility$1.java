package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.hardware.DeviceManager;

static class Utility$1 {
    static {
        a = new int[DeviceManager.DeviceType.values().length];
        while (true) {
            try {
                Utility$1.a[DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER.ordinal()] = 1;
                try {
                    Utility$1.a[DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER.ordinal()] = 2;
                    try {
                        Utility$1.a[DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE.ordinal()] = 3;
                        try {
                            Utility$1.a[DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE.ordinal()] = 4;
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