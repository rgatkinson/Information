//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ftdi.j2xx;

import com.ftdi.j2xx.D2xxManager.D2xxException;

class FT_EE_4232H_Ctrl extends FT_EE_Ctrl
    {
    FT_EE_4232H_Ctrl(FT_Device var1) throws D2xxException {
        super(var1);
        this.getEepromSize((byte)12);
    }

    short programEeprom(FT_EEPROM var1) {
        int[] var2 = new int[this.mEepromSize];
        if(var1.getClass() != FT_EEPROM_4232H.class) {
            return (short)1;
        } else {
            FT_EEPROM_4232H var3 = (FT_EEPROM_4232H)var1;

            try {
                var2[0] = 0;
                if(var3.AL_LoadVCP) {
                    var2[0] |= 8;
                }

                if(var3.BL_LoadVCP) {
                    var2[0] |= 128;
                }

                if(var3.AH_LoadVCP) {
                    var2[0] |= 2048;
                }

                if(var3.BH_LoadVCP) {
                    var2[0] |= '耀';
                }

                var2[1] = var3.VendorId;
                var2[2] = var3.ProductId;
                var2[3] = 2048;
                var2[4] = this.setUSBConfig((Object)var1);
                var2[5] = this.b(var1);
                if(var3.AL_LoadRI_RS485) {
                    var2[5] = (short)(var2[5] | 4096);
                }

                if(var3.AH_LoadRI_RS485) {
                    var2[5] = (short)(var2[5] | 8192);
                }

                if(var3.BL_LoadRI_RS485) {
                    var2[5] = (short)(var2[5] | 16384);
                }

                if(var3.BH_LoadRI_RS485) {
                    var2[5] = (short)(var2[5] | '耀');
                }

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
                if(this.mEepromType == 70) {
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

                var2[12] = this.mEepromType;
                if(var2[1] != 0 && var2[2] != 0) {
                    boolean var10 = false;
                    var10 = this.a(var2, this.mEepromSize - 1);
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

    FT_EEPROM readEeprom() {
        FT_EEPROM_4232H var1 = new FT_EEPROM_4232H();
        int[] var2 = new int[this.mEepromSize];
        if(this.mEepromBlank) {
            return var1;
        } else {
            try {
                short var3;
                for(var3 = 0; var3 < this.mEepromSize; ++var3) {
                    var2[var3] = this.readWord(var3);
                }

                var3 = (short)((var2[0] & 8) >> 3);
                if(var3 == 1) {
                    var1.AL_LoadVCP = true;
                    var1.AL_LoadD2XX = false;
                } else {
                    var1.AL_LoadVCP = false;
                    var1.AL_LoadD2XX = true;
                }

                short var4 = (short)((var2[0] & 128) >> 7);
                if(var4 == 1) {
                    var1.BL_LoadVCP = true;
                    var1.BL_LoadD2XX = false;
                } else {
                    var1.BL_LoadVCP = false;
                    var1.BL_LoadD2XX = true;
                }

                short var5 = (short)((var2[0] & 2048) >> 11);
                if(var5 == 1) {
                    var1.AH_LoadVCP = true;
                    var1.AH_LoadD2XX = false;
                } else {
                    var1.AH_LoadVCP = false;
                    var1.AH_LoadD2XX = true;
                }

                short var6 = (short)((var2[0] & '耀') >> 15);
                if(var6 == 1) {
                    var1.BH_LoadVCP = true;
                    var1.BH_LoadD2XX = false;
                } else {
                    var1.BH_LoadVCP = false;
                    var1.BH_LoadD2XX = true;
                }

                var1.VendorId = (short)var2[1];
                var1.ProductId = (short)var2[2];
                this.getUSBConfig(var1, var2[4]);
                this.getUSBConfig(var1, var2[5]);
                if((var2[5] & 4096) == 4096) {
                    var1.AL_LoadRI_RS485 = true;
                }

                if((var2[5] & 8192) == 8192) {
                    var1.AH_LoadRI_RS485 = true;
                }

                if((var2[5] & 16384) == 16384) {
                    var1.AH_LoadRI_RS485 = true;
                }

                if((var2[5] & '耀') == '耀') {
                    var1.AH_LoadRI_RS485 = true;
                }

                short var7 = (short)(var2[6] & 3);
                switch(var7) {
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

                short var8 = (short)(var2[6] & 4);
                if(var8 == 4) {
                    var1.AL_SlowSlew = true;
                } else {
                    var1.AL_SlowSlew = false;
                }

                short var9 = (short)(var2[6] & 8);
                if(var9 == 8) {
                    var1.AL_SchmittInput = true;
                } else {
                    var1.AL_SchmittInput = false;
                }

                short var10 = (short)((var2[6] & 48) >> 4);
                switch(var10) {
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

                short var11 = (short)(var2[6] & 64);
                if(var11 == 64) {
                    var1.AH_SlowSlew = true;
                } else {
                    var1.AH_SlowSlew = false;
                }

                short var12 = (short)(var2[6] & 128);
                if(var12 == 128) {
                    var1.AH_SchmittInput = true;
                } else {
                    var1.AH_SchmittInput = false;
                }

                short var13 = (short)((var2[6] & 768) >> 8);
                switch(var13) {
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

                short var14 = (short)(var2[6] & 1024);
                if(var14 == 1024) {
                    var1.BL_SlowSlew = true;
                } else {
                    var1.BL_SlowSlew = false;
                }

                short var15 = (short)(var2[6] & 2048);
                if(var15 == 2048) {
                    var1.BL_SchmittInput = true;
                } else {
                    var1.BL_SchmittInput = false;
                }

                short var16 = (short)((var2[6] & 12288) >> 12);
                switch(var16) {
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

                short var17 = (short)(var2[6] & 16384);
                if(var17 == 16384) {
                    var1.BH_SlowSlew = true;
                } else {
                    var1.BH_SlowSlew = false;
                }

                short var18 = (short)(var2[6] & '耀');
                if(var18 == '耀') {
                    var1.BH_SchmittInput = true;
                } else {
                    var1.BH_SchmittInput = false;
                }

                short var19 = (short)((var2[11] & 24) >> 3);
                if(var19 < 4) {
                    var1.TPRDRV = var19;
                } else {
                    var1.TPRDRV = 0;
                }

                int var20 = var2[7] & 255;
                if(this.mEepromType == 70) {
                    var20 -= 128;
                    var20 /= 2;
                    var1.Manufacturer = this.a(var20, var2);
                    var20 = var2[8] & 255;
                    var20 -= 128;
                    var20 /= 2;
                    var1.Product = this.a(var20, var2);
                    var20 = var2[9] & 255;
                    var20 -= 128;
                    var20 /= 2;
                    var1.SerialNumber = this.a(var20, var2);
                } else {
                    var20 /= 2;
                    var1.Manufacturer = this.a(var20, var2);
                    var20 = var2[8] & 255;
                    var20 /= 2;
                    var1.Product = this.a(var20, var2);
                    var20 = var2[9] & 255;
                    var20 /= 2;
                    var1.SerialNumber = this.a(var20, var2);
                }

                return var1;
            } catch (Exception var21) {
                return null;
            }
        }
    }

    int getUserSize() {
        int var1 = this.readWord((short)9);
        int var2 = var1 & 255;
        var2 /= 2;
        int var3 = (var1 & '\uff00') >> 8;
        var2 += var3 / 2;
        ++var2;
        return (this.mEepromSize - 1 - 1 - var2) * 2;
    }

    int writeUserData(byte[] var1) {
        boolean var2 = false;
        boolean var3 = false;
        if(var1.length > this.getUserSize()) {
            return 0;
        } else {
            int[] var4 = new int[this.mEepromSize];

            for(short var5 = 0; var5 < this.mEepromSize; ++var5) {
                var4[var5] = this.readWord(var5);
            }

            short var7 = (short)(this.mEepromSize - this.getUserSize() / 2 - 1 - 1);

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
                var9 = this.a(var4, this.mEepromSize - 1);
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

    byte[] readUserData(int var1) {
        boolean var2 = false;
        boolean var3 = false;
        boolean var4 = false;
        byte[] var5 = new byte[var1];
        if(var1 != 0 && var1 <= this.getUserSize()) {
            short var6 = (short)(this.mEepromSize - this.getUserSize() / 2 - 1 - 1);

            for(int var7 = 0; var7 < var1; var7 += 2) {
                int var10 = this.readWord(var6++);
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
