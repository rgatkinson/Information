//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ftdi.j2xx;

class h extends k {
    private static FT_Device d;

    h(FT_Device var1) {
        super(var1);
        d = var1;
    }

    boolean a(short var1, short var2) {
        int var3 = var2 & '\uffff';
        int var4 = var1 & '\uffff';
        boolean var5 = false;
        boolean var6 = false;
        boolean var7 = false;
        if(var1 >= 1024) {
            return var6;
        } else {
            byte var9 = d.getLatencyTimer();
            d.setLatencyTimer((byte)119);
            int var8 = d.getUsbDeviceConnection().controlTransfer(64, 145, var3, var4, (byte[])null, 0, 0);
            if(var8 == 0) {
                var6 = true;
            }

            d.setLatencyTimer(var9);
            return var6;
        }
    }

    short a(FT_EEPROM var1) {
        if(var1.getClass() != FT_EEPROM_232R.class) {
            return (short)1;
        } else {
            int[] var2 = new int[80];
            FT_EEPROM_232R var7 = (FT_EEPROM_232R)var1;

            try {
                for(short var8 = 0; var8 < 80; ++var8) {
                    var2[var8] = this.a(var8);
                }

                byte var3 = 0;
                int var17 = var3 | var2[0] & '\uff00';
                if(var7.HighIO) {
                    var17 |= 4;
                }

                if(var7.LoadVCP) {
                    var17 |= 8;
                }

                if(var7.ExternalOscillator) {
                    var17 |= 2;
                } else {
                    var17 &= '�';
                }

                var2[0] = var17;
                var2[1] = var7.VendorId;
                var2[2] = var7.ProductId;
                var2[3] = 1536;
                var2[4] = this.a((Object)var1);
                int var4 = this.b(var1);
                if(var7.InvertTXD) {
                    var4 |= 256;
                }

                if(var7.InvertRXD) {
                    var4 |= 512;
                }

                if(var7.InvertRTS) {
                    var4 |= 1024;
                }

                if(var7.InvertCTS) {
                    var4 |= 2048;
                }

                if(var7.InvertDTR) {
                    var4 |= 4096;
                }

                if(var7.InvertDSR) {
                    var4 |= 8192;
                }

                if(var7.InvertDCD) {
                    var4 |= 16384;
                }

                if(var7.InvertRI) {
                    var4 |= '耀';
                }

                var2[5] = var4;
                boolean var5 = false;
                byte var19 = var7.CBus0;
                byte var9 = var7.CBus1;
                int var20 = var9 << 4;
                byte var10 = var7.CBus2;
                int var21 = var10 << 8;
                byte var11 = var7.CBus3;
                int var22 = var11 << 12;
                int var18 = var19 | var20 | var21 | var22;
                var2[10] = var18;
                boolean var6 = false;
                byte var12 = var7.CBus4;
                var2[11] = var12;
                byte var13 = 12;
                int var23 = this.a(var7.Manufacturer, var2, var13, 7, true);
                var23 = this.a(var7.Product, var2, var23, 8, true);
                if(var7.SerNumEnable) {
                    this.a(var7.SerialNumber, var2, var23, 9, true);
                }

                boolean var14 = false;
                if(var2[1] != 0 && var2[2] != 0) {
                    boolean var15 = false;
                    byte var24 = d.getLatencyTimer();
                    d.setLatencyTimer((byte)119);
                    var15 = this.a(var2, 63);
                    d.setLatencyTimer(var24);
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

    FT_EEPROM a() {
        FT_EEPROM_232R var1 = new FT_EEPROM_232R();
        int[] var2 = new int[80];

        try {
            int var3;
            for(var3 = 0; var3 < 80; ++var3) {
                var2[var3] = this.a((short)var3);
            }

            if((var2[0] & 4) == 4) {
                var1.HighIO = true;
            } else {
                var1.HighIO = false;
            }

            if((var2[0] & 8) == 8) {
                var1.LoadVCP = true;
            } else {
                var1.LoadVCP = false;
            }

            if((var2[0] & 2) == 2) {
                var1.ExternalOscillator = true;
            } else {
                var1.ExternalOscillator = false;
            }

            var1.VendorId = (short)var2[1];
            var1.ProductId = (short)var2[2];
            this.a(var1, var2[4]);
            this.a(var1, var2[5]);
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

            var3 = var2[10];
            int var4 = var3 & 15;
            var1.CBus0 = (byte)var4;
            int var5 = var3 & 240;
            var1.CBus1 = (byte)(var5 >> 4);
            int var6 = var3 & 3840;
            var1.CBus2 = (byte)(var6 >> 8);
            int var7 = var3 & '\uf000';
            var1.CBus3 = (byte)(var7 >> 12);
            int var8 = var2[11] & 255;
            var1.CBus4 = (byte)var8;
            int var9 = var2[7] & 255;
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
            return var1;
        } catch (Exception var10) {
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
        int var4 = 12 + var2 + var3 + 1;
        var1 = this.a((short)9);
        int var5 = (var1 & '\uff00') >> 8;
        var5 /= 2;
        return (63 - var4 - var5 - 1) * 2;
    }

    int a(byte[] var1) {
        boolean var2 = false;
        boolean var3 = false;
        if(var1.length > this.b()) {
            return 0;
        } else {
            int[] var4 = new int[80];

            for(short var5 = 0; var5 < 80; ++var5) {
                var4[var5] = this.a(var5);
            }

            short var8 = (short)(63 - this.b() / 2 - 1);
            var8 = (short)(var8 & '\uffff');

            for(int var9 = 0; var9 < var1.length; var9 += 2) {
                int var7;
                if(var9 + 1 < var1.length) {
                    var7 = var1[var9 + 1] & 255;
                } else {
                    var7 = 0;
                }

                var7 <<= 8;
                var7 |= var1[var9] & 255;
                var4[var8++] = var7;
            }

            boolean var10 = false;
            if(var4[1] != 0 && var4[2] != 0) {
                boolean var6 = false;
                byte var11 = d.getLatencyTimer();
                d.setLatencyTimer((byte)119);
                var6 = this.a(var4, 63);
                d.setLatencyTimer(var11);
                if(!var6) {
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
