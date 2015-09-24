package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;

public class IrSeekerOp extends OpMode {
   static final double HOLD_IR_SIGNAL_STRENGTH = 0.2D;
   static final double MOTOR_POWER = 0.15D;
   IrSeekerSensor irSeeker;
   DcMotor motorLeft;
   DcMotor motorRight;

   public void init() {
      this.irSeeker = (IrSeekerSensor)this.hardwareMap.irSeekerSensor.get("ir_seeker");
      this.motorRight = (DcMotor)this.hardwareMap.dcMotor.get("motor_2");
      this.motorLeft = (DcMotor)this.hardwareMap.dcMotor.get("motor_1");
   }

   public void loop() {
      double var1 = 0.0D;
      double var3 = 0.0D;
      if(this.irSeeker.signalDetected()) {
         var1 = this.irSeeker.getAngle();
         var3 = this.irSeeker.getStrength();
         if(var1 < -20.0D) {
            this.motorRight.setPower(0.15D);
            this.motorLeft.setPower(-0.15D);
         } else if(var1 > 20.0D) {
            this.motorRight.setPower(-0.15D);
            this.motorLeft.setPower(0.15D);
         } else if(var3 < 0.2D) {
            this.motorRight.setPower(0.15D);
            this.motorLeft.setPower(0.15D);
         } else {
            this.motorRight.setPower(0.0D);
            this.motorLeft.setPower(0.0D);
         }
      } else {
         this.motorRight.setPower(0.0D);
         this.motorLeft.setPower(0.0D);
      }

      this.telemetry.addData("angle", var1);
      this.telemetry.addData("strength", var3);
      DbgLog.msg(this.irSeeker.toString());
   }

   public void start() {
      this.motorLeft.setDirection(DcMotor.Direction.REVERSE);
   }
}
