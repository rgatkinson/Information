package com.ftdi.j2xx;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import android.util.Log;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbRequest;
import android.content.Context;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbDevice;

public class FT_Device
{
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
    
    public FT_Device(final Context j, final UsbManager usbManager, final UsbDevice c, final UsbInterface d) {
        this.t = 0;
        final byte[] array = new byte[255];
        this.j = j;
        this.s = new D2xxManager.DriverParameters();
        try {
            this.c = c;
            this.d = d;
            this.e = null;
            this.f = null;
            this.u = 0;
            this.h = new r();
            this.i = new q();
            this.g = new D2xxManager.FtDeviceInfoListNode();
            this.k = new UsbRequest();
            this.a(usbManager.openDevice(this.c));
            if (this.c() == null) {
                Log.e("FTDI_Device::", "Failed to open the device!");
                throw new D2xxManager.D2xxException("Failed to open the device!");
            }
        }
        catch (Exception ex) {
            if (ex.getMessage() != null) {
                Log.e("FTDI_Device::", ex.getMessage());
            }
            return;
        }
        this.c().claimInterface(this.d, false);
        final byte[] rawDescriptors = this.c().getRawDescriptors();
        final int deviceId = this.c.getDeviceId();
        this.t = 1 + this.d.getId();
        this.g.location = (deviceId << 4 | (0xF & this.t));
        final ByteBuffer allocate = ByteBuffer.allocate(2);
        allocate.order(ByteOrder.LITTLE_ENDIAN);
        allocate.put(rawDescriptors[12]);
        allocate.put(rawDescriptors[13]);
        this.g.bcdDevice = allocate.getShort(0);
        this.g.iSerialNumber = rawDescriptors[16];
        this.g.serialNumber = this.c().getSerial();
        this.g.id = (this.c.getVendorId() << 16 | this.c.getProductId());
        this.g.breakOnParam = 8;
        this.c().controlTransfer(-128, 6, 0x300 | rawDescriptors[15], 0, array, 255, 0);
        this.g.description = this.a(array);
        switch (0xFF00 & this.g.bcdDevice) {
            default: {
                this.g.type = 3;
                this.q = new k(this);
                break;
            }
            case 512: {
                if (this.g.iSerialNumber == 0) {
                    this.q = new f(this);
                    this.g.type = 0;
                    break;
                }
                this.g.type = 1;
                this.q = new e(this);
                break;
            }
            case 1024: {
                this.q = new f(this);
                this.g.type = 0;
                break;
            }
            case 1280: {
                this.q = new d(this);
                this.g.type = 4;
                this.n();
                break;
            }
            case 1536: {
                this.q = new k(this);
                final short n = (short)(0x1 & this.q.a((short)0));
                this.q = null;
                if (n == 0) {
                    this.g.type = 5;
                    this.q = new h(this);
                    break;
                }
                this.g.type = 5;
                this.q = new i(this);
                break;
            }
            case 1792: {
                this.g.type = 6;
                this.n();
                this.q = new c(this);
                break;
            }
            case 2048: {
                this.g.type = 7;
                this.n();
                this.q = new j(this);
                break;
            }
            case 2304: {
                this.g.type = 8;
                this.q = new g(this);
                break;
            }
            case 4096: {
                this.g.type = 9;
                this.q = new l(this);
                break;
            }
            case 6144: {
                this.g.type = 10;
                if (this.t == 1) {
                    this.g.flags = 2;
                    break;
                }
                this.g.flags = 0;
                break;
            }
            case 6400: {
                this.g.type = 11;
                if (this.t != 4) {
                    this.g.flags = 2;
                    break;
                }
                final int maxPacketSize = this.c.getInterface(-1 + this.t).getEndpoint(0).getMaxPacketSize();
                Log.e("dev", "mInterfaceID : " + this.t + "   iMaxPacketSize : " + maxPacketSize);
                if (maxPacketSize == 8) {
                    this.g.flags = 0;
                    break;
                }
                this.g.flags = 2;
                break;
            }
            case 5888: {
                this.g.type = 12;
                this.g.flags = 2;
                break;
            }
        }
        switch (0xFF00 & this.g.bcdDevice) {
            case 5888:
            case 6144:
            case 6400: {
                if (this.g.serialNumber == null) {
                    final byte[] array2 = new byte[16];
                    this.c().controlTransfer(-64, 144, 0, 27, array2, 16, 0);
                    String string = "";
                    for (int i = 0; i < 8; ++i) {
                        string = String.valueOf(string) + (char)array2[i * 2];
                    }
                    this.g.serialNumber = new String(string);
                    break;
                }
                break;
            }
        }
        switch (0xFF00 & this.g.bcdDevice) {
            case 6144:
            case 6400: {
                if (this.t == 1) {
                    final D2xxManager.FtDeviceInfoListNode g = this.g;
                    g.description = String.valueOf(g.description) + " A";
                    final D2xxManager.FtDeviceInfoListNode g2 = this.g;
                    g2.serialNumber = String.valueOf(g2.serialNumber) + "A";
                    break;
                }
                if (this.t == 2) {
                    final D2xxManager.FtDeviceInfoListNode g3 = this.g;
                    g3.description = String.valueOf(g3.description) + " B";
                    final D2xxManager.FtDeviceInfoListNode g4 = this.g;
                    g4.serialNumber = String.valueOf(g4.serialNumber) + "B";
                    break;
                }
                if (this.t == 3) {
                    final D2xxManager.FtDeviceInfoListNode g5 = this.g;
                    g5.description = String.valueOf(g5.description) + " C";
                    final D2xxManager.FtDeviceInfoListNode g6 = this.g;
                    g6.serialNumber = String.valueOf(g6.serialNumber) + "C";
                    break;
                }
                if (this.t == 4) {
                    final D2xxManager.FtDeviceInfoListNode g7 = this.g;
                    g7.description = String.valueOf(g7.description) + " D";
                    final D2xxManager.FtDeviceInfoListNode g8 = this.g;
                    g8.serialNumber = String.valueOf(g8.serialNumber) + "D";
                    break;
                }
                break;
            }
        }
        this.c().releaseInterface(this.d);
        this.c().close();
        this.a((UsbDeviceConnection)null);
        this.p();
    }
    
    private final String a(final byte[] array) throws UnsupportedEncodingException {
        return new String(array, 2, -2 + array[0], "UTF-16LE");
    }
    
    private boolean a(final int n) {
        final int n2 = n | this.g.breakOnParam;
        return this.isOpen() && this.c().controlTransfer(64, 4, n2, this.t, (byte[])null, 0, 0) == 0;
    }
    
    private boolean a(final boolean b, final boolean b2) {
        if (this.isOpen()) {
            if (b) {
                int i = 0;
                int n = 0;
                while (i < 6) {
                    final int controlTransfer = this.c().controlTransfer(64, 0, 1, this.t, (byte[])null, 0, 0);
                    ++i;
                    n = controlTransfer;
                }
                if (n > 0) {
                    return false;
                }
                this.p.e();
            }
            return b2 && this.c().controlTransfer(64, 0, 2, this.t, (byte[])null, 0, 0) == 0;
        }
        return false;
    }
    
    private final boolean f() {
        return this.i() || this.j() || this.b();
    }
    
    private final boolean g() {
        return this.m() || this.l() || this.k() || this.j() || this.b() || this.i() || this.h();
    }
    
    private final boolean h() {
        return (0xFF00 & this.g.bcdDevice) == 0x1000;
    }
    
    private final boolean i() {
        return (0xFF00 & this.g.bcdDevice) == 0x900;
    }
    
    private final boolean j() {
        return (0xFF00 & this.g.bcdDevice) == 0x700;
    }
    
    private final boolean k() {
        return (0xFF00 & this.g.bcdDevice) == 0x600;
    }
    
    private final boolean l() {
        return (0xFF00 & this.g.bcdDevice) == 0x500;
    }
    
    private final boolean m() {
        return (0xFF00 & this.g.bcdDevice) == 0x400 || ((0xFF00 & this.g.bcdDevice) == 0x200 && this.g.iSerialNumber == 0);
    }
    
    private void n() {
        if (this.t == 1) {
            final D2xxManager.FtDeviceInfoListNode g = this.g;
            g.serialNumber = String.valueOf(g.serialNumber) + "A";
            final D2xxManager.FtDeviceInfoListNode g2 = this.g;
            g2.description = String.valueOf(g2.description) + " A";
        }
        else {
            if (this.t == 2) {
                final D2xxManager.FtDeviceInfoListNode g3 = this.g;
                g3.serialNumber = String.valueOf(g3.serialNumber) + "B";
                final D2xxManager.FtDeviceInfoListNode g4 = this.g;
                g4.description = String.valueOf(g4.description) + " B";
                return;
            }
            if (this.t == 3) {
                final D2xxManager.FtDeviceInfoListNode g5 = this.g;
                g5.serialNumber = String.valueOf(g5.serialNumber) + "C";
                final D2xxManager.FtDeviceInfoListNode g6 = this.g;
                g6.description = String.valueOf(g6.description) + " C";
                return;
            }
            if (this.t == 4) {
                final D2xxManager.FtDeviceInfoListNode g7 = this.g;
                g7.serialNumber = String.valueOf(g7.serialNumber) + "D";
                final D2xxManager.FtDeviceInfoListNode g8 = this.g;
                g8.description = String.valueOf(g8.description) + " D";
            }
        }
    }
    
    private void o() {
        synchronized (this) {
            this.b = true;
            final D2xxManager.FtDeviceInfoListNode g = this.g;
            g.flags |= 0x1;
        }
    }
    
    private void p() {
        synchronized (this) {
            this.b = false;
            final D2xxManager.FtDeviceInfoListNode g = this.g;
            g.flags &= 0x2;
        }
    }
    
    private boolean q() {
        for (int i = 0; i < this.d.getEndpointCount(); ++i) {
            Log.i("FTDI_Device::", "EP: " + String.format("0x%02X", this.d.getEndpoint(i).getAddress()));
            if (this.d.getEndpoint(i).getType() == 2) {
                if (this.d.getEndpoint(i).getDirection() == 128) {
                    this.f = this.d.getEndpoint(i);
                    this.u = this.f.getMaxPacketSize();
                }
                else {
                    this.e = this.d.getEndpoint(i);
                }
            }
            else {
                Log.i("FTDI_Device::", "Not Bulk Endpoint");
            }
        }
        return this.e != null && this.f != null;
    }
    
    public int VendorCmdGet(final int n, final int n2, final byte[] array, final int n3) {
        if (!this.isOpen()) {
            Log.e("FTDI_Device::", "VendorCmdGet: Device not open");
            return -1;
        }
        if (n3 < 0) {
            Log.e("FTDI_Device::", "VendorCmdGet: Invalid data length");
            return -1;
        }
        if (array == null) {
            Log.e("FTDI_Device::", "VendorCmdGet: buf is null");
            return -1;
        }
        if (array.length < n3) {
            Log.e("FTDI_Device::", "VendorCmdGet: length of buffer is smaller than data length to get");
            return -1;
        }
        return this.c().controlTransfer(-64, n, n2, this.t, array, n3, 0);
    }
    
    public int VendorCmdSet(final int n, final int n2) {
        if (!this.isOpen()) {
            return -1;
        }
        return this.c().controlTransfer(64, n, n2, this.t, (byte[])null, 0, 0);
    }
    
    public int VendorCmdSet(final int n, final int n2, final byte[] array, final int n3) {
        if (!this.isOpen()) {
            Log.e("FTDI_Device::", "VendorCmdSet: Device not open");
            return -1;
        }
        if (n3 < 0) {
            Log.e("FTDI_Device::", "VendorCmdSet: Invalid data length");
            return -1;
        }
        if (array == null) {
            if (n3 > 0) {
                Log.e("FTDI_Device::", "VendorCmdSet: buf is null!");
                return -1;
            }
        }
        else if (array.length < n3) {
            Log.e("FTDI_Device::", "VendorCmdSet: length of buffer is smaller than data length to set");
            return -1;
        }
        return this.c().controlTransfer(64, n, n2, this.t, array, n3, 0);
    }
    
    void a(final UsbDeviceConnection l) {
        this.l = l;
    }
    
    final boolean a() {
        return this.l() || this.j() || this.b();
    }
    
    boolean a(final Context j) {
        // monitorenter(this)
        boolean b = false;
        if (j == null) {
            return b;
        }
        try {
            this.j = j;
            b = true;
            return b;
        }
        finally {
        }
        // monitorexit(this)
    }
    
    boolean a(final UsbManager usbManager) {
        while (true) {
            Label_0043: {
                synchronized (this) {
                    final boolean open = this.isOpen();
                    boolean b = false;
                    if (!open) {
                        if (usbManager != null) {
                            break Label_0043;
                        }
                        Log.e("FTDI_Device::", "UsbManager cannot be null.");
                        b = false;
                    }
                    return b;
                }
            }
            if (this.c() != null) {
                Log.e("FTDI_Device::", "There should not have an UsbConnection.");
                return false;
            }
            this.a(usbManager.openDevice(this.c));
            if (this.c() == null) {
                Log.e("FTDI_Device::", "UsbConnection cannot be null.");
                return false;
            }
            if (!this.c().claimInterface(this.d, true)) {
                Log.e("FTDI_Device::", "ClaimInteface returned false.");
                return false;
            }
            Log.d("FTDI_Device::", "open SUCCESS");
            if (!this.q()) {
                Log.e("FTDI_Device::", "Failed to find endpoints.");
                return false;
            }
            this.k.initialize(this.l, this.e);
            Log.d("D2XX::", "**********************Device Opened**********************");
            this.p = new o(this);
            this.m = new a(this, this.p, this.c(), this.f);
            (this.o = new Thread(this.m)).setName("bulkInThread");
            (this.n = new Thread(new p(this.p))).setName("processRequestThread");
            this.a(true, true);
            this.o.start();
            this.n.start();
            this.o();
            return true;
        }
    }
    
    final boolean b() {
        return (0xFF00 & this.g.bcdDevice) == 0x800;
    }
    
    UsbDeviceConnection c() {
        return this.l;
    }
    
    public void close() {
        synchronized (this) {
            if (this.n != null) {
                this.n.interrupt();
            }
            if (this.o != null) {
                this.o.interrupt();
            }
            if (this.l != null) {
                this.l.releaseInterface(this.d);
                this.l.close();
                this.l = null;
            }
            if (this.p != null) {
                this.p.g();
            }
            this.n = null;
            this.o = null;
            this.m = null;
            this.p = null;
            this.p();
        }
    }
    
    public boolean clrDtr() {
        int n = 1;
        if (!this.isOpen()) {
            return false;
        }
        if (this.c().controlTransfer(64, n, 256, this.t, (byte[])null, 0, 0) != 0) {
            n = 0;
        }
        return n != 0;
    }
    
    public boolean clrRts() {
        int n = 1;
        if (!this.isOpen()) {
            return false;
        }
        if (this.c().controlTransfer(64, n, 512, this.t, (byte[])null, 0, 0) != 0) {
            n = 0;
        }
        return n != 0;
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
        if (!this.isOpen()) {
            return -1;
        }
        return this.q.b();
    }
    
    public FT_EEPROM eepromRead() {
        if (!this.isOpen()) {
            return null;
        }
        return this.q.a();
    }
    
    public byte[] eepromReadUserArea(final int n) {
        if (!this.isOpen()) {
            return null;
        }
        return this.q.a(n);
    }
    
    public int eepromReadWord(final short n) {
        if (!this.isOpen()) {
            return -1;
        }
        return this.q.a(n);
    }
    
    public short eepromWrite(final FT_EEPROM ft_EEPROM) {
        if (!this.isOpen()) {
            return -1;
        }
        return this.q.a(ft_EEPROM);
    }
    
    public int eepromWriteUserArea(final byte[] array) {
        if (!this.isOpen()) {
            return 0;
        }
        return this.q.a(array);
    }
    
    public boolean eepromWriteWord(final short n, final short n2) {
        return this.isOpen() && this.q.a(n, n2);
    }
    
    public byte getBitMode() {
        final byte[] array = { 0 };
        if (!this.isOpen()) {
            return -1;
        }
        if (!this.g()) {
            return -2;
        }
        if (this.c().controlTransfer(-64, 12, 0, this.t, array, array.length, 0) == array.length) {
            return array[0];
        }
        return -3;
    }
    
    public D2xxManager.FtDeviceInfoListNode getDeviceInfo() {
        return this.g;
    }
    
    public long getEventStatus() {
        if (!this.isOpen()) {
            return -1L;
        }
        if (this.p == null) {
            return -2L;
        }
        final long a = this.a;
        this.a = 0L;
        return a;
    }
    
    public byte getLatencyTimer() {
        final byte[] array = { 0 };
        byte b;
        if (!this.isOpen()) {
            b = -1;
        }
        else {
            final int controlTransfer = this.c().controlTransfer(-64, 10, 0, this.t, array, array.length, 0);
            final int length = array.length;
            b = 0;
            if (controlTransfer == length) {
                return array[0];
            }
        }
        return b;
    }
    
    public short getLineStatus() {
        if (!this.isOpen()) {
            return -1;
        }
        if (this.p == null) {
            return -2;
        }
        return this.g.lineStatus;
    }
    
    public short getModemStatus() {
        if (!this.isOpen()) {
            return -1;
        }
        if (this.p == null) {
            return -2;
        }
        this.a &= 0xFFFFFFFFFFFFFFFDL;
        return (short)(0xFF & this.g.modemStatus);
    }
    
    public int getQueueStatus() {
        if (!this.isOpen()) {
            return -1;
        }
        if (this.p == null) {
            return -2;
        }
        return this.p.c();
    }
    
    public int getReadTimeout() {
        return this.s.getReadTimeout();
    }
    
    protected UsbDevice getUsbDevice() {
        return this.c;
    }
    
    public boolean isOpen() {
        synchronized (this) {
            return this.b;
        }
    }
    
    public boolean purge(final byte b) {
        boolean b2 = true;
        final boolean b3 = (b & 0x1) == (b2 ? 1 : 0) && b2;
        if ((b & 0x2) != 0x2) {
            b2 = false;
        }
        return this.a(b3, b2);
    }
    
    public int read(final byte[] array) {
        return this.read(array, array.length, this.s.getReadTimeout());
    }
    
    public int read(final byte[] array, final int n) {
        return this.read(array, n, this.s.getReadTimeout());
    }
    
    public int read(final byte[] array, final int n, final long n2) {
        if (!this.isOpen()) {
            return -1;
        }
        if (n <= 0) {
            return -2;
        }
        if (this.p == null) {
            return -3;
        }
        return this.p.a(array, n, n2);
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
    
    public boolean setBaudRate(final int n) {
        final int[] array = new int[2];
        if (this.isOpen()) {
            byte b = 0;
            switch (n) {
                default: {
                    if (this.f() && n >= 1200) {
                        b = com.ftdi.j2xx.b.a(n, array);
                        break;
                    }
                    b = com.ftdi.j2xx.b.a(n, array, this.g());
                    break;
                }
                case 300: {
                    array[0] = 10000;
                    b = 1;
                    break;
                }
                case 600: {
                    array[0] = 5000;
                    b = 1;
                    break;
                }
                case 1200: {
                    array[0] = 2500;
                    b = 1;
                    break;
                }
                case 2400: {
                    array[0] = 1250;
                    b = 1;
                    break;
                }
                case 4800: {
                    array[0] = 625;
                    b = 1;
                    break;
                }
                case 9600: {
                    array[0] = 16696;
                    b = 1;
                    break;
                }
                case 19200: {
                    array[0] = 32924;
                    b = 1;
                    break;
                }
                case 38400: {
                    array[0] = 49230;
                    b = 1;
                    break;
                }
                case 57600: {
                    array[0] = 52;
                    b = 1;
                    break;
                }
                case 115200: {
                    array[0] = 26;
                    b = 1;
                    break;
                }
                case 230400: {
                    array[0] = 13;
                    b = 1;
                    break;
                }
                case 460800: {
                    array[0] = 16390;
                    b = 1;
                    break;
                }
                case 921600: {
                    array[0] = 32771;
                    b = 1;
                    break;
                }
            }
            if (this.a() || this.i() || this.h()) {
                array[1] <<= 8;
                array[1] &= 0xFF00;
                array[1] |= this.t;
            }
            if (b == 1 && this.c().controlTransfer(64, 3, array[0], array[1], (byte[])null, 0, 0) == 0) {
                return true;
            }
        }
        return false;
    }
    
    public boolean setBitMode(final byte b, final byte b2) {
        boolean b3 = true;
        final int type = this.g.type;
        if (this.isOpen() && type != (b3 ? 1 : 0)) {
            if (type == 0 && b2 != 0) {
                if ((b2 & 0x1) == 0x0) {
                    return false;
                }
            }
            else if (type == 4 && b2 != 0) {
                if ((b2 & 0x1F) == 0x0) {
                    return false;
                }
                if ((b2 == 2 && b3) & (this.d.getId() != 0 && b3)) {
                    return false;
                }
            }
            else if (type == 5 && b2 != 0) {
                if ((b2 & 0x25) == 0x0) {
                    return false;
                }
            }
            else if (type == 6 && b2 != 0) {
                if ((b2 & 0x5F) == 0x0) {
                    return false;
                }
                if (((b2 & 0x48) > 0 && b3) & (this.d.getId() != 0 && b3)) {
                    return false;
                }
            }
            else if (type == 7 && b2 != 0) {
                if ((b2 & 0x7) == 0x0) {
                    return false;
                }
                if ((this.d.getId() != (b3 ? 1 : 0) && b3) & ((this.d.getId() != 0 && b3) & (b2 == 2 && b3))) {
                    return false;
                }
            }
            else if (type == 8 && b2 != 0 && b2 > 64) {
                return false;
            }
            if (this.c().controlTransfer(64, 11, b2 << 8 | (b & 0xFF), this.t, (byte[])null, 0, 0) != 0) {
                b3 = false;
            }
            return b3;
        }
        return false;
    }
    
    public boolean setBreakOff() {
        return this.a(0);
    }
    
    public boolean setBreakOn() {
        return this.a(16384);
    }
    
    public boolean setChars(final byte a, final byte b, final byte c, final byte d) {
        final r h = new r();
        h.a = a;
        h.b = b;
        h.c = c;
        h.d = d;
        if (this.isOpen()) {
            int n = a & 0xFF;
            if (b != 0) {
                n |= 0x100;
            }
            if (this.c().controlTransfer(64, 6, n, this.t, (byte[])null, 0, 0) == 0) {
                int n2 = c & 0xFF;
                if (d > 0) {
                    n2 |= 0x100;
                }
                if (this.c().controlTransfer(64, 7, n2, this.t, (byte[])null, 0, 0) == 0) {
                    this.h = h;
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean setDataCharacteristics(final byte b, final byte b2, final byte b3) {
        if (this.isOpen()) {
            final short breakOnParam = (short)((short)(b | b3 << 8) | b2 << 11);
            this.g.breakOnParam = breakOnParam;
            if (this.c().controlTransfer(64, 4, (int)breakOnParam, this.t, (byte[])null, 0, 0) == 0) {
                return true;
            }
        }
        return false;
    }
    
    protected void setDriverParameters(final D2xxManager.DriverParameters driverParameters) {
        this.s.setMaxBufferSize(driverParameters.getMaxBufferSize());
        this.s.setMaxTransferSize(driverParameters.getMaxTransferSize());
        this.s.setBufferNumber(driverParameters.getBufferNumber());
        this.s.setReadTimeout(driverParameters.getReadTimeout());
    }
    
    public boolean setDtr() {
        int n = 1;
        if (!this.isOpen()) {
            return false;
        }
        if (this.c().controlTransfer(64, n, 257, this.t, (byte[])null, 0, 0) != 0) {
            n = 0;
        }
        return n != 0;
    }
    
    public boolean setEventNotification(final long a) {
        if (this.isOpen() && a != 0L) {
            this.a = 0L;
            this.i.a = a;
            return true;
        }
        return false;
    }
    
    public boolean setFlowControl(final short n, final byte b, final byte b2) {
        final boolean open = this.isOpen();
        boolean b3 = false;
        if (open) {
            short n2;
            if (n == 1024) {
                n2 = (short)((short)(b2 << 8) | (b & 0xFF));
            }
            else {
                n2 = 0;
            }
            final int controlTransfer = this.c().controlTransfer(64, 2, (int)n2, n | this.t, (byte[])null, 0, 0);
            b3 = false;
            if (controlTransfer == 0) {
                b3 = true;
                if (n == 256) {
                    return this.setRts();
                }
                if (n == 512) {
                    return this.setDtr();
                }
            }
        }
        return b3;
    }
    
    public boolean setLatencyTimer(final byte r) {
        final int n = r & 0xFF;
        if (this.isOpen() && this.c().controlTransfer(64, 9, n, this.t, (byte[])null, 0, 0) == 0) {
            this.r = r;
            return true;
        }
        return false;
    }
    
    public boolean setRts() {
        int n = 1;
        if (!this.isOpen()) {
            return false;
        }
        if (this.c().controlTransfer(64, n, 514, this.t, (byte[])null, 0, 0) != 0) {
            n = 0;
        }
        return n != 0;
    }
    
    public void stopInTask() {
        try {
            if (!this.m.c()) {
                this.m.a();
            }
        }
        catch (InterruptedException ex) {
            Log.d("FTDI_Device::", "stopInTask called!");
            ex.printStackTrace();
        }
    }
    
    public boolean stoppedInTask() {
        return this.m.c();
    }
    
    public int write(final byte[] array) {
        return this.write(array, array.length, true);
    }
    
    public int write(final byte[] array, final int n) {
        return this.write(array, n, true);
    }
    
    public int write(final byte[] array, final int n, final boolean b) {
        int n2 = -1;
        if (this.isOpen() && n >= 0) {
            final UsbRequest k = this.k;
            if (b) {
                k.setClientData((Object)this);
            }
            if (n == 0) {
                if (k.queue(ByteBuffer.wrap(new byte[1]), n)) {
                    n2 = n;
                }
            }
            else if (k.queue(ByteBuffer.wrap(array), n)) {
                n2 = n;
            }
            if (b) {
                UsbRequest requestWait;
                do {
                    requestWait = this.l.requestWait();
                    if (requestWait != null) {
                        continue;
                    }
                    Log.e("FTDI_Device::", "UsbConnection.requestWait() == null");
                    return -99;
                } while (requestWait.getClientData() != this);
                return n2;
            }
        }
        return n2;
    }
}
