package com.qualcomm.hardware;

import android.graphics.Color;
import com.qualcomm.robotcore.util.TypeConversion;
import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.ColorSensor;

public class ModernRoboticsColorSensor extends ColorSensor implements I2cPortReadyCallback
{
    public static final int ADDRESS_COLOR_NUMBER = 4;
    public static final int ADDRESS_COMMAND = 3;
    public static final int ADDRESS_I2C = 60;
    public static final int BUFFER_LENGTH = 6;
    public static final int COMMAND_ACTIVE_LED = 0;
    public static final int COMMAND_PASSIVE_LED = 1;
    public static final int OFFSET_ALPHA_VALUE = 9;
    public static final int OFFSET_BLUE_READING = 8;
    public static final int OFFSET_COLOR_NUMBER = 5;
    public static final int OFFSET_COMMAND = 4;
    public static final int OFFSET_GREEN_READING = 7;
    public static final int OFFSET_RED_READING = 6;
    private final DeviceInterfaceModule a;
    private final byte[] b;
    private final Lock c;
    private final byte[] d;
    private final Lock e;
    private a f;
    private volatile int g;
    private final int h;
    
    ModernRoboticsColorSensor(final DeviceInterfaceModule a, final int n) {
        this.f = ModernRoboticsColorSensor.a.a;
        this.g = 0;
        this.a = a;
        this.h = n;
        this.b = a.getI2cReadCache(n);
        this.c = a.getI2cReadCacheLock(n);
        this.d = a.getI2cWriteCache(n);
        this.e = a.getI2cWriteCacheLock(n);
        a.enableI2cReadMode(n, 60, 3, 6);
        a.setI2cPortActionFlag(n);
        a.writeI2cCacheToController(n);
        a.registerForI2cPortReadyCallback((I2cController.I2cPortReadyCallback)this, n);
    }
    
    private int a(final int n) {
        try {
            this.c.lock();
            final byte b = this.b[n];
            this.c.unlock();
            return TypeConversion.unsignedByteToInt(b);
        }
        finally {
            this.c.unlock();
        }
    }
    
    @Override
    public int alpha() {
        return this.a(9);
    }
    
    @Override
    public int argb() {
        return Color.argb(this.alpha(), this.red(), this.green(), this.blue());
    }
    
    @Override
    public int blue() {
        return this.a(8);
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public void enableLed(final boolean b) {
        byte g = 1;
        if (b) {
            g = 0;
        }
        if (this.g == g) {
            return;
        }
        this.g = g;
        this.f = ModernRoboticsColorSensor.a.b;
        try {
            this.e.lock();
            this.d[4] = g;
        }
        finally {
            this.e.unlock();
        }
    }
    
    @Override
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; I2C port: " + this.h;
    }
    
    @Override
    public String getDeviceName() {
        return "Modern Robotics Color Sensor";
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    @Override
    public int green() {
        return this.a(7);
    }
    
    @Override
    public void portIsReady(final int n) {
        this.a.setI2cPortActionFlag(this.h);
        this.a.readI2cCacheFromController(this.h);
        if (this.f == ModernRoboticsColorSensor.a.b) {
            this.a.enableI2cWriteMode(this.h, 60, 3, 6);
            this.a.writeI2cCacheToController(this.h);
            this.f = ModernRoboticsColorSensor.a.c;
            return;
        }
        if (this.f == ModernRoboticsColorSensor.a.c) {
            this.a.enableI2cReadMode(this.h, 60, 3, 6);
            this.a.writeI2cCacheToController(this.h);
            this.f = ModernRoboticsColorSensor.a.a;
            return;
        }
        this.a.writeI2cPortFlagOnlyToController(this.h);
    }
    
    @Override
    public int red() {
        return this.a(6);
    }
    
    private enum a
    {
        a, 
        b, 
        c;
    }
}
