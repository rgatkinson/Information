//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class D2xxManager {
    private static D2xxManager a = null;
    protected static final String ACTION_USB_PERMISSION = "com.ftdi.j2xx";
    public static final byte FT_DATA_BITS_7 = 7;
    public static final byte FT_DATA_BITS_8 = 8;
    public static final byte FT_STOP_BITS_1 = 0;
    public static final byte FT_STOP_BITS_2 = 2;
    public static final byte FT_PARITY_NONE = 0;
    public static final byte FT_PARITY_ODD = 1;
    public static final byte FT_PARITY_EVEN = 2;
    public static final byte FT_PARITY_MARK = 3;
    public static final byte FT_PARITY_SPACE = 4;
    public static final short FT_FLOW_NONE = 0;
    public static final short FT_FLOW_RTS_CTS = 256;
    public static final short FT_FLOW_DTR_DSR = 512;
    public static final short FT_FLOW_XON_XOFF = 1024;
    public static final byte FT_PURGE_RX = 1;
    public static final byte FT_PURGE_TX = 2;
    public static final byte FT_CTS = 16;
    public static final byte FT_DSR = 32;
    public static final byte FT_RI = 64;
    public static final byte FT_DCD = -128;
    public static final byte FT_OE = 2;
    public static final byte FT_PE = 4;
    public static final byte FT_FE = 8;
    public static final byte FT_BI = 16;
    public static final byte FT_EVENT_RXCHAR = 1;
    public static final byte FT_EVENT_MODEM_STATUS = 2;
    public static final byte FT_EVENT_LINE_STATUS = 4;
    public static final byte FT_EVENT_REMOVED = 8;
    public static final byte FT_FLAGS_OPENED = 1;
    public static final byte FT_FLAGS_HI_SPEED = 2;
    public static final int FT_DEVICE_232B = 0;
    public static final int FT_DEVICE_8U232AM = 1;
    public static final int FT_DEVICE_UNKNOWN = 3;
    public static final int FT_DEVICE_2232 = 4;
    public static final int FT_DEVICE_232R = 5;
    public static final int FT_DEVICE_245R = 5;
    public static final int FT_DEVICE_2232H = 6;
    public static final int FT_DEVICE_4232H = 7;
    public static final int FT_DEVICE_232H = 8;
    public static final int FT_DEVICE_X_SERIES = 9;
    public static final int FT_DEVICE_4222_0 = 10;
    public static final int FT_DEVICE_4222_1_2 = 11;
    public static final int FT_DEVICE_4222_3 = 12;
    public static final byte FT_BITMODE_RESET = 0;
    public static final byte FT_BITMODE_ASYNC_BITBANG = 1;
    public static final byte FT_BITMODE_MPSSE = 2;
    public static final byte FT_BITMODE_SYNC_BITBANG = 4;
    public static final byte FT_BITMODE_MCU_HOST = 8;
    public static final byte FT_BITMODE_FAST_SERIAL = 16;
    public static final byte FT_BITMODE_CBUS_BITBANG = 32;
    public static final byte FT_BITMODE_SYNC_FIFO = 64;
    public static final int FTDI_BREAK_OFF = 0;
    public static final int FTDI_BREAK_ON = 16384;
    private static Context b = null;
    private static PendingIntent c = null;
    private static IntentFilter d = null;
    private static List<m> e = new ArrayList(Arrays.asList(new m[]{new m(1027, 24597), new m(1027, 24596), new m(1027, 24593), new m(1027, 24592), new m(1027, 24577), new m(1027, 24582), new m(1027, 24604), new m(1027, '贈'), new m(1027, '輸'), new m(1027, '遲'), new m(1027, '醙'), new m(1027, '鉶'), new m(1027, '陼'), new m(1027, 24594), new m(2220, 4133), new m(5590, 1), new m(1027, 24599)}));
    private ArrayList<FT_Device> f;
    private static UsbManager g;
    private BroadcastReceiver h = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String var3 = intent.getAction();
            UsbDevice var4 = null;
            FT_Device var5 = null;
            if("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(var3)) {
                var4 = (UsbDevice)intent.getParcelableExtra("device");

                for(var5 = D2xxManager.this.a(var4); var5 != null; var5 = D2xxManager.this.a(var4)) {
                    var5.close();
                    synchronized(D2xxManager.this.f) {
                        D2xxManager.this.f.remove(var5);
                    }
                }
            } else if("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(var3)) {
                var4 = (UsbDevice)intent.getParcelableExtra("device");
                D2xxManager.this.addUsbDevice(var4);
            }

        }
    };
    private static BroadcastReceiver i = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String var3 = intent.getAction();
            if("com.ftdi.j2xx".equals(var3)) {
                synchronized(this) {
                    UsbDevice var5 = (UsbDevice)intent.getParcelableExtra("device");
                    if(!intent.getBooleanExtra("permission", false)) {
                        Log.d("D2xx::", "permission denied for device " + var5);
                    }
                }
            }

        }
    };

    private FT_Device a(UsbDevice var1) {
        FT_Device var2 = null;
        ArrayList var3 = this.f;
        synchronized(this.f) {
            int var4 = this.f.size();

            for(int var5 = 0; var5 < var4; ++var5) {
                FT_Device var6 = (FT_Device)this.f.get(var5);
                UsbDevice var7 = var6.getUsbDevice();
                if(var7.equals(var1)) {
                    var2 = var6;
                    break;
                }
            }

            return var2;
        }
    }

    public boolean isFtDevice(UsbDevice dev) {
        boolean var3 = false;
        if(b == null) {
            return var3;
        } else {
            m var2 = new m(dev.getVendorId(), dev.getProductId());
            if(e.contains(var2)) {
                var3 = true;
            }

            Log.v("D2xx::", var2.toString());
            return var3;
        }
    }

    private static synchronized boolean a(Context context) {
        boolean result = false;
        if(context == null) {
            return result;
        } else {
            if(b != context) {
                b = context;
                c = PendingIntent.getBroadcast(b.getApplicationContext(), 0, new Intent("com.ftdi.j2xx"), 134217728);
                d = new IntentFilter("com.ftdi.j2xx");
                b.getApplicationContext().registerReceiver(i, d);
            }

            result = true;
            return result;
        }
    }

    private boolean b(UsbDevice var1) {
        boolean var2 = false;
        if(!g.hasPermission(var1)) {
            g.requestPermission(var1, c);
        }

        if(g.hasPermission(var1)) {
            var2 = true;
        }

        return var2;
    }

    private D2xxManager(Context parentContext) throws D2xxManager.D2xxException {
        Log.v("D2xx::", "Start constructor");
        if(parentContext == null) {
            throw new D2xxManager.D2xxException("D2xx init failed: Can not find parentContext!");
        } else {
            a(parentContext);
            if(!a()) {
                throw new D2xxManager.D2xxException("D2xx init failed: Can not find UsbManager!");
            } else {
                this.f = new ArrayList();
                IntentFilter var2 = new IntentFilter();
                var2.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
                var2.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
                parentContext.getApplicationContext().registerReceiver(this.h, var2);
                Log.v("D2xx::", "End constructor");
            }
        }
    }

    public static synchronized D2xxManager getInstance(Context parentContext) throws D2xxManager.D2xxException {
        if(a == null) {
            a = new D2xxManager(parentContext);
        }

        if(parentContext != null) {
            a(parentContext);
        }

        return a;
    }

    private static boolean a() {
        if(g == null && b != null) {
            g = (UsbManager)b.getApplicationContext().getSystemService("usb");
        }

        return g != null;
    }

    public boolean setVIDPID(int vendorId, int productId) {
        boolean var3 = false;
        if(vendorId != 0 && productId != 0) {
            m var4 = new m(vendorId, productId);
            if(e.contains(var4)) {
                Log.i("D2xx::", "Existing vid:" + vendorId + "  pid:" + productId);
                return true;
            }

            if(!e.add(var4)) {
                Log.d("D2xx::", "Failed to add VID/PID combination to list.");
            } else {
                var3 = true;
            }
        } else {
            Log.d("D2xx::", "Invalid parameter to setVIDPID");
        }

        return var3;
    }

    public int[][] getVIDPID() {
        int var1 = e.size();
        int[][] var2 = new int[2][var1];

        for(int var3 = 0; var3 < var1; ++var3) {
            m var4 = (m)e.get(var3);
            var2[0][var3] = var4.a();
            var2[1][var3] = var4.b();
        }

        return var2;
    }

    private void b() {
        ArrayList var1 = this.f;
        synchronized(this.f) {
            int var2 = this.f.size();

            for(int var3 = 0; var3 < var2; ++var3) {
                this.f.remove(var3);
            }

        }
    }

    public int createDeviceInfoList(Context parentContext) {
        HashMap var2 = g.getDeviceList();
        Iterator var3 = var2.values().iterator();
        ArrayList var4 = new ArrayList();
        FT_Device var5 = null;
        byte var6 = 0;
        if(parentContext == null) {
            return var6;
        } else {
            a(parentContext);

            while(true) {
                UsbDevice var7;
                do {
                    if(!var3.hasNext()) {
                        ArrayList var14 = this.f;
                        synchronized(this.f) {
                            this.b();
                            this.f = var4;
                            int numberOfDevices = this.f.size();
                            return numberOfDevices;
                        }
                    }

                    var7 = (UsbDevice)var3.next();
                } while(!this.isFtDevice(var7));

                boolean var8 = false;
                int var15 = var7.getInterfaceCount();

                for(int var9 = 0; var9 < var15; ++var9) {
                    if(this.b(var7)) {
                        ArrayList var10 = this.f;
                        synchronized(this.f) {
                            var5 = this.a(var7);
                            if(var5 == null) {
                                var5 = new FT_Device(parentContext, g, var7, var7.getInterface(var9));
                            } else {
                                this.f.remove(var5);
                                var5.a(parentContext);
                            }

                            var4.add(var5);
                        }
                    }
                }
            }
        }
    }

    public synchronized int getDeviceInfoList(int numDevs, D2xxManager.FtDeviceInfoListNode[] deviceList) {
        for(int var3 = 0; var3 < numDevs; ++var3) {
            deviceList[var3] = ((FT_Device)this.f.get(var3)).ftDeviceInfoListNode;
        }

        return this.f.size();
    }

    public synchronized D2xxManager.FtDeviceInfoListNode getDeviceInfoListDetail(int index) {
        return index <= this.f.size() && index >= 0?((FT_Device)this.f.get(index)).ftDeviceInfoListNode :null;
    }

    public static int getLibraryVersion() {
        return 537919488;
    }

    private boolean a(Context var1, FT_Device var2, D2xxManager.DriverParameters var3) {
        boolean var4 = false;
        if(var2 == null) {
            return var4;
        } else if(var1 == null) {
            return var4;
        } else {
            var2.a(var1);
            if(var3 != null) {
                var2.setDriverParameters(var3);
            }

            if(var2.a(g) && var2.isOpen()) {
                var4 = true;
            }

            return var4;
        }
    }

    public synchronized FT_Device openByUsbDevice(Context parentContext, UsbDevice dev, D2xxManager.DriverParameters params) {
        FT_Device var4 = null;
        if(this.isFtDevice(dev)) {
            var4 = this.a(dev);
            if(!this.a(parentContext, var4, params)) {
                var4 = null;
            }
        }

        return var4;
    }

    public synchronized FT_Device openByUsbDevice(Context parentContext, UsbDevice dev) {
        return this.openByUsbDevice(parentContext, dev, (D2xxManager.DriverParameters)null);
    }

    public synchronized FT_Device openByIndex(Context parentContext, int index, D2xxManager.DriverParameters params) {
        FT_Device var4 = null;
        if(index < 0) {
            return var4;
        } else if(parentContext == null) {
            return var4;
        } else {
            a(parentContext);
            var4 = (FT_Device)this.f.get(index);
            if(!this.a(parentContext, var4, params)) {
                var4 = null;
            }

            return var4;
        }
    }

    public synchronized FT_Device openByIndex(Context parentContext, int index) {
        return this.openByIndex(parentContext, index, (D2xxManager.DriverParameters)null);
    }

    // returns null if can't open
    public synchronized FT_Device openBySerialNumber(Context parentContext, String serialNumber, D2xxManager.DriverParameters params) {
        D2xxManager.FtDeviceInfoListNode infoListNode = null;
        FT_Device ftDevice = null;
        if(parentContext == null) {
            return ftDevice;
        } else {
            a(parentContext);

            for(int i = 0; i < this.f.size(); ++i) {
                FT_Device device = (FT_Device)this.f.get(i);
                if(device != null) {
                    infoListNode = device.ftDeviceInfoListNode;
                    if(infoListNode == null) {
                        Log.d("D2xx::", "***devInfo cannot be null***");
                    } else if(infoListNode.serialNumber.equals(serialNumber)) {
                        ftDevice = device;
                        break;
                    }
                }
            }

            if(!this.a(parentContext, ftDevice, params)) {
                ftDevice = null;
            }

            return ftDevice;
        }
    }

    public synchronized FT_Device openBySerialNumber(Context parentContext, String serialNumber) {
        return this.openBySerialNumber(parentContext, serialNumber, (D2xxManager.DriverParameters)null);
    }

    public synchronized FT_Device openByDescription(Context parentContext, String description, D2xxManager.DriverParameters params) {
        D2xxManager.FtDeviceInfoListNode var4 = null;
        FT_Device var5 = null;
        if(parentContext == null) {
            return var5;
        } else {
            a(parentContext);

            for(int var7 = 0; var7 < this.f.size(); ++var7) {
                FT_Device var6 = (FT_Device)this.f.get(var7);
                if(var6 != null) {
                    var4 = var6.ftDeviceInfoListNode;
                    if(var4 == null) {
                        Log.d("D2xx::", "***devInfo cannot be null***");
                    } else if(var4.description.equals(description)) {
                        var5 = var6;
                        break;
                    }
                }
            }

            if(!this.a(parentContext, var5, params)) {
                var5 = null;
            }

            return var5;
        }
    }

    public synchronized FT_Device openByDescription(Context parentContext, String description) {
        return this.openByDescription(parentContext, description, (D2xxManager.DriverParameters)null);
    }

    public synchronized FT_Device openByLocation(Context parentContext, int location, D2xxManager.DriverParameters params) {
        D2xxManager.FtDeviceInfoListNode var4 = null;
        FT_Device var5 = null;
        if(parentContext == null) {
            return var5;
        } else {
            a(parentContext);

            for(int var7 = 0; var7 < this.f.size(); ++var7) {
                FT_Device var6 = (FT_Device)this.f.get(var7);
                if(var6 != null) {
                    var4 = var6.ftDeviceInfoListNode;
                    if(var4 == null) {
                        Log.d("D2xx::", "***devInfo cannot be null***");
                    } else if(var4.location == location) {
                        var5 = var6;
                        break;
                    }
                }
            }

            if(!this.a(parentContext, var5, params)) {
                var5 = null;
            }

            return var5;
        }
    }

    public synchronized FT_Device openByLocation(Context parentContext, int location) {
        return this.openByLocation(parentContext, location, (D2xxManager.DriverParameters)null);
    }

    public int addUsbDevice(UsbDevice dev) {
        int var3 = 0;
        if(this.isFtDevice(dev)) {
            boolean var4 = false;
            int var8 = dev.getInterfaceCount();

            for(int var5 = 0; var5 < var8; ++var5) {
                if(this.b(dev)) {
                    ArrayList var6 = this.f;
                    synchronized(this.f) {
                        FT_Device var2 = this.a(dev);
                        if(var2 == null) {
                            var2 = new FT_Device(b, g, dev, dev.getInterface(var5));
                        } else {
                            var2.a(b);
                        }

                        this.f.add(var2);
                        ++var3;
                    }
                }
            }
        }

        return var3;
    }

    public static class D2xxException extends IOException {
        public D2xxException() {
        }

        public D2xxException(String ftStatusMsg) {
            super(ftStatusMsg);
        }
    }

    public static class DriverParameters {
        private int a = 16384;
        private int b = 16384;
        private int c = 16;
        private int d = 5000;

        public DriverParameters() {
        }

        public boolean setMaxBufferSize(int size) {
            boolean var2 = false;
            if(size >= 64 && size <= 262144) {
                this.a = size;
                var2 = true;
            } else {
                Log.e("D2xx::", "***bufferSize Out of correct range***");
            }

            return var2;
        }

        public int getMaxBufferSize() {
            return this.a;
        }

        public boolean setMaxTransferSize(int size) {
            boolean var2 = false;
            if(size >= 64 && size <= 262144) {
                this.b = size;
                var2 = true;
            } else {
                Log.e("D2xx::", "***maxTransferSize Out of correct range***");
            }

            return var2;
        }

        public int getMaxTransferSize() {
            return this.b;
        }

        public boolean setBufferNumber(int number) {
            boolean var2 = false;
            if(number >= 2 && number <= 16) {
                this.c = number;
                var2 = true;
            } else {
                Log.e("D2xx::", "***nrBuffers Out of correct range***");
            }

            return var2;
        }

        public int getBufferNumber() {
            return this.c;
        }

        public boolean setReadTimeout(int timeout) {
            this.d = timeout;
            return true;
        }

        public int getReadTimeout() {
            return this.d;
        }
    }

    public static class FtDeviceInfoListNode {
        public int flags;
        public short bcdDevice;
        public int type;
        public byte iSerialNumber;
        public int id;
        public int location;
        public String serialNumber;
        public String description;
        public int handle;
        public int breakOnParam;
        public short modemStatus;
        public short lineStatus;

        public FtDeviceInfoListNode() {
        }
    }
}
