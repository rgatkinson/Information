package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MotorControllerConfiguration extends ControllerConfiguration implements Serializable {
   public MotorControllerConfiguration() {
      super("", new ArrayList(), new SerialNumber(ControllerConfiguration.NO_SERIAL_NUMBER.getSerialNumber()), DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER);
   }

   public MotorControllerConfiguration(String var1, List<DeviceConfiguration> var2, SerialNumber var3) {
      super(var1, var2, var3, DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER);
   }

   public void addMotors(List<DeviceConfiguration> var1) {
      super.addDevices(var1);
   }

   public List<DeviceConfiguration> getMotors() {
      return super.getDevices();
   }
}
