package com.qualcomm.hardware.hardware;

import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class ModernRoboticsDigitalTouchSensor extends TouchSensor {
   private DeviceInterfaceModule a = null;
   private int b = -1;

   public ModernRoboticsDigitalTouchSensor(DeviceInterfaceModule var1, int var2) {
      this.a = var1;
      this.b = var2;
   }

   public void close() {
   }

   public String getConnectionInfo() {
      return this.a.getConnectionInfo() + "; digital port " + this.b;
   }

   public String getDeviceName() {
      return "Modern Robotics Digital Touch Sensor";
   }

   public double getValue() {
      return this.isPressed()?1.0D:0.0D;
   }

   public int getVersion() {
      return 1;
   }

   public boolean isPressed() {
      return this.a.getDigitalChannelState(this.b);
   }
}
