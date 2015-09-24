package com.qualcomm.ftcrobotcontroller.opmodes;

import android.view.View;
import android.graphics.Color;
import android.app.Activity;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class ColorSensorDriver extends LinearOpMode
{
    DeviceInterfaceModule cdim;
    ColorSensor colorSensor;
    public ColorSensorDevice device;
    LED led;
    TouchSensor t;
    
    public ColorSensorDriver() {
        this.device = ColorSensorDevice.MODERN_ROBOTICS_I2C;
    }
    
    private void enableLed(final boolean b) {
        switch (this.device) {
            default: {}
            case HITECHNIC_NXT: {
                this.colorSensor.enableLed(b);
            }
            case ADAFRUIT: {
                this.led.enable(b);
            }
            case MODERN_ROBOTICS_I2C: {
                this.colorSensor.enableLed(b);
            }
        }
    }
    
    @Override
    public void runOpMode() throws InterruptedException {
        this.hardwareMap.logDevices();
        this.cdim = this.hardwareMap.deviceInterfaceModule.get("dim");
        switch (this.device) {
            case HITECHNIC_NXT: {
                this.colorSensor = this.hardwareMap.colorSensor.get("nxt");
                break;
            }
            case ADAFRUIT: {
                this.colorSensor = this.hardwareMap.colorSensor.get("lady");
                break;
            }
            case MODERN_ROBOTICS_I2C: {
                this.colorSensor = this.hardwareMap.colorSensor.get("mr");
                break;
            }
        }
        this.led = this.hardwareMap.led.get("led");
        this.t = this.hardwareMap.touchSensor.get("t");
        this.waitForStart();
        final float[] array = { 0.0f, 0.0f, 0.0f };
        final View viewById = ((Activity)this.hardwareMap.appContext).findViewById(2131427353);
        while (this.opModeIsActive()) {
            this.enableLed(this.t.isPressed());
            switch (this.device) {
                case HITECHNIC_NXT: {
                    Color.RGBToHSV(this.colorSensor.red(), this.colorSensor.green(), this.colorSensor.blue(), array);
                    break;
                }
                case ADAFRUIT: {
                    Color.RGBToHSV(255 * this.colorSensor.red() / 800, 255 * this.colorSensor.green() / 800, 255 * this.colorSensor.blue() / 800, array);
                    break;
                }
                case MODERN_ROBOTICS_I2C: {
                    Color.RGBToHSV(8 * this.colorSensor.red(), 8 * this.colorSensor.green(), 8 * this.colorSensor.blue(), array);
                    break;
                }
            }
            this.telemetry.addData("Clear", this.colorSensor.alpha());
            this.telemetry.addData("Red  ", this.colorSensor.red());
            this.telemetry.addData("Green", this.colorSensor.green());
            this.telemetry.addData("Blue ", this.colorSensor.blue());
            this.telemetry.addData("Hue", array[0]);
            viewById.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    viewById.setBackgroundColor(Color.HSVToColor(255, array));
                }
            });
            this.waitOneFullHardwareCycle();
        }
    }
    
    public enum ColorSensorDevice
    {
        ADAFRUIT, 
        HITECHNIC_NXT, 
        MODERN_ROBOTICS_I2C;
    }
}
