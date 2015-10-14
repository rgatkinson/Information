package com.qualcomm.robotcore.hardware;

import android.content.Context;

import com.qualcomm.robotcore.util.RobotLog;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HardwareMap {
    public DeviceMapping<AccelerationSensor> accelerationSensor = new DeviceMapping();
    public DeviceMapping<AnalogInput> analogInput = new DeviceMapping();
    public DeviceMapping<AnalogOutput> analogOutput = new DeviceMapping();
    public Context appContext = null;
    public DeviceMapping<ColorSensor> colorSensor = new DeviceMapping();
    public DeviceMapping<CompassSensor> compassSensor = new DeviceMapping();
    public DeviceMapping<DcMotor> dcMotor = new DeviceMapping();
    public DeviceMapping<DcMotorController> dcMotorController = new DeviceMapping();
    public DeviceMapping<DeviceInterfaceModule> deviceInterfaceModule = new DeviceMapping();
    public DeviceMapping<DigitalChannel> digitalChannel = new DeviceMapping();
    public DeviceMapping<GyroSensor> gyroSensor = new DeviceMapping();
    public DeviceMapping<I2cDevice> i2cDevice = new DeviceMapping();
    public DeviceMapping<IrSeekerSensor> irSeekerSensor = new DeviceMapping();
    public DeviceMapping<LED> led = new DeviceMapping();
    public DeviceMapping<LegacyModule> legacyModule = new DeviceMapping();
    public DeviceMapping<LightSensor> lightSensor = new DeviceMapping();
    public DeviceMapping<OpticalDistanceSensor> opticalDistanceSensor = new DeviceMapping();
    public DeviceMapping<PWMOutput> pwmOutput = new DeviceMapping();
    public DeviceMapping<Servo> servo = new DeviceMapping();
    public DeviceMapping<ServoController> servoController = new DeviceMapping();
    public DeviceMapping<TouchSensor> touchSensor = new DeviceMapping();
    public DeviceMapping<TouchSensorMultiplexer> touchSensorMultiplexer = new DeviceMapping();
    public DeviceMapping<UltrasonicSensor> ultrasonicSensor = new DeviceMapping();
    public DeviceMapping<VoltageSensor> voltageSensor = new DeviceMapping();

    public void logDevices() {
        RobotLog.i("========= Device Information ===================================================");
        RobotLog.i(String.format("%-45s %-30s %s", "Type", "Name", "Connection"));
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
            DEVICE_TYPE var2 = this.a.get(var1);
            if (var2 == null) {
                throw new IllegalArgumentException(String.format("Unable to find a hardware device with the name \"%s\"", new Object[]{var1}));
            } else {
                return var2;
            }
        }

        public Iterator<DEVICE_TYPE> iterator() {
            return this.a.values().iterator();
        }

        public void logDevices() {
            if (!this.a.isEmpty()) {
                Iterator var1 = this.a.entrySet().iterator();

                while (var1.hasNext()) {
                    Entry var2 = (Entry) var1.next();
                    if (var2.getValue() instanceof HardwareDevice) {
                        HardwareDevice var3 = (HardwareDevice) var2.getValue();
                        String var4 = var3.getConnectionInfo();
                        String var5 = (String) var2.getKey();
                        RobotLog.i(String.format("%-45s %-30s %s", var3.getDeviceName(), var5, var4));
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
