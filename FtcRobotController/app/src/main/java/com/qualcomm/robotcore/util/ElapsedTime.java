package com.qualcomm.robotcore.util;

public class ElapsedTime
{
    private long a;
    
    public ElapsedTime() {
        this.a = 0L;
        this.reset();
    }
    
    public ElapsedTime(final long a) {
        this.a = 0L;
        this.a = a;
    }
    
    public void log(final String s) {
        RobotLog.v(String.format("TIMER: %20s - %1.3f", s, this.time()));
    }
    
    public void reset() {
        this.a = System.nanoTime();
    }
    
    public double startTime() {
        return this.a / 1.0E9;
    }
    
    public double time() {
        return (System.nanoTime() - this.a) / 1.0E9;
    }
    
    @Override
    public String toString() {
        return String.format("%1.4f seconds", this.time());
    }
}
