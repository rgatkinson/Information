package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class K9TankDrive extends OpMode
{
    static final double ARM_MAX_RANGE = 0.9;
    static final double ARM_MIN_RANGE = 0.2;
    static final double CLAW_MAX_RANGE = 0.7;
    static final double CLAW_MIN_RANGE = 0.2;
    Servo arm;
    double armDelta;
    double armPosition;
    Servo claw;
    double clawDelta;
    double clawPosition;
    DcMotor motorLeft;
    DcMotor motorRight;
    
    public K9TankDrive() {
        this.armDelta = 0.1;
        this.clawDelta = 0.1;
    }
    
    @Override
    public void init() {
        this.motorRight = this.hardwareMap.dcMotor.get("motor_2");
        (this.motorLeft = this.hardwareMap.dcMotor.get("motor_1")).setDirection(DcMotor.Direction.REVERSE);
        this.arm = this.hardwareMap.servo.get("servo_1");
        this.claw = this.hardwareMap.servo.get("servo_6");
        this.armPosition = 0.2;
        this.clawPosition = 0.2;
    }
    
    @Override
    public void loop() {
        final float n = -this.gamepad1.left_stick_y;
        final float clip = Range.clip(-this.gamepad1.right_stick_y, -1.0f, 1.0f);
        final float clip2 = Range.clip(n, -1.0f, 1.0f);
        final float n2 = (float)this.scaleInput(clip);
        final float n3 = (float)this.scaleInput(clip2);
        this.motorRight.setPower(n2);
        this.motorLeft.setPower(n3);
        if (this.gamepad1.a) {
            this.armPosition += this.armDelta;
        }
        if (this.gamepad1.y) {
            this.armPosition -= this.armDelta;
        }
        if (this.gamepad1.left_bumper) {
            this.clawPosition += this.clawDelta;
        }
        if (this.gamepad1.left_trigger > 0.25) {
            this.clawPosition -= this.clawDelta;
        }
        if (this.gamepad1.b) {
            this.clawPosition -= this.clawDelta;
        }
        if (this.gamepad1.x) {
            this.clawPosition += this.clawDelta;
        }
        if (this.gamepad1.b) {
            this.clawPosition -= this.clawDelta;
        }
        this.armPosition = Range.clip(this.armPosition, 0.2, 0.9);
        this.clawPosition = Range.clip(this.clawPosition, 0.2, 0.7);
        this.arm.setPosition(this.armPosition);
        this.claw.setPosition(this.clawPosition);
        this.telemetry.addData("Text", "*** Robot Data***");
        this.telemetry.addData("arm", "arm:  " + String.format("%.2f", this.armPosition));
        this.telemetry.addData("claw", "claw:  " + String.format("%.2f", this.clawPosition));
        this.telemetry.addData("left tgt pwr", "left  pwr: " + String.format("%.2f", n3));
        this.telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", n2));
    }
    
    double scaleInput(final double n) {
        final double[] array = { 0.0, 0.05, 0.09, 0.1, 0.12, 0.15, 0.18, 0.24, 0.3, 0.36, 0.43, 0.5, 0.6, 0.72, 0.85, 1.0, 1.0 };
        int n2 = (int)(16.0 * n);
        if (n2 < 0) {
            n2 = -n2;
        }
        if (n2 > 16) {
            n2 = 16;
        }
        if (n < 0.0) {
            return -array[n2];
        }
        return array[n2];
    }
    
    @Override
    public void stop() {
    }
}
