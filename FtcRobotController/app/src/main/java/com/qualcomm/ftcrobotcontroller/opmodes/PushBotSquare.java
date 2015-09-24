package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class PushBotSquare extends LinearOpMode
{
    DcMotor leftMotor;
    DcMotor rightMotor;
    
    @Override
    public void runOpMode() throws InterruptedException {
        this.leftMotor = this.hardwareMap.dcMotor.get("left_drive");
        (this.rightMotor = this.hardwareMap.dcMotor.get("right_drive")).setDirection(DcMotor.Direction.REVERSE);
        this.leftMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        this.rightMotor.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        this.waitForStart();
        for (int i = 0; i < 4; ++i) {
            this.leftMotor.setPower(1.0);
            this.rightMotor.setPower(1.0);
            this.sleep(1000L);
            this.leftMotor.setPower(0.5);
            this.rightMotor.setPower(-0.5);
            this.sleep(500L);
        }
        this.leftMotor.setPowerFloat();
        this.rightMotor.setPowerFloat();
    }
}
