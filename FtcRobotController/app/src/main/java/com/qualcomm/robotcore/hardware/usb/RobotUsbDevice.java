package com.qualcomm.robotcore.hardware.usb;

import com.qualcomm.robotcore.exception.RobotCoreException;

public interface RobotUsbDevice
{
    void close();
    
    void purge(Channel p0) throws RobotCoreException;
    
    int read(byte[] p0) throws RobotCoreException;
    
    int read(byte[] p0, int p1, int p2) throws RobotCoreException;
    
    void setBaudRate(int p0) throws RobotCoreException;
    
    void setDataCharacteristics(byte p0, byte p1, byte p2) throws RobotCoreException;
    
    void setLatencyTimer(int p0) throws RobotCoreException;
    
    void write(byte[] p0) throws RobotCoreException;
    
    public enum Channel
    {
        BOTH, 
        NONE, 
        RX, 
        TX;
    }
}
