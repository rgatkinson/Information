//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.hardware;

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
    private volatile boolean writeNeeded = false;
    protected Callback callback;
    protected final boolean DEBUG_LOGGING;

    public ReadWriteRunnableStandard(SerialNumber serialNumber, RobotUsbDevice robotUsbDevice, int monitorLength, int startAddress, boolean debug) {
        this.serialNumber = serialNumber;
        this.startAddress = startAddress;
        this.monitorLength = monitorLength;
        this.DEBUG_LOGGING = debug;
        this.callback = new EmptyCallback();
        this.usbHandler = new ReadWriteRunnableUsbHandler(robotUsbDevice);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void blockUntilReady() throws RobotCoreException, InterruptedException {
        if(this.shutdownComplete) {
            RobotLog.w("sync device block requested, but device is shut down - " + this.serialNumber);
            RobotLog.setGlobalErrorMsg("There were problems communicating with a Modern Robotics USB device for an extended period of time.");
            throw new RobotCoreException("cannot block, device is shut down");
        }
    }

    public void startBlockingWork() {
    }

    public boolean writeNeeded() {
        return this.writeNeeded;
    }

    public void setWriteNeeded(boolean set) {
        this.writeNeeded = set;
    }

    public void write(int address, byte[] data) {
        synchronized(this.localDeviceWriteCache) {
            System.arraycopy(data, 0, this.localDeviceWriteCache, address, data.length);
            this.writeNeeded = true;
        }
    }

    public byte[] readFromWriteCache(int address, int size) {
        synchronized(this.localDeviceWriteCache) {
            return Arrays.copyOfRange(this.localDeviceWriteCache, address, address + size);
        }
    }

    public byte[] read(int address, int size) {
        synchronized(this.localDeviceReadCache) {
            return Arrays.copyOfRange(this.localDeviceReadCache, address, address + size);
        }
    }

    public void close() {
        try {
            this.blockUntilReady();
            this.startBlockingWork();
        } catch (InterruptedException var6) {
            ;
        } catch (RobotCoreException var7) {
            ;
        } finally {
            this.running = false;

            while(!this.shutdownComplete) {
                Thread.yield();
            }

            this.usbHandler.close();
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
        boolean isFirstReadWriteCycle = true;
        int ibFirst = 0;
        byte[] monitorData = new byte[this.monitorLength + this.startAddress];
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

                ReadWriteRunnableSegment segment;
                byte[] usbData;
                try {
                    this.usbHandler.read(ibFirst, monitorData);

                    while(!this.segmentReadQueue.isEmpty()) {
                        segment = (ReadWriteRunnableSegment)this.segments.get(this.segmentReadQueue.remove());
                        usbData = new byte[segment.getReadBuffer().length];
                        this.usbHandler.read(segment.getAddress(), usbData);

                        try {
                            segment.getReadLock().lock();
                            System.arraycopy(usbData, 0, segment.getReadBuffer(), 0, segment.getReadBuffer().length);
                        } finally {
                            segment.getReadLock().unlock();
                        }
                    }
                } catch (RobotCoreException var34) {
                    RobotLog.w(String.format("could not read from device %s: %s", new Object[]{this.serialNumber, var34.getMessage()}));
                }

                byte[] var39 = this.localDeviceReadCache;
                synchronized(this.localDeviceReadCache) {
                    System.arraycopy(monitorData, 0, this.localDeviceReadCache, ibFirst, monitorData.length);
                }

                if(this.DEBUG_LOGGING) {
                    this.dumpBuffers("read", this.localDeviceReadCache);
                }

                this.callback.readComplete();
                this.waitForSyncdEvents();
                if (isFirstReadWriteCycle) {
                    ibFirst = this.startAddress;
                    monitorData = new byte[this.monitorLength];
                    isFirstReadWriteCycle = false;
                }

                var39 = this.localDeviceWriteCache;
                synchronized(this.localDeviceWriteCache) {
                    System.arraycopy(this.localDeviceWriteCache, ibFirst, monitorData, 0, monitorData.length);
                }

                try {
                    if (this.writeNeeded()) {
                        this.usbHandler.write(ibFirst, monitorData);
                        this.setWriteNeeded(false);
                    }

                    for(; !this.segmentWriteQueue.isEmpty(); this.usbHandler.write(segment.getAddress(), usbData)) {
                        segment = (ReadWriteRunnableSegment)this.segments.get(this.segmentWriteQueue.remove());

                        try {
                            segment.getWriteLock().lock();
                            usbData = Arrays.copyOf(segment.getWriteBuffer(), segment.getWriteBuffer().length);
                        } finally {
                            segment.getWriteLock().unlock();
                        }
                    }
                } catch (RobotCoreException var35) {
                    RobotLog.w(String.format("could not write to device %s: %s", this.serialNumber, var35.getMessage()));
                }

                if(this.DEBUG_LOGGING) {
                    this.dumpBuffers("write", this.localDeviceWriteCache);
                }

                this.callback.writeComplete();
                this.usbHandler.throwIfUsbErrorCountIsTooHigh();
            }
        } catch (NullPointerException var36) {
            RobotLog.w(String.format("could not write to device %s: FTDI Null Pointer Exception", new Object[]{this.serialNumber}));
            RobotLog.logStacktrace(var36);
            RobotLog.setGlobalErrorMsg("There was a problem communicating with a Modern Robotics USB device");
        } catch (InterruptedException var37) {
            RobotLog.w(String.format("could not write to device %s: Interrupted Exception", new Object[]{this.serialNumber}));
        } catch (RobotCoreException var38) {
            RobotLog.w(var38.getMessage());
            RobotLog.setGlobalErrorMsg(String.format("There was a problem communicating with a Modern Robotics USB device %s", new Object[]{this.serialNumber}));
        }

        RobotLog.v(String.format("stopped read/write loop for device %s", new Object[]{this.serialNumber}));
        this.running = false;
        this.shutdownComplete = true;
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
