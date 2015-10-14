//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ftdi.j2xx;

import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.FT_EEPROM;
import com.ftdi.j2xx.FT_EEPROM_2232H;
import com.ftdi.j2xx.k;
import com.ftdi.j2xx.D2xxManager.D2xxException;

class c extends k {
    c(FT_Device var1) throws D2xxException {
        super(var1);
        this.a((byte)12);
    }

    short a(FT_EEPROM var1) {
        int[] var2 = new int[this.b];
        if(var1.getClass() != FT_EEPROM_2232H.class) {
            return (short)1;
        } else {
            FT_EEPROM_2232H var3 = (FT_EEPROM_2232H)var1;

            try {
                if(!var3.A_UART) {
                    if(var3.A_FIFO) {
                        var2[0] |= 1;
                    } else if(var3.A_FIFOTarget) {
                        var2[0] |= 2;
                    } else {
                        var2[0] |= 4;
                    }
                }

                if(var3.A_LoadVCP) {
                    var2[0] |= 8;
                }

                if(!var3.B_UART) {
                    if(var3.B_FIFO) {
                        var2[0] |= 256;
                    } else if(var3.B_FIFOTarget) {
                        var2[0] |= 512;
                    } else {
                        var2[0] |= 1024;
                    }
                }

                if(var3.B_LoadVCP) {
                    var2[0] |= 2048;
                }

                if(var3.PowerSaveEnable) {
                    var2[0] |= '耀';
                }

                var2[1] = var3.VendorId;
                var2[2] = var3.ProductId;
                var2[3] = 1792;
                var2[4] = this.a((Object)var1);
                var2[5] = this.b(var1);
                var2[6] = 0;
                byte var4 = var3.AL_DriveCurrent;
                if(var4 == -1) {
                    var4 = 0;
                }

                var2[6] |= var4;
                if(var3.AL_SlowSlew) {
                    var2[6] |= 4;
                }

                if(var3.AL_SchmittInput) {
                    var2[6] |= 8;
                }

                byte var5 = var3.AH_DriveCurrent;
                if(var5 == -1) {
                    var5 = 0;
                }

                short var12 = (short)(var5 << 4);
                var2[6] |= var12;
                if(var3.AH_SlowSlew) {
                    var2[6] |= 64;
                }

                if(var3.AH_SchmittInput) {
                    var2[6] |= 128;
                }

                byte var6 = var3.BL_DriveCurrent;
                if(var6 == -1) {
                    var6 = 0;
                }

                short var13 = (short)(var6 << 8);
                var2[6] |= var13;
                if(var3.BL_SlowSlew) {
                    var2[6] |= 1024;
                }

                if(var3.BL_SchmittInput) {
                    var2[6] |= 2048;
                }

                byte var7 = var3.BH_DriveCurrent;
                short var14 = (short)(var7 << 12);
                var2[6] |= var14;
                if(var3.BH_SlowSlew) {
                    var2[6] |= 16384;
                }

                if(var3.BH_SchmittInput) {
                    var2[6] |= '耀';
                }

                boolean var8 = false;
                byte var9 = 77;
                if(this.a == 70) {
                    var9 = 13;
                    var8 = true;
                }

                int var15 = this.a(var3.Manufacturer, var2, var9, 7, var8);
                var15 = this.a(var3.Product, var2, var15, 8, var8);
                if(var3.SerNumEnable) {
                    this.a(var3.SerialNumber, var2, var15, 9, var8);
                }

                switch(var3.TPRDRV) {
                case 0:
                    var2[11] = 0;
                    break;
                case 1:
                    var2[11] = 8;
                    break;
                case 2:
                    var2[11] = 16;
                    break;
                case 3:
                    var2[11] = 24;
                    break;
                default:
                    var2[11] = 0;
                }

                var2[12] = this.a;
                if(var2[1] != 0 && var2[2] != 0) {
                    boolean var10 = false;
                    var10 = this.a(var2, this.b - 1);
                    return (short)(var10?0:1);
                } else {
                    return (short)2;
                }
            } catch (Exception var11) {
                var11.printStackTrace();
                return (short)0;
            }
        }
    }

    FT_EEPROM a() {
        FT_EEPROM_2232H var1 = new FT_EEPROM_2232H();
        int[] var2 = new int[this.b];
        if(this.c) {
            return var1;
        } else {
            try {
                short var4;
                for(var4 = 0; var4 < this.b; ++var4) {
                    var2[var4] = this.a(var4);
                }

                boolean var3 = false;
                int var24 = var2[0];
                var4 = (short)(var24 & 7);
                switch(var4) {
                case 0:
                    var1.A_UART = true;
                    break;
                case 1:
                    var1.A_FIFO = true;
                    break;
                case 2:
                    var1.A_FIFOTarget = true;
                    break;
                case 3:
                default:
                    var1.A_UART = true;
                    break;
                case 4:
                    var1.A_FastSerial = true;
                }

                short var5 = (short)((var24 & 8) >> 3);
                if(var5 == 1) {
                    var1.A_LoadVCP = true;
                    var1.A_LoadD2XX = false;
                } else {
                    var1.A_LoadVCP = false;
                    var1.A_LoadD2XX = true;
                }

                short var6 = (short)((var24 & 1792) >> 8);
                switch(var6) {
                case 0:
                    var1.B_UART = true;
                    break;
                case 1:
                    var1.B_FIFO = true;
                    break;
                case 2:
                    var1.B_FIFOTarget = true;
                    break;
                case 3:
                default:
                    var1.B_UART = true;
                    break;
                case 4:
                    var1.B_FastSerial = true;
                }

                short var7 = (short)((var24 & 2048) >> 11);
                if(var7 == 1) {
                    var1.B_LoadVCP = true;
                    var1.B_LoadD2XX = false;
                } else {
                    var1.B_LoadVCP = false;
                    var1.B_LoadD2XX = true;
                }

                short var8 = (short)((var24 & '耀') >> 15);
                if(var8 == 1) {
                    var1.PowerSaveEnable = true;
                } else {
                    var1.PowerSaveEnable = false;
                }

                var1.VendorId = (short)var2[1];
                var1.ProductId = (short)var2[2];
                this.a(var1, var2[4]);
                this.a(var1, var2[5]);
                short var9 = (short)(var2[6] & 3);
                switch(var9) {
                case 0:
                    var1.AL_DriveCurrent = 0;
                    break;
                case 1:
                    var1.AL_DriveCurrent = 1;
                    break;
                case 2:
                    var1.AL_DriveCurrent = 2;
                    break;
                case 3:
                    var1.AL_DriveCurrent = 3;
                }

                short var10 = (short)(var2[6] & 4);
                if(var10 == 4) {
                    var1.AL_SlowSlew = true;
                } else {
                    var1.AL_SlowSlew = false;
                }

                short var11 = (short)(var2[6] & 8);
                if(var11 == 8) {
                    var1.AL_SchmittInput = true;
                } else {
                    var1.AL_SchmittInput = false;
                }

                short var12 = (short)((var2[6] & 48) >> 4);
                switch(var12) {
                case 0:
                    var1.AH_DriveCurrent = 0;
                    break;
                case 1:
                    var1.AH_DriveCurrent = 1;
                    break;
                case 2:
                    var1.AH_DriveCurrent = 2;
                    break;
                case 3:
                    var1.AH_DriveCurrent = 3;
                }

                short var13 = (short)(var2[6] & 64);
                if(var13 == 64) {
                    var1.AH_SlowSlew = true;
                } else {
                    var1.AH_SlowSlew = false;
                }

                short var14 = (short)(var2[6] & 128);
                if(var14 == 128) {
                    var1.AH_SchmittInput = true;
                } else {
                    var1.AH_SchmittInput = false;
                }

                short var15 = (short)((var2[6] & 768) >> 8);
                switch(var15) {
                case 0:
                    var1.BL_DriveCurrent = 0;
                    break;
                case 1:
                    var1.BL_DriveCurrent = 1;
                    break;
                case 2:
                    var1.BL_DriveCurrent = 2;
                    break;
                case 3:
                    var1.BL_DriveCurrent = 3;
                }

                short var16 = (short)(var2[6] & 1024);
                if(var16 == 1024) {
                    var1.BL_SlowSlew = true;
                } else {
                    var1.BL_SlowSlew = false;
                }

                short var17 = (short)(var2[6] & 2048);
                if(var14 == 2048) {
                    var1.BL_SchmittInput = true;
                } else {
                    var1.BL_SchmittInput = false;
                }

                short var18 = (short)((var2[6] & 12288) >> 12);
                switch(var18) {
                case 0:
                    var1.BH_DriveCurrent = 0;
                    break;
                case 1:
                    var1.BH_DriveCurrent = 1;
                    break;
                case 2:
                    var1.BH_DriveCurrent = 2;
                    break;
                case 3:
                    var1.BH_DriveCurrent = 3;
                }

                short var19 = (short)(var2[6] & 16384);
                if(var19 == 16384) {
                    var1.BH_SlowSlew = true;
                } else {
                    var1.BH_SlowSlew = false;
                }

                short var20 = (short)(var2[6] & '耀');
                if(var20 == '耀') {
                    var1.BH_SchmittInput = true;
                } else {
                    var1.BH_SchmittInput = false;
                }

                short var21 = (short)((var2[11] & 24) >> 3);
                if(var21 < 4) {
                    var1.TPRDRV = var21;
                } else {
                    var1.TPRDRV = 0;
                }

                int var22 = var2[7] & 255;
                if(this.a == 70) {
                    var22 -= 128;
                    var22 /= 2;
                    var1.Manufacturer = this.a(var22, var2);
                    var22 = var2[8] & 255;
                    var22 -= 128;
                    var22 /= 2;
                    var1.Product = this.a(var22, var2);
                    var22 = var2[9] & 255;
                    var22 -= 128;
                    var22 /= 2;
                    var1.SerialNumber = this.a(var22, var2);
                } else {
                    var22 /= 2;
                    var1.Manufacturer = this.a(var22, var2);
                    var22 = var2[8] & 255;
                    var22 /= 2;
                    var1.Product = this.a(var22, var2);
                    var22 = var2[9] & 255;
                    var22 /= 2;
                    var1.SerialNumber = this.a(var22, var2);
                }

                return var1;
            } catch (Exception var23) {
                return null;
            }
        }
    }

    int b() {
        int var1 = this.a((short)9);
        int var2 = var1 & 255;
        var2 /= 2;
        int var3 = (var1 & '\uff00') >> 8;
        var2 += var3 / 2;
        ++var2;
        return (this.b - 1 - 1 - var2) * 2;
    }

    int a(byte[] var1) {
        boolean var2 = false;
        boolean var3 = false;
        if(var1.length > this.b()) {
            return 0;
        } else {
            int[] var4 = new int[this.b];

            for(short var5 = 0; var5 < this.b; ++var5) {
                var4[var5] = this.a(var5);
            }

            short var7 = (short)(this.b - this.b() / 2 - 1 - 1);

            for(int var8 = 0; var8 < var1.length; var8 += 2) {
                int var6;
                if(var8 + 1 < var1.length) {
                    var6 = var1[var8 + 1] & 255;
                } else {
                    var6 = 0;
                }

                var6 <<= 8;
                var6 |= var1[var8] & 255;
                var4[var7++] = var6;
            }

            if(var4[1] != 0 && var4[2] != 0) {
                boolean var9 = false;
                var9 = this.a(var4, this.b - 1);
                if(!var9) {
                    return 0;
                } else {
                    return var1.length;
                }
            } else {
                return 0;
            }
        }
    }

    byte[] a(int var1) {
        boolean var2 = false;
        boolean var3 = false;
        boolean var4 = false;
        byte[] var5 = new byte[var1];
        if(var1 != 0 && var1 <= this.b()) {
            short var6 = (short)(this.b - this.b() / 2 - 1 - 1);

            for(int var7 = 0; var7 < var1; var7 += 2) {
                int var10 = this.a(var6++);
                if(var7 + 1 < var5.length) {
                    byte var8 = (byte)(var10 & 255);
                    var5[var7 + 1] = var8;
                } else {
                    var3 = false;
                }

                byte var9 = (byte)((var10 & '\uff00') >> 8);
                var5[var7] = var9;
            }

            return var5;
        } else {
            return null;
        }
    }
}
