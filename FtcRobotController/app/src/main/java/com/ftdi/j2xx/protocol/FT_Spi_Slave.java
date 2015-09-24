package com.ftdi.j2xx.protocol;

import com.ftdi.j2xx.interfaces.SpiSlave;
import com.ftdi.j2xx.protocol.SpiSlaveEvent;
import com.ftdi.j2xx.protocol.SpiSlaveListener;
import com.ftdi.j2xx.protocol.SpiSlaveRequestEvent;
import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;
import com.ftdi.j2xx.protocol.SpiSlaveThread;
import junit.framework.Assert;

public class FT_Spi_Slave extends SpiSlaveThread {
   // $FF: synthetic field
   private static int[] m;
   private FT_Spi_Slave.a a;
   private int b;
   private int c;
   private int d;
   private int e;
   private int f;
   private byte[] g;
   private int h;
   private int i;
   private SpiSlave j;
   private SpiSlaveListener k;
   private boolean l;

   public FT_Spi_Slave(SpiSlave var1) {
      this.j = var1;
      this.a = FT_Spi_Slave.a.a;
   }

   private int a(byte[] var1, int var2, int var3, int var4, int var5) {
      int var6 = 0;
      int var7;
      if(var1 != null) {
         for(var7 = 0; var6 < var1.length; ++var6) {
            var7 += 255 & var1[var6];
         }
      } else {
         var7 = 0;
      }

      return var4 + var3 + var7 + var2 + (('\uff00' & var5) >> 8) + (var5 & 255);
   }

   private void a(byte[] var1) {
      int var2 = 0;
      boolean var3 = false;

      boolean var9;
      for(boolean var4 = false; var2 < var1.length; var4 = var9) {
         int var5 = 255 & var1[var2];
         boolean var7;
         switch(a()[this.a.ordinal()]) {
         case 1:
            if(var5 != 90) {
               var7 = true;
            } else {
               this.a = FT_Spi_Slave.a.b;
               this.b = var5;
               var7 = var4;
            }
            break;
         case 2:
            if(!this.a(var5)) {
               var3 = true;
               var4 = true;
            } else {
               this.c = var5;
            }

            this.a = FT_Spi_Slave.a.c;
            var7 = var4;
            break;
         case 3:
            this.d = var5;
            this.a = FT_Spi_Slave.a.d;
            var7 = var4;
            break;
         case 4:
            this.e = var5 * 256;
            this.a = FT_Spi_Slave.a.e;
            var7 = var4;
            break;
         case 5:
            this.e += var5;
            this.f = 0;
            this.g = new byte[this.e];
            this.a = FT_Spi_Slave.a.f;
            var7 = var4;
            break;
         case 6:
            this.g[this.f] = var1[var2];
            ++this.f;
            if(this.f == this.e) {
               this.a = FT_Spi_Slave.a.g;
               var7 = var4;
               break;
            }
         default:
            var7 = var4;
            break;
         case 7:
            this.h = var5 * 256;
            this.a = FT_Spi_Slave.a.h;
            var7 = var4;
            break;
         case 8:
            this.h += var5;
            int var6 = this.a(this.g, this.b, this.c, this.d, this.e);
            if(this.h == var6) {
               if(this.c == 128) {
                  this.b();
                  if(this.k != null) {
                     SpiSlaveResponseEvent var12 = new SpiSlaveResponseEvent(3, 0, this.g, (Object)null, (Object)null);
                     this.k.OnDataReceived(var12);
                  }
               }
            } else {
               var3 = true;
            }

            var7 = true;
         }

         if(var3 && this.k != null) {
            SpiSlaveResponseEvent var10 = new SpiSlaveResponseEvent(3, 1, (Object)null, (Object)null, (Object)null);
            this.k.OnDataReceived(var10);
         }

         boolean var8;
         if(var7) {
            this.a = FT_Spi_Slave.a.a;
            this.b = 0;
            this.c = 0;
            this.d = 0;
            this.e = 0;
            this.f = 0;
            this.h = 0;
            this.g = null;
            var8 = false;
            var9 = false;
         } else {
            var8 = var3;
            var9 = var7;
         }

         ++var2;
         var3 = var8;
      }

   }

   private boolean a(int var1) {
      return var1 == 128 || var1 == 130 || var1 == 136;
   }

   // $FF: synthetic method
   static int[] a() {
      int[] var0 = m;
      if(var0 != null) {
         return var0;
      } else {
         int[] var1 = new int[FT_Spi_Slave.a.values().length];

         try {
            var1[FT_Spi_Slave.a.g.ordinal()] = 7;
         } catch (NoSuchFieldError var17) {
            ;
         }

         try {
            var1[FT_Spi_Slave.a.h.ordinal()] = 8;
         } catch (NoSuchFieldError var16) {
            ;
         }

         try {
            var1[FT_Spi_Slave.a.b.ordinal()] = 2;
         } catch (NoSuchFieldError var15) {
            ;
         }

         try {
            var1[FT_Spi_Slave.a.f.ordinal()] = 6;
         } catch (NoSuchFieldError var14) {
            ;
         }

         try {
            var1[FT_Spi_Slave.a.d.ordinal()] = 4;
         } catch (NoSuchFieldError var13) {
            ;
         }

         try {
            var1[FT_Spi_Slave.a.e.ordinal()] = 5;
         } catch (NoSuchFieldError var12) {
            ;
         }

         try {
            var1[FT_Spi_Slave.a.c.ordinal()] = 3;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            var1[FT_Spi_Slave.a.a.ordinal()] = 1;
         } catch (NoSuchFieldError var10) {
            ;
         }

         m = var1;
         return var1;
      }
   }

   private void b() {
      byte[] var1 = new byte[]{(byte)0, (byte)90, (byte)-124, (byte)this.d, (byte)0, (byte)0, (byte)0, (byte)0};
      int var2 = this.a((byte[])null, 90, 132, this.d, 0);
      var1[6] = (byte)(('\uff00' & var2) >> 8);
      var1[7] = (byte)(var2 & 255);
      int[] var3 = new int[1];
      this.j.write(var1, var1.length, var3);
   }

   public int close() {
      if(!this.l) {
         return 3;
      } else {
         this.sendMessage(new SpiSlaveRequestEvent(-1, true, (Object)null, (Object)null, (Object)null));
         this.l = false;
         return 0;
      }
   }

   protected boolean isTerminateEvent(SpiSlaveEvent var1) {
      if(Thread.interrupted()) {
         if(var1 instanceof SpiSlaveRequestEvent) {
            switch(var1.getEventType()) {
            case -1:
               return true;
            }
         } else {
            Assert.assertTrue("processEvent wrong type" + var1.getEventType(), false);
         }

         return false;
      } else {
         return true;
      }
   }

   public int open() {
      if(this.l) {
         return 1;
      } else {
         this.l = true;
         this.j.init();
         this.start();
         return 0;
      }
   }

   protected boolean pollData() {
      int[] var1 = new int[1];
      int var2 = this.j.getRxStatus(var1);
      if(var1[0] > 0 && var2 == 0) {
         byte[] var6 = new byte[var1[0]];
         var2 = this.j.read(var6, var6.length, var1);
         if(var2 == 0) {
            this.a(var6);
         }
      }

      if(var2 == 4 && this.k != null) {
         SpiSlaveResponseEvent var4 = new SpiSlaveResponseEvent(3, 2, this.g, (Object)null, (Object)null);
         this.k.OnDataReceived(var4);
      }

      try {
         Thread.sleep(10L);
         return true;
      } catch (InterruptedException var7) {
         return true;
      }
   }

   public void registerSpiSlaveListener(SpiSlaveListener var1) {
      this.k = var1;
   }

   protected void requestEvent(SpiSlaveEvent var1) {
      if(var1 instanceof SpiSlaveRequestEvent) {
         switch(var1.getEventType()) {
         case -1:
         default:
         }
      } else {
         Assert.assertTrue("processEvent wrong type" + var1.getEventType(), false);
      }
   }

   public int write(byte[] var1) {
      byte var13;
      if(!this.l) {
         var13 = 3;
      } else {
         if(var1.length > 65536) {
            return 1010;
         }

         int[] var2 = new int[1];
         int var3 = var1.length;
         int var4 = this.a(var1, 90, 129, this.i, var3);
         byte[] var5 = new byte[8 + var1.length];
         var5[0] = 0;
         var5[1] = 90;
         var5[2] = -127;
         var5[3] = (byte)this.i;
         var5[4] = (byte)(('\uff00' & var3) >> 8);
         var5[5] = (byte)(var3 & 255);
         int var6 = 6;

         int var8;
         for(int var7 = 0; var7 < var1.length; var6 = var8) {
            var8 = var6 + 1;
            var5[var6] = var1[var7];
            ++var7;
         }

         int var9 = var6 + 1;
         var5[var6] = (byte)(('\uff00' & var4) >> 8);
         int var10000 = var9 + 1;
         var5[var9] = (byte)(var4 & 255);
         this.j.write(var5, var5.length, var2);
         if(var2[0] != var5.length) {
            return 4;
         }

         ++this.i;
         int var12 = this.i;
         var13 = 0;
         if(var12 >= 256) {
            this.i = 0;
            return 0;
         }
      }

      return var13;
   }

   private static enum a {
      a,
      b,
      c,
      d,
      e,
      f,
      g,
      h;

      static {
         FT_Spi_Slave.a[] var0 = new FT_Spi_Slave.a[]{a, b, c, d, e, f, g, h};
      }
   }
}
