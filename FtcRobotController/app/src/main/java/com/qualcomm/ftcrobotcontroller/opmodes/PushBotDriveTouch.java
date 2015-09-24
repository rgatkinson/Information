package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class PushBotDriveTouch extends LinearOpMode
{
    DcMotor leftMotor;
    DcMotor rightMotor;
    TouchSensor touchSensor;
    
    @Override
    public void runOpMode() throws InterruptedException {
        this.leftMotor = this.hardwareMap.dcMotor.get("left_drive");
        (this.rightMotor = this.hardwareMap.dcMotor.get("right_drive")).setDirection(DcMotor.Direction.REVERSE);
        this.touchSensor = this.hardwareMap.touchSensor.get("sensor_touch");
        this.waitForStart();
        while (this.opModeIsActive()) {
            if (this.touchSensor.isPressed()) {
                this.leftMotor.setPower(0.0);
                this.rightMotor.setPower(0.0);
            }
            else {
                this.leftMotor.setPower(0.5);
                this.rightMotor.setPower(0.5);
            }
            this.telemetry.addData("isPressed", String.valueOf(this.touchSensor.isPressed()));
            this.waitOneFullHardwareCycle();
        }
    }
}
