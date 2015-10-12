package com.ftdi.j2xx.ft4222;

import android.util.Log;
import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.ft4222.FT_4222_Device;
import com.ftdi.j2xx.ft4222.a;
import com.ftdi.j2xx.ft4222.b;
import com.ftdi.j2xx.interfaces.SpiMaster;
import junit.framework.Assert;

public class FT_4222_Spi_Master implements SpiMaster {
   private FT_4222_Device a;
   private FT_Device b;

   public FT_4222_Spi_Master(FT_4222_Device var1) {
      this.a = var1;
      this.b = var1.mFtDev;
   }

   private int a(FT_Device var1, byte[] var2, byte[] var3) {
      int var4;
      if(var1 != null && var1.isOpen()) {
         var1.write(var2, var2.length);
         var4 = 0;
         int var6 = 0;

         while(var4 < var3.length && var6 < 30000) {
            int var8 = var1.getQueueStatus();
            if(var8 > 0) {
               byte[] var12 = new byte[var8];
               int var13 = var1.read(var12, var8);
               boolean var14;
               if(var12.length == var13) {
                  var14 = true;
               } else {
                  var14 = false;
               }

               Assert.assertEquals(var14, true);

               for(int var15 = 0; var15 < var12.length; ++var15) {
                  if(var4 + var15 < var3.length) {
                     var3[var4 + var15] = var12[var15];
                  }
               }

               var4 += var13;
               var6 = 0;
            }

            long var9 = (long)10;

            try {
               Thread.sleep(var9);
            } catch (InterruptedException var16) {
               var6 = 30000;
               continue;
            }

            var6 += 10;
         }

         if(var3.length != var4 || var6 > 30000) {
            Log.e("FTDI_Device::", "MultiReadWritePackage timeout!!!!");
            return -1;
         }
      } else {
         var4 = -1;
      }

      return var4;
   }

   private int a(FT_Device var1, byte[] var2, byte[] var3, int var4) {
      byte[] var5 = new byte[16384];
      byte[] var6 = new byte[var5.length];
      int var7 = var4 / var5.length;
      int var8 = var4 % var5.length;
      int var9 = 0;
      int var10 = 0;

      int var11;
      int var12;
      for(var11 = 0; var9 < var7; var10 = var12) {
         var12 = var10;

         for(int var13 = 0; var13 < var5.length; ++var13) {
            var5[var13] = var2[var12];
            ++var12;
         }

         if(this.b(var1, var5, var6) <= 0) {
            return -1;
         }

         for(int var14 = 0; var14 < var6.length; ++var14) {
            var3[var11] = var6[var14];
            ++var11;
         }

         ++var9;
      }

      if(var8 > 0) {
         byte[] var15 = new byte[var8];
         byte[] var16 = new byte[var15.length];
         int var17 = var10;

         for(int var18 = 0; var18 < var15.length; ++var18) {
            var15[var18] = var2[var17];
            ++var17;
         }

         int var19 = this.b(var1, var15, var16);
         int var20 = 0;
         if(var19 > 0) {
            while(var20 < var16.length) {
               var3[var11] = var16[var20];
               ++var11;
               ++var20;
            }

            return var11;
         }

         var11 = -1;
      }

      return var11;
   }

   private int b(FT_Device var1, byte[] var2, byte[] var3) {
      int var4;
      if(var1 != null && var1.isOpen()) {
         boolean var5;
         if(var2.length == var3.length) {
            var5 = true;
         } else {
            var5 = false;
         }

         Assert.assertEquals(var5, true);
         int var6 = var1.write(var2, var2.length);
         if(var2.length != var6) {
            Log.e("FTDI_Device::", "setReadWritePackage Incomplete Write Error!!!");
            return -1;
         }

         var4 = 0;
         int var7 = 0;

         while(var4 < var3.length && var7 < 30000) {
            int var9 = var1.getQueueStatus();
            if(var9 > 0) {
               byte[] var13 = new byte[var9];
               int var14 = var1.read(var13, var9);
               boolean var15;
               if(var13.length == var14) {
                  var15 = true;
               } else {
                  var15 = false;
               }

               Assert.assertEquals(var15, true);

               for(int var16 = 0; var16 < var13.length; ++var16) {
                  if(var4 + var16 < var3.length) {
                     var3[var4 + var16] = var13[var16];
                  }
               }

               var4 += var14;
               var7 = 0;
            }

            long var10 = (long)10;

            try {
               Thread.sleep(var10);
            } catch (InterruptedException var17) {
               var7 = 30000;
               continue;
            }

            var7 += 10;
         }

         if(var3.length != var4 || var7 > 30000) {
            Log.e("FTDI_Device::", "SingleReadWritePackage timeout!!!!");
            return -1;
         }
      } else {
         var4 = -1;
      }

      return var4;
   }

   public int init(int var1, int var2, int var3, int var4, byte var5) {
      byte var6 = 1;
      b var7 = this.a.mChipStatus;
      a var8 = this.a.mSpiMasterCfg;
      var8.a = var1;
      var8.b = var2;
      var8.c = var3;
      var8.d = var4;
      var8.e = var5;
      if(var8.a != var6 && var8.a != 2 && var8.a != 4) {
         return 6;
      } else {
         this.a.cleanRxData();
         switch(var7.a) {
         case 0:
         case 3:
            break;
         case 1:
            var6 = 7;
            break;
         case 2:
            var6 = 15;
            break;
         default:
            var6 = 0;
         }

         if((var6 & var8.e) == 0) {
            return 6;
         } else {
            var8.e &= var6;
            if(this.b.VendorCmdSet(33, 66 | var8.a << 8) < 0) {
               return 4;
            } else if(this.b.VendorCmdSet(33, 68 | var8.b << 8) < 0) {
               return 4;
            } else if(this.b.VendorCmdSet(33, 69 | var8.c << 8) < 0) {
               return 4;
            } else if(this.b.VendorCmdSet(33, 70 | var8.d << 8) < 0) {
               return 4;
            } else if(this.b.VendorCmdSet(33, 67) < 0) {
               return 4;
            } else if(this.b.VendorCmdSet(33, 72 | var8.e << 8) < 0) {
               return 4;
            } else if(this.b.VendorCmdSet(33, 773) < 0) {
               return 4;
            } else {
               var7.g = 3;
               return 0;
            }
         }
      }
   }

   public int multiReadWrite(byte[] var1, byte[] var2, int var3, int var4, int var5, int[] var6) {
      b var7 = this.a.mChipStatus;
      a var8 = this.a.mSpiMasterCfg;
      if((var5 <= 0 || var1 != null) && (var3 + var4 <= 0 || var2 != null) && (var5 <= 0 || var6 != null)) {
         if(var7.g == 3 && var8.a != 1) {
            if(var3 > 15) {
               Log.e("FTDI_Device::", "The maxium single write bytes are 15 bytes");
               return 6;
            } else {
               byte[] var9 = new byte[var4 + var3 + 5];
               var9[0] = (byte)(128 | var3 & 15);
               var9[1] = (byte)((var4 & '\uff00') >> 8);
               var9[2] = (byte)(var4 & 255);
               var9[3] = (byte)((var5 & '\uff00') >> 8);
               var9[4] = (byte)(var5 & 255);

               for(int var10 = 0; var10 < var3 + var4; ++var10) {
                  var9[var10 + 5] = var2[var10];
               }

               var6[0] = this.a(this.b, var9, var1);
               return 0;
            }
         } else {
            return 1006;
         }
      } else {
         return 1009;
      }
   }

   public int reset() {
      return this.b.VendorCmdSet(33, 74) < 0?4:0;
   }

   public int setDrivingStrength(int var1, int var2, int var3) {
      short var4 = 3;
      short var5 = 4;
      b var6 = this.a.mChipStatus;
      if(var6.g != var4 && var6.g != var5) {
         var5 = 1003;
      } else {
         int var7 = var3 | var1 << 4 | var2 << 2;
         if(var6.g != var4) {
            var4 = var5;
         }

         if(this.b.VendorCmdSet(33, 160 | var7 << 8) >= 0 && this.b.VendorCmdSet(33, 5 | var4 << 8) >= 0) {
            return 0;
         }
      }

      return var5;
   }

   public int setLines(int var1) {
      short var2 = 4;
      if(this.a.mChipStatus.g != 3) {
         var2 = 1003;
      } else {
         if(var1 == 0) {
            return 17;
         }

         if(this.b.VendorCmdSet(33, 66 | var1 << 8) >= 0 && this.b.VendorCmdSet(33, 330) >= 0) {
            this.a.mSpiMasterCfg.a = var1;
            return 0;
         }
      }

      return var2;
   }

   public int singleRead(byte[] var1, int var2, int[] var3, boolean var4) {
      return this.singleReadWrite(var1, new byte[var1.length], var2, var3, var4);
   }

   public int singleReadWrite(byte[] var1, byte[] var2, int var3, int[] var4, boolean var5) {
      b var6 = this.a.mChipStatus;
      a var7 = this.a.mSpiMasterCfg;
      short var8;
      if(var2 != null && var1 != null && var4 != null) {
         var4[0] = 0;
         if(var6.g != 3 || var7.a != 1) {
            return 1005;
         }

         if(var3 == 0) {
            return 6;
         }

         if(var3 > var2.length || var3 > var1.length) {
            Assert.assertTrue("sizeToTransfer > writeBuffer.length || sizeToTransfer > readBuffer.length", false);
         }

         if(var2.length != var1.length || var2.length == 0) {
            Assert.assertTrue("writeBuffer.length != readBuffer.length || writeBuffer.length == 0", false);
         }

         var4[0] = this.a(this.b, var2, var1, var3);
         var8 = 0;
         if(var5) {
            this.b.write((byte[])null, 0);
            return 0;
         }
      } else {
         var8 = 1009;
      }

      return var8;
   }

   public int singleWrite(byte[] var1, int var2, int[] var3, boolean var4) {
      return this.singleReadWrite(new byte[var1.length], var1, var2, var3, var4);
   }
}
