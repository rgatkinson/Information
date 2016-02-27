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

import com.ftdi.j2xx.D2xxManager.D2xxException;
import com.ftdi.j2xx.D2xxManager.DriverParameters;
import com.ftdi.j2xx.D2xxManager.FtDeviceInfoListNode;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FT_Device {
    long a;
    Boolean b;
    UsbDevice usbDevice;
    UsbInterface usbInterface;
    UsbEndpoint e;
    UsbEndpoint f;
    private UsbRequest usbRequest;
    private UsbDeviceConnection usbDeviceConnection;
    private BulkInRunnable bulkInRunnable;
    private Thread processRequestThread;
    private Thread bulkInThread;
    FtDeviceInfoListNode ftDeviceInfoListNode;
    private ProcessInCtrl processInCtrl;
    private FT_EE_Ctrl ft_ee_ctrl;
    private byte r;
    r h;
    q i;
    private DriverParameters driverParameters;
    private int intfId = 0;
    Context context;
    private int u;

    public FT_Device(Context parentContext, UsbManager usbManager, UsbDevice dev, UsbInterface itf) {
        byte[] var6 = new byte[255];
        this.context = parentContext;
        this.driverParameters = new DriverParameters();

        try {
            this.usbDevice = dev;
            this.usbInterface = itf;
            this.e = null;
            this.f = null;
            this.u = 0;
            this.h = new r();
            this.i = new q();
            this.ftDeviceInfoListNode = new FtDeviceInfoListNode();
            this.usbRequest = new UsbRequest();
            this.setUsbDeviceConnection(usbManager.openDevice(this.usbDevice));
            if(this.getUsbDeviceConnection() == null) {
                Log.e("FTDI_Device::", "Failed to open the device!");
                throw new D2xxException("Failed to open the device!");
            } else {
                this.getUsbDeviceConnection().claimInterface(this.usbInterface, false);
                byte[] var5 = this.getUsbDeviceConnection().getRawDescriptors();
                int deviceId = this.usbDevice.getDeviceId();
                this.intfId = this.usbInterface.getId() + 1;
                this.ftDeviceInfoListNode.location = deviceId << 4 | this.intfId & 15;
                ByteBuffer var8 = ByteBuffer.allocate(2);
                var8.order(ByteOrder.LITTLE_ENDIAN);
                var8.put(var5[12]);
                var8.put(var5[13]);
                this.ftDeviceInfoListNode.bcdDevice = var8.getShort(0);
                this.ftDeviceInfoListNode.iSerialNumber = var5[16];
                this.ftDeviceInfoListNode.serialNumber = this.getUsbDeviceConnection().getSerial();
                this.ftDeviceInfoListNode.id = this.usbDevice.getVendorId() << 16 | this.usbDevice.getProductId();
                this.ftDeviceInfoListNode.breakOnParam = 8;
                this.getUsbDeviceConnection().controlTransfer(-128, 6, 768 | var5[15], 0, var6, 255, 0);
                this.ftDeviceInfoListNode.description = this.a(var6);
                switch(this.ftDeviceInfoListNode.bcdDevice & 65280) {
                case 512:
                    if(this.ftDeviceInfoListNode.iSerialNumber == 0) {
                        this.ft_ee_ctrl = new f(this);
                        this.ftDeviceInfoListNode.type = 0;
                    } else {
                        this.ftDeviceInfoListNode.type = 1;
                        this.ft_ee_ctrl = new e(this);
                    }
                    break;
                case 1024:
                    this.ft_ee_ctrl = new f(this);
                    this.ftDeviceInfoListNode.type = 0;
                    break;
                case 1280:
                    this.ft_ee_ctrl = new d(this);
                    this.ftDeviceInfoListNode.type = 4;
                    this.n();
                    break;
                case 1536:
                    this.ft_ee_ctrl = new FT_EE_Ctrl(this);
                    short var9 = (short)(this.ft_ee_ctrl.a((short)0) & 1);
                    this.ft_ee_ctrl = null;
                    if(var9 == 0) {
                        this.ftDeviceInfoListNode.type = 5;
                        this.ft_ee_ctrl = new h(this);
                    } else {
                        this.ftDeviceInfoListNode.type = 5;
                        this.ft_ee_ctrl = new i(this);
                    }
                    break;
                case 1792:
                    this.ftDeviceInfoListNode.type = 6;
                    this.n();
                    this.ft_ee_ctrl = new c(this);
                    break;
                case 2048:
                    this.ftDeviceInfoListNode.type = 7;
                    this.n();
                    this.ft_ee_ctrl = new j(this);
                    break;
                case 2304:
                    this.ftDeviceInfoListNode.type = 8;
                    this.ft_ee_ctrl = new g(this);
                    break;
                case 4096:
                    this.ftDeviceInfoListNode.type = 9;
                    this.ft_ee_ctrl = new l(this);
                    break;
                case 5888:
                    this.ftDeviceInfoListNode.type = 12;
                    this.ftDeviceInfoListNode.flags = 2;
                    break;
                case 6144:
                    this.ftDeviceInfoListNode.type = 10;
                    if(this.intfId == 1) {
                        this.ftDeviceInfoListNode.flags = 2;
                    } else {
                        this.ftDeviceInfoListNode.flags = 0;
                    }
                    break;
                case 6400:
                    this.ftDeviceInfoListNode.type = 11;
                    if(this.intfId == 4) {
                        int var10 = this.usbDevice.getInterface(this.intfId - 1).getEndpoint(0).getMaxPacketSize();
                        Log.e("dev", "mInterfaceID : " + this.intfId + "   iMaxPacketSize : " + var10);
                        if(var10 == 8) {
                            this.ftDeviceInfoListNode.flags = 0;
                        } else {
                            this.ftDeviceInfoListNode.flags = 2;
                        }
                    } else {
                        this.ftDeviceInfoListNode.flags = 2;
                    }
                    break;
                default:
                    this.ftDeviceInfoListNode.type = 3;
                    this.ft_ee_ctrl = new FT_EE_Ctrl(this);
                }

                switch(this.ftDeviceInfoListNode.bcdDevice & 65280) {
                case 5888:
                case 6144:
                case 6400:
                    if(this.ftDeviceInfoListNode.serialNumber == null) {
                        byte[] var13 = new byte[16];
                        this.getUsbDeviceConnection().controlTransfer(-64, 144, 0, 27, var13, 16, 0);
                        String var14 = "";

                        for(int var11 = 0; var11 < 8; ++var11) {
                            var14 = var14 + (char)var13[var11 * 2];
                        }

                        this.ftDeviceInfoListNode.serialNumber = new String(var14);
                    }
                default:
                    switch(this.ftDeviceInfoListNode.bcdDevice & 65280) {
                    case 6144:
                    case 6400:
                        if(this.intfId == 1) {
                            this.ftDeviceInfoListNode.description = this.ftDeviceInfoListNode.description + " A";
                            this.ftDeviceInfoListNode.serialNumber = this.ftDeviceInfoListNode.serialNumber + "A";
                        } else if(this.intfId == 2) {
                            this.ftDeviceInfoListNode.description = this.ftDeviceInfoListNode.description + " B";
                            this.ftDeviceInfoListNode.serialNumber = this.ftDeviceInfoListNode.serialNumber + "B";
                        } else if(this.intfId == 3) {
                            this.ftDeviceInfoListNode.description = this.ftDeviceInfoListNode.description + " C";
                            this.ftDeviceInfoListNode.serialNumber = this.ftDeviceInfoListNode.serialNumber + "C";
                        } else if(this.intfId == 4) {
                            this.ftDeviceInfoListNode.description = this.ftDeviceInfoListNode.description + " D";
                            this.ftDeviceInfoListNode.serialNumber = this.ftDeviceInfoListNode.serialNumber + "D";
                        }
                    default:
                        this.getUsbDeviceConnection().releaseInterface(this.usbInterface);
                        this.getUsbDeviceConnection().close();
                        this.setUsbDeviceConnection((UsbDeviceConnection) null);
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

    private final boolean isDeviceBCD() {
        return this.isDeviceB() || this.isDeviceD() || this.isDeviceC();
    }

    private final boolean isDeviceAThroughG() {
        return this.isDeviceG() || this.isDeviceF() || this.isDeviceE() || this.isDeviceD() || this.isDeviceC() || this.isDeviceB() || this.isDeviceA();
    }

    final boolean isDeviceCDF() {
        return this.isDeviceF() || this.isDeviceD() || this.isDeviceC();
    }

    private final boolean isDeviceA() {
        return (this.ftDeviceInfoListNode.bcdDevice & '\uff00') == 4096;
    }

    private final boolean isDeviceB() {
        return (this.ftDeviceInfoListNode.bcdDevice & '\uff00') == 2304;
    }

    final boolean isDeviceC() {
        return (this.ftDeviceInfoListNode.bcdDevice & '\uff00') == 2048;
    }

    private final boolean isDeviceD() {
        return (this.ftDeviceInfoListNode.bcdDevice & '\uff00') == 1792;
    }

    private final boolean isDeviceE() {
        return (this.ftDeviceInfoListNode.bcdDevice & '\uff00') == 1536;
    }

    private final boolean isDeviceF() {
        return (this.ftDeviceInfoListNode.bcdDevice & '\uff00') == 1280;
    }

    private final boolean isDeviceG() {
        return (this.ftDeviceInfoListNode.bcdDevice & '\uff00') == 1024 || (this.ftDeviceInfoListNode.bcdDevice & '\uff00') == 512 && this.ftDeviceInfoListNode.iSerialNumber == 0;
    }

    private final String a(byte[] var1) throws UnsupportedEncodingException {
        return new String(var1, 2, var1[0] - 2, "UTF-16LE");
    }

    UsbDeviceConnection getUsbDeviceConnection() {
        return this.usbDeviceConnection;
    }

    void setUsbDeviceConnection(UsbDeviceConnection var1) {
        this.usbDeviceConnection = var1;
    }

    synchronized boolean setContext(Context context) {
        boolean result = false;
        if (context != null) {
            this.context = context;
            result = true;
        }

        return result;
    }

    protected void setDriverParameters(DriverParameters params) {
        this.driverParameters.setMaxBufferSize(params.getMaxBufferSize());
        this.driverParameters.setMaxTransferSize(params.getMaxTransferSize());
        this.driverParameters.setBufferNumber(params.getBufferNumber());
        this.driverParameters.setReadTimeout(params.getReadTimeout());
    }

    DriverParameters getDriverParameters() {
        return this.driverParameters;
    }

    public int getReadTimeout() {
        return this.driverParameters.getReadTimeout();
    }

    private void n() {
        if(this.intfId == 1) {
            this.ftDeviceInfoListNode.serialNumber = this.ftDeviceInfoListNode.serialNumber + "A";
            this.ftDeviceInfoListNode.description = this.ftDeviceInfoListNode.description + " A";
        } else if(this.intfId == 2) {
            this.ftDeviceInfoListNode.serialNumber = this.ftDeviceInfoListNode.serialNumber + "B";
            this.ftDeviceInfoListNode.description = this.ftDeviceInfoListNode.description + " B";
        } else if(this.intfId == 3) {
            this.ftDeviceInfoListNode.serialNumber = this.ftDeviceInfoListNode.serialNumber + "C";
            this.ftDeviceInfoListNode.description = this.ftDeviceInfoListNode.description + " C";
        } else if(this.intfId == 4) {
            this.ftDeviceInfoListNode.serialNumber = this.ftDeviceInfoListNode.serialNumber + "D";
            this.ftDeviceInfoListNode.description = this.ftDeviceInfoListNode.description + " D";
        }

    }

    synchronized boolean a(UsbManager var1) {
        boolean var2 = false;
        if(this.isOpen()) {
            return var2;
        } else if(var1 == null) {
            Log.e("FTDI_Device::", "UsbManager cannot be null.");
            return var2;
        } else if(this.getUsbDeviceConnection() != null) {
            Log.e("FTDI_Device::", "There should not have an UsbConnection.");
            return var2;
        } else {
            this.setUsbDeviceConnection(var1.openDevice(this.usbDevice));
            if(this.getUsbDeviceConnection() == null) {
                Log.e("FTDI_Device::", "UsbConnection cannot be null.");
                return var2;
            } else if(!this.getUsbDeviceConnection().claimInterface(this.usbInterface, true)) {
                Log.e("FTDI_Device::", "ClaimInteface returned false.");
                return var2;
            } else {
                Log.d("FTDI_Device::", "open SUCCESS");
                if(!this.q()) {
                    Log.e("FTDI_Device::", "Failed to find endpoints.");
                    return var2;
                } else {
                    this.usbRequest.initialize(this.usbDeviceConnection, this.e);
                    Log.d("D2XX::", "**********************Device Opened**********************");
                    this.processInCtrl = new ProcessInCtrl(this);
                    this.bulkInRunnable = new BulkInRunnable(this, this.processInCtrl, this.getUsbDeviceConnection(), this.f);
                    this.bulkInThread = new Thread(this.bulkInRunnable);
                    this.bulkInThread.setName("bulkInThread");
                    this.processRequestThread = new Thread(new ProcessRequestThread(this.processInCtrl));
                    this.processRequestThread.setName("processRequestThread");
                    this.a(true, true);
                    this.bulkInThread.start();
                    this.processRequestThread.start();
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
        this.ftDeviceInfoListNode.flags |= 1;
    }

    private synchronized void p() {
        this.b = Boolean.valueOf(false);
        this.ftDeviceInfoListNode.flags &= 2;
    }

    public synchronized void close() {
        if(this.processRequestThread != null) {
            this.processRequestThread.interrupt();
        }

        if(this.bulkInThread != null) {
            this.bulkInThread.interrupt();
        }

        if(this.usbDeviceConnection != null) {
            this.usbDeviceConnection.releaseInterface(this.usbInterface);
            this.usbDeviceConnection.close();
            this.usbDeviceConnection = null;
        }

        if(this.processInCtrl != null) {
            this.processInCtrl.g();
        }

        this.processRequestThread = null;
        this.bulkInThread = null;
        this.bulkInRunnable = null;
        this.processInCtrl = null;
        this.p();
    }

    protected UsbDevice getUsbDevice() {
        return this.usbDevice;
    }

    public FtDeviceInfoListNode getDeviceInfo() {
        return this.ftDeviceInfoListNode;
    }

    public int read(byte[] data, int length, long wait_ms) {
        boolean var5 = false;
        if(!this.isOpen()) {
            return -1;
        } else if(length <= 0) {
            return -2;
        } else if(this.processInCtrl == null) {
            return -3;
        } else {
            int cbRead = this.processInCtrl.read(data, length, wait_ms);
            return cbRead;
        }
    }

    public int read(byte[] data, int length) {
        return this.read(data, length, (long) this.driverParameters.getReadTimeout());
    }

    public int read(byte[] data) {
        return this.read(data, data.length, (long) this.driverParameters.getReadTimeout());
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
            UsbRequest var4 = this.usbRequest;
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
                    var4 = this.usbDeviceConnection.requestWait();
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
        } else if(this.processInCtrl == null) {
            return (short)-2;
        } else {
            this.a &= -3L;
            return (short)(this.ftDeviceInfoListNode.modemStatus & 255);
        }
    }

    public short getLineStatus() {
        return !this.isOpen()?-1:(this.processInCtrl == null?-2:this.ftDeviceInfoListNode.lineStatus);
    }

    public int getQueueStatus() {
        return !this.isOpen()?-1:(this.processInCtrl == null?-2:this.processInCtrl.c());
    }

    public boolean readBufferFull() {
        return this.processInCtrl.readBufferFull();
    }

    public long getEventStatus() {
        if(!this.isOpen()) {
            return -1L;
        } else if(this.processInCtrl == null) {
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
                if(this.isDeviceBCD() && baudRate >= 1200) {
                    var2 = b.a(baudRate, var3);
                } else {
                    var2 = b.a(baudRate, var3, this.isDeviceAThroughG());
                }

                var4 = true;
            }

            if(this.isDeviceCDF() || this.isDeviceB() || this.isDeviceA()) {
                var3[1] <<= 8;
                var3[1] &= '\uff00';
                var3[1] |= this.intfId;
            }

            if(var2 == 1) {
                int var6 = this.getUsbDeviceConnection().controlTransfer(64, 3, var3[0], var3[1], (byte[])null, 0, 0);
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
            this.ftDeviceInfoListNode.breakOnParam = var7;
            int var8 = this.getUsbDeviceConnection().controlTransfer(64, 4, var7, this.intfId, (byte[])null, 0, 0);
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
        int var4 = this.ftDeviceInfoListNode.breakOnParam;
        var4 |= var1;
        if(!this.isOpen()) {
            return var2;
        } else {
            int var3 = this.getUsbDeviceConnection().controlTransfer(64, 4, var4, this.intfId, (byte[])null, 0, 0);
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

            int var8 = this.getUsbDeviceConnection().controlTransfer(64, 2, var6, this.intfId | flowControl, (byte[])null, 0, 0);
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
            int var2 = this.getUsbDeviceConnection().controlTransfer(64, 1, var3, this.intfId, (byte[])null, 0, 0);
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
            int var2 = this.getUsbDeviceConnection().controlTransfer(64, 1, var3, this.intfId, (byte[])null, 0, 0);
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
            int var2 = this.getUsbDeviceConnection().controlTransfer(64, 1, var3, this.intfId, (byte[])null, 0, 0);
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
            int var2 = this.getUsbDeviceConnection().controlTransfer(64, 1, var3, this.intfId, (byte[])null, 0, 0);
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

            int var5 = this.getUsbDeviceConnection().controlTransfer(64, 6, var6, this.intfId, (byte[])null, 0, 0);
            if(var5 != 0) {
                return var7;
            } else {
                var6 = errorChar & 255;
                if(errorCharEnable > 0) {
                    var6 |= 256;
                }

                var5 = this.getUsbDeviceConnection().controlTransfer(64, 7, var6, this.intfId, (byte[])null, 0, 0);
                if(var5 == 0) {
                    this.h = var8;
                    var7 = true;
                }

                return var7;
            }
        }
    }

    public boolean setBitMode(byte mask, byte bitMode) {
        int var5 = this.ftDeviceInfoListNode.type;
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

                if(bitMode == 2 & this.usbInterface.getId() != 0) {
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

                if((bitMode & 72) > 0 & this.usbInterface.getId() != 0) {
                    return var6;
                }
            } else if(var5 == 7 && bitMode != 0) {
                if((bitMode & 7) == 0) {
                    return var6;
                }

                if(bitMode == 2 & this.usbInterface.getId() != 0 & this.usbInterface.getId() != 1) {
                    return var6;
                }
            } else if(var5 == 8 && bitMode != 0 && bitMode > 64) {
                return var6;
            }

            int var3 = bitMode << 8;
            var3 |= mask & 255;
            int var4 = this.getUsbDeviceConnection().controlTransfer(64, 11, var3, this.intfId, (byte[])null, 0, 0);
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
        } else if(!this.isDeviceAThroughG()) {
            return (byte)-2;
        } else {
            int var3 = this.getUsbDeviceConnection().controlTransfer(-64, 12, 0, this.intfId, var2, var2.length, 0);
            return var3 == var2.length?var2[0]:-3;
        }
    }

    public boolean resetDevice() {
        boolean var1 = false;
        boolean var2 = false;
        if(!this.isOpen()) {
            return var2;
        } else {
            int var3 = this.getUsbDeviceConnection().controlTransfer(64, 0, 0, 0, (byte[])null, 0, 0);
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
            int var4 = this.getUsbDeviceConnection().controlTransfer(64, request, wValue, this.intfId, (byte[])null, 0, 0);
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

            int var6 = this.getUsbDeviceConnection().controlTransfer(64, request, wValue, this.intfId, buf, datalen, 0);
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
            int var6 = this.getUsbDeviceConnection().controlTransfer(-64, request, wValue, this.intfId, buf, datalen, 0);
            return var6;
        }
    }

    public void stopInTask() {
        try {
            if(!this.bulkInRunnable.stopped()) {
                this.bulkInRunnable.stop();
            }
        } catch (InterruptedException e) {
            Log.d("FTDI_Device::", "stopInTask called!");
            e.printStackTrace();
        }

    }

    public void restartInTask() {
        this.bulkInRunnable.restart();
    }

    public boolean stoppedInTask() {
        return this.bulkInRunnable.stopped();
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
                    var4 = this.getUsbDeviceConnection().controlTransfer(64, 0, var7, this.intfId, (byte[])null, 0, 0);
                }

                if(var4 > 0) {
                    return var3;
                }

                this.processInCtrl.e();
            }

            if(var2) {
                var7 = 2;
                var4 = this.getUsbDeviceConnection().controlTransfer(64, 0, var7, this.intfId, (byte[])null, 0, 0);
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
            int var3 = this.getUsbDeviceConnection().controlTransfer(64, 9, var2, this.intfId, (byte[])null, 0, 0);
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
            int var3 = this.getUsbDeviceConnection().controlTransfer(-64, 10, 0, this.intfId, var1, var1.length, 0);
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
        for(int var1 = 0; var1 < this.usbInterface.getEndpointCount(); ++var1) {
            Log.i("FTDI_Device::", "EP: " + String.format("0x%02X", new Object[]{Integer.valueOf(this.usbInterface.getEndpoint(var1).getAddress())}));
            if(this.usbInterface.getEndpoint(var1).getType() == 2) {
                if(this.usbInterface.getEndpoint(var1).getDirection() == 128) {
                    this.f = this.usbInterface.getEndpoint(var1);
                    this.u = this.f.getMaxPacketSize();
                } else {
                    this.e = this.usbInterface.getEndpoint(var1);
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
        return !this.isOpen()?null:this.ft_ee_ctrl.a();
    }

    public short eepromWrite(FT_EEPROM eeData) {
        return !this.isOpen()?-1:this.ft_ee_ctrl.a(eeData);
    }

    public boolean eepromErase() {
        boolean var1 = false;
        if(!this.isOpen()) {
            return var1;
        } else {
            if(this.ft_ee_ctrl.c() == 0) {
                var1 = true;
            }

            return var1;
        }
    }

    public int eepromWriteUserArea(byte[] data) {
        return !this.isOpen()?0:this.ft_ee_ctrl.a(data);
    }

    public byte[] eepromReadUserArea(int length) {
        return !this.isOpen()?null:this.ft_ee_ctrl.a(length);
    }

    public int eepromGetUserAreaSize() {
        return !this.isOpen()?-1:this.ft_ee_ctrl.b();
    }

    public int eepromReadWord(short offset) {
        byte var2 = -1;
        if(!this.isOpen()) {
            return var2;
        } else {
            int var3 = this.ft_ee_ctrl.a(offset);
            return var3;
        }
    }

    public boolean eepromWriteWord(short address, short data) {
        boolean var3 = false;
        if(!this.isOpen()) {
            return var3;
        } else {
            var3 = this.ft_ee_ctrl.a(address, data);
            return var3;
        }
    }

    int e() {
        return this.u;
    }
}
