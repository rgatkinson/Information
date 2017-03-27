package com.ftdi.j2xx.ft4222;

import android.util.Log;
import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.ft4222.FT_4222_Device;
import com.ftdi.j2xx.ft4222.SPI_MasterCfg;
import com.ftdi.j2xx.ft4222.chiptop_mgr;
import com.ftdi.j2xx.interfaces.SpiMaster;
import junit.framework.Assert;

public class FT_4222_Spi_Master implements SpiMaster {
   private FT_4222_Device mFT4222Device;
   private FT_Device mFTDevice;
   private static final String TAG = "FTDI_Device::";
   private byte[] mPackWrBuf = new byte[16384];
   private byte[] mPackRdBuf = new byte[16384];

   public FT_4222_Spi_Master(FT_4222_Device pDevice) {
      this.mFT4222Device = pDevice;
      this.mFTDevice = pDevice.mFtDev;
   }

   public int init(int ioLine, int clock, int cpol, int cpha, byte ssoMap) {
      chiptop_mgr chipStatus = this.mFT4222Device.mChipStatus;
      byte venderSSOMap = 0;
      SPI_MasterCfg config = this.mFT4222Device.mSpiMasterCfg;
      config.ioLine = ioLine;
      config.clock = clock;
      config.cpol = cpol;
      config.cpha = cpha;
      config.ssoMap = ssoMap;
      if(config.ioLine != 1 && config.ioLine != 2 && config.ioLine != 4) {
         return 6;
      } else {
         this.mFT4222Device.cleanRxData();
         switch(chipStatus.chip_mode) {
         case 0:
            venderSSOMap = 1;
            break;
         case 1:
            venderSSOMap = 7;
            break;
         case 2:
            venderSSOMap = 15;
            break;
         case 3:
            venderSSOMap = 1;
         }

         if((venderSSOMap & config.ssoMap) == 0) {
            return 6;
         } else {
            config.ssoMap &= venderSSOMap;
            byte venderSS = 0;
            byte verderFun = 3;
            if(this.mFTDevice.VendorCmdSet(33, 66 | config.ioLine << 8) < 0) {
               return 4;
            } else if(this.mFTDevice.VendorCmdSet(33, 68 | config.clock << 8) < 0) {
               return 4;
            } else if(this.mFTDevice.VendorCmdSet(33, 69 | config.cpol << 8) < 0) {
               return 4;
            } else if(this.mFTDevice.VendorCmdSet(33, 70 | config.cpha << 8) < 0) {
               return 4;
            } else if(this.mFTDevice.VendorCmdSet(33, 67 | venderSS << 8) < 0) {
               return 4;
            } else if(this.mFTDevice.VendorCmdSet(33, 72 | config.ssoMap << 8) < 0) {
               return 4;
            } else if(this.mFTDevice.VendorCmdSet(33, 5 | verderFun << 8) < 0) {
               return 4;
            } else {
               chipStatus.function = 3;
               return 0;
            }
         }
      }
   }

   public int setLines(int spiMode) {
      byte verderRestart = 1;
      chiptop_mgr chipStatus = this.mFT4222Device.mChipStatus;
      if(chipStatus.function != 3) {
         return 1003;
      } else if(spiMode == 0) {
         return 17;
      } else if(this.mFTDevice.VendorCmdSet(33, 66 | spiMode << 8) < 0) {
         return 4;
      } else if(this.mFTDevice.VendorCmdSet(33, 74 | verderRestart << 8) < 0) {
         return 4;
      } else {
         SPI_MasterCfg spiCfg = this.mFT4222Device.mSpiMasterCfg;
         spiCfg.ioLine = spiMode;
         return 0;
      }
   }

   public int singleWrite(byte[] writeBuffer, int sizeToTransfer, int[] sizeTransferred, boolean isEndTransaction) {
      byte[] readBuffer = new byte[writeBuffer.length];
      return this.singleReadWrite(readBuffer, writeBuffer, sizeToTransfer, sizeTransferred, isEndTransaction);
   }

   public int singleRead(byte[] readBuffer, int sizeToTransfer, int[] sizeOfRead, boolean isEndTransaction) {
      byte[] writeBuffer = new byte[readBuffer.length];
      return this.singleReadWrite(readBuffer, writeBuffer, sizeToTransfer, sizeOfRead, isEndTransaction);
   }

   public int singleReadWrite(byte[] readBuffer, byte[] writeBuffer, int sizeToTransfer, int[] sizeTransferred, boolean isEndTransaction) {
      chiptop_mgr chipStatus = this.mFT4222Device.mChipStatus;
      SPI_MasterCfg spiCfg = this.mFT4222Device.mSpiMasterCfg;
      if(writeBuffer != null && readBuffer != null && sizeTransferred != null) {
         sizeTransferred[0] = 0;
         if(chipStatus.function == 3 && spiCfg.ioLine == 1) {
            if(sizeToTransfer == 0) {
               return 6;
            } else {
               if(sizeToTransfer > writeBuffer.length || sizeToTransfer > readBuffer.length) {
                  Assert.assertTrue("sizeToTransfer > writeBuffer.length || sizeToTransfer > readBuffer.length", false);
               }

               if(writeBuffer.length != readBuffer.length || writeBuffer.length == 0) {
                  Assert.assertTrue("writeBuffer.length != readBuffer.length || writeBuffer.length == 0", false);
               }

               sizeTransferred[0] = this.sendReadWriteBuffer(this.mFTDevice, writeBuffer, readBuffer, sizeToTransfer);
               if(isEndTransaction) {
                  this.mFTDevice.write((byte[])null, 0);
               }

               return sizeTransferred[0] == -1?10:(sizeTransferred[0] == -2?1011:0);
            }
         } else {
            return 1005;
         }
      } else {
         return 1009;
      }
   }

   public int multiReadWrite(byte[] readBuffer, byte[] writeBuffer, int singleWriteBytes, int multiWriteBytes, int multiReadBytes, int[] sizeOfRead) {
      chiptop_mgr chipStatus = this.mFT4222Device.mChipStatus;
      SPI_MasterCfg spiCfg = this.mFT4222Device.mSpiMasterCfg;
      if(multiReadBytes > 0 && readBuffer == null) {
         return 1009;
      } else if(singleWriteBytes + multiWriteBytes > 0 && writeBuffer == null) {
         return 1009;
      } else if(multiReadBytes > 0 && sizeOfRead == null) {
         return 1009;
      } else if(chipStatus.function == 3 && spiCfg.ioLine != 1) {
         if(singleWriteBytes > 15) {
            Log.e("FTDI_Device::", "The maxium single write bytes are 15 bytes");
            return 6;
         } else {
            int sendDataSize = 5 + singleWriteBytes + multiWriteBytes;
            byte[] sendData = new byte[sendDataSize];
            sendData[0] = (byte)(128 | singleWriteBytes & 15);
            sendData[1] = (byte)((multiWriteBytes & '\uff00') >> 8);
            sendData[2] = (byte)(multiWriteBytes & 255);
            sendData[3] = (byte)((multiReadBytes & '\uff00') >> 8);
            sendData[4] = (byte)(multiReadBytes & 255);

            for(int i = 0; i < singleWriteBytes + multiWriteBytes; ++i) {
               sendData[i + 5] = writeBuffer[i];
            }

            sizeOfRead[0] = this.setMultiReadWritePackage(this.mFTDevice, sendData, readBuffer, multiReadBytes);
            return 0;
         }
      } else {
         return 1006;
      }
   }

   public int reset() {
      byte verderReset = 0;
      return this.mFTDevice.VendorCmdSet(33, 74 | verderReset << 8) < 0?4:0;
   }

   public int setDrivingStrength(int clkStrength, int ioStrength, int ssoStregth) {
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

         return this.mFTDevice.VendorCmdSet(33, 160 | actual_strength << 8) < 0?4:(this.mFTDevice.VendorCmdSet(33, 5 | verderFun << 8) < 0?4:0);
      }
   }

   private int setMultiReadWritePackage(FT_Device ftSPIDevice, byte[] wr_buffer, byte[] rd_buffer, int multiReadBytes) {
      boolean ret = false;
      boolean sleepCnt = false;
      boolean maxWaitTime = true;
      boolean bytesRead = false;
      if(ftSPIDevice != null && ftSPIDevice.isOpen()) {
         this.sendMultiWriteBuffer(ftSPIDevice, wr_buffer);
         int bytesRead1 = this.sendMultiReadBuffer(ftSPIDevice, rd_buffer, multiReadBytes);
         return bytesRead1;
      } else {
         return -1;
      }
   }

   private int sendMultiWriteBuffer(FT_Device ftSPIDevice, byte[] wr_buffer) {
      int packCount = wr_buffer.length / this.mPackWrBuf.length;
      int restCount = wr_buffer.length % this.mPackWrBuf.length;
      int writeIdx = 0;
      boolean valRet = false;

      int j;
      int var9;
      for(j = 0; j < packCount; ++j) {
         for(int j1 = 0; j1 < this.mPackWrBuf.length; ++j1) {
            this.mPackWrBuf[j1] = wr_buffer[writeIdx];
            ++writeIdx;
         }

         var9 = ftSPIDevice.write(this.mPackWrBuf, this.mPackWrBuf.length);
         if(this.mPackWrBuf.length != var9) {
            Log.e("FTDI_Device::", "sendMultiWriteBuffer write error!!!");
            return -1;
         }

         if(var9 <= 0) {
            return var9;
         }
      }

      if(restCount > 0) {
         for(j = 0; j < restCount; ++j) {
            this.mPackWrBuf[j] = wr_buffer[writeIdx];
            ++writeIdx;
         }

         var9 = ftSPIDevice.write(this.mPackWrBuf, restCount);
         if(restCount != var9) {
            Log.e("FTDI_Device::", "sendMultiWriteBuffer write error!!!");
            return -1;
         }

         if(var9 <= 0) {
            return var9;
         }
      }

      return writeIdx;
   }

   private int sendMultiReadBuffer(FT_Device ftSPIDevice, byte[] rd_buffer, int multiReadBytes) {
      boolean ret = false;
      boolean bytesRead = false;
      int bytesRead1 = ftSPIDevice.read(rd_buffer, multiReadBytes);
      return bytesRead1;
   }

   private int sendReadWriteBuffer(FT_Device ftDevice, byte[] wr_buffer, byte[] rd_buffer, int sizeToTransfer) {
      int packCount = sizeToTransfer / this.mPackWrBuf.length;
      int restCount = sizeToTransfer % this.mPackWrBuf.length;
      int readIdx = 0;
      int writeIdx = 0;
      boolean valRet = false;

      int j;
      int var12;
      for(j = 0; j < packCount; ++j) {
         int j1;
         for(j1 = 0; j1 < this.mPackWrBuf.length; ++j1) {
            this.mPackWrBuf[j1] = wr_buffer[writeIdx];
            ++writeIdx;
         }

         var12 = this.setReadWritePackage(ftDevice, this.mPackWrBuf, this.mPackRdBuf, this.mPackWrBuf.length);
         if(var12 <= 0) {
            return var12;
         }

         for(j1 = 0; j1 < this.mPackRdBuf.length; ++j1) {
            rd_buffer[readIdx] = this.mPackRdBuf[j1];
            ++readIdx;
         }
      }

      if(restCount > 0) {
         for(j = 0; j < restCount; ++j) {
            this.mPackWrBuf[j] = wr_buffer[writeIdx];
            ++writeIdx;
         }

         var12 = this.setReadWritePackage(ftDevice, this.mPackWrBuf, this.mPackRdBuf, restCount);
         if(var12 <= 0) {
            return var12;
         }

         for(j = 0; j < restCount; ++j) {
            rd_buffer[readIdx] = this.mPackRdBuf[j];
            ++readIdx;
         }
      }

      return readIdx;
   }

   private int setReadWritePackage(FT_Device ftSPIDevice, byte[] wr_buffer, byte[] rd_buffer, int wr_rd_size) {
      boolean ret = false;
      byte sleepCnt = 0;
      short maxWaitTime = 30000;
      int bytesRead = 0;
      if(ftSPIDevice != null && ftSPIDevice.isOpen()) {
         int var10 = ftSPIDevice.write(wr_buffer, wr_rd_size);
         if(wr_rd_size != var10) {
            Log.e("FTDI_Device::", "setReadWritePackage write error!!!");
            return -1;
         } else {
            for(; bytesRead < wr_rd_size && sleepCnt < maxWaitTime; Thread.yield()) {
               var10 = ftSPIDevice.getQueueStatus();
               if(var10 > 0) {
                  sleepCnt = 0;
                  var10 = ftSPIDevice.read(wr_buffer, var10);

                  for(int i = 0; i < var10; ++i) {
                     if(bytesRead + i < wr_rd_size) {
                        rd_buffer[bytesRead + i] = wr_buffer[i];
                     }
                  }

                  bytesRead += var10;
               }
            }

            if(wr_rd_size == bytesRead && sleepCnt <= maxWaitTime) {
               return bytesRead;
            } else {
               Log.e("FTDI_Device::", "SingleReadWritePackage timeout!!!!");
               return -1;
            }
         }
      } else {
         return -1;
      }
   }
}
