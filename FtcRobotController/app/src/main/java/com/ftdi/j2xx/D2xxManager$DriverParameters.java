package com.ftdi.j2xx;

import android.util.Log;

public static class DriverParameters
{
    private int a;
    private int b;
    private int c;
    private int d;
    
    public DriverParameters() {
        this.a = 16384;
        this.b = 16384;
        this.c = 16;
        this.d = 5000;
    }
    
    public int getBufferNumber() {
        return this.c;
    }
    
    public int getMaxBufferSize() {
        return this.a;
    }
    
    public int getMaxTransferSize() {
        return this.b;
    }
    
    public int getReadTimeout() {
        return this.d;
    }
    
    public boolean setBufferNumber(final int c) {
        if (c >= 2 && c <= 16) {
            this.c = c;
            return true;
        }
        Log.e("D2xx::", "***nrBuffers Out of correct range***");
        return false;
    }
    
    public boolean setMaxBufferSize(final int a) {
        if (a >= 64 && a <= 262144) {
            this.a = a;
            return true;
        }
        Log.e("D2xx::", "***bufferSize Out of correct range***");
        return false;
    }
    
    public boolean setMaxTransferSize(final int b) {
        if (b >= 64 && b <= 262144) {
            this.b = b;
            return true;
        }
        Log.e("D2xx::", "***maxTransferSize Out of correct range***");
        return false;
    }
    
    public boolean setReadTimeout(final int d) {
        this.d = d;
        return true;
    }
}
