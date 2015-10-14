package com.qualcomm.robotcore.hardware;

public class PWMOutput implements HardwareDevice {
    private PWMOutputController a = null;
    private int b = -1;

    public PWMOutput(PWMOutputController var1, int var2) {
        this.a = var1;
        this.b = var2;
    }

    public void close() {
    }

    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; port " + this.b;
    }

    public String getDeviceName() {
        return "PWM Output";
    }

    public int getPulseWidthOutputTime() {
        return this.a.getPulseWidthOutputTime(this.b);
    }

    public void setPulseWidthOutputTime(int var1) {
        this.a.setPulseWidthOutputTime(this.b, var1);
    }

    public int getPulseWidthPeriod() {
        return this.a.getPulseWidthPeriod(this.b);
    }

    public void setPulseWidthPeriod(int var1) {
        this.a.setPulseWidthPeriod(this.b, var1);
    }

    public int getVersion() {
        return 1;
    }
}
