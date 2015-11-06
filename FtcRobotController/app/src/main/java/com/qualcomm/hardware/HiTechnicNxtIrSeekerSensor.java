//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.hardware;

import com.qualcomm.hardware.ModernRoboticsUsbLegacyModule;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.I2cController.I2cPortReadyCallback;
import com.qualcomm.robotcore.hardware.IrSeekerSensor.IrSeekerIndividualSensor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor.Mode;
import com.qualcomm.robotcore.util.TypeConversion;
import java.util.concurrent.locks.Lock;

public class HiTechnicNxtIrSeekerSensor extends IrSeekerSensor implements I2cPortReadyCallback {
    public static final int I2C_ADDRESS = 16;
    public static final int MEM_MODE_ADDRESS = 65;
    public static final int MEM_DC_START_ADDRESS = 66;
    public static final int MEM_AC_START_ADDRESS = 73;
    public static final int MEM_READ_LENGTH = 6;
    public static final byte MODE_AC = 0;
    public static final byte MODE_DC = 2;
    public static final byte DIRECTION = 4;
    public static final byte SENSOR_FIRST = 5;
    public static final byte SENSOR_COUNT = 9;
    public static final double MAX_SENSOR_STRENGTH = 256.0D;
    public static final byte INVALID_ANGLE = 0;
    public static final byte MIN_ANGLE = 1;
    public static final byte MAX_ANGLE = 9;
    public static final double[] DIRECTION_TO_ANGLE = new double[]{0.0D, -120.0D, -90.0D, -60.0D, -30.0D, 0.0D, 30.0D, 60.0D, 90.0D, 120.0D};
    public static final double DEFAULT_SIGNAL_DETECTED_THRESHOLD = 0.00390625D;
    private final ModernRoboticsUsbLegacyModule a;
    private final byte[] b;
    private final Lock c;
    private final byte[] d;
    private final Lock e;
    private final int f;
    private Mode g;
    private double h = 0.00390625D;
    private volatile boolean i;

    public HiTechnicNxtIrSeekerSensor(ModernRoboticsUsbLegacyModule legacyModule, int physicalPort) {
        this.a = legacyModule;
        this.b = legacyModule.getI2cReadCache(physicalPort);
        this.c = legacyModule.getI2cReadCacheLock(physicalPort);
        this.d = legacyModule.getI2cWriteCache(physicalPort);
        this.e = legacyModule.getI2cWriteCacheLock(physicalPort);
        this.f = physicalPort;
        this.g = Mode.MODE_1200HZ;
        legacyModule.registerForI2cPortReadyCallback(this, physicalPort);
        this.i = true;
    }

    public void setSignalDetectedThreshold(double threshold) {
        this.h = threshold;
    }

    public double getSignalDetectedThreshold() {
        return this.h;
    }

    public void setMode(Mode mode) {
        if(this.g != mode) {
            this.g = mode;
            this.a();
        }
    }

    public Mode getMode() {
        return this.g;
    }

    public boolean signalDetected() {
        if(this.i) {
            return false;
        } else {
            boolean var1 = false;

            try {
                this.c.lock();
                var1 = this.b[4] != 0;
            } finally {
                this.c.unlock();
            }

            var1 = var1 && this.getStrength() > this.h;
            return var1;
        }
    }

    public double getAngle() {
        if(this.i) {
            return 0.0D;
        } else {
            double var1 = 0.0D;

            try {
                this.c.lock();
                if(this.b[4] >= 1 && this.b[4] <= 9) {
                    var1 = DIRECTION_TO_ANGLE[this.b[4]];
                } else {
                    var1 = 0.0D;
                }
            } finally {
                this.c.unlock();
            }

            return var1;
        }
    }

    public double getStrength() {
        if(this.i) {
            return 0.0D;
        } else {
            double var1 = 0.0D;

            try {
                this.c.lock();

                for(int var3 = 0; var3 < 9; ++var3) {
                    var1 = Math.max(var1, this.a(this.b, var3));
                }
            } finally {
                this.c.unlock();
            }

            return var1;
        }
    }

    public IrSeekerIndividualSensor[] getIndividualSensors() {
        IrSeekerIndividualSensor[] var1 = new IrSeekerIndividualSensor[9];
        if(this.i) {
            return var1;
        } else {
            try {
                this.c.lock();

                for(int var2 = 0; var2 < 9; ++var2) {
                    double var3 = DIRECTION_TO_ANGLE[var2 * 2 + 1];
                    double var5 = this.a(this.b, var2);
                    var1[var2] = new IrSeekerIndividualSensor(var3, var5);
                }
            } finally {
                this.c.unlock();
            }

            return var1;
        }
    }

    public void setI2cAddress(int newAddress) {
        throw new UnsupportedOperationException("This method is not supported.");
    }

    public int getI2cAddress() {
        return 16;
    }

    private void a() {
        this.i = true;
        int var1 = this.g == Mode.MODE_600HZ?2:0;
        this.a.enableI2cWriteMode(this.f, 16, 65, 1);

        try {
            this.e.lock();
            this.d[4] = (byte)var1;
        } finally {
            this.e.unlock();
        }

    }

    private double a(byte[] var1, int var2) {
        return TypeConversion.unsignedByteToDouble(var1[var2 + 5]) / 256.0D;
    }

    public void portIsReady(int port) {
        this.a.setI2cPortActionFlag(this.f);
        this.a.readI2cCacheFromController(this.f);
        if(this.i) {
            if(this.g == Mode.MODE_600HZ) {
                this.a.enableI2cReadMode(this.f, 16, 66, 6);
            } else {
                this.a.enableI2cReadMode(this.f, 16, 73, 6);
            }

            this.a.writeI2cCacheToController(this.f);
            this.i = false;
        } else {
            this.a.writeI2cPortFlagOnlyToController(this.f);
        }

    }

    public String getDeviceName() {
        return "NXT IR Seeker Sensor";
    }

    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; port " + this.f;
    }

    public int getVersion() {
        return 2;
    }

    public void close() {
    }
}
