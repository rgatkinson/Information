package com.qualcomm.robotcore.hardware.usb.ftdi;

import com.ftdi.j2xx.FT_Device;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;

public class RobotUsbDeviceFtdi implements RobotUsbDevice {
   private FT_Device a;

   public RobotUsbDeviceFtdi(FT_Device var1) {
      this.a = var1;
   }

   public void close() {
      this.a.close();
   }

   public void purge(RobotUsbDevice.Channel var1) throws RobotCoreException {
      int var2 = var1.ordinal();
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

      this.a.purge(var3);
   }

   public int read(byte[] var1) throws RobotCoreException {
      return this.a.read(var1);
   }

   public int read(byte[] var1, int var2, int var3) throws RobotCoreException {
      return this.a.read(var1, var2, (long)var3);
   }

   public void setBaudRate(int var1) throws RobotCoreException {
      if(!this.a.setBaudRate(var1)) {
         throw new RobotCoreException("failed to set baud rate to " + var1);
      }
   }

   public void setDataCharacteristics(byte var1, byte var2, byte var3) throws RobotCoreException {
      if(!this.a.setDataCharacteristics(var1, var2, var3)) {
         throw new RobotCoreException("failed to set data characteristics");
      }
   }

   public void setLatencyTimer(int var1) throws RobotCoreException {
      if(!this.a.setLatencyTimer((byte)var1)) {
         throw new RobotCoreException("failed to set latency timer to " + var1);
      }
   }

   public void write(byte[] var1) throws RobotCoreException {
      this.a.write(var1);
   }
}
