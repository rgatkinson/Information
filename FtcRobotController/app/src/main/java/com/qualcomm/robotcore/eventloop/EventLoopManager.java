package com.qualcomm.robotcore.eventloop;

import java.util.Collection;
import java.util.HashSet;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.robocol.Telemetry;
import java.net.SocketException;
import com.qualcomm.robotcore.robocol.RobocolParsable;
import com.qualcomm.robotcore.robocol.PeerDiscovery;
import com.qualcomm.robotcore.robocol.RobocolDatagram;
import java.util.Iterator;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.concurrent.CopyOnWriteArraySet;
import java.net.InetAddress;
import com.qualcomm.robotcore.robocol.Command;
import java.util.Set;
import com.qualcomm.robotcore.robocol.Heartbeat;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;

public class EventLoopManager
{
    public static final String RC_BATTERY_LEVEL_KEY = "RobotController Battery Level";
    public static final String ROBOT_BATTERY_LEVEL_KEY = "Robot Battery Level";
    public static final String SYSTEM_TELEMETRY = "SYSTEM_TELEMETRY";
    private static final EventLoop a;
    private Thread b;
    private Thread c;
    private final RobocolDatagramSocket d;
    private boolean e;
    private ElapsedTime f;
    private EventLoop g;
    private final Gamepad[] h;
    private Heartbeat i;
    private EventLoopMonitor j;
    private final Set<SyncdDevice> k;
    private final Command[] l;
    private int m;
    private final Set<Command> n;
    private InetAddress o;
    public State state;
    
    static {
        a = new a();
    }
    
    public EventLoopManager(final RobocolDatagramSocket d) {
        this.state = State.NOT_STARTED;
        this.b = new Thread();
        this.c = new Thread();
        this.e = false;
        this.f = new ElapsedTime();
        this.g = EventLoopManager.a;
        this.h = new Gamepad[] { new Gamepad(), new Gamepad() };
        this.i = new Heartbeat(Heartbeat.Token.EMPTY);
        this.j = null;
        this.k = new CopyOnWriteArraySet<SyncdDevice>();
        this.l = new Command[8];
        this.m = 0;
        this.n = new CopyOnWriteArraySet<Command>();
        this.d = d;
        this.a(State.NOT_STARTED);
    }
    
    private void a() throws RobotCoreException {
        try {
            this.a(State.INIT);
            this.g.init(this);
            final Iterator<SyncdDevice> iterator = this.k.iterator();
            while (iterator.hasNext()) {
                iterator.next().startBlockingWork();
            }
        }
        catch (Exception ex) {
            RobotLog.w("Caught exception during looper init: " + ex.toString());
            RobotLog.logStacktrace(ex);
            this.a(State.EMERGENCY_STOP);
            if (RobotLog.hasGlobalErrorMsg()) {
                this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
            }
            throw new RobotCoreException("Robot failed to start: " + ex.getMessage());
        }
        this.f = new ElapsedTime(0L);
        this.a(State.RUNNING);
        (this.b = new Thread(new b())).start();
    }
    
    private void a(final State state) {
        this.state = state;
        RobotLog.v("EventLoopManager state is " + state.toString());
        if (this.j != null) {
            this.j.onStateChange(state);
        }
    }
    
    private void a(final RobocolDatagram robocolDatagram) throws RobotCoreException {
        final Gamepad gamepad = new Gamepad();
        gamepad.fromByteArray(robocolDatagram.getData());
        if (gamepad.user < 1 || gamepad.user > 2) {
            RobotLog.d("Gamepad with user %d received. Only users 1 and 2 are valid");
        }
        else {
            final byte b = (byte)(-1 + gamepad.user);
            this.h[b] = gamepad;
            if (this.h[0].id == this.h[1].id) {
                RobotLog.v("Gamepad moved position, removing stale gamepad");
                if (b == 0) {
                    this.h[1] = new Gamepad();
                }
                if (b == 1) {
                    this.h[0] = new Gamepad();
                }
            }
        }
    }
    
    private void b() {
        this.b.interrupt();
        while (true) {
            try {
                Thread.sleep(200L);
                this.a(State.STOPPED);
                this.c();
                this.g = EventLoopManager.a;
                this.k.clear();
            }
            catch (InterruptedException ex) {
                continue;
            }
            break;
        }
    }
    
    private void b(final RobocolDatagram robocolDatagram) throws RobotCoreException {
        this.d.send(robocolDatagram);
        final Heartbeat i = new Heartbeat(Heartbeat.Token.EMPTY);
        i.fromByteArray(robocolDatagram.getData());
        this.f.reset();
        this.i = i;
    }
    
    private void c() {
        try {
            this.g.teardown();
        }
        catch (Exception ex) {
            RobotLog.w("Caught exception during looper teardown: " + ex.toString());
            RobotLog.logStacktrace(ex);
            if (RobotLog.hasGlobalErrorMsg()) {
                this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
            }
        }
    }
    
    private void c(final RobocolDatagram robocolDatagram) throws RobotCoreException {
        if (!robocolDatagram.getAddress().equals(this.o)) {
            if (this.state == State.DROPPED_CONNECTION) {
                this.a(State.RUNNING);
            }
            if (this.g != EventLoopManager.a) {
                this.o = robocolDatagram.getAddress();
                RobotLog.i("new remote peer discovered: " + this.o.getHostAddress());
                while (true) {
                    try {
                        this.d.connect(this.o);
                        final PeerDiscovery peerDiscovery = new PeerDiscovery(PeerDiscovery.PeerType.PEER);
                        RobotLog.v("Sending peer discovery packet");
                        final RobocolDatagram robocolDatagram2 = new RobocolDatagram(peerDiscovery);
                        if (this.d.getInetAddress() == null) {
                            robocolDatagram2.setAddress(this.o);
                        }
                        this.d.send(robocolDatagram2);
                    }
                    catch (SocketException ex) {
                        RobotLog.e("Unable to connect to peer:" + ex.toString());
                        continue;
                    }
                    break;
                }
            }
        }
    }
    
    private void d() {
    }
    
    private void d(final RobocolDatagram robocolDatagram) throws RobotCoreException {
        final Command command = new Command(robocolDatagram.getData());
        if (command.isAcknowledged()) {
            this.n.remove(command);
        }
        else {
            command.acknowledge();
            this.d.send(new RobocolDatagram(command));
            for (final Command command2 : this.l) {
                if (command2 != null && command2.equals(command)) {
                    return;
                }
            }
            this.l[this.m++ % this.l.length] = command;
            try {
                this.g.processCommand(command);
            }
            catch (Exception ex) {
                RobotLog.e("Event loop threw an exception while processing a command");
                RobotLog.logStacktrace(ex);
            }
        }
    }
    
    private void e(final RobocolDatagram robocolDatagram) {
        RobotLog.w("RobotCore event loop received unknown event type: " + robocolDatagram.getMsgType().name());
    }
    
    public void buildAndSendTelemetry(final String tag, final String s) {
        final Telemetry telemetry = new Telemetry();
        telemetry.setTag(tag);
        telemetry.addData(tag, s);
        this.sendTelemetryData(telemetry);
    }
    
    public EventLoop getEventLoop() {
        return this.g;
    }
    
    public Gamepad getGamepad() {
        return this.getGamepad(0);
    }
    
    public Gamepad getGamepad(final int n) {
        Range.throwIfRangeIsInvalid(n, 0.0, 1.0);
        return this.h[n];
    }
    
    public Gamepad[] getGamepads() {
        return this.h;
    }
    
    public Heartbeat getHeartbeat() {
        return this.i;
    }
    
    public void handleDroppedConnection() {
        final OpModeManager opModeManager = this.g.getOpModeManager();
        final String string = "Lost connection while running op mode: " + opModeManager.getActiveOpModeName();
        opModeManager.initActiveOpMode("Stop Robot");
        this.a(State.DROPPED_CONNECTION);
        RobotLog.i(string);
    }
    
    public void registerSyncdDevice(final SyncdDevice syncdDevice) {
        this.k.add(syncdDevice);
    }
    
    public void sendCommand(final Command command) {
        this.n.add(command);
    }
    
    public void sendTelemetryData(final Telemetry telemetry) {
        while (true) {
            try {
                this.d.send(new RobocolDatagram(telemetry.toByteArray()));
                telemetry.clearData();
            }
            catch (RobotCoreException ex) {
                RobotLog.w("Failed to send telemetry data");
                RobotLog.logStacktrace(ex);
                continue;
            }
            break;
        }
    }
    
    public void setEventLoop(EventLoop a) throws RobotCoreException {
        if (a == null) {
            a = EventLoopManager.a;
            RobotLog.d("Event loop cannot be null, using empty event loop");
        }
        this.b();
        this.g = a;
        this.a();
    }
    
    public void setMonitor(final EventLoopMonitor j) {
        this.j = j;
    }
    
    public void shutdown() {
        this.d.close();
        this.c.interrupt();
        this.e = true;
        this.b();
    }
    
    public void start(final EventLoop eventLoop) throws RobotCoreException {
        this.e = false;
        (this.c = new Thread(new d())).start();
        new Thread(new c()).start();
        this.setEventLoop(eventLoop);
    }
    
    public void unregisterSyncdDevice(final SyncdDevice syncdDevice) {
        this.k.remove(syncdDevice);
    }
    
    public interface EventLoopMonitor
    {
        void onStateChange(State p0);
    }
    
    public enum State
    {
        DROPPED_CONNECTION, 
        EMERGENCY_STOP, 
        INIT, 
        NOT_STARTED, 
        RUNNING, 
        STOPPED;
    }
    
    private static class a implements EventLoop
    {
        @Override
        public OpModeManager getOpModeManager() {
            return null;
        }
        
        @Override
        public void init(final EventLoopManager eventLoopManager) {
        }
        
        @Override
        public void loop() {
        }
        
        @Override
        public void processCommand(final Command command) {
            RobotLog.w("Dropping command " + command.getName() + ", no active event loop");
        }
        
        @Override
        public void teardown() {
        }
    }
    
    private class b implements Runnable
    {
        @Override
        public void run() {
            RobotLog.v("EventLoopRunnable has started");
            while (true) {
                try {
                    final ElapsedTime elapsedTime = new ElapsedTime();
                    if (!Thread.interrupted()) {
                        while (elapsedTime.time() < 0.001) {
                            Thread.sleep(5L);
                        }
                        goto Label_0058;
                    }
                    goto Label_0045;
                }
                catch (InterruptedException ex3) {
                    RobotLog.v("EventLoopRunnable interrupted");
                }
                catch (RobotCoreException ex) {
                    RobotLog.v("RobotCoreException in EventLoopManager: " + ex.getMessage());
                    EventLoopManager.this.a(State.EMERGENCY_STOP);
                    EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
                    goto Label_0045;
                }
                if (EventLoopManager.this.f.time() > 2.0) {
                    EventLoopManager.this.handleDroppedConnection();
                    EventLoopManager.this.o = null;
                    EventLoopManager.this.f = new ElapsedTime(0L);
                    goto Label_0101;
                }
                goto Label_0101;
                try {
                    EventLoopManager.this.g.loop();
                    final Iterator<SyncdDevice> iterator = EventLoopManager.this.k.iterator();
                    while (iterator.hasNext()) {
                        iterator.next().startBlockingWork();
                    }
                    continue;
                }
                catch (Exception ex2) {
                    RobotLog.e("Event loop threw an exception");
                    RobotLog.logStacktrace(ex2);
                    RobotLog.setGlobalErrorMsg("User code threw an uncaught exception: " + ex2.getMessage());
                    EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
                    throw new RobotCoreException("EventLoop Exception in loop()");
                }
                finally {
                    final Iterator<SyncdDevice> iterator2 = EventLoopManager.this.k.iterator();
                    while (iterator2.hasNext()) {
                        iterator2.next().startBlockingWork();
                    }
                }
                break;
            }
        }
    }
    
    private class c implements Runnable
    {
        ElapsedTime a;
        
        private c() {
            this.a = new ElapsedTime();
        }
        
        @Override
        public void run() {
            while (true) {
                final RobocolDatagram recv = EventLoopManager.this.d.recv();
                if (EventLoopManager.this.e || EventLoopManager.this.d.isClosed()) {
                    break;
                }
                if (recv == null) {
                    Thread.yield();
                }
                else {
                    if (RobotLog.hasGlobalErrorMsg()) {
                        EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
                    }
                    Label_0192: {
                        Label_0181: {
                            Label_0170: {
                                Label_0159: {
                                    Label_0148: {
                                        try {
                                            switch (EventLoopManager$1.a[recv.getMsgType().ordinal()]) {
                                                default: {
                                                    EventLoopManager.this.e(recv);
                                                    continue;
                                                }
                                                case 1: {
                                                    break Label_0148;
                                                }
                                                case 2: {
                                                    break Label_0159;
                                                }
                                                case 3: {
                                                    break Label_0170;
                                                }
                                                case 4: {
                                                    break Label_0181;
                                                }
                                                case 5: {
                                                    break Label_0192;
                                                }
                                            }
                                        }
                                        catch (RobotCoreException ex) {
                                            RobotLog.w("RobotCore event loop cannot process event: " + ex.toString());
                                        }
                                        continue;
                                    }
                                    EventLoopManager.this.a(recv);
                                    continue;
                                }
                                EventLoopManager.this.b(recv);
                                continue;
                            }
                            EventLoopManager.this.c(recv);
                            continue;
                        }
                        EventLoopManager.this.d(recv);
                        continue;
                    }
                    EventLoopManager.this.d();
                }
            }
        }
    }
    
    private class d implements Runnable
    {
        private Set<Command> b;
        
        private d() {
            this.b = new HashSet<Command>();
        }
        
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                for (final Command command : EventLoopManager.this.n) {
                    if (command.getAttempts() > 10) {
                        RobotLog.w("Failed to send command, too many attempts: " + command.toString());
                        this.b.add(command);
                    }
                    else if (command.isAcknowledged()) {
                        RobotLog.v("Command " + command.getName() + " has been acknowledged by remote device");
                        this.b.add(command);
                    }
                    else {
                        try {
                            RobotLog.v("Sending command: " + command.getName() + ", attempt " + command.getAttempts());
                            EventLoopManager.this.d.send(new RobocolDatagram(command.toByteArray()));
                        }
                        catch (RobotCoreException ex) {
                            RobotLog.w("Failed to send command " + command.getName());
                            RobotLog.logStacktrace(ex);
                        }
                    }
                }
                EventLoopManager.this.n.removeAll(this.b);
                this.b.clear();
                try {
                    Thread.sleep(100L);
                    continue;
                }
                catch (InterruptedException ex2) {}
                break;
            }
        }
    }
}
