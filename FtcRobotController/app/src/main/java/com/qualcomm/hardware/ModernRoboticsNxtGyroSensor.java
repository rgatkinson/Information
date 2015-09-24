package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import com.qualcomm.robotcore.hardware.GyroSensor;

public class ModernRoboticsNxtGyroSensor extends GyroSensor
{
    private final ModernRoboticsUsbLegacyModule a;
    private final int b;
    
    ModernRoboticsNxtGyroSensor(final ModernRoboticsUsbLegacyModule a, final int b) {
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
        return "NXT Gyro Sensor";
    }
    
    @Override
    public double getRotation() {
        return TypeConversion.byteArrayToShort(this.a.readAnalog(this.b), ByteOrder.LITTLE_ENDIAN);
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    @Override
    public String status() {
        return String.format("NXT Gyro Sensor, connected via device %s, port %d", this.a.getSerialNumber().toString(), this.b);
    }
}
