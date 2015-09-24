package com.qualcomm.ftcrobotcontroller.opmodes;

public class PushBotManual1 extends PushBotTelemetry
{
    @Override
    public void loop() {
        this.set_drive_power(this.scale_motor_power(-this.gamepad1.left_stick_y), this.scale_motor_power(-this.gamepad1.right_stick_y));
        final float n = this.scale_motor_power(this.gamepad1.right_trigger) - this.scale_motor_power(this.gamepad1.left_trigger);
        this.m_left_arm_power(n);
        if (this.gamepad1.x) {
            this.m_hand_position(0.05 + this.a_hand_position());
        }
        else if (this.gamepad1.b) {
            this.m_hand_position(this.a_hand_position() - 0.05);
        }
        this.update_telemetry();
        this.update_gamepad_telemetry();
        this.telemetry.addData("12", "Left Arm: " + n);
    }
}
