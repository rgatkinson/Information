package com.qualcomm.modernrobotics;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.Util;
import java.util.Arrays;

public class ReadWriteRunnableUsbHandler {
   protected final int MAX_SEQUENTIAL_USB_ERROR_COUNT = 10;
   protected final int USB_MSG_TIMEOUT = 100;
   protected RobotUsbDevice robotUsbDevice;
   protected byte[]         readCmd = new byte[]{(byte)85, (byte)-86, (byte)-128, (byte)0, (byte)0};
   protected final byte[]   respHeader = new byte[5];
   protected int            usbSequentialReadErrorCount = 0;
   protected int            usbSequentialWriteErrorCount = 0;
   protected byte[]         writeCmd = new byte[]{(byte)85, (byte)-86, (byte)0, (byte)0, (byte)0};

   public ReadWriteRunnableUsbHandler(RobotUsbDevice var1) {
      this.robotUsbDevice = var1;
   }

   private void readInternal(int address, byte[] data) throws RobotCoreException {

      // Form the command packet with a read request and write it out
      this.readCmd[3] = (byte)address;
      this.readCmd[4] = (byte)data.length;
      this.robotUsbDevice.write(this.readCmd);

      Arrays.fill(this.respHeader, (byte) 0);
      int cbRead = this.robotUsbDevice.read(this.respHeader, this.respHeader.length, USB_MSG_TIMEOUT);

      if (!ModernRoboticsPacket.validatePacket(this.respHeader, data.length)) {
         ++this.usbSequentialReadErrorCount;
         if(cbRead == this.respHeader.length) {
            this.a(this.readCmd, "comm error");
         } else {
            this.a(this.readCmd, "comm timeout");
         }
      }

      if(this.robotUsbDevice.read(data, data.length, USB_MSG_TIMEOUT) != data.length) {
         this.a(this.readCmd, "comm timeout on payload");
      }

      this.usbSequentialReadErrorCount = 0;
   }

   private void a(byte[] var1, String var2) throws RobotCoreException {
      RobotLog.w(bufferToString(var1) + " -> " + bufferToString(this.respHeader));
      this.robotUsbDevice.purge(RobotUsbDevice.Channel.BOTH);
      throw new RobotCoreException(var2);
   }

   private void writeInternal(int address, byte[] data) throws RobotCoreException {
      // fill in the addressing information in the packet command header
      this.writeCmd[3] = (byte)address;
      this.writeCmd[4] = (byte)data.length;

      // send the data to the device
      this.robotUsbDevice.write(Util.concatenateByteArrays(this.writeCmd, data));

      // initialize and retrieve the response
      Arrays.fill(this.respHeader, (byte) 0);
      int cbReceived = this.robotUsbDevice.read(this.respHeader, this.respHeader.length, USB_MSG_TIMEOUT);

      if (!ModernRoboticsPacket.validatePacket(this.respHeader, 0)) {
         ++this.usbSequentialWriteErrorCount;
         if(cbReceived == this.respHeader.length) {
            this.a(this.writeCmd, "comm error");
         } else {
            this.a(this.writeCmd, "comm timeout");
         }
      }

      this.usbSequentialWriteErrorCount = 0;
   }

   protected static String bufferToString(byte[] buffer) {
      StringBuilder var1 = new StringBuilder();
      var1.append("[");
      if(buffer.length > 0) {
         Object[] var7 = new Object[]{Byte.valueOf(buffer[0])};
         var1.append(String.format("%02x", var7));
      }

      for(int var3 = 1; var3 < buffer.length; ++var3) {
         Object[] var5 = new Object[]{Byte.valueOf(buffer[var3])};
         var1.append(String.format(" %02x", var5));
      }

      var1.append("]");
      return var1.toString();
   }

   public void close() {
      this.robotUsbDevice.close();
   }

   public void purge(RobotUsbDevice.Channel channel) throws RobotCoreException {
      this.robotUsbDevice.purge(channel);
   }

   public void read(int address, byte[] data) throws RobotCoreException {
      this.readInternal(address, data);
   }

   public void throwIfUsbErrorCountIsTooHigh() throws RobotCoreException {
      if(this.usbSequentialReadErrorCount >= 10 && this.usbSequentialWriteErrorCount >= 10) {
         throw new RobotCoreException("Too many sequential USB errors on device");
      }
   }

   public void write(int address, byte[] data) throws RobotCoreException {
      this.writeInternal(address, data);
   }
}
