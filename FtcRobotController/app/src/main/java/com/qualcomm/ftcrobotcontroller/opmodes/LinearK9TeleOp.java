package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class LinearK9TeleOp extends LinearOpMode
{
    Servo jaw;
    double jawPosition;
    DcMotor motorLeft;
    DcMotor motorRight;
    Servo neck;
    double neckDelta;
    double neckPosition;
    
    public LinearK9TeleOp() {
        this.neckDelta = 0.01;
    }
    
    @Override
    public void runOpMode() throws InterruptedException {
        this.motorLeft = this.hardwareMap.dcMotor.get("motor_1");
        this.motorRight = this.hardwareMap.dcMotor.get("motor_2");
        this.neck = this.hardwareMap.servo.get("servo_1");
        this.jaw = this.hardwareMap.servo.get("servo_6");
        this.motorLeft.setDirection(DcMotor.Direction.REVERSE);
        this.neckPosition = 0.5;
        this.waitForStart();
        while (this.opModeIsActive()) {
            final float n = -this.gamepad1.left_stick_y;
            final float left_stick_x = this.gamepad1.left_stick_x;
            final float n2 = n - left_stick_x;
            final float n3 = n + left_stick_x;
            final float clip = Range.clip(n2, -1.0f, 1.0f);
            final float clip2 = Range.clip(n3, -1.0f, 1.0f);
            this.motorRight.setPower(clip);
            this.motorLeft.setPower(clip2);
            if (this.gamepad1.y) {
                this.neckPosition -= this.neckDelta;
            }
            if (this.gamepad1.a) {
                this.neckPosition += this.neckDelta;
            }
            this.neckPosition = Range.clip(this.neckPosition, 0.0, 1.0);
            this.jawPosition = 1.0 - Range.scale(this.gamepad1.right_trigger, 0.0, 1.0, 0.3, 1.0);
            this.neck.setPosition(this.neckPosition);
            this.jaw.setPosition(this.jawPosition);
            this.telemetry.addData("Text", "K9TeleOp");
            this.telemetry.addData(" left motor", this.motorLeft.getPower());
            this.telemetry.addData("right motor", this.motorRight.getPower());
            this.telemetry.addData("neck", this.neck.getPosition());
            this.telemetry.addData("jaw", this.jaw.getPosition());
            this.waitOneFullHardwareCycle();
        }
    }
}
