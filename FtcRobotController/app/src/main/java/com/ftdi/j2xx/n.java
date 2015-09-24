package com.ftdi.j2xx;

import java.nio.ByteBuffer;

class n
{
    private int a;
    private ByteBuffer b;
    private int c;
    private boolean d;
    
    public n(final int n) {
        this.b = ByteBuffer.allocate(n);
        this.b(0);
    }
    
    ByteBuffer a() {
        return this.b;
    }
    
    void a(final int a) {
        this.a = a;
    }
    
    int b() {
        return this.c;
    }
    
    void b(final int c) {
        this.c = c;
    }
    
    ByteBuffer c(final int a) {
        synchronized (this) {
            final boolean d = this.d;
            ByteBuffer b = null;
            if (!d) {
                this.d = true;
                this.a = a;
                b = this.b;
            }
            return b;
        }
    }
    
    void c() {
        synchronized (this) {
            this.b.clear();
            this.b(0);
        }
    }
    
    boolean d() {
        synchronized (this) {
            return this.d;
        }
    }
    
    boolean d(final int n) {
        synchronized (this) {
            final boolean d = this.d;
            boolean b = false;
            if (d) {
                final int a = this.a;
                b = false;
                if (n == a) {
                    this.d = false;
                    b = true;
                }
            }
            return b;
        }
    }
}
