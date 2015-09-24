package com.qualcomm.ftcrobotcontroller.opmodes;

public class PushBotOdsFollowEvent extends PushBotTelemetrySensors
{
    @Override
    public void loop() {
        if (this.a_ods_white_tape_detected()) {
            this.set_drive_power(0.0, 0.2);
        }
        else {
            this.set_drive_power(0.2, 0.0);
        }
        this.update_telemetry();
    }
}
