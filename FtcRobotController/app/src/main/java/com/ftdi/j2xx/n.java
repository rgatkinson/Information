//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ftdi.j2xx;

import java.nio.ByteBuffer;

class n {
    private int a;
    private ByteBuffer b;
    private int c;
    private boolean d;

    public n(int var1) {
        this.b = ByteBuffer.allocate(var1);
        this.b(0);
    }

    void a(int var1) {
        this.a = var1;
    }

    ByteBuffer a() {
        return this.b;
    }

    int b() {
        return this.c;
    }

    void b(int var1) {
        this.c = var1;
    }

    synchronized void c() {
        this.b.clear();
        this.b(0);
    }

    synchronized boolean d() {
        return this.d;
    }

    synchronized ByteBuffer c(int var1) {
        ByteBuffer var2 = null;
        if(!this.d) {
            this.d = true;
            this.a = var1;
            var2 = this.b;
        }

        return var2;
    }

    synchronized boolean d(int var1) {
        boolean var2 = false;
        if(this.d && var1 == this.a) {
            this.d = false;
            var2 = true;
        }

        return var2;
    }
}
