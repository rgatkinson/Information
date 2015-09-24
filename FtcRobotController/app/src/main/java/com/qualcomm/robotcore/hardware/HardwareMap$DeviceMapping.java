package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.RobotLog;
import java.util.Iterator;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

public static class DeviceMapping<DEVICE_TYPE> implements Iterable<DEVICE_TYPE>
{
    private Map<String, DEVICE_TYPE> a;
    
    public DeviceMapping() {
        this.a = new HashMap<String, DEVICE_TYPE>();
    }
    
    public Set<Map.Entry<String, DEVICE_TYPE>> entrySet() {
        return this.a.entrySet();
    }
    
    public DEVICE_TYPE get(final String s) {
        final DEVICE_TYPE value = this.a.get(s);
        if (value == null) {
            throw new IllegalArgumentException(String.format("Unable to find a hardware device with the name \"%s\"", s));
        }
        return value;
    }
    
    @Override
    public Iterator<DEVICE_TYPE> iterator() {
        return this.a.values().iterator();
    }
    
    public void logDevices() {
        if (!this.a.isEmpty()) {
            for (final Map.Entry<String, DEVICE_TYPE> entry : this.a.entrySet()) {
                if (entry.getValue() instanceof HardwareDevice) {
                    final HardwareDevice hardwareDevice = (HardwareDevice)entry.getValue();
                    RobotLog.i(String.format("%-45s %-30s %s", hardwareDevice.getDeviceName(), entry.getKey(), hardwareDevice.getConnectionInfo()));
                }
            }
        }
    }
    
    public void put(final String s, final DEVICE_TYPE device_TYPE) {
        this.a.put(s, device_TYPE);
    }
    
    public int size() {
        return this.a.size();
    }
}
