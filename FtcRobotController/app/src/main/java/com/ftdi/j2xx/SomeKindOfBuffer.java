//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ftdi.j2xx;

import java.nio.ByteBuffer;

class SomeKindOfBuffer
    {
    private int a;
    private ByteBuffer byteBuffer;
    private int someCount;
    private boolean d;

    public SomeKindOfBuffer(int var1)
        {
        this.byteBuffer = ByteBuffer.allocate(var1);
        this.setSomeCount(0);
        }

    void a(int var1)
        {
        this.a = var1;
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

    synchronized boolean d()
        {
        return this.d;
        }

    synchronized ByteBuffer c(int var1)
        {
        ByteBuffer var2 = null;
        if (!this.d)
            {
            this.d = true;
            this.a = var1;
            var2 = this.byteBuffer;
            }

        return var2;
        }

    synchronized boolean d(int var1)
        {
        boolean result = false;
        if (this.d && var1 == this.a)
            {
            this.d = false;
            result = true;
            }

        return result;
        }
    }
