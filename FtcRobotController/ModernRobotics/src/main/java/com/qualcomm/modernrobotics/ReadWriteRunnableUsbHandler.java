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
   protected byte[] readCmd = new byte[]{(byte)85, (byte)-86, (byte)-128, (byte)0, (byte)0};
   protected final byte[] respHeader = new byte[5];
   protected int usbSequentialReadErrorCount = 0;
   protected int usbSequentialWriteErrorCount = 0;
   protected byte[] writeCmd = new byte[]{(byte)85, (byte)-86, (byte)0, (byte)0, (byte)0};

   public ReadWriteRunnableUsbHandler(RobotUsbDevice var1) {
      this.device = var1;
   }

   private void a(int var1, byte[] var2) throws RobotCoreException {
      this.readCmd[3] = (byte)var1;
      this.readCmd[4] = (byte)var2.length;
      this.device.write(this.readCmd);
      Arrays.fill(this.respHeader, (byte)0);
      int var3 = this.device.read(this.respHeader, this.respHeader.length, 100);
      if(!ModernRoboticsPacket.a(this.respHeader, var2.length)) {
         ++this.usbSequentialReadErrorCount;
         if(var3 == this.respHeader.length) {
            this.a(this.readCmd, "comm error");
         } else {
            this.a(this.readCmd, "comm timeout");
         }
      }

      if(this.device.read(var2, var2.length, 100) != var2.length) {
         this.a(this.readCmd, "comm timeout on payload");
      }

      this.usbSequentialReadErrorCount = 0;
   }

   private void a(byte[] var1, String var2) throws RobotCoreException {
      RobotLog.w(bufferToString(var1) + " -> " + bufferToString(this.respHeader));
      this.device.purge(RobotUsbDevice.Channel.BOTH);
      throw new RobotCoreException(var2);
   }

   private void b(int var1, byte[] var2) throws RobotCoreException {
      this.writeCmd[3] = (byte)var1;
      this.writeCmd[4] = (byte)var2.length;
      this.device.write(Util.concatenateByteArrays(this.writeCmd, var2));
      Arrays.fill(this.respHeader, (byte)0);
      int var3 = this.device.read(this.respHeader, this.respHeader.length, 100);
      if(!ModernRoboticsPacket.a(this.respHeader, 0)) {
         ++this.usbSequentialWriteErrorCount;
         if(var3 == this.respHeader.length) {
            this.a(this.writeCmd, "comm error");
         } else {
            this.a(this.writeCmd, "comm timeout");
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

   public void purge(RobotUsbDevice.Channel var1) throws RobotCoreException {
      this.device.purge(var1);
   }

   public void read(int var1, byte[] var2) throws RobotCoreException {
      this.a(var1, var2);
   }

   public void throwIfUsbErrorCountIsTooHigh() throws RobotCoreException {
      if(this.usbSequentialReadErrorCount >= 10 && this.usbSequentialWriteErrorCount >= 10) {
         throw new RobotCoreException("Too many sequential USB errors on device");
      }
   }

   public void write(int var1, byte[] var2) throws RobotCoreException {
      this.b(var1, var2);
   }
}
