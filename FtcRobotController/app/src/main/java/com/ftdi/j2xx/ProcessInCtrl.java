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

class ProcessInCtrl
    {
    private Semaphore[] rgBufferSempaphores;
    private Semaphore[] rgOtherBufferSempahores;
    private SomeKindOfBuffer[] rgBuffers;
    private ByteBuffer d;
    private ByteBuffer[] e;
    private Pipe pipe;
    private SinkChannel sinkChannel;
    private SourceChannel sourceChannel;
    private int bufferCount;
    private int cbAvailableToRead;
    private Object cbAvailableToReadLock;
    private FT_Device ftDevice;
    private DriverParameters driverParameters;
    private Lock n;
    private Condition condition;
    private boolean readBufferFull;
    private Lock q;
    private Condition r;
    private Object s;
    private int t;

    public ProcessInCtrl(FT_Device ftDevice) {
        this.ftDevice = ftDevice;
        this.driverParameters = this.ftDevice.getDriverParameters();
        this.bufferCount = this.driverParameters.getBufferNumber();
        int maxBufferSize = this.driverParameters.getMaxBufferSize();
        this.t = this.ftDevice.e();
        this.rgBufferSempaphores = new Semaphore[this.bufferCount];
        this.rgOtherBufferSempahores = new Semaphore[this.bufferCount];
        this.rgBuffers = new SomeKindOfBuffer[this.bufferCount];
        this.e = new ByteBuffer[256];
        this.n = new ReentrantLock();
        this.condition = this.n.newCondition();
        this.readBufferFull = false;
        this.q = new ReentrantLock();
        this.r = this.q.newCondition();
        this.cbAvailableToReadLock = new Object();
        this.s = new Object();
        this.clearCbAvailableToRead();
        this.d = ByteBuffer.allocateDirect(maxBufferSize);

        try {
            this.pipe = Pipe.open();
            this.sinkChannel = this.pipe.sink();
            this.sourceChannel = this.pipe.source();
        } catch (IOException e) {
            Log.d("ProcessInCtrl", "Create mMainPipe failed!");
            e.printStackTrace();
        }

        for(int iBuffer = 0; iBuffer < this.bufferCount; ++iBuffer) {
            this.rgBuffers[iBuffer] = new SomeKindOfBuffer(maxBufferSize);
            this.rgOtherBufferSempahores[iBuffer] = new Semaphore(1);
            this.rgBufferSempaphores[iBuffer] = new Semaphore(1);

            try {
                this.getAcquireBuffer(iBuffer);
            } catch (Exception var5) {
                Log.d("ProcessInCtrl", "Acquire read buffer " + iBuffer + " failed!");
                var5.printStackTrace();
            }
        }

    }

    boolean readBufferFull() {
        return this.readBufferFull;
    }

    DriverParameters getDriverParameters() {
        return this.driverParameters;
    }

    SomeKindOfBuffer getSomeKindOfBuffer(int iBuffer) {
        SomeKindOfBuffer result = null;
        synchronized(this.rgBuffers) {
            if(iBuffer >= 0 && iBuffer < this.bufferCount) {
                result = this.rgBuffers[iBuffer];
            }

            return result;
        }
    }

    SomeKindOfBuffer b(int iBuffer) throws InterruptedException {
        SomeKindOfBuffer someKindOfBuffer = null;
        this.rgBufferSempaphores[iBuffer].acquire();
        someKindOfBuffer = this.getSomeKindOfBuffer(iBuffer);
        if(someKindOfBuffer.c(iBuffer) == null) {
            someKindOfBuffer = null;
        }

        return someKindOfBuffer;
    }

    SomeKindOfBuffer getAcquireBuffer(int iBuffer) throws InterruptedException {
        SomeKindOfBuffer someKindOfBuffer = null;
        this.rgOtherBufferSempahores[iBuffer].acquire();
        someKindOfBuffer = this.getSomeKindOfBuffer(iBuffer);
        return someKindOfBuffer;
    }

    public void releaseBuffer(int iBuffer) throws InterruptedException {
        SomeKindOfBuffer[] var2 = this.rgBuffers;
        synchronized(this.rgBuffers) {
            this.rgBuffers[iBuffer].d(iBuffer);
        }

        this.rgBufferSempaphores[iBuffer].release();
    }

    public void e(int iBuffer) throws InterruptedException {
        this.rgOtherBufferSempahores[iBuffer].release();
    }

    public void a(SomeKindOfBuffer var1) throws D2xxException {
        boolean var2 = false;
        byte var3 = 0;
        byte var4 = 0;
        boolean var5 = false;

        try {
            int var12 = var1.getSomeCount();
            if (var12 < 2) {
                var1.getByteBuffer().clear();
                return;
            }

            Object var8 = this.s;
            int var6;
            int var7;
            synchronized(this.s) {
                var6 = this.cbBufferFree();
                var7 = var12 - 2;
                if(var6 < var7) {
                    Log.d("ProcessBulkIn::", " Buffer is full, waiting for read....");
                    this.a(var5, var3, var4);
                    this.n.lock();
                    this.readBufferFull = true;
                }
            }

            if(var6 < var7) {
                this.condition.await();
                this.n.unlock();
            }

            this.b(var1);
        } catch (InterruptedException var10) {
            this.n.unlock();
            Log.e("ProcessInCtrl", "Exception in Full await!");
            var10.printStackTrace();
        } catch (Exception var11) {
            Log.e("ProcessInCtrl", "Exception in ProcessBulkIN");
            var11.printStackTrace();
            throw new D2xxException("Fatal error in BulkIn.");
        }

    }

    private void b(SomeKindOfBuffer var1) throws InterruptedException {
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
        var12 = var1.getByteBuffer();
        int var17 = var1.getSomeCount();
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
                    var9 = (short)(this.ftDevice.ftDeviceInfoListNode.modemStatus ^ (short)(var14 & 240));
                    this.ftDevice.ftDeviceInfoListNode.modemStatus = (short)(var14 & 240);
                    byte var15 = var12.get();
                    this.ftDevice.ftDeviceInfoListNode.lineStatus = (short)(var15 & 255);
                    var19 += 2;
                    if(var12.hasRemaining()) {
                        var10 = (short)(this.ftDevice.ftDeviceInfoListNode.lineStatus & 30);
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
                    var7 = this.sinkChannel.write(this.e, 0, var18);
                    if(var7 != (long)var4) {
                        Log.d("extractReadData::", "written != totalData, written= " + var7 + " totalData=" + var4);
                    }

                    this.incCbAvailableToRead((int) var7);
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

    public int read(byte[] rgbDest, int cbLength, long msWait)
        {
        boolean var5 = false;
        int cbRead = 0;
        int cbMaxBufferSize = this.driverParameters.getMaxBufferSize();
        long msNow = System.currentTimeMillis();
        ByteBuffer destBuffer = ByteBuffer.wrap(rgbDest, 0, cbLength);
        if (msWait == 0L)
            {
            msWait = (long) this.driverParameters.getReadTimeout();
            }

        while (this.ftDevice.isOpen())
            {
            if (this.cbAvailableToRead() >= cbLength)
                {
                synchronized (this.sourceChannel)
                    {
                    try
                        {
                        this.sourceChannel.read(destBuffer);
                        this.decCbAvailableToRead(cbLength);
                        }
                    catch (Exception e)
                        {
                        Log.d("readBulkInData::", "Cannot read data from Source!!");
                        e.printStackTrace();
                        }
                    }

                synchronized (this.s)
                    {
                    if (this.readBufferFull)
                        {
                        Log.i("FTDI debug::", "buffer is full , and also re start buffer");
                        this.n.lock();
                        this.condition.signalAll();
                        this.readBufferFull = false;
                        this.n.unlock();
                        }
                    }

                cbRead = cbLength;
                break;
                }

            try
                {
                this.q.lock();
                this.r.await(System.currentTimeMillis() - msNow, TimeUnit.MILLISECONDS);
                this.q.unlock();
                }
            catch (InterruptedException interrupt)
                {
                Log.d("readBulkInData::", "Cannot wait to read data!!");
                interrupt.printStackTrace();
                this.q.unlock();
                }

            if (System.currentTimeMillis() - msNow >= msWait)
                {
                break;
                }
            }

        return cbRead;
        }

    private int incCbAvailableToRead(int dcb) {
        synchronized(this.cbAvailableToReadLock) {
            this.cbAvailableToRead += dcb;
            int result = this.cbAvailableToRead;
            return result;
        }
    }

    private int decCbAvailableToRead(int dcb) {
        synchronized(this.cbAvailableToReadLock) {
            this.cbAvailableToRead -= dcb;
            int result = this.cbAvailableToRead;
            return result;
        }
    }

    private void clearCbAvailableToRead() {
        synchronized(this.cbAvailableToReadLock) {
            this.cbAvailableToRead = 0;
        }
    }

    public int cbAvailableToRead() {
        synchronized(this.cbAvailableToReadLock) {
            int result = this.cbAvailableToRead;
            return result;
        }
    }

    public int cbBufferFree() {
        return this.driverParameters.getMaxBufferSize() - this.cbAvailableToRead() - 1;
    }

    public int e() {
        int var1 = this.driverParameters.getBufferNumber();
        SomeKindOfBuffer var2 = null;
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

            this.clearCbAvailableToRead();

            for(int var5 = 0; var5 < var1; ++var5) {
                var2 = this.getSomeKindOfBuffer(var5);
                if(var2.d() && var2.getSomeCount() > 2) {
                    var2.clear();
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
            LocalBroadcastManager.getInstance(this.ftDevice.context).sendBroadcast(var11);
        }

        if(var2 != 0 && (var10.a & 2L) != 0L && (this.ftDevice.a ^ 2L) == 2L) {
            this.ftDevice.a |= 2L;
            var11 = new Intent("FT_EVENT_MODEM_STATUS");
            var11.putExtra("message", "FT_EVENT_MODEM_STATUS");
            LocalBroadcastManager.getInstance(this.ftDevice.context).sendBroadcast(var11);
        }

        if(var3 != 0 && (var10.a & 4L) != 0L && (this.ftDevice.a ^ 4L) == 4L) {
            this.ftDevice.a |= 4L;
            var11 = new Intent("FT_EVENT_LINE_STATUS");
            var11.putExtra("message", "FT_EVENT_LINE_STATUS");
            LocalBroadcastManager.getInstance(this.ftDevice.context).sendBroadcast(var11);
        }

        return 0;
    }

    public void f() throws InterruptedException {
        int var1 = this.driverParameters.getBufferNumber();

        for(int var2 = 0; var2 < var1; ++var2) {
            if(this.getSomeKindOfBuffer(var2).d()) {
                this.releaseBuffer(var2);
            }
        }

    }

    void g() {
        int var1;
        for(var1 = 0; var1 < this.bufferCount; ++var1) {
            try {
                this.e(var1);
            } catch (Exception var4) {
                Log.d("ProcessInCtrl", "Acquire read buffer " + var1 + " failed!");
                var4.printStackTrace();
            }

            this.rgBuffers[var1] = null;
            this.rgOtherBufferSempahores[var1] = null;
            this.rgBufferSempaphores[var1] = null;
        }

        for(var1 = 0; var1 < 256; ++var1) {
            this.e[var1] = null;
        }

        this.rgBufferSempaphores = null;
        this.rgOtherBufferSempahores = null;
        this.rgBuffers = null;
        this.e = null;
        this.d = null;
        if(this.readBufferFull) {
            this.n.lock();
            this.condition.signalAll();
            this.n.unlock();
        }

        this.q.lock();
        this.r.signalAll();
        this.q.unlock();
        this.n = null;
        this.condition = null;
        this.cbAvailableToReadLock = null;
        this.q = null;
        this.r = null;

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
