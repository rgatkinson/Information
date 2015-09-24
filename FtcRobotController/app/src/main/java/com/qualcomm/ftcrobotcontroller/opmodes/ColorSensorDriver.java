package com.qualcomm.ftcrobotcontroller.opmodes;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class ColorSensorDriver extends LinearOpMode {
   DeviceInterfaceModule cdim;
   ColorSensor colorSensor;
   public ColorSensorDriver.ColorSensorDevice device;
   LED led;
   TouchSensor t;

   public ColorSensorDriver() {
      this.device = ColorSensorDriver.ColorSensorDevice.MODERN_ROBOTICS_I2C;
   }

   private void enableLed(boolean var1) {
      switch(null.$SwitchMap$com$qualcomm$ftcrobotcontroller$opmodes$ColorSensorDriver$ColorSensorDevice[this.device.ordinal()]) {
      case 1:
         this.colorSensor.enableLed(var1);
         return;
      case 2:
         this.led.enable(var1);
         return;
      case 3:
         this.colorSensor.enableLed(var1);
         return;
      default:
      }
   }

   public void runOpMode() throws InterruptedException {
      this.hardwareMap.logDevices();
      this.cdim = (DeviceInterfaceModule)this.hardwareMap.deviceInterfaceModule.get("dim");
      switch(null.$SwitchMap$com$qualcomm$ftcrobotcontroller$opmodes$ColorSensorDriver$ColorSensorDevice[this.device.ordinal()]) {
      case 1:
         this.colorSensor = (ColorSensor)this.hardwareMap.colorSensor.get("nxt");
         break;
      case 2:
         this.colorSensor = (ColorSensor)this.hardwareMap.colorSensor.get("lady");
         break;
      case 3:
         this.colorSensor = (ColorSensor)this.hardwareMap.colorSensor.get("mr");
      }

      this.led = (LED)this.hardwareMap.led.get("led");
      this.t = (TouchSensor)this.hardwareMap.touchSensor.get("t");
      this.waitForStart();
      final float[] var1 = new float[]{0.0F, 0.0F, 0.0F};
      final View var2 = ((Activity)this.hardwareMap.appContext).findViewById(2131427353);

      while(this.opModeIsActive()) {
         this.enableLed(this.t.isPressed());
         switch(null.$SwitchMap$com$qualcomm$ftcrobotcontroller$opmodes$ColorSensorDriver$ColorSensorDevice[this.device.ordinal()]) {
         case 1:
            Color.RGBToHSV(this.colorSensor.red(), this.colorSensor.green(), this.colorSensor.blue(), var1);
            break;
         case 2:
            Color.RGBToHSV(255 * this.colorSensor.red() / 800, 255 * this.colorSensor.green() / 800, 255 * this.colorSensor.blue() / 800, var1);
            break;
         case 3:
            Color.RGBToHSV(8 * this.colorSensor.red(), 8 * this.colorSensor.green(), 8 * this.colorSensor.blue(), var1);
         }

         this.telemetry.addData("Clear", (float)this.colorSensor.alpha());
         this.telemetry.addData("Red  ", (float)this.colorSensor.red());
         this.telemetry.addData("Green", (float)this.colorSensor.green());
         this.telemetry.addData("Blue ", (float)this.colorSensor.blue());
         this.telemetry.addData("Hue", var1[0]);
         var2.post(new Runnable() {
            public void run() {
               var2.setBackgroundColor(Color.HSVToColor(255, var1));
            }
         });
         this.waitOneFullHardwareCycle();
      }

   }

   public static enum ColorSensorDevice {
      ADAFRUIT,
      HITECHNIC_NXT,
      MODERN_ROBOTICS_I2C;

      static {
         ColorSensorDriver.ColorSensorDevice[] var0 = new ColorSensorDriver.ColorSensorDevice[]{ADAFRUIT, HITECHNIC_NXT, MODERN_ROBOTICS_I2C};
      }
   }
}
