package com.ftdi.j2xx.protocol;

public class SpiSlaveEvent
{
    private int a;
    private boolean b;
    private Object c;
    private Object d;
    private Object e;
    
    public SpiSlaveEvent(final int a, final boolean b, final Object c, final Object d, final Object e) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }
    
    public Object getArg0() {
        return this.c;
    }
    
    public Object getArg1() {
        return this.d;
    }
    
    public Object getArg2() {
        return this.e;
    }
    
    public int getEventType() {
        return this.a;
    }
    
    public boolean getSync() {
        return this.b;
    }
    
    public void setArg0(final Object c) {
        this.c = c;
    }
    
    public void setArg1(final Object d) {
        this.d = d;
    }
    
    public void setArg2(final Object e) {
        this.e = e;
    }
    
    public void setEventType(final int a) {
        this.a = a;
    }
    
    public void setSync(final boolean b) {
        this.b = b;
    }
}
