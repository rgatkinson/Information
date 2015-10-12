package com.qualcomm.hardware;

import com.qualcomm.hardware.MatrixI2cTransaction;
import com.qualcomm.hardware.MatrixMasterController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.TypeConversion;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

public class MatrixDcMotorController implements DcMotorController {
   public static final byte POWER_MAX = 100;
   public static final byte POWER_MIN = -100;
   private MatrixDcMotorController.a[] a;
   private int b;
   protected DcMotorController.DeviceMode deviceMode;
   protected MatrixMasterController master;

   public MatrixDcMotorController(MatrixMasterController var1) {
      MatrixDcMotorController.a[] var2 = new MatrixDcMotorController.a[]{new MatrixDcMotorController.a(), new MatrixDcMotorController.a(), new MatrixDcMotorController.a(), new MatrixDcMotorController.a(), new MatrixDcMotorController.a()};
      this.a = var2;
      this.master = var1;
      this.b = 0;
      var1.registerMotorController(this);

      for(byte var3 = 0; var3 < 4; ++var3) {
         var1.queueTransaction(new MatrixI2cTransaction(var3, (byte)0, 0, (byte)0));
         this.a[var3].f = DcMotorController.RunMode.RUN_WITHOUT_ENCODERS;
         this.a[var3].d = true;
      }

      this.deviceMode = DcMotorController.DeviceMode.READ_ONLY;
   }

   private void a(int var1) {
      if(var1 < 1 || var1 > 4) {
         Object[] var2 = new Object[]{Integer.valueOf(var1), Integer.valueOf(4)};
         throw new IllegalArgumentException(String.format("Motor %d is invalid; valid motors are 1..%d", var2));
      }
   }

   public void close() {
      this.setMotorPowerFloat(1);
      this.setMotorPowerFloat(2);
      this.setMotorPowerFloat(3);
      this.setMotorPowerFloat(4);
   }

   protected DcMotorController.RunMode flagMatrixToRunMode(byte var1) {
      switch(var1) {
      case 1:
         return DcMotorController.RunMode.RUN_WITHOUT_ENCODERS;
      case 2:
         return DcMotorController.RunMode.RUN_USING_ENCODERS;
      case 3:
         return DcMotorController.RunMode.RUN_TO_POSITION;
      case 4:
         return DcMotorController.RunMode.RESET_ENCODERS;
      default:
         RobotLog.e("Invalid run mode flag " + var1);
         return DcMotorController.RunMode.RUN_WITHOUT_ENCODERS;
      }
   }

   public int getBattery() {
      MatrixI2cTransaction var1 = new MatrixI2cTransaction((byte)0, MatrixI2cTransaction.a.d);
      if(this.master.queueTransaction(var1)) {
         this.master.waitOnRead();
      }

      return this.b;
   }

   public String getConnectionInfo() {
      return this.master.getConnectionInfo();
   }

   public String getDeviceName() {
      return "Matrix Motor Controller";
   }

   public DcMotorController.RunMode getMotorChannelMode(int var1) {
      this.a(var1);
      return this.a[var1].f;
   }

   public DcMotorController.DeviceMode getMotorControllerDeviceMode() {
      return this.deviceMode;
   }

   public int getMotorCurrentPosition(int var1) {
      this.a(var1);
      MatrixI2cTransaction var2 = new MatrixI2cTransaction((byte)var1, MatrixI2cTransaction.a.e);
      if(this.master.queueTransaction(var2)) {
         this.master.waitOnRead();
      }

      return this.a[var1].b;
   }

   public double getMotorPower(int var1) {
      this.a(var1);
      return this.a[var1].e;
   }

   public boolean getMotorPowerFloat(int var1) {
      this.a(var1);
      return this.a[var1].d;
   }

   public int getMotorTargetPosition(int var1) {
      this.a(var1);
      MatrixI2cTransaction var2 = new MatrixI2cTransaction((byte)var1, MatrixI2cTransaction.a.b);
      if(this.master.queueTransaction(var2)) {
         this.master.waitOnRead();
      }

      return this.a[var1].a;
   }

   public int getVersion() {
      return 1;
   }

   public void handleReadBattery(byte[] var1) {
      this.b = 40 * TypeConversion.unsignedByteToInt(var1[4]);
      RobotLog.v("Battery voltage: " + this.b + "mV");
   }

   public void handleReadMode(MatrixI2cTransaction var1, byte[] var2) {
      this.a[var1.motor].c = var2[4];
      RobotLog.v("Mode: " + this.a[var1.motor].c);
   }

   public void handleReadPosition(MatrixI2cTransaction var1, byte[] var2) {
      this.a[var1.motor].b = TypeConversion.byteArrayToInt(Arrays.copyOfRange(var2, 4, 8));
      RobotLog.v("Position motor: " + var1.motor + " " + this.a[var1.motor].b);
   }

   public void handleReadTargetPosition(MatrixI2cTransaction var1, byte[] var2) {
      this.a[var1.motor].a = TypeConversion.byteArrayToInt(Arrays.copyOfRange(var2, 4, 8));
      RobotLog.v("Target motor: " + var1.motor + " " + this.a[var1.motor].a);
   }

   public boolean isBusy(int var1) {
      MatrixI2cTransaction var2 = new MatrixI2cTransaction((byte)var1, MatrixI2cTransaction.a.a);
      this.master.queueTransaction(var2);
      this.master.waitOnRead();
      return (128 & this.a[var2.motor].c) != 0;
   }

   protected byte runModeToFlagMatrix(DcMotorController.RunMode var1) {
      switch(null.a[var1.ordinal()]) {
      case 1:
         return (byte)2;
      case 2:
         return (byte)1;
      case 3:
         return (byte)3;
      case 4:
      default:
         return (byte)4;
      }
   }

   public void setMotorChannelMode(int var1, DcMotorController.RunMode var2) {
      this.a(var1);
      if(this.a[var1].d || this.a[var1].f != var2) {
         byte var3 = this.runModeToFlagMatrix(var2);
         MatrixI2cTransaction var4 = new MatrixI2cTransaction((byte)var1, MatrixI2cTransaction.a.a, var3);
         this.master.queueTransaction(var4);
         this.a[var1].f = var2;
         if(var2 == DcMotorController.RunMode.RESET_ENCODERS) {
            this.a[var1].d = true;
         } else {
            this.a[var1].d = false;
         }
      }
   }

   public void setMotorControllerDeviceMode(DcMotorController.DeviceMode var1) {
      this.deviceMode = var1;
   }

   public void setMotorPower(int var1, double var2) {
      this.a(var1);
      Range.throwIfRangeIsInvalid(var2, -1.0D, 1.0D);
      byte var4 = (byte)((int)(100.0D * var2));
      MatrixI2cTransaction var5 = new MatrixI2cTransaction((byte)var1, var4, this.a[var1].a, this.runModeToFlagMatrix(this.a[var1].f));
      this.master.queueTransaction(var5);
      this.a[var1].e = var2;
   }

   public void setMotorPower(Set<DcMotor> var1, double var2) {
      Range.throwIfRangeIsInvalid(var2, -1.0D, 1.0D);
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         DcMotor var7 = (DcMotor)var4.next();
         byte var8 = (byte)((int)(100.0D * var2));
         if(var7.getDirection() == DcMotor.Direction.REVERSE) {
            var8 *= -1;
         }

         int var9 = var7.getPortNumber();
         MatrixI2cTransaction var10 = new MatrixI2cTransaction((byte)var9, var8, this.a[var9].a, (byte)(8 | this.runModeToFlagMatrix(this.a[var9].f)));
         this.master.queueTransaction(var10);
      }

      MatrixI2cTransaction var5 = new MatrixI2cTransaction((byte)0, MatrixI2cTransaction.a.i, 1);
      this.master.queueTransaction(var5);
   }

   public void setMotorPowerFloat(int var1) {
      this.a(var1);
      if(!this.a[var1].d) {
         MatrixI2cTransaction var2 = new MatrixI2cTransaction((byte)var1, MatrixI2cTransaction.a.a, 4);
         this.master.queueTransaction(var2);
      }

      this.a[var1].d = true;
   }

   public void setMotorTargetPosition(int var1, int var2) {
      this.a(var1);
      MatrixI2cTransaction var3 = new MatrixI2cTransaction((byte)var1, MatrixI2cTransaction.a.b, var2);
      this.master.queueTransaction(var3);
      this.a[var1].a = var2;
   }

   private class a {
      public int a = 0;
      public int b = 0;
      public byte c = 0;
      public boolean d = true;
      public double e = 0.0D;
      public DcMotorController.RunMode f;

      public a() {
         this.f = DcMotorController.RunMode.RESET_ENCODERS;
      }
   }
}
