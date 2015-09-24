package com.ftdi.j2xx.interfaces;

public interface Gpio
{
    int init(int[] p0);
    
    int read(int p0, boolean[] p1);
    
    int write(int p0, boolean p1);
}
