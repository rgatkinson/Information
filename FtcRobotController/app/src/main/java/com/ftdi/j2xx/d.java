//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ftdi.j2xx;

import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.FT_EEPROM;
import com.ftdi.j2xx.FT_EEPROM_2232D;
import com.ftdi.j2xx.k;
import com.ftdi.j2xx.D2xxManager.D2xxException;

class d extends k {
    d(FT_Device var1) throws D2xxException {
        super(var1);
        this.a((byte)10);
    }

    short a(FT_EEPROM var1) {
        int[] var2 = new int[this.b];
        if(var1.getClass() != FT_EEPROM_2232D.class) {
            return (short)1;
        } else {
            FT_EEPROM_2232D var3 = (FT_EEPROM_2232D)var1;

            try {
                var2[0] = 0;
                if(var3.A_FIFO) {
                    var2[0] |= 1;
                } else if(var3.A_FIFOTarget) {
                    var2[0] |= 2;
                } else {
                    var2[0] |= 4;
                }

                if(var3.A_HighIO) {
                    var2[0] |= 16;
                }

                if(var3.A_LoadVCP) {
                    var2[0] |= 8;
                } else if(var3.B_FIFO) {
                    var2[0] |= 256;
                } else if(var3.B_FIFOTarget) {
                    var2[0] |= 512;
                } else {
                    var2[0] |= 1024;
                }

                if(var3.B_HighIO) {
                    var2[0] |= 4096;
                }

                if(var3.B_LoadVCP) {
                    var2[0] |= 2048;
                }

                var2[1] = var3.VendorId;
                var2[2] = var3.ProductId;
                var2[3] = 1280;
                var2[4] = this.a((Object)var1);
                var2[4] = this.b(var1);
                boolean var4 = false;
                byte var5 = 75;
                if(this.a == 70) {
                    var5 = 11;
                    var4 = true;
                }

                int var8 = this.a(var3.Manufacturer, var2, var5, 7, var4);
                var8 = this.a(var3.Product, var2, var8, 8, var4);
                if(var3.SerNumEnable) {
                    this.a(var3.SerialNumber, var2, var8, 9, var4);
                }

                var2[10] = this.a;
                if(var2[1] != 0 && var2[2] != 0) {
                    boolean var6 = false;
                    var6 = this.a(var2, this.b - 1);
                    return (short)(var6?0:1);
                } else {
                    return (short)2;
                }
            } catch (Exception var7) {
                var7.printStackTrace();
                return (short)0;
            }
        }
    }

    FT_EEPROM a() {
        FT_EEPROM_2232D var1 = new FT_EEPROM_2232D();
        int[] var2 = new int[this.b];

        try {
            for(int var3 = 0; var3 < this.b; ++var3) {
                var2[var3] = this.a((short)var3);
            }

            short var11 = (short)(var2[0] & 7);
            switch(var11) {
            case 0:
                var1.A_UART = true;
                break;
            case 1:
                var1.A_FIFO = true;
                break;
            case 2:
                var1.A_FIFOTarget = true;
            case 3:
            default:
                break;
            case 4:
                var1.A_FastSerial = true;
            }

            short var4 = (short)((var2[0] & 8) >> 3);
            if(var4 == 1) {
                var1.A_LoadVCP = true;
            } else {
                var1.A_HighIO = true;
            }

            short var5 = (short)((var2[0] & 16) >> 4);
            if(var5 == 1) {
                var1.A_HighIO = true;
            }

            short var6 = (short)((var2[0] & 1792) >> 8);
            switch(var6) {
            case 0:
                var1.B_UART = true;
                break;
            case 1:
                var1.B_FIFO = true;
                break;
            case 2:
                var1.B_FIFOTarget = true;
            case 3:
            default:
                break;
            case 4:
                var1.B_FastSerial = true;
            }

            short var7 = (short)((var2[0] & 2048) >> 11);
            if(var7 == 1) {
                var1.B_LoadVCP = true;
            } else {
                var1.B_LoadD2XX = true;
            }

            short var8 = (short)((var2[0] & 4096) >> 12);
            if(var8 == 1) {
                var1.B_HighIO = true;
            }

            var1.VendorId = (short)var2[1];
            var1.ProductId = (short)var2[2];
            this.a(var1, var2[4]);
            int var9 = var2[7] & 255;
            if(this.a == 70) {
                var9 -= 128;
                var9 /= 2;
                var1.Manufacturer = this.a(var9, var2);
                var9 = var2[8] & 255;
                var9 -= 128;
                var9 /= 2;
                var1.Product = this.a(var9, var2);
                var9 = var2[9] & 255;
                var9 -= 128;
                var9 /= 2;
                var1.SerialNumber = this.a(var9, var2);
            } else {
                var9 /= 2;
                var1.Manufacturer = this.a(var9, var2);
                var9 = var2[8] & 255;
                var9 /= 2;
                var1.Product = this.a(var9, var2);
                var9 = var2[9] & 255;
                var9 /= 2;
                var1.SerialNumber = this.a(var9, var2);
            }

            return var1;
        } catch (Exception var10) {
            return null;
        }
    }

    int b() {
        int var1 = this.a((short)9);
        int var2 = var1 & 255;
        int var3 = (var1 & '\uff00') >> 8;
        var2 += var3 / 2;
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
