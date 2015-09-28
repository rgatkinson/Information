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

   public void purge(RobotUsbDevice.Channel channel) throws RobotCoreException {
      int var2 = channel.ordinal();
      byte ftDeviceChannel = 0;
      switch(var2) {
      case 1:
         ftDeviceChannel = 1;
         break;
      case 2:
         ftDeviceChannel = 2;
         break;
      case 3:
         ftDeviceChannel = 3;
      }

      this.ftDevice.purge(ftDeviceChannel);
   }

   public int read(byte[] data) throws RobotCoreException {
      return this.ftDevice.read(data);
   }

   public int read(byte[] data, int length, int msWait) throws RobotCoreException {
      return this.ftDevice.read(data, length, (long)msWait);
   }

   public void setBaudRate(int var1) throws RobotCoreException {
      if(!this.ftDevice.setBaudRate(var1)) {
         throw new RobotCoreException("failed to set baud rate to " + var1);
      }
   }

   public void setDataCharacteristics(byte var1, byte var2, byte var3) throws RobotCoreException {
      if(!this.ftDevice.setDataCharacteristics(var1, var2, var3)) {
         throw new RobotCoreException("failed to set data characteristics");
      }
   }

   public void setLatencyTimer(int var1) throws RobotCoreException {
      if(!this.ftDevice.setLatencyTimer((byte)var1)) {
         throw new RobotCoreException("failed to set latency timer to " + var1);
      }
   }

   public void write(byte[] packet) throws RobotCoreException {
      this.ftDevice.write(packet);
   }
}
