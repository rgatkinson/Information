package com.qualcomm.modernrobotics;

import com.qualcomm.robotcore.util.Util;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.exception.RobotCoreException;
import java.util.Arrays;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;

public class ReadWriteRunnableUsbHandler
{
    protected final int MAX_SEQUENTIAL_USB_ERROR_COUNT;
    protected final int USB_MSG_TIMEOUT;
    protected RobotUsbDevice device;
    protected byte[] readCmd;
    protected final byte[] respHeader;
    protected int usbSequentialReadErrorCount;
    protected int usbSequentialWriteErrorCount;
    protected byte[] writeCmd;
    
    public ReadWriteRunnableUsbHandler(final RobotUsbDevice device) {
        this.MAX_SEQUENTIAL_USB_ERROR_COUNT = 10;
        this.USB_MSG_TIMEOUT = 100;
        this.usbSequentialReadErrorCount = 0;
        this.usbSequentialWriteErrorCount = 0;
        this.respHeader = new byte[5];
        this.writeCmd = new byte[] { 85, -86, 0, 0, 0 };
        this.readCmd = new byte[] { 85, -86, -128, 0, 0 };
        this.device = device;
    }
    
    private void a(final int n, final byte[] array) throws RobotCoreException {
        this.readCmd[3] = (byte)n;
        this.readCmd[4] = (byte)array.length;
        this.device.write(this.readCmd);
        Arrays.fill(this.respHeader, (byte)0);
        final int read = this.device.read(this.respHeader, this.respHeader.length, 100);
        if (!ModernRoboticsPacket.a(this.respHeader, array.length)) {
            ++this.usbSequentialReadErrorCount;
            if (read == this.respHeader.length) {
                this.a(this.readCmd, "comm error");
            }
            else {
                this.a(this.readCmd, "comm timeout");
            }
        }
        if (this.device.read(array, array.length, 100) != array.length) {
            this.a(this.readCmd, "comm timeout on payload");
        }
        this.usbSequentialReadErrorCount = 0;
    }
    
    private void a(final byte[] array, final String s) throws RobotCoreException {
        RobotLog.w(bufferToString(array) + " -> " + bufferToString(this.respHeader));
        this.device.purge(RobotUsbDevice.Channel.BOTH);
        throw new RobotCoreException(s);
    }
    
    private void b(final int n, final byte[] array) throws RobotCoreException {
        this.writeCmd[3] = (byte)n;
        this.writeCmd[4] = (byte)array.length;
        this.device.write(Util.concatenateByteArrays(this.writeCmd, array));
        Arrays.fill(this.respHeader, (byte)0);
        final int read = this.device.read(this.respHeader, this.respHeader.length, 100);
        if (!ModernRoboticsPacket.a(this.respHeader, 0)) {
            ++this.usbSequentialWriteErrorCount;
            if (read == this.respHeader.length) {
                this.a(this.writeCmd, "comm error");
            }
            else {
                this.a(this.writeCmd, "comm timeout");
            }
        }
        this.usbSequentialWriteErrorCount = 0;
    }
    
    protected static String bufferToString(final byte[] array) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (array.length > 0) {
            sb.append(String.format("%02x", array[0]));
        }
        for (int i = 1; i < array.length; ++i) {
            sb.append(String.format(" %02x", array[i]));
        }
        sb.append("]");
        return sb.toString();
    }
    
    public void close() {
        this.device.close();
    }
    
    public void purge(final RobotUsbDevice.Channel channel) throws RobotCoreException {
        this.device.purge(channel);
    }
    
    public void read(final int n, final byte[] array) throws RobotCoreException {
        this.a(n, array);
    }
    
    public void throwIfUsbErrorCountIsTooHigh() throws RobotCoreException {
        if (this.usbSequentialReadErrorCount >= 10 && this.usbSequentialWriteErrorCount >= 10) {
            throw new RobotCoreException("Too many sequential USB errors on device");
        }
    }
    
    public void write(final int n, final byte[] array) throws RobotCoreException {
        this.b(n, array);
    }
}
