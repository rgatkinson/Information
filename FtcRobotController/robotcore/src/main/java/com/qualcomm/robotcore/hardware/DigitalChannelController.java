package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.SerialNumber;

public interface DigitalChannelController extends HardwareDevice {
    Mode getDigitalChannelMode(int var1);

    boolean getDigitalChannelState(int var1);

    SerialNumber getSerialNumber();

    void setDigitalChannelMode(int var1, Mode var2);

    void setDigitalChannelState(int var1, boolean var2);

    enum Mode {
        INPUT,
        OUTPUT;

        static {
            Mode[] var0 = new Mode[]{INPUT, OUTPUT};
        }
    }
}
