package com.ftdi.j2xx;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.util.Log;
import com.ftdi.j2xx.BulkInWorker;
import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_BaudRate;
import com.ftdi.j2xx.FT_EEPROM;
import com.ftdi.j2xx.FT_EE_2232H_Ctrl;
import com.ftdi.j2xx.FT_EE_2232_Ctrl;
import com.ftdi.j2xx.FT_EE_232A_Ctrl;
import com.ftdi.j2xx.FT_EE_232B_Ctrl;
import com.ftdi.j2xx.FT_EE_232H_Ctrl;
import com.ftdi.j2xx.FT_EE_232R_Ctrl;
import com.ftdi.j2xx.FT_EE_245R_Ctrl;
import com.ftdi.j2xx.FT_EE_4232H_Ctrl;
import com.ftdi.j2xx.FT_EE_Ctrl;
import com.ftdi.j2xx.FT_EE_X_Ctrl;
import com.ftdi.j2xx.ProcessInCtrl;
import com.ftdi.j2xx.ProcessRequestWorker;
import com.ftdi.j2xx.TFtEventNotify;
import com.ftdi.j2xx.TFtSpecialChars;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FT_Device {
   long mEventMask;
   private static final String TAG = "FTDI_Device::";
   Boolean mIsOpen;
   UsbDevice mUsbDevice;
   UsbInterface mUsbInterface;
   UsbEndpoint mBulkOutEndpoint;
   UsbEndpoint mBulkInEndpoint;
   private UsbRequest mUsbRequest;
   private UsbDeviceConnection mUsbConnection;
   private BulkInWorker mBulkIn;
   private Thread mProcessRequestThread;
   private Thread mBulkInThread;
   D2xxManager.FtDeviceInfoListNode mDeviceInfoNode;
   private ProcessInCtrl mProcessInCtrl;
   private FT_EE_Ctrl mEEPROM;
   private byte mLatencyTimer;
   TFtSpecialChars mTftSpecialChars;
   TFtEventNotify mEventNotification;
   private D2xxManager.DriverParameters mDriverParams;
   private int mInterfaceID = 0;
   Context mContext;
   private int mMaxPacketSize;

   public FT_Device(Context parentContext, UsbManager usbManager, UsbDevice dev, UsbInterface itf) {
      byte[] buffer = new byte[255];
      this.mContext = parentContext;
      this.mDriverParams = new D2xxManager.DriverParameters();

      try {
         this.mUsbDevice = dev;
         this.mUsbInterface = itf;
         this.mBulkOutEndpoint = null;
         this.mBulkInEndpoint = null;
         this.mMaxPacketSize = 0;
         this.mTftSpecialChars = new TFtSpecialChars();
         this.mEventNotification = new TFtEventNotify();
         this.mDeviceInfoNode = new D2xxManager.FtDeviceInfoListNode();
         this.mUsbRequest = new UsbRequest();
         this.setConnection(usbManager.openDevice(this.mUsbDevice));
         if(this.getConnection() == null) {
            Log.e("FTDI_Device::", "Failed to open the device!");
            throw new D2xxManager.D2xxException("Failed to open the device!");
         } else {
            this.getConnection().claimInterface(this.mUsbInterface, false);
            byte[] rawDescriptors = this.getConnection().getRawDescriptors();
            int e = this.mUsbDevice.getDeviceId();
            this.mInterfaceID = this.mUsbInterface.getId() + 1;
            this.mDeviceInfoNode.location = e << 4 | this.mInterfaceID & 15;
            ByteBuffer bcdDevice = ByteBuffer.allocate(2);
            bcdDevice.order(ByteOrder.LITTLE_ENDIAN);
            bcdDevice.put(rawDescriptors[12]);
            bcdDevice.put(rawDescriptors[13]);
            this.mDeviceInfoNode.bcdDevice = bcdDevice.getShort(0);
            this.mDeviceInfoNode.iSerialNumber = rawDescriptors[16];
            this.mDeviceInfoNode.serialNumber = this.getConnection().getSerial();
            this.mDeviceInfoNode.id = this.mUsbDevice.getVendorId() << 16 | this.mUsbDevice.getProductId();
            this.mDeviceInfoNode.breakOnParam = 8;
            this.getConnection().controlTransfer(-128, 6, 768 | rawDescriptors[15], 0, buffer, 255, 0);
            this.mDeviceInfoNode.description = this.stringFromUtf16le(buffer);
            switch(this.mDeviceInfoNode.bcdDevice & 65280) {
            case 512:
               if(this.mDeviceInfoNode.iSerialNumber == 0) {
                  this.mEEPROM = new FT_EE_232B_Ctrl(this);
                  this.mDeviceInfoNode.type = 0;
               } else {
                  this.mDeviceInfoNode.type = 1;
                  this.mEEPROM = new FT_EE_232A_Ctrl(this);
               }
               break;
            case 1024:
               this.mEEPROM = new FT_EE_232B_Ctrl(this);
               this.mDeviceInfoNode.type = 0;
               break;
            case 1280:
               this.mEEPROM = new FT_EE_2232_Ctrl(this);
               this.mDeviceInfoNode.type = 4;
               this.dualQuadChannelDevice();
               break;
            case 1536:
               this.mEEPROM = new FT_EE_Ctrl(this);
               short dataRead = (short)(this.mEEPROM.readWord((short)0) & 1);
               this.mEEPROM = null;
               if(dataRead == 0) {
                  this.mDeviceInfoNode.type = 5;
                  this.mEEPROM = new FT_EE_232R_Ctrl(this);
               } else {
                  this.mDeviceInfoNode.type = 5;
                  this.mEEPROM = new FT_EE_245R_Ctrl(this);
               }
               break;
            case 1792:
               this.mDeviceInfoNode.type = 6;
               this.mDeviceInfoNode.flags = 2;
               this.dualQuadChannelDevice();
               this.mEEPROM = new FT_EE_2232H_Ctrl(this);
               break;
            case 2048:
               this.mDeviceInfoNode.type = 7;
               this.mDeviceInfoNode.flags = 2;
               this.dualQuadChannelDevice();
               this.mEEPROM = new FT_EE_4232H_Ctrl(this);
               break;
            case 2304:
               this.mDeviceInfoNode.type = 8;
               this.mDeviceInfoNode.flags = 2;
               this.mEEPROM = new FT_EE_232H_Ctrl(this);
               break;
            case 4096:
               this.mDeviceInfoNode.type = 9;
               this.mEEPROM = new FT_EE_X_Ctrl(this);
               break;
            case 5888:
               this.mDeviceInfoNode.type = 12;
               this.mDeviceInfoNode.flags = 2;
               break;
            case 6144:
               this.mDeviceInfoNode.type = 10;
               if(this.mInterfaceID == 1) {
                  this.mDeviceInfoNode.flags = 2;
               } else {
                  this.mDeviceInfoNode.flags = 0;
               }
               break;
            case 6400:
               this.mDeviceInfoNode.type = 11;
               if(this.mInterfaceID == 4) {
                  int tmpStr = this.mUsbDevice.getInterface(this.mInterfaceID - 1).getEndpoint(0).getMaxPacketSize();
                  Log.e("dev", "mInterfaceID : " + this.mInterfaceID + "   iMaxPacketSize : " + tmpStr);
                  if(tmpStr == 8) {
                     this.mDeviceInfoNode.flags = 0;
                  } else {
                     this.mDeviceInfoNode.flags = 2;
                  }
               } else {
                  this.mDeviceInfoNode.flags = 2;
               }
               break;
            default:
               this.mDeviceInfoNode.type = 3;
               this.mEEPROM = new FT_EE_Ctrl(this);
            }

            switch(this.mDeviceInfoNode.bcdDevice & 65280) {
            case 5888:
            case 6144:
            case 6400:
               if(this.mDeviceInfoNode.serialNumber == null) {
                  byte[] var13 = new byte[16];
                  this.getConnection().controlTransfer(-64, 144, 0, 27, var13, 16, 0);
                  String var14 = "";

                  for(int m = 0; m < 8; ++m) {
                     var14 = var14 + (char)var13[m * 2];
                  }

                  this.mDeviceInfoNode.serialNumber = new String(var14);
               }
            default:
               switch(this.mDeviceInfoNode.bcdDevice & 65280) {
               case 6144:
               case 6400:
                  if(this.mInterfaceID == 1) {
                     this.mDeviceInfoNode.description = this.mDeviceInfoNode.description + " A";
                     this.mDeviceInfoNode.serialNumber = this.mDeviceInfoNode.serialNumber + "A";
                  } else if(this.mInterfaceID == 2) {
                     this.mDeviceInfoNode.description = this.mDeviceInfoNode.description + " B";
                     this.mDeviceInfoNode.serialNumber = this.mDeviceInfoNode.serialNumber + "B";
                  } else if(this.mInterfaceID == 3) {
                     this.mDeviceInfoNode.description = this.mDeviceInfoNode.description + " C";
                     this.mDeviceInfoNode.serialNumber = this.mDeviceInfoNode.serialNumber + "C";
                  } else if(this.mInterfaceID == 4) {
                     this.mDeviceInfoNode.description = this.mDeviceInfoNode.description + " D";
                     this.mDeviceInfoNode.serialNumber = this.mDeviceInfoNode.serialNumber + "D";
                  }
               default:
                  this.getConnection().releaseInterface(this.mUsbInterface);
                  this.getConnection().close();
                  this.setConnection((UsbDeviceConnection)null);
                  this.setClosed();
               }
            }
         }
      } catch (Exception var12) {
         if(var12.getMessage() != null) {
            Log.e("FTDI_Device::", var12.getMessage());
         }

      }
   }

   private final boolean isHiSpeed() {
      return this.isFt232h() || this.isFt2232h() || this.isFt4232h();
   }

   private final boolean isBmDevice() {
      return this.isFt232b() || this.isFt2232() || this.isFt232r() || this.isFt2232h() || this.isFt4232h() || this.isFt232h() || this.isFt232ex();
   }

   final boolean isMultiIfDevice() {
      return this.isFt2232() || this.isFt2232h() || this.isFt4232h();
   }

   private final boolean isFt232ex() {
      return (this.mDeviceInfoNode.bcdDevice & '\uff00') == 4096;
   }

   private final boolean isFt232h() {
      return (this.mDeviceInfoNode.bcdDevice & '\uff00') == 2304;
   }

   final boolean isFt4232h() {
      return (this.mDeviceInfoNode.bcdDevice & '\uff00') == 2048;
   }

   private final boolean isFt2232h() {
      return (this.mDeviceInfoNode.bcdDevice & '\uff00') == 1792;
   }

   private final boolean isFt232r() {
      return (this.mDeviceInfoNode.bcdDevice & '\uff00') == 1536;
   }

   private final boolean isFt2232() {
      return (this.mDeviceInfoNode.bcdDevice & '\uff00') == 1280;
   }

   private final boolean isFt232b() {
      return (this.mDeviceInfoNode.bcdDevice & '\uff00') == 1024 || (this.mDeviceInfoNode.bcdDevice & '\uff00') == 512 && this.mDeviceInfoNode.iSerialNumber == 0;
   }

   private final boolean ifFt8u232am() {
      return (this.mDeviceInfoNode.bcdDevice & '\uff00') == 512 && this.mDeviceInfoNode.iSerialNumber != 0;
   }

   private final String stringFromUtf16le(byte[] data) throws UnsupportedEncodingException {
      return new String(data, 2, data[0] - 2, "UTF-16LE");
   }

   UsbDeviceConnection getConnection() {
      return this.mUsbConnection;
   }

   void setConnection(UsbDeviceConnection mUsbConnection) {
      this.mUsbConnection = mUsbConnection;
   }

   synchronized boolean setContext(Context parentContext) {
      boolean rc = false;
      if(parentContext != null) {
         this.mContext = parentContext;
         rc = true;
      }

      return rc;
   }

   protected void setDriverParameters(D2xxManager.DriverParameters params) {
      this.mDriverParams.setMaxBufferSize(params.getMaxBufferSize());
      this.mDriverParams.setMaxTransferSize(params.getMaxTransferSize());
      this.mDriverParams.setBufferNumber(params.getBufferNumber());
      this.mDriverParams.setReadTimeout(params.getReadTimeout());
   }

   D2xxManager.DriverParameters getDriverParameters() {
      return this.mDriverParams;
   }

   public int getReadTimeout() {
      return this.mDriverParams.getReadTimeout();
   }

   private void dualQuadChannelDevice() {
      if(this.mInterfaceID == 1) {
         this.mDeviceInfoNode.serialNumber = this.mDeviceInfoNode.serialNumber + "A";
         this.mDeviceInfoNode.description = this.mDeviceInfoNode.description + " A";
      } else if(this.mInterfaceID == 2) {
         this.mDeviceInfoNode.serialNumber = this.mDeviceInfoNode.serialNumber + "B";
         this.mDeviceInfoNode.description = this.mDeviceInfoNode.description + " B";
      } else if(this.mInterfaceID == 3) {
         this.mDeviceInfoNode.serialNumber = this.mDeviceInfoNode.serialNumber + "C";
         this.mDeviceInfoNode.description = this.mDeviceInfoNode.description + " C";
      } else if(this.mInterfaceID == 4) {
         this.mDeviceInfoNode.serialNumber = this.mDeviceInfoNode.serialNumber + "D";
         this.mDeviceInfoNode.description = this.mDeviceInfoNode.description + " D";
      }

   }

   synchronized boolean openDevice(UsbManager usbManager) {
      boolean rc = false;
      if(this.isOpen()) {
         return rc;
      } else if(usbManager == null) {
         Log.e("FTDI_Device::", "UsbManager cannot be null.");
         return rc;
      } else if(this.getConnection() != null) {
         Log.e("FTDI_Device::", "There should not have an UsbConnection.");
         return rc;
      } else {
         this.setConnection(usbManager.openDevice(this.mUsbDevice));
         if(this.getConnection() == null) {
            Log.e("FTDI_Device::", "UsbConnection cannot be null.");
            return rc;
         } else if(!this.getConnection().claimInterface(this.mUsbInterface, true)) {
            Log.e("FTDI_Device::", "ClaimInteface returned false.");
            return rc;
         } else {
            Log.d("FTDI_Device::", "open SUCCESS");
            if(!this.findDeviceEndpoints()) {
               Log.e("FTDI_Device::", "Failed to find endpoints.");
               return rc;
            } else {
               this.mUsbRequest.initialize(this.mUsbConnection, this.mBulkOutEndpoint);
               Log.d("D2XX::", "**********************Device Opened**********************");
               this.mProcessInCtrl = new ProcessInCtrl(this);
               this.mBulkIn = new BulkInWorker(this, this.mProcessInCtrl, this.getConnection(), this.mBulkInEndpoint);
               this.mBulkInThread = new Thread(this.mBulkIn);
               this.mBulkInThread.setName("bulkInThread");
               this.mProcessRequestThread = new Thread(new ProcessRequestWorker(this.mProcessInCtrl));
               this.mProcessRequestThread.setName("processRequestThread");
               this.purgeRxTx(true, true);
               this.mBulkInThread.start();
               this.mProcessRequestThread.start();
               this.setOpen();
               rc = true;
               return rc;
            }
         }
      }
   }

   public synchronized boolean isOpen() {
      return this.mIsOpen.booleanValue();
   }

   private synchronized void setOpen() {
      this.mIsOpen = Boolean.valueOf(true);
      this.mDeviceInfoNode.flags |= 1;
   }

   private synchronized void setClosed() {
      this.mIsOpen = Boolean.valueOf(false);
      this.mDeviceInfoNode.flags &= 2;
   }

   public synchronized void close() {
      if(this.mProcessRequestThread != null) {
         this.mProcessRequestThread.interrupt();
      }

      if(this.mBulkInThread != null) {
         this.mBulkInThread.interrupt();
      }

      if(this.mUsbConnection != null) {
         this.mUsbConnection.releaseInterface(this.mUsbInterface);
         this.mUsbConnection.close();
         this.mUsbConnection = null;
      }

      if(this.mProcessInCtrl != null) {
         this.mProcessInCtrl.close();
      }

      this.mProcessRequestThread = null;
      this.mBulkInThread = null;
      this.mBulkIn = null;
      this.mProcessInCtrl = null;
      this.setClosed();
   }

   protected UsbDevice getUsbDevice() {
      return this.mUsbDevice;
   }

   public D2xxManager.FtDeviceInfoListNode getDeviceInfo() {
      return this.mDeviceInfoNode;
   }

   public int read(byte[] data, int length, long wait_ms) {
      boolean rc = false;
      if(!this.isOpen()) {
         return -1;
      } else if(length <= 0) {
         return -2;
      } else if(this.mProcessInCtrl == null) {
         return -3;
      } else {
         int rc1 = this.mProcessInCtrl.readBulkInData(data, length, wait_ms);
         return rc1;
      }
   }

   public int read(byte[] data, int length) {
      return this.read(data, length, (long)this.mDriverParams.getReadTimeout());
   }

   public int read(byte[] data) {
      return this.read(data, data.length, (long)this.mDriverParams.getReadTimeout());
   }

   public int write(byte[] data, int length) {
      return this.write(data, length, true);
   }

   public int write(byte[] data, int length, boolean wait) {
      int rc = -1;
      if(!this.isOpen()) {
         return rc;
      } else if(length < 0) {
         return rc;
      } else {
         UsbRequest request = this.mUsbRequest;
         if(wait) {
            request.setClientData(this);
         }

         if(length == 0) {
            byte[] tmpData = new byte[1];
            if(request.queue(ByteBuffer.wrap(tmpData), length)) {
               rc = length;
            }
         } else if(request.queue(ByteBuffer.wrap(data), length)) {
            rc = length;
         }

         Object obj;
         if(wait) {
            do {
               request = this.mUsbConnection.requestWait();
               if(request == null) {
                  Log.e("FTDI_Device::", "UsbConnection.requestWait() == null");
                  return -99;
               }

               obj = request.getClientData();
            } while(obj != this);
         }

         return rc;
      }
   }

   public int write(byte[] data) {
      return this.write(data, data.length, true);
   }

   public short getModemStatus() {
      if(!this.isOpen()) {
         return (short)-1;
      } else if(this.mProcessInCtrl == null) {
         return (short)-2;
      } else {
         this.mEventMask &= -3L;
         return (short)(this.mDeviceInfoNode.modemStatus & 255);
      }
   }

   public short getLineStatus() {
      return !this.isOpen()?-1:(this.mProcessInCtrl == null?-2:this.mDeviceInfoNode.lineStatus);
   }

   public int getQueueStatus() {
      return !this.isOpen()?-1:(this.mProcessInCtrl == null?-2:this.mProcessInCtrl.getBytesAvailable());
   }

   public boolean readBufferFull() {
      return this.mProcessInCtrl.isSinkFull();
   }

   public long getEventStatus() {
      if(!this.isOpen()) {
         return -1L;
      } else if(this.mProcessInCtrl == null) {
         return -2L;
      } else {
         long temp = this.mEventMask;
         this.mEventMask = 0L;
         return temp;
      }
   }

   public boolean setBaudRate(int baudRate) {
      byte result = 1;
      int[] divisors = new int[2];
      boolean status = false;
      boolean boolresult = false;
      if(!this.isOpen()) {
         return boolresult;
      } else {
         switch(baudRate) {
         case 300:
            divisors[0] = 10000;
            break;
         case 600:
            divisors[0] = 5000;
            break;
         case 1200:
            divisors[0] = 2500;
            break;
         case 2400:
            divisors[0] = 1250;
            break;
         case 4800:
            divisors[0] = 625;
            break;
         case 9600:
            divisors[0] = 16696;
            break;
         case 19200:
            divisors[0] = '肜';
            break;
         case 38400:
            divisors[0] = '쁎';
            break;
         case 57600:
            divisors[0] = 52;
            break;
         case 115200:
            divisors[0] = 26;
            break;
         case 230400:
            divisors[0] = 13;
            break;
         case 460800:
            divisors[0] = 16390;
            break;
         case 921600:
            divisors[0] = '考';
            break;
         default:
            if(this.isHiSpeed() && baudRate >= 1200) {
               result = FT_BaudRate.FT_GetDivisorHi(baudRate, divisors);
            } else {
               result = FT_BaudRate.FT_GetDivisor(baudRate, divisors, this.isBmDevice());
            }

            status = true;
         }

         if(this.isMultiIfDevice() || this.isFt232h() || this.isFt232ex()) {
            divisors[1] <<= 8;
            divisors[1] &= '\uff00';
            divisors[1] |= this.mInterfaceID;
         }

         if(result == 1) {
            int status1 = this.getConnection().controlTransfer(64, 3, divisors[0], divisors[1], (byte[])null, 0, 0);
            if(status1 == 0) {
               boolresult = true;
            }
         }

         return boolresult;
      }
   }

   public boolean setDataCharacteristics(byte dataBits, byte stopBits, byte parity) {
      boolean wValue = false;
      boolean status = false;
      boolean rc = false;
      if(!this.isOpen()) {
         return rc;
      } else {
         short wValue1 = (short)(dataBits | parity << 8);
         wValue1 = (short)(wValue1 | stopBits << 11);
         this.mDeviceInfoNode.breakOnParam = wValue1;
         int status1 = this.getConnection().controlTransfer(64, 4, wValue1, this.mInterfaceID, (byte[])null, 0, 0);
         if(status1 == 0) {
            rc = true;
         }

         return rc;
      }
   }

   public boolean setBreakOn() {
      return this.setBreak(16384);
   }

   public boolean setBreakOff() {
      return this.setBreak(0);
   }

   private boolean setBreak(int OnOrOff) {
      boolean rc = false;
      int wValue = this.mDeviceInfoNode.breakOnParam;
      wValue |= OnOrOff;
      if(!this.isOpen()) {
         return rc;
      } else {
         int status = this.getConnection().controlTransfer(64, 4, wValue, this.mInterfaceID, (byte[])null, 0, 0);
         if(status == 0) {
            rc = true;
         }

         return rc;
      }
   }

   public boolean setFlowControl(short flowControl, byte xon, byte xoff) {
      boolean rc = false;
      boolean status = false;
      short wValue = 0;
      if(!this.isOpen()) {
         return rc;
      } else {
         if(flowControl == 1024) {
            wValue = (short)(xoff << 8);
            wValue = (short)(wValue | xon & 255);
         }

         int status1 = this.getConnection().controlTransfer(64, 2, wValue, this.mInterfaceID | flowControl, (byte[])null, 0, 0);
         if(status1 == 0) {
            rc = true;
            if(flowControl == 256) {
               rc = this.setRts();
            } else if(flowControl == 512) {
               rc = this.setDtr();
            }
         }

         return rc;
      }
   }

   public boolean setRts() {
      boolean rc = false;
      short wValue = 514;
      if(!this.isOpen()) {
         return rc;
      } else {
         int status = this.getConnection().controlTransfer(64, 1, wValue, this.mInterfaceID, (byte[])null, 0, 0);
         if(status == 0) {
            rc = true;
         }

         return rc;
      }
   }

   public boolean clrRts() {
      boolean rc = false;
      short wValue = 512;
      if(!this.isOpen()) {
         return rc;
      } else {
         int status = this.getConnection().controlTransfer(64, 1, wValue, this.mInterfaceID, (byte[])null, 0, 0);
         if(status == 0) {
            rc = true;
         }

         return rc;
      }
   }

   public boolean setDtr() {
      boolean rc = false;
      short wValue = 257;
      if(!this.isOpen()) {
         return rc;
      } else {
         int status = this.getConnection().controlTransfer(64, 1, wValue, this.mInterfaceID, (byte[])null, 0, 0);
         if(status == 0) {
            rc = true;
         }

         return rc;
      }
   }

   public boolean clrDtr() {
      boolean rc = false;
      short wValue = 256;
      if(!this.isOpen()) {
         return rc;
      } else {
         int status = this.getConnection().controlTransfer(64, 1, wValue, this.mInterfaceID, (byte[])null, 0, 0);
         if(status == 0) {
            rc = true;
         }

         return rc;
      }
   }

   public boolean setChars(byte eventChar, byte eventCharEnable, byte errorChar, byte errorCharEnable) {
      boolean rc = false;
      TFtSpecialChars SpecialChars = new TFtSpecialChars();
      SpecialChars.EventChar = eventChar;
      SpecialChars.EventCharEnabled = eventCharEnable;
      SpecialChars.ErrorChar = errorChar;
      SpecialChars.ErrorCharEnabled = errorCharEnable;
      if(!this.isOpen()) {
         return rc;
      } else {
         int wValue = eventChar & 255;
         if(eventCharEnable != 0) {
            wValue |= 256;
         }

         int status = this.getConnection().controlTransfer(64, 6, wValue, this.mInterfaceID, (byte[])null, 0, 0);
         if(status != 0) {
            return rc;
         } else {
            wValue = errorChar & 255;
            if(errorCharEnable > 0) {
               wValue |= 256;
            }

            status = this.getConnection().controlTransfer(64, 7, wValue, this.mInterfaceID, (byte[])null, 0, 0);
            if(status == 0) {
               this.mTftSpecialChars = SpecialChars;
               rc = true;
            }

            return rc;
         }
      }
   }

   public boolean setBitMode(byte mask, byte bitMode) {
      int devType = this.mDeviceInfoNode.type;
      boolean boolStatus = false;
      if(!this.isOpen()) {
         return boolStatus;
      } else if(devType == 1) {
         return boolStatus;
      } else {
         if(devType == 0 && bitMode != 0) {
            if((bitMode & 1) == 0) {
               return boolStatus;
            }
         } else if(devType == 4 && bitMode != 0) {
            if((bitMode & 31) == 0) {
               return boolStatus;
            }

            if(bitMode == 2 & this.mUsbInterface.getId() != 0) {
               return boolStatus;
            }
         } else if(devType == 5 && bitMode != 0) {
            if((bitMode & 37) == 0) {
               return boolStatus;
            }
         } else if(devType == 6 && bitMode != 0) {
            if((bitMode & 95) == 0) {
               return boolStatus;
            }

            if((bitMode & 72) > 0 & this.mUsbInterface.getId() != 0) {
               return boolStatus;
            }
         } else if(devType == 7 && bitMode != 0) {
            if((bitMode & 7) == 0) {
               return boolStatus;
            }

            if(bitMode == 2 & this.mUsbInterface.getId() != 0 & this.mUsbInterface.getId() != 1) {
               return boolStatus;
            }
         } else if(devType == 8 && bitMode != 0 && bitMode > 64) {
            return boolStatus;
         }

         int wValue = bitMode << 8;
         wValue |= mask & 255;
         int status = this.getConnection().controlTransfer(64, 11, wValue, this.mInterfaceID, (byte[])null, 0, 0);
         if(status == 0) {
            boolStatus = true;
         }

         return boolStatus;
      }
   }

   public byte getBitMode() {
      boolean status = false;
      byte[] buf = new byte[1];
      if(!this.isOpen()) {
         return (byte)-1;
      } else if(!this.isBmDevice()) {
         return (byte)-2;
      } else {
         int status1 = this.getConnection().controlTransfer(-64, 12, 0, this.mInterfaceID, buf, buf.length, 0);
         return status1 == buf.length?buf[0]:-3;
      }
   }

   public boolean resetDevice() {
      boolean status = false;
      boolean rc = false;
      if(!this.isOpen()) {
         return rc;
      } else {
         int status1 = this.getConnection().controlTransfer(64, 0, 0, 0, (byte[])null, 0, 0);
         if(status1 == 0) {
            rc = true;
         }

         return rc;
      }
   }

   public int VendorCmdSet(int request, int wValue) {
      boolean status = false;
      if(!this.isOpen()) {
         return -1;
      } else {
         int status1 = this.getConnection().controlTransfer(64, request, wValue, this.mInterfaceID, (byte[])null, 0, 0);
         return status1;
      }
   }

   public int VendorCmdSet(int request, int wValue, byte[] buf, int datalen) {
      boolean status = false;
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

         int status1 = this.getConnection().controlTransfer(64, request, wValue, this.mInterfaceID, buf, datalen, 0);
         return status1;
      }
   }

   public int VendorCmdGet(int request, int wValue, byte[] buf, int datalen) {
      boolean status = false;
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
         int status1 = this.getConnection().controlTransfer(-64, request, wValue, this.mInterfaceID, buf, datalen, 0);
         return status1;
      }
   }

   public void stopInTask() {
      try {
         if(!this.mBulkIn.paused()) {
            this.mBulkIn.pause();
         }
      } catch (InterruptedException var2) {
         Log.d("FTDI_Device::", "stopInTask called!");
         var2.printStackTrace();
      }

   }

   public void restartInTask() {
      this.mBulkIn.restart();
   }

   public boolean stoppedInTask() {
      return this.mBulkIn.paused();
   }

   public boolean purge(byte flags) {
      boolean RXBuffer = false;
      boolean TXBuffer = false;
      boolean rc = false;
      if((flags & 1) == 1) {
         RXBuffer = true;
      }

      if((flags & 2) == 2) {
         TXBuffer = true;
      }

      return this.purgeRxTx(RXBuffer, TXBuffer);
   }

   private boolean purgeRxTx(boolean RXBuffer, boolean TXBuffer) {
      boolean rc = false;
      int status = 0;
      boolean wValue = false;
      if(!this.isOpen()) {
         return rc;
      } else {
         byte var7;
         if(RXBuffer) {
            var7 = 1;

            for(int i = 0; i < 6; ++i) {
               status = this.getConnection().controlTransfer(64, 0, var7, this.mInterfaceID, (byte[])null, 0, 0);
            }

            if(status > 0) {
               return rc;
            }

            this.mProcessInCtrl.purgeINData();
         }

         if(TXBuffer) {
            var7 = 2;
            status = this.getConnection().controlTransfer(64, 0, var7, this.mInterfaceID, (byte[])null, 0, 0);
            if(status == 0) {
               rc = true;
            }
         }

         return rc;
      }
   }

   public boolean setLatencyTimer(byte latency) {
      boolean rc = false;
      int wValue = latency & 255;
      if(!this.isOpen()) {
         return rc;
      } else {
         int status = this.getConnection().controlTransfer(64, 9, wValue, this.mInterfaceID, (byte[])null, 0, 0);
         if(status == 0) {
            this.mLatencyTimer = latency;
            rc = true;
         } else {
            rc = false;
         }

         return rc;
      }
   }

   public byte getLatencyTimer() {
      byte[] latency = new byte[1];
      boolean status = false;
      if(!this.isOpen()) {
         return (byte)-1;
      } else {
         int status1 = this.getConnection().controlTransfer(-64, 10, 0, this.mInterfaceID, latency, latency.length, 0);
         return status1 == latency.length?latency[0]:0;
      }
   }

   public boolean setEventNotification(long Mask) {
      boolean rc = false;
      if(!this.isOpen()) {
         return rc;
      } else {
         if(Mask != 0L) {
            this.mEventMask = 0L;
            this.mEventNotification.Mask = Mask;
            rc = true;
         }

         return rc;
      }
   }

   private boolean findDeviceEndpoints() {
      for(int i = 0; i < this.mUsbInterface.getEndpointCount(); ++i) {
         Log.i("FTDI_Device::", "EP: " + String.format("0x%02X", new Object[]{Integer.valueOf(this.mUsbInterface.getEndpoint(i).getAddress())}));
         if(this.mUsbInterface.getEndpoint(i).getType() == 2) {
            if(this.mUsbInterface.getEndpoint(i).getDirection() == 128) {
               this.mBulkInEndpoint = this.mUsbInterface.getEndpoint(i);
               this.mMaxPacketSize = this.mBulkInEndpoint.getMaxPacketSize();
            } else {
               this.mBulkOutEndpoint = this.mUsbInterface.getEndpoint(i);
            }
         } else {
            Log.i("FTDI_Device::", "Not Bulk Endpoint");
         }
      }

      if(this.mBulkOutEndpoint != null && this.mBulkInEndpoint != null) {
         return true;
      } else {
         return false;
      }
   }

   public FT_EEPROM eepromRead() {
      return !this.isOpen()?null:this.mEEPROM.readEeprom();
   }

   public short eepromWrite(FT_EEPROM eeData) {
      return !this.isOpen()?-1:this.mEEPROM.programEeprom(eeData);
   }

   public boolean eepromErase() {
      boolean rc = false;
      if(!this.isOpen()) {
         return rc;
      } else {
         if(this.mEEPROM.eraseEeprom() == 0) {
            rc = true;
         }

         return rc;
      }
   }

   public int eepromWriteUserArea(byte[] data) {
      return !this.isOpen()?0:this.mEEPROM.writeUserData(data);
   }

   public byte[] eepromReadUserArea(int length) {
      return !this.isOpen()?null:this.mEEPROM.readUserData(length);
   }

   public int eepromGetUserAreaSize() {
      return !this.isOpen()?-1:this.mEEPROM.getUserSize();
   }

   public int eepromReadWord(short offset) {
      byte rc = -1;
      if(!this.isOpen()) {
         return rc;
      } else {
         int rc1 = this.mEEPROM.readWord(offset);
         return rc1;
      }
   }

   public boolean eepromWriteWord(short address, short data) {
      boolean rc = false;
      if(!this.isOpen()) {
         return rc;
      } else {
         rc = this.mEEPROM.writeWord(address, data);
         return rc;
      }
   }

   int getMaxPacketSize() {
      return this.mMaxPacketSize;
   }
}
