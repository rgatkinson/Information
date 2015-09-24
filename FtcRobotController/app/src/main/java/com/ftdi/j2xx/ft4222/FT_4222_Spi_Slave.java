package com.ftdi.j2xx.ft4222;

import android.util.Log;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.interfaces.SpiSlave;

public class FT_4222_Spi_Slave implements SpiSlave
{
    private FT_4222_Device a;
    private FT_Device b;
    private Lock c;
    
    public FT_4222_Spi_Slave(final FT_4222_Device a) {
        this.a = a;
        this.b = a.mFtDev;
        this.c = new ReentrantLock();
    }
    
    private int a() {
        if (this.a.mChipStatus.g != 4) {
            return 1003;
        }
        return 0;
    }
    
    @Override
    public int getRxStatus(final int[] array) {
        if (array == null) {
            return 1009;
        }
        final int a = this.a();
        if (a != 0) {
            return a;
        }
        this.c.lock();
        final int queueStatus = this.b.getQueueStatus();
        this.c.unlock();
        if (queueStatus >= 0) {
            array[0] = queueStatus;
            return 0;
        }
        array[0] = -1;
        return 4;
    }
    
    @Override
    public int init() {
        final b mChipStatus = this.a.mChipStatus;
        final a mSpiMasterCfg = this.a.mSpiMasterCfg;
        mSpiMasterCfg.a = 1;
        mSpiMasterCfg.b = 2;
        mSpiMasterCfg.c = 0;
        mSpiMasterCfg.d = 0;
        mSpiMasterCfg.e = 1;
        this.c.lock();
        this.a.cleanRxData();
        final int vendorCmdSet = this.b.VendorCmdSet(33, 0x42 | mSpiMasterCfg.a << 8);
        int n = 0;
        if (vendorCmdSet < 0) {
            n = 4;
        }
        if (this.b.VendorCmdSet(33, 0x44 | mSpiMasterCfg.b << 8) < 0) {
            n = 4;
        }
        if (this.b.VendorCmdSet(33, 0x45 | mSpiMasterCfg.c << 8) < 0) {
            n = 4;
        }
        if (this.b.VendorCmdSet(33, 0x46 | mSpiMasterCfg.d << 8) < 0) {
            n = 4;
        }
        if (this.b.VendorCmdSet(33, 67) < 0) {
            n = 4;
        }
        if (this.b.VendorCmdSet(33, 0x48 | mSpiMasterCfg.e << 8) < 0) {
            n = 4;
        }
        if (this.b.VendorCmdSet(33, 1029) < 0) {
            n = 4;
        }
        this.c.unlock();
        mChipStatus.g = 4;
        return n;
    }
    
    @Override
    public int read(final byte[] array, final int n, final int[] array2) {
        this.c.lock();
        int n2;
        if (this.b == null || !this.b.isOpen()) {
            this.c.unlock();
            n2 = 3;
        }
        else {
            final int read = this.b.read(array, n);
            this.c.unlock();
            array2[0] = read;
            n2 = 0;
            if (read < 0) {
                return 4;
            }
        }
        return n2;
    }
    
    @Override
    public int reset() {
        this.c.lock();
        final int vendorCmdSet = this.b.VendorCmdSet(33, 74);
        int n = 0;
        if (vendorCmdSet < 0) {
            n = 4;
        }
        this.c.unlock();
        return n;
    }
    
    public int setDrivingStrength(final int n, final int n2, final int n3) {
        byte b = 3;
        byte b2 = 4;
        final b mChipStatus = this.a.mChipStatus;
        if (mChipStatus.g != b && mChipStatus.g != b2) {
            return 1003;
        }
        final int n4 = n3 | (n << 4 | n2 << 2);
        if (mChipStatus.g != b) {
            b = b2;
        }
        this.c.lock();
        final int vendorCmdSet = this.b.VendorCmdSet(33, 0xA0 | n4 << 8);
        byte b3 = 0;
        if (vendorCmdSet < 0) {
            b3 = b2;
        }
        if (this.b.VendorCmdSet(33, 0x5 | b << 8) >= 0) {
            b2 = b3;
        }
        this.c.unlock();
        return b2;
    }
    
    @Override
    public int write(final byte[] array, final int n, final int[] array2) {
        int a;
        if (array2 == null || array == null) {
            a = 1009;
        }
        else {
            a = this.a();
            if (a == 0) {
                if (n > 512) {
                    return 1010;
                }
                this.c.lock();
                array2[0] = this.b.write(array, n);
                this.c.unlock();
                if (array2[0] != n) {
                    Log.e("FTDI_Device::", "Error write =" + n + " tx=" + array2[0]);
                    return 4;
                }
            }
        }
        return a;
    }
}
