package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.Servo;

public class REF_FULL_SET01_TEST extends LinearOpMode {
   public void runOpMode() throws InterruptedException {
      this.hardwareMap.logDevices();
      DcMotor var1 = (DcMotor)this.hardwareMap.dcMotor.get("mc1_mtr1");
      DcMotor var2 = (DcMotor)this.hardwareMap.dcMotor.get("mc2_mtr1");
      Servo var3 = (Servo)this.hardwareMap.servo.get("servo_1");
      Servo var4 = (Servo)this.hardwareMap.servo.get("servo_2");
      Servo var5 = (Servo)this.hardwareMap.servo.get("servo_3");
      Servo var6 = (Servo)this.hardwareMap.servo.get("servo_4");
      Servo var7 = (Servo)this.hardwareMap.servo.get("servo_5");
      Servo var8 = (Servo)this.hardwareMap.servo.get("servo_6");
      LED var9 = (LED)this.hardwareMap.led.get("LED");
      this.waitForStart();
      boolean var10 = true;
      double var11 = 0.2D;
      double var13 = 1.0D;

      while(this.opModeIsActive()) {
         var1.setPower(var11);
         var2.setPower(var11);
         var3.setPosition(var13);
         var4.setPosition(var13);
         var5.setPosition(var13);
         var6.setPosition(var13);
         var7.setPosition(var13);
         var8.setPosition(var13);
         var9.enable(var10);
         Thread.sleep(3000L);
         if(var10) {
            var13 = 0.0D;
         } else {
            var13 = 1.0D;
         }

         var11 = -var11;
         if(!var10) {
            var10 = true;
         } else {
            var10 = false;
         }
      }

   }
}
