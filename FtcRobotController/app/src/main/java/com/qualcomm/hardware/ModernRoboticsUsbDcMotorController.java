package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.TypeConversion;
import com.qualcomm.robotcore.util.DifferentialControlLoopCoefficients;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class ModernRoboticsUsbDcMotorController extends ModernRoboticsUsbDevice implements DcMotorController, VoltageSensor
{
    public static final int ADDRESS_BATTERY_VOLTAGE = 84;
    public static final int[] ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP;
    public static final int ADDRESS_MOTOR1_CURRENT_ENCODER_VALUE = 76;
    public static final int ADDRESS_MOTOR1_D_COEFFICIENT = 89;
    public static final int ADDRESS_MOTOR1_GEAR_RATIO = 86;
    public static final int ADDRESS_MOTOR1_I_COEFFICIENT = 88;
    public static final int ADDRESS_MOTOR1_MODE = 68;
    public static final int ADDRESS_MOTOR1_POWER = 69;
    public static final int ADDRESS_MOTOR1_P_COEFFICIENT = 87;
    public static final int ADDRESS_MOTOR1_TARGET_ENCODER_VALUE = 64;
    public static final int ADDRESS_MOTOR2_CURRENT_ENCODER_VALUE = 80;
    public static final int ADDRESS_MOTOR2_D_COEFFICIENT = 93;
    public static final int ADDRESS_MOTOR2_GEAR_RATIO = 90;
    public static final int ADDRESS_MOTOR2_I_COEFFICIENT = 92;
    public static final int ADDRESS_MOTOR2_MODE = 71;
    public static final int ADDRESS_MOTOR2_POWER = 70;
    public static final int ADDRESS_MOTOR2_P_COEFFICIENT = 91;
    public static final int ADDRESS_MOTOR2_TARGET_ENCODER_VALUE = 72;
    public static final int[] ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP;
    public static final int[] ADDRESS_MOTOR_GEAR_RATIO_MAP;
    public static final int[] ADDRESS_MOTOR_MODE_MAP;
    public static final int[] ADDRESS_MOTOR_POWER_MAP;
    public static final int[] ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP;
    public static final int ADDRESS_UNUSED = 255;
    public static final double BATTERY_MAX_MEASURABLE_VOLTAGE = 20.4;
    public static final int BATTERY_MAX_MEASURABLE_VOLTAGE_INT = 1023;
    public static final byte CHANNEL_MODE_FLAG_BUSY = Byte.MIN_VALUE;
    public static final byte CHANNEL_MODE_FLAG_ERROR = 64;
    public static final byte CHANNEL_MODE_FLAG_LOCK = 4;
    public static final byte CHANNEL_MODE_FLAG_NO_TIMEOUT = 16;
    public static final byte CHANNEL_MODE_FLAG_REVERSE = 8;
    public static final byte CHANNEL_MODE_FLAG_SELECT_RESET = 3;
    public static final byte CHANNEL_MODE_FLAG_SELECT_RUN_CONSTANT_SPEED = 1;
    public static final byte CHANNEL_MODE_FLAG_SELECT_RUN_POWER_CONTROL_ONLY = 0;
    public static final byte CHANNEL_MODE_FLAG_SELECT_RUN_TO_POSITION = 2;
    public static final byte CHANNEL_MODE_FLAG_UNUSED = 32;
    public static final int CHANNEL_MODE_MASK_BUSY = 128;
    public static final int CHANNEL_MODE_MASK_EMPTY_D5 = 32;
    public static final int CHANNEL_MODE_MASK_ERROR = 64;
    public static final int CHANNEL_MODE_MASK_LOCK = 4;
    public static final int CHANNEL_MODE_MASK_NO_TIMEOUT = 16;
    public static final int CHANNEL_MODE_MASK_REVERSE = 8;
    public static final int CHANNEL_MODE_MASK_SELECTION = 3;
    public static final boolean DEBUG_LOGGING = false;
    public static final byte DEFAULT_D_COEFFICIENT = -72;
    public static final byte DEFAULT_I_COEFFICIENT = 64;
    public static final byte DEFAULT_P_COEFFICIENT = Byte.MIN_VALUE;
    public static final int DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAX = 255;
    public static final int MAX_MOTOR = 2;
    public static final int MIN_MOTOR = 1;
    public static final int MONITOR_LENGTH = 30;
    public static final byte POWER_BREAK = 0;
    public static final byte POWER_FLOAT = Byte.MIN_VALUE;
    public static final byte POWER_MAX = 100;
    public static final byte POWER_MIN = -100;
    public static final byte RATIO_MAX = Byte.MAX_VALUE;
    public static final byte RATIO_MIN = Byte.MIN_VALUE;
    public static final byte START_ADDRESS = 64;
    private a[] a;
    
    static {
        ADDRESS_MOTOR_POWER_MAP = new int[] { 255, 69, 70 };
        ADDRESS_MOTOR_MODE_MAP = new int[] { 255, 68, 71 };
        ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP = new int[] { 255, 64, 72 };
        ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP = new int[] { 255, 76, 80 };
        ADDRESS_MOTOR_GEAR_RATIO_MAP = new int[] { 255, 86, 90 };
        ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP = new int[] { 255, 87, 91 };
    }
    
    protected ModernRoboticsUsbDcMotorController(final SerialNumber serialNumber, final RobotUsbDevice robotUsbDevice, final EventLoopManager eventLoopManager) throws RobotCoreException, InterruptedException {
        int i = 0;
        super(serialNumber, eventLoopManager, new ReadWriteRunnableBlocking(serialNumber, robotUsbDevice, 30, 64, false));
        this.a = new a[3];
        this.readWriteRunnable.setCallback((ReadWriteRunnable.Callback)this);
        while (i < this.a.length) {
            this.a[i] = new a();
            ++i;
        }
        this.a();
        this.b();
    }
    
    private void a() {
        this.setMotorPowerFloat(1);
        this.setMotorPowerFloat(2);
    }
    
    private void a(final int n) {
        if (n < 1 || n > 2) {
            throw new IllegalArgumentException(String.format("Motor %d is invalid; valid motors are 1..%d", n, 2));
        }
    }
    
    private void b() {
        for (int i = 1; i <= 2; ++i) {
            this.write(ModernRoboticsUsbDcMotorController.ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP[i], new byte[] { -128, 64, -72 });
        }
    }
    
    public static RunMode flagToRunMode(final byte b) {
        switch (b & 0x3) {
            default: {
                return RunMode.RUN_WITHOUT_ENCODERS;
            }
            case 1: {
                return RunMode.RUN_USING_ENCODERS;
            }
            case 0: {
                return RunMode.RUN_WITHOUT_ENCODERS;
            }
            case 2: {
                return RunMode.RUN_TO_POSITION;
            }
            case 3: {
                return RunMode.RESET_ENCODERS;
            }
        }
    }
    
    public static byte runModeToFlag(final RunMode runMode) {
        switch (ModernRoboticsUsbDcMotorController$1.a[runMode.ordinal()]) {
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
        this.a();
        super.close();
    }
    
    @Override
    public String getConnectionInfo() {
        return "USB " + this.getSerialNumber();
    }
    
    @Override
    public String getDeviceName() {
        return "Modern Robotics USB DC Motor Controller";
    }
    
    public DifferentialControlLoopCoefficients getDifferentialControlLoopCoefficients(final int n) {
        this.a(n);
        final DifferentialControlLoopCoefficients differentialControlLoopCoefficients = new DifferentialControlLoopCoefficients();
        final byte[] read = this.read(ModernRoboticsUsbDcMotorController.ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP[n], 3);
        differentialControlLoopCoefficients.p = read[0];
        differentialControlLoopCoefficients.i = read[1];
        differentialControlLoopCoefficients.d = read[2];
        return differentialControlLoopCoefficients;
    }
    
    public double getGearRatio(final int n) {
        this.a(n);
        return this.read(ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_GEAR_RATIO_MAP[n], 1)[0] / 127.0;
    }
    
    @Override
    public RunMode getMotorChannelMode(final int n) {
        this.a(n);
        return flagToRunMode(this.read(ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_MODE_MAP[n]));
    }
    
    @Override
    public DeviceMode getMotorControllerDeviceMode() {
        return DeviceMode.READ_WRITE;
    }
    
    @Override
    public int getMotorCurrentPosition(final int n) {
        this.a(n);
        return TypeConversion.byteArrayToInt(this.read(ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP[n], 4));
    }
    
    @Override
    public double getMotorPower(final int n) {
        this.a(n);
        final byte read = this.read(ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_POWER_MAP[n]);
        if (read == -128) {
            return 0.0;
        }
        return read / 100.0;
    }
    
    @Override
    public boolean getMotorPowerFloat(final int n) {
        this.a(n);
        return this.read(ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_POWER_MAP[n]) == -128;
    }
    
    @Override
    public int getMotorTargetPosition(final int n) {
        this.a(n);
        return TypeConversion.byteArrayToInt(this.read(ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP[n], 4));
    }
    
    @Override
    public double getVoltage() {
        return 20.4 * ((0x3FF & TypeConversion.byteArrayToShort(this.read(84, 2)) >> 6) / 1023.0);
    }
    
    @Override
    public boolean isBusy(final int n) {
        this.a(n);
        return this.a[n].a();
    }
    
    @Override
    public void readComplete() throws InterruptedException {
        for (int i = 1; i <= 2; ++i) {
            this.a[i].a(this.getMotorCurrentPosition(i));
        }
    }
    
    public void setDifferentialControlLoopCoefficients(final int n, final DifferentialControlLoopCoefficients differentialControlLoopCoefficients) {
        this.a(n);
        if (differentialControlLoopCoefficients.p > 255.0) {
            differentialControlLoopCoefficients.p = 255.0;
        }
        if (differentialControlLoopCoefficients.i > 255.0) {
            differentialControlLoopCoefficients.i = 255.0;
        }
        if (differentialControlLoopCoefficients.d > 255.0) {
            differentialControlLoopCoefficients.d = 255.0;
        }
        this.write(ModernRoboticsUsbDcMotorController.ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP[n], new byte[] { (byte)differentialControlLoopCoefficients.p, (byte)differentialControlLoopCoefficients.i, (byte)differentialControlLoopCoefficients.d });
    }
    
    public void setGearRatio(final int n, final double n2) {
        this.a(n);
        Range.throwIfRangeIsInvalid(n2, -1.0, 1.0);
        this.write(ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_GEAR_RATIO_MAP[n], new byte[] { (byte)(127.0 * n2) });
    }
    
    @Override
    public void setMotorChannelMode(final int n, final RunMode runMode) {
        this.a(n);
        this.write(ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_MODE_MAP[n], runModeToFlag(runMode));
    }
    
    @Override
    public void setMotorControllerDeviceMode(final DeviceMode deviceMode) {
    }
    
    @Override
    public void setMotorPower(final int n, final double n2) {
        this.a(n);
        Range.throwIfRangeIsInvalid(n2, -1.0, 1.0);
        this.write(ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_POWER_MAP[n], new byte[] { (byte)(100.0 * n2) });
    }
    
    @Override
    public void setMotorPowerFloat(final int n) {
        this.a(n);
        this.write(ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_POWER_MAP[n], new byte[] { -128 });
    }
    
    @Override
    public void setMotorTargetPosition(final int n, final int n2) {
        this.a(n);
        Range.throwIfRangeIsInvalid(n2, -2.147483648E9, 2.147483647E9);
        this.write(ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP[n], TypeConversion.intToByteArray(n2));
    }
    
    private static class a
    {
        private int[] a;
        private int[] b;
        private int c;
        
        private a() {
            this.a = new int[3];
            this.b = new int[3];
            this.c = 0;
        }
        
        public void a(final int n) {
            final int n2 = this.a[this.c];
            this.c = (1 + this.c) % this.a.length;
            this.b[this.c] = Math.abs(n2 - n);
            this.a[this.c] = n;
        }
        
        public boolean a() {
            final int[] b = this.b;
            final int length = b.length;
            int i = 0;
            int n = 0;
            while (i < length) {
                n += b[i];
                ++i;
            }
            boolean b2 = false;
            if (n > 6) {
                b2 = true;
            }
            return b2;
        }
    }
}
