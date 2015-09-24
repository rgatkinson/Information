package com.qualcomm.hardware;

import com.qualcomm.hardware.ReadWriteRunnable;
import com.qualcomm.hardware.ReadWriteRunnableStandard;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReadWriteRunnableBlocking extends ReadWriteRunnableStandard {
   private volatile boolean a;
   protected final Condition blockingCondition;
   protected final Lock blockingLock = new ReentrantLock();
   protected ReadWriteRunnable.BlockingState blockingState;
   protected final Condition waitingCondition;
   protected final Lock waitingLock = new ReentrantLock();

   public ReadWriteRunnableBlocking(SerialNumber var1, RobotUsbDevice var2, int var3, int var4, boolean var5) {
      super(var1, var2, var3, var4, var5);
      this.blockingCondition = this.blockingLock.newCondition();
      this.waitingCondition = this.waitingLock.newCondition();
      this.blockingState = ReadWriteRunnable.BlockingState.BLOCKING;
      this.a = false;
   }

   public void blockUntilReady() throws RobotCoreException, InterruptedException {
      try {
         this.blockingLock.lock();

         while(this.blockingState == ReadWriteRunnable.BlockingState.BLOCKING) {
            this.blockingCondition.await(100L, TimeUnit.MILLISECONDS);
            if(this.shutdownComplete) {
               RobotLog.w("sync device block requested, but device is shut down - " + this.serialNumber);
               RobotLog.setGlobalErrorMsg("There were problems communicating with a Modern Robotics USB device for an extended period of time.");
               throw new RobotCoreException("cannot block, device is shut down");
            }
         }
      } finally {
         this.blockingLock.unlock();
      }

   }

   public void setWriteNeeded(boolean var1) {
      this.a = var1;
   }

   public void startBlockingWork() {
      try {
         this.waitingLock.lock();
         this.blockingState = ReadWriteRunnable.BlockingState.BLOCKING;
         this.waitingCondition.signalAll();
      } finally {
         this.waitingLock.unlock();
      }

   }

   protected void waitForSyncdEvents() throws RobotCoreException, InterruptedException {
      try {
         this.blockingLock.lock();
         this.blockingState = ReadWriteRunnable.BlockingState.WAITING;
         this.blockingCondition.signalAll();
      } finally {
         this.blockingLock.unlock();
      }

      try {
         this.waitingLock.lock();

         while(this.blockingState == ReadWriteRunnable.BlockingState.WAITING) {
            this.waitingCondition.await();
            if(this.shutdownComplete) {
               RobotLog.w("wait for sync\'d events requested, but device is shut down - " + this.serialNumber);
               throw new RobotCoreException("cannot block, device is shut down");
            }
         }
      } finally {
         this.waitingLock.unlock();
      }

   }

   public void write(int param1, byte[] param2) {
      // $FF: Couldn't be decompiled
   }

   public boolean writeNeeded() {
      return this.a;
   }
}
