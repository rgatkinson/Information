package com.qualcomm.modernrobotics;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.Util;
import java.util.Arrays;

public class ReadWriteRunnableUsbHandler {
   protected final int MAX_SEQUENTIAL_USB_ERROR_COUNT = 10;
   protected final int USB_MSG_TIMEOUT = 100;
   protected RobotUsbDevice device;
   protected byte[] readCmd = new byte[]{(byte)0x55, (byte)0xAA, (byte)0x80, (byte)0, (byte)0};
   protected final byte[] respHeader = new byte[5];
   protected int usbSequentialReadErrorCount = 0;
   protected int usbSequentialWriteErrorCount = 0;
   protected byte[] writeCmd = new byte[]{(byte)85, (byte)-86, (byte)0, (byte)0, (byte)0};

   public ReadWriteRunnableUsbHandler(RobotUsbDevice device) {
      this.device = device;
   }

   private void internalRead(int regAddress, byte[] buffer) throws RobotCoreException, InterruptedException {

      this.readCmd[3] = (byte)regAddress;
      this.readCmd[4] = (byte)buffer.length;
      this.device.write(this.readCmd);

      Arrays.fill(this.respHeader, (byte) 0);
      int cbHeaderRead = this.device.read(this.respHeader, this.respHeader.length, 100);
      if(!ModernRoboticsUsbHeaderVerifier.verifyHeader(this.respHeader, buffer.length)) {
         ++this.usbSequentialReadErrorCount;
         if(cbHeaderRead == this.respHeader.length) {
            Thread.sleep(100L);
            this.logPurgeAndThrow(this.readCmd, "comm error");
         } else {
            this.logPurgeAndThrow(this.readCmd, "comm timeout");
         }
      }

      if (this.device.read(buffer, buffer.length, 100) != buffer.length) {
         this.logPurgeAndThrow(this.readCmd, "comm timeout on payload");
      }

      this.usbSequentialReadErrorCount = 0;
   }

   private void logPurgeAndThrow(byte[] var1, String var2) throws RobotCoreException {
      RobotLog.w(bufferToString(var1) + " -> " + bufferToString(this.respHeader));
      this.device.purge(RobotUsbDevice.Channel.BOTH);
      throw new RobotCoreException(var2);
   }

   private void internalWrite(int regAddress, byte[] buffer) throws RobotCoreException, InterruptedException {
      this.writeCmd[3] = (byte)regAddress;
      this.writeCmd[4] = (byte)buffer.length;
      this.device.write(Util.concatenateByteArrays(this.writeCmd, buffer));

      Arrays.fill(this.respHeader, (byte) 0);
      int cbHeaderRead = this.device.read(this.respHeader, this.respHeader.length, 100);
      if(!ModernRoboticsUsbHeaderVerifier.verifyHeader(this.respHeader, 0)) {
         ++this.usbSequentialWriteErrorCount;
         if(cbHeaderRead == this.respHeader.length) {
            Thread.sleep(100L);  // allow more data to arrive before purge?
            this.logPurgeAndThrow(this.writeCmd, "comm error");
         } else {
            this.logPurgeAndThrow(this.writeCmd, "comm timeout");
         }
      }

      this.usbSequentialWriteErrorCount = 0;
   }

   protected static String bufferToString(byte[] var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append("[");
      if(var0.length > 0) {
         Object[] var7 = new Object[]{Byte.valueOf(var0[0])};
         var1.append(String.format("%02x", var7));
      }

      for(int var3 = 1; var3 < var0.length; ++var3) {
         Object[] var5 = new Object[]{Byte.valueOf(var0[var3])};
         var1.append(String.format(" %02x", var5));
      }

      var1.append("]");
      return var1.toString();
   }

   public void close() {
      this.device.close();
   }

   public void purge(RobotUsbDevice.Channel channel) throws RobotCoreException {
      this.device.purge(channel);
   }

   public void read(int regAddress, byte[] buffer) throws RobotCoreException, InterruptedException {
      this.internalRead(regAddress, buffer);
   }

   public void throwIfUsbErrorCountIsTooHigh() throws RobotCoreException {
      if(this.usbSequentialReadErrorCount > 10 || this.usbSequentialWriteErrorCount > 10) {
         throw new RobotCoreException("Too many sequential USB errors on device");
      }
   }

   public void write(int regAddress, byte[] buffer) throws RobotCoreException, InterruptedException {
      this.internalWrite(regAddress, buffer);
   }
}
