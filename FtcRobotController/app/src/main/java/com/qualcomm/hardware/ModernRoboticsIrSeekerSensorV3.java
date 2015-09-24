package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;

public class ModernRoboticsIrSeekerSensorV3 extends IrSeekerSensor implements I2cPortReadyCallback
{
    public static final int ADDRESS_MEM_START = 4;
    public static final double DEFAULT_SIGNAL_DETECTED_THRESHOLD = 0.00390625;
    public static final byte INVALID_ANGLE = 0;
    public static final double MAX_SENSOR_STRENGTH = 256.0;
    public static final int MEM_LENGTH = 12;
    public static final int OFFSET_1200HZ_HEADING_DATA = 4;
    public static final int OFFSET_1200HZ_LEFT_SIDE_RAW_DATA = 8;
    public static final int OFFSET_1200HZ_RIGHT_SIDE_RAW_DATA = 10;
    public static final int OFFSET_1200HZ_SIGNAL_STRENGTH = 5;
    public static final int OFFSET_600HZ_HEADING_DATA = 6;
    public static final int OFFSET_600HZ_LEFT_SIDE_RAW_DATA = 12;
    public static final int OFFSET_600HZ_RIGHT_SIDE_RAW_DATA = 14;
    public static final int OFFSET_600HZ_SIGNAL_STRENGTH = 7;
    public static final byte SENSOR_COUNT = 2;
    public volatile int I2C_ADDRESS;
    private final DeviceInterfaceModule a;
    private final int b;
    private Mode c;
    private final byte[] d;
    private final Lock e;
    private double f;
    
    public ModernRoboticsIrSeekerSensorV3(final DeviceInterfaceModule a, final int n) {
        this.I2C_ADDRESS = 56;
        this.f = 0.00390625;
        this.a = a;
        this.b = n;
        this.c = Mode.MODE_1200HZ;
        this.d = this.a.getI2cReadCache(n);
        this.e = this.a.getI2cReadCacheLock(n);
        this.a.enableI2cReadMode(n, this.I2C_ADDRESS, 4, 12);
        this.a.setI2cPortActionFlag(n);
        this.a.writeI2cCacheToController(n);
        this.a.registerForI2cPortReadyCallback((I2cController.I2cPortReadyCallback)this, n);
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public double getAngle() {
        Label_0044: {
            if (this.c != Mode.MODE_1200HZ) {
                break Label_0044;
            }
            int n = 4;
            try {
                while (true) {
                    this.e.lock();
                    return this.d[n];
                    n = 6;
                    continue;
                }
            }
            finally {
                this.e.unlock();
            }
        }
    }
    
    @Override
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; I2C port " + this.b;
    }
    
    @Override
    public String getDeviceName() {
        return "Modern Robotics IR Seeker Sensor";
    }
    
    @Override
    public int getI2cAddress() {
        return this.I2C_ADDRESS;
    }
    
    @Override
    public IrSeekerIndividualSensor[] getIndividualSensors() {
        final IrSeekerIndividualSensor[] array = new IrSeekerIndividualSensor[2];
        try {
            this.e.lock();
            int n;
            if (this.c == Mode.MODE_1200HZ) {
                n = 8;
            }
            else {
                n = 12;
            }
            final byte[] array2 = new byte[2];
            System.arraycopy(this.d, n, array2, 0, array2.length);
            array[0] = new IrSeekerIndividualSensor(-1.0, TypeConversion.byteArrayToShort(array2, ByteOrder.LITTLE_ENDIAN) / 256.0);
            int n2;
            if (this.c == Mode.MODE_1200HZ) {
                n2 = 10;
            }
            else {
                n2 = 14;
            }
            final byte[] array3 = new byte[2];
            System.arraycopy(this.d, n2, array3, 0, array3.length);
            array[1] = new IrSeekerIndividualSensor(1.0, TypeConversion.byteArrayToShort(array3, ByteOrder.LITTLE_ENDIAN) / 256.0);
            return array;
        }
        finally {
            this.e.unlock();
        }
    }
    
    @Override
    public Mode getMode() {
        return this.c;
    }
    
    @Override
    public double getSignalDetectedThreshold() {
        return this.f;
    }
    
    @Override
    public double getStrength() {
        Label_0050: {
            if (this.c != Mode.MODE_1200HZ) {
                break Label_0050;
            }
            int n = 5;
            try {
                while (true) {
                    this.e.lock();
                    return TypeConversion.unsignedByteToDouble(this.d[n]) / 256.0;
                    n = 7;
                    continue;
                }
            }
            finally {
                this.e.unlock();
            }
        }
    }
    
    @Override
    public int getVersion() {
        return 3;
    }
    
    @Override
    public void portIsReady(final int i2cPortActionFlag) {
        this.a.setI2cPortActionFlag(i2cPortActionFlag);
        this.a.readI2cCacheFromController(i2cPortActionFlag);
        this.a.writeI2cPortFlagOnlyToController(i2cPortActionFlag);
    }
    
    @Override
    public void setI2cAddress(final int i2C_ADDRESS) {
        IrSeekerSensor.throwIfModernRoboticsI2cAddressIsInvalid(i2C_ADDRESS);
        this.I2C_ADDRESS = i2C_ADDRESS;
    }
    
    @Override
    public void setMode(final Mode c) {
        this.c = c;
    }
    
    @Override
    public void setSignalDetectedThreshold(final double f) {
        this.f = f;
    }
    
    @Override
    public boolean signalDetected() {
        return this.getStrength() > this.f;
    }
}
