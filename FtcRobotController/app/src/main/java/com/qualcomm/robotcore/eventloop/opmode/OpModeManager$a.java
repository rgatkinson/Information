package com.qualcomm.robotcore.eventloop.opmode;

import java.util.Iterator;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.ServoController;

private static class a extends OpMode
{
    private void a() {
        final Iterator<ServoController> iterator = this.hardwareMap.servoController.iterator();
        while (iterator.hasNext()) {
            iterator.next().pwmDisable();
        }
        final Iterator<DcMotorController> iterator2 = this.hardwareMap.dcMotorController.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
        }
        for (final DcMotor dcMotor : this.hardwareMap.dcMotor) {
            dcMotor.setPower(0.0);
            dcMotor.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        }
        final Iterator<LightSensor> iterator4 = this.hardwareMap.lightSensor.iterator();
        while (iterator4.hasNext()) {
            iterator4.next().enableLed(false);
        }
    }
    
    @Override
    public void init() {
        this.a();
    }
    
    @Override
    public void init_loop() {
        this.a();
        this.telemetry.addData("Status", "Robot is stopped");
    }
    
    @Override
    public void loop() {
        this.a();
        this.telemetry.addData("Status", "Robot is stopped");
    }
    
    @Override
    public void stop() {
    }
}
