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
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
import com.qualcomm.robotcore.hardware.usb.ftdi.RobotUsbManagerFtdi;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.Map;

public class HardwareDeviceManager extends DeviceManager {
   private static HardwareDeviceManager.a a;
   private RobotUsbManager b;
   private final EventLoopManager c;

   static {
      a = HardwareDeviceManager.a.a;
   }

   public HardwareDeviceManager(Context var1, EventLoopManager var2) throws RobotCoreException {
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

   private void a(RobotUsbDevice var1, String var2, SerialNumber var3) throws RobotCoreException {
      String var4 = var2 + " [" + var3 + "] is returning garbage data via the USB bus";
      var1.close();
      this.a(var4);
   }

   private void a(String var1) throws RobotCoreException {
      System.err.println(var1);
      throw new RobotCoreException(var1);
   }

   public static void disableDeviceEmulation() {
      a = HardwareDeviceManager.a.a;
   }

   public static void enableDeviceEmulation() {
      a = HardwareDeviceManager.a.b;
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
            this.a(var3, "Modern Robotics USB Core Device Interface Module", var1);
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

   public GyroSensor createModernRoboticsI2cGyroSensor(DeviceInterfaceModule var1, int var2) {
      RobotLog.v("Creating Modern Robotics I2C Gyro Sensor - Port: " + var2);
      return new ModernRoboticsI2cGyro(var1, var2);
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
            this.a(var3, "Modern Robotics USB DC Motor Controller", var1);
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
            this.a(var3, "Modern Robotics USB Legacy Module", var1);
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
            this.a(var3, "Modern Robotics USB Servo Controller", var1);
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
         HardwareDeviceManager.a[] var0 = new HardwareDeviceManager.a[]{a, b};
      }
   }
}
