package com.qualcomm.robotcore.hardware.usb.ftdi;

import android.content.Context;
import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
import com.qualcomm.robotcore.hardware.usb.ftdi.RobotUsbDeviceFtdi;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;

public class RobotUsbManagerFtdi implements RobotUsbManager {
   private Context a;
   private D2xxManager b;

   public RobotUsbManagerFtdi(Context var1) {
      this.a = var1;

      try {
         this.b = D2xxManager.getInstance(var1);
      } catch (D2xxManager.D2xxException var3) {
         RobotLog.e("Unable to create D2xxManager; cannot open USB devices");
      }
   }

   public String getDeviceDescriptionByIndex(int var1) throws RobotCoreException {
      return this.b.getDeviceInfoListDetail(var1).description;
   }

   public SerialNumber getDeviceSerialNumberByIndex(int var1) throws RobotCoreException {
      return new SerialNumber(this.b.getDeviceInfoListDetail(var1).serialNumber);
   }

   public RobotUsbDevice openBySerialNumber(SerialNumber var1) throws RobotCoreException {
      FT_Device var2 = this.b.openBySerialNumber(this.a, var1.toString());
      if(var2 == null) {
         throw new RobotCoreException("Unable to open USB device with serial number " + var1);
      } else {
         return new RobotUsbDeviceFtdi(var2);
      }
   }

   public int scanForDevices() throws RobotCoreException {
      return this.b.createDeviceInfoList(this.a);
   }
}
