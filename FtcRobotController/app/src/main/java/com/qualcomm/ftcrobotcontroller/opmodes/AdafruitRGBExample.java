package com.qualcomm.ftcrobotcontroller.opmodes;

import android.view.View;
import android.graphics.Color;
import com.qualcomm.ftccommon.DbgLog;
import android.app.Activity;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class AdafruitRGBExample extends LinearOpMode
{
    static final int LED_CHANNEL = 5;
    DeviceInterfaceModule cdim;
    ColorSensor sensorRGB;
    
    @Override
    public void runOpMode() throws InterruptedException {
        this.hardwareMap.logDevices();
        (this.cdim = this.hardwareMap.deviceInterfaceModule.get("dim")).setDigitalChannelMode(5, DigitalChannelController.Mode.OUTPUT);
        this.sensorRGB = this.hardwareMap.colorSensor.get("color");
        this.cdim.setDigitalChannelState(5, true);
        this.waitOneFullHardwareCycle();
        this.waitForStart();
        final float[] array = { 0.0f, 0.0f, 0.0f };
        final View viewById = ((Activity)this.hardwareMap.appContext).findViewById(2131427353);
        int n = false ? 1 : 0;
        while (this.opModeIsActive()) {
            boolean b;
            if (this.gamepad1.x || this.gamepad2.x) {
                b = true;
            }
            else {
                b = false;
            }
            if (b && (b ? 1 : 0) != n) {
                DbgLog.msg("MY_DEBUG - x button was pressed!");
                n = (b ? 1 : 0);
                this.cdim.setDigitalChannelState(5, true);
            }
            else if (!b && (b ? 1 : 0) != n) {
                DbgLog.msg("MY_DEBUG - x button was released!");
                n = (b ? 1 : 0);
                this.cdim.setDigitalChannelState(5, false);
            }
            Color.RGBToHSV(255 * this.sensorRGB.red() / 800, 255 * this.sensorRGB.green() / 800, 255 * this.sensorRGB.blue() / 800, array);
            this.telemetry.addData("Clear", this.sensorRGB.alpha());
            this.telemetry.addData("Red  ", this.sensorRGB.red());
            this.telemetry.addData("Green", this.sensorRGB.green());
            this.telemetry.addData("Blue ", this.sensorRGB.blue());
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
}
