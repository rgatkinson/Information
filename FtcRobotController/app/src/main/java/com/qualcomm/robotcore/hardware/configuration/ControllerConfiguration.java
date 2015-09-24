package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.hardware.DeviceManager;
import java.util.ArrayList;
import java.util.List;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.Serializable;

public class ControllerConfiguration extends DeviceConfiguration implements Serializable
{
    public static final SerialNumber NO_SERIAL_NUMBER;
    private List<DeviceConfiguration> a;
    private SerialNumber b;
    
    static {
        NO_SERIAL_NUMBER = new SerialNumber("-1");
    }
    
    public ControllerConfiguration(final String s, final SerialNumber serialNumber, final ConfigurationType configurationType) {
        this(s, new ArrayList<DeviceConfiguration>(), serialNumber, configurationType);
    }
    
    public ControllerConfiguration(final String name, final List<DeviceConfiguration> a, final SerialNumber b, final ConfigurationType configurationType) {
        super(configurationType);
        super.setName(name);
        this.a = a;
        this.b = b;
    }
    
    public void addDevices(final List<DeviceConfiguration> a) {
        this.a = a;
    }
    
    public DeviceManager.DeviceType configTypeToDeviceType(final ConfigurationType configurationType) {
        if (configurationType == ConfigurationType.MOTOR_CONTROLLER) {
            return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER;
        }
        if (configurationType == ConfigurationType.SERVO_CONTROLLER) {
            return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER;
        }
        if (configurationType == ConfigurationType.LEGACY_MODULE_CONTROLLER) {
            return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE;
        }
        return DeviceManager.DeviceType.FTDI_USB_UNKNOWN_DEVICE;
    }
    
    public ConfigurationType deviceTypeToConfigType(final DeviceManager.DeviceType deviceType) {
        if (deviceType == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER) {
            return ConfigurationType.MOTOR_CONTROLLER;
        }
        if (deviceType == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER) {
            return ConfigurationType.SERVO_CONTROLLER;
        }
        if (deviceType == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE) {
            return ConfigurationType.LEGACY_MODULE_CONTROLLER;
        }
        return ConfigurationType.NOTHING;
    }
    
    public List<DeviceConfiguration> getDevices() {
        return this.a;
    }
    
    public SerialNumber getSerialNumber() {
        return this.b;
    }
    
    @Override
    public ConfigurationType getType() {
        return super.getType();
    }
}
