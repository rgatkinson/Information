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
    private static D2xxManager theInstance = null;
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
    private static Context theContext = null;
    private static PendingIntent theJ2xxPendingIntent = null;
    private static IntentFilter theJ2xxIntentFilter = null;
    private static final List<USBVendorAndProduct> ftVendorProductPairs = new ArrayList(Arrays.asList(new USBVendorAndProduct[]
                {
                        new USBVendorAndProduct(1027, 24597),
                        new USBVendorAndProduct(1027, 24596),
                        new USBVendorAndProduct(1027, 24593),
                        new USBVendorAndProduct(1027, 24592),
                        new USBVendorAndProduct(1027, 24577),
                        new USBVendorAndProduct(1027, 24582),
                        new USBVendorAndProduct(1027, 24604),
                        new USBVendorAndProduct(1027, '贈'),
                        new USBVendorAndProduct(1027, '輸'),
                        new USBVendorAndProduct(1027, '遲'),
                        new USBVendorAndProduct(1027, '醙'),
                        new USBVendorAndProduct(1027, '鉶'),
                        new USBVendorAndProduct(1027, '陼'),
                        new USBVendorAndProduct(1027, 24594),
                        new USBVendorAndProduct(2220, 4133),
                        new USBVendorAndProduct(5590, 1),
                        new USBVendorAndProduct(1027, 24599)
                }));
    private ArrayList<FT_Device> ftDevices;
    private static UsbManager theUsbManager;

    private BroadcastReceiver deviceAttachReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            UsbDevice usbDevice = null;
            FT_Device ftDevice = null;

            if("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(intentAction)) {
                usbDevice = (UsbDevice)intent.getParcelableExtra("device");

                for(ftDevice = D2xxManager.this.ftDeviceFromUsbDevice(usbDevice); ftDevice != null; ftDevice = D2xxManager.this.ftDeviceFromUsbDevice(usbDevice)) {
                    ftDevice.close();
                    synchronized(D2xxManager.this.ftDevices) {
                        D2xxManager.this.ftDevices.remove(ftDevice);
                    }
                }

            } else if("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(intentAction)) {
                usbDevice = (UsbDevice)intent.getParcelableExtra("device");
                D2xxManager.this.addUsbDevice(usbDevice);
            }

        }
    };
    private static BroadcastReceiver usbPermissionReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            if(ACTION_USB_PERMISSION.equals(intentAction)) {
                synchronized(this) {
                    UsbDevice usbDevice = (UsbDevice)intent.getParcelableExtra("device");
                    if(!intent.getBooleanExtra("permission", false)) {
                        Log.d("D2xx::", "permission denied for device " + usbDevice);
                    }
                }
            }

        }
    };

    private FT_Device ftDeviceFromUsbDevice(UsbDevice usbDeviceSeek) {
        FT_Device result = null;

        synchronized(this.ftDevices) {
            int numDevices = this.ftDevices.size();

            for(int i = 0; i < numDevices; ++i) {
                FT_Device ftDevice = (FT_Device)this.ftDevices.get(i);
                UsbDevice usbDevice = ftDevice.getUsbDevice();
                if(usbDevice.equals(usbDeviceSeek)) {
                    result = ftDevice;
                    break;
                }
            }

            return result;
        }
    }

    public boolean isFtDevice(UsbDevice dev) {
        boolean result = false;
        if(theContext == null) {
            return result;
        } else {
            USBVendorAndProduct var2 = new USBVendorAndProduct(dev.getVendorId(), dev.getProductId());
            if(ftVendorProductPairs.contains(var2)) {
                result = true;
            }

            Log.v("D2xx::", var2.toString());
            return result;
        }
    }

    private static synchronized boolean registerForJ2xxIntents(Context context) {
        boolean result = false;
        if(context == null) {
            return result;
        } else {
            if(D2xxManager.theContext != context) {
                D2xxManager.theContext = context;
                theJ2xxPendingIntent = PendingIntent.getBroadcast(D2xxManager.theContext.getApplicationContext(), 0, new Intent(ACTION_USB_PERMISSION), 134217728);
                theJ2xxIntentFilter = new IntentFilter(ACTION_USB_PERMISSION);
                D2xxManager.theContext.getApplicationContext().registerReceiver(usbPermissionReceiver, theJ2xxIntentFilter);
            }

            result = true;
            return result;
        }
    }

    private boolean allowedToAccessDevice(UsbDevice usbDevice) {
        boolean result = false;
        if(!theUsbManager.hasPermission(usbDevice)) {
            theUsbManager.requestPermission(usbDevice, theJ2xxPendingIntent);
        }
        if(theUsbManager.hasPermission(usbDevice)) {
            result = true;
        }
        return result;
    }

    private D2xxManager(Context parentContext) throws D2xxManager.D2xxException {
        Log.v("D2xx::", "Start constructor");
        if(parentContext == null) {
            throw new D2xxManager.D2xxException("D2xx init failed: Can not find parentContext!");
        } else {
            registerForJ2xxIntents(parentContext);
            if(!usbServiceExists()) {
                throw new D2xxManager.D2xxException("D2xx init failed: Can not find UsbManager!");
            } else {
                this.ftDevices = new ArrayList();
                IntentFilter var2 = new IntentFilter();
                var2.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
                var2.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
                parentContext.getApplicationContext().registerReceiver(this.deviceAttachReceiver, var2);
                Log.v("D2xx::", "End constructor");
            }
        }
    }

    public static synchronized D2xxManager getInstance(Context parentContext) throws D2xxManager.D2xxException {
        if(theInstance == null) {
            theInstance = new D2xxManager(parentContext);
        }

        if(parentContext != null) {
            registerForJ2xxIntents(parentContext);
        }

        return theInstance;
    }

    private static boolean usbServiceExists() {
        if(theUsbManager == null && theContext != null) {
            theUsbManager = (UsbManager) theContext.getApplicationContext().getSystemService("usb");
        }

        return theUsbManager != null;
    }

    public boolean setVIDPID(int vendorId, int productId) {
        boolean var3 = false;
        if(vendorId != 0 && productId != 0) {
            USBVendorAndProduct var4 = new USBVendorAndProduct(vendorId, productId);
            if(ftVendorProductPairs.contains(var4)) {
                Log.i("D2xx::", "Existing vid:" + vendorId + "  pid:" + productId);
                return true;
            }

            if(!ftVendorProductPairs.add(var4)) {
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
        int var1 = ftVendorProductPairs.size();
        int[][] var2 = new int[2][var1];

        for(int var3 = 0; var3 < var1; ++var3) {
            USBVendorAndProduct var4 = (USBVendorAndProduct) ftVendorProductPairs.get(var3);
            var2[0][var3] = var4.getVendor();
            var2[1][var3] = var4.getProduct();
        }

        return var2;
    }

    private void removeAllDevices() {
        synchronized(this.ftDevices) {
            int numDevices = this.ftDevices.size();

            for(int i = 0; i < numDevices; ++i) {
                this.ftDevices.remove(i);
            }

        }
    }

    // This is the root 'scanForDevices' call
    public int createDeviceInfoList(Context parentContext) {
        HashMap attachedUSBDevices = theUsbManager.getDeviceList();
        Iterator interator = attachedUSBDevices.values().iterator();
        ArrayList newDevices = new ArrayList();
        FT_Device ftDevice = null;
        byte countDevices = 0;
        if (parentContext == null) {
            return countDevices;
        } else {
            registerForJ2xxIntents(parentContext);

            while(true) {
                UsbDevice usbDevice;

                do {
                    if(!interator.hasNext()) {

                        // We're done. Substitute new devices for old and get out of here
                        synchronized(this.ftDevices) {
                            this.removeAllDevices();
                            this.ftDevices = newDevices;
                            int numberOfDevices = this.ftDevices.size();
                            return numberOfDevices;
                        }
                    }
                    usbDevice = (UsbDevice)interator.next();
                } while(!this.isFtDevice(usbDevice));       // prints, e.g., "Vendor: 0403, Product: 6001"

                int interfaceCount = usbDevice.getInterfaceCount();

                for(int iInterface = 0; iInterface < interfaceCount; ++iInterface) {
                    if(this.allowedToAccessDevice(usbDevice)) {

                        synchronized(this.ftDevices) {
                            ftDevice = this.ftDeviceFromUsbDevice(usbDevice);
                            if(ftDevice == null) {
                                ftDevice = new FT_Device(parentContext, theUsbManager, usbDevice, usbDevice.getInterface(iInterface));
                            } else {
                                this.ftDevices.remove(ftDevice);
                                ftDevice.setParentContext(parentContext);
                            }

                            newDevices.add(ftDevice);
                        }
                    }
                }
            }
        }
    }

    public synchronized int getDeviceInfoList(int numDevs, D2xxManager.FtDeviceInfoListNode[] deviceList) {
        for(int var3 = 0; var3 < numDevs; ++var3) {
            deviceList[var3] = ((FT_Device)this.ftDevices.get(var3)).ftDeviceInfoListNode;
        }

        return this.ftDevices.size();
    }

    public synchronized D2xxManager.FtDeviceInfoListNode getDeviceInfoListDetail(int index) {
        return index <= this.ftDevices.size() && index >= 0
                ? ((FT_Device)this.ftDevices.get(index)).ftDeviceInfoListNode
                : null;
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
            var2.setParentContext(var1);
            if(var3 != null) {
                var2.setDriverParameters(var3);
            }

            if(var2.a(theUsbManager) && var2.isOpen()) {
                var4 = true;
            }

            return var4;
        }
    }

    public synchronized FT_Device openByUsbDevice(Context parentContext, UsbDevice dev, D2xxManager.DriverParameters params) {
        FT_Device var4 = null;
        if(this.isFtDevice(dev)) {
            var4 = this.ftDeviceFromUsbDevice(dev);
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
            registerForJ2xxIntents(parentContext);
            var4 = (FT_Device)this.ftDevices.get(index);
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
            registerForJ2xxIntents(parentContext);

            for(int i = 0; i < this.ftDevices.size(); ++i) {
                FT_Device device = (FT_Device)this.ftDevices.get(i);
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
            registerForJ2xxIntents(parentContext);

            for(int var7 = 0; var7 < this.ftDevices.size(); ++var7) {
                FT_Device var6 = (FT_Device)this.ftDevices.get(var7);
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
            registerForJ2xxIntents(parentContext);

            for(int var7 = 0; var7 < this.ftDevices.size(); ++var7) {
                FT_Device var6 = (FT_Device)this.ftDevices.get(var7);
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
        if(this.isFtDevice(dev)) {  // prints, e.g., "Vendor: 0403, Product: 6001"
            boolean var4 = false;
            int var8 = dev.getInterfaceCount();

            for(int var5 = 0; var5 < var8; ++var5) {
                if(this.allowedToAccessDevice(dev)) {
                    ArrayList var6 = this.ftDevices;
                    synchronized(this.ftDevices) {
                        FT_Device var2 = this.ftDeviceFromUsbDevice(dev);
                        if(var2 == null) {
                            var2 = new FT_Device(theContext, theUsbManager, dev, dev.getInterface(var5));
                        } else {
                            var2.setParentContext(theContext);
                        }

                        this.ftDevices.add(var2);
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
