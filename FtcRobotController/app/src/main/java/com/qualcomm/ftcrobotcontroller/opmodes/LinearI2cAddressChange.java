package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.TypeConversion;
import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class LinearI2cAddressChange extends LinearOpMode
{
    public static final int ADDRESS_MEMORY_START = 0;
    public static final int ADDRESS_SET_NEW_I2C_ADDRESS = 112;
    public static final int BUFFER_CHANGE_ADDRESS_LENGTH = 3;
    public static final byte FIRMWARE_REV = 18;
    public static final byte IR_SEEKER_V3_FIRMWARE_REV = 18;
    public static final byte IR_SEEKER_V3_ORIGINAL_ADDRESS = 56;
    public static final byte IR_SEEKER_V3_SENSOR_ID = 73;
    public static final byte MANUFACTURER_CODE = 77;
    public static final int READ_MODE = 128;
    public static final byte SENSOR_ID = 73;
    public static final int TOTAL_MEMORY_LENGTH = 12;
    public static final byte TRIGGER_BYTE_1 = 85;
    public static final byte TRIGGER_BYTE_2 = -86;
    int currentAddress;
    DeviceInterfaceModule dim;
    int newAddress;
    int port;
    byte[] readCache;
    Lock readLock;
    byte[] writeCache;
    Lock writeLock;
    
    public LinearI2cAddressChange() {
        this.port = 3;
        this.currentAddress = 56;
        this.newAddress = 66;
    }
    
    private boolean foundExpectedBytes(final int[] array, final Lock lock, final byte[] array2) {
        try {
            lock.lock();
            boolean b = true;
            final StringBuilder sb = new StringBuilder(1200);
            String format = "";
            for (int i = 0; i < array.length; ++i) {
                sb.append(String.format("expected: %02x, got: %02x \n", TypeConversion.unsignedByteToInt((byte)array[i]), array2[i]));
                if (TypeConversion.unsignedByteToInt(array2[i]) != TypeConversion.unsignedByteToInt((byte)array[i])) {
                    format = String.format("i: %d, byteArray[i]: %02x, cache[i]: %02x", i, array[i], array2[i]);
                    b = false;
                    break;
                }
            }
            RobotLog.e(sb.toString() + "\n allMatch: " + b + ", mismatch: " + format);
            return b;
        }
        finally {
            lock.unlock();
        }
    }
    
    private void performAction(final String s, final int i2cPortActionFlag, final int n, final int n2, final int n3) {
        if (s.equalsIgnoreCase("read")) {
            this.dim.enableI2cReadMode(i2cPortActionFlag, n, n2, n3);
        }
        if (s.equalsIgnoreCase("write")) {
            this.dim.enableI2cWriteMode(i2cPortActionFlag, n, n2, n3);
        }
        this.dim.setI2cPortActionFlag(i2cPortActionFlag);
        this.dim.writeI2cCacheToController(i2cPortActionFlag);
        this.dim.readI2cCacheFromController(i2cPortActionFlag);
    }
    
    private void writeNewAddress() {
        try {
            this.writeLock.lock();
            this.writeCache[4] = (byte)this.newAddress;
            this.writeCache[5] = 85;
            this.writeCache[6] = -86;
        }
        finally {
            this.writeLock.unlock();
        }
    }
    
    @Override
    public void runOpMode() throws InterruptedException {
        this.dim = this.hardwareMap.deviceInterfaceModule.get("dim");
        this.readCache = this.dim.getI2cReadCache(this.port);
        this.readLock = this.dim.getI2cReadCacheLock(this.port);
        this.writeCache = this.dim.getI2cWriteCache(this.port);
        this.writeLock = this.dim.getI2cWriteCacheLock(this.port);
        IrSeekerSensor.throwIfModernRoboticsI2cAddressIsInvalid(this.newAddress);
        this.waitForStart();
        this.performAction("read", this.port, this.currentAddress, 0, 12);
        while (!this.dim.isI2cPortReady(this.port)) {
            this.telemetry.addData("I2cAddressChange", "waiting for the port to be ready...");
            this.sleep(1000L);
        }
        this.dim.readI2cCacheFromController(this.port);
        int n = 0;
        while (!this.foundExpectedBytes(new int[] { 128, this.currentAddress, 0, 12, 18, 77, 73 }, this.readLock, this.readCache)) {
            this.telemetry.addData("I2cAddressChange", "Confirming that we're reading the correct bytes...");
            this.dim.readI2cCacheFromController(this.port);
            this.sleep(1000L);
            if (++n >= 10) {
                this.telemetry.addData("I2cAddressChange", String.format("Looping too long with no change, probably have the wrong address. Current address: %02x", this.currentAddress));
                this.hardwareMap.irSeekerSensor.get(String.format("Looping too long with no change, probably have the wrong address. Current address: %02x", this.currentAddress));
            }
        }
        this.performAction("write", this.port, this.currentAddress, 112, 3);
        this.waitOneFullHardwareCycle();
        this.writeNewAddress();
        this.dim.setI2cPortActionFlag(this.port);
        this.dim.writeI2cCacheToController(this.port);
        this.telemetry.addData("I2cAddressChange", "Giving the hardware some time to make the change...");
        for (int i = 0; i < 5000; ++i) {
            this.waitOneFullHardwareCycle();
        }
        this.dim.enableI2cReadMode(this.port, this.newAddress, 0, 12);
        this.dim.setI2cPortActionFlag(this.port);
        this.dim.writeI2cCacheToController(this.port);
        while (!this.foundExpectedBytes(new int[] { 128, this.newAddress, 0, 12, 18, 77, 73 }, this.readLock, this.readCache)) {
            this.telemetry.addData("I2cAddressChange", "Have not confirmed the changes yet...");
            this.dim.readI2cCacheFromController(this.port);
            this.sleep(1000L);
        }
        this.telemetry.addData("I2cAddressChange", "Successfully changed the I2C address." + String.format("New address: %02x", this.newAddress));
    }
}
