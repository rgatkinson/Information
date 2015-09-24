package com.qualcomm.robotcore.hardware.configuration;

import java.util.List;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.ArrayList;

public class ServoControllerConfiguration extends ControllerConfiguration
{
    public ServoControllerConfiguration() {
        super("", new ArrayList<DeviceConfiguration>(), new SerialNumber(ControllerConfiguration.NO_SERIAL_NUMBER.getSerialNumber()), ConfigurationType.SERVO_CONTROLLER);
    }
    
    public ServoControllerConfiguration(final String s, final List<DeviceConfiguration> list, final SerialNumber serialNumber) {
        super(s, list, serialNumber, ConfigurationType.SERVO_CONTROLLER);
    }
    
    public void addServos(final ArrayList<DeviceConfiguration> list) {
        super.addDevices(list);
    }
    
    public List<DeviceConfiguration> getServos() {
        return super.getDevices();
    }
}
