package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.util.SerialNumber;

import java.util.ArrayList;
import java.util.List;

public class MatrixControllerConfiguration extends ControllerConfiguration {
    private List<DeviceConfiguration> a;
    private List<DeviceConfiguration> b;

    public MatrixControllerConfiguration(String var1, List<DeviceConfiguration> var2, List<DeviceConfiguration> var3, SerialNumber var4) {
        super(var1, var4, DeviceConfiguration.ConfigurationType.MATRIX_CONTROLLER);
        this.a = var3;
        this.b = var2;
    }

    public void addMotors(ArrayList<DeviceConfiguration> var1) {
        this.b = var1;
    }

    public void addServos(ArrayList<DeviceConfiguration> var1) {
        this.a = var1;
    }

    public List<DeviceConfiguration> getMotors() {
        return this.b;
    }

    public List<DeviceConfiguration> getServos() {
        return this.a;
    }
}
