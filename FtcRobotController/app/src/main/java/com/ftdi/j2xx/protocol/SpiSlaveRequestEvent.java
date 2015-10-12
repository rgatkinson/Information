package com.ftdi.j2xx.protocol;

import com.ftdi.j2xx.protocol.SpiSlaveEvent;

public class SpiSlaveRequestEvent extends SpiSlaveEvent {
   protected static final int REQ_DESTORY_THREAD = -1;
   protected static final int REQ_INIT_SLAVE = 1;
   protected static final int REQ_SLAVE_READ = 3;
   protected static final int REQ_SLAVE_WRITE = 2;

   public SpiSlaveRequestEvent(int var1, boolean var2, Object var3, Object var4, Object var5) {
      super(var1, var2, var3, var4, var5);
   }
}
