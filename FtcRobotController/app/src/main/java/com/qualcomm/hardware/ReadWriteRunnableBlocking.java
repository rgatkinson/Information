package com.qualcomm.hardware;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;

public class ReadWriteRunnableBlocking extends ReadWriteRunnableStandard
{
    private volatile boolean a;
    protected final Condition blockingCondition;
    protected final Lock blockingLock;
    protected BlockingState blockingState;
    protected final Condition waitingCondition;
    protected final Lock waitingLock;
    
    public ReadWriteRunnableBlocking(final SerialNumber serialNumber, final RobotUsbDevice robotUsbDevice, final int n, final int n2, final boolean b) {
        super(serialNumber, robotUsbDevice, n, n2, b);
        this.blockingLock = new ReentrantLock();
        this.waitingLock = new ReentrantLock();
        this.blockingCondition = this.blockingLock.newCondition();
        this.waitingCondition = this.waitingLock.newCondition();
        this.blockingState = BlockingState.BLOCKING;
        this.a = false;
    }
    
    @Override
    public void blockUntilReady() throws RobotCoreException, InterruptedException {
        Label_0094: {
            try {
                this.blockingLock.lock();
                Block_3: {
                    while (this.blockingState == BlockingState.BLOCKING) {
                        this.blockingCondition.await(100L, TimeUnit.MILLISECONDS);
                        if (this.shutdownComplete) {
                            break Block_3;
                        }
                    }
                    break Label_0094;
                }
                RobotLog.w("sync device block requested, but device is shut down - " + this.serialNumber);
                RobotLog.setGlobalErrorMsg("There were problems communicating with a Modern Robotics USB device for an extended period of time.");
                throw new RobotCoreException("cannot block, device is shut down");
            }
            finally {
                this.blockingLock.unlock();
            }
        }
        this.blockingLock.unlock();
    }
    
    @Override
    public void setWriteNeeded(final boolean a) {
        this.a = a;
    }
    
    @Override
    public void startBlockingWork() {
        try {
            this.waitingLock.lock();
            this.blockingState = BlockingState.BLOCKING;
            this.waitingCondition.signalAll();
        }
        finally {
            this.waitingLock.unlock();
        }
    }
    
    @Override
    protected void waitForSyncdEvents() throws RobotCoreException, InterruptedException {
        Label_0128: {
            try {
                this.blockingLock.lock();
                this.blockingState = BlockingState.WAITING;
                this.blockingCondition.signalAll();
                this.blockingLock.unlock();
                try {
                    this.waitingLock.lock();
                    Block_5: {
                        while (this.blockingState == BlockingState.WAITING) {
                            this.waitingCondition.await();
                            if (this.shutdownComplete) {
                                break Block_5;
                            }
                        }
                        break Label_0128;
                    }
                    RobotLog.w("wait for sync'd events requested, but device is shut down - " + this.serialNumber);
                    throw new RobotCoreException("cannot block, device is shut down");
                }
                finally {
                    this.waitingLock.unlock();
                }
            }
            finally {
                this.blockingLock.unlock();
            }
        }
        this.waitingLock.unlock();
    }
    
    @Override
    public void write(final int n, final byte[] array) {
        synchronized (this.localDeviceWriteCache) {
            System.arraycopy(array, 0, this.localDeviceWriteCache, n, array.length);
            this.a = true;
        }
    }
    
    @Override
    public boolean writeNeeded() {
        return this.a;
    }
}
