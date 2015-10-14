//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ftdi.j2xx;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ftdi.j2xx.D2xxManager.D2xxException;
import com.ftdi.j2xx.D2xxManager.DriverParameters;
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
    private DriverParameters m;
    private Lock n;
    private Condition o;
    private boolean p;
    private Lock q;
    private Condition r;
    private Object s;
    private int t;

    public o(FT_Device var1) {
        this.l = var1;
        this.m = this.l.d();
        this.i = this.m.getBufferNumber();
        int var2 = this.m.getMaxBufferSize();
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
        this.d = ByteBuffer.allocateDirect(var2);

        try {
            this.f = Pipe.open();
            this.g = this.f.sink();
            this.h = this.f.source();
        } catch (IOException var6) {
            Log.d("ProcessInCtrl", "Create mMainPipe failed!");
            var6.printStackTrace();
        }

        for(int var3 = 0; var3 < this.i; ++var3) {
            this.c[var3] = new n(var2);
            this.b[var3] = new Semaphore(1);
            this.a[var3] = new Semaphore(1);

            try {
                this.c(var3);
            } catch (Exception var5) {
                Log.d("ProcessInCtrl", "Acquire read buffer " + var3 + " failed!");
                var5.printStackTrace();
            }
        }

    }

    boolean a() {
        return this.p;
    }

    DriverParameters b() {
        return this.m;
    }

    n a(int var1) {
        n var2 = null;
        n[] var3 = this.c;
        synchronized(this.c) {
            if(var1 >= 0 && var1 < this.i) {
                var2 = this.c[var1];
            }

            return var2;
        }
    }

    n b(int var1) throws InterruptedException {
        n var2 = null;
        this.a[var1].acquire();
        var2 = this.a(var1);
        if(var2.c(var1) == null) {
            var2 = null;
        }

        return var2;
    }

    n c(int var1) throws InterruptedException {
        n var2 = null;
        this.b[var1].acquire();
        var2 = this.a(var1);
        return var2;
    }

    public void d(int var1) throws InterruptedException {
        n[] var2 = this.c;
        synchronized(this.c) {
            this.c[var1].d(var1);
        }

        this.a[var1].release();
    }

    public void e(int var1) throws InterruptedException {
        this.b[var1].release();
    }

    public void processBulkIn(n var1) throws D2xxException {
        boolean var2 = false;
        byte var3 = 0;
        byte var4 = 0;
        boolean var5 = false;

        try {
            int var12 = var1.b();
            if(var12 < 2) {
                var1.a().clear();
                return;
            }

            Object var8 = this.s;
            int var6;
            int var7;
            synchronized(this.s) {
                var6 = this.d();
                var7 = var12 - 2;
                if(var6 < var7) {
                    Log.d("ProcessBulkIn::", " Buffer is full, waiting for read....");
                    this.a(var5, var3, var4);
                    this.n.lock();
                    this.p = true;
                }
            }

            if(var6 < var7) {
                this.o.await();
                this.n.unlock();
            }

            this.extractReadData(var1);
        } catch (InterruptedException interrupt) {
            this.n.unlock();
            Log.e("ProcessInCtrl", "Exception in Full await!");
            interrupt.printStackTrace();
        } catch (Exception e) {
            Log.e("ProcessInCtrl", "Exception in ProcessBulkIN");
            e.printStackTrace();
            throw new D2xxException("Fatal error in BulkIn.");
        }

    }

    private void extractReadData(n var1) throws InterruptedException {
        boolean var2 = false;
        boolean var3 = false;
        int var4 = 0;
        boolean var5 = false;
        boolean var6 = false;
        long var7 = 0L;
        short var9 = 0;
        short var10 = 0;
        boolean var11 = false;
        ByteBuffer var12 = null;
        var12 = var1.a();
        int var17 = var1.b();
        if(var17 > 0) {
            int var18 = var17 / this.t + (var17 % this.t > 0?1:0);

            for(int var13 = 0; var13 < var18; ++var13) {
                int var19;
                int var20;
                if(var13 == var18 - 1) {
                    var20 = var17;
                    var12.limit(var17);
                    var19 = var13 * this.t;
                    var12.position(var19);
                    byte var14 = var12.get();
                    var9 = (short)(this.l.ftDeviceInfoListNode.modemStatus ^ (short)(var14 & 240));
                    this.l.ftDeviceInfoListNode.modemStatus = (short)(var14 & 240);
                    byte var15 = var12.get();
                    this.l.ftDeviceInfoListNode.lineStatus = (short)(var15 & 255);
                    var19 += 2;
                    if(var12.hasRemaining()) {
                        var10 = (short)(this.l.ftDeviceInfoListNode.lineStatus & 30);
                    } else {
                        var10 = 0;
                    }
                } else {
                    var20 = (var13 + 1) * this.t;
                    var12.limit(var20);
                    var19 = var13 * this.t + 2;
                    var12.position(var19);
                }

                var4 += var20 - var19;
                this.e[var13] = var12.slice();
            }

            if(var4 != 0) {
                var11 = true;

                try {
                    var7 = this.g.write(this.e, 0, var18);
                    if(var7 != (long)var4) {
                        Log.d("extractReadData::", "written != totalData, written= " + var7 + " totalData=" + var4);
                    }

                    this.f((int)var7);
                    this.q.lock();
                    this.r.signalAll();
                    this.q.unlock();
                } catch (Exception var16) {
                    Log.d("extractReadData::", "Write data to sink failed!!");
                    var16.printStackTrace();
                }
            }

            var12.clear();
            this.a(var11, var9, var10);
        }

    }

    public int readBulkInData(byte[] var1, int var2, long var3) {
        boolean var5 = false;
        int var6 = 0;
        int var7 = this.m.getMaxBufferSize();
        long var8 = System.currentTimeMillis();
        ByteBuffer var10 = ByteBuffer.wrap(var1, 0, var2);
        if(var3 == 0L) {
            var3 = (long)this.m.getReadTimeout();
        }

        while(this.l.isOpen()) {
            if(this.c() >= var2) {
                SourceChannel var11 = this.h;
                synchronized(this.h) {
                    try {
                        this.h.read(var10);
                        this.g(var2);
                    } catch (Exception var13) {
                        Log.d("readBulkInData::", "Cannot read data from Source!!");
                        var13.printStackTrace();
                    }
                }

                Object var17 = this.s;
                synchronized(this.s) {
                    if(this.p) {
                        Log.i("FTDI debug::", "buffer is full , and also re start buffer");
                        this.n.lock();
                        this.o.signalAll();
                        this.p = false;
                        this.n.unlock();
                    }
                }

                var6 = var2;
                break;
            }

            try {
                this.q.lock();
                this.r.await(System.currentTimeMillis() - var8, TimeUnit.MILLISECONDS);
                this.q.unlock();
            } catch (InterruptedException var15) {
                Log.d("readBulkInData::", "Cannot wait to read data!!");
                var15.printStackTrace();
                this.q.unlock();
            }

            if(System.currentTimeMillis() - var8 >= var3) {
                break;
            }
        }

        return var6;
    }

    private int f(int var1) {
        Object var3 = this.k;
        synchronized(this.k) {
            this.j += var1;
            int var2 = this.j;
            return var2;
        }
    }

    private int g(int var1) {
        Object var3 = this.k;
        synchronized(this.k) {
            this.j -= var1;
            int var2 = this.j;
            return var2;
        }
    }

    private void h() {
        Object var1 = this.k;
        synchronized(this.k) {
            this.j = 0;
        }
    }

    public int c() {
        Object var2 = this.k;
        synchronized(this.k) {
            int var1 = this.j;
            return var1;
        }
    }

    public int d() {
        return this.m.getMaxBufferSize() - this.c() - 1;
    }

    public int e() {
        int var1 = this.m.getBufferNumber();
        n var2 = null;
        boolean var3 = false;
        ByteBuffer var4 = this.d;
        synchronized(this.d) {
            int var8;
            try {
                do {
                    this.h.configureBlocking(false);
                    var8 = this.h.read(this.d);
                    this.d.clear();
                } while(var8 != 0);
            } catch (Exception var6) {
                var6.printStackTrace();
            }

            this.h();

            for(int var5 = 0; var5 < var1; ++var5) {
                var2 = this.a(var5);
                if(var2.d() && var2.b() > 2) {
                    var2.c();
                }
            }

            return 0;
        }
    }

    public int a(boolean var1, short var2, short var3) throws InterruptedException {
        long var6 = 0L;
        boolean var8 = false;
        boolean var9 = false;
        var6 = 0L;
        q var10 = new q();
        var10.a = this.l.i.a;
        Intent var11;
        if(var1 && (var10.a & 1L) != 0L && (this.l.a ^ 1L) == 1L) {
            this.l.a |= 1L;
            var11 = new Intent("FT_EVENT_RXCHAR");
            var11.putExtra("message", "FT_EVENT_RXCHAR");
            LocalBroadcastManager.getInstance(this.l.parentContext).sendBroadcast(var11);
        }

        if(var2 != 0 && (var10.a & 2L) != 0L && (this.l.a ^ 2L) == 2L) {
            this.l.a |= 2L;
            var11 = new Intent("FT_EVENT_MODEM_STATUS");
            var11.putExtra("message", "FT_EVENT_MODEM_STATUS");
            LocalBroadcastManager.getInstance(this.l.parentContext).sendBroadcast(var11);
        }

        if(var3 != 0 && (var10.a & 4L) != 0L && (this.l.a ^ 4L) == 4L) {
            this.l.a |= 4L;
            var11 = new Intent("FT_EVENT_LINE_STATUS");
            var11.putExtra("message", "FT_EVENT_LINE_STATUS");
            LocalBroadcastManager.getInstance(this.l.parentContext).sendBroadcast(var11);
        }

        return 0;
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
        int var1;
        for(var1 = 0; var1 < this.i; ++var1) {
            try {
                this.e(var1);
            } catch (Exception var4) {
                Log.d("ProcessInCtrl", "Acquire read buffer " + var1 + " failed!");
                var4.printStackTrace();
            }

            this.c[var1] = null;
            this.b[var1] = null;
            this.a[var1] = null;
        }

        for(var1 = 0; var1 < 256; ++var1) {
            this.e[var1] = null;
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
        } catch (IOException var3) {
            Log.d("ProcessInCtrl", "Close mMainPipe failed!");
            var3.printStackTrace();
        }

        this.l = null;
        this.m = null;
    }
}
