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
    int bufferCount;
    int cbMaxTransfer;
    int msReadTimeout;
    Semaphore semaphore;
    boolean isStopped;

    BulkInRunnable(FT_Device var1, ProcessInCtrl var2, UsbDeviceConnection var3, UsbEndpoint var4) {
        this.ftDevice = var1;
        this.usbEndpoint = var4;
        this.usbDeviceConnection = var3;
        this.processInCtrl = var2;
        this.bufferCount = this.processInCtrl.getDriverParameters().getBufferNumber();
        this.cbMaxTransfer = this.processInCtrl.getDriverParameters().getMaxTransferSize();
        this.msReadTimeout = this.ftDevice.getDriverParameters().getReadTimeout();
        this.semaphore = new Semaphore(1);
        this.isStopped = false;
    }

    void stop() throws InterruptedException {
        this.semaphore.acquire();
        this.isStopped = true;
    }

    void restart() {
        this.isStopped = false;
        this.semaphore.release();
    }

    boolean stopped() {
        return this.isStopped;
    }

    public void run() {
        ByteBuffer byteBuffer = null;
        SomeKindOfBuffer var2 = null;
        int iBuffer = 0;
        boolean var4 = false;
        Object var5 = null;

        try {
            do {
                if(this.isStopped) {
                    this.semaphore.acquire();
                    this.semaphore.release();
                }

                var2 = this.processInCtrl.b(iBuffer);
                if(var2.getSomeCount() == 0) {
                    byteBuffer = var2.getByteBuffer();
                    byteBuffer.clear();
                    var2.a(iBuffer);
                    byte[] var12 = byteBuffer.array();
                    int var11 = this.usbDeviceConnection.bulkTransfer(this.usbEndpoint, var12, this.cbMaxTransfer, this.msReadTimeout);
                    if(var11 > 0) {
                        byteBuffer.position(var11);
                        byteBuffer.flip();
                        var2.setSomeCount(var11);
                        this.processInCtrl.e(iBuffer);
                    }
                }

                ++iBuffer;
                iBuffer %= this.bufferCount;
            } while(!Thread.interrupted());

            throw new InterruptedException();
        } catch (InterruptedException interrupt) {
            try {
                this.processInCtrl.f();
                this.processInCtrl.e();
            } catch (Exception except) {
                Log.d("BulkIn::", "Stop BulkIn thread");
                except.printStackTrace();
            }
        } catch (Exception except) {
            except.printStackTrace();
            Log.e("BulkIn::", "Fatal error in BulkIn thread");
        }

    }
}
