package com.ftdi.j2xx.protocol;

import com.ftdi.j2xx.protocol.SpiSlaveEvent;

public class SpiSlaveResponseEvent extends SpiSlaveEvent {
   public static final int RES_SLAVE_READ = 3;
   public static final int OK = 0;
   public static final int DATA_CORRUPTED = 1;
   public static final int IO_ERROR = 2;
   public static final int RESET = 3;
   private int mResponseCode;

   public SpiSlaveResponseEvent(int iEventType, int responseCode, Object pArg0, Object pArg1, Object pArg2) {
      super(iEventType, false, pArg0, pArg1, pArg2);
      this.mResponseCode = responseCode;
   }

   public int getResponseCode() {
      return this.mResponseCode;
   }
}
