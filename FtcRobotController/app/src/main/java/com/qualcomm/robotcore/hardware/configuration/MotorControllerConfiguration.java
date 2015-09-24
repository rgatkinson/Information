package com.qualcomm.robotcore.hardware.configuration;

import java.util.List;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.ArrayList;
import java.io.Serializable;

public class MotorControllerConfiguration extends ControllerConfiguration implements Serializable
{
    public MotorControllerConfiguration() {
        super("", new ArrayList<DeviceConfiguration>(), new SerialNumber(ControllerConfiguration.NO_SERIAL_NUMBER.getSerialNumber()), ConfigurationType.MOTOR_CONTROLLER);
    }
    
    public MotorControllerConfiguration(final String s, final List<DeviceConfiguration> list, final SerialNumber serialNumber) {
        super(s, list, serialNumber, ConfigurationType.MOTOR_CONTROLLER);
    }
    
    public void addMotors(final List<DeviceConfiguration> list) {
        super.addDevices(list);
    }
    
    public List<DeviceConfiguration> getMotors() {
        return super.getDevices();
    }
}
