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
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ReadXMLFileHandler {
   private static boolean b = false;
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
   List<ControllerConfiguration> a = new ArrayList();
   private XmlPullParser m;

   public ReadXMLFileHandler(Context var1) {
   }

   private ControllerConfiguration a() throws IOException, XmlPullParserException, RobotCoreException {
      String var1 = this.m.getAttributeValue((String)null, "name");
      String var2 = this.m.getAttributeValue((String)null, "serialNumber");
      ArrayList var3 = this.a(c, DeviceConfiguration.ConfigurationType.PULSE_WIDTH_DEVICE);
      ArrayList var4 = this.a(g, DeviceConfiguration.ConfigurationType.I2C_DEVICE);
      ArrayList var5 = this.a(e, DeviceConfiguration.ConfigurationType.ANALOG_INPUT);
      ArrayList var6 = this.a(d, DeviceConfiguration.ConfigurationType.DIGITAL_DEVICE);
      ArrayList var7 = this.a(f, DeviceConfiguration.ConfigurationType.ANALOG_OUTPUT);
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

               if(var9.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.DEVICE_INTERFACE_MODULE.toString())) {
                  DeviceInterfaceModuleConfiguration var20 = new DeviceInterfaceModuleConfiguration(var1, new SerialNumber(var2));
                  var20.setPwmDevices(var3);
                  var20.setI2cDevices(var4);
                  var20.setAnalogInputDevices(var5);
                  var20.setDigitalDevices(var6);
                  var20.setAnalogOutputDevices(var7);
                  var20.setEnabled(true);
                  return var20;
               }
               break;
            }
         }

         if(var8 == 2) {
            if(var9.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.ANALOG_INPUT.toString()) || var9.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.OPTICAL_DISTANCE_SENSOR.toString())) {
               DeviceConfiguration var10 = this.c();
               var5.set(var10.getPort(), var10);
            }

            if(var9.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.PULSE_WIDTH_DEVICE.toString())) {
               DeviceConfiguration var18 = this.c();
               var3.set(var18.getPort(), var18);
            }

            if(var9.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.I2C_DEVICE.toString()) || var9.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.IR_SEEKER_V3.toString()) || var9.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.ADAFRUIT_COLOR_SENSOR.toString()) || var9.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.COLOR_SENSOR.toString())) {
               DeviceConfiguration var12 = this.c();
               var4.set(var12.getPort(), var12);
            }

            if(var9.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.ANALOG_OUTPUT.toString())) {
               DeviceConfiguration var16 = this.c();
               var7.set(var16.getPort(), var16);
            }

            if(var9.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.DIGITAL_DEVICE.toString()) || var9.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.TOUCH_SENSOR.toString()) || var9.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.LED.toString())) {
               DeviceConfiguration var14 = this.c();
               var6.set(var14.getPort(), var14);
            }
         }

         var8 = this.m.next();
         var9 = this.a(this.m.getName());
      }
   }

   private ControllerConfiguration a(boolean var1) throws IOException, XmlPullParserException {
      String var2 = this.m.getAttributeValue((String)null, "name");
      int var3 = -1;
      String var4 = ControllerConfiguration.NO_SERIAL_NUMBER.toString();
      if(var1) {
         var4 = this.m.getAttributeValue((String)null, "serialNumber");
      } else {
         var3 = Integer.parseInt(this.m.getAttributeValue((String)null, "port"));
      }

      ArrayList var5 = this.a(i, DeviceConfiguration.ConfigurationType.SERVO);
      int var6 = this.m.next();
      String var7 = this.a(this.m.getName());

      while(true) {
         while(true) {
            if(var6 == 1) {
               ServoControllerConfiguration var8 = new ServoControllerConfiguration(var2, var5, new SerialNumber(var4));
               var8.setPort(var3);
               return var8;
            }

            if(var6 != 3) {
               break;
            }

            if(var7 != null) {
               if(var7.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER.toString())) {
                  ServoControllerConfiguration var12 = new ServoControllerConfiguration(var2, var5, new SerialNumber(var4));
                  var12.setPort(var3);
                  var12.setEnabled(true);
                  return var12;
               }
               break;
            }
         }

         if(var6 == 2 && var7.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.SERVO.toString())) {
            int var9 = Integer.parseInt(this.m.getAttributeValue((String)null, "port"));
            ServoConfiguration var10 = new ServoConfiguration(var9, this.m.getAttributeValue((String)null, "name"), true);
            var5.set(var9 - 1, var10);
         }

         var6 = this.m.next();
         var7 = this.a(this.m.getName());
      }
   }

   private String a(String var1) {
      if(var1 == null) {
         var1 = null;
      } else {
         if(var1.equalsIgnoreCase("MotorController")) {
            return DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER.toString();
         }

         if(var1.equalsIgnoreCase("ServoController")) {
            return DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER.toString();
         }

         if(var1.equalsIgnoreCase("LegacyModuleController")) {
            return DeviceConfiguration.ConfigurationType.LEGACY_MODULE_CONTROLLER.toString();
         }

         if(var1.equalsIgnoreCase("DeviceInterfaceModule")) {
            return DeviceConfiguration.ConfigurationType.DEVICE_INTERFACE_MODULE.toString();
         }

         if(var1.equalsIgnoreCase("AnalogInput")) {
            return DeviceConfiguration.ConfigurationType.ANALOG_INPUT.toString();
         }

         if(var1.equalsIgnoreCase("OpticalDistanceSensor")) {
            return DeviceConfiguration.ConfigurationType.OPTICAL_DISTANCE_SENSOR.toString();
         }

         if(var1.equalsIgnoreCase("IrSeeker")) {
            return DeviceConfiguration.ConfigurationType.IR_SEEKER.toString();
         }

         if(var1.equalsIgnoreCase("LightSensor")) {
            return DeviceConfiguration.ConfigurationType.LIGHT_SENSOR.toString();
         }

         if(var1.equalsIgnoreCase("DigitalDevice")) {
            return DeviceConfiguration.ConfigurationType.DIGITAL_DEVICE.toString();
         }

         if(var1.equalsIgnoreCase("TouchSensor")) {
            return DeviceConfiguration.ConfigurationType.TOUCH_SENSOR.toString();
         }

         if(var1.equalsIgnoreCase("IrSeekerV3")) {
            return DeviceConfiguration.ConfigurationType.IR_SEEKER_V3.toString();
         }

         if(var1.equalsIgnoreCase("PulseWidthDevice")) {
            return DeviceConfiguration.ConfigurationType.PULSE_WIDTH_DEVICE.toString();
         }

         if(var1.equalsIgnoreCase("I2cDevice")) {
            return DeviceConfiguration.ConfigurationType.I2C_DEVICE.toString();
         }

         if(var1.equalsIgnoreCase("AnalogOutput")) {
            return DeviceConfiguration.ConfigurationType.ANALOG_OUTPUT.toString();
         }

         if(var1.equalsIgnoreCase("TouchSensorMultiplexer")) {
            return DeviceConfiguration.ConfigurationType.TOUCH_SENSOR_MULTIPLEXER.toString();
         }

         if(var1.equalsIgnoreCase("MatrixController")) {
            return DeviceConfiguration.ConfigurationType.MATRIX_CONTROLLER.toString();
         }

         if(var1.equalsIgnoreCase("UltrasonicSensor")) {
            return DeviceConfiguration.ConfigurationType.ULTRASONIC_SENSOR.toString();
         }

         if(var1.equalsIgnoreCase("AdafruitColorSensor")) {
            return DeviceConfiguration.ConfigurationType.ADAFRUIT_COLOR_SENSOR.toString();
         }

         if(var1.equalsIgnoreCase("ColorSensor")) {
            return DeviceConfiguration.ConfigurationType.COLOR_SENSOR.toString();
         }

         if(var1.equalsIgnoreCase("Led")) {
            return DeviceConfiguration.ConfigurationType.LED.toString();
         }
      }

      return var1;
   }

   private ArrayList<DeviceConfiguration> a(int var1, DeviceConfiguration.ConfigurationType var2) {
      ArrayList var3 = new ArrayList();

      for(int var4 = 0; var4 < var1; ++var4) {
         if(var2 == DeviceConfiguration.ConfigurationType.SERVO) {
            var3.add(new ServoConfiguration(var4 + 1, "NO DEVICE ATTACHED", false));
         } else if(var2 == DeviceConfiguration.ConfigurationType.MOTOR) {
            var3.add(new MotorConfiguration(var4 + 1, "NO DEVICE ATTACHED", false));
         } else {
            var3.add(new DeviceConfiguration(var4, var2, "NO DEVICE ATTACHED", false));
         }
      }

      return var3;
   }

   private ControllerConfiguration b() throws IOException, XmlPullParserException, RobotCoreException {
      String var1 = this.m.getAttributeValue((String)null, "name");
      String var2 = this.m.getAttributeValue((String)null, "serialNumber");
      ArrayList var3 = this.a(h, DeviceConfiguration.ConfigurationType.NOTHING);
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
               if(var5.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.LEGACY_MODULE_CONTROLLER.toString())) {
                  LegacyModuleControllerConfiguration var14 = new LegacyModuleControllerConfiguration(var1, var3, new SerialNumber(var2));
                  var14.setEnabled(true);
                  return var14;
               }
               break;
            }
         }

         if(var4 == 2) {
            if(b) {
               RobotLog.e("[handleLegacyModule] tagname: " + var5);
            }

            if(!var5.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.COMPASS.toString()) && !var5.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.LIGHT_SENSOR.toString()) && !var5.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.IR_SEEKER.toString()) && !var5.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.ACCELEROMETER.toString()) && !var5.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.GYRO.toString()) && !var5.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.TOUCH_SENSOR.toString()) && !var5.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.TOUCH_SENSOR_MULTIPLEXER.toString()) && !var5.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.ULTRASONIC_SENSOR.toString()) && !var5.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.COLOR_SENSOR.toString()) && !var5.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.NOTHING.toString())) {
               if(var5.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER.toString())) {
                  ControllerConfiguration var12 = this.b(false);
                  var3.set(var12.getPort(), var12);
               } else if(var5.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER.toString())) {
                  ControllerConfiguration var10 = this.a(false);
                  var3.set(var10.getPort(), var10);
               } else if(var5.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MATRIX_CONTROLLER.toString())) {
                  ControllerConfiguration var8 = this.d();
                  var3.set(var8.getPort(), var8);
               }
            } else {
               DeviceConfiguration var6 = this.c();
               var3.set(var6.getPort(), var6);
            }
         }

         var4 = this.m.next();
         var5 = this.a(this.m.getName());
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

      ArrayList var5 = this.a(j, DeviceConfiguration.ConfigurationType.MOTOR);
      int var6 = this.m.next();
      String var7 = this.a(this.m.getName());

      while(true) {
         while(true) {
            if(var6 == 1) {
               MotorControllerConfiguration var8 = new MotorControllerConfiguration(var2, var5, new SerialNumber(var4));
               var8.setPort(var3);
               return var8;
            }

            if(var6 != 3) {
               break;
            }

            if(var7 != null) {
               if(var7.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER.toString())) {
                  MotorControllerConfiguration var12 = new MotorControllerConfiguration(var2, var5, new SerialNumber(var4));
                  var12.setPort(var3);
                  var12.setEnabled(true);
                  return var12;
               }
               break;
            }
         }

         if(var6 == 2 && var7.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MOTOR.toString())) {
            int var9 = Integer.parseInt(this.m.getAttributeValue((String)null, "port"));
            MotorConfiguration var10 = new MotorConfiguration(var9, this.m.getAttributeValue((String)null, "name"), true);
            var5.set(var9 - 1, var10);
         }

         var6 = this.m.next();
         var7 = this.a(this.m.getName());
      }
   }

   private DeviceConfiguration c() {
      String var1 = this.a(this.m.getName());
      DeviceConfiguration var2 = new DeviceConfiguration(Integer.parseInt(this.m.getAttributeValue((String)null, "port")));
      var2.setType(var2.typeFromString(var1));
      var2.setName(this.m.getAttributeValue((String)null, "name"));
      if(!var2.getName().equalsIgnoreCase("NO DEVICE ATTACHED")) {
         var2.setEnabled(true);
      }

      if(b) {
         RobotLog.e("[handleDevice] name: " + var2.getName() + ", port: " + var2.getPort() + ", type: " + var2.getType());
      }

      return var2;
   }

   private ControllerConfiguration d() throws IOException, XmlPullParserException, RobotCoreException {
      String var1 = this.m.getAttributeValue((String)null, "name");
      String var2 = ControllerConfiguration.NO_SERIAL_NUMBER.toString();
      int var3 = Integer.parseInt(this.m.getAttributeValue((String)null, "port"));
      ArrayList var4 = this.a(l, DeviceConfiguration.ConfigurationType.SERVO);
      ArrayList var5 = this.a(k, DeviceConfiguration.ConfigurationType.MOTOR);
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
               if(var7.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MATRIX_CONTROLLER.toString())) {
                  MatrixControllerConfiguration var14 = new MatrixControllerConfiguration(var1, var5, var4, new SerialNumber(var2));
                  var14.setPort(var3);
                  var14.setEnabled(true);
                  return var14;
               }
               break;
            }
         }

         if(var6 == 2) {
            if(var7.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.SERVO.toString())) {
               int var11 = Integer.parseInt(this.m.getAttributeValue((String)null, "port"));
               ServoConfiguration var12 = new ServoConfiguration(var11, this.m.getAttributeValue((String)null, "name"), true);
               var4.set(var11 - 1, var12);
            } else if(var7.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MOTOR.toString())) {
               int var8 = Integer.parseInt(this.m.getAttributeValue((String)null, "port"));
               MotorConfiguration var9 = new MotorConfiguration(var8, this.m.getAttributeValue((String)null, "name"), true);
               var5.set(var8 - 1, var9);
            }
         }

         var6 = this.m.next();
         var7 = this.a(this.m.getName());
      }
   }

   public List<ControllerConfiguration> getDeviceControllers() {
      return this.a;
   }

   public List<ControllerConfiguration> parse(InputStream param1) throws RobotCoreException {
      // $FF: Couldn't be decompiled
   }
}
