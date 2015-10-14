package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ControllerConfiguration extends DeviceConfiguration implements Serializable {
   public static final SerialNumber NO_SERIAL_NUMBER = new SerialNumber("-1");
   private List<DeviceConfiguration> configurations;
   private SerialNumber serialNumber;

   public ControllerConfiguration(String name, SerialNumber serialNumber, DeviceConfiguration.ConfigurationType configurationType) {
      this(name, new ArrayList(), serialNumber, configurationType);
   }

   public ControllerConfiguration(String name, List<DeviceConfiguration> configurations, SerialNumber serialNumber, DeviceConfiguration.ConfigurationType configurationType) {
      super(configurationType);
      super.setName(name);
      this.configurations = configurations;
      this.serialNumber = serialNumber;
   }

   // better called setDevices()
   public void addDevices(List<DeviceConfiguration> var1) {
      this.configurations = var1;
   }

   public DeviceManager.DeviceType configTypeToDeviceType(DeviceConfiguration.ConfigurationType configType) {
      return configType == DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER
              ? DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER
              : (configType == DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER
                  ? DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER
                  : (configType == DeviceConfiguration.ConfigurationType.LEGACY_MODULE_CONTROLLER
                     ? DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE
                     : DeviceManager.DeviceType.FTDI_USB_UNKNOWN_DEVICE));
   }

   public DeviceConfiguration.ConfigurationType deviceTypeToConfigType(DeviceManager.DeviceType var1) {
      return var1 == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER?DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER:(var1 == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER?DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER:(var1 == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE?DeviceConfiguration.ConfigurationType.LEGACY_MODULE_CONTROLLER:DeviceConfiguration.ConfigurationType.NOTHING));
   }

   public List<DeviceConfiguration> getDevices() {
      return this.configurations;
   }

   public SerialNumber getSerialNumber() {
      return this.serialNumber;
   }

   public DeviceConfiguration.ConfigurationType getType() {
      return super.getType();
   }
}
