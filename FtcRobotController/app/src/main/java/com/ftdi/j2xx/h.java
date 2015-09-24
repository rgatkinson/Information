package com.ftdi.j2xx;

class h extends k
{
    private static FT_Device d;
    
    h(final FT_Device d) {
        super(d);
        h.d = d;
    }
    
    @Override
    int a(final byte[] array) {
        if (array.length <= this.b()) {
            final int[] array2 = new int[80];
            for (short n = 0; n < 80; ++n) {
                array2[n] = this.a(n);
            }
            int n2 = (short)(0xFFFF & (short)(-1 + (63 - this.b() / 2)));
            short n5;
            for (int i = 0; i < array.length; i += 2, n2 = n5) {
                int n3;
                if (i + 1 < array.length) {
                    n3 = (0xFF & array[i + 1]);
                }
                else {
                    n3 = 0;
                }
                final int n4 = n3 << 8 | (0xFF & array[i]);
                n5 = (short)(n2 + 1);
                array2[n2] = n4;
            }
            if (array2[1] != 0 && array2[2] != 0) {
                final byte latencyTimer = h.d.getLatencyTimer();
                h.d.setLatencyTimer((byte)119);
                final boolean a = this.a(array2, 63);
                h.d.setLatencyTimer(latencyTimer);
                if (a) {
                    return array.length;
                }
            }
        }
        return 0;
    }
    
    @Override
    FT_EEPROM a() {
        int n = 0;
        final FT_EEPROM_232R ft_EEPROM_232R = new FT_EEPROM_232R();
        final int[] array = new int[80];
        while (true) {
            Label_0376: {
                if (n < 80) {
                    break Label_0376;
                }
                try {
                    if ((0x4 & array[0]) == 0x4) {
                        ft_EEPROM_232R.HighIO = true;
                    }
                    else {
                        ft_EEPROM_232R.HighIO = false;
                    }
                    if ((0x8 & array[0]) == 0x8) {
                        ft_EEPROM_232R.LoadVCP = true;
                    }
                    else {
                        ft_EEPROM_232R.LoadVCP = false;
                    }
                    if ((0x2 & array[0]) == 0x2) {
                        ft_EEPROM_232R.ExternalOscillator = true;
                    }
                    else {
                        ft_EEPROM_232R.ExternalOscillator = false;
                    }
                    ft_EEPROM_232R.VendorId = (short)array[1];
                    ft_EEPROM_232R.ProductId = (short)array[2];
                    this.a(ft_EEPROM_232R, array[4]);
                    this.a((Object)ft_EEPROM_232R, array[5]);
                    if ((0x100 & array[5]) == 0x100) {
                        ft_EEPROM_232R.InvertTXD = true;
                    }
                    else {
                        ft_EEPROM_232R.InvertTXD = false;
                    }
                    if ((0x200 & array[5]) == 0x200) {
                        ft_EEPROM_232R.InvertRXD = true;
                    }
                    else {
                        ft_EEPROM_232R.InvertRXD = false;
                    }
                    if ((0x400 & array[5]) == 0x400) {
                        ft_EEPROM_232R.InvertRTS = true;
                    }
                    else {
                        ft_EEPROM_232R.InvertRTS = false;
                    }
                    if ((0x800 & array[5]) == 0x800) {
                        ft_EEPROM_232R.InvertCTS = true;
                    }
                    else {
                        ft_EEPROM_232R.InvertCTS = false;
                    }
                    if ((0x1000 & array[5]) == 0x1000) {
                        ft_EEPROM_232R.InvertDTR = true;
                    }
                    else {
                        ft_EEPROM_232R.InvertDTR = false;
                    }
                    if ((0x2000 & array[5]) == 0x2000) {
                        ft_EEPROM_232R.InvertDSR = true;
                    }
                    else {
                        ft_EEPROM_232R.InvertDSR = false;
                    }
                    if ((0x4000 & array[5]) == 0x4000) {
                        ft_EEPROM_232R.InvertDCD = true;
                    }
                    else {
                        ft_EEPROM_232R.InvertDCD = false;
                    }
                    if ((0x8000 & array[5]) == 0x8000) {
                        ft_EEPROM_232R.InvertRI = true;
                    }
                    else {
                        ft_EEPROM_232R.InvertRI = false;
                    }
                    final int n2 = array[10];
                    ft_EEPROM_232R.CBus0 = (byte)(n2 & 0xF);
                    ft_EEPROM_232R.CBus1 = (byte)((n2 & 0xF0) >> 4);
                    ft_EEPROM_232R.CBus2 = (byte)((n2 & 0xF00) >> 8);
                    ft_EEPROM_232R.CBus3 = (byte)((n2 & 0xF000) >> 12);
                    ft_EEPROM_232R.CBus4 = (byte)(0xFF & array[11]);
                    ft_EEPROM_232R.Manufacturer = this.a((-128 + (0xFF & array[7])) / 2, array);
                    ft_EEPROM_232R.Product = this.a((-128 + (0xFF & array[8])) / 2, array);
                    ft_EEPROM_232R.SerialNumber = this.a((-128 + (0xFF & array[9])) / 2, array);
                    return ft_EEPROM_232R;
                    array[n] = this.a((short)n);
                    ++n;
                }
                catch (Exception ex) {
                    return null;
                }
            }
        }
    }
    
    @Override
    short a(final FT_EEPROM ft_EEPROM) {
        if (ft_EEPROM.getClass() != FT_EEPROM_232R.class) {
            return 1;
        }
        final int[] array = new int[80];
        final FT_EEPROM_232R ft_EEPROM_232R = (FT_EEPROM_232R)ft_EEPROM;
        short n = 0;
        while (true) {
            Label_0404: {
                if (n < 80) {
                    break Label_0404;
                }
                try {
                    int n2 = 0x0 | (0xFF00 & array[0]);
                    if (ft_EEPROM_232R.HighIO) {
                        n2 |= 0x4;
                    }
                    if (ft_EEPROM_232R.LoadVCP) {
                        n2 |= 0x8;
                    }
                    int n3;
                    if (ft_EEPROM_232R.ExternalOscillator) {
                        n3 = (n2 | 0x2);
                    }
                    else {
                        n3 = (n2 & 0xFFFD);
                    }
                    array[0] = n3;
                    array[1] = ft_EEPROM_232R.VendorId;
                    array[2] = ft_EEPROM_232R.ProductId;
                    array[3] = 1536;
                    array[4] = this.a((Object)ft_EEPROM);
                    int b = this.b(ft_EEPROM);
                    if (ft_EEPROM_232R.InvertTXD) {
                        b |= 0x100;
                    }
                    if (ft_EEPROM_232R.InvertRXD) {
                        b |= 0x200;
                    }
                    if (ft_EEPROM_232R.InvertRTS) {
                        b |= 0x400;
                    }
                    if (ft_EEPROM_232R.InvertCTS) {
                        b |= 0x800;
                    }
                    if (ft_EEPROM_232R.InvertDTR) {
                        b |= 0x1000;
                    }
                    if (ft_EEPROM_232R.InvertDSR) {
                        b |= 0x2000;
                    }
                    if (ft_EEPROM_232R.InvertDCD) {
                        b |= 0x4000;
                    }
                    if (ft_EEPROM_232R.InvertRI) {
                        b |= 0x8000;
                    }
                    array[5] = b;
                    array[10] = (ft_EEPROM_232R.CBus3 << 12 | (ft_EEPROM_232R.CBus2 << 8 | (ft_EEPROM_232R.CBus0 | ft_EEPROM_232R.CBus1 << 4)));
                    array[11] = ft_EEPROM_232R.CBus4;
                    final int a = this.a(ft_EEPROM_232R.Product, array, this.a(ft_EEPROM_232R.Manufacturer, array, 12, 7, true), 8, true);
                    if (ft_EEPROM_232R.SerNumEnable) {
                        this.a(ft_EEPROM_232R.SerialNumber, array, a, 9, true);
                    }
                    if (array[1] == 0 || array[2] == 0) {
                        return 2;
                    }
                    final byte latencyTimer = h.d.getLatencyTimer();
                    h.d.setLatencyTimer((byte)119);
                    final boolean a2 = this.a(array, 63);
                    h.d.setLatencyTimer(latencyTimer);
                    if (a2) {
                        return 0;
                    }
                    return 1;
                    array[n] = this.a(n);
                    ++n;
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    return 0;
                }
            }
        }
    }
    
    @Override
    boolean a(final short n, final short n2) {
        final int n3 = n2 & 0xFFFF;
        final int n4 = n & 0xFFFF;
        if (n >= 1024) {
            return false;
        }
        final byte latencyTimer = h.d.getLatencyTimer();
        h.d.setLatencyTimer((byte)119);
        final int controlTransfer = h.d.c().controlTransfer(64, 145, n3, n4, (byte[])null, 0, 0);
        boolean b = false;
        if (controlTransfer == 0) {
            b = true;
        }
        h.d.setLatencyTimer(latencyTimer);
        return b;
    }
    
    @Override
    byte[] a(final int n) {
        byte[] array = new byte[n];
        if (n == 0 || n > this.b()) {
            array = null;
        }
        else {
            short n2 = (short)(0xFFFF & (short)(-1 + (63 - this.b() / 2)));
            short n3;
            for (int i = 0; i < n; i += 2, n2 = n3) {
                n3 = (short)(n2 + 1);
                final int a = this.a(n2);
                if (i + 1 < array.length) {
                    array[i + 1] = (byte)(a & 0xFF);
                }
                array[i] = (byte)((a & 0xFF00) >> 8);
            }
        }
        return array;
    }
    
    @Override
    int b() {
        return 2 * (-1 + (63 - (1 + (((0xFF00 & this.a((short)8)) >> 8) / 2 + (((0xFF00 & this.a((short)7)) >> 8) / 2 + 12))) - ((0xFF00 & this.a((short)9)) >> 8) / 2));
    }
}
