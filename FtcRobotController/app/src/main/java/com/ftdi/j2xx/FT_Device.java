package com.ftdi.j2xx;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.util.Log;
import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_EEPROM;
import com.ftdi.j2xx.a;
import com.ftdi.j2xx.b;
import com.ftdi.j2xx.k;
import com.ftdi.j2xx.o;
import com.ftdi.j2xx.q;
import com.ftdi.j2xx.r;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class FT_Device {
   long a;
   Boolean b;
   UsbDevice c;
   UsbInterface d;
   UsbEndpoint e;
   UsbEndpoint f;
   D2xxManager.FtDeviceInfoListNode g;
   r h;
   q i;
   Context j;
   private UsbRequest k;
   private UsbDeviceConnection l;
   private a m;
   private Thread n;
   private Thread o;
   private o p;
   private k q;
   private byte r;
   private D2xxManager.DriverParameters s;
   private int t;
   private int u;

   public FT_Device(Context param1, UsbManager param2, UsbDevice param3, UsbInterface param4) {
      // $FF: Couldn't be decompiled
   }

   private final String a(byte[] var1) throws UnsupportedEncodingException {
      return new String(var1, 2, -2 + var1[0], "UTF-16LE");
   }

   private boolean a(int var1) {
      int var2 = var1 | this.g.breakOnParam;
      return this.isOpen() && this.c().controlTransfer(64, 4, var2, this.t, (byte[])null, 0, 0) == 0;
   }

   private boolean a(boolean var1, boolean var2) {
      if(this.isOpen()) {
         if(var1) {
            int var4 = 0;

            int var5;
            int var6;
            for(var5 = 0; var4 < 6; var5 = var6) {
               var6 = this.c().controlTransfer(64, 0, 1, this.t, (byte[])null, 0, 0);
               ++var4;
            }

            if(var5 > 0) {
               return false;
            }

            this.p.e();
         }

         boolean var3;
         if(var2 && this.c().controlTransfer(64, 0, 2, this.t, (byte[])null, 0, 0) == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      } else {
         return false;
      }
   }

   private final boolean f() {
      return this.i() || this.j() || this.b();
   }

   private final boolean g() {
      return this.m() || this.l() || this.k() || this.j() || this.b() || this.i() || this.h();
   }

   private final boolean h() {
      return ('\uff00' & this.g.bcdDevice) == 4096;
   }

   private final boolean i() {
      return ('\uff00' & this.g.bcdDevice) == 2304;
   }

   private final boolean j() {
      return ('\uff00' & this.g.bcdDevice) == 1792;
   }

   private final boolean k() {
      return ('\uff00' & this.g.bcdDevice) == 1536;
   }

   private final boolean l() {
      return ('\uff00' & this.g.bcdDevice) == 1280;
   }

   private final boolean m() {
      return ('\uff00' & this.g.bcdDevice) == 1024 || ('\uff00' & this.g.bcdDevice) == 512 && this.g.iSerialNumber == 0;
   }

   private void n() {
      if(this.t == 1) {
         D2xxManager.FtDeviceInfoListNode var7 = this.g;
         var7.serialNumber = var7.serialNumber + "A";
         D2xxManager.FtDeviceInfoListNode var8 = this.g;
         var8.description = var8.description + " A";
      } else {
         if(this.t == 2) {
            D2xxManager.FtDeviceInfoListNode var5 = this.g;
            var5.serialNumber = var5.serialNumber + "B";
            D2xxManager.FtDeviceInfoListNode var6 = this.g;
            var6.description = var6.description + " B";
            return;
         }

         if(this.t == 3) {
            D2xxManager.FtDeviceInfoListNode var3 = this.g;
            var3.serialNumber = var3.serialNumber + "C";
            D2xxManager.FtDeviceInfoListNode var4 = this.g;
            var4.description = var4.description + " C";
            return;
         }

         if(this.t == 4) {
            D2xxManager.FtDeviceInfoListNode var1 = this.g;
            var1.serialNumber = var1.serialNumber + "D";
            D2xxManager.FtDeviceInfoListNode var2 = this.g;
            var2.description = var2.description + " D";
            return;
         }
      }

   }

   private void o() {
      synchronized(this){}

      try {
         this.b = Boolean.valueOf(true);
         D2xxManager.FtDeviceInfoListNode var2 = this.g;
         var2.flags |= 1;
      } finally {
         ;
      }

   }

   private void p() {
      synchronized(this){}

      try {
         this.b = Boolean.valueOf(false);
         D2xxManager.FtDeviceInfoListNode var2 = this.g;
         var2.flags &= 2;
      } finally {
         ;
      }

   }

   private boolean q() {
      for(int var1 = 0; var1 < this.d.getEndpointCount(); ++var1) {
         StringBuilder var2 = new StringBuilder("EP: ");
         Object[] var3 = new Object[]{Integer.valueOf(this.d.getEndpoint(var1).getAddress())};
         Log.i("FTDI_Device::", var2.append(String.format("0x%02X", var3)).toString());
         if(this.d.getEndpoint(var1).getType() == 2) {
            if(this.d.getEndpoint(var1).getDirection() == 128) {
               this.f = this.d.getEndpoint(var1);
               this.u = this.f.getMaxPacketSize();
            } else {
               this.e = this.d.getEndpoint(var1);
            }
         } else {
            Log.i("FTDI_Device::", "Not Bulk Endpoint");
         }
      }

      if(this.e != null && this.f != null) {
         return true;
      } else {
         return false;
      }
   }

   public int VendorCmdGet(int var1, int var2, byte[] var3, int var4) {
      if(!this.isOpen()) {
         Log.e("FTDI_Device::", "VendorCmdGet: Device not open");
         return -1;
      } else if(var4 < 0) {
         Log.e("FTDI_Device::", "VendorCmdGet: Invalid data length");
         return -1;
      } else if(var3 == null) {
         Log.e("FTDI_Device::", "VendorCmdGet: buf is null");
         return -1;
      } else if(var3.length < var4) {
         Log.e("FTDI_Device::", "VendorCmdGet: length of buffer is smaller than data length to get");
         return -1;
      } else {
         return this.c().controlTransfer(-64, var1, var2, this.t, var3, var4, 0);
      }
   }

   public int VendorCmdSet(int var1, int var2) {
      return !this.isOpen()?-1:this.c().controlTransfer(64, var1, var2, this.t, (byte[])null, 0, 0);
   }

   public int VendorCmdSet(int var1, int var2, byte[] var3, int var4) {
      if(!this.isOpen()) {
         Log.e("FTDI_Device::", "VendorCmdSet: Device not open");
         return -1;
      } else if(var4 < 0) {
         Log.e("FTDI_Device::", "VendorCmdSet: Invalid data length");
         return -1;
      } else {
         if(var3 == null) {
            if(var4 > 0) {
               Log.e("FTDI_Device::", "VendorCmdSet: buf is null!");
               return -1;
            }
         } else if(var3.length < var4) {
            Log.e("FTDI_Device::", "VendorCmdSet: length of buffer is smaller than data length to set");
            return -1;
         }

         return this.c().controlTransfer(64, var1, var2, this.t, var3, var4, 0);
      }
   }

   void a(UsbDeviceConnection var1) {
      this.l = var1;
   }

   final boolean a() {
      return this.l() || this.j() || this.b();
   }

   boolean a(Context var1) {
      synchronized(this){}
      boolean var2 = false;
      if(var1 != null) {
         boolean var5 = false;

         try {
            var5 = true;
            this.j = var1;
            var5 = false;
         } finally {
            if(var5) {
               ;
            }
         }

         var2 = true;
      }

      return var2;
   }

   boolean a(UsbManager param1) {
      // $FF: Couldn't be decompiled
   }

   final boolean b() {
      return ('\uff00' & this.g.bcdDevice) == 2048;
   }

   UsbDeviceConnection c() {
      return this.l;
   }

   public void close() {
      synchronized(this){}

      try {
         if(this.n != null) {
            this.n.interrupt();
         }

         if(this.o != null) {
            this.o.interrupt();
         }

         if(this.l != null) {
            this.l.releaseInterface(this.d);
            this.l.close();
            this.l = null;
         }

         if(this.p != null) {
            this.p.g();
         }

         this.n = null;
         this.o = null;
         this.m = null;
         this.p = null;
         this.p();
      } finally {
         ;
      }

   }

   public boolean clrDtr() {
      byte var1 = 1;
      if(!this.isOpen()) {
         return false;
      } else {
         if(this.c().controlTransfer(64, var1, 256, this.t, (byte[])null, 0, 0) != 0) {
            var1 = 0;
         }

         return (boolean)var1;
      }
   }

   public boolean clrRts() {
      byte var1 = 1;
      if(!this.isOpen()) {
         return false;
      } else {
         if(this.c().controlTransfer(64, var1, 512, this.t, (byte[])null, 0, 0) != 0) {
            var1 = 0;
         }

         return (boolean)var1;
      }
   }

   D2xxManager.DriverParameters d() {
      return this.s;
   }

   int e() {
      return this.u;
   }

   public boolean eepromErase() {
      return this.isOpen() && this.q.c() == 0;
   }

   public int eepromGetUserAreaSize() {
      return !this.isOpen()?-1:this.q.b();
   }

   public FT_EEPROM eepromRead() {
      return !this.isOpen()?null:this.q.a();
   }

   public byte[] eepromReadUserArea(int var1) {
      return !this.isOpen()?null:this.q.a(var1);
   }

   public int eepromReadWord(short var1) {
      return !this.isOpen()?-1:this.q.a(var1);
   }

   public short eepromWrite(FT_EEPROM var1) {
      return !this.isOpen()?-1:this.q.a(var1);
   }

   public int eepromWriteUserArea(byte[] var1) {
      return !this.isOpen()?0:this.q.a(var1);
   }

   public boolean eepromWriteWord(short var1, short var2) {
      return !this.isOpen()?false:this.q.a(var1, var2);
   }

   public byte getBitMode() {
      byte[] var1 = new byte[1];
      return !this.isOpen()?-1:(!this.g()?-2:(this.c().controlTransfer(-64, 12, 0, this.t, var1, var1.length, 0) == var1.length?var1[0]:-3));
   }

   public D2xxManager.FtDeviceInfoListNode getDeviceInfo() {
      return this.g;
   }

   public long getEventStatus() {
      if(!this.isOpen()) {
         return -1L;
      } else if(this.p == null) {
         return -2L;
      } else {
         long var1 = this.a;
         this.a = 0L;
         return var1;
      }
   }

   public byte getLatencyTimer() {
      byte[] var1 = new byte[1];
      byte var4;
      if(!this.isOpen()) {
         var4 = -1;
      } else {
         int var2 = this.c().controlTransfer(-64, 10, 0, this.t, var1, var1.length, 0);
         int var3 = var1.length;
         var4 = 0;
         if(var2 == var3) {
            return var1[0];
         }
      }

      return var4;
   }

   public short getLineStatus() {
      return !this.isOpen()?-1:(this.p == null?-2:this.g.lineStatus);
   }

   public short getModemStatus() {
      if(!this.isOpen()) {
         return (short)-1;
      } else if(this.p == null) {
         return (short)-2;
      } else {
         this.a &= -3L;
         return (short)(255 & this.g.modemStatus);
      }
   }

   public int getQueueStatus() {
      return !this.isOpen()?-1:(this.p == null?-2:this.p.c());
   }

   public int getReadTimeout() {
      return this.s.getReadTimeout();
   }

   protected UsbDevice getUsbDevice() {
      return this.c;
   }

   public boolean isOpen() {
      synchronized(this){}

      boolean var2;
      try {
         var2 = this.b.booleanValue();
      } finally {
         ;
      }

      return var2;
   }

   public boolean purge(byte var1) {
      byte var2 = 1;
      byte var3;
      if((var1 & 1) == var2) {
         var3 = var2;
      } else {
         var3 = 0;
      }

      if((var1 & 2) != 2) {
         var2 = 0;
      }

      return this.a((boolean)var3, (boolean)var2);
   }

   public int read(byte[] var1) {
      return this.read(var1, var1.length, (long)this.s.getReadTimeout());
   }

   public int read(byte[] var1, int var2) {
      return this.read(var1, var2, (long)this.s.getReadTimeout());
   }

   public int read(byte[] var1, int var2, long var3) {
      return !this.isOpen()?-1:(var2 <= 0?-2:(this.p == null?-3:this.p.a(var1, var2, var3)));
   }

   public boolean readBufferFull() {
      return this.p.a();
   }

   public boolean resetDevice() {
      return this.isOpen() && this.c().controlTransfer(64, 0, 0, 0, (byte[])null, 0, 0) == 0;
   }

   public void restartInTask() {
      this.m.b();
   }

   public boolean setBaudRate(int var1) {
      int[] var2 = new int[2];
      if(this.isOpen()) {
         byte var3;
         switch(var1) {
         case 300:
            var2[0] = 10000;
            var3 = 1;
            break;
         case 600:
            var2[0] = 5000;
            var3 = 1;
            break;
         case 1200:
            var2[0] = 2500;
            var3 = 1;
            break;
         case 2400:
            var2[0] = 1250;
            var3 = 1;
            break;
         case 4800:
            var2[0] = 625;
            var3 = 1;
            break;
         case 9600:
            var2[0] = 16696;
            var3 = 1;
            break;
         case 19200:
            var2[0] = '肜';
            var3 = 1;
            break;
         case 38400:
            var2[0] = '쁎';
            var3 = 1;
            break;
         case 57600:
            var2[0] = 52;
            var3 = 1;
            break;
         case 115200:
            var2[0] = 26;
            var3 = 1;
            break;
         case 230400:
            var2[0] = 13;
            var3 = 1;
            break;
         case 460800:
            var2[0] = 16390;
            var3 = 1;
            break;
         case 921600:
            var2[0] = '考';
            var3 = 1;
            break;
         default:
            if(this.f() && var1 >= 1200) {
               var3 = b.a(var1, var2);
            } else {
               var3 = b.a(var1, var2, this.g());
            }
         }

         if(this.a() || this.i() || this.h()) {
            var2[1] <<= 8;
            var2[1] &= '\uff00';
            var2[1] |= this.t;
         }

         if(var3 == 1 && this.c().controlTransfer(64, 3, var2[0], var2[1], (byte[])null, 0, 0) == 0) {
            return true;
         }
      }

      return false;
   }

   public boolean setBitMode(byte var1, byte var2) {
      byte var3 = 1;
      int var4 = this.g.type;
      if(this.isOpen() && var4 != var3) {
         if(var4 == 0 && var2 != 0) {
            if((var2 & 1) == 0) {
               return false;
            }
         } else if(var4 == 4 && var2 != 0) {
            if((var2 & 31) == 0) {
               return false;
            }

            byte var12;
            if(var2 == 2) {
               var12 = var3;
            } else {
               var12 = 0;
            }

            byte var13;
            if(this.d.getId() != 0) {
               var13 = var3;
            } else {
               var13 = 0;
            }

            if((var12 & var13) != 0) {
               return false;
            }
         } else if(var4 == 5 && var2 != 0) {
            if((var2 & 37) == 0) {
               return false;
            }
         } else if(var4 == 6 && var2 != 0) {
            if((var2 & 95) == 0) {
               return false;
            }

            byte var10;
            if((var2 & 72) > 0) {
               var10 = var3;
            } else {
               var10 = 0;
            }

            byte var11;
            if(this.d.getId() != 0) {
               var11 = var3;
            } else {
               var11 = 0;
            }

            if((var10 & var11) != 0) {
               return false;
            }
         } else if(var4 == 7 && var2 != 0) {
            if((var2 & 7) == 0) {
               return false;
            }

            byte var6;
            if(var2 == 2) {
               var6 = var3;
            } else {
               var6 = 0;
            }

            byte var7;
            if(this.d.getId() != 0) {
               var7 = var3;
            } else {
               var7 = 0;
            }

            int var8 = var7 & var6;
            byte var9;
            if(this.d.getId() != var3) {
               var9 = var3;
            } else {
               var9 = 0;
            }

            if((var9 & var8) != 0) {
               return false;
            }
         } else if(var4 == 8 && var2 != 0 && var2 > 64) {
            return false;
         }

         int var5 = var2 << 8 | var1 & 255;
         if(this.c().controlTransfer(64, 11, var5, this.t, (byte[])null, 0, 0) != 0) {
            var3 = 0;
         }

         return (boolean)var3;
      } else {
         return false;
      }
   }

   public boolean setBreakOff() {
      return this.a(0);
   }

   public boolean setBreakOn() {
      return this.a(16384);
   }

   public boolean setChars(byte var1, byte var2, byte var3, byte var4) {
      r var5 = new r();
      var5.a = var1;
      var5.b = var2;
      var5.c = var3;
      var5.d = var4;
      if(this.isOpen()) {
         int var6 = var1 & 255;
         if(var2 != 0) {
            var6 |= 256;
         }

         if(this.c().controlTransfer(64, 6, var6, this.t, (byte[])null, 0, 0) == 0) {
            int var7 = var3 & 255;
            if(var4 > 0) {
               var7 |= 256;
            }

            if(this.c().controlTransfer(64, 7, var7, this.t, (byte[])null, 0, 0) == 0) {
               this.h = var5;
               return true;
            }
         }
      }

      return false;
   }

   public boolean setDataCharacteristics(byte var1, byte var2, byte var3) {
      if(this.isOpen()) {
         short var4 = (short)((short)(var1 | var3 << 8) | var2 << 11);
         this.g.breakOnParam = var4;
         if(this.c().controlTransfer(64, 4, var4, this.t, (byte[])null, 0, 0) == 0) {
            return true;
         }
      }

      return false;
   }

   protected void setDriverParameters(D2xxManager.DriverParameters var1) {
      this.s.setMaxBufferSize(var1.getMaxBufferSize());
      this.s.setMaxTransferSize(var1.getMaxTransferSize());
      this.s.setBufferNumber(var1.getBufferNumber());
      this.s.setReadTimeout(var1.getReadTimeout());
   }

   public boolean setDtr() {
      byte var1 = 1;
      if(!this.isOpen()) {
         return false;
      } else {
         if(this.c().controlTransfer(64, var1, 257, this.t, (byte[])null, 0, 0) != 0) {
            var1 = 0;
         }

         return (boolean)var1;
      }
   }

   public boolean setEventNotification(long var1) {
      if(this.isOpen() && var1 != 0L) {
         this.a = 0L;
         this.i.a = var1;
         return true;
      } else {
         return false;
      }
   }

   public boolean setFlowControl(short var1, byte var2, byte var3) {
      boolean var4 = this.isOpen();
      boolean var5 = false;
      if(var4) {
         short var6;
         if(var1 == 1024) {
            var6 = (short)((short)(var3 << 8) | var2 & 255);
         } else {
            var6 = 0;
         }

         int var7 = this.c().controlTransfer(64, 2, var6, var1 | this.t, (byte[])null, 0, 0);
         var5 = false;
         if(var7 == 0) {
            var5 = true;
            if(var1 == 256) {
               return this.setRts();
            }

            if(var1 == 512) {
               return this.setDtr();
            }
         }
      }

      return var5;
   }

   public boolean setLatencyTimer(byte var1) {
      int var2 = var1 & 255;
      if(this.isOpen() && this.c().controlTransfer(64, 9, var2, this.t, (byte[])null, 0, 0) == 0) {
         this.r = var1;
         return true;
      } else {
         return false;
      }
   }

   public boolean setRts() {
      byte var1 = 1;
      if(!this.isOpen()) {
         return false;
      } else {
         if(this.c().controlTransfer(64, var1, 514, this.t, (byte[])null, 0, 0) != 0) {
            var1 = 0;
         }

         return (boolean)var1;
      }
   }

   public void stopInTask() {
      try {
         if(!this.m.c()) {
            this.m.a();
         }

      } catch (InterruptedException var2) {
         Log.d("FTDI_Device::", "stopInTask called!");
         var2.printStackTrace();
      }
   }

   public boolean stoppedInTask() {
      return this.m.c();
   }

   public int write(byte[] var1) {
      return this.write(var1, var1.length, true);
   }

   public int write(byte[] var1, int var2) {
      return this.write(var1, var2, true);
   }

   public int write(byte[] var1, int var2, boolean var3) {
      int var4 = -1;
      if(this.isOpen() && var2 >= 0) {
         UsbRequest var5 = this.k;
         if(var3) {
            var5.setClientData(this);
         }

         if(var2 == 0) {
            if(var5.queue(ByteBuffer.wrap(new byte[1]), var2)) {
               var4 = var2;
            }
         } else if(var5.queue(ByteBuffer.wrap(var1), var2)) {
            var4 = var2;
         }

         if(var3) {
            UsbRequest var6;
            do {
               var6 = this.l.requestWait();
               if(var6 == null) {
                  Log.e("FTDI_Device::", "UsbConnection.requestWait() == null");
                  return -99;
               }
            } while(var6.getClientData() != this);

            return var4;
         }
      }

      return var4;
   }
}
