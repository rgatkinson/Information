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
    private Thread eventLoopThread;
    private Thread scheduledSendsThread;
    private final RobocolDatagramSocket socket;
    private boolean receivingProhibited;
    private ElapsedTime elapsedSinceHeartbeatReceived;
    private EventLoop eventLoop;
    private final Gamepad[] gamepads;
    private Heartbeat heartbeat;
    private EventLoopManager.EventLoopMonitor eventLoopMonitor;
    private final Set<SyncdDevice> syncdDevices;
    private final Command[] commandsReceived;
    private int m;
    private final Set<Command> commandsToSend;
    private InetAddress currentPeerAddressAndPort;

    public void handleDroppedConnection() {
        OpModeManager var1 = this.eventLoop.getOpModeManager();
        String var2 = "Lost connection while running op mode: " + var1.getActiveOpModeName();
        this.resetGamepads();
        var1.initActiveOpMode("Stop Robot");
        this.reportRobotStatus(RobotState.DROPPED_CONNECTION);
        RobotLog.i(var2);
    }

    public EventLoopManager(RobocolDatagramSocket socket) {
        this.state = RobotState.NOT_STARTED;
        this.eventLoopThread = new Thread();
        this.scheduledSendsThread = new Thread();
        this.receivingProhibited = false;
        this.elapsedSinceHeartbeatReceived = new ElapsedTime();
        this.eventLoop = nullEventLoop;
        this.gamepads = new Gamepad[]{new Gamepad(), new Gamepad()};
        this.heartbeat = new Heartbeat(Token.EMPTY);
        this.eventLoopMonitor = null;
        this.syncdDevices = new CopyOnWriteArraySet();
        this.commandsReceived = new Command[8];
        this.m = 0;
        this.commandsToSend = new CopyOnWriteArraySet();
        this.socket = socket;
        this.reportRobotStatus(RobotState.NOT_STARTED);
    }

    public void setMonitor(EventLoopManager.EventLoopMonitor monitor) {
        this.eventLoopMonitor = monitor;
    }

    public void start(EventLoop eventLoop) throws RobotCoreException {
        this.receivingProhibited = false;
        this.setEventLoop(eventLoop);
        this.scheduledSendsThread = new Thread(new ScheduledSendsRunnable((EventLoopManager.SyntheticClass_1)null), "Scheduled Sends");
        this.scheduledSendsThread.start();

        // DANGER DANGER Does this thread terminate reliably?
        (new Thread(new ReceiveRunnable((EventLoopManager.SyntheticClass_1)null))).start();
    }

    public void shutdown() {
        this.socket.close();
        this.scheduledSendsThread.interrupt();
        this.receivingProhibited = true;
        this.stopEventLoopThread();
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

        this.stopEventLoopThread();
        this.eventLoop = eventLoop;
        this.startEventLoopThread();
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

    public void resetGamepads() {
        Gamepad[] var1 = this.gamepads;
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Gamepad var4 = var1[var3];
            var4.reset();
        }

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
        this.commandsToSend.add(command);
    }

    private void startEventLoopThread() throws RobotCoreException {
        try {
            this.reportRobotStatus(RobotState.INIT);
            this.eventLoop.init(this);
            Iterator var1 = this.syncdDevices.iterator();

            while(var1.hasNext()) {
                SyncdDevice var2 = (SyncdDevice)var1.next();
                var2.startBlockingWork();
            }
        } catch (Exception var3) {
            RobotLog.w("Caught exception during looper init: " + var3.toString());
            RobotLog.logStacktrace(var3);
            this.reportRobotStatus(RobotState.EMERGENCY_STOP);
            if(RobotLog.hasGlobalErrorMsg()) {
                this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
            }

            throw new RobotCoreException("Robot failed to start: " + var3.getMessage());
        }

        this.elapsedSinceHeartbeatReceived = new ElapsedTime(0L);
        this.reportRobotStatus(RobotState.RUNNING);
        this.eventLoopThread = new Thread(new EventLoopRunnable((EventLoopManager.SyntheticClass_1)null), "Event Loop");
        this.eventLoopThread.start();
    }

    private void stopEventLoopThread() {
        this.eventLoopThread.interrupt();

        try {
            Thread.sleep(200L);
        } catch (InterruptedException var2) {
            ;
        }

        this.reportRobotStatus(RobotState.STOPPED);
        this.eventLoop = nullEventLoop;
        this.syncdDevices.clear();
    }

    private void reportRobotStatus(RobotState var1) {
        this.state = var1;
        RobotLog.v("EventLoopManager state is " + var1.toString());
        if(this.eventLoopMonitor != null) {
            this.eventLoopMonitor.onStateChange(var1);
        }

    }

    private void onGamepadDatagram(RobocolDatagram datagram) throws RobotCoreException {
        Gamepad var2 = new Gamepad();
        var2.fromByteArray(datagram.getData());
        if(var2.user >= 1 && var2.user <= 2) {
            int var3 = var2.user - 1;
            this.gamepads[var3].copy(var2);
            if(this.gamepads[0].id == this.gamepads[1].id) {
                RobotLog.v("Gamepad moved position, removing stale gamepad");
                if(var3 == 0) {
                    this.gamepads[1].copy(new Gamepad());
                }

                if(var3 == 1) {
                    this.gamepads[0].copy(new Gamepad());
                }
            }

        } else {
            RobotLog.d("Gamepad with user %d received. Only users 1 and 2 are valid");
        }
    }

    private void onHeartbeatDatagramReceived(RobocolDatagram datagram) throws RobotCoreException {
        Heartbeat incomingHeartbeat = new Heartbeat(Token.EMPTY);
        incomingHeartbeat.fromByteArray(datagram.getData());
        incomingHeartbeat.setRobotState(this.state);
        datagram.setData(incomingHeartbeat.toByteArray());
        this.socket.send(datagram);
        this.elapsedSinceHeartbeatReceived.reset();
        this.heartbeat = incomingHeartbeat;
    }

    private void onConnectionDatagram(RobocolDatagram datagram) throws RobotCoreException {
        if (!datagram.getAddress().equals(this.currentPeerAddressAndPort)) {
            if(this.state == RobotState.DROPPED_CONNECTION) {
                this.reportRobotStatus(RobotState.RUNNING);
            }

            if (this.eventLoop != nullEventLoop) {
                this.currentPeerAddressAndPort = datagram.getAddress();
                RobotLog.i("new remote peer discovered: " + this.currentPeerAddressAndPort.getHostAddress());

                try {
                    this.socket.connect(this.currentPeerAddressAndPort);
                } catch (SocketException var4) {
                    RobotLog.e("Unable to connect to peer:" + var4.toString());
                }

                PeerDiscovery peerDiscovery = new PeerDiscovery(PeerType.PEER);
                RobotLog.v("Sending peer discovery packet");
                RobocolDatagram dgPeerDiscovery = new RobocolDatagram(peerDiscovery);
                if(this.socket.getInetAddress() == null) {
                    dgPeerDiscovery.setAddress(this.currentPeerAddressAndPort);
                }

                this.socket.send(dgPeerDiscovery);
            }
        }
    }

    private void onCommandDatagram(RobocolDatagram datagram) throws RobotCoreException {
        Command command = new Command(datagram.getData());
        if(command.isAcknowledged()) {
            this.commandsToSend.remove(command);
        } else {
            command.acknowledge();
            this.socket.send(new RobocolDatagram(command));
            Command[] var3 = this.commandsReceived;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Command var6 = var3[var5];
                if(var6 != null && var6.equals(command)) {
                    return;
                }
            }

            this.commandsReceived[this.m++ % this.commandsReceived.length] = command;

            try {
                this.eventLoop.processCommand(command);
            } catch (Exception var7) {
                RobotLog.e("Event loop threw an exception while processing a command");
                RobotLog.logStacktrace(var7);
            }

        }
    }

    private void onEmptyDatagram() {
    }

    private void onUnknownDatagram(RobocolDatagram var1) {
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

    private class EventLoopRunnable implements Runnable {
        private EventLoopRunnable() {
        }

        public void run() {
            RobotLog.v("EventLoopRunnable has started");

            try {
                ElapsedTime elapsedEventLoop = new ElapsedTime();
                double sMinLoopInterval = 0.001D;
                long msLoopIntervalStep = 5L;

                while (!Thread.interrupted()) {
                    while (elapsedEventLoop.time() < sMinLoopInterval) {
                        Thread.sleep(msLoopIntervalStep);
                    }

                    elapsedEventLoop.reset();
                    if(RobotLog.hasGlobalErrorMsg()) {
                        EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
                    }

                    if(EventLoopManager.this.elapsedSinceHeartbeatReceived.startTime() == 0.0D) {
                        Thread.sleep(500L);
                    } else if(EventLoopManager.this.elapsedSinceHeartbeatReceived.time() > 2.0D) {
                        EventLoopManager.this.handleDroppedConnection();
                        EventLoopManager.this.currentPeerAddressAndPort = null;
                        EventLoopManager.this.elapsedSinceHeartbeatReceived = new ElapsedTime(0L);
                    }

                    Iterator syncdDeviceIterator = EventLoopManager.this.syncdDevices.iterator();
                    SyncdDevice syncdDevice;
                    while(syncdDeviceIterator.hasNext()) {
                        syncdDevice = (SyncdDevice)syncdDeviceIterator.next();
                        syncdDevice.blockUntilReady();
                    }

                    boolean unblockOnException = false;

                    try {
                        unblockOnException = true;
                        EventLoopManager.this.eventLoop.loop();
                        unblockOnException = false;
                    } catch (Exception e) {
                        RobotLog.e("Event loop threw an exception");
                        RobotLog.logStacktrace(e);
                        String exceptionMessage = e.getClass().getSimpleName() + (e.getMessage() != null?" - " + e.getMessage():"");
                        RobotLog.setGlobalErrorMsg("User code threw an uncaught exception: " + exceptionMessage);
                        EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
                        throw new RobotCoreException("EventLoop Exception in loop()");
                    } finally {
                        if(unblockOnException) {
                            Iterator var9 = EventLoopManager.this.syncdDevices.iterator();

                            while(var9.hasNext()) {
                                SyncdDevice var10 = (SyncdDevice)var9.next();
                                var10.startBlockingWork();
                            }
                        }
                    }

                    syncdDeviceIterator = EventLoopManager.this.syncdDevices.iterator();

                    while(syncdDeviceIterator.hasNext()) {
                        syncdDevice = (SyncdDevice)syncdDeviceIterator.next();
                        syncdDevice.startBlockingWork();
                    }
                }
            } catch (InterruptedException var20) {
                RobotLog.v("EventLoopRunnable interrupted");
                EventLoopManager.this.reportRobotStatus(RobotState.STOPPED);
            } catch (RobotCoreException var21) {
                RobotLog.v("RobotCoreException in EventLoopManager: " + var21.getMessage());
                EventLoopManager.this.reportRobotStatus(RobotState.EMERGENCY_STOP);
                EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
            }

            try {
                EventLoopManager.this.eventLoop.teardown();
            } catch (Exception var17) {
                RobotLog.w("Caught exception during looper teardown: " + var17.toString());
                RobotLog.logStacktrace(var17);
                if(RobotLog.hasGlobalErrorMsg()) {
                    EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
                }
            }

            RobotLog.v("EventLoopRunnable has exited");
        }
    }

    /** Does this shutdown reliably? */
    private class ReceiveRunnable implements Runnable {
        ElapsedTime elapsed;

        private ReceiveRunnable() {
            this.elapsed = new ElapsedTime();
        }

        public void run() {
            while(true) {
                RobocolDatagram datagram = EventLoopManager.this.socket.recv();     // what's the timeout?
                if(EventLoopManager.this.receivingProhibited || EventLoopManager.this.socket.isClosed()) {
                    return;
                }

                if (datagram == null) {
                    Thread.yield();
                } else {
                    if(RobotLog.hasGlobalErrorMsg()) {
                        EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
                    }

                    try {
                        switch(EventLoopManager.SyntheticClass_1.a[datagram.getMsgType().ordinal()]) {
                        case 1:
                            EventLoopManager.this.onGamepadDatagram(datagram);
                            break;
                        case 2:
                            EventLoopManager.this.onHeartbeatDatagramReceived(datagram);
                            break;
                        case 3:
                            EventLoopManager.this.onConnectionDatagram(datagram);
                            break;
                        case 4:
                            EventLoopManager.this.onCommandDatagram(datagram);
                            break;
                        case 5:
                            EventLoopManager.this.onEmptyDatagram();
                            break;
                        default:
                            EventLoopManager.this.onUnknownDatagram(datagram);
                        }
                    } catch (RobotCoreException var3) {
                        RobotLog.w("RobotCore event loop cannot process event: " + var3.toString());
                    }
                }
            }
        }
    }

    private class ScheduledSendsRunnable implements Runnable {
        private Set<Command> completedCommands;

        private ScheduledSendsRunnable() {
            this.completedCommands = new HashSet();
        }

        public void run() {
            while(!Thread.interrupted()) {
                Iterator iterator = EventLoopManager.this.commandsToSend.iterator();

                while(iterator.hasNext()) {
                    Command command = (Command)iterator.next();
                    if(command.getAttempts() > 10) {
                        RobotLog.w("Failed to send command, too many attempts: " + command.toString());
                        this.completedCommands.add(command);
                    } else if(command.isAcknowledged()) {
                        RobotLog.v("Command " + command.getName() + " has been acknowledged by remote device");
                        this.completedCommands.add(command);
                    } else {
                        try {
                            RobotLog.v("Sending command: " + command.getName() + ", attempt " + command.getAttempts());
                            EventLoopManager.this.socket.send(new RobocolDatagram(command.toByteArray()));
                        } catch (RobotCoreException var5) {
                            RobotLog.w("Failed to send command " + command.getName());
                            RobotLog.logStacktrace(var5);
                        }
                    }
                }

                EventLoopManager.this.commandsToSend.removeAll(this.completedCommands);
                this.completedCommands.clear();

                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    return;
                }
            }

        }
    }

    public interface EventLoopMonitor {
        void onStateChange(RobotState var1);
    }
}
