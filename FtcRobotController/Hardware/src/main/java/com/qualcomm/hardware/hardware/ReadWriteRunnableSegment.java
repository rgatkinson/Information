package com.qualcomm.hardware.hardware;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReadWriteRunnableSegment {
   final Lock a;
   final Lock b;
   private int c;
   private final byte[] d;
   private final byte[] e;

   public ReadWriteRunnableSegment(int var1, int var2) {
      this.c = var1;
      this.a = new ReentrantLock();
      this.d = new byte[var2];
      this.b = new ReentrantLock();
      this.e = new byte[var2];
   }

   public int getAddress() {
      return this.c;
   }

   public byte[] getReadBuffer() {
      return this.d;
   }

   public Lock getReadLock() {
      return this.a;
   }

   public byte[] getWriteBuffer() {
      return this.e;
   }

   public Lock getWriteLock() {
      return this.b;
   }

   public void setAddress(int var1) {
      this.c = var1;
   }

   public String toString() {
      Object[] var1 = new Object[]{Integer.valueOf(this.c), Integer.valueOf(this.d.length), Integer.valueOf(this.e.length)};
      return String.format("Segment - address:%d read:%d write:%d", var1);
   }
}
