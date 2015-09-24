package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.RobocolParsable;
import com.qualcomm.robotcore.util.RobotLog;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

public class Heartbeat implements RobocolParsable {
   public static final short BUFFER_SIZE = 13;
   public static final short MAX_SEQUENCE_NUMBER = 10000;
   public static final short PAYLOAD_SIZE = 10;
   private static short a = 0;
   private long b;
   private short c;

   public Heartbeat() {
      this.c = a();
      this.b = System.nanoTime();
   }

   public Heartbeat(Heartbeat.Token var1) {
      switch(null.a[var1.ordinal()]) {
      case 1:
         this.c = 0;
         this.b = 0L;
         return;
      default:
      }
   }

   private static short a() {
      synchronized(Heartbeat.class){}

      short var1;
      try {
         var1 = a++;
         if(a > 10000) {
            a = 0;
         }
      } finally {
         ;
      }

      return var1;
   }

   public void fromByteArray(byte[] var1) throws RobotCoreException {
      if(var1.length < 13) {
         throw new RobotCoreException("Expected buffer of at least 13 bytes, received " + var1.length);
      } else {
         ByteBuffer var2 = ByteBuffer.wrap(var1, 3, 10);
         this.c = var2.getShort();
         this.b = var2.getLong();
      }
   }

   public double getElapsedTime() {
      return (double)(System.nanoTime() - this.b) / 1.0E9D;
   }

   public RobocolParsable.MsgType getRobocolMsgType() {
      return RobocolParsable.MsgType.HEARTBEAT;
   }

   public short getSequenceNumber() {
      return this.c;
   }

   public long getTimestamp() {
      return this.b;
   }

   public byte[] toByteArray() throws RobotCoreException {
      ByteBuffer var1 = ByteBuffer.allocate(13);

      try {
         var1.put(this.getRobocolMsgType().asByte());
         var1.putShort((short)10);
         var1.putShort(this.c);
         var1.putLong(this.b);
      } catch (BufferOverflowException var3) {
         RobotLog.logStacktrace((Exception)var3);
      }

      return var1.array();
   }

   public String toString() {
      Object[] var1 = new Object[]{Short.valueOf(this.c), Long.valueOf(this.b)};
      return String.format("Heartbeat - seq: %4d, time: %d", var1);
   }

   public static enum Token {
      EMPTY;

      static {
         Heartbeat.Token[] var0 = new Heartbeat.Token[]{EMPTY};
      }
   }
}
