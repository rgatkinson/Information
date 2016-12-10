//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ftdi.j2xx;

import java.nio.ByteBuffer;

class InBuffer
    {
    private int iBuffer;
    private ByteBuffer byteBuffer;
    private int someCount;
    private boolean isSomeStatus;

    public InBuffer(int cbCapacity)
        {
        this.byteBuffer = ByteBuffer.allocate(cbCapacity);
        this.setLength(0);
        }

    void setBufferId(int iBuffer)
        {
        this.iBuffer = iBuffer;
        }

    ByteBuffer getByteBuffer()
        {
        return this.byteBuffer;
        }

    int getLength()
        {
        return this.someCount;
        }

    void setLength(int count)
        {
        this.someCount = count;
        }

    synchronized void purge()
        {
        this.byteBuffer.clear();
        this.setLength(0);
        }

    synchronized boolean isSomeStatus()
        {
        return this.isSomeStatus;
        }

    synchronized ByteBuffer setSomeStatus(int iBuffer)
        {
        ByteBuffer result = null;
        if (!this.isSomeStatus)
            {
            this.isSomeStatus = true;
            this.iBuffer = iBuffer;
            result = this.byteBuffer;
            }

        return result;
        }

    synchronized boolean release(int iBuffer)
        {
        boolean result = false;
        if (this.isSomeStatus && iBuffer == this.iBuffer)
            {
            this.isSomeStatus = false;
            result = true;
            }

        return result;
        }
    }
