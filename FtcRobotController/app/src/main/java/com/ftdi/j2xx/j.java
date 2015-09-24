package com.ftdi.j2xx;

class j extends k
{
    j(final FT_Device ft_Device) throws D2xxManager.D2xxException {
        super(ft_Device);
        this.a((byte)12);
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
        final FT_EEPROM_4232H ft_EEPROM_4232H = new FT_EEPROM_4232H();
        final int[] array = new int[this.b];
        final boolean c = this.c;
        short n = 0;
        if (c) {
            return ft_EEPROM_4232H;
        }
        try {
            do {
                array[n] = this.a(n);
                ++n;
            } while (n < this.b);
            if ((short)((0x8 & array[0]) >> 3) == 1) {
                ft_EEPROM_4232H.AL_LoadVCP = true;
                ft_EEPROM_4232H.AL_LoadD2XX = false;
            }
            else {
                ft_EEPROM_4232H.AL_LoadVCP = false;
                ft_EEPROM_4232H.AL_LoadD2XX = true;
            }
            if ((short)((0x80 & array[0]) >> 7) == 1) {
                ft_EEPROM_4232H.BL_LoadVCP = true;
                ft_EEPROM_4232H.BL_LoadD2XX = false;
            }
            else {
                ft_EEPROM_4232H.BL_LoadVCP = false;
                ft_EEPROM_4232H.BL_LoadD2XX = true;
            }
            if ((short)((0x800 & array[0]) >> 11) == 1) {
                ft_EEPROM_4232H.AH_LoadVCP = true;
                ft_EEPROM_4232H.AH_LoadD2XX = false;
            }
            else {
                ft_EEPROM_4232H.AH_LoadVCP = false;
                ft_EEPROM_4232H.AH_LoadD2XX = true;
            }
            if ((short)((0x8000 & array[0]) >> 15) == 1) {
                ft_EEPROM_4232H.BH_LoadVCP = true;
                ft_EEPROM_4232H.BH_LoadD2XX = false;
            }
            else {
                ft_EEPROM_4232H.BH_LoadVCP = false;
                ft_EEPROM_4232H.BH_LoadD2XX = true;
            }
            ft_EEPROM_4232H.VendorId = (short)array[1];
            ft_EEPROM_4232H.ProductId = (short)array[2];
            this.a(ft_EEPROM_4232H, array[4]);
            this.a((Object)ft_EEPROM_4232H, array[5]);
            if ((0x1000 & array[5]) == 0x1000) {
                ft_EEPROM_4232H.AL_LoadRI_RS485 = true;
            }
            if ((0x2000 & array[5]) == 0x2000) {
                ft_EEPROM_4232H.AH_LoadRI_RS485 = true;
            }
            if ((0x4000 & array[5]) == 0x4000) {
                ft_EEPROM_4232H.AH_LoadRI_RS485 = true;
            }
            if ((0x8000 & array[5]) == 0x8000) {
                ft_EEPROM_4232H.AH_LoadRI_RS485 = true;
            }
            switch ((short)(0x3 & array[6])) {
                case 0: {
                    ft_EEPROM_4232H.AL_DriveCurrent = 0;
                    break;
                }
                case 1: {
                    ft_EEPROM_4232H.AL_DriveCurrent = 1;
                    break;
                }
                case 2: {
                    ft_EEPROM_4232H.AL_DriveCurrent = 2;
                    break;
                }
                case 3: {
                    ft_EEPROM_4232H.AL_DriveCurrent = 3;
                    break;
                }
            }
            if ((short)(0x4 & array[6]) == 4) {
                ft_EEPROM_4232H.AL_SlowSlew = true;
            }
            else {
                ft_EEPROM_4232H.AL_SlowSlew = false;
            }
            if ((short)(0x8 & array[6]) == 8) {
                ft_EEPROM_4232H.AL_SchmittInput = true;
            }
            else {
                ft_EEPROM_4232H.AL_SchmittInput = false;
            }
            switch ((short)((0x30 & array[6]) >> 4)) {
                case 0: {
                    ft_EEPROM_4232H.AH_DriveCurrent = 0;
                    break;
                }
                case 1: {
                    ft_EEPROM_4232H.AH_DriveCurrent = 1;
                    break;
                }
                case 2: {
                    ft_EEPROM_4232H.AH_DriveCurrent = 2;
                    break;
                }
                case 3: {
                    ft_EEPROM_4232H.AH_DriveCurrent = 3;
                    break;
                }
            }
            if ((short)(0x40 & array[6]) == 64) {
                ft_EEPROM_4232H.AH_SlowSlew = true;
            }
            else {
                ft_EEPROM_4232H.AH_SlowSlew = false;
            }
            if ((short)(0x80 & array[6]) == 128) {
                ft_EEPROM_4232H.AH_SchmittInput = true;
            }
            else {
                ft_EEPROM_4232H.AH_SchmittInput = false;
            }
            switch ((short)((0x300 & array[6]) >> 8)) {
                case 0: {
                    ft_EEPROM_4232H.BL_DriveCurrent = 0;
                    break;
                }
                case 1: {
                    ft_EEPROM_4232H.BL_DriveCurrent = 1;
                    break;
                }
                case 2: {
                    ft_EEPROM_4232H.BL_DriveCurrent = 2;
                    break;
                }
                case 3: {
                    ft_EEPROM_4232H.BL_DriveCurrent = 3;
                    break;
                }
            }
            if ((short)(0x400 & array[6]) == 1024) {
                ft_EEPROM_4232H.BL_SlowSlew = true;
            }
            else {
                ft_EEPROM_4232H.BL_SlowSlew = false;
            }
            if ((short)(0x800 & array[6]) == 2048) {
                ft_EEPROM_4232H.BL_SchmittInput = true;
            }
            else {
                ft_EEPROM_4232H.BL_SchmittInput = false;
            }
            switch ((short)((0x3000 & array[6]) >> 12)) {
                case 0: {
                    ft_EEPROM_4232H.BH_DriveCurrent = 0;
                    break;
                }
                case 1: {
                    ft_EEPROM_4232H.BH_DriveCurrent = 1;
                    break;
                }
                case 2: {
                    ft_EEPROM_4232H.BH_DriveCurrent = 2;
                    break;
                }
                case 3: {
                    ft_EEPROM_4232H.BH_DriveCurrent = 3;
                    break;
                }
            }
            if ((short)(0x4000 & array[6]) == 16384) {
                ft_EEPROM_4232H.BH_SlowSlew = true;
            }
            else {
                ft_EEPROM_4232H.BH_SlowSlew = false;
            }
            if ((short)(0x8000 & array[6]) == 32768) {
                ft_EEPROM_4232H.BH_SchmittInput = true;
            }
            else {
                ft_EEPROM_4232H.BH_SchmittInput = false;
            }
            final short tprdrv = (short)((0x18 & array[11]) >> 3);
            if (tprdrv < 4) {
                ft_EEPROM_4232H.TPRDRV = tprdrv;
            }
            else {
                ft_EEPROM_4232H.TPRDRV = 0;
            }
            final int n2 = 0xFF & array[7];
            if (this.a == 70) {
                ft_EEPROM_4232H.Manufacturer = this.a((n2 - 128) / 2, array);
                ft_EEPROM_4232H.Product = this.a((-128 + (0xFF & array[8])) / 2, array);
                ft_EEPROM_4232H.SerialNumber = this.a((-128 + (0xFF & array[9])) / 2, array);
                return ft_EEPROM_4232H;
            }
            ft_EEPROM_4232H.Manufacturer = this.a(n2 / 2, array);
            ft_EEPROM_4232H.Product = this.a((0xFF & array[8]) / 2, array);
            ft_EEPROM_4232H.SerialNumber = this.a((0xFF & array[9]) / 2, array);
            return ft_EEPROM_4232H;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    @Override
    short a(final FT_EEPROM ft_EEPROM) {
        final int[] array = new int[this.b];
        if (ft_EEPROM.getClass() != FT_EEPROM_4232H.class) {
            return 1;
        }
        while (true) {
            final FT_EEPROM_4232H ft_EEPROM_4232H = (FT_EEPROM_4232H)ft_EEPROM;
            while (true) {
                Label_0653: {
                    while (true) {
                        Label_0640: {
                            Label_0631: {
                                Label_0622: {
                                    try {
                                        array[0] = 0;
                                        if (ft_EEPROM_4232H.AL_LoadVCP) {
                                            array[0] |= 0x8;
                                        }
                                        if (ft_EEPROM_4232H.BL_LoadVCP) {
                                            array[0] |= 0x80;
                                        }
                                        if (ft_EEPROM_4232H.AH_LoadVCP) {
                                            array[0] |= 0x800;
                                        }
                                        if (ft_EEPROM_4232H.BH_LoadVCP) {
                                            array[0] |= 0x8000;
                                        }
                                        array[1] = ft_EEPROM_4232H.VendorId;
                                        array[2] = ft_EEPROM_4232H.ProductId;
                                        array[3] = 2048;
                                        array[4] = this.a((Object)ft_EEPROM);
                                        array[5] = this.b(ft_EEPROM);
                                        if (ft_EEPROM_4232H.AL_LoadRI_RS485) {
                                            array[5] = (short)(0x1000 | array[5]);
                                        }
                                        if (ft_EEPROM_4232H.AH_LoadRI_RS485) {
                                            array[5] = (short)(0x2000 | array[5]);
                                        }
                                        if (ft_EEPROM_4232H.BL_LoadRI_RS485) {
                                            array[5] = (short)(0x4000 | array[5]);
                                        }
                                        if (ft_EEPROM_4232H.BH_LoadRI_RS485) {
                                            array[5] = (short)(0x8000 | array[5]);
                                        }
                                        array[6] = 0;
                                        int al_DriveCurrent = ft_EEPROM_4232H.AL_DriveCurrent;
                                        if (al_DriveCurrent == -1) {
                                            al_DriveCurrent = 0;
                                        }
                                        array[6] |= al_DriveCurrent;
                                        if (ft_EEPROM_4232H.AL_SlowSlew) {
                                            array[6] |= 0x4;
                                        }
                                        if (ft_EEPROM_4232H.AL_SchmittInput) {
                                            array[6] |= 0x8;
                                        }
                                        int ah_DriveCurrent = ft_EEPROM_4232H.AH_DriveCurrent;
                                        if (ah_DriveCurrent == -1) {
                                            ah_DriveCurrent = 0;
                                        }
                                        array[6] |= (short)(ah_DriveCurrent << 4);
                                        if (ft_EEPROM_4232H.AH_SlowSlew) {
                                            array[6] |= 0x40;
                                        }
                                        if (ft_EEPROM_4232H.AH_SchmittInput) {
                                            array[6] |= 0x80;
                                        }
                                        int bl_DriveCurrent = ft_EEPROM_4232H.BL_DriveCurrent;
                                        if (bl_DriveCurrent == -1) {
                                            bl_DriveCurrent = 0;
                                        }
                                        array[6] |= (short)(bl_DriveCurrent << 8);
                                        if (ft_EEPROM_4232H.BL_SlowSlew) {
                                            array[6] |= 0x400;
                                        }
                                        if (ft_EEPROM_4232H.BL_SchmittInput) {
                                            array[6] |= 0x800;
                                        }
                                        array[6] |= (short)(ft_EEPROM_4232H.BH_DriveCurrent << 12);
                                        if (ft_EEPROM_4232H.BH_SlowSlew) {
                                            array[6] |= 0x4000;
                                        }
                                        if (ft_EEPROM_4232H.BH_SchmittInput) {
                                            array[6] |= 0x8000;
                                        }
                                        int n = 77;
                                        if (this.a != 70) {
                                            break Label_0653;
                                        }
                                        n = 13;
                                        final boolean b = true;
                                        final int a = this.a(ft_EEPROM_4232H.Product, array, this.a(ft_EEPROM_4232H.Manufacturer, array, n, 7, b), 8, b);
                                        if (ft_EEPROM_4232H.SerNumEnable) {
                                            this.a(ft_EEPROM_4232H.SerialNumber, array, a, 9, b);
                                        }
                                        switch (ft_EEPROM_4232H.TPRDRV) {
                                            default: {
                                                array[11] = 0;
                                                break;
                                            }
                                            case 0: {
                                                array[11] = 0;
                                                break;
                                            }
                                            case 1: {
                                                break Label_0622;
                                            }
                                            case 2: {
                                                break Label_0631;
                                            }
                                            case 3: {
                                                break Label_0640;
                                            }
                                        }
                                        array[12] = this.a;
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
                                array[11] = 8;
                                continue;
                            }
                            array[11] = 16;
                            continue;
                        }
                        array[11] = 24;
                        continue;
                    }
                }
                final boolean b = false;
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
        return 2 * (-1 + (-1 + this.b) - (1 + ((a & 0xFF) / 2 + ((a & 0xFF00) >> 8) / 2)));
    }
}
