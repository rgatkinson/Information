package com.qualcomm.hardware;

import com.qualcomm.robotcore.eventloop.SyncdDevice;
import com.qualcomm.robotcore.exception.RobotCoreException;

interface ReadWriteRunnable extends SyncdDevice, Runnable {
   void blockUntilReady() throws RobotCoreException, InterruptedException;

   void close();

   ReadWriteRunnableSegment createSegment(int var1, int var2, int var3);

   void queueSegmentRead(int var1);

   void queueSegmentWrite(int var1);

   byte[] read(int var1, int var2);

   byte[] readFromWriteCache(int var1, int var2);

   void setCallback(ReadWriteRunnable.Callback var1);

   void write(int var1, byte[] var2);

   public static enum BlockingState {
      BLOCKING,
      WAITING;

      static {
         ReadWriteRunnable.BlockingState[] var0 = new ReadWriteRunnable.BlockingState[]{BLOCKING, WAITING};
      }
   }

   public interface Callback {
      void readComplete() throws InterruptedException;

      void writeComplete() throws InterruptedException;
   }

   public static class EmptyCallback implements ReadWriteRunnable.Callback {
      public void readComplete() throws InterruptedException {
      }

      public void writeComplete() throws InterruptedException {
      }
   }
}
