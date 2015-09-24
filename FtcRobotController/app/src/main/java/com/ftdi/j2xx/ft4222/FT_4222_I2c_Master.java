package com.ftdi.j2xx.ft4222;

import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.ft4222.FT_4222_Device;
import com.ftdi.j2xx.interfaces.I2cMaster;

public class FT_4222_I2c_Master implements I2cMaster {
   FT_4222_Device a;
   FT_Device b;
   int c;

   public FT_4222_I2c_Master(FT_4222_Device var1) {
      this.a = var1;
      this.b = this.a.mFtDev;
   }

   private int a(int var1, int var2) {
      double var3;
      switch(var1) {
      case 1:
         var3 = 41.666666666666664D;
         break;
      case 2:
         var3 = 20.833333333333332D;
         break;
      case 3:
         var3 = 12.5D;
         break;
      default:
         var3 = 16.666666666666668D;
      }

      if(60 <= var2 && var2 <= 100) {
         int var5 = (int)(0.5D + (1000000.0D / (double)var2 / (var3 * 8.0D) - 1.0D));
         if(var5 > 127) {
            var5 = 127;
         }

         return var5;
      } else {
         return 100 < var2 && var2 <= 400?192 | (int)(0.5D + (1000000.0D / (double)var2 / (var3 * 6.0D) - 1.0D)):(400 < var2 && var2 <= 1000?192 | (int)(0.5D + (1000000.0D / (double)var2 / (var3 * 6.0D) - 1.0D)):(1000 < var2 && var2 <= 3400?-65 & (128 | (int)(0.5D + (1000000.0D / (double)var2 / (var3 * 6.0D) - 1.0D))):74));
      }
   }

   int a(int var1) {
      return ('ﰀ' & var1) > 0?1007:0;
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
      case 1:
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

   public int init(int var1) {
      byte[] var2 = new byte[1];
      int var3 = this.a.init();
      if(var3 == 0) {
         if(!this.a()) {
            return 1012;
         }

         this.cmdSet(81, 0);
         var3 = this.a.getClock(var2);
         if(var3 == 0) {
            int var5 = this.a(var2[0], var1);
            var3 = this.cmdSet(5, 1);
            if(var3 >= 0) {
               this.a.mChipStatus.g = 1;
               var3 = this.cmdSet(82, var5);
               if(var3 >= 0) {
                  this.c = var1;
                  return 0;
               }
            }
         }
      }

      return var3;
   }

   public int read(int var1, byte[] var2, int var3, int[] var4) {
      short var5 = (short)('\uffff' & var1);
      short var6 = (short)((var1 & 896) >> 7);
      short var7 = (short)var3;
      int[] var8 = new int[1];
      byte[] var9 = new byte[4];
      long var10 = System.currentTimeMillis();
      int var12 = this.b.getReadTimeout();
      int var13 = this.a(var1);
      if(var13 == 0) {
         if(var3 < 1) {
            return 6;
         }

         var13 = this.a(true);
         if(var13 == 0) {
            var13 = this.a(var8);
            if(var13 == 0) {
               if(var3 > var8[0]) {
                  return 1010;
               }

               var4[0] = 0;
               var9[0] = (byte)((short)(1 + (var5 << 1)));
               var9[1] = (byte)var6;
               var9[2] = (byte)(255 & var7 >> 8);
               var9[3] = (byte)(var7 & 255);
               if(4 != this.b.write(var9, 4)) {
                  return 1011;
               }

               int var14;
               for(var14 = this.b.getQueueStatus(); var14 < var3 && System.currentTimeMillis() - var10 < (long)var12; var14 = this.b.getQueueStatus()) {
                  ;
               }

               if(var14 <= var3) {
                  var3 = var14;
               }

               int var15 = this.b.read(var2, var3);
               var4[0] = var15;
               if(var15 >= 0) {
                  return 0;
               }

               return 1011;
            }
         }
      }

      return var13;
   }

   public int reset() {
      int var1 = this.a(true);
      return var1 != 0?var1:this.cmdSet(81, 1);
   }

   public int write(int var1, byte[] var2, int var3, int[] var4) {
      short var5 = (short)var1;
      short var6 = (short)((var1 & 896) >> 7);
      short var7 = (short)var3;
      byte[] var8 = new byte[var3 + 4];
      int[] var9 = new int[1];
      int var10 = this.a(var1);
      if(var10 == 0) {
         if(var3 < 1) {
            return 6;
         }

         var10 = this.a(true);
         if(var10 == 0) {
            var10 = this.a(var9);
            if(var10 == 0) {
               if(var3 > var9[0]) {
                  return 1010;
               }

               var4[0] = 0;
               var8[0] = (byte)((short)(var5 << 1));
               var8[1] = (byte)var6;
               var8[2] = (byte)(255 & var7 >> 8);
               var8[3] = (byte)(var7 & 255);

               for(int var11 = 0; var11 < var3; ++var11) {
                  var8[var11 + 4] = var2[var11];
               }

               var4[0] = -4 + this.b.write(var8, var3 + 4);
               if(var3 == var4[0]) {
                  return 0;
               }

               return 10;
            }
         }
      }

      return var10;
   }
}
