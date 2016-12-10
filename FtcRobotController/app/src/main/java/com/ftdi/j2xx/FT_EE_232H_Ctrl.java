//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ftdi.j2xx;

import com.ftdi.j2xx.D2xxManager.D2xxException;

class FT_EE_232H_Ctrl extends FT_EE_Ctrl
    {
    FT_EE_232H_Ctrl(FT_Device var1) throws D2xxException {
        super(var1);
        this.getEepromSize((byte)15);
    }

    short programEeprom(FT_EEPROM var1) {
        int[] var2 = new int[this.mEepromSize];
        if(var1.getClass() != FT_EEPROM_232H.class) {
            return (short)1;
        } else {
            FT_EEPROM_232H var3 = (FT_EEPROM_232H)var1;

            try {
                if(var3.FIFO) {
                    var2[0] |= 1;
                } else if(var3.FIFOTarget) {
                    var2[0] |= 2;
                } else if(var3.FastSerial) {
                    var2[0] |= 4;
                }

                if(var3.FT1248) {
                    var2[0] |= 8;
                }

                if(var3.LoadVCP) {
                    var2[0] |= 16;
                }

                if(var3.FT1248ClockPolarity) {
                    var2[0] |= 256;
                }

                if(var3.FT1248LSB) {
                    var2[0] |= 512;
                }

                if(var3.FT1248FlowControl) {
                    var2[0] |= 1024;
                }

                if(var3.PowerSaveEnable) {
                    var2[0] |= '耀';
                }

                var2[1] = var3.VendorId;
                var2[2] = var3.ProductId;
                var2[3] = 2304;
                var2[4] = this.setUSBConfig((Object)var1);
                var2[5] = this.b(var1);
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

                byte var5 = var3.BL_DriveCurrent;
                if(var5 == -1) {
                    var5 = 0;
                }

                var2[6] |= (short)(var5 << 8);
                if(var3.BL_SlowSlew) {
                    var2[6] |= 1024;
                }

                if(var3.BL_SchmittInput) {
                    var2[6] |= 2048;
                }

                byte var6 = 80;
                int var19 = this.a(var3.Manufacturer, var2, var6, 7, false);
                var19 = this.a(var3.Product, var2, var19, 8, false);
                if(var3.SerNumEnable) {
                    this.a(var3.SerialNumber, var2, var19, 9, false);
                }

                var2[10] = 0;
                var2[11] = 0;
                var2[12] = 0;
                byte var7 = var3.CBus0;
                byte var8 = var3.CBus1;
                int var20 = var8 << 4;
                byte var9 = var3.CBus2;
                int var21 = var9 << 8;
                byte var10 = var3.CBus3;
                int var22 = var10 << 12;
                var2[12] = var7 | var20 | var21 | var22;
                var2[13] = 0;
                byte var11 = var3.CBus4;
                byte var12 = var3.CBus5;
                int var23 = var12 << 4;
                byte var13 = var3.CBus6;
                int var24 = var13 << 8;
                byte var14 = var3.CBus7;
                int var25 = var14 << 12;
                var2[13] = var11 | var23 | var24 | var25;
                var2[14] = 0;
                byte var15 = var3.CBus8;
                byte var16 = var3.CBus9;
                int var26 = var16 << 4;
                var2[14] = var15 | var26;
                var2[15] = this.mEepromType;
                var2[69] = 72;
                if(this.mEepromType == 70) {
                    return (short)1;
                } else if(var2[1] != 0 && var2[2] != 0) {
                    boolean var17 = false;
                    var17 = this.a(var2, this.mEepromSize - 1);
                    return (short)(var17?0:1);
                } else {
                    return (short)2;
                }
            } catch (Exception var18) {
                var18.printStackTrace();
                return (short)0;
            }
        }
    }

    FT_EEPROM readEeprom() {
        FT_EEPROM_232H var1 = new FT_EEPROM_232H();
        int[] var2 = new int[this.mEepromSize];
        if(this.mEepromBlank) {
            return var1;
        } else {
            try {
                for(short var3 = 0; var3 < this.mEepromSize; ++var3) {
                    var2[var3] = this.readWord(var3);
                }

                var1.UART = false;
                switch(var2[0] & 15) {
                case 0:
                    var1.UART = true;
                    break;
                case 1:
                    var1.FIFO = true;
                    break;
                case 2:
                    var1.FIFOTarget = true;
                    break;
                case 3:
                case 5:
                case 6:
                case 7:
                default:
                    var1.UART = true;
                    break;
                case 4:
                    var1.FastSerial = true;
                    break;
                case 8:
                    var1.FT1248 = true;
                }

                if((var2[0] & 16) > 0) {
                    var1.LoadVCP = true;
                    var1.LoadD2XX = false;
                } else {
                    var1.LoadVCP = false;
                    var1.LoadD2XX = true;
                }

                if((var2[0] & 256) > 0) {
                    var1.FT1248ClockPolarity = true;
                } else {
                    var1.FT1248ClockPolarity = false;
                }

                if((var2[0] & 512) > 0) {
                    var1.FT1248LSB = true;
                } else {
                    var1.FT1248LSB = false;
                }

                if((var2[0] & 1024) > 0) {
                    var1.FT1248FlowControl = true;
                } else {
                    var1.FT1248FlowControl = false;
                }

                if((var2[0] & '耀') > 0) {
                    var1.PowerSaveEnable = true;
                }

                var1.VendorId = (short)var2[1];
                var1.ProductId = (short)var2[2];
                this.getUSBConfig(var1, var2[4]);
                this.getUSBConfig(var1, var2[5]);
                int var17 = var2[6] & 3;
                switch(var17) {
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

                if((var2[6] & 4) > 0) {
                    var1.AL_SlowSlew = true;
                } else {
                    var1.AL_SlowSlew = false;
                }

                if((var2[6] & 8) > 0) {
                    var1.AL_SchmittInput = true;
                } else {
                    var1.AL_SchmittInput = false;
                }

                short var4 = (short)((var2[6] & 768) >> 8);
                switch(var4) {
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

                if((var2[6] & 1024) > 0) {
                    var1.BL_SlowSlew = true;
                } else {
                    var1.BL_SlowSlew = false;
                }

                if((var2[6] & 2048) > 0) {
                    var1.BL_SchmittInput = true;
                } else {
                    var1.BL_SchmittInput = false;
                }

                short var5 = (short)(var2[12] >> 0 & 15);
                var1.CBus0 = (byte)var5;
                short var6 = (short)(var2[12] >> 4 & 15);
                var1.CBus1 = (byte)var6;
                short var7 = (short)(var2[12] >> 8 & 15);
                var1.CBus2 = (byte)var7;
                short var8 = (short)(var2[12] >> 12 & 15);
                var1.CBus3 = (byte)var8;
                short var9 = (short)(var2[13] >> 0 & 15);
                var1.CBus4 = (byte)var9;
                short var10 = (short)(var2[13] >> 4 & 15);
                var1.CBus5 = (byte)var10;
                short var11 = (short)(var2[13] >> 8 & 15);
                var1.CBus6 = (byte)var11;
                short var12 = (short)(var2[13] >> 12 & 15);
                var1.CBus7 = (byte)var12;
                short var13 = (short)(var2[14] >> 0 & 15);
                var1.CBus8 = (byte)var13;
                short var14 = (short)(var2[14] >> 4 & 15);
                var1.CBus9 = (byte)var14;
                int var15 = var2[7] & 255;
                var15 /= 2;
                var1.Manufacturer = this.a(var15, var2);
                var15 = var2[8] & 255;
                var15 /= 2;
                var1.Product = this.a(var15, var2);
                var15 = var2[9] & 255;
                var15 /= 2;
                var1.SerialNumber = this.a(var15, var2);
                return var1;
            } catch (Exception var16) {
                return null;
            }
        }
    }

    int getUserSize() {
        int var1 = this.readWord((short)9);
        int var2 = var1 & 255;
        var2 /= 2;
        ++var2;
        int var3 = (var1 & '\uff00') >> 8;
        var3 /= 2;
        ++var3;
        return (this.mEepromSize - var2 - 1 - var3) * 2;
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
