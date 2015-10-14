package com.qualcomm.robotcore.util;

import java.io.Serializable;

public class SerialNumber implements Serializable {
    private String a;

    public SerialNumber() {
        this.a = "N/A";
    }

    public SerialNumber(String var1) {
        this.a = var1;
    }

    public boolean equals(Object var1) {
        if (var1 != null) {
            if (var1 == this) {
                return true;
            }

            if (var1 instanceof SerialNumber) {
                return this.a.equals(((SerialNumber) var1).getSerialNumber());
            }

            if (var1 instanceof String) {
                return this.a.equals(var1);
            }
        }

        return false;
    }

    public String getSerialNumber() {
        return this.a;
    }

    public void setSerialNumber(String var1) {
        this.a = var1;
    }

    public int hashCode() {
        return this.a.hashCode();
    }

    public String toString() {
        return this.a;
    }
}
