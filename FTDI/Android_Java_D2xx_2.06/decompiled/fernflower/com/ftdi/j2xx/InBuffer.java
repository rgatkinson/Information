package com.ftdi.j2xx;

import java.nio.ByteBuffer;

class InBuffer {
   private int mBufId;
   private ByteBuffer mBuffer;
   private int mLength;
   private boolean mAcquired;

   public InBuffer(int size) {
      this.mBuffer = ByteBuffer.allocate(size);
      this.setLength(0);
   }

   void setBufferId(int id) {
      this.mBufId = id;
   }

   int getBufferId() {
      return this.mBufId;
   }

   ByteBuffer getInputBuffer() {
      return this.mBuffer;
   }

   int getLength() {
      return this.mLength;
   }

   void setLength(int length) {
      this.mLength = length;
   }

   synchronized void purge() {
      this.mBuffer.clear();
      this.setLength(0);
   }

   synchronized boolean acquired() {
      return this.mAcquired;
   }

   synchronized ByteBuffer acquire(int bufId) {
      ByteBuffer buffer = null;
      if(!this.mAcquired) {
         this.mAcquired = true;
         this.mBufId = bufId;
         buffer = this.mBuffer;
      }

      return buffer;
   }

   synchronized boolean release(int bufId) {
      boolean rc = false;
      if(this.mAcquired && bufId == this.mBufId) {
         this.mAcquired = false;
         rc = true;
      }

      return rc;
   }
}
