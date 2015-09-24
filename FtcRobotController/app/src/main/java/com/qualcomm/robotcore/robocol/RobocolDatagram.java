package com.qualcomm.robotcore.robocol;

import java.net.InetAddress;
import com.qualcomm.robotcore.exception.RobotCoreException;
import java.net.DatagramPacket;

public class RobocolDatagram
{
    private DatagramPacket a;
    
    protected RobocolDatagram() {
        this.a = null;
    }
    
    public RobocolDatagram(final RobocolParsable robocolParsable) throws RobotCoreException {
        this.setData(robocolParsable.toByteArray());
    }
    
    protected RobocolDatagram(final DatagramPacket a) {
        this.a = a;
    }
    
    public RobocolDatagram(final byte[] data) {
        this.setData(data);
    }
    
    public InetAddress getAddress() {
        return this.a.getAddress();
    }
    
    public byte[] getData() {
        return this.a.getData();
    }
    
    public int getLength() {
        return this.a.getLength();
    }
    
    public RobocolParsable.MsgType getMsgType() {
        return RobocolParsable.MsgType.fromByte(this.a.getData()[0]);
    }
    
    protected DatagramPacket getPacket() {
        return this.a;
    }
    
    public int getPayloadLength() {
        return -3 + this.a.getLength();
    }
    
    public void setAddress(final InetAddress address) {
        this.a.setAddress(address);
    }
    
    public void setData(final byte[] array) {
        this.a = new DatagramPacket(array, array.length);
    }
    
    protected void setPacket(final DatagramPacket a) {
        this.a = a;
    }
    
    @Override
    public String toString() {
        String name = "NONE";
        int length;
        String hostAddress;
        if (this.a != null && this.a.getAddress() != null && this.a.getLength() > 0) {
            name = RobocolParsable.MsgType.fromByte(this.a.getData()[0]).name();
            length = this.a.getLength();
            hostAddress = this.a.getAddress().getHostAddress();
        }
        else {
            hostAddress = null;
            length = 0;
        }
        return String.format("RobocolDatagram - type:%s, addr:%s, size:%d", name, hostAddress, length);
    }
}
