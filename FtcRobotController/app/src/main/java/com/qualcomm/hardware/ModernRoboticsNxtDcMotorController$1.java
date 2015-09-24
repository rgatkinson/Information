package com.qualcomm.hardware;

import com.qualcomm.robotcore.hardware.DcMotorController;

static class ModernRoboticsNxtDcMotorController$1 {
    static {
        b = new int[RunMode.values().length];
        while (true) {
            try {
                ModernRoboticsNxtDcMotorController$1.b[RunMode.RUN_USING_ENCODERS.ordinal()] = 1;
                try {
                    ModernRoboticsNxtDcMotorController$1.b[RunMode.RUN_WITHOUT_ENCODERS.ordinal()] = 2;
                    try {
                        ModernRoboticsNxtDcMotorController$1.b[RunMode.RUN_TO_POSITION.ordinal()] = 3;
                        try {
                            ModernRoboticsNxtDcMotorController$1.b[RunMode.RESET_ENCODERS.ordinal()] = 4;
                            a = new int[DeviceMode.values().length];
                            try {
                                ModernRoboticsNxtDcMotorController$1.a[DeviceMode.READ_ONLY.ordinal()] = 1;
                                try {
                                    ModernRoboticsNxtDcMotorController$1.a[DeviceMode.WRITE_ONLY.ordinal()] = 2;
                                    try {
                                        ModernRoboticsNxtDcMotorController$1.a[DeviceMode.SWITCHING_TO_READ_MODE.ordinal()] = 3;
                                        try {
                                            ModernRoboticsNxtDcMotorController$1.a[DeviceMode.SWITCHING_TO_WRITE_MODE.ordinal()] = 4;
                                        }
                                        catch (NoSuchFieldError noSuchFieldError) {}
                                    }
                                    catch (NoSuchFieldError noSuchFieldError2) {}
                                }
                                catch (NoSuchFieldError noSuchFieldError3) {}
                            }
                            catch (NoSuchFieldError noSuchFieldError4) {}
                        }
                        catch (NoSuchFieldError noSuchFieldError5) {}
                    }
                    catch (NoSuchFieldError noSuchFieldError6) {}
                }
                catch (NoSuchFieldError noSuchFieldError7) {}
            }
            catch (NoSuchFieldError noSuchFieldError8) {
                continue;
            }
            break;
        }
    }
}