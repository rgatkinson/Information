package com.ftdi.j2xx.protocol;

public class SpiSlaveResponseEvent extends SpiSlaveEvent
{
    public static final int DATA_CORRUPTED = 1;
    public static final int IO_ERROR = 2;
    public static final int OK = 0;
    public static final int RESET = 3;
    public static final int RES_SLAVE_READ = 3;
    private int a;
    
    public SpiSlaveResponseEvent(final int n, final int a, final Object o, final Object o2, final Object o3) {
        super(n, false, o, o2, o3);
        this.a = a;
    }
    
    public int getResponseCode() {
        return this.a;
    }
}
