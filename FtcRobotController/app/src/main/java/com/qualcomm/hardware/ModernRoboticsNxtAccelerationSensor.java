package com.qualcomm.hardware;

import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.AccelerationSensor;

public class ModernRoboticsNxtAccelerationSensor extends AccelerationSensor implements I2cPortReadyCallback
{
    public static final int ACCEL_LENGTH = 6;
    public static final int ADDRESS_ACCEL_START = 66;
    public static final byte I2C_ADDRESS = 2;
    private final ModernRoboticsUsbLegacyModule a;
    private final byte[] b;
    private final Lock c;
    private final int d;
    
    public ModernRoboticsNxtAccelerationSensor(final ModernRoboticsUsbLegacyModule a, final int d) {
        a.enableI2cReadMode(d, 2, 66, 6);
        this.a = a;
        this.b = a.getI2cReadCache(d);
        this.c = a.getI2cReadCacheLock(d);
        a.registerForI2cPortReadyCallback(this, this.d = d);
    }
    
    private double a(final double n, final double n2) {
        return (n2 + 4.0 * n) / 200.0;
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public Acceleration getAcceleration() {
        final Acceleration acceleration = new Acceleration();
        try {
            this.c.lock();
            acceleration.x = this.a(this.b[4], this.b[7]);
            acceleration.y = this.a(this.b[5], this.b[8]);
            acceleration.z = this.a(this.b[6], this.b[9]);
            return acceleration;
        }
        finally {
            this.c.unlock();
        }
    }
    
    @Override
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; port " + this.d;
    }
    
    @Override
    public String getDeviceName() {
        return "NXT Servo Controller";
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    @Override
    public void portIsReady(final int n) {
        this.a.setI2cPortActionFlag(this.d);
        this.a.writeI2cPortFlagOnlyToController(this.d);
        this.a.readI2cCacheFromController(this.d);
    }
    
    @Override
    public String status() {
        return String.format("NXT Acceleration Sensor, connected via device %s, port %d", this.a.getSerialNumber().toString(), this.d);
    }
}
