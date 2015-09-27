package com.qualcomm.hardware;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReadWriteRunnableSegment {
   final Lock readLock;
   final Lock writeLock;
   private int address;
   private final byte[] readBuffer;
   private final byte[] writeBuffer;

   public ReadWriteRunnableSegment(int address, int cb) {
      this.address = address;
      this.readLock = new ReentrantLock();
      this.readBuffer = new byte[cb];
      this.writeLock = new ReentrantLock();
      this.writeBuffer = new byte[cb];
   }

   /** returns the 'register' address at which this segment starts in the CDIM (or whomever) map*/
   public int getAddress() {
      return this.address;
   }

   public byte[] getReadBuffer() {
      return this.readBuffer;
   }

   public Lock getReadLock() {
      return this.readLock;
   }

   public byte[] getWriteBuffer() {
      return this.writeBuffer;
   }

   public Lock getWriteLock() {
      return this.writeLock;
   }

   public void setAddress(int var1) {
      this.address = var1;
   }

   public String toString() {
      Object[] var1 = new Object[]{Integer.valueOf(this.address), Integer.valueOf(this.readBuffer.length), Integer.valueOf(this.writeBuffer.length)};
      return String.format("Segment - address:%d read:%d write:%d", var1);
   }
}
