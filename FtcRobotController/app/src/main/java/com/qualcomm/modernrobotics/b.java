package com.qualcomm.modernrobotics;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

class b implements RobotUsbDevice {
   public final boolean a;
   public SerialNumber b;
   public String c;
   protected final byte[] d;
   protected final byte[] e;
   private byte[] f;
   private byte[] g;
   private BlockingQueue<byte[]> h;

   private int a(byte[] var1, int var2, int var3) {
      byte[] var5;
      if(this.g != null) {
         var5 = Arrays.copyOf(this.g, this.g.length);
         this.g = null;
      } else {
         try {
            var5 = (byte[])this.h.poll((long)var3, TimeUnit.MILLISECONDS);
         } catch (InterruptedException var6) {
            RobotLog.w("USB mock bus interrupted during read");
            var5 = null;
         }
      }

      if(var5 == null) {
         RobotLog.w("USB mock bus read timeout");
         System.arraycopy(this.e, 0, var1, 0, this.e.length);
         var1[2] = -1;
         var1[4] = 0;
      } else {
         System.arraycopy(var5, 0, var1, 0, var2);
      }

      if(var5 != null && var2 < var5.length) {
         this.g = new byte[var5.length - var2];
         System.arraycopy(var5, var1.length, this.g, 0, this.g.length);
      }

      if(this.a) {
         RobotLog.d(this.b + " USB send: " + Arrays.toString(var1));
      }

      return var1.length;
   }

   private void a(final byte[] var1) {
      if(this.a) {
         RobotLog.d(this.b + " USB recd: " + Arrays.toString(var1));
      }

      (new Thread() {
         public void run() {
            // $FF: Couldn't be decompiled
         }
      }).start();
   }

   // $FF: synthetic method
   static byte[] a(b var0) {
      return var0.f;
   }

   // $FF: synthetic method
   static BlockingQueue b(b var0) {
      return var0.h;
   }

   public void close() {
   }

   public void purge(RobotUsbDevice.Channel var1) throws RobotCoreException {
      this.h.clear();
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
