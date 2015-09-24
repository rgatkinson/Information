package com.qualcomm.modernrobotics;

import com.qualcomm.robotcore.util.RobotLog;
import java.util.Arrays;
import com.qualcomm.robotcore.util.TypeConversion;

class RobotUsbDeviceEmulator$1 extends Thread {
    final /* synthetic */ byte[] a;
    
    @Override
    public void run() {
        while (true) {
            final int unsignedByteToInt = TypeConversion.unsignedByteToInt(this.a[3]);
            final int unsignedByteToInt2 = TypeConversion.unsignedByteToInt(this.a[4]);
            while (true) {
                Label_0197: {
                    try {
                        Thread.sleep(10L);
                        byte[] copy = null;
                        switch (this.a[2]) {
                            default: {
                                copy = Arrays.copyOf(this.a, this.a.length);
                                copy[2] = -1;
                                copy[3] = this.a[3];
                                copy[4] = 0;
                                break;
                            }
                            case Byte.MIN_VALUE: {
                                copy = new byte[unsignedByteToInt2 + RobotUsbDeviceEmulator.this.readRsp.length];
                                System.arraycopy(RobotUsbDeviceEmulator.this.readRsp, 0, copy, 0, RobotUsbDeviceEmulator.this.readRsp.length);
                                copy[3] = this.a[3];
                                copy[4] = this.a[4];
                                System.arraycopy(RobotUsbDeviceEmulator.a(RobotUsbDeviceEmulator.this), unsignedByteToInt, copy, RobotUsbDeviceEmulator.this.readRsp.length, unsignedByteToInt2);
                                break;
                            }
                            case 0: {
                                break Label_0197;
                            }
                        }
                        RobotUsbDeviceEmulator.b(RobotUsbDeviceEmulator.this).put(copy);
                        return;
                    }
                    catch (InterruptedException ex) {
                        RobotLog.w("USB mock bus interrupted during write");
                        return;
                    }
                }
                byte[] copy = new byte[RobotUsbDeviceEmulator.this.writeRsp.length];
                System.arraycopy(RobotUsbDeviceEmulator.this.writeRsp, 0, copy, 0, RobotUsbDeviceEmulator.this.writeRsp.length);
                copy[3] = this.a[3];
                copy[4] = 0;
                System.arraycopy(this.a, 5, RobotUsbDeviceEmulator.a(RobotUsbDeviceEmulator.this), unsignedByteToInt, unsignedByteToInt2);
                continue;
            }
        }
    }
}