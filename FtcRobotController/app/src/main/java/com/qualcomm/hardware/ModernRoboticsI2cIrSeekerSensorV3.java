//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.hardware;

import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.I2cController.I2cPortReadyCallback;
import com.qualcomm.robotcore.hardware.IrSeekerSensor.IrSeekerIndividualSensor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor.Mode;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import java.util.concurrent.locks.Lock;

public class ModernRoboticsI2cIrSeekerSensorV3 extends IrSeekerSensor implements I2cPortReadyCallback {
    public volatile int I2C_ADDRESS = 56;
    public static final int ADDRESS_MEM_START = 4;
    public static final int MEM_LENGTH = 12;
    public static final int OFFSET_1200HZ_HEADING_DATA = 4;
    public static final int OFFSET_1200HZ_SIGNAL_STRENGTH = 5;
    public static final int OFFSET_600HZ_HEADING_DATA = 6;
    public static final int OFFSET_600HZ_SIGNAL_STRENGTH = 7;
    public static final int OFFSET_1200HZ_LEFT_SIDE_RAW_DATA = 8;
    public static final int OFFSET_1200HZ_RIGHT_SIDE_RAW_DATA = 10;
    public static final int OFFSET_600HZ_LEFT_SIDE_RAW_DATA = 12;
    public static final int OFFSET_600HZ_RIGHT_SIDE_RAW_DATA = 14;
    public static final byte SENSOR_COUNT = 2;
    public static final double MAX_SENSOR_STRENGTH = 256.0D;
    public static final byte INVALID_ANGLE = 0;
    public static final double DEFAULT_SIGNAL_DETECTED_THRESHOLD = 0.00390625D;
    private final DeviceInterfaceModule a;
    private final int b;
    private Mode c;
    private final byte[] d;
    private final Lock e;
    private double f = 0.00390625D;

    public ModernRoboticsI2cIrSeekerSensorV3(DeviceInterfaceModule module, int physicalPort) {
        this.a = module;
        this.b = physicalPort;
        this.c = Mode.MODE_1200HZ;
        this.d = this.a.getI2cReadCache(physicalPort);
        this.e = this.a.getI2cReadCacheLock(physicalPort);
        this.a.enableI2cReadMode(physicalPort, this.I2C_ADDRESS, 4, 12);
        this.a.setI2cPortActionFlag(physicalPort);
        this.a.writeI2cCacheToController(physicalPort);
        this.a.registerForI2cPortReadyCallback(this, physicalPort);
    }

    public void setSignalDetectedThreshold(double threshold) {
        this.f = threshold;
    }

    public double getSignalDetectedThreshold() {
        return this.f;
    }

    public void setMode(Mode mode) {
        this.c = mode;
    }

    public Mode getMode() {
        return this.c;
    }

    public boolean signalDetected() {
        return this.getStrength() > this.f;
    }

    public double getAngle() {
        double var1 = 0.0D;
        int var3 = this.c == Mode.MODE_1200HZ?4:6;

        try {
            this.e.lock();
            var1 = (double)this.d[var3];
        } finally {
            this.e.unlock();
        }

        return var1;
    }

    public double getStrength() {
        double var1 = 0.0D;
        int var3 = this.c == Mode.MODE_1200HZ?5:7;

        try {
            this.e.lock();
            var1 = TypeConversion.unsignedByteToDouble(this.d[var3]) / 256.0D;
        } finally {
            this.e.unlock();
        }

        return var1;
    }

    public IrSeekerIndividualSensor[] getIndividualSensors() {
        IrSeekerIndividualSensor[] var1 = new IrSeekerIndividualSensor[2];

        try {
            this.e.lock();
            int var2 = this.c == Mode.MODE_1200HZ?8:12;
            byte[] var3 = new byte[2];
            System.arraycopy(this.d, var2, var3, 0, var3.length);
            double var4 = (double)TypeConversion.byteArrayToShort(var3, ByteOrder.LITTLE_ENDIAN) / 256.0D;
            var1[0] = new IrSeekerIndividualSensor(-1.0D, var4);
            int var6 = this.c == Mode.MODE_1200HZ?10:14;
            byte[] var7 = new byte[2];
            System.arraycopy(this.d, var6, var7, 0, var7.length);
            double var8 = (double)TypeConversion.byteArrayToShort(var7, ByteOrder.LITTLE_ENDIAN) / 256.0D;
            var1[1] = new IrSeekerIndividualSensor(1.0D, var8);
        } finally {
            this.e.unlock();
        }

        return var1;
    }

    public void portIsReady(int port) {
        this.a.setI2cPortActionFlag(port);
        this.a.readI2cCacheFromController(port);
        this.a.writeI2cPortFlagOnlyToController(port);
    }

    public String getDeviceName() {
        return "Modern Robotics I2C IR Seeker Sensor";
    }

    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; I2C port " + this.b;
    }

    public int getVersion() {
        return 3;
    }

    public void close() {
    }

    public void setI2cAddress(int newAddress) {
        IrSeekerSensor.throwIfModernRoboticsI2cAddressIsInvalid(newAddress);
        RobotLog.i(this.getDeviceName() + ", just changed the I2C address. Original address: " + this.I2C_ADDRESS + ", new address: " + newAddress);
        this.I2C_ADDRESS = newAddress;
        this.a.enableI2cReadMode(this.b, this.I2C_ADDRESS, 4, 12);
        this.a.setI2cPortActionFlag(this.b);
        this.a.writeI2cCacheToController(this.b);
        this.a.registerForI2cPortReadyCallback(this, this.b);
    }

    public int getI2cAddress() {
        return this.I2C_ADDRESS;
    }
}
