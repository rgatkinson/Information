package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.util.SerialNumber;
import java.util.List;

public class LegacyModuleControllerConfiguration extends ControllerConfiguration
{
    public LegacyModuleControllerConfiguration(final String s, final List<DeviceConfiguration> list, final SerialNumber serialNumber) {
        super(s, list, serialNumber, ConfigurationType.LEGACY_MODULE_CONTROLLER);
    }
}
