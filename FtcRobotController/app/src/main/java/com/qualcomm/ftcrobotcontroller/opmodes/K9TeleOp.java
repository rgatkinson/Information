package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.util.Range;

public class K9TeleOp extends OpMode {
   static final double ARM_MAX_RANGE = 0.9D;
   static final double ARM_MIN_RANGE = 0.2D;
   static final double CLAW_MAX_RANGE = 0.7D;
   static final double CLAW_MIN_RANGE = 0.2D;
   Servo arm;
   double armDelta = 0.1D;
   double armPosition;
   Servo claw;
   double clawDelta = 0.1D;
   double clawPosition;
   DcMotor motorLeft;
   DcMotor motorRight;

   public void init() {
      this.motorRight = (DcMotor)this.hardwareMap.dcMotor.get("motor_2");
      this.motorLeft = (DcMotor)this.hardwareMap.dcMotor.get("motor_1");
      this.motorLeft.setDirection(DcMotor.Direction.REVERSE);
      this.arm = (Servo)this.hardwareMap.servo.get("servo_1");
      this.claw = (Servo)this.hardwareMap.servo.get("servo_6");
      this.armPosition = 0.2D;
      this.clawPosition = 0.2D;
   }

   public void loop() {
      float var1 = -this.gamepad1.left_stick_y;
      float var2 = this.gamepad1.left_stick_x;
      float var3 = var1 - var2;
      float var4 = var1 + var2;
      float var5 = Range.clip(var3, -1.0F, 1.0F);
      float var6 = Range.clip(var4, -1.0F, 1.0F);
      float var7 = (float)this.scaleInput((double)var5);
      float var8 = (float)this.scaleInput((double)var6);
      this.motorRight.setPower((double)var7);
      this.motorLeft.setPower((double)var8);
      if(this.gamepad1.a) {
         this.armPosition += this.armDelta;
      }

      if(this.gamepad1.y) {
         this.armPosition -= this.armDelta;
      }

      if(this.gamepad1.x) {
         this.clawPosition += this.clawDelta;
      }

      if(this.gamepad1.b) {
         this.clawPosition -= this.clawDelta;
      }

      this.armPosition = Range.clip(this.armPosition, 0.2D, 0.9D);
      this.clawPosition = Range.clip(this.clawPosition, 0.2D, 0.7D);
      this.arm.setPosition(this.armPosition);
      this.claw.setPosition(this.clawPosition);
      this.telemetry.addData("Text", "*** Robot Data***");
      Telemetry var9 = this.telemetry;
      StringBuilder var10 = (new StringBuilder()).append("arm:  ");
      Object[] var11 = new Object[]{Double.valueOf(this.armPosition)};
      var9.addData("arm", var10.append(String.format("%.2f", var11)).toString());
      Telemetry var12 = this.telemetry;
      StringBuilder var13 = (new StringBuilder()).append("claw:  ");
      Object[] var14 = new Object[]{Double.valueOf(this.clawPosition)};
      var12.addData("claw", var13.append(String.format("%.2f", var14)).toString());
      Telemetry var15 = this.telemetry;
      StringBuilder var16 = (new StringBuilder()).append("left  pwr: ");
      Object[] var17 = new Object[]{Float.valueOf(var8)};
      var15.addData("left tgt pwr", var16.append(String.format("%.2f", var17)).toString());
      Telemetry var18 = this.telemetry;
      StringBuilder var19 = (new StringBuilder()).append("right pwr: ");
      Object[] var20 = new Object[]{Float.valueOf(var7)};
      var18.addData("right tgt pwr", var19.append(String.format("%.2f", var20)).toString());
   }

   double scaleInput(double var1) {
      double[] var3 = new double[]{0.0D, 0.05D, 0.09D, 0.1D, 0.12D, 0.15D, 0.18D, 0.24D, 0.3D, 0.36D, 0.43D, 0.5D, 0.6D, 0.72D, 0.85D, 1.0D, 1.0D};
      int var4 = (int)(16.0D * var1);
      if(var4 < 0) {
         var4 = -var4;
      }

      if(var4 > 16) {
         var4 = 16;
      }

      return var1 < 0.0D?-var3[var4]:var3[var4];
   }

   public void stop() {
   }
}
