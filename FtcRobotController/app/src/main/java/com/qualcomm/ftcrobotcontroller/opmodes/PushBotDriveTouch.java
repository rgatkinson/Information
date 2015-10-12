package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class PushBotDriveTouch extends LinearOpMode {
   DcMotor leftMotor;
   DcMotor rightMotor;
   TouchSensor touchSensor;

   public void runOpMode() throws InterruptedException {
      this.leftMotor = (DcMotor)this.hardwareMap.dcMotor.get("left_drive");
      this.rightMotor = (DcMotor)this.hardwareMap.dcMotor.get("right_drive");
      this.rightMotor.setDirection(DcMotor.Direction.REVERSE);
      this.touchSensor = (TouchSensor)this.hardwareMap.touchSensor.get("sensor_touch");
      this.waitForStart();

      while(this.opModeIsActive()) {
         if(this.touchSensor.isPressed()) {
            this.leftMotor.setPower(0.0D);
            this.rightMotor.setPower(0.0D);
         } else {
            this.leftMotor.setPower(0.5D);
            this.rightMotor.setPower(0.5D);
         }

         this.telemetry.addData("isPressed", String.valueOf(this.touchSensor.isPressed()));
         this.waitOneFullHardwareCycle();
      }

   }
}
