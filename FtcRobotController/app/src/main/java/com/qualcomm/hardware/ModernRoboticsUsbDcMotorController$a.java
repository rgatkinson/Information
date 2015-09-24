package com.qualcomm.hardware;

private static class a
{
    private int[] a;
    private int[] b;
    private int c;
    
    private a() {
        this.a = new int[3];
        this.b = new int[3];
        this.c = 0;
    }
    
    public void a(final int n) {
        final int n2 = this.a[this.c];
        this.c = (1 + this.c) % this.a.length;
        this.b[this.c] = Math.abs(n2 - n);
        this.a[this.c] = n;
    }
    
    public boolean a() {
        final int[] b = this.b;
        final int length = b.length;
        int i = 0;
        int n = 0;
        while (i < length) {
            n += b[i];
            ++i;
        }
        boolean b2 = false;
        if (n > 6) {
            b2 = true;
        }
        return b2;
    }
}
