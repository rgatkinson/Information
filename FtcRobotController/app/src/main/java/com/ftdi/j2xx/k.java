//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ftdi.j2xx;

import android.util.Log;
import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.FT_EEPROM;
import com.ftdi.j2xx.D2xxManager.D2xxException;

class k {
    private FT_Device d;
    short a;
    int b;
    boolean c;

    k(FT_Device var1) {
        this.d = var1;
    }

    int a(short var1) {
        byte[] var3 = new byte[2];
        byte var4 = -1;
        if(var1 >= 1024) {
            return var4;
        } else {
            this.d.c().controlTransfer(-64, 144, 0, var1, var3, 2, 0);
            int var5 = var3[1] & 255;
            var5 <<= 8;
            var5 |= var3[0] & 255;
            return var5;
        }
    }

    boolean a(short var1, short var2) {
        int var3 = var2 & '\uffff';
        int var4 = var1 & '\uffff';
        boolean var5 = false;
        boolean var6 = false;
        if(var1 >= 1024) {
            return var6;
        } else {
            int var7 = this.d.c().controlTransfer(64, 145, var3, var4, (byte[])null, 0, 0);
            if(var7 == 0) {
                var6 = true;
            }

            return var6;
        }
    }

    int c() {
        boolean var1 = false;
        int var2 = this.d.c().controlTransfer(64, 146, 0, 0, (byte[])null, 0, 0);
        return var2;
    }

    short a(FT_EEPROM var1) {
        return (short)1;
    }

    boolean a(int[] var1, int var2) {
        int var3 = var2;
        int var4 = 'ꪪ';
        boolean var5 = false;
        int var6 = 0;
        boolean var7 = false;
        boolean var8 = false;

        while(var6 < var3) {
            this.a((short)var6, (short)var1[var6]);
            int var9 = var1[var6] ^ var4;
            var9 &= '\uffff';
            short var10 = (short)(var9 << 1 & '\uffff');
            short var11 = (short)(var9 >> 15 & '\uffff');
            var4 = var10 | var11;
            var4 &= '\uffff';
            ++var6;
            Log.d("FT_EE_Ctrl", "Entered WriteWord Checksum : " + var4);
        }

        this.a((short)var3, (short)var4);
        return true;
    }

    FT_EEPROM a() {
        return null;
    }

    int a(Object var1) {
        FT_EEPROM var2 = (FT_EEPROM)var1;
        boolean var3 = false;
        byte var4 = 0;
        boolean var5 = false;
        int var7 = var4 | 128;
        if(var2.RemoteWakeup) {
            var7 |= 32;
        }

        if(var2.SelfPowered) {
            var7 |= 64;
        }

        short var8 = var2.MaxPower;
        int var9 = var8 / 2;
        var9 <<= 8;
        int var6 = var9 | var7;
        return var6;
    }

    void a(FT_EEPROM var1, int var2) {
        byte var3 = (byte)(var2 >> 8);
        var1.MaxPower = (short)(2 * var3);
        byte var4 = (byte)var2;
        if((var4 & 64) == 64 && (var4 & 128) == 128) {
            var1.SelfPowered = true;
        } else {
            var1.SelfPowered = false;
        }

        if((var4 & 32) == 32) {
            var1.RemoteWakeup = true;
        } else {
            var1.RemoteWakeup = false;
        }

    }

    int b(Object var1) {
        FT_EEPROM var2 = (FT_EEPROM)var1;
        byte var3 = 0;
        int var4;
        if(var2.PullDownEnable) {
            var4 = var3 | 4;
        } else {
            var4 = var3 & 251;
        }

        if(var2.SerNumEnable) {
            var4 |= 8;
        } else {
            var4 &= 247;
        }

        return var4;
    }

    void a(Object var1, int var2) {
        FT_EEPROM var3 = (FT_EEPROM)var1;
        if((var2 & 4) > 0) {
            var3.PullDownEnable = true;
        } else {
            var3.PullDownEnable = false;
        }

        if((var2 & 8) > 0) {
            var3.SerNumEnable = true;
        } else {
            var3.SerNumEnable = false;
        }

    }

    int a(String var1, int[] var2, int var3, int var4, boolean var5) {
        int var6 = 0;
        int var7 = var1.length() * 2 + 2;
        var2[var4] = var7 << 8 | var3 * 2;
        if(var5) {
            var2[var4] += 128;
        }

        char[] var8 = var1.toCharArray();
        var2[var3++] = var7 | 768;
        var7 -= 2;
        var7 /= 2;

        do {
            var2[var3++] = var8[var6];
            ++var6;
        } while(var6 < var7);

        return var3;
    }

    String a(int var1, int[] var2) {
        String var3 = "";
        int var5 = var2[var1] & 255;
        var5 = var5 / 2 - 1;
        ++var1;
        int var4 = var1 + var5;

        for(int var6 = var1; var6 < var4; ++var6) {
            var3 = var3 + (char)var2[var6];
        }

        return var3;
    }

    int a(byte var1) throws D2xxException {
        short var2 = 192;
        boolean var3 = false;
        boolean var4 = false;
        short var5 = (short)(var1 & -1);
        int[] var6 = new int[3];
        boolean var7 = false;
        short var9 = (short)this.a(var5);
        if(var9 != '\uffff') {
            switch(var9) {
            case 70:
                this.a = 70;
                this.b = 64;
                this.c = false;
                return 64;
            case 82:
                this.a = 82;
                this.b = 1024;
                this.c = false;
                return 1024;
            case 86:
                this.a = 86;
                this.b = 128;
                this.c = false;
                return 128;
            case 102:
                this.a = 102;
                this.b = 128;
                this.c = false;
                return 256;
            default:
                return 0;
            }
        } else {
            short var10 = 192;
            var7 = this.a(var10, (short)var2);
            var6[0] = this.a((short)192);
            var6[1] = this.a((short)64);
            var6[2] = this.a((short)0);
            if(!var7) {
                this.a = 255;
                this.b = 0;
                return 0;
            } else {
                this.c = true;
                byte var11 = 0;
                int var8 = this.a((short)var11);
                if((var8 & 255) == 192) {
                    this.c();
                    this.a = 70;
                    this.b = 64;
                    return 64;
                } else {
                    var11 = 64;
                    var8 = this.a((short)var11);
                    if((var8 & 255) == 192) {
                        this.c();
                        this.a = 86;
                        this.b = 128;
                        return 128;
                    } else {
                        var10 = 192;
                        var8 = this.a(var10);
                        if((var8 & 255) == 192) {
                            this.c();
                            this.a = 102;
                            this.b = 128;
                            return 256;
                        } else {
                            this.c();
                            return 0;
                        }
                    }
                }
            }
        }
    }

    int a(byte[] var1) {
        return 0;
    }

    byte[] a(int var1) {
        return null;
    }

    int b() {
        return 0;
    }
}
