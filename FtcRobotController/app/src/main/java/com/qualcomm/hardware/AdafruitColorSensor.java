package com.qualcomm.hardware;

import android.graphics.Color;
import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.ColorSensor;

public class AdafruitColorSensor extends ColorSensor implements I2cPortReadyCallback
{
    public static final int ADDRESS_TCS34725_ENABLE = 0;
    public static final int I2C_ADDRESS_TCS34725 = 82;
    public static final int OFFSET_ALPHA_HIGH_BYTE = 5;
    public static final int OFFSET_ALPHA_LOW_BYTE = 4;
    public static final int OFFSET_BLUE_HIGH_BYTE = 11;
    public static final int OFFSET_BLUE_LOW_BYTE = 10;
    public static final int OFFSET_GREEN_HIGH_BYTE = 9;
    public static final int OFFSET_GREEN_LOW_BYTE = 8;
    public static final int OFFSET_RED_HIGH_BYTE = 7;
    public static final int OFFSET_RED_LOW_BYTE = 6;
    public static final int TCS34725_BDATAL = 26;
    public static final int TCS34725_CDATAL = 20;
    public static final int TCS34725_COMMAND_BIT = 128;
    public static final int TCS34725_ENABLE_AEN = 2;
    public static final int TCS34725_ENABLE_AIEN = 16;
    public static final int TCS34725_ENABLE_PON = 1;
    public static final int TCS34725_GDATAL = 24;
    public static final int TCS34725_ID = 18;
    public static final int TCS34725_RDATAL = 22;
    private final DeviceInterfaceModule a;
    private final byte[] b;
    private final Lock c;
    private final byte[] d;
    private final Lock e;
    private final int f;
    private boolean g;
    private boolean h;
    
    public AdafruitColorSensor(final DeviceInterfaceModule a, final int f) {
        this.g = false;
        this.h = false;
        this.f = f;
        this.a = a;
        this.b = a.getI2cReadCache(f);
        this.c = a.getI2cReadCacheLock(f);
        this.d = a.getI2cWriteCache(f);
        this.e = a.getI2cWriteCacheLock(f);
        this.g = true;
        a.registerForI2cPortReadyCallback((I2cController.I2cPortReadyCallback)this, f);
    }
    
    private int a(final int n, final int n2) {
        try {
            this.c.lock();
            return this.b[n] << 8 | (this.b[n2] & 0xFF);
        }
        finally {
            this.c.unlock();
        }
    }
    
    private void a() {
        this.a.enableI2cReadMode(this.f, 82, 148, 8);
        this.a.writeI2cCacheToController(this.f);
    }
    
    private void b() {
        this.a.enableI2cWriteMode(this.f, 82, 128, 1);
        try {
            this.e.lock();
            this.d[4] = 3;
            this.e.unlock();
            this.a.setI2cPortActionFlag(this.f);
            this.a.writeI2cCacheToController(this.f);
        }
        finally {
            this.e.unlock();
        }
    }
    
    @Override
    public int alpha() {
        return this.a(5, 4);
    }
    
    @Override
    public int argb() {
        return Color.argb(this.alpha(), this.red(), this.green(), this.blue());
    }
    
    @Override
    public int blue() {
        return this.a(11, 10);
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public void enableLed(final boolean b) {
        throw new UnsupportedOperationException("enableLed is not implemented.");
    }
    
    @Override
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; I2C port: " + this.f;
    }
    
    @Override
    public String getDeviceName() {
        return "Adafruit Color Sensor";
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    @Override
    public int green() {
        return this.a(9, 8);
    }
    
    @Override
    public void portIsReady(final int n) {
        if (this.g) {
            this.b();
            this.g = false;
            this.h = true;
        }
        else if (this.h) {
            this.a();
            this.h = false;
        }
        this.a.readI2cCacheFromController(this.f);
        this.a.setI2cPortActionFlag(this.f);
        this.a.writeI2cPortFlagOnlyToController(this.f);
    }
    
    @Override
    public int red() {
        return this.a(7, 6);
    }
}
