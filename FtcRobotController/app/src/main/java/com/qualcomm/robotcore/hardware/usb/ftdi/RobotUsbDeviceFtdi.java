package com.qualcomm.robotcore.hardware.usb.ftdi;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.ftdi.j2xx.FT_Device;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;

public class RobotUsbDeviceFtdi implements RobotUsbDevice
{
    private FT_Device a;
    
    public RobotUsbDeviceFtdi(final FT_Device a) {
        this.a = a;
    }
    
    @Override
    public void close() {
        this.a.close();
    }
    
    @Override
    public void purge(final Channel channel) throws RobotCoreException {
        final int n = RobotUsbDeviceFtdi$1.a[channel.ordinal()];
        byte b = 0;
        switch (n) {
            case 1: {
                b = 1;
                break;
            }
            case 2: {
                b = 2;
                break;
            }
            case 3: {
                b = 3;
                break;
            }
        }
        this.a.purge(b);
    }
    
    @Override
    public int read(final byte[] array) throws RobotCoreException {
        return this.a.read(array);
    }
    
    @Override
    public int read(final byte[] array, final int n, final int n2) throws RobotCoreException {
        return this.a.read(array, n, n2);
    }
    
    @Override
    public void setBaudRate(final int baudRate) throws RobotCoreException {
        if (!this.a.setBaudRate(baudRate)) {
            throw new RobotCoreException("failed to set baud rate to " + baudRate);
        }
    }
    
    @Override
    public void setDataCharacteristics(final byte b, final byte b2, final byte b3) throws RobotCoreException {
        if (!this.a.setDataCharacteristics(b, b2, b3)) {
            throw new RobotCoreException("failed to set data characteristics");
        }
    }
    
    @Override
    public void setLatencyTimer(final int n) throws RobotCoreException {
        if (!this.a.setLatencyTimer((byte)n)) {
            throw new RobotCoreException("failed to set latency timer to " + n);
        }
    }
    
    @Override
    public void write(final byte[] array) throws RobotCoreException {
        this.a.write(array);
    }
}
