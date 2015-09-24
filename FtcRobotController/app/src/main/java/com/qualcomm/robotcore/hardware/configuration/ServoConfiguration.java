package com.qualcomm.robotcore.hardware.configuration;

public class ServoConfiguration extends DeviceConfiguration
{
    public ServoConfiguration(final int n) {
        super(n, ConfigurationType.SERVO, "NO DEVICE ATTACHED", false);
    }
    
    public ServoConfiguration(final int n, final String s, final boolean b) {
        super(n, ConfigurationType.SERVO, s, b);
    }
    
    public ServoConfiguration(final String name) {
        super(ConfigurationType.SERVO);
        super.setName(name);
        super.setType(ConfigurationType.SERVO);
    }
}
