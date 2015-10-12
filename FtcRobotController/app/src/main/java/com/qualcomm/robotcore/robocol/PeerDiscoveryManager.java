package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.PeerDiscovery;
import com.qualcomm.robotcore.robocol.RobocolDatagram;
import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;
import com.qualcomm.robotcore.util.RobotLog;
import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PeerDiscoveryManager {
   private InetAddress a;
   private final RobocolDatagramSocket b;
   private ScheduledExecutorService c;
   private ScheduledFuture<?> d;
   private final PeerDiscovery e;

   public PeerDiscoveryManager(RobocolDatagramSocket var1) {
      this.e = new PeerDiscovery(PeerDiscovery.PeerType.PEER);
      this.b = var1;
   }

   public InetAddress getPeerDiscoveryDevice() {
      return this.a;
   }

   public void start(InetAddress var1) {
      RobotLog.v("Starting peer discovery");
      if(var1 == this.b.getLocalAddress()) {
         RobotLog.v("No need for peer discovery, we are the peer discovery device");
      } else {
         if(this.d != null) {
            this.d.cancel(true);
         }

         this.a = var1;
         this.c = Executors.newSingleThreadScheduledExecutor();
         this.d = this.c.scheduleAtFixedRate(new PeerDiscoveryManager.a(null), 1L, 1L, TimeUnit.SECONDS);
      }
   }

   public void stop() {
      RobotLog.v("Stopping peer discovery");
      if(this.d != null) {
         this.d.cancel(true);
      }

   }

   private class a implements Runnable {
      private a() {
      }

      // $FF: synthetic method
      a(Object var2) {
         this();
      }

      public void run() {
         try {
            RobotLog.v("Sending peer discovery packet");
            RobocolDatagram var2 = new RobocolDatagram(PeerDiscoveryManager.this.e);
            if(PeerDiscoveryManager.this.b.getInetAddress() == null) {
               var2.setAddress(PeerDiscoveryManager.this.a);
            }

            PeerDiscoveryManager.this.b.send(var2);
         } catch (RobotCoreException var3) {
            RobotLog.d("Unable to send peer discovery packet: " + var3.toString());
         }
      }
   }
}
