package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.util.Range;

public class PushBotIrSeek extends LinearOpMode {
   static final double kBaseSpeed = 0.15D;
   static final double kMaximumStrength = 0.6D;
   static final double kMinimumStrength = 0.08D;
   IrSeekerSensor irSeeker;
   DcMotor leftMotor;
   DcMotor rightMotor;

   public void runOpMode() throws InterruptedException {
      this.irSeeker = (IrSeekerSensor)this.hardwareMap.irSeekerSensor.get("sensor_ir");
      this.leftMotor = (DcMotor)this.hardwareMap.dcMotor.get("left_drive");
      this.rightMotor = (DcMotor)this.hardwareMap.dcMotor.get("right_drive");
      this.rightMotor.setDirection(DcMotor.Direction.REVERSE);
      this.waitForStart();

      while(this.opModeIsActive()) {
         double var1 = this.irSeeker.getAngle() / 30.0D;
         double var3 = this.irSeeker.getStrength();
         if(var3 > 0.08D && var3 < 0.6D) {
            double var5 = Range.clip(0.15D + var1 / 8.0D, -1.0D, 1.0D);
            double var7 = Range.clip(0.15D - var1 / 8.0D, -1.0D, 1.0D);
            this.leftMotor.setPower(var5);
            this.rightMotor.setPower(var7);
         } else {
            this.leftMotor.setPower(0.0D);
            this.rightMotor.setPower(0.0D);
         }

         this.telemetry.addData("Seeker", this.irSeeker.toString());
         this.telemetry.addData("Speed", " Left=" + this.leftMotor.getPower() + " Right=" + this.rightMotor.getPower());
         this.waitOneFullHardwareCycle();
      }

   }
}
