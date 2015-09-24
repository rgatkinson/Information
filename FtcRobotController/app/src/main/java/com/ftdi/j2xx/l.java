package com.ftdi.j2xx;

class l extends k
{
    private static FT_Device d;
    
    l(final FT_Device d) {
        super(d);
        l.d = d;
        this.b = 128;
        this.a = 1;
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
            if (array2[1] != 0 && array2[2] != 0 && this.b(array2, -1 + this.b)) {
                return array.length;
            }
        }
        return 0;
    }
    
    @Override
    FT_EEPROM a() {
        short n = 0;
        final FT_EEPROM_X_Series ft_EEPROM_X_Series = new FT_EEPROM_X_Series();
        final int[] array = new int[this.b];
        try {
            while (n < this.b) {
                array[n] = this.a(n);
                ++n;
            }
            if ((0x1 & array[0]) > 0) {
                ft_EEPROM_X_Series.BCDEnable = true;
            }
            else {
                ft_EEPROM_X_Series.BCDEnable = false;
            }
            if ((0x2 & array[0]) > 0) {
                ft_EEPROM_X_Series.BCDForceCBusPWREN = true;
            }
            else {
                ft_EEPROM_X_Series.BCDForceCBusPWREN = false;
            }
            if ((0x4 & array[0]) > 0) {
                ft_EEPROM_X_Series.BCDDisableSleep = true;
            }
            else {
                ft_EEPROM_X_Series.BCDDisableSleep = false;
            }
            if ((0x8 & array[0]) > 0) {
                ft_EEPROM_X_Series.RS485EchoSuppress = true;
            }
            else {
                ft_EEPROM_X_Series.RS485EchoSuppress = false;
            }
            if ((0x40 & array[0]) > 0) {
                ft_EEPROM_X_Series.PowerSaveEnable = true;
            }
            else {
                ft_EEPROM_X_Series.PowerSaveEnable = false;
            }
            if ((0x80 & array[0]) > 0) {
                ft_EEPROM_X_Series.A_LoadVCP = true;
                ft_EEPROM_X_Series.A_LoadD2XX = false;
            }
            else {
                ft_EEPROM_X_Series.A_LoadVCP = false;
                ft_EEPROM_X_Series.A_LoadD2XX = true;
            }
            ft_EEPROM_X_Series.VendorId = (short)array[1];
            ft_EEPROM_X_Series.ProductId = (short)array[2];
            this.a(ft_EEPROM_X_Series, array[4]);
            this.a((Object)ft_EEPROM_X_Series, array[5]);
            if ((0x10 & array[5]) > 0) {
                ft_EEPROM_X_Series.FT1248ClockPolarity = true;
            }
            else {
                ft_EEPROM_X_Series.FT1248ClockPolarity = false;
            }
            if ((0x20 & array[5]) > 0) {
                ft_EEPROM_X_Series.FT1248LSB = true;
            }
            else {
                ft_EEPROM_X_Series.FT1248LSB = false;
            }
            if ((0x40 & array[5]) > 0) {
                ft_EEPROM_X_Series.FT1248FlowControl = true;
            }
            else {
                ft_EEPROM_X_Series.FT1248FlowControl = false;
            }
            if ((0x80 & array[5]) > 0) {
                ft_EEPROM_X_Series.I2CDisableSchmitt = true;
            }
            else {
                ft_EEPROM_X_Series.I2CDisableSchmitt = false;
            }
            if ((0x100 & array[5]) == 0x100) {
                ft_EEPROM_X_Series.InvertTXD = true;
            }
            else {
                ft_EEPROM_X_Series.InvertTXD = false;
            }
            if ((0x200 & array[5]) == 0x200) {
                ft_EEPROM_X_Series.InvertRXD = true;
            }
            else {
                ft_EEPROM_X_Series.InvertRXD = false;
            }
            if ((0x400 & array[5]) == 0x400) {
                ft_EEPROM_X_Series.InvertRTS = true;
            }
            else {
                ft_EEPROM_X_Series.InvertRTS = false;
            }
            if ((0x800 & array[5]) == 0x800) {
                ft_EEPROM_X_Series.InvertCTS = true;
            }
            else {
                ft_EEPROM_X_Series.InvertCTS = false;
            }
            if ((0x1000 & array[5]) == 0x1000) {
                ft_EEPROM_X_Series.InvertDTR = true;
            }
            else {
                ft_EEPROM_X_Series.InvertDTR = false;
            }
            if ((0x2000 & array[5]) == 0x2000) {
                ft_EEPROM_X_Series.InvertDSR = true;
            }
            else {
                ft_EEPROM_X_Series.InvertDSR = false;
            }
            if ((0x4000 & array[5]) == 0x4000) {
                ft_EEPROM_X_Series.InvertDCD = true;
            }
            else {
                ft_EEPROM_X_Series.InvertDCD = false;
            }
            if ((0x8000 & array[5]) == 0x8000) {
                ft_EEPROM_X_Series.InvertRI = true;
            }
            else {
                ft_EEPROM_X_Series.InvertRI = false;
            }
            switch ((short)(0x3 & array[6])) {
                case 0: {
                    ft_EEPROM_X_Series.AD_DriveCurrent = 0;
                    break;
                }
                case 1: {
                    ft_EEPROM_X_Series.AD_DriveCurrent = 1;
                    break;
                }
                case 2: {
                    ft_EEPROM_X_Series.AD_DriveCurrent = 2;
                    break;
                }
                case 3: {
                    ft_EEPROM_X_Series.AD_DriveCurrent = 3;
                    break;
                }
            }
            if ((short)(0x4 & array[6]) == 4) {
                ft_EEPROM_X_Series.AD_SlowSlew = true;
            }
            else {
                ft_EEPROM_X_Series.AD_SlowSlew = false;
            }
            if ((short)(0x8 & array[6]) == 8) {
                ft_EEPROM_X_Series.AD_SchmittInput = true;
            }
            else {
                ft_EEPROM_X_Series.AD_SchmittInput = false;
            }
            switch ((short)((0x30 & array[6]) >> 4)) {
                case 0: {
                    ft_EEPROM_X_Series.AC_DriveCurrent = 0;
                    break;
                }
                case 1: {
                    ft_EEPROM_X_Series.AC_DriveCurrent = 1;
                    break;
                }
                case 2: {
                    ft_EEPROM_X_Series.AC_DriveCurrent = 2;
                    break;
                }
                case 3: {
                    ft_EEPROM_X_Series.AC_DriveCurrent = 3;
                    break;
                }
            }
            if ((short)(0x40 & array[6]) == 64) {
                ft_EEPROM_X_Series.AC_SlowSlew = true;
            }
            else {
                ft_EEPROM_X_Series.AC_SlowSlew = false;
            }
            if ((short)(0x80 & array[6]) == 128) {
                ft_EEPROM_X_Series.AC_SchmittInput = true;
            }
            else {
                ft_EEPROM_X_Series.AC_SchmittInput = false;
            }
            ft_EEPROM_X_Series.I2CSlaveAddress = array[10];
            ft_EEPROM_X_Series.I2CDeviceID = array[11];
            ft_EEPROM_X_Series.I2CDeviceID |= (0xFF & array[12]) << 16;
            ft_EEPROM_X_Series.CBus0 = (byte)(0xFF & array[13]);
            ft_EEPROM_X_Series.CBus1 = (byte)(0xFF & array[13] >> 8);
            ft_EEPROM_X_Series.CBus2 = (byte)(0xFF & array[14]);
            ft_EEPROM_X_Series.CBus3 = (byte)(0xFF & array[14] >> 8);
            ft_EEPROM_X_Series.CBus4 = (byte)(0xFF & array[15]);
            ft_EEPROM_X_Series.CBus5 = (byte)(0xFF & array[15] >> 8);
            ft_EEPROM_X_Series.CBus6 = (byte)(0xFF & array[16]);
            this.a = (short)(array[73] >> 8);
            ft_EEPROM_X_Series.Manufacturer = this.a((0xFF & array[7]) / 2, array);
            ft_EEPROM_X_Series.Product = this.a((0xFF & array[8]) / 2, array);
            ft_EEPROM_X_Series.SerialNumber = this.a((0xFF & array[9]) / 2, array);
            return ft_EEPROM_X_Series;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    @Override
    short a(final FT_EEPROM ft_EEPROM) {
        final int[] array = new int[this.b];
        short n;
        if (ft_EEPROM.getClass() != FT_EEPROM_X_Series.class) {
            n = 1;
        }
        else {
            final FT_EEPROM_X_Series ft_EEPROM_X_Series = (FT_EEPROM_X_Series)ft_EEPROM;
            short n2 = 0;
            do {
                array[n2] = this.a(n2);
                ++n2;
            } while (n2 < this.b);
            while (true) {
                while (true) {
                    Label_0915: {
                        while (true) {
                            byte cBus0 = 0;
                            Label_0908: {
                                while (true) {
                                    byte cBus2 = 0;
                                    Label_0901: {
                                        while (true) {
                                            byte cBus4;
                                            try {
                                                array[0] = 0;
                                                if (ft_EEPROM_X_Series.BCDEnable) {
                                                    array[0] |= 0x1;
                                                }
                                                if (ft_EEPROM_X_Series.BCDForceCBusPWREN) {
                                                    array[0] |= 0x2;
                                                }
                                                if (ft_EEPROM_X_Series.BCDDisableSleep) {
                                                    array[0] |= 0x4;
                                                }
                                                if (ft_EEPROM_X_Series.RS485EchoSuppress) {
                                                    array[0] |= 0x8;
                                                }
                                                if (ft_EEPROM_X_Series.A_LoadVCP) {
                                                    array[0] |= 0x80;
                                                }
                                                if (ft_EEPROM_X_Series.PowerSaveEnable) {
                                                    if (ft_EEPROM_X_Series.CBus0 != 17) {
                                                        break Label_0915;
                                                    }
                                                    int n3 = 1;
                                                    if (ft_EEPROM_X_Series.CBus1 == 17) {
                                                        n3 = 1;
                                                    }
                                                    if (ft_EEPROM_X_Series.CBus2 == 17) {
                                                        n3 = 1;
                                                    }
                                                    if (ft_EEPROM_X_Series.CBus3 == 17) {
                                                        n3 = 1;
                                                    }
                                                    if (ft_EEPROM_X_Series.CBus4 == 17) {
                                                        n3 = 1;
                                                    }
                                                    if (ft_EEPROM_X_Series.CBus5 == 17) {
                                                        n3 = 1;
                                                    }
                                                    if (ft_EEPROM_X_Series.CBus6 == 17) {
                                                        n3 = 1;
                                                    }
                                                    if (n3 == 0) {
                                                        return 1;
                                                    }
                                                    array[0] |= 0x40;
                                                }
                                                array[1] = ft_EEPROM_X_Series.VendorId;
                                                array[2] = ft_EEPROM_X_Series.ProductId;
                                                array[3] = 4096;
                                                array[4] = this.a((Object)ft_EEPROM);
                                                array[5] = this.b(ft_EEPROM);
                                                if (ft_EEPROM_X_Series.FT1248ClockPolarity) {
                                                    array[5] |= 0x10;
                                                }
                                                if (ft_EEPROM_X_Series.FT1248LSB) {
                                                    array[5] |= 0x20;
                                                }
                                                if (ft_EEPROM_X_Series.FT1248FlowControl) {
                                                    array[5] |= 0x40;
                                                }
                                                if (ft_EEPROM_X_Series.I2CDisableSchmitt) {
                                                    array[5] |= 0x80;
                                                }
                                                if (ft_EEPROM_X_Series.InvertTXD) {
                                                    array[5] |= 0x100;
                                                }
                                                if (ft_EEPROM_X_Series.InvertRXD) {
                                                    array[5] |= 0x200;
                                                }
                                                if (ft_EEPROM_X_Series.InvertRTS) {
                                                    array[5] |= 0x400;
                                                }
                                                if (ft_EEPROM_X_Series.InvertCTS) {
                                                    array[5] |= 0x800;
                                                }
                                                if (ft_EEPROM_X_Series.InvertDTR) {
                                                    array[5] |= 0x1000;
                                                }
                                                if (ft_EEPROM_X_Series.InvertDSR) {
                                                    array[5] |= 0x2000;
                                                }
                                                if (ft_EEPROM_X_Series.InvertDCD) {
                                                    array[5] |= 0x4000;
                                                }
                                                if (ft_EEPROM_X_Series.InvertRI) {
                                                    array[5] |= 0x8000;
                                                }
                                                array[6] = 0;
                                                int ad_DriveCurrent = ft_EEPROM_X_Series.AD_DriveCurrent;
                                                if (ad_DriveCurrent == -1) {
                                                    ad_DriveCurrent = 0;
                                                }
                                                array[6] |= ad_DriveCurrent;
                                                if (ft_EEPROM_X_Series.AD_SlowSlew) {
                                                    array[6] |= 0x4;
                                                }
                                                if (ft_EEPROM_X_Series.AD_SchmittInput) {
                                                    array[6] |= 0x8;
                                                }
                                                int ac_DriveCurrent = ft_EEPROM_X_Series.AC_DriveCurrent;
                                                if (ac_DriveCurrent == -1) {
                                                    ac_DriveCurrent = 0;
                                                }
                                                array[6] |= (short)(ac_DriveCurrent << 4);
                                                if (ft_EEPROM_X_Series.AC_SlowSlew) {
                                                    array[6] |= 0x40;
                                                }
                                                if (ft_EEPROM_X_Series.AC_SchmittInput) {
                                                    array[6] |= 0x80;
                                                }
                                                final int a = this.a(ft_EEPROM_X_Series.Product, array, this.a(ft_EEPROM_X_Series.Manufacturer, array, 80, 7, false), 8, false);
                                                if (ft_EEPROM_X_Series.SerNumEnable) {
                                                    this.a(ft_EEPROM_X_Series.SerialNumber, array, a, 9, false);
                                                }
                                                array[10] = ft_EEPROM_X_Series.I2CSlaveAddress;
                                                array[11] = (0xFFFF & ft_EEPROM_X_Series.I2CDeviceID);
                                                array[12] = ft_EEPROM_X_Series.I2CDeviceID >> 16;
                                                cBus0 = ft_EEPROM_X_Series.CBus0;
                                                if (cBus0 != -1) {
                                                    break Label_0908;
                                                }
                                                final byte b = 0;
                                                int cBus = ft_EEPROM_X_Series.CBus1;
                                                if (cBus == -1) {
                                                    cBus = 0;
                                                }
                                                array[13] = (short)(b | cBus << 8);
                                                cBus2 = ft_EEPROM_X_Series.CBus2;
                                                if (cBus2 != -1) {
                                                    break Label_0901;
                                                }
                                                final byte b2 = 0;
                                                int cBus3 = ft_EEPROM_X_Series.CBus3;
                                                if (cBus3 == -1) {
                                                    cBus3 = 0;
                                                }
                                                array[14] = (short)(b2 | cBus3 << 8);
                                                cBus4 = ft_EEPROM_X_Series.CBus4;
                                                if (cBus4 == -1) {
                                                    final byte b3 = 0;
                                                    int cBus5 = ft_EEPROM_X_Series.CBus5;
                                                    if (cBus5 == -1) {
                                                        cBus5 = 0;
                                                    }
                                                    array[15] = (short)(b3 | cBus5 << 8);
                                                    int cBus6 = ft_EEPROM_X_Series.CBus6;
                                                    if (cBus6 == -1) {
                                                        cBus6 = 0;
                                                    }
                                                    array[16] = (short)cBus6;
                                                    if (array[1] == 0 || array[2] == 0) {
                                                        return 2;
                                                    }
                                                    final boolean b4 = this.b(array, -1 + this.b);
                                                    n = 0;
                                                    if (!b4) {
                                                        return 1;
                                                    }
                                                    break;
                                                }
                                            }
                                            catch (Exception ex) {
                                                ex.printStackTrace();
                                                return 0;
                                            }
                                            final byte b3 = cBus4;
                                            continue;
                                        }
                                    }
                                    final byte b2 = cBus2;
                                    continue;
                                }
                            }
                            final byte b = cBus0;
                            continue;
                        }
                    }
                    int n3 = 0;
                    continue;
                }
            }
        }
        return n;
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
    
    boolean b(final int[] array, final int n) {
        int n2 = 43690;
        int n3 = 0;
        do {
            final int n4 = 0xFFFF & array[n3];
            this.a((short)n3, (short)n4);
            final int n5 = 0xFFFF & (n2 ^ n4);
            final int n6 = 0xFFFF & n5 << 1;
            boolean b;
            if ((n5 & 0x8000) > 0) {
                b = true;
            }
            else {
                b = false;
            }
            n2 = (0xFFFF & ((b ? 1 : 0) | n6));
            if (++n3 == 18) {
                n3 = 64;
            }
        } while (n3 != n);
        this.a((short)n, (short)n2);
        return true;
    }
}
