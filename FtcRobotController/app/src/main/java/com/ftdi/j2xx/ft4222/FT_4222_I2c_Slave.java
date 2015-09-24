package com.ftdi.j2xx.ft4222;

import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.interfaces.I2cSlave;

public class FT_4222_I2c_Slave implements I2cSlave
{
    FT_4222_Device a;
    FT_Device b;
    
    public FT_4222_I2c_Slave(final FT_4222_Device a) {
        this.a = a;
        this.b = this.a.mFtDev;
    }
    
    int a(final boolean b) {
        if (b) {
            if (this.a.mChipStatus.g == 1) {
                return 0;
            }
        }
        else if (this.a.mChipStatus.g == 2) {
            return 0;
        }
        return 1004;
    }
    
    int a(final int[] array) {
        array[0] = 0;
        final int maxBuckSize = this.a.getMaxBuckSize();
        switch (this.a.mChipStatus.g) {
            default: {
                return 17;
            }
            case 2: {
                array[0] = maxBuckSize - 4;
                return 0;
            }
        }
    }
    
    boolean a() {
        return this.a.mChipStatus.a == 0 || this.a.mChipStatus.a == 3;
    }
    
    public int cmdGet(final int n, final int n2, final byte[] array, final int n3) {
        return this.b.VendorCmdGet(32, n | n2 << 8, array, n3);
    }
    
    public int cmdSet(final int n, final int n2) {
        return this.b.VendorCmdSet(33, n | n2 << 8);
    }
    
    public int cmdSet(final int n, final int n2, final byte[] array, final int n3) {
        return this.b.VendorCmdSet(33, n | n2 << 8, array, n3);
    }
    
    @Override
    public int getAddress(final int[] array) {
        final byte[] array2 = { 0 };
        final int a = this.a(false);
        if (a != 0) {
            return a;
        }
        if (this.b.VendorCmdGet(33, 92, array2, 1) < 0) {
            return 18;
        }
        array[0] = array2[0];
        return 0;
    }
    
    @Override
    public int init() {
        int n = this.a.init();
        if (n == 0) {
            if (!this.a()) {
                return 1012;
            }
            n = this.cmdSet(5, 2);
            if (n >= 0) {
                this.a.mChipStatus.g = 2;
                return 0;
            }
        }
        return n;
    }
    
    @Override
    public int read(final byte[] array, int n, final int[] array2) {
        final int[] array3 = { 0 };
        final long currentTimeMillis = System.currentTimeMillis();
        final int readTimeout = this.b.getReadTimeout();
        int n2;
        if (n < 1) {
            n2 = 6;
        }
        else {
            n2 = this.a(false);
            if (n2 == 0) {
                n2 = this.a(array3);
                if (n2 == 0) {
                    if (n > array3[0]) {
                        return 1010;
                    }
                    array2[0] = 0;
                    int n3;
                    for (n3 = this.b.getQueueStatus(); n3 < n && System.currentTimeMillis() - currentTimeMillis < readTimeout; n3 = this.b.getQueueStatus()) {}
                    if (n3 <= n) {
                        n = n3;
                    }
                    final int read = this.b.read(array, n);
                    if (read < 0) {
                        return 1011;
                    }
                    array2[0] = read;
                    return 0;
                }
            }
        }
        return n2;
    }
    
    @Override
    public int reset() {
        final int a = this.a(false);
        if (a != 0) {
            return a;
        }
        return this.cmdSet(91, 1);
    }
    
    @Override
    public int setAddress(final int n) {
        final byte[] array = { (byte)(n & 0xFF) };
        final int a = this.a(false);
        if (a != 0) {
            return a;
        }
        if (this.cmdSet(92, array[0]) < 0) {
            return 18;
        }
        return 0;
    }
    
    @Override
    public int write(final byte[] array, final int n, final int[] array2) {
        final int[] array3 = { 0 };
        int n2;
        if (n < 1) {
            n2 = 6;
        }
        else {
            n2 = this.a(false);
            if (n2 == 0) {
                n2 = this.a(array3);
                if (n2 == 0) {
                    if (n > array3[0]) {
                        return 1010;
                    }
                    array2[0] = 0;
                    if (n == (array2[0] = this.b.write(array, n))) {
                        return 0;
                    }
                    return 10;
                }
            }
        }
        return n2;
    }
}
