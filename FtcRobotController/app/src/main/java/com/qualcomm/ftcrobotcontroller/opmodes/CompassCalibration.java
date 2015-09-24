package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

public class CompassCalibration extends OpMode {
   static final double HOLD_POSITION = 3.0D;
   static final double MOTOR_POWER = 0.2D;
   CompassSensor compass;
   private boolean keepTurning = true;
   private boolean monitorCalibrationSuccess = false;
   DcMotor motorLeft;
   DcMotor motorRight;
   double pauseTime = 2.0D;
   private boolean returnToMeasurementMode = false;
   private double turnTime = 20.0D;

   private String calibrationMessageToString(boolean var1) {
      return var1?"Calibration Failed!":"Calibration Succeeded.";
   }

   public void init() {
      this.compass = (CompassSensor)this.hardwareMap.compassSensor.get("compass");
      this.motorRight = (DcMotor)this.hardwareMap.dcMotor.get("right");
      this.motorLeft = (DcMotor)this.hardwareMap.dcMotor.get("left");
   }

   public void init_loop() {
      this.motorRight.setDirection(DcMotor.Direction.REVERSE);
      this.compass.setMode(CompassSensor.CompassMode.CALIBRATION_MODE);
      this.telemetry.addData("Compass", "Compass in calibration mode");
      this.pauseTime = 3.0D + this.time;
   }

   public void loop() {
      if(this.time > this.pauseTime) {
         if(this.keepTurning) {
            this.telemetry.addData("Compass", "Calibration mode. Turning the robot...");
            DbgLog.msg("Calibration mode. Turning the robot...");
            this.motorRight.setPower(-0.2D);
            this.motorLeft.setPower(0.2D);
            if(this.time > 3.0D + this.turnTime) {
               this.keepTurning = false;
               this.returnToMeasurementMode = true;
            }
         } else if(this.returnToMeasurementMode) {
            this.telemetry.addData("Compass", "Returning to measurement mode");
            DbgLog.msg("Returning to measurement mode");
            this.motorRight.setPower(0.0D);
            this.motorLeft.setPower(0.0D);
            this.compass.setMode(CompassSensor.CompassMode.MEASUREMENT_MODE);
            this.pauseTime = 3.0D + this.time;
            this.returnToMeasurementMode = false;
            this.monitorCalibrationSuccess = true;
            this.telemetry.addData("Compass", "Waiting for feedback from sensor...");
         } else if(this.monitorCalibrationSuccess) {
            String var1 = this.calibrationMessageToString(this.compass.calibrationFailed());
            this.telemetry.addData("Compass", var1);
            if(this.compass.calibrationFailed()) {
               DbgLog.error("Calibration failed and needs to be re-run");
            } else {
               DbgLog.msg(var1);
            }
         }

         this.pauseTime = 3.0D + this.time;
      }

   }
}
