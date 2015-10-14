package com.qualcomm.hardware.hardware;

import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.util.concurrent.locks.Lock;

public class HiTechnicNxtServoController implements I2cController.I2cPortReadyCallback, ServoController {
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
   public static final byte[] OFFSET_SERVO_MAP = new byte[]{(byte)-1, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8, (byte)9};
   public static final int OFFSET_UNUSED = -1;
   public static final byte PWM_DISABLE = -1;
   public static final byte PWM_ENABLE = 0;
   public static final byte PWM_ENABLE_WITHOUT_TIMEOUT = -86;
   public static final int SERVO_POSITION_MAX = 255;
   private final ModernRoboticsUsbLegacyModule a;
   private final byte[] b;
   private final Lock c;
   private final int d;
   private ElapsedTime e = new ElapsedTime(0L);
   private volatile boolean f = true;

   public HiTechnicNxtServoController(ModernRoboticsUsbLegacyModule var1, int var2) {
      this.a = var1;
      this.d = var2;
      this.b = var1.getI2cWriteCache(var2);
      this.c = var1.getI2cWriteCacheLock(var2);
      var1.enableI2cWriteMode(var2, 2, 66, 7);
      this.pwmDisable();
      var1.setI2cPortActionFlag(var2);
      var1.writeI2cCacheToController(var2);
      var1.registerForI2cPortReadyCallback(this, var2);
   }

   private void a(int var1) {
      if(var1 < 1 || var1 > OFFSET_SERVO_MAP.length) {
         Object[] var2 = new Object[]{Integer.valueOf(var1), Integer.valueOf(6)};
         throw new IllegalArgumentException(String.format("Channel %d is invalid; valid channels are 1..%d", var2));
      }
   }

   public void close() {
      this.pwmDisable();
   }

   public String getConnectionInfo() {
      return this.a.getConnectionInfo() + "; port " + this.d;
   }

   public String getDeviceName() {
      return "NXT Servo Controller";
   }

   public ServoController.PwmStatus getPwmStatus() {
      return ServoController.PwmStatus.DISABLED;
   }

   public double getServoPosition(int var1) {
      return 0.0D;
   }

   public int getVersion() {
      return 1;
   }

   public void portIsReady(int var1) {
      if(this.f || this.e.time() > 5.0D) {
         this.a.setI2cPortActionFlag(this.d);
         this.a.writeI2cCacheToController(this.d);
         this.e.reset();
      }

      this.f = false;
   }

   public void pwmDisable() {
      try {
         this.c.lock();
         if(-1 != this.b[10]) {
            this.b[10] = -1;
            this.f = true;
         }
      } finally {
         this.c.unlock();
      }

   }

   public void pwmEnable() {
      try {
         this.c.lock();
         if(this.b[10] != 0) {
            this.b[10] = 0;
            this.f = true;
         }
      } finally {
         this.c.unlock();
      }

   }

   public void setServoPosition(int var1, double var2) {
      this.a(var1);
      Range.throwIfRangeIsInvalid(var2, 0.0D, 1.0D);
      byte var4 = (byte)((int)(255.0D * var2));

      try {
         this.c.lock();
         if(var4 != this.b[OFFSET_SERVO_MAP[var1]]) {
            this.f = true;
            this.b[OFFSET_SERVO_MAP[var1]] = var4;
            this.b[10] = 0;
         }
      } finally {
         this.c.unlock();
      }

   }
}
