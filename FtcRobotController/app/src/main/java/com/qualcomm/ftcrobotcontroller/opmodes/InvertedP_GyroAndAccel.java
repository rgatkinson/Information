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

public class InvertedP_GyroAndAccel extends OpMode implements SensorEventListener {
   static final float kc1 = 8.5E-4F;
   static final float kc2 = 8.5E-4F;
   static final float kc3 = 0.4F;
   static final float km = 1.0F;
   float D = 0.0F;
   float I = 0.0F;
   float P = 0.0F;
   float angle;
   float angle_accel;
   float angle_accel_err = 0.0F;
   float angle_accel_ref;
   float angle_rate;
   Context context;
   float currTime;
   float deltaTime;
   float dt = 0.0F;
   float err = 0.0F;
   float fPowerLeft = 0.0F;
   float fPowerRight = 0.0F;
   float inerr1 = 0.0F;
   float inerr2 = 0.0F;
   float kAccel = 0.1F;
   float kAngleOffset = 0.001F;
   float kd = 0.001F;
   float ki = 1.0E-6F;
   float kp = 0.003F;
   float lrerr = 0.0F;
   float mota = 0.0F;
   float motb = 0.0F;
   float motcomp = 0.0F;
   DcMotor motorLeft;
   DcMotor motorRight;
   float prevGyroTime = 0.0F;
   float prevTime;
   Sensor sensorAccel;
   Sensor sensorGyro;
   SensorManager sensorManager;
   float x;
   float y;
   float z;

   private void adjustAngle() {
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
      this.err = this.angle;
      this.P = this.kp * this.err;
      this.I += this.ki * this.err * this.deltaTime;
      this.D = this.kd * this.angle_rate;
   }

   private void processAccel(SensorEvent var1) {
      this.x = var1.values[0];
      this.y = var1.values[1];
      this.z = var1.values[2];
      this.angle_accel = (float)Math.atan2((double)(-this.z), (double)this.y);
      this.angle_accel_err = this.angle_accel - this.angle_accel_ref;
   }

   private void processEncoders() {
   }

   private void processGyro(SensorEvent var1) {
      this.angle_rate = var1.values[0];
      float var2 = (float)System.currentTimeMillis();
      this.angle = this.angle + this.angle_rate * ((float)System.currentTimeMillis() - this.prevGyroTime) + this.kAngleOffset;
      this.prevGyroTime = var2;
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
      this.sensorAccel = this.sensorManager.getDefaultSensor(1);
      this.prevTime = (float)System.currentTimeMillis();
      SensorManager var1 = this.sensorManager;
      Sensor var2 = this.sensorGyro;
      SensorManager var10000 = this.sensorManager;
      var1.registerListener(this, var2, 0);
      SensorManager var5 = this.sensorManager;
      Sensor var6 = this.sensorAccel;
      var10000 = this.sensorManager;
      var5.registerListener(this, var6, 0);
   }

   public void loop() {
      if(this.motorLeft.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS && this.motorRight.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS) {
         this.inerr1 = 0.0F;
         this.inerr2 = 0.0F;
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
      } else if(var1.sensor.getType() == 1) {
         this.processAccel(var1);
      }

      this.angle += this.kAccel * this.angle_accel_err;
      this.currTime = (float)System.currentTimeMillis();
      this.deltaTime = this.currTime - this.prevTime;
      this.prevTime = this.currTime;
      this.calculatePID();
      this.adjustMotors();
   }

   public void start() {
      this.angle_accel_ref = this.angle_accel;
      this.angle = 0.0F;
      this.prevTime = (float)System.currentTimeMillis();
   }

   public void stop() {
      this.sensorManager.unregisterListener(this, this.sensorGyro);
   }
}
