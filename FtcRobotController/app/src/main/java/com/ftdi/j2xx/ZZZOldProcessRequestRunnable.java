package com.ftdi.j2xx;

import android.util.Log;

class ZZZOldProcessRequestRunnable implements Runnable {
   int bufferCount;
   private ProcessInCtrl oDevice;

   ZZZOldProcessRequestRunnable(ProcessInCtrl var1) {
      this.oDevice = var1;
      this.bufferCount = this.oDevice.getDriverParameters().getBufferNumber();
   }

   public void run() {
      int bufferNum = 0;

      try {
         do {
            n var6 = this.oDevice.waitForDataReceived(bufferNum);
            if(var6.getCbTransferred() > 0) {
               this.oDevice.processBulkIn(var6);
               var6.clear();
            }

            this.oDevice.d(bufferNum);
            bufferNum = (bufferNum + 1) % this.bufferCount;
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
