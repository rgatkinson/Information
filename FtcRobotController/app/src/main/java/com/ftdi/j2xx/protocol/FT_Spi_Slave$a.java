package com.ftdi.j2xx.protocol;

private enum a
{
    a("STATE_SYNC", 0), 
    b("STATE_CMD", 1), 
    c("STATE_SN", 2), 
    d("STATE_SIZE_HIGH", 3), 
    e("STATE_SIZE_LOW", 4), 
    f("STATE_COLLECT_DATA", 5), 
    g("STATE_CHECKSUM_HIGH", 6), 
    h("STATE_CHECKSUM_LOW", 7);
    
    static {
        i = new a[] { a.a, a.b, a.c, a.d, a.e, a.f, a.g, a.h };
    }
    
    private a(final String s, final int n) {
    }
}
