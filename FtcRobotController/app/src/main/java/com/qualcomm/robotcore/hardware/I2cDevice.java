package com.qualcomm.robotcore.hardware;

import java.util.concurrent.locks.Lock;

public class I2cDevice implements HardwareDevice
{
    private I2cController a;
    private int b;
    
    public I2cDevice(final I2cController a, final int b) {
        this.a = null;
        this.b = -1;
        this.a = a;
        this.b = b;
    }
    
    @Override
    public void close() {
    }
    
    public void copyBufferIntoWriteBuffer(final byte[] array) {
        this.a.copyBufferIntoWriteBuffer(this.b, array);
    }
    
    public void deregisterForPortReadyCallback() {
        this.a.deregisterForPortReadyCallback(this.b);
    }
    
    public void enableI2cReadMode(final int n, final int n2, final int n3) {
        this.a.enableI2cReadMode(this.b, n, n2, n3);
    }
    
    public void enableI2cWriteMode(final int n, final int n2, final int n3) {
        this.a.enableI2cWriteMode(this.b, n, n2, n3);
    }
    
    @Override
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; port " + this.b;
    }
    
    public byte[] getCopyOfReadBuffer() {
        return this.a.getCopyOfReadBuffer(this.b);
    }
    
    public byte[] getCopyOfWriteBuffer() {
        return this.a.getCopyOfWriteBuffer(this.b);
    }
    
    @Override
    public String getDeviceName() {
        return "I2cDevice";
    }
    
    public byte[] getI2cReadCache() {
        return this.a.getI2cReadCache(this.b);
    }
    
    public Lock getI2cReadCacheLock() {
        return this.a.getI2cReadCacheLock(this.b);
    }
    
    public byte[] getI2cWriteCache() {
        return this.a.getI2cWriteCache(this.b);
    }
    
    public Lock getI2cWriteCacheLock() {
        return this.a.getI2cWriteCacheLock(this.b);
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    public boolean isI2cPortActionFlagSet() {
        return this.a.isI2cPortActionFlagSet(this.b);
    }
    
    public boolean isI2cPortInReadMode() {
        return this.a.isI2cPortInReadMode(this.b);
    }
    
    public boolean isI2cPortInWriteMode() {
        return this.a.isI2cPortInWriteMode(this.b);
    }
    
    public boolean isI2cPortReady() {
        return this.a.isI2cPortReady(this.b);
    }
    
    public void readI2cCacheFromController() {
        this.a.readI2cCacheFromController(this.b);
    }
    
    @Deprecated
    public void readI2cCacheFromModule() {
        this.readI2cCacheFromController();
    }
    
    public void registerForI2cPortReadyCallback(final I2cController.I2cPortReadyCallback i2cPortReadyCallback) {
        this.a.registerForI2cPortReadyCallback(i2cPortReadyCallback, this.b);
    }
    
    public void setI2cPortActionFlag() {
        this.a.setI2cPortActionFlag(this.b);
    }
    
    public void writeI2cCacheToController() {
        this.a.writeI2cCacheToController(this.b);
    }
    
    @Deprecated
    public void writeI2cCacheToModule() {
        this.writeI2cCacheToController();
    }
    
    public void writeI2cPortFlagOnlyToController() {
        this.a.writeI2cPortFlagOnlyToController(this.b);
    }
    
    @Deprecated
    public void writeI2cPortFlagOnlyToModule() {
        this.writeI2cPortFlagOnlyToController();
    }
}
