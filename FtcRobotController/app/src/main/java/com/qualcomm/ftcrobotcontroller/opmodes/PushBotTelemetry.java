package com.qualcomm.ftcrobotcontroller.opmodes;

public class PushBotTelemetry extends PushBotHardware
{
    public void set_error_message(final String s) {
        this.set_first_message("ERROR: " + s);
    }
    
    public void set_first_message(final String s) {
        this.telemetry.addData("00", s);
    }
    
    public void update_gamepad_telemetry() {
        this.telemetry.addData("05", "GP1 Left: " + -this.gamepad1.left_stick_y);
        this.telemetry.addData("06", "GP1 Right: " + -this.gamepad1.right_stick_y);
        this.telemetry.addData("07", "GP2 Left: " + -this.gamepad2.left_stick_y);
        this.telemetry.addData("08", "GP2 X: " + this.gamepad2.x);
        this.telemetry.addData("09", "GP2 Y: " + this.gamepad2.y);
        this.telemetry.addData("10", "GP1 LT: " + this.gamepad1.left_trigger);
        this.telemetry.addData("11", "GP1 RT: " + this.gamepad1.right_trigger);
    }
    
    public void update_telemetry() {
        if (this.a_warning_generated()) {
            this.set_first_message(this.a_warning_message());
        }
        this.telemetry.addData("01", "Left Drive: " + this.a_left_drive_power() + ", " + this.a_left_encoder_count());
        this.telemetry.addData("02", "Right Drive: " + this.a_right_drive_power() + ", " + this.a_right_encoder_count());
        this.telemetry.addData("03", "Left Arm: " + this.a_left_arm_power());
        this.telemetry.addData("04", "Hand Position: " + this.a_hand_position());
    }
}
