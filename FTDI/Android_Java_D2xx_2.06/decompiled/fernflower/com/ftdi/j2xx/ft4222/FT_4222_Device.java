package com.ftdi.j2xx.ft4222;

import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.ft4222.FT_4222_Gpio;
import com.ftdi.j2xx.ft4222.FT_4222_I2c_Master;
import com.ftdi.j2xx.ft4222.FT_4222_I2c_Slave;
import com.ftdi.j2xx.ft4222.FT_4222_Spi_Master;
import com.ftdi.j2xx.ft4222.FT_4222_Spi_Slave;
import com.ftdi.j2xx.ft4222.SPI_MasterCfg;
import com.ftdi.j2xx.ft4222.chiptop_mgr;
import com.ftdi.j2xx.ft4222.gpio_mgr;
import com.ftdi.j2xx.interfaces.Gpio;
import com.ftdi.j2xx.interfaces.I2cMaster;
import com.ftdi.j2xx.interfaces.I2cSlave;
import com.ftdi.j2xx.interfaces.SpiMaster;
import com.ftdi.j2xx.interfaces.SpiSlave;

public class FT_4222_Device {
   protected String TAG = "FT4222";
   protected FT_Device mFtDev;
   protected chiptop_mgr mChipStatus;
   protected SPI_MasterCfg mSpiMasterCfg;
   protected gpio_mgr mGpio;
   protected char version;

   public FT_4222_Device(FT_Device ftDev) {
      this.mFtDev = ftDev;
      this.mChipStatus = new chiptop_mgr();
      this.mSpiMasterCfg = new SPI_MasterCfg();
      this.mGpio = new gpio_mgr();
   }

   public int init() {
      byte[] buf = new byte[13];
      int ftStatus = this.mFtDev.VendorCmdGet(32, 1, buf, 13);
      if(ftStatus != 13) {
         return 18;
      } else {
         this.mChipStatus.formByteArray(buf);
         byte[] bVer = new byte[12];
         ftStatus = this.mFtDev.VendorCmdGet(32, 0, bVer, 12);
         if(ftStatus < 0) {
            return 18;
         } else {
            if(bVer[2] == 1) {
               this.version = 65;
            } else if(bVer[2] == 2) {
               this.version = 66;
            } else if(bVer[2] >= 3) {
               this.version = 67;
            }

            return 0;
         }
      }
   }

   public int setClock(byte clk) {
      if(clk == this.mChipStatus.clk_ctl) {
         return 0;
      } else {
         int ftStatus = this.mFtDev.VendorCmdSet(33, 4 | clk << 8);
         if(ftStatus == 0) {
            this.mChipStatus.clk_ctl = clk;
         }

         return ftStatus;
      }
   }

   public int getClock(byte[] clk) {
      if(this.mFtDev.VendorCmdGet(32, 4, clk, 1) >= 0) {
         this.mChipStatus.clk_ctl = clk[0];
         return 0;
      } else {
         return 18;
      }
   }

   public boolean cleanRxData() {
      int ret = this.mFtDev.getQueueStatus();
      if(ret > 0) {
         byte[] rd_tmp_buf = new byte[ret];
         ret = this.mFtDev.read(rd_tmp_buf, ret);
         if(ret != rd_tmp_buf.length) {
            return false;
         }
      }

      return true;
   }

   protected int getMaxBuckSize() {
      if(this.mChipStatus.fs_only != 0) {
         return 64;
      } else {
         switch(this.mChipStatus.chip_mode) {
         case 0:
         case 3:
         default:
            return 512;
         case 1:
         case 2:
            return 256;
         }
      }
   }

   public boolean isFT4222Device() {
      if(this.mFtDev != null) {
         switch(this.mFtDev.getDeviceInfo().bcdDevice & 65280) {
         case 5888:
            this.mFtDev.getDeviceInfo().type = 12;
            return true;
         case 6144:
            this.mFtDev.getDeviceInfo().type = 10;
            return true;
         case 6400:
            this.mFtDev.getDeviceInfo().type = 11;
            return true;
         }
      }

      return false;
   }

   public I2cMaster getI2cMasterDevice() {
      return !this.isFT4222Device()?null:new FT_4222_I2c_Master(this);
   }

   public I2cSlave getI2cSlaveDevice() {
      return !this.isFT4222Device()?null:new FT_4222_I2c_Slave(this);
   }

   public SpiMaster getSpiMasterDevice() {
      return !this.isFT4222Device()?null:new FT_4222_Spi_Master(this);
   }

   public SpiSlave getSpiSlaveDevice() {
      return !this.isFT4222Device()?null:new FT_4222_Spi_Slave(this);
   }

   public Gpio getGpioDevice() {
      return !this.isFT4222Device()?null:new FT_4222_Gpio(this);
   }

   public char GetVersion() {
      return this.version;
   }
}
