package com.qualcomm.robotcore.hardware.configuration;

public class MotorConfiguration extends DeviceConfiguration
{
    public MotorConfiguration(final int port) {
        super(ConfigurationType.MOTOR);
        super.setName("NO DEVICE ATTACHED");
        super.setPort(port);
    }
    
    public MotorConfiguration(final int n, final String s, final boolean b) {
        super(n, ConfigurationType.MOTOR, s, b);
    }
    
    public MotorConfiguration(final String name) {
        super(ConfigurationType.MOTOR);
        super.setName(name);
        super.setType(ConfigurationType.MOTOR);
    }
}
