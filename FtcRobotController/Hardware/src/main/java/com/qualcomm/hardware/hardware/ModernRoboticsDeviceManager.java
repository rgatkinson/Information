package com.qualcomm.hardware.hardware;

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

public class ModernRoboticsDeviceManager extends DeviceManager {
   private static ModernRoboticsDeviceManager.States a;

   static {
      a = ModernRoboticsDeviceManager.States.a;
   }

   private final EventLoopManager c;
   private RobotUsbManager b;

   public ModernRoboticsDeviceManager(Context var1, EventLoopManager var2) throws RobotCoreException {
      this.c = var2;
      switch (a.ordinal()) {
      case 1:
         this.b = new RobotUsbManagerEmulator();
         return;
      default:
         this.b = new RobotUsbManagerFtdi(var1);
      }
   }

   public static void disableDeviceEmulation() {
      a = ModernRoboticsDeviceManager.States.a;
   }

   public static void enableDeviceEmulation() {
      a = ModernRoboticsDeviceManager.States.b;
   }

   private ModernRoboticsUsbDeviceInterfaceModule a(DeviceInterfaceModule var1) {
      if(!(var1 instanceof ModernRoboticsUsbDeviceInterfaceModule)) {
         throw new IllegalArgumentException("Modern Robotics Device Manager needs Type1 Modern Robotics Device Interface Module");
      } else {
         return (ModernRoboticsUsbDeviceInterfaceModule)var1;
      }
   }

   private ModernRoboticsUsbLegacyModule a(LegacyModule var1) {
      if(!(var1 instanceof ModernRoboticsUsbLegacyModule)) {
         throw new IllegalArgumentException("Modern Robotics Device Manager needs Type1 Modern Robotics LegacyModule");
      } else {
         return (ModernRoboticsUsbLegacyModule)var1;
      }
   }

   private void a(String var1) throws RobotCoreException {
      System.err.println(var1);
      throw new RobotCoreException(var1);
   }

   public ColorSensor createAdafruitI2cColorSensor(DeviceInterfaceModule var1, int var2) {
      RobotLog.v("Creating Adafruit I2C Color Sensor - Port: " + var2);
      return new AdafruitI2cColorSensor(var1, var2);
   }

   public AnalogInput createAnalogInputDevice(AnalogInputController var1, int var2) {
      RobotLog.v("Creating Analog Input Device - Port: " + var2);
      return new AnalogInput(var1, var2);
   }

   public OpticalDistanceSensor createAnalogOpticalDistanceSensor(DeviceInterfaceModule var1, int var2) {
      RobotLog.v("Creating Modern Robotics Analog Optical Distance Sensor - Port: " + var2);
      return new ModernRoboticsAnalogOpticalDistanceSensor(this.a(var1), var2);
   }

   public AnalogOutput createAnalogOutputDevice(AnalogOutputController var1, int var2) {
      RobotLog.v("Creating Analog Output Device - Port: " + var2);
      return new AnalogOutput(var1, var2);
   }

   public DeviceInterfaceModule createDeviceInterfaceModule(SerialNumber var1) throws RobotCoreException, InterruptedException {
      RobotLog.v("Creating Modern Robotics USB Core Device Interface Module - " + var1.toString());

      try {
         RobotUsbDevice var3 = ModernRoboticsUsbUtil.openUsbDevice(this.b, var1);
         if(ModernRoboticsUsbUtil.getDeviceType(ModernRoboticsUsbUtil.getUsbDeviceHeader(var3)) != DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE) {
            this.a(var1.toString() + " is not Type1 Modern Robotics USB Core Device Interface Module");
         }

         ModernRoboticsUsbDeviceInterfaceModule var4 = new ModernRoboticsUsbDeviceInterfaceModule(var1, var3, this.c);
         return var4;
      } catch (RobotCoreException var5) {
         RobotLog.setGlobalErrorMsgAndThrow("Unable to open Modern Robotics USB Core Device Interface Module", var5);
         return null;
      }
   }

   public DigitalChannel createDigitalChannelDevice(DigitalChannelController var1, int var2) {
      RobotLog.v("Creating Digital Channel Device - Port: " + var2);
      return new DigitalChannel(var1, var2);
   }

   public TouchSensor createDigitalTouchSensor(DeviceInterfaceModule var1, int var2) {
      RobotLog.v("Creating Modern Robotics Digital Touch Sensor - Port: " + var2);
      return new ModernRoboticsDigitalTouchSensor(this.a(var1), var2);
   }

   public I2cDevice createI2cDevice(I2cController var1, int var2) {
      RobotLog.v("Creating I2C Device - Port: " + var2);
      return new I2cDevice(var1, var2);
   }

   public IrSeekerSensor createI2cIrSeekerSensorV3(DeviceInterfaceModule var1, int var2) {
      RobotLog.v("Creating Modern Robotics I2C IR Seeker Sensor V3 - Port: " + var2);
      return new ModernRoboticsI2cIrSeekerSensorV3(this.a(var1), var2);
   }

   public LED createLED(DigitalChannelController var1, int var2) {
      RobotLog.v("Creating LED - Port: " + var2);
      return new LED(var1, var2);
   }

   public ColorSensor createModernRoboticsI2cColorSensor(DeviceInterfaceModule var1, int var2) {
      RobotLog.v("Creating Modern Robotics I2C Color Sensor - Port: " + var2);
      return new ModernRoboticsI2cColorSensor(var1, var2);
   }

   public AccelerationSensor createNxtAccelerationSensor(LegacyModule var1, int var2) {
      RobotLog.v("Creating HiTechnic NXT Acceleration Sensor - Port: " + var2);
      return new HiTechnicNxtAccelerationSensor(this.a(var1), var2);
   }

   public ColorSensor createNxtColorSensor(LegacyModule var1, int var2) {
      RobotLog.v("Creating HiTechnic NXT Color Sensor - Port: " + var2);
      return new HiTechnicNxtColorSensor(var1, var2);
   }

   public CompassSensor createNxtCompassSensor(LegacyModule var1, int var2) {
      RobotLog.v("Creating HiTechnic NXT Compass Sensor - Port: " + var2);
      return new HiTechnicNxtCompassSensor(this.a(var1), var2);
   }

   public DcMotorController createNxtDcMotorController(LegacyModule var1, int var2) {
      RobotLog.v("Creating HiTechnic NXT DC Motor Controller - Port: " + var2);
      return new HiTechnicNxtDcMotorController(this.a(var1), var2);
   }

   public GyroSensor createNxtGyroSensor(LegacyModule var1, int var2) {
      RobotLog.v("Creating HiTechnic NXT Gyro Sensor - Port: " + var2);
      return new HiTechnicNxtGyroSensor(this.a(var1), var2);
   }

   public IrSeekerSensor createNxtIrSeekerSensor(LegacyModule var1, int var2) {
      RobotLog.v("Creating HiTechnic NXT IR Seeker Sensor - Port: " + var2);
      return new HiTechnicNxtIrSeekerSensor(this.a(var1), var2);
   }

   public LightSensor createNxtLightSensor(LegacyModule var1, int var2) {
      RobotLog.v("Creating HiTechnic NXT Light Sensor - Port: " + var2);
      return new HiTechnicNxtLightSensor(this.a(var1), var2);
   }

   public ServoController createNxtServoController(LegacyModule var1, int var2) {
      RobotLog.v("Creating HiTechnic NXT Servo Controller - Port: " + var2);
      return new HiTechnicNxtServoController(this.a(var1), var2);
   }

   public TouchSensor createNxtTouchSensor(LegacyModule var1, int var2) {
      RobotLog.v("Creating HiTechnic NXT Touch Sensor - Port: " + var2);
      return new HiTechnicNxtTouchSensor(this.a(var1), var2);
   }

   public TouchSensorMultiplexer createNxtTouchSensorMultiplexer(LegacyModule var1, int var2) {
      RobotLog.v("Creating HiTechnic NXT Touch Sensor Multiplexer - Port: " + var2);
      return new HiTechnicNxtTouchSensorMultiplexer(this.a(var1), var2);
   }

   public UltrasonicSensor createNxtUltrasonicSensor(LegacyModule var1, int var2) {
      RobotLog.v("Creating HiTechnic NXT Ultrasonic Sensor - Port: " + var2);
      return new HiTechnicNxtUltrasonicSensor(this.a(var1), var2);
   }

   public PWMOutput createPwmOutputDevice(DeviceInterfaceModule var1, int var2) {
      RobotLog.v("Creating PWM Output Device - Port: " + var2);
      return new PWMOutput(var1, var2);
   }

   public DcMotorController createUsbDcMotorController(SerialNumber var1) throws RobotCoreException, InterruptedException {
      RobotLog.v("Creating Modern Robotics USB DC Motor Controller - " + var1.toString());

      try {
         RobotUsbDevice var3 = ModernRoboticsUsbUtil.openUsbDevice(this.b, var1);
         if(ModernRoboticsUsbUtil.getDeviceType(ModernRoboticsUsbUtil.getUsbDeviceHeader(var3)) != DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER) {
            this.a(var1.toString() + " is not Type1 Modern Robotics USB DC Motor Controller");
         }

         ModernRoboticsUsbDcMotorController var4 = new ModernRoboticsUsbDcMotorController(var1, var3, this.c);
         return var4;
      } catch (RobotCoreException var5) {
         RobotLog.setGlobalErrorMsgAndThrow("Unable to open Modern Robotics USB DC Motor Controller", var5);
         return null;
      }
   }

   public LegacyModule createUsbLegacyModule(SerialNumber var1) throws RobotCoreException, InterruptedException {
      RobotLog.v("Creating Modern Robotics USB Legacy Module - " + var1.toString());

      try {
         RobotUsbDevice var3 = ModernRoboticsUsbUtil.openUsbDevice(this.b, var1);
         if(ModernRoboticsUsbUtil.getDeviceType(ModernRoboticsUsbUtil.getUsbDeviceHeader(var3)) != DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE) {
            this.a(var1.toString() + " is not Type1 Modern Robotics USB Legacy Module");
         }

         ModernRoboticsUsbLegacyModule var4 = new ModernRoboticsUsbLegacyModule(var1, var3, this.c);
         return var4;
      } catch (RobotCoreException var5) {
         RobotLog.setGlobalErrorMsgAndThrow("Unable to open Modern Robotics USB Legacy Module", var5);
         return null;
      }
   }

   public ServoController createUsbServoController(SerialNumber var1) throws RobotCoreException, InterruptedException {
      RobotLog.v("Creating Modern Robotics USB Servo Controller - " + var1.toString());

      try {
         RobotUsbDevice var3 = ModernRoboticsUsbUtil.openUsbDevice(this.b, var1);
         if(ModernRoboticsUsbUtil.getDeviceType(ModernRoboticsUsbUtil.getUsbDeviceHeader(var3)) != DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER) {
            this.a(var1.toString() + " is not Type1 Modern Robotics USB Servo Controller");
         }

         ModernRoboticsUsbServoController var4 = new ModernRoboticsUsbServoController(var1, var3, this.c);
         return var4;
      } catch (RobotCoreException var5) {
         RobotLog.setGlobalErrorMsgAndThrow("Unable to open Modern Robotics USB Servo Controller", var5);
         return null;
      }
   }

   public Map<SerialNumber, DeviceManager.DeviceType> scanForUsbDevices() throws RobotCoreException {
      HashMap var1 = new HashMap();

      try {
         int var2 = this.b.scanForDevices();

         for (int var3 = 0; var3 < var2; ++var3) {
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

   private enum States {
      a,
      b;

      static {
         States[] var0 = new States[]{a, b};
      }
   }
}
