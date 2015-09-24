package com.qualcomm.hardware;

import com.qualcomm.robotcore.hardware.DcMotorController;

static class ModernRoboticsUsbDcMotorController$1 {
    static {
        a = new int[RunMode.values().length];
        while (true) {
            try {
                ModernRoboticsUsbDcMotorController$1.a[RunMode.RUN_USING_ENCODERS.ordinal()] = 1;
                try {
                    ModernRoboticsUsbDcMotorController$1.a[RunMode.RUN_WITHOUT_ENCODERS.ordinal()] = 2;
                    try {
                        ModernRoboticsUsbDcMotorController$1.a[RunMode.RUN_TO_POSITION.ordinal()] = 3;
                        try {
                            ModernRoboticsUsbDcMotorController$1.a[RunMode.RESET_ENCODERS.ordinal()] = 4;
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