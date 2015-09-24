package com.ftdi.j2xx.ft4222;

import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.ft4222.FT_4222_Gpio;
import com.ftdi.j2xx.ft4222.FT_4222_I2c_Master;
import com.ftdi.j2xx.ft4222.FT_4222_I2c_Slave;
import com.ftdi.j2xx.ft4222.FT_4222_Spi_Master;
import com.ftdi.j2xx.ft4222.FT_4222_Spi_Slave;
import com.ftdi.j2xx.ft4222.a;
import com.ftdi.j2xx.ft4222.b;
import com.ftdi.j2xx.ft4222.e;
import com.ftdi.j2xx.interfaces.Gpio;
import com.ftdi.j2xx.interfaces.I2cMaster;
import com.ftdi.j2xx.interfaces.I2cSlave;
import com.ftdi.j2xx.interfaces.SpiMaster;
import com.ftdi.j2xx.interfaces.SpiSlave;

public class FT_4222_Device {
   protected String TAG = "FT4222";
   protected b mChipStatus;
   protected FT_Device mFtDev;
   protected e mGpio;
   protected a mSpiMasterCfg;

   public FT_4222_Device(FT_Device var1) {
      this.mFtDev = var1;
      this.mChipStatus = new b();
      this.mSpiMasterCfg = new a();
      this.mGpio = new e();
   }

   public boolean cleanRxData() {
      int var1 = this.mFtDev.getQueueStatus();
      if(var1 > 0) {
         byte[] var2 = new byte[var1];
         if(this.mFtDev.read(var2, var1) != var2.length) {
            return false;
         }
      }

      return true;
   }

   public int getClock(byte[] var1) {
      if(this.mFtDev.VendorCmdGet(32, 4, var1, 1) >= 0) {
         this.mChipStatus.f = var1[0];
         return 0;
      } else {
         return 18;
      }
   }

   public Gpio getGpioDevice() {
      return !this.isFT4222Device()?null:new FT_4222_Gpio(this);
   }

   public I2cMaster getI2cMasterDevice() {
      return !this.isFT4222Device()?null:new FT_4222_I2c_Master(this);
   }

   public I2cSlave getI2cSlaveDevice() {
      return !this.isFT4222Device()?null:new FT_4222_I2c_Slave(this);
   }

   protected int getMaxBuckSize() {
      if(this.mChipStatus.c != 0) {
         return 64;
      } else {
         switch(this.mChipStatus.a) {
         case 1:
         case 2:
            return 256;
         default:
            return 512;
         }
      }
   }

   public SpiMaster getSpiMasterDevice() {
      return !this.isFT4222Device()?null:new FT_4222_Spi_Master(this);
   }

   public SpiSlave getSpiSlaveDevice() {
      return !this.isFT4222Device()?null:new FT_4222_Spi_Slave(this);
   }

   public int init() {
      byte[] var1 = new byte[13];
      if(this.mFtDev.VendorCmdGet(32, 1, var1, 13) != 13) {
         return 18;
      } else {
         this.mChipStatus.a(var1);
         return 0;
      }
   }

   public boolean isFT4222Device() {
      if(this.mFtDev != null) {
         switch(65280 & this.mFtDev.getDeviceInfo().bcdDevice) {
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

   public int setClock(byte var1) {
      int var2;
      if(var1 == this.mChipStatus.f) {
         var2 = 0;
      } else {
         var2 = this.mFtDev.VendorCmdSet(33, 4 | var1 << 8);
         if(var2 == 0) {
            this.mChipStatus.f = var1;
            return var2;
         }
      }

      return var2;
   }
}
