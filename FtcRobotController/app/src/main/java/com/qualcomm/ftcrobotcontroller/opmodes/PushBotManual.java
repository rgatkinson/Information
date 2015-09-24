package com.qualcomm.ftcrobotcontroller.opmodes;

public class PushBotManual extends PushBotTelemetry
{
    @Override
    public void loop() {
        this.set_drive_power(this.scale_motor_power(-this.gamepad1.left_stick_y), this.scale_motor_power(-this.gamepad1.right_stick_y));
        this.m_left_arm_power(this.scale_motor_power(-this.gamepad2.left_stick_y));
        if (this.gamepad2.x) {
            this.m_hand_position(0.05 + this.a_hand_position());
        }
        else if (this.gamepad2.b) {
            this.m_hand_position(this.a_hand_position() - 0.05);
        }
        this.update_telemetry();
        this.update_gamepad_telemetry();
    }
}
