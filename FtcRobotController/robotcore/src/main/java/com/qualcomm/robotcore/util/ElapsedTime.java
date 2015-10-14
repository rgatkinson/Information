package com.qualcomm.robotcore.util;

public class ElapsedTime {
    private long a = 0L;
    private double b = 1.0E9D;

    public ElapsedTime() {
        this.reset();
    }

    public ElapsedTime(long var1) {
        this.a = var1;
    }

    public ElapsedTime(Resolution var1) {
        this.reset();
        switch (var1.ordinal()) {
            case 1:
                this.b = 1.0E9D;
                return;
            case 2:
                this.b = 1000000.0D;
                return;
            default:
        }
    }

    private String a() {
        return this.b == 1.0E9D ? "seconds" : (this.b == 1000000.0D ? "milliseconds" : "Unknown units");
    }

    public void log(String var1) {
        Object[] var2 = new Object[]{var1, Double.valueOf(this.time()), this.a()};
        RobotLog.v(String.format("TIMER: %20s - %1.3f %s", var2));
    }

    public void reset() {
        this.a = System.nanoTime();
    }

    public double startTime() {
        return (double) this.a / this.b;
    }

    public double time() {
        return (double) (System.nanoTime() - this.a) / this.b;
    }

    public String toString() {
        Object[] var1 = new Object[]{Double.valueOf(this.time()), this.a()};
        return String.format("%1.4f %s", var1);
    }

    public enum Resolution {
        MILLISECONDS,
        SECONDS;

        static {
            Resolution[] var0 = new Resolution[]{SECONDS, MILLISECONDS};
        }
    }
}
