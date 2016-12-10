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

class BulkInWorker implements Runnable {
    UsbDeviceConnection usbDeviceConnection;
    UsbEndpoint usbEndpoint;
    ProcessInCtrl processInCtrl;
    FT_Device ftDevice;
    int bufferCount;
    int cbMaxTransfer;
    int msReadTimeout;
    Semaphore semaphore;
    boolean stopRequested;

    BulkInWorker(FT_Device var1, ProcessInCtrl var2, UsbDeviceConnection var3, UsbEndpoint var4) {
        this.ftDevice = var1;
        this.usbEndpoint = var4;
        this.usbDeviceConnection = var3;
        this.processInCtrl = var2;
        this.bufferCount = this.processInCtrl.getDriverParameters().getBufferCount();
        this.cbMaxTransfer = this.processInCtrl.getDriverParameters().getMaxTransferSize();
        this.msReadTimeout = this.ftDevice.getDriverParameters().getReadTimeout();
        this.semaphore = new Semaphore(1);
        this.stopRequested = false;
    }

    void stop() throws InterruptedException {
        this.semaphore.acquire();
        this.stopRequested = true;
    }

    void restart() {
        this.stopRequested = false;
        this.semaphore.release();
    }

    boolean stopped() {
        return this.stopRequested;
    }

    public void run() {
        ByteBuffer byteBuffer = null;
        InBuffer inBuffer = null;
        int iBuffer = 0;

        try {
            do {
                if (this.stopRequested) {
                    this.semaphore.acquire();
                    this.semaphore.release();
                }

                inBuffer = this.processInCtrl.acquireWritableBuffer(iBuffer);
                if(inBuffer.getLength() == 0) {
                    byteBuffer = inBuffer.getByteBuffer();
                    byteBuffer.clear();
                    inBuffer.setBufferId(iBuffer);
                    byte[] buffer = byteBuffer.array();

                    int cbRead = this.usbDeviceConnection.bulkTransfer(this.usbEndpoint, buffer, this.cbMaxTransfer, this.msReadTimeout);
                    if (cbRead > 0) {
                        byteBuffer.position(cbRead);
                        byteBuffer.flip();
                        inBuffer.setLength(cbRead);
                        this.processInCtrl.releaseReadableBuffer(iBuffer);
                    }
                }

                ++iBuffer;
                iBuffer %= this.bufferCount;
            } while(!Thread.interrupted());

            throw new InterruptedException();
        } catch (InterruptedException interrupt) {
            try {
                this.processInCtrl.releaseWritableBuffers();
                this.processInCtrl.purgeINData();
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
