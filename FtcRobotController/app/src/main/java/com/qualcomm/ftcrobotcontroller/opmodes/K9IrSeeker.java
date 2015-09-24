package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class K9IrSeeker extends OpMode
{
    static final double HOLD_IR_SIGNAL_STRENGTH = 0.5;
    static final double MOTOR_POWER = 0.15;
    Servo arm;
    double armPosition;
    Servo claw;
    double clawPosition;
    IrSeekerSensor irSeeker;
    DcMotor motorLeft;
    DcMotor motorRight;
    
    @Override
    public void init() {
        this.motorRight = this.hardwareMap.dcMotor.get("motor_2");
        (this.motorLeft = this.hardwareMap.dcMotor.get("motor_1")).setDirection(DcMotor.Direction.REVERSE);
        this.arm = this.hardwareMap.servo.get("servo_1");
        this.claw = this.hardwareMap.servo.get("servo_6");
        this.armPosition = 0.1;
        this.clawPosition = 0.25;
        this.irSeeker = this.hardwareMap.irSeekerSensor.get("ir_seeker");
    }
    
    @Override
    public void loop() {
        double angle = 0.0;
        double strength = 0.0;
        this.arm.setPosition(this.armPosition);
        this.claw.setPosition(this.clawPosition);
        double power;
        double power2;
        if (this.irSeeker.signalDetected()) {
            angle = this.irSeeker.getAngle();
            strength = this.irSeeker.getStrength();
            if (angle < -60.0) {
                power = -0.15;
                power2 = 0.15;
            }
            else if (angle < -5.0) {
                power = 0.09999999999999999;
                power2 = 0.15;
            }
            else if (angle > 5.0 && angle < 60.0) {
                power = 0.15;
                power2 = 0.09999999999999999;
            }
            else if (angle > 60.0) {
                power = 0.15;
                power2 = -0.15;
            }
            else if (strength < 0.5) {
                power = 0.15;
                power2 = 0.15;
            }
            else {
                power = 0.0;
                power2 = 0.0;
            }
        }
        else {
            power = 0.0;
            power2 = 0.0;
        }
        this.motorRight.setPower(power2);
        this.motorLeft.setPower(power);
        this.telemetry.addData("Text", "*** Robot Data***");
        this.telemetry.addData("angle", "angle:  " + Double.toString(angle));
        this.telemetry.addData("strength", "sig strength: " + Double.toString(strength));
        this.telemetry.addData("left tgt pwr", "left  pwr: " + Double.toString(power));
        this.telemetry.addData("right tgt pwr", "right pwr: " + Double.toString(power2));
    }
    
    @Override
    public void stop() {
    }
}
