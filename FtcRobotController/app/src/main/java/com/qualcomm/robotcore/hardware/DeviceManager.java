package com.qualcomm.robotcore.hardware;

import java.util.Map;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.SerialNumber;

public abstract class DeviceManager
{
    public abstract ColorSensor createAdafruitColorSensor(final DeviceInterfaceModule p0, final int p1);
    
    public abstract AnalogInput createAnalogInputDevice(final AnalogInputController p0, final int p1);
    
    public abstract AnalogOutput createAnalogOutputDevice(final AnalogOutputController p0, final int p1);
    
    public DcMotor createDcMotor(final DcMotorController dcMotorController, final int n) {
        return new DcMotor(dcMotorController, n, DcMotor.Direction.FORWARD);
    }
    
    public abstract DeviceInterfaceModule createDeviceInterfaceModule(final SerialNumber p0) throws RobotCoreException, InterruptedException;
    
    public abstract DigitalChannel createDigitalChannelDevice(final DigitalChannelController p0, final int p1);
    
    public abstract I2cDevice createI2cDevice(final I2cController p0, final int p1);
    
    public abstract IrSeekerSensor createIrSeekerSensorV3(final DeviceInterfaceModule p0, final int p1);
    
    public abstract LED createLED(final DigitalChannelController p0, final int p1);
    
    public abstract ColorSensor createModernRoboticsColorSensor(final DeviceInterfaceModule p0, final int p1);
    
    public abstract AccelerationSensor createNxtAccelerationSensor(final LegacyModule p0, final int p1);
    
    public abstract ColorSensor createNxtColorSensor(final LegacyModule p0, final int p1);
    
    public abstract CompassSensor createNxtCompassSensor(final LegacyModule p0, final int p1);
    
    public abstract DcMotorController createNxtDcMotorController(final LegacyModule p0, final int p1);
    
    public abstract GyroSensor createNxtGyroSensor(final LegacyModule p0, final int p1);
    
    public abstract IrSeekerSensor createNxtIrSeekerSensor(final LegacyModule p0, final int p1);
    
    public abstract LightSensor createNxtLightSensor(final LegacyModule p0, final int p1);
    
    public abstract ServoController createNxtServoController(final LegacyModule p0, final int p1);
    
    public abstract TouchSensor createNxtTouchSensor(final LegacyModule p0, final int p1);
    
    public abstract TouchSensorMultiplexer createNxtTouchSensorMultiplexer(final LegacyModule p0, final int p1);
    
    public abstract UltrasonicSensor createNxtUltrasonicSensor(final LegacyModule p0, final int p1);
    
    public abstract OpticalDistanceSensor createOpticalDistanceSensor(final DeviceInterfaceModule p0, final int p1);
    
    public abstract PWMOutput createPwmOutputDevice(final DeviceInterfaceModule p0, final int p1);
    
    public Servo createServo(final ServoController servoController, final int n) {
        return new Servo(servoController, n, Servo.Direction.FORWARD);
    }
    
    public abstract TouchSensor createTouchSensor(final DeviceInterfaceModule p0, final int p1);
    
    public abstract DcMotorController createUsbDcMotorController(final SerialNumber p0) throws RobotCoreException, InterruptedException;
    
    public abstract LegacyModule createUsbLegacyModule(final SerialNumber p0) throws RobotCoreException, InterruptedException;
    
    public abstract ServoController createUsbServoController(final SerialNumber p0) throws RobotCoreException, InterruptedException;
    
    public abstract Map<SerialNumber, DeviceType> scanForUsbDevices() throws RobotCoreException;
    
    public enum DeviceType
    {
        FTDI_USB_UNKNOWN_DEVICE, 
        MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER, 
        MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE, 
        MODERN_ROBOTICS_USB_LEGACY_MODULE, 
        MODERN_ROBOTICS_USB_SENSOR_MUX, 
        MODERN_ROBOTICS_USB_SERVO_CONTROLLER, 
        MODERN_ROBOTICS_USB_UNKNOWN_DEVICE;
    }
}
