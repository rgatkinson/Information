//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ftdi.j2xx;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;

class ZZZOldBulkInRunnable implements Runnable {
    UsbDeviceConnection usbDeviceConnection;
    UsbEndpoint usbEndpoint;
    o oDevice;
    FT_Device ftDevice;
    int bufferCount;
    int maxTransferSize;
    int readTimeout;
    Semaphore semaphore;
    boolean semaphoreAcquired;

    ZZZOldBulkInRunnable(FT_Device var1, o var2, UsbDeviceConnection var3, UsbEndpoint var4) {
        this.ftDevice = var1;
        this.usbEndpoint = var4;
        this.usbDeviceConnection = var3;
        this.oDevice = var2;
        this.bufferCount = this.oDevice.getDriverParameters().getBufferNumber();
        this.maxTransferSize = this.oDevice.getDriverParameters().getMaxTransferSize();
        this.readTimeout = this.ftDevice.d().getReadTimeout();
        this.semaphore = new Semaphore(1);
        this.semaphoreAcquired = false;
    }

    void acquireSemaphore() throws InterruptedException {
        this.semaphore.acquire();
        this.semaphoreAcquired = true;
    }

    void releaseSemaphore() {
        this.semaphoreAcquired = false;
        this.semaphore.release();
    }

    boolean isSemaphoreAcquired() {
        return this.semaphoreAcquired;
    }

    public void run() {
        ByteBuffer var1 = null;
        n var2 = null;
        int var3 = 0;
        boolean var4 = false;
        Object var5 = null;

        try {
            do {
                if (this.semaphoreAcquired) {
                    this.semaphore.acquire();
                    this.semaphore.release();
                }

                var2 = this.oDevice.b(var3);
                if(var2.b() == 0) {
                    var1 = var2.a();
                    var1.clear();
                    var2.a(var3);
                    byte[] var12 = var1.array();
                    int var11 = this.usbDeviceConnection.bulkTransfer(this.usbEndpoint, var12, this.maxTransferSize, this.readTimeout);
                    if(var11 > 0) {
                        var1.position(var11);
                        var1.flip();
                        var2.b(var11);
                        this.oDevice.e(var3);
                    }
                }

                ++var3;
                var3 %= this.bufferCount;
            } while(!Thread.interrupted());

            throw new InterruptedException();
        } catch (InterruptedException var9) {
            try {
                this.oDevice.f();
                this.oDevice.e();
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
