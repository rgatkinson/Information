package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.SerialNumber;
import java.util.concurrent.locks.Lock;

public interface I2cController extends HardwareDevice
{
    public static final byte I2C_BUFFER_START_ADDRESS = 4;
    
    void copyBufferIntoWriteBuffer(int p0, byte[] p1);
    
    void deregisterForPortReadyCallback(int p0);
    
    void enableI2cReadMode(int p0, int p1, int p2, int p3);
    
    void enableI2cWriteMode(int p0, int p1, int p2, int p3);
    
    byte[] getCopyOfReadBuffer(int p0);
    
    byte[] getCopyOfWriteBuffer(int p0);
    
    byte[] getI2cReadCache(int p0);
    
    Lock getI2cReadCacheLock(int p0);
    
    byte[] getI2cWriteCache(int p0);
    
    Lock getI2cWriteCacheLock(int p0);
    
    SerialNumber getSerialNumber();
    
    boolean isI2cPortActionFlagSet(int p0);
    
    boolean isI2cPortInReadMode(int p0);
    
    boolean isI2cPortInWriteMode(int p0);
    
    boolean isI2cPortReady(int p0);
    
    void readI2cCacheFromController(int p0);
    
    @Deprecated
    void readI2cCacheFromModule(int p0);
    
    void registerForI2cPortReadyCallback(I2cPortReadyCallback p0, int p1);
    
    void setI2cPortActionFlag(int p0);
    
    void writeI2cCacheToController(int p0);
    
    @Deprecated
    void writeI2cCacheToModule(int p0);
    
    void writeI2cPortFlagOnlyToController(int p0);
    
    @Deprecated
    void writeI2cPortFlagOnlyToModule(int p0);
    
    public interface I2cPortReadyCallback
    {
        void portIsReady(int p0);
    }
}
