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

   protected ModernRoboticsUsbServoController(SerialNumber var1, RobotUsbDevice var2, EventLoopManager var3) throws RobotCoreException, InterruptedException {
      super(var1, var3, new ReadWriteRunnableBlocking(var1, var2, 9, 64, false));
      this.pwmDisable();
   }

   private void a(int var1) {
      if(var1 < 1 || var1 > ADDRESS_CHANNEL_MAP.length) {
         Object[] var2 = new Object[]{Integer.valueOf(var1), Integer.valueOf(6)};
         throw new IllegalArgumentException(String.format("Channel %d is invalid; valid channels are 1..%d", var2));
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
      return this.read(72, 1)[0] == -1?ServoController.PwmStatus.DISABLED:ServoController.PwmStatus.ENABLED;
   }

   public double getServoPosition(int var1) {
      this.a(var1);
      return TypeConversion.unsignedByteToDouble(this.read(ADDRESS_CHANNEL_MAP[var1], 1)[0]) / 255.0D;
   }

   public void pwmDisable() {
      this.write(72, (byte)-1);
   }

   public void pwmEnable() {
      this.write(72, (byte)0);
   }

   public void setServoPosition(int var1, double var2) {
      this.a(var1);
      Range.throwIfRangeIsInvalid(var2, 0.0D, 1.0D);
      this.write(ADDRESS_CHANNEL_MAP[var1], 255.0D * var2);
      this.pwmEnable();
   }
}
