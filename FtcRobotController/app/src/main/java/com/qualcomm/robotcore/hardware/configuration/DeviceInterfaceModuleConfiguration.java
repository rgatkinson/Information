package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.List;

public class DeviceInterfaceModuleConfiguration extends ControllerConfiguration {
   private List<DeviceConfiguration> a;
   private List<DeviceConfiguration> b;
   private List<DeviceConfiguration> c;
   private List<DeviceConfiguration> d;
   private List<DeviceConfiguration> e;

   public DeviceInterfaceModuleConfiguration(String var1, SerialNumber var2) {
      super(var1, var2, DeviceConfiguration.ConfigurationType.DEVICE_INTERFACE_MODULE);
   }

   public List<DeviceConfiguration> getAnalogInputDevices() {
      return this.c;
   }

   public List<DeviceConfiguration> getAnalogOutputDevices() {
      return this.e;
   }

   public List<DeviceConfiguration> getDigitalDevices() {
      return this.d;
   }

   public List<DeviceConfiguration> getI2cDevices() {
      return this.b;
   }

   public List<DeviceConfiguration> getPwmDevices() {
      return this.a;
   }

   public void setAnalogInputDevices(List<DeviceConfiguration> var1) {
      this.c = var1;
   }

   public void setAnalogOutputDevices(List<DeviceConfiguration> var1) {
      this.e = var1;
   }

   public void setDigitalDevices(List<DeviceConfiguration> var1) {
      this.d = var1;
   }

   public void setI2cDevices(List<DeviceConfiguration> var1) {
      this.b = var1;
   }

   public void setPwmDevices(List<DeviceConfiguration> var1) {
      this.a = var1;
   }
}
