package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.Servo;

public class K9IrSeeker extends OpMode {
   static final double HOLD_IR_SIGNAL_STRENGTH = 0.5D;
   static final double MOTOR_POWER = 0.15D;
   Servo arm;
   double armPosition;
   Servo claw;
   double clawPosition;
   IrSeekerSensor irSeeker;
   DcMotor motorLeft;
   DcMotor motorRight;

   public void init() {
      this.motorRight = (DcMotor)this.hardwareMap.dcMotor.get("motor_2");
      this.motorLeft = (DcMotor)this.hardwareMap.dcMotor.get("motor_1");
      this.motorLeft.setDirection(DcMotor.Direction.REVERSE);
      this.arm = (Servo)this.hardwareMap.servo.get("servo_1");
      this.claw = (Servo)this.hardwareMap.servo.get("servo_6");
      this.armPosition = 0.1D;
      this.clawPosition = 0.25D;
      this.irSeeker = (IrSeekerSensor)this.hardwareMap.irSeekerSensor.get("ir_seeker");
   }

   public void loop() {
      double var1 = 0.0D;
      double var3 = 0.0D;
      this.arm.setPosition(this.armPosition);
      this.claw.setPosition(this.clawPosition);
      double var5;
      double var7;
      if(this.irSeeker.signalDetected()) {
         var1 = this.irSeeker.getAngle();
         var3 = this.irSeeker.getStrength();
         if(var1 < -60.0D) {
            var5 = -0.15D;
            var7 = 0.15D;
         } else if(var1 < -5.0D) {
            var5 = 0.09999999999999999D;
            var7 = 0.15D;
         } else if(var1 > 5.0D && var1 < 60.0D) {
            var5 = 0.15D;
            var7 = 0.09999999999999999D;
         } else if(var1 > 60.0D) {
            var5 = 0.15D;
            var7 = -0.15D;
         } else if(var3 < 0.5D) {
            var5 = 0.15D;
            var7 = 0.15D;
         } else {
            var5 = 0.0D;
            var7 = 0.0D;
         }
      } else {
         var5 = 0.0D;
         var7 = 0.0D;
      }

      this.motorRight.setPower(var7);
      this.motorLeft.setPower(var5);
      this.telemetry.addData("Text", "*** Robot Data***");
      this.telemetry.addData("angle", "angle:  " + Double.toString(var1));
      this.telemetry.addData("strength", "sig strength: " + Double.toString(var3));
      this.telemetry.addData("left tgt pwr", "left  pwr: " + Double.toString(var5));
      this.telemetry.addData("right tgt pwr", "right pwr: " + Double.toString(var7));
   }

   public void stop() {
   }
}
