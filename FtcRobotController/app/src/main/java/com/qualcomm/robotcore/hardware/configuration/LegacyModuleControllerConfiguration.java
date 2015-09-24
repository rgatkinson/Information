package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.List;

public class LegacyModuleControllerConfiguration extends ControllerConfiguration {
   public LegacyModuleControllerConfiguration(String var1, List<DeviceConfiguration> var2, SerialNumber var3) {
      super(var1, var2, var3, DeviceConfiguration.ConfigurationType.LEGACY_MODULE_CONTROLLER);
   }
}
