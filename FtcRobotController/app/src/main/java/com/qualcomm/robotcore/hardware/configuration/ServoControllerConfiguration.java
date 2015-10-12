package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.ArrayList;
import java.util.List;

public class ServoControllerConfiguration extends ControllerConfiguration {
   public ServoControllerConfiguration() {
      super("", new ArrayList(), new SerialNumber(ControllerConfiguration.NO_SERIAL_NUMBER.getSerialNumber()), DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER);
   }

   public ServoControllerConfiguration(String var1, List<DeviceConfiguration> var2, SerialNumber var3) {
      super(var1, var2, var3, DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER);
   }

   public void addServos(ArrayList<DeviceConfiguration> var1) {
      super.addDevices(var1);
   }

   public List<DeviceConfiguration> getServos() {
      return super.getDevices();
   }
}
