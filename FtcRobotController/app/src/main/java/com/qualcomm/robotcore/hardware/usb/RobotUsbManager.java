package com.qualcomm.robotcore.hardware.usb;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.SerialNumber;

public interface RobotUsbManager {
   String getDeviceDescriptionByIndex(int var1) throws RobotCoreException;

   SerialNumber getDeviceSerialNumberByIndex(int var1) throws RobotCoreException;

   RobotUsbDevice openBySerialNumber(SerialNumber var1) throws RobotCoreException;

   int scanForDevices() throws RobotCoreException;
}
