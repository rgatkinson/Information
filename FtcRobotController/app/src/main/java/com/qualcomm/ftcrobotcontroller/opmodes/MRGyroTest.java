package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.robocol.Telemetry;

public class MRGyroTest extends LinearOpMode {
   public void runOpMode() throws InterruptedException {
      this.hardwareMap.logDevices();
      GyroSensor var1 = (GyroSensor)this.hardwareMap.gyroSensor.get("gyro");
      var1.calibrate();
      this.waitForStart();

      while(var1.isCalibrating()) {
         Thread.sleep(50L);
      }

      while(this.opModeIsActive()) {
         if(this.gamepad1.a && this.gamepad1.b) {
            var1.resetZAxisIntegrator();
         }

         int var2 = var1.rawX();
         int var3 = var1.rawY();
         int var4 = var1.rawZ();
         int var5 = var1.getHeading();
         Telemetry var6 = this.telemetry;
         Object[] var7 = new Object[]{Integer.valueOf(var2)};
         var6.addData("1. x", String.format("%03d", var7));
         Telemetry var8 = this.telemetry;
         Object[] var9 = new Object[]{Integer.valueOf(var3)};
         var8.addData("2. y", String.format("%03d", var9));
         Telemetry var10 = this.telemetry;
         Object[] var11 = new Object[]{Integer.valueOf(var4)};
         var10.addData("3. z", String.format("%03d", var11));
         Telemetry var12 = this.telemetry;
         Object[] var13 = new Object[]{Integer.valueOf(var5)};
         var12.addData("4. h", String.format("%03d", var13));
         Thread.sleep(100L);
      }

   }
}
