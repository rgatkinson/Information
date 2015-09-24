package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ControllerConfiguration extends DeviceConfiguration implements Serializable {
   public static final SerialNumber NO_SERIAL_NUMBER = new SerialNumber("-1");
   private List<DeviceConfiguration> a;
   private SerialNumber b;

   public ControllerConfiguration(String var1, SerialNumber var2, DeviceConfiguration.ConfigurationType var3) {
      this(var1, new ArrayList(), var2, var3);
   }

   public ControllerConfiguration(String var1, List<DeviceConfiguration> var2, SerialNumber var3, DeviceConfiguration.ConfigurationType var4) {
      super(var4);
      super.setName(var1);
      this.a = var2;
      this.b = var3;
   }

   public void addDevices(List<DeviceConfiguration> var1) {
      this.a = var1;
   }

   public DeviceManager.DeviceType configTypeToDeviceType(DeviceConfiguration.ConfigurationType var1) {
      return var1 == DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER?DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER:(var1 == DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER?DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER:(var1 == DeviceConfiguration.ConfigurationType.LEGACY_MODULE_CONTROLLER?DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE:DeviceManager.DeviceType.FTDI_USB_UNKNOWN_DEVICE));
   }

   public DeviceConfiguration.ConfigurationType deviceTypeToConfigType(DeviceManager.DeviceType var1) {
      return var1 == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER?DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER:(var1 == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER?DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER:(var1 == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE?DeviceConfiguration.ConfigurationType.LEGACY_MODULE_CONTROLLER:DeviceConfiguration.ConfigurationType.NOTHING));
   }

   public List<DeviceConfiguration> getDevices() {
      return this.a;
   }

   public SerialNumber getSerialNumber() {
      return this.b;
   }

   public DeviceConfiguration.ConfigurationType getType() {
      return super.getType();
   }
}
