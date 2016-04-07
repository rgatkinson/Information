package com.ftdi.j2xx.protocol;

import com.ftdi.j2xx.interfaces.SpiSlave;
import com.ftdi.j2xx.protocol.SpiSlaveEvent;
import com.ftdi.j2xx.protocol.SpiSlaveListener;
import com.ftdi.j2xx.protocol.SpiSlaveRequestEvent;
import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;
import com.ftdi.j2xx.protocol.SpiSlaveThread;
import junit.framework.Assert;

public class FT_Spi_Slave extends SpiSlaveThread {
   private static final int FT4222_SPI_SLAVE_SYNC_WORD = 90;
   private static final int SPI_MASTER_TRANSFER = 128;
   private static final int SPI_SLAVE_TRANSFER = 129;
   private static final int SPI_SHORT_MASTER_TRANSFER = 130;
   private static final int SPI_SHART_SLAVE_TRANSFER = 131;
   private static final int SPI_ACK = 132;
   private static final int SPI_QUERY_VER = 136;
   private FT_Spi_Slave.DECODE_STATE mDecodeState;
   private int mSync;
   private int mCmd;
   private int mSn;
   private int mBufferSize;
   private int mCurrentBufferSize;
   private byte[] mBuffer;
   private int mCheckSum;
   private int mWrSn;
   private SpiSlave mSpiSlave;
   private SpiSlaveListener mSpiSlaveListener;
   private boolean mIsOpened;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$com$ftdi$j2xx$protocol$FT_Spi_Slave$DECODE_STATE;

   public FT_Spi_Slave(SpiSlave pSlaveInterface) {
      this.mSpiSlave = pSlaveInterface;
      this.mDecodeState = FT_Spi_Slave.DECODE_STATE.STATE_SYNC;
   }

   public void registerSpiSlaveListener(SpiSlaveListener pListener) {
      this.mSpiSlaveListener = pListener;
   }

   public int open() {
      if(this.mIsOpened) {
         return 1;
      } else {
         this.mIsOpened = true;
         this.mSpiSlave.init();
         this.start();
         return 0;
      }
   }

   public int close() {
      if(!this.mIsOpened) {
         return 3;
      } else {
         SpiSlaveRequestEvent event = new SpiSlaveRequestEvent(-1, true, (Object)null, (Object)null, (Object)null);
         this.sendMessage(event);
         this.mIsOpened = false;
         return 0;
      }
   }

   public int write(byte[] wrBuf) {
      if(!this.mIsOpened) {
         return 3;
      } else if(wrBuf.length > 65536) {
         return 1010;
      } else {
         int[] sizeTransferred = new int[1];
         byte idx = 0;
         int wrSize = wrBuf.length;
         int checksum = this.getCheckSum(wrBuf, 90, 129, this.mWrSn, wrSize);
         byte[] buffer = new byte[8 + wrBuf.length];
         int var8 = idx + 1;
         buffer[idx] = 0;
         buffer[var8++] = 90;
         buffer[var8++] = -127;
         buffer[var8++] = (byte)this.mWrSn;
         buffer[var8++] = (byte)((wrSize & '\uff00') >> 8);
         buffer[var8++] = (byte)(wrSize & 255);

         for(int i = 0; i < wrBuf.length; ++i) {
            buffer[var8++] = wrBuf[i];
         }

         buffer[var8++] = (byte)((checksum & '\uff00') >> 8);
         buffer[var8++] = (byte)(checksum & 255);
         this.mSpiSlave.write(buffer, buffer.length, sizeTransferred);
         if(sizeTransferred[0] != buffer.length) {
            return 4;
         } else {
            ++this.mWrSn;
            if(this.mWrSn >= 256) {
               this.mWrSn = 0;
            }

            return 0;
         }
      }
   }

   private boolean check_valid_spi_cmd(int cmd) {
      return cmd == 128 || cmd == 130 || cmd == 136;
   }

   private int getCheckSum(byte[] sendBuf, int sync, int cmd, int sn, int bufsize) {
      int sum = 0;
      if(sendBuf != null) {
         for(int idx = 0; idx < sendBuf.length; ++idx) {
            sum += sendBuf[idx] & 255;
         }
      }

      sum += sync;
      sum += cmd;
      sum += sn;
      sum += (bufsize & '\uff00') >> 8;
      sum += bufsize & 255;
      return sum;
   }

   private void spi_push_req_ack_queue() {
      byte idx = 0;
      byte[] buffer = new byte[8];
      int var5 = idx + 1;
      buffer[idx] = 0;
      buffer[var5++] = 90;
      buffer[var5++] = -124;
      buffer[var5++] = (byte)this.mSn;
      buffer[var5++] = 0;
      buffer[var5++] = 0;
      int checksum = this.getCheckSum((byte[])null, 90, 132, this.mSn, 0);
      buffer[var5++] = (byte)((checksum & '\uff00') >> 8);
      buffer[var5++] = (byte)(checksum & 255);
      int[] sizeTransferred = new int[1];
      this.mSpiSlave.write(buffer, buffer.length, sizeTransferred);
   }

   private void sp_slave_parse_and_push_queue(byte[] rdBuf) {
      boolean reset = false;
      boolean dataCorrupted = false;

      for(int i = 0; i < rdBuf.length; ++i) {
         int val = rdBuf[i] & 255;
         switch($SWITCH_TABLE$com$ftdi$j2xx$protocol$FT_Spi_Slave$DECODE_STATE()[this.mDecodeState.ordinal()]) {
         case 1:
            if(val != 90) {
               reset = true;
            } else {
               this.mDecodeState = FT_Spi_Slave.DECODE_STATE.STATE_CMD;
               this.mSync = val;
            }
            break;
         case 2:
            if(!this.check_valid_spi_cmd(val)) {
               reset = true;
               dataCorrupted = true;
            } else {
               this.mCmd = val;
            }

            this.mDecodeState = FT_Spi_Slave.DECODE_STATE.STATE_SN;
            break;
         case 3:
            this.mSn = val;
            this.mDecodeState = FT_Spi_Slave.DECODE_STATE.STATE_SIZE_HIGH;
            break;
         case 4:
            this.mBufferSize = val * 256;
            this.mDecodeState = FT_Spi_Slave.DECODE_STATE.STATE_SIZE_LOW;
            break;
         case 5:
            this.mBufferSize += val;
            this.mCurrentBufferSize = 0;
            this.mBuffer = new byte[this.mBufferSize];
            this.mDecodeState = FT_Spi_Slave.DECODE_STATE.STATE_COLLECT_DATA;
            break;
         case 6:
            this.mBuffer[this.mCurrentBufferSize] = rdBuf[i];
            ++this.mCurrentBufferSize;
            if(this.mCurrentBufferSize == this.mBufferSize) {
               this.mDecodeState = FT_Spi_Slave.DECODE_STATE.STATE_CHECKSUM_HIGH;
            }
            break;
         case 7:
            this.mCheckSum = val * 256;
            this.mDecodeState = FT_Spi_Slave.DECODE_STATE.STATE_CHECKSUM_LOW;
            break;
         case 8:
            this.mCheckSum += val;
            int pEvent = this.getCheckSum(this.mBuffer, this.mSync, this.mCmd, this.mSn, this.mBufferSize);
            if(this.mCheckSum == pEvent) {
               if(this.mCmd == 128) {
                  this.spi_push_req_ack_queue();
                  if(this.mSpiSlaveListener != null) {
                     SpiSlaveResponseEvent pEvent1 = new SpiSlaveResponseEvent(3, 0, this.mBuffer, (Object)null, (Object)null);
                     this.mSpiSlaveListener.OnDataReceived(pEvent1);
                  }
               }
            } else {
               dataCorrupted = true;
            }

            reset = true;
         }

         if(dataCorrupted && this.mSpiSlaveListener != null) {
            SpiSlaveResponseEvent var8 = new SpiSlaveResponseEvent(3, 1, (Object)null, (Object)null, (Object)null);
            this.mSpiSlaveListener.OnDataReceived(var8);
         }

         if(reset) {
            this.mDecodeState = FT_Spi_Slave.DECODE_STATE.STATE_SYNC;
            this.mSync = 0;
            this.mCmd = 0;
            this.mSn = 0;
            this.mBufferSize = 0;
            this.mCurrentBufferSize = 0;
            this.mCheckSum = 0;
            this.mBuffer = null;
            reset = false;
            dataCorrupted = false;
         }
      }

   }

   protected boolean pollData() {
      boolean status = false;
      int[] rxSize = new int[1];
      int status1 = this.mSpiSlave.getRxStatus(rxSize);
      if(rxSize[0] > 0 && status1 == 0) {
         byte[] pEvent = new byte[rxSize[0]];
         status1 = this.mSpiSlave.read(pEvent, pEvent.length, rxSize);
         if(status1 == 0) {
            this.sp_slave_parse_and_push_queue(pEvent);
         }
      }

      if(status1 == 4 && this.mSpiSlaveListener != null) {
         SpiSlaveResponseEvent pEvent1 = new SpiSlaveResponseEvent(3, 2, this.mBuffer, (Object)null, (Object)null);
         this.mSpiSlaveListener.OnDataReceived(pEvent1);
      }

      try {
         Thread.sleep(10L);
      } catch (InterruptedException var4) {
         ;
      }

      return true;
   }

   protected void requestEvent(SpiSlaveEvent pEvent) {
      if(pEvent instanceof SpiSlaveRequestEvent) {
         Object responseEvent = null;
         switch(pEvent.getEventType()) {
         case -1:
         }
      } else {
         Assert.assertTrue("processEvent wrong type" + pEvent.getEventType(), false);
      }

   }

   protected boolean isTerminateEvent(SpiSlaveEvent pEvent) {
      if(!Thread.interrupted()) {
         return true;
      } else {
         if(pEvent instanceof SpiSlaveRequestEvent) {
            switch(pEvent.getEventType()) {
            case -1:
               return true;
            }
         } else {
            Assert.assertTrue("processEvent wrong type" + pEvent.getEventType(), false);
         }

         return false;
      }
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$com$ftdi$j2xx$protocol$FT_Spi_Slave$DECODE_STATE() {
      int[] var10000 = $SWITCH_TABLE$com$ftdi$j2xx$protocol$FT_Spi_Slave$DECODE_STATE;
      if($SWITCH_TABLE$com$ftdi$j2xx$protocol$FT_Spi_Slave$DECODE_STATE != null) {
         return var10000;
      } else {
         int[] var0 = new int[FT_Spi_Slave.DECODE_STATE.values().length];

         try {
            var0[FT_Spi_Slave.DECODE_STATE.STATE_CHECKSUM_HIGH.ordinal()] = 7;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            var0[FT_Spi_Slave.DECODE_STATE.STATE_CHECKSUM_LOW.ordinal()] = 8;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            var0[FT_Spi_Slave.DECODE_STATE.STATE_CMD.ordinal()] = 2;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            var0[FT_Spi_Slave.DECODE_STATE.STATE_COLLECT_DATA.ordinal()] = 6;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            var0[FT_Spi_Slave.DECODE_STATE.STATE_SIZE_HIGH.ordinal()] = 4;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            var0[FT_Spi_Slave.DECODE_STATE.STATE_SIZE_LOW.ordinal()] = 5;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            var0[FT_Spi_Slave.DECODE_STATE.STATE_SN.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            var0[FT_Spi_Slave.DECODE_STATE.STATE_SYNC.ordinal()] = 1;
         } catch (NoSuchFieldError var1) {
            ;
         }

         $SWITCH_TABLE$com$ftdi$j2xx$protocol$FT_Spi_Slave$DECODE_STATE = var0;
         return var0;
      }
   }

   private static enum DECODE_STATE {
      STATE_SYNC,
      STATE_CMD,
      STATE_SN,
      STATE_SIZE_HIGH,
      STATE_SIZE_LOW,
      STATE_COLLECT_DATA,
      STATE_CHECKSUM_HIGH,
      STATE_CHECKSUM_LOW;
   }
}
