package com.qualcomm.robotcore.hardware;

public interface ServoController extends HardwareDevice
{
    PwmStatus getPwmStatus();
    
    double getServoPosition(int p0);
    
    void pwmDisable();
    
    void pwmEnable();
    
    void setServoPosition(int p0, double p1);
    
    public enum PwmStatus
    {
        DISABLED, 
        ENABLED;
    }
}
