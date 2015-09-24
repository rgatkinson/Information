package com.qualcomm.robotcore.robocol;

import java.nio.BufferOverflowException;
import com.qualcomm.robotcore.util.RobotLog;
import java.nio.ByteBuffer;
import com.qualcomm.robotcore.exception.RobotCoreException;

public class Heartbeat implements RobocolParsable
{
    public static final short BUFFER_SIZE = 13;
    public static final short MAX_SEQUENCE_NUMBER = 10000;
    public static final short PAYLOAD_SIZE = 10;
    private static short a;
    private long b;
    private short c;
    
    static {
        Heartbeat.a = 0;
    }
    
    public Heartbeat() {
        this.c = a();
        this.b = System.nanoTime();
    }
    
    public Heartbeat(final Token token) {
        switch (Heartbeat$1.a[token.ordinal()]) {
            default: {}
            case 1: {
                this.c = 0;
                this.b = 0L;
            }
        }
    }
    
    private static short a() {
        synchronized (Heartbeat.class) {
            final short a = Heartbeat.a;
            ++Heartbeat.a;
            if (Heartbeat.a > 10000) {
                Heartbeat.a = 0;
            }
            return a;
        }
    }
    
    @Override
    public void fromByteArray(final byte[] array) throws RobotCoreException {
        if (array.length < 13) {
            throw new RobotCoreException("Expected buffer of at least 13 bytes, received " + array.length);
        }
        final ByteBuffer wrap = ByteBuffer.wrap(array, 3, 10);
        this.c = wrap.getShort();
        this.b = wrap.getLong();
    }
    
    public double getElapsedTime() {
        return (System.nanoTime() - this.b) / 1.0E9;
    }
    
    @Override
    public MsgType getRobocolMsgType() {
        return MsgType.HEARTBEAT;
    }
    
    public short getSequenceNumber() {
        return this.c;
    }
    
    public long getTimestamp() {
        return this.b;
    }
    
    @Override
    public byte[] toByteArray() throws RobotCoreException {
        final ByteBuffer allocate = ByteBuffer.allocate(13);
        try {
            allocate.put(this.getRobocolMsgType().asByte());
            allocate.putShort((short)10);
            allocate.putShort(this.c);
            allocate.putLong(this.b);
            return allocate.array();
        }
        catch (BufferOverflowException ex) {
            RobotLog.logStacktrace(ex);
            return allocate.array();
        }
    }
    
    @Override
    public String toString() {
        return String.format("Heartbeat - seq: %4d, time: %d", this.c, this.b);
    }
    
    public enum Token
    {
        EMPTY;
    }
}
