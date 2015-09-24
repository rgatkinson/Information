package com.qualcomm.modernrobotics;

import java.util.Iterator;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.exception.RobotCoreException;
import java.util.ArrayList;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;

public class RobotUsbManagerEmulator implements RobotUsbManager
{
    private ArrayList<RobotUsbDeviceEmulator> a;
    
    public RobotUsbManagerEmulator() {
        this.a = new ArrayList<RobotUsbDeviceEmulator>();
    }
    
    @Override
    public String getDeviceDescriptionByIndex(final int n) throws RobotCoreException {
        return this.a.get(n).description;
    }
    
    @Override
    public SerialNumber getDeviceSerialNumberByIndex(final int n) throws RobotCoreException {
        return this.a.get(n).serialNumber;
    }
    
    @Override
    public RobotUsbDevice openBySerialNumber(final SerialNumber serialNumber) throws RobotCoreException {
        RobotLog.d("attempting to open emulated device " + serialNumber);
        for (final RobotUsbDeviceEmulator robotUsbDeviceEmulator : this.a) {
            if (robotUsbDeviceEmulator.serialNumber.equals(serialNumber)) {
                return robotUsbDeviceEmulator;
            }
        }
        throw new RobotCoreException("cannot open device - could not find device with serial number " + serialNumber);
    }
    
    @Override
    public int scanForDevices() throws RobotCoreException {
        return this.a.size();
    }
}
