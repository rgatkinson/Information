package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.util.RobotLog;

public enum MsgType
{
    COMMAND(4), 
    EMPTY(0), 
    GAMEPAD(2), 
    HEARTBEAT(1), 
    PEER_DISCOVERY(3), 
    TELEMETRY(5);
    
    private static final MsgType[] a;
    private final int b;
    
    static {
        a = values();
    }
    
    private MsgType(final int b) {
        this.b = b;
    }
    
    public static MsgType fromByte(final byte b) {
        final MsgType empty = MsgType.EMPTY;
        try {
            return MsgType.a[b];
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            RobotLog.w(String.format("Cannot convert %d to MsgType: %s", b, ex.toString()));
            return empty;
        }
    }
    
    public byte asByte() {
        return (byte)this.b;
    }
}
