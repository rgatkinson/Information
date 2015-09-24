package com.ftdi.j2xx;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Iterator;
import android.util.Log;
import android.hardware.usb.UsbDevice;
import android.content.Intent;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import android.content.BroadcastReceiver;
import android.hardware.usb.UsbManager;
import java.util.List;
import android.content.IntentFilter;
import android.app.PendingIntent;
import android.content.Context;

public class D2xxManager
{
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
    public static final byte FT_DCD = Byte.MIN_VALUE;
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
    private static D2xxManager a;
    private static Context b;
    private static PendingIntent c;
    private static IntentFilter d;
    private static List<m> e;
    private static UsbManager g;
    private static BroadcastReceiver i;
    private ArrayList<FT_Device> f;
    private BroadcastReceiver h;
    
    static {
        D2xxManager.a = null;
        D2xxManager.b = null;
        D2xxManager.c = null;
        D2xxManager.d = null;
        D2xxManager.e = new ArrayList<m>(Arrays.asList(new m(1027, 24597), new m(1027, 24596), new m(1027, 24593), new m(1027, 24592), new m(1027, 24577), new m(1027, 24582), new m(1027, 24604), new m(1027, 64193), new m(1027, 64194), new m(1027, 64195), new m(1027, 64196), new m(1027, 64197), new m(1027, 64198), new m(1027, 24594), new m(2220, 4133), new m(5590, 1), new m(1027, 24599)));
        D2xxManager.i = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if ("com.ftdi.j2xx".equals(intent.getAction())) {
                    synchronized (this) {
                        final UsbDevice usbDevice = (UsbDevice)intent.getParcelableExtra("device");
                        if (!intent.getBooleanExtra("permission", false)) {
                            Log.d("D2xx::", "permission denied for device " + usbDevice);
                        }
                    }
                }
            }
        };
    }
    
    private D2xxManager(final Context context) throws D2xxException {
        this.h = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String action = intent.getAction();
                Label_0097: {
                    if (!"android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
                        break Label_0097;
                    }
                    final UsbDevice usbDevice = (UsbDevice)intent.getParcelableExtra("device");
                    FT_Device ft_Device = D2xxManager.this.a(usbDevice);
                    while (ft_Device != null) {
                        ft_Device.close();
                        synchronized (D2xxManager.this.f) {
                            D2xxManager.this.f.remove(ft_Device);
                            // monitorexit(D2xxManager.a(this.a))
                            ft_Device = D2xxManager.this.a(usbDevice);
                            continue;
                        }
                        break Label_0097;
                    }
                    return;
                }
                if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(action)) {
                    D2xxManager.this.addUsbDevice((UsbDevice)intent.getParcelableExtra("device"));
                }
            }
        };
        Log.v("D2xx::", "Start constructor");
        if (context == null) {
            throw new D2xxException("D2xx init failed: Can not find parentContext!");
        }
        a(context);
        if (!a()) {
            throw new D2xxException("D2xx init failed: Can not find UsbManager!");
        }
        this.f = new ArrayList<FT_Device>();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        context.getApplicationContext().registerReceiver(this.h, intentFilter);
        Log.v("D2xx::", "End constructor");
    }
    
    private FT_Device a(final UsbDevice usbDevice) {
        while (true) {
            while (true) {
                final int size;
                int n = 0;
                Label_0064: {
                    synchronized (this.f) {
                        size = this.f.size();
                        n = 0;
                        break Label_0064;
                        Label_0022: {
                            return;
                        }
                        final FT_Device ft_Device = this.f.get(n);
                        // iftrue(Label_0022:, ft_Device.getUsbDevice().equals((Object)usbDevice))
                        ++n;
                    }
                }
                if (n >= size) {
                    return null;
                }
                continue;
            }
        }
    }
    
    private static boolean a() {
        if (D2xxManager.g == null && D2xxManager.b != null) {
            D2xxManager.g = (UsbManager)D2xxManager.b.getApplicationContext().getSystemService("usb");
        }
        return D2xxManager.g != null;
    }
    
    private static boolean a(final Context b) {
        // monitorenter(D2xxManager.class)
        boolean b2 = false;
        if (b != null) {
            try {
                if (D2xxManager.b != b) {
                    D2xxManager.b = b;
                    D2xxManager.c = PendingIntent.getBroadcast(D2xxManager.b.getApplicationContext(), 0, new Intent("com.ftdi.j2xx"), 134217728);
                    D2xxManager.d = new IntentFilter("com.ftdi.j2xx");
                    D2xxManager.b.getApplicationContext().registerReceiver(D2xxManager.i, D2xxManager.d);
                }
                b2 = true;
            }
            finally {
            }
            // monitorexit(D2xxManager.class)
        }
        // monitorexit(D2xxManager.class)
        return b2;
    }
    
    private boolean a(final Context context, final FT_Device ft_Device, final DriverParameters driverParameters) {
        if (ft_Device != null && context != null) {
            ft_Device.a(context);
            if (driverParameters != null) {
                ft_Device.setDriverParameters(driverParameters);
            }
            if (ft_Device.a(D2xxManager.g) && ft_Device.isOpen()) {
                return true;
            }
        }
        return false;
    }
    
    private void b() {
        synchronized (this.f) {
            for (int size = this.f.size(), i = 0; i < size; ++i) {
                this.f.remove(i);
            }
        }
    }
    
    private boolean b(final UsbDevice usbDevice) {
        if (!D2xxManager.g.hasPermission(usbDevice)) {
            D2xxManager.g.requestPermission(usbDevice, D2xxManager.c);
        }
        final boolean hasPermission = D2xxManager.g.hasPermission(usbDevice);
        boolean b = false;
        if (hasPermission) {
            b = true;
        }
        return b;
    }
    
    public static D2xxManager getInstance(final Context context) throws D2xxException {
        synchronized (D2xxManager.class) {
            if (D2xxManager.a == null) {
                D2xxManager.a = new D2xxManager(context);
            }
            if (context != null) {
                a(context);
            }
            return D2xxManager.a;
        }
    }
    
    public static int getLibraryVersion() {
        return 537919488;
    }
    
    public int addUsbDevice(final UsbDevice usbDevice) {
        final boolean ftDevice = this.isFtDevice(usbDevice);
        int n = 0;
        if (ftDevice) {
            for (int interfaceCount = usbDevice.getInterfaceCount(), i = 0; i < interfaceCount; ++i) {
                if (this.b(usbDevice)) {
                    while (true) {
                        while (true) {
                            FT_Device a;
                            synchronized (this.f) {
                                a = this.a(usbDevice);
                                if (a == null) {
                                    a = new FT_Device(D2xxManager.b, D2xxManager.g, usbDevice, usbDevice.getInterface(i));
                                    this.f.add(a);
                                    ++n;
                                    break;
                                }
                            }
                            a.a(D2xxManager.b);
                            continue;
                        }
                    }
                }
            }
        }
        return n;
    }
    
    public int createDeviceInfoList(final Context context) {
        final Iterator<UsbDevice> iterator = D2xxManager.g.getDeviceList().values().iterator();
        final ArrayList<FT_Device> f = new ArrayList<FT_Device>();
        if (context == null) {
            return 0;
        }
        a(context);
        while (true) {
            if (!iterator.hasNext()) {
                synchronized (this.f) {
                    this.b();
                    this.f = f;
                    return this.f.size();
                }
            }
            final UsbDevice usbDevice = iterator.next();
            if (this.isFtDevice(usbDevice)) {
                for (int interfaceCount = usbDevice.getInterfaceCount(), i = 0; i < interfaceCount; ++i) {
                    if (this.b(usbDevice)) {
                        while (true) {
                            while (true) {
                                FT_Device a;
                                synchronized (this.f) {
                                    a = this.a(usbDevice);
                                    if (a == null) {
                                        a = new FT_Device(context, D2xxManager.g, usbDevice, usbDevice.getInterface(i));
                                        f.add(a);
                                        break;
                                    }
                                }
                                this.f.remove(a);
                                a.a(context);
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }
    
    public int getDeviceInfoList(final int n, final FtDeviceInfoListNode[] array) {
        // monitorenter(this)
        int n2 = 0;
        while (true) {
            Label_0023: {
                if (n2 < n) {
                    break Label_0023;
                }
                try {
                    return this.f.size();
                    array[n2] = this.f.get(n2).g;
                    ++n2;
                }
                finally {
                }
                // monitorexit(this)
            }
        }
    }
    
    public FtDeviceInfoListNode getDeviceInfoListDetail(final int n) {
        synchronized (this) {
            Object g;
            if (n > this.f.size() || n < 0) {
                g = null;
            }
            else {
                g = this.f.get(n).g;
            }
            return (FtDeviceInfoListNode)g;
        }
    }
    
    public int[][] getVIDPID() {
        final int size = D2xxManager.e.size();
        final int[][] array = (int[][])Array.newInstance(Integer.TYPE, 2, size);
        for (int i = 0; i < size; ++i) {
            final m m = D2xxManager.e.get(i);
            array[0][i] = m.a();
            array[1][i] = m.b();
        }
        return array;
    }
    
    public boolean isFtDevice(final UsbDevice usbDevice) {
        if (D2xxManager.b == null) {
            return false;
        }
        final m m = new m(usbDevice.getVendorId(), usbDevice.getProductId());
        final boolean contains = D2xxManager.e.contains(m);
        boolean b = false;
        if (contains) {
            b = true;
        }
        Log.v("D2xx::", m.toString());
        return b;
    }
    
    public FT_Device openByDescription(final Context context, final String s) {
        synchronized (this) {
            return this.openByDescription(context, s, null);
        }
    }
    
    public FT_Device openByDescription(final Context context, final String s, final DriverParameters driverParameters) {
        // monitorenter(this)
        FT_Device ft_Device = null;
        if (context != null) {
            FT_Device ft_Device2;
            while (true) {
                while (true) {
                    int n = 0;
                    Label_0130: {
                        try {
                            a(context);
                            n = 0;
                            if (n >= this.f.size()) {
                                ft_Device2 = null;
                            }
                            else {
                                ft_Device2 = this.f.get(n);
                                if (ft_Device2 == null) {
                                    break Label_0130;
                                }
                                final FtDeviceInfoListNode g = ft_Device2.g;
                                if (g == null) {
                                    Log.d("D2xx::", "***devInfo cannot be null***");
                                    break Label_0130;
                                }
                                if (!g.description.equals(s)) {
                                    break Label_0130;
                                }
                            }
                            if (!this.a(context, ft_Device2, driverParameters)) {
                                ft_Device2 = null;
                            }
                        }
                        finally {
                        }
                        // monitorexit(this)
                        break;
                    }
                    ++n;
                    continue;
                }
            }
            ft_Device = ft_Device2;
        }
        // monitorexit(this)
        return ft_Device;
    }
    
    public FT_Device openByIndex(final Context context, final int n) {
        synchronized (this) {
            return this.openByIndex(context, n, null);
        }
    }
    
    public FT_Device openByIndex(final Context context, final int n, final DriverParameters driverParameters) {
        // monitorenter(this)
        FT_Device ft_Device = null;
        if (n >= 0) {
            ft_Device = null;
            if (context != null) {
                try {
                    a(context);
                    FT_Device ft_Device2 = this.f.get(n);
                    if (!this.a(context, ft_Device2, driverParameters)) {
                        ft_Device2 = null;
                    }
                    ft_Device = ft_Device2;
                }
                finally {
                }
                // monitorexit(this)
            }
        }
        // monitorexit(this)
        return ft_Device;
    }
    
    public FT_Device openByLocation(final Context context, final int n) {
        synchronized (this) {
            return this.openByLocation(context, n, null);
        }
    }
    
    public FT_Device openByLocation(final Context context, final int n, final DriverParameters driverParameters) {
        // monitorenter(this)
        FT_Device ft_Device = null;
        if (context != null) {
            FT_Device ft_Device2;
            while (true) {
                while (true) {
                    int n2 = 0;
                    Label_0127: {
                        try {
                            a(context);
                            n2 = 0;
                            if (n2 >= this.f.size()) {
                                ft_Device2 = null;
                            }
                            else {
                                ft_Device2 = this.f.get(n2);
                                if (ft_Device2 == null) {
                                    break Label_0127;
                                }
                                final FtDeviceInfoListNode g = ft_Device2.g;
                                if (g == null) {
                                    Log.d("D2xx::", "***devInfo cannot be null***");
                                    break Label_0127;
                                }
                                if (g.location != n) {
                                    break Label_0127;
                                }
                            }
                            if (!this.a(context, ft_Device2, driverParameters)) {
                                ft_Device2 = null;
                            }
                        }
                        finally {
                        }
                        // monitorexit(this)
                        break;
                    }
                    ++n2;
                    continue;
                }
            }
            ft_Device = ft_Device2;
        }
        // monitorexit(this)
        return ft_Device;
    }
    
    public FT_Device openBySerialNumber(final Context context, final String s) {
        synchronized (this) {
            return this.openBySerialNumber(context, s, null);
        }
    }
    
    public FT_Device openBySerialNumber(final Context context, final String s, final DriverParameters driverParameters) {
        // monitorenter(this)
        FT_Device ft_Device = null;
        if (context != null) {
            FT_Device ft_Device2;
            while (true) {
                while (true) {
                    int n = 0;
                    Label_0130: {
                        try {
                            a(context);
                            n = 0;
                            if (n >= this.f.size()) {
                                ft_Device2 = null;
                            }
                            else {
                                ft_Device2 = this.f.get(n);
                                if (ft_Device2 == null) {
                                    break Label_0130;
                                }
                                final FtDeviceInfoListNode g = ft_Device2.g;
                                if (g == null) {
                                    Log.d("D2xx::", "***devInfo cannot be null***");
                                    break Label_0130;
                                }
                                if (!g.serialNumber.equals(s)) {
                                    break Label_0130;
                                }
                            }
                            if (!this.a(context, ft_Device2, driverParameters)) {
                                ft_Device2 = null;
                            }
                        }
                        finally {
                        }
                        // monitorexit(this)
                        break;
                    }
                    ++n;
                    continue;
                }
            }
            ft_Device = ft_Device2;
        }
        // monitorexit(this)
        return ft_Device;
    }
    
    public FT_Device openByUsbDevice(final Context context, final UsbDevice usbDevice) {
        synchronized (this) {
            return this.openByUsbDevice(context, usbDevice, null);
        }
    }
    
    public FT_Device openByUsbDevice(final Context context, final UsbDevice usbDevice, final DriverParameters driverParameters) {
        while (true) {
            final FT_Device a;
            synchronized (this) {
                final boolean ftDevice = this.isFtDevice(usbDevice);
                FT_Device ft_Device = null;
                if (ftDevice) {
                    a = this.a(usbDevice);
                    final boolean a2 = this.a(context, a, driverParameters);
                    ft_Device = null;
                    if (a2) {
                        return a;
                    }
                }
                return ft_Device;
            }
            return a;
        }
    }
    
    public boolean setVIDPID(final int n, final int n2) {
        boolean b = false;
        if (n != 0 && n2 != 0) {
            final m m = new m(n, n2);
            if (D2xxManager.e.contains(m)) {
                Log.i("D2xx::", "Existing vid:" + n + "  pid:" + n2);
                return true;
            }
            if (!D2xxManager.e.add(m)) {
                Log.d("D2xx::", "Failed to add VID/PID combination to list.");
            }
            else {
                b = true;
            }
        }
        else {
            Log.d("D2xx::", "Invalid parameter to setVIDPID");
            b = false;
        }
        return b;
    }
    
    public static class D2xxException extends IOException
    {
        public D2xxException() {
        }
        
        public D2xxException(final String s) {
            super(s);
        }
    }
    
    public static class DriverParameters
    {
        private int a;
        private int b;
        private int c;
        private int d;
        
        public DriverParameters() {
            this.a = 16384;
            this.b = 16384;
            this.c = 16;
            this.d = 5000;
        }
        
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
        
        public boolean setBufferNumber(final int c) {
            if (c >= 2 && c <= 16) {
                this.c = c;
                return true;
            }
            Log.e("D2xx::", "***nrBuffers Out of correct range***");
            return false;
        }
        
        public boolean setMaxBufferSize(final int a) {
            if (a >= 64 && a <= 262144) {
                this.a = a;
                return true;
            }
            Log.e("D2xx::", "***bufferSize Out of correct range***");
            return false;
        }
        
        public boolean setMaxTransferSize(final int b) {
            if (b >= 64 && b <= 262144) {
                this.b = b;
                return true;
            }
            Log.e("D2xx::", "***maxTransferSize Out of correct range***");
            return false;
        }
        
        public boolean setReadTimeout(final int d) {
            this.d = d;
            return true;
        }
    }
    
    public static class FtDeviceInfoListNode
    {
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
