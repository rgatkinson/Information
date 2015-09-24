package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.RobocolParsable;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class RobocolDatagram {
   private DatagramPacket a;

   protected RobocolDatagram() {
      this.a = null;
   }

   public RobocolDatagram(RobocolParsable var1) throws RobotCoreException {
      this.setData(var1.toByteArray());
   }

   protected RobocolDatagram(DatagramPacket var1) {
      this.a = var1;
   }

   public RobocolDatagram(byte[] var1) {
      this.setData(var1);
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

   public void setAddress(InetAddress var1) {
      this.a.setAddress(var1);
   }

   public void setData(byte[] var1) {
      this.a = new DatagramPacket(var1, var1.length);
   }

   protected void setPacket(DatagramPacket var1) {
      this.a = var1;
   }

   public String toString() {
      String var1 = "NONE";
      String var2;
      int var3;
      if(this.a != null && this.a.getAddress() != null && this.a.getLength() > 0) {
         var1 = RobocolParsable.MsgType.fromByte(this.a.getData()[0]).name();
         var3 = this.a.getLength();
         var2 = this.a.getAddress().getHostAddress();
      } else {
         var2 = null;
         var3 = 0;
      }

      Object[] var4 = new Object[]{var1, var2, Integer.valueOf(var3)};
      return String.format("RobocolDatagram - type:%s, addr:%s, size:%d", var4);
   }
}
