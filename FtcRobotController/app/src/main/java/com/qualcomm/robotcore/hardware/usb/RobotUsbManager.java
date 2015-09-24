package com.qualcomm.robotcore.hardware.usb;

import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.exception.RobotCoreException;

public interface RobotUsbManager
{
    String getDeviceDescriptionByIndex(int p0) throws RobotCoreException;
    
    SerialNumber getDeviceSerialNumberByIndex(int p0) throws RobotCoreException;
    
    RobotUsbDevice openBySerialNumber(SerialNumber p0) throws RobotCoreException;
    
    int scanForDevices() throws RobotCoreException;
}
