package com.qualcomm.robotcore.util;

import java.io.Serializable;

public class SerialNumber implements Serializable
{
    private String a;
    
    public SerialNumber() {
        this.a = "N/A";
    }
    
    public SerialNumber(final String a) {
        this.a = a;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != null) {
            if (o == this) {
                return true;
            }
            if (o instanceof SerialNumber) {
                return this.a.equals(((SerialNumber)o).getSerialNumber());
            }
            if (o instanceof String) {
                return this.a.equals(o);
            }
        }
        return false;
    }
    
    public String getSerialNumber() {
        return this.a;
    }
    
    @Override
    public int hashCode() {
        return this.a.hashCode();
    }
    
    public void setSerialNumber(final String a) {
        this.a = a;
    }
    
    @Override
    public String toString() {
        return this.a;
    }
}
