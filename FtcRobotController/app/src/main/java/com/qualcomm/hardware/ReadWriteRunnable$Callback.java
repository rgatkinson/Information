package com.qualcomm.hardware;

public interface Callback
{
    void readComplete() throws InterruptedException;
    
    void writeComplete() throws InterruptedException;
}
