package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.hardware.AnalogOutput;
import com.qualcomm.robotcore.hardware.AnalogOutputController;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
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
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.TouchSensorMultiplexer;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.Map;

public abstract class DeviceManager {
   public abstract ColorSensor createAdafruitColorSensor(DeviceInterfaceModule var1, int var2);

   public abstract AnalogInput createAnalogInputDevice(AnalogInputController var1, int var2);

   public abstract AnalogOutput createAnalogOutputDevice(AnalogOutputController var1, int var2);

   public DcMotor createDcMotor(DcMotorController var1, int var2) {
      return new DcMotor(var1, var2, DcMotor.Direction.FORWARD);
   }

   public abstract DeviceInterfaceModule createDeviceInterfaceModule(SerialNumber var1) throws RobotCoreException, InterruptedException;

   public abstract DigitalChannel createDigitalChannelDevice(DigitalChannelController var1, int var2);

   public abstract I2cDevice createI2cDevice(I2cController var1, int var2);

   public abstract IrSeekerSensor createIrSeekerSensorV3(DeviceInterfaceModule var1, int var2);

   public abstract LED createLED(DigitalChannelController var1, int var2);

   public abstract ColorSensor createModernRoboticsColorSensor(DeviceInterfaceModule var1, int var2);

   public abstract AccelerationSensor createNxtAccelerationSensor(LegacyModule var1, int var2);

   public abstract ColorSensor createNxtColorSensor(LegacyModule var1, int var2);

   public abstract CompassSensor createNxtCompassSensor(LegacyModule var1, int var2);

   public abstract DcMotorController createNxtDcMotorController(LegacyModule var1, int var2);

   public abstract GyroSensor createNxtGyroSensor(LegacyModule var1, int var2);

   public abstract IrSeekerSensor createNxtIrSeekerSensor(LegacyModule var1, int var2);

   public abstract LightSensor createNxtLightSensor(LegacyModule var1, int var2);

   public abstract ServoController createNxtServoController(LegacyModule var1, int var2);

   public abstract TouchSensor createNxtTouchSensor(LegacyModule var1, int var2);

   public abstract TouchSensorMultiplexer createNxtTouchSensorMultiplexer(LegacyModule var1, int var2);

   public abstract UltrasonicSensor createNxtUltrasonicSensor(LegacyModule var1, int var2);

   public abstract OpticalDistanceSensor createOpticalDistanceSensor(DeviceInterfaceModule var1, int var2);

   public abstract PWMOutput createPwmOutputDevice(DeviceInterfaceModule var1, int var2);

   public Servo createServo(ServoController var1, int var2) {
      return new Servo(var1, var2, Servo.Direction.FORWARD);
   }

   public abstract TouchSensor createTouchSensor(DeviceInterfaceModule var1, int var2);

   public abstract DcMotorController createUsbDcMotorController(SerialNumber var1) throws RobotCoreException, InterruptedException;

   public abstract LegacyModule createUsbLegacyModule(SerialNumber var1) throws RobotCoreException, InterruptedException;

   public abstract ServoController createUsbServoController(SerialNumber var1) throws RobotCoreException, InterruptedException;

   public abstract Map<SerialNumber, DeviceManager.DeviceType> scanForUsbDevices() throws RobotCoreException;

   public static enum DeviceType {
      FTDI_USB_UNKNOWN_DEVICE,
      MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER,
      MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE,
      MODERN_ROBOTICS_USB_LEGACY_MODULE,
      MODERN_ROBOTICS_USB_SENSOR_MUX,
      MODERN_ROBOTICS_USB_SERVO_CONTROLLER,
      MODERN_ROBOTICS_USB_UNKNOWN_DEVICE;

      static {
         DeviceManager.DeviceType[] var0 = new DeviceManager.DeviceType[]{FTDI_USB_UNKNOWN_DEVICE, MODERN_ROBOTICS_USB_UNKNOWN_DEVICE, MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER, MODERN_ROBOTICS_USB_SERVO_CONTROLLER, MODERN_ROBOTICS_USB_LEGACY_MODULE, MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE, MODERN_ROBOTICS_USB_SENSOR_MUX};
      }
   }
}
