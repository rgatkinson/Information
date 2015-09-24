package com.ftdi.j2xx;

import android.util.Log;

class k
{
    short a;
    int b;
    boolean c;
    private FT_Device d;
    
    k(final FT_Device d) {
        this.d = d;
    }
    
    int a(final byte b) throws D2xxManager.D2xxException {
        final short n = (short)(b & -1);
        final int[] array = new int[3];
        final short n2 = (short)this.a(n);
        if (n2 != 65535) {
            switch (n2) {
                default: {
                    return 0;
                }
                case 70: {
                    this.a = 70;
                    this.b = 64;
                    this.c = false;
                    return 64;
                }
                case 86: {
                    this.a = 86;
                    this.b = 128;
                    this.c = false;
                    return 128;
                }
                case 102: {
                    this.a = 102;
                    this.b = 128;
                    this.c = false;
                    return 256;
                }
                case 82: {
                    this.a = 82;
                    this.b = 1024;
                    this.c = false;
                    return 1024;
                }
            }
        }
        else {
            final boolean a = this.a((short)192, (short)192);
            array[0] = this.a((short)192);
            array[1] = this.a((short)64);
            array[2] = this.a((short)0);
            if (!a) {
                this.a = 255;
                return this.b = 0;
            }
            this.c = true;
            if ((0xFF & this.a((short)0)) == 0xC0) {
                this.c();
                this.a = 70;
                return this.b = 64;
            }
            if ((0xFF & this.a((short)64)) == 0xC0) {
                this.c();
                this.a = 86;
                return this.b = 128;
            }
            if ((0xFF & this.a((short)192)) == 0xC0) {
                this.c();
                this.a = 102;
                this.b = 128;
                return 256;
            }
            this.c();
            return 0;
        }
    }
    
    int a(final Object o) {
        final FT_EEPROM ft_EEPROM = (FT_EEPROM)o;
        int n = 128;
        if (ft_EEPROM.RemoteWakeup) {
            n = 160;
        }
        if (ft_EEPROM.SelfPowered) {
            n |= 0x40;
        }
        return n | ft_EEPROM.MaxPower / 2 << 8;
    }
    
    int a(final String s, final int[] array, final int n, final int n2, final boolean b) {
        int n3 = 0;
        final int n4 = 2 + 2 * s.length();
        array[n2] = (n4 << 8 | n * 2);
        if (b) {
            array[n2] += 128;
        }
        final char[] charArray = s.toCharArray();
        int n5 = n + 1;
        array[n] = (n4 | 0x300);
        final int n6 = (n4 - 2) / 2;
        int n7;
        while (true) {
            n7 = n5 + 1;
            array[n5] = charArray[n3];
            if (++n3 >= n6) {
                break;
            }
            n5 = n7;
        }
        return n7;
    }
    
    int a(final short n) {
        final byte[] array = new byte[2];
        if (n >= 1024) {
            return -1;
        }
        this.d.c().controlTransfer(-64, 144, 0, (int)n, array, 2, 0);
        return (0xFF & array[1]) << 8 | (0xFF & array[0]);
    }
    
    int a(final byte[] array) {
        return 0;
    }
    
    FT_EEPROM a() {
        return null;
    }
    
    String a(final int n, final int[] array) {
        String string = "";
        for (int n2 = -1 + (0xFF & array[n]) / 2, i = n + 1; i < n2 + i; ++i) {
            string = String.valueOf(string) + (char)array[i];
        }
        return string;
    }
    
    short a(final FT_EEPROM ft_EEPROM) {
        return 1;
    }
    
    void a(final FT_EEPROM ft_EEPROM, final int n) {
        ft_EEPROM.MaxPower = (short)(2 * (byte)(n >> 8));
        final byte b = (byte)n;
        if ((b & 0x40) == 0x40 && (b & 0x80) == 0x80) {
            ft_EEPROM.SelfPowered = true;
        }
        else {
            ft_EEPROM.SelfPowered = false;
        }
        if ((b & 0x20) == 0x20) {
            ft_EEPROM.RemoteWakeup = true;
            return;
        }
        ft_EEPROM.RemoteWakeup = false;
    }
    
    void a(final Object o, final int n) {
        final FT_EEPROM ft_EEPROM = (FT_EEPROM)o;
        if ((n & 0x4) > 0) {
            ft_EEPROM.PullDownEnable = true;
        }
        else {
            ft_EEPROM.PullDownEnable = false;
        }
        if ((n & 0x8) > 0) {
            ft_EEPROM.SerNumEnable = true;
            return;
        }
        ft_EEPROM.SerNumEnable = false;
    }
    
    boolean a(final short n, final short n2) {
        final int n3 = n2 & 0xFFFF;
        final int n4 = n & 0xFFFF;
        return n < 1024 && this.d.c().controlTransfer(64, 145, n3, n4, (byte[])null, 0, 0) == 0;
    }
    
    boolean a(final int[] array, final int n) {
        int n2 = 43690;
        int i = 0;
        while (i < n) {
            this.a((short)i, (short)array[i]);
            final int n3 = 0xFFFF & (n2 ^ array[i]);
            n2 = (0xFFFF & ((short)(0xFFFF & n3 << 1) | (short)(0xFFFF & n3 >> 15)));
            ++i;
            Log.d("FT_EE_Ctrl", "Entered WriteWord Checksum : " + n2);
        }
        this.a((short)n, (short)n2);
        return true;
    }
    
    byte[] a(final int n) {
        return null;
    }
    
    int b() {
        return 0;
    }
    
    int b(final Object o) {
        final FT_EEPROM ft_EEPROM = (FT_EEPROM)o;
        int n;
        if (ft_EEPROM.PullDownEnable) {
            n = 4;
        }
        else {
            n = 0;
        }
        if (ft_EEPROM.SerNumEnable) {
            return n | 0x8;
        }
        return n & 0xF7;
    }
    
    int c() {
        return this.d.c().controlTransfer(64, 146, 0, 0, (byte[])null, 0, 0);
    }
}
