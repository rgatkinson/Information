package com.qualcomm.ftcrobotcontroller.opmodes;

public class PushBotAutoSensors extends PushBotTelemetrySensors
{
    private int v_arm_state;
    private int v_state;
    
    public PushBotAutoSensors() {
        this.v_state = 0;
        this.v_arm_state = 0;
    }
    
    @Override
    public void loop() {
        switch (this.v_state) {
            case 0: {
                if (this.have_drive_encoders_reset()) {
                    this.drive_using_encoders(-1.0, 1.0, -2880.0, 2880.0);
                    ++this.v_state;
                    break;
                }
                break;
            }
            case 1: {
                if (this.drive_using_encoders(1.0, 1.0, 2880.0, 2880.0)) {
                    ++this.v_state;
                    break;
                }
                break;
            }
            case 2: {
                if (this.have_drive_encoders_reset()) {
                    this.drive_using_encoders(-1.0, 1.0, -2880.0, 2880.0);
                    this.v_arm_state = 1;
                    ++this.v_state;
                    break;
                }
                break;
            }
            case 3: {
                if (this.drive_using_encoders(-1.0, 1.0, -2880.0, 2880.0)) {
                    ++this.v_state;
                    break;
                }
                break;
            }
            case 4: {
                if (this.have_drive_encoders_reset()) {
                    this.run_without_drive_encoders();
                    ++this.v_state;
                    break;
                }
                break;
            }
            case 5: {
                if (this.a_ods_white_tape_detected()) {
                    this.set_drive_power(0.0, 0.0);
                    this.drive_to_ir_beacon();
                    ++this.v_state;
                    break;
                }
                this.set_drive_power(1.0, 1.0);
                break;
            }
            case 6: {
                final int drive_to_ir_beacon = this.drive_to_ir_beacon();
                if (drive_to_ir_beacon == 1) {
                    this.open_hand();
                    ++this.v_state;
                    break;
                }
                if (drive_to_ir_beacon == -2) {
                    this.set_error_message("IR beacon not detected!");
                    break;
                }
                break;
            }
        }
        this.update_arm_state();
        this.update_telemetry();
        this.telemetry.addData("11", "Drive State: " + this.v_state);
        this.telemetry.addData("12", "Arm State: " + this.v_arm_state);
    }
    
    @Override
    public void start() {
        super.start();
        this.reset_drive_encoders();
    }
    
    public void update_arm_state() {
        switch (this.v_arm_state) {
            case 1: {
                if (this.move_arm_upward_until_touch()) {
                    ++this.v_arm_state;
                    return;
                }
                break;
            }
        }
    }
}
