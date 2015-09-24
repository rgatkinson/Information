package com.ftdi.j2xx;

import java.io.IOException;

public static class D2xxException extends IOException
{
    public D2xxException() {
    }
    
    public D2xxException(final String s) {
        super(s);
    }
}
