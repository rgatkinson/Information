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

public class LoopTest extends OpMode implements SensorEventListener {
   static final double dFactor = 0.0062832D;
   Context context;
   double dDeltaTime = 0.0D;
   double dPower;
   double dPrevTime = 0.0D;
   int iCount = 0;
   int iLeftCount;
   int iRightCount;
   DcMotor motorLeft;
   DcMotor motorRight;
   Sensor sensorGyro;
   SensorManager sensorManager;

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
   }

   public void loop() {
      if(this.motorLeft.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS && this.motorRight.getChannelMode() != DcMotorController.RunMode.RESET_ENCODERS) {
         this.iLeftCount = this.motorLeft.getCurrentPosition();
         this.iRightCount = this.motorRight.getCurrentPosition();
         double var1 = this.getRuntime();
         this.dDeltaTime = var1 - this.dPrevTime;
         this.dPower = Math.sin(0.0062832D * (double)this.iCount);
         this.motorLeft.setPower(this.dPower);
         this.motorRight.setPower(this.dPower);
         Object[] var3 = new Object[]{Double.valueOf(var1), Double.valueOf(this.dDeltaTime), Double.valueOf(this.dPower), Integer.valueOf(this.iLeftCount), Integer.valueOf(this.iRightCount)};
         DbgLog.msg(String.format("TIE,%.003f,%.003f,%.003f,%08d,%08d", var3));
         this.dPrevTime = var1;
         ++this.iCount;
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
         ;
      }

   }

   public void start() {
      this.dPrevTime = this.getRuntime();
      DbgLog.msg("TIE,curr_Time,delta_Time,dPower,left_cnt,right_cnt");
   }

   public void stop() {
      this.sensorManager.unregisterListener(this, this.sensorGyro);
   }
}
