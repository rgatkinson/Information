package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;
import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PeerDiscoveryManager {
   private InetAddress inetAddress;
   private final RobocolDatagramSocket socket;
   private ScheduledExecutorService executorService;
   private ScheduledFuture<?> peerDiscoveryMessageSender;
   private final PeerDiscovery peerDiscovery;

   public PeerDiscoveryManager(RobocolDatagramSocket socket) {
      this.peerDiscovery = new PeerDiscovery(PeerDiscovery.PeerType.PEER);
      this.socket = socket;
   }

   public InetAddress getPeerDiscoveryDevice() {
      return this.inetAddress;
   }

   public void start(InetAddress inetAddress) {
      RobotLog.v("Starting peer discovery");
      if(inetAddress == this.socket.getLocalAddress()) {
         RobotLog.v("No need for peer discovery, we are the peer discovery device");
      } else {
         if(this.peerDiscoveryMessageSender != null) {
            this.peerDiscoveryMessageSender.cancel(true);
         }

         this.inetAddress = inetAddress;
         this.executorService = Executors.newSingleThreadScheduledExecutor();
         this.peerDiscoveryMessageSender = this.executorService.scheduleAtFixedRate(new SendPeerDiscoveryRunnable(null), 1L, 1L, TimeUnit.SECONDS);
      }
   }

   public void stop() {
      RobotLog.v("Stopping peer discovery");
      if(this.peerDiscoveryMessageSender != null) {
         this.peerDiscoveryMessageSender.cancel(true);
      }

   }

   private class SendPeerDiscoveryRunnable implements Runnable {
      private SendPeerDiscoveryRunnable() {
      }

      // $FF: synthetic method
      SendPeerDiscoveryRunnable(Object var2) {
         this();
      }

      public void run() {
         try {
            RobotLog.v("Sending peer discovery packet");
            RobocolDatagram var2 = new RobocolDatagram(PeerDiscoveryManager.this.peerDiscovery);
            if(PeerDiscoveryManager.this.socket.getInetAddress() == null) {
               var2.setAddress(PeerDiscoveryManager.this.inetAddress);
            }

            PeerDiscoveryManager.this.socket.send(var2);
         } catch (RobotCoreException var3) {
            RobotLog.d("Unable to send peer discovery packet: " + var3.toString());
         }
      }
   }
}
