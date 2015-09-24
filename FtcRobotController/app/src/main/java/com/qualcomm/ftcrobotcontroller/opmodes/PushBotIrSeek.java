package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class PushBotIrSeek extends LinearOpMode
{
    static final double kBaseSpeed = 0.15;
    static final double kMaximumStrength = 0.6;
    static final double kMinimumStrength = 0.08;
    IrSeekerSensor irSeeker;
    DcMotor leftMotor;
    DcMotor rightMotor;
    
    @Override
    public void runOpMode() throws InterruptedException {
        this.irSeeker = this.hardwareMap.irSeekerSensor.get("sensor_ir");
        this.leftMotor = this.hardwareMap.dcMotor.get("left_drive");
        (this.rightMotor = this.hardwareMap.dcMotor.get("right_drive")).setDirection(DcMotor.Direction.REVERSE);
        this.waitForStart();
        while (this.opModeIsActive()) {
            final double n = this.irSeeker.getAngle() / 30.0;
            final double strength = this.irSeeker.getStrength();
            if (strength > 0.08 && strength < 0.6) {
                final double clip = Range.clip(0.15 + n / 8.0, -1.0, 1.0);
                final double clip2 = Range.clip(0.15 - n / 8.0, -1.0, 1.0);
                this.leftMotor.setPower(clip);
                this.rightMotor.setPower(clip2);
            }
            else {
                this.leftMotor.setPower(0.0);
                this.rightMotor.setPower(0.0);
            }
            this.telemetry.addData("Seeker", this.irSeeker.toString());
            this.telemetry.addData("Speed", " Left=" + this.leftMotor.getPower() + " Right=" + this.rightMotor.getPower());
            this.waitOneFullHardwareCycle();
        }
    }
}
