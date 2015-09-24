package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;

public interface RobocolParsable {
   byte[] EMPTY_HEADER_BUFFER = new byte[3];
   int HEADER_LENGTH = 3;

   void fromByteArray(byte[] var1) throws RobotCoreException;

   RobocolParsable.MsgType getRobocolMsgType();

   byte[] toByteArray() throws RobotCoreException;

   public static enum MsgType {
      COMMAND(4),
      EMPTY(0),
      GAMEPAD(2),
      HEARTBEAT(1),
      PEER_DISCOVERY(3),
      TELEMETRY(5);

      private static final RobocolParsable.MsgType[] a;
      private final int b;

      static {
         RobocolParsable.MsgType[] var0 = new RobocolParsable.MsgType[]{EMPTY, HEARTBEAT, GAMEPAD, PEER_DISCOVERY, COMMAND, TELEMETRY};
         a = values();
      }

      private MsgType(int var3) {
         this.b = var3;
      }

      public static RobocolParsable.MsgType fromByte(byte var0) {
         RobocolParsable.MsgType var1 = EMPTY;

         try {
            RobocolParsable.MsgType var4 = a[var0];
            return var4;
         } catch (ArrayIndexOutOfBoundsException var5) {
            Object[] var3 = new Object[]{Byte.valueOf(var0), var5.toString()};
            RobotLog.w(String.format("Cannot convert %d to MsgType: %s", var3));
            return var1;
         }
      }

      public byte asByte() {
         return (byte)this.b;
      }
   }
}
