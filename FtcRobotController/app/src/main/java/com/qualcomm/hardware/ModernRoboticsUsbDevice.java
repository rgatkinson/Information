package com.qualcomm.hardware;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.eventloop.SyncdDevice;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.concurrent.Executors;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.concurrent.ExecutorService;

public abstract class ModernRoboticsUsbDevice implements Callback
{
    public static final int DEVICE_ID_DC_MOTOR_CONTROLLER = 77;
    public static final int DEVICE_ID_DEVICE_INTERFACE_MODULE = 65;
    public static final int DEVICE_ID_LEGACY_MODULE = 73;
    public static final int DEVICE_ID_SERVO_CONTROLLER = 83;
    public static final int MFG_CODE_MODERN_ROBOTICS = 77;
    protected ReadWriteRunnable readWriteRunnable;
    protected ExecutorService readWriteService;
    protected SerialNumber serialNumber;
    
    public ModernRoboticsUsbDevice(final SerialNumber serialNumber, final EventLoopManager eventLoopManager, final ReadWriteRunnable readWriteRunnable) throws RobotCoreException, InterruptedException {
        this.readWriteService = Executors.newSingleThreadExecutor();
        this.serialNumber = serialNumber;
        this.readWriteRunnable = readWriteRunnable;
        RobotLog.v("Starting up device " + serialNumber.toString());
        this.readWriteService.execute(readWriteRunnable);
        readWriteRunnable.blockUntilReady();
        readWriteRunnable.setCallback((ReadWriteRunnable.Callback)this);
        eventLoopManager.registerSyncdDevice(readWriteRunnable);
    }
    
    public void close() {
        RobotLog.v("Shutting down device " + this.serialNumber.toString());
        this.readWriteService.shutdown();
        this.readWriteRunnable.close();
    }
    
    public abstract String getDeviceName();
    
    public SerialNumber getSerialNumber() {
        return this.serialNumber;
    }
    
    public int getVersion() {
        return this.read(0);
    }
    
    public byte read(final int n) {
        return this.read(n, 1)[0];
    }
    
    public byte[] read(final int n, final int n2) {
        return this.readWriteRunnable.read(n, n2);
    }
    
    @Override
    public void readComplete() throws InterruptedException {
    }
    
    public byte readFromWriteCache(final int n) {
        return this.readFromWriteCache(n, 1)[0];
    }
    
    public byte[] readFromWriteCache(final int n, final int n2) {
        return this.readWriteRunnable.readFromWriteCache(n, n2);
    }
    
    public void write(final int n, final byte b) {
        this.write(n, new byte[] { b });
    }
    
    public void write(final int n, final double n2) {
        this.write(n, new byte[] { (byte)n2 });
    }
    
    public void write(final int n, final int n2) {
        this.write(n, new byte[] { (byte)n2 });
    }
    
    public void write(final int n, final byte[] array) {
        this.readWriteRunnable.write(n, array);
    }
    
    @Override
    public void writeComplete() throws InterruptedException {
    }
}
