package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.TypeConversion;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.concurrent.locks.Lock;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class ModernRoboticsNxtDcMotorController implements DcMotorController, I2cPortReadyCallback
{
    public static final byte CHANNEL_MODE_FLAG_SELECT_RESET = 3;
    public static final byte CHANNEL_MODE_FLAG_SELECT_RUN_CONSTANT_SPEED_NXT = 1;
    public static final byte CHANNEL_MODE_FLAG_SELECT_RUN_POWER_CONTROL_ONLY_NXT = 0;
    public static final byte CHANNEL_MODE_FLAG_SELECT_RUN_TO_POSITION = 2;
    public static final int CHANNEL_MODE_MASK_BUSY = 128;
    public static final int CHANNEL_MODE_MASK_EMPTY_D5 = 32;
    public static final int CHANNEL_MODE_MASK_ERROR = 64;
    public static final int CHANNEL_MODE_MASK_LOCK = 4;
    public static final int CHANNEL_MODE_MASK_NO_TIMEOUT = 16;
    public static final int CHANNEL_MODE_MASK_REVERSE = 8;
    public static final int CHANNEL_MODE_MASK_SELECTION = 3;
    public static final int I2C_ADDRESS = 2;
    public static final int MAX_MOTOR = 2;
    public static final int MEM_READ_LENGTH = 20;
    public static final int MEM_START_ADDRESS = 64;
    public static final int MIN_MOTOR = 1;
    public static final int NUM_BYTES = 20;
    public static final byte[] OFFSET_MAP_MOTOR_CURRENT_ENCODER_VALUE;
    public static final byte[] OFFSET_MAP_MOTOR_MODE;
    public static final byte[] OFFSET_MAP_MOTOR_POWER;
    public static final byte[] OFFSET_MAP_MOTOR_TARGET_ENCODER_VALUE;
    public static final int OFFSET_MOTOR1_CURRENT_ENCODER_VALUE = 16;
    public static final int OFFSET_MOTOR1_MODE = 8;
    public static final int OFFSET_MOTOR1_POWER = 9;
    public static final int OFFSET_MOTOR1_TARGET_ENCODER_VALUE = 4;
    public static final int OFFSET_MOTOR2_CURRENT_ENCODER_VALUE = 20;
    public static final int OFFSET_MOTOR2_MODE = 11;
    public static final int OFFSET_MOTOR2_POWER = 10;
    public static final int OFFSET_MOTOR2_TARGET_ENCODER_VALUE = 12;
    public static final int OFFSET_UNUSED = -1;
    public static final byte POWER_BREAK = 0;
    public static final byte POWER_FLOAT = Byte.MIN_VALUE;
    public static final byte POWER_MAX = 100;
    public static final byte POWER_MIN = -100;
    private final ModernRoboticsUsbLegacyModule a;
    private final byte[] b;
    private final Lock c;
    private final byte[] d;
    private final Lock e;
    private final int f;
    private final ElapsedTime g;
    private volatile DeviceMode h;
    private volatile boolean i;
    
    static {
        OFFSET_MAP_MOTOR_POWER = new byte[] { -1, 9, 10 };
        OFFSET_MAP_MOTOR_MODE = new byte[] { -1, 8, 11 };
        OFFSET_MAP_MOTOR_TARGET_ENCODER_VALUE = new byte[] { -1, 4, 12 };
        OFFSET_MAP_MOTOR_CURRENT_ENCODER_VALUE = new byte[] { -1, 16, 20 };
    }
    
    public ModernRoboticsNxtDcMotorController(final ModernRoboticsUsbLegacyModule a, final int f) {
        this.g = new ElapsedTime(0L);
        this.i = true;
        this.a = a;
        this.f = f;
        this.b = a.getI2cReadCache(f);
        this.c = a.getI2cReadCacheLock(f);
        this.d = a.getI2cWriteCache(f);
        this.e = a.getI2cWriteCacheLock(f);
        this.h = DeviceMode.WRITE_ONLY;
        a.enableI2cWriteMode(f, 2, 64, 20);
        try {
            this.e.lock();
            this.d[9] = -128;
            this.d[10] = -128;
            this.e.unlock();
            a.writeI2cCacheToController(f);
            a.registerForI2cPortReadyCallback(this, f);
        }
        finally {
            this.e.unlock();
        }
    }
    
    private void a() {
        if (this.h != DeviceMode.SWITCHING_TO_WRITE_MODE && (this.h == DeviceMode.READ_ONLY || this.h == DeviceMode.SWITCHING_TO_READ_MODE)) {
            String s = "Cannot write while in this mode: " + this.h;
            final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            if (stackTrace != null && stackTrace.length > 3) {
                s = s + "\n from method: " + stackTrace[3].getMethodName();
            }
            throw new IllegalArgumentException(s);
        }
    }
    
    private void a(final int n) {
        if (n < 1 || n > 2) {
            throw new IllegalArgumentException(String.format("Motor %d is invalid; valid motors are 1..%d", n, 2));
        }
    }
    
    private void b() {
        if (this.h != DeviceMode.SWITCHING_TO_READ_MODE && (this.h == DeviceMode.WRITE_ONLY || this.h == DeviceMode.SWITCHING_TO_WRITE_MODE)) {
            String s = "Cannot read while in this mode: " + this.h;
            final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            if (stackTrace != null && stackTrace.length > 3) {
                s = s + "\n from method: " + stackTrace[3].getMethodName();
            }
            throw new IllegalArgumentException(s);
        }
    }
    
    public static RunMode flagToRunModeNXT(final byte b) {
        switch (b & 0x3) {
            default: {
                return RunMode.RUN_WITHOUT_ENCODERS;
            }
            case 0: {
                return RunMode.RUN_WITHOUT_ENCODERS;
            }
            case 1: {
                return RunMode.RUN_USING_ENCODERS;
            }
            case 2: {
                return RunMode.RUN_TO_POSITION;
            }
            case 3: {
                return RunMode.RESET_ENCODERS;
            }
        }
    }
    
    public static byte runModeToFlagNXT(final RunMode runMode) {
        switch (ModernRoboticsNxtDcMotorController$1.b[runMode.ordinal()]) {
            default: {
                return 1;
            }
            case 2: {
                return 0;
            }
            case 3: {
                return 2;
            }
            case 4: {
                return 3;
            }
        }
    }
    
    @Override
    public void close() {
        if (this.h == DeviceMode.WRITE_ONLY) {
            this.setMotorPowerFloat(1);
            this.setMotorPowerFloat(2);
        }
    }
    
    @Override
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; port " + this.f;
    }
    
    @Override
    public String getDeviceName() {
        return "NXT DC Motor Controller";
    }
    
    @Override
    public RunMode getMotorChannelMode(final int n) {
        this.a(n);
        this.b();
        try {
            this.c.lock();
            final byte b = this.b[ModernRoboticsNxtDcMotorController.OFFSET_MAP_MOTOR_MODE[n]];
            this.c.unlock();
            return flagToRunModeNXT(b);
        }
        finally {
            this.c.unlock();
        }
    }
    
    @Override
    public DeviceMode getMotorControllerDeviceMode() {
        return this.h;
    }
    
    @Override
    public int getMotorCurrentPosition(final int n) {
        this.a(n);
        this.b();
        final byte[] array = new byte[4];
        try {
            this.c.lock();
            System.arraycopy(this.b, ModernRoboticsNxtDcMotorController.OFFSET_MAP_MOTOR_CURRENT_ENCODER_VALUE[n], array, 0, array.length);
            this.c.unlock();
            return TypeConversion.byteArrayToInt(array);
        }
        finally {
            this.c.unlock();
        }
    }
    
    @Override
    public double getMotorPower(final int n) {
        this.a(n);
        this.b();
        byte b;
        try {
            this.c.lock();
            b = this.b[ModernRoboticsNxtDcMotorController.OFFSET_MAP_MOTOR_POWER[n]];
            this.c.unlock();
            if (b == -128) {
                return 0.0;
            }
        }
        finally {
            this.c.unlock();
        }
        return b / 100.0;
    }
    
    @Override
    public boolean getMotorPowerFloat(final int n) {
        this.a(n);
        this.b();
        try {
            this.c.lock();
            return this.b[ModernRoboticsNxtDcMotorController.OFFSET_MAP_MOTOR_POWER[n]] == -128;
        }
        finally {
            this.c.unlock();
        }
    }
    
    @Override
    public int getMotorTargetPosition(final int n) {
        this.a(n);
        this.b();
        final byte[] array = new byte[4];
        try {
            this.c.lock();
            System.arraycopy(this.b, ModernRoboticsNxtDcMotorController.OFFSET_MAP_MOTOR_TARGET_ENCODER_VALUE[n], array, 0, array.length);
            this.c.unlock();
            return TypeConversion.byteArrayToInt(array);
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
    public boolean isBusy(final int n) {
        this.a(n);
        this.b();
        try {
            this.c.lock();
            return (this.b[ModernRoboticsNxtDcMotorController.OFFSET_MAP_MOTOR_MODE[n]] & 0x80) == 0x80;
        }
        finally {
            this.c.unlock();
        }
    }
    
    @Override
    public void portIsReady(final int n) {
        switch (ModernRoboticsNxtDcMotorController$1.a[this.h.ordinal()]) {
            case 3: {
                if (this.a.isI2cPortInReadMode(n)) {
                    this.h = DeviceMode.READ_ONLY;
                    break;
                }
                break;
            }
            case 4: {
                if (this.a.isI2cPortInWriteMode(n)) {
                    this.h = DeviceMode.WRITE_ONLY;
                    break;
                }
                break;
            }
        }
        if (this.h == DeviceMode.READ_ONLY) {
            this.a.setI2cPortActionFlag(this.f);
            this.a.writeI2cPortFlagOnlyToController(this.f);
        }
        else {
            if (this.i || this.g.time() > 2.0) {
                this.a.setI2cPortActionFlag(this.f);
                this.a.writeI2cCacheToController(this.f);
                this.g.reset();
            }
            this.i = false;
        }
        this.a.readI2cCacheFromController(this.f);
    }
    
    @Override
    public void setMotorChannelMode(final int n, final RunMode runMode) {
        this.a(n);
        this.a();
        final byte runModeToFlagNXT = runModeToFlagNXT(runMode);
        try {
            this.e.lock();
            if (this.d[ModernRoboticsNxtDcMotorController.OFFSET_MAP_MOTOR_MODE[n]] != runModeToFlagNXT) {
                this.d[ModernRoboticsNxtDcMotorController.OFFSET_MAP_MOTOR_MODE[n]] = runModeToFlagNXT;
                this.i = true;
            }
        }
        finally {
            this.e.unlock();
        }
    }
    
    @Override
    public void setMotorControllerDeviceMode(final DeviceMode deviceMode) {
        if (this.h == deviceMode) {
            return;
        }
        switch (ModernRoboticsNxtDcMotorController$1.a[deviceMode.ordinal()]) {
            case 1: {
                this.h = DeviceMode.SWITCHING_TO_READ_MODE;
                this.a.enableI2cReadMode(this.f, 2, 64, 20);
                break;
            }
            case 2: {
                this.h = DeviceMode.SWITCHING_TO_WRITE_MODE;
                this.a.enableI2cWriteMode(this.f, 2, 64, 20);
                break;
            }
        }
        this.i = true;
    }
    
    @Override
    public void setMotorPower(final int n, final double n2) {
        this.a(n);
        this.a();
        Range.throwIfRangeIsInvalid(n2, -1.0, 1.0);
        final byte b = (byte)(100.0 * n2);
        try {
            this.e.lock();
            if (b != this.d[ModernRoboticsNxtDcMotorController.OFFSET_MAP_MOTOR_POWER[n]]) {
                this.d[ModernRoboticsNxtDcMotorController.OFFSET_MAP_MOTOR_POWER[n]] = b;
                this.i = true;
            }
        }
        finally {
            this.e.unlock();
        }
    }
    
    @Override
    public void setMotorPowerFloat(final int n) {
        this.a(n);
        this.a();
        try {
            this.e.lock();
            if (-128 != this.d[ModernRoboticsNxtDcMotorController.OFFSET_MAP_MOTOR_POWER[n]]) {
                this.d[ModernRoboticsNxtDcMotorController.OFFSET_MAP_MOTOR_POWER[n]] = -128;
                this.i = true;
            }
        }
        finally {
            this.e.unlock();
        }
    }
    
    @Override
    public void setMotorTargetPosition(final int n, final int n2) {
        this.a(n);
        this.a();
        final byte[] intToByteArray = TypeConversion.intToByteArray(n2);
        try {
            this.e.lock();
            System.arraycopy(intToByteArray, 0, this.d, ModernRoboticsNxtDcMotorController.OFFSET_MAP_MOTOR_TARGET_ENCODER_VALUE[n], intToByteArray.length);
            this.i = true;
        }
        finally {
            this.e.unlock();
        }
    }
}
