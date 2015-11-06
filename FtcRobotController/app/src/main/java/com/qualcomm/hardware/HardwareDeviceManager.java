//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.hardware;

import android.content.Context;
import com.qualcomm.hardware.AdafruitI2cColorSensor;
import com.qualcomm.hardware.HiTechnicNxtAccelerationSensor;
import com.qualcomm.hardware.HiTechnicNxtColorSensor;
import com.qualcomm.hardware.HiTechnicNxtCompassSensor;
import com.qualcomm.hardware.HiTechnicNxtDcMotorController;
import com.qualcomm.hardware.HiTechnicNxtGyroSensor;
import com.qualcomm.hardware.HiTechnicNxtIrSeekerSensor;
import com.qualcomm.hardware.HiTechnicNxtLightSensor;
import com.qualcomm.hardware.HiTechnicNxtServoController;
import com.qualcomm.hardware.HiTechnicNxtTouchSensor;
import com.qualcomm.hardware.HiTechnicNxtTouchSensorMultiplexer;
import com.qualcomm.hardware.HiTechnicNxtUltrasonicSensor;
import com.qualcomm.hardware.ModernRoboticsAnalogOpticalDistanceSensor;
import com.qualcomm.hardware.ModernRoboticsDigitalTouchSensor;
import com.qualcomm.hardware.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.ModernRoboticsI2cIrSeekerSensorV3;
import com.qualcomm.hardware.ModernRoboticsUsbDcMotorController;
import com.qualcomm.hardware.ModernRoboticsUsbDeviceInterfaceModule;
import com.qualcomm.hardware.ModernRoboticsUsbLegacyModule;
import com.qualcomm.hardware.ModernRoboticsUsbServoController;
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
import com.qualcomm.robotcore.hardware.DeviceManager.DeviceType;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
import com.qualcomm.robotcore.hardware.usb.ftdi.RobotUsbManagerFtdi;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.HashMap;
import java.util.Map;

public class HardwareDeviceManager extends DeviceManager {
    private static HardwareDeviceManager.a a;
    private RobotUsbManager b;
    private final EventLoopManager c;

    public HardwareDeviceManager(Context context, EventLoopManager manager) throws RobotCoreException {
        this.c = manager;
        switch(HardwareDeviceManager.SyntheticClass_1.a[a.ordinal()]) {
        case 1:
            this.b = new RobotUsbManagerEmulator();
            break;
        default:
            this.b = new RobotUsbManagerFtdi(context);
        }

    }

    public Map<SerialNumber, DeviceType> scanForUsbDevices() throws RobotCoreException {
        HashMap var1 = new HashMap();

        try {
            int var2 = this.b.scanForDevices();

            for(int var3 = 0; var3 < var2; ++var3) {
                SerialNumber var4 = this.b.getDeviceSerialNumberByIndex(var3);
                RobotUsbDevice var5 = ModernRoboticsUsbUtil.openUsbDevice(this.b, var4);
                var1.put(var4, ModernRoboticsUsbUtil.getDeviceType(ModernRoboticsUsbUtil.getUsbDeviceHeader(var5)));
                var5.close();
            }
        } catch (RobotCoreException var6) {
            RobotLog.setGlobalErrorMsgAndThrow("Error while scanning for USB devices", var6);
        }

        return var1;
    }

    public DcMotorController createUsbDcMotorController(SerialNumber serialNumber) throws RobotCoreException, InterruptedException {
        RobotLog.v("Creating Modern Robotics USB DC Motor Controller - " + serialNumber.toString());
        ModernRoboticsUsbDcMotorController var2 = null;

        try {
            RobotUsbDevice var3 = ModernRoboticsUsbUtil.openUsbDevice(this.b, serialNumber);
            byte[] var4 = ModernRoboticsUsbUtil.getUsbDeviceHeader(var3);
            DeviceType var5 = ModernRoboticsUsbUtil.getDeviceType(var4);
            if(var5 != DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER) {
                this.a(var3, "Modern Robotics USB DC Motor Controller", serialNumber);
            }

            var2 = new ModernRoboticsUsbDcMotorController(serialNumber, var3, this.c);
        } catch (RobotCoreException var6) {
            RobotLog.setGlobalErrorMsgAndThrow("Unable to open Modern Robotics USB DC Motor Controller", var6);
        }

        return var2;
    }

    public ServoController createUsbServoController(SerialNumber serialNumber) throws RobotCoreException, InterruptedException {
        RobotLog.v("Creating Modern Robotics USB Servo Controller - " + serialNumber.toString());
        ModernRoboticsUsbServoController var2 = null;

        try {
            RobotUsbDevice var3 = ModernRoboticsUsbUtil.openUsbDevice(this.b, serialNumber);
            byte[] var4 = ModernRoboticsUsbUtil.getUsbDeviceHeader(var3);
            DeviceType var5 = ModernRoboticsUsbUtil.getDeviceType(var4);
            if(var5 != DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER) {
                this.a(var3, "Modern Robotics USB Servo Controller", serialNumber);
            }

            var2 = new ModernRoboticsUsbServoController(serialNumber, var3, this.c);
        } catch (RobotCoreException var6) {
            RobotLog.setGlobalErrorMsgAndThrow("Unable to open Modern Robotics USB Servo Controller", var6);
        }

        return var2;
    }

    public DeviceInterfaceModule createDeviceInterfaceModule(SerialNumber serialNumber) throws RobotCoreException, InterruptedException {
        RobotLog.v("Creating Modern Robotics USB Core Device Interface Module - " + serialNumber.toString());
        ModernRoboticsUsbDeviceInterfaceModule var2 = null;

        try {
            RobotUsbDevice var3 = ModernRoboticsUsbUtil.openUsbDevice(this.b, serialNumber);
            byte[] var4 = ModernRoboticsUsbUtil.getUsbDeviceHeader(var3);
            DeviceType var5 = ModernRoboticsUsbUtil.getDeviceType(var4);
            if(var5 != DeviceType.MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE) {
                this.a(var3, "Modern Robotics USB Core Device Interface Module", serialNumber);
            }

            var2 = new ModernRoboticsUsbDeviceInterfaceModule(serialNumber, var3, this.c);
        } catch (RobotCoreException var6) {
            RobotLog.setGlobalErrorMsgAndThrow("Unable to open Modern Robotics USB Core Device Interface Module", var6);
        }

        return var2;
    }

    public LegacyModule createUsbLegacyModule(SerialNumber serialNumber) throws RobotCoreException, InterruptedException {
        RobotLog.v("Creating Modern Robotics USB Legacy Module - " + serialNumber.toString());
        ModernRoboticsUsbLegacyModule var2 = null;

        try {
            RobotUsbDevice var3 = ModernRoboticsUsbUtil.openUsbDevice(this.b, serialNumber);
            byte[] var4 = ModernRoboticsUsbUtil.getUsbDeviceHeader(var3);
            DeviceType var5 = ModernRoboticsUsbUtil.getDeviceType(var4);
            if(var5 != DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE) {
                this.a(var3, "Modern Robotics USB Legacy Module", serialNumber);
            }

            var2 = new ModernRoboticsUsbLegacyModule(serialNumber, var3, this.c);
        } catch (RobotCoreException var6) {
            RobotLog.setGlobalErrorMsgAndThrow("Unable to open Modern Robotics USB Legacy Module", var6);
        }

        return var2;
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

    public GyroSensor createModernRoboticsI2cGyroSensor(DeviceInterfaceModule controller, int channel) {
        RobotLog.v("Creating Modern Robotics I2C Gyro Sensor - Port: " + channel);
        return new ModernRoboticsI2cGyro(controller, channel);
    }

    public LED createLED(DigitalChannelController controller, int channel) {
        RobotLog.v("Creating LED - Port: " + channel);
        return new LED(controller, channel);
    }

    public static void enableDeviceEmulation() {
        a = HardwareDeviceManager.a.b;
    }

    public static void disableDeviceEmulation() {
        a = HardwareDeviceManager.a.a;
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

    private void a(RobotUsbDevice var1, String var2, SerialNumber var3) throws RobotCoreException {
        String var4 = var2 + " [" + var3 + "] is returning garbage data via the USB bus";
        var1.close();
        this.a(var4);
    }

    private void a(String var1) throws RobotCoreException {
        System.err.println(var1);
        throw new RobotCoreException(var1);
    }

    static {
        a = HardwareDeviceManager.a.a;
    }

    private static enum a {
        a,
        b;

        private a() {
        }
    }
}
