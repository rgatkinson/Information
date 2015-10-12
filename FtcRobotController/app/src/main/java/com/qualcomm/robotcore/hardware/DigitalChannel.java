package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.HardwareDevice;

public class DigitalChannel implements HardwareDevice {
   private DigitalChannelController a = null;
   private int b = -1;

   public DigitalChannel(DigitalChannelController var1, int var2) {
      this.a = var1;
      this.b = var2;
   }

   public void close() {
   }

   public String getConnectionInfo() {
      return this.a.getConnectionInfo() + "; digital port " + this.b;
   }

   public String getDeviceName() {
      return "Digital Channel";
   }

   public DigitalChannelController.Mode getMode() {
      return this.a.getDigitalChannelMode(this.b);
   }

   public boolean getState() {
      return this.a.getDigitalChannelState(this.b);
   }

   public int getVersion() {
      return 1;
   }

   public void setMode(DigitalChannelController.Mode var1) {
      this.a.setDigitalChannelMode(this.b, var1);
   }

   public void setState(boolean var1) {
      this.a.setDigitalChannelState(this.b, var1);
   }
}
