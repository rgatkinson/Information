package com.ftdi.j2xx.ft4222;

import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.interfaces.I2cMaster;

public class FT_4222_I2c_Master implements I2cMaster
{
    FT_4222_Device a;
    FT_Device b;
    int c;
    
    public FT_4222_I2c_Master(final FT_4222_Device a) {
        this.a = a;
        this.b = this.a.mFtDev;
    }
    
    private int a(final int n, final int n2) {
        double n3 = 0.0;
        switch (n) {
            default: {
                n3 = 16.666666666666668;
                break;
            }
            case 1: {
                n3 = 41.666666666666664;
                break;
            }
            case 2: {
                n3 = 20.833333333333332;
                break;
            }
            case 3: {
                n3 = 12.5;
                break;
            }
        }
        if (60 <= n2 && n2 <= 100) {
            int n4 = (int)(0.5 + (1000000.0 / n2 / (n3 * 8.0) - 1.0));
            if (n4 > 127) {
                n4 = 127;
            }
            return n4;
        }
        if (100 < n2 && n2 <= 400) {
            return 0xC0 | (int)(0.5 + (1000000.0 / n2 / (n3 * 6.0) - 1.0));
        }
        if (400 < n2 && n2 <= 1000) {
            return 0xC0 | (int)(0.5 + (1000000.0 / n2 / (n3 * 6.0) - 1.0));
        }
        if (1000 < n2 && n2 <= 3400) {
            return 0xFFFFFFBF & (0x80 | (int)(0.5 + (1000000.0 / n2 / (n3 * 6.0) - 1.0)));
        }
        return 74;
    }
    
    int a(final int n) {
        if ((0xFC00 & n) > 0) {
            return 1007;
        }
        return 0;
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
            case 1: {
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
    public int init(final int c) {
        final byte[] array = { 0 };
        int n = this.a.init();
        if (n == 0) {
            if (!this.a()) {
                return 1012;
            }
            this.cmdSet(81, 0);
            n = this.a.getClock(array);
            if (n == 0) {
                final int a = this.a(array[0], c);
                n = this.cmdSet(5, 1);
                if (n >= 0) {
                    this.a.mChipStatus.g = 1;
                    n = this.cmdSet(82, a);
                    if (n >= 0) {
                        this.c = c;
                        return 0;
                    }
                }
            }
        }
        return n;
    }
    
    @Override
    public int read(final int n, final byte[] array, int n2, final int[] array2) {
        final short n3 = (short)(0xFFFF & n);
        final short n4 = (short)((n & 0x380) >> 7);
        final short n5 = (short)n2;
        final int[] array3 = { 0 };
        final byte[] array4 = new byte[4];
        final long currentTimeMillis = System.currentTimeMillis();
        final int readTimeout = this.b.getReadTimeout();
        int n6 = this.a(n);
        if (n6 == 0) {
            if (n2 < 1) {
                return 6;
            }
            n6 = this.a(true);
            if (n6 == 0) {
                n6 = this.a(array3);
                if (n6 == 0) {
                    if (n2 > array3[0]) {
                        return 1010;
                    }
                    array4[array2[0] = 0] = (byte)(1 + (n3 << 1));
                    array4[1] = (byte)n4;
                    array4[2] = (byte)(0xFF & n5 >> 8);
                    array4[3] = (byte)(n5 & 0xFF);
                    if (4 != this.b.write(array4, 4)) {
                        return 1011;
                    }
                    int n7;
                    for (n7 = this.b.getQueueStatus(); n7 < n2 && System.currentTimeMillis() - currentTimeMillis < readTimeout; n7 = this.b.getQueueStatus()) {}
                    if (n7 <= n2) {
                        n2 = n7;
                    }
                    if ((array2[0] = this.b.read(array, n2)) >= 0) {
                        return 0;
                    }
                    return 1011;
                }
            }
        }
        return n6;
    }
    
    @Override
    public int reset() {
        final int a = this.a(true);
        if (a != 0) {
            return a;
        }
        return this.cmdSet(81, 1);
    }
    
    @Override
    public int write(final int n, final byte[] array, final int n2, final int[] array2) {
        final short n3 = (short)n;
        final short n4 = (short)((n & 0x380) >> 7);
        final short n5 = (short)n2;
        final byte[] array3 = new byte[n2 + 4];
        final int[] array4 = { 0 };
        int n6 = this.a(n);
        if (n6 == 0) {
            if (n2 < 1) {
                return 6;
            }
            n6 = this.a(true);
            if (n6 == 0) {
                n6 = this.a(array4);
                if (n6 == 0) {
                    if (n2 > array4[0]) {
                        return 1010;
                    }
                    array3[array2[0] = 0] = (byte)(n3 << 1);
                    array3[1] = (byte)n4;
                    array3[2] = (byte)(0xFF & n5 >> 8);
                    array3[3] = (byte)(n5 & 0xFF);
                    for (int i = 0; i < n2; ++i) {
                        array3[i + 4] = array[i];
                    }
                    array2[0] = -4 + this.b.write(array3, n2 + 4);
                    if (n2 == array2[0]) {
                        return 0;
                    }
                    return 10;
                }
            }
        }
        return n6;
    }
}
