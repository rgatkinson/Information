package com.ftdi.j2xx;

import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.FT_EEPROM;
import com.ftdi.j2xx.FT_EEPROM_232R;
import com.ftdi.j2xx.FT_EE_Ctrl;

class FT_EE_232R_Ctrl extends FT_EE_Ctrl {
   private static final short EEPROM_SIZE = 80;
   private static final short ENDOFUSERLOCATION = 63;
   private static final short EE_MAX_SIZE = 1024;
   private static final int EXTERNAL_OSCILLATOR = 2;
   private static final int HIGH_CURRENT_IO = 4;
   private static final int LOAD_D2XX_DRIVER = 8;
   private static final int INVERT_TXD = 256;
   private static final int INVERT_RXD = 512;
   private static final int INVERT_RTS = 1024;
   private static final int INVERT_CTS = 2048;
   private static final int INVERT_DTR = 4096;
   private static final int INVERT_DSR = 8192;
   private static final int INVERT_DCD = 16384;
   private static final int INVERT_RI = 32768;
   private static FT_Device ft_device;

   FT_EE_232R_Ctrl(FT_Device usbC) {
      super(usbC);
      ft_device = usbC;
   }

   boolean writeWord(short offset, short value) {
      int wValue = value & '\uffff';
      int wIndex = offset & '\uffff';
      boolean status = false;
      boolean rc = false;
      boolean latency = false;
      if(offset >= 1024) {
         return rc;
      } else {
         byte latency1 = ft_device.getLatencyTimer();
         ft_device.setLatencyTimer((byte)119);
         int status1 = ft_device.getConnection().controlTransfer(64, 145, wValue, wIndex, (byte[])null, 0, 0);
         if(status1 == 0) {
            rc = true;
         }

         ft_device.setLatencyTimer(latency1);
         return rc;
      }
   }

   short programEeprom(FT_EEPROM ee) {
      if(ee.getClass() != FT_EEPROM_232R.class) {
         return (short)1;
      } else {
         int[] data = new int[80];
         FT_EEPROM_232R eeprom = (FT_EEPROM_232R)ee;

         try {
            for(short e = 0; e < 80; ++e) {
               data[e] = this.readWord(e);
            }

            byte wordx00 = 0;
            int var17 = wordx00 | data[0] & '\uff00';
            if(eeprom.HighIO) {
               var17 |= 4;
            }

            if(eeprom.LoadVCP) {
               var17 |= 8;
            }

            if(eeprom.ExternalOscillator) {
               var17 |= 2;
            } else {
               var17 &= '�';
            }

            data[0] = var17;
            data[1] = eeprom.VendorId;
            data[2] = eeprom.ProductId;
            data[3] = 1536;
            data[4] = this.setUSBConfig(ee);
            int wordx05 = this.setDeviceControl(ee);
            if(eeprom.InvertTXD) {
               wordx05 |= 256;
            }

            if(eeprom.InvertRXD) {
               wordx05 |= 512;
            }

            if(eeprom.InvertRTS) {
               wordx05 |= 1024;
            }

            if(eeprom.InvertCTS) {
               wordx05 |= 2048;
            }

            if(eeprom.InvertDTR) {
               wordx05 |= 4096;
            }

            if(eeprom.InvertDSR) {
               wordx05 |= 8192;
            }

            if(eeprom.InvertDCD) {
               wordx05 |= 16384;
            }

            if(eeprom.InvertRI) {
               wordx05 |= '耀';
            }

            data[5] = wordx05;
            boolean wordx0A = false;
            byte var19 = eeprom.CBus0;
            byte c1 = eeprom.CBus1;
            int var20 = c1 << 4;
            byte c2 = eeprom.CBus2;
            int var21 = c2 << 8;
            byte c3 = eeprom.CBus3;
            int var22 = c3 << 12;
            int var18 = var19 | var20 | var21 | var22;
            data[10] = var18;
            boolean wordx0B = false;
            byte c4 = eeprom.CBus4;
            data[11] = c4;
            byte saddr = 12;
            int var23 = this.setStringDescriptor(eeprom.Manufacturer, data, saddr, 7, true);
            var23 = this.setStringDescriptor(eeprom.Product, data, var23, 8, true);
            if(eeprom.SerNumEnable) {
               this.setStringDescriptor(eeprom.SerialNumber, data, var23, 9, true);
            }

            boolean latency = false;
            if(data[1] != 0 && data[2] != 0) {
               boolean returnCode = false;
               byte var24 = ft_device.getLatencyTimer();
               ft_device.setLatencyTimer((byte)119);
               returnCode = this.programEeprom(data, 63);
               ft_device.setLatencyTimer(var24);
               return (short)(returnCode?0:1);
            } else {
               return (short)2;
            }
         } catch (Exception var16) {
            var16.printStackTrace();
            return (short)0;
         }
      }
   }

   FT_EEPROM readEeprom() {
      FT_EEPROM_232R eeprom = new FT_EEPROM_232R();
      int[] data = new int[80];

      try {
         int e;
         for(e = 0; e < 80; ++e) {
            data[e] = this.readWord((short)e);
         }

         if((data[0] & 4) == 4) {
            eeprom.HighIO = true;
         } else {
            eeprom.HighIO = false;
         }

         if((data[0] & 8) == 8) {
            eeprom.LoadVCP = true;
         } else {
            eeprom.LoadVCP = false;
         }

         if((data[0] & 2) == 2) {
            eeprom.ExternalOscillator = true;
         } else {
            eeprom.ExternalOscillator = false;
         }

         eeprom.VendorId = (short)data[1];
         eeprom.ProductId = (short)data[2];
         this.getUSBConfig(eeprom, data[4]);
         this.getDeviceControl(eeprom, data[5]);
         if((data[5] & 256) == 256) {
            eeprom.InvertTXD = true;
         } else {
            eeprom.InvertTXD = false;
         }

         if((data[5] & 512) == 512) {
            eeprom.InvertRXD = true;
         } else {
            eeprom.InvertRXD = false;
         }

         if((data[5] & 1024) == 1024) {
            eeprom.InvertRTS = true;
         } else {
            eeprom.InvertRTS = false;
         }

         if((data[5] & 2048) == 2048) {
            eeprom.InvertCTS = true;
         } else {
            eeprom.InvertCTS = false;
         }

         if((data[5] & 4096) == 4096) {
            eeprom.InvertDTR = true;
         } else {
            eeprom.InvertDTR = false;
         }

         if((data[5] & 8192) == 8192) {
            eeprom.InvertDSR = true;
         } else {
            eeprom.InvertDSR = false;
         }

         if((data[5] & 16384) == 16384) {
            eeprom.InvertDCD = true;
         } else {
            eeprom.InvertDCD = false;
         }

         if((data[5] & '耀') == '耀') {
            eeprom.InvertRI = true;
         } else {
            eeprom.InvertRI = false;
         }

         e = data[10];
         int cbus0 = e & 15;
         eeprom.CBus0 = (byte)cbus0;
         int cbus1 = e & 240;
         eeprom.CBus1 = (byte)(cbus1 >> 4);
         int cbus2 = e & 3840;
         eeprom.CBus2 = (byte)(cbus2 >> 8);
         int cbus3 = e & '\uf000';
         eeprom.CBus3 = (byte)(cbus3 >> 12);
         int cbus4 = data[11] & 255;
         eeprom.CBus4 = (byte)cbus4;
         int addr = data[7] & 255;
         addr -= 128;
         addr /= 2;
         eeprom.Manufacturer = this.getStringDescriptor(addr, data);
         addr = data[8] & 255;
         addr -= 128;
         addr /= 2;
         eeprom.Product = this.getStringDescriptor(addr, data);
         addr = data[9] & 255;
         addr -= 128;
         addr /= 2;
         eeprom.SerialNumber = this.getStringDescriptor(addr, data);
         return eeprom;
      } catch (Exception var10) {
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
      int ptr = 12 + ptr07 + ptr08 + 1;
      data = this.readWord((short)9);
      int length = (data & '\uff00') >> 8;
      length /= 2;
      return (63 - ptr - length - 1) * 2;
   }

   int writeUserData(byte[] data) {
      boolean dataWrite = false;
      boolean offset = false;
      if(data.length > this.getUserSize()) {
         return 0;
      } else {
         int[] eeprom = new int[80];

         for(short latency = 0; latency < 80; ++latency) {
            eeprom[latency] = this.readWord(latency);
         }

         short var8 = (short)(63 - this.getUserSize() / 2 - 1);
         var8 = (short)(var8 & '\uffff');

         for(int var9 = 0; var9 < data.length; var9 += 2) {
            int var7;
            if(var9 + 1 < data.length) {
               var7 = data[var9 + 1] & 255;
            } else {
               var7 = 0;
            }

            var7 <<= 8;
            var7 |= data[var9] & 255;
            eeprom[var8++] = var7;
         }

         boolean var10 = false;
         if(eeprom[1] != 0 && eeprom[2] != 0) {
            boolean returnCode = false;
            byte var11 = ft_device.getLatencyTimer();
            ft_device.setLatencyTimer((byte)119);
            returnCode = this.programEeprom(eeprom, 63);
            ft_device.setLatencyTimer(var11);
            if(!returnCode) {
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
      if(length != 0 && length <= this.getUserSize()) {
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
