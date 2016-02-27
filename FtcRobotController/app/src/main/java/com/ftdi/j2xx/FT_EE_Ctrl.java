package com.ftdi.j2xx;

import android.util.Log;

class FT_EE_Ctrl
   {
   short a;
   int b;
   boolean c;
   private FT_Device d;

   FT_EE_Ctrl(FT_Device var1) {
      this.d = var1;
   }

   int a(byte var1) throws D2xxManager.D2xxException {
      short var2 = (short)(var1 & -1);
      int[] var3 = new int[3];
      short var4 = (short)this.a(var2);
      if(var4 != '\uffff') {
         switch(var4) {
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
         boolean var5 = this.a((short)192, (short)192);
         var3[0] = this.a((short)192);
         var3[1] = this.a((short)64);
         var3[2] = this.a((short)0);
         if(!var5) {
            this.a = 255;
            this.b = 0;
            return 0;
         } else {
            this.c = true;
            if((255 & this.a((short)0)) == 192) {
               this.c();
               this.a = 70;
               this.b = 64;
               return 64;
            } else if((255 & this.a((short)64)) == 192) {
               this.c();
               this.a = 86;
               this.b = 128;
               return 128;
            } else if((255 & this.a((short)192)) == 192) {
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

   int a(Object var1) {
      FT_EEPROM var2 = (FT_EEPROM)var1;
      int var3 = 128;
      if(var2.RemoteWakeup) {
         var3 = 160;
      }

      if(var2.SelfPowered) {
         var3 |= 64;
      }

      return var3 | var2.MaxPower / 2 << 8;
   }

   int a(String var1, int[] var2, int var3, int var4, boolean var5) {
      int var6 = 0;
      int var7 = 2 + 2 * var1.length();
      var2[var4] = var7 << 8 | var3 * 2;
      if(var5) {
         var2[var4] += 128;
      }

      char[] var8 = var1.toCharArray();
      int var9 = var3 + 1;
      var2[var3] = var7 | 768;
      int var10 = (var7 - 2) / 2;

      while(true) {
         int var11 = var9 + 1;
         var2[var9] = var8[var6];
         ++var6;
         if(var6 >= var10) {
            return var11;
         }

         var9 = var11;
      }
   }

   int a(short var1) {
      byte[] var2 = new byte[2];
      if(var1 >= 1024) {
         return -1;
      } else {
         this.d.getUsbDeviceConnection().controlTransfer(-64, 144, 0, var1, var2, 2, 0);
         return (255 & var2[1]) << 8 | 255 & var2[0];
      }
   }

   int a(byte[] var1) {
      return 0;
   }

   FT_EEPROM a() {
      return null;
   }

   String a(int var1, int[] var2) {
      String var3 = "";
      int var4 = -1 + (255 & var2[var1]) / 2;
      int var5 = var1 + 1;

      for(int var6 = var4 + var5; var5 < var6; ++var5) {
         var3 = var3 + (char)var2[var5];
      }

      return var3;
   }

   short a(FT_EEPROM var1) {
      return (short)1;
   }

   void a(FT_EEPROM var1, int var2) {
      var1.MaxPower = (short)(2 * (byte)(var2 >> 8));
      byte var3 = (byte)var2;
      if((var3 & 64) == 64 && (var3 & 128) == 128) {
         var1.SelfPowered = true;
      } else {
         var1.SelfPowered = false;
      }

      if((var3 & 32) == 32) {
         var1.RemoteWakeup = true;
      } else {
         var1.RemoteWakeup = false;
      }
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

   boolean a(short var1, short var2) {
      int var3 = var2 & '\uffff';
      int var4 = var1 & '\uffff';
      return var1 < 1024 && this.d.getUsbDeviceConnection().controlTransfer(64, 145, var3, var4, (byte[])null, 0, 0) == 0;
   }

   boolean a(int[] var1, int var2) {
      int var3 = 'ꪪ';
      int var4 = 0;

      while(var4 < var2) {
         this.a((short)var4, (short)var1[var4]);
         int var6 = '\uffff' & (var3 ^ var1[var4]);
         var3 = '\uffff' & ((short)('\uffff' & var6 << 1) | (short)('\uffff' & var6 >> 15));
         ++var4;
         Log.d("FT_EE_Ctrl", "Entered WriteWord Checksum : " + var3);
      }

      this.a((short)var2, (short)var3);
      return true;
   }

   byte[] a(int var1) {
      return null;
   }

   int b() {
      return 0;
   }

   int b(Object var1) {
      FT_EEPROM var2 = (FT_EEPROM)var1;
      byte var3;
      if(var2.PullDownEnable) {
         var3 = 4;
      } else {
         var3 = 0;
      }

      return var2.SerNumEnable?var3 | 8:var3 & 247;
   }

   int c() {
      return this.d.getUsbDeviceConnection().controlTransfer(64, 146, 0, 0, (byte[])null, 0, 0);
   }
}
