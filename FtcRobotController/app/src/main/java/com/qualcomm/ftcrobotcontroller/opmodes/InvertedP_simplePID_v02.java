package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;

public class InvertedP_simplePID_v02 extends OpMode implements SensorEventListener {
   private static final float F_INITIAL_ANGLE = 0.0F;
   private static final float deadZone = 2.5F;
   private static final float kA = 1.0E-5F;
   private static final float kD = 1.0E-4F;
   private static final float kDA = 1.0E-4F;
   private static final float kI = 5.0E-6F;
   private static final float kM = 1.0F;
   private static final float kP = 0.002F;
   float D = 0.0F;
   float I = 0.0F;
   float P = 0.0F;
   Context context;
   private float cumulativeError = 0.0F;
   private float currentAngle = 0.0F;
   private float currentAngleRate = 0.0F;
   private float currentError = 0.0F;
   private float currentErrorRate = 0.0F;
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
   private float prevError = 0.0F;
   private double scaledPowerLeft = 0.0D;
   private double scaledPowerRight = 0.0D;
   Sensor sensorGyro;
   SensorManager sensorManager;
   private float targetAngle = 0.0F;

   private void adjustAngle() {
      this.currentAngle += 1.0E-4F * (float)this.iDeltaMotorAngle;
      this.currentAngle += 1.0E-5F * (float)this.iMotorAngle;
   }

   private void adjustMotors() {
      this.fPowerLeft = 1.0F * (this.P + this.I + this.D);
      this.fPowerRight = this.fPowerLeft;
      this.fPowerLeft = Range.clip(this.fPowerLeft, -1.0F, 1.0F);
      this.fPowerRight = Range.clip(this.fPowerRight, -1.0F, 1.0F);
      this.motorLeft.setPower((double)this.fPowerLeft);
      this.motorRight.setPower((double)this.fPowerRight);
   }

   private void calculatePID() {
      this.currentError = this.currentAngle - this.targetAngle;
      this.currentErrorRate = this.currentAngleRate;
      this.P = 0.002F * this.currentError;
      this.I += 5.0E-6F * this.currentError * (float)this.lDeltaTime;
      this.D = 1.0E-4F * this.currentErrorRate;
   }

   private void processEncoders() {
      this.iLeftPosition = this.motorLeft.getCurrentPosition();
      this.iRightPosition = this.motorRight.getCurrentPosition();
      this.iMotorAngle = this.iLeftPosition;
      this.iDeltaMotorAngle = this.iMotorAngle - this.iPrevMotorAngle;
      this.iPrevMotorAngle = this.iMotorAngle;
   }

   private void processGyro(SensorEvent var1) {
      this.currentAngleRate = var1.values[0];
      if((double)Math.abs(this.currentAngleRate) < 0.05D) {
         this.currentAngleRate = 0.0F;
      }

      long var2 = System.currentTimeMillis();
      this.lDeltaTime = var2 - this.lPrevTime;
      this.lPrevTime = var2;
      this.currentAngle += this.currentAngleRate * (float)this.lDeltaTime;
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
      if(this.motorLeft.getChannelMode() == DcMotorController.RunMode.RESET_ENCODERS || this.motorRight.getChannelMode() == DcMotorController.RunMode.RESET_ENCODERS) {
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
         this.calculatePID();
         this.adjustMotors();
      }

   }

   public void start() {
      this.iPrevMotorAngle = 0;
   }

   public void stop() {
      this.sensorManager.unregisterListener(this, this.sensorGyro);
   }
}
