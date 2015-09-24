package com.ftdi.j2xx.interfaces;

public interface SpiMaster
{
    int init(int p0, int p1, int p2, int p3, byte p4);
    
    int multiReadWrite(byte[] p0, byte[] p1, int p2, int p3, int p4, int[] p5);
    
    int reset();
    
    int setLines(int p0);
    
    int singleRead(byte[] p0, int p1, int[] p2, boolean p3);
    
    int singleReadWrite(byte[] p0, byte[] p1, int p2, int[] p3, boolean p4);
    
    int singleWrite(byte[] p0, int p1, int[] p2, boolean p3);
}
