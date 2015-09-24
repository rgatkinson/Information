package com.ftdi.j2xx;

class m
{
    private int a;
    private int b;
    
    m() {
        this.a = 0;
        this.b = 0;
    }
    
    m(final int a, final int b) {
        this.a = a;
        this.b = b;
    }
    
    public int a() {
        return this.a;
    }
    
    public int b() {
        return this.b;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (!(o instanceof m)) {
                return false;
            }
            final m m = (m)o;
            if (this.a != m.a) {
                return false;
            }
            if (this.b != m.b) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String toString() {
        return "Vendor: " + String.format("%04x", this.a) + ", Product: " + String.format("%04x", this.b);
    }
}
