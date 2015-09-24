package com.ftdi.j2xx;

class g extends k
{
    g(final FT_Device ft_Device) throws D2xxManager.D2xxException {
        super(ft_Device);
        this.a((byte)15);
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
        final FT_EEPROM_232H ft_EEPROM_232H = new FT_EEPROM_232H();
        final int[] array = new int[this.b];
        final boolean c = this.c;
        short n = 0;
        if (c) {
            return ft_EEPROM_232H;
        }
        try {
            do {
                array[n] = this.a(n);
                ++n;
            } while (n < this.b);
            ft_EEPROM_232H.UART = false;
            switch (0xF & array[0]) {
                default: {
                    ft_EEPROM_232H.UART = true;
                    break;
                }
                case 0: {
                    ft_EEPROM_232H.UART = true;
                    break;
                }
                case 1: {
                    ft_EEPROM_232H.FIFO = true;
                    break;
                }
                case 2: {
                    ft_EEPROM_232H.FIFOTarget = true;
                    break;
                }
                case 4: {
                    ft_EEPROM_232H.FastSerial = true;
                    break;
                }
                case 8: {
                    ft_EEPROM_232H.FT1248 = true;
                    break;
                }
            }
            if ((0x10 & array[0]) > 0) {
                ft_EEPROM_232H.LoadVCP = true;
                ft_EEPROM_232H.LoadD2XX = false;
            }
            else {
                ft_EEPROM_232H.LoadVCP = false;
                ft_EEPROM_232H.LoadD2XX = true;
            }
            if ((0x100 & array[0]) > 0) {
                ft_EEPROM_232H.FT1248ClockPolarity = true;
            }
            else {
                ft_EEPROM_232H.FT1248ClockPolarity = false;
            }
            if ((0x200 & array[0]) > 0) {
                ft_EEPROM_232H.FT1248LSB = true;
            }
            else {
                ft_EEPROM_232H.FT1248LSB = false;
            }
            if ((0x400 & array[0]) > 0) {
                ft_EEPROM_232H.FT1248FlowControl = true;
            }
            else {
                ft_EEPROM_232H.FT1248FlowControl = false;
            }
            if ((0x8000 & array[0]) > 0) {
                ft_EEPROM_232H.PowerSaveEnable = true;
            }
            ft_EEPROM_232H.VendorId = (short)array[1];
            ft_EEPROM_232H.ProductId = (short)array[2];
            this.a(ft_EEPROM_232H, array[4]);
            this.a((Object)ft_EEPROM_232H, array[5]);
            switch (0x3 & array[6]) {
                case 0: {
                    ft_EEPROM_232H.AL_DriveCurrent = 0;
                    break;
                }
                case 1: {
                    ft_EEPROM_232H.AL_DriveCurrent = 1;
                    break;
                }
                case 2: {
                    ft_EEPROM_232H.AL_DriveCurrent = 2;
                    break;
                }
                case 3: {
                    ft_EEPROM_232H.AL_DriveCurrent = 3;
                    break;
                }
            }
            if ((0x4 & array[6]) > 0) {
                ft_EEPROM_232H.AL_SlowSlew = true;
            }
            else {
                ft_EEPROM_232H.AL_SlowSlew = false;
            }
            if ((0x8 & array[6]) > 0) {
                ft_EEPROM_232H.AL_SchmittInput = true;
            }
            else {
                ft_EEPROM_232H.AL_SchmittInput = false;
            }
            switch ((short)((0x300 & array[6]) >> 8)) {
                case 0: {
                    ft_EEPROM_232H.BL_DriveCurrent = 0;
                    break;
                }
                case 1: {
                    ft_EEPROM_232H.BL_DriveCurrent = 1;
                    break;
                }
                case 2: {
                    ft_EEPROM_232H.BL_DriveCurrent = 2;
                    break;
                }
                case 3: {
                    ft_EEPROM_232H.BL_DriveCurrent = 3;
                    break;
                }
            }
            if ((0x400 & array[6]) > 0) {
                ft_EEPROM_232H.BL_SlowSlew = true;
            }
            else {
                ft_EEPROM_232H.BL_SlowSlew = false;
            }
            if ((0x800 & array[6]) > 0) {
                ft_EEPROM_232H.BL_SchmittInput = true;
            }
            else {
                ft_EEPROM_232H.BL_SchmittInput = false;
            }
            ft_EEPROM_232H.CBus0 = (byte)(0xF & array[12] >> 0);
            ft_EEPROM_232H.CBus1 = (byte)(0xF & array[12] >> 4);
            ft_EEPROM_232H.CBus2 = (byte)(0xF & array[12] >> 8);
            ft_EEPROM_232H.CBus3 = (byte)(0xF & array[12] >> 12);
            ft_EEPROM_232H.CBus4 = (byte)(0xF & array[13] >> 0);
            ft_EEPROM_232H.CBus5 = (byte)(0xF & array[13] >> 4);
            ft_EEPROM_232H.CBus6 = (byte)(0xF & array[13] >> 8);
            ft_EEPROM_232H.CBus7 = (byte)(0xF & array[13] >> 12);
            ft_EEPROM_232H.CBus8 = (byte)(0xF & array[14] >> 0);
            ft_EEPROM_232H.CBus9 = (byte)(0xF & array[14] >> 4);
            ft_EEPROM_232H.Manufacturer = this.a((0xFF & array[7]) / 2, array);
            ft_EEPROM_232H.Product = this.a((0xFF & array[8]) / 2, array);
            ft_EEPROM_232H.SerialNumber = this.a((0xFF & array[9]) / 2, array);
            return ft_EEPROM_232H;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    @Override
    short a(final FT_EEPROM ft_EEPROM) {
        final int[] array = new int[this.b];
        if (ft_EEPROM.getClass() != FT_EEPROM_232H.class) {
            return 1;
        }
        while (true) {
            final FT_EEPROM_232H ft_EEPROM_232H = (FT_EEPROM_232H)ft_EEPROM;
            while (true) {
                Label_0534: {
                    try {
                        if (ft_EEPROM_232H.FIFO) {
                            array[0] |= 0x1;
                        }
                        else {
                            if (!ft_EEPROM_232H.FIFOTarget) {
                                break Label_0534;
                            }
                            array[0] |= 0x2;
                        }
                        if (ft_EEPROM_232H.FT1248) {
                            array[0] |= 0x8;
                        }
                        if (ft_EEPROM_232H.LoadVCP) {
                            array[0] |= 0x10;
                        }
                        if (ft_EEPROM_232H.FT1248ClockPolarity) {
                            array[0] |= 0x100;
                        }
                        if (ft_EEPROM_232H.FT1248LSB) {
                            array[0] |= 0x200;
                        }
                        if (ft_EEPROM_232H.FT1248FlowControl) {
                            array[0] |= 0x400;
                        }
                        if (ft_EEPROM_232H.PowerSaveEnable) {
                            array[0] |= 0x8000;
                        }
                        array[1] = ft_EEPROM_232H.VendorId;
                        array[2] = ft_EEPROM_232H.ProductId;
                        array[3] = 2304;
                        array[4] = this.a((Object)ft_EEPROM);
                        array[5] = this.b(ft_EEPROM);
                        int al_DriveCurrent = ft_EEPROM_232H.AL_DriveCurrent;
                        if (al_DriveCurrent == -1) {
                            al_DriveCurrent = 0;
                        }
                        array[6] |= al_DriveCurrent;
                        if (ft_EEPROM_232H.AL_SlowSlew) {
                            array[6] |= 0x4;
                        }
                        if (ft_EEPROM_232H.AL_SchmittInput) {
                            array[6] |= 0x8;
                        }
                        int bl_DriveCurrent = ft_EEPROM_232H.BL_DriveCurrent;
                        if (bl_DriveCurrent == -1) {
                            bl_DriveCurrent = 0;
                        }
                        array[6] |= (short)(bl_DriveCurrent << 8);
                        if (ft_EEPROM_232H.BL_SlowSlew) {
                            array[6] |= 0x400;
                        }
                        if (ft_EEPROM_232H.BL_SchmittInput) {
                            array[6] |= 0x800;
                        }
                        final int a = this.a(ft_EEPROM_232H.Product, array, this.a(ft_EEPROM_232H.Manufacturer, array, 80, 7, false), 8, false);
                        if (ft_EEPROM_232H.SerNumEnable) {
                            this.a(ft_EEPROM_232H.SerialNumber, array, a, 9, false);
                        }
                        array[10] = 0;
                        array[12] = (array[11] = 0);
                        array[12] = (ft_EEPROM_232H.CBus3 << 12 | (ft_EEPROM_232H.CBus2 << 8 | (ft_EEPROM_232H.CBus0 | ft_EEPROM_232H.CBus1 << 4)));
                        array[13] = 0;
                        array[13] = (ft_EEPROM_232H.CBus7 << 12 | (ft_EEPROM_232H.CBus6 << 8 | (ft_EEPROM_232H.CBus4 | ft_EEPROM_232H.CBus5 << 4)));
                        array[14] = 0;
                        array[14] = (ft_EEPROM_232H.CBus8 | ft_EEPROM_232H.CBus9 << 4);
                        array[15] = this.a;
                        array[69] = 72;
                        if (this.a == 70) {
                            return 1;
                        }
                        break;
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                        return 0;
                    }
                }
                if (ft_EEPROM_232H.FastSerial) {
                    array[0] |= 0x4;
                    continue;
                }
                continue;
            }
        }
        if (array[1] == 0 || array[2] == 0) {
            return 2;
        }
        if (this.a(array, -1 + this.b)) {
            return 0;
        }
        return 1;
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
        return 2 * (-1 + (this.b - (1 + (a & 0xFF) / 2)) - (1 + ((a & 0xFF00) >> 8) / 2));
    }
}
