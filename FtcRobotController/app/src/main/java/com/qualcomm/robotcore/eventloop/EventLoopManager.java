//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.robotcore.eventloop;

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
    private static final EventLoop nullEventLoop = new NullEventLoop((EventLoopManager.SyntheticClass_1)null);
    public RobotState state;
    private Thread b;
    private Thread scheduledSendsThread;
    private final RobocolDatagramSocket socket;
    private boolean isShutDown;
    private ElapsedTime elapsed;
    private EventLoop eventLoop;
    private final Gamepad[] gamepads;
    private Heartbeat heartbeat;
    private EventLoopManager.EventLoopMonitor eventLoopMonitor;
    private final Set<SyncdDevice> syncdDevices;
    private final Command[] commands;
    private int m;
    private final Set<Command> n;
    private InetAddress inetAddress;

    public void handleDroppedConnection() {
        OpModeManager var1 = this.eventLoop.getOpModeManager();
        String var2 = "Lost connection while running op mode: " + var1.getActiveOpModeName();
        var1.initActiveOpMode("Stop Robot");
        this.reportRobotState(RobotState.DROPPED_CONNECTION);
        RobotLog.i(var2);
    }

    public EventLoopManager(RobocolDatagramSocket socket) {
        this.state = RobotState.NOT_STARTED;
        this.b = new Thread();
        this.scheduledSendsThread = new Thread();
        this.isShutDown = false;
        this.elapsed = new ElapsedTime();
        this.eventLoop = nullEventLoop;
        this.gamepads = new Gamepad[]{new Gamepad(), new Gamepad()};
        this.heartbeat = new Heartbeat(Token.EMPTY);
        this.eventLoopMonitor = null;
        this.syncdDevices = new CopyOnWriteArraySet();
        this.commands = new Command[8];
        this.m = 0;
        this.n = new CopyOnWriteArraySet();
        this.socket = socket;
        this.reportRobotState(RobotState.NOT_STARTED);
    }

    public void setMonitor(EventLoopManager.EventLoopMonitor monitor) {
        this.eventLoopMonitor = monitor;
    }

    public void start(EventLoop eventLoop) throws RobotCoreException {
        this.isShutDown = false;
        this.scheduledSendsThread = new Thread(new EventLoopManager.d((EventLoopManager.SyntheticClass_1)null), "Scheduled Sends");
        this.scheduledSendsThread.start();
        (new Thread(new EventLoopManager.c((EventLoopManager.SyntheticClass_1)null))).start();
        this.setEventLoop(eventLoop);
    }

    public void shutdown() {
        this.socket.close();
        this.scheduledSendsThread.interrupt();
        this.isShutDown = true;
        this.b();
    }

    public void registerSyncdDevice(SyncdDevice device) {
        this.syncdDevices.add(device);
    }

    public void unregisterSyncdDevice(SyncdDevice device) {
        this.syncdDevices.remove(device);
    }

    public void setEventLoop(EventLoop eventLoop) throws RobotCoreException {
        if(eventLoop == null) {
            eventLoop = nullEventLoop;
            RobotLog.d("Event loop cannot be null, using empty event loop");
        }

        this.b();
        this.eventLoop = eventLoop;
        this.a();
    }

    public EventLoop getEventLoop() {
        return this.eventLoop;
    }

    public Gamepad getGamepad() {
        return this.getGamepad(0);
    }

    public Gamepad getGamepad(int port) {
        Range.throwIfRangeIsInvalid((double)port, 0.0D, 1.0D);
        return this.gamepads[port];
    }

    public Gamepad[] getGamepads() {
        return this.gamepads;
    }

    public Heartbeat getHeartbeat() {
        return this.heartbeat;
    }

    public void sendTelemetryData(Telemetry telemetry) {
        try {
            this.socket.send(new RobocolDatagram(telemetry.toByteArray()));
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
            this.reportRobotState(RobotState.INIT);
            this.eventLoop.init(this);
            Iterator var1 = this.syncdDevices.iterator();

            while(var1.hasNext()) {
                SyncdDevice var2 = (SyncdDevice)var1.next();
                var2.startBlockingWork();
            }
        } catch (Exception var3) {
            RobotLog.w("Caught exception during looper init: " + var3.toString());
            RobotLog.logStacktrace(var3);
            this.reportRobotState(RobotState.EMERGENCY_STOP);
            if(RobotLog.hasGlobalErrorMsg()) {
                this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
            }

            throw new RobotCoreException("Robot failed to start: " + var3.getMessage());
        }

        this.elapsed = new ElapsedTime(0L);
        this.reportRobotState(RobotState.RUNNING);
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

        this.reportRobotState(RobotState.STOPPED);
        this.eventLoop = nullEventLoop;
        this.syncdDevices.clear();
    }

    private void reportRobotState(RobotState var1) {
        this.state = var1;
        RobotLog.v("EventLoopManager state is " + var1.toString());
        if(this.eventLoopMonitor != null) {
            this.eventLoopMonitor.onStateChange(var1);
        }

    }

    private void onGamePadPacket(RobocolDatagram datagram) throws RobotCoreException {
        Gamepad var2 = new Gamepad();
        var2.fromByteArray(datagram.getData());
        if(var2.user >= 1 && var2.user <= 2) {
            int var3 = var2.user - 1;
            this.gamepads[var3] = var2;
            if(this.gamepads[0].id == this.gamepads[1].id) {
                RobotLog.v("Gamepad moved position, removing stale gamepad");
                if(var3 == 0) {
                    this.gamepads[1] = new Gamepad();
                }

                if(var3 == 1) {
                    this.gamepads[0] = new Gamepad();
                }
            }

        } else {
            RobotLog.d("Gamepad with user %d received. Only users 1 and 2 are valid");
        }
    }

    private void onHeartbeatPacket(RobocolDatagram datagram) throws RobotCoreException {
        Heartbeat var2 = new Heartbeat(Token.EMPTY);
        var2.fromByteArray(datagram.getData());
        var2.setRobotState(this.state);
        datagram.setData(var2.toByteArray());
        this.socket.send(datagram);
        this.elapsed.reset();
        this.heartbeat = var2;
    }

    private void onPeerDiscoveryPacket(RobocolDatagram datagram) throws RobotCoreException {

        if (!datagram.getAddress().equals(this.inetAddress)) {

            if (this.state == RobotState.DROPPED_CONNECTION) {
                this.reportRobotState(RobotState.RUNNING);
            }

            if (this.eventLoop != nullEventLoop) {
                this.inetAddress = datagram.getAddress();
                RobotLog.i("new remote peer discovered: " + this.inetAddress.getHostAddress());

                try {
                    this.socket.connect(this.inetAddress);
                } catch (SocketException var4) {
                    RobotLog.e("Unable to connect to peer:" + var4.toString());
                }

                PeerDiscovery peerDiscovery = new PeerDiscovery(PeerType.PEER);
                RobotLog.v("Sending peer discovery packet");
                RobocolDatagram outboundDatagram = new RobocolDatagram(peerDiscovery);

                if(this.socket.getInetAddress() == null) {
                    outboundDatagram.setAddress(this.inetAddress);
                }

                this.socket.send(outboundDatagram);
            }
        }
    }

    private void onCommandPacket(RobocolDatagram datagram) throws RobotCoreException {
        Command command = new Command(datagram.getData());
        if(command.isAcknowledged()) {
            this.n.remove(command);
        } else {
            command.acknowledge();
            this.socket.send(new RobocolDatagram(command));
            Command[] var3 = this.commands;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Command var6 = var3[var5];
                if(var6 != null && var6.equals(command)) {
                    return;
                }
            }

            this.commands[this.m++ % this.commands.length] = command;

            try {
                this.eventLoop.processCommand(command);
            } catch (Exception var7) {
                RobotLog.e("Event loop threw an exception while processing a command");
                RobotLog.logStacktrace(var7);
            }

        }
    }

    private void onTelemetryPacket() {
    // ignore: those are for us to them, not them to us
    }

    private void onUnknownPacket(RobocolDatagram var1) {
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

    private static class NullEventLoop implements EventLoop {
        private NullEventLoop() {
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

                    if(EventLoopManager.this.elapsed.startTime() == 0.0D) {
                        Thread.sleep(500L);
                    } else if(EventLoopManager.this.elapsed.time() > 2.0D) {
                        EventLoopManager.this.handleDroppedConnection();
                        EventLoopManager.this.inetAddress = null;
                        EventLoopManager.this.elapsed = new ElapsedTime(0L);
                    }

                    Iterator var6 = EventLoopManager.this.syncdDevices.iterator();

                    SyncdDevice var7;
                    while(var6.hasNext()) {
                        var7 = (SyncdDevice)var6.next();
                        var7.blockUntilReady();
                    }

                    boolean var16 = false;

                    try {
                        var16 = true;
                        EventLoopManager.this.eventLoop.loop();
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
                            Iterator var9 = EventLoopManager.this.syncdDevices.iterator();

                            while(var9.hasNext()) {
                                SyncdDevice var10 = (SyncdDevice)var9.next();
                                var10.startBlockingWork();
                            }

                        }
                    }

                    var6 = EventLoopManager.this.syncdDevices.iterator();

                    while(var6.hasNext()) {
                        var7 = (SyncdDevice)var6.next();
                        var7.startBlockingWork();
                    }
                }
            } catch (InterruptedException var20) {
                RobotLog.v("EventLoopRunnable interrupted");
                EventLoopManager.this.reportRobotState(RobotState.STOPPED);
            } catch (RobotCoreException var21) {
                RobotLog.v("RobotCoreException in EventLoopManager: " + var21.getMessage());
                EventLoopManager.this.reportRobotState(RobotState.EMERGENCY_STOP);
                EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
            }

            try {
                EventLoopManager.this.eventLoop.teardown();
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
                RobocolDatagram var1 = EventLoopManager.this.socket.recv();
                if(EventLoopManager.this.isShutDown || EventLoopManager.this.socket.isClosed()) {
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
                            EventLoopManager.this.onGamePadPacket(var1);
                            break;
                        case 2:
                            EventLoopManager.this.onHeartbeatPacket(var1);
                            break;
                        case 3:
                            EventLoopManager.this.onPeerDiscoveryPacket(var1);
                            break;
                        case 4:
                            EventLoopManager.this.onCommandPacket(var1);
                            break;
                        case 5:
                            EventLoopManager.this.onTelemetryPacket();
                            break;
                        default:
                            EventLoopManager.this.onUnknownPacket(var1);
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
                            EventLoopManager.this.socket.send(new RobocolDatagram(var2.toByteArray()));
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
