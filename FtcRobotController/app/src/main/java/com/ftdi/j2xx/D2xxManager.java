package com.ftdi.j2xx;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class D2xxManager {
   protected static final String ACTION_USB_PERMISSION = "com.ftdi.j2xx";
   public static final int FTDI_BREAK_OFF = 0;
   public static final int FTDI_BREAK_ON = 16384;
   public static final byte FT_BI = 16;
   public static final byte FT_BITMODE_ASYNC_BITBANG = 1;
   public static final byte FT_BITMODE_CBUS_BITBANG = 32;
   public static final byte FT_BITMODE_FAST_SERIAL = 16;
   public static final byte FT_BITMODE_MCU_HOST = 8;
   public static final byte FT_BITMODE_MPSSE = 2;
   public static final byte FT_BITMODE_RESET = 0;
   public static final byte FT_BITMODE_SYNC_BITBANG = 4;
   public static final byte FT_BITMODE_SYNC_FIFO = 64;
   public static final byte FT_CTS = 16;
   public static final byte FT_DATA_BITS_7 = 7;
   public static final byte FT_DATA_BITS_8 = 8;
   public static final byte FT_DCD = -128;
   public static final int FT_DEVICE_2232 = 4;
   public static final int FT_DEVICE_2232H = 6;
   public static final int FT_DEVICE_232B = 0;
   public static final int FT_DEVICE_232H = 8;
   public static final int FT_DEVICE_232R = 5;
   public static final int FT_DEVICE_245R = 5;
   public static final int FT_DEVICE_4222_0 = 10;
   public static final int FT_DEVICE_4222_1_2 = 11;
   public static final int FT_DEVICE_4222_3 = 12;
   public static final int FT_DEVICE_4232H = 7;
   public static final int FT_DEVICE_8U232AM = 1;
   public static final int FT_DEVICE_UNKNOWN = 3;
   public static final int FT_DEVICE_X_SERIES = 9;
   public static final byte FT_DSR = 32;
   public static final byte FT_EVENT_LINE_STATUS = 4;
   public static final byte FT_EVENT_MODEM_STATUS = 2;
   public static final byte FT_EVENT_REMOVED = 8;
   public static final byte FT_EVENT_RXCHAR = 1;
   public static final byte FT_FE = 8;
   public static final byte FT_FLAGS_HI_SPEED = 2;
   public static final byte FT_FLAGS_OPENED = 1;
   public static final short FT_FLOW_DTR_DSR = 512;
   public static final short FT_FLOW_NONE = 0;
   public static final short FT_FLOW_RTS_CTS = 256;
   public static final short FT_FLOW_XON_XOFF = 1024;
   public static final byte FT_OE = 2;
   public static final byte FT_PARITY_EVEN = 2;
   public static final byte FT_PARITY_MARK = 3;
   public static final byte FT_PARITY_NONE = 0;
   public static final byte FT_PARITY_ODD = 1;
   public static final byte FT_PARITY_SPACE = 4;
   public static final byte FT_PE = 4;
   public static final byte FT_PURGE_RX = 1;
   public static final byte FT_PURGE_TX = 2;
   public static final byte FT_RI = 64;
   public static final byte FT_STOP_BITS_1 = 0;
   public static final byte FT_STOP_BITS_2 = 2;
   private static D2xxManager a = null;
   private static Context b = null;
   private static PendingIntent c = null;
   private static IntentFilter d = null;
   private static List<m> e;
   private static UsbManager g;
   private static BroadcastReceiver i;
   private ArrayList<FT_Device> f;
   private BroadcastReceiver h = new BroadcastReceiver() {
      public void onReceive(Context param1, Intent param2) {
         // $FF: Couldn't be decompiled
      }
   };

   static {
      m[] var0 = new m[]{new m(1027, 24597), new m(1027, 24596), new m(1027, 24593), new m(1027, 24592), new m(1027, 24577), new m(1027, 24582), new m(1027, 24604), new m(1027, '贈'), new m(1027, '輸'), new m(1027, '遲'), new m(1027, '醙'), new m(1027, '鉶'), new m(1027, '陼'), new m(1027, 24594), new m(2220, 4133), new m(5590, 1), new m(1027, 24599)};
      e = new ArrayList(Arrays.asList(var0));
      i = new BroadcastReceiver() {
         public void onReceive(Context param1, Intent param2) {
            // $FF: Couldn't be decompiled
         }
      };
   }

   private D2xxManager(Context var1) throws D2xxManager.D2xxException {
      Log.v("D2xx::", "Start constructor");
      if(var1 == null) {
         throw new D2xxManager.D2xxException("D2xx init failed: Can not find parentContext!");
      } else {
         a(var1);
         if(!a()) {
            throw new D2xxManager.D2xxException("D2xx init failed: Can not find UsbManager!");
         } else {
            this.f = new ArrayList();
            IntentFilter var4 = new IntentFilter();
            var4.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
            var4.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
            var1.getApplicationContext().registerReceiver(this.h, var4);
            Log.v("D2xx::", "End constructor");
         }
      }
   }

   private FT_Device a(UsbDevice param1) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   static FT_Device a(D2xxManager var0, UsbDevice var1) {
      return var0.a(var1);
   }

   // $FF: synthetic method
   static ArrayList a(D2xxManager var0) {
      return var0.f;
   }

   private static boolean a() {
      if(g == null && b != null) {
         g = (UsbManager)b.getApplicationContext().getSystemService("usb");
      }

      return g != null;
   }

   private static boolean a(Context var0) {
      synchronized(D2xxManager.class){}
      boolean var1 = false;
      if(var0 != null) {
         boolean var4 = false;

         try {
            var4 = true;
            if(b != var0) {
               b = var0;
               c = PendingIntent.getBroadcast(b.getApplicationContext(), 0, new Intent("com.ftdi.j2xx"), 134217728);
               d = new IntentFilter("com.ftdi.j2xx");
               b.getApplicationContext().registerReceiver(i, d);
               var4 = false;
            } else {
               var4 = false;
            }
         } finally {
            if(var4) {
               ;
            }
         }

         var1 = true;
      }

      return var1;
   }

   private boolean a(Context var1, FT_Device var2, D2xxManager.DriverParameters var3) {
      if(var2 != null && var1 != null) {
         var2.a(var1);
         if(var3 != null) {
            var2.setDriverParameters(var3);
         }

         if(var2.open(g) && var2.isOpen()) {
            return true;
         }
      }

      return false;
   }

   private void b() {
      // $FF: Couldn't be decompiled
   }

   private boolean b(UsbDevice var1) {
      if(!g.hasPermission(var1)) {
         g.requestPermission(var1, c);
      }

      boolean var2 = g.hasPermission(var1);
      boolean var3 = false;
      if(var2) {
         var3 = true;
      }

      return var3;
   }

   public static D2xxManager getInstance(Context param0) throws D2xxManager.D2xxException {
      // $FF: Couldn't be decompiled
   }

   public static int getLibraryVersion() {
      return 537919488;
   }

   public int addUsbDevice(UsbDevice param1) {
      // $FF: Couldn't be decompiled
   }

   public int createDeviceInfoList(Context param1) {
      // $FF: Couldn't be decompiled
   }

   public int getDeviceInfoList(int param1, D2xxManager.FtDeviceInfoListNode[] param2) {
      // $FF: Couldn't be decompiled
   }

   public D2xxManager.FtDeviceInfoListNode getDeviceInfoListDetail(int param1) {
      // $FF: Couldn't be decompiled
   }

   public int[][] getVIDPID() {
      int var1 = e.size();
      int[] var2 = new int[]{2, var1};
      int[][] var3 = (int[][])Array.newInstance(Integer.TYPE, var2);

      for(int var4 = 0; var4 < var1; ++var4) {
         m var5 = (m)e.get(var4);
         var3[0][var4] = var5.a();
         var3[1][var4] = var5.b();
      }

      return var3;
   }

   public boolean isFtDevice(UsbDevice var1) {
      if(b == null) {
         return false;
      } else {
         m var2 = new m(var1.getVendorId(), var1.getProductId());
         boolean var3 = e.contains(var2);
         boolean var4 = false;
         if(var3) {
            var4 = true;
         }

         Log.v("D2xx::", var2.toString());
         return var4;
      }
   }

   public FT_Device openByDescription(Context var1, String var2) {
      synchronized(this){}

      FT_Device var4;
      try {
         var4 = this.openByDescription(var1, var2, (D2xxManager.DriverParameters)null);
      } finally {
         ;
      }

      return var4;
   }

   public FT_Device openByDescription(Context param1, String param2, D2xxManager.DriverParameters param3) {
      // $FF: Couldn't be decompiled
   }

   public FT_Device openByIndex(Context var1, int var2) {
      synchronized(this){}

      FT_Device var4;
      try {
         var4 = this.openByIndex(var1, var2, (D2xxManager.DriverParameters)null);
      } finally {
         ;
      }

      return var4;
   }

   public FT_Device openByIndex(Context var1, int var2, D2xxManager.DriverParameters var3) {
      synchronized(this){}
      FT_Device var4 = null;
      if(var2 >= 0) {
         var4 = null;
         if(var1 != null) {
            boolean var10 = false;

            FT_Device var7;
            boolean var8;
            try {
               var10 = true;
               a(var1);
               var7 = (FT_Device)this.f.get(var2);
               var8 = this.a(var1, var7, var3);
               var10 = false;
            } finally {
               if(var10) {
                  ;
               }
            }

            if(!var8) {
               var7 = null;
            }

            var4 = var7;
         }
      }

      return var4;
   }

   public FT_Device openByLocation(Context var1, int var2) {
      synchronized(this){}

      FT_Device var4;
      try {
         var4 = this.openByLocation(var1, var2, (D2xxManager.DriverParameters)null);
      } finally {
         ;
      }

      return var4;
   }

   public FT_Device openByLocation(Context param1, int param2, D2xxManager.DriverParameters param3) {
      // $FF: Couldn't be decompiled
   }

   public FT_Device openBySerialNumber(Context var1, String var2) {
      synchronized(this){}

      FT_Device var4;
      try {
         var4 = this.openBySerialNumber(var1, var2, (D2xxManager.DriverParameters)null);
      } finally {
         ;
      }

      return var4;
   }

   public FT_Device openBySerialNumber(Context param1, String param2, D2xxManager.DriverParameters param3) {
      // $FF: Couldn't be decompiled
   }

   public FT_Device openByUsbDevice(Context var1, UsbDevice var2) {
      synchronized(this){}

      FT_Device var4;
      try {
         var4 = this.openByUsbDevice(var1, var2, (D2xxManager.DriverParameters)null);
      } finally {
         ;
      }

      return var4;
   }

   public FT_Device openByUsbDevice(Context param1, UsbDevice param2, D2xxManager.DriverParameters param3) {
      // $FF: Couldn't be decompiled
   }

   public boolean setVIDPID(int var1, int var2) {
      boolean var3 = false;
      if(var1 != 0 && var2 != 0) {
         m var5 = new m(var1, var2);
         if(e.contains(var5)) {
            Log.i("D2xx::", "Existing vid:" + var1 + "  pid:" + var2);
            return true;
         }

         if(!e.add(var5)) {
            Log.d("D2xx::", "Failed to add VID/PID combination to list.");
         } else {
            var3 = true;
         }
      } else {
         Log.d("D2xx::", "Invalid parameter to setVIDPID");
         var3 = false;
      }

      return var3;
   }

   public static class D2xxException extends IOException {
      public D2xxException() {
      }

      public D2xxException(String var1) {
         super(var1);
      }
   }

   public static class DriverParameters {
      private int a = 16384;
      private int b = 16384;
      private int c = 16;
      private int d = 5000;

      public int getBufferNumber() {
         return this.c;
      }

      public int getMaxBufferSize() {
         return this.a;
      }

      public int getMaxTransferSize() {
         return this.b;
      }

      public int getReadTimeout() {
         return this.d;
      }

      public boolean setBufferNumber(int var1) {
         if(var1 >= 2 && var1 <= 16) {
            this.c = var1;
            return true;
         } else {
            Log.e("D2xx::", "***nrBuffers Out of correct range***");
            return false;
         }
      }

      public boolean setMaxBufferSize(int var1) {
         if(var1 >= 64 && var1 <= 262144) {
            this.a = var1;
            return true;
         } else {
            Log.e("D2xx::", "***bufferSize Out of correct range***");
            return false;
         }
      }

      public boolean setMaxTransferSize(int var1) {
         if(var1 >= 64 && var1 <= 262144) {
            this.b = var1;
            return true;
         } else {
            Log.e("D2xx::", "***maxTransferSize Out of correct range***");
            return false;
         }
      }

      public boolean setReadTimeout(int var1) {
         this.d = var1;
         return true;
      }
   }

   public static class FtDeviceInfoListNode {
      public short bcdDevice;
      public int breakOnParam;
      public String description;
      public int flags;
      public int handle;
      public byte iSerialNumber;
      public int id;
      public short lineStatus;
      public int location;
      public short modemStatus;
      public String serialNumber;
      public int type;
   }
}
