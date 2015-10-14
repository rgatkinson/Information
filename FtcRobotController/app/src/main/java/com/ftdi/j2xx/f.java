//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ftdi.j2xx;

class f extends k {
    private static FT_Device d;

    f(FT_Device var1) {
        super(var1);
        d = var1;
    }

    short a(FT_EEPROM var1) {
        int[] var2 = new int[64];
        if(var1.getClass() != FT_EEPROM.class) {
            return (short)1;
        } else {
            FT_EEPROM var3 = var1;

            try {
                for(short var4 = 0; var4 < 64; ++var4) {
                    var2[var4] = this.a(var4);
                }

                var2[1] = var3.VendorId;
                var2[2] = var3.ProductId;
                var2[3] = d.ftDeviceInfoListNode.bcdDevice;
                var2[4] = this.a((Object)var1);
                byte var7 = 10;
                int var8 = this.a(var3.Manufacturer, var2, var7, 7, true);
                var8 = this.a(var3.Product, var2, var8, 8, true);
                if(var3.SerNumEnable) {
                    this.a(var3.SerialNumber, var2, var8, 9, true);
                }

                if(var2[1] != 0 && var2[2] != 0) {
                    boolean var5 = false;
                    var5 = this.a(var2, 63);
                    return (short)(var5?0:1);
                } else {
                    return (short)2;
                }
            } catch (Exception var6) {
                var6.printStackTrace();
                return (short)0;
            }
        }
    }

    FT_EEPROM a() {
        FT_EEPROM var1 = new FT_EEPROM();
        int[] var2 = new int[64];

        try {
            int var3;
            for(var3 = 0; var3 < 64; ++var3) {
                var2[var3] = this.a((short)var3);
            }

            var1.VendorId = (short)var2[1];
            var1.ProductId = (short)var2[2];
            this.a(var1, var2[4]);
            byte var5 = 10;
            var1.Manufacturer = this.a(var5, var2);
            var3 = var5 + var1.Manufacturer.length() + 1;
            var1.Product = this.a(var3, var2);
            var3 += var1.Product.length() + 1;
            var1.SerialNumber = this.a(var3, var2);
            return var1;
        } catch (Exception var4) {
            return null;
        }
    }

    int b() {
        int var1 = this.a((short)7);
        int var2 = (var1 & '\uff00') >> 8;
        var2 /= 2;
        var1 = this.a((short)8);
        int var3 = (var1 & '\uff00') >> 8;
        var3 /= 2;
        int var4 = 10 + var2 + var3 + 1;
        var1 = this.a((short)9);
        int var5 = (var1 & '\uff00') >> 8;
        var5 /= 2;
        return (63 - var4 - 1 - var5) * 2;
    }

    int a(byte[] var1) {
        boolean var2 = false;
        boolean var3 = false;
        if(var1.length > this.b()) {
            return 0;
        } else {
            int[] var4 = new int[64];

            for(short var5 = 0; var5 < 64; ++var5) {
                var4[var5] = this.a(var5);
            }

            short var7 = (short)(63 - this.b() / 2 - 1);
            var7 = (short)(var7 & '\uffff');

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
                var9 = this.a(var4, 63);
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
            short var6 = (short)(63 - this.b() / 2 - 1);
            var6 = (short)(var6 & '\uffff');

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
