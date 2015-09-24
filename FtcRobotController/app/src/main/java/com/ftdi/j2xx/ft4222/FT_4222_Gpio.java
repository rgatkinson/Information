package com.ftdi.j2xx.ft4222;

import android.util.Log;
import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.ft4222.FT_4222_Device;
import com.ftdi.j2xx.ft4222.b;
import com.ftdi.j2xx.ft4222.d;
import com.ftdi.j2xx.ft4222.e;
import com.ftdi.j2xx.interfaces.Gpio;

public class FT_4222_Gpio implements Gpio {
   boolean a = true;
   private FT_4222_Device b;
   private FT_Device c;

   public FT_4222_Gpio(FT_4222_Device var1) {
      this.b = var1;
      this.c = this.b.mFtDev;
   }

   int a(int var1) {
      b var2 = this.b.mChipStatus;
      return var2.a != 2 && var2.a != 3?(var1 >= 4?1014:0):1013;
   }

   int a(d var1) {
      byte[] var2 = new byte[8];
      int var3 = this.cmdGet(32, 0, var2, 8);
      var1.a.a = var2[0];
      var1.a.b = var2[1];
      var1.b = var2[5];
      var1.c = var2[6];
      var1.d[0] = var2[7];
      return var3 == 8?0:var3;
   }

   void a(int var1, byte var2, boolean[] var3) {
      var3[0] = this.d(1 & (var2 & 1 << var1) >> var1);
   }

   boolean b(int var1) {
      byte var2 = 1;
      b var3 = this.b.mChipStatus;
      switch(var3.a) {
      case 0:
         if((var1 == 0 || var1 == var2) && (var3.g == var2 || var3.g == 2)) {
            var2 = 0;
         }

         if(this.d(var3.i) && var1 == 2) {
            var2 = 0;
         }

         if(this.d(var3.j) && var1 == 3) {
            return false;
         }
         break;
      case 1:
         if(var1 == 0 || var1 == var2) {
            var2 = 0;
         }

         if(this.d(var3.i) && var1 == 2) {
            var2 = 0;
         }

         if(this.d(var3.j) && var1 == 3) {
            return false;
         }
         break;
      case 2:
      case 3:
         return false;
      }

      return (boolean)var2;
   }

   boolean c(int var1) {
      d var2 = new d();
      boolean var3 = this.b(var1);
      this.a(var2);
      if(var3 && (1 & var2.c >> var1) != 1) {
         var3 = false;
      }

      return var3;
   }

   public int cmdGet(int var1, int var2, byte[] var3, int var4) {
      return this.c.VendorCmdGet(32, var1 | var2 << 8, var3, var4);
   }

   public int cmdSet(int var1, int var2) {
      return this.c.VendorCmdSet(33, var1 | var2 << 8);
   }

   public int cmdSet(int var1, int var2, byte[] var3, int var4) {
      return this.c.VendorCmdSet(33, var1 | var2 << 8, var3, var4);
   }

   boolean d(int var1) {
      return var1 != 0;
   }

   public int init(int[] var1) {
      b var2 = this.b.mChipStatus;
      d var3 = new d();
      byte[] var4 = new byte[1];
      e var5 = new e();
      this.cmdSet(7, 0);
      this.cmdSet(6, 0);
      int var8 = this.b.init();
      if(var8 != 0) {
         Log.e("GPIO_M", "FT4222_GPIO init - 1 NG ftStatus:" + var8);
         return var8;
      } else if(var2.a != 2 && var2.a != 3) {
         this.a(var3);
         byte var10 = var3.c;
         var4[0] = var3.d[0];

         for(int var11 = 0; var11 < 4; ++var11) {
            if(var1[var11] == 1) {
               var10 = (byte)(15 & (var10 | 1 << var11));
            } else {
               var10 = (byte)(15 & var10 & ~(1 << var11));
            }
         }

         var5.c = var4[0];
         this.cmdSet(33, var10);
         return 0;
      } else {
         return 1013;
      }
   }

   public int newRead(int var1, boolean[] var2) {
      int var3 = this.a(var1);
      if(var3 != 0) {
         return var3;
      } else {
         int var4 = this.c.getQueueStatus();
         if(var4 > 0) {
            byte[] var5 = new byte[var4];
            this.c.read(var5, var4);
            this.a(var1, var5[var4 - 1], var2);
            return var4;
         } else {
            return -1;
         }
      }
   }

   public int newWrite(int var1, boolean var2) {
      d var3 = new d();
      int var4 = this.a(var1);
      if(var4 != 0) {
         return var4;
      } else if(!this.c(var1)) {
         return 1015;
      } else {
         this.a(var3);
         if(var2) {
            byte[] var12 = var3.d;
            var12[0] = (byte)(var12[0] | 1 << var1);
         } else {
            byte[] var6 = var3.d;
            var6[0] = (byte)(var6[0] & 15 & ~(1 << var1));
         }

         if(this.a) {
            byte[] var11 = var3.d;
            var11[0] = (byte)(8 | var11[0]);
         } else {
            byte[] var7 = var3.d;
            var7[0] = (byte)(7 & var7[0]);
         }

         int var8 = this.c.write(var3.d, 1);
         boolean var9 = this.a;
         boolean var10 = false;
         if(!var9) {
            var10 = true;
         }

         this.a = var10;
         return var8;
      }
   }

   public int read(int var1, boolean[] var2) {
      d var3 = new d();
      int var4 = this.a(var1);
      if(var4 == 0) {
         var4 = this.a(var3);
         if(var4 == 0) {
            this.a(var1, var3.d[0], var2);
            return 0;
         }
      }

      return var4;
   }

   public int write(int var1, boolean var2) {
      d var3 = new d();
      int var4 = this.a(var1);
      if(var4 != 0) {
         return var4;
      } else if(!this.c(var1)) {
         return 1015;
      } else {
         this.a(var3);
         if(var2) {
            byte[] var7 = var3.d;
            var7[0] = (byte)(var7[0] | 1 << var1);
         } else {
            byte[] var6 = var3.d;
            var6[0] = (byte)(var6[0] & 15 & ~(1 << var1));
         }

         return this.c.write(var3.d, 1);
      }
   }
}
