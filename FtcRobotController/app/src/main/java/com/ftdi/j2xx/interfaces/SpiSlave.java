package com.ftdi.j2xx.interfaces;

public interface SpiSlave
{
    int getRxStatus(int[] p0);
    
    int init();
    
    int read(byte[] p0, int p1, int[] p2);
    
    int reset();
    
    int write(byte[] p0, int p1, int[] p2);
}
