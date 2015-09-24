package com.ftdi.j2xx.interfaces;

public interface I2cMaster
{
    int init(int p0);
    
    int read(int p0, byte[] p1, int p2, int[] p3);
    
    int reset();
    
    int write(int p0, byte[] p1, int p2, int[] p3);
}
