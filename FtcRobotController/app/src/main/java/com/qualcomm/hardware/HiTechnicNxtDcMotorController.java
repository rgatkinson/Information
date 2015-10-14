package com.qualcomm.hardware;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.TypeConversion;

import java.util.concurrent.locks.Lock;

public class HiTechnicNxtDcMotorController implements DcMotorController, I2cController.I2cPortReadyCallback {
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
   private final ModernRoboticsUsbLegacyModule a;
   private final byte[] b;
   private final Lock c;
   private final byte[] d;
   private final Lock e;
   private final int f;
   private final ElapsedTime g = new ElapsedTime(0L);
   private volatile DcMotorController.DeviceMode h;
   private volatile boolean i = true;

   public HiTechnicNxtDcMotorController(ModernRoboticsUsbLegacyModule var1, int var2) {
      this.a = var1;
      this.f = var2;
      this.b = var1.getI2cReadCache(var2);
      this.c = var1.getI2cReadCacheLock(var2);
      this.d = var1.getI2cWriteCache(var2);
      this.e = var1.getI2cWriteCacheLock(var2);
      this.h = DcMotorController.DeviceMode.WRITE_ONLY;
      var1.enableI2cWriteMode(var2, 2, 64, 20);

      try {
         this.e.lock();
         this.d[9] = -128;
         this.d[10] = -128;
      } finally {
         this.e.unlock();
      }

      var1.writeI2cCacheToController(var2);
      var1.registerForI2cPortReadyCallback(this, var2);
   }

   public static DcMotorController.RunMode flagToRunModeNXT(byte var0) {
      switch (var0 & 3) {
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

   public static byte runModeToFlagNXT(DcMotorController.RunMode var0) {
      switch (var0.ordinal()) {
         case 1:
         default:
            return (byte) 1;
         case 2:
            return (byte) 0;
         case 3:
            return (byte) 2;
         case 4:
            return (byte) 3;
      }
   }

   private void a() {
      if(this.h != DcMotorController.DeviceMode.SWITCHING_TO_WRITE_MODE && (this.h == DcMotorController.DeviceMode.READ_ONLY || this.h == DcMotorController.DeviceMode.SWITCHING_TO_READ_MODE)) {
         String var1 = "Cannot write while in this mode: " + this.h;
         StackTraceElement[] var2 = Thread.currentThread().getStackTrace();
         if(var2 != null && var2.length > 3) {
            var1 = var1 + "\n from method: " + var2[3].getMethodName();
         }

         throw new IllegalArgumentException(var1);
      }
   }

   private void a(int var1) {
      if(var1 < 1 || var1 > 2) {
         Object[] var2 = new Object[]{Integer.valueOf(var1), Integer.valueOf(2)};
         throw new IllegalArgumentException(String.format("Motor %d is invalid; valid motors are 1..%d", var2));
      }
   }

   private void b() {
      if(this.h != DcMotorController.DeviceMode.SWITCHING_TO_READ_MODE && (this.h == DcMotorController.DeviceMode.WRITE_ONLY || this.h == DcMotorController.DeviceMode.SWITCHING_TO_WRITE_MODE)) {
         String var1 = "Cannot read while in this mode: " + this.h;
         StackTraceElement[] var2 = Thread.currentThread().getStackTrace();
         if(var2 != null && var2.length > 3) {
            var1 = var1 + "\n from method: " + var2[3].getMethodName();
         }

         throw new IllegalArgumentException(var1);
      }
   }

   public void close() {
      if(this.h == DcMotorController.DeviceMode.WRITE_ONLY) {
         this.setMotorPowerFloat(1);
         this.setMotorPowerFloat(2);
      }

   }

   public String getConnectionInfo() {
      return this.a.getConnectionInfo() + "; port " + this.f;
   }

   public String getDeviceName() {
      return "NXT DC Motor Controller";
   }

   public DcMotorController.RunMode getMotorChannelMode(int var1) {
      this.a(var1);
      this.b();

      byte var3;
      try {
         this.c.lock();
         var3 = this.b[OFFSET_MAP_MOTOR_MODE[var1]];
      } finally {
         this.c.unlock();
      }

      return flagToRunModeNXT(var3);
   }

   public DcMotorController.DeviceMode getMotorControllerDeviceMode() {
      return this.h;
   }

   public void setMotorControllerDeviceMode(DcMotorController.DeviceMode var1) {
      if (this.h != var1) {
         switch (var1.ordinal()) {
            case 1:
               this.h = DcMotorController.DeviceMode.SWITCHING_TO_READ_MODE;
               this.a.enableI2cReadMode(this.f, 2, 64, 20);
               break;
            case 2:
               this.h = DcMotorController.DeviceMode.SWITCHING_TO_WRITE_MODE;
               this.a.enableI2cWriteMode(this.f, 2, 64, 20);
         }

         this.i = true;
      }
   }

   public int getMotorCurrentPosition(int var1) {
      this.a(var1);
      this.b();
      byte[] var2 = new byte[4];

      try {
         this.c.lock();
         System.arraycopy(this.b, OFFSET_MAP_MOTOR_CURRENT_ENCODER_VALUE[var1], var2, 0, var2.length);
      } finally {
         this.c.unlock();
      }

      return TypeConversion.byteArrayToInt(var2);
   }

   public double getMotorPower(int var1) {
      this.a(var1);
      this.b();

      byte var3;
      try {
         this.c.lock();
         var3 = this.b[OFFSET_MAP_MOTOR_POWER[var1]];
      } finally {
         this.c.unlock();
      }

      return var3 == -128?0.0D:(double)var3 / 100.0D;
   }

   public boolean getMotorPowerFloat(int var1) {
      this.a(var1);
      this.b();
      boolean var6 = false;

      byte var3;
      try {
         var6 = true;
         this.c.lock();
         var3 = this.b[OFFSET_MAP_MOTOR_POWER[var1]];
         var6 = false;
      } finally {
         if(var6) {
            this.c.unlock();
         }
      }

      boolean var4;
      var4 = var3 == -128;

      this.c.unlock();
      return var4;
   }

   public int getMotorTargetPosition(int var1) {
      this.a(var1);
      this.b();
      byte[] var2 = new byte[4];

      try {
         this.c.lock();
         System.arraycopy(this.b, OFFSET_MAP_MOTOR_TARGET_ENCODER_VALUE[var1], var2, 0, var2.length);
      } finally {
         this.c.unlock();
      }

      return TypeConversion.byteArrayToInt(var2);
   }

   public int getVersion() {
      return 1;
   }

   public boolean isBusy(int var1) {
      this.a(var1);
      this.b();
      boolean var6 = false;

      byte var3;
      try {
         var6 = true;
         this.c.lock();
         var3 = this.b[OFFSET_MAP_MOTOR_MODE[var1]];
         var6 = false;
      } finally {
         if(var6) {
            this.c.unlock();
         }
      }

      boolean var4;
      var4 = (var3 & 128) == 128;

      this.c.unlock();
      return var4;
   }

   public void portIsReady(int var1) {
      switch (this.h.ordinal()) {
      case 3:
         if(this.a.isI2cPortInReadMode(var1)) {
            this.h = DcMotorController.DeviceMode.READ_ONLY;
         }
         break;
      case 4:
         if(this.a.isI2cPortInWriteMode(var1)) {
            this.h = DcMotorController.DeviceMode.WRITE_ONLY;
         }
      }

      if(this.h == DcMotorController.DeviceMode.READ_ONLY) {
         this.a.setI2cPortActionFlag(this.f);
         this.a.writeI2cPortFlagOnlyToController(this.f);
      } else {
         if(this.i || this.g.time() > 2.0D) {
            this.a.setI2cPortActionFlag(this.f);
            this.a.writeI2cCacheToController(this.f);
            this.g.reset();
         }

         this.i = false;
      }

      this.a.readI2cCacheFromController(this.f);
   }

   public void setMotorChannelMode(int var1, DcMotorController.RunMode var2) {
      this.a(var1);
      this.a();
      byte var3 = runModeToFlagNXT(var2);

      try {
         this.e.lock();
         if(this.d[OFFSET_MAP_MOTOR_MODE[var1]] != var3) {
            this.d[OFFSET_MAP_MOTOR_MODE[var1]] = var3;
            this.i = true;
         }
      } finally {
         this.e.unlock();
      }

   }

   public void setMotorPower(int var1, double var2) {
      this.a(var1);
      this.a();
      Range.throwIfRangeIsInvalid(var2, -1.0D, 1.0D);
      byte var4 = (byte)((int)(100.0D * var2));

      try {
         this.e.lock();
         if(var4 != this.d[OFFSET_MAP_MOTOR_POWER[var1]]) {
            this.d[OFFSET_MAP_MOTOR_POWER[var1]] = var4;
            this.i = true;
         }
      } finally {
         this.e.unlock();
      }

   }

   public void setMotorPowerFloat(int var1) {
      this.a(var1);
      this.a();

      try {
         this.e.lock();
         if(-128 != this.d[OFFSET_MAP_MOTOR_POWER[var1]]) {
            this.d[OFFSET_MAP_MOTOR_POWER[var1]] = -128;
            this.i = true;
         }
      } finally {
         this.e.unlock();
      }

   }

   public void setMotorTargetPosition(int var1, int var2) {
      this.a(var1);
      this.a();
      byte[] var3 = TypeConversion.intToByteArray(var2);

      try {
         this.e.lock();
         System.arraycopy(var3, 0, this.d, OFFSET_MAP_MOTOR_TARGET_ENCODER_VALUE[var1], var3.length);
         this.i = true;
      } finally {
         this.e.unlock();
      }

   }
}
