package com.qualcomm.robotcore.hardware.configuration;

import java.util.ArrayList;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.List;

public class MatrixControllerConfiguration extends ControllerConfiguration
{
    private List<DeviceConfiguration> a;
    private List<DeviceConfiguration> b;
    
    public MatrixControllerConfiguration(final String s, final List<DeviceConfiguration> b, final List<DeviceConfiguration> a, final SerialNumber serialNumber) {
        super(s, serialNumber, ConfigurationType.MATRIX_CONTROLLER);
        this.a = a;
        this.b = b;
    }
    
    public void addMotors(final ArrayList<DeviceConfiguration> b) {
        this.b = b;
    }
    
    public void addServos(final ArrayList<DeviceConfiguration> a) {
        this.a = a;
    }
    
    public List<DeviceConfiguration> getMotors() {
        return this.b;
    }
    
    public List<DeviceConfiguration> getServos() {
        return this.a;
    }
}
