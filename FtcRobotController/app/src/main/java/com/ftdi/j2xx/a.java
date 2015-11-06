//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ftdi.j2xx;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.util.Log;
import com.ftdi.j2xx.FT_Device;
import com.ftdi.j2xx.n;
import com.ftdi.j2xx.o;
import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;

class a implements Runnable {
    UsbDeviceConnection a;
    UsbEndpoint b;
    o c;
    FT_Device d;
    int e;
    int f;
    int g;
    Semaphore h;
    boolean i;

    a(FT_Device var1, o var2, UsbDeviceConnection var3, UsbEndpoint var4) {
        this.d = var1;
        this.b = var4;
        this.a = var3;
        this.c = var2;
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

    public void run() {
        ByteBuffer var1 = null;
        n var2 = null;
        int var3 = 0;
        boolean var4 = false;
        Object var5 = null;

        try {
            do {
                if(this.i) {
                    this.h.acquire();
                    this.h.release();
                }

                var2 = this.c.b(var3);
                if(var2.b() == 0) {
                    var1 = var2.a();
                    var1.clear();
                    var2.a(var3);
                    byte[] var12 = var1.array();
                    int var11 = this.a.bulkTransfer(this.b, var12, this.f, this.g);
                    if(var11 > 0) {
                        var1.position(var11);
                        var1.flip();
                        var2.b(var11);
                        this.c.e(var3);
                    }
                }

                ++var3;
                var3 %= this.e;
            } while(!Thread.interrupted());

            throw new InterruptedException();
        } catch (InterruptedException var9) {
            try {
                this.c.f();
                this.c.e();
            } catch (Exception var8) {
                Log.d("BulkIn::", "Stop BulkIn thread");
                var8.printStackTrace();
            }
        } catch (Exception var10) {
            var10.printStackTrace();
            Log.e("BulkIn::", "Fatal error in BulkIn thread");
        }

    }
}
