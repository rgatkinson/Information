package com.qualcomm.hardware;

import com.qualcomm.hardware.ReadWriteRunnable;
import com.qualcomm.hardware.ReadWriteRunnableSegment;
import com.qualcomm.modernrobotics.ReadWriteRunnableUsbHandler;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.TypeConversion;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ReadWriteRunnableStandard implements ReadWriteRunnable {
   protected final boolean DEBUG_LOGGING;
   private volatile boolean a = false;
   protected ReadWriteRunnable.Callback callback;
   protected final byte[] localDeviceReadCache = new byte[256];
   protected final byte[] localDeviceWriteCache = new byte[256];
   protected int monitorLength;
   protected volatile boolean running = false;
   protected ConcurrentLinkedQueue<Integer> segmentReadQueue = new ConcurrentLinkedQueue();
   protected ConcurrentLinkedQueue<Integer> segmentWriteQueue = new ConcurrentLinkedQueue();
   protected Map<Integer, ReadWriteRunnableSegment> segments = new HashMap();
   protected final SerialNumber serialNumber;
   protected volatile boolean shutdownComplete = false;
   protected int startAddress;
   protected final ReadWriteRunnableUsbHandler usbHandler;

   public ReadWriteRunnableStandard(SerialNumber var1, RobotUsbDevice var2, int var3, int var4, boolean var5) {
      this.serialNumber = var1;
      this.startAddress = var4;
      this.monitorLength = var3;
      this.DEBUG_LOGGING = var5;
      this.callback = new ReadWriteRunnable.EmptyCallback();
      this.usbHandler = new ReadWriteRunnableUsbHandler(var2);
   }

   public void blockUntilReady() throws RobotCoreException, InterruptedException {
      if(this.shutdownComplete) {
         RobotLog.w("sync device block requested, but device is shut down - " + this.serialNumber);
         RobotLog.setGlobalErrorMsg("There were problems communicating with a Modern Robotics USB device for an extended period of time.");
         throw new RobotCoreException("cannot block, device is shut down");
      }
   }

   public void close() {
      try {
         this.blockUntilReady();
         this.startBlockingWork();
      } catch (InterruptedException var7) {
         RobotLog.w("Exception while closing USB device: " + var7.getMessage());
      } catch (RobotCoreException var8) {
         RobotLog.w("Exception while closing USB device: " + var8.getMessage());
      } finally {
         this.running = false;

         while(!this.shutdownComplete) {
            Thread.yield();
         }

      }

   }

   public ReadWriteRunnableSegment createSegment(int var1, int var2, int var3) {
      ReadWriteRunnableSegment var4 = new ReadWriteRunnableSegment(var2, var3);
      this.segments.put(Integer.valueOf(var1), var4);
      return var4;
   }

   public void destroySegment(int var1) {
      this.segments.remove(Integer.valueOf(var1));
   }

   protected void dumpBuffers(String var1, byte[] var2) {
      RobotLog.v("Dumping " + var1 + " buffers for " + this.serialNumber);
      StringBuilder var3 = new StringBuilder(1024);

      for(int var4 = 0; var4 < this.startAddress + this.monitorLength; ++var4) {
         Object[] var5 = new Object[]{Integer.valueOf(TypeConversion.unsignedByteToInt(var2[var4]))};
         var3.append(String.format(" %02x", var5));
         if((var4 + 1) % 16 == 0) {
            var3.append("\n");
         }
      }

      RobotLog.v(var3.toString());
   }

   public ReadWriteRunnableSegment getSegment(int var1) {
      return (ReadWriteRunnableSegment)this.segments.get(Integer.valueOf(var1));
   }

   protected void queueIfNotAlreadyQueued(int var1, ConcurrentLinkedQueue<Integer> var2) {
      if(!var2.contains(Integer.valueOf(var1))) {
         var2.add(Integer.valueOf(var1));
      }

   }

   public void queueSegmentRead(int var1) {
      this.queueIfNotAlreadyQueued(var1, this.segmentReadQueue);
   }

   public void queueSegmentWrite(int var1) {
      this.queueIfNotAlreadyQueued(var1, this.segmentWriteQueue);
   }

   public byte[] read(int param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public byte[] readFromWriteCache(int param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   public void run() {
      // $FF: Couldn't be decompiled
   }

   public void setCallback(ReadWriteRunnable.Callback var1) {
      this.callback = var1;
   }

   public void setWriteNeeded(boolean var1) {
      this.a = var1;
   }

   public void startBlockingWork() {
   }

   protected void waitForSyncdEvents() throws RobotCoreException, InterruptedException {
   }

   public void write(int param1, byte[] param2) {
      // $FF: Couldn't be decompiled
   }

   public boolean writeNeeded() {
      return this.a;
   }
}
