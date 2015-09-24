package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.I2cController;

public class ModernRoboticsNxtServoController implements I2cPortReadyCallback, ServoController
{
    public static final int I2C_ADDRESS = 2;
    public static final int MAX_SERVOS = 6;
    public static final int MEM_READ_LENGTH = 7;
    public static final int MEM_START_ADDRESS = 66;
    public static final int OFFSET_PWM = 10;
    public static final int OFFSET_SERVO1_POSITION = 4;
    public static final int OFFSET_SERVO2_POSITION = 5;
    public static final int OFFSET_SERVO3_POSITION = 6;
    public static final int OFFSET_SERVO4_POSITION = 7;
    public static final int OFFSET_SERVO5_POSITION = 8;
    public static final int OFFSET_SERVO6_POSITION = 9;
    public static final byte[] OFFSET_SERVO_MAP;
    public static final int OFFSET_UNUSED = -1;
    public static final byte PWM_DISABLE = -1;
    public static final byte PWM_ENABLE = 0;
    public static final byte PWM_ENABLE_WITHOUT_TIMEOUT = -86;
    public static final int SERVO_POSITION_MAX = 255;
    private final ModernRoboticsUsbLegacyModule a;
    private final byte[] b;
    private final Lock c;
    private final int d;
    private ElapsedTime e;
    private volatile boolean f;
    
    static {
        OFFSET_SERVO_MAP = new byte[] { -1, 4, 5, 6, 7, 8, 9 };
    }
    
    public ModernRoboticsNxtServoController(final ModernRoboticsUsbLegacyModule a, final int n) {
        this.e = new ElapsedTime(0L);
        this.f = true;
        this.a = a;
        this.d = n;
        this.b = a.getI2cWriteCache(n);
        this.c = a.getI2cWriteCacheLock(n);
        a.enableI2cWriteMode(n, 2, 66, 7);
        this.pwmDisable();
        a.setI2cPortActionFlag(n);
        a.writeI2cCacheToController(n);
        a.registerForI2cPortReadyCallback(this, n);
    }
    
    private void a(final int n) {
        if (n < 1 || n > ModernRoboticsNxtServoController.OFFSET_SERVO_MAP.length) {
            throw new IllegalArgumentException(String.format("Channel %d is invalid; valid channels are 1..%d", n, 6));
        }
    }
    
    @Override
    public void close() {
        this.pwmDisable();
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
    public PwmStatus getPwmStatus() {
        return PwmStatus.DISABLED;
    }
    
    @Override
    public double getServoPosition(final int n) {
        return 0.0;
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    @Override
    public void portIsReady(final int n) {
        if (this.f || this.e.time() > 5.0) {
            this.a.setI2cPortActionFlag(this.d);
            this.a.writeI2cCacheToController(this.d);
            this.e.reset();
        }
        this.f = false;
    }
    
    @Override
    public void pwmDisable() {
        try {
            this.c.lock();
            if (-1 != this.b[10]) {
                this.b[10] = -1;
                this.f = true;
            }
        }
        finally {
            this.c.unlock();
        }
    }
    
    @Override
    public void pwmEnable() {
        try {
            this.c.lock();
            if (this.b[10] != 0) {
                this.b[10] = 0;
                this.f = true;
            }
        }
        finally {
            this.c.unlock();
        }
    }
    
    @Override
    public void setServoPosition(final int n, final double n2) {
        this.a(n);
        Range.throwIfRangeIsInvalid(n2, 0.0, 1.0);
        final byte b = (byte)(255.0 * n2);
        try {
            this.c.lock();
            if (b != this.b[ModernRoboticsNxtServoController.OFFSET_SERVO_MAP[n]]) {
                this.f = true;
                this.b[ModernRoboticsNxtServoController.OFFSET_SERVO_MAP[n]] = b;
                this.b[10] = 0;
            }
        }
        finally {
            this.c.unlock();
        }
    }
}
