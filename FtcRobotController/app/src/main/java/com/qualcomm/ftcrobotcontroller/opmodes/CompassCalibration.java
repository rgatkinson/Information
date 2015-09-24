package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class CompassCalibration extends OpMode
{
    static final double HOLD_POSITION = 3.0;
    static final double MOTOR_POWER = 0.2;
    CompassSensor compass;
    private boolean keepTurning;
    private boolean monitorCalibrationSuccess;
    DcMotor motorLeft;
    DcMotor motorRight;
    double pauseTime;
    private boolean returnToMeasurementMode;
    private double turnTime;
    
    public CompassCalibration() {
        this.turnTime = 20.0;
        this.keepTurning = true;
        this.returnToMeasurementMode = false;
        this.monitorCalibrationSuccess = false;
        this.pauseTime = 2.0;
    }
    
    private String calibrationMessageToString(final boolean b) {
        if (b) {
            return "Calibration Failed!";
        }
        return "Calibration Succeeded.";
    }
    
    @Override
    public void init() {
        this.compass = this.hardwareMap.compassSensor.get("compass");
        this.motorRight = this.hardwareMap.dcMotor.get("right");
        this.motorLeft = this.hardwareMap.dcMotor.get("left");
    }
    
    @Override
    public void init_loop() {
        this.motorRight.setDirection(DcMotor.Direction.REVERSE);
        this.compass.setMode(CompassSensor.CompassMode.CALIBRATION_MODE);
        this.telemetry.addData("Compass", "Compass in calibration mode");
        this.pauseTime = 3.0 + this.time;
    }
    
    @Override
    public void loop() {
        if (this.time > this.pauseTime) {
            if (this.keepTurning) {
                this.telemetry.addData("Compass", "Calibration mode. Turning the robot...");
                DbgLog.msg("Calibration mode. Turning the robot...");
                this.motorRight.setPower(-0.2);
                this.motorLeft.setPower(0.2);
                if (this.time > 3.0 + this.turnTime) {
                    this.keepTurning = false;
                    this.returnToMeasurementMode = true;
                }
            }
            else if (this.returnToMeasurementMode) {
                this.telemetry.addData("Compass", "Returning to measurement mode");
                DbgLog.msg("Returning to measurement mode");
                this.motorRight.setPower(0.0);
                this.motorLeft.setPower(0.0);
                this.compass.setMode(CompassSensor.CompassMode.MEASUREMENT_MODE);
                this.pauseTime = 3.0 + this.time;
                this.returnToMeasurementMode = false;
                this.monitorCalibrationSuccess = true;
                this.telemetry.addData("Compass", "Waiting for feedback from sensor...");
            }
            else if (this.monitorCalibrationSuccess) {
                final String calibrationMessageToString = this.calibrationMessageToString(this.compass.calibrationFailed());
                this.telemetry.addData("Compass", calibrationMessageToString);
                if (this.compass.calibrationFailed()) {
                    DbgLog.error("Calibration failed and needs to be re-run");
                }
                else {
                    DbgLog.msg(calibrationMessageToString);
                }
            }
            this.pauseTime = 3.0 + this.time;
        }
    }
}
