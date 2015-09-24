package com.qualcomm.robotcore.hardware.configuration;

import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStream;
import com.qualcomm.robotcore.exception.RobotCoreException;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.ArrayList;
import android.content.Context;
import org.xmlpull.v1.XmlPullParser;
import java.util.List;

public class ReadXMLFileHandler
{
    private static boolean b;
    private static int c;
    private static int d;
    private static int e;
    private static int f;
    private static int g;
    private static int h;
    private static int i;
    private static int j;
    private static int k;
    private static int l;
    List<ControllerConfiguration> a;
    private XmlPullParser m;
    
    static {
        ReadXMLFileHandler.b = false;
        ReadXMLFileHandler.c = 2;
        ReadXMLFileHandler.d = 8;
        ReadXMLFileHandler.e = 8;
        ReadXMLFileHandler.f = 2;
        ReadXMLFileHandler.g = 6;
        ReadXMLFileHandler.h = 6;
        ReadXMLFileHandler.i = 6;
        ReadXMLFileHandler.j = 2;
        ReadXMLFileHandler.k = 4;
        ReadXMLFileHandler.l = 4;
    }
    
    public ReadXMLFileHandler(final Context context) {
        this.a = new ArrayList<ControllerConfiguration>();
    }
    
    private ControllerConfiguration a() throws IOException, XmlPullParserException, RobotCoreException {
        final String attributeValue = this.m.getAttributeValue((String)null, "name");
        final String attributeValue2 = this.m.getAttributeValue((String)null, "serialNumber");
        final ArrayList<DeviceConfiguration> a = this.a(ReadXMLFileHandler.c, DeviceConfiguration.ConfigurationType.PULSE_WIDTH_DEVICE);
        final ArrayList<DeviceConfiguration> a2 = this.a(ReadXMLFileHandler.g, DeviceConfiguration.ConfigurationType.I2C_DEVICE);
        final ArrayList<DeviceConfiguration> a3 = this.a(ReadXMLFileHandler.e, DeviceConfiguration.ConfigurationType.ANALOG_INPUT);
        final ArrayList<DeviceConfiguration> a4 = this.a(ReadXMLFileHandler.d, DeviceConfiguration.ConfigurationType.DIGITAL_DEVICE);
        final ArrayList<DeviceConfiguration> a5 = this.a(ReadXMLFileHandler.f, DeviceConfiguration.ConfigurationType.ANALOG_OUTPUT);
        int i = this.m.next();
        String s = this.a(this.m.getName());
        while (i != 1) {
            if (i == 3) {
                if (s == null) {
                    continue;
                }
                if (ReadXMLFileHandler.b) {
                    RobotLog.e("[handleDeviceInterfaceModule] tagname: " + s);
                }
                if (s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.DEVICE_INTERFACE_MODULE.toString())) {
                    final DeviceInterfaceModuleConfiguration deviceInterfaceModuleConfiguration = new DeviceInterfaceModuleConfiguration(attributeValue, new SerialNumber(attributeValue2));
                    deviceInterfaceModuleConfiguration.setPwmDevices(a);
                    deviceInterfaceModuleConfiguration.setI2cDevices(a2);
                    deviceInterfaceModuleConfiguration.setAnalogInputDevices(a3);
                    deviceInterfaceModuleConfiguration.setDigitalDevices(a4);
                    deviceInterfaceModuleConfiguration.setAnalogOutputDevices(a5);
                    deviceInterfaceModuleConfiguration.setEnabled(true);
                    return deviceInterfaceModuleConfiguration;
                }
            }
            if (i == 2) {
                if (s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.ANALOG_INPUT.toString()) || s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.OPTICAL_DISTANCE_SENSOR.toString())) {
                    final DeviceConfiguration c = this.c();
                    a3.set(c.getPort(), c);
                }
                if (s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.PULSE_WIDTH_DEVICE.toString())) {
                    final DeviceConfiguration c2 = this.c();
                    a.set(c2.getPort(), c2);
                }
                if (s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.I2C_DEVICE.toString()) || s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.IR_SEEKER_V3.toString()) || s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.ADAFRUIT_COLOR_SENSOR.toString()) || s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.COLOR_SENSOR.toString())) {
                    final DeviceConfiguration c3 = this.c();
                    a2.set(c3.getPort(), c3);
                }
                if (s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.ANALOG_OUTPUT.toString())) {
                    final DeviceConfiguration c4 = this.c();
                    a5.set(c4.getPort(), c4);
                }
                if (s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.DIGITAL_DEVICE.toString()) || s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.TOUCH_SENSOR.toString()) || s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.LED.toString())) {
                    final DeviceConfiguration c5 = this.c();
                    a4.set(c5.getPort(), c5);
                }
            }
            i = this.m.next();
            s = this.a(this.m.getName());
        }
        RobotLog.logAndThrow("Reached the end of the XML file while parsing.");
        return null;
    }
    
    private ControllerConfiguration a(final boolean b) throws IOException, XmlPullParserException {
        final String attributeValue = this.m.getAttributeValue((String)null, "name");
        int int1 = -1;
        String s = ControllerConfiguration.NO_SERIAL_NUMBER.toString();
        if (b) {
            s = this.m.getAttributeValue((String)null, "serialNumber");
        }
        else {
            int1 = Integer.parseInt(this.m.getAttributeValue((String)null, "port"));
        }
        final ArrayList<DeviceConfiguration> a = this.a(ReadXMLFileHandler.i, DeviceConfiguration.ConfigurationType.SERVO);
        int i = this.m.next();
        String s2 = this.a(this.m.getName());
        while (i != 1) {
            if (i == 3) {
                if (s2 == null) {
                    continue;
                }
                if (s2.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER.toString())) {
                    final ServoControllerConfiguration servoControllerConfiguration = new ServoControllerConfiguration(attributeValue, a, new SerialNumber(s));
                    servoControllerConfiguration.setPort(int1);
                    servoControllerConfiguration.setEnabled(true);
                    return servoControllerConfiguration;
                }
            }
            if (i == 2 && s2.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.SERVO.toString())) {
                final int int2 = Integer.parseInt(this.m.getAttributeValue((String)null, "port"));
                a.set(int2 - 1, new ServoConfiguration(int2, this.m.getAttributeValue((String)null, "name"), true));
            }
            i = this.m.next();
            s2 = this.a(this.m.getName());
        }
        final ServoControllerConfiguration servoControllerConfiguration2 = new ServoControllerConfiguration(attributeValue, a, new SerialNumber(s));
        servoControllerConfiguration2.setPort(int1);
        return servoControllerConfiguration2;
    }
    
    private String a(String s) {
        if (s == null) {
            s = null;
        }
        else {
            if (s.equalsIgnoreCase("MotorController")) {
                return DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER.toString();
            }
            if (s.equalsIgnoreCase("ServoController")) {
                return DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER.toString();
            }
            if (s.equalsIgnoreCase("LegacyModuleController")) {
                return DeviceConfiguration.ConfigurationType.LEGACY_MODULE_CONTROLLER.toString();
            }
            if (s.equalsIgnoreCase("DeviceInterfaceModule")) {
                return DeviceConfiguration.ConfigurationType.DEVICE_INTERFACE_MODULE.toString();
            }
            if (s.equalsIgnoreCase("AnalogInput")) {
                return DeviceConfiguration.ConfigurationType.ANALOG_INPUT.toString();
            }
            if (s.equalsIgnoreCase("OpticalDistanceSensor")) {
                return DeviceConfiguration.ConfigurationType.OPTICAL_DISTANCE_SENSOR.toString();
            }
            if (s.equalsIgnoreCase("IrSeeker")) {
                return DeviceConfiguration.ConfigurationType.IR_SEEKER.toString();
            }
            if (s.equalsIgnoreCase("LightSensor")) {
                return DeviceConfiguration.ConfigurationType.LIGHT_SENSOR.toString();
            }
            if (s.equalsIgnoreCase("DigitalDevice")) {
                return DeviceConfiguration.ConfigurationType.DIGITAL_DEVICE.toString();
            }
            if (s.equalsIgnoreCase("TouchSensor")) {
                return DeviceConfiguration.ConfigurationType.TOUCH_SENSOR.toString();
            }
            if (s.equalsIgnoreCase("IrSeekerV3")) {
                return DeviceConfiguration.ConfigurationType.IR_SEEKER_V3.toString();
            }
            if (s.equalsIgnoreCase("PulseWidthDevice")) {
                return DeviceConfiguration.ConfigurationType.PULSE_WIDTH_DEVICE.toString();
            }
            if (s.equalsIgnoreCase("I2cDevice")) {
                return DeviceConfiguration.ConfigurationType.I2C_DEVICE.toString();
            }
            if (s.equalsIgnoreCase("AnalogOutput")) {
                return DeviceConfiguration.ConfigurationType.ANALOG_OUTPUT.toString();
            }
            if (s.equalsIgnoreCase("TouchSensorMultiplexer")) {
                return DeviceConfiguration.ConfigurationType.TOUCH_SENSOR_MULTIPLEXER.toString();
            }
            if (s.equalsIgnoreCase("MatrixController")) {
                return DeviceConfiguration.ConfigurationType.MATRIX_CONTROLLER.toString();
            }
            if (s.equalsIgnoreCase("UltrasonicSensor")) {
                return DeviceConfiguration.ConfigurationType.ULTRASONIC_SENSOR.toString();
            }
            if (s.equalsIgnoreCase("AdafruitColorSensor")) {
                return DeviceConfiguration.ConfigurationType.ADAFRUIT_COLOR_SENSOR.toString();
            }
            if (s.equalsIgnoreCase("ColorSensor")) {
                return DeviceConfiguration.ConfigurationType.COLOR_SENSOR.toString();
            }
            if (s.equalsIgnoreCase("Led")) {
                return DeviceConfiguration.ConfigurationType.LED.toString();
            }
        }
        return s;
    }
    
    private ArrayList<DeviceConfiguration> a(final int n, final DeviceConfiguration.ConfigurationType configurationType) {
        final ArrayList<ServoConfiguration> list = (ArrayList<ServoConfiguration>)new ArrayList<MotorConfiguration>();
        for (int i = 0; i < n; ++i) {
            if (configurationType == DeviceConfiguration.ConfigurationType.SERVO) {
                list.add((MotorConfiguration)new ServoConfiguration(i + 1, "NO DEVICE ATTACHED", false));
            }
            else if (configurationType == DeviceConfiguration.ConfigurationType.MOTOR) {
                list.add(new MotorConfiguration(i + 1, "NO DEVICE ATTACHED", false));
            }
            else {
                list.add(new DeviceConfiguration(i, configurationType, "NO DEVICE ATTACHED", false));
            }
        }
        return (ArrayList<DeviceConfiguration>)list;
    }
    
    private ControllerConfiguration b() throws IOException, XmlPullParserException, RobotCoreException {
        final String attributeValue = this.m.getAttributeValue((String)null, "name");
        final String attributeValue2 = this.m.getAttributeValue((String)null, "serialNumber");
        final ArrayList<DeviceConfiguration> a = this.a(ReadXMLFileHandler.h, DeviceConfiguration.ConfigurationType.NOTHING);
        int i = this.m.next();
        String s = this.a(this.m.getName());
        while (i != 1) {
            if (i == 3) {
                if (s == null) {
                    continue;
                }
                if (s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.LEGACY_MODULE_CONTROLLER.toString())) {
                    final LegacyModuleControllerConfiguration legacyModuleControllerConfiguration = new LegacyModuleControllerConfiguration(attributeValue, a, new SerialNumber(attributeValue2));
                    legacyModuleControllerConfiguration.setEnabled(true);
                    return legacyModuleControllerConfiguration;
                }
            }
            if (i == 2) {
                if (ReadXMLFileHandler.b) {
                    RobotLog.e("[handleLegacyModule] tagname: " + s);
                }
                if (s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.COMPASS.toString()) || s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.LIGHT_SENSOR.toString()) || s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.IR_SEEKER.toString()) || s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.ACCELEROMETER.toString()) || s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.GYRO.toString()) || s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.TOUCH_SENSOR.toString()) || s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.TOUCH_SENSOR_MULTIPLEXER.toString()) || s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.ULTRASONIC_SENSOR.toString()) || s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.COLOR_SENSOR.toString()) || s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.NOTHING.toString())) {
                    final DeviceConfiguration c = this.c();
                    a.set(c.getPort(), c);
                }
                else if (s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER.toString())) {
                    final ControllerConfiguration b = this.b(false);
                    a.set(b.getPort(), b);
                }
                else if (s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER.toString())) {
                    final ControllerConfiguration a2 = this.a(false);
                    a.set(a2.getPort(), a2);
                }
                else if (s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MATRIX_CONTROLLER.toString())) {
                    final ControllerConfiguration d = this.d();
                    a.set(d.getPort(), d);
                }
            }
            i = this.m.next();
            s = this.a(this.m.getName());
        }
        return new LegacyModuleControllerConfiguration(attributeValue, a, new SerialNumber(attributeValue2));
    }
    
    private ControllerConfiguration b(final boolean b) throws IOException, XmlPullParserException {
        final String attributeValue = this.m.getAttributeValue((String)null, "name");
        int int1 = -1;
        String s = ControllerConfiguration.NO_SERIAL_NUMBER.toString();
        if (b) {
            s = this.m.getAttributeValue((String)null, "serialNumber");
        }
        else {
            int1 = Integer.parseInt(this.m.getAttributeValue((String)null, "port"));
        }
        final ArrayList<DeviceConfiguration> a = this.a(ReadXMLFileHandler.j, DeviceConfiguration.ConfigurationType.MOTOR);
        int i = this.m.next();
        String s2 = this.a(this.m.getName());
        while (i != 1) {
            if (i == 3) {
                if (s2 == null) {
                    continue;
                }
                if (s2.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER.toString())) {
                    final MotorControllerConfiguration motorControllerConfiguration = new MotorControllerConfiguration(attributeValue, a, new SerialNumber(s));
                    motorControllerConfiguration.setPort(int1);
                    motorControllerConfiguration.setEnabled(true);
                    return motorControllerConfiguration;
                }
            }
            if (i == 2 && s2.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MOTOR.toString())) {
                final int int2 = Integer.parseInt(this.m.getAttributeValue((String)null, "port"));
                a.set(int2 - 1, new MotorConfiguration(int2, this.m.getAttributeValue((String)null, "name"), true));
            }
            i = this.m.next();
            s2 = this.a(this.m.getName());
        }
        final MotorControllerConfiguration motorControllerConfiguration2 = new MotorControllerConfiguration(attributeValue, a, new SerialNumber(s));
        motorControllerConfiguration2.setPort(int1);
        return motorControllerConfiguration2;
    }
    
    private DeviceConfiguration c() {
        final String a = this.a(this.m.getName());
        final DeviceConfiguration deviceConfiguration = new DeviceConfiguration(Integer.parseInt(this.m.getAttributeValue((String)null, "port")));
        deviceConfiguration.setType(deviceConfiguration.typeFromString(a));
        deviceConfiguration.setName(this.m.getAttributeValue((String)null, "name"));
        if (!deviceConfiguration.getName().equalsIgnoreCase("NO DEVICE ATTACHED")) {
            deviceConfiguration.setEnabled(true);
        }
        if (ReadXMLFileHandler.b) {
            RobotLog.e("[handleDevice] name: " + deviceConfiguration.getName() + ", port: " + deviceConfiguration.getPort() + ", type: " + deviceConfiguration.getType());
        }
        return deviceConfiguration;
    }
    
    private ControllerConfiguration d() throws IOException, XmlPullParserException, RobotCoreException {
        final String attributeValue = this.m.getAttributeValue((String)null, "name");
        final String string = ControllerConfiguration.NO_SERIAL_NUMBER.toString();
        final int int1 = Integer.parseInt(this.m.getAttributeValue((String)null, "port"));
        final ArrayList<DeviceConfiguration> a = this.a(ReadXMLFileHandler.l, DeviceConfiguration.ConfigurationType.SERVO);
        final ArrayList<DeviceConfiguration> a2 = this.a(ReadXMLFileHandler.k, DeviceConfiguration.ConfigurationType.MOTOR);
        int i = this.m.next();
        String s = this.a(this.m.getName());
        while (i != 1) {
            if (i == 3) {
                if (s == null) {
                    continue;
                }
                if (s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MATRIX_CONTROLLER.toString())) {
                    final MatrixControllerConfiguration matrixControllerConfiguration = new MatrixControllerConfiguration(attributeValue, a2, a, new SerialNumber(string));
                    matrixControllerConfiguration.setPort(int1);
                    matrixControllerConfiguration.setEnabled(true);
                    return matrixControllerConfiguration;
                }
            }
            if (i == 2) {
                if (s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.SERVO.toString())) {
                    final int int2 = Integer.parseInt(this.m.getAttributeValue((String)null, "port"));
                    a.set(int2 - 1, new ServoConfiguration(int2, this.m.getAttributeValue((String)null, "name"), true));
                }
                else if (s.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MOTOR.toString())) {
                    final int int3 = Integer.parseInt(this.m.getAttributeValue((String)null, "port"));
                    a2.set(int3 - 1, new MotorConfiguration(int3, this.m.getAttributeValue((String)null, "name"), true));
                }
            }
            i = this.m.next();
            s = this.a(this.m.getName());
        }
        RobotLog.logAndThrow("Reached the end of the XML file while parsing.");
        return null;
    }
    
    public List<ControllerConfiguration> getDeviceControllers() {
        return this.a;
    }
    
    public List<ControllerConfiguration> parse(final InputStream inputStream) throws RobotCoreException {
        this.m = null;
        try {
            final XmlPullParserFactory instance = XmlPullParserFactory.newInstance();
            instance.setNamespaceAware(true);
            (this.m = instance.newPullParser()).setInput(inputStream, (String)null);
            for (int i = this.m.getEventType(); i != 1; i = this.m.next()) {
                final String a = this.a(this.m.getName());
                if (i == 2) {
                    if (a.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER.toString())) {
                        this.a.add(this.b(true));
                    }
                    if (a.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER.toString())) {
                        this.a.add(this.a(true));
                    }
                    if (a.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.LEGACY_MODULE_CONTROLLER.toString())) {
                        this.a.add(this.b());
                    }
                    if (a.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.DEVICE_INTERFACE_MODULE.toString())) {
                        this.a.add(this.a());
                    }
                }
            }
            goto Label_0217;
        }
        catch (XmlPullParserException ex) {
            RobotLog.w("XmlPullParserException");
            ex.printStackTrace();
        }
        catch (IOException ex2) {
            RobotLog.w("IOException");
            ex2.printStackTrace();
            goto Label_0217;
        }
    }
}
