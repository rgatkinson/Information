package com.ftdi.j2xx.ft4222;

import android.util.Log;
import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.ft4222.FT_4222_Device;
import com.ftdi.j2xx.ft4222.chiptop_mgr;
import com.ftdi.j2xx.ft4222.gpio_dev;
import com.ftdi.j2xx.ft4222.gpio_mgr;
import com.ftdi.j2xx.interfaces.Gpio;

public class FT_4222_Gpio implements Gpio {
   private FT_4222_Device mFT4222Device;
   private FT_Device mFtDev;
   private static final int TOTAL_GPIOS = 4;
   static final int GET_STATUS = 32;
   static final int SET_DIRECTION = 33;
   static final int GET_DIRECTION = 33;
   static final int SET_PULL_UP = 34;
   static final int GET_PULL_UP = 34;
   static final int SET_OPEN_DRAIN = 35;
   static final int GET_OPEN_DRAIN = 35;
   static final int SET_PULL_DOWN = 36;
   static final int GET_PULL_DOWN = 36;

   public FT_4222_Gpio(FT_4222_Device ft4222Device) {
      this.mFT4222Device = ft4222Device;
      this.mFtDev = this.mFT4222Device.mFtDev;
   }

   int cmdSet(int wValue1, int wValue2) {
      return this.mFtDev.VendorCmdSet(33, wValue1 | wValue2 << 8);
   }

   int cmdSet(int wValue1, int wValue2, byte[] buf, int datalen) {
      return this.mFtDev.VendorCmdSet(33, wValue1 | wValue2 << 8, buf, datalen);
   }

   int cmdGet(int wValue1, int wValue2, byte[] buf, int datalen) {
      return this.mFtDev.VendorCmdGet(32, wValue1 | wValue2 << 8, buf, datalen);
   }

   public int init(int[] gpio) {
      chiptop_mgr chipStatus = this.mFT4222Device.mChipStatus;
      char cFwVer = this.getFWVersion();
      gpio_dev gpioStatus = new gpio_dev(cFwVer);
      byte[] data = new byte[1];
      gpio_mgr gpioMgr = new gpio_mgr();
      this.cmdSet(7, 0);
      this.cmdSet(6, 0);
      int ftStatus = this.mFT4222Device.init();
      if(ftStatus != 0) {
         Log.e("GPIO_M", "FT4222_GPIO init - 1 NG ftStatus:" + ftStatus);
         return ftStatus;
      } else if(chipStatus.chip_mode != 2 && chipStatus.chip_mode != 3) {
         this.getStatus(gpioStatus);
         byte dir = gpioStatus.dir;
         data[0] = gpioStatus.dat[0];

         for(int idx = 0; idx < 4; ++idx) {
            if(gpio[idx] == 1) {
               dir = (byte)((dir | 1 << idx) & 15);
            } else {
               dir = (byte)(dir & ~(1 << idx) & 15);
            }
         }

         gpioMgr.lastGpioData = data[0];
         this.cmdSet(33, dir);
         return 0;
      } else {
         return 1013;
      }
   }

   public int read(int portNum, boolean[] bValue) {
      char cFwVer = this.getFWVersion();
      gpio_dev gpioStatus = new gpio_dev(cFwVer);
      int ftStatus = this.check(portNum);
      if(ftStatus != 0) {
         return ftStatus;
      } else {
         ftStatus = this.getStatus(gpioStatus);
         if(ftStatus != 0) {
            return ftStatus;
         } else {
            this.getGpioPinLevel(portNum, gpioStatus.dat[0], bValue);
            return 0;
         }
      }
   }

   public int write(int portNum, boolean bValue) {
      char cFwVer = this.getFWVersion();
      gpio_dev gpioStatus = new gpio_dev(cFwVer);
      int ftStatus = this.check(portNum);
      if(ftStatus != 0) {
         return ftStatus;
      } else if(!this.is_GPIOPort_Valid_Output(portNum)) {
         return 1015;
      } else {
         this.getStatus(gpioStatus);
         if(bValue) {
            gpioStatus.dat[0] = (byte)(gpioStatus.dat[0] | 1 << portNum);
         } else {
            gpioStatus.dat[0] = (byte)(gpioStatus.dat[0] & ~(1 << portNum) & 15);
         }

         int iWriteByte = this.mFtDev.write(gpioStatus.dat, 1);
         return iWriteByte > 0?0:18;
      }
   }

   int check(int portNum) {
      chiptop_mgr chipStatus = this.mFT4222Device.mChipStatus;
      return chipStatus.chip_mode != 2 && chipStatus.chip_mode != 3?(portNum >= 4?1014:0):1013;
   }

   int getStatus(gpio_dev gpioStatus) {
      char cFwVer = this.getFWVersion();
      byte[] buf;
      if(cFwVer < 66) {
         buf = new byte[8];
      } else {
         buf = new byte[6];
      }

      int ftStatus = this.cmdGet(32, 0, buf, buf.length);
      gpioStatus.usb.ep_in = buf[0];
      gpioStatus.usb.ep_out = buf[1];
      gpioStatus.mask = buf[buf.length - 3];
      gpioStatus.dir = buf[buf.length - 2];
      gpioStatus.dat[0] = buf[buf.length - 1];
      return ftStatus == buf.length?0:ftStatus;
   }

   void getGpioPinLevel(int portNum, byte data, boolean[] value) {
      value[0] = this.IntToBool((data & 1 << portNum) >> portNum & 1);
   }

   boolean is_GPIOPort(int portNum) {
      chiptop_mgr chipStatus = this.mFT4222Device.mChipStatus;
      boolean ret = true;
      switch(chipStatus.chip_mode) {
      case 0:
         if((portNum == 0 || portNum == 1) && (chipStatus.function == 1 || chipStatus.function == 2)) {
            ret = false;
         }

         if(this.IntToBool(chipStatus.enable_suspend_out) && portNum == 2) {
            ret = false;
         }

         if(this.IntToBool(chipStatus.enable_wakeup_int) && portNum == 3) {
            ret = false;
         }
         break;
      case 1:
         if(portNum == 0 || portNum == 1) {
            ret = false;
         }

         if(this.IntToBool(chipStatus.enable_suspend_out) && portNum == 2) {
            ret = false;
         }

         if(this.IntToBool(chipStatus.enable_wakeup_int) && portNum == 3) {
            ret = false;
         }
         break;
      case 2:
      case 3:
         ret = false;
      }

      return ret;
   }

   boolean is_GPIOPort_Valid_Output(int portNum) {
      char cFwVer = this.getFWVersion();
      gpio_dev gpioStatus = new gpio_dev(cFwVer);
      boolean ret = this.is_GPIOPort(portNum);
      this.getStatus(gpioStatus);
      if(ret && (gpioStatus.dir >> portNum & 1) != 1) {
         ret = false;
      }

      return ret;
   }

   boolean is_GPIOPort_Valid_Input(int portNum) {
      char cFwVer = this.getFWVersion();
      gpio_dev gpioStatus = new gpio_dev(cFwVer);
      boolean ret = this.is_GPIOPort(portNum);
      this.getStatus(gpioStatus);
      if(ret && (gpioStatus.dir >> portNum & 1) != 0) {
         ret = false;
      }

      return ret;
   }

   boolean update_GPIO_Status(int portNum, int gpioStatus) {
      gpio_mgr gpio = new gpio_mgr();
      if(gpio.gpioStatus[portNum] != gpioStatus) {
         char pullup = 0;
         char pulldown = 0;
         char opendrain = 0;
         gpio.gpioStatus[portNum] = gpioStatus;

         for(int idx = 0; idx < 4; ++idx) {
            switch(gpio.gpioStatus[idx]) {
            case 1:
               pullup = (char)(pullup + (1 << idx));
               break;
            case 2:
               pulldown = (char)(pulldown + (1 << idx));
               break;
            case 3:
               opendrain = (char)(opendrain + (1 << idx));
            }
         }

         int ftStatus = this.cmdSet(34, pullup);
         ftStatus |= this.cmdSet(36, pulldown);
         ftStatus |= this.cmdSet(35, opendrain);
         if(ftStatus == 0) {
            gpio.gpioStatus[portNum] = gpioStatus;
         }

         if(ftStatus == 0) {
            return true;
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   boolean IntToBool(int i) {
      return i != 0;
   }

   char getFWVersion() {
      return this.mFT4222Device.GetVersion();
   }
}
