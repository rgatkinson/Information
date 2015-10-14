package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.SerialNumber;

import java.util.Map;

public abstract class DeviceManager {
    public abstract ColorSensor createAdafruitI2cColorSensor(DeviceInterfaceModule var1, int var2);

    public abstract AnalogInput createAnalogInputDevice(AnalogInputController var1, int var2);

    public abstract OpticalDistanceSensor createAnalogOpticalDistanceSensor(DeviceInterfaceModule var1, int var2);

    public abstract AnalogOutput createAnalogOutputDevice(AnalogOutputController var1, int var2);

    public DcMotor createDcMotor(DcMotorController var1, int var2) {
        return new DcMotor(var1, var2, DcMotor.Direction.FORWARD);
    }

    public abstract DeviceInterfaceModule createDeviceInterfaceModule(SerialNumber var1) throws RobotCoreException, InterruptedException;

    public abstract DigitalChannel createDigitalChannelDevice(DigitalChannelController var1, int var2);

    public abstract TouchSensor createDigitalTouchSensor(DeviceInterfaceModule var1, int var2);

    public abstract I2cDevice createI2cDevice(I2cController var1, int var2);

    public abstract IrSeekerSensor createI2cIrSeekerSensorV3(DeviceInterfaceModule var1, int var2);

    public abstract LED createLED(DigitalChannelController var1, int var2);

    public abstract ColorSensor createModernRoboticsI2cColorSensor(DeviceInterfaceModule var1, int var2);

    public abstract AccelerationSensor createNxtAccelerationSensor(LegacyModule var1, int var2);

    public abstract ColorSensor createNxtColorSensor(LegacyModule var1, int var2);

    public abstract CompassSensor createNxtCompassSensor(LegacyModule var1, int var2);

    public abstract DcMotorController createNxtDcMotorController(LegacyModule var1, int var2);

    public abstract GyroSensor createNxtGyroSensor(LegacyModule var1, int var2);

    public abstract IrSeekerSensor createNxtIrSeekerSensor(LegacyModule var1, int var2);

    public abstract LightSensor createNxtLightSensor(LegacyModule var1, int var2);

    public abstract ServoController createNxtServoController(LegacyModule var1, int var2);

    public abstract TouchSensor createNxtTouchSensor(LegacyModule var1, int var2);

    public abstract TouchSensorMultiplexer createNxtTouchSensorMultiplexer(LegacyModule var1, int var2);

    public abstract UltrasonicSensor createNxtUltrasonicSensor(LegacyModule var1, int var2);

    public abstract PWMOutput createPwmOutputDevice(DeviceInterfaceModule var1, int var2);

    public Servo createServo(ServoController var1, int var2) {
        return new Servo(var1, var2, Servo.Direction.FORWARD);
    }

    public abstract DcMotorController createUsbDcMotorController(SerialNumber var1) throws RobotCoreException, InterruptedException;

    public abstract LegacyModule createUsbLegacyModule(SerialNumber var1) throws RobotCoreException, InterruptedException;

    public abstract ServoController createUsbServoController(SerialNumber var1) throws RobotCoreException, InterruptedException;

    public abstract Map<SerialNumber, DeviceType> scanForUsbDevices() throws RobotCoreException;

    public enum DeviceType {
        FTDI_USB_UNKNOWN_DEVICE,
        MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER,
        MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE,
        MODERN_ROBOTICS_USB_LEGACY_MODULE,
        MODERN_ROBOTICS_USB_SENSOR_MUX,
        MODERN_ROBOTICS_USB_SERVO_CONTROLLER,
        MODERN_ROBOTICS_USB_UNKNOWN_DEVICE;

        static {
            DeviceType[] var0 = new DeviceType[]{FTDI_USB_UNKNOWN_DEVICE, MODERN_ROBOTICS_USB_UNKNOWN_DEVICE, MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER, MODERN_ROBOTICS_USB_SERVO_CONTROLLER, MODERN_ROBOTICS_USB_LEGACY_MODULE, MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE, MODERN_ROBOTICS_USB_SENSOR_MUX};
        }
    }
}
