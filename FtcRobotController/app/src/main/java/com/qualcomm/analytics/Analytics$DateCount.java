package com.qualcomm.analytics;

public static class DateCount
{
    private final String a;
    private final String b;
    
    public DateCount(final String a, final String b) {
        this.a = a;
        this.b = b;
    }
    
    public String count() {
        return this.b;
    }
    
    public String date() {
        return this.a;
    }
}
