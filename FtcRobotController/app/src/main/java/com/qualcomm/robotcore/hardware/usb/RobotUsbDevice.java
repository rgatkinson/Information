package com.qualcomm.robotcore.hardware.usb;

import com.qualcomm.robotcore.exception.RobotCoreException;

public interface RobotUsbDevice {
   void close();

   void purge(RobotUsbDevice.Channel channel) throws RobotCoreException;

   int read(byte[] var1) throws RobotCoreException;

   int read(byte[] var1, int var2, int var3) throws RobotCoreException;

   void setBaudRate(int var1) throws RobotCoreException;

   void setDataCharacteristics(byte var1, byte var2, byte var3) throws RobotCoreException;

   void setLatencyTimer(int var1) throws RobotCoreException;

   void write(byte[] var1) throws RobotCoreException;

   public static enum Channel {
      BOTH,
      NONE,
      RX,
      TX;

      static {
         RobotUsbDevice.Channel[] var0 = new RobotUsbDevice.Channel[]{RX, TX, NONE, BOTH};
      }
   }
}
