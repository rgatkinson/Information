package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.HardwareDevice;

public interface ServoController extends HardwareDevice {
   ServoController.PwmStatus getPwmStatus();

   double getServoPosition(int var1);

   void pwmDisable();

   void pwmEnable();

   void setServoPosition(int var1, double var2);

   public static enum PwmStatus {
      DISABLED,
      ENABLED;

      static {
         ServoController.PwmStatus[] var0 = new ServoController.PwmStatus[]{ENABLED, DISABLED};
      }
   }
}
