package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.SerialNumber;

public interface AnalogOutputController extends HardwareDevice
{
    SerialNumber getSerialNumber();
    
    void setAnalogOutputFrequency(int p0, int p1);
    
    void setAnalogOutputMode(int p0, byte p1);
    
    void setAnalogOutputVoltage(int p0, int p1);
}
