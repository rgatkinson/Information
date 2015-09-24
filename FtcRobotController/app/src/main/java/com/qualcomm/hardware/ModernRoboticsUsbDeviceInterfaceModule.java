package com.qualcomm.hardware;

import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;

public class ModernRoboticsUsbDeviceInterfaceModule extends ModernRoboticsUsbDevice implements DeviceInterfaceModule
{
    public static final int ADDRESS_ANALOG_PORT_A0 = 4;
    public static final int ADDRESS_ANALOG_PORT_A1 = 6;
    public static final int ADDRESS_ANALOG_PORT_A2 = 8;
    public static final int ADDRESS_ANALOG_PORT_A3 = 10;
    public static final int ADDRESS_ANALOG_PORT_A4 = 12;
    public static final int ADDRESS_ANALOG_PORT_A5 = 14;
    public static final int ADDRESS_ANALOG_PORT_A6 = 16;
    public static final int ADDRESS_ANALOG_PORT_A7 = 18;
    public static final int[] ADDRESS_ANALOG_PORT_MAP;
    public static final int ADDRESS_BUFFER_STATUS = 3;
    public static final int[] ADDRESS_DIGITAL_BIT_MASK;
    public static final int ADDRESS_DIGITAL_INPUT_STATE = 20;
    public static final int ADDRESS_DIGITAL_IO_CONTROL = 21;
    public static final int ADDRESS_DIGITAL_OUTPUT_STATE = 22;
    public static final int ADDRESS_I2C0 = 48;
    public static final int ADDRESS_I2C1 = 80;
    public static final int ADDRESS_I2C2 = 112;
    public static final int ADDRESS_I2C3 = 144;
    public static final int ADDRESS_I2C4 = 176;
    public static final int ADDRESS_I2C5 = 208;
    public static final int[] ADDRESS_I2C_PORT_MAP;
    public static final int ADDRESS_LED_SET = 23;
    public static final int ADDRESS_PULSE_OUTPUT_PORT_0 = 36;
    public static final int ADDRESS_PULSE_OUTPUT_PORT_1 = 40;
    public static final int[] ADDRESS_PULSE_OUTPUT_PORT_MAP;
    public static final int ADDRESS_VOLTAGE_OUTPUT_PORT_0 = 24;
    public static final int ADDRESS_VOLTAGE_OUTPUT_PORT_1 = 30;
    public static final int[] ADDRESS_VOLTAGE_OUTPUT_PORT_MAP;
    public static final int ANALOG_VOLTAGE_OUTPUT_BUFFER_SIZE = 5;
    public static final byte BUFFER_FLAG_I2C0 = 1;
    public static final byte BUFFER_FLAG_I2C1 = 2;
    public static final byte BUFFER_FLAG_I2C2 = 4;
    public static final byte BUFFER_FLAG_I2C3 = 8;
    public static final byte BUFFER_FLAG_I2C4 = 16;
    public static final byte BUFFER_FLAG_I2C5 = 32;
    public static final int[] BUFFER_FLAG_MAP;
    public static final int D0_MASK = 1;
    public static final int D1_MASK = 2;
    public static final int D2_MASK = 4;
    public static final int D3_MASK = 8;
    public static final int D4_MASK = 16;
    public static final int D5_MASK = 32;
    public static final int D6_MASK = 64;
    public static final int D7_MASK = 128;
    public static final boolean DEBUG_LOGGING = false;
    public static final byte I2C_ACTION_FLAG = -1;
    public static final byte I2C_MODE_READ = Byte.MIN_VALUE;
    public static final byte I2C_MODE_WRITE = 0;
    public static final byte I2C_NO_ACTION_FLAG = 0;
    public static final int I2C_PORT_BUFFER_SIZE = 32;
    public static final int LED_0_BIT_MASK = 1;
    public static final int LED_1_BIT_MASK = 2;
    public static final int[] LED_BIT_MASK_MAP;
    public static final int MAX_ANALOG_PORT_NUMBER = 7;
    public static final int MAX_I2C_PORT_NUMBER = 5;
    public static final int MIN_ANALOG_PORT_NUMBER = 0;
    public static final int MIN_I2C_PORT_NUMBER = 0;
    public static final int MONITOR_LENGTH = 21;
    public static final int NUMBER_OF_PORTS = 6;
    public static final int OFFSET_ANALOG_VOLTAGE_OUTPUT_FREQ = 2;
    public static final int OFFSET_ANALOG_VOLTAGE_OUTPUT_MODE = 4;
    public static final int OFFSET_ANALOG_VOLTAGE_OUTPUT_VOLTAGE = 0;
    public static final int OFFSET_I2C_PORT_FLAG = 31;
    public static final int OFFSET_I2C_PORT_I2C_ADDRESS = 1;
    public static final int OFFSET_I2C_PORT_MEMORY_ADDRESS = 2;
    public static final int OFFSET_I2C_PORT_MEMORY_BUFFER = 4;
    public static final int OFFSET_I2C_PORT_MEMORY_LENGTH = 3;
    public static final int OFFSET_I2C_PORT_MODE = 0;
    public static final int OFFSET_PULSE_OUTPUT_PERIOD = 2;
    public static final int OFFSET_PULSE_OUTPUT_TIME = 0;
    public static final int PULSE_OUTPUT_BUFFER_SIZE = 4;
    public static final int SIZE_ANALOG_BUFFER = 2;
    public static final int SIZE_I2C_BUFFER = 27;
    public static final int START_ADDRESS = 3;
    public static final int WORD_SIZE = 2;
    private static final int[] a;
    private static final int[] b;
    private static final int[] c;
    private static final int[] d;
    private final I2cPortReadyCallback[] e;
    private final ElapsedTime[] f;
    private ReadWriteRunnableSegment[] g;
    private ReadWriteRunnableSegment[] h;
    private ReadWriteRunnableSegment[] i;
    private ReadWriteRunnableSegment[] j;
    
    static {
        LED_BIT_MASK_MAP = new int[] { 1, 2 };
        ADDRESS_DIGITAL_BIT_MASK = new int[] { 1, 2, 4, 8, 16, 32, 64, 128 };
        ADDRESS_ANALOG_PORT_MAP = new int[] { 4, 6, 8, 10, 12, 14, 16, 18 };
        ADDRESS_VOLTAGE_OUTPUT_PORT_MAP = new int[] { 24, 30 };
        ADDRESS_PULSE_OUTPUT_PORT_MAP = new int[] { 36, 40 };
        ADDRESS_I2C_PORT_MAP = new int[] { 48, 80, 112, 144, 176, 208 };
        BUFFER_FLAG_MAP = new int[] { 1, 2, 4, 8, 16, 32 };
        a = new int[] { 0, 1 };
        b = new int[] { 2, 3 };
        c = new int[] { 4, 5, 6, 7, 8, 9 };
        d = new int[] { 10, 11, 12, 13, 14, 15 };
    }
    
    protected ModernRoboticsUsbDeviceInterfaceModule(final SerialNumber serialNumber, final RobotUsbDevice robotUsbDevice, final EventLoopManager eventLoopManager) throws RobotCoreException, InterruptedException {
        super(serialNumber, eventLoopManager, new ReadWriteRunnableStandard(serialNumber, robotUsbDevice, 21, 3, false));
        this.e = new I2cPortReadyCallback[6];
        this.f = new ElapsedTime[6];
        this.g = new ReadWriteRunnableSegment[ModernRoboticsUsbDeviceInterfaceModule.a.length];
        this.h = new ReadWriteRunnableSegment[ModernRoboticsUsbDeviceInterfaceModule.b.length];
        this.i = new ReadWriteRunnableSegment[ModernRoboticsUsbDeviceInterfaceModule.c.length];
        this.j = new ReadWriteRunnableSegment[ModernRoboticsUsbDeviceInterfaceModule.d.length];
        for (int i = 0; i < ModernRoboticsUsbDeviceInterfaceModule.a.length; ++i) {
            this.g[i] = this.readWriteRunnable.createSegment(ModernRoboticsUsbDeviceInterfaceModule.a[i], ModernRoboticsUsbDeviceInterfaceModule.ADDRESS_VOLTAGE_OUTPUT_PORT_MAP[i], 5);
        }
        int n = 0;
        int j;
        while (true) {
            final int length = ModernRoboticsUsbDeviceInterfaceModule.b.length;
            j = 0;
            if (n >= length) {
                break;
            }
            this.h[n] = this.readWriteRunnable.createSegment(ModernRoboticsUsbDeviceInterfaceModule.b[n], ModernRoboticsUsbDeviceInterfaceModule.ADDRESS_PULSE_OUTPUT_PORT_MAP[n], 4);
            ++n;
        }
        while (j < ModernRoboticsUsbDeviceInterfaceModule.c.length) {
            this.i[j] = this.readWriteRunnable.createSegment(ModernRoboticsUsbDeviceInterfaceModule.c[j], ModernRoboticsUsbDeviceInterfaceModule.ADDRESS_I2C_PORT_MAP[j], 32);
            this.j[j] = this.readWriteRunnable.createSegment(ModernRoboticsUsbDeviceInterfaceModule.d[j], 31 + ModernRoboticsUsbDeviceInterfaceModule.ADDRESS_I2C_PORT_MAP[j], 1);
            ++j;
        }
    }
    
    private int a(final int n, final Mode mode) {
        if (mode == Mode.OUTPUT) {
            return ModernRoboticsUsbDeviceInterfaceModule.ADDRESS_DIGITAL_BIT_MASK[n];
        }
        return -1 ^ ModernRoboticsUsbDeviceInterfaceModule.ADDRESS_DIGITAL_BIT_MASK[n];
    }
    
    private Mode a(final int n, final int n2) {
        if ((n2 & ModernRoboticsUsbDeviceInterfaceModule.ADDRESS_DIGITAL_BIT_MASK[n]) > 0) {
            return Mode.OUTPUT;
        }
        return Mode.INPUT;
    }
    
    private void a(final int n) {
        if (n != 0 && n != 1) {
            throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are 0 and 1.", n));
        }
    }
    
    private boolean a(final int n, final byte b) {
        return (b & ModernRoboticsUsbDeviceInterfaceModule.BUFFER_FLAG_MAP[n]) == 0x0;
    }
    
    private void b(final int n) {
        if (n != 0 && n != 1) {
            throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are 0 and 1.", n));
        }
    }
    
    private void c(final int n) {
        if (n != 0 && n != 1) {
            throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are 0 and 1.", n));
        }
    }
    
    private void d(final int n) {
        if (n < 0 || n > 7) {
            throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are %d..%d", n, 0, 7));
        }
    }
    
    private void e(final int n) {
        if (n < 0 || n > 5) {
            throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are %d..%d", n, 0, 5));
        }
    }
    
    private void f(final int n) {
        if (n > 27) {
            throw new IllegalArgumentException(String.format("buffer is too large (%d byte), max size is %d bytes", n, 27));
        }
    }
    
    @Override
    public void copyBufferIntoWriteBuffer(final int n, final byte[] array) {
        this.e(n);
        this.f(array.length);
        try {
            this.i[n].getWriteLock().lock();
            System.arraycopy(array, 0, this.i[n].getWriteBuffer(), 4, array.length);
        }
        finally {
            this.i[n].getWriteLock().unlock();
        }
    }
    
    @Override
    public void deregisterForPortReadyCallback(final int n) {
        this.e[n] = null;
    }
    
    @Override
    public void enableI2cReadMode(final int n, final int n2, final int n3, final int n4) {
        this.e(n);
        this.f(n4);
        try {
            this.i[n].getWriteLock().lock();
            final byte[] writeBuffer = this.i[n].getWriteBuffer();
            writeBuffer[0] = -128;
            writeBuffer[1] = (byte)n2;
            writeBuffer[2] = (byte)n3;
            writeBuffer[3] = (byte)n4;
        }
        finally {
            this.i[n].getWriteLock().unlock();
        }
    }
    
    @Override
    public void enableI2cWriteMode(final int n, final int n2, final int n3, final int n4) {
        this.e(n);
        this.f(n4);
        try {
            this.i[n].getWriteLock().lock();
            final byte[] writeBuffer = this.i[n].getWriteBuffer();
            writeBuffer[0] = 0;
            writeBuffer[1] = (byte)n2;
            writeBuffer[2] = (byte)n3;
            writeBuffer[3] = (byte)n4;
        }
        finally {
            this.i[n].getWriteLock().unlock();
        }
    }
    
    @Override
    public int getAnalogInputValue(final int n) {
        this.d(n);
        return TypeConversion.byteArrayToShort(this.read(ModernRoboticsUsbDeviceInterfaceModule.ADDRESS_ANALOG_PORT_MAP[n], 2), ByteOrder.LITTLE_ENDIAN);
    }
    
    @Override
    public String getConnectionInfo() {
        return "USB " + this.getSerialNumber();
    }
    
    @Override
    public byte[] getCopyOfReadBuffer(final int n) {
        this.e(n);
        try {
            this.i[n].getReadLock().lock();
            final byte[] readBuffer = this.i[n].getReadBuffer();
            final byte[] array = new byte[readBuffer[3]];
            System.arraycopy(readBuffer, 4, array, 0, array.length);
            return array;
        }
        finally {
            this.i[n].getReadLock().unlock();
        }
    }
    
    @Override
    public byte[] getCopyOfWriteBuffer(final int n) {
        this.e(n);
        try {
            this.i[n].getWriteLock().lock();
            final byte[] writeBuffer = this.i[n].getWriteBuffer();
            final byte[] array = new byte[writeBuffer[3]];
            System.arraycopy(writeBuffer, 4, array, 0, array.length);
            return array;
        }
        finally {
            this.i[n].getWriteLock().unlock();
        }
    }
    
    @Override
    public String getDeviceName() {
        return "Modern Robotics USB Device Interface Module";
    }
    
    @Override
    public Mode getDigitalChannelMode(final int n) {
        return this.a(n, (int)this.getDigitalIOControlByte());
    }
    
    @Override
    public boolean getDigitalChannelState(final int n) {
        int n2;
        if (Mode.OUTPUT == this.getDigitalChannelMode(n)) {
            n2 = this.getDigitalOutputStateByte();
        }
        else {
            n2 = this.getDigitalInputStateByte();
        }
        return (n2 & ModernRoboticsUsbDeviceInterfaceModule.ADDRESS_DIGITAL_BIT_MASK[n]) > 0;
    }
    
    @Override
    public byte getDigitalIOControlByte() {
        return this.read(21);
    }
    
    @Override
    public int getDigitalInputStateByte() {
        return TypeConversion.unsignedByteToInt(this.read(20));
    }
    
    @Override
    public byte getDigitalOutputStateByte() {
        return this.read(22);
    }
    
    @Override
    public byte[] getI2cReadCache(final int n) {
        return this.i[n].getReadBuffer();
    }
    
    @Override
    public Lock getI2cReadCacheLock(final int n) {
        return this.i[n].getReadLock();
    }
    
    @Override
    public byte[] getI2cWriteCache(final int n) {
        return this.i[n].getWriteBuffer();
    }
    
    @Override
    public Lock getI2cWriteCacheLock(final int n) {
        return this.i[n].getWriteLock();
    }
    
    @Override
    public boolean getLEDState(final int n) {
        this.a(n);
        return (this.read(23) & ModernRoboticsUsbDeviceInterfaceModule.LED_BIT_MASK_MAP[n]) > 0;
    }
    
    @Override
    public int getPulseWidthOutputTime(final int n) {
        throw new UnsupportedOperationException("getPulseWidthOutputTime is not implemented.");
    }
    
    @Override
    public int getPulseWidthPeriod(final int n) {
        throw new UnsupportedOperationException("getPulseWidthOutputTime is not implemented.");
    }
    
    @Override
    public boolean isI2cPortActionFlagSet(final int n) {
        this.e(n);
        try {
            this.i[n].getReadLock().lock();
            return this.i[n].getReadBuffer()[31] == -1;
        }
        finally {
            this.i[n].getReadLock().unlock();
        }
    }
    
    @Override
    public boolean isI2cPortInReadMode(final int n) {
        this.e(n);
        try {
            this.i[n].getReadLock().lock();
            final byte b = this.i[n].getReadBuffer()[0];
            boolean b2 = false;
            if (b == -128) {
                b2 = true;
            }
            return b2;
        }
        finally {
            this.i[n].getReadLock().unlock();
        }
    }
    
    @Override
    public boolean isI2cPortInWriteMode(final int n) {
        this.e(n);
        try {
            this.i[n].getReadLock().lock();
            final byte b = this.i[n].getReadBuffer()[0];
            boolean b2 = false;
            if (b == 0) {
                b2 = true;
            }
            return b2;
        }
        finally {
            this.i[n].getReadLock().unlock();
        }
    }
    
    @Override
    public boolean isI2cPortReady(final int n) {
        return this.a(n, this.read(3));
    }
    
    @Override
    public void readComplete() throws InterruptedException {
        if (this.e != null) {
            final byte read = this.read(3);
            for (int i = 0; i < 6; ++i) {
                if (this.e[i] != null && this.a(i, read)) {
                    this.e[i].portIsReady(i);
                }
            }
        }
    }
    
    @Override
    public void readI2cCacheFromController(final int n) {
        this.e(n);
        this.readWriteRunnable.queueSegmentRead(ModernRoboticsUsbDeviceInterfaceModule.c[n]);
    }
    
    @Deprecated
    @Override
    public void readI2cCacheFromModule(final int n) {
        this.readI2cCacheFromController(n);
    }
    
    @Override
    public void registerForI2cPortReadyCallback(final I2cPortReadyCallback i2cPortReadyCallback, final int n) {
        this.e[n] = i2cPortReadyCallback;
    }
    
    @Override
    public void setAnalogOutputFrequency(final int n, final int n2) {
        this.b(n);
        final Lock writeLock = this.g[n].getWriteLock();
        final byte[] writeBuffer = this.g[n].getWriteBuffer();
        final byte[] shortToByteArray = TypeConversion.shortToByteArray((short)n2, ByteOrder.LITTLE_ENDIAN);
        try {
            writeLock.lock();
            System.arraycopy(shortToByteArray, 0, writeBuffer, 2, shortToByteArray.length);
            writeLock.unlock();
            this.readWriteRunnable.queueSegmentWrite(ModernRoboticsUsbDeviceInterfaceModule.a[n]);
        }
        finally {
            writeLock.unlock();
        }
    }
    
    @Override
    public void setAnalogOutputMode(final int n, final byte b) {
        this.b(n);
        final Lock writeLock = this.g[n].getWriteLock();
        final byte[] writeBuffer = this.g[n].getWriteBuffer();
        try {
            writeLock.lock();
            writeBuffer[4] = b;
            writeLock.unlock();
            this.readWriteRunnable.queueSegmentWrite(ModernRoboticsUsbDeviceInterfaceModule.a[n]);
        }
        finally {
            writeLock.unlock();
        }
    }
    
    @Override
    public void setAnalogOutputVoltage(final int n, final int n2) {
        this.b(n);
        final Lock writeLock = this.g[n].getWriteLock();
        final byte[] writeBuffer = this.g[n].getWriteBuffer();
        final byte[] shortToByteArray = TypeConversion.shortToByteArray((short)n2, ByteOrder.LITTLE_ENDIAN);
        try {
            writeLock.lock();
            System.arraycopy(shortToByteArray, 0, writeBuffer, 0, shortToByteArray.length);
            writeLock.unlock();
            this.readWriteRunnable.queueSegmentWrite(ModernRoboticsUsbDeviceInterfaceModule.a[n]);
        }
        finally {
            writeLock.unlock();
        }
    }
    
    @Override
    public void setDigitalChannelMode(final int n, final Mode mode) {
        final int a = this.a(n, mode);
        final byte fromWriteCache = this.readFromWriteCache(21);
        byte b;
        if (mode == Mode.OUTPUT) {
            b = (byte)(a | fromWriteCache);
        }
        else {
            b = (byte)(a & fromWriteCache);
        }
        this.write(21, (int)b);
    }
    
    @Override
    public void setDigitalChannelState(final int n, final boolean b) {
        if (Mode.OUTPUT == this.getDigitalChannelMode(n)) {
            final byte fromWriteCache = this.readFromWriteCache(22);
            int n2;
            if (b) {
                n2 = (fromWriteCache | ModernRoboticsUsbDeviceInterfaceModule.ADDRESS_DIGITAL_BIT_MASK[n]);
            }
            else {
                n2 = (fromWriteCache & (-1 ^ ModernRoboticsUsbDeviceInterfaceModule.ADDRESS_DIGITAL_BIT_MASK[n]));
            }
            this.setDigitalOutputByte((byte)n2);
        }
    }
    
    @Override
    public void setDigitalIOControlByte(final byte b) {
        this.write(21, b);
    }
    
    @Override
    public void setDigitalOutputByte(final byte b) {
        this.write(22, b);
    }
    
    @Override
    public void setI2cPortActionFlag(final int n) {
        this.e(n);
        try {
            this.i[n].getWriteLock().lock();
            this.i[n].getWriteBuffer()[31] = -1;
        }
        finally {
            this.i[n].getWriteLock().unlock();
        }
    }
    
    @Override
    public void setLED(final int n, final boolean b) {
        this.a(n);
        final byte fromWriteCache = this.readFromWriteCache(23);
        int n2;
        if (b) {
            n2 = (fromWriteCache | ModernRoboticsUsbDeviceInterfaceModule.LED_BIT_MASK_MAP[n]);
        }
        else {
            n2 = (fromWriteCache & (-1 ^ ModernRoboticsUsbDeviceInterfaceModule.LED_BIT_MASK_MAP[n]));
        }
        this.write(23, n2);
    }
    
    @Override
    public void setPulseWidthOutputTime(final int n, final int n2) {
        this.c(n);
        final Lock writeLock = this.h[n].getWriteLock();
        final byte[] writeBuffer = this.h[n].getWriteBuffer();
        final byte[] shortToByteArray = TypeConversion.shortToByteArray((short)n2, ByteOrder.LITTLE_ENDIAN);
        try {
            writeLock.lock();
            System.arraycopy(shortToByteArray, 0, writeBuffer, 0, shortToByteArray.length);
            writeLock.unlock();
            this.readWriteRunnable.queueSegmentWrite(ModernRoboticsUsbDeviceInterfaceModule.b[n]);
        }
        finally {
            writeLock.unlock();
        }
    }
    
    @Override
    public void setPulseWidthPeriod(final int n, final int n2) {
        this.e(n);
        final Lock writeLock = this.h[n].getWriteLock();
        final byte[] writeBuffer = this.h[n].getWriteBuffer();
        final byte[] shortToByteArray = TypeConversion.shortToByteArray((short)n2, ByteOrder.LITTLE_ENDIAN);
        try {
            writeLock.lock();
            System.arraycopy(shortToByteArray, 0, writeBuffer, 2, shortToByteArray.length);
            writeLock.unlock();
            this.readWriteRunnable.queueSegmentWrite(ModernRoboticsUsbDeviceInterfaceModule.b[n]);
        }
        finally {
            writeLock.unlock();
        }
    }
    
    @Override
    public void writeI2cCacheToController(final int n) {
        this.e(n);
        this.readWriteRunnable.queueSegmentWrite(ModernRoboticsUsbDeviceInterfaceModule.c[n]);
    }
    
    @Deprecated
    @Override
    public void writeI2cCacheToModule(final int n) {
        this.writeI2cCacheToController(n);
    }
    
    @Override
    public void writeI2cPortFlagOnlyToController(final int n) {
        this.e(n);
        final ReadWriteRunnableSegment readWriteRunnableSegment = this.i[n];
        final ReadWriteRunnableSegment readWriteRunnableSegment2 = this.j[n];
        try {
            readWriteRunnableSegment.getWriteLock().lock();
            readWriteRunnableSegment2.getWriteLock().lock();
            readWriteRunnableSegment2.getWriteBuffer()[0] = readWriteRunnableSegment.getWriteBuffer()[31];
            readWriteRunnableSegment.getWriteLock().unlock();
            readWriteRunnableSegment2.getWriteLock().unlock();
            this.readWriteRunnable.queueSegmentWrite(ModernRoboticsUsbDeviceInterfaceModule.d[n]);
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
