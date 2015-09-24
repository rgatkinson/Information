package com.ftdi.j2xx;

import java.nio.ByteBuffer;
import android.util.Log;
import java.util.concurrent.Semaphore;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbDeviceConnection;

class a implements Runnable
{
    UsbDeviceConnection a;
    UsbEndpoint b;
    o c;
    FT_Device d;
    int e;
    int f;
    int g;
    Semaphore h;
    boolean i;
    
    a(final FT_Device d, final o c, final UsbDeviceConnection a, final UsbEndpoint b) {
        this.d = d;
        this.b = b;
        this.a = a;
        this.c = c;
        this.e = this.c.b().getBufferNumber();
        this.f = this.c.b().getMaxTransferSize();
        this.g = this.d.d().getReadTimeout();
        this.h = new Semaphore(1);
        this.i = false;
    }
    
    void a() throws InterruptedException {
        this.h.acquire();
        this.i = true;
    }
    
    void b() {
        this.i = false;
        this.h.release();
    }
    
    boolean c() {
        return this.i;
    }
    
    @Override
    public void run() {
        int n = 0;
        try {
            do {
                if (this.i) {
                    this.h.acquire();
                    this.h.release();
                }
                final n b = this.c.b(n);
                if (b.b() == 0) {
                    final ByteBuffer a = b.a();
                    a.clear();
                    b.a(n);
                    final int bulkTransfer = this.a.bulkTransfer(this.b, a.array(), this.f, this.g);
                    if (bulkTransfer > 0) {
                        a.position(bulkTransfer);
                        a.flip();
                        b.b(bulkTransfer);
                        this.c.e(n);
                    }
                }
                n = (n + 1) % this.e;
            } while (!Thread.interrupted());
            throw new InterruptedException();
        }
        catch (InterruptedException ex3) {
            try {
                this.c.f();
                this.c.e();
            }
            catch (Exception ex) {
                Log.d("BulkIn::", "Stop BulkIn thread");
                ex.printStackTrace();
            }
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
            Log.e("BulkIn::", "Fatal error in BulkIn thread");
        }
    }
}
