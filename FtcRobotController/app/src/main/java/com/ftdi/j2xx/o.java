package com.ftdi.j2xx;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.n;
import com.ftdi.j2xx.q;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.Pipe.SinkChannel;
import java.nio.channels.Pipe.SourceChannel;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class o {
   private Semaphore[] a;
   private Semaphore[] b;
   private n[] c;
   private ByteBuffer d;
   private ByteBuffer[] e;
   private Pipe f;
   private SinkChannel g;
   private SourceChannel h;
   private int i;
   private int j;
   private Object k;
   private FT_Device l;
   private D2xxManager.DriverParameters m;
   private Lock n;
   private Condition o;
   private boolean p;
   private Lock q;
   private Condition r;
   private Object s;
   private int t;

   public o(FT_Device var1) {
      int var2 = 0;
      super();
      this.l = var1;
      this.m = this.l.d();
      this.i = this.m.getBufferNumber();
      int var3 = this.m.getMaxBufferSize();
      this.t = this.l.e();
      this.a = new Semaphore[this.i];
      this.b = new Semaphore[this.i];
      this.c = new n[this.i];
      this.e = new ByteBuffer[256];
      this.n = new ReentrantLock();
      this.o = this.n.newCondition();
      this.p = false;
      this.q = new ReentrantLock();
      this.r = this.q.newCondition();
      this.k = new Object();
      this.s = new Object();
      this.h();
      this.d = ByteBuffer.allocateDirect(var3);

      try {
         this.f = Pipe.open();
         this.g = this.f.sink();
         this.h = this.f.source();
      } catch (IOException var8) {
         Log.d("ProcessInCtrl", "Create mMainPipe failed!");
         var8.printStackTrace();
         var2 = 0;
      }

      for(; var2 < this.i; ++var2) {
         this.c[var2] = new n(var3);
         this.b[var2] = new Semaphore(1);
         this.a[var2] = new Semaphore(1);

         try {
            this.c(var2);
         } catch (Exception var7) {
            Log.d("ProcessInCtrl", "Acquire read buffer " + var2 + " failed!");
            var7.printStackTrace();
         }
      }

   }

   private void b(n param1) throws InterruptedException {
      // $FF: Couldn't be decompiled
   }

   private int f(int param1) {
      // $FF: Couldn't be decompiled
   }

   private int g(int param1) {
      // $FF: Couldn't be decompiled
   }

   private void h() {
      // $FF: Couldn't be decompiled
   }

   public int a(boolean var1, short var2, short var3) throws InterruptedException {
      q var4 = new q();
      var4.a = this.l.i.a;
      if(var1 && (1L & var4.a) != 0L && (1L ^ this.l.a) == 1L) {
         FT_Device var13 = this.l;
         var13.a |= 1L;
         Intent var14 = new Intent("FT_EVENT_RXCHAR");
         var14.putExtra("message", "FT_EVENT_RXCHAR");
         LocalBroadcastManager.getInstance(this.l.j).sendBroadcast(var14);
      }

      if(var2 != 0 && (2L & var4.a) != 0L && (2L ^ this.l.a) == 2L) {
         FT_Device var9 = this.l;
         var9.a |= 2L;
         Intent var10 = new Intent("FT_EVENT_MODEM_STATUS");
         var10.putExtra("message", "FT_EVENT_MODEM_STATUS");
         LocalBroadcastManager.getInstance(this.l.j).sendBroadcast(var10);
      }

      if(var3 != 0 && (4L & var4.a) != 0L && (4L ^ this.l.a) == 4L) {
         FT_Device var5 = this.l;
         var5.a |= 4L;
         Intent var6 = new Intent("FT_EVENT_LINE_STATUS");
         var6.putExtra("message", "FT_EVENT_LINE_STATUS");
         LocalBroadcastManager.getInstance(this.l.j).sendBroadcast(var6);
      }

      return 0;
   }

   public int a(byte[] param1, int param2, long param3) {
      // $FF: Couldn't be decompiled
   }

   n a(int param1) {
      // $FF: Couldn't be decompiled
   }

   public void a(n param1) throws D2xxManager.D2xxException {
      // $FF: Couldn't be decompiled
   }

   boolean a() {
      return this.p;
   }

   D2xxManager.DriverParameters b() {
      return this.m;
   }

   n b(int var1) throws InterruptedException {
      this.a[var1].acquire();
      n var2 = this.a(var1);
      if(var2.c(var1) == null) {
         var2 = null;
      }

      return var2;
   }

   public int c() {
      // $FF: Couldn't be decompiled
   }

   n c(int var1) throws InterruptedException {
      this.b[var1].acquire();
      return this.a(var1);
   }

   public int d() {
      return -1 + (this.m.getMaxBufferSize() - this.c());
   }

   public void d(int param1) throws InterruptedException {
      // $FF: Couldn't be decompiled
   }

   public int e() {
      // $FF: Couldn't be decompiled
   }

   public void e(int var1) throws InterruptedException {
      this.b[var1].release();
   }

   public void f() throws InterruptedException {
      int var1 = this.m.getBufferNumber();

      for(int var2 = 0; var2 < var1; ++var2) {
         if(this.a(var2).d()) {
            this.d(var2);
         }
      }

   }

   void g() {
      int var1 = 0;

      while(true) {
         int var2 = this.i;
         int var3 = 0;
         if(var1 >= var2) {
            while(var3 < 256) {
               this.e[var3] = null;
               ++var3;
            }

            this.a = null;
            this.b = null;
            this.c = null;
            this.e = null;
            this.d = null;
            if(this.p) {
               this.n.lock();
               this.o.signalAll();
               this.n.unlock();
            }

            this.q.lock();
            this.r.signalAll();
            this.q.unlock();
            this.n = null;
            this.o = null;
            this.k = null;
            this.q = null;
            this.r = null;

            try {
               this.g.close();
               this.g = null;
               this.h.close();
               this.h = null;
               this.f = null;
            } catch (IOException var7) {
               Log.d("ProcessInCtrl", "Close mMainPipe failed!");
               var7.printStackTrace();
            }

            this.l = null;
            this.m = null;
            return;
         }

         try {
            this.e(var1);
         } catch (Exception var8) {
            Log.d("ProcessInCtrl", "Acquire read buffer " + var1 + " failed!");
            var8.printStackTrace();
         }

         this.c[var1] = null;
         this.b[var1] = null;
         this.a[var1] = null;
         ++var1;
      }
   }
}
