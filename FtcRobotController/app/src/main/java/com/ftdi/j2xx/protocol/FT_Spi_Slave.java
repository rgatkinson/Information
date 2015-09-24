package com.ftdi.j2xx.protocol;

import junit.framework.Assert;
import com.ftdi.j2xx.interfaces.SpiSlave;

public class FT_Spi_Slave extends SpiSlaveThread
{
    private static /* synthetic */ int[] m;
    private a a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;
    private byte[] g;
    private int h;
    private int i;
    private SpiSlave j;
    private SpiSlaveListener k;
    private boolean l;
    
    public FT_Spi_Slave(final SpiSlave j) {
        this.j = j;
        this.a = FT_Spi_Slave.a.a;
    }
    
    private int a(final byte[] array, final int n, final int n2, final int n3, final int n4) {
        int i = 0;
        int n5;
        if (array != null) {
            n5 = 0;
            while (i < array.length) {
                n5 += (0xFF & array[i]);
                ++i;
            }
        }
        else {
            n5 = 0;
        }
        return n3 + (n2 + (n5 + n)) + ((0xFF00 & n4) >> 8) + (n4 & 0xFF);
    }
    
    private void a(final byte[] array) {
        int i = 0;
        int n = 0;
        int n2 = 0;
        while (i < array.length) {
            final int d = 0xFF & array[i];
            int n3 = 0;
            Label_0084: {
                switch (a()[this.a.ordinal()]) {
                    case 1: {
                        if (d != 90) {
                            n3 = 1;
                            break Label_0084;
                        }
                        this.a = FT_Spi_Slave.a.b;
                        this.b = d;
                        n3 = n2;
                        break Label_0084;
                    }
                    case 2: {
                        if (!this.a(d)) {
                            n = 1;
                            n2 = 1;
                        }
                        else {
                            this.c = d;
                        }
                        this.a = FT_Spi_Slave.a.c;
                        n3 = n2;
                        break Label_0084;
                    }
                    case 3: {
                        this.d = d;
                        this.a = FT_Spi_Slave.a.d;
                        n3 = n2;
                        break Label_0084;
                    }
                    case 4: {
                        this.e = d * 256;
                        this.a = FT_Spi_Slave.a.e;
                        n3 = n2;
                        break Label_0084;
                    }
                    case 5: {
                        this.e += d;
                        this.f = 0;
                        this.g = new byte[this.e];
                        this.a = FT_Spi_Slave.a.f;
                        n3 = n2;
                        break Label_0084;
                    }
                    case 6: {
                        this.g[this.f] = array[i];
                        ++this.f;
                        if (this.f == this.e) {
                            this.a = FT_Spi_Slave.a.g;
                            n3 = n2;
                            break Label_0084;
                        }
                        break;
                    }
                    case 7: {
                        this.h = d * 256;
                        this.a = FT_Spi_Slave.a.h;
                        n3 = n2;
                        break Label_0084;
                    }
                    case 8: {
                        this.h += d;
                        if (this.h == this.a(this.g, this.b, this.c, this.d, this.e)) {
                            if (this.c == 128) {
                                this.b();
                                if (this.k != null) {
                                    this.k.OnDataReceived(new SpiSlaveResponseEvent(3, 0, this.g, null, null));
                                }
                            }
                        }
                        else {
                            n = 1;
                        }
                        n3 = 1;
                        break Label_0084;
                    }
                }
                n3 = n2;
            }
            if (n != 0 && this.k != null) {
                this.k.OnDataReceived(new SpiSlaveResponseEvent(3, 1, null, null, null));
            }
            int n4;
            int n5;
            if (n3 != 0) {
                this.a = FT_Spi_Slave.a.a;
                this.b = 0;
                this.c = 0;
                this.d = 0;
                this.e = 0;
                this.f = 0;
                this.h = 0;
                this.g = null;
                n4 = 0;
                n5 = 0;
            }
            else {
                n4 = n;
                n5 = n3;
            }
            ++i;
            n = n4;
            n2 = n5;
        }
    }
    
    private boolean a(final int n) {
        return n == 128 || n == 130 || n == 136;
    }
    
    static /* synthetic */ int[] a() {
        final int[] m = FT_Spi_Slave.m;
        if (m != null) {
            return m;
        }
        final int[] i = new int[a.values().length];
        while (true) {
            try {
                i[a.g.ordinal()] = 7;
                try {
                    i[a.h.ordinal()] = 8;
                    try {
                        i[a.b.ordinal()] = 2;
                        try {
                            i[a.f.ordinal()] = 6;
                            try {
                                i[a.d.ordinal()] = 4;
                                try {
                                    i[a.e.ordinal()] = 5;
                                    try {
                                        i[a.c.ordinal()] = 3;
                                        try {
                                            i[a.a.ordinal()] = 1;
                                            return FT_Spi_Slave.m = i;
                                        }
                                        catch (NoSuchFieldError noSuchFieldError) {}
                                    }
                                    catch (NoSuchFieldError noSuchFieldError2) {}
                                }
                                catch (NoSuchFieldError noSuchFieldError3) {}
                            }
                            catch (NoSuchFieldError noSuchFieldError4) {}
                        }
                        catch (NoSuchFieldError noSuchFieldError5) {}
                    }
                    catch (NoSuchFieldError noSuchFieldError6) {}
                }
                catch (NoSuchFieldError noSuchFieldError7) {}
            }
            catch (NoSuchFieldError noSuchFieldError8) {
                continue;
            }
            break;
        }
    }
    
    private void b() {
        final byte[] array = { 0, 90, -124, (byte)this.d, 0, 0, 0, 0 };
        final int a = this.a(null, 90, 132, this.d, 0);
        array[6] = (byte)((0xFF00 & a) >> 8);
        array[7] = (byte)(a & 0xFF);
        this.j.write(array, array.length, new int[1]);
    }
    
    public int close() {
        if (!this.l) {
            return 3;
        }
        this.sendMessage(new SpiSlaveRequestEvent(-1, true, null, null, null));
        this.l = false;
        return 0;
    }
    
    @Override
    protected boolean isTerminateEvent(final SpiSlaveEvent spiSlaveEvent) {
        if (Thread.interrupted()) {
            if (spiSlaveEvent instanceof SpiSlaveRequestEvent) {
                switch (spiSlaveEvent.getEventType()) {
                    case -1: {
                        return true;
                    }
                }
            }
            else {
                Assert.assertTrue("processEvent wrong type" + spiSlaveEvent.getEventType(), false);
            }
            return false;
        }
        return true;
    }
    
    public int open() {
        if (this.l) {
            return 1;
        }
        this.l = true;
        this.j.init();
        this.start();
        return 0;
    }
    
    @Override
    protected boolean pollData() {
        final int[] array = { 0 };
        int n = this.j.getRxStatus(array);
        if (array[0] > 0 && n == 0) {
            final byte[] array2 = new byte[array[0]];
            n = this.j.read(array2, array2.length, array);
            if (n == 0) {
                this.a(array2);
            }
        }
        if (n == 4 && this.k != null) {
            this.k.OnDataReceived(new SpiSlaveResponseEvent(3, 2, this.g, null, null));
        }
        try {
            Thread.sleep(10L);
            return true;
        }
        catch (InterruptedException ex) {
            return true;
        }
    }
    
    public void registerSpiSlaveListener(final SpiSlaveListener k) {
        this.k = k;
    }
    
    @Override
    protected void requestEvent(final SpiSlaveEvent spiSlaveEvent) {
        if (!(spiSlaveEvent instanceof SpiSlaveRequestEvent)) {
            Assert.assertTrue("processEvent wrong type" + spiSlaveEvent.getEventType(), false);
            return;
        }
        switch (spiSlaveEvent.getEventType()) {
            default: {}
        }
    }
    
    public int write(final byte[] array) {
        int n;
        if (!this.l) {
            n = 3;
        }
        else {
            if (array.length > 65536) {
                return 1010;
            }
            final int[] array2 = { 0 };
            final int length = array.length;
            final int a = this.a(array, 90, 129, this.i, length);
            final byte[] array3 = new byte[8 + array.length];
            array3[0] = 0;
            array3[1] = 90;
            array3[2] = -127;
            array3[3] = (byte)this.i;
            array3[4] = (byte)((0xFF00 & length) >> 8);
            array3[5] = (byte)(length & 0xFF);
            int n2 = 6;
            int n3;
            for (int i = 0; i < array.length; ++i, n2 = n3) {
                n3 = n2 + 1;
                array3[n2] = array[i];
            }
            final int n4 = n2 + 1;
            array3[n2] = (byte)((0xFF00 & a) >> 8);
            array3[n4] = (byte)(a & 0xFF);
            this.j.write(array3, array3.length, array2);
            if (array2[0] != array3.length) {
                return 4;
            }
            ++this.i;
            final int j = this.i;
            n = 0;
            if (j >= 256) {
                return this.i = 0;
            }
        }
        return n;
    }
    
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
}
