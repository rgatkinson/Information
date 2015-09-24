package com.qualcomm.robotcore.eventloop.opmode;

import java.util.concurrent.TimeUnit;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;

public abstract class OpMode
{
    private long a;
    public Gamepad gamepad1;
    public Gamepad gamepad2;
    public HardwareMap hardwareMap;
    public Telemetry telemetry;
    public double time;
    
    public OpMode() {
        this.gamepad1 = new Gamepad();
        this.gamepad2 = new Gamepad();
        this.telemetry = new Telemetry();
        this.hardwareMap = new HardwareMap();
        this.time = 0.0;
        this.a = 0L;
        this.a = System.nanoTime();
    }
    
    public double getRuntime() {
        return (System.nanoTime() - this.a) / TimeUnit.SECONDS.toNanos(1L);
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
