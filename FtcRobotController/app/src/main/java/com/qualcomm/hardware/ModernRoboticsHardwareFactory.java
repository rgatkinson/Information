package com.qualcomm.hardware;

import android.content.Context;

import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogOutput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareFactory;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.PWMOutput;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.TouchSensorMultiplexer;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceInterfaceModuleConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MatrixControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ReadXMLFileHandler;
import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public class ModernRoboticsHardwareFactory implements HardwareFactory {
   private Context context;
   private InputStream inputStream = null;

   public ModernRoboticsHardwareFactory(Context var1) {
      this.context = var1;
   }

   private void a(HardwareMap var1, DeviceManager var2, DeviceInterfaceModule var3, DeviceConfiguration var4) {
      IrSeekerSensor var5 = var2.createI2cIrSeekerSensorV3(var3, var4.getPort());
      var1.irSeekerSensor.put(var4.getName(), var5);
   }

   private void a(HardwareMap var1, DeviceManager var2, DigitalChannelController var3, DeviceConfiguration var4) {
      LED var5 = var2.createLED(var3, var4.getPort());
      var1.led.put(var4.getName(), var5);
   }

   private void a(HardwareMap var1, DeviceManager var2, LegacyModule var3, DeviceConfiguration var4) {
      TouchSensor var5 = var2.createNxtTouchSensor(var3, var4.getPort());
      var1.touchSensor.put(var4.getName(), var5);
   }

   private void a(HardwareMap var1, DeviceManager var2, ControllerConfiguration var3) throws RobotCoreException, InterruptedException {
      ModernRoboticsUsbDcMotorController var4 = (ModernRoboticsUsbDcMotorController)var2.createUsbDcMotorController(var3.getSerialNumber());
      var1.dcMotorController.put(var3.getName(), var4);
      Iterator var5 = var3.getDevices().iterator();

      while(var5.hasNext()) {
         DeviceConfiguration var6 = (DeviceConfiguration)var5.next();
         if(var6.isEnabled()) {
            DcMotor var7 = var2.createDcMotor(var4, var6.getPort());
            var1.dcMotor.put(var6.getName(), var7);
         }
      }

      var1.voltageSensor.put(var3.getName(), var4);
   }

   private void a(List<DeviceConfiguration> var1, HardwareMap var2, DeviceManager var3, DeviceInterfaceModule var4) {
      Iterator var5 = var1.iterator();

      while(var5.hasNext()) {
         DeviceConfiguration var6 = (DeviceConfiguration)var5.next();
         if(var6.isEnabled()) {
            DeviceConfiguration.ConfigurationType var7 = var6.getType();
            switch(null.a[var7.ordinal()]) {
            case 5:
               this.h(var2, var3, var4, var6);
               break;
            case 6:
               this.d(var2, var3, var4, var6);
               break;
            case 7:
               this.c(var2, var3, var4, var6);
               break;
            case 8:
               this.b(var2, var3, var4, var6);
               break;
            case 9:
               this.e(var2, var3, var4, var6);
               break;
            case 10:
               this.a(var2, var3, var4, var6);
               break;
            case 11:
               this.f(var2, var3, var4, var6);
               break;
            case 12:
               this.g(var2, var3, var4, var6);
               break;
            case 13:
               this.i(var2, var3, var4, var6);
               break;
            case 14:
               this.a((HardwareMap)var2, (DeviceManager)var3, (DigitalChannelController)var4, (DeviceConfiguration)var6);
               break;
            case 15:
               this.j(var2, var3, var4, var6);
            case 16:
               break;
            default:
               RobotLog.w("Unexpected device type connected to Device Interface Module while parsing XML: " + var7.toString());
            }
         }
      }

   }

   private void b(HardwareMap var1, DeviceManager var2, DeviceInterfaceModule var3, DeviceConfiguration var4) {
      DigitalChannel var5 = var2.createDigitalChannelDevice(var3, var4.getPort());
      var1.digitalChannel.put(var4.getName(), var5);
   }

   private void b(HardwareMap var1, DeviceManager var2, LegacyModule var3, DeviceConfiguration var4) {
      TouchSensorMultiplexer var5 = var2.createNxtTouchSensorMultiplexer(var3, var4.getPort());
      var1.touchSensorMultiplexer.put(var4.getName(), var5);
   }

   private void b(HardwareMap var1, DeviceManager var2, ControllerConfiguration var3) throws RobotCoreException, InterruptedException {
      ServoController var4 = var2.createUsbServoController(var3.getSerialNumber());
      var1.servoController.put(var3.getName(), var4);
      Iterator var5 = var3.getDevices().iterator();

      while(var5.hasNext()) {
         DeviceConfiguration var6 = (DeviceConfiguration)var5.next();
         if(var6.isEnabled()) {
            Servo var7 = var2.createServo(var4, var6.getPort());
            var1.servo.put(var6.getName(), var7);
         }
      }

   }

   private void c(HardwareMap var1, DeviceManager var2, DeviceInterfaceModule var3, DeviceConfiguration var4) {
      TouchSensor var5 = var2.createDigitalTouchSensor(var3, var4.getPort());
      var1.touchSensor.put(var4.getName(), var5);
   }

   private void c(HardwareMap var1, DeviceManager var2, LegacyModule var3, DeviceConfiguration var4) {
      UltrasonicSensor var5 = var2.createNxtUltrasonicSensor(var3, var4.getPort());
      var1.ultrasonicSensor.put(var4.getName(), var5);
   }

   private void c(HardwareMap var1, DeviceManager var2, ControllerConfiguration var3) throws RobotCoreException, InterruptedException {
      DeviceInterfaceModule var4 = var2.createDeviceInterfaceModule(var3.getSerialNumber());
      var1.deviceInterfaceModule.put(var3.getName(), var4);
      this.a(((DeviceInterfaceModuleConfiguration)var3).getPwmDevices(), var1, var2, var4);
      this.a(((DeviceInterfaceModuleConfiguration)var3).getI2cDevices(), var1, var2, var4);
      this.a(((DeviceInterfaceModuleConfiguration)var3).getAnalogInputDevices(), var1, var2, var4);
      this.a(((DeviceInterfaceModuleConfiguration)var3).getDigitalDevices(), var1, var2, var4);
      this.a(((DeviceInterfaceModuleConfiguration)var3).getAnalogOutputDevices(), var1, var2, var4);
   }

   private void d(HardwareMap var1, DeviceManager var2, DeviceInterfaceModule var3, DeviceConfiguration var4) {
      AnalogInput var5 = var2.createAnalogInputDevice(var3, var4.getPort());
      var1.analogInput.put(var4.getName(), var5);
   }

   private void d(HardwareMap var1, DeviceManager var2, LegacyModule var3, DeviceConfiguration var4) {
      ColorSensor var5 = var2.createNxtColorSensor(var3, var4.getPort());
      var1.colorSensor.put(var4.getName(), var5);
   }

   private void d(HardwareMap var1, DeviceManager var2, ControllerConfiguration var3) throws RobotCoreException, InterruptedException {
      LegacyModule var4 = var2.createUsbLegacyModule(var3.getSerialNumber());
      var1.legacyModule.put(var3.getName(), var4);
      Iterator var5 = var3.getDevices().iterator();

      while(var5.hasNext()) {
         DeviceConfiguration var6 = (DeviceConfiguration)var5.next();
         if(var6.isEnabled()) {
            DeviceConfiguration.ConfigurationType var7 = var6.getType();
            switch(null.a[var7.ordinal()]) {
            case 1:
               this.j(var1, var2, var4, var6);
               break;
            case 2:
               this.k(var1, var2, var4, var6);
               break;
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            default:
               RobotLog.w("Unexpected device type connected to Legacy Module while parsing XML: " + var7.toString());
               break;
            case 7:
               this.a(var1, var2, var4, var6);
               break;
            case 15:
               this.d(var1, var2, var4, var6);
            case 16:
               break;
            case 17:
               this.e(var1, var2, var4, var6);
               break;
            case 18:
               this.f(var1, var2, var4, var6);
               break;
            case 19:
               this.g(var1, var2, var4, var6);
               break;
            case 20:
               this.h(var1, var2, var4, var6);
               break;
            case 21:
               this.i(var1, var2, var4, var6);
               break;
            case 22:
               this.b(var1, var2, var4, var6);
               break;
            case 23:
               this.c(var1, var2, var4, var6);
               break;
            case 24:
               this.l(var1, var2, var4, var6);
            }
         }
      }

   }

   public static void disableDeviceEmulation() {
      ModernRoboticsDeviceManager.disableDeviceEmulation();
   }

   private void e(HardwareMap var1, DeviceManager var2, DeviceInterfaceModule var3, DeviceConfiguration var4) {
      PWMOutput var5 = var2.createPwmOutputDevice(var3, var4.getPort());
      var1.pwmOutput.put(var4.getName(), var5);
   }

   private void e(HardwareMap var1, DeviceManager var2, LegacyModule var3, DeviceConfiguration var4) {
      GyroSensor var5 = var2.createNxtGyroSensor(var3, var4.getPort());
      var1.gyroSensor.put(var4.getName(), var5);
   }

   public static void enableDeviceEmulation() {
      ModernRoboticsDeviceManager.enableDeviceEmulation();
   }

   private void f(HardwareMap var1, DeviceManager var2, DeviceInterfaceModule var3, DeviceConfiguration var4) {
      I2cDevice var5 = var2.createI2cDevice(var3, var4.getPort());
      var1.i2cDevice.put(var4.getName(), var5);
   }

   private void f(HardwareMap var1, DeviceManager var2, LegacyModule var3, DeviceConfiguration var4) {
      CompassSensor var5 = var2.createNxtCompassSensor(var3, var4.getPort());
      var1.compassSensor.put(var4.getName(), var5);
   }

   private void g(HardwareMap var1, DeviceManager var2, DeviceInterfaceModule var3, DeviceConfiguration var4) {
      AnalogOutput var5 = var2.createAnalogOutputDevice(var3, var4.getPort());
      var1.analogOutput.put(var4.getName(), var5);
   }

   private void g(HardwareMap var1, DeviceManager var2, LegacyModule var3, DeviceConfiguration var4) {
      IrSeekerSensor var5 = var2.createNxtIrSeekerSensor(var3, var4.getPort());
      var1.irSeekerSensor.put(var4.getName(), var5);
   }

   private void h(HardwareMap var1, DeviceManager var2, DeviceInterfaceModule var3, DeviceConfiguration var4) {
      OpticalDistanceSensor var5 = var2.createAnalogOpticalDistanceSensor(var3, var4.getPort());
      var1.opticalDistanceSensor.put(var4.getName(), var5);
   }

   private void h(HardwareMap var1, DeviceManager var2, LegacyModule var3, DeviceConfiguration var4) {
      LightSensor var5 = var2.createNxtLightSensor(var3, var4.getPort());
      var1.lightSensor.put(var4.getName(), var5);
   }

   private void i(HardwareMap var1, DeviceManager var2, DeviceInterfaceModule var3, DeviceConfiguration var4) {
      ColorSensor var5 = var2.createAdafruitI2cColorSensor(var3, var4.getPort());
      var1.colorSensor.put(var4.getName(), var5);
   }

   private void i(HardwareMap var1, DeviceManager var2, LegacyModule var3, DeviceConfiguration var4) {
      AccelerationSensor var5 = var2.createNxtAccelerationSensor(var3, var4.getPort());
      var1.accelerationSensor.put(var4.getName(), var5);
   }

   private void j(HardwareMap var1, DeviceManager var2, DeviceInterfaceModule var3, DeviceConfiguration var4) {
      ColorSensor var5 = var2.createModernRoboticsI2cColorSensor(var3, var4.getPort());
      var1.colorSensor.put(var4.getName(), var5);
   }

   private void j(HardwareMap var1, DeviceManager var2, LegacyModule var3, DeviceConfiguration var4) {
      DcMotorController var5 = var2.createNxtDcMotorController(var3, var4.getPort());
      var1.dcMotorController.put(var4.getName(), var5);
      Iterator var6 = ((MotorControllerConfiguration)var4).getMotors().iterator();

      while(var6.hasNext()) {
         DeviceConfiguration var7 = (DeviceConfiguration)var6.next();
         DcMotor var8 = var2.createDcMotor(var5, var7.getPort());
         var1.dcMotor.put(var7.getName(), var8);
      }

   }

   private void k(HardwareMap var1, DeviceManager var2, LegacyModule var3, DeviceConfiguration var4) {
      ServoController var5 = var2.createNxtServoController(var3, var4.getPort());
      var1.servoController.put(var4.getName(), var5);
      Iterator var6 = ((ServoControllerConfiguration)var4).getServos().iterator();

      while(var6.hasNext()) {
         DeviceConfiguration var7 = (DeviceConfiguration)var6.next();
         Servo var8 = var2.createServo(var5, var7.getPort());
         var1.servo.put(var7.getName(), var8);
      }

   }

   private void l(HardwareMap var1, DeviceManager var2, LegacyModule var3, DeviceConfiguration var4) {
      MatrixMasterController var5 = new MatrixMasterController((ModernRoboticsUsbLegacyModule)var3, var4.getPort());
      MatrixDcMotorController var6 = new MatrixDcMotorController(var5);
      var1.dcMotorController.put(var4.getName() + "Motor", var6);
      Iterator var7 = ((MatrixControllerConfiguration)var4).getMotors().iterator();

      while(var7.hasNext()) {
         DeviceConfiguration var12 = (DeviceConfiguration)var7.next();
         DcMotor var13 = var2.createDcMotor(var6, var12.getPort());
         var1.dcMotor.put(var12.getName(), var13);
      }

      MatrixServoController var8 = new MatrixServoController(var5);
      var1.servoController.put(var4.getName() + "Servo", var8);
      Iterator var9 = ((MatrixControllerConfiguration)var4).getServos().iterator();

      while(var9.hasNext()) {
         DeviceConfiguration var10 = (DeviceConfiguration)var9.next();
         Servo var11 = var2.createServo(var8, var10.getPort());
         var1.servo.put(var10.getName(), var11);
      }

   }

   public HardwareMap createHardwareMap(EventLoopManager var1) throws RobotCoreException, InterruptedException {
      if(this.inputStream == null) {
         throw new RobotCoreException("XML input stream is null, ModernRoboticsHardwareFactory cannot create a hardware map");
      } else {
         HardwareMap var2 = new HardwareMap();
         RobotLog.v("Starting Modern Robotics device manager");
         ModernRoboticsDeviceManager var3 = new ModernRoboticsDeviceManager(this.context, var1);
         Iterator var4 = (new ReadXMLFileHandler(this.context)).parse(this.inputStream).iterator();

         while(var4.hasNext()) {
            ControllerConfiguration var5 = (ControllerConfiguration)var4.next();
            DeviceConfiguration.ConfigurationType var6 = var5.getType();
            switch(null.a[var6.ordinal()]) {
            case 1:
               this.a(var2, var3, var5);
               break;
            case 2:
               this.b(var2, var3, var5);
               break;
            case 3:
               this.d(var2, var3, var5);
               break;
            case 4:
               this.c(var2, var3, var5);
               break;
            default:
               RobotLog.w("Unexpected controller type while parsing XML: " + var6.toString());
            }
         }

         var2.appContext = this.context;
         return var2;
      }
   }

   public InputStream getXmlInputStream() {
      return this.inputStream;
   }

   public void setXmlInputStream(InputStream var1) {
      this.inputStream = var1;
   }
}
