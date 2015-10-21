package com.qualcomm.robotcore.hardware.usb;

import com.qualcomm.robotcore.exception.RobotCoreException;

public interface RobotUsbDevice {
   void close();

   void purge(RobotUsbDevice.Channel var1) throws RobotCoreException;

   int read(byte[] data) throws RobotCoreException;

   int read(byte[] data, int length, int msTimeout) throws RobotCoreException;

   void setBaudRate(int var1) throws RobotCoreException;

   void setDataCharacteristics(byte databits, byte stopbits, byte parity) throws RobotCoreException;

   void setLatencyTimer(int ms) throws RobotCoreException;

   void write(byte[] data) throws RobotCoreException;

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
