package com.qualcomm.hardware;

import java.util.HashMap;
import java.util.Map;
import com.qualcomm.robotcore.hardware.PWMOutputController;
import com.qualcomm.robotcore.hardware.PWMOutput;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.hardware.TouchSensorMultiplexer;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.modernrobotics.ModernRoboticsUsbUtil;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.hardware.AnalogOutput;
import com.qualcomm.robotcore.hardware.AnalogOutputController;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.modernrobotics.RobotUsbManagerEmulator;
import com.qualcomm.robotcore.hardware.usb.ftdi.RobotUsbManagerFtdi;
import android.content.Context;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
import com.qualcomm.robotcore.hardware.DeviceManager;

public class ModernRoboticsDeviceManager extends DeviceManager
{
    private static a a;
    private RobotUsbManager b;
    private final EventLoopManager c;
    
    static {
        ModernRoboticsDeviceManager.a = ModernRoboticsDeviceManager.a.a;
    }
    
    public ModernRoboticsDeviceManager(final Context context, final EventLoopManager c) throws RobotCoreException {
        this.c = c;
        switch (ModernRoboticsDeviceManager$1.a[ModernRoboticsDeviceManager.a.ordinal()]) {
            default: {
                this.b = new RobotUsbManagerFtdi(context);
            }
            case 1: {
                this.b = new RobotUsbManagerEmulator();
            }
        }
    }
    
    private ModernRoboticsUsbDeviceInterfaceModule a(final DeviceInterfaceModule deviceInterfaceModule) {
        if (!(deviceInterfaceModule instanceof ModernRoboticsUsbDeviceInterfaceModule)) {
            throw new IllegalArgumentException("Modern Robotics Device Manager needs a Modern Robotics Device Interface Module");
        }
        return (ModernRoboticsUsbDeviceInterfaceModule)deviceInterfaceModule;
    }
    
    private ModernRoboticsUsbLegacyModule a(final LegacyModule legacyModule) {
        if (!(legacyModule instanceof ModernRoboticsUsbLegacyModule)) {
            throw new IllegalArgumentException("Modern Robotics Device Manager needs a Modern Robotics LegacyModule");
        }
        return (ModernRoboticsUsbLegacyModule)legacyModule;
    }
    
    private void a(final String s) throws RobotCoreException {
        System.err.println(s);
        throw new RobotCoreException(s);
    }
    
    public static void disableDeviceEmulation() {
        ModernRoboticsDeviceManager.a = ModernRoboticsDeviceManager.a.a;
    }
    
    public static void enableDeviceEmulation() {
        ModernRoboticsDeviceManager.a = ModernRoboticsDeviceManager.a.b;
    }
    
    @Override
    public ColorSensor createAdafruitColorSensor(final DeviceInterfaceModule deviceInterfaceModule, final int n) {
        RobotLog.v("Creating Adafruit Color Sensor - Port: " + n);
        return new AdafruitColorSensor(deviceInterfaceModule, n);
    }
    
    @Override
    public AnalogInput createAnalogInputDevice(final AnalogInputController analogInputController, final int n) {
        RobotLog.v("Creating Analog Input Device - Port: " + n);
        return new AnalogInput(analogInputController, n);
    }
    
    @Override
    public AnalogOutput createAnalogOutputDevice(final AnalogOutputController analogOutputController, final int n) {
        RobotLog.v("Creating Analog Output Device - Port: " + n);
        return new AnalogOutput(analogOutputController, n);
    }
    
    @Override
    public DeviceInterfaceModule createDeviceInterfaceModule(final SerialNumber serialNumber) throws RobotCoreException, InterruptedException {
        RobotLog.v("Creating Modern Robotics USB Core Device Interface Module - " + serialNumber.toString());
        try {
            final RobotUsbDevice openUsbDevice = ModernRoboticsUsbUtil.openUsbDevice(this.b, serialNumber);
            if (ModernRoboticsUsbUtil.getDeviceType(ModernRoboticsUsbUtil.getUsbDeviceHeader(openUsbDevice)) != DeviceType.MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE) {
                this.a(serialNumber.toString() + " is not a Modern Robotics USB Core Device Interface Module");
            }
            return new ModernRoboticsUsbDeviceInterfaceModule(serialNumber, openUsbDevice, this.c);
        }
        catch (RobotCoreException ex) {
            RobotLog.setGlobalErrorMsgAndThrow("Unable to open Modern Robotics USB Core Device Interface Module", ex);
            return null;
        }
    }
    
    @Override
    public DigitalChannel createDigitalChannelDevice(final DigitalChannelController digitalChannelController, final int n) {
        RobotLog.v("Creating Digital Channel Device - Port: " + n);
        return new DigitalChannel(digitalChannelController, n);
    }
    
    @Override
    public I2cDevice createI2cDevice(final I2cController i2cController, final int n) {
        RobotLog.v("Creating I2C Device - Port: " + n);
        return new I2cDevice(i2cController, n);
    }
    
    @Override
    public IrSeekerSensor createIrSeekerSensorV3(final DeviceInterfaceModule deviceInterfaceModule, final int n) {
        RobotLog.v("Creating Modern Robotics IR Seeker Sensor V3 - Port: " + n);
        return new ModernRoboticsIrSeekerSensorV3(this.a(deviceInterfaceModule), n);
    }
    
    @Override
    public LED createLED(final DigitalChannelController digitalChannelController, final int n) {
        RobotLog.v("Creating LED - Port: " + n);
        return new LED(digitalChannelController, n);
    }
    
    @Override
    public ColorSensor createModernRoboticsColorSensor(final DeviceInterfaceModule deviceInterfaceModule, final int n) {
        RobotLog.v("Creating ModernRobotics Color Sensor - Port: " + n);
        return new ModernRoboticsColorSensor(deviceInterfaceModule, n);
    }
    
    @Override
    public AccelerationSensor createNxtAccelerationSensor(final LegacyModule legacyModule, final int n) {
        RobotLog.v("Creating Modern Robotics NXT Acceleration Sensor - Port: " + n);
        return new ModernRoboticsNxtAccelerationSensor(this.a(legacyModule), n);
    }
    
    @Override
    public ColorSensor createNxtColorSensor(final LegacyModule legacyModule, final int n) {
        RobotLog.v("Creating NXT Color Sensor - Port: " + n);
        return new ModernRoboticsNxtColorSensor(legacyModule, n);
    }
    
    @Override
    public CompassSensor createNxtCompassSensor(final LegacyModule legacyModule, final int n) {
        RobotLog.v("Creating Modern Robotics NXT Compass Sensor - Port: " + n);
        return new ModernRoboticsNxtCompassSensor(this.a(legacyModule), n);
    }
    
    @Override
    public DcMotorController createNxtDcMotorController(final LegacyModule legacyModule, final int n) {
        RobotLog.v("Creating Modern Robotics NXT DC Motor Controller - Port: " + n);
        return new ModernRoboticsNxtDcMotorController(this.a(legacyModule), n);
    }
    
    @Override
    public GyroSensor createNxtGyroSensor(final LegacyModule legacyModule, final int n) {
        RobotLog.v("Creating Modern Robotics NXT Gyro Sensor - Port: " + n);
        return new ModernRoboticsNxtGyroSensor(this.a(legacyModule), n);
    }
    
    @Override
    public IrSeekerSensor createNxtIrSeekerSensor(final LegacyModule legacyModule, final int n) {
        RobotLog.v("Creating Modern Robotics NXT IR Seeker Sensor - Port: " + n);
        return new ModernRoboticsNxtIrSeekerSensor(this.a(legacyModule), n);
    }
    
    @Override
    public LightSensor createNxtLightSensor(final LegacyModule legacyModule, final int n) {
        RobotLog.v("Creating Modern Robotics NXT Light Sensor - Port: " + n);
        return new ModernRoboticsNxtLightSensor(this.a(legacyModule), n);
    }
    
    @Override
    public ServoController createNxtServoController(final LegacyModule legacyModule, final int n) {
        RobotLog.v("Creating Modern Robotics NXT Servo Controller - Port: " + n);
        return new ModernRoboticsNxtServoController(this.a(legacyModule), n);
    }
    
    @Override
    public TouchSensor createNxtTouchSensor(final LegacyModule legacyModule, final int n) {
        RobotLog.v("Creating Modern Robotics NXT Touch Sensor - Port: " + n);
        return new ModernRoboticsNxtTouchSensor(this.a(legacyModule), n);
    }
    
    @Override
    public TouchSensorMultiplexer createNxtTouchSensorMultiplexer(final LegacyModule legacyModule, final int n) {
        RobotLog.v("Creating Modern Robotics NXT Touch Sensor Multiplexer - Port: " + n);
        return new ModernRoboticsNxtTouchSensorMultiplexer(this.a(legacyModule), n);
    }
    
    @Override
    public UltrasonicSensor createNxtUltrasonicSensor(final LegacyModule legacyModule, final int n) {
        RobotLog.v("Creating Modern Robotics NXT Ultrasonic Sensor - Port: " + n);
        return new ModernRoboticsNxtUltrasonicSensor(this.a(legacyModule), n);
    }
    
    @Override
    public OpticalDistanceSensor createOpticalDistanceSensor(final DeviceInterfaceModule deviceInterfaceModule, final int n) {
        RobotLog.v("Creating Modern Robotics Optical Distance Sensor - Port: " + n);
        return new ModernRoboticsOpticalDistanceSensor(this.a(deviceInterfaceModule), n);
    }
    
    @Override
    public PWMOutput createPwmOutputDevice(final DeviceInterfaceModule deviceInterfaceModule, final int n) {
        RobotLog.v("Creating PWM Output Device - Port: " + n);
        return new PWMOutput(deviceInterfaceModule, n);
    }
    
    @Override
    public TouchSensor createTouchSensor(final DeviceInterfaceModule deviceInterfaceModule, final int n) {
        RobotLog.v("Creating Modern Robotics Touch Sensor - Port: " + n);
        return new ModernRoboticsTouchSensor(this.a(deviceInterfaceModule), n);
    }
    
    @Override
    public DcMotorController createUsbDcMotorController(final SerialNumber serialNumber) throws RobotCoreException, InterruptedException {
        RobotLog.v("Creating Modern Robotics USB DC Motor Controller - " + serialNumber.toString());
        try {
            final RobotUsbDevice openUsbDevice = ModernRoboticsUsbUtil.openUsbDevice(this.b, serialNumber);
            if (ModernRoboticsUsbUtil.getDeviceType(ModernRoboticsUsbUtil.getUsbDeviceHeader(openUsbDevice)) != DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER) {
                this.a(serialNumber.toString() + " is not a Modern Robotics USB DC Motor Controller");
            }
            return new ModernRoboticsUsbDcMotorController(serialNumber, openUsbDevice, this.c);
        }
        catch (RobotCoreException ex) {
            RobotLog.setGlobalErrorMsgAndThrow("Unable to open Modern Robotics USB DC Motor Controller", ex);
            return null;
        }
    }
    
    @Override
    public LegacyModule createUsbLegacyModule(final SerialNumber serialNumber) throws RobotCoreException, InterruptedException {
        RobotLog.v("Creating Modern Robotics USB Legacy Module - " + serialNumber.toString());
        try {
            final RobotUsbDevice openUsbDevice = ModernRoboticsUsbUtil.openUsbDevice(this.b, serialNumber);
            if (ModernRoboticsUsbUtil.getDeviceType(ModernRoboticsUsbUtil.getUsbDeviceHeader(openUsbDevice)) != DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE) {
                this.a(serialNumber.toString() + " is not a Modern Robotics USB Legacy Module");
            }
            return new ModernRoboticsUsbLegacyModule(serialNumber, openUsbDevice, this.c);
        }
        catch (RobotCoreException ex) {
            RobotLog.setGlobalErrorMsgAndThrow("Unable to open Modern Robotics USB Legacy Module", ex);
            return null;
        }
    }
    
    @Override
    public ServoController createUsbServoController(final SerialNumber serialNumber) throws RobotCoreException, InterruptedException {
        RobotLog.v("Creating Modern Robotics USB Servo Controller - " + serialNumber.toString());
        try {
            final RobotUsbDevice openUsbDevice = ModernRoboticsUsbUtil.openUsbDevice(this.b, serialNumber);
            if (ModernRoboticsUsbUtil.getDeviceType(ModernRoboticsUsbUtil.getUsbDeviceHeader(openUsbDevice)) != DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER) {
                this.a(serialNumber.toString() + " is not a Modern Robotics USB Servo Controller");
            }
            return new ModernRoboticsUsbServoController(serialNumber, openUsbDevice, this.c);
        }
        catch (RobotCoreException ex) {
            RobotLog.setGlobalErrorMsgAndThrow("Unable to open Modern Robotics USB Servo Controller", ex);
            return null;
        }
    }
    
    @Override
    public Map<SerialNumber, DeviceType> scanForUsbDevices() throws RobotCoreException {
        final HashMap<SerialNumber, DeviceType> hashMap = new HashMap<SerialNumber, DeviceType>();
        try {
            for (int scanForDevices = this.b.scanForDevices(), i = 0; i < scanForDevices; ++i) {
                final SerialNumber deviceSerialNumberByIndex = this.b.getDeviceSerialNumberByIndex(i);
                final RobotUsbDevice openUsbDevice = ModernRoboticsUsbUtil.openUsbDevice(this.b, deviceSerialNumberByIndex);
                hashMap.put(deviceSerialNumberByIndex, ModernRoboticsUsbUtil.getDeviceType(ModernRoboticsUsbUtil.getUsbDeviceHeader(openUsbDevice)));
                openUsbDevice.close();
            }
        }
        catch (RobotCoreException ex) {
            RobotLog.setGlobalErrorMsgAndThrow("Error while scanning for USB devices", ex);
        }
        return hashMap;
    }
    
    private enum a
    {
        a, 
        b;
    }
}
