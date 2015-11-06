package com.qualcomm.ftcrobotcontroller.opmodes;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

public class HTRGBExample extends LinearOpMode {
   ColorSensor sensorRGB;

   public void runOpMode() throws InterruptedException {
      this.hardwareMap.logDevices();
      this.sensorRGB = (ColorSensor)this.hardwareMap.colorSensor.get("nxt");
      this.sensorRGB.enableLed(true);
      this.waitOneFullHardwareCycle();
      this.waitForStart();
      final float[] var1 = new float[]{0.0F, 0.0F, 0.0F};
      final View var2 = ((Activity)this.hardwareMap.appContext).findViewById(2131427353);
      boolean var3 = false;

      while(this.opModeIsActive()) {
         boolean var4;
         if(!this.gamepad1.x && !this.gamepad2.x) {
            var4 = false;
         } else {
            var4 = true;
         }

         if(var4 && var4 != var3) {
            DbgLog.msg("MY_DEBUG - x button was pressed!");
            var3 = var4;
            this.sensorRGB.enableLed(true);
         } else if(!var4 && var4 != var3) {
            DbgLog.msg("MY_DEBUG - x button was released!");
            var3 = var4;
            this.sensorRGB.enableLed(false);
         }

         Color.RGBToHSV(this.sensorRGB.red(), this.sensorRGB.green(), this.sensorRGB.blue(), var1);
         this.telemetry.addData("Clear", (float)this.sensorRGB.alpha());
         this.telemetry.addData("Red  ", (float)this.sensorRGB.red());
         this.telemetry.addData("Green", (float)this.sensorRGB.green());
         this.telemetry.addData("Blue ", (float)this.sensorRGB.blue());
         this.telemetry.addData("Hue", var1[0]);
         var2.post(new Runnable() {
            public void run() {
               var2.setBackgroundColor(Color.HSVToColor(255, var1));
            }
         });
         this.waitOneFullHardwareCycle();
      }

   }
}
