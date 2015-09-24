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
      double var1;
      double var3;
      if(0.0D < 0.5D) {
         var1 = 0.15D;
         var3 = 0.0D;
      } else {
         var1 = 0.0D;
         var3 = 0.15D;
      }

      this.motorRight.setPower(var1);
      this.motorLeft.setPower(var3);
      this.telemetry.addData("Text", "*** Robot Data***");
      this.telemetry.addData("reflection", "reflection:  " + Double.toString(0.0D));
      this.telemetry.addData("left tgt pwr", "left  pwr: " + Double.toString(var1));
      this.telemetry.addData("right tgt pwr", "right pwr: " + Double.toString(var3));
   }

   public void stop() {
   }
}
