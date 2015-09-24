package com.ftdi.j2xx.ft4222;

import android.util.Log;
import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.interfaces.Gpio;

public class FT_4222_Gpio implements Gpio
{
    boolean a;
    private FT_4222_Device b;
    private FT_Device c;
    
    public FT_4222_Gpio(final FT_4222_Device b) {
        this.a = true;
        this.b = b;
        this.c = this.b.mFtDev;
    }
    
    int a(final int n) {
        final b mChipStatus = this.b.mChipStatus;
        if (mChipStatus.a == 2 || mChipStatus.a == 3) {
            return 1013;
        }
        if (n >= 4) {
            return 1014;
        }
        return 0;
    }
    
    int a(final d d) {
        final byte[] array = new byte[8];
        final int cmdGet = this.cmdGet(32, 0, array, 8);
        d.a.a = array[0];
        d.a.b = array[1];
        d.b = array[5];
        d.c = array[6];
        d.d[0] = array[7];
        if (cmdGet == 8) {
            return 0;
        }
        return cmdGet;
    }
    
    void a(final int n, final byte b, final boolean[] array) {
        array[0] = this.d(0x1 & (b & 1 << n) >> n);
    }
    
    boolean b(final int n) {
        byte b = (byte)(true ? 1 : 0);
        final b mChipStatus = this.b.mChipStatus;
        switch (mChipStatus.a) {
            case 0: {
                if ((n == 0 || n == b) && (mChipStatus.g == b || mChipStatus.g == 2)) {
                    b = (byte)(false ? 1 : 0);
                }
                if (this.d(mChipStatus.i) && n == 2) {
                    b = (byte)(false ? 1 : 0);
                }
                if (this.d(mChipStatus.j) && n == 3) {
                    return false;
                }
                break;
            }
            case 1: {
                if (n == 0 || n == b) {
                    b = (byte)(false ? 1 : 0);
                }
                if (this.d(mChipStatus.i) && n == 2) {
                    b = (byte)(false ? 1 : 0);
                }
                if (this.d(mChipStatus.j) && n == 3) {
                    return false;
                }
                break;
            }
            case 2:
            case 3: {
                return false;
            }
        }
        return b != 0;
    }
    
    boolean c(final int n) {
        final d d = new d();
        boolean b = this.b(n);
        this.a(d);
        if (b && (0x1 & d.c >> n) != 0x1) {
            b = false;
        }
        return b;
    }
    
    public int cmdGet(final int n, final int n2, final byte[] array, final int n3) {
        return this.c.VendorCmdGet(32, n | n2 << 8, array, n3);
    }
    
    public int cmdSet(final int n, final int n2) {
        return this.c.VendorCmdSet(33, n | n2 << 8);
    }
    
    public int cmdSet(final int n, final int n2, final byte[] array, final int n3) {
        return this.c.VendorCmdSet(33, n | n2 << 8, array, n3);
    }
    
    boolean d(final int n) {
        return n != 0;
    }
    
    @Override
    public int init(final int[] array) {
        final b mChipStatus = this.b.mChipStatus;
        final d d = new d();
        final byte[] array2 = { 0 };
        final e e = new e();
        this.cmdSet(7, 0);
        this.cmdSet(6, 0);
        final int init = this.b.init();
        if (init != 0) {
            Log.e("GPIO_M", "FT4222_GPIO init - 1 NG ftStatus:" + init);
            return init;
        }
        if (mChipStatus.a == 2 || mChipStatus.a == 3) {
            return 1013;
        }
        this.a(d);
        byte c = d.c;
        array2[0] = d.d[0];
        for (int i = 0; i < 4; ++i) {
            if (array[i] == 1) {
                c = (byte)(0xF & (c | 1 << i));
            }
            else {
                c = (byte)(0xF & (c & (-1 ^ 1 << i)));
            }
        }
        e.c = array2[0];
        this.cmdSet(33, c);
        return 0;
    }
    
    public int newRead(final int n, final boolean[] array) {
        final int a = this.a(n);
        if (a != 0) {
            return a;
        }
        final int queueStatus = this.c.getQueueStatus();
        if (queueStatus > 0) {
            final byte[] array2 = new byte[queueStatus];
            this.c.read(array2, queueStatus);
            this.a(n, array2[queueStatus - 1], array);
            return queueStatus;
        }
        return -1;
    }
    
    public int newWrite(final int n, final boolean b) {
        final d d = new d();
        final int a = this.a(n);
        if (a != 0) {
            return a;
        }
        if (!this.c(n)) {
            return 1015;
        }
        this.a(d);
        if (b) {
            final byte[] d2 = d.d;
            d2[0] |= (byte)(1 << n);
        }
        else {
            final byte[] d3 = d.d;
            d3[0] &= (byte)(0xF & (-1 ^ 1 << n));
        }
        if (this.a) {
            final byte[] d4 = d.d;
            d4[0] |= 0x8;
        }
        else {
            final byte[] d5 = d.d;
            d5[0] &= 0x7;
        }
        final int write = this.c.write(d.d, 1);
        final boolean a2 = this.a;
        boolean a3 = false;
        if (!a2) {
            a3 = true;
        }
        this.a = a3;
        return write;
    }
    
    @Override
    public int read(final int n, final boolean[] array) {
        final d d = new d();
        int n2 = this.a(n);
        if (n2 == 0) {
            n2 = this.a(d);
            if (n2 == 0) {
                this.a(n, d.d[0], array);
                return 0;
            }
        }
        return n2;
    }
    
    @Override
    public int write(final int n, final boolean b) {
        final d d = new d();
        final int a = this.a(n);
        if (a != 0) {
            return a;
        }
        if (!this.c(n)) {
            return 1015;
        }
        this.a(d);
        if (b) {
            final byte[] d2 = d.d;
            d2[0] |= (byte)(1 << n);
        }
        else {
            final byte[] d3 = d.d;
            d3[0] &= (byte)(0xF & (-1 ^ 1 << n));
        }
        return this.c.write(d.d, 1);
    }
}
