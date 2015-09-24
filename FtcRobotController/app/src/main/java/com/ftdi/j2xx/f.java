package com.ftdi.j2xx;

class f extends k
{
    private static FT_Device d;
    
    f(final FT_Device d) {
        super(d);
        f.d = d;
    }
    
    @Override
    int a(final byte[] array) {
        if (array.length <= this.b()) {
            final int[] array2 = new int[64];
            for (short n = 0; n < 64; ++n) {
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
            if (array2[1] != 0 && array2[2] != 0 && this.a(array2, 63)) {
                return array.length;
            }
        }
        return 0;
    }
    
    @Override
    FT_EEPROM a() {
        final FT_EEPROM ft_EEPROM = new FT_EEPROM();
        final int[] array = new int[64];
        int n = 0;
        while (true) {
            Label_0104: {
                if (n < 64) {
                    break Label_0104;
                }
                try {
                    ft_EEPROM.VendorId = (short)array[1];
                    ft_EEPROM.ProductId = (short)array[2];
                    this.a(ft_EEPROM, array[4]);
                    ft_EEPROM.Manufacturer = this.a(10, array);
                    final int n2 = 10 + (1 + ft_EEPROM.Manufacturer.length());
                    ft_EEPROM.Product = this.a(n2, array);
                    ft_EEPROM.SerialNumber = this.a(n2 + (1 + ft_EEPROM.Product.length()), array);
                    return ft_EEPROM;
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
        final int[] array = new int[64];
        if (ft_EEPROM.getClass() != FT_EEPROM.class) {
            return 1;
        }
        short n = 0;
        while (true) {
            Label_0136: {
                if (n < 64) {
                    break Label_0136;
                }
                try {
                    array[1] = ft_EEPROM.VendorId;
                    array[2] = ft_EEPROM.ProductId;
                    array[3] = f.d.g.bcdDevice;
                    array[4] = this.a((Object)ft_EEPROM);
                    final int a = this.a(ft_EEPROM.Product, array, this.a(ft_EEPROM.Manufacturer, array, 10, 7, true), 8, true);
                    if (ft_EEPROM.SerNumEnable) {
                        this.a(ft_EEPROM.SerialNumber, array, a, 9, true);
                    }
                    if (array[1] == 0 || array[2] == 0) {
                        return 2;
                    }
                    if (this.a(array, 63)) {
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
        return 2 * (-1 + (63 - (1 + (((0xFF00 & this.a((short)8)) >> 8) / 2 + (((0xFF00 & this.a((short)7)) >> 8) / 2 + 10)))) - ((0xFF00 & this.a((short)9)) >> 8) / 2);
    }
}
