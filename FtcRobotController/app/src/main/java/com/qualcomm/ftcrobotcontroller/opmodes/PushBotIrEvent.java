package com.qualcomm.ftcrobotcontroller.opmodes;

public class PushBotIrEvent extends PushBotTelemetrySensors
{
    @Override
    public void loop() {
        final int drive_to_ir_beacon = this.drive_to_ir_beacon();
        if (drive_to_ir_beacon == 0) {
            this.set_first_message("Driving to IR beacon.");
        }
        else if (drive_to_ir_beacon == 1) {
            this.set_error_message("IR beacon is close!");
        }
        else if (drive_to_ir_beacon == -2) {
            this.set_error_message("IR beacon not detected!");
        }
        else if (drive_to_ir_beacon == -1) {
            this.set_error_message("IR beacon return value is invalid!");
        }
        this.update_telemetry();
    }
}
