package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.util.RobotLog;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.net.SocketException;

public class RobocolDatagramSocket {
   private final byte[] a = new byte[4098];
   private DatagramSocket socket;
   private final DatagramPacket packet;
   private final RobocolDatagram datagram;
   private volatile RobocolDatagramSocket.State state;

   public RobocolDatagramSocket() {
      this.packet = new DatagramPacket(this.a, this.a.length);
      this.datagram = new RobocolDatagram();
      this.state = RobocolDatagramSocket.State.CLOSED;
   }

   public void bind(InetSocketAddress address) throws SocketException {
      if(this.state != RobocolDatagramSocket.State.CLOSED) {
         this.close();
      }

      this.state = RobocolDatagramSocket.State.LISTENING;
      RobotLog.d("RobocolDatagramSocket binding to " + address.toString());
      this.socket = new DatagramSocket(address);
   }

   public void close() {
      this.state = RobocolDatagramSocket.State.CLOSED;
      if(this.socket != null) {
         this.socket.close();
      }

      RobotLog.d("RobocolDatagramSocket is closed");
   }

   public void connect(InetAddress address) throws SocketException {
      InetSocketAddress addressAndPort = new InetSocketAddress(address, 20884);
      RobotLog.d("RobocolDatagramSocket connected to " + addressAndPort.toString());
      this.socket.connect(addressAndPort);
   }

   public InetAddress getInetAddress() {
      return this.socket == null?null:this.socket.getInetAddress();
   }

   public InetAddress getLocalAddress() {
      return this.socket == null?null:this.socket.getLocalAddress();
   }

   public RobocolDatagramSocket.State getState() {
      return this.state;
   }

   public boolean isClosed() {
      return this.state == RobocolDatagramSocket.State.CLOSED;
   }

   public boolean isRunning() {
      return this.state == RobocolDatagramSocket.State.LISTENING;
   }

   public void listen(InetAddress address) throws SocketException {
      this.bind(new InetSocketAddress(RobocolConfig.determineBindAddress(address), 20884));
   }

   public RobocolDatagram recv() {
      try {
         this.socket.receive(this.packet);
      } catch (PortUnreachableException var4) {
         RobotLog.d("RobocolDatagramSocket receive error: remote port unreachable");
         return null;
      } catch (IOException var5) {
         RobotLog.d("RobocolDatagramSocket receive error: " + var5.toString());
         return null;
      } catch (NullPointerException var6) {
         RobotLog.d("RobocolDatagramSocket receive error: " + var6.toString());
      }

      this.datagram.setPacket(this.packet);
      return this.datagram;
   }

   public void send(RobocolDatagram var1) {
      try {
         this.socket.send(var1.getPacket());
      } catch (IllegalArgumentException var5) {
         RobotLog.w("Unable to send RobocolDatagram: " + var5.toString());
         RobotLog.w("               " + var1.toString());
      } catch (IOException var6) {
         RobotLog.w("Unable to send RobocolDatagram: " + var6.toString());
         RobotLog.w("               " + var1.toString());
      } catch (NullPointerException var7) {
         RobotLog.w("Unable to send RobocolDatagram: " + var7.toString());
         RobotLog.w("               " + var1.toString());
      }
   }

   public static enum State {
      CLOSED,
      ERROR,
      LISTENING;

      static {
         RobocolDatagramSocket.State[] var0 = new RobocolDatagramSocket.State[]{LISTENING, CLOSED, ERROR};
      }
   }
}
