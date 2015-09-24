package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.TypeConversion;
import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;

public class ModernRoboticsNxtIrSeekerSensor extends IrSeekerSensor implements I2cPortReadyCallback
{
    public static final double DEFAULT_SIGNAL_DETECTED_THRESHOLD = 0.00390625;
    public static final byte DIRECTION = 4;
    public static final double[] DIRECTION_TO_ANGLE;
    public static final int I2C_ADDRESS = 16;
    public static final byte INVALID_ANGLE = 0;
    public static final byte MAX_ANGLE = 9;
    public static final double MAX_SENSOR_STRENGTH = 256.0;
    public static final int MEM_AC_START_ADDRESS = 73;
    public static final int MEM_DC_START_ADDRESS = 66;
    public static final int MEM_MODE_ADDRESS = 65;
    public static final int MEM_READ_LENGTH = 6;
    public static final byte MIN_ANGLE = 1;
    public static final byte MODE_AC = 0;
    public static final byte MODE_DC = 2;
    public static final byte SENSOR_COUNT = 9;
    public static final byte SENSOR_FIRST = 5;
    private final ModernRoboticsUsbLegacyModule a;
    private final byte[] b;
    private final Lock c;
    private final byte[] d;
    private final Lock e;
    private final int f;
    private Mode g;
    private double h;
    private volatile boolean i;
    
    static {
        DIRECTION_TO_ANGLE = new double[] { 0.0, -120.0, -90.0, -60.0, -30.0, 0.0, 30.0, 60.0, 90.0, 120.0 };
    }
    
    public ModernRoboticsNxtIrSeekerSensor(final ModernRoboticsUsbLegacyModule a, final int f) {
        this.h = 0.00390625;
        this.a = a;
        this.b = a.getI2cReadCache(f);
        this.c = a.getI2cReadCacheLock(f);
        this.d = a.getI2cWriteCache(f);
        this.e = a.getI2cWriteCacheLock(f);
        this.f = f;
        this.g = Mode.MODE_1200HZ;
        a.registerForI2cPortReadyCallback(this, f);
        this.i = true;
    }
    
    private double a(final byte[] array, final int n) {
        return TypeConversion.unsignedByteToDouble(array[n + 5]) / 256.0;
    }
    
    private void a() {
        this.i = true;
        Label_0059: {
            if (this.g != Mode.MODE_600HZ) {
                break Label_0059;
            }
            byte b = 2;
            while (true) {
                this.a.enableI2cWriteMode(this.f, 16, 65, 1);
                try {
                    this.e.lock();
                    this.d[4] = b;
                    return;
                    b = 0;
                }
                finally {
                    this.e.unlock();
                }
            }
        }
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public double getAngle() {
        double n = 0.0;
        if (this.i) {
            return n;
        }
        try {
            this.c.lock();
            if (this.b[4] >= 1 && this.b[4] <= 9) {
                n = ModernRoboticsNxtIrSeekerSensor.DIRECTION_TO_ANGLE[this.b[4]];
            }
            return n;
        }
        finally {
            this.c.unlock();
        }
    }
    
    @Override
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; port " + this.f;
    }
    
    @Override
    public String getDeviceName() {
        return "NXT IR Seeker Sensor";
    }
    
    @Override
    public int getI2cAddress() {
        return 16;
    }
    
    @Override
    public IrSeekerIndividualSensor[] getIndividualSensors() {
        final IrSeekerIndividualSensor[] array = new IrSeekerIndividualSensor[9];
        if (this.i) {
            return array;
        }
        try {
            this.c.lock();
            for (int i = 0; i < 9; ++i) {
                array[i] = new IrSeekerIndividualSensor(ModernRoboticsNxtIrSeekerSensor.DIRECTION_TO_ANGLE[1 + i * 2], this.a(this.b, i));
            }
            return array;
        }
        finally {
            this.c.unlock();
        }
    }
    
    @Override
    public Mode getMode() {
        return this.g;
    }
    
    @Override
    public double getSignalDetectedThreshold() {
        return this.h;
    }
    
    @Override
    public double getStrength() {
        double n = 0.0;
        if (this.i) {
            return n;
        }
        try {
            this.c.lock();
            double max;
            for (int i = 0; i < 9; ++i, n = max) {
                max = Math.max(n, this.a(this.b, i));
            }
            return n;
        }
        finally {
            this.c.unlock();
        }
    }
    
    @Override
    public int getVersion() {
        return 2;
    }
    
    @Override
    public void portIsReady(final int n) {
        this.a.setI2cPortActionFlag(this.f);
        this.a.readI2cCacheFromController(this.f);
        if (this.i) {
            if (this.g == Mode.MODE_600HZ) {
                this.a.enableI2cReadMode(this.f, 16, 66, 6);
            }
            else {
                this.a.enableI2cReadMode(this.f, 16, 73, 6);
            }
            this.a.writeI2cCacheToController(this.f);
            this.i = false;
            return;
        }
        this.a.writeI2cPortFlagOnlyToController(this.f);
    }
    
    @Override
    public void setI2cAddress(final int n) {
        throw new UnsupportedOperationException("This method is not supported.");
    }
    
    @Override
    public void setMode(final Mode g) {
        if (this.g == g) {
            return;
        }
        this.g = g;
        this.a();
    }
    
    @Override
    public void setSignalDetectedThreshold(final double h) {
        this.h = h;
    }
    
    @Override
    public boolean signalDetected() {
        boolean b = true;
        if (this.i) {
            return false;
        }
        while (true) {
            try {
                this.c.lock();
                final boolean b2 = this.b[4] != 0 && b;
                this.c.unlock();
                if (b2 && this.getStrength() > this.h) {
                    return b;
                }
            }
            finally {
                this.c.unlock();
            }
            b = false;
            return b;
        }
    }
}
