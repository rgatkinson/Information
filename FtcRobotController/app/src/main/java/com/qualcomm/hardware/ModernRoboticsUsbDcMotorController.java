package com.qualcomm.hardware;

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.DifferentialControlLoopCoefficients;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.TypeConversion;

public class ModernRoboticsUsbDcMotorController extends ModernRoboticsUsbDevice implements DcMotorController, VoltageSensor {
   public static final int ADDRESS_BATTERY_VOLTAGE = 84;
   public static final int[] ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP = new int[]{255, 87, 91};
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
   public static final int[] ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP = new int[]{255, 76, 80};
   public static final int[] ADDRESS_MOTOR_GEAR_RATIO_MAP = new int[]{255, 86, 90};
   public static final int[] ADDRESS_MOTOR_MODE_MAP = new int[]{255, 68, 71};
   public static final int[] ADDRESS_MOTOR_POWER_MAP = new int[]{255, 69, 70};
   public static final int[] ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP = new int[]{255, 64, 72};
   public static final int ADDRESS_UNUSED = 255;
   public static final double BATTERY_MAX_MEASURABLE_VOLTAGE = 20.4D;
   public static final int BATTERY_MAX_MEASURABLE_VOLTAGE_INT = 1023;
   public static final byte CHANNEL_MODE_FLAG_BUSY = -128;
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
   public static final byte DEFAULT_P_COEFFICIENT = -128;
   public static final int DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAX = 255;
   public static final int MAX_MOTOR = 2;
   public static final int MIN_MOTOR = 1;
   public static final int MONITOR_LENGTH = 30;
   public static final byte POWER_BREAK = 0;
   public static final byte POWER_FLOAT = -128;
   public static final byte POWER_MAX = 100;
   public static final byte POWER_MIN = -100;
   public static final byte RATIO_MAX = 127;
   public static final byte RATIO_MIN = -128;
   public static final byte START_ADDRESS = 64;
   private IsBusyHelper[] isBusyHelpers;

   protected ModernRoboticsUsbDcMotorController(SerialNumber var1, RobotUsbDevice var2, EventLoopManager var3) throws RobotCoreException, InterruptedException {
      super(var1, var3, new ReadWriteRunnableBlocking(var1, var2, 30, 64, false));
      this.isBusyHelpers = new IsBusyHelper[3];
      this.readWriteRunnable.setCallback(this);

      int index = 0;
      while(index < this.isBusyHelpers.length) {
         this.isBusyHelpers[index] = new IsBusyHelper(null);
         ++index;
      }

      this.floatMotors();
      this.initializePID();
   }

   private void floatMotors() {
      this.setMotorPowerFloat(1);
      this.setMotorPowerFloat(2);
   }

   private void validateMotor(int motor) {
      if(motor < 1 || motor > 2) {
         Object[] var2 = new Object[]{Integer.valueOf(motor), Integer.valueOf(2)};
         throw new IllegalArgumentException(String.format("Motor %d is invalid; valid motors are 1..%d", var2));
      }
   }

   private void initializePID() {
      for(int motor = 1; motor <= 2; ++motor) {
         this.write(ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP[motor], new byte[]{(byte)-128, (byte)64, (byte)-72});
      }

   }

   public static DcMotorController.RunMode flagToRunMode(byte grf) {
      switch(grf & 3) {
      case 0:
         return DcMotorController.RunMode.RUN_WITHOUT_ENCODERS;
      case 1:
         return DcMotorController.RunMode.RUN_USING_ENCODERS;
      case 2:
         return DcMotorController.RunMode.RUN_TO_POSITION;
      case 3:
         return DcMotorController.RunMode.RESET_ENCODERS;
      default:
         return DcMotorController.RunMode.RUN_WITHOUT_ENCODERS;
      }
   }

   public static byte runModeToFlag(DcMotorController.RunMode mode) {
      switch(mode.ordinal()) {
      case 1:
      default:
         return (byte)1;
      case 2:
         return (byte)0;
      case 3:
         return (byte)2;
      case 4:
         return (byte)3;
      }
   }

   public void close() {
      this.floatMotors();
      super.close();
   }

   public String getConnectionInfo() {
      return "USB " + this.getSerialNumber();
   }

   public String getDeviceName() {
      return "Modern Robotics USB DC Motor Controller";
   }

   public DifferentialControlLoopCoefficients getDifferentialControlLoopCoefficients(int motor) {
      this.validateMotor(motor);
      DifferentialControlLoopCoefficients coeffs = new DifferentialControlLoopCoefficients();
      byte[] data = this.read(ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP[motor], 3);
      coeffs.p = (double)data[0];
      coeffs.i = (double)data[1];
      coeffs.d = (double)data[2];
      return coeffs;
   }

   public double getGearRatio(int motor) {
      this.validateMotor(motor);
      return (double)this.read(ADDRESS_MOTOR_GEAR_RATIO_MAP[motor], 1)[0] / 127.0D;
   }

   public DcMotorController.RunMode getMotorChannelMode(int motor) {
      this.validateMotor(motor);
      return flagToRunMode(this.read(ADDRESS_MOTOR_MODE_MAP[motor]));
   }

   public DcMotorController.DeviceMode getMotorControllerDeviceMode() {
      return DcMotorController.DeviceMode.READ_WRITE;
   }

   public int getMotorCurrentPosition(int motor) {
      this.validateMotor(motor);
      return TypeConversion.byteArrayToInt(this.read(ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP[motor], 4));
   }

   public double getMotorPower(int motor) {
      this.validateMotor(motor);
      byte var2 = this.read(ADDRESS_MOTOR_POWER_MAP[motor]);
      return var2 == -128?0.0D:(double)var2 / 100.0D;
   }

   public boolean getMotorPowerFloat(int motor) {
      this.validateMotor(motor);
      return this.read(ADDRESS_MOTOR_POWER_MAP[motor]) == -128;
   }

   public int getMotorTargetPosition(int motor) {
      this.validateMotor(motor);
      return TypeConversion.byteArrayToInt(this.read(ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP[motor], 4));
   }

   public double getVoltage() {
      return 20.4D * ((double)(1023 & TypeConversion.byteArrayToShort(this.read(84, 2)) >> 6) / 1023.0D);
   }

   public boolean isBusy(int motor) {
      this.validateMotor(motor);
      return this.isBusyHelpers[motor].isBusyHelper();
   }

   public void readComplete() throws InterruptedException {
      for(int motor = 1; motor <= 2; ++motor) {
         this.isBusyHelpers[motor].a(this.getMotorCurrentPosition(motor));
      }

   }

   public void setDifferentialControlLoopCoefficients(int motor, DifferentialControlLoopCoefficients var2) {
      this.validateMotor(motor);
      if(var2.p > 255.0D) {
         var2.p = 255.0D;
      }

      if(var2.i > 255.0D) {
         var2.i = 255.0D;
      }

      if(var2.d > 255.0D) {
         var2.d = 255.0D;
      }

      int var3 = ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP[motor];
      byte[] var4 = new byte[]{(byte)((int)var2.p), (byte)((int)var2.i), (byte)((int)var2.d)};
      this.write(var3, var4);
   }

   public void setGearRatio(int motor, double ratio) {
      this.validateMotor(motor);
      Range.throwIfRangeIsInvalid(ratio, -1.0D, 1.0D);
      int var4 = ADDRESS_MOTOR_GEAR_RATIO_MAP[motor];
      byte[] var5 = new byte[]{(byte)((int)(127.0D * ratio))};
      this.write(var4, var5);
   }

   public void setMotorChannelMode(int motor, DcMotorController.RunMode mode) {
      this.validateMotor(motor);
      byte var3 = runModeToFlag(mode);
      this.write(ADDRESS_MOTOR_MODE_MAP[motor], var3);
   }

   public void setMotorControllerDeviceMode(DcMotorController.DeviceMode var1) {
   }

   public void setMotorPower(int var1, double var2) {
      this.validateMotor(var1);
      Range.throwIfRangeIsInvalid(var2, -1.0D, 1.0D);
      int var4 = ADDRESS_MOTOR_POWER_MAP[var1];
      byte[] var5 = new byte[]{(byte)((int)(100.0D * var2))};
      this.write(var4, var5);
   }

   public void setMotorPowerFloat(int var1) {
      this.validateMotor(var1);
      this.write(ADDRESS_MOTOR_POWER_MAP[var1], new byte[]{(byte)-128});
   }

   public void setMotorTargetPosition(int var1, int var2) {
      this.validateMotor(var1);
      Range.throwIfRangeIsInvalid((double)var2, -2.147483648E9D, 2.147483647E9D);
      this.write(ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP[var1], TypeConversion.intToByteArray(var2));
   }

   private static class IsBusyHelper
      {
      private int[] a;
      private int[] b;
      private int c;

      private IsBusyHelper() {
         this.a = new int[3];
         this.b = new int[3];
         this.c = 0;
      }

      // $FF: synthetic method
      IsBusyHelper(Object var1) {
         this();
      }

      public void a(int var1) {
         int var2 = this.a[this.c];
         this.c = (1 + this.c) % this.a.length;
         this.b[this.c] = Math.abs(var2 - var1);
         this.a[this.c] = var1;
      }

      public boolean a() {
         int[] var1 = this.b;
         int var2 = var1.length;
         int var3 = 0;

         int var4;
         for(var4 = 0; var3 < var2; ++var3) {
            var4 += var1[var3];
         }

         boolean var5 = false;
         if(var4 > 6) {
            var5 = true;
         }

         return var5;
      }
   }
}
