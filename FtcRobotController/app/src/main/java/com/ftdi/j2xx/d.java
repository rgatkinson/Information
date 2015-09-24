package com.ftdi.j2xx;

class d extends k
{
    d(final FT_Device ft_Device) throws D2xxManager.D2xxException {
        super(ft_Device);
        this.a((byte)10);
    }
    
    @Override
    int a(final byte[] array) {
        if (array.length <= this.b()) {
            final int[] array2 = new int[this.b];
            for (short n = 0; n < this.b; ++n) {
                array2[n] = this.a(n);
            }
            int n2 = (short)(-1 + (-1 + (this.b - this.b() / 2)));
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
            if (array2[1] != 0 && array2[2] != 0 && this.a(array2, -1 + this.b)) {
                return array.length;
            }
        }
        return 0;
    }
    
    @Override
    FT_EEPROM a() {
        int i = 0;
        final FT_EEPROM_2232D ft_EEPROM_2232D = new FT_EEPROM_2232D();
        final int[] array = new int[this.b];
        try {
            while (i < this.b) {
                array[i] = this.a((short)i);
                ++i;
            }
            switch ((short)(0x7 & array[0])) {
                case 0: {
                    ft_EEPROM_2232D.A_UART = true;
                    break;
                }
                case 1: {
                    ft_EEPROM_2232D.A_FIFO = true;
                    break;
                }
                case 2: {
                    ft_EEPROM_2232D.A_FIFOTarget = true;
                    break;
                }
                case 4: {
                    ft_EEPROM_2232D.A_FastSerial = true;
                    break;
                }
            }
            if ((short)((0x8 & array[0]) >> 3) == 1) {
                ft_EEPROM_2232D.A_LoadVCP = true;
            }
            else {
                ft_EEPROM_2232D.A_HighIO = true;
            }
            if ((short)((0x10 & array[0]) >> 4) == 1) {
                ft_EEPROM_2232D.A_HighIO = true;
            }
            switch ((short)((0x700 & array[0]) >> 8)) {
                case 0: {
                    ft_EEPROM_2232D.B_UART = true;
                    break;
                }
                case 1: {
                    ft_EEPROM_2232D.B_FIFO = true;
                    break;
                }
                case 2: {
                    ft_EEPROM_2232D.B_FIFOTarget = true;
                    break;
                }
                case 4: {
                    ft_EEPROM_2232D.B_FastSerial = true;
                    break;
                }
            }
            if ((short)((0x800 & array[0]) >> 11) == 1) {
                ft_EEPROM_2232D.B_LoadVCP = true;
            }
            else {
                ft_EEPROM_2232D.B_LoadD2XX = true;
            }
            if ((short)((0x1000 & array[0]) >> 12) == 1) {
                ft_EEPROM_2232D.B_HighIO = true;
            }
            ft_EEPROM_2232D.VendorId = (short)array[1];
            ft_EEPROM_2232D.ProductId = (short)array[2];
            this.a(ft_EEPROM_2232D, array[4]);
            final int n = 0xFF & array[7];
            if (this.a == 70) {
                ft_EEPROM_2232D.Manufacturer = this.a((n - 128) / 2, array);
                ft_EEPROM_2232D.Product = this.a((-128 + (0xFF & array[8])) / 2, array);
                ft_EEPROM_2232D.SerialNumber = this.a((-128 + (0xFF & array[9])) / 2, array);
                return ft_EEPROM_2232D;
            }
            ft_EEPROM_2232D.Manufacturer = this.a(n / 2, array);
            ft_EEPROM_2232D.Product = this.a((0xFF & array[8]) / 2, array);
            ft_EEPROM_2232D.SerialNumber = this.a((0xFF & array[9]) / 2, array);
            return ft_EEPROM_2232D;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    @Override
    short a(final FT_EEPROM ft_EEPROM) {
        final int[] array = new int[this.b];
        if (ft_EEPROM.getClass() != FT_EEPROM_2232D.class) {
            return 1;
        }
        FT_EEPROM_2232D ft_EEPROM_2232D;
        int n;
        boolean b;
        int a;
        Label_0074_Outer:Label_0164_Outer:
        while (true) {
            ft_EEPROM_2232D = (FT_EEPROM_2232D)ft_EEPROM;
            while (true) {
            Label_0352:
                while (true) {
                Label_0295:
                    while (true) {
                        Label_0284: {
                            try {
                                array[0] = 0;
                                if (ft_EEPROM_2232D.A_FIFO) {
                                    array[0] |= 0x1;
                                }
                                else {
                                    if (!ft_EEPROM_2232D.A_FIFOTarget) {
                                        break Label_0284;
                                    }
                                    array[0] |= 0x2;
                                }
                                if (ft_EEPROM_2232D.A_HighIO) {
                                    array[0] |= 0x10;
                                }
                                if (!ft_EEPROM_2232D.A_LoadVCP) {
                                    break Label_0295;
                                }
                                array[0] |= 0x8;
                                if (ft_EEPROM_2232D.B_HighIO) {
                                    array[0] |= 0x1000;
                                }
                                if (ft_EEPROM_2232D.B_LoadVCP) {
                                    array[0] |= 0x800;
                                }
                                array[1] = ft_EEPROM_2232D.VendorId;
                                array[2] = ft_EEPROM_2232D.ProductId;
                                array[3] = 1280;
                                array[4] = this.a((Object)ft_EEPROM);
                                array[4] = this.b(ft_EEPROM);
                                n = 75;
                                if (this.a != 70) {
                                    break Label_0352;
                                }
                                n = 11;
                                b = true;
                                a = this.a(ft_EEPROM_2232D.Product, array, this.a(ft_EEPROM_2232D.Manufacturer, array, n, 7, b), 8, b);
                                if (ft_EEPROM_2232D.SerNumEnable) {
                                    this.a(ft_EEPROM_2232D.SerialNumber, array, a, 9, b);
                                }
                                array[10] = this.a;
                                if (array[1] == 0 || array[2] == 0) {
                                    return 2;
                                }
                                if (this.a(array, -1 + this.b)) {
                                    return 0;
                                }
                                return 1;
                            }
                            catch (Exception ex) {
                                ex.printStackTrace();
                                return 0;
                            }
                        }
                        array[0] |= 0x4;
                        continue Label_0074_Outer;
                    }
                    if (ft_EEPROM_2232D.B_FIFO) {
                        array[0] |= 0x100;
                        continue Label_0164_Outer;
                    }
                    if (ft_EEPROM_2232D.B_FIFOTarget) {
                        array[0] |= 0x200;
                        continue Label_0164_Outer;
                    }
                    array[0] |= 0x400;
                    continue Label_0164_Outer;
                }
                b = false;
                continue;
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
            short n2 = (short)(-1 + (-1 + (this.b - this.b() / 2)));
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
        final int a = this.a((short)9);
        return 2 * (-1 + (-1 + this.b) - ((a & 0xFF) + ((a & 0xFF00) >> 8) / 2));
    }
}
