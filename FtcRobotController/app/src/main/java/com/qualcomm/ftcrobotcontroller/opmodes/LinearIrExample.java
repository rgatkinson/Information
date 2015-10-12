package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;

public class LinearIrExample extends LinearOpMode {
   static final double HOLD_IR_SIGNAL_STRENGTH = 0.2D;
   static final double MOTOR_POWER = 0.15D;
   IrSeekerSensor irSeeker;
   DcMotor motorLeft;
   DcMotor motorRight;

   public void runOpMode() throws InterruptedException {
      this.irSeeker = (IrSeekerSensor)this.hardwareMap.irSeekerSensor.get("ir_seeker");
      this.motorLeft = (DcMotor)this.hardwareMap.dcMotor.get("motor_1");
      this.motorRight = (DcMotor)this.hardwareMap.dcMotor.get("motor_2");
      this.motorLeft.setDirection(DcMotor.Direction.REVERSE);
      this.waitForStart();

      while(!this.irSeeker.signalDetected()) {
         this.sleep(1000L);
      }

      if(this.irSeeker.getAngle() < 0.0D) {
         this.motorRight.setPower(0.15D);
         this.motorLeft.setPower(-0.15D);
      } else if(this.irSeeker.getAngle() > 0.0D) {
         this.motorRight.setPower(-0.15D);
         this.motorLeft.setPower(0.15D);
      }

      while(this.irSeeker.getAngle() != 0.0D) {
         this.waitOneFullHardwareCycle();
      }

      this.motorRight.setPower(0.15D);
      this.motorLeft.setPower(0.15D);

      while(this.irSeeker.getStrength() < 0.2D) {
         this.waitOneFullHardwareCycle();
      }

      this.motorRight.setPower(0.0D);
      this.motorLeft.setPower(0.0D);
   }
}
