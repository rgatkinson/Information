package com.qualcomm.hardware;

import com.qualcomm.hardware.ModernRoboticsUsbDeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

public class ModernRoboticsAnalogOpticalDistanceSensor extends OpticalDistanceSensor {
   private final ModernRoboticsUsbDeviceInterfaceModule a;
   private final int b;

   public ModernRoboticsAnalogOpticalDistanceSensor(ModernRoboticsUsbDeviceInterfaceModule var1, int var2) {
      this.a = var1;
      this.b = var2;
   }

   public void close() {
   }

   public void enableLed(boolean var1) {
   }

   public String getConnectionInfo() {
      return this.a.getConnectionInfo() + "; analog port " + this.b;
   }

   public String getDeviceName() {
      return "Modern Robotics Analog Optical Distance Sensor";
   }

   public double getLightDetected() {
      return (double)this.a.getAnalogInputValue(this.b) / 1023.0D;
   }

   public int getLightDetectedRaw() {
      return this.a.getAnalogInputValue(this.b);
   }

   public int getVersion() {
      return 0;
   }

   public String status() {
      Object[] var1 = new Object[]{this.a.getSerialNumber().toString(), Integer.valueOf(this.b)};
      return String.format("Optical Distance Sensor, connected via device %s, port %d", var1);
   }
}
