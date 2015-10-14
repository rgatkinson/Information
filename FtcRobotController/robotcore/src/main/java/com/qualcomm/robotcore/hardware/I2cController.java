package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.SerialNumber;

import java.util.concurrent.locks.Lock;

public interface I2cController extends HardwareDevice {
    byte I2C_BUFFER_START_ADDRESS = 4;

    void copyBufferIntoWriteBuffer(int var1, byte[] var2);

    void deregisterForPortReadyCallback(int var1);

    void enableI2cReadMode(int var1, int var2, int var3, int var4);

    void enableI2cWriteMode(int var1, int var2, int var3, int var4);

    byte[] getCopyOfReadBuffer(int var1);

    byte[] getCopyOfWriteBuffer(int var1);

    byte[] getI2cReadCache(int var1);

    Lock getI2cReadCacheLock(int var1);

    byte[] getI2cWriteCache(int var1);

    Lock getI2cWriteCacheLock(int var1);

    SerialNumber getSerialNumber();

    boolean isI2cPortActionFlagSet(int var1);

    boolean isI2cPortInReadMode(int var1);

    boolean isI2cPortInWriteMode(int var1);

    boolean isI2cPortReady(int var1);

    void readI2cCacheFromController(int var1);

    @Deprecated
    void readI2cCacheFromModule(int var1);

    void registerForI2cPortReadyCallback(I2cPortReadyCallback var1, int var2);

    void setI2cPortActionFlag(int var1);

    void writeI2cCacheToController(int var1);

    @Deprecated
    void writeI2cCacheToModule(int var1);

    void writeI2cPortFlagOnlyToController(int var1);

    @Deprecated
    void writeI2cPortFlagOnlyToModule(int var1);

    interface I2cPortReadyCallback {
        void portIsReady(int var1);
    }
}
