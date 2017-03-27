package com.ftdi.j2xx.ft4222;

import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.ft4222.FT_4222_Device;
import com.ftdi.j2xx.interfaces.I2cSlave;

public class FT_4222_I2c_Slave implements I2cSlave {
   FT_4222_Device mFt4222Dev;
   FT_Device mFtDev;

   public FT_4222_I2c_Slave(FT_4222_Device ft4222Device) {
      this.mFt4222Dev = ft4222Device;
      this.mFtDev = this.mFt4222Dev.mFtDev;
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

   public int init() {
      int ftStatus = this.mFt4222Dev.init();
      if(ftStatus != 0) {
         return ftStatus;
      } else if(!this.I2C_ModeCheck()) {
         return 1012;
      } else {
         ftStatus = this.cmdSet(5, 2);
         if(ftStatus < 0) {
            return ftStatus;
         } else {
            this.mFt4222Dev.mChipStatus.function = 2;
            return 0;
         }
      }
   }

   public int reset() {
      byte reset = 1;
      int ftStatus = this.I2C_Check(false);
      return ftStatus != 0?ftStatus:this.cmdSet(91, reset);
   }

   public int getAddress(int[] addr) {
      byte[] bAddr = new byte[1];
      int ftStatus = this.I2C_Check(false);
      if(ftStatus != 0) {
         return ftStatus;
      } else {
         ftStatus = this.mFtDev.VendorCmdGet(33, 92, bAddr, 1);
         if(ftStatus < 0) {
            return 18;
         } else {
            addr[0] = bAddr[0];
            return 0;
         }
      }
   }

   public int setAddress(int addr) {
      byte[] bAddr = new byte[]{(byte)(addr & 255)};
      int ftStatus = this.I2C_Check(false);
      if(ftStatus != 0) {
         return ftStatus;
      } else {
         ftStatus = this.cmdSet(92, bAddr[0]);
         return ftStatus < 0?18:0;
      }
   }

   public int read(byte[] buffer, int sizeToTransfer, int[] sizeTransferred) {
      int[] maxSize = new int[1];
      long startTime = System.currentTimeMillis();
      int iTimeout = this.mFtDev.getReadTimeout();
      if(sizeToTransfer < 1) {
         return 6;
      } else {
         int ftStatus = this.I2C_Check(false);
         if(ftStatus != 0) {
            return ftStatus;
         } else {
            ftStatus = this.getMaxTransferSize(maxSize);
            if(ftStatus != 0) {
               return ftStatus;
            } else if(sizeToTransfer > maxSize[0]) {
               return 1010;
            } else {
               sizeTransferred[0] = 0;

               int dataSize;
               for(dataSize = this.mFtDev.getQueueStatus(); dataSize < sizeToTransfer && System.currentTimeMillis() - startTime < (long)iTimeout; dataSize = this.mFtDev.getQueueStatus()) {
                  ;
               }

               if(dataSize > sizeToTransfer) {
                  dataSize = sizeToTransfer;
               }

               ftStatus = this.mFtDev.read(buffer, dataSize);
               if(ftStatus < 0) {
                  return 1011;
               } else {
                  sizeTransferred[0] = ftStatus;
                  return 0;
               }
            }
         }
      }
   }

   public int write(byte[] buffer, int sizeToTransfer, int[] sizeTransferred) {
      int[] maxSize = new int[1];
      if(sizeToTransfer < 1) {
         return 6;
      } else {
         int ftStatus = this.I2C_Check(false);
         if(ftStatus != 0) {
            return ftStatus;
         } else {
            ftStatus = this.getMaxTransferSize(maxSize);
            if(ftStatus != 0) {
               return ftStatus;
            } else if(sizeToTransfer > maxSize[0]) {
               return 1010;
            } else {
               sizeTransferred[0] = 0;
               ftStatus = this.mFtDev.write(buffer, sizeToTransfer);
               sizeTransferred[0] = ftStatus;
               return sizeToTransfer == ftStatus?0:10;
            }
         }
      }
   }

   boolean I2C_ModeCheck() {
      return this.mFt4222Dev.mChipStatus.chip_mode == 0 || this.mFt4222Dev.mChipStatus.chip_mode == 3;
   }

   int I2C_Check(boolean isMaster) {
      if(isMaster) {
         if(this.mFt4222Dev.mChipStatus.function != 1) {
            return 1004;
         }
      } else if(this.mFt4222Dev.mChipStatus.function != 2) {
         return 1004;
      }

      return 0;
   }

   int getMaxTransferSize(int[] pMaxSize) {
      pMaxSize[0] = 0;
      int maxBuckSize = this.mFt4222Dev.getMaxBuckSize();
      switch(this.mFt4222Dev.mChipStatus.function) {
      case 2:
         pMaxSize[0] = maxBuckSize - 4;
         return 0;
      default:
         return 17;
      }
   }
}
