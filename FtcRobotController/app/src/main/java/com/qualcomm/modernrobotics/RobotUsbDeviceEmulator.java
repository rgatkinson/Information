package com.qualcomm.modernrobotics;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class RobotUsbDeviceEmulator implements RobotUsbDevice {
   public static final int MFG_CODE_MODERN_ROBOTICS = 77;
   public final boolean DEBUG_LOGGING;
   private byte[] a;
   private byte[] b;
   private BlockingQueue<byte[]> c;
   public String description;
   protected final byte[] readRsp;
   public SerialNumber serialNumber;
   protected final byte[] writeRsp;

   public RobotUsbDeviceEmulator(SerialNumber var1, String var2, int var3) {
      this(var1, var2, var3, false);
   }

   public RobotUsbDeviceEmulator(SerialNumber var1, String var2, int var3, boolean var4) {
      this.a = new byte[256];
      this.b = null;
      this.c = new LinkedBlockingQueue();
      this.writeRsp = new byte[]{(byte)51, (byte)-52, (byte)0, (byte)0, (byte)0};
      this.readRsp = new byte[]{(byte)51, (byte)-52, (byte)-128, (byte)0, (byte)0};
      this.DEBUG_LOGGING = var4;
      this.serialNumber = var1;
      this.description = var2;
      this.a[0] = -1;
      this.a[1] = 77;
      this.a[2] = (byte)var3;
   }

   private int a(byte[] var1, int var2, int var3) {
      byte[] var5;
      if(this.b != null) {
         var5 = Arrays.copyOf(this.b, this.b.length);
         this.b = null;
      } else {
         try {
            var5 = (byte[])this.c.poll((long)var3, TimeUnit.MILLISECONDS);
         } catch (InterruptedException var6) {
            RobotLog.w("USB mock bus interrupted during read");
            var5 = null;
         }
      }

      if(var5 == null) {
         RobotLog.w("USB mock bus read timeout");
         System.arraycopy(this.readRsp, 0, var1, 0, this.readRsp.length);
         var1[2] = -1;
         var1[4] = 0;
      } else {
         System.arraycopy(var5, 0, var1, 0, var2);
      }

      if(var5 != null && var2 < var5.length) {
         this.b = new byte[var5.length - var2];
         System.arraycopy(var5, var1.length, this.b, 0, this.b.length);
      }

      if(this.DEBUG_LOGGING) {
         RobotLog.d(this.serialNumber + " USB send: " + Arrays.toString(var1));
      }

      return var1.length;
   }

   private void a(final byte[] var1) {
      if(this.DEBUG_LOGGING) {
         RobotLog.d(this.serialNumber + " USB recd: " + Arrays.toString(var1));
      }

      (new Thread() {
         public void run() {
            // $FF: Couldn't be decompiled
         }
      }).start();
   }

   // $FF: synthetic method
   static byte[] a(RobotUsbDeviceEmulator var0) {
      return var0.a;
   }

   // $FF: synthetic method
   static BlockingQueue b(RobotUsbDeviceEmulator var0) {
      return var0.c;
   }

   public void close() {
   }

   public void purge(RobotUsbDevice.Channel var1) throws RobotCoreException {
      this.c.clear();
   }

   public int read(byte[] var1) throws RobotCoreException {
      return this.read(var1, var1.length, Integer.MAX_VALUE);
   }

   public int read(byte[] var1, int var2, int var3) throws RobotCoreException {
      return this.a(var1, var2, var3);
   }

   public void setBaudRate(int var1) throws RobotCoreException {
   }

   public void setDataCharacteristics(byte var1, byte var2, byte var3) throws RobotCoreException {
   }

   public void setLatencyTimer(int var1) throws RobotCoreException {
   }

   public void write(byte[] var1) throws RobotCoreException {
      this.a(var1);
   }
}
