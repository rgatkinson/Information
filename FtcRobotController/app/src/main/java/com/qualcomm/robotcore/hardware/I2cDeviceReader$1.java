package com.qualcomm.robotcore.hardware;

class I2cDeviceReader$1 implements I2cPortReadyCallback {
    @Override
    public void portIsReady(final int n) {
        I2cDeviceReader.a(I2cDeviceReader.this);
    }
}