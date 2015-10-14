//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.hardware;

import android.content.Context;

import com.qualcomm.modernrobotics.ModernRoboticsUsbUtil;
import com.qualcomm.modernrobotics.RobotUsbManagerEmulator;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.hardware.AnalogOutput;
import com.qualcomm.robotcore.hardware.AnalogOutputController;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.PWMOutput;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.TouchSensorMultiplexer;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
import com.qualcomm.robotcore.hardware.usb.ftdi.RobotUsbManagerFtdi;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.HashMap;
import java.util.Map;

public class HardwareDeviceManager extends DeviceManager {
    private static EnumA enumA;
    private RobotUsbManager robotUsbManager;
    private final EventLoopManager eventLoopManager;

    public HardwareDeviceManager(Context context, EventLoopManager manager) throws RobotCoreException {
        this.eventLoopManager = manager;
        switch(HardwareDeviceManager.SyntheticClass_1.a[enumA.ordinal()]) {
        case 1:
            this.robotUsbManager = new RobotUsbManagerEmulator();
            break;
        default:
            this.robotUsbManager = new RobotUsbManagerFtdi(context);
        }

    }

    public Map<SerialNumber, DeviceType> scanForUsbDevices() throws RobotCoreException {
        HashMap mapSerialNumberToDeviceType = new HashMap();

        try {
            int numberOfDevices = this.robotUsbManager.scanForDevices();

            for(int iDevice = 0; iDevice < numberOfDevices; ++iDevice) {
                SerialNumber serialNumber = this.robotUsbManager.getDeviceSerialNumberByIndex(iDevice);

                RobotUsbDevice robotUsbDevice = ModernRoboticsUsbUtil.openUsbDevice(this.robotUsbManager, serialNumber);
                mapSerialNumberToDeviceType.put(serialNumber, ModernRoboticsUsbUtil.getDeviceType(ModernRoboticsUsbUtil.getUsbDeviceHeader(robotUsbDevice)));
                robotUsbDevice.close();
            }
        } catch (RobotCoreException var6) {
            RobotLog.setGlobalErrorMsgAndThrow("Error while scanning for USB devices", var6);
        }

        return mapSerialNumberToDeviceType;
    }

    public DcMotorController createUsbDcMotorController(SerialNumber serialNumber) throws RobotCoreException, InterruptedException {
        RobotLog.v("Creating Modern Robotics USB DC Motor Controller - " + serialNumber.toString());
        ModernRoboticsUsbDcMotorController var2 = null;

        try {
            RobotUsbDevice var3 = ModernRoboticsUsbUtil.openUsbDevice(this.robotUsbManager, serialNumber);
            byte[] var4 = ModernRoboticsUsbUtil.getUsbDeviceHeader(var3);
            DeviceType var5 = ModernRoboticsUsbUtil.getDeviceType(var4);
            if(var5 != DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER) {
                this.printErrAndThrow(serialNumber.toString() + " is not a Modern Robotics USB DC Motor Controller");
            }

            var2 = new ModernRoboticsUsbDcMotorController(serialNumber, var3, this.eventLoopManager);
        } catch (RobotCoreException var6) {
            RobotLog.setGlobalErrorMsgAndThrow("Unable to open Modern Robotics USB DC Motor Controller", var6);
        }

        return var2;
    }

    public ServoController createUsbServoController(SerialNumber serialNumber) throws RobotCoreException, InterruptedException {
        RobotLog.v("Creating Modern Robotics USB Servo Controller - " + serialNumber.toString());
        ModernRoboticsUsbServoController var2 = null;

        try {
            RobotUsbDevice var3 = ModernRoboticsUsbUtil.openUsbDevice(this.robotUsbManager, serialNumber);
            byte[] var4 = ModernRoboticsUsbUtil.getUsbDeviceHeader(var3);
            DeviceType var5 = ModernRoboticsUsbUtil.getDeviceType(var4);
            if(var5 != DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER) {
                this.printErrAndThrow(serialNumber.toString() + " is not a Modern Robotics USB Servo Controller");
            }

            var2 = new ModernRoboticsUsbServoController(serialNumber, var3, this.eventLoopManager);
        } catch (RobotCoreException var6) {
            RobotLog.setGlobalErrorMsgAndThrow("Unable to open Modern Robotics USB Servo Controller", var6);
        }

        return var2;
    }

    public DeviceInterfaceModule createDeviceInterfaceModule(SerialNumber serialNumber) throws RobotCoreException, InterruptedException {
        RobotLog.v("Creating Modern Robotics USB Core Device Interface Module - " + serialNumber.toString());
        ModernRoboticsUsbDeviceInterfaceModule var2 = null;

        try {
            RobotUsbDevice robotUsbDevice = ModernRoboticsUsbUtil.openUsbDevice(this.robotUsbManager, serialNumber);
            byte[] var4 = ModernRoboticsUsbUtil.getUsbDeviceHeader(robotUsbDevice);
            DeviceType var5 = ModernRoboticsUsbUtil.getDeviceType(var4);
            if(var5 != DeviceType.MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE) {
                this.printErrAndThrow(serialNumber.toString() + " is not a Modern Robotics USB Core Device Interface Module");
            }

            var2 = new ModernRoboticsUsbDeviceInterfaceModule(serialNumber, robotUsbDevice, this.eventLoopManager);
        } catch (RobotCoreException var6) {
            RobotLog.setGlobalErrorMsgAndThrow("Unable to open Modern Robotics USB Core Device Interface Module", var6);
        }

        return var2;
    }

    public LegacyModule createUsbLegacyModule(SerialNumber serialNumber) throws RobotCoreException, InterruptedException {
        RobotLog.v("Creating Modern Robotics USB Legacy Module - " + serialNumber.toString());
        ModernRoboticsUsbLegacyModule legacyModule = null;

        try {
            RobotUsbDevice robotUsbDevice = ModernRoboticsUsbUtil.openUsbDevice(this.robotUsbManager, serialNumber);

            byte[] usbDeviceHeader = ModernRoboticsUsbUtil.getUsbDeviceHeader(robotUsbDevice);
            DeviceType deviceType = ModernRoboticsUsbUtil.getDeviceType(usbDeviceHeader);
            if(deviceType != DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE) {
                this.printErrAndThrow(serialNumber.toString() + " is not a Modern Robotics USB Legacy Module");
            }

            legacyModule = new ModernRoboticsUsbLegacyModule(serialNumber, robotUsbDevice, this.eventLoopManager);
        } catch (RobotCoreException var6) {
            RobotLog.setGlobalErrorMsgAndThrow("Unable to open Modern Robotics USB Legacy Module", var6);
        }

        return legacyModule;
    }

    public DcMotorController createNxtDcMotorController(LegacyModule legacyModule, int physicalPort) {
        RobotLog.v("Creating HiTechnic NXT DC Motor Controller - Port: " + physicalPort);
        return new HiTechnicNxtDcMotorController(this.a(legacyModule), physicalPort);
    }

    public ServoController createNxtServoController(LegacyModule legacyModule, int physicalPort) {
        RobotLog.v("Creating HiTechnic NXT Servo Controller - Port: " + physicalPort);
        return new HiTechnicNxtServoController(this.a(legacyModule), physicalPort);
    }

    public CompassSensor createNxtCompassSensor(LegacyModule legacyModule, int physicalPort) {
        RobotLog.v("Creating HiTechnic NXT Compass Sensor - Port: " + physicalPort);
        return new HiTechnicNxtCompassSensor(this.a(legacyModule), physicalPort);
    }

    public TouchSensor createDigitalTouchSensor(DeviceInterfaceModule deviceInterfaceModule, int physicalPort) {
        RobotLog.v("Creating Modern Robotics Digital Touch Sensor - Port: " + physicalPort);
        return new ModernRoboticsDigitalTouchSensor(this.a(deviceInterfaceModule), physicalPort);
    }

    public AccelerationSensor createNxtAccelerationSensor(LegacyModule legacyModule, int physicalPort) {
        RobotLog.v("Creating HiTechnic NXT Acceleration Sensor - Port: " + physicalPort);
        return new HiTechnicNxtAccelerationSensor(this.a(legacyModule), physicalPort);
    }

    public LightSensor createNxtLightSensor(LegacyModule legacyModule, int physicalPort) {
        RobotLog.v("Creating HiTechnic NXT Light Sensor - Port: " + physicalPort);
        return new HiTechnicNxtLightSensor(this.a(legacyModule), physicalPort);
    }

    public GyroSensor createNxtGyroSensor(LegacyModule legacyModule, int physicalPort) {
        RobotLog.v("Creating HiTechnic NXT Gyro Sensor - Port: " + physicalPort);
        return new HiTechnicNxtGyroSensor(this.a(legacyModule), physicalPort);
    }

    public IrSeekerSensor createNxtIrSeekerSensor(LegacyModule legacyModule, int physicalPort) {
        RobotLog.v("Creating HiTechnic NXT IR Seeker Sensor - Port: " + physicalPort);
        return new HiTechnicNxtIrSeekerSensor(this.a(legacyModule), physicalPort);
    }

    public IrSeekerSensor createI2cIrSeekerSensorV3(DeviceInterfaceModule deviceInterfaceModule, int physicalPort) {
        RobotLog.v("Creating Modern Robotics I2C IR Seeker Sensor V3 - Port: " + physicalPort);
        return new ModernRoboticsI2cIrSeekerSensorV3(this.a(deviceInterfaceModule), physicalPort);
    }

    public UltrasonicSensor createNxtUltrasonicSensor(LegacyModule legacyModule, int physicalPort) {
        RobotLog.v("Creating HiTechnic NXT Ultrasonic Sensor - Port: " + physicalPort);
        return new HiTechnicNxtUltrasonicSensor(this.a(legacyModule), physicalPort);
    }

    public OpticalDistanceSensor createAnalogOpticalDistanceSensor(DeviceInterfaceModule deviceInterfaceModule, int physicalPort) {
        RobotLog.v("Creating Modern Robotics Analog Optical Distance Sensor - Port: " + physicalPort);
        return new ModernRoboticsAnalogOpticalDistanceSensor(this.a(deviceInterfaceModule), physicalPort);
    }

    public TouchSensor createNxtTouchSensor(LegacyModule legacyModule, int physicalPort) {
        RobotLog.v("Creating HiTechnic NXT Touch Sensor - Port: " + physicalPort);
        return new HiTechnicNxtTouchSensor(this.a(legacyModule), physicalPort);
    }

    public TouchSensorMultiplexer createNxtTouchSensorMultiplexer(LegacyModule legacyModule, int port) {
        RobotLog.v("Creating HiTechnic NXT Touch Sensor Multiplexer - Port: " + port);
        return new HiTechnicNxtTouchSensorMultiplexer(this.a(legacyModule), port);
    }

    public AnalogInput createAnalogInputDevice(AnalogInputController controller, int channel) {
        RobotLog.v("Creating Analog Input Device - Port: " + channel);
        return new AnalogInput(controller, channel);
    }

    public AnalogOutput createAnalogOutputDevice(AnalogOutputController controller, int channel) {
        RobotLog.v("Creating Analog Output Device - Port: " + channel);
        return new AnalogOutput(controller, channel);
    }

    public DigitalChannel createDigitalChannelDevice(DigitalChannelController controller, int channel) {
        RobotLog.v("Creating Digital Channel Device - Port: " + channel);
        return new DigitalChannel(controller, channel);
    }

    public PWMOutput createPwmOutputDevice(DeviceInterfaceModule controller, int channel) {
        RobotLog.v("Creating PWM Output Device - Port: " + channel);
        return new PWMOutput(controller, channel);
    }

    public I2cDevice createI2cDevice(I2cController controller, int channel) {
        RobotLog.v("Creating I2C Device - Port: " + channel);
        return new I2cDevice(controller, channel);
    }

    public ColorSensor createAdafruitI2cColorSensor(DeviceInterfaceModule controller, int channel) {
        RobotLog.v("Creating Adafruit I2C Color Sensor - Port: " + channel);
        return new AdafruitI2cColorSensor(controller, channel);
    }

    public ColorSensor createNxtColorSensor(LegacyModule controller, int channel) {
        RobotLog.v("Creating HiTechnic NXT Color Sensor - Port: " + channel);
        return new HiTechnicNxtColorSensor(controller, channel);
    }

    public ColorSensor createModernRoboticsI2cColorSensor(DeviceInterfaceModule controller, int channel) {
        RobotLog.v("Creating Modern Robotics I2C Color Sensor - Port: " + channel);
        return new ModernRoboticsI2cColorSensor(controller, channel);
    }

    public LED createLED(DigitalChannelController controller, int channel) {
        RobotLog.v("Creating LED - Port: " + channel);
        return new LED(controller, channel);
    }

    public static void enableDeviceEmulation() {
        enumA = HardwareDeviceManager.enumA.b;
    }

    public static void disableDeviceEmulation() {
        enumA = HardwareDeviceManager.enumA.a;
    }

    private ModernRoboticsUsbLegacyModule a(LegacyModule var1) {
        if(!(var1 instanceof ModernRoboticsUsbLegacyModule)) {
            throw new IllegalArgumentException("Modern Robotics Device Manager needs a Modern Robotics LegacyModule");
        } else {
            return (ModernRoboticsUsbLegacyModule)var1;
        }
    }

    private ModernRoboticsUsbDeviceInterfaceModule a(DeviceInterfaceModule var1) {
        if(!(var1 instanceof ModernRoboticsUsbDeviceInterfaceModule)) {
            throw new IllegalArgumentException("Modern Robotics Device Manager needs a Modern Robotics Device Interface Module");
        } else {
            return (ModernRoboticsUsbDeviceInterfaceModule)var1;
        }
    }

    private void printErrAndThrow(String message) throws RobotCoreException {
        System.err.println(message);
        throw new RobotCoreException(message);
    }

    static {
        enumA = HardwareDeviceManager.enumA.a;
    }

    private static enum EnumA
        {
        a,
        b;

        private EnumA() {
        }
    }
}
