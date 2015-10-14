package com.qualcomm.robotcore.hardware;

public interface ServoController extends HardwareDevice {
    PwmStatus getPwmStatus();

    double getServoPosition(int var1);

    void pwmDisable();

    void pwmEnable();

    void setServoPosition(int var1, double var2);

    enum PwmStatus {
        DISABLED,
        ENABLED;

        static {
            PwmStatus[] var0 = new PwmStatus[]{ENABLED, DISABLED};
        }
    }
}
