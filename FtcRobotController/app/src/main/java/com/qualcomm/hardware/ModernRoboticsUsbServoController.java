package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.TypeConversion;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.hardware.ServoController;

public class ModernRoboticsUsbServoController extends ModernRoboticsUsbDevice implements ServoController
{
    public static final int ADDRESS_CHANNEL1 = 66;
    public static final int ADDRESS_CHANNEL2 = 67;
    public static final int ADDRESS_CHANNEL3 = 68;
    public static final int ADDRESS_CHANNEL4 = 69;
    public static final int ADDRESS_CHANNEL5 = 70;
    public static final int ADDRESS_CHANNEL6 = 71;
    public static final byte[] ADDRESS_CHANNEL_MAP;
    public static final int ADDRESS_PWM = 72;
    public static final int ADDRESS_UNUSED = -1;
    public static final boolean DEBUG_LOGGING = false;
    public static final int MAX_SERVOS = 6;
    public static final int MONITOR_LENGTH = 9;
    public static final byte PWM_DISABLE = -1;
    public static final byte PWM_ENABLE = 0;
    public static final byte PWM_ENABLE_WITHOUT_TIMEOUT = -86;
    public static final int SERVO_POSITION_MAX = 255;
    public static final byte START_ADDRESS = 64;
    
    static {
        ADDRESS_CHANNEL_MAP = new byte[] { -1, 66, 67, 68, 69, 70, 71 };
    }
    
    protected ModernRoboticsUsbServoController(final SerialNumber serialNumber, final RobotUsbDevice robotUsbDevice, final EventLoopManager eventLoopManager) throws RobotCoreException, InterruptedException {
        super(serialNumber, eventLoopManager, new ReadWriteRunnableBlocking(serialNumber, robotUsbDevice, 9, 64, false));
        this.pwmDisable();
    }
    
    private void a(final int n) {
        if (n < 1 || n > ModernRoboticsUsbServoController.ADDRESS_CHANNEL_MAP.length) {
            throw new IllegalArgumentException(String.format("Channel %d is invalid; valid channels are 1..%d", n, 6));
        }
    }
    
    @Override
    public void close() {
        this.pwmDisable();
        super.close();
    }
    
    @Override
    public String getConnectionInfo() {
        return "USB " + this.getSerialNumber();
    }
    
    @Override
    public String getDeviceName() {
        return "Modern Robotics USB Servo Controller";
    }
    
    @Override
    public PwmStatus getPwmStatus() {
        if (this.read(72, 1)[0] == -1) {
            return PwmStatus.DISABLED;
        }
        return PwmStatus.ENABLED;
    }
    
    @Override
    public double getServoPosition(final int n) {
        this.a(n);
        return TypeConversion.unsignedByteToDouble(this.read(ModernRoboticsUsbServoController.ADDRESS_CHANNEL_MAP[n], 1)[0]) / 255.0;
    }
    
    @Override
    public void pwmDisable() {
        this.write(72, (byte)(-1));
    }
    
    @Override
    public void pwmEnable() {
        this.write(72, (byte)0);
    }
    
    @Override
    public void setServoPosition(final int n, final double n2) {
        this.a(n);
        Range.throwIfRangeIsInvalid(n2, 0.0, 1.0);
        this.write(ModernRoboticsUsbServoController.ADDRESS_CHANNEL_MAP[n], 255.0 * n2);
        this.pwmEnable();
    }
}
