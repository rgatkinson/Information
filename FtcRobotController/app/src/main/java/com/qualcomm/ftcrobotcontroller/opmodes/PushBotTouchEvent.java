package com.qualcomm.ftcrobotcontroller.opmodes;

public class PushBotTouchEvent extends PushBotTelemetrySensors
{
    @Override
    public void loop() {
        if (this.is_touch_sensor_pressed()) {
            this.set_drive_power(0.0, 0.0);
        }
        else {
            this.set_drive_power(1.0, 1.0);
        }
        this.update_telemetry();
    }
}
