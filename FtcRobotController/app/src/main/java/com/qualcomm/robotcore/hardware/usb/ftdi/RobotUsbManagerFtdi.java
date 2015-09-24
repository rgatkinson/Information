package com.qualcomm.robotcore.hardware.usb.ftdi;

import com.ftdi.j2xx.FT_Device;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;
import com.ftdi.j2xx.D2xxManager;
import android.content.Context;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;

public class RobotUsbManagerFtdi implements RobotUsbManager
{
    private Context a;
    private D2xxManager b;
    
    public RobotUsbManagerFtdi(final Context a) {
        this.a = a;
        try {
            this.b = D2xxManager.getInstance(a);
        }
        catch (D2xxManager.D2xxException ex) {
            RobotLog.e("Unable to create D2xxManager; cannot open USB devices");
        }
    }
    
    @Override
    public String getDeviceDescriptionByIndex(final int n) throws RobotCoreException {
        return this.b.getDeviceInfoListDetail(n).description;
    }
    
    @Override
    public SerialNumber getDeviceSerialNumberByIndex(final int n) throws RobotCoreException {
        return new SerialNumber(this.b.getDeviceInfoListDetail(n).serialNumber);
    }
    
    @Override
    public RobotUsbDevice openBySerialNumber(final SerialNumber serialNumber) throws RobotCoreException {
        final FT_Device openBySerialNumber = this.b.openBySerialNumber(this.a, serialNumber.toString());
        if (openBySerialNumber == null) {
            throw new RobotCoreException("Unable to open USB device with serial number " + serialNumber);
        }
        return new RobotUsbDeviceFtdi(openBySerialNumber);
    }
    
    @Override
    public int scanForDevices() throws RobotCoreException {
        return this.b.createDeviceInfoList(this.a);
    }
}
