package com.ftdi.j2xx.ft4222;

import android.util.Log;
import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.ft4222.FT_4222_Device;
import com.ftdi.j2xx.ft4222.a;
import com.ftdi.j2xx.ft4222.b;
import com.ftdi.j2xx.interfaces.SpiSlave;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FT_4222_Spi_Slave implements SpiSlave {
   private FT_4222_Device a;
   private FT_Device b;
   private Lock c;

   public FT_4222_Spi_Slave(FT_4222_Device var1) {
      this.a = var1;
      this.b = var1.mFtDev;
      this.c = new ReentrantLock();
   }

   private int a() {
      return this.a.mChipStatus.g != 4?1003:0;
   }

   public int getRxStatus(int[] var1) {
      if(var1 == null) {
         return 1009;
      } else {
         int var2 = this.a();
         if(var2 != 0) {
            return var2;
         } else {
            this.c.lock();
            int var3 = this.b.getQueueStatus();
            this.c.unlock();
            if(var3 >= 0) {
               var1[0] = var3;
               return 0;
            } else {
               var1[0] = -1;
               return 4;
            }
         }
      }
   }

   public int init() {
      b var1 = this.a.mChipStatus;
      a var2 = this.a.mSpiMasterCfg;
      var2.a = 1;
      var2.b = 2;
      var2.c = 0;
      var2.d = 0;
      var2.e = 1;
      this.c.lock();
      this.a.cleanRxData();
      int var4 = this.b.VendorCmdSet(33, 66 | var2.a << 8);
      byte var5 = 0;
      if(var4 < 0) {
         var5 = 4;
      }

      if(this.b.VendorCmdSet(33, 68 | var2.b << 8) < 0) {
         var5 = 4;
      }

      if(this.b.VendorCmdSet(33, 69 | var2.c << 8) < 0) {
         var5 = 4;
      }

      if(this.b.VendorCmdSet(33, 70 | var2.d << 8) < 0) {
         var5 = 4;
      }

      if(this.b.VendorCmdSet(33, 67) < 0) {
         var5 = 4;
      }

      if(this.b.VendorCmdSet(33, 72 | var2.e << 8) < 0) {
         var5 = 4;
      }

      if(this.b.VendorCmdSet(33, 1029) < 0) {
         var5 = 4;
      }

      this.c.unlock();
      var1.g = 4;
      return var5;
   }

   public int read(byte[] var1, int var2, int[] var3) {
      this.c.lock();
      byte var4;
      if(this.b != null && this.b.isOpen()) {
         int var5 = this.b.read(var1, var2);
         this.c.unlock();
         var3[0] = var5;
         var4 = 0;
         if(var5 < 0) {
            return 4;
         }
      } else {
         this.c.unlock();
         var4 = 3;
      }

      return var4;
   }

   public int reset() {
      this.c.lock();
      int var1 = this.b.VendorCmdSet(33, 74);
      byte var2 = 0;
      if(var1 < 0) {
         var2 = 4;
      }

      this.c.unlock();
      return var2;
   }

   public int setDrivingStrength(int var1, int var2, int var3) {
      byte var4 = 3;
      byte var5 = 4;
      b var6 = this.a.mChipStatus;
      if(var6.g != var4 && var6.g != var5) {
         return 1003;
      } else {
         int var7 = var3 | var1 << 4 | var2 << 2;
         if(var6.g != var4) {
            var4 = var5;
         }

         this.c.lock();
         int var8 = this.b.VendorCmdSet(33, 160 | var7 << 8);
         byte var9 = 0;
         if(var8 < 0) {
            var9 = var5;
         }

         if(this.b.VendorCmdSet(33, 5 | var4 << 8) >= 0) {
            var5 = var9;
         }

         this.c.unlock();
         return var5;
      }
   }

   public int write(byte[] var1, int var2, int[] var3) {
      int var4;
      if(var3 != null && var1 != null) {
         var4 = this.a();
         if(var4 == 0) {
            if(var2 > 512) {
               return 1010;
            }

            this.c.lock();
            var3[0] = this.b.write(var1, var2);
            this.c.unlock();
            if(var3[0] != var2) {
               Log.e("FTDI_Device::", "Error write =" + var2 + " tx=" + var3[0]);
               return 4;
            }
         }
      } else {
         var4 = 1009;
      }

      return var4;
   }
}
