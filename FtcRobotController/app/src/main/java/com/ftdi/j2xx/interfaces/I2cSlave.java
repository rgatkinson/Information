package com.ftdi.j2xx.interfaces;

public interface I2cSlave
{
    int getAddress(int[] p0);
    
    int init();
    
    int read(byte[] p0, int p1, int[] p2);
    
    int reset();
    
    int setAddress(int p0);
    
    int write(byte[] p0, int p1, int[] p2);
}
