package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class NxtTeleOp extends OpMode {
   Servo claw;
   double clawDelta = 0.01D;
   double clawPosition;
   DcMotorController.DeviceMode devMode;
   DcMotor motorLeft;
   DcMotor motorRight;
   int numOpLoops = 1;
   DcMotorController wheelController;
   Servo wrist;
   double wristDelta = 0.01D;
   double wristPosition;

   private boolean allowedToWrite() {
      return this.devMode == DcMotorController.DeviceMode.WRITE_ONLY;
   }

   public void init() {
      this.motorRight = (DcMotor)this.hardwareMap.dcMotor.get("motor_2");
      this.motorLeft = (DcMotor)this.hardwareMap.dcMotor.get("motor_1");
      this.claw = (Servo)this.hardwareMap.servo.get("servo_6");
      this.wrist = (Servo)this.hardwareMap.servo.get("servo_1");
      this.wheelController = (DcMotorController)this.hardwareMap.dcMotorController.get("wheels");
   }

   public void init_loop() {
      this.devMode = DcMotorController.DeviceMode.WRITE_ONLY;
      this.motorRight.setDirection(DcMotor.Direction.REVERSE);
      this.motorLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
      this.motorRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
      this.wristPosition = 0.6D;
      this.clawPosition = 0.5D;
   }

   public void loop() {
      if(this.allowedToWrite()) {
         if(this.gamepad1.dpad_left) {
            this.motorLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
            this.motorRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
         }

         if(this.gamepad1.dpad_right) {
            this.motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            this.motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
         }

         float var1 = -this.gamepad1.left_stick_y;
         float var2 = this.gamepad1.left_stick_x;
         float var3 = var1 - var2;
         float var4 = var1 + var2;
         float var5 = Range.clip(var3, -1.0F, 1.0F);
         float var6 = Range.clip(var4, -1.0F, 1.0F);
         this.motorRight.setPower((double)var5);
         this.motorLeft.setPower((double)var6);
         if(this.gamepad1.a) {
            this.wristPosition -= this.wristDelta;
         }

         if(this.gamepad1.y) {
            this.wristPosition += this.wristDelta;
         }

         if(this.gamepad1.x) {
            this.clawPosition -= this.clawDelta;
         }

         if(this.gamepad1.b) {
            this.clawPosition += this.clawDelta;
         }

         this.wristPosition = Range.clip(this.wristPosition, 0.0D, 1.0D);
         this.clawPosition = Range.clip(this.clawPosition, 0.0D, 1.0D);
         this.wrist.setPosition(this.wristPosition);
         this.claw.setPosition(this.clawPosition);
         if(!this.gamepad2.atRest()) {
            float var7 = this.gamepad2.right_trigger;
            if((double)this.gamepad2.left_trigger != 0.0D) {
               var7 = -this.gamepad2.left_trigger;
            }

            float var8 = var7;
            float var9 = var7;
            if(this.gamepad2.left_stick_x < 0.0F) {
               var9 = var7 * (1.0F + this.gamepad2.left_stick_x);
            }

            if(this.gamepad2.left_stick_x > 0.0F) {
               var8 = var7 * (1.0F - this.gamepad2.left_stick_x);
            }

            this.motorRight.setPower((double)var8);
            this.motorLeft.setPower((double)var9);
         }
      }

      if(this.numOpLoops % 17 == 0) {
         this.wheelController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);
      }

      if(this.wheelController.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.READ_ONLY) {
         this.telemetry.addData("Text", "free flow text");
         this.telemetry.addData("left motor", this.motorLeft.getPower());
         this.telemetry.addData("right motor", this.motorRight.getPower());
         this.telemetry.addData("RunMode: ", this.motorLeft.getMode().toString());
         this.wheelController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
         this.numOpLoops = 0;
      }

      this.devMode = this.wheelController.getMotorControllerDeviceMode();
      ++this.numOpLoops;
   }
}
