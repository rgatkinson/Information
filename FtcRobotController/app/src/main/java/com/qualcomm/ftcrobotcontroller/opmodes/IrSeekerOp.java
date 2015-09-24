package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class IrSeekerOp extends OpMode
{
    static final double HOLD_IR_SIGNAL_STRENGTH = 0.2;
    static final double MOTOR_POWER = 0.15;
    IrSeekerSensor irSeeker;
    DcMotor motorLeft;
    DcMotor motorRight;
    
    @Override
    public void init() {
        this.irSeeker = this.hardwareMap.irSeekerSensor.get("ir_seeker");
        this.motorRight = this.hardwareMap.dcMotor.get("motor_2");
        this.motorLeft = this.hardwareMap.dcMotor.get("motor_1");
    }
    
    @Override
    public void loop() {
        double angle = 0.0;
        double strength = 0.0;
        if (this.irSeeker.signalDetected()) {
            angle = this.irSeeker.getAngle();
            strength = this.irSeeker.getStrength();
            if (angle < -20.0) {
                this.motorRight.setPower(0.15);
                this.motorLeft.setPower(-0.15);
            }
            else if (angle > 20.0) {
                this.motorRight.setPower(-0.15);
                this.motorLeft.setPower(0.15);
            }
            else if (strength < 0.2) {
                this.motorRight.setPower(0.15);
                this.motorLeft.setPower(0.15);
            }
            else {
                this.motorRight.setPower(0.0);
                this.motorLeft.setPower(0.0);
            }
        }
        else {
            this.motorRight.setPower(0.0);
            this.motorLeft.setPower(0.0);
        }
        this.telemetry.addData("angle", angle);
        this.telemetry.addData("strength", strength);
        DbgLog.msg(this.irSeeker.toString());
    }
    
    @Override
    public void start() {
        this.motorLeft.setDirection(DcMotor.Direction.REVERSE);
    }
}
