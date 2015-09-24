package com.qualcomm.hardware;

import java.util.concurrent.locks.Lock;
import java.util.Arrays;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.LegacyModule;

public class ModernRoboticsUsbLegacyModule extends ModernRoboticsUsbDevice implements LegacyModule
{
    public static final int[] ADDRESS_ANALOG_PORT_MAP;
    public static final int ADDRESS_ANALOG_PORT_S0 = 4;
    public static final int ADDRESS_ANALOG_PORT_S1 = 6;
    public static final int ADDRESS_ANALOG_PORT_S2 = 8;
    public static final int ADDRESS_ANALOG_PORT_S3 = 10;
    public static final int ADDRESS_ANALOG_PORT_S4 = 12;
    public static final int ADDRESS_ANALOG_PORT_S5 = 14;
    public static final int ADDRESS_BUFFER_STATUS = 3;
    public static final int[] ADDRESS_I2C_PORT_MAP;
    public static final int ADDRESS_I2C_PORT_S1 = 48;
    public static final int ADDRESS_I2C_PORT_S2 = 80;
    public static final int ADDRESS_I2C_PORT_S3 = 112;
    public static final int ADDRESS_I2C_PORT_S4 = 144;
    public static final int ADDRESS_I2C_PORT_S5 = 176;
    public static final int ADDRESS_I2C_PORT_SO = 16;
    public static final int[] BUFFER_FLAG_MAP;
    public static final byte BUFFER_FLAG_S0 = 1;
    public static final byte BUFFER_FLAG_S1 = 2;
    public static final byte BUFFER_FLAG_S2 = 4;
    public static final byte BUFFER_FLAG_S3 = 8;
    public static final byte BUFFER_FLAG_S4 = 16;
    public static final byte BUFFER_FLAG_S5 = 32;
    public static final boolean DEBUG_LOGGING = false;
    public static final int[] DIGITAL_LINE;
    public static final byte I2C_ACTION_FLAG = -1;
    public static final byte I2C_NO_ACTION_FLAG = 0;
    public static final byte MAX_PORT_NUMBER = 5;
    public static final byte MIN_PORT_NUMBER = 0;
    public static final int MONITOR_LENGTH = 13;
    public static final byte NUMBER_OF_PORTS = 6;
    public static final byte NXT_MODE_9V_ENABLED = 2;
    public static final byte NXT_MODE_ANALOG = 0;
    public static final byte NXT_MODE_DIGITAL_0 = 4;
    public static final byte NXT_MODE_DIGITAL_1 = 8;
    public static final byte NXT_MODE_I2C = 1;
    public static final byte NXT_MODE_READ = Byte.MIN_VALUE;
    public static final byte NXT_MODE_WRITE = 0;
    public static final byte OFFSET_I2C_PORT_FLAG = 31;
    public static final byte OFFSET_I2C_PORT_I2C_ADDRESS = 1;
    public static final byte OFFSET_I2C_PORT_MEMORY_ADDRESS = 2;
    public static final byte OFFSET_I2C_PORT_MEMORY_BUFFER = 4;
    public static final byte OFFSET_I2C_PORT_MEMORY_LENGTH = 3;
    public static final byte OFFSET_I2C_PORT_MODE = 0;
    public static final int[] PORT_9V_CAPABLE;
    public static final byte SIZE_ANALOG_BUFFER = 2;
    public static final byte SIZE_I2C_BUFFER = 27;
    public static final byte SIZE_OF_PORT_BUFFER = 32;
    public static final byte START_ADDRESS = 3;
    private final ReadWriteRunnableSegment[] a;
    private final I2cPortReadyCallback[] b;
    
    static {
        ADDRESS_ANALOG_PORT_MAP = new int[] { 4, 6, 8, 10, 12, 14 };
        ADDRESS_I2C_PORT_MAP = new int[] { 16, 48, 80, 112, 144, 176 };
        BUFFER_FLAG_MAP = new int[] { 1, 2, 4, 8, 16, 32 };
        DIGITAL_LINE = new int[] { 4, 8 };
        PORT_9V_CAPABLE = new int[] { 4, 5 };
    }
    
    protected ModernRoboticsUsbLegacyModule(final SerialNumber serialNumber, final RobotUsbDevice robotUsbDevice, final EventLoopManager eventLoopManager) throws RobotCoreException, InterruptedException {
        int i = 0;
        super(serialNumber, eventLoopManager, new ReadWriteRunnableStandard(serialNumber, robotUsbDevice, 13, 3, false));
        this.a = new ReadWriteRunnableSegment[12];
        this.b = new I2cPortReadyCallback[6];
        this.readWriteRunnable.setCallback((ReadWriteRunnable.Callback)this);
        while (i < 6) {
            this.a[i] = this.readWriteRunnable.createSegment(i, ModernRoboticsUsbLegacyModule.ADDRESS_I2C_PORT_MAP[i], 32);
            this.a[i + 6] = this.readWriteRunnable.createSegment(i + 6, 31 + ModernRoboticsUsbLegacyModule.ADDRESS_I2C_PORT_MAP[i], 1);
            this.enableAnalogReadMode(i);
            this.readWriteRunnable.queueSegmentWrite(i);
            ++i;
        }
    }
    
    private void a(final int n) {
        if (n < 0 || n > 5) {
            throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are %d..%d", n, (byte)0, (byte)5));
        }
    }
    
    private boolean a(final int n, final byte b) {
        return (b & ModernRoboticsUsbLegacyModule.BUFFER_FLAG_MAP[n]) == 0x0;
    }
    
    private void b(final int n) {
        if (n < 0 || n > 27) {
            throw new IllegalArgumentException(String.format("buffer length of %d is invalid; max value is %d", n, (byte)27));
        }
    }
    
    private void c(final int n) {
        if (n != 0 && n != 1) {
            throw new IllegalArgumentException("line is invalid, valid lines are 0 and 1");
        }
    }
    
    @Override
    public void close() {
        super.close();
    }
    
    @Override
    public void copyBufferIntoWriteBuffer(final int n, final byte[] array) {
        this.a(n);
        this.b(array.length);
        try {
            this.a[n].getWriteLock().lock();
            System.arraycopy(array, 0, this.a[n].getWriteBuffer(), 4, array.length);
        }
        finally {
            this.a[n].getWriteLock().unlock();
        }
    }
    
    @Override
    public void deregisterForPortReadyCallback(final int n) {
        this.b[n] = null;
    }
    
    @Override
    public void enable9v(final int n, final boolean b) {
        if (Arrays.binarySearch(ModernRoboticsUsbLegacyModule.PORT_9V_CAPABLE, n) < 0) {
            throw new IllegalArgumentException("9v is only available on the following ports: " + Arrays.toString(ModernRoboticsUsbLegacyModule.PORT_9V_CAPABLE));
        }
        try {
            this.a[n].getWriteLock().lock();
            final byte b2 = this.a[n].getWriteBuffer()[0];
            byte b3;
            if (b) {
                b3 = (byte)(b2 | 0x2);
            }
            else {
                b3 = (byte)(b2 & 0xFFFFFFFD);
            }
            this.a[n].getWriteBuffer()[0] = b3;
            this.a[n].getWriteLock().unlock();
            this.writeI2cCacheToController(n);
        }
        finally {
            this.a[n].getWriteLock().unlock();
        }
    }
    
    @Override
    public void enableAnalogReadMode(final int n) {
        this.a(n);
        try {
            this.a[n].getWriteLock().lock();
            this.a[n].getWriteBuffer()[0] = 0;
            this.a[n].getWriteLock().unlock();
            this.writeI2cCacheToController(n);
        }
        finally {
            this.a[n].getWriteLock().unlock();
        }
    }
    
    @Override
    public void enableI2cReadMode(final int n, final int n2, final int n3, final int n4) {
        this.a(n);
        this.b(n4);
        try {
            this.a[n].getWriteLock().lock();
            final byte[] writeBuffer = this.a[n].getWriteBuffer();
            writeBuffer[0] = -127;
            writeBuffer[1] = (byte)n2;
            writeBuffer[2] = (byte)n3;
            writeBuffer[3] = (byte)n4;
        }
        finally {
            this.a[n].getWriteLock().unlock();
        }
    }
    
    @Override
    public void enableI2cWriteMode(final int n, final int n2, final int n3, final int n4) {
        this.a(n);
        this.b(n4);
        try {
            this.a[n].getWriteLock().lock();
            final byte[] writeBuffer = this.a[n].getWriteBuffer();
            writeBuffer[0] = 1;
            writeBuffer[1] = (byte)n2;
            writeBuffer[2] = (byte)n3;
            writeBuffer[3] = (byte)n4;
        }
        finally {
            this.a[n].getWriteLock().unlock();
        }
    }
    
    @Override
    public String getConnectionInfo() {
        return "USB " + this.getSerialNumber();
    }
    
    @Override
    public byte[] getCopyOfReadBuffer(final int n) {
        this.a(n);
        try {
            this.a[n].getReadLock().lock();
            final byte[] readBuffer = this.a[n].getReadBuffer();
            final byte[] array = new byte[readBuffer[3]];
            System.arraycopy(readBuffer, 4, array, 0, array.length);
            return array;
        }
        finally {
            this.a[n].getReadLock().unlock();
        }
    }
    
    @Override
    public byte[] getCopyOfWriteBuffer(final int n) {
        this.a(n);
        try {
            this.a[n].getWriteLock().lock();
            final byte[] writeBuffer = this.a[n].getWriteBuffer();
            final byte[] array = new byte[writeBuffer[3]];
            System.arraycopy(writeBuffer, 4, array, 0, array.length);
            return array;
        }
        finally {
            this.a[n].getWriteLock().unlock();
        }
    }
    
    @Override
    public String getDeviceName() {
        return "Modern Robotics USB Legacy Module";
    }
    
    @Override
    public byte[] getI2cReadCache(final int n) {
        this.a(n);
        return this.a[n].getReadBuffer();
    }
    
    @Override
    public Lock getI2cReadCacheLock(final int n) {
        this.a(n);
        return this.a[n].getReadLock();
    }
    
    @Override
    public byte[] getI2cWriteCache(final int n) {
        this.a(n);
        return this.a[n].getWriteBuffer();
    }
    
    @Override
    public Lock getI2cWriteCacheLock(final int n) {
        this.a(n);
        return this.a[n].getWriteLock();
    }
    
    @Override
    public boolean isI2cPortActionFlagSet(final int n) {
        this.a(n);
        try {
            this.a[n].getReadLock().lock();
            return this.a[n].getReadBuffer()[31] == -1;
        }
        finally {
            this.a[n].getReadLock().unlock();
        }
    }
    
    @Override
    public boolean isI2cPortInReadMode(final int n) {
        this.a(n);
        try {
            this.a[n].getReadLock().lock();
            final byte b = this.a[n].getReadBuffer()[0];
            boolean b2 = false;
            if (b == -127) {
                b2 = true;
            }
            return b2;
        }
        finally {
            this.a[n].getReadLock().unlock();
        }
    }
    
    @Override
    public boolean isI2cPortInWriteMode(final int n) {
        byte b = (byte)(true ? 1 : 0);
        this.a(n);
        try {
            this.a[n].getReadLock().lock();
            if (this.a[n].getReadBuffer()[0] != b) {
                b = (byte)(false ? 1 : 0);
            }
            return b != 0;
        }
        finally {
            this.a[n].getReadLock().unlock();
        }
    }
    
    @Override
    public boolean isI2cPortReady(final int n) {
        return this.a(n, this.read(3));
    }
    
    @Override
    public byte[] readAnalog(final int n) {
        this.a(n);
        return this.read(ModernRoboticsUsbLegacyModule.ADDRESS_ANALOG_PORT_MAP[n], 2);
    }
    
    @Override
    public void readComplete() throws InterruptedException {
        if (this.b != null) {
            final byte read = this.read(3);
            for (int i = 0; i < 6; ++i) {
                if (this.b[i] != null && this.a(i, read)) {
                    this.b[i].portIsReady(i);
                }
            }
        }
    }
    
    @Override
    public void readI2cCacheFromController(final int n) {
        this.a(n);
        this.readWriteRunnable.queueSegmentRead(n);
    }
    
    @Deprecated
    @Override
    public void readI2cCacheFromModule(final int n) {
        this.readI2cCacheFromController(n);
    }
    
    @Override
    public void registerForI2cPortReadyCallback(final I2cPortReadyCallback i2cPortReadyCallback, final int n) {
        this.b[n] = i2cPortReadyCallback;
    }
    
    @Override
    public void setDigitalLine(final int n, final int n2, final boolean b) {
        this.a(n);
        this.c(n2);
        try {
            this.a[n].getWriteLock().lock();
            final byte b2 = this.a[n].getWriteBuffer()[0];
            byte b3;
            if (b) {
                b3 = (byte)(b2 | ModernRoboticsUsbLegacyModule.DIGITAL_LINE[n2]);
            }
            else {
                b3 = (byte)(b2 & ~ModernRoboticsUsbLegacyModule.DIGITAL_LINE[n2]);
            }
            this.a[n].getWriteBuffer()[0] = b3;
            this.a[n].getWriteLock().unlock();
            this.writeI2cCacheToController(n);
        }
        finally {
            this.a[n].getWriteLock().unlock();
        }
    }
    
    @Override
    public void setI2cPortActionFlag(final int n) {
        this.a(n);
        try {
            this.a[n].getWriteLock().lock();
            this.a[n].getWriteBuffer()[31] = -1;
        }
        finally {
            this.a[n].getWriteLock().unlock();
        }
    }
    
    @Override
    public void writeI2cCacheToController(final int n) {
        this.a(n);
        this.readWriteRunnable.queueSegmentWrite(n);
    }
    
    @Deprecated
    @Override
    public void writeI2cCacheToModule(final int n) {
        this.writeI2cCacheToController(n);
    }
    
    @Override
    public void writeI2cPortFlagOnlyToController(final int n) {
        this.a(n);
        final ReadWriteRunnableSegment readWriteRunnableSegment = this.a[n];
        final ReadWriteRunnableSegment readWriteRunnableSegment2 = this.a[n + 6];
        try {
            readWriteRunnableSegment.getWriteLock().lock();
            readWriteRunnableSegment2.getWriteLock().lock();
            readWriteRunnableSegment2.getWriteBuffer()[0] = readWriteRunnableSegment.getWriteBuffer()[31];
            readWriteRunnableSegment.getWriteLock().unlock();
            readWriteRunnableSegment2.getWriteLock().unlock();
            this.readWriteRunnable.queueSegmentWrite(n + 6);
        }
        finally {
            readWriteRunnableSegment.getWriteLock().unlock();
            readWriteRunnableSegment2.getWriteLock().unlock();
        }
    }
    
    @Deprecated
    @Override
    public void writeI2cPortFlagOnlyToModule(final int n) {
        this.writeI2cPortFlagOnlyToController(n);
    }
}
