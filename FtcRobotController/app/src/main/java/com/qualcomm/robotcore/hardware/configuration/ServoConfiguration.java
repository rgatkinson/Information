package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;

public class ServoConfiguration extends DeviceConfiguration {
   public ServoConfiguration(int var1) {
      super(var1, DeviceConfiguration.ConfigurationType.SERVO, "NO DEVICE ATTACHED", false);
   }

   public ServoConfiguration(int var1, String var2, boolean var3) {
      super(var1, DeviceConfiguration.ConfigurationType.SERVO, var2, var3);
   }

   public ServoConfiguration(String var1) {
      super(DeviceConfiguration.ConfigurationType.SERVO);
      super.setName(var1);
      super.setType(DeviceConfiguration.ConfigurationType.SERVO);
   }
}
