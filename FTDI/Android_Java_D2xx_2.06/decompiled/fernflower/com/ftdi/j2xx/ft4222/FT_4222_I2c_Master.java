package com.ftdi.j2xx.ft4222;

import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.ft4222.FT_4222_Device;
import com.ftdi.j2xx.interfaces.I2cMaster;

public class FT_4222_I2c_Master implements I2cMaster {
   FT_4222_Device mFt4222Dev;
   FT_Device mFtDev;
   int mI2cMasterKbps;

   public FT_4222_I2c_Master(FT_4222_Device ft4222Device) {
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

   public int init(int kbps) {
      byte[] clk = new byte[1];
      int ftStatus = this.mFt4222Dev.init();
      if(ftStatus != 0) {
         return ftStatus;
      } else if(!this.I2C_Mode_Check()) {
         return 1012;
      } else {
         this.cmdSet(81, 0);
         ftStatus = this.mFt4222Dev.getClock(clk);
         if(ftStatus != 0) {
            return ftStatus;
         } else {
            int i2cMP = this.i2c_master_setup_timer_period(clk[0], kbps);
            ftStatus = this.cmdSet(5, 1);
            if(ftStatus < 0) {
               return ftStatus;
            } else {
               this.mFt4222Dev.mChipStatus.function = 1;
               ftStatus = this.cmdSet(82, i2cMP);
               if(ftStatus < 0) {
                  return ftStatus;
               } else {
                  this.mI2cMasterKbps = kbps;
                  return 0;
               }
            }
         }
      }
   }

   public int reset() {
      byte reset = 1;
      int ftStatus = this.I2C_Check(true);
      return ftStatus != 0?ftStatus:this.cmdSet(81, reset);
   }

   public int read(int deviceAddress, byte[] buffer, int sizeToTransfer, int[] sizeTransferred) {
      return this.readEx(deviceAddress, 6, buffer, sizeToTransfer, sizeTransferred);
   }

   public int readEx(int deviceAddress, int flag, byte[] buffer, int sizeToTransfer, int[] sizeTransferred) {
      short slave_addr = (short)(deviceAddress & '\uffff');
      short shortSizeToTransfer = (short)sizeToTransfer;
      int[] maxSize = new int[1];
      byte[] headBuf = new byte[4];
      long startTime = System.currentTimeMillis();
      int iTimeout = this.mFtDev.getReadTimeout();
      int ftStatus = this.I2C_Version_Check(flag);
      if(ftStatus != 0) {
         return ftStatus;
      } else {
         ftStatus = this.I2C_Address_Check(deviceAddress);
         if(ftStatus != 0) {
            return ftStatus;
         } else if(sizeToTransfer < 1) {
            return 6;
         } else {
            ftStatus = this.I2C_Check(true);
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
                  slave_addr = (short)((slave_addr << 1) + 1);
                  headBuf[0] = (byte)slave_addr;
                  headBuf[1] = (byte)flag;
                  headBuf[2] = (byte)(shortSizeToTransfer >> 8 & 255);
                  headBuf[3] = (byte)(shortSizeToTransfer & 255);
                  ftStatus = this.mFtDev.write(headBuf, 4);
                  if(4 != ftStatus) {
                     return 1011;
                  } else {
                     int dataSize;
                     for(dataSize = this.mFtDev.getQueueStatus(); dataSize < sizeToTransfer && System.currentTimeMillis() - startTime < (long)iTimeout; dataSize = this.mFtDev.getQueueStatus()) {
                        ;
                     }

                     if(dataSize > sizeToTransfer) {
                        dataSize = sizeToTransfer;
                     }

                     ftStatus = this.mFtDev.read(buffer, dataSize);
                     sizeTransferred[0] = ftStatus;
                     return ftStatus >= 0?0:1011;
                  }
               }
            }
         }
      }
   }

   public int write(int deviceAddress, byte[] buffer, int sizeToTransfer, int[] sizeTransferred) {
      return this.writeEx(deviceAddress, 6, buffer, sizeToTransfer, sizeTransferred);
   }

   public int writeEx(int deviceAddress, int flag, byte[] buffer, int sizeToTransfer, int[] sizeTransferred) {
      short slave_addr = (short)deviceAddress;
      short shortSizeToTransfer = (short)sizeToTransfer;
      byte[] transferBuf = new byte[sizeToTransfer + 4];
      int[] maxSize = new int[1];
      int ftStatus = this.I2C_Version_Check(flag);
      if(ftStatus != 0) {
         return ftStatus;
      } else {
         ftStatus = this.I2C_Address_Check(deviceAddress);
         if(ftStatus != 0) {
            return ftStatus;
         } else if(sizeToTransfer < 1) {
            return 6;
         } else {
            ftStatus = this.I2C_Check(true);
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
                  slave_addr = (short)(slave_addr << 1);
                  transferBuf[0] = (byte)slave_addr;
                  transferBuf[1] = (byte)flag;
                  transferBuf[2] = (byte)(shortSizeToTransfer >> 8 & 255);
                  transferBuf[3] = (byte)(shortSizeToTransfer & 255);

                  for(int i = 0; i < sizeToTransfer; ++i) {
                     transferBuf[i + 4] = buffer[i];
                  }

                  ftStatus = this.mFtDev.write(transferBuf, sizeToTransfer + 4);
                  sizeTransferred[0] = ftStatus - 4;
                  return sizeToTransfer == sizeTransferred[0]?0:10;
               }
            }
         }
      }
   }

   public int getStatus(int deviceAddress, byte[] controllerStatus) {
      int ftStatus = this.I2C_Check(true);
      if(ftStatus != 0) {
         return ftStatus;
      } else {
         ftStatus = this.mFtDev.VendorCmdGet(34, '\uf5b4', controllerStatus, 1);
         return ftStatus < 0?18:0;
      }
   }

   boolean I2C_Mode_Check() {
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

   int I2C_Version_Check(int flag) {
      if(this.mFtDev != null && this.mFtDev.isOpen()) {
         if(flag != 6) {
            char fwVer = this.getFWVersion();
            if(fwVer < 66) {
               return 1022;
            }
         }

         return 0;
      } else {
         return 3;
      }
   }

   int I2C_Address_Check(int deviceAddress) {
      return (deviceAddress & 'ﰀ') > 0?1007:0;
   }

   private int i2c_master_setup_timer_period(int CLK_CTRL, int kbps) {
      double CLK_PRD;
      switch(CLK_CTRL) {
      case 0:
      default:
         CLK_PRD = 16.666666666666668D;
         break;
      case 1:
         CLK_PRD = 41.666666666666664D;
         break;
      case 2:
         CLK_PRD = 20.833333333333332D;
         break;
      case 3:
         CLK_PRD = 12.5D;
      }

      int I2CMTP;
      boolean SCL_LP;
      boolean SCL_HP;
      double SCL_PERIOD;
      int TIMER_PRD;
      if(60 <= kbps && kbps <= 100) {
         SCL_LP = true;
         SCL_HP = true;
         SCL_PERIOD = 1000000.0D / (double)kbps;
         TIMER_PRD = (int)(SCL_PERIOD / (8.0D * CLK_PRD) - 1.0D + 0.5D);
         if(TIMER_PRD > 127) {
            TIMER_PRD = 127;
         }

         I2CMTP = TIMER_PRD;
      } else if(100 < kbps && kbps <= 400) {
         SCL_LP = true;
         SCL_HP = true;
         SCL_PERIOD = 1000000.0D / (double)kbps;
         TIMER_PRD = (int)(SCL_PERIOD / (6.0D * CLK_PRD) - 1.0D + 0.5D);
         I2CMTP = TIMER_PRD | 192;
      } else if(400 < kbps && kbps <= 1000) {
         SCL_LP = true;
         SCL_HP = true;
         SCL_PERIOD = 1000000.0D / (double)kbps;
         TIMER_PRD = (int)(SCL_PERIOD / (6.0D * CLK_PRD) - 1.0D + 0.5D);
         I2CMTP = TIMER_PRD | 192;
      } else if(1000 < kbps && kbps <= 3400) {
         SCL_LP = true;
         SCL_HP = true;
         SCL_PERIOD = 1000000.0D / (double)kbps;
         TIMER_PRD = (int)(SCL_PERIOD / (6.0D * CLK_PRD) - 1.0D + 0.5D);
         I2CMTP = TIMER_PRD | 128;
         I2CMTP &= -65;
      } else {
         I2CMTP = 74;
      }

      return I2CMTP;
   }

   int getMaxTransferSize(int[] pMaxSize) {
      pMaxSize[0] = 0;
      int maxBuckSize = this.mFt4222Dev.getMaxBuckSize();
      switch(this.mFt4222Dev.mChipStatus.function) {
      case 1:
         pMaxSize[0] = maxBuckSize - 4;
         return 0;
      default:
         return 17;
      }
   }

   char getFWVersion() {
      return this.mFt4222Dev.GetVersion();
   }
}
