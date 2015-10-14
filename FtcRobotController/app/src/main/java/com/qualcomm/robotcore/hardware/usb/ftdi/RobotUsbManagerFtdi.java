package com.qualcomm.robotcore.hardware.usb.ftdi;

import android.content.Context;
import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;

public class RobotUsbManagerFtdi implements RobotUsbManager {
   private Context context;
   private D2xxManager d2xxManager;

   public RobotUsbManagerFtdi(Context context) {
      this.context = context;

      try {
         this.d2xxManager = D2xxManager.getInstance(context);
      } catch (D2xxManager.D2xxException var3) {
         RobotLog.e("Unable to create D2xxManager; cannot open USB devices");
      }
   }

   public String getDeviceDescriptionByIndex(int var1) throws RobotCoreException {
      return this.d2xxManager.getDeviceInfoListDetail(var1).description;
   }

   public SerialNumber getDeviceSerialNumberByIndex(int index) throws RobotCoreException {
      // It would be a good idea here to assert that the returned serial number is non-null
      return new SerialNumber(this.d2xxManager.getDeviceInfoListDetail(index).serialNumber);
   }

   public RobotUsbDevice openBySerialNumber(SerialNumber serialNumber) throws RobotCoreException {
      FT_Device ftDevice = this.d2xxManager.openBySerialNumber(this.context, serialNumber.toString());
      if(ftDevice == null) {
         throw new RobotCoreException("Unable to open USB device with serial number " + serialNumber);
      } else {
         return new RobotUsbDeviceFtdi(ftDevice);
      }
   }

   public int scanForDevices() throws RobotCoreException {
      return this.d2xxManager.createDeviceInfoList(this.context);
   }
}
