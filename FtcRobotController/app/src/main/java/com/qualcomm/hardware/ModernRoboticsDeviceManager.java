package com.qualcomm.hardware;

import android.content.Context;
import com.qualcomm.hardware.AdafruitColorSensor;
import com.qualcomm.hardware.ModernRoboticsColorSensor;
import com.qualcomm.hardware.ModernRoboticsIrSeekerSensorV3;
import com.qualcomm.hardware.ModernRoboticsNxtAccelerationSensor;
import com.qualcomm.hardware.ModernRoboticsNxtColorSensor;
import com.qualcomm.hardware.ModernRoboticsNxtCompassSensor;
import com.qualcomm.hardware.ModernRoboticsNxtDcMotorController;
import com.qualcomm.hardware.ModernRoboticsNxtGyroSensor;
import com.qualcomm.hardware.ModernRoboticsNxtIrSeekerSensor;
import com.qualcomm.hardware.ModernRoboticsNxtLightSensor;
import com.qualcomm.hardware.ModernRoboticsNxtServoController;
import com.qualcomm.hardware.ModernRoboticsNxtTouchSensor;
import com.qualcomm.hardware.ModernRoboticsNxtTouchSensorMultiplexer;
import com.qualcomm.hardware.ModernRoboticsNxtUltrasonicSensor;
import com.qualcomm.hardware.ModernRoboticsOpticalDistanceSensor;
import com.qualcomm.hardware.ModernRoboticsTouchSensor;
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
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
import com.qualcomm.robotcore.hardware.usb.ftdi.RobotUsbManagerFtdi;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.Map;

public class ModernRoboticsDeviceManager extends DeviceManager {
   private static ModernRoboticsDeviceManager.a a;
   private RobotUsbManager b;
   private final EventLoopManager c;

   static {
      a = ModernRoboticsDeviceManager.a.a;
   }

   public ModernRoboticsDeviceManager(Context var1, EventLoopManager var2) throws RobotCoreException {
      this.c = var2;
      switch(null.a[a.ordinal()]) {
      case 1:
         this.b = new RobotUsbManagerEmulator();
         return;
      default:
         this.b = new RobotUsbManagerFtdi(var1);
      }
   }

   private ModernRoboticsUsbDeviceInterfaceModule a(DeviceInterfaceModule var1) {
      if(!(var1 instanceof ModernRoboticsUsbDeviceInterfaceModule)) {
         throw new IllegalArgumentException("Modern Robotics Device Manager needs a Modern Robotics Device Interface Module");
      } else {
         return (ModernRoboticsUsbDeviceInterfaceModule)var1;
      }
   }

   private ModernRoboticsUsbLegacyModule a(LegacyModule var1) {
      if(!(var1 instanceof ModernRoboticsUsbLegacyModule)) {
         throw new IllegalArgumentException("Modern Robotics Device Manager needs a Modern Robotics LegacyModule");
      } else {
         return (ModernRoboticsUsbLegacyModule)var1;
      }
   }

   private void a(String var1) throws RobotCoreException {
      System.err.println(var1);
      throw new RobotCoreException(var1);
   }

   public static void disableDeviceEmulation() {
      a = ModernRoboticsDeviceManager.a.a;
   }

   public static void enableDeviceEmulation() {
      a = ModernRoboticsDeviceManager.a.b;
   }

   public ColorSensor createAdafruitColorSensor(DeviceInterfaceModule var1, int var2) {
      RobotLog.v("Creating Adafruit Color Sensor - Port: " + var2);
      return new AdafruitColorSensor(var1, var2);
   }

   public AnalogInput createAnalogInputDevice(AnalogInputController var1, int var2) {
      RobotLog.v("Creating Analog Input Device - Port: " + var2);
      return new AnalogInput(var1, var2);
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
            this.a(var1.toString() + " is not a Modern Robotics USB Core Device Interface Module");
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

   public I2cDevice createI2cDevice(I2cController var1, int var2) {
      RobotLog.v("Creating I2C Device - Port: " + var2);
      return new I2cDevice(var1, var2);
   }

   public IrSeekerSensor createIrSeekerSensorV3(DeviceInterfaceModule var1, int var2) {
      RobotLog.v("Creating Modern Robotics IR Seeker Sensor V3 - Port: " + var2);
      return new ModernRoboticsIrSeekerSensorV3(this.a(var1), var2);
   }

   public LED createLED(DigitalChannelController var1, int var2) {
      RobotLog.v("Creating LED - Port: " + var2);
      return new LED(var1, var2);
   }

   public ColorSensor createModernRoboticsColorSensor(DeviceInterfaceModule var1, int var2) {
      RobotLog.v("Creating ModernRobotics Color Sensor - Port: " + var2);
      return new ModernRoboticsColorSensor(var1, var2);
   }

   public AccelerationSensor createNxtAccelerationSensor(LegacyModule var1, int var2) {
      RobotLog.v("Creating Modern Robotics NXT Acceleration Sensor - Port: " + var2);
      return new ModernRoboticsNxtAccelerationSensor(this.a(var1), var2);
   }

   public ColorSensor createNxtColorSensor(LegacyModule var1, int var2) {
      RobotLog.v("Creating NXT Color Sensor - Port: " + var2);
      return new ModernRoboticsNxtColorSensor(var1, var2);
   }

   public CompassSensor createNxtCompassSensor(LegacyModule var1, int var2) {
      RobotLog.v("Creating Modern Robotics NXT Compass Sensor - Port: " + var2);
      return new ModernRoboticsNxtCompassSensor(this.a(var1), var2);
   }

   public DcMotorController createNxtDcMotorController(LegacyModule var1, int var2) {
      RobotLog.v("Creating Modern Robotics NXT DC Motor Controller - Port: " + var2);
      return new ModernRoboticsNxtDcMotorController(this.a(var1), var2);
   }

   public GyroSensor createNxtGyroSensor(LegacyModule var1, int var2) {
      RobotLog.v("Creating Modern Robotics NXT Gyro Sensor - Port: " + var2);
      return new ModernRoboticsNxtGyroSensor(this.a(var1), var2);
   }

   public IrSeekerSensor createNxtIrSeekerSensor(LegacyModule var1, int var2) {
      RobotLog.v("Creating Modern Robotics NXT IR Seeker Sensor - Port: " + var2);
      return new ModernRoboticsNxtIrSeekerSensor(this.a(var1), var2);
   }

   public LightSensor createNxtLightSensor(LegacyModule var1, int var2) {
      RobotLog.v("Creating Modern Robotics NXT Light Sensor - Port: " + var2);
      return new ModernRoboticsNxtLightSensor(this.a(var1), var2);
   }

   public ServoController createNxtServoController(LegacyModule var1, int var2) {
      RobotLog.v("Creating Modern Robotics NXT Servo Controller - Port: " + var2);
      return new ModernRoboticsNxtServoController(this.a(var1), var2);
   }

   public TouchSensor createNxtTouchSensor(LegacyModule var1, int var2) {
      RobotLog.v("Creating Modern Robotics NXT Touch Sensor - Port: " + var2);
      return new ModernRoboticsNxtTouchSensor(this.a(var1), var2);
   }

   public TouchSensorMultiplexer createNxtTouchSensorMultiplexer(LegacyModule var1, int var2) {
      RobotLog.v("Creating Modern Robotics NXT Touch Sensor Multiplexer - Port: " + var2);
      return new ModernRoboticsNxtTouchSensorMultiplexer(this.a(var1), var2);
   }

   public UltrasonicSensor createNxtUltrasonicSensor(LegacyModule var1, int var2) {
      RobotLog.v("Creating Modern Robotics NXT Ultrasonic Sensor - Port: " + var2);
      return new ModernRoboticsNxtUltrasonicSensor(this.a(var1), var2);
   }

   public OpticalDistanceSensor createOpticalDistanceSensor(DeviceInterfaceModule var1, int var2) {
      RobotLog.v("Creating Modern Robotics Optical Distance Sensor - Port: " + var2);
      return new ModernRoboticsOpticalDistanceSensor(this.a(var1), var2);
   }

   public PWMOutput createPwmOutputDevice(DeviceInterfaceModule var1, int var2) {
      RobotLog.v("Creating PWM Output Device - Port: " + var2);
      return new PWMOutput(var1, var2);
   }

   public TouchSensor createTouchSensor(DeviceInterfaceModule var1, int var2) {
      RobotLog.v("Creating Modern Robotics Touch Sensor - Port: " + var2);
      return new ModernRoboticsTouchSensor(this.a(var1), var2);
   }

   public DcMotorController createUsbDcMotorController(SerialNumber var1) throws RobotCoreException, InterruptedException {
      RobotLog.v("Creating Modern Robotics USB DC Motor Controller - " + var1.toString());

      try {
         RobotUsbDevice var3 = ModernRoboticsUsbUtil.openUsbDevice(this.b, var1);
         if(ModernRoboticsUsbUtil.getDeviceType(ModernRoboticsUsbUtil.getUsbDeviceHeader(var3)) != DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER) {
            this.a(var1.toString() + " is not a Modern Robotics USB DC Motor Controller");
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
            this.a(var1.toString() + " is not a Modern Robotics USB Legacy Module");
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
            this.a(var1.toString() + " is not a Modern Robotics USB Servo Controller");
         }

         ModernRoboticsUsbServoController var4 = new ModernRoboticsUsbServoController(var1, var3, this.c);
         return var4;
      } catch (RobotCoreException var5) {
         RobotLog.setGlobalErrorMsgAndThrow("Unable to open Modern Robotics USB Servo Controller", var5);
         return null;
      }
   }

   public Map<SerialNumber, DeviceManager.DeviceType> scanForUsbDevices() throws RobotCoreException {
      // $FF: Couldn't be decompiled
   }

   private static enum a {
      a,
      b;

      static {
         ModernRoboticsDeviceManager.a[] var0 = new ModernRoboticsDeviceManager.a[]{a, b};
      }
   }
}
