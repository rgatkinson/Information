package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.RobocolParsable;
import com.qualcomm.robotcore.util.RobotLog;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

public class PeerDiscovery implements RobocolParsable {
   public static final short BUFFER_SIZE = 13;
   public static final short PAYLOAD_SIZE = 10;
   public static final byte ROBOCOL_VERSION = 1;
   private PeerDiscovery.PeerType a;

   public PeerDiscovery(PeerDiscovery.PeerType var1) {
      this.a = var1;
   }

   public void fromByteArray(byte[] var1) throws RobotCoreException {
      if(var1.length < 13) {
         throw new RobotCoreException("Expected buffer of at least 13 bytes, received " + var1.length);
      } else {
         ByteBuffer var2 = ByteBuffer.wrap(var1, 3, 10);
         switch(var2.get()) {
         case 1:
            this.a = PeerDiscovery.PeerType.fromByte(var2.get());
            return;
         default:
         }
      }
   }

   public PeerDiscovery.PeerType getPeerType() {
      return this.a;
   }

   public RobocolParsable.MsgType getRobocolMsgType() {
      return RobocolParsable.MsgType.PEER_DISCOVERY;
   }

   public byte[] toByteArray() throws RobotCoreException {
      ByteBuffer var1 = ByteBuffer.allocate(13);

      try {
         var1.put(this.getRobocolMsgType().asByte());
         var1.putShort((short)10);
         var1.put((byte)1);
         var1.put(this.a.asByte());
      } catch (BufferOverflowException var3) {
         RobotLog.logStacktrace((Exception)var3);
      }

      return var1.array();
   }

   public String toString() {
      Object[] var1 = new Object[]{this.a.name()};
      return String.format("Peer Discovery - peer type: %s", var1);
   }

   public static enum PeerType {
      GROUP_OWNER(2),
      NOT_SET(0),
      PEER(1);

      private static final PeerDiscovery.PeerType[] a;
      private int b;

      static {
         PeerDiscovery.PeerType[] var0 = new PeerDiscovery.PeerType[]{NOT_SET, PEER, GROUP_OWNER};
         a = values();
      }

      private PeerType(int var3) {
         this.b = var3;
      }

      public static PeerDiscovery.PeerType fromByte(byte var0) {
         PeerDiscovery.PeerType var1 = NOT_SET;

         try {
            PeerDiscovery.PeerType var4 = a[var0];
            return var4;
         } catch (ArrayIndexOutOfBoundsException var5) {
            Object[] var3 = new Object[]{Byte.valueOf(var0), var5.toString()};
            RobotLog.w(String.format("Cannot convert %d to Peer: %s", var3));
            return var1;
         }
      }

      public byte asByte() {
         return (byte)this.b;
      }
   }
}
