package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.CompassSensor;

public class ModernRoboticsNxtCompassSensor extends CompassSensor implements I2cPortReadyCallback
{
    public static final byte CALIBRATION = 67;
    public static final byte CALIBRATION_FAILURE = 70;
    public static final int COMPASS_BUFFER = 65;
    public static final int COMPASS_BUFFER_SIZE = 5;
    public static final byte DIRECTION_END = 9;
    public static final byte DIRECTION_START = 7;
    public static final byte HEADING_IN_TWO_DEGREE_INCREMENTS = 66;
    public static final int HEADING_WORD_LENGTH = 2;
    public static final byte I2C_ADDRESS = 2;
    public static final double INVALID_DIRECTION = -1.0;
    public static final byte MEASUREMENT = 0;
    public static final byte MODE_CONTROL_ADDRESS = 65;
    public static final byte ONE_DEGREE_HEADING_ADDER = 67;
    private final ModernRoboticsUsbLegacyModule a;
    private final byte[] b;
    private final Lock c;
    private final byte[] d;
    private final Lock e;
    private final int f;
    private CompassMode g;
    private boolean h;
    private boolean i;
    
    public ModernRoboticsNxtCompassSensor(final ModernRoboticsUsbLegacyModule a, final int f) {
        this.g = CompassMode.MEASUREMENT_MODE;
        this.h = false;
        this.i = false;
        a.enableI2cReadMode(f, 2, 65, 5);
        this.a = a;
        this.b = a.getI2cReadCache(f);
        this.c = a.getI2cReadCacheLock(f);
        this.d = a.getI2cWriteCache(f);
        this.e = a.getI2cWriteCacheLock(f);
        a.registerForI2cPortReadyCallback(this, this.f = f);
    }
    
    private void a() {
        this.h = true;
        Label_0059: {
            if (this.g != CompassMode.CALIBRATION_MODE) {
                break Label_0059;
            }
            byte b = 67;
            while (true) {
                this.a.enableI2cWriteMode(this.f, 2, 65, 1);
                try {
                    this.e.lock();
                    this.d[3] = b;
                    return;
                    b = 0;
                }
                finally {
                    this.e.unlock();
                }
            }
        }
    }
    
    private void b() {
        if (this.g == CompassMode.MEASUREMENT_MODE) {
            this.a.enableI2cReadMode(this.f, 2, 65, 5);
        }
        this.h = false;
    }
    
    @Override
    public boolean calibrationFailed() {
        if (this.g == CompassMode.CALIBRATION_MODE || this.h) {
            return false;
        }
        try {
            this.c.lock();
            final byte b = this.b[3];
            boolean b2 = false;
            if (b == 70) {
                b2 = true;
            }
            return b2;
        }
        finally {
            this.c.unlock();
        }
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; port " + this.f;
    }
    
    @Override
    public String getDeviceName() {
        return "NXT Compass Sensor";
    }
    
    @Override
    public double getDirection() {
        if (this.h || this.g == CompassMode.CALIBRATION_MODE) {
            return -1.0;
        }
        try {
            this.c.lock();
            final byte[] copyOfRange = Arrays.copyOfRange(this.b, 7, 9);
            this.c.unlock();
            return TypeConversion.byteArrayToShort(copyOfRange, ByteOrder.LITTLE_ENDIAN);
        }
        finally {
            this.c.unlock();
        }
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    @Override
    public void portIsReady(final int n) {
        this.a.setI2cPortActionFlag(this.f);
        this.a.readI2cCacheFromController(this.f);
        if (this.h) {
            this.b();
            this.a.writeI2cCacheToController(this.f);
            return;
        }
        this.a.writeI2cPortFlagOnlyToController(this.f);
    }
    
    @Override
    public void setMode(final CompassMode g) {
        if (this.g == g) {
            return;
        }
        this.g = g;
        this.a();
    }
    
    @Override
    public String status() {
        return String.format("NXT Compass Sensor, connected via device %s, port %d", this.a.getSerialNumber().toString(), this.f);
    }
}
