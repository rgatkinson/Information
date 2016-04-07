package com.ftdi.j2xx.ft4222;

import android.util.Log;
import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.ft4222.FT_4222_Device;
import com.ftdi.j2xx.ft4222.SPI_MasterCfg;
import com.ftdi.j2xx.ft4222.chiptop_mgr;
import com.ftdi.j2xx.interfaces.SpiSlave;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FT_4222_Spi_Slave implements SpiSlave {
   private FT_4222_Device mFT4222Device;
   private FT_Device mFTDevice;
   private Lock m_pDevLock;
   private static final String TAG = "FTDI_Device::";

   public FT_4222_Spi_Slave(FT_4222_Device pDevice) {
      this.mFT4222Device = pDevice;
      this.mFTDevice = pDevice.mFtDev;
      this.m_pDevLock = new ReentrantLock();
   }

   public int init() {
      byte status = 0;
      chiptop_mgr chipStatus = this.mFT4222Device.mChipStatus;
      SPI_MasterCfg config = this.mFT4222Device.mSpiMasterCfg;
      byte ioLine = 1;
      byte clock = 2;
      byte cpol = 0;
      byte cpha = 0;
      byte ss = 0;
      byte ssoMap = 1;
      byte fun = 4;
      config.ioLine = ioLine;
      config.clock = clock;
      config.cpol = cpol;
      config.cpha = cpha;
      config.ssoMap = ssoMap;
      this.m_pDevLock.lock();
      this.mFT4222Device.cleanRxData();
      if(this.mFTDevice.VendorCmdSet(33, 66 | config.ioLine << 8) < 0) {
         status = 4;
      }

      if(this.mFTDevice.VendorCmdSet(33, 68 | config.clock << 8) < 0) {
         status = 4;
      }

      if(this.mFTDevice.VendorCmdSet(33, 69 | config.cpol << 8) < 0) {
         status = 4;
      }

      if(this.mFTDevice.VendorCmdSet(33, 70 | config.cpha << 8) < 0) {
         status = 4;
      }

      if(this.mFTDevice.VendorCmdSet(33, 67 | ss << 8) < 0) {
         status = 4;
      }

      if(this.mFTDevice.VendorCmdSet(33, 72 | config.ssoMap << 8) < 0) {
         status = 4;
      }

      if(this.mFTDevice.VendorCmdSet(33, 5 | fun << 8) < 0) {
         status = 4;
      }

      this.m_pDevLock.unlock();
      chipStatus.function = 4;
      return status;
   }

   public int getRxStatus(int[] pRxSize) {
      if(pRxSize == null) {
         return 1009;
      } else {
         int status = this.check();
         if(status != 0) {
            return status;
         } else {
            this.m_pDevLock.lock();
            int ret = this.mFTDevice.getQueueStatus();
            this.m_pDevLock.unlock();
            byte status1;
            if(ret >= 0) {
               pRxSize[0] = ret;
               status1 = 0;
            } else {
               pRxSize[0] = -1;
               status1 = 4;
            }

            return status1;
         }
      }
   }

   public int read(byte[] buffer, int bufferSize, int[] sizeOfRead) {
      boolean status = false;
      this.m_pDevLock.lock();
      if(this.mFTDevice != null && this.mFTDevice.isOpen()) {
         int ret = this.mFTDevice.read(buffer, bufferSize);
         this.m_pDevLock.unlock();
         sizeOfRead[0] = ret;
         byte status1;
         if(ret >= 0) {
            status1 = 0;
         } else {
            status1 = 4;
         }

         return status1;
      } else {
         this.m_pDevLock.unlock();
         return 3;
      }
   }

   public int write(byte[] buffer, int bufferSize, int[] sizeTransferred) {
      boolean status = false;
      if(sizeTransferred != null && buffer != null) {
         int status1 = this.check();
         if(status1 != 0) {
            return status1;
         } else if(bufferSize > 512) {
            return 1010;
         } else {
            this.m_pDevLock.lock();
            sizeTransferred[0] = this.mFTDevice.write(buffer, bufferSize);
            this.m_pDevLock.unlock();
            if(sizeTransferred[0] != bufferSize) {
               Log.e("FTDI_Device::", "Error write =" + bufferSize + " tx=" + sizeTransferred[0]);
               status1 = 4;
            }

            return status1;
         }
      } else {
         return 1009;
      }
   }

   private int check() {
      chiptop_mgr chipStatus = this.mFT4222Device.mChipStatus;
      return chipStatus.function != 4?1003:0;
   }

   public int reset() {
      byte verderReset = 0;
      byte status = 0;
      this.m_pDevLock.lock();
      if(this.mFTDevice.VendorCmdSet(33, 74 | verderReset << 8) < 0) {
         status = 4;
      }

      this.m_pDevLock.unlock();
      return status;
   }

   public int setDrivingStrength(int clkStrength, int ioStrength, int ssoStregth) {
      byte status = 0;
      chiptop_mgr chipStatus = this.mFT4222Device.mChipStatus;
      if(chipStatus.function != 3 && chipStatus.function != 4) {
         return 1003;
      } else {
         int actual_strength = clkStrength << 4;
         actual_strength |= ioStrength << 2;
         actual_strength |= ssoStregth;
         byte verderFun;
         if(chipStatus.function == 3) {
            verderFun = 3;
         } else {
            verderFun = 4;
         }

         this.m_pDevLock.lock();
         if(this.mFTDevice.VendorCmdSet(33, 160 | actual_strength << 8) < 0) {
            status = 4;
         }

         if(this.mFTDevice.VendorCmdSet(33, 5 | verderFun << 8) < 0) {
            status = 4;
         }

         this.m_pDevLock.unlock();
         return status;
      }
   }
}
