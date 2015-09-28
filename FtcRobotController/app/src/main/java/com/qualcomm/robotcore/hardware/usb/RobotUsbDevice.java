package com.qualcomm.robotcore.hardware.usb;

import com.qualcomm.robotcore.exception.RobotCoreException;

// Idea: create a shim implementation of this interface so we can see read() and write()
// requests as they fly by
public interface RobotUsbDevice {
   void close();

   void purge(RobotUsbDevice.Channel channel) throws RobotCoreException;

   int read(byte[] var1) throws RobotCoreException;

   int read(byte[] var1, int var2, int var3) throws RobotCoreException;

   void setBaudRate(int var1) throws RobotCoreException;

   void setDataCharacteristics(byte var1, byte var2, byte var3) throws RobotCoreException;

   void setLatencyTimer(int var1) throws RobotCoreException;

   void write(byte[] packet) throws RobotCoreException;

   public static enum Channel {
      BOTH,
      NONE,
      RX,
      TX;

      static {
         RobotUsbDevice.Channel[] channels = new RobotUsbDevice.Channel[]{RX, TX, NONE, BOTH};
      }
   }
}
