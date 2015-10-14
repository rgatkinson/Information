package com.qualcomm.robotcore.hardware;

import java.util.concurrent.locks.Lock;

public class I2cDevice implements HardwareDevice {
    private I2cController a = null;
    private int b = -1;

    public I2cDevice(I2cController var1, int var2) {
        this.a = var1;
        this.b = var2;
    }

    public void close() {
    }

    public void copyBufferIntoWriteBuffer(byte[] var1) {
        this.a.copyBufferIntoWriteBuffer(this.b, var1);
    }

    public void deregisterForPortReadyCallback() {
        this.a.deregisterForPortReadyCallback(this.b);
    }

    public void enableI2cReadMode(int var1, int var2, int var3) {
        this.a.enableI2cReadMode(this.b, var1, var2, var3);
    }

    public void enableI2cWriteMode(int var1, int var2, int var3) {
        this.a.enableI2cWriteMode(this.b, var1, var2, var3);
    }

    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; port " + this.b;
    }

    public byte[] getCopyOfReadBuffer() {
        return this.a.getCopyOfReadBuffer(this.b);
    }

    public byte[] getCopyOfWriteBuffer() {
        return this.a.getCopyOfWriteBuffer(this.b);
    }

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

    public void registerForI2cPortReadyCallback(I2cController.I2cPortReadyCallback var1) {
        this.a.registerForI2cPortReadyCallback(var1, this.b);
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
