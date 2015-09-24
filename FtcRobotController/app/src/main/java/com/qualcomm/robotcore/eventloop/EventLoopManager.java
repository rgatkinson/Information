package com.qualcomm.robotcore.eventloop;

import com.qualcomm.robotcore.eventloop.EventLoop;
import com.qualcomm.robotcore.eventloop.SyncdDevice;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.Heartbeat;
import com.qualcomm.robotcore.robocol.PeerDiscovery;
import com.qualcomm.robotcore.robocol.RobocolDatagram;
import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class EventLoopManager {
   public static final String RC_BATTERY_LEVEL_KEY = "RobotController Battery Level";
   public static final String ROBOT_BATTERY_LEVEL_KEY = "Robot Battery Level";
   public static final String SYSTEM_TELEMETRY = "SYSTEM_TELEMETRY";
   private static final EventLoop a = new EventLoopManager.a(null);
   private Thread b;
   private Thread c;
   private final RobocolDatagramSocket d;
   private boolean e;
   private ElapsedTime f;
   private EventLoop g;
   private final Gamepad[] h;
   private Heartbeat i;
   private EventLoopManager.EventLoopMonitor j;
   private final Set<SyncdDevice> k;
   private final Command[] l;
   private int m;
   private final Set<Command> n;
   private InetAddress o;
   public EventLoopManager.State state;

   public EventLoopManager(RobocolDatagramSocket var1) {
      this.state = EventLoopManager.State.NOT_STARTED;
      this.b = new Thread();
      this.c = new Thread();
      this.e = false;
      this.f = new ElapsedTime();
      this.g = a;
      Gamepad[] var2 = new Gamepad[]{new Gamepad(), new Gamepad()};
      this.h = var2;
      this.i = new Heartbeat(Heartbeat.Token.EMPTY);
      this.j = null;
      this.k = new CopyOnWriteArraySet();
      this.l = new Command[8];
      this.m = 0;
      this.n = new CopyOnWriteArraySet();
      this.d = var1;
      this.a(EventLoopManager.State.NOT_STARTED);
   }

   // $FF: synthetic method
   static ElapsedTime a(EventLoopManager var0, ElapsedTime var1) {
      var0.f = var1;
      return var1;
   }

   // $FF: synthetic method
   static InetAddress a(EventLoopManager var0, InetAddress var1) {
      var0.o = var1;
      return var1;
   }

   private void a() throws RobotCoreException {
      // $FF: Couldn't be decompiled
   }

   private void a(EventLoopManager.State var1) {
      this.state = var1;
      RobotLog.v("EventLoopManager state is " + var1.toString());
      if(this.j != null) {
         this.j.onStateChange(var1);
      }

   }

   // $FF: synthetic method
   static void a(EventLoopManager var0, EventLoopManager.State var1) {
      var0.a(var1);
   }

   private void a(RobocolDatagram var1) throws RobotCoreException {
      Gamepad var2 = new Gamepad();
      var2.fromByteArray(var1.getData());
      if(var2.user >= 1 && var2.user <= 2) {
         int var3 = -1 + var2.user;
         this.h[var3] = var2;
         if(this.h[0].id == this.h[1].id) {
            RobotLog.v("Gamepad moved position, removing stale gamepad");
            if(var3 == 0) {
               this.h[1] = new Gamepad();
            }

            if(var3 == 1) {
               this.h[0] = new Gamepad();
               return;
            }
         }
      } else {
         RobotLog.d("Gamepad with user %d received. Only users 1 and 2 are valid");
      }

   }

   private void b() {
      this.b.interrupt();

      try {
         Thread.sleep(200L);
      } catch (InterruptedException var2) {
         ;
      }

      this.a(EventLoopManager.State.STOPPED);
      this.c();
      this.g = a;
      this.k.clear();
   }

   private void b(RobocolDatagram var1) throws RobotCoreException {
      this.d.send(var1);
      Heartbeat var2 = new Heartbeat(Heartbeat.Token.EMPTY);
      var2.fromByteArray(var1.getData());
      this.f.reset();
      this.i = var2;
   }

   private void c() {
      try {
         this.g.teardown();
      } catch (Exception var2) {
         RobotLog.w("Caught exception during looper teardown: " + var2.toString());
         RobotLog.logStacktrace(var2);
         if(RobotLog.hasGlobalErrorMsg()) {
            this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
            return;
         }
      }

   }

   private void c(RobocolDatagram var1) throws RobotCoreException {
      if(!var1.getAddress().equals(this.o)) {
         if(this.state == EventLoopManager.State.DROPPED_CONNECTION) {
            this.a(EventLoopManager.State.RUNNING);
         }

         if(this.g != a) {
            this.o = var1.getAddress();
            RobotLog.i("new remote peer discovered: " + this.o.getHostAddress());

            try {
               this.d.connect(this.o);
            } catch (SocketException var5) {
               RobotLog.e("Unable to connect to peer:" + var5.toString());
            }

            PeerDiscovery var3 = new PeerDiscovery(PeerDiscovery.PeerType.PEER);
            RobotLog.v("Sending peer discovery packet");
            RobocolDatagram var4 = new RobocolDatagram(var3);
            if(this.d.getInetAddress() == null) {
               var4.setAddress(this.o);
            }

            this.d.send(var4);
            return;
         }
      }

   }

   private void d() {
   }

   private void d(RobocolDatagram var1) throws RobotCoreException {
      Command var2 = new Command(var1.getData());
      if(var2.isAcknowledged()) {
         this.n.remove(var2);
      } else {
         var2.acknowledge();
         this.d.send(new RobocolDatagram(var2));
         Command[] var3 = this.l;
         int var4 = var3.length;
         int var5 = 0;

         while(true) {
            if(var5 >= var4) {
               Command[] var6 = this.l;
               int var7 = this.m;
               this.m = var7 + 1;
               var6[var7 % this.l.length] = var2;

               try {
                  this.g.processCommand(var2);
                  return;
               } catch (Exception var10) {
                  RobotLog.e("Event loop threw an exception while processing a command");
                  RobotLog.logStacktrace(var10);
                  return;
               }
            }

            Command var9 = var3[var5];
            if(var9 != null && var9.equals(var2)) {
               break;
            }

            ++var5;
         }
      }

   }

   // $FF: synthetic method
   static ElapsedTime e(EventLoopManager var0) {
      return var0.f;
   }

   private void e(RobocolDatagram var1) {
      RobotLog.w("RobotCore event loop received unknown event type: " + var1.getMsgType().name());
   }

   // $FF: synthetic method
   static Set f(EventLoopManager var0) {
      return var0.k;
   }

   // $FF: synthetic method
   static EventLoop g(EventLoopManager var0) {
      return var0.g;
   }

   // $FF: synthetic method
   static void h(EventLoopManager var0) {
      var0.c();
   }

   public void buildAndSendTelemetry(String var1, String var2) {
      Telemetry var3 = new Telemetry();
      var3.setTag(var1);
      var3.addData(var1, var2);
      this.sendTelemetryData(var3);
   }

   public EventLoop getEventLoop() {
      return this.g;
   }

   public Gamepad getGamepad() {
      return this.getGamepad(0);
   }

   public Gamepad getGamepad(int var1) {
      Range.throwIfRangeIsInvalid((double)var1, 0.0D, 1.0D);
      return this.h[var1];
   }

   public Gamepad[] getGamepads() {
      return this.h;
   }

   public Heartbeat getHeartbeat() {
      return this.i;
   }

   public void handleDroppedConnection() {
      OpModeManager var1 = this.g.getOpModeManager();
      String var2 = "Lost connection while running op mode: " + var1.getActiveOpModeName();
      var1.initActiveOpMode("Stop Robot");
      this.a(EventLoopManager.State.DROPPED_CONNECTION);
      RobotLog.i(var2);
   }

   public void registerSyncdDevice(SyncdDevice var1) {
      this.k.add(var1);
   }

   public void sendCommand(Command var1) {
      this.n.add(var1);
   }

   public void sendTelemetryData(Telemetry var1) {
      try {
         this.d.send(new RobocolDatagram(var1.toByteArray()));
      } catch (RobotCoreException var3) {
         RobotLog.w("Failed to send telemetry data");
         RobotLog.logStacktrace(var3);
      }

      var1.clearData();
   }

   public void setEventLoop(EventLoop var1) throws RobotCoreException {
      if(var1 == null) {
         var1 = a;
         RobotLog.d("Event loop cannot be null, using empty event loop");
      }

      this.b();
      this.g = var1;
      this.a();
   }

   public void setMonitor(EventLoopManager.EventLoopMonitor var1) {
      this.j = var1;
   }

   public void shutdown() {
      this.d.close();
      this.c.interrupt();
      this.e = true;
      this.b();
   }

   public void start(EventLoop var1) throws RobotCoreException {
      this.e = false;
      this.c = new Thread(new EventLoopManager.d(null));
      this.c.start();
      (new Thread(new EventLoopManager.c(null))).start();
      this.setEventLoop(var1);
   }

   public void unregisterSyncdDevice(SyncdDevice var1) {
      this.k.remove(var1);
   }

   public interface EventLoopMonitor {
      void onStateChange(EventLoopManager.State var1);
   }

   public static enum State {
      DROPPED_CONNECTION,
      EMERGENCY_STOP,
      INIT,
      NOT_STARTED,
      RUNNING,
      STOPPED;

      static {
         EventLoopManager.State[] var0 = new EventLoopManager.State[]{NOT_STARTED, INIT, RUNNING, STOPPED, EMERGENCY_STOP, DROPPED_CONNECTION};
      }
   }

   private static class a implements EventLoop {
      private a() {
      }

      // $FF: synthetic method
      a(Object var1) {
         this();
      }

      public OpModeManager getOpModeManager() {
         return null;
      }

      public void init(EventLoopManager var1) {
      }

      public void loop() {
      }

      public void processCommand(Command var1) {
         RobotLog.w("Dropping command " + var1.getName() + ", no active event loop");
      }

      public void teardown() {
      }
   }

   private class b implements Runnable {
      private b() {
      }

      // $FF: synthetic method
      b(Object var2) {
         this();
      }

      public void run() {
         // $FF: Couldn't be decompiled
      }
   }

   private class c implements Runnable {
      ElapsedTime a;

      private c() {
         this.a = new ElapsedTime();
      }

      // $FF: synthetic method
      c(Object var2) {
         this();
      }

      public void run() {
         while(true) {
            RobocolDatagram var1 = EventLoopManager.this.d.recv();
            if(EventLoopManager.this.e || EventLoopManager.this.d.isClosed()) {
               return;
            }

            if(var1 == null) {
               Thread.yield();
            } else {
               if(RobotLog.hasGlobalErrorMsg()) {
                  EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
               }

               try {
                  switch(null.a[var1.getMsgType().ordinal()]) {
                  case 1:
                     EventLoopManager.this.a(var1);
                     break;
                  case 2:
                     EventLoopManager.this.b(var1);
                     break;
                  case 3:
                     EventLoopManager.this.c(var1);
                     break;
                  case 4:
                     EventLoopManager.this.d(var1);
                     break;
                  case 5:
                     EventLoopManager.this.d();
                     break;
                  default:
                     EventLoopManager.this.e(var1);
                  }
               } catch (RobotCoreException var3) {
                  RobotLog.w("RobotCore event loop cannot process event: " + var3.toString());
               }
            }
         }
      }
   }

   private class d implements Runnable {
      private Set<Command> b;

      private d() {
         this.b = new HashSet();
      }

      // $FF: synthetic method
      d(Object var2) {
         this();
      }

      public void run() {
         while(true) {
            if(!Thread.interrupted()) {
               Iterator var1 = EventLoopManager.this.n.iterator();

               while(var1.hasNext()) {
                  Command var4 = (Command)var1.next();
                  if(var4.getAttempts() > 10) {
                     RobotLog.w("Failed to send command, too many attempts: " + var4.toString());
                     this.b.add(var4);
                  } else if(var4.isAcknowledged()) {
                     RobotLog.v("Command " + var4.getName() + " has been acknowledged by remote device");
                     this.b.add(var4);
                  } else {
                     try {
                        RobotLog.v("Sending command: " + var4.getName() + ", attempt " + var4.getAttempts());
                        EventLoopManager.this.d.send(new RobocolDatagram(var4.toByteArray()));
                     } catch (RobotCoreException var6) {
                        RobotLog.w("Failed to send command " + var4.getName());
                        RobotLog.logStacktrace(var6);
                     }
                  }
               }

               EventLoopManager.this.n.removeAll(this.b);
               this.b.clear();

               try {
                  Thread.sleep(100L);
                  continue;
               } catch (InterruptedException var7) {
                  ;
               }
            }

            return;
         }
      }
   }
}
