package com.qualcomm.robotcore.hardware;

public class PWMOutput implements HardwareDevice
{
    private PWMOutputController a;
    private int b;
    
    public PWMOutput(final PWMOutputController a, final int b) {
        this.a = null;
        this.b = -1;
        this.a = a;
        this.b = b;
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; port " + this.b;
    }
    
    @Override
    public String getDeviceName() {
        return "PWM Output";
    }
    
    public int getPulseWidthOutputTime() {
        return this.a.getPulseWidthOutputTime(this.b);
    }
    
    public int getPulseWidthPeriod() {
        return this.a.getPulseWidthPeriod(this.b);
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    public void setPulseWidthOutputTime(final int n) {
        this.a.setPulseWidthOutputTime(this.b, n);
    }
    
    public void setPulseWidthPeriod(final int n) {
        this.a.setPulseWidthPeriod(this.b, n);
    }
}
