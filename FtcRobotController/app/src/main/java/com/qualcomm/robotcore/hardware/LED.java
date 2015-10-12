package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.HardwareDevice;

public class LED implements HardwareDevice {
   private DigitalChannelController a = null;
   private int b = -1;

   public LED(DigitalChannelController var1, int var2) {
      this.a = var1;
      this.b = var2;
      var1.setDigitalChannelMode(var2, DigitalChannelController.Mode.OUTPUT);
   }

   public void close() {
   }

   public void enable(boolean var1) {
      this.a.setDigitalChannelState(this.b, var1);
   }

   public String getConnectionInfo() {
      return null;
   }

   public String getDeviceName() {
      return null;
   }

   public int getVersion() {
      return 0;
   }
}
