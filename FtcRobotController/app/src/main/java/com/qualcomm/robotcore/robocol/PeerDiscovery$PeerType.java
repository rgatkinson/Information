package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.util.RobotLog;

public enum PeerType
{
    GROUP_OWNER(2), 
    NOT_SET(0), 
    PEER(1);
    
    private static final PeerType[] a;
    private int b;
    
    static {
        a = values();
    }
    
    private PeerType(final int b) {
        this.b = b;
    }
    
    public static PeerType fromByte(final byte b) {
        final PeerType not_SET = PeerType.NOT_SET;
        try {
            return PeerType.a[b];
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            RobotLog.w(String.format("Cannot convert %d to Peer: %s", b, ex.toString()));
            return not_SET;
        }
    }
    
    public byte asByte() {
        return (byte)this.b;
    }
}
