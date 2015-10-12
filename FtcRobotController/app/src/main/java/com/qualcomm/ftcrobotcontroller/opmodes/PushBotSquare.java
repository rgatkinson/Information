package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class PushBotSquare extends LinearOpMode {
   DcMotor leftMotor;
   DcMotor rightMotor;

   public void runOpMode() throws InterruptedException {
      this.leftMotor = (DcMotor)this.hardwareMap.dcMotor.get("left_drive");
      this.rightMotor = (DcMotor)this.hardwareMap.dcMotor.get("right_drive");
      this.rightMotor.setDirection(DcMotor.Direction.REVERSE);
      this.leftMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
      this.rightMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
      this.waitForStart();

      for(int var1 = 0; var1 < 4; ++var1) {
         this.leftMotor.setPower(1.0D);
         this.rightMotor.setPower(1.0D);
         this.sleep(1000L);
         this.leftMotor.setPower(0.5D);
         this.rightMotor.setPower(-0.5D);
         this.sleep(500L);
      }

      this.leftMotor.setPowerFloat();
      this.rightMotor.setPowerFloat();
   }
}
