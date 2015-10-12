package com.qualcomm.robotcore.hardware;

import android.content.Context;
import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogOutput;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
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
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class HardwareMap {
   public HardwareMap.DeviceMapping<AccelerationSensor> accelerationSensor = new HardwareMap.DeviceMapping();
   public HardwareMap.DeviceMapping<AnalogInput> analogInput = new HardwareMap.DeviceMapping();
   public HardwareMap.DeviceMapping<AnalogOutput> analogOutput = new HardwareMap.DeviceMapping();
   public Context appContext = null;
   public HardwareMap.DeviceMapping<ColorSensor> colorSensor = new HardwareMap.DeviceMapping();
   public HardwareMap.DeviceMapping<CompassSensor> compassSensor = new HardwareMap.DeviceMapping();
   public HardwareMap.DeviceMapping<DcMotor> dcMotor = new HardwareMap.DeviceMapping();
   public HardwareMap.DeviceMapping<DcMotorController> dcMotorController = new HardwareMap.DeviceMapping();
   public HardwareMap.DeviceMapping<DeviceInterfaceModule> deviceInterfaceModule = new HardwareMap.DeviceMapping();
   public HardwareMap.DeviceMapping<DigitalChannel> digitalChannel = new HardwareMap.DeviceMapping();
   public HardwareMap.DeviceMapping<GyroSensor> gyroSensor = new HardwareMap.DeviceMapping();
   public HardwareMap.DeviceMapping<I2cDevice> i2cDevice = new HardwareMap.DeviceMapping();
   public HardwareMap.DeviceMapping<IrSeekerSensor> irSeekerSensor = new HardwareMap.DeviceMapping();
   public HardwareMap.DeviceMapping<LED> led = new HardwareMap.DeviceMapping();
   public HardwareMap.DeviceMapping<LegacyModule> legacyModule = new HardwareMap.DeviceMapping();
   public HardwareMap.DeviceMapping<LightSensor> lightSensor = new HardwareMap.DeviceMapping();
   public HardwareMap.DeviceMapping<OpticalDistanceSensor> opticalDistanceSensor = new HardwareMap.DeviceMapping();
   public HardwareMap.DeviceMapping<PWMOutput> pwmOutput = new HardwareMap.DeviceMapping();
   public HardwareMap.DeviceMapping<Servo> servo = new HardwareMap.DeviceMapping();
   public HardwareMap.DeviceMapping<ServoController> servoController = new HardwareMap.DeviceMapping();
   public HardwareMap.DeviceMapping<TouchSensor> touchSensor = new HardwareMap.DeviceMapping();
   public HardwareMap.DeviceMapping<TouchSensorMultiplexer> touchSensorMultiplexer = new HardwareMap.DeviceMapping();
   public HardwareMap.DeviceMapping<UltrasonicSensor> ultrasonicSensor = new HardwareMap.DeviceMapping();
   public HardwareMap.DeviceMapping<VoltageSensor> voltageSensor = new HardwareMap.DeviceMapping();

   public void logDevices() {
      RobotLog.i("========= Device Information ===================================================");
      RobotLog.i(String.format("%-45s %-30s %s", new Object[]{"Type", "Name", "Connection"}));
      this.dcMotorController.logDevices();
      this.dcMotor.logDevices();
      this.servoController.logDevices();
      this.servo.logDevices();
      this.legacyModule.logDevices();
      this.touchSensorMultiplexer.logDevices();
      this.deviceInterfaceModule.logDevices();
      this.analogInput.logDevices();
      this.digitalChannel.logDevices();
      this.opticalDistanceSensor.logDevices();
      this.touchSensor.logDevices();
      this.pwmOutput.logDevices();
      this.i2cDevice.logDevices();
      this.analogOutput.logDevices();
      this.colorSensor.logDevices();
      this.led.logDevices();
      this.accelerationSensor.logDevices();
      this.compassSensor.logDevices();
      this.gyroSensor.logDevices();
      this.irSeekerSensor.logDevices();
      this.lightSensor.logDevices();
      this.ultrasonicSensor.logDevices();
      this.voltageSensor.logDevices();
   }

   public static class DeviceMapping<DEVICE_TYPE> implements Iterable<DEVICE_TYPE> {
      private Map<String, DEVICE_TYPE> a = new HashMap();

      public Set<Entry<String, DEVICE_TYPE>> entrySet() {
         return this.a.entrySet();
      }

      public DEVICE_TYPE get(String var1) {
         Object var2 = this.a.get(var1);
         if(var2 == null) {
            throw new IllegalArgumentException(String.format("Unable to find a hardware device with the name \"%s\"", new Object[]{var1}));
         } else {
            return var2;
         }
      }

      public Iterator<DEVICE_TYPE> iterator() {
         return this.a.values().iterator();
      }

      public void logDevices() {
         if(!this.a.isEmpty()) {
            Iterator var1 = this.a.entrySet().iterator();

            while(var1.hasNext()) {
               Entry var2 = (Entry)var1.next();
               if(var2.getValue() instanceof HardwareDevice) {
                  HardwareDevice var3 = (HardwareDevice)var2.getValue();
                  String var4 = var3.getConnectionInfo();
                  String var5 = (String)var2.getKey();
                  RobotLog.i(String.format("%-45s %-30s %s", new Object[]{var3.getDeviceName(), var5, var4}));
               }
            }
         }

      }

      public void put(String var1, DEVICE_TYPE var2) {
         this.a.put(var1, var2);
      }

      public int size() {
         return this.a.size();
      }
   }
}
