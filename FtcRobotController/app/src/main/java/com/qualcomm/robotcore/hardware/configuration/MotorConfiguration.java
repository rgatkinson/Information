package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;

public class MotorConfiguration extends DeviceConfiguration {
   public MotorConfiguration(int var1) {
      super(DeviceConfiguration.ConfigurationType.MOTOR);
      super.setName("NO DEVICE ATTACHED");
      super.setPort(var1);
   }

   public MotorConfiguration(int var1, String var2, boolean var3) {
      super(var1, DeviceConfiguration.ConfigurationType.MOTOR, var2, var3);
   }

   public MotorConfiguration(String var1) {
      super(DeviceConfiguration.ConfigurationType.MOTOR);
      super.setName(var1);
      super.setType(DeviceConfiguration.ConfigurationType.MOTOR);
   }
}
