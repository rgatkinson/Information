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
    private Semaphore[] mWritable;
    private Semaphore[] mReadable;
    private InBuffer[] mInputBufs;
    private ByteBuffer d;
    private ByteBuffer[] byteBuffers;
    private Pipe pipe;
    private SinkChannel sinkChannel;
    private SourceChannel sourceChannel;
    private int bufferCount;
    private int cbAvailableToRead;
    private Object cbAvailableToReadLock;
    private FT_Device ftDevice;
    private DriverParameters driverParameters;
    private Lock mInFullLock;
    private Condition mFullCon;
    private boolean mSinkFull;
    private Lock qLock;
    private Condition qCondition;
    private Object mSinkFullLock;
    private int cbMaxPacketSizeIn;

    public ProcessInCtrl(FT_Device ftDevice) {
        this.ftDevice = ftDevice;
        this.driverParameters = this.ftDevice.getDriverParameters();
        this.bufferCount = this.driverParameters.getBufferCount();
        int maxBufferSize = this.driverParameters.getMaxBufferSize();
        this.cbMaxPacketSizeIn = this.ftDevice.getCbMaxPacketSizeIn();
        this.mWritable = new Semaphore[this.bufferCount];
        this.mReadable = new Semaphore[this.bufferCount];
        this.mInputBufs = new InBuffer[this.bufferCount];
        this.byteBuffers = new ByteBuffer[256];
        this.mInFullLock = new ReentrantLock();
        this.mFullCon = this.mInFullLock.newCondition();
        this.mSinkFull = false;
        this.qLock = new ReentrantLock();
        this.qCondition = this.qLock.newCondition();
        this.cbAvailableToReadLock = new Object();
        this.mSinkFullLock = new Object();
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
            this.mInputBufs[iBuffer] = new InBuffer(maxBufferSize);
            this.mReadable[iBuffer] = new Semaphore(1);
            this.mWritable[iBuffer] = new Semaphore(1);

            try {
                this.acquireReadableBuffer(iBuffer);
            } catch (Exception var5) {
                Log.d("ProcessInCtrl", "Acquire read buffer " + iBuffer + " failed!");
                var5.printStackTrace();
            }
        }

    }

    boolean readBufferFull() {
        return this.mSinkFull;
    }

    DriverParameters getDriverParameters() {
        return this.driverParameters;
    }

    InBuffer getBuffer(int iBuffer) {
        InBuffer result = null;
        synchronized(this.mInputBufs) {
            if(iBuffer >= 0 && iBuffer < this.bufferCount) {
                result = this.mInputBufs[iBuffer];
            }

            return result;
        }
    }

    InBuffer acquireWritableBuffer(int iBuffer) throws InterruptedException {
        InBuffer result = null;
        this.mWritable[iBuffer].acquire();
        result = this.getBuffer(iBuffer);
        if (result.setSomeStatus(iBuffer) == null) {
            result = null;
        }

        return result;
    }

    InBuffer acquireReadableBuffer(int iBuffer) throws InterruptedException {
        InBuffer inBuffer = null;
        this.mReadable[iBuffer].acquire();
        inBuffer = this.getBuffer(iBuffer);
        return inBuffer;
    }

    public void releaseWritableBuffer(int iBuffer) throws InterruptedException {
        InBuffer[] var2 = this.mInputBufs;
        synchronized(this.mInputBufs) {
            this.mInputBufs[iBuffer].release(iBuffer);
        }
        this.mWritable[iBuffer].release();
    }

    public void releaseReadableBuffer(int iBuffer) throws InterruptedException {
        this.mReadable[iBuffer].release();
    }

    public void processBulkInData(InBuffer inBuffer) throws D2xxException {
        byte signalEvents = 0;
        byte signalLineEvents = 0;
        boolean signalRxChar = false;

        try {
            int cbSomeCount = inBuffer.getLength();
            if (cbSomeCount < 2) {
                inBuffer.getByteBuffer().clear();
                return;
            }

            int cbBufferFree;
            int var7;
            synchronized(this.mSinkFullLock) {
                cbBufferFree = this.cbBufferFree();
                var7 = cbSomeCount - 2;
                if (cbBufferFree < var7) {
                    Log.d("ProcessBulkIn::", " Buffer is full, waiting for read....");
                    this.processEventChars(signalRxChar, signalEvents, signalLineEvents);
                    this.mInFullLock.lock();
                    this.mSinkFull = true;
                }
            }

            if(cbBufferFree < var7) {
                this.mFullCon.await();
                this.mInFullLock.unlock();
            }

            this.extractReadData(inBuffer);
        } catch (InterruptedException var10) {
            this.mInFullLock.unlock();
            Log.e("ProcessInCtrl", "Exception in Full await!");
            var10.printStackTrace();
        } catch (Exception var11) {
            Log.e("ProcessInCtrl", "Exception in ProcessBulkIN");
            var11.printStackTrace();
            throw new D2xxException("Fatal error in BulkIn.");
        }

    }

    private void extractReadData(InBuffer inBuffer) throws InterruptedException {
        int cbTotalData = 0;
        long cbWritten = 0L;
        short var9 = 0;
        short var10 = 0;
        boolean var11 = false;
        ByteBuffer byteBuffer = inBuffer.getByteBuffer();
        int cbSomeCount = inBuffer.getLength();
        if (cbSomeCount > 0) {
            int var18 = cbSomeCount / this.cbMaxPacketSizeIn + (cbSomeCount % this.cbMaxPacketSizeIn > 0?1:0);

            for(int var13 = 0; var13 < var18; ++var13) {
                int var19;
                int var20;
                if(var13 == var18 - 1) {
                    var20 = cbSomeCount;
                    byteBuffer.limit(cbSomeCount);
                    var19 = var13 * this.cbMaxPacketSizeIn;
                    byteBuffer.position(var19);
                    byte bNext = byteBuffer.get();
                    var9 = (short)(this.ftDevice.ftDeviceInfoListNode.modemStatus ^ (short)(bNext & 240));
                    this.ftDevice.ftDeviceInfoListNode.modemStatus = (short)(bNext & 240);
                    byte bNextNext = byteBuffer.get();
                    this.ftDevice.ftDeviceInfoListNode.lineStatus = (short)(bNextNext & 255);
                    var19 += 2;
                    if (byteBuffer.hasRemaining()) {
                        var10 = (short)(this.ftDevice.ftDeviceInfoListNode.lineStatus & 30);
                    } else {
                        var10 = 0;
                    }
                } else {
                    var20 = (var13 + 1) * this.cbMaxPacketSizeIn;
                    byteBuffer.limit(var20);
                    var19 = var13 * this.cbMaxPacketSizeIn + 2;
                    byteBuffer.position(var19);
                }

                cbTotalData += var20 - var19;
                this.byteBuffers[var13] = byteBuffer.slice();
            }

            if(cbTotalData != 0) {
                var11 = true;

                try {
                    cbWritten = this.sinkChannel.write(this.byteBuffers, 0, var18);
                    if(cbWritten != (long)cbTotalData) {
                        Log.d("extractReadData::", "written != totalData, written= " + cbWritten + " totalData=" + cbTotalData);
                    }

                    this.incCbAvailableToRead((int) cbWritten);
                    this.qLock.lock();
                    this.qCondition.signalAll();
                    this.qLock.unlock();
                } catch (Exception e) {
                    Log.d("extractReadData::", "Write data to sink failed!!");
                    e.printStackTrace();
                }
            }

            byteBuffer.clear();
            this.processEventChars(var11, var9, var10);
        }

    }

    /** this is the core read routine */
    public int read(byte[] rgbDest, int cbLength, long msWait)
        {
        int cbRead = 0;
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

                synchronized (this.mSinkFullLock)
                    {
                    if (this.mSinkFull)
                        {
                        Log.i("FTDI debug::", "buffer is full , and also re start buffer");
                        this.mInFullLock.lock();
                        this.mFullCon.signalAll();
                        this.mSinkFull = false;
                        this.mInFullLock.unlock();
                        }
                    }

                cbRead = cbLength;
                break;
                }

            try
                {
                this.qLock.lock();
                this.qCondition.await(System.currentTimeMillis() - msNow, TimeUnit.MILLISECONDS);
                this.qLock.unlock();
                }
            catch (InterruptedException interrupt)
                {
                Log.d("readBulkInData::", "Cannot wait to read data!!");
                interrupt.printStackTrace();
                this.qLock.unlock();
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

    public int purgeINData() {
        int var1 = this.driverParameters.getBufferCount();
        InBuffer var2 = null;
        boolean var3 = false;
        ByteBuffer var4 = this.d;
        synchronized(this.d) {
            int cbRead;
            try {
                do {
                    this.sourceChannel.configureBlocking(false);
                    cbRead = this.sourceChannel.read(this.d);
                    this.d.clear();
                } while(cbRead != 0);
            } catch (Exception var6) {
                var6.printStackTrace();
            }

            this.clearCbAvailableToRead();

            for(int var5 = 0; var5 < var1; ++var5) {
                var2 = this.getBuffer(var5);
                if(var2.isSomeStatus() && var2.getLength() > 2) {
                    var2.purge();
                }
            }

            return 0;
        }
    }

    public int processEventChars(boolean var1, short var2, short var3) throws InterruptedException {
        long var6 = 0L;
        boolean var8 = false;
        boolean var9 = false;
        var6 = 0L;
        q var10 = new q();
        var10.a = this.ftDevice.i.a;
        Intent intent;
        if(var1 && (var10.a & 1L) != 0L && (this.ftDevice.a ^ 1L) == 1L) {
            this.ftDevice.a |= 1L;
            intent = new Intent("FT_EVENT_RXCHAR");
            intent.putExtra("message", "FT_EVENT_RXCHAR");
            LocalBroadcastManager.getInstance(this.ftDevice.context).sendBroadcast(intent);
        }

        if(var2 != 0 && (var10.a & 2L) != 0L && (this.ftDevice.a ^ 2L) == 2L) {
            this.ftDevice.a |= 2L;
            intent = new Intent("FT_EVENT_MODEM_STATUS");
            intent.putExtra("message", "FT_EVENT_MODEM_STATUS");
            LocalBroadcastManager.getInstance(this.ftDevice.context).sendBroadcast(intent);
        }

        if(var3 != 0 && (var10.a & 4L) != 0L && (this.ftDevice.a ^ 4L) == 4L) {
            this.ftDevice.a |= 4L;
            intent = new Intent("FT_EVENT_LINE_STATUS");
            intent.putExtra("message", "FT_EVENT_LINE_STATUS");
            LocalBroadcastManager.getInstance(this.ftDevice.context).sendBroadcast(intent);
        }

        return 0;
    }

    public void releaseWritableBuffers() throws InterruptedException {
        int bufferCount = this.driverParameters.getBufferCount();

        for(int iBuffer = 0; iBuffer < bufferCount; ++iBuffer) {
            if(this.getBuffer(iBuffer).isSomeStatus()) {
                this.releaseWritableBuffer(iBuffer);
            }
        }

    }

    void g() {
        int var1;
        for(var1 = 0; var1 < this.bufferCount; ++var1) {
            try {
                this.releaseReadableBuffer(var1);
            } catch (Exception var4) {
                Log.d("ProcessInCtrl", "Acquire read buffer " + var1 + " failed!");
                var4.printStackTrace();
            }

            this.mInputBufs[var1] = null;
            this.mReadable[var1] = null;
            this.mWritable[var1] = null;
        }

        for(var1 = 0; var1 < 256; ++var1) {
            this.byteBuffers[var1] = null;
        }

        this.mWritable = null;
        this.mReadable = null;
        this.mInputBufs = null;
        this.byteBuffers = null;
        this.d = null;
        if(this.mSinkFull) {
            this.mInFullLock.lock();
            this.mFullCon.signalAll();
            this.mInFullLock.unlock();
        }

        this.qLock.lock();
        this.qCondition.signalAll();
        this.qLock.unlock();
        this.mInFullLock = null;
        this.mFullCon = null;
        this.cbAvailableToReadLock = null;
        this.qLock = null;
        this.qCondition = null;

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
