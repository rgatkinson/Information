package com.qualcomm.robotcore.robocol;

import java.nio.BufferOverflowException;
import com.qualcomm.robotcore.util.RobotLog;
import java.nio.ByteBuffer;
import com.qualcomm.robotcore.exception.RobotCoreException;

public class PeerDiscovery implements RobocolParsable
{
    public static final short BUFFER_SIZE = 13;
    public static final short PAYLOAD_SIZE = 10;
    public static final byte ROBOCOL_VERSION = 1;
    private PeerType a;
    
    public PeerDiscovery(final PeerType a) {
        this.a = a;
    }
    
    @Override
    public void fromByteArray(final byte[] array) throws RobotCoreException {
        if (array.length < 13) {
            throw new RobotCoreException("Expected buffer of at least 13 bytes, received " + array.length);
        }
        final ByteBuffer wrap = ByteBuffer.wrap(array, 3, 10);
        switch (wrap.get()) {
            default: {}
            case 1: {
                this.a = PeerType.fromByte(wrap.get());
            }
        }
    }
    
    public PeerType getPeerType() {
        return this.a;
    }
    
    @Override
    public MsgType getRobocolMsgType() {
        return MsgType.PEER_DISCOVERY;
    }
    
    @Override
    public byte[] toByteArray() throws RobotCoreException {
        final ByteBuffer allocate = ByteBuffer.allocate(13);
        try {
            allocate.put(this.getRobocolMsgType().asByte());
            allocate.putShort((short)10);
            allocate.put((byte)1);
            allocate.put(this.a.asByte());
            return allocate.array();
        }
        catch (BufferOverflowException ex) {
            RobotLog.logStacktrace(ex);
            return allocate.array();
        }
    }
    
    @Override
    public String toString() {
        return String.format("Peer Discovery - peer type: %s", this.a.name());
    }
    
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
}
