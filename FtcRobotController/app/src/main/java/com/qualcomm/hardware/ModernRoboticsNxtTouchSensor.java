package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class ModernRoboticsNxtTouchSensor extends TouchSensor
{
    private final ModernRoboticsUsbLegacyModule a;
    private final int b;
    
    public ModernRoboticsNxtTouchSensor(final ModernRoboticsUsbLegacyModule a, final int b) {
        a.enableAnalogReadMode(b);
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
        return "NXT Touch Sensor";
    }
    
    @Override
    public double getValue() {
        if (TypeConversion.byteArrayToShort(this.a.readAnalog(this.b), ByteOrder.LITTLE_ENDIAN) > 675.0) {
            return 0.0;
        }
        return 1.0;
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    @Override
    public boolean isPressed() {
        return this.getValue() > 0.0;
    }
    
    public String status() {
        return String.format("NXT Touch Sensor, connected via device %s, port %d", this.a.getSerialNumber().toString(), this.b);
    }
}
