package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class LinearK9TeleOp extends LinearOpMode {
   Servo jaw;
   double jawPosition;
   DcMotor motorLeft;
   DcMotor motorRight;
   Servo neck;
   double neckDelta = 0.01D;
   double neckPosition;

   public void runOpMode() throws InterruptedException {
      this.motorLeft = (DcMotor)this.hardwareMap.dcMotor.get("motor_1");
      this.motorRight = (DcMotor)this.hardwareMap.dcMotor.get("motor_2");
      this.neck = (Servo)this.hardwareMap.servo.get("servo_1");
      this.jaw = (Servo)this.hardwareMap.servo.get("servo_6");
      this.motorLeft.setDirection(DcMotor.Direction.REVERSE);
      this.neckPosition = 0.5D;
      this.waitForStart();

      while(this.opModeIsActive()) {
         float var1 = -this.gamepad1.left_stick_y;
         float var2 = this.gamepad1.left_stick_x;
         float var3 = var1 - var2;
         float var4 = var1 + var2;
         float var5 = Range.clip(var3, -1.0F, 1.0F);
         float var6 = Range.clip(var4, -1.0F, 1.0F);
         this.motorRight.setPower((double)var5);
         this.motorLeft.setPower((double)var6);
         if(this.gamepad1.y) {
            this.neckPosition -= this.neckDelta;
         }

         if(this.gamepad1.a) {
            this.neckPosition += this.neckDelta;
         }

         this.neckPosition = Range.clip(this.neckPosition, 0.0D, 1.0D);
         this.jawPosition = 1.0D - Range.scale((double)this.gamepad1.right_trigger, 0.0D, 1.0D, 0.3D, 1.0D);
         this.neck.setPosition(this.neckPosition);
         this.jaw.setPosition(this.jawPosition);
         this.telemetry.addData("Text", "K9TeleOp");
         this.telemetry.addData(" left motor", this.motorLeft.getPower());
         this.telemetry.addData("right motor", this.motorRight.getPower());
         this.telemetry.addData("neck", this.neck.getPosition());
         this.telemetry.addData("jaw", this.jaw.getPosition());
         this.waitOneFullHardwareCycle();
      }

   }
}
