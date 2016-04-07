package com.ftdi.j2xx;

import android.util.Log;
import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.FT_EEPROM;

class FT_EE_Ctrl {
   private static final short EE_MAX_SIZE = 1024;
   private static final int PULL_DOWN_IN_USB_SUSPEND = 4;
   private static final int ENABLE_SERIAL_NUMBER = 8;
   private static final int SELF_POWERED = 64;
   private static final int BUS_POWERED = 128;
   private static final int USB_REMOTE_WAKEUP = 32;
   private FT_Device mDevice;
   short mEepromType;
   int mEepromSize;
   boolean mEepromBlank;

   FT_EE_Ctrl(FT_Device dev) {
      this.mDevice = dev;
   }

   int readWord(short offset) {
      byte[] dataRead = new byte[2];
      byte rc = -1;
      if(offset >= 1024) {
         return rc;
      } else {
         this.mDevice.getConnection().controlTransfer(-64, 144, 0, offset, dataRead, 2, 0);
         int value = dataRead[1] & 255;
         value <<= 8;
         value |= dataRead[0] & 255;
         return value;
      }
   }

   boolean writeWord(short offset, short value) {
      int wValue = value & '\uffff';
      int wIndex = offset & '\uffff';
      boolean status = false;
      boolean rc = false;
      if(offset >= 1024) {
         return rc;
      } else {
         int status1 = this.mDevice.getConnection().controlTransfer(64, 145, wValue, wIndex, (byte[])null, 0, 0);
         if(status1 == 0) {
            rc = true;
         }

         return rc;
      }
   }

   int eraseEeprom() {
      boolean status = false;
      int status1 = this.mDevice.getConnection().controlTransfer(64, 146, 0, 0, (byte[])null, 0, 0);
      return status1;
   }

   short programEeprom(FT_EEPROM eeprom) {
      return (short)1;
   }

   boolean programEeprom(int[] dataToWrite, int ee_size) {
      int checksumLocation = ee_size;
      int Checksum = 'ꪪ';
      boolean TempChecksum = false;
      int addressCounter = 0;
      boolean a = false;
      boolean b = false;

      while(addressCounter < checksumLocation) {
         this.writeWord((short)addressCounter, (short)dataToWrite[addressCounter]);
         int var9 = dataToWrite[addressCounter] ^ Checksum;
         var9 &= '\uffff';
         short var10 = (short)(var9 << 1 & '\uffff');
         short var11 = (short)(var9 >> 15 & '\uffff');
         Checksum = var10 | var11;
         Checksum &= '\uffff';
         ++addressCounter;
         Log.d("FT_EE_Ctrl", "Entered WriteWord Checksum : " + Checksum);
      }

      this.writeWord((short)checksumLocation, (short)Checksum);
      return true;
   }

   FT_EEPROM readEeprom() {
      return null;
   }

   int setUSBConfig(Object ee) {
      FT_EEPROM ft = (FT_EEPROM)ee;
      boolean word0x04 = false;
      byte lowerbits = 0;
      boolean upperbits = false;
      int lowerbits1 = lowerbits | 128;
      if(ft.RemoteWakeup) {
         lowerbits1 |= 32;
      }

      if(ft.SelfPowered) {
         lowerbits1 |= 64;
      }

      short upperbits1 = ft.MaxPower;
      int upperbits2 = upperbits1 / 2;
      upperbits2 <<= 8;
      int word0x041 = upperbits2 | lowerbits1;
      return word0x041;
   }

   void getUSBConfig(FT_EEPROM ee, int dataRead) {
      byte mP = (byte)(dataRead >> 8);
      ee.MaxPower = (short)(2 * mP);
      byte P = (byte)dataRead;
      if((P & 64) == 64 && (P & 128) == 128) {
         ee.SelfPowered = true;
      } else {
         ee.SelfPowered = false;
      }

      if((P & 32) == 32) {
         ee.RemoteWakeup = true;
      } else {
         ee.RemoteWakeup = false;
      }

   }

   int setDeviceControl(Object ee) {
      FT_EEPROM ft = (FT_EEPROM)ee;
      byte data = 0;
      int data1;
      if(ft.PullDownEnable) {
         data1 = data | 4;
      } else {
         data1 = data & 251;
      }

      if(ft.SerNumEnable) {
         data1 |= 8;
      } else {
         data1 &= 247;
      }

      return data1;
   }

   void getDeviceControl(Object ee, int dataRead) {
      FT_EEPROM ft = (FT_EEPROM)ee;
      if((dataRead & 4) > 0) {
         ft.PullDownEnable = true;
      } else {
         ft.PullDownEnable = false;
      }

      if((dataRead & 8) > 0) {
         ft.SerNumEnable = true;
      } else {
         ft.SerNumEnable = false;
      }

   }

   int setStringDescriptor(String s, int[] data, int addrs, int pointer, boolean rdevice) {
      int i = 0;
      int strLength = s.length() * 2 + 2;
      data[pointer] = strLength << 8 | addrs * 2;
      if(rdevice) {
         data[pointer] += 128;
      }

      char[] strchar = s.toCharArray();
      data[addrs++] = strLength | 768;
      strLength -= 2;
      strLength /= 2;

      do {
         data[addrs++] = strchar[i];
         ++i;
      } while(i < strLength);

      return addrs;
   }

   String getStringDescriptor(int addr, int[] dataRead) {
      String descriptor = "";
      int len = dataRead[addr] & 255;
      len = len / 2 - 1;
      ++addr;
      int endaddr = addr + len;

      for(int i = addr; i < endaddr; ++i) {
         descriptor = descriptor + (char)dataRead[i];
      }

      return descriptor;
   }

   void clearUserDataArea(int saddr, int eeprom_size, int[] data) {
      while(saddr < eeprom_size) {
         data[saddr++] = 0;
      }

   }

   int getEepromSize(byte location) throws D2xxManager.D2xxException {
      short data = 192;
      boolean wordRead = false;
      boolean eeData = false;
      short address = (short)(location & -1);
      int[] dataRead = new int[3];
      boolean rc = false;
      short eeData1 = (short)this.readWord(address);
      if(eeData1 != '\uffff') {
         switch(eeData1) {
         case 70:
            this.mEepromType = 70;
            this.mEepromSize = 64;
            this.mEepromBlank = false;
            return 64;
         case 82:
            this.mEepromType = 82;
            this.mEepromSize = 1024;
            this.mEepromBlank = false;
            return 1024;
         case 86:
            this.mEepromType = 86;
            this.mEepromSize = 128;
            this.mEepromBlank = false;
            return 128;
         case 102:
            this.mEepromType = 102;
            this.mEepromSize = 128;
            this.mEepromBlank = false;
            return 256;
         default:
            return 0;
         }
      } else {
         short address1 = 192;
         rc = this.writeWord(address1, (short)data);
         dataRead[0] = this.readWord((short)192);
         dataRead[1] = this.readWord((short)64);
         dataRead[2] = this.readWord((short)0);
         if(!rc) {
            this.mEepromType = 255;
            this.mEepromSize = 0;
            return 0;
         } else {
            this.mEepromBlank = true;
            byte address2 = 0;
            int wordRead1 = this.readWord(address2);
            if((wordRead1 & 255) == 192) {
               this.eraseEeprom();
               this.mEepromType = 70;
               this.mEepromSize = 64;
               return 64;
            } else {
               address2 = 64;
               wordRead1 = this.readWord(address2);
               if((wordRead1 & 255) == 192) {
                  this.eraseEeprom();
                  this.mEepromType = 86;
                  this.mEepromSize = 128;
                  return 128;
               } else {
                  address1 = 192;
                  wordRead1 = this.readWord(address1);
                  if((wordRead1 & 255) == 192) {
                     this.eraseEeprom();
                     this.mEepromType = 102;
                     this.mEepromSize = 128;
                     return 256;
                  } else {
                     this.eraseEeprom();
                     return 0;
                  }
               }
            }
         }
      }
   }

   int writeUserData(byte[] data) {
      return 0;
   }

   byte[] readUserData(int length) {
      return null;
   }

   int getUserSize() {
      return 0;
   }

   static final class EepromType {
      static final short TYPE_46 = 70;
      static final short TYPE_52 = 82;
      static final short TYPE_56 = 86;
      static final short TYPE_66 = 102;
      static final short TYPE_MTP = 1;
      static final short INVALID = 255;
   }
}
