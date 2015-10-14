package com.qualcomm.hardware.hardware;

import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.TypeConversion;

public class HiTechnicNxtLightSensor extends LightSensor {
   public static final byte LED_DIGITAL_LINE_NUMBER = 0;
   private final ModernRoboticsUsbLegacyModule a;
   private final int b;

   HiTechnicNxtLightSensor(ModernRoboticsUsbLegacyModule var1, int var2) {
      var1.enableAnalogReadMode(var2);
      this.a = var1;
      this.b = var2;
   }

   public void close() {
   }

   public void enableLed(boolean var1) {
      this.a.setDigitalLine(this.b, 0, var1);
   }

   public String getConnectionInfo() {
      return this.a.getConnectionInfo() + "; port " + this.b;
   }

   public String getDeviceName() {
      return "NXT Light Sensor";
   }

   public double getLightDetected() {
      return Range.scale((double)this.a.readAnalog(this.b)[0], -128.0D, 127.0D, 0.0D, 1.0D);
   }

   public int getLightDetectedRaw() {
      return TypeConversion.unsignedByteToInt(this.a.readAnalog(this.b)[0]);
   }

   public int getVersion() {
      return 1;
   }

   public String status() {
      Object[] var1 = new Object[]{this.a.getSerialNumber().toString(), Integer.valueOf(this.b)};
      return String.format("NXT Light Sensor, connected via device %s, port %d", var1);
   }
}
