//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ftdi.j2xx;

import java.nio.ByteBuffer;

class n {
    private int iBuffer;
    private ByteBuffer byteBuffer;
    private int cbTransferred;
    private boolean d;

    public n(int cbAllocate) {
        this.byteBuffer = ByteBuffer.allocate(cbAllocate);
        this.setCbTransferred(0);
    }

    void setBufferNumber(int iBuffer) {
        this.iBuffer = iBuffer;
    }

    ByteBuffer getByteBuffer() {
        return this.byteBuffer;
    }

    int getCbTransferred() {
        return this.cbTransferred;
    }

    void setCbTransferred(int cbTransferred) {
        this.cbTransferred = cbTransferred;
    }

    synchronized void clear() {
        this.byteBuffer.clear();
        this.setCbTransferred(0);
    }

    synchronized boolean d() {
        return this.d;
    }

    synchronized ByteBuffer c(int var1) {
        ByteBuffer var2 = null;
        if(!this.d) {
            this.d = true;
            this.iBuffer = var1;
            var2 = this.byteBuffer;
        }
        return var2;
    }

    synchronized boolean d(int var1) {
        boolean var2 = false;
        if(this.d && var1 == this.iBuffer) {
            this.d = false;
            var2 = true;
        }
        return var2;
    }
}
