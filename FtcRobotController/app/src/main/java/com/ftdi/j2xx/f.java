package com.ftdi.j2xx;

import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.FT_EEPROM;
import com.ftdi.j2xx.k;

class f extends k {
   private static FT_Device d;

   f(FT_Device var1) {
      super(var1);
      d = var1;
   }

   int a(byte[] var1) {
      if(var1.length <= this.b()) {
         int[] var2 = new int[64];

         for(short var3 = 0; var3 < 64; ++var3) {
            var2[var3] = this.a(var3);
         }

         short var4 = (short)('\uffff' & (short)(-1 + (63 - this.b() / 2)));

         short var8;
         for(int var5 = 0; var5 < var1.length; var4 = var8) {
            int var6;
            if(var5 + 1 < var1.length) {
               var6 = 255 & var1[var5 + 1];
            } else {
               var6 = 0;
            }

            int var7 = var6 << 8 | 255 & var1[var5];
            var8 = (short)(var4 + 1);
            var2[var4] = var7;
            var5 += 2;
         }

         if(var2[1] != 0 && var2[2] != 0 && this.a(var2, 63)) {
            return var1.length;
         }
      }

      return 0;
   }

   FT_EEPROM a() {
      // $FF: Couldn't be decompiled
   }

   short a(FT_EEPROM param1) {
      // $FF: Couldn't be decompiled
   }

   byte[] a(int var1) {
      byte[] var2 = new byte[var1];
      if(var1 != 0 && var1 <= this.b()) {
         short var3 = (short)('\uffff' & (short)(-1 + (63 - this.b() / 2)));

         short var5;
         for(int var4 = 0; var4 < var1; var3 = var5) {
            var5 = (short)(var3 + 1);
            int var6 = this.a(var3);
            if(var4 + 1 < var2.length) {
               byte var7 = (byte)(var6 & 255);
               var2[var4 + 1] = var7;
            }

            var2[var4] = (byte)((var6 & '\uff00') >> 8);
            var4 += 2;
         }
      } else {
         var2 = null;
      }

      return var2;
   }

   int b() {
      int var1 = (('\uff00' & this.a((short)7)) >> 8) / 2;
      int var2 = 1 + (('\uff00' & this.a((short)8)) >> 8) / 2 + var1 + 10;
      int var3 = (('\uff00' & this.a((short)9)) >> 8) / 2;
      return 2 * (-1 + (63 - var2) - var3);
   }
}
