package com.qualcomm.robotcore.util;

import java.util.LinkedList;
import java.util.Queue;

public class RollingAverage
{
    public static final int DEFAULT_SIZE = 100;
    private final Queue<Integer> a;
    private long b;
    private int c;
    
    public RollingAverage() {
        this.a = new LinkedList<Integer>();
        this.resize(100);
    }
    
    public RollingAverage(final int n) {
        this.a = new LinkedList<Integer>();
        this.resize(n);
    }
    
    public void addNumber(final int n) {
        if (this.a.size() >= this.c) {
            this.b -= this.a.remove();
        }
        this.a.add(n);
        this.b += n;
    }
    
    public int getAverage() {
        if (this.a.isEmpty()) {
            return 0;
        }
        return (int)(this.b / this.a.size());
    }
    
    public void reset() {
        this.a.clear();
    }
    
    public void resize(final int c) {
        this.c = c;
        this.a.clear();
    }
    
    public int size() {
        return this.c;
    }
}
