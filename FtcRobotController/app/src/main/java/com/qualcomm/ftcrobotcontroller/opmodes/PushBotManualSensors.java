package com.qualcomm.ftcrobotcontroller.opmodes;

public class PushBotManualSensors extends PushBotTelemetrySensors
{
    private boolean v_raise_arm_automatically;
    
    public PushBotManualSensors() {
        this.v_raise_arm_automatically = false;
    }
    
    @Override
    public void loop() {
        this.set_drive_power(this.scale_motor_power(-this.gamepad1.left_stick_y), this.scale_motor_power(-this.gamepad1.right_stick_y));
        if (this.gamepad2.y && !this.v_raise_arm_automatically) {
            this.v_raise_arm_automatically = true;
        }
        final float n = -this.gamepad2.left_stick_y;
        float scale_motor_power;
        if (this.v_raise_arm_automatically) {
            scale_motor_power = 1.0f;
            if (this.is_touch_sensor_pressed() || Math.abs(n) > 0.8) {
                scale_motor_power = 0.0f;
                this.v_raise_arm_automatically = false;
            }
        }
        else {
            this.v_raise_arm_automatically = false;
            scale_motor_power = this.scale_motor_power(n);
        }
        this.m_left_arm_power(scale_motor_power);
        if (this.gamepad2.x) {
            this.m_hand_position(0.05 + this.a_hand_position());
        }
        else if (this.gamepad2.b) {
            this.m_hand_position(this.a_hand_position() - 0.05);
        }
        this.update_telemetry();
        this.telemetry.addData("18", "Raise Arm: " + this.v_raise_arm_automatically);
        this.telemetry.addData("19", "Left arm command: " + scale_motor_power);
    }
}
