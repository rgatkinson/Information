package com.ftdi.j2xx.ft4222;

import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.ft4222.FT_4222_Device;
import com.ftdi.j2xx.interfaces.I2cSlave;

public class FT_4222_I2c_Slave implements I2cSlave {
   FT_4222_Device a;
   FT_Device b;

   public FT_4222_I2c_Slave(FT_4222_Device var1) {
      this.a = var1;
      this.b = this.a.mFtDev;
   }

   int a(boolean var1) {
      if(var1) {
         if(this.a.mChipStatus.g != 1) {
            return 1004;
         }
      } else if(this.a.mChipStatus.g != 2) {
         return 1004;
      }

      return 0;
   }

   int a(int[] var1) {
      var1[0] = 0;
      int var2 = this.a.getMaxBuckSize();
      switch(this.a.mChipStatus.g) {
      case 2:
         var1[0] = var2 - 4;
         return 0;
      default:
         return 17;
      }
   }

   boolean a() {
      return this.a.mChipStatus.a == 0 || this.a.mChipStatus.a == 3;
   }

   public int cmdGet(int var1, int var2, byte[] var3, int var4) {
      return this.b.VendorCmdGet(32, var1 | var2 << 8, var3, var4);
   }

   public int cmdSet(int var1, int var2) {
      return this.b.VendorCmdSet(33, var1 | var2 << 8);
   }

   public int cmdSet(int var1, int var2, byte[] var3, int var4) {
      return this.b.VendorCmdSet(33, var1 | var2 << 8, var3, var4);
   }

   public int getAddress(int[] var1) {
      byte[] var2 = new byte[1];
      int var3 = this.a(false);
      if(var3 != 0) {
         return var3;
      } else if(this.b.VendorCmdGet(33, 92, var2, 1) < 0) {
         return 18;
      } else {
         var1[0] = var2[0];
         return 0;
      }
   }

   public int init() {
      int var1 = this.a.init();
      if(var1 == 0) {
         if(!this.a()) {
            return 1012;
         }

         var1 = this.cmdSet(5, 2);
         if(var1 >= 0) {
            this.a.mChipStatus.g = 2;
            return 0;
         }
      }

      return var1;
   }

   public int read(byte[] var1, int var2, int[] var3) {
      int[] var4 = new int[1];
      long var5 = System.currentTimeMillis();
      int var7 = this.b.getReadTimeout();
      int var8;
      if(var2 < 1) {
         var8 = 6;
      } else {
         var8 = this.a(false);
         if(var8 == 0) {
            var8 = this.a(var4);
            if(var8 == 0) {
               if(var2 > var4[0]) {
                  return 1010;
               }

               var3[0] = 0;

               int var9;
               for(var9 = this.b.getQueueStatus(); var9 < var2 && System.currentTimeMillis() - var5 < (long)var7; var9 = this.b.getQueueStatus()) {
                  ;
               }

               if(var9 <= var2) {
                  var2 = var9;
               }

               int var10 = this.b.read(var1, var2);
               if(var10 < 0) {
                  return 1011;
               }

               var3[0] = var10;
               return 0;
            }
         }
      }

      return var8;
   }

   public int reset() {
      int var1 = this.a(false);
      return var1 != 0?var1:this.cmdSet(91, 1);
   }

   public int setAddress(int var1) {
      byte[] var2 = new byte[]{(byte)(var1 & 255)};
      int var3 = this.a(false);
      return var3 != 0?var3:(this.cmdSet(92, var2[0]) < 0?18:0);
   }

   public int write(byte[] var1, int var2, int[] var3) {
      int[] var4 = new int[1];
      int var5;
      if(var2 < 1) {
         var5 = 6;
      } else {
         var5 = this.a(false);
         if(var5 == 0) {
            var5 = this.a(var4);
            if(var5 == 0) {
               if(var2 > var4[0]) {
                  return 1010;
               }

               var3[0] = 0;
               int var6 = this.b.write(var1, var2);
               var3[0] = var6;
               if(var2 == var6) {
                  return 0;
               }

               return 10;
            }
         }
      }

      return var5;
   }
}
