//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.robotcore.hardware.configuration;

import android.content.Context;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceInterfaceModuleConfiguration;
import com.qualcomm.robotcore.hardware.configuration.LegacyModuleControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MatrixControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ServoConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration.ConfigurationType;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class ReadXMLFileHandler {
    private static boolean b = false;
    List<ControllerConfiguration> a = new ArrayList();
    private static int c = 2;
    private static int d = 8;
    private static int e = 8;
    private static int f = 2;
    private static int g = 6;
    private static int h = 6;
    private static int i = 6;
    private static int j = 2;
    private static int k = 4;
    private static int l = 4;
    private XmlPullParser m;

    public ReadXMLFileHandler(Context context) {
    }

    public List<ControllerConfiguration> getDeviceControllers() {
        return this.a;
    }

    public List<ControllerConfiguration> parse(InputStream is) throws RobotCoreException {
        this.m = null;

        try {
            XmlPullParserFactory var2 = XmlPullParserFactory.newInstance();
            var2.setNamespaceAware(true);
            this.m = var2.newPullParser();
            this.m.setInput(is, (String)null);

            for(int var3 = this.m.getEventType(); var3 != 1; var3 = this.m.next()) {
                String var4 = this.a(this.m.getName());
                if(var3 == 2) {
                    if(var4.equalsIgnoreCase(ConfigurationType.MOTOR_CONTROLLER.toString())) {
                        this.a.add(this.b(true));
                    }

                    if(var4.equalsIgnoreCase(ConfigurationType.SERVO_CONTROLLER.toString())) {
                        this.a.add(this.a(true));
                    }

                    if(var4.equalsIgnoreCase(ConfigurationType.LEGACY_MODULE_CONTROLLER.toString())) {
                        this.a.add(this.b());
                    }

                    if(var4.equalsIgnoreCase(ConfigurationType.DEVICE_INTERFACE_MODULE.toString())) {
                        this.a.add(this.a());
                    }
                }
            }
        } catch (XmlPullParserException var5) {
            RobotLog.w("XmlPullParserException");
            var5.printStackTrace();
        } catch (IOException var6) {
            RobotLog.w("IOException");
            var6.printStackTrace();
        }

        return this.a;
    }

    private ControllerConfiguration a() throws IOException, XmlPullParserException, RobotCoreException {
        String var1 = this.m.getAttributeValue((String)null, "name");
        String var2 = this.m.getAttributeValue((String)null, "serialNumber");
        ArrayList var3 = this.a(c, ConfigurationType.PULSE_WIDTH_DEVICE);
        ArrayList var4 = this.a(g, ConfigurationType.I2C_DEVICE);
        ArrayList var5 = this.a(e, ConfigurationType.ANALOG_INPUT);
        ArrayList var6 = this.a(d, ConfigurationType.DIGITAL_DEVICE);
        ArrayList var7 = this.a(f, ConfigurationType.ANALOG_OUTPUT);
        int var8 = this.m.next();
        String var9 = this.a(this.m.getName());

        while(true) {
            while(true) {
                if(var8 == 1) {
                    RobotLog.logAndThrow("Reached the end of the XML file while parsing.");
                    return null;
                }

                if(var8 != 3) {
                    break;
                }

                if(var9 != null) {
                    if(b) {
                        RobotLog.e("[handleDeviceInterfaceModule] tagname: " + var9);
                    }

                    if(var9.equalsIgnoreCase(ConfigurationType.DEVICE_INTERFACE_MODULE.toString())) {
                        DeviceInterfaceModuleConfiguration var10 = new DeviceInterfaceModuleConfiguration(var1, new SerialNumber(var2));
                        var10.setPwmDevices(var3);
                        var10.setI2cDevices(var4);
                        var10.setAnalogInputDevices(var5);
                        var10.setDigitalDevices(var6);
                        var10.setAnalogOutputDevices(var7);
                        var10.setEnabled(true);
                        return var10;
                    }
                    break;
                }
            }

            if(var8 == 2) {
                DeviceConfiguration var11;
                if(var9.equalsIgnoreCase(ConfigurationType.ANALOG_INPUT.toString()) || var9.equalsIgnoreCase(ConfigurationType.OPTICAL_DISTANCE_SENSOR.toString())) {
                    var11 = this.c();
                    var5.set(var11.getPort(), var11);
                }

                if(var9.equalsIgnoreCase(ConfigurationType.PULSE_WIDTH_DEVICE.toString())) {
                    var11 = this.c();
                    var3.set(var11.getPort(), var11);
                }

                if(var9.equalsIgnoreCase(ConfigurationType.I2C_DEVICE.toString()) || var9.equalsIgnoreCase(ConfigurationType.IR_SEEKER_V3.toString()) || var9.equalsIgnoreCase(ConfigurationType.ADAFRUIT_COLOR_SENSOR.toString()) || var9.equalsIgnoreCase(ConfigurationType.COLOR_SENSOR.toString()) || var9.equalsIgnoreCase(ConfigurationType.GYRO.toString())) {
                    var11 = this.c();
                    var4.set(var11.getPort(), var11);
                }

                if(var9.equalsIgnoreCase(ConfigurationType.ANALOG_OUTPUT.toString())) {
                    var11 = this.c();
                    var7.set(var11.getPort(), var11);
                }

                if(var9.equalsIgnoreCase(ConfigurationType.DIGITAL_DEVICE.toString()) || var9.equalsIgnoreCase(ConfigurationType.TOUCH_SENSOR.toString()) || var9.equalsIgnoreCase(ConfigurationType.LED.toString())) {
                    var11 = this.c();
                    var6.set(var11.getPort(), var11);
                }
            }

            var8 = this.m.next();
            var9 = this.a(this.m.getName());
        }
    }

    private ControllerConfiguration b() throws IOException, XmlPullParserException, RobotCoreException {
        String var1 = this.m.getAttributeValue((String)null, "name");
        String var2 = this.m.getAttributeValue((String)null, "serialNumber");
        ArrayList var3 = this.a(h, ConfigurationType.NOTHING);
        int var4 = this.m.next();
        String var5 = this.a(this.m.getName());

        while(true) {
            while(true) {
                if(var4 == 1) {
                    return new LegacyModuleControllerConfiguration(var1, var3, new SerialNumber(var2));
                }

                if(var4 != 3) {
                    break;
                }

                if(var5 != null) {
                    if(var5.equalsIgnoreCase(ConfigurationType.LEGACY_MODULE_CONTROLLER.toString())) {
                        LegacyModuleControllerConfiguration var6 = new LegacyModuleControllerConfiguration(var1, var3, new SerialNumber(var2));
                        var6.setEnabled(true);
                        return var6;
                    }
                    break;
                }
            }

            if(var4 == 2) {
                if(b) {
                    RobotLog.e("[handleLegacyModule] tagname: " + var5);
                }

                if(!var5.equalsIgnoreCase(ConfigurationType.COMPASS.toString()) && !var5.equalsIgnoreCase(ConfigurationType.LIGHT_SENSOR.toString()) && !var5.equalsIgnoreCase(ConfigurationType.IR_SEEKER.toString()) && !var5.equalsIgnoreCase(ConfigurationType.ACCELEROMETER.toString()) && !var5.equalsIgnoreCase(ConfigurationType.GYRO.toString()) && !var5.equalsIgnoreCase(ConfigurationType.TOUCH_SENSOR.toString()) && !var5.equalsIgnoreCase(ConfigurationType.TOUCH_SENSOR_MULTIPLEXER.toString()) && !var5.equalsIgnoreCase(ConfigurationType.ULTRASONIC_SENSOR.toString()) && !var5.equalsIgnoreCase(ConfigurationType.COLOR_SENSOR.toString()) && !var5.equalsIgnoreCase(ConfigurationType.NOTHING.toString())) {
                    ControllerConfiguration var8;
                    if(var5.equalsIgnoreCase(ConfigurationType.MOTOR_CONTROLLER.toString())) {
                        var8 = this.b(false);
                        var3.set(var8.getPort(), var8);
                    } else if(var5.equalsIgnoreCase(ConfigurationType.SERVO_CONTROLLER.toString())) {
                        var8 = this.a(false);
                        var3.set(var8.getPort(), var8);
                    } else if(var5.equalsIgnoreCase(ConfigurationType.MATRIX_CONTROLLER.toString())) {
                        var8 = this.d();
                        var3.set(var8.getPort(), var8);
                    }
                } else {
                    DeviceConfiguration var7 = this.c();
                    var3.set(var7.getPort(), var7);
                }
            }

            var4 = this.m.next();
            var5 = this.a(this.m.getName());
        }
    }

    private DeviceConfiguration c() {
        String var1 = this.a(this.m.getName());
        int var2 = Integer.parseInt(this.m.getAttributeValue((String) null, "port"));
        DeviceConfiguration var3 = new DeviceConfiguration(var2);
        var3.setType(var3.typeFromString(var1));
        var3.setName(this.m.getAttributeValue((String)null, "name"));
        if(!var3.getName().equalsIgnoreCase("NO DEVICE ATTACHED")) {
            var3.setEnabled(true);
        }

        if(b) {
            RobotLog.e("[handleDevice] name: " + var3.getName() + ", port: " + var3.getPort() + ", type: " + var3.getType());
        }

        return var3;
    }

    private ArrayList<DeviceConfiguration> a(int var1, ConfigurationType var2) {
        ArrayList var3 = new ArrayList();

        for(int var4 = 0; var4 < var1; ++var4) {
            if(var2 == ConfigurationType.SERVO) {
                var3.add(new ServoConfiguration(var4 + 1, "NO DEVICE ATTACHED", false));
            } else if(var2 == ConfigurationType.MOTOR) {
                var3.add(new MotorConfiguration(var4 + 1, "NO DEVICE ATTACHED", false));
            } else {
                var3.add(new DeviceConfiguration(var4, var2, "NO DEVICE ATTACHED", false));
            }
        }

        return var3;
    }

    private ControllerConfiguration d() throws IOException, XmlPullParserException, RobotCoreException {
        String var1 = this.m.getAttributeValue((String) null, "name");
        String var2 = ControllerConfiguration.NO_SERIAL_NUMBER.toString();
        int var3 = Integer.parseInt(this.m.getAttributeValue((String)null, "port"));
        ArrayList var4 = this.a(l, ConfigurationType.SERVO);
        ArrayList var5 = this.a(k, ConfigurationType.MOTOR);
        int var6 = this.m.next();
        String var7 = this.a(this.m.getName());

        while(true) {
            while(true) {
                if(var6 == 1) {
                    RobotLog.logAndThrow("Reached the end of the XML file while parsing.");
                    return null;
                }

                if(var6 != 3) {
                    break;
                }

                if(var7 != null) {
                    if(var7.equalsIgnoreCase(ConfigurationType.MATRIX_CONTROLLER.toString())) {
                        MatrixControllerConfiguration var8 = new MatrixControllerConfiguration(var1, var5, var4, new SerialNumber(var2));
                        var8.setPort(var3);
                        var8.setEnabled(true);
                        return var8;
                    }
                    break;
                }
            }

            if(var6 == 2) {
                String var9;
                int var11;
                if(var7.equalsIgnoreCase(ConfigurationType.SERVO.toString())) {
                    var11 = Integer.parseInt(this.m.getAttributeValue((String)null, "port"));
                    var9 = this.m.getAttributeValue((String)null, "name");
                    ServoConfiguration var10 = new ServoConfiguration(var11, var9, true);
                    var4.set(var11 - 1, var10);
                } else if(var7.equalsIgnoreCase(ConfigurationType.MOTOR.toString())) {
                    var11 = Integer.parseInt(this.m.getAttributeValue((String)null, "port"));
                    var9 = this.m.getAttributeValue((String)null, "name");
                    MotorConfiguration var12 = new MotorConfiguration(var11, var9, true);
                    var5.set(var11 - 1, var12);
                }
            }

            var6 = this.m.next();
            var7 = this.a(this.m.getName());
        }
    }

    private ControllerConfiguration a(boolean var1) throws IOException, XmlPullParserException {
        String var2 = this.m.getAttributeValue((String) null, "name");
        int var3 = -1;
        String var4 = ControllerConfiguration.NO_SERIAL_NUMBER.toString();
        if(var1) {
            var4 = this.m.getAttributeValue((String)null, "serialNumber");
        } else {
            var3 = Integer.parseInt(this.m.getAttributeValue((String)null, "port"));
        }

        ArrayList var5 = this.a(i, ConfigurationType.SERVO);
        int var6 = this.m.next();
        String var7 = this.a(this.m.getName());

        while(true) {
            while(true) {
                ServoControllerConfiguration var8;
                if(var6 == 1) {
                    var8 = new ServoControllerConfiguration(var2, var5, new SerialNumber(var4));
                    var8.setPort(var3);
                    return var8;
                }

                if(var6 != 3) {
                    break;
                }

                if(var7 != null) {
                    if(var7.equalsIgnoreCase(ConfigurationType.SERVO_CONTROLLER.toString())) {
                        var8 = new ServoControllerConfiguration(var2, var5, new SerialNumber(var4));
                        var8.setPort(var3);
                        var8.setEnabled(true);
                        return var8;
                    }
                    break;
                }
            }

            if(var6 == 2 && var7.equalsIgnoreCase(ConfigurationType.SERVO.toString())) {
                int var11 = Integer.parseInt(this.m.getAttributeValue((String)null, "port"));
                String var9 = this.m.getAttributeValue((String)null, "name");
                ServoConfiguration var10 = new ServoConfiguration(var11, var9, true);
                var5.set(var11 - 1, var10);
            }

            var6 = this.m.next();
            var7 = this.a(this.m.getName());
        }
    }

    private ControllerConfiguration b(boolean var1) throws IOException, XmlPullParserException {
        String var2 = this.m.getAttributeValue((String)null, "name");
        int var3 = -1;
        String var4 = ControllerConfiguration.NO_SERIAL_NUMBER.toString();
        if(var1) {
            var4 = this.m.getAttributeValue((String)null, "serialNumber");
        } else {
            var3 = Integer.parseInt(this.m.getAttributeValue((String)null, "port"));
        }

        ArrayList var5 = this.a(j, ConfigurationType.MOTOR);
        int var6 = this.m.next();
        String var7 = this.a(this.m.getName());

        while(true) {
            while(true) {
                MotorControllerConfiguration var8;
                if(var6 == 1) {
                    var8 = new MotorControllerConfiguration(var2, var5, new SerialNumber(var4));
                    var8.setPort(var3);
                    return var8;
                }

                if(var6 != 3) {
                    break;
                }

                if(var7 != null) {
                    if(var7.equalsIgnoreCase(ConfigurationType.MOTOR_CONTROLLER.toString())) {
                        var8 = new MotorControllerConfiguration(var2, var5, new SerialNumber(var4));
                        var8.setPort(var3);
                        var8.setEnabled(true);
                        return var8;
                    }
                    break;
                }
            }

            if(var6 == 2 && var7.equalsIgnoreCase(ConfigurationType.MOTOR.toString())) {
                int var11 = Integer.parseInt(this.m.getAttributeValue((String)null, "port"));
                String var9 = this.m.getAttributeValue((String)null, "name");
                MotorConfiguration var10 = new MotorConfiguration(var11, var9, true);
                var5.set(var11 - 1, var10);
            }

            var6 = this.m.next();
            var7 = this.a(this.m.getName());
        }
    }

    private String a(String var1) {
        return var1 == null?null:(var1.equalsIgnoreCase("MotorController")?ConfigurationType.MOTOR_CONTROLLER.toString():(var1.equalsIgnoreCase("ServoController")?ConfigurationType.SERVO_CONTROLLER.toString():(var1.equalsIgnoreCase("LegacyModuleController")?ConfigurationType.LEGACY_MODULE_CONTROLLER.toString():(var1.equalsIgnoreCase("DeviceInterfaceModule")?ConfigurationType.DEVICE_INTERFACE_MODULE.toString():(var1.equalsIgnoreCase("AnalogInput")?ConfigurationType.ANALOG_INPUT.toString():(var1.equalsIgnoreCase("OpticalDistanceSensor")?ConfigurationType.OPTICAL_DISTANCE_SENSOR.toString():(var1.equalsIgnoreCase("IrSeeker")?ConfigurationType.IR_SEEKER.toString():(var1.equalsIgnoreCase("LightSensor")?ConfigurationType.LIGHT_SENSOR.toString():(var1.equalsIgnoreCase("DigitalDevice")?ConfigurationType.DIGITAL_DEVICE.toString():(var1.equalsIgnoreCase("TouchSensor")?ConfigurationType.TOUCH_SENSOR.toString():(var1.equalsIgnoreCase("IrSeekerV3")?ConfigurationType.IR_SEEKER_V3.toString():(var1.equalsIgnoreCase("PulseWidthDevice")?ConfigurationType.PULSE_WIDTH_DEVICE.toString():(var1.equalsIgnoreCase("I2cDevice")?ConfigurationType.I2C_DEVICE.toString():(var1.equalsIgnoreCase("AnalogOutput")?ConfigurationType.ANALOG_OUTPUT.toString():(var1.equalsIgnoreCase("TouchSensorMultiplexer")?ConfigurationType.TOUCH_SENSOR_MULTIPLEXER.toString():(var1.equalsIgnoreCase("MatrixController")?ConfigurationType.MATRIX_CONTROLLER.toString():(var1.equalsIgnoreCase("UltrasonicSensor")?ConfigurationType.ULTRASONIC_SENSOR.toString():(var1.equalsIgnoreCase("AdafruitColorSensor")?ConfigurationType.ADAFRUIT_COLOR_SENSOR.toString():(var1.equalsIgnoreCase("ColorSensor")?ConfigurationType.COLOR_SENSOR.toString():(var1.equalsIgnoreCase("Led")?ConfigurationType.LED.toString():(var1.equalsIgnoreCase("Gyro")?ConfigurationType.GYRO.toString():var1)))))))))))))))))))));
    }
}
