package com.ftdi.j2xx;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.InBuffer;
import com.ftdi.j2xx.TFtEventNotify;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SinkChannel;
import java.nio.channels.Pipe.SourceChannel;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ProcessInCtrl {
   private static final byte FT_PACKET_SIZE = 64;
   private static final int FT_PACKET_SIZE_HI = 512;
   private static final byte FT_MODEM_STATUS_SIZE = 2;
   private static final int MAX_PACKETS = 256;
   private Semaphore[] mWritable;
   private Semaphore[] mReadable;
   private InBuffer[] mInputBufs;
   private ByteBuffer mMainBuf;
   private ByteBuffer[] mBuffers;
   private Pipe mMainPipe;
   private SinkChannel mMainSink;
   private SourceChannel mMainSource;
   private int mNrBuf;
   private int mBufInCounter;
   private Object mCounterLock;
   private FT_Device mDevice;
   private D2xxManager.DriverParameters mParams;
   private Lock mInFullLock;
   private Condition mFullCon;
   private boolean mSinkFull;
   private Lock mReadInLock;
   private Condition mReadInCon;
   private Object mSinkFullLock;
   private int mMaxPacketSize;

   public ProcessInCtrl(FT_Device dev) {
      this.mDevice = dev;
      this.mParams = this.mDevice.getDriverParameters();
      this.mNrBuf = this.mParams.getBufferNumber();
      int bufSize = this.mParams.getMaxBufferSize();
      this.mMaxPacketSize = this.mDevice.getMaxPacketSize();
      this.mWritable = new Semaphore[this.mNrBuf];
      this.mReadable = new Semaphore[this.mNrBuf];
      this.mInputBufs = new InBuffer[this.mNrBuf];
      this.mBuffers = new ByteBuffer[256];
      this.mInFullLock = new ReentrantLock();
      this.mFullCon = this.mInFullLock.newCondition();
      this.mSinkFull = false;
      this.mReadInLock = new ReentrantLock();
      this.mReadInCon = this.mReadInLock.newCondition();
      this.mCounterLock = new Object();
      this.mSinkFullLock = new Object();
      this.resetBufCount();
      this.mMainBuf = ByteBuffer.allocateDirect(bufSize);

      try {
         this.mMainPipe = Pipe.open();
         this.mMainSink = this.mMainPipe.sink();
         this.mMainSource = this.mMainPipe.source();
      } catch (IOException var6) {
         Log.d("ProcessInCtrl", "Create mMainPipe failed!");
         var6.printStackTrace();
      }

      for(int i = 0; i < this.mNrBuf; ++i) {
         this.mInputBufs[i] = new InBuffer(bufSize);
         this.mReadable[i] = new Semaphore(1);
         this.mWritable[i] = new Semaphore(1);

         try {
            this.acquireReadableBuffer(i);
         } catch (Exception var5) {
            Log.d("ProcessInCtrl", "Acquire read buffer " + i + " failed!");
            var5.printStackTrace();
         }
      }

   }

   boolean isSinkFull() {
      return this.mSinkFull;
   }

   D2xxManager.DriverParameters getParams() {
      return this.mParams;
   }

   InBuffer getBuffer(int idx) {
      InBuffer buffer = null;
      InBuffer[] var3 = this.mInputBufs;
      synchronized(this.mInputBufs) {
         if(idx >= 0 && idx < this.mNrBuf) {
            buffer = this.mInputBufs[idx];
         }

         return buffer;
      }
   }

   InBuffer acquireWritableBuffer(int idx) throws InterruptedException {
      InBuffer buffer = null;
      this.mWritable[idx].acquire();
      buffer = this.getBuffer(idx);
      if(buffer.acquire(idx) == null) {
         buffer = null;
      }

      return buffer;
   }

   InBuffer acquireReadableBuffer(int idx) throws InterruptedException {
      InBuffer buffer = null;
      this.mReadable[idx].acquire();
      buffer = this.getBuffer(idx);
      return buffer;
   }

   public void releaseWritableBuffer(int idx) throws InterruptedException {
      InBuffer[] var2 = this.mInputBufs;
      synchronized(this.mInputBufs) {
         this.mInputBufs[idx].release(idx);
      }

      this.mWritable[idx].release();
   }

   public void releaseReadableBuffer(int idx) throws InterruptedException {
      this.mReadable[idx].release();
   }

   public void processBulkInData(InBuffer inBuffer) throws D2xxManager.D2xxException {
      boolean bufSize = false;
      byte signalEvents = 0;
      byte signalLineEvents = 0;
      boolean signalRxChar = false;

      try {
         int bufSize1 = inBuffer.getLength();
         if(bufSize1 < 2) {
            inBuffer.getInputBuffer().clear();
            return;
         }

         Object var8 = this.mSinkFullLock;
         int ex;
         int needS;
         synchronized(this.mSinkFullLock) {
            ex = this.getFreeSpace();
            needS = bufSize1 - 2;
            if(ex < needS) {
               Log.d("ProcessBulkIn::", " Buffer is full, waiting for read....");
               this.processEventChars(signalRxChar, signalEvents, signalLineEvents);
               this.mInFullLock.lock();
               this.mSinkFull = true;
            }
         }

         if(ex < needS) {
            this.mFullCon.await();
            this.mInFullLock.unlock();
         }

         this.extractReadData(inBuffer);
      } catch (InterruptedException var10) {
         this.mInFullLock.unlock();
         Log.e("ProcessInCtrl", "Exception in Full await!");
         var10.printStackTrace();
      } catch (Exception var11) {
         Log.e("ProcessInCtrl", "Exception in ProcessBulkIN");
         var11.printStackTrace();
         throw new D2xxManager.D2xxException("Fatal error in BulkIn.");
      }

   }

   private void extractReadData(InBuffer inBuffer) throws InterruptedException {
      boolean bufSize = false;
      boolean nrPackets = false;
      int totalData = 0;
      boolean pos = false;
      boolean lim = false;
      long written = 0L;
      short signalEvents = 0;
      short signalLineEvents = 0;
      boolean signalRxChar = false;
      ByteBuffer buffer = null;
      buffer = inBuffer.getInputBuffer();
      int var17 = inBuffer.getLength();
      if(var17 > 0) {
         int var18 = var17 / this.mMaxPacketSize + (var17 % this.mMaxPacketSize > 0?1:0);

         for(int ex = 0; ex < var18; ++ex) {
            int var19;
            int var20;
            if(ex == var18 - 1) {
               var20 = var17;
               buffer.limit(var17);
               var19 = ex * this.mMaxPacketSize;
               buffer.position(var19);
               byte b0 = buffer.get();
               signalEvents = (short)(this.mDevice.mDeviceInfoNode.modemStatus ^ (short)(b0 & 240));
               this.mDevice.mDeviceInfoNode.modemStatus = (short)(b0 & 240);
               byte b1 = buffer.get();
               this.mDevice.mDeviceInfoNode.lineStatus = (short)(b1 & 255);
               var19 += 2;
               if(buffer.hasRemaining()) {
                  signalLineEvents = (short)(this.mDevice.mDeviceInfoNode.lineStatus & 30);
               } else {
                  signalLineEvents = 0;
               }
            } else {
               var20 = (ex + 1) * this.mMaxPacketSize;
               buffer.limit(var20);
               var19 = ex * this.mMaxPacketSize + 2;
               buffer.position(var19);
            }

            totalData += var20 - var19;
            this.mBuffers[ex] = buffer.slice();
         }

         if(totalData != 0) {
            signalRxChar = true;

            try {
               written = this.mMainSink.write(this.mBuffers, 0, var18);
               if(written != (long)totalData) {
                  Log.d("extractReadData::", "written != totalData, written= " + written + " totalData=" + totalData);
               }

               this.incBufCount((int)written);
               this.mReadInLock.lock();
               this.mReadInCon.signalAll();
               this.mReadInLock.unlock();
            } catch (Exception var16) {
               Log.d("extractReadData::", "Write data to sink failed!!");
               var16.printStackTrace();
            }
         }

         buffer.clear();
         this.processEventChars(signalRxChar, signalEvents, signalLineEvents);
      }

   }

   public int readBulkInData(byte[] data, int length, long timeout_ms) {
      boolean i = false;
      int rc = 0;
      int bufSize = this.mParams.getMaxBufferSize();
      long startTime = System.currentTimeMillis();
      ByteBuffer buffer = ByteBuffer.wrap(data, 0, length);
      if(timeout_ms == 0L) {
         timeout_ms = (long)this.mParams.getReadTimeout();
      }

      while(this.mDevice.isOpen()) {
         if(this.getBytesAvailable() >= length) {
            SourceChannel ex = this.mMainSource;
            synchronized(this.mMainSource) {
               try {
                  this.mMainSource.read(buffer);
                  this.decBufCount(length);
               } catch (Exception var13) {
                  Log.d("readBulkInData::", "Cannot read data from Source!!");
                  var13.printStackTrace();
               }
            }

            Object ex1 = this.mSinkFullLock;
            synchronized(this.mSinkFullLock) {
               if(this.mSinkFull) {
                  Log.i("FTDI debug::", "buffer is full , and also re start buffer");
                  this.mInFullLock.lock();
                  this.mFullCon.signalAll();
                  this.mSinkFull = false;
                  this.mInFullLock.unlock();
               }
            }

            rc = length;
            break;
         }

         try {
            this.mReadInLock.lock();
            this.mReadInCon.await(System.currentTimeMillis() - startTime, TimeUnit.MILLISECONDS);
            this.mReadInLock.unlock();
         } catch (InterruptedException var15) {
            Log.d("readBulkInData::", "Cannot wait to read data!!");
            var15.printStackTrace();
            this.mReadInLock.unlock();
         }

         if(System.currentTimeMillis() - startTime >= timeout_ms) {
            break;
         }
      }

      return rc;
   }

   private int incBufCount(int size) {
      Object var3 = this.mCounterLock;
      synchronized(this.mCounterLock) {
         this.mBufInCounter += size;
         int rc = this.mBufInCounter;
         return rc;
      }
   }

   private int decBufCount(int size) {
      Object var3 = this.mCounterLock;
      synchronized(this.mCounterLock) {
         this.mBufInCounter -= size;
         int rc = this.mBufInCounter;
         return rc;
      }
   }

   private void resetBufCount() {
      Object var1 = this.mCounterLock;
      synchronized(this.mCounterLock) {
         this.mBufInCounter = 0;
      }
   }

   public int getBytesAvailable() {
      Object var2 = this.mCounterLock;
      synchronized(this.mCounterLock) {
         int rc = this.mBufInCounter;
         return rc;
      }
   }

   public int getFreeSpace() {
      return this.mParams.getMaxBufferSize() - this.getBytesAvailable() - 1;
   }

   public int purgeINData() {
      int nrBuf = this.mParams.getBufferNumber();
      InBuffer inBuf = null;
      boolean read = false;
      ByteBuffer var4 = this.mMainBuf;
      synchronized(this.mMainBuf) {
         int var8;
         try {
            do {
               this.mMainSource.configureBlocking(false);
               var8 = this.mMainSource.read(this.mMainBuf);
               this.mMainBuf.clear();
            } while(var8 != 0);
         } catch (Exception var6) {
            var6.printStackTrace();
         }

         this.resetBufCount();

         for(int i = 0; i < nrBuf; ++i) {
            inBuf = this.getBuffer(i);
            if(inBuf.acquired() && inBuf.getLength() > 2) {
               inBuf.purge();
            }
         }

         return 0;
      }
   }

   public int processEventChars(boolean fRxChar, short sEvents, short slEvents) throws InterruptedException {
      long Mask = 0L;
      boolean signalEvents = false;
      boolean signalLineEvents = false;
      Mask = 0L;
      TFtEventNotify Events = new TFtEventNotify();
      Events.Mask = this.mDevice.mEventNotification.Mask;
      Intent intent;
      if(fRxChar && (Events.Mask & 1L) != 0L && (this.mDevice.mEventMask ^ 1L) == 1L) {
         this.mDevice.mEventMask |= 1L;
         intent = new Intent("FT_EVENT_RXCHAR");
         intent.putExtra("message", "FT_EVENT_RXCHAR");
         LocalBroadcastManager.getInstance(this.mDevice.mContext).sendBroadcast(intent);
      }

      if(sEvents != 0 && (Events.Mask & 2L) != 0L && (this.mDevice.mEventMask ^ 2L) == 2L) {
         this.mDevice.mEventMask |= 2L;
         intent = new Intent("FT_EVENT_MODEM_STATUS");
         intent.putExtra("message", "FT_EVENT_MODEM_STATUS");
         LocalBroadcastManager.getInstance(this.mDevice.mContext).sendBroadcast(intent);
      }

      if(slEvents != 0 && (Events.Mask & 4L) != 0L && (this.mDevice.mEventMask ^ 4L) == 4L) {
         this.mDevice.mEventMask |= 4L;
         intent = new Intent("FT_EVENT_LINE_STATUS");
         intent.putExtra("message", "FT_EVENT_LINE_STATUS");
         LocalBroadcastManager.getInstance(this.mDevice.mContext).sendBroadcast(intent);
      }

      return 0;
   }

   public void releaseWritableBuffers() throws InterruptedException {
      int nrBuf = this.mParams.getBufferNumber();

      for(int i = 0; i < nrBuf; ++i) {
         if(this.getBuffer(i).acquired()) {
            this.releaseWritableBuffer(i);
         }
      }

   }

   void close() {
      int i;
      for(i = 0; i < this.mNrBuf; ++i) {
         try {
            this.releaseReadableBuffer(i);
         } catch (Exception var4) {
            Log.d("ProcessInCtrl", "Acquire read buffer " + i + " failed!");
            var4.printStackTrace();
         }

         this.mInputBufs[i] = null;
         this.mReadable[i] = null;
         this.mWritable[i] = null;
      }

      for(i = 0; i < 256; ++i) {
         this.mBuffers[i] = null;
      }

      this.mWritable = null;
      this.mReadable = null;
      this.mInputBufs = null;
      this.mBuffers = null;
      this.mMainBuf = null;
      if(this.mSinkFull) {
         this.mInFullLock.lock();
         this.mFullCon.signalAll();
         this.mInFullLock.unlock();
      }

      this.mReadInLock.lock();
      this.mReadInCon.signalAll();
      this.mReadInLock.unlock();
      this.mInFullLock = null;
      this.mFullCon = null;
      this.mCounterLock = null;
      this.mReadInLock = null;
      this.mReadInCon = null;

      try {
         this.mMainSink.close();
         this.mMainSink = null;
         this.mMainSource.close();
         this.mMainSource = null;
         this.mMainPipe = null;
      } catch (IOException var3) {
         Log.d("ProcessInCtrl", "Close mMainPipe failed!");
         var3.printStackTrace();
      }

      this.mDevice = null;
      this.mParams = null;
   }
}
