package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.HardwareDevice;

public abstract class ColorSensor implements HardwareDevice {
   public abstract int alpha();

   public abstract int argb();

   public abstract int blue();

   public abstract void enableLed(boolean var1);

   public abstract int green();

   public abstract int red();

   public String toString() {
      Object[] var1 = new Object[]{Integer.valueOf(this.argb())};
      return String.format("argb: %d", var1);
   }
}
