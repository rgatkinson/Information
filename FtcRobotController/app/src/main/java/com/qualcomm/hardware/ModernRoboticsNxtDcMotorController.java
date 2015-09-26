package com.qualcomm.hardware;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.TypeConversion;
import java.util.concurrent.locks.Lock;

public class ModernRoboticsNxtDcMotorController implements DcMotorController, I2cController.I2cPortReadyCallback {
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
   public static final byte[] OFFSET_MAP_MOTOR_CURRENT_ENCODER_VALUE = new byte[]{(byte)-1, (byte)16, (byte)20};
   public static final byte[] OFFSET_MAP_MOTOR_MODE = new byte[]{(byte)-1, (byte)8, (byte)11};
   public static final byte[] OFFSET_MAP_MOTOR_POWER = new byte[]{(byte)-1, (byte)9, (byte)10};
   public static final byte[] OFFSET_MAP_MOTOR_TARGET_ENCODER_VALUE = new byte[]{(byte)-1, (byte)4, (byte)12};
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
   public static final byte POWER_FLOAT = -128;
   public static final byte POWER_MAX = 100;
   public static final byte POWER_MIN = -100;
   private final ModernRoboticsUsbLegacyModule legacyModule;
   private final byte[] readCache;
   private final Lock readCacheLock;
   private final byte[] writeCache;
   private final Lock writeCacheLock;
   private final int port;
   private final ElapsedTime elapsedTime = new ElapsedTime(0L);
   private volatile DcMotorController.DeviceMode deviceMode;
   private volatile boolean writeCacheIsDirty = true;

   public ModernRoboticsNxtDcMotorController(ModernRoboticsUsbLegacyModule legacyModule, int port) {
      this.legacyModule    = legacyModule;
      this.port            = port;
      this.readCache       = legacyModule.getI2cReadCache(port);
      this.readCacheLock   = legacyModule.getI2cReadCacheLock(port);
      this.writeCache      = legacyModule.getI2cWriteCache(port);
      this.writeCacheLock  = legacyModule.getI2cWriteCacheLock(port);
      this.deviceMode      = DcMotorController.DeviceMode.WRITE_ONLY;
      legacyModule.enableI2cWriteMode(port, 2, 64, 20);

      try {
         this.writeCacheLock.lock();
         this.writeCache[9] = -128;
         this.writeCache[10] = -128;
      } finally {
         this.writeCacheLock.unlock();
      }

      legacyModule.writeI2cCacheToController(port);
      legacyModule.registerForI2cPortReadyCallback(this, port);
   }

   private void checkCanWrite() {
      if(this.deviceMode != DcMotorController.DeviceMode.SWITCHING_TO_WRITE_MODE && (this.deviceMode == DcMotorController.DeviceMode.READ_ONLY || this.deviceMode == DcMotorController.DeviceMode.SWITCHING_TO_READ_MODE)) {
         String var1 = "Cannot write while in this mode: " + this.deviceMode;
         StackTraceElement[] var2 = Thread.currentThread().getStackTrace();
         if(var2 != null && var2.length > 3) {
            var1 = var1 + "\n from method: " + var2[3].getMethodName();
         }

         throw new IllegalArgumentException(var1);
      }
   }

   private void validateMotor(int motor) {
      if(motor < 1 || motor > 2) {
         Object[] var2 = new Object[]{Integer.valueOf(motor), Integer.valueOf(2)};
         throw new IllegalArgumentException(String.format("Motor %d is invalid; valid motors are 1..%d", var2));
      }
   }

   private void checkCanRead() {
      if(this.deviceMode != DcMotorController.DeviceMode.SWITCHING_TO_READ_MODE && (this.deviceMode == DcMotorController.DeviceMode.WRITE_ONLY || this.deviceMode == DcMotorController.DeviceMode.SWITCHING_TO_WRITE_MODE)) {
         String var1 = "Cannot read while in this mode: " + this.deviceMode;
         StackTraceElement[] var2 = Thread.currentThread().getStackTrace();
         if(var2 != null && var2.length > 3) {
            var1 = var1 + "\n from method: " + var2[3].getMethodName();
         }

         throw new IllegalArgumentException(var1);
      }
   }

   public static DcMotorController.RunMode flagToRunModeNXT(byte flagByte) {
      switch(flagByte & 3) {
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

   public static byte runModeToFlagNXT(DcMotorController.RunMode mode) {
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
      if(this.deviceMode == DcMotorController.DeviceMode.WRITE_ONLY) {
         this.setMotorPowerFloat(1);
         this.setMotorPowerFloat(2);
      }

   }

   public String getConnectionInfo() {
      return this.legacyModule.getConnectionInfo() + "; port " + this.port;
   }

   public String getDeviceName() {
      return "NXT DC Motor Controller";
   }

   public DcMotorController.RunMode getMotorChannelMode(int motor) {
      this.validateMotor(motor);
      this.checkCanRead();

      byte channelMode;
      try {
         this.readCacheLock.lock();
         channelMode = this.readCache[OFFSET_MAP_MOTOR_MODE[motor]];
      } finally {
         this.readCacheLock.unlock();
      }

      return flagToRunModeNXT(channelMode);
   }

   public DcMotorController.DeviceMode getMotorControllerDeviceMode() {
      return this.deviceMode;
   }

   public int getMotorCurrentPosition(int var1) {
      this.validateMotor(var1);
      this.checkCanRead();
      byte[] resultBytes = new byte[4];

      try {
         this.readCacheLock.lock();
         System.arraycopy(this.readCache, OFFSET_MAP_MOTOR_CURRENT_ENCODER_VALUE[var1], resultBytes, 0, resultBytes.length);
      } finally {
         this.readCacheLock.unlock();
      }

      return TypeConversion.byteArrayToInt(resultBytes);
   }

   public double getMotorPower(int var1) {
      this.validateMotor(var1);
      this.checkCanRead();

      byte motorPower;
      try {
         this.readCacheLock.lock();
         motorPower = this.readCache[OFFSET_MAP_MOTOR_POWER[var1]];
      } finally {
         this.readCacheLock.unlock();
      }

      return motorPower == -128?0.0D:(double)motorPower / 100.0D;
   }

   public boolean getMotorPowerFloat(int var1) {
      this.validateMotor(var1);
      this.checkCanRead();
      boolean locked = false;

      byte motorPower;
      try {
         locked = true;
         this.readCacheLock.lock();
         motorPower = this.readCache[OFFSET_MAP_MOTOR_POWER[var1]];
         locked = false;
      } finally {
         if(locked) {
            this.readCacheLock.unlock();
         }
      }

      boolean result;
      if(motorPower == -128) {
         result = true;
      } else {
         result = false;
      }

      this.readCacheLock.unlock();
      return result;
   }

   public int getMotorTargetPosition(int motor) {
      this.validateMotor(motor);
      this.checkCanRead();
      byte[] resultBytes = new byte[4];

      try {
         this.readCacheLock.lock();
         System.arraycopy(this.readCache, OFFSET_MAP_MOTOR_TARGET_ENCODER_VALUE[motor], resultBytes, 0, resultBytes.length);
      } finally {
         this.readCacheLock.unlock();
      }

      return TypeConversion.byteArrayToInt(resultBytes);
   }

   public int getVersion() {
      return 1;
   }

   public boolean isBusy(int motor) {
      this.validateMotor(motor);
      this.checkCanRead();
      boolean locked = false;

      byte channelMode;
      try {
         locked = true;
         this.readCacheLock.lock();
         channelMode = this.readCache[OFFSET_MAP_MOTOR_MODE[motor]];
         locked = false;
      } finally {
         if(locked) {
            this.readCacheLock.unlock();
         }
      }

      boolean result;
      if((channelMode & 128) == 128) {
         result = true;
      } else {
         result = false;
      }

      this.readCacheLock.unlock();
      return result;
   }

   public void portIsReady(int port) {
      switch(this.deviceMode.ordinal()) {
      case 3:
         if(this.legacyModule.isI2cPortInReadMode(port)) {
            this.deviceMode = DcMotorController.DeviceMode.READ_ONLY;
         }
         break;
      case 4:
         if(this.legacyModule.isI2cPortInWriteMode(port)) {
            this.deviceMode = DcMotorController.DeviceMode.WRITE_ONLY;
         }
      }

      if(this.deviceMode == DcMotorController.DeviceMode.READ_ONLY) {
         this.legacyModule.setI2cPortActionFlag(this.port);
         this.legacyModule.writeI2cPortFlagOnlyToController(this.port);
      } else {
         if(this.writeCacheIsDirty || this.elapsedTime.time() > 2.0D) {
            this.legacyModule.setI2cPortActionFlag(this.port);
            this.legacyModule.writeI2cCacheToController(this.port);
            this.elapsedTime.reset();
         }

         this.writeCacheIsDirty = false;
      }

      this.legacyModule.readI2cCacheFromController(this.port);
   }

   public void setMotorChannelMode(int motor, DcMotorController.RunMode mode) {
      this.validateMotor(motor);
      this.checkCanWrite();
      byte channelMode = runModeToFlagNXT(mode);
      //
      // NB: Never uses the motor controller 'rev' bit, instead choosing to
      // do that at the DCMotor level
      //
      try {
         this.writeCacheLock.lock();
         if(this.writeCache[OFFSET_MAP_MOTOR_MODE[motor]] != channelMode) {
            this.writeCache[OFFSET_MAP_MOTOR_MODE[motor]] = channelMode;
            this.writeCacheIsDirty = true;
         }
      } finally {
         this.writeCacheLock.unlock();
      }

   }

   public void setMotorControllerDeviceMode(DcMotorController.DeviceMode mode) {
      if(this.deviceMode != mode) {
         switch(mode.ordinal()) {
         case 1:
            this.deviceMode = DcMotorController.DeviceMode.SWITCHING_TO_READ_MODE;
            this.legacyModule.enableI2cReadMode(this.port, 2, 64, 20);
            break;
         case 2:
            this.deviceMode = DcMotorController.DeviceMode.SWITCHING_TO_WRITE_MODE;
            this.legacyModule.enableI2cWriteMode(this.port, 2, 64, 20);
         }

         this.writeCacheIsDirty = true;
      }
   }

   public void setMotorPower(int motor, double power) {
      this.validateMotor(motor);
      this.checkCanWrite();
      Range.throwIfRangeIsInvalid(power, -1.0D, 1.0D);
      byte var4 = (byte)((int)(100.0D * power));

      try {
         this.writeCacheLock.lock();
         if(var4 != this.writeCache[OFFSET_MAP_MOTOR_POWER[motor]]) {
            this.writeCache[OFFSET_MAP_MOTOR_POWER[motor]] = var4;
            this.writeCacheIsDirty = true;
         }
      } finally {
         this.writeCacheLock.unlock();
      }

   }

   public void setMotorPowerFloat(int motor) {
      this.validateMotor(motor);
      this.checkCanWrite();

      try {
         this.writeCacheLock.lock();
         if(-128 != this.writeCache[OFFSET_MAP_MOTOR_POWER[motor]]) {
            this.writeCache[OFFSET_MAP_MOTOR_POWER[motor]] = -128;
            this.writeCacheIsDirty = true;
         }
      } finally {
         this.writeCacheLock.unlock();
      }

   }

   public void setMotorTargetPosition(int motor, int position) {
      this.validateMotor(motor);
      this.checkCanWrite();
      byte[] var3 = TypeConversion.intToByteArray(position);

      try {
         this.writeCacheLock.lock();
         System.arraycopy(var3, 0, this.writeCache, OFFSET_MAP_MOTOR_TARGET_ENCODER_VALUE[motor], var3.length);
         this.writeCacheIsDirty = true;
      } finally {
         this.writeCacheLock.unlock();
      }

   }
}
