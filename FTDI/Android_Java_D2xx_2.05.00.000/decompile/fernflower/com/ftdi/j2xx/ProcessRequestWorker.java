package com.ftdi.j2xx;

import android.util.Log;
import com.ftdi.j2xx.InBuffer;
import com.ftdi.j2xx.ProcessInCtrl;

class ProcessRequestWorker implements Runnable {
   private ProcessInCtrl mProInCtrl;
   int mNrBuf;

   ProcessRequestWorker(ProcessInCtrl inCtrl) {
      this.mProInCtrl = inCtrl;
      this.mNrBuf = this.mProInCtrl.getParams().getBufferNumber();
   }

   public void run() {
      InBuffer inBuf = null;
      int bufferIndex = 0;

      try {
         do {
            inBuf = this.mProInCtrl.acquireReadableBuffer(bufferIndex);
            if(inBuf.getLength() > 0) {
               this.mProInCtrl.processBulkInData(inBuf);
               inBuf.purge();
            }

            this.mProInCtrl.releaseWritableBuffer(bufferIndex);
            ++bufferIndex;
            bufferIndex %= this.mNrBuf;
         } while(!Thread.interrupted());

         throw new InterruptedException();
      } catch (InterruptedException var4) {
         Log.d("ProcessRequestThread::", "Device has been closed.");
         var4.printStackTrace();
      } catch (Exception var5) {
         Log.e("ProcessRequestThread::", "Fatal error!");
         var5.printStackTrace();
      }

   }
}
