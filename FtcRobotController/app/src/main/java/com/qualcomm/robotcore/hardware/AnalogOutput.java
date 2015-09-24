package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.AnalogOutputController;
import com.qualcomm.robotcore.hardware.HardwareDevice;

public class AnalogOutput implements HardwareDevice {
   private AnalogOutputController a = null;
   private int b = -1;

   public AnalogOutput(AnalogOutputController var1, int var2) {
      this.a = var1;
      this.b = var2;
   }

   public void close() {
   }

   public String getConnectionInfo() {
      return this.a.getConnectionInfo() + "; analog port " + this.b;
   }

   public String getDeviceName() {
      return "Analog Output";
   }

   public int getVersion() {
      return 1;
   }

   public void setAnalogOutputFrequency(int var1) {
      this.a.setAnalogOutputFrequency(this.b, var1);
   }

   public void setAnalogOutputMode(byte var1) {
      this.a.setAnalogOutputMode(this.b, var1);
   }

   public void setAnalogOutputVoltage(int var1) {
      this.a.setAnalogOutputVoltage(this.b, var1);
   }
}
