package com.ftdi.j2xx.ft4222;

import com.ftdi.j2xx.interfaces.SpiSlave;
import com.ftdi.j2xx.interfaces.SpiMaster;
import com.ftdi.j2xx.interfaces.I2cSlave;
import com.ftdi.j2xx.interfaces.I2cMaster;
import com.ftdi.j2xx.interfaces.Gpio;
import com.ftdi.j2xx.FT_Device;

public class FT_4222_Device
{
    protected String TAG;
    protected b mChipStatus;
    protected FT_Device mFtDev;
    protected e mGpio;
    protected a mSpiMasterCfg;
    
    public FT_4222_Device(final FT_Device mFtDev) {
        this.TAG = "FT4222";
        this.mFtDev = mFtDev;
        this.mChipStatus = new b();
        this.mSpiMasterCfg = new a();
        this.mGpio = new e();
    }
    
    public boolean cleanRxData() {
        final int queueStatus = this.mFtDev.getQueueStatus();
        if (queueStatus > 0) {
            final byte[] array = new byte[queueStatus];
            if (this.mFtDev.read(array, queueStatus) != array.length) {
                return false;
            }
        }
        return true;
    }
    
    public int getClock(final byte[] array) {
        if (this.mFtDev.VendorCmdGet(32, 4, array, 1) >= 0) {
            this.mChipStatus.f = array[0];
            return 0;
        }
        return 18;
    }
    
    public Gpio getGpioDevice() {
        if (!this.isFT4222Device()) {
            return null;
        }
        return new FT_4222_Gpio(this);
    }
    
    public I2cMaster getI2cMasterDevice() {
        if (!this.isFT4222Device()) {
            return null;
        }
        return new FT_4222_I2c_Master(this);
    }
    
    public I2cSlave getI2cSlaveDevice() {
        if (!this.isFT4222Device()) {
            return null;
        }
        return new FT_4222_I2c_Slave(this);
    }
    
    protected int getMaxBuckSize() {
        if (this.mChipStatus.c != 0) {
            return 64;
        }
        switch (this.mChipStatus.a) {
            default: {
                return 512;
            }
            case 1:
            case 2: {
                return 256;
            }
        }
    }
    
    public SpiMaster getSpiMasterDevice() {
        if (!this.isFT4222Device()) {
            return null;
        }
        return new FT_4222_Spi_Master(this);
    }
    
    public SpiSlave getSpiSlaveDevice() {
        if (!this.isFT4222Device()) {
            return null;
        }
        return new FT_4222_Spi_Slave(this);
    }
    
    public int init() {
        final byte[] array = new byte[13];
        if (this.mFtDev.VendorCmdGet(32, 1, array, 13) != 13) {
            return 18;
        }
        this.mChipStatus.a(array);
        return 0;
    }
    
    public boolean isFT4222Device() {
        if (this.mFtDev != null) {
            switch (0xFF00 & this.mFtDev.getDeviceInfo().bcdDevice) {
                case 6144: {
                    this.mFtDev.getDeviceInfo().type = 10;
                    return true;
                }
                case 6400: {
                    this.mFtDev.getDeviceInfo().type = 11;
                    return true;
                }
                case 5888: {
                    this.mFtDev.getDeviceInfo().type = 12;
                    return true;
                }
            }
        }
        return false;
    }
    
    public int setClock(final byte f) {
        int vendorCmdSet;
        if (f == this.mChipStatus.f) {
            vendorCmdSet = 0;
        }
        else {
            vendorCmdSet = this.mFtDev.VendorCmdSet(33, 0x4 | f << 8);
            if (vendorCmdSet == 0) {
                this.mChipStatus.f = f;
                return vendorCmdSet;
            }
        }
        return vendorCmdSet;
    }
}
