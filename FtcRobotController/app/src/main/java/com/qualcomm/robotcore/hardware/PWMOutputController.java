package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.SerialNumber;

public interface PWMOutputController extends HardwareDevice
{
    int getPulseWidthOutputTime(int p0);
    
    int getPulseWidthPeriod(int p0);
    
    SerialNumber getSerialNumber();
    
    void setPulseWidthOutputTime(int p0, int p1);
    
    void setPulseWidthPeriod(int p0, int p1);
}
