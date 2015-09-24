package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.TypeConversion;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.LightSensor;

public class ModernRoboticsNxtLightSensor extends LightSensor
{
    public static final byte LED_DIGITAL_LINE_NUMBER;
    private final ModernRoboticsUsbLegacyModule a;
    private final int b;
    
    ModernRoboticsNxtLightSensor(final ModernRoboticsUsbLegacyModule a, final int b) {
        a.enableAnalogReadMode(b);
        this.a = a;
        this.b = b;
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public void enableLed(final boolean b) {
        this.a.setDigitalLine(this.b, 0, b);
    }
    
    @Override
    public String getConnectionInfo() {
        return this.a.getConnectionInfo() + "; port " + this.b;
    }
    
    @Override
    public String getDeviceName() {
        return "NXT Light Sensor";
    }
    
    @Override
    public double getLightDetected() {
        return Range.scale(this.a.readAnalog(this.b)[0], -128.0, 127.0, 0.0, 1.0);
    }
    
    @Override
    public int getLightDetectedRaw() {
        return TypeConversion.unsignedByteToInt(this.a.readAnalog(this.b)[0]);
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    @Override
    public String status() {
        return String.format("NXT Light Sensor, connected via device %s, port %d", this.a.getSerialNumber().toString(), this.b);
    }
}
