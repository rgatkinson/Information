package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.exception.RobotCoreException;

public interface RobocolParsable
{
    public static final byte[] EMPTY_HEADER_BUFFER = new byte[3];
    public static final int HEADER_LENGTH = 3;
    
    void fromByteArray(byte[] p0) throws RobotCoreException;
    
    MsgType getRobocolMsgType();
    
    byte[] toByteArray() throws RobotCoreException;
    
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
        
        private MsgType(int b) {
            this.b = b;
        }
        
        public static MsgType fromByte(byte b) {
            MsgType empty;
            ArrayIndexOutOfBoundsException ex;
            empty = MsgType.EMPTY;
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
}
