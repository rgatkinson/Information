package com.qualcomm.robotcore.eventloop.opmode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robocol.Telemetry;

import java.util.concurrent.TimeUnit;

public abstract class OpMode {
    public Gamepad gamepad1 = new Gamepad();
    public Gamepad gamepad2 = new Gamepad();
    public HardwareMap hardwareMap = new HardwareMap();
    public Telemetry telemetry = new Telemetry();
    public double time = 0.0D;
    private long a = 0L;

    public OpMode() {
        this.a = System.nanoTime();
    }

    public double getRuntime() {
        double var1 = (double) TimeUnit.SECONDS.toNanos(1L);
        return (double) (System.nanoTime() - this.a) / var1;
    }

    public abstract void init();

    public void init_loop() {
    }

    public abstract void loop();

    public void resetStartTime() {
        this.a = System.nanoTime();
    }

    public void start() {
    }

    public void stop() {
    }
}
