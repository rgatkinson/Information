package com.qualcomm.robotcore.hardware;

public class AnalogInput implements HardwareDevice {
    private AnalogInputController a = null;
    private int b = -1;

    public AnalogInput(AnalogInputController var1, int var2) {
        this.a = var1;
        this.b = var2;
    }

    public void close() {
    }

    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; analog port " + this.b;
    }

    public String getDeviceName() {
        return "Analog Input";
    }

    public int getValue() {
        return this.a.getAnalogInputValue(this.b);
    }

    public int getVersion() {
        return 1;
    }
}
