package com.ftdi.j2xx.protocol;

import com.ftdi.j2xx.protocol.SpiSlaveResponseEvent;

public interface SpiSlaveListener {
   boolean OnDataReceived(SpiSlaveResponseEvent var1);
}
