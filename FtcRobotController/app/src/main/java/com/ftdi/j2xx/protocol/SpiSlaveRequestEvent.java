package com.ftdi.j2xx.protocol;

public class SpiSlaveRequestEvent extends SpiSlaveEvent
{
    protected static final int REQ_DESTORY_THREAD = -1;
    protected static final int REQ_INIT_SLAVE = 1;
    protected static final int REQ_SLAVE_READ = 3;
    protected static final int REQ_SLAVE_WRITE = 2;
    
    public SpiSlaveRequestEvent(final int n, final boolean b, final Object o, final Object o2, final Object o3) {
        super(n, b, o, o2, o3);
    }
}
