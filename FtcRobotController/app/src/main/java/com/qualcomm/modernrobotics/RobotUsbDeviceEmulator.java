package com.qualcomm.modernrobotics;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.TypeConversion;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.concurrent.BlockingQueue;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;

public class RobotUsbDeviceEmulator implements RobotUsbDevice
{
    public static final int MFG_CODE_MODERN_ROBOTICS = 77;
    public final boolean DEBUG_LOGGING;
    private byte[] a;
    private byte[] b;
    private BlockingQueue<byte[]> c;
    public String description;
    protected final byte[] readRsp;
    public SerialNumber serialNumber;
    protected final byte[] writeRsp;
    
    public RobotUsbDeviceEmulator(final SerialNumber serialNumber, final String s, final int n) {
        this(serialNumber, s, n, false);
    }
    
    public RobotUsbDeviceEmulator(final SerialNumber serialNumber, final String description, final int n, final boolean debug_LOGGING) {
        this.a = new byte[256];
        this.b = null;
        this.c = new LinkedBlockingQueue<byte[]>();
        this.writeRsp = new byte[] { 51, -52, 0, 0, 0 };
        this.readRsp = new byte[] { 51, -52, -128, 0, 0 };
        this.DEBUG_LOGGING = debug_LOGGING;
        this.serialNumber = serialNumber;
        this.description = description;
        this.a[0] = -1;
        this.a[1] = 77;
        this.a[2] = (byte)n;
    }
    
    private int a(final byte[] array, final int n, final int n2) {
        byte[] copy;
        if (this.b != null) {
            copy = Arrays.copyOf(this.b, this.b.length);
            this.b = null;
        }
        else {
            try {
                copy = this.c.poll(n2, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException ex) {
                RobotLog.w("USB mock bus interrupted during read");
                copy = null;
            }
        }
        if (copy == null) {
            RobotLog.w("USB mock bus read timeout");
            System.arraycopy(this.readRsp, 0, array, 0, this.readRsp.length);
            array[2] = -1;
            array[4] = 0;
        }
        else {
            System.arraycopy(copy, 0, array, 0, n);
        }
        if (copy != null && n < copy.length) {
            this.b = new byte[copy.length - n];
            System.arraycopy(copy, array.length, this.b, 0, this.b.length);
        }
        if (this.DEBUG_LOGGING) {
            RobotLog.d(this.serialNumber + " USB send: " + Arrays.toString(array));
        }
        return array.length;
    }
    
    private void a(final byte[] array) {
        if (this.DEBUG_LOGGING) {
            RobotLog.d(this.serialNumber + " USB recd: " + Arrays.toString(array));
        }
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    final int unsignedByteToInt = TypeConversion.unsignedByteToInt(array[3]);
                    final int unsignedByteToInt2 = TypeConversion.unsignedByteToInt(array[4]);
                    while (true) {
                        Label_0197: {
                            try {
                                Thread.sleep(10L);
                                byte[] copy = null;
                                switch (array[2]) {
                                    default: {
                                        copy = Arrays.copyOf(array, array.length);
                                        copy[2] = -1;
                                        copy[3] = array[3];
                                        copy[4] = 0;
                                        break;
                                    }
                                    case Byte.MIN_VALUE: {
                                        copy = new byte[unsignedByteToInt2 + RobotUsbDeviceEmulator.this.readRsp.length];
                                        System.arraycopy(RobotUsbDeviceEmulator.this.readRsp, 0, copy, 0, RobotUsbDeviceEmulator.this.readRsp.length);
                                        copy[3] = array[3];
                                        copy[4] = array[4];
                                        System.arraycopy(RobotUsbDeviceEmulator.this.a, unsignedByteToInt, copy, RobotUsbDeviceEmulator.this.readRsp.length, unsignedByteToInt2);
                                        break;
                                    }
                                    case 0: {
                                        break Label_0197;
                                    }
                                }
                                RobotUsbDeviceEmulator.this.c.put(copy);
                                return;
                            }
                            catch (InterruptedException ex) {
                                RobotLog.w("USB mock bus interrupted during write");
                                return;
                            }
                        }
                        byte[] copy = new byte[RobotUsbDeviceEmulator.this.writeRsp.length];
                        System.arraycopy(RobotUsbDeviceEmulator.this.writeRsp, 0, copy, 0, RobotUsbDeviceEmulator.this.writeRsp.length);
                        copy[3] = array[3];
                        copy[4] = 0;
                        System.arraycopy(array, 5, RobotUsbDeviceEmulator.this.a, unsignedByteToInt, unsignedByteToInt2);
                        continue;
                    }
                }
            }
        }.start();
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public void purge(final Channel channel) throws RobotCoreException {
        this.c.clear();
    }
    
    @Override
    public int read(final byte[] array) throws RobotCoreException {
        return this.read(array, array.length, Integer.MAX_VALUE);
    }
    
    @Override
    public int read(final byte[] array, final int n, final int n2) throws RobotCoreException {
        return this.a(array, n, n2);
    }
    
    @Override
    public void setBaudRate(final int n) throws RobotCoreException {
    }
    
    @Override
    public void setDataCharacteristics(final byte b, final byte b2, final byte b3) throws RobotCoreException {
    }
    
    @Override
    public void setLatencyTimer(final int n) throws RobotCoreException {
    }
    
    @Override
    public void write(final byte[] array) throws RobotCoreException {
        this.a(array);
    }
}
