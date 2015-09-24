package com.qualcomm.ftcrobotcontroller.opmodes;

public class PushBotAuto extends PushBotTelemetry
{
    private int v_state;
    
    public PushBotAuto() {
        this.v_state = 0;
    }
    
    @Override
    public void loop() {
        switch (this.v_state) {
            case 0: {
                this.reset_drive_encoders();
                ++this.v_state;
                break;
            }
            case 1: {
                this.run_using_encoders();
                this.set_drive_power(1.0, 1.0);
                if (this.have_drive_encoders_reached(2880.0, 2880.0)) {
                    this.reset_drive_encoders();
                    this.set_drive_power(0.0, 0.0);
                    ++this.v_state;
                    break;
                }
                break;
            }
            case 2: {
                if (this.have_drive_encoders_reset()) {
                    ++this.v_state;
                    break;
                }
                break;
            }
            case 3: {
                this.run_using_encoders();
                this.set_drive_power(-1.0, 1.0);
                if (this.have_drive_encoders_reached(2880.0, 2880.0)) {
                    this.reset_drive_encoders();
                    this.set_drive_power(0.0, 0.0);
                    ++this.v_state;
                    break;
                }
                break;
            }
            case 4: {
                if (this.have_drive_encoders_reset()) {
                    ++this.v_state;
                    break;
                }
                break;
            }
            case 5: {
                this.run_using_encoders();
                this.set_drive_power(1.0, -1.0);
                if (this.have_drive_encoders_reached(2880.0, 2880.0)) {
                    this.reset_drive_encoders();
                    this.set_drive_power(0.0, 0.0);
                    ++this.v_state;
                    break;
                }
                break;
            }
            case 6: {
                if (this.have_drive_encoders_reset()) {
                    ++this.v_state;
                    break;
                }
                break;
            }
        }
        this.update_telemetry();
        this.telemetry.addData("18", "State: " + this.v_state);
    }
    
    @Override
    public void start() {
        super.start();
        this.reset_drive_encoders();
    }
}
