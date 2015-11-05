package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;

public class test extends OpMode implements SensorEventListener {
   private static final float F_INITIAL_ANGLE = 0.0F;
   private static final float kD = 3.0E-4F;
   private static final float kI = 20.0F;
   private static final float kM = 1.0F;
   private static final float kP = 15.0F;
   float D;
   float I;
   float P;
   Context context;
   float fAngle = 0.0F;
   float fAngleRate = 0.0F;
   float fError = 0.0F;
   private float fPowerLeft = 0.0F;
   private float fPowerRight = 0.0F;
   int iDeltaMotorAngle = 0;
   int iLeftPosition = 0;
   int iMotorAngle = 0;
   int iPrevMotorAngle = 0;
   int iRightPosition = 0;
   long lDeltaTime;
   long lPrevTime;
   DcMotor motorLeft;
   DcMotor motorRight;
   private double scaledPowerLeft = 0.0D;
   private double scaledPowerRight = 0.0D;
   Sensor sensorGyro;
   SensorManager sensorManager;

   private void adjustAngle() {
      this.fAngle += 0.002F * (float)this.iDeltaMotorAngle;
      this.fAngle += 2.0E-7F * (float)this.iMotorAngle;
   }

   private void adjustMotors() {
      this.fPowerLeft = 1.0F * (this.P + this.I + this.D);
      this.fPowerRight = this.fPowerLeft;
      if(this.iLeftPosition > this.iRightPosition) {
         this.fPowerLeft -= 3.0F;
         this.fPowerRight += 3.0F;
      } else if(this.iLeftPosition < this.iRightPosition) {
         this.fPowerLeft += 3.0F;
         this.fPowerRight -= 3.0F;
      }

      this.scaledPowerLeft = (double)(this.fPowerLeft / 100.0F);
      this.scaledPowerRight = (double)(this.fPowerRight / 100.0F);
      this.scaledPowerLeft = Range.clip(this.scaledPowerLeft, -1.0D, 1.0D);
      this.scaledPowerRight = Range.clip(this.scaledPowerRight, -1.0D, 1.0D);
      this.motorLeft.setPower(this.scaledPowerLeft);
      this.motorRight.setPower(this.scaledPowerRight);
   }

   private void calculatePID() {
      this.fError = this.fAngle;
      this.P = 15.0F * this.fError;
      this.I += 20.0F * this.fError * (float)this.lDeltaTime;
      this.D = 3.0E-4F * this.fAngleRate / (float)this.lDeltaTime;
   }

   private void processEncoders() {
      this.iLeftPosition = this.motorLeft.getCurrentPosition();
      this.iRightPosition = this.motorRight.getCurrentPosition();
      this.iMotorAngle = this.iLeftPosition;
      this.iDeltaMotorAngle = this.iMotorAngle - this.iPrevMotorAngle;
      this.iPrevMotorAngle = this.iMotorAngle;
   }

   private void processGyro(SensorEvent var1) {
      this.fAngleRate = 180.0F * var1.values[0] / 3.1415927F;
      long var2 = System.currentTimeMillis();
      this.lDeltaTime = var2 - this.lPrevTime;
      this.lPrevTime = var2;
      this.fAngle = 0.0F + this.fAngleRate + this.fAngleRate * (float)this.lDeltaTime;
   }

   public void init() {
      this.motorRight = (DcMotor)this.hardwareMap.dcMotor.get("motor_1");
      this.motorLeft = (DcMotor)this.hardwareMap.dcMotor.get("motor_2");
      this.motorLeft.setDirection(DcMotor.Direction.REVERSE);
      this.motorLeft.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
      this.motorRight.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
      this.context = this.hardwareMap.appContext;
      this.sensorManager = (SensorManager)this.context.getSystemService("sensor");
      this.sensorGyro = this.sensorManager.getDefaultSensor(4);
      SensorManager var1 = this.sensorManager;
      Sensor var2 = this.sensorGyro;
      SensorManager var10000 = this.sensorManager;
      var1.registerListener(this, var2, 0);
      this.lPrevTime = System.currentTimeMillis();
   }

   public void loop() {
      if(this.motorLeft.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS && this.motorRight.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS) {
         Object[] var1 = new Object[]{Long.valueOf(System.currentTimeMillis())};
         DbgLog.msg(String.format("TIE: Loop current time = %03d", var1));
      } else {
         if(this.motorLeft.getChannelMode() == DcMotorController.RunMode.RESET_ENCODERS) {
            this.motorLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
         }

         if(this.motorRight.getChannelMode() == DcMotorController.RunMode.RESET_ENCODERS) {
            this.motorRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
         }

      }
   }

   public void onAccuracyChanged(Sensor var1, int var2) {
   }

   public void onSensorChanged(SensorEvent var1) {
      if(var1.sensor.getType() == 4) {
         this.processGyro(var1);
         this.processEncoders();
         this.adjustAngle();
         this.calculatePID();
         Object[] var2 = new Object[]{Long.valueOf(this.lDeltaTime)};
         DbgLog.msg(String.format("TIE: lDeltaTime = %03d", var2));
      }

   }

   public void stop() {
      this.sensorManager.unregisterListener(this, this.sensorGyro);
   }
}
