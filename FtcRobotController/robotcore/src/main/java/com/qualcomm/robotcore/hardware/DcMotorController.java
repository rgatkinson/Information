package com.qualcomm.robotcore.hardware;

public interface DcMotorController extends HardwareDevice {
    RunMode getMotorChannelMode(int var1);

    DeviceMode getMotorControllerDeviceMode();

    void setMotorControllerDeviceMode(DeviceMode var1);

    int getMotorCurrentPosition(int var1);

    double getMotorPower(int var1);

    boolean getMotorPowerFloat(int var1);

    int getMotorTargetPosition(int var1);

    boolean isBusy(int var1);

    void setMotorChannelMode(int var1, RunMode var2);

    void setMotorPower(int var1, double var2);

    void setMotorPowerFloat(int var1);

    void setMotorTargetPosition(int var1, int var2);

    enum DeviceMode {
        READ_ONLY,
        READ_WRITE,
        SWITCHING_TO_READ_MODE,
        SWITCHING_TO_WRITE_MODE,
        WRITE_ONLY;

        static {
            DeviceMode[] var0 = new DeviceMode[]{SWITCHING_TO_READ_MODE, SWITCHING_TO_WRITE_MODE, READ_ONLY, WRITE_ONLY, READ_WRITE};
        }
    }

    enum RunMode {
        RESET_ENCODERS,
        RUN_TO_POSITION,
        RUN_USING_ENCODERS,
        RUN_WITHOUT_ENCODERS;

        static {
            RunMode[] var0 = new RunMode[]{RUN_USING_ENCODERS, RUN_WITHOUT_ENCODERS, RUN_TO_POSITION, RESET_ENCODERS};
        }
    }
}
