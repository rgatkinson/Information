package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.TypeConversion;
import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

public class ModernRoboticsNxtUltrasonicSensor extends UltrasonicSensor implements I2cPortReadyCallback
{
    public static final int ADDRESS_DISTANCE = 66;
    public static final int I2C_ADDRESS = 2;
    Lock a;
    byte[] b;
    private final ModernRoboticsUsbLegacyModule c;
    private final int d;
    
    ModernRoboticsNxtUltrasonicSensor(final ModernRoboticsUsbLegacyModule c, final int n) {
        this.c = c;
        this.d = n;
        this.a = c.getI2cReadCacheLock(n);
        this.b = c.getI2cReadCache(n);
        c.enableI2cReadMode(n, 2, 66, 1);
        c.enable9v(n, true);
        c.setI2cPortActionFlag(n);
        c.readI2cCacheFromController(n);
        c.registerForI2cPortReadyCallback(this, n);
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public String getConnectionInfo() {
        return this.c.getConnectionInfo() + "; port " + this.d;
    }
    
    @Override
    public String getDeviceName() {
        return "NXT Ultrasonic Sensor";
    }
    
    @Override
    public double getUltrasonicLevel() {
        try {
            this.a.lock();
            final byte b = this.b[4];
            this.a.unlock();
            return TypeConversion.unsignedByteToDouble(b);
        }
        finally {
            this.a.unlock();
        }
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    @Override
    public void portIsReady(final int n) {
        this.c.setI2cPortActionFlag(this.d);
        this.c.writeI2cCacheToController(this.d);
        this.c.readI2cCacheFromController(this.d);
    }
    
    @Override
    public String status() {
        return String.format("NXT Ultrasonic Sensor, connected via device %s, port %d", this.c.getSerialNumber().toString(), this.d);
    }
}
