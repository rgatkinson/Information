package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class NxtTeleOp extends OpMode
{
    Servo claw;
    double clawDelta;
    double clawPosition;
    DcMotorController.DeviceMode devMode;
    DcMotor motorLeft;
    DcMotor motorRight;
    int numOpLoops;
    DcMotorController wheelController;
    Servo wrist;
    double wristDelta;
    double wristPosition;
    
    public NxtTeleOp() {
        this.clawDelta = 0.01;
        this.wristDelta = 0.01;
        this.numOpLoops = 1;
    }
    
    private boolean allowedToWrite() {
        return this.devMode == DcMotorController.DeviceMode.WRITE_ONLY;
    }
    
    @Override
    public void init() {
        this.motorRight = this.hardwareMap.dcMotor.get("motor_2");
        this.motorLeft = this.hardwareMap.dcMotor.get("motor_1");
        this.claw = this.hardwareMap.servo.get("servo_6");
        this.wrist = this.hardwareMap.servo.get("servo_1");
        this.wheelController = this.hardwareMap.dcMotorController.get("wheels");
    }
    
    @Override
    public void init_loop() {
        this.devMode = DcMotorController.DeviceMode.WRITE_ONLY;
        this.motorRight.setDirection(DcMotor.Direction.REVERSE);
        this.motorLeft.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        this.motorRight.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        this.wristPosition = 0.6;
        this.clawPosition = 0.5;
    }
    
    @Override
    public void loop() {
        if (this.allowedToWrite()) {
            if (this.gamepad1.dpad_left) {
                this.motorLeft.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
                this.motorRight.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
            }
            if (this.gamepad1.dpad_right) {
                this.motorLeft.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
                this.motorRight.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            }
            final float n = -this.gamepad1.left_stick_y;
            final float left_stick_x = this.gamepad1.left_stick_x;
            final float n2 = n - left_stick_x;
            final float n3 = n + left_stick_x;
            final float clip = Range.clip(n2, -1.0f, 1.0f);
            final float clip2 = Range.clip(n3, -1.0f, 1.0f);
            this.motorRight.setPower(clip);
            this.motorLeft.setPower(clip2);
            if (this.gamepad1.a) {
                this.wristPosition -= this.wristDelta;
            }
            if (this.gamepad1.y) {
                this.wristPosition += this.wristDelta;
            }
            if (this.gamepad1.x) {
                this.clawPosition -= this.clawDelta;
            }
            if (this.gamepad1.b) {
                this.clawPosition += this.clawDelta;
            }
            this.wristPosition = Range.clip(this.wristPosition, 0.0, 1.0);
            this.clawPosition = Range.clip(this.clawPosition, 0.0, 1.0);
            this.wrist.setPosition(this.wristPosition);
            this.claw.setPosition(this.clawPosition);
            if (!this.gamepad2.atRest()) {
                float right_trigger = this.gamepad2.right_trigger;
                if (this.gamepad2.left_trigger != 0.0) {
                    right_trigger = -this.gamepad2.left_trigger;
                }
                float n4 = right_trigger;
                float n5 = right_trigger;
                if (this.gamepad2.left_stick_x < 0.0f) {
                    n5 *= 1.0f + this.gamepad2.left_stick_x;
                }
                if (this.gamepad2.left_stick_x > 0.0f) {
                    n4 *= 1.0f - this.gamepad2.left_stick_x;
                }
                this.motorRight.setPower(n4);
                this.motorLeft.setPower(n5);
            }
        }
        if (this.numOpLoops % 17 == 0) {
            this.wheelController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);
        }
        if (this.wheelController.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.READ_ONLY) {
            this.telemetry.addData("Text", "free flow text");
            this.telemetry.addData("left motor", this.motorLeft.getPower());
            this.telemetry.addData("right motor", this.motorRight.getPower());
            this.telemetry.addData("RunMode: ", this.motorLeft.getChannelMode().toString());
            this.wheelController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
            this.numOpLoops = 0;
        }
        this.devMode = this.wheelController.getMotorControllerDeviceMode();
        ++this.numOpLoops;
    }
}
