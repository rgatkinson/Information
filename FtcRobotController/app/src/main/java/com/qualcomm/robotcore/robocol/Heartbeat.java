package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.util.RobotLog;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

public class Heartbeat implements RobocolParsable {
   public static final short BUFFER_SIZE = 14;
   public static final short MAX_SEQUENCE_NUMBER = 10000;
   public static final short PAYLOAD_SIZE = 11;
   private static short a = 0;
   private long timeStamp;
   private short sequenceNumber;
   private RobotState robotState;

   public Heartbeat() {
      this.sequenceNumber = a();
      this.timeStamp = System.nanoTime();
      this.robotState = RobotState.NOT_STARTED;
   }

   public Heartbeat(Heartbeat.Token var1) {
      switch(null.a[var1.ordinal()]) {
      case 1:
         this.sequenceNumber = 0;
         this.timeStamp = 0L;
         this.robotState = RobotState.NOT_STARTED;
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

   public void fromByteArray(byte[] bytes) throws RobotCoreException {
      if(bytes.length < 14) {
         throw new RobotCoreException("Expected buffer of at least 14 bytes, received " + bytes.length);
      } else {
         ByteBuffer buffer = ByteBuffer.wrap(bytes, 3, 11);
         this.sequenceNumber = buffer.getShort();
         this.timeStamp = buffer.getLong();
         this.robotState = RobotState.fromByte(buffer.get());
      }
   }

   public double getElapsedTime() {
      return (double)(System.nanoTime() - this.timeStamp) / 1.0E9D;
   }

   public RobocolParsable.MsgType getRobocolMsgType() {
      return RobocolParsable.MsgType.HEARTBEAT;
   }

   public byte getRobotState() {
      return this.robotState.asByte();
   }

   public short getSequenceNumber() {
      return this.sequenceNumber;
   }

   public long getTimestamp() {
      return this.timeStamp;
   }

   public void setRobotState(RobotState state) {
      this.robotState = state;
   }

   public byte[] toByteArray() throws RobotCoreException {
      ByteBuffer buffer = ByteBuffer.allocate(14);

      try {
         buffer.put(this.getRobocolMsgType().asByte());
         buffer.putShort((short) 11);
         buffer.putShort(this.sequenceNumber);
         buffer.putLong(this.timeStamp);
         buffer.put(this.robotState.asByte());
      } catch (BufferOverflowException var3) {
         RobotLog.logStacktrace((Exception)var3);
      }

      return buffer.array();
   }

   public String toString() {
      return String.format("Heartbeat - seq: %4d, time: %d", this.sequenceNumber, this.timeStamp);
   }

   public static enum Token {
      EMPTY;

      static {
         Heartbeat.Token[] var0 = new Heartbeat.Token[]{EMPTY};
      }
   }
}
