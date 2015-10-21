package com.ftdi.j2xx;

import android.util.Log;

class ProcessRequestRunnable implements Runnable {
   int cBuffer;
   private ProcessInCtrl processInCtrl;

   ProcessRequestRunnable(ProcessInCtrl var1) {
      this.processInCtrl = var1;
      this.cBuffer = this.processInCtrl.getDriverParameters().getBufferNumber();
   }

   public void run() {
      int iBuffer = 0;

      try {
         do {
            n var6 = this.processInCtrl.waitForDataReceived(iBuffer);
            if(var6.getCbTransferred() > 0) {
               this.processInCtrl.processBulkIn(var6);
               var6.clear();
            }

            this.processInCtrl.d(iBuffer);
            iBuffer = (iBuffer + 1) % this.cBuffer;
         } while(!Thread.interrupted());

         throw new InterruptedException();
      } catch (InterruptedException var7) {
         Log.d("ProcessRequestThread::", "Device has been closed.");
         var7.printStackTrace();
      } catch (Exception var8) {
         Log.e("ProcessRequestThread::", "Fatal error!");
         var8.printStackTrace();
      }
   }
}
