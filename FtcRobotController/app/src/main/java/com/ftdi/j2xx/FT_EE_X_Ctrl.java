//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ftdi.j2xx;

class FT_EE_X_Ctrl extends FT_EE_Ctrl
    {
    private static FT_Device d;

    FT_EE_X_Ctrl(FT_Device var1) {
        super(var1);
        d = var1;
        this.mEepromSize = 128;
        this.mEepromType = 1;
    }

    short programEeprom(FT_EEPROM var1) {
        int[] var2 = new int[this.mEepromSize];
        short var3 = 0;
        if(var1.getClass() != FT_EEPROM_X_Series.class) {
            return (short)1;
        } else {
            FT_EEPROM_X_Series var4 = (FT_EEPROM_X_Series)var1;

            do {
                var2[var3] = this.readWord(var3);
            } while(++var3 < this.mEepromSize);

            try {
                var2[0] = 0;
                if(var4.BCDEnable) {
                    var2[0] |= 1;
                }

                if(var4.BCDForceCBusPWREN) {
                    var2[0] |= 2;
                }

                if(var4.BCDDisableSleep) {
                    var2[0] |= 4;
                }

                if(var4.RS485EchoSuppress) {
                    var2[0] |= 8;
                }

                if(var4.A_LoadVCP) {
                    var2[0] |= 128;
                }

                if(var4.PowerSaveEnable) {
                    boolean var5 = false;
                    if(var4.CBus0 == 17) {
                        var5 = true;
                    }

                    if(var4.CBus1 == 17) {
                        var5 = true;
                    }

                    if(var4.CBus2 == 17) {
                        var5 = true;
                    }

                    if(var4.CBus3 == 17) {
                        var5 = true;
                    }

                    if(var4.CBus4 == 17) {
                        var5 = true;
                    }

                    if(var4.CBus5 == 17) {
                        var5 = true;
                    }

                    if(var4.CBus6 == 17) {
                        var5 = true;
                    }

                    if(!var5) {
                        return (short)1;
                    }

                    var2[0] |= 64;
                }

                var2[1] = var4.VendorId;
                var2[2] = var4.ProductId;
                var2[3] = 4096;
                var2[4] = this.setUSBConfig((Object)var1);
                var2[5] = this.b(var1);
                if(var4.FT1248ClockPolarity) {
                    var2[5] |= 16;
                }

                if(var4.FT1248LSB) {
                    var2[5] |= 32;
                }

                if(var4.FT1248FlowControl) {
                    var2[5] |= 64;
                }

                if(var4.I2CDisableSchmitt) {
                    var2[5] |= 128;
                }

                if(var4.InvertTXD) {
                    var2[5] |= 256;
                }

                if(var4.InvertRXD) {
                    var2[5] |= 512;
                }

                if(var4.InvertRTS) {
                    var2[5] |= 1024;
                }

                if(var4.InvertCTS) {
                    var2[5] |= 2048;
                }

                if(var4.InvertDTR) {
                    var2[5] |= 4096;
                }

                if(var4.InvertDSR) {
                    var2[5] |= 8192;
                }

                if(var4.InvertDCD) {
                    var2[5] |= 16384;
                }

                if(var4.InvertRI) {
                    var2[5] |= '耀';
                }

                var2[6] = 0;
                byte var19 = var4.AD_DriveCurrent;
                if(var19 == -1) {
                    var19 = 0;
                }

                var2[6] |= var19;
                if(var4.AD_SlowSlew) {
                    var2[6] |= 4;
                }

                if(var4.AD_SchmittInput) {
                    var2[6] |= 8;
                }

                byte var6 = var4.AC_DriveCurrent;
                if(var6 == -1) {
                    var6 = 0;
                }

                short var17 = (short)(var6 << 4);
                var2[6] |= var17;
                if(var4.AC_SlowSlew) {
                    var2[6] |= 64;
                }

                if(var4.AC_SchmittInput) {
                    var2[6] |= 128;
                }

                byte var7 = 80;
                int var18 = this.a(var4.Manufacturer, var2, var7, 7, false);
                var18 = this.a(var4.Product, var2, var18, 8, false);
                if(var4.SerNumEnable) {
                    this.a(var4.SerialNumber, var2, var18, 9, false);
                }

                var2[10] = var4.I2CSlaveAddress;
                var2[11] = var4.I2CDeviceID & '\uffff';
                var2[12] = var4.I2CDeviceID >> 16;
                byte var8 = var4.CBus0;
                if(var8 == -1) {
                    var8 = 0;
                }

                byte var9 = var4.CBus1;
                if(var9 == -1) {
                    var9 = 0;
                }

                int var20 = var9 << 8;
                var2[13] = (short)(var8 | var20);
                byte var10 = var4.CBus2;
                if(var10 == -1) {
                    var10 = 0;
                }

                byte var11 = var4.CBus3;
                if(var11 == -1) {
                    var11 = 0;
                }

                int var21 = var11 << 8;
                var2[14] = (short)(var10 | var21);
                byte var12 = var4.CBus4;
                if(var12 == -1) {
                    var12 = 0;
                }

                byte var13 = var4.CBus5;
                if(var13 == -1) {
                    var13 = 0;
                }

                int var22 = var13 << 8;
                var2[15] = (short)(var12 | var22);
                byte var14 = var4.CBus6;
                if(var14 == -1) {
                    var14 = 0;
                }

                var2[16] = (short)var14;
                if(var2[1] != 0 && var2[2] != 0) {
                    boolean var15 = false;
                    var15 = this.b(var2, this.mEepromSize - 1);
                    return (short)(var15?0:1);
                } else {
                    return (short)2;
                }
            } catch (Exception var16) {
                var16.printStackTrace();
                return (short)0;
            }
        }
    }

    boolean b(int[] var1, int var2) {
        int var3 = var2;
        int var4 = 'ꪪ';
        boolean var5 = false;
        int var6 = 0;
        boolean var7 = false;
        boolean var8 = false;
        boolean var9 = false;

        do {
            int var13 = var1[var6];
            var13 &= '\uffff';
            this.writeWord((short)var6, (short)var13);
            int var10 = var13 ^ var4;
            var10 &= '\uffff';
            int var11 = var10 << 1;
            var11 &= '\uffff';
            byte var12;
            if((var10 & '耀') > 0) {
                var12 = 1;
            } else {
                var12 = 0;
            }

            var4 = var11 | var12;
            var4 &= '\uffff';
            ++var6;
            if(var6 == 18) {
                var6 = 64;
            }
        } while(var6 != var3);

        this.writeWord((short)var3, (short)var4);
        return true;
    }

    FT_EEPROM readEeprom() {
        FT_EEPROM_X_Series var1 = new FT_EEPROM_X_Series();
        int[] var2 = new int[this.mEepromSize];

        try {
            short var3;
            for(var3 = 0; var3 < this.mEepromSize; ++var3) {
                var2[var3] = this.readWord(var3);
            }

            if((var2[0] & 1) > 0) {
                var1.BCDEnable = true;
            } else {
                var1.BCDEnable = false;
            }

            if((var2[0] & 2) > 0) {
                var1.BCDForceCBusPWREN = true;
            } else {
                var1.BCDForceCBusPWREN = false;
            }

            if((var2[0] & 4) > 0) {
                var1.BCDDisableSleep = true;
            } else {
                var1.BCDDisableSleep = false;
            }

            if((var2[0] & 8) > 0) {
                var1.RS485EchoSuppress = true;
            } else {
                var1.RS485EchoSuppress = false;
            }

            if((var2[0] & 64) > 0) {
                var1.PowerSaveEnable = true;
            } else {
                var1.PowerSaveEnable = false;
            }

            if((var2[0] & 128) > 0) {
                var1.A_LoadVCP = true;
                var1.A_LoadD2XX = false;
            } else {
                var1.A_LoadVCP = false;
                var1.A_LoadD2XX = true;
            }

            var1.VendorId = (short)var2[1];
            var1.ProductId = (short)var2[2];
            this.getUSBConfig(var1, var2[4]);
            this.getUSBConfig(var1, var2[5]);
            if((var2[5] & 16) > 0) {
                var1.FT1248ClockPolarity = true;
            } else {
                var1.FT1248ClockPolarity = false;
            }

            if((var2[5] & 32) > 0) {
                var1.FT1248LSB = true;
            } else {
                var1.FT1248LSB = false;
            }

            if((var2[5] & 64) > 0) {
                var1.FT1248FlowControl = true;
            } else {
                var1.FT1248FlowControl = false;
            }

            if((var2[5] & 128) > 0) {
                var1.I2CDisableSchmitt = true;
            } else {
                var1.I2CDisableSchmitt = false;
            }

            if((var2[5] & 256) == 256) {
                var1.InvertTXD = true;
            } else {
                var1.InvertTXD = false;
            }

            if((var2[5] & 512) == 512) {
                var1.InvertRXD = true;
            } else {
                var1.InvertRXD = false;
            }

            if((var2[5] & 1024) == 1024) {
                var1.InvertRTS = true;
            } else {
                var1.InvertRTS = false;
            }

            if((var2[5] & 2048) == 2048) {
                var1.InvertCTS = true;
            } else {
                var1.InvertCTS = false;
            }

            if((var2[5] & 4096) == 4096) {
                var1.InvertDTR = true;
            } else {
                var1.InvertDTR = false;
            }

            if((var2[5] & 8192) == 8192) {
                var1.InvertDSR = true;
            } else {
                var1.InvertDSR = false;
            }

            if((var2[5] & 16384) == 16384) {
                var1.InvertDCD = true;
            } else {
                var1.InvertDCD = false;
            }

            if((var2[5] & '耀') == '耀') {
                var1.InvertRI = true;
            } else {
                var1.InvertRI = false;
            }

            var3 = (short)(var2[6] & 3);
            switch(var3) {
            case 0:
                var1.AD_DriveCurrent = 0;
                break;
            case 1:
                var1.AD_DriveCurrent = 1;
                break;
            case 2:
                var1.AD_DriveCurrent = 2;
                break;
            case 3:
                var1.AD_DriveCurrent = 3;
            }

            short var4 = (short)(var2[6] & 4);
            if(var4 == 4) {
                var1.AD_SlowSlew = true;
            } else {
                var1.AD_SlowSlew = false;
            }

            short var5 = (short)(var2[6] & 8);
            if(var5 == 8) {
                var1.AD_SchmittInput = true;
            } else {
                var1.AD_SchmittInput = false;
            }

            short var6 = (short)((var2[6] & 48) >> 4);
            switch(var6) {
            case 0:
                var1.AC_DriveCurrent = 0;
                break;
            case 1:
                var1.AC_DriveCurrent = 1;
                break;
            case 2:
                var1.AC_DriveCurrent = 2;
                break;
            case 3:
                var1.AC_DriveCurrent = 3;
            }

            short var7 = (short)(var2[6] & 64);
            if(var7 == 64) {
                var1.AC_SlowSlew = true;
            } else {
                var1.AC_SlowSlew = false;
            }

            short var8 = (short)(var2[6] & 128);
            if(var8 == 128) {
                var1.AC_SchmittInput = true;
            } else {
                var1.AC_SchmittInput = false;
            }

            var1.I2CSlaveAddress = var2[10];
            var1.I2CDeviceID = var2[11];
            var1.I2CDeviceID |= (var2[12] & 255) << 16;
            var1.CBus0 = (byte)(var2[13] & 255);
            var1.CBus1 = (byte)(var2[13] >> 8 & 255);
            var1.CBus2 = (byte)(var2[14] & 255);
            var1.CBus3 = (byte)(var2[14] >> 8 & 255);
            var1.CBus4 = (byte)(var2[15] & 255);
            var1.CBus5 = (byte)(var2[15] >> 8 & 255);
            var1.CBus6 = (byte)(var2[16] & 255);
            this.mEepromType = (short)(var2[73] >> 8);
            int var9 = var2[7] & 255;
            var9 /= 2;
            var1.Manufacturer = this.a(var9, var2);
            var9 = var2[8] & 255;
            var9 /= 2;
            var1.Product = this.a(var9, var2);
            var9 = var2[9] & 255;
            var9 /= 2;
            var1.SerialNumber = this.a(var9, var2);
            return var1;
        } catch (Exception var10) {
            return null;
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
                var9 = this.b(var4, this.mEepromSize - 1);
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
