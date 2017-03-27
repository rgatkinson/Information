package com.ftdi.j2xx;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.util.Log;
import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.InBuffer;
import com.ftdi.j2xx.ProcessInCtrl;
import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;

class BulkInWorker implements Runnable {
   UsbDeviceConnection mConnection;
   UsbEndpoint mBulkInEndpoint;
   ProcessInCtrl mProInCtrl;
   FT_Device mDevice;
   int mNrBuf;
   int mTransSize;
   int mReadTimeout;
   Semaphore mPauseLock;
   boolean mPaused;

   BulkInWorker(FT_Device dev, ProcessInCtrl inCtrl, UsbDeviceConnection connection, UsbEndpoint endpoint) {
      this.mDevice = dev;
      this.mBulkInEndpoint = endpoint;
      this.mConnection = connection;
      this.mProInCtrl = inCtrl;
      this.mNrBuf = this.mProInCtrl.getParams().getBufferNumber();
      this.mTransSize = this.mProInCtrl.getParams().getMaxTransferSize();
      this.mReadTimeout = this.mDevice.getDriverParameters().getReadTimeout();
      this.mPauseLock = new Semaphore(1);
      this.mPaused = false;
   }

   void pause() throws InterruptedException {
      this.mPauseLock.acquire();
      this.mPaused = true;
   }

   void restart() {
      this.mPaused = false;
      this.mPauseLock.release();
   }

   boolean paused() {
      return this.mPaused;
   }

   public void run() {
      ByteBuffer buffer = null;
      InBuffer inBuf = null;
      int bufferIndex = 0;
      boolean totalBytesRead = false;
      Object readBuf = null;

      try {
         do {
            if(this.mPaused) {
               this.mPauseLock.acquire();
               this.mPauseLock.release();
            }

            inBuf = this.mProInCtrl.acquireWritableBuffer(bufferIndex);
            if(inBuf.getLength() == 0) {
               buffer = inBuf.getInputBuffer();
               buffer.clear();
               inBuf.setBufferId(bufferIndex);
               byte[] var12 = buffer.array();
               int var11 = this.mConnection.bulkTransfer(this.mBulkInEndpoint, var12, this.mTransSize, this.mReadTimeout);
               if(var11 > 0) {
                  buffer.position(var11);
                  buffer.flip();
                  inBuf.setLength(var11);
                  this.mProInCtrl.releaseReadableBuffer(bufferIndex);
               }
            }

            ++bufferIndex;
            bufferIndex %= this.mNrBuf;
         } while(!Thread.interrupted());

         throw new InterruptedException();
      } catch (InterruptedException var9) {
         try {
            this.mProInCtrl.releaseWritableBuffers();
            this.mProInCtrl.purgeINData();
         } catch (Exception var8) {
            Log.d("BulkIn::", "Stop BulkIn thread");
            var8.printStackTrace();
         }
      } catch (Exception var10) {
         var10.printStackTrace();
         Log.e("BulkIn::", "Fatal error in BulkIn thread");
      }

   }
}
