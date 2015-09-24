package com.qualcomm.hardware;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class ReadWriteRunnableSegment
{
    final Lock a;
    final Lock b;
    private int c;
    private final byte[] d;
    private final byte[] e;
    
    public ReadWriteRunnableSegment(final int c, final int n) {
        this.c = c;
        this.a = new ReentrantLock();
        this.d = new byte[n];
        this.b = new ReentrantLock();
        this.e = new byte[n];
    }
    
    public int getAddress() {
        return this.c;
    }
    
    public byte[] getReadBuffer() {
        return this.d;
    }
    
    public Lock getReadLock() {
        return this.a;
    }
    
    public byte[] getWriteBuffer() {
        return this.e;
    }
    
    public Lock getWriteLock() {
        return this.b;
    }
    
    public void setAddress(final int c) {
        this.c = c;
    }
    
    @Override
    public String toString() {
        return String.format("Segment - address:%d read:%d write:%d", this.c, this.d.length, this.e.length);
    }
}
