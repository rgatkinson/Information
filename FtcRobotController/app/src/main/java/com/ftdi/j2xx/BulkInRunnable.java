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

class BulkInRunnable implements Runnable {
    UsbDeviceConnection usbDeviceConnection;
    UsbEndpoint usbEndpoint;
    ProcessInCtrl processInCtrl;
    FT_Device ftDevice;
    int cBuffer;
    int maxTransferSize;
    int readTimeout;
    Semaphore semaphore;
    boolean isStopped;

    BulkInRunnable(FT_Device ftDevice, ProcessInCtrl processInCtrl, UsbDeviceConnection usbDeviceConnection, UsbEndpoint usbEndpoint) {
        this.ftDevice           = ftDevice;
        this.usbEndpoint        = usbEndpoint;
        this.usbDeviceConnection = usbDeviceConnection;
        this.processInCtrl      = processInCtrl;
        this.cBuffer = this.processInCtrl.getDriverParameters().getBufferNumber();
        this.maxTransferSize    = this.processInCtrl.getDriverParameters().getMaxTransferSize();
        this.readTimeout        = this.ftDevice.getDriverParameters().getReadTimeout();
        this.semaphore          = new Semaphore(1);
        this.isStopped          = false;
    }

    void stop() throws InterruptedException {
        this.semaphore.acquire();
        this.isStopped = true;
    }

    void restart() {
        this.isStopped = false;
        this.semaphore.release();
    }

    boolean isStopped() {
        return this.isStopped;
    }

    public void run() {
        ByteBuffer byteBuffer = null;
        n var2 = null;
        int iBuffer = 0;
        boolean var4 = false;
        Object var5 = null;

        try {
            do {
                if (this.isStopped) {
                    this.semaphore.acquire();
                    this.semaphore.release();
                }

                var2 = this.processInCtrl.b(iBuffer);
                if (var2.getCbTransferred() == 0) {
                    byteBuffer = var2.getByteBuffer();
                    byteBuffer.clear();
                    var2.setBufferNumber(iBuffer);
                    byte[] var12 = byteBuffer.array();

                    int cbTransferred = this.usbDeviceConnection.bulkTransfer(this.usbEndpoint, var12, this.maxTransferSize, this.readTimeout);
                    if (cbTransferred > 0) {
                        byteBuffer.position(cbTransferred);
                        byteBuffer.flip();
                        var2.setCbTransferred(cbTransferred);
                        this.processInCtrl.onDataReceived(iBuffer);
                    }
                }

                ++iBuffer;
                iBuffer %= this.cBuffer;
            } while(!Thread.interrupted());

            throw new InterruptedException();
        } catch (InterruptedException interrupt) {
            try {
                this.processInCtrl.f();
                this.processInCtrl.e();
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
