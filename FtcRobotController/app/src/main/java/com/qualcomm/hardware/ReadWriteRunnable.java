package com.qualcomm.hardware;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.eventloop.SyncdDevice;

interface ReadWriteRunnable extends SyncdDevice, Runnable
{
    void blockUntilReady() throws RobotCoreException, InterruptedException;
    
    void close();
    
    ReadWriteRunnableSegment createSegment(int p0, int p1, int p2);
    
    void queueSegmentRead(int p0);
    
    void queueSegmentWrite(int p0);
    
    byte[] read(int p0, int p1);
    
    byte[] readFromWriteCache(int p0, int p1);
    
    void setCallback(Callback p0);
    
    void write(int p0, byte[] p1);
    
    public enum BlockingState
    {
        BLOCKING, 
        WAITING;
    }
    
    public interface Callback
    {
        void readComplete() throws InterruptedException;
        
        void writeComplete() throws InterruptedException;
    }
    
    public static class EmptyCallback implements Callback
    {
        @Override
        public void readComplete() throws InterruptedException {
        }
        
        @Override
        public void writeComplete() throws InterruptedException {
        }
    }
}
