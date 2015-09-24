package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class LinearIrExample extends LinearOpMode
{
    static final double HOLD_IR_SIGNAL_STRENGTH = 0.2;
    static final double MOTOR_POWER = 0.15;
    IrSeekerSensor irSeeker;
    DcMotor motorLeft;
    DcMotor motorRight;
    
    @Override
    public void runOpMode() throws InterruptedException {
        this.irSeeker = this.hardwareMap.irSeekerSensor.get("ir_seeker");
        this.motorLeft = this.hardwareMap.dcMotor.get("motor_1");
        this.motorRight = this.hardwareMap.dcMotor.get("motor_2");
        this.motorLeft.setDirection(DcMotor.Direction.REVERSE);
        this.waitForStart();
        while (!this.irSeeker.signalDetected()) {
            this.sleep(1000L);
        }
        if (this.irSeeker.getAngle() < 0.0) {
            this.motorRight.setPower(0.15);
            this.motorLeft.setPower(-0.15);
        }
        else if (this.irSeeker.getAngle() > 0.0) {
            this.motorRight.setPower(-0.15);
            this.motorLeft.setPower(0.15);
        }
        while (this.irSeeker.getAngle() != 0.0) {
            this.waitOneFullHardwareCycle();
        }
        this.motorRight.setPower(0.15);
        this.motorLeft.setPower(0.15);
        while (this.irSeeker.getStrength() < 0.2) {
            this.waitOneFullHardwareCycle();
        }
        this.motorRight.setPower(0.0);
        this.motorLeft.setPower(0.0);
    }
}
