package com.qualcomm.hardware;

import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.TypeConversion;

import java.util.Arrays;

public class MatrixServoController implements ServoController {
   public static final int SERVO_POSITION_MAX = 240;
   protected ServoController.PwmStatus pwmStatus;
   protected double[] servoCache = new double[4];
   private MatrixMasterController a;

   public MatrixServoController(MatrixMasterController var1) {
      this.a = var1;
      this.pwmStatus = ServoController.PwmStatus.DISABLED;
      Arrays.fill(this.servoCache, 0.0D);
      var1.registerServoController(this);
   }

   private void a(int var1) {
      if(var1 < 1 || var1 > 4) {
         Object[] var2 = new Object[]{Integer.valueOf(var1), Byte.valueOf((byte)4)};
         throw new IllegalArgumentException(String.format("Channel %d is invalid; valid channels are 1..%d", var2));
      }
   }

   public void close() {
      this.pwmDisable();
   }

   public String getConnectionInfo() {
      return this.a.getConnectionInfo();
   }

   public String getDeviceName() {
      return "Matrix Servo Controller";
   }

   public ServoController.PwmStatus getPwmStatus() {
      return this.pwmStatus;
   }

   public double getServoPosition(int var1) {
      MatrixI2cTransaction var2 = new MatrixI2cTransaction((byte) var1, MatrixI2cTransaction.Type1.g);
      if(this.a.queueTransaction(var2)) {
         this.a.waitOnRead();
      }

      return this.servoCache[var1] / 240.0D;
   }

   public int getVersion() {
      return 1;
   }

   public void handleReadServo(MatrixI2cTransaction var1, byte[] var2) {
      this.servoCache[var1.servo] = (double)TypeConversion.unsignedByteToInt(var2[4]);
   }

   public void pwmDisable() {
      MatrixI2cTransaction var1 = new MatrixI2cTransaction((byte) 0, MatrixI2cTransaction.Type1.h, 0);
      this.a.queueTransaction(var1);
      this.pwmStatus = ServoController.PwmStatus.DISABLED;
   }

   public void pwmEnable() {
      MatrixI2cTransaction var1 = new MatrixI2cTransaction((byte) 0, MatrixI2cTransaction.Type1.h, 15);
      this.a.queueTransaction(var1);
      this.pwmStatus = ServoController.PwmStatus.ENABLED;
   }

   public void setServoPosition(int var1, double var2) {
      this.a(var1);
      Range.throwIfRangeIsInvalid(var2, 0.0D, 1.0D);
      byte var4 = (byte)((int)(240.0D * var2));
      MatrixI2cTransaction var5 = new MatrixI2cTransaction((byte)var1, var4, (byte)0);
      this.a.queueTransaction(var5);
   }

   public void setServoPosition(int var1, double var2, byte var4) {
      this.a(var1);
      Range.throwIfRangeIsInvalid(var2, 0.0D, 1.0D);
      byte var5 = (byte)((int)(240.0D * var2));
      MatrixI2cTransaction var6 = new MatrixI2cTransaction((byte)var1, var5, var4);
      this.a.queueTransaction(var6);
   }
}
