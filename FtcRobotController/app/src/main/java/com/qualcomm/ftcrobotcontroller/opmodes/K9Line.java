package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class K9Line extends OpMode
{
    static final double HOLD_IR_SIGNAL_STRENGTH = 0.2;
    static final double LIGHT_THRESHOLD = 0.5;
    static final double MOTOR_POWER = 0.15;
    Servo arm;
    double armPosition;
    Servo claw;
    double clawPosition;
    DcMotor motorLeft;
    DcMotor motorRight;
    LightSensor reflectedLight;
    
    @Override
    public void init() {
        this.motorRight = this.hardwareMap.dcMotor.get("motor_2");
        (this.motorLeft = this.hardwareMap.dcMotor.get("motor_1")).setDirection(DcMotor.Direction.REVERSE);
        this.arm = this.hardwareMap.servo.get("servo_1");
        this.claw = this.hardwareMap.servo.get("servo_6");
        this.armPosition = 0.2;
        this.clawPosition = 0.25;
        (this.reflectedLight = this.hardwareMap.lightSensor.get("light_sensor")).enableLed(true);
    }
    
    @Override
    public void loop() {
        this.arm.setPosition(this.armPosition);
        this.claw.setPosition(this.clawPosition);
        double power;
        double power2;
        if (0.0 < 0.5) {
            power = 0.15;
            power2 = 0.0;
        }
        else {
            power = 0.0;
            power2 = 0.15;
        }
        this.motorRight.setPower(power);
        this.motorLeft.setPower(power2);
        this.telemetry.addData("Text", "*** Robot Data***");
        this.telemetry.addData("reflection", "reflection:  " + Double.toString(0.0));
        this.telemetry.addData("left tgt pwr", "left  pwr: " + Double.toString(power));
        this.telemetry.addData("right tgt pwr", "right pwr: " + Double.toString(power2));
    }
    
    @Override
    public void stop() {
    }
}
