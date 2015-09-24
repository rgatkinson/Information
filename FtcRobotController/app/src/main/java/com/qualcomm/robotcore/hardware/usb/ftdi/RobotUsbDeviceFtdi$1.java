package com.qualcomm.robotcore.hardware.usb.ftdi;

import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;

static class RobotUsbDeviceFtdi$1 {
    static {
        a = new int[Channel.values().length];
        while (true) {
            try {
                RobotUsbDeviceFtdi$1.a[Channel.RX.ordinal()] = 1;
                try {
                    RobotUsbDeviceFtdi$1.a[Channel.TX.ordinal()] = 2;
                    try {
                        RobotUsbDeviceFtdi$1.a[Channel.BOTH.ordinal()] = 3;
                    }
                    catch (NoSuchFieldError noSuchFieldError) {}
                }
                catch (NoSuchFieldError noSuchFieldError2) {}
            }
            catch (NoSuchFieldError noSuchFieldError3) {
                continue;
            }
            break;
        }
    }
}