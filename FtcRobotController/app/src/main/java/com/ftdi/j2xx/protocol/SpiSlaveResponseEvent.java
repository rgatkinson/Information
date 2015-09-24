package com.ftdi.j2xx.protocol;

import com.ftdi.j2xx.protocol.SpiSlaveEvent;

public class SpiSlaveResponseEvent extends SpiSlaveEvent {
   public static final int DATA_CORRUPTED = 1;
   public static final int IO_ERROR = 2;
   public static final int OK = 0;
   public static final int RESET = 3;
   public static final int RES_SLAVE_READ = 3;
   private int a;

   public SpiSlaveResponseEvent(int var1, int var2, Object var3, Object var4, Object var5) {
      super(var1, false, var3, var4, var5);
      this.a = var2;
   }

   public int getResponseCode() {
      return this.a;
   }
}
