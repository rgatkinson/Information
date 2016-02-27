//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ftdi.j2xx;

import java.nio.ByteBuffer;

class SomeKindOfBuffer
    {
    private int iBuffer;
    private ByteBuffer byteBuffer;
    private int someCount;
    private boolean isSomeStatus;

    public SomeKindOfBuffer(int cbCapacity)
        {
        this.byteBuffer = ByteBuffer.allocate(cbCapacity);
        this.setSomeCount(0);
        }

    void setBufferNumber(int iBuffer)
        {
        this.iBuffer = iBuffer;
        }

    ByteBuffer getByteBuffer()
        {
        return this.byteBuffer;
        }

    int getSomeCount()
        {
        return this.someCount;
        }

    void setSomeCount(int count)
        {
        this.someCount = count;
        }

    synchronized void clear()
        {
        this.byteBuffer.clear();
        this.setSomeCount(0);
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

    synchronized boolean clearSomeStatus(int iBuffer)
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
