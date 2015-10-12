package com.ftdi.j2xx;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.o;
import java.util.concurrent.Semaphore;

class a implements Runnable {
   UsbDeviceConnection a;
   UsbEndpoint b;
   o c;
   FT_Device d;
   int e;
   int f;
   int g;
   Semaphore h;
   boolean i;

   a(FT_Device var1, o var2, UsbDeviceConnection var3, UsbEndpoint var4) {
      this.d = var1;
      this.b = var4;
      this.a = var3;
      this.c = var2;
      this.e = this.c.b().getBufferNumber();
      this.f = this.c.b().getMaxTransferSize();
      this.g = this.d.d().getReadTimeout();
      this.h = new Semaphore(1);
      this.i = false;
   }

   void a() throws InterruptedException {
      this.h.acquire();
      this.i = true;
   }

   void b() {
      this.i = false;
      this.h.release();
   }

   boolean c() {
      return this.i;
   }

   public void run() {
      // $FF: Couldn't be decompiled
   }
}
