package com.ftdi.j2xx;

import android.util.Log;

class ProcessRequestRunnable implements Runnable {
   int a;
   private o b;

   ProcessRequestRunnable(o var1) {
      this.b = var1;
      this.a = this.b.b().getBufferNumber();
   }

   public void run() {
      int var1 = 0;

      try {
         do {
            n var6 = this.b.c(var1);
            if(var6.b() > 0) {
               this.b.processBulkIn(var6);
               var6.c();
            }

            this.b.d(var1);
            var1 = (var1 + 1) % this.a;
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
