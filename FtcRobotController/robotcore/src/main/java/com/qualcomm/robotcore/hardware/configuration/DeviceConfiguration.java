package com.qualcomm.robotcore.hardware.configuration;

import java.io.Serializable;

public class DeviceConfiguration implements Serializable {
    public static final String DISABLED_DEVICE_NAME = "NO DEVICE ATTACHED";
    protected String name;
    private ConfigurationType a;
    private int b;
    private boolean c;

    public DeviceConfiguration(int var1) {
        this.a = ConfigurationType.NOTHING;
        this.c = false;
        this.name = "NO DEVICE ATTACHED";
        this.a = ConfigurationType.NOTHING;
        this.b = var1;
        this.c = false;
    }

    public DeviceConfiguration(int var1, ConfigurationType var2) {
        this.a = ConfigurationType.NOTHING;
        this.c = false;
        this.name = "NO DEVICE ATTACHED";
        this.a = var2;
        this.b = var1;
        this.c = false;
    }

    public DeviceConfiguration(int var1, ConfigurationType var2, String var3, boolean var4) {
        this.a = ConfigurationType.NOTHING;
        this.c = false;
        this.b = var1;
        this.a = var2;
        this.name = var3;
        this.c = var4;
    }

    public DeviceConfiguration(ConfigurationType var1) {
        this.a = ConfigurationType.NOTHING;
        this.c = false;
        this.name = "";
        this.a = var1;
        this.c = false;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String var1) {
        this.name = var1;
    }

    public int getPort() {
        return this.b;
    }

    public void setPort(int var1) {
        this.b = var1;
    }

    public ConfigurationType getType() {
        return this.a;
    }

    public void setType(ConfigurationType var1) {
        this.a = var1;
    }

    public boolean isEnabled() {
        return this.c;
    }

    public void setEnabled(boolean var1) {
        this.c = var1;
    }

    public ConfigurationType typeFromString(String var1) {
        ConfigurationType[] var2 = ConfigurationType.values();
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            ConfigurationType var5 = var2[var4];
            if (var1.equalsIgnoreCase(var5.toString())) {
                return var5;
            }
        }

        return ConfigurationType.NOTHING;
    }

    public enum ConfigurationType {
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

        static {
            ConfigurationType[] var0 = new ConfigurationType[]{MOTOR, SERVO, GYRO, COMPASS, IR_SEEKER, LIGHT_SENSOR, ACCELEROMETER, MOTOR_CONTROLLER, SERVO_CONTROLLER, LEGACY_MODULE_CONTROLLER, DEVICE_INTERFACE_MODULE, I2C_DEVICE, ANALOG_INPUT, TOUCH_SENSOR, OPTICAL_DISTANCE_SENSOR, ANALOG_OUTPUT, DIGITAL_DEVICE, PULSE_WIDTH_DEVICE, IR_SEEKER_V3, TOUCH_SENSOR_MULTIPLEXER, MATRIX_CONTROLLER, ULTRASONIC_SENSOR, ADAFRUIT_COLOR_SENSOR, COLOR_SENSOR, LED, OTHER, NOTHING};
        }
    }
}
