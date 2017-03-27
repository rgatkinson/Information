package com.ftdi.j2xx;

import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.FT_EEPROM;
import com.ftdi.j2xx.FT_EE_Ctrl;

class FT_EE_232A_Ctrl extends FT_EE_Ctrl {
   private static final short EEPROM_SIZE = 64;
   private static final short CHECKSUM_LOCATION = 63;

   FT_EE_232A_Ctrl(FT_Device usbC) {
      super(usbC);
   }

   short programEeprom(FT_EEPROM ee) {
      int[] data = new int[64];
      if(ee.getClass() != FT_EEPROM.class) {
         return (short)1;
      } else {
         FT_EEPROM eeprom = ee;

         try {
            data[1] = eeprom.VendorId;
            data[2] = eeprom.ProductId;
            data[3] = 512;
            data[4] = this.setUSBConfig(ee);
            byte e = 10;
            int e1 = this.setStringDescriptor(eeprom.Manufacturer, data, e, 7, true);
            e1 += eeprom.Manufacturer.length() + 2;
            e1 = this.setStringDescriptor(eeprom.Product, data, e1, 8, true);
            e1 += eeprom.Product.length() + 2;
            if(eeprom.SerNumEnable) {
               e1 = this.setStringDescriptor(eeprom.SerialNumber, data, e1, 9, true);
               int var10000 = e1 + eeprom.SerialNumber.length() + 2;
            }

            if(data[1] != 0 && data[2] != 0) {
               boolean returnCode = false;
               returnCode = this.programEeprom(data, 63);
               return (short)(returnCode?0:1);
            } else {
               return (short)2;
            }
         } catch (Exception var6) {
            var6.printStackTrace();
            return (short)0;
         }
      }
   }

   FT_EEPROM readEeprom() {
      FT_EEPROM eeprom = new FT_EEPROM();
      int[] data = new int[64];

      try {
         int e;
         for(e = 0; e < 64; ++e) {
            data[e] = this.readWord((short)e);
         }

         eeprom.VendorId = (short)data[1];
         eeprom.ProductId = (short)data[2];
         this.getUSBConfig(eeprom, data[4]);
         byte var5 = 10;
         eeprom.Manufacturer = this.getStringDescriptor(var5, data);
         e = var5 + eeprom.Manufacturer.length() + 1;
         eeprom.Product = this.getStringDescriptor(e, data);
         e += eeprom.Product.length() + 1;
         eeprom.SerialNumber = this.getStringDescriptor(e, data);
         return eeprom;
      } catch (Exception var4) {
         return null;
      }
   }

   int getUserSize() {
      int data = this.readWord((short)7);
      int ptr07 = (data & '\uff00') >> 8;
      ptr07 /= 2;
      data = this.readWord((short)8);
      int ptr08 = (data & '\uff00') >> 8;
      ptr08 /= 2;
      int ptr = 10 + ptr07 + ptr08 + 1;
      data = this.readWord((short)9);
      int length = (data & '\uff00') >> 8;
      length /= 2;
      return (63 - ptr - 1 - length) * 2;
   }

   int writeUserData(byte[] data) {
      boolean dataWrite = false;
      boolean offset = false;
      if(data.length > this.getUserSize()) {
         return 0;
      } else {
         int[] eeprom = new int[64];

         for(short returnCode = 0; returnCode < 64; ++returnCode) {
            eeprom[returnCode] = this.readWord(returnCode);
         }

         short var7 = (short)(63 - this.getUserSize() / 2 - 1);
         var7 = (short)(var7 & '\uffff');

         for(int var8 = 0; var8 < data.length; var8 += 2) {
            int var6;
            if(var8 + 1 < data.length) {
               var6 = data[var8 + 1] & 255;
            } else {
               var6 = 0;
            }

            var6 <<= 8;
            var6 |= data[var8] & 255;
            eeprom[var7++] = var6;
         }

         if(eeprom[1] != 0 && eeprom[2] != 0) {
            boolean var9 = false;
            var9 = this.programEeprom(eeprom, 63);
            if(!var9) {
               return 0;
            } else {
               return data.length;
            }
         } else {
            return 0;
         }
      }
   }

   byte[] readUserData(int length) {
      boolean Hi = false;
      boolean Lo = false;
      boolean dataRead = false;
      byte[] data = new byte[length];
      if(length != 0 && length <= this.getUserSize() - 1) {
         short offset = (short)(63 - this.getUserSize() / 2 - 1);
         offset = (short)(offset & '\uffff');

         for(int i = 0; i < length; i += 2) {
            int var10 = this.readWord(offset++);
            if(i + 1 < data.length) {
               byte var8 = (byte)(var10 & 255);
               data[i + 1] = var8;
            } else {
               Lo = false;
            }

            byte var9 = (byte)((var10 & '\uff00') >> 8);
            data[i] = var9;
         }

         return data;
      } else {
         return null;
      }
   }
}
