package com.qualcomm.modernrobotics;

import com.qualcomm.modernrobotics.RobotUsbDeviceEmulator;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.ArrayList;
import java.util.Iterator;

public class RobotUsbManagerEmulator implements RobotUsbManager {
   private ArrayList<RobotUsbDeviceEmulator> a = new ArrayList();

   public String getDeviceDescriptionByIndex(int var1) throws RobotCoreException {
      return ((RobotUsbDeviceEmulator)this.a.get(var1)).description;
   }

   public SerialNumber getDeviceSerialNumberByIndex(int var1) throws RobotCoreException {
      return ((RobotUsbDeviceEmulator)this.a.get(var1)).serialNumber;
   }

   public RobotUsbDevice openBySerialNumber(SerialNumber var1) throws RobotCoreException {
      RobotLog.d("attempting to open emulated device " + var1);
      Iterator var2 = this.a.iterator();

      RobotUsbDeviceEmulator var3;
      do {
         if(!var2.hasNext()) {
            throw new RobotCoreException("cannot open device - could not find device with serial number " + var1);
         }

         var3 = (RobotUsbDeviceEmulator)var2.next();
      } while(!var3.serialNumber.equals(var1));

      return var3;
   }

   public int scanForDevices() throws RobotCoreException {
      return this.a.size();
   }
}
