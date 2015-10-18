package com.qualcomm.hardware;

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.TypeConversion;

public class ModernRoboticsUsbServoController extends ModernRoboticsUsbDevice implements ServoController {
   public static final int ADDRESS_CHANNEL1 = 66;
   public static final int ADDRESS_CHANNEL2 = 67;
   public static final int ADDRESS_CHANNEL3 = 68;
   public static final int ADDRESS_CHANNEL4 = 69;
   public static final int ADDRESS_CHANNEL5 = 70;
   public static final int ADDRESS_CHANNEL6 = 71;
   public static final byte[] ADDRESS_CHANNEL_MAP = new byte[]{(byte)-1, (byte)66, (byte)67, (byte)68, (byte)69, (byte)70, (byte)71};
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

   protected ModernRoboticsUsbServoController(SerialNumber serialNumber, RobotUsbDevice robotUsbDevice, EventLoopManager eventLoopManager) throws RobotCoreException, InterruptedException {
      super(serialNumber, eventLoopManager, new ReadWriteRunnableBlocking(serialNumber, robotUsbDevice, MONITOR_LENGTH, START_ADDRESS, false));
      this.pwmDisable();
   }

   private void validateServo(int servo) {
      if(servo < 1 || servo > ADDRESS_CHANNEL_MAP.length) {
         throw new IllegalArgumentException(String.format("Channel %d is invalid; valid channels are 1..%d", servo, MAX_SERVOS));
      }
   }

   public void close() {
      this.pwmDisable();
      super.close();
   }

   public String getConnectionInfo() {
      return "USB " + this.getSerialNumber();
   }

   public String getDeviceName() {
      return "Modern Robotics USB Servo Controller";
   }

   public ServoController.PwmStatus getPwmStatus() {
      return this.read(ADDRESS_PWM, 1)[0] == PWM_DISABLE
              ? ServoController.PwmStatus.DISABLED
              : ServoController.PwmStatus.ENABLED;
   }

   public double getServoPosition(int servo) {
      this.validateServo(servo);
      return TypeConversion.unsignedByteToDouble(this.read(ADDRESS_CHANNEL_MAP[servo], 1)[0]) / ((double)SERVO_POSITION_MAX);
   }

   public void pwmDisable() {
      this.write(ADDRESS_PWM, (byte)PWM_DISABLE);
   }

   public void pwmEnable() {
      this.write(ADDRESS_PWM, (byte)PWM_ENABLE);
   }

   public void setServoPosition(int servo, double position) {
      this.validateServo(servo);
      Range.throwIfRangeIsInvalid(position, 0.0D, 1.0D);
      this.write(ADDRESS_CHANNEL_MAP[servo], SERVO_POSITION_MAX * position);
      this.pwmEnable();
   }
}
