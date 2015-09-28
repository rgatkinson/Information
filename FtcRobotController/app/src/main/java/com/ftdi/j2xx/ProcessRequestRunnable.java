package com.ftdi.j2xx;

import android.util.Log;

class ProcessRequestRunnable implements Runnable {
   int bufferCount;
   private o oDevice;

   ProcessRequestRunnable(o var1) {
      this.oDevice = var1;
      this.bufferCount = this.oDevice.getDriverParameters().getBufferNumber();
   }

   public void run() {
      int bufferNum = 0;

      try {
         do {
            n var6 = this.oDevice.c(bufferNum);
            if(var6.b() > 0) {
               this.oDevice.processBulkIn(var6);
               var6.c();
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
