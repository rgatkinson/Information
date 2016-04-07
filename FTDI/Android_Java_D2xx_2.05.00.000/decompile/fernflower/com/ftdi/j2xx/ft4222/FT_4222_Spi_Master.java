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

            sizeOfRead[0] = this.setMultiReadWritePackage(this.mFTDevice, sendData, readBuffer);
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

   private int setMultiReadWritePackage(FT_Device ftSPIDevice, byte[] wr_buffer, byte[] rd_buffer) {
      boolean ret = false;
      int sleepCnt = 0;
      byte sleepTime = 10;
      short maxWaitTime = 30000;
      int bytesRead = 0;
      if(ftSPIDevice != null && ftSPIDevice.isOpen()) {
         ftSPIDevice.write(wr_buffer, wr_buffer.length);

         while(bytesRead < rd_buffer.length && sleepCnt < maxWaitTime) {
            int var12 = ftSPIDevice.getQueueStatus();
            if(var12 > 0) {
               sleepCnt = 0;
               byte[] e = new byte[var12];
               var12 = ftSPIDevice.read(e, var12);
               Assert.assertEquals(e.length == var12, true);

               for(int i = 0; i < e.length; ++i) {
                  if(bytesRead + i < rd_buffer.length) {
                     rd_buffer[bytesRead + i] = e[i];
                  }
               }

               bytesRead += var12;
            }

            try {
               Thread.sleep((long)sleepTime);
               sleepCnt += sleepTime;
            } catch (InterruptedException var11) {
               sleepCnt = maxWaitTime;
            }
         }

         if(rd_buffer.length == bytesRead && sleepCnt <= maxWaitTime) {
            return bytesRead;
         } else {
            Log.e("FTDI_Device::", "MultiReadWritePackage timeout!!!!");
            return -1;
         }
      } else {
         return -1;
      }
   }

   private int sendReadWriteBuffer(FT_Device ftDevice, byte[] wr_buffer, byte[] rd_buffer, int sizeToTransfer) {
      byte[] wrPackBuf = new byte[16384];
      byte[] rdPackBuf = new byte[wrPackBuf.length];
      int packCount = sizeToTransfer / wrPackBuf.length;
      int restCount = sizeToTransfer % wrPackBuf.length;
      int readIdx = 0;
      int writeIdx = 0;
      boolean valRet = false;

      int j;
      int var14;
      for(j = 0; j < packCount; ++j) {
         int j1;
         for(j1 = 0; j1 < wrPackBuf.length; ++j1) {
            wrPackBuf[j1] = wr_buffer[writeIdx];
            ++writeIdx;
         }

         var14 = this.setReadWritePackage(ftDevice, wrPackBuf, rdPackBuf);
         if(var14 <= 0) {
            return var14;
         }

         for(j1 = 0; j1 < rdPackBuf.length; ++j1) {
            rd_buffer[readIdx] = rdPackBuf[j1];
            ++readIdx;
         }
      }

      if(restCount > 0) {
         wrPackBuf = new byte[restCount];
         rdPackBuf = new byte[wrPackBuf.length];

         for(j = 0; j < wrPackBuf.length; ++j) {
            wrPackBuf[j] = wr_buffer[writeIdx];
            ++writeIdx;
         }

         var14 = this.setReadWritePackage(ftDevice, wrPackBuf, rdPackBuf);
         if(var14 <= 0) {
            return var14;
         }

         for(j = 0; j < rdPackBuf.length; ++j) {
            rd_buffer[readIdx] = rdPackBuf[j];
            ++readIdx;
         }
      }

      return readIdx;
   }

   private int setReadWritePackage(FT_Device ftSPIDevice, byte[] wr_buffer, byte[] rd_buffer) {
      boolean ret = false;
      int sleepCnt = 0;
      byte sleepTime = 10;
      short maxWaitTime = 30000;
      int bytesRead = 0;
      if(ftSPIDevice != null && ftSPIDevice.isOpen()) {
         Assert.assertEquals(wr_buffer.length == rd_buffer.length, true);
         int var12 = ftSPIDevice.write(wr_buffer, wr_buffer.length);
         if(wr_buffer.length != var12) {
            Log.e("FTDI_Device::", "setReadWritePackage write error!!!");
            return -1;
         } else {
            while(bytesRead < rd_buffer.length && sleepCnt < maxWaitTime) {
               var12 = ftSPIDevice.getQueueStatus();
               if(var12 > 0) {
                  sleepCnt = 0;
                  byte[] e = new byte[var12];
                  var12 = ftSPIDevice.read(e, var12);
                  Assert.assertEquals(e.length == var12, true);

                  for(int i = 0; i < e.length; ++i) {
                     if(bytesRead + i < rd_buffer.length) {
                        rd_buffer[bytesRead + i] = e[i];
                     }
                  }

                  bytesRead += var12;
               }

               try {
                  Thread.sleep((long)sleepTime);
                  sleepCnt += sleepTime;
               } catch (InterruptedException var11) {
                  sleepCnt = maxWaitTime;
               }
            }

            if(rd_buffer.length == bytesRead && sleepCnt <= maxWaitTime) {
               return bytesRead;
            } else {
               Log.e("FTDI_Device::", "SingleReadWritePackage read errpr!!!!");
               return -2;
            }
         }
      } else {
         return -1;
      }
   }
}
