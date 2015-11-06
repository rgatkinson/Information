//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.hardware;

import com.qualcomm.hardware.ReadWriteRunnable;
import com.qualcomm.hardware.ReadWriteRunnableSegment;
import com.qualcomm.hardware.ReadWriteRunnable.Callback;
import com.qualcomm.hardware.ReadWriteRunnable.EmptyCallback;
import com.qualcomm.modernrobotics.ReadWriteRunnableUsbHandler;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice.Channel;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.TypeConversion;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ReadWriteRunnableStandard implements ReadWriteRunnable {
    protected final byte[] localDeviceReadCache = new byte[256];
    protected final byte[] localDeviceWriteCache = new byte[256];
    protected Map<Integer, ReadWriteRunnableSegment> segments = new HashMap();
    protected ConcurrentLinkedQueue<Integer> segmentReadQueue = new ConcurrentLinkedQueue();
    protected ConcurrentLinkedQueue<Integer> segmentWriteQueue = new ConcurrentLinkedQueue();
    protected final SerialNumber serialNumber;
    protected final ReadWriteRunnableUsbHandler usbHandler;
    protected int startAddress;
    protected int monitorLength;
    protected volatile boolean running = false;
    protected volatile boolean shutdownComplete = false;
    private volatile boolean a = false;
    protected Callback callback;
    protected final boolean DEBUG_LOGGING;

    public ReadWriteRunnableStandard(SerialNumber serialNumber, RobotUsbDevice device, int monitorLength, int startAddress, boolean debug) {
        this.serialNumber = serialNumber;
        this.startAddress = startAddress;
        this.monitorLength = monitorLength;
        this.DEBUG_LOGGING = debug;
        this.callback = new EmptyCallback();
        this.usbHandler = new ReadWriteRunnableUsbHandler(device);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void blockUntilReady() throws RobotCoreException, InterruptedException {
        if (this.shutdownComplete) {
            RobotLog.w("sync device block requested, but device is shut down - " + this.serialNumber);
            RobotLog.setGlobalErrorMsg("There were problems communicating with a Modern Robotics USB device for an extended period of time.");
            throw new RobotCoreException("cannot block, device is shut down");
        }
    }

    public void startBlockingWork() {
    }

    public boolean writeNeeded() {
        return this.a;
    }

    public void setWriteNeeded(boolean set) {
        this.a = set;
    }

    public void write(int address, byte[] data) {
        byte[] var3 = this.localDeviceWriteCache;
        synchronized(this.localDeviceWriteCache) {
            System.arraycopy(data, 0, this.localDeviceWriteCache, address, data.length);
            this.a = true;
        }
    }

    public byte[] readFromWriteCache(int address, int size) {
        byte[] var3 = this.localDeviceWriteCache;
        synchronized(this.localDeviceWriteCache) {
            return Arrays.copyOfRange(this.localDeviceWriteCache, address, address + size);
        }
    }

    public byte[] read(int address, int size) {
        byte[] var3 = this.localDeviceReadCache;
        synchronized(this.localDeviceReadCache) {
            return Arrays.copyOfRange(this.localDeviceReadCache, address, address + size);
        }
    }

    public void close() {
        try {
            this.blockUntilReady();
            this.startBlockingWork();
        } catch (InterruptedException var6) {
            RobotLog.w("Exception while closing USB device: " + var6.getMessage());
        } catch (RobotCoreException var7) {
            RobotLog.w("Exception while closing USB device: " + var7.getMessage());
        } finally {
            this.running = false;

            while(!this.shutdownComplete) {
                Thread.yield();
            }

        }

    }

    public ReadWriteRunnableSegment createSegment(int key, int address, int size) {
        ReadWriteRunnableSegment var4 = new ReadWriteRunnableSegment(address, size);
        this.segments.put(Integer.valueOf(key), var4);
        return var4;
    }

    public void destroySegment(int key) {
        this.segments.remove(Integer.valueOf(key));
    }

    public ReadWriteRunnableSegment getSegment(int key) {
        return (ReadWriteRunnableSegment)this.segments.get(Integer.valueOf(key));
    }

    public void queueSegmentRead(int key) {
        this.queueIfNotAlreadyQueued(key, this.segmentReadQueue);
    }

    public void queueSegmentWrite(int key) {
        this.queueIfNotAlreadyQueued(key, this.segmentWriteQueue);
    }

    public void run() {
        boolean var1 = true;
        int var2 = 0;
        byte[] var3 = new byte[this.monitorLength + this.startAddress];
        ElapsedTime var4 = new ElapsedTime();
        String var5 = "Device " + this.serialNumber.toString();
        this.running = true;
        RobotLog.v(String.format("starting read/write loop for device %s", new Object[]{this.serialNumber}));

        try {
            this.usbHandler.purge(Channel.RX);

            while(this.running) {
                if(this.DEBUG_LOGGING) {
                    var4.log(var5);
                    var4.reset();
                }

                ReadWriteRunnableSegment var6;
                byte[] var7;
                try {
                    this.usbHandler.read(var2, var3);

                    while(!this.segmentReadQueue.isEmpty()) {
                        var6 = (ReadWriteRunnableSegment)this.segments.get(this.segmentReadQueue.remove());
                        var7 = new byte[var6.getReadBuffer().length];
                        this.usbHandler.read(var6.getAddress(), var7);

                        try {
                            var6.getReadLock().lock();
                            System.arraycopy(var7, 0, var6.getReadBuffer(), 0, var6.getReadBuffer().length);
                        } finally {
                            var6.getReadLock().unlock();
                        }
                    }
                } catch (RobotCoreException var47) {
                    RobotLog.w(String.format("could not read from device %s: %s", new Object[]{this.serialNumber, var47.getMessage()}));
                }

                byte[] var53 = this.localDeviceReadCache;
                synchronized(this.localDeviceReadCache) {
                    System.arraycopy(var3, 0, this.localDeviceReadCache, var2, var3.length);
                }

                if(this.DEBUG_LOGGING) {
                    this.dumpBuffers("read", this.localDeviceReadCache);
                }

                this.callback.readComplete();
                this.waitForSyncdEvents();
                if(var1) {
                    var2 = this.startAddress;
                    var3 = new byte[this.monitorLength];
                    var1 = false;
                }

                var53 = this.localDeviceWriteCache;
                synchronized(this.localDeviceWriteCache) {
                    System.arraycopy(this.localDeviceWriteCache, var2, var3, 0, var3.length);
                }

                try {
                    if(this.writeNeeded()) {
                        this.usbHandler.write(var2, var3);
                        this.setWriteNeeded(false);
                    }

                    for(; !this.segmentWriteQueue.isEmpty(); this.usbHandler.write(var6.getAddress(), var7)) {
                        var6 = (ReadWriteRunnableSegment)this.segments.get(this.segmentWriteQueue.remove());

                        try {
                            var6.getWriteLock().lock();
                            var7 = Arrays.copyOf(var6.getWriteBuffer(), var6.getWriteBuffer().length);
                        } finally {
                            var6.getWriteLock().unlock();
                        }
                    }
                } catch (RobotCoreException var48) {
                    RobotLog.w(String.format("could not write to device %s: %s", new Object[]{this.serialNumber, var48.getMessage()}));
                }

                if(this.DEBUG_LOGGING) {
                    this.dumpBuffers("write", this.localDeviceWriteCache);
                }

                this.callback.writeComplete();
                this.usbHandler.throwIfUsbErrorCountIsTooHigh();
            }
        } catch (NullPointerException var49) {
            RobotLog.w(String.format("could not write to device %s: FTDI Null Pointer Exception", new Object[]{this.serialNumber}));
            RobotLog.logStacktrace(var49);
            RobotLog.setGlobalErrorMsg("There was a problem communicating with a Modern Robotics USB device");
        } catch (InterruptedException var50) {
            RobotLog.w(String.format("could not write to device %s: Interrupted Exception", new Object[]{this.serialNumber}));
        } catch (RobotCoreException var51) {
            RobotLog.w(var51.getMessage());
            RobotLog.setGlobalErrorMsg(String.format("There was a problem communicating with a Modern Robotics USB device %s", new Object[]{this.serialNumber}));
        } finally {
            this.usbHandler.close();
            this.running = false;
            this.shutdownComplete = true;
        }

        RobotLog.v(String.format("stopped read/write loop for device %s", new Object[]{this.serialNumber}));
    }

    protected void waitForSyncdEvents() throws RobotCoreException, InterruptedException {
    }

    protected void dumpBuffers(String name, byte[] byteArray) {
        RobotLog.v("Dumping " + name + " buffers for " + this.serialNumber);
        StringBuilder var3 = new StringBuilder(1024);

        for(int var4 = 0; var4 < this.startAddress + this.monitorLength; ++var4) {
            var3.append(String.format(" %02x", new Object[]{Integer.valueOf(TypeConversion.unsignedByteToInt(byteArray[var4]))}));
            if((var4 + 1) % 16 == 0) {
                var3.append("\n");
            }
        }

        RobotLog.v(var3.toString());
    }

    protected void queueIfNotAlreadyQueued(int key, ConcurrentLinkedQueue<Integer> queue) {
        if(!queue.contains(Integer.valueOf(key))) {
            queue.add(Integer.valueOf(key));
        }

    }
}
