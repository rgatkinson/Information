package com.qualcomm.robotcore.hardware;

public interface DcMotorController extends HardwareDevice
{
    RunMode getMotorChannelMode(int p0);
    
    DeviceMode getMotorControllerDeviceMode();
    
    int getMotorCurrentPosition(int p0);
    
    double getMotorPower(int p0);
    
    boolean getMotorPowerFloat(int p0);
    
    int getMotorTargetPosition(int p0);
    
    boolean isBusy(int p0);
    
    void setMotorChannelMode(int p0, RunMode p1);
    
    void setMotorControllerDeviceMode(DeviceMode p0);
    
    void setMotorPower(int p0, double p1);
    
    void setMotorPowerFloat(int p0);
    
    void setMotorTargetPosition(int p0, int p1);
    
    public enum DeviceMode
    {
        READ_ONLY, 
        READ_WRITE, 
        SWITCHING_TO_READ_MODE, 
        SWITCHING_TO_WRITE_MODE, 
        WRITE_ONLY;
    }
    
    public enum RunMode
    {
        RESET_ENCODERS, 
        RUN_TO_POSITION, 
        RUN_USING_ENCODERS, 
        RUN_WITHOUT_ENCODERS;
    }
}
