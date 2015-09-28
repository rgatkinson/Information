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
    private Pipe pipe;
    private SinkChannel sinkChannel;
    private SourceChannel sourceChannel;
    private int i;
    private int j;
    private Object k;
    private FT_Device ftDevice;
    private DriverParameters driverParameters;
    private Lock lockA;
    private Condition lockACondition;
    private boolean bufferIsFull;
    private Lock lockB;
    private Condition lockBCondition;
    private Object s;
    private int t;

    public o(FT_Device var1) {
        this.ftDevice = var1;
        this.driverParameters = this.ftDevice.d();
        this.i = this.driverParameters.getBufferNumber();
        int var2 = this.driverParameters.getMaxBufferSize();
        this.t = this.ftDevice.e();
        this.a = new Semaphore[this.i];
        this.b = new Semaphore[this.i];
        this.c = new n[this.i];
        this.e = new ByteBuffer[256];
        this.lockA = new ReentrantLock();
        this.lockACondition = this.lockA.newCondition();
        this.bufferIsFull = false;
        this.lockB = new ReentrantLock();
        this.lockBCondition = this.lockB.newCondition();
        this.k = new Object();
        this.s = new Object();
        this.zeroJ();
        this.d = ByteBuffer.allocateDirect(var2);

        try {
            this.pipe = Pipe.open();
            this.sinkChannel = this.pipe.sink();
            this.sourceChannel = this.pipe.source();
        } catch (IOException e) {
            Log.d("ProcessInCtrl", "Create mMainPipe failed!");
            e.printStackTrace();
        }

        for(int var3 = 0; var3 < this.i; ++var3) {
            this.c[var3] = new n(var2);
            this.b[var3] = new Semaphore(1);
            this.a[var3] = new Semaphore(1);

            try {
                this.c(var3);
            } catch (Exception e) {
                Log.d("ProcessInCtrl", "Acquire read buffer " + var3 + " failed!");
                e.printStackTrace();
            }
        }

    }

    boolean isBufferFull() {
        return this.bufferIsFull;
    }

    DriverParameters getDriverParameters() {
        return this.driverParameters;
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
                    this.lockA.lock();
                    this.bufferIsFull = true;
                }
            }

            if(var6 < var7) {
                this.lockACondition.await();
                this.lockA.unlock();
            }

            this.extractReadData(var1);
        } catch (InterruptedException var10) {
            this.lockA.unlock();
            Log.e("ProcessInCtrl", "Exception in Full await!");
            var10.printStackTrace();
        } catch (Exception var11) {
            Log.e("ProcessInCtrl", "Exception in ProcessBulkIN");
            var11.printStackTrace();
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
        ByteBuffer buffer = null;
        buffer = var1.a();
        int var17 = var1.b();
        if(var17 > 0) {
            int var18 = var17 / this.t + (var17 % this.t > 0?1:0);

            for(int var13 = 0; var13 < var18; ++var13) {
                int var19;
                int var20;
                if(var13 == var18 - 1) {
                    var20 = var17;
                    buffer.limit(var17);
                    var19 = var13 * this.t;
                    buffer.position(var19);
                    byte var14 = buffer.get();
                    var9 = (short)(this.ftDevice.ftDeviceInfoListNode.modemStatus ^ (short)(var14 & 240));
                    this.ftDevice.ftDeviceInfoListNode.modemStatus = (short)(var14 & 240);
                    byte var15 = buffer.get();
                    this.ftDevice.ftDeviceInfoListNode.lineStatus = (short)(var15 & 255);
                    var19 += 2;
                    if(buffer.hasRemaining()) {
                        var10 = (short)(this.ftDevice.ftDeviceInfoListNode.lineStatus & 30);
                    } else {
                        var10 = 0;
                    }
                } else {
                    var20 = (var13 + 1) * this.t;
                    buffer.limit(var20);
                    var19 = var13 * this.t + 2;
                    buffer.position(var19);
                }

                var4 += var20 - var19;
                this.e[var13] = buffer.slice();
            }

            if(var4 != 0) {
                var11 = true;

                try {
                    var7 = this.sinkChannel.write(this.e, 0, var18);
                    if(var7 != (long)var4) {
                        Log.d("extractReadData::", "written != totalData, written= " + var7 + " totalData=" + var4);
                    }

                    this.incrementAndGetJ((int) var7);
                    this.lockB.lock();
                    this.lockBCondition.signalAll();
                    this.lockB.unlock();
                } catch (Exception var16) {
                    Log.d("extractReadData::", "Write data to sink failed!!");
                    var16.printStackTrace();
                }
            }

            buffer.clear();
            this.a(var11, var9, var10);
        }

    }

    public int read(byte[] data, int length, long msWait) {
        boolean var5 = false;
        int var6 = 0;
        int var7 = this.driverParameters.getMaxBufferSize();
        long msStartRead = System.currentTimeMillis();
        ByteBuffer buffer = ByteBuffer.wrap(data, 0, length);
        if(msWait == 0L) {
            msWait = (long)this.driverParameters.getReadTimeout();
        }

        while(this.ftDevice.isOpen()) {
            if(this.getJ() >= length) {

                synchronized(this.sourceChannel) {
                    try {
                        this.sourceChannel.read(buffer);
                        this.decrementAndGetJ(length);
                    } catch (Exception var13) {
                        Log.d("readBulkInData::", "Cannot read data from Source!!");
                        var13.printStackTrace();
                    }
                }

                synchronized(this.s) {
                    if (this.bufferIsFull) {
                        Log.i("FTDI debug::", "buffer is full , and also re start buffer");
                        this.lockA.lock();
                        this.lockACondition.signalAll();
                        this.bufferIsFull = false;
                        this.lockA.unlock();
                    }
                }

                var6 = length;
                break;
            }

            try {
                this.lockB.lock();
                this.lockBCondition.await(System.currentTimeMillis() - msStartRead, TimeUnit.MILLISECONDS);
                this.lockB.unlock();
            } catch (InterruptedException e) {
                Log.d("readBulkInData::", "Cannot wait to read data!!");
                e.printStackTrace();
                this.lockB.unlock();
            }

            if(System.currentTimeMillis() - msStartRead >= msWait) {
                break;
            }
        }

        return var6;
    }

    private int incrementAndGetJ(int incrementAmount) {
        Object var3 = this.k;
        synchronized(this.k) {
            this.j += incrementAmount;
            int result = this.j;
            return result;
        }
    }

    private int decrementAndGetJ(int decrementAmount) {
        Object var3 = this.k;
        synchronized(this.k) {
            this.j -= decrementAmount;
            int result = this.j;
            return result;
        }
    }

    private void zeroJ() {
        Object var1 = this.k;
        synchronized(this.k) {
            this.j = 0;
        }
    }

    public int getJ() {
        Object var2 = this.k;
        synchronized(this.k) {
            int result = this.j;
            return result;
        }
    }

    public int d() {
        return this.driverParameters.getMaxBufferSize() - this.getJ() - 1;
    }

    public int e() {
        int var1 = this.driverParameters.getBufferNumber();
        n var2 = null;
        boolean var3 = false;
        ByteBuffer var4 = this.d;
        synchronized(this.d) {
            int var8;
            try {
                do {
                    this.sourceChannel.configureBlocking(false);
                    var8 = this.sourceChannel.read(this.d);
                    this.d.clear();
                } while(var8 != 0);
            } catch (Exception var6) {
                var6.printStackTrace();
            }

            this.zeroJ();

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
        var10.a = this.ftDevice.i.a;
        Intent var11;
        if(var1 && (var10.a & 1L) != 0L && (this.ftDevice.a ^ 1L) == 1L) {
            this.ftDevice.a |= 1L;
            var11 = new Intent("FT_EVENT_RXCHAR");
            var11.putExtra("message", "FT_EVENT_RXCHAR");
            LocalBroadcastManager.getInstance(this.ftDevice.parentContext).sendBroadcast(var11);
        }

        if(var2 != 0 && (var10.a & 2L) != 0L && (this.ftDevice.a ^ 2L) == 2L) {
            this.ftDevice.a |= 2L;
            var11 = new Intent("FT_EVENT_MODEM_STATUS");
            var11.putExtra("message", "FT_EVENT_MODEM_STATUS");
            LocalBroadcastManager.getInstance(this.ftDevice.parentContext).sendBroadcast(var11);
        }

        if(var3 != 0 && (var10.a & 4L) != 0L && (this.ftDevice.a ^ 4L) == 4L) {
            this.ftDevice.a |= 4L;
            var11 = new Intent("FT_EVENT_LINE_STATUS");
            var11.putExtra("message", "FT_EVENT_LINE_STATUS");
            LocalBroadcastManager.getInstance(this.ftDevice.parentContext).sendBroadcast(var11);
        }

        return 0;
    }

    public void f() throws InterruptedException {
        int bufferCount = this.driverParameters.getBufferNumber();

        for(int buffer = 0; buffer < bufferCount; ++buffer) {
            if(this.a(buffer).d()) {
                this.d(buffer);
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
        if(this.bufferIsFull) {
            this.lockA.lock();
            this.lockACondition.signalAll();
            this.lockA.unlock();
        }

        this.lockB.lock();
        this.lockBCondition.signalAll();
        this.lockB.unlock();
        this.lockA = null;
        this.lockACondition = null;
        this.k = null;
        this.lockB = null;
        this.lockBCondition = null;

        try {
            this.sinkChannel.close();
            this.sinkChannel = null;
            this.sourceChannel.close();
            this.sourceChannel = null;
            this.pipe = null;
        } catch (IOException var3) {
            Log.d("ProcessInCtrl", "Close mMainPipe failed!");
            var3.printStackTrace();
        }

        this.ftDevice = null;
        this.driverParameters = null;
    }
}
