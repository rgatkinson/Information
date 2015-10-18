package com.qualcomm.hardware;

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
   private final ModernRoboticsUsbLegacyModule usbLegacyModule;
   private final byte[] writeCache;
   private final Lock writeCacheLock;
   private final int port;
   private ElapsedTime elapsed = new ElapsedTime(0L);
   private volatile boolean writeCacheDirty = true;

   public HiTechnicNxtServoController(ModernRoboticsUsbLegacyModule usbLegacyModule, int port) {
      this.usbLegacyModule = usbLegacyModule;
      this.port = port;
      this.writeCache = usbLegacyModule.getI2cWriteCache(port);
      this.writeCacheLock = usbLegacyModule.getI2cWriteCacheLock(port);
      usbLegacyModule.enableI2cWriteMode(port, 2, 66, 7);
      this.pwmDisable();
      usbLegacyModule.setI2cPortActionFlag(port);
      usbLegacyModule.writeI2cCacheToController(port);
      usbLegacyModule.registerForI2cPortReadyCallback(this, port);
   }

   private void validateServo(int servo) {
      if(servo < 1 || servo > OFFSET_SERVO_MAP.length) {
         Object[] var2 = new Object[]{Integer.valueOf(servo), Integer.valueOf(6)};
         throw new IllegalArgumentException(String.format("Channel %d is invalid; valid channels are 1..%d", var2));
      }
   }

   public void close() {
      this.pwmDisable();
   }

   public String getConnectionInfo() {
      return this.usbLegacyModule.getConnectionInfo() + "; port " + this.port;
   }

   public String getDeviceName() {
      return "NXT Servo Controller";
   }

   public ServoController.PwmStatus getPwmStatus() {
      return ServoController.PwmStatus.DISABLED;
   }

   public double getServoPosition(int servo) {
      return 0.0D;
   }

   public int getVersion() {
      return 1;
   }

   public void portIsReady(int port) {
      if(this.writeCacheDirty || this.elapsed.time() > 5.0D) {
         this.usbLegacyModule.setI2cPortActionFlag(this.port);
         this.usbLegacyModule.writeI2cCacheToController(this.port);
         this.elapsed.reset();
      }

      this.writeCacheDirty = false;
   }

   public void pwmDisable() {
      try {
         this.writeCacheLock.lock();
         if(PWM_DISABLE != this.writeCache[OFFSET_PWM]) {
            this.writeCache[OFFSET_PWM] = PWM_DISABLE;
            this.writeCacheDirty = true;
         }
      } finally {
         this.writeCacheLock.unlock();
      }

   }

   public void pwmEnable() {
      try {
         this.writeCacheLock.lock();
         if(this.writeCache[OFFSET_PWM] != PWM_ENABLE) {
            this.writeCache[OFFSET_PWM] = PWM_ENABLE;
            this.writeCacheDirty = true;
         }
      } finally {
         this.writeCacheLock.unlock();
      }

   }

   public void setServoPosition(int servo, double position) {
      this.validateServo(servo);
      Range.throwIfRangeIsInvalid(position, 0.0D, 1.0D);
      byte var4 = (byte)((int)(255.0D * position));

      try {
         this.writeCacheLock.lock();
         if(var4 != this.writeCache[OFFSET_SERVO_MAP[servo]]) {
            this.writeCacheDirty = true;
            this.writeCache[OFFSET_SERVO_MAP[servo]] = var4;
            this.writeCache[OFFSET_PWM] = PWM_ENABLE;
         }
      } finally {
         this.writeCacheLock.unlock();
      }

   }
}
