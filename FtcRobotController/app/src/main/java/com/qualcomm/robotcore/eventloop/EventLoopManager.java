//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

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
import com.qualcomm.robotcore.robocol.Heartbeat.Token;
import com.qualcomm.robotcore.robocol.PeerDiscovery.PeerType;
import com.qualcomm.robotcore.robot.RobotState;
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
    public static final String SYSTEM_TELEMETRY = "SYSTEM_TELEMETRY";
    public static final String ROBOT_BATTERY_LEVEL_KEY = "Robot Battery Level";
    public static final String RC_BATTERY_LEVEL_KEY = "RobotController Battery Level";
    private static final EventLoop a = new EventLoopManager.a((EventLoopManager.SyntheticClass_1)null);
    public RobotState state;
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

    public void handleDroppedConnection() {
        OpModeManager var1 = this.g.getOpModeManager();
        String var2 = "Lost connection while running op mode: " + var1.getActiveOpModeName();
        var1.initActiveOpMode("Stop Robot");
        this.a(RobotState.DROPPED_CONNECTION);
        RobotLog.i(var2);
    }

    public EventLoopManager(RobocolDatagramSocket socket) {
        this.state = RobotState.NOT_STARTED;
        this.b = new Thread();
        this.c = new Thread();
        this.e = false;
        this.f = new ElapsedTime();
        this.g = a;
        this.h = new Gamepad[]{new Gamepad(), new Gamepad()};
        this.i = new Heartbeat(Token.EMPTY);
        this.j = null;
        this.k = new CopyOnWriteArraySet();
        this.l = new Command[8];
        this.m = 0;
        this.n = new CopyOnWriteArraySet();
        this.d = socket;
        this.a(RobotState.NOT_STARTED);
    }

    public void setMonitor(EventLoopManager.EventLoopMonitor monitor) {
        this.j = monitor;
    }

    public void start(EventLoop eventLoop) throws RobotCoreException {
        this.e = false;
        this.c = new Thread(new EventLoopManager.d((EventLoopManager.SyntheticClass_1)null), "Scheduled Sends");
        this.c.start();
        (new Thread(new EventLoopManager.c((EventLoopManager.SyntheticClass_1)null))).start();
        this.setEventLoop(eventLoop);
    }

    public void shutdown() {
        this.d.close();
        this.c.interrupt();
        this.e = true;
        this.b();
    }

    public void registerSyncdDevice(SyncdDevice device) {
        this.k.add(device);
    }

    public void unregisterSyncdDevice(SyncdDevice device) {
        this.k.remove(device);
    }

    public void setEventLoop(EventLoop eventLoop) throws RobotCoreException {
        if(eventLoop == null) {
            eventLoop = a;
            RobotLog.d("Event loop cannot be null, using empty event loop");
        }

        this.b();
        this.g = eventLoop;
        this.a();
    }

    public EventLoop getEventLoop() {
        return this.g;
    }

    public Gamepad getGamepad() {
        return this.getGamepad(0);
    }

    public Gamepad getGamepad(int port) {
        Range.throwIfRangeIsInvalid((double)port, 0.0D, 1.0D);
        return this.h[port];
    }

    public Gamepad[] getGamepads() {
        return this.h;
    }

    public Heartbeat getHeartbeat() {
        return this.i;
    }

    public void sendTelemetryData(Telemetry telemetry) {
        try {
            this.d.send(new RobocolDatagram(telemetry.toByteArray()));
        } catch (RobotCoreException var3) {
            RobotLog.w("Failed to send telemetry data");
            RobotLog.logStacktrace(var3);
        }

        telemetry.clearData();
    }

    public void sendCommand(Command command) {
        this.n.add(command);
    }

    private void a() throws RobotCoreException {
        try {
            this.a(RobotState.INIT);
            this.g.init(this);
            Iterator var1 = this.k.iterator();

            while(var1.hasNext()) {
                SyncdDevice var2 = (SyncdDevice)var1.next();
                var2.startBlockingWork();
            }
        } catch (Exception var3) {
            RobotLog.w("Caught exception during looper init: " + var3.toString());
            RobotLog.logStacktrace(var3);
            this.a(RobotState.EMERGENCY_STOP);
            if(RobotLog.hasGlobalErrorMsg()) {
                this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
            }

            throw new RobotCoreException("Robot failed to start: " + var3.getMessage());
        }

        this.f = new ElapsedTime(0L);
        this.a(RobotState.RUNNING);
        this.b = new Thread(new EventLoopManager.b((EventLoopManager.SyntheticClass_1)null), "Event Loop");
        this.b.start();
    }

    private void b() {
        this.b.interrupt();

        try {
            Thread.sleep(200L);
        } catch (InterruptedException var2) {
            ;
        }

        this.a(RobotState.STOPPED);
        this.g = a;
        this.k.clear();
    }

    private void a(RobotState var1) {
        this.state = var1;
        RobotLog.v("EventLoopManager state is " + var1.toString());
        if(this.j != null) {
            this.j.onStateChange(var1);
        }

    }

    private void a(RobocolDatagram var1) throws RobotCoreException {
        Gamepad var2 = new Gamepad();
        var2.fromByteArray(var1.getData());
        if(var2.user >= 1 && var2.user <= 2) {
            int var3 = var2.user - 1;
            this.h[var3] = var2;
            if(this.h[0].id == this.h[1].id) {
                RobotLog.v("Gamepad moved position, removing stale gamepad");
                if(var3 == 0) {
                    this.h[1] = new Gamepad();
                }

                if(var3 == 1) {
                    this.h[0] = new Gamepad();
                }
            }

        } else {
            RobotLog.d("Gamepad with user %d received. Only users 1 and 2 are valid");
        }
    }

    private void b(RobocolDatagram var1) throws RobotCoreException {
        Heartbeat var2 = new Heartbeat(Token.EMPTY);
        var2.fromByteArray(var1.getData());
        var2.setRobotState(this.state);
        var1.setData(var2.toByteArray());
        this.d.send(var1);
        this.f.reset();
        this.i = var2;
    }

    private void c(RobocolDatagram var1) throws RobotCoreException {
        if(!var1.getAddress().equals(this.o)) {
            if(this.state == RobotState.DROPPED_CONNECTION) {
                this.a(RobotState.RUNNING);
            }

            if(this.g != a) {
                this.o = var1.getAddress();
                RobotLog.i("new remote peer discovered: " + this.o.getHostAddress());

                try {
                    this.d.connect(this.o);
                } catch (SocketException var4) {
                    RobotLog.e("Unable to connect to peer:" + var4.toString());
                }

                PeerDiscovery var2 = new PeerDiscovery(PeerType.PEER);
                RobotLog.v("Sending peer discovery packet");
                RobocolDatagram var3 = new RobocolDatagram(var2);
                if(this.d.getInetAddress() == null) {
                    var3.setAddress(this.o);
                }

                this.d.send(var3);
            }
        }
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

            for(int var5 = 0; var5 < var4; ++var5) {
                Command var6 = var3[var5];
                if(var6 != null && var6.equals(var2)) {
                    return;
                }
            }

            this.l[this.m++ % this.l.length] = var2;

            try {
                this.g.processCommand(var2);
            } catch (Exception var7) {
                RobotLog.e("Event loop threw an exception while processing a command");
                RobotLog.logStacktrace(var7);
            }

        }
    }

    private void c() {
    }

    private void e(RobocolDatagram var1) {
        RobotLog.w("RobotCore event loop received unknown event type: " + var1.getMsgType().name());
    }

    public void buildAndSendTelemetry(String tag, String msg) {
        Telemetry var3 = new Telemetry();
        var3.setTag(tag);
        var3.addData(tag, msg);
        this.sendTelemetryData(var3);
    }

    public static enum State {
        NOT_STARTED,
        INIT,
        RUNNING,
        STOPPED,
        EMERGENCY_STOP,
        DROPPED_CONNECTION;

        private State() {
        }
    }

    private static class a implements EventLoop {
        private a() {
        }

        public void init(EventLoopManager eventProcessor) {
        }

        public void loop() {
        }

        public void teardown() {
        }

        public void processCommand(Command command) {
            RobotLog.w("Dropping command " + command.getName() + ", no active event loop");
        }

        public OpModeManager getOpModeManager() {
            return null;
        }
    }

    private class b implements Runnable {
        private b() {
        }

        public void run() {
            RobotLog.v("EventLoopRunnable has started");

            try {
                ElapsedTime var1 = new ElapsedTime();
                double var2 = 0.001D;
                long var4 = 5L;

                while(!Thread.interrupted()) {
                    while(var1.time() < 0.001D) {
                        Thread.sleep(5L);
                    }

                    var1.reset();
                    if(RobotLog.hasGlobalErrorMsg()) {
                        EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
                    }

                    if(EventLoopManager.this.f.startTime() == 0.0D) {
                        Thread.sleep(500L);
                    } else if(EventLoopManager.this.f.time() > 2.0D) {
                        EventLoopManager.this.handleDroppedConnection();
                        EventLoopManager.this.o = null;
                        EventLoopManager.this.f = new ElapsedTime(0L);
                    }

                    Iterator var6 = EventLoopManager.this.k.iterator();

                    SyncdDevice var7;
                    while(var6.hasNext()) {
                        var7 = (SyncdDevice)var6.next();
                        var7.blockUntilReady();
                    }

                    boolean var16 = false;

                    try {
                        var16 = true;
                        EventLoopManager.this.g.loop();
                        var16 = false;
                    } catch (Exception var17) {
                        RobotLog.e("Event loop threw an exception");
                        RobotLog.logStacktrace(var17);
                        String var22 = var17.getMessage();
                        RobotLog.setGlobalErrorMsg("User code threw an uncaught exception: " + var22);
                        EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
                        throw new RobotCoreException("EventLoop Exception in loop()");
                    } finally {
                        if(var16) {
                            Iterator var9 = EventLoopManager.this.k.iterator();

                            while(var9.hasNext()) {
                                SyncdDevice var10 = (SyncdDevice)var9.next();
                                var10.startBlockingWork();
                            }

                        }
                    }

                    var6 = EventLoopManager.this.k.iterator();

                    while(var6.hasNext()) {
                        var7 = (SyncdDevice)var6.next();
                        var7.startBlockingWork();
                    }
                }
            } catch (InterruptedException var20) {
                RobotLog.v("EventLoopRunnable interrupted");
                EventLoopManager.this.a(RobotState.STOPPED);
            } catch (RobotCoreException var21) {
                RobotLog.v("RobotCoreException in EventLoopManager: " + var21.getMessage());
                EventLoopManager.this.a(RobotState.EMERGENCY_STOP);
                EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
            }

            try {
                EventLoopManager.this.g.teardown();
            } catch (Exception var18) {
                RobotLog.w("Caught exception during looper teardown: " + var18.toString());
                RobotLog.logStacktrace(var18);
                if(RobotLog.hasGlobalErrorMsg()) {
                    EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
                }
            }

            RobotLog.v("EventLoopRunnable has exited");
        }
    }

    private class c implements Runnable {
        ElapsedTime a;

        private c() {
            this.a = new ElapsedTime();
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
                        switch(EventLoopManager.SyntheticClass_1.a[var1.getMsgType().ordinal()]) {
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
                            EventLoopManager.this.c();
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

        public void run() {
            while(!Thread.interrupted()) {
                Iterator var1 = EventLoopManager.this.n.iterator();

                while(var1.hasNext()) {
                    Command var2 = (Command)var1.next();
                    if(var2.getAttempts() > 10) {
                        RobotLog.w("Failed to send command, too many attempts: " + var2.toString());
                        this.b.add(var2);
                    } else if(var2.isAcknowledged()) {
                        RobotLog.v("Command " + var2.getName() + " has been acknowledged by remote device");
                        this.b.add(var2);
                    } else {
                        try {
                            RobotLog.v("Sending command: " + var2.getName() + ", attempt " + var2.getAttempts());
                            EventLoopManager.this.d.send(new RobocolDatagram(var2.toByteArray()));
                        } catch (RobotCoreException var5) {
                            RobotLog.w("Failed to send command " + var2.getName());
                            RobotLog.logStacktrace(var5);
                        }
                    }
                }

                EventLoopManager.this.n.removeAll(this.b);
                this.b.clear();

                try {
                    Thread.sleep(100L);
                } catch (InterruptedException var4) {
                    return;
                }
            }

        }
    }

    public interface EventLoopMonitor {
        void onStateChange(RobotState var1);
    }
}
