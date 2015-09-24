package com.qualcomm.hardware;

import com.qualcomm.robotcore.hardware.configuration.ReadXMLFileHandler;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
import com.qualcomm.robotcore.hardware.AnalogOutputController;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.hardware.configuration.DeviceInterfaceModuleConfiguration;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.List;
import com.qualcomm.robotcore.exception.RobotCoreException;
import java.util.Iterator;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.modernrobotics.ModernRoboticsUsbUtil;
import java.io.InputStream;
import android.content.Context;
import com.qualcomm.robotcore.hardware.HardwareFactory;

public class ModernRoboticsHardwareFactory implements HardwareFactory
{
    private Context a;
    private InputStream b;
    
    public ModernRoboticsHardwareFactory(final Context a) {
        this.b = null;
        ModernRoboticsUsbUtil.init(this.a = a);
    }
    
    private void a(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.irSeekerSensor.put(deviceConfiguration.getName(), deviceManager.createIrSeekerSensorV3(deviceInterfaceModule, deviceConfiguration.getPort()));
    }
    
    private void a(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DigitalChannelController digitalChannelController, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.led.put(deviceConfiguration.getName(), deviceManager.createLED(digitalChannelController, deviceConfiguration.getPort()));
    }
    
    private void a(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.touchSensor.put(deviceConfiguration.getName(), deviceManager.createNxtTouchSensor(legacyModule, deviceConfiguration.getPort()));
    }
    
    private void a(final HardwareMap hardwareMap, final DeviceManager deviceManager, final ControllerConfiguration controllerConfiguration) throws RobotCoreException, InterruptedException {
        final ModernRoboticsUsbDcMotorController modernRoboticsUsbDcMotorController = (ModernRoboticsUsbDcMotorController)deviceManager.createUsbDcMotorController(controllerConfiguration.getSerialNumber());
        hardwareMap.dcMotorController.put(controllerConfiguration.getName(), modernRoboticsUsbDcMotorController);
        for (final DeviceConfiguration deviceConfiguration : controllerConfiguration.getDevices()) {
            if (deviceConfiguration.isEnabled()) {
                hardwareMap.dcMotor.put(deviceConfiguration.getName(), deviceManager.createDcMotor(modernRoboticsUsbDcMotorController, deviceConfiguration.getPort()));
            }
        }
        hardwareMap.voltageSensor.put(controllerConfiguration.getName(), modernRoboticsUsbDcMotorController);
    }
    
    private void a(final List<DeviceConfiguration> list, final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule) {
        for (final DeviceConfiguration deviceConfiguration : list) {
            if (deviceConfiguration.isEnabled()) {
                final DeviceConfiguration.ConfigurationType type = deviceConfiguration.getType();
                switch (ModernRoboticsHardwareFactory$1.a[type.ordinal()]) {
                    case 15: {
                        this.j(hardwareMap, deviceManager, deviceInterfaceModule, deviceConfiguration);
                        continue;
                    }
                    case 14: {
                        this.a(hardwareMap, deviceManager, (DigitalChannelController)deviceInterfaceModule, deviceConfiguration);
                        continue;
                    }
                    case 13: {
                        this.i(hardwareMap, deviceManager, deviceInterfaceModule, deviceConfiguration);
                        continue;
                    }
                    case 12: {
                        this.g(hardwareMap, deviceManager, deviceInterfaceModule, deviceConfiguration);
                        continue;
                    }
                    case 11: {
                        this.f(hardwareMap, deviceManager, deviceInterfaceModule, deviceConfiguration);
                        continue;
                    }
                    case 10: {
                        this.a(hardwareMap, deviceManager, deviceInterfaceModule, deviceConfiguration);
                        continue;
                    }
                    case 9: {
                        this.e(hardwareMap, deviceManager, deviceInterfaceModule, deviceConfiguration);
                        continue;
                    }
                    case 8: {
                        this.b(hardwareMap, deviceManager, deviceInterfaceModule, deviceConfiguration);
                        continue;
                    }
                    case 7: {
                        this.c(hardwareMap, deviceManager, deviceInterfaceModule, deviceConfiguration);
                        continue;
                    }
                    case 6: {
                        this.d(hardwareMap, deviceManager, deviceInterfaceModule, deviceConfiguration);
                        continue;
                    }
                    case 5: {
                        this.h(hardwareMap, deviceManager, deviceInterfaceModule, deviceConfiguration);
                    }
                    case 16: {
                        continue;
                    }
                    default: {
                        RobotLog.w("Unexpected device type connected to Device Interface Module while parsing XML: " + type.toString());
                        continue;
                    }
                }
            }
        }
    }
    
    private void b(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.digitalChannel.put(deviceConfiguration.getName(), deviceManager.createDigitalChannelDevice(deviceInterfaceModule, deviceConfiguration.getPort()));
    }
    
    private void b(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.touchSensorMultiplexer.put(deviceConfiguration.getName(), deviceManager.createNxtTouchSensorMultiplexer(legacyModule, deviceConfiguration.getPort()));
    }
    
    private void b(final HardwareMap hardwareMap, final DeviceManager deviceManager, final ControllerConfiguration controllerConfiguration) throws RobotCoreException, InterruptedException {
        final ServoController usbServoController = deviceManager.createUsbServoController(controllerConfiguration.getSerialNumber());
        hardwareMap.servoController.put(controllerConfiguration.getName(), usbServoController);
        for (final DeviceConfiguration deviceConfiguration : controllerConfiguration.getDevices()) {
            if (deviceConfiguration.isEnabled()) {
                hardwareMap.servo.put(deviceConfiguration.getName(), deviceManager.createServo(usbServoController, deviceConfiguration.getPort()));
            }
        }
    }
    
    private void c(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.touchSensor.put(deviceConfiguration.getName(), deviceManager.createTouchSensor(deviceInterfaceModule, deviceConfiguration.getPort()));
    }
    
    private void c(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.ultrasonicSensor.put(deviceConfiguration.getName(), deviceManager.createNxtUltrasonicSensor(legacyModule, deviceConfiguration.getPort()));
    }
    
    private void c(final HardwareMap hardwareMap, final DeviceManager deviceManager, final ControllerConfiguration controllerConfiguration) throws RobotCoreException, InterruptedException {
        final DeviceInterfaceModule deviceInterfaceModule = deviceManager.createDeviceInterfaceModule(controllerConfiguration.getSerialNumber());
        hardwareMap.deviceInterfaceModule.put(controllerConfiguration.getName(), deviceInterfaceModule);
        this.a(((DeviceInterfaceModuleConfiguration)controllerConfiguration).getPwmDevices(), hardwareMap, deviceManager, deviceInterfaceModule);
        this.a(((DeviceInterfaceModuleConfiguration)controllerConfiguration).getI2cDevices(), hardwareMap, deviceManager, deviceInterfaceModule);
        this.a(((DeviceInterfaceModuleConfiguration)controllerConfiguration).getAnalogInputDevices(), hardwareMap, deviceManager, deviceInterfaceModule);
        this.a(((DeviceInterfaceModuleConfiguration)controllerConfiguration).getDigitalDevices(), hardwareMap, deviceManager, deviceInterfaceModule);
        this.a(((DeviceInterfaceModuleConfiguration)controllerConfiguration).getAnalogOutputDevices(), hardwareMap, deviceManager, deviceInterfaceModule);
    }
    
    private void d(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.analogInput.put(deviceConfiguration.getName(), deviceManager.createAnalogInputDevice(deviceInterfaceModule, deviceConfiguration.getPort()));
    }
    
    private void d(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.colorSensor.put(deviceConfiguration.getName(), deviceManager.createNxtColorSensor(legacyModule, deviceConfiguration.getPort()));
    }
    
    private void d(final HardwareMap hardwareMap, final DeviceManager deviceManager, final ControllerConfiguration controllerConfiguration) throws RobotCoreException, InterruptedException {
        final LegacyModule usbLegacyModule = deviceManager.createUsbLegacyModule(controllerConfiguration.getSerialNumber());
        hardwareMap.legacyModule.put(controllerConfiguration.getName(), usbLegacyModule);
        for (final DeviceConfiguration deviceConfiguration : controllerConfiguration.getDevices()) {
            if (deviceConfiguration.isEnabled()) {
                final DeviceConfiguration.ConfigurationType type = deviceConfiguration.getType();
                switch (ModernRoboticsHardwareFactory$1.a[type.ordinal()]) {
                    case 15: {
                        this.d(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                        continue;
                    }
                    case 23: {
                        this.c(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                        continue;
                    }
                    case 22: {
                        this.b(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                        continue;
                    }
                    case 7: {
                        this.a(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                        continue;
                    }
                    case 2: {
                        this.k(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                        continue;
                    }
                    case 1: {
                        this.j(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                        continue;
                    }
                    case 21: {
                        this.i(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                        continue;
                    }
                    case 20: {
                        this.h(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                        continue;
                    }
                    case 19: {
                        this.g(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                        continue;
                    }
                    case 18: {
                        this.f(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                        continue;
                    }
                    case 17: {
                        this.e(hardwareMap, deviceManager, usbLegacyModule, deviceConfiguration);
                    }
                    case 16: {
                        continue;
                    }
                    default: {
                        RobotLog.w("Unexpected device type connected to Legacy Module while parsing XML: " + type.toString());
                        continue;
                    }
                }
            }
        }
    }
    
    public static void disableDeviceEmulation() {
        ModernRoboticsDeviceManager.disableDeviceEmulation();
    }
    
    private void e(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.pwmOutput.put(deviceConfiguration.getName(), deviceManager.createPwmOutputDevice(deviceInterfaceModule, deviceConfiguration.getPort()));
    }
    
    private void e(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.gyroSensor.put(deviceConfiguration.getName(), deviceManager.createNxtGyroSensor(legacyModule, deviceConfiguration.getPort()));
    }
    
    public static void enableDeviceEmulation() {
        ModernRoboticsDeviceManager.enableDeviceEmulation();
    }
    
    private void f(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.i2cDevice.put(deviceConfiguration.getName(), deviceManager.createI2cDevice(deviceInterfaceModule, deviceConfiguration.getPort()));
    }
    
    private void f(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.compassSensor.put(deviceConfiguration.getName(), deviceManager.createNxtCompassSensor(legacyModule, deviceConfiguration.getPort()));
    }
    
    private void g(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.analogOutput.put(deviceConfiguration.getName(), deviceManager.createAnalogOutputDevice(deviceInterfaceModule, deviceConfiguration.getPort()));
    }
    
    private void g(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.irSeekerSensor.put(deviceConfiguration.getName(), deviceManager.createNxtIrSeekerSensor(legacyModule, deviceConfiguration.getPort()));
    }
    
    private void h(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.opticalDistanceSensor.put(deviceConfiguration.getName(), deviceManager.createOpticalDistanceSensor(deviceInterfaceModule, deviceConfiguration.getPort()));
    }
    
    private void h(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.lightSensor.put(deviceConfiguration.getName(), deviceManager.createNxtLightSensor(legacyModule, deviceConfiguration.getPort()));
    }
    
    private void i(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.colorSensor.put(deviceConfiguration.getName(), deviceManager.createAdafruitColorSensor(deviceInterfaceModule, deviceConfiguration.getPort()));
    }
    
    private void i(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.accelerationSensor.put(deviceConfiguration.getName(), deviceManager.createNxtAccelerationSensor(legacyModule, deviceConfiguration.getPort()));
    }
    
    private void j(final HardwareMap hardwareMap, final DeviceManager deviceManager, final DeviceInterfaceModule deviceInterfaceModule, final DeviceConfiguration deviceConfiguration) {
        hardwareMap.colorSensor.put(deviceConfiguration.getName(), deviceManager.createModernRoboticsColorSensor(deviceInterfaceModule, deviceConfiguration.getPort()));
    }
    
    private void j(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        final DcMotorController nxtDcMotorController = deviceManager.createNxtDcMotorController(legacyModule, deviceConfiguration.getPort());
        hardwareMap.dcMotorController.put(deviceConfiguration.getName(), nxtDcMotorController);
        for (final DeviceConfiguration deviceConfiguration2 : ((MotorControllerConfiguration)deviceConfiguration).getMotors()) {
            hardwareMap.dcMotor.put(deviceConfiguration2.getName(), deviceManager.createDcMotor(nxtDcMotorController, deviceConfiguration2.getPort()));
        }
    }
    
    private void k(final HardwareMap hardwareMap, final DeviceManager deviceManager, final LegacyModule legacyModule, final DeviceConfiguration deviceConfiguration) {
        final ServoController nxtServoController = deviceManager.createNxtServoController(legacyModule, deviceConfiguration.getPort());
        hardwareMap.servoController.put(deviceConfiguration.getName(), nxtServoController);
        for (final DeviceConfiguration deviceConfiguration2 : ((ServoControllerConfiguration)deviceConfiguration).getServos()) {
            hardwareMap.servo.put(deviceConfiguration2.getName(), deviceManager.createServo(nxtServoController, deviceConfiguration2.getPort()));
        }
    }
    
    @Override
    public HardwareMap createHardwareMap(final EventLoopManager eventLoopManager) throws RobotCoreException, InterruptedException {
        if (this.b == null) {
            throw new RobotCoreException("XML input stream is null, ModernRoboticsHardwareFactory cannot create a hardware map");
        }
        final HardwareMap hardwareMap = new HardwareMap();
        RobotLog.v("Starting Modern Robotics device manager");
        final ModernRoboticsDeviceManager modernRoboticsDeviceManager = new ModernRoboticsDeviceManager(this.a, eventLoopManager);
        for (final ControllerConfiguration controllerConfiguration : new ReadXMLFileHandler(this.a).parse(this.b)) {
            final DeviceConfiguration.ConfigurationType type = controllerConfiguration.getType();
            switch (ModernRoboticsHardwareFactory$1.a[type.ordinal()]) {
                default: {
                    RobotLog.w("Unexpected controller type while parsing XML: " + type.toString());
                    continue;
                }
                case 1: {
                    this.a(hardwareMap, modernRoboticsDeviceManager, controllerConfiguration);
                    continue;
                }
                case 2: {
                    this.b(hardwareMap, modernRoboticsDeviceManager, controllerConfiguration);
                    continue;
                }
                case 3: {
                    this.d(hardwareMap, modernRoboticsDeviceManager, controllerConfiguration);
                    continue;
                }
                case 4: {
                    this.c(hardwareMap, modernRoboticsDeviceManager, controllerConfiguration);
                    continue;
                }
            }
        }
        hardwareMap.appContext = this.a;
        return hardwareMap;
    }
    
    public InputStream getXmlInputStream() {
        return this.b;
    }
    
    public void setXmlInputStream(final InputStream b) {
        this.b = b;
    }
}
