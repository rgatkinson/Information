//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.hardware;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReadWriteRunnableBlocking extends ReadWriteRunnableStandard {
    protected final Lock blockingLock = new ReentrantLock();
    protected final Lock waitingLock = new ReentrantLock();
    protected final Condition blockingCondition;
    protected final Condition waitingCondition;
    protected BlockingState blockingState;
    private volatile boolean writeNeeded;

    public ReadWriteRunnableBlocking(SerialNumber serialNumber, RobotUsbDevice device, int monitorLength, int startAddress, boolean debug) {
        super(serialNumber, device, monitorLength, startAddress, debug);
        this.blockingCondition = this.blockingLock.newCondition();
        this.waitingCondition = this.waitingLock.newCondition();
        this.blockingState = BlockingState.BLOCKING;
        this.writeNeeded = false;
    }

    // called just before OpMode loop()
    // called on the EventLoopManager.EventLoopRunnable thread
    // net effect: waits until ReadWriteRunnable thread calls waitForSyncdEvents(), then returns
    public void blockUntilReady() throws RobotCoreException, InterruptedException {
        try {
            this.blockingLock.lock();

            // wait until BlockingState.WAITING
            while(this.blockingState == BlockingState.BLOCKING) {
                this.blockingCondition.await(100L, TimeUnit.MILLISECONDS);
                if(this.shutdownComplete) {
                    RobotLog.w("sync device block requested, but device is shut down - " + this.serialNumber);
                    RobotLog.setGlobalErrorMsg("There were problems communicating with a Modern Robotics USB device for an extended period of time.");
                    throw new RobotCoreException("cannot block, device is shut down");
                }
            }
        } finally {
            this.blockingLock.unlock();
        }

    }

    // called just after OpMode loop()
    // called on the EventLoopManager.EventLoopRunnable thread
    public void startBlockingWork() {

        // set BlockingState.BLOCKING
        try {
            this.waitingLock.lock();
            this.blockingState = BlockingState.BLOCKING;
            this.waitingCondition.signalAll();
        } finally {
            this.waitingLock.unlock();
        }

    }

    // called immediately after readComplete() callback
    // called on the ReadWriteRunnable thread
    // net effect: ReadWriteRunnable thread is parked here while OpMode loop() runs
    protected void waitForSyncdEvents() throws RobotCoreException, InterruptedException {

        // set BlockingState.WAITING
        // signal blockingCondition
        try {
            this.blockingLock.lock();
            this.blockingState = BlockingState.WAITING;
            this.blockingCondition.signalAll();
        } finally {
            this.blockingLock.unlock();
        }

        // await BlockingState.BLOCKING
        try {
            this.waitingLock.lock();
            while(this.blockingState == BlockingState.WAITING) {
                this.waitingCondition.await();
                if(this.shutdownComplete) {
                    RobotLog.w("wait for sync\'d events requested, but device is shut down - " + this.serialNumber);
                    throw new RobotCoreException("cannot block, device is shut down");
                }
            }
        } finally {
            this.waitingLock.unlock();
        }

    }

    public void write(int address, byte[] data) {
        synchronized(this.localDeviceWriteCache) {
            System.arraycopy(data, 0, this.localDeviceWriteCache, address, data.length);
            this.writeNeeded = true;
        }
    }

    public boolean writeNeeded() {
        return this.writeNeeded;
    }

    public void setWriteNeeded(boolean set) {
        this.writeNeeded = set;
    }

}
