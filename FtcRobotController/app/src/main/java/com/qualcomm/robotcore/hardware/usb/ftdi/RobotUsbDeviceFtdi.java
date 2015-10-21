package com.qualcomm.robotcore.hardware.usb.ftdi;

import com.ftdi.j2xx.FT_Device;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;

public class RobotUsbDeviceFtdi implements RobotUsbDevice {
   private FT_Device ftDevice;

   public RobotUsbDeviceFtdi(FT_Device var1) {
      this.ftDevice = var1;
   }

   public void close() {
      this.ftDevice.close();
   }

   public void purge(RobotUsbDevice.Channel var1) throws RobotCoreException {
      int var2 = null.a[var1.ordinal()];
      byte var3 = 0;
      switch(var2) {
      case 1:
         var3 = 1;
         break;
      case 2:
         var3 = 2;
         break;
      case 3:
         var3 = 3;
      }

      this.ftDevice.purge(var3);
   }

   public int read(byte[] data) throws RobotCoreException {
      return this.ftDevice.read(data);
   }

   public int read(byte[] data, int length, int msTimeout) throws RobotCoreException {
      return this.ftDevice.read(data, length, (long)msTimeout);
   }

   public void setBaudRate(int var1) throws RobotCoreException {
      if(!this.ftDevice.setBaudRate(var1)) {
         throw new RobotCoreException("failed to set baud rate to " + var1);
      }
   }

   public void setDataCharacteristics(byte dataBits, byte stopBits, byte parity) throws RobotCoreException {
      if(!this.ftDevice.setDataCharacteristics(dataBits, stopBits, parity)) {
         throw new RobotCoreException("failed to set data characteristics");
      }
   }

   public void setLatencyTimer(int ms) throws RobotCoreException {
      if(!this.ftDevice.setLatencyTimer((byte)ms)) {
         throw new RobotCoreException("failed to set latency timer to " + ms);
      }
   }

   public void write(byte[] data) throws RobotCoreException {
      this.ftDevice.write(data);
   }
}
