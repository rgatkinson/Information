//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ftdi.j2xx;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.util.Log;
import com.ftdi.j2xx.FT_EEPROM;
import com.ftdi.j2xx.a;
import com.ftdi.j2xx.b;
import com.ftdi.j2xx.c;
import com.ftdi.j2xx.d;
import com.ftdi.j2xx.e;
import com.ftdi.j2xx.f;
import com.ftdi.j2xx.g;
import com.ftdi.j2xx.h;
import com.ftdi.j2xx.i;
import com.ftdi.j2xx.j;
import com.ftdi.j2xx.k;
import com.ftdi.j2xx.l;
import com.ftdi.j2xx.o;
import com.ftdi.j2xx.p;
import com.ftdi.j2xx.q;
import com.ftdi.j2xx.r;
import com.ftdi.j2xx.D2xxManager.D2xxException;
import com.ftdi.j2xx.D2xxManager.DriverParameters;
import com.ftdi.j2xx.D2xxManager.FtDeviceInfoListNode;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FT_Device {
    long a;
    Boolean b;
    UsbDevice c;
    UsbInterface d;
    UsbEndpoint e;
    UsbEndpoint f;
    private UsbRequest k;
    private UsbDeviceConnection l;
    private a m;
    private Thread n;
    private Thread o;
    FtDeviceInfoListNode g;
    private o p;
    private k q;
    private byte r;
    r h;
    q i;
    private DriverParameters s;
    private int t = 0;
    Context j;
    private int u;

    public FT_Device(Context parentContext, UsbManager usbManager, UsbDevice dev, UsbInterface itf) {
        byte[] var6 = new byte[255];
        this.j = parentContext;
        this.s = new DriverParameters();

        try {
            this.c = dev;
            this.d = itf;
            this.e = null;
            this.f = null;
            this.u = 0;
            this.h = new r();
            this.i = new q();
            this.g = new FtDeviceInfoListNode();
            this.k = new UsbRequest();
            this.a(usbManager.openDevice(this.c));
            if(this.c() == null) {
                Log.e("FTDI_Device::", "Failed to open the device!");
                throw new D2xxException("Failed to open the device!");
            } else {
                this.c().claimInterface(this.d, false);
                byte[] var5 = this.c().getRawDescriptors();
                int var7 = this.c.getDeviceId();
                this.t = this.d.getId() + 1;
                this.g.location = var7 << 4 | this.t & 15;
                ByteBuffer var8 = ByteBuffer.allocate(2);
                var8.order(ByteOrder.LITTLE_ENDIAN);
                var8.put(var5[12]);
                var8.put(var5[13]);
                this.g.bcdDevice = var8.getShort(0);
                this.g.iSerialNumber = var5[16];
                this.g.serialNumber = this.c().getSerial();
                this.g.id = this.c.getVendorId() << 16 | this.c.getProductId();
                this.g.breakOnParam = 8;
                this.c().controlTransfer(-128, 6, 768 | var5[15], 0, var6, 255, 0);
                this.g.description = this.a(var6);
                switch(this.g.bcdDevice & 65280) {
                case 512:
                    if(this.g.iSerialNumber == 0) {
                        this.q = new f(this);
                        this.g.type = 0;
                    } else {
                        this.g.type = 1;
                        this.q = new e(this);
                    }
                    break;
                case 1024:
                    this.q = new f(this);
                    this.g.type = 0;
                    break;
                case 1280:
                    this.q = new d(this);
                    this.g.type = 4;
                    this.n();
                    break;
                case 1536:
                    this.q = new k(this);
                    short var9 = (short)(this.q.a((short)0) & 1);
                    this.q = null;
                    if(var9 == 0) {
                        this.g.type = 5;
                        this.q = new h(this);
                    } else {
                        this.g.type = 5;
                        this.q = new i(this);
                    }
                    break;
                case 1792:
                    this.g.type = 6;
                    this.n();
                    this.q = new c(this);
                    break;
                case 2048:
                    this.g.type = 7;
                    this.n();
                    this.q = new j(this);
                    break;
                case 2304:
                    this.g.type = 8;
                    this.q = new g(this);
                    break;
                case 4096:
                    this.g.type = 9;
                    this.q = new l(this);
                    break;
                case 5888:
                    this.g.type = 12;
                    this.g.flags = 2;
                    break;
                case 6144:
                    this.g.type = 10;
                    if(this.t == 1) {
                        this.g.flags = 2;
                    } else {
                        this.g.flags = 0;
                    }
                    break;
                case 6400:
                    this.g.type = 11;
                    if(this.t == 4) {
                        int var10 = this.c.getInterface(this.t - 1).getEndpoint(0).getMaxPacketSize();
                        Log.e("dev", "mInterfaceID : " + this.t + "   iMaxPacketSize : " + var10);
                        if(var10 == 8) {
                            this.g.flags = 0;
                        } else {
                            this.g.flags = 2;
                        }
                    } else {
                        this.g.flags = 2;
                    }
                    break;
                default:
                    this.g.type = 3;
                    this.q = new k(this);
                }

                switch(this.g.bcdDevice & 65280) {
                case 5888:
                case 6144:
                case 6400:
                    if(this.g.serialNumber == null) {
                        byte[] var13 = new byte[16];
                        this.c().controlTransfer(-64, 144, 0, 27, var13, 16, 0);
                        String var14 = "";

                        for(int var11 = 0; var11 < 8; ++var11) {
                            var14 = var14 + (char)var13[var11 * 2];
                        }

                        this.g.serialNumber = new String(var14);
                    }
                default:
                    switch(this.g.bcdDevice & 65280) {
                    case 6144:
                    case 6400:
                        if(this.t == 1) {
                            this.g.description = this.g.description + " A";
                            this.g.serialNumber = this.g.serialNumber + "A";
                        } else if(this.t == 2) {
                            this.g.description = this.g.description + " B";
                            this.g.serialNumber = this.g.serialNumber + "B";
                        } else if(this.t == 3) {
                            this.g.description = this.g.description + " C";
                            this.g.serialNumber = this.g.serialNumber + "C";
                        } else if(this.t == 4) {
                            this.g.description = this.g.description + " D";
                            this.g.serialNumber = this.g.serialNumber + "D";
                        }
                    default:
                        this.c().releaseInterface(this.d);
                        this.c().close();
                        this.a((UsbDeviceConnection)null);
                        this.p();
                    }
                }
            }
        } catch (Exception var12) {
            if(var12.getMessage() != null) {
                Log.e("FTDI_Device::", var12.getMessage());
            }

        }
    }

    private final boolean f() {
        return this.i() || this.j() || this.b();
    }

    private final boolean g() {
        return this.m() || this.l() || this.k() || this.j() || this.b() || this.i() || this.h();
    }

    final boolean a() {
        return this.l() || this.j() || this.b();
    }

    private final boolean h() {
        return (this.g.bcdDevice & '\uff00') == 4096;
    }

    private final boolean i() {
        return (this.g.bcdDevice & '\uff00') == 2304;
    }

    final boolean b() {
        return (this.g.bcdDevice & '\uff00') == 2048;
    }

    private final boolean j() {
        return (this.g.bcdDevice & '\uff00') == 1792;
    }

    private final boolean k() {
        return (this.g.bcdDevice & '\uff00') == 1536;
    }

    private final boolean l() {
        return (this.g.bcdDevice & '\uff00') == 1280;
    }

    private final boolean m() {
        return (this.g.bcdDevice & '\uff00') == 1024 || (this.g.bcdDevice & '\uff00') == 512 && this.g.iSerialNumber == 0;
    }

    private final String a(byte[] var1) throws UnsupportedEncodingException {
        return new String(var1, 2, var1[0] - 2, "UTF-16LE");
    }

    UsbDeviceConnection c() {
        return this.l;
    }

    void a(UsbDeviceConnection var1) {
        this.l = var1;
    }

    synchronized boolean a(Context var1) {
        boolean var2 = false;
        if(var1 != null) {
            this.j = var1;
            var2 = true;
        }

        return var2;
    }

    protected void setDriverParameters(DriverParameters params) {
        this.s.setMaxBufferSize(params.getMaxBufferSize());
        this.s.setMaxTransferSize(params.getMaxTransferSize());
        this.s.setBufferNumber(params.getBufferNumber());
        this.s.setReadTimeout(params.getReadTimeout());
    }

    DriverParameters d() {
        return this.s;
    }

    public int getReadTimeout() {
        return this.s.getReadTimeout();
    }

    private void n() {
        if(this.t == 1) {
            this.g.serialNumber = this.g.serialNumber + "A";
            this.g.description = this.g.description + " A";
        } else if(this.t == 2) {
            this.g.serialNumber = this.g.serialNumber + "B";
            this.g.description = this.g.description + " B";
        } else if(this.t == 3) {
            this.g.serialNumber = this.g.serialNumber + "C";
            this.g.description = this.g.description + " C";
        } else if(this.t == 4) {
            this.g.serialNumber = this.g.serialNumber + "D";
            this.g.description = this.g.description + " D";
        }

    }

    synchronized boolean a(UsbManager var1) {
        boolean var2 = false;
        if(this.isOpen()) {
            return var2;
        } else if(var1 == null) {
            Log.e("FTDI_Device::", "UsbManager cannot be null.");
            return var2;
        } else if(this.c() != null) {
            Log.e("FTDI_Device::", "There should not have an UsbConnection.");
            return var2;
        } else {
            this.a(var1.openDevice(this.c));
            if(this.c() == null) {
                Log.e("FTDI_Device::", "UsbConnection cannot be null.");
                return var2;
            } else if(!this.c().claimInterface(this.d, true)) {
                Log.e("FTDI_Device::", "ClaimInteface returned false.");
                return var2;
            } else {
                Log.d("FTDI_Device::", "open SUCCESS");
                if(!this.q()) {
                    Log.e("FTDI_Device::", "Failed to find endpoints.");
                    return var2;
                } else {
                    this.k.initialize(this.l, this.e);
                    Log.d("D2XX::", "**********************Device Opened**********************");
                    this.p = new o(this);
                    this.m = new a(this, this.p, this.c(), this.f);
                    this.o = new Thread(this.m);
                    this.o.setName("bulkInThread");
                    this.n = new Thread(new p(this.p));
                    this.n.setName("processRequestThread");
                    this.a(true, true);
                    this.o.start();
                    this.n.start();
                    this.o();
                    var2 = true;
                    return var2;
                }
            }
        }
    }

    public synchronized boolean isOpen() {
        return this.b.booleanValue();
    }

    private synchronized void o() {
        this.b = Boolean.valueOf(true);
        this.g.flags |= 1;
    }

    private synchronized void p() {
        this.b = Boolean.valueOf(false);
        this.g.flags &= 2;
    }

    public synchronized void close() {
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
    }

    protected UsbDevice getUsbDevice() {
        return this.c;
    }

    public FtDeviceInfoListNode getDeviceInfo() {
        return this.g;
    }

    public int read(byte[] data, int length, long wait_ms) {
        boolean var5 = false;
        if(!this.isOpen()) {
            return -1;
        } else if(length <= 0) {
            return -2;
        } else if(this.p == null) {
            return -3;
        } else {
            int var6 = this.p.a(data, length, wait_ms);
            return var6;
        }
    }

    public int read(byte[] data, int length) {
        return this.read(data, length, (long)this.s.getReadTimeout());
    }

    public int read(byte[] data) {
        return this.read(data, data.length, (long)this.s.getReadTimeout());
    }

    public int write(byte[] data, int length) {
        return this.write(data, length, true);
    }

    public int write(byte[] data, int length, boolean wait) {
        int var6 = -1;
        if(!this.isOpen()) {
            return var6;
        } else if(length < 0) {
            return var6;
        } else {
            UsbRequest var4 = this.k;
            if(wait) {
                var4.setClientData(this);
            }

            if(length == 0) {
                byte[] var7 = new byte[1];
                if(var4.queue(ByteBuffer.wrap(var7), length)) {
                    var6 = length;
                }
            } else if(var4.queue(ByteBuffer.wrap(data), length)) {
                var6 = length;
            }

            Object var5;
            if(wait) {
                do {
                    var4 = this.l.requestWait();
                    if(var4 == null) {
                        Log.e("FTDI_Device::", "UsbConnection.requestWait() == null");
                        return -99;
                    }

                    var5 = var4.getClientData();
                } while(var5 != this);
            }

            return var6;
        }
    }

    public int write(byte[] data) {
        return this.write(data, data.length, true);
    }

    public short getModemStatus() {
        if(!this.isOpen()) {
            return (short)-1;
        } else if(this.p == null) {
            return (short)-2;
        } else {
            this.a &= -3L;
            return (short)(this.g.modemStatus & 255);
        }
    }

    public short getLineStatus() {
        return !this.isOpen()?-1:(this.p == null?-2:this.g.lineStatus);
    }

    public int getQueueStatus() {
        return !this.isOpen()?-1:(this.p == null?-2:this.p.c());
    }

    public boolean readBufferFull() {
        return this.p.a();
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

    public boolean setBaudRate(int baudRate) {
        byte var2 = 1;
        int[] var3 = new int[2];
        boolean var4 = false;
        boolean var5 = false;
        if(!this.isOpen()) {
            return var5;
        } else {
            switch(baudRate) {
            case 300:
                var3[0] = 10000;
                break;
            case 600:
                var3[0] = 5000;
                break;
            case 1200:
                var3[0] = 2500;
                break;
            case 2400:
                var3[0] = 1250;
                break;
            case 4800:
                var3[0] = 625;
                break;
            case 9600:
                var3[0] = 16696;
                break;
            case 19200:
                var3[0] = '肜';
                break;
            case 38400:
                var3[0] = '쁎';
                break;
            case 57600:
                var3[0] = 52;
                break;
            case 115200:
                var3[0] = 26;
                break;
            case 230400:
                var3[0] = 13;
                break;
            case 460800:
                var3[0] = 16390;
                break;
            case 921600:
                var3[0] = '考';
                break;
            default:
                if(this.f() && baudRate >= 1200) {
                    var2 = b.a(baudRate, var3);
                } else {
                    var2 = b.a(baudRate, var3, this.g());
                }

                var4 = true;
            }

            if(this.a() || this.i() || this.h()) {
                var3[1] <<= 8;
                var3[1] &= '\uff00';
                var3[1] |= this.t;
            }

            if(var2 == 1) {
                int var6 = this.c().controlTransfer(64, 3, var3[0], var3[1], (byte[])null, 0, 0);
                if(var6 == 0) {
                    var5 = true;
                }
            }

            return var5;
        }
    }

    public boolean setDataCharacteristics(byte dataBits, byte stopBits, byte parity) {
        boolean var4 = false;
        boolean var5 = false;
        boolean var6 = false;
        if(!this.isOpen()) {
            return var6;
        } else {
            short var7 = (short)(dataBits | parity << 8);
            var7 = (short)(var7 | stopBits << 11);
            this.g.breakOnParam = var7;
            int var8 = this.c().controlTransfer(64, 4, var7, this.t, (byte[])null, 0, 0);
            if(var8 == 0) {
                var6 = true;
            }

            return var6;
        }
    }

    public boolean setBreakOn() {
        return this.a(16384);
    }

    public boolean setBreakOff() {
        return this.a(0);
    }

    private boolean a(int var1) {
        boolean var2 = false;
        int var4 = this.g.breakOnParam;
        var4 |= var1;
        if(!this.isOpen()) {
            return var2;
        } else {
            int var3 = this.c().controlTransfer(64, 4, var4, this.t, (byte[])null, 0, 0);
            if(var3 == 0) {
                var2 = true;
            }

            return var2;
        }
    }

    public boolean setFlowControl(short flowControl, byte xon, byte xoff) {
        boolean var4 = false;
        boolean var5 = false;
        short var6 = 0;
        if(!this.isOpen()) {
            return var4;
        } else {
            if(flowControl == 1024) {
                var6 = (short)(xoff << 8);
                var6 = (short)(var6 | xon & 255);
            }

            int var8 = this.c().controlTransfer(64, 2, var6, this.t | flowControl, (byte[])null, 0, 0);
            if(var8 == 0) {
                var4 = true;
                if(flowControl == 256) {
                    var4 = this.setRts();
                } else if(flowControl == 512) {
                    var4 = this.setDtr();
                }
            }

            return var4;
        }
    }

    public boolean setRts() {
        boolean var1 = false;
        short var3 = 514;
        if(!this.isOpen()) {
            return var1;
        } else {
            int var2 = this.c().controlTransfer(64, 1, var3, this.t, (byte[])null, 0, 0);
            if(var2 == 0) {
                var1 = true;
            }

            return var1;
        }
    }

    public boolean clrRts() {
        boolean var1 = false;
        short var3 = 512;
        if(!this.isOpen()) {
            return var1;
        } else {
            int var2 = this.c().controlTransfer(64, 1, var3, this.t, (byte[])null, 0, 0);
            if(var2 == 0) {
                var1 = true;
            }

            return var1;
        }
    }

    public boolean setDtr() {
        boolean var1 = false;
        short var3 = 257;
        if(!this.isOpen()) {
            return var1;
        } else {
            int var2 = this.c().controlTransfer(64, 1, var3, this.t, (byte[])null, 0, 0);
            if(var2 == 0) {
                var1 = true;
            }

            return var1;
        }
    }

    public boolean clrDtr() {
        boolean var1 = false;
        short var3 = 256;
        if(!this.isOpen()) {
            return var1;
        } else {
            int var2 = this.c().controlTransfer(64, 1, var3, this.t, (byte[])null, 0, 0);
            if(var2 == 0) {
                var1 = true;
            }

            return var1;
        }
    }

    public boolean setChars(byte eventChar, byte eventCharEnable, byte errorChar, byte errorCharEnable) {
        boolean var7 = false;
        r var8 = new r();
        var8.a = eventChar;
        var8.b = eventCharEnable;
        var8.c = errorChar;
        var8.d = errorCharEnable;
        if(!this.isOpen()) {
            return var7;
        } else {
            int var6 = eventChar & 255;
            if(eventCharEnable != 0) {
                var6 |= 256;
            }

            int var5 = this.c().controlTransfer(64, 6, var6, this.t, (byte[])null, 0, 0);
            if(var5 != 0) {
                return var7;
            } else {
                var6 = errorChar & 255;
                if(errorCharEnable > 0) {
                    var6 |= 256;
                }

                var5 = this.c().controlTransfer(64, 7, var6, this.t, (byte[])null, 0, 0);
                if(var5 == 0) {
                    this.h = var8;
                    var7 = true;
                }

                return var7;
            }
        }
    }

    public boolean setBitMode(byte mask, byte bitMode) {
        int var5 = this.g.type;
        boolean var6 = false;
        if(!this.isOpen()) {
            return var6;
        } else if(var5 == 1) {
            return var6;
        } else {
            if(var5 == 0 && bitMode != 0) {
                if((bitMode & 1) == 0) {
                    return var6;
                }
            } else if(var5 == 4 && bitMode != 0) {
                if((bitMode & 31) == 0) {
                    return var6;
                }

                if(bitMode == 2 & this.d.getId() != 0) {
                    return var6;
                }
            } else if(var5 == 5 && bitMode != 0) {
                if((bitMode & 37) == 0) {
                    return var6;
                }
            } else if(var5 == 6 && bitMode != 0) {
                if((bitMode & 95) == 0) {
                    return var6;
                }

                if((bitMode & 72) > 0 & this.d.getId() != 0) {
                    return var6;
                }
            } else if(var5 == 7 && bitMode != 0) {
                if((bitMode & 7) == 0) {
                    return var6;
                }

                if(bitMode == 2 & this.d.getId() != 0 & this.d.getId() != 1) {
                    return var6;
                }
            } else if(var5 == 8 && bitMode != 0 && bitMode > 64) {
                return var6;
            }

            int var3 = bitMode << 8;
            var3 |= mask & 255;
            int var4 = this.c().controlTransfer(64, 11, var3, this.t, (byte[])null, 0, 0);
            if(var4 == 0) {
                var6 = true;
            }

            return var6;
        }
    }

    public byte getBitMode() {
        boolean var1 = false;
        byte[] var2 = new byte[1];
        if(!this.isOpen()) {
            return (byte)-1;
        } else if(!this.g()) {
            return (byte)-2;
        } else {
            int var3 = this.c().controlTransfer(-64, 12, 0, this.t, var2, var2.length, 0);
            return var3 == var2.length?var2[0]:-3;
        }
    }

    public boolean resetDevice() {
        boolean var1 = false;
        boolean var2 = false;
        if(!this.isOpen()) {
            return var2;
        } else {
            int var3 = this.c().controlTransfer(64, 0, 0, 0, (byte[])null, 0, 0);
            if(var3 == 0) {
                var2 = true;
            }

            return var2;
        }
    }

    public int VendorCmdSet(int request, int wValue) {
        boolean var3 = false;
        if(!this.isOpen()) {
            return -1;
        } else {
            int var4 = this.c().controlTransfer(64, request, wValue, this.t, (byte[])null, 0, 0);
            return var4;
        }
    }

    public int VendorCmdSet(int request, int wValue, byte[] buf, int datalen) {
        boolean var5 = false;
        if(!this.isOpen()) {
            Log.e("FTDI_Device::", "VendorCmdSet: Device not open");
            return -1;
        } else if(datalen < 0) {
            Log.e("FTDI_Device::", "VendorCmdSet: Invalid data length");
            return -1;
        } else {
            if(buf == null) {
                if(datalen > 0) {
                    Log.e("FTDI_Device::", "VendorCmdSet: buf is null!");
                    return -1;
                }
            } else if(buf.length < datalen) {
                Log.e("FTDI_Device::", "VendorCmdSet: length of buffer is smaller than data length to set");
                return -1;
            }

            int var6 = this.c().controlTransfer(64, request, wValue, this.t, buf, datalen, 0);
            return var6;
        }
    }

    public int VendorCmdGet(int request, int wValue, byte[] buf, int datalen) {
        boolean var5 = false;
        if(!this.isOpen()) {
            Log.e("FTDI_Device::", "VendorCmdGet: Device not open");
            return -1;
        } else if(datalen < 0) {
            Log.e("FTDI_Device::", "VendorCmdGet: Invalid data length");
            return -1;
        } else if(buf == null) {
            Log.e("FTDI_Device::", "VendorCmdGet: buf is null");
            return -1;
        } else if(buf.length < datalen) {
            Log.e("FTDI_Device::", "VendorCmdGet: length of buffer is smaller than data length to get");
            return -1;
        } else {
            int var6 = this.c().controlTransfer(-64, request, wValue, this.t, buf, datalen, 0);
            return var6;
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

    public void restartInTask() {
        this.m.b();
    }

    public boolean stoppedInTask() {
        return this.m.c();
    }

    public boolean purge(byte flags) {
        boolean var2 = false;
        boolean var3 = false;
        boolean var4 = false;
        if((flags & 1) == 1) {
            var2 = true;
        }

        if((flags & 2) == 2) {
            var3 = true;
        }

        return this.a(var2, var3);
    }

    private boolean a(boolean var1, boolean var2) {
        boolean var3 = false;
        int var4 = 0;
        boolean var5 = false;
        if(!this.isOpen()) {
            return var3;
        } else {
            byte var7;
            if(var1) {
                var7 = 1;

                for(int var6 = 0; var6 < 6; ++var6) {
                    var4 = this.c().controlTransfer(64, 0, var7, this.t, (byte[])null, 0, 0);
                }

                if(var4 > 0) {
                    return var3;
                }

                this.p.e();
            }

            if(var2) {
                var7 = 2;
                var4 = this.c().controlTransfer(64, 0, var7, this.t, (byte[])null, 0, 0);
                if(var4 == 0) {
                    var3 = true;
                }
            }

            return var3;
        }
    }

    public boolean setLatencyTimer(byte latency) {
        boolean var4 = false;
        int var2 = latency & 255;
        if(!this.isOpen()) {
            return var4;
        } else {
            int var3 = this.c().controlTransfer(64, 9, var2, this.t, (byte[])null, 0, 0);
            if(var3 == 0) {
                this.r = latency;
                var4 = true;
            } else {
                var4 = false;
            }

            return var4;
        }
    }

    public byte getLatencyTimer() {
        byte[] var1 = new byte[1];
        boolean var2 = false;
        if(!this.isOpen()) {
            return (byte)-1;
        } else {
            int var3 = this.c().controlTransfer(-64, 10, 0, this.t, var1, var1.length, 0);
            return var3 == var1.length?var1[0]:0;
        }
    }

    public boolean setEventNotification(long Mask) {
        boolean var3 = false;
        if(!this.isOpen()) {
            return var3;
        } else {
            if(Mask != 0L) {
                this.a = 0L;
                this.i.a = Mask;
                var3 = true;
            }

            return var3;
        }
    }

    private boolean q() {
        for(int var1 = 0; var1 < this.d.getEndpointCount(); ++var1) {
            Log.i("FTDI_Device::", "EP: " + String.format("0x%02X", new Object[]{Integer.valueOf(this.d.getEndpoint(var1).getAddress())}));
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

    public FT_EEPROM eepromRead() {
        return !this.isOpen()?null:this.q.a();
    }

    public short eepromWrite(FT_EEPROM eeData) {
        return !this.isOpen()?-1:this.q.a(eeData);
    }

    public boolean eepromErase() {
        boolean var1 = false;
        if(!this.isOpen()) {
            return var1;
        } else {
            if(this.q.c() == 0) {
                var1 = true;
            }

            return var1;
        }
    }

    public int eepromWriteUserArea(byte[] data) {
        return !this.isOpen()?0:this.q.a(data);
    }

    public byte[] eepromReadUserArea(int length) {
        return !this.isOpen()?null:this.q.a(length);
    }

    public int eepromGetUserAreaSize() {
        return !this.isOpen()?-1:this.q.b();
    }

    public int eepromReadWord(short offset) {
        byte var2 = -1;
        if(!this.isOpen()) {
            return var2;
        } else {
            int var3 = this.q.a(offset);
            return var3;
        }
    }

    public boolean eepromWriteWord(short address, short data) {
        boolean var3 = false;
        if(!this.isOpen()) {
            return var3;
        } else {
            var3 = this.q.a(address, data);
            return var3;
        }
    }

    int e() {
        return this.u;
    }
}
