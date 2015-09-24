package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.HardwareDevice;

public interface DcMotorController extends HardwareDevice {
   DcMotorController.RunMode getMotorChannelMode(int var1);

   DcMotorController.DeviceMode getMotorControllerDeviceMode();

   int getMotorCurrentPosition(int var1);

   double getMotorPower(int var1);

   boolean getMotorPowerFloat(int var1);

   int getMotorTargetPosition(int var1);

   boolean isBusy(int var1);

   void setMotorChannelMode(int var1, DcMotorController.RunMode var2);

   void setMotorControllerDeviceMode(DcMotorController.DeviceMode var1);

   void setMotorPower(int var1, double var2);

   void setMotorPowerFloat(int var1);

   void setMotorTargetPosition(int var1, int var2);

   public static enum DeviceMode {
      READ_ONLY,
      READ_WRITE,
      SWITCHING_TO_READ_MODE,
      SWITCHING_TO_WRITE_MODE,
      WRITE_ONLY;

      static {
         DcMotorController.DeviceMode[] var0 = new DcMotorController.DeviceMode[]{SWITCHING_TO_READ_MODE, SWITCHING_TO_WRITE_MODE, READ_ONLY, WRITE_ONLY, READ_WRITE};
      }
   }

   public static enum RunMode {
      RESET_ENCODERS,
      RUN_TO_POSITION,
      RUN_USING_ENCODERS,
      RUN_WITHOUT_ENCODERS;

      static {
         DcMotorController.RunMode[] var0 = new DcMotorController.RunMode[]{RUN_USING_ENCODERS, RUN_WITHOUT_ENCODERS, RUN_TO_POSITION, RESET_ENCODERS};
      }
   }
}
