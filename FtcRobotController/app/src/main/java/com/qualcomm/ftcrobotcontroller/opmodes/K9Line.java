package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;

public class K9Line extends OpMode {
   static final double HOLD_IR_SIGNAL_STRENGTH = 0.2D;
   static final double LIGHT_THRESHOLD = 0.5D;
   static final double MOTOR_POWER = 0.15D;
   Servo arm;
   double armPosition;
   Servo claw;
   double clawPosition;
   DcMotor motorLeft;
   DcMotor motorRight;
   LightSensor reflectedLight;

   public void init() {
      this.motorRight = (DcMotor)this.hardwareMap.dcMotor.get("motor_2");
      this.motorLeft = (DcMotor)this.hardwareMap.dcMotor.get("motor_1");
      this.motorLeft.setDirection(DcMotor.Direction.REVERSE);
      this.arm = (Servo)this.hardwareMap.servo.get("servo_1");
      this.claw = (Servo)this.hardwareMap.servo.get("servo_6");
      this.armPosition = 0.2D;
      this.clawPosition = 0.25D;
      this.reflectedLight = (LightSensor)this.hardwareMap.lightSensor.get("light_sensor");
      this.reflectedLight.enableLed(true);
   }

   public void loop() {
      this.arm.setPosition(this.armPosition);
      this.claw.setPosition(this.clawPosition);
      double var1 = this.reflectedLight.getLightDetected();
      double var3;
      double var5;
      if(var1 < 0.5D) {
         var3 = 0.15D;
         var5 = 0.0D;
      } else {
         var3 = 0.0D;
         var5 = 0.15D;
      }

      this.motorRight.setPower(var3);
      this.motorLeft.setPower(var5);
      this.telemetry.addData("Text", "*** Robot Data***");
      this.telemetry.addData("reflection", "reflection:  " + Double.toString(var1));
      this.telemetry.addData("left tgt pwr", "left  pwr: " + Double.toString(var3));
      this.telemetry.addData("right tgt pwr", "right pwr: " + Double.toString(var5));
   }

   public void stop() {
   }
}
