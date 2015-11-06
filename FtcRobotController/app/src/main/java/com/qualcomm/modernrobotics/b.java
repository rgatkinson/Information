//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.modernrobotics;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice.Channel;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.TypeConversion;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

class b implements RobotUsbDevice {
    public final boolean a;
    public SerialNumber b;
    public String c;
    private byte[] f;
    private byte[] g;
    private BlockingQueue<byte[]> h;
    protected final byte[] d;
    protected final byte[] e;

    public void setBaudRate(int rate) throws RobotCoreException {
    }

    public void setDataCharacteristics(byte dataBits, byte stopBits, byte parity) throws RobotCoreException {
    }

    public void setLatencyTimer(int latencyTimer) throws RobotCoreException {
    }

    public void purge(Channel channel) throws RobotCoreException {
        this.h.clear();
    }

    public void write(byte[] data) throws RobotCoreException {
        this.a(data);
    }

    public int read(byte[] data) throws RobotCoreException {
        return this.read(data, data.length, 2147483647);
    }

    public int read(byte[] data, int length, int timeout) throws RobotCoreException {
        return this.a(data, length, timeout);
    }

    public void close() {
    }

    private void a(final byte[] var1) {
        if(this.a) {
            RobotLog.d(this.b + " USB recd: " + Arrays.toString(var1));
        }

        (new Thread() {
            public void run() {
                int var1x = TypeConversion.unsignedByteToInt(var1[3]);
                int var2 = TypeConversion.unsignedByteToInt(var1[4]);

                try {
                    Thread.sleep(10L);
                    byte[] var3;
                    switch(var1[2]) {
                    case -128:
                        var3 = new byte[b.this.e.length + var2];
                        System.arraycopy(b.this.e, 0, var3, 0, b.this.e.length);
                        var3[3] = var1[3];
                        var3[4] = var1[4];
                        System.arraycopy(b.this.f, var1x, var3, b.this.e.length, var2);
                        break;
                    case 0:
                        var3 = new byte[b.this.d.length];
                        System.arraycopy(b.this.d, 0, var3, 0, b.this.d.length);
                        var3[3] = var1[3];
                        var3[4] = 0;
                        System.arraycopy(var1, 5, b.this.f, var1x, var2);
                        break;
                    default:
                        var3 = Arrays.copyOf(var1, var1.length);
                        var3[2] = -1;
                        var3[3] = var1[3];
                        var3[4] = 0;
                    }

                    b.this.h.put(var3);
                } catch (InterruptedException var4) {
                    RobotLog.w("USB mock bus interrupted during write");
                }

            }
        }).start();
    }

    private int a(byte[] var1, int var2, int var3) {
        byte[] var4 = null;
        if(this.g != null) {
            var4 = Arrays.copyOf(this.g, this.g.length);
            this.g = null;
        } else {
            try {
                var4 = (byte[])this.h.poll((long)var3, TimeUnit.MILLISECONDS);
            } catch (InterruptedException var6) {
                RobotLog.w("USB mock bus interrupted during read");
            }
        }

        if(var4 == null) {
            RobotLog.w("USB mock bus read timeout");
            System.arraycopy(this.e, 0, var1, 0, this.e.length);
            var1[2] = -1;
            var1[4] = 0;
        } else {
            System.arraycopy(var4, 0, var1, 0, var2);
        }

        if(var4 != null && var2 < var4.length) {
            this.g = new byte[var4.length - var2];
            System.arraycopy(var4, var1.length, this.g, 0, this.g.length);
        }

        if(this.a) {
            RobotLog.d(this.b + " USB send: " + Arrays.toString(var1));
        }

        return var1.length;
    }
}
