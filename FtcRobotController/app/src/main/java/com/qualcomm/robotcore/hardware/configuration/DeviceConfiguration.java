package com.qualcomm.robotcore.hardware.configuration;

import java.io.Serializable;

public class DeviceConfiguration implements Serializable
{
    public static final String DISABLED_DEVICE_NAME = "NO DEVICE ATTACHED";
    private ConfigurationType a;
    private int b;
    private boolean c;
    protected String name;
    
    public DeviceConfiguration(final int b) {
        this.a = ConfigurationType.NOTHING;
        this.c = false;
        this.name = "NO DEVICE ATTACHED";
        this.a = ConfigurationType.NOTHING;
        this.b = b;
        this.c = false;
    }
    
    public DeviceConfiguration(final int b, final ConfigurationType a) {
        this.a = ConfigurationType.NOTHING;
        this.c = false;
        this.name = "NO DEVICE ATTACHED";
        this.a = a;
        this.b = b;
        this.c = false;
    }
    
    public DeviceConfiguration(final int b, final ConfigurationType a, final String name, final boolean c) {
        this.a = ConfigurationType.NOTHING;
        this.c = false;
        this.b = b;
        this.a = a;
        this.name = name;
        this.c = c;
    }
    
    public DeviceConfiguration(final ConfigurationType a) {
        this.a = ConfigurationType.NOTHING;
        this.c = false;
        this.name = "";
        this.a = a;
        this.c = false;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getPort() {
        return this.b;
    }
    
    public ConfigurationType getType() {
        return this.a;
    }
    
    public boolean isEnabled() {
        return this.c;
    }
    
    public void setEnabled(final boolean c) {
        this.c = c;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setPort(final int b) {
        this.b = b;
    }
    
    public void setType(final ConfigurationType a) {
        this.a = a;
    }
    
    public ConfigurationType typeFromString(final String s) {
        for (final ConfigurationType configurationType : ConfigurationType.values()) {
            if (s.equalsIgnoreCase(configurationType.toString())) {
                return configurationType;
            }
        }
        return ConfigurationType.NOTHING;
    }
    
    public enum ConfigurationType
    {
        ACCELEROMETER, 
        ADAFRUIT_COLOR_SENSOR, 
        ANALOG_INPUT, 
        ANALOG_OUTPUT, 
        COLOR_SENSOR, 
        COMPASS, 
        DEVICE_INTERFACE_MODULE, 
        DIGITAL_DEVICE, 
        GYRO, 
        I2C_DEVICE, 
        IR_SEEKER, 
        IR_SEEKER_V3, 
        LED, 
        LEGACY_MODULE_CONTROLLER, 
        LIGHT_SENSOR, 
        MATRIX_CONTROLLER, 
        MOTOR, 
        MOTOR_CONTROLLER, 
        NOTHING, 
        OPTICAL_DISTANCE_SENSOR, 
        OTHER, 
        PULSE_WIDTH_DEVICE, 
        SERVO, 
        SERVO_CONTROLLER, 
        TOUCH_SENSOR, 
        TOUCH_SENSOR_MULTIPLEXER, 
        ULTRASONIC_SENSOR;
    }
}
