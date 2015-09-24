package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import com.qualcomm.robotcore.hardware.TouchSensorMultiplexer;

public class ModernRoboticsNxtTouchSensorMultiplexer extends TouchSensorMultiplexer
{
    public static final int INVALID = -1;
    public static final int[] MASK_MAP;
    public static final int MASK_TOUCH_SENSOR_1 = 1;
    public static final int MASK_TOUCH_SENSOR_2 = 2;
    public static final int MASK_TOUCH_SENSOR_3 = 4;
    public static final int MASK_TOUCH_SENSOR_4 = 8;
    int a;
    private final ModernRoboticsUsbLegacyModule b;
    private final int c;
    
    static {
        MASK_MAP = new int[] { -1, 1, 2, 4, 8 };
    }
    
    public ModernRoboticsNxtTouchSensorMultiplexer(final ModernRoboticsUsbLegacyModule b, final int c) {
        this.a = 4;
        b.enableAnalogReadMode(c);
        this.b = b;
        this.c = c;
    }
    
    private int a() {
        final short n = (short)(1023 - TypeConversion.byteArrayToShort(this.b.readAnalog(3), ByteOrder.LITTLE_ENDIAN));
        return (5 + n * 339 / (1023 - n)) / 10;
    }
    
    private void a(final int n) {
        if (n <= 0 || n > this.a) {
            throw new IllegalArgumentException(String.format("Channel %d is invalid; valid channels are 1..%d", n, this.a));
        }
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public String getConnectionInfo() {
        return this.b.getConnectionInfo() + "; port " + this.c;
    }
    
    @Override
    public String getDeviceName() {
        return "NXT Touch Sensor Multiplexer";
    }
    
    @Override
    public int getSwitches() {
        return this.a();
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    @Override
    public boolean isTouchSensorPressed(final int n) {
        this.a(n);
        return (this.a() & ModernRoboticsNxtTouchSensorMultiplexer.MASK_MAP[n]) > 0;
    }
    
    public String status() {
        return String.format("NXT Touch Sensor Multiplexer, connected via device %s, port %d", this.b.getSerialNumber().toString(), this.c);
    }
}
