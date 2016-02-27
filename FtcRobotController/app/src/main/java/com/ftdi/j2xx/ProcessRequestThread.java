package com.ftdi.j2xx;

import android.util.Log;

class ProcessRequestThread implements Runnable {
   int bufferCount;
   private ProcessInCtrl processInCtrl;

   ProcessRequestThread(ProcessInCtrl var1) {
      this.processInCtrl = var1;
      this.bufferCount = this.processInCtrl.getDriverParameters().getBufferNumber();
   }

   public void run() {
      int iBuffer = 0;

      try {
         do {
            SomeKindOfBuffer var6 = this.processInCtrl.getAcquireBuffer(iBuffer);
            if(var6.getSomeCount() > 0) {
               this.processInCtrl.a(var6);
               var6.clear();
            }

            this.processInCtrl.releaseBuffer(iBuffer);
            iBuffer = (iBuffer + 1) % this.bufferCount;
         } while(!Thread.interrupted());

         throw new InterruptedException();
      } catch (InterruptedException e) {
         Log.d("ProcessRequestThread::", "Device has been closed.");
         e.printStackTrace();
      } catch (Exception e) {
         Log.e("ProcessRequestThread::", "Fatal error!");
         e.printStackTrace();
      }
   }
}
