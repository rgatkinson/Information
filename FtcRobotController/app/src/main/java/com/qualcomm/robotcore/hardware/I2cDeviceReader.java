package com.qualcomm.robotcore.hardware;

public class I2cDeviceReader
{
    private final I2cDevice a;
    
    public I2cDeviceReader(final I2cDevice a, final int n, final int n2, final int n3) {
        (this.a = a).enableI2cReadMode(n, n2, n3);
        a.setI2cPortActionFlag();
        a.writeI2cCacheToModule();
        a.registerForI2cPortReadyCallback(new I2cController.I2cPortReadyCallback() {
            @Override
            public void portIsReady(final int n) {
                I2cDeviceReader.this.a();
            }
        });
    }
    
    private void a() {
        this.a.setI2cPortActionFlag();
        this.a.readI2cCacheFromModule();
        this.a.writeI2cPortFlagOnlyToModule();
    }
    
    public byte[] getReadBuffer() {
        return this.a.getCopyOfReadBuffer();
    }
}
