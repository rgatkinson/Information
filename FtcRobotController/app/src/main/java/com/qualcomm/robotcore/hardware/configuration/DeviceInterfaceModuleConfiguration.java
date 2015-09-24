package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.util.SerialNumber;
import java.util.List;

public class DeviceInterfaceModuleConfiguration extends ControllerConfiguration
{
    private List<DeviceConfiguration> a;
    private List<DeviceConfiguration> b;
    private List<DeviceConfiguration> c;
    private List<DeviceConfiguration> d;
    private List<DeviceConfiguration> e;
    
    public DeviceInterfaceModuleConfiguration(final String s, final SerialNumber serialNumber) {
        super(s, serialNumber, ConfigurationType.DEVICE_INTERFACE_MODULE);
    }
    
    public List<DeviceConfiguration> getAnalogInputDevices() {
        return this.c;
    }
    
    public List<DeviceConfiguration> getAnalogOutputDevices() {
        return this.e;
    }
    
    public List<DeviceConfiguration> getDigitalDevices() {
        return this.d;
    }
    
    public List<DeviceConfiguration> getI2cDevices() {
        return this.b;
    }
    
    public List<DeviceConfiguration> getPwmDevices() {
        return this.a;
    }
    
    public void setAnalogInputDevices(final List<DeviceConfiguration> c) {
        this.c = c;
    }
    
    public void setAnalogOutputDevices(final List<DeviceConfiguration> e) {
        this.e = e;
    }
    
    public void setDigitalDevices(final List<DeviceConfiguration> d) {
        this.d = d;
    }
    
    public void setI2cDevices(final List<DeviceConfiguration> b) {
        this.b = b;
    }
    
    public void setPwmDevices(final List<DeviceConfiguration> a) {
        this.a = a;
    }
}
