//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.hardware;

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;

public class ModernRoboticsUsbLegacyModule extends ModernRoboticsUsbDevice implements LegacyModule {
    public static final boolean DEBUG_LOGGING = false;
    public static final int MONITOR_LENGTH = 13;
    public static final byte START_ADDRESS = 3;
    public static final byte MIN_PORT_NUMBER = 0;
    public static final byte MAX_PORT_NUMBER = 5;
    public static final byte NUMBER_OF_PORTS = 6;
    public static final byte I2C_ACTION_FLAG = -1;
    public static final byte I2C_NO_ACTION_FLAG = 0;
    public static final byte SIZE_ANALOG_BUFFER = 2;
    public static final byte SIZE_I2C_BUFFER = 27;
    public static final byte SIZE_OF_PORT_BUFFER = 32;
    public static final byte NXT_MODE_ANALOG     = 0x00;
    public static final byte NXT_MODE_I2C        = 0x01;
    public static final byte NXT_MODE_9V_ENABLED = 0x02;
    public static final byte NXT_MODE_DIGITAL_0  = 0x04;
    public static final byte NXT_MODE_DIGITAL_1  = 0x08;
    // What do the missing three bits do? Anything? I'm guessing no...
    public static final byte NXT_MODE_READ       = 0x80;
    public static final byte NXT_MODE_WRITE      = 0;
    public static final byte BUFFER_FLAG_S0 = 1;
    public static final byte BUFFER_FLAG_S1 = 2;
    public static final byte BUFFER_FLAG_S2 = 4;
    public static final byte BUFFER_FLAG_S3 = 8;
    public static final byte BUFFER_FLAG_S4 = 16;
    public static final byte BUFFER_FLAG_S5 = 32;
    public static final int ADDRESS_BUFFER_STATUS = 3;
    public static final int ADDRESS_ANALOG_PORT_S0 = 4;
    public static final int ADDRESS_ANALOG_PORT_S1 = 6;
    public static final int ADDRESS_ANALOG_PORT_S2 = 8;
    public static final int ADDRESS_ANALOG_PORT_S3 = 10;
    public static final int ADDRESS_ANALOG_PORT_S4 = 12;
    public static final int ADDRESS_ANALOG_PORT_S5 = 14;
    public static final int ADDRESS_I2C_PORT_SO = 16;
    public static final int ADDRESS_I2C_PORT_S1 = 48;
    public static final int ADDRESS_I2C_PORT_S2 = 80;
    public static final int ADDRESS_I2C_PORT_S3 = 112;
    public static final int ADDRESS_I2C_PORT_S4 = 144;
    public static final int ADDRESS_I2C_PORT_S5 = 176;
    public static final byte OFFSET_I2C_PORT_MODE = 0;
    public static final byte OFFSET_I2C_PORT_I2C_ADDRESS = 1;
    public static final byte OFFSET_I2C_PORT_MEMORY_ADDRESS = 2;
    public static final byte OFFSET_I2C_PORT_MEMORY_LENGTH = 3;
    public static final byte OFFSET_I2C_PORT_MEMORY_BUFFER = 4;
    public static final byte OFFSET_I2C_PORT_FLAG = 31;
    public static final int[] ADDRESS_ANALOG_PORT_MAP = new int[]{4, 6, 8, 10, 12, 14};
    public static final int[] ADDRESS_I2C_PORT_MAP = new int[]{16, 48, 80, 112, 144, 176};
    public static final int[] BUFFER_FLAG_MAP = new int[]{1, 2, 4, 8, 16, 32};
    public static final int[] DIGITAL_LINE = new int[]{NXT_MODE_DIGITAL_0, NXT_MODE_DIGITAL_1};
    public static final int[] PORT_9V_CAPABLE = new int[]{4, 5};

    // First six are the data segments, length 32; last six are corresponding action flag segments, length 1
    private final ReadWriteRunnableSegment[] runnableSegments = new ReadWriteRunnableSegment[12];
    private final I2cPortReadyCallback[] portCallbacks = new I2cPortReadyCallback[6];

    protected ModernRoboticsUsbLegacyModule(SerialNumber serialNumber, RobotUsbDevice device, EventLoopManager manager) throws RobotCoreException, InterruptedException {
        super(serialNumber, manager, new ReadWriteRunnableStandard(serialNumber, device, MONITOR_LENGTH, START_ADDRESS, false));
        this.readWriteRunnable.setCallback(this);

        for(int port = 0; port < NUMBER_OF_PORTS; ++port) {
            this.runnableSegments[port]                   = this.readWriteRunnable.createSegment(port, ADDRESS_I2C_PORT_MAP[port], SIZE_OF_PORT_BUFFER);
            this.runnableSegments[port + NUMBER_OF_PORTS] = this.readWriteRunnable.createSegment(port + NUMBER_OF_PORTS, ADDRESS_I2C_PORT_MAP[port] + 31, 1);
            this.enableAnalogReadMode(port);
            this.readWriteRunnable.queueSegmentWrite(port);
        }
    }

    public String getDeviceName() {
        return "Modern Robotics USB Legacy Module";
    }

    public String getConnectionInfo() {
        return "USB " + this.getSerialNumber();
    }

    public void close() {
        super.close();
    }

    public void registerForI2cPortReadyCallback(I2cPortReadyCallback callback, int port) {
        this.portCallbacks[port] = callback;
    }

    public void deregisterForPortReadyCallback(int port) {
        this.portCallbacks[port] = null;
    }

    public void enableI2cReadMode(int physicalPort, int i2cAddress, int memAddress, int length) {
        this.validatePort(physicalPort);
        this.validateI2cBufferLength(length);

        try {
            this.runnableSegments[physicalPort].getWriteLock().lock();
            byte[] buffer = this.runnableSegments[physicalPort].getWriteBuffer();
            buffer[0] = (NXT_MODE_I2C | NXT_MODE_READ);
            buffer[1] = (byte)i2cAddress;
            buffer[2] = (byte)memAddress;
            buffer[3] = (byte)length;
        } finally {
            this.runnableSegments[physicalPort].getWriteLock().unlock();
        }
    }

    public void enableI2cWriteMode(int physicalPort, int i2cAddress, int memAddress, int length) {
        this.validatePort(physicalPort);
        this.validateI2cBufferLength(length);

        try {
            this.runnableSegments[physicalPort].getWriteLock().lock();
            byte[] buffer = this.runnableSegments[physicalPort].getWriteBuffer();
            buffer[0] = NXT_MODE_I2C;
            buffer[1] = (byte)i2cAddress;
            buffer[2] = (byte)memAddress;
            buffer[3] = (byte)length;
        } finally {
            this.runnableSegments[physicalPort].getWriteLock().unlock();
        }

    }

    public void enableAnalogReadMode(int physicalPort) {
        this.validatePort(physicalPort);

        try {
            this.runnableSegments[physicalPort].getWriteLock().lock();
            byte[] buffer = this.runnableSegments[physicalPort].getWriteBuffer();
            buffer[0] = NXT_MODE_ANALOG;
        } finally {
            this.runnableSegments[physicalPort].getWriteLock().unlock();
        }

        this.writeI2cCacheToController(physicalPort);
    }

    public void enable9v(int physicalPort, boolean enable) {
        if (Arrays.binarySearch(PORT_9V_CAPABLE, physicalPort) < 0) {
            throw new IllegalArgumentException("9v is only available on the following ports: " + Arrays.toString(PORT_9V_CAPABLE));
        } else {
            try {
                this.runnableSegments[physicalPort].getWriteLock().lock();
                byte b = this.runnableSegments[physicalPort].getWriteBuffer()[0];
                if(enable) {
                    b = (byte)(b | NXT_MODE_9V_ENABLED /*2*/);
                } else {
                    b &= ~NXT_MODE_9V_ENABLED /*-3*/;
                }

                this.runnableSegments[physicalPort].getWriteBuffer()[0] = b;
            } finally {
                this.runnableSegments[physicalPort].getWriteLock().unlock();
            }

            this.writeI2cCacheToController(physicalPort);
        }
    }

    public void setDigitalLine(int physicalPort, int line, boolean set) {
        this.validatePort(physicalPort);
        this.validateDigitalLine(line);

        try {
            this.runnableSegments[physicalPort].getWriteLock().lock();
            byte b = this.runnableSegments[physicalPort].getWriteBuffer()[0];
            if(set) {
                b = (byte)(b | DIGITAL_LINE[line]);
            } else {
                b = (byte)(b & ~DIGITAL_LINE[line]);
            }

            this.runnableSegments[physicalPort].getWriteBuffer()[0] = b;
        } finally {
            this.runnableSegments[physicalPort].getWriteLock().unlock();
        }

        this.writeI2cCacheToController(physicalPort);
    }

    public byte[] readAnalog(int physicalPort) {
        this.validatePort(physicalPort);
        return this.read(ADDRESS_ANALOG_PORT_MAP[physicalPort], SIZE_ANALOG_BUFFER);
    }

    public byte[] getCopyOfReadBuffer(int physicalPort) {
        this.validatePort(physicalPort);
        Object var2 = null;

        byte[] result;
        try {
            this.runnableSegments[physicalPort].getReadLock().lock();
            byte[] buffer = this.runnableSegments[physicalPort].getReadBuffer();
            byte cbResult = buffer[OFFSET_I2C_PORT_MEMORY_LENGTH];
            result = new byte[cbResult];
            System.arraycopy(buffer, OFFSET_I2C_PORT_MEMORY_BUFFER, result, 0, result.length);
        } finally {
            this.runnableSegments[physicalPort].getReadLock().unlock();
        }

        return result;
    }

    public byte[] getCopyOfWriteBuffer(int physicalPort) {
        this.validatePort(physicalPort);
        Object var2 = null;

        byte[] result;
        try {
            this.runnableSegments[physicalPort].getWriteLock().lock();
            byte[] buffer = this.runnableSegments[physicalPort].getWriteBuffer();
            byte cbResult = buffer[OFFSET_I2C_PORT_MEMORY_LENGTH];
            result = new byte[cbResult];
            System.arraycopy(buffer, OFFSET_I2C_PORT_MEMORY_BUFFER, result, 0, result.length);
        } finally {
            this.runnableSegments[physicalPort].getWriteLock().unlock();
        }

        return result;
    }

    public void copyBufferIntoWriteBuffer(int physicalPort, byte[] buffer) {
        this.validatePort(physicalPort);
        this.validateI2cBufferLength(buffer.length);

        try {
            this.runnableSegments[physicalPort].getWriteLock().lock();
            byte[] buffer = this.runnableSegments[physicalPort].getWriteBuffer();
            System.arraycopy(buffer, 0, buffer, OFFSET_I2C_PORT_MEMORY_BUFFER, buffer.length);
        } finally {
            this.runnableSegments[physicalPort].getWriteLock().unlock();
        }

    }

    public void setI2cPortActionFlag(int physicalPort) {
        this.validatePort(physicalPort);

        try {
            this.runnableSegments[physicalPort].getWriteLock().lock();
            byte[] buffer = this.runnableSegments[physicalPort].getWriteBuffer();
            buffer[31] = I2C_ACTION_FLAG;
        } finally {
            this.runnableSegments[physicalPort].getWriteLock().unlock();
        }

    }

    public boolean isI2cPortActionFlagSet(int physicalPort) {
        this.validatePort(physicalPort);
        boolean var2 = false;

        try {
            this.runnableSegments[physicalPort].getReadLock().lock();
            byte[] var3 = this.runnableSegments[physicalPort].getReadBuffer();
            var2 = var3[31] == I2C_ACTION_FLAG;
        } finally {
            this.runnableSegments[physicalPort].getReadLock().unlock();
        }

        return var2;
    }

    public void readI2cCacheFromController(int physicalPort) {
        this.validatePort(physicalPort);
        this.readWriteRunnable.queueSegmentRead(physicalPort);
    }

    public void writeI2cCacheToController(int physicalPort) {
        this.validatePort(physicalPort);
        this.readWriteRunnable.queueSegmentWrite(physicalPort);
    }

    public void writeI2cPortFlagOnlyToController(int physicalPort) {
        this.validatePort(physicalPort);
        ReadWriteRunnableSegment writeSegment = this.runnableSegments[physicalPort];
        ReadWriteRunnableSegment actionFlagSegment = this.runnableSegments[physicalPort + 6];

        try {
            writeSegment.getWriteLock().lock();
            actionFlagSegment.getWriteLock().lock();
            actionFlagSegment.getWriteBuffer()[0] = writeSegment.getWriteBuffer()[31];
        } finally {
            writeSegment.getWriteLock().unlock();
            actionFlagSegment.getWriteLock().unlock();
        }

        this.readWriteRunnable.queueSegmentWrite(physicalPort + 6);
    }

    public boolean isI2cPortInReadMode(int physicalPort) {
        this.validatePort(physicalPort);
        boolean var2 = false;

        try {
            this.runnableSegments[physicalPort].getReadLock().lock();
            byte[] var3 = this.runnableSegments[physicalPort].getReadBuffer();
            var2 = var3[0] == (NXT_MODE_I2C | NXT_MODE_READ);
        } finally {
            this.runnableSegments[physicalPort].getReadLock().unlock();
        }

        return var2;
    }

    public boolean isI2cPortInWriteMode(int physicalPort) {
        this.validatePort(physicalPort);
        boolean result = false;

        try {
            this.runnableSegments[physicalPort].getReadLock().lock();
            byte[] var3 = this.runnableSegments[physicalPort].getReadBuffer();
            result = var3[0] == NXT_MODE_I2C;
        } finally {
            this.runnableSegments[physicalPort].getReadLock().unlock();
        }

        return result;
    }

    public boolean isI2cPortReady(int physicalPort) {
        byte var2 = this.read(3);
        return this.a(physicalPort, var2);
    }

    private void validatePort(int port) {
        if(port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are %d..%d", new Object[]{port, MIN_PORT_NUMBER, MAX_PORT_NUMBER}));
        }
    }

    private void validateI2cBufferLength(int cb) {
        if(cb < 0 || cb > SIZE_I2C_BUFFER) {
            throw new IllegalArgumentException(String.format("buffer length of %d is invalid; max value is %d", new Object[]{Integer.valueOf(cb), Byte.valueOf((byte)SIZE_I2C_BUFFER)}));
        }
    }

    private void validateDigitalLine(int line) {
        if(line != 0 && line != 1) {
            throw new IllegalArgumentException("line is invalid, valid lines are 0 and 1");
        }
    }

    public void readComplete() throws InterruptedException {
        if(this.portCallbacks != null) {
            byte var1 = this.read(3);

            for(int var2 = 0; var2 < 6; ++var2) {
                if(this.portCallbacks[var2] != null && this.a(var2, var1)) {
                    this.portCallbacks[var2].portIsReady(var2);
                }
            }

        }
    }

    private boolean a(int var1, byte var2) {
        return (var2 & BUFFER_FLAG_MAP[var1]) == 0;
    }

    public Lock getI2cReadCacheLock(int physicalPort) {
        this.validatePort(physicalPort);
        return this.runnableSegments[physicalPort].getReadLock();
    }

    public Lock getI2cWriteCacheLock(int physicalPort) {
        this.validatePort(physicalPort);
        return this.runnableSegments[physicalPort].getWriteLock();
    }

    public byte[] getI2cReadCache(int physicalPort) {
        this.validatePort(physicalPort);
        return this.runnableSegments[physicalPort].getReadBuffer();
    }

    public byte[] getI2cWriteCache(int physicalPort) {
        this.validatePort(physicalPort);
        return this.runnableSegments[physicalPort].getWriteBuffer();
    }

    /** @deprecated */
    @Deprecated
    public void readI2cCacheFromModule(int port) {
        this.readI2cCacheFromController(port);
    }

    /** @deprecated */
    @Deprecated
    public void writeI2cCacheToModule(int port) {
        this.writeI2cCacheToController(port);
    }

    /** @deprecated */
    @Deprecated
    public void writeI2cPortFlagOnlyToModule(int port) {
        this.writeI2cPortFlagOnlyToController(port);
    }
}
