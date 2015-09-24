package com.qualcomm.hardware;

public static class EmptyCallback implements Callback
{
    @Override
    public void readComplete() throws InterruptedException {
    }
    
    @Override
    public void writeComplete() throws InterruptedException {
    }
}
