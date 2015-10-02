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
    private static final EventLoop trivialEventLoop = new TrivialEventLoop(null);
    public EventLoopManager.State state;
    private Thread eventLoopRunnableThread;
    private Thread sendDataThread;
    private final RobocolDatagramSocket socket;
    private boolean e;
    private ElapsedTime elapsed;
    private EventLoop eventLoop;
    private final Gamepad[] gamepads;
    private Heartbeat heartbeat;
    private EventLoopManager.EventLoopMonitor eventLoopMonitor;
    private final Set<SyncdDevice> syncdDevices;
    private final Command[] processedCommands;
    private int numProcessedCommands;
    private final Set<Command> commandsReceivedPendingAck;
    private InetAddress inetAddress;

    public void handleDroppedConnection() {
        OpModeManager var1 = this.eventLoop.getOpModeManager();
        String var2 = "Lost connection while running op mode: " + var1.getActiveOpModeName();
        var1.initActiveOpMode("Stop Robot");
        this.onStateChange(EventLoopManager.State.DROPPED_CONNECTION);
        RobotLog.i(var2);
    }

    public EventLoopManager(RobocolDatagramSocket socket) {
        this.state = EventLoopManager.State.NOT_STARTED;
        this.eventLoopRunnableThread = new Thread();
        this.sendDataThread = new Thread();
        this.e = false;
        this.elapsed = new ElapsedTime();
        this.eventLoop = trivialEventLoop;
        this.gamepads = new Gamepad[]{new Gamepad(), new Gamepad()};
        this.heartbeat = new Heartbeat(Token.EMPTY);
        this.eventLoopMonitor = null;
        this.syncdDevices = new CopyOnWriteArraySet();
        this.processedCommands = new Command[8];
        this.numProcessedCommands = 0;
        this.commandsReceivedPendingAck = new CopyOnWriteArraySet();
        this.socket = socket;
        this.onStateChange(EventLoopManager.State.NOT_STARTED);
    }

    public void setMonitor(EventLoopManager.EventLoopMonitor monitor) {
        this.eventLoopMonitor = monitor;
    }

    public void start(EventLoop eventLoop) throws RobotCoreException {
        this.e = false;
        this.sendDataThread = new Thread(new SendDataLoop());
        this.sendDataThread.start();
        (new Thread(new ReceiveDriverStationMessageLoop())).start();
        this.setEventLoop(eventLoop);
    }

    public void shutdown() {
        this.socket.close();
        this.sendDataThread.interrupt();
        this.e = true;
        this.stopEventLoopRunnable();
    }

    public void registerSyncdDevice(SyncdDevice device) {
        this.syncdDevices.add(device);
    }

    public void unregisterSyncdDevice(SyncdDevice device) {
        this.syncdDevices.remove(device);
    }

    public void setEventLoop(EventLoop eventLoop) throws RobotCoreException {
        if(eventLoop == null) {
            eventLoop = trivialEventLoop;
            RobotLog.d("Event loop cannot be null, using empty event loop");
        }

        this.stopEventLoopRunnable();
        this.eventLoop = eventLoop;
        this.startEventLoopRunnable();
    }

    public EventLoop getEventLoop() {
        return this.eventLoop;
    }

    public Gamepad getGamepad() {
        return this.getGamepad(0);
    }

    public Gamepad getGamepad(int port) {
        Range.throwIfRangeIsInvalid((double) port, 0.0D, 1.0D);
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
        this.commandsReceivedPendingAck.add(command);
    }

    private void startEventLoopRunnable() throws RobotCoreException {
        try {
            this.onStateChange(EventLoopManager.State.INIT);
            this.eventLoop.init(this);
            Iterator var1 = this.syncdDevices.iterator();

            while(var1.hasNext()) {
                SyncdDevice var2 = (SyncdDevice)var1.next();
                var2.startBlockingWork();
            }
        } catch (Exception var3) {
            RobotLog.w("Caught exception during looper init: " + var3.toString());
            RobotLog.logStacktrace(var3);
            this.onStateChange(EventLoopManager.State.EMERGENCY_STOP);
            if(RobotLog.hasGlobalErrorMsg()) {
                this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
            }

            throw new RobotCoreException("Robot failed to start: " + var3.getMessage());
        }

        this.elapsed = new ElapsedTime(0L);
        this.onStateChange(EventLoopManager.State.RUNNING);
        this.eventLoopRunnableThread = new Thread(new EventLoopRunnable());
        this.eventLoopRunnableThread.start();
    }

    private void stopEventLoopRunnable() {
        this.eventLoopRunnableThread.interrupt();

        try {
            Thread.sleep(200L);
        } catch (InterruptedException var2) {
            ;
        }

        this.onStateChange(EventLoopManager.State.STOPPED);
        this.c();
        this.eventLoop = trivialEventLoop;
        this.syncdDevices.clear();
    }

    private void c() {
        try {
            this.eventLoop.teardown();
        } catch (Exception var2) {
            RobotLog.w("Caught exception during looper teardown: " + var2.toString());
            RobotLog.logStacktrace(var2);
            if(RobotLog.hasGlobalErrorMsg()) {
                this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
            }
        }

    }

    private void onStateChange(EventLoopManager.State newState) {
        this.state = newState;
        RobotLog.v("EventLoopManager state is " + newState.toString());
        if(this.eventLoopMonitor != null) {
            this.eventLoopMonitor.onStateChange(newState);
        }

    }

    private void onUserInputDatagramReceived(RobocolDatagram datagram) throws RobotCoreException {
        Gamepad gamepad = new Gamepad();
        gamepad.fromByteArray(datagram.getData());
        if(gamepad.user >= 1 && gamepad.user <= 2) {
            int iGamePad = gamepad.user - 1;
            this.gamepads[iGamePad] = gamepad;
            if(this.gamepads[0].id == this.gamepads[1].id) {
                RobotLog.v("Gamepad moved position, removing stale gamepad");
                if(iGamePad == 0) {
                    this.gamepads[1] = new Gamepad();
                }

                if(iGamePad == 1) {
                    this.gamepads[0] = new Gamepad();
                }
            }

        } else {
            RobotLog.d("Gamepad with user %d received. Only users 1 and 2 are valid");
        }
    }

    private void onHeartbeatDatagramReceived(RobocolDatagram datagram) throws RobotCoreException {
        this.socket.send(datagram);
        Heartbeat var2 = new Heartbeat(Token.EMPTY);
        var2.fromByteArray(datagram.getData());
        this.elapsed.reset();
        this.heartbeat = var2;
    }

    private void c(RobocolDatagram var1) throws RobotCoreException {
        if(!var1.getAddress().equals(this.inetAddress)) {
            if(this.state == EventLoopManager.State.DROPPED_CONNECTION) {
                this.onStateChange(EventLoopManager.State.RUNNING);
            }

            if(this.eventLoop != trivialEventLoop) {
                this.inetAddress = var1.getAddress();
                RobotLog.i("new remote peer discovered: " + this.inetAddress.getHostAddress());

                try {
                    this.socket.connect(this.inetAddress);
                } catch (SocketException var4) {
                    RobotLog.log("Unable to connect to peer:" + var4.toString());
                }

                PeerDiscovery var2 = new PeerDiscovery(PeerType.PEER);
                RobotLog.v("Sending peer discovery packet");
                RobocolDatagram var3 = new RobocolDatagram(var2);
                if(this.socket.getInetAddress() == null) {
                    var3.setAddress(this.inetAddress);
                }

                this.socket.send(var3);
            }
        }
    }

    private void onCommandReceived(RobocolDatagram datagram) throws RobotCoreException {
        Command command = new Command(datagram.getData());
        if(command.isAcknowledged()) {
            this.commandsReceivedPendingAck.remove(command);
        } else {
            command.acknowledge();
            this.socket.send(new RobocolDatagram(command));
            Command[] commands = this.processedCommands;
            int nCommands = commands.length;

            for(int i = 0; i < nCommands; ++i) {
                Command command1 = commands[i];
                if(command1 != null && command1.equals(command)) {
                    return;
                }
            }

            this.processedCommands[this.numProcessedCommands++ % this.processedCommands.length] = command;

            try {
                this.eventLoop.processCommand(command);
            } catch (Exception e) {
                RobotLog.log("Event loop threw an exception while processing a command");
                RobotLog.logStacktrace(e);
            }

        }
    }

    private void d() {
    }

    private void unknownDatagramReceived(RobocolDatagram var1) {
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

    private static class TrivialEventLoop implements EventLoop {
        private TrivialEventLoop() {
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
                ElapsedTime elapsed = new ElapsedTime();
                double var2 = 0.001D;
                long var4 = 5L;

                while(!Thread.interrupted()) {
                    while(elapsed.time() < 0.001D) {
                        Thread.sleep(5L);
                    }

                    elapsed.reset();
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

                    Iterator iterator1 = EventLoopManager.this.syncdDevices.iterator();

                    SyncdDevice syncdDevice1;
                    while(iterator1.hasNext()) {
                        syncdDevice1 = (SyncdDevice)iterator1.next();
                        syncdDevice1.blockUntilReady();
                    }

                    boolean inUserEventLoop = false;

                    try {
                        inUserEventLoop = true;
                        //
                        EventLoopManager.this.eventLoop.loop();
                        //
                        inUserEventLoop = false;
                    } catch (Exception e) {
                        RobotLog.log("Event loop threw an exception");
                        RobotLog.logStacktrace(e);
                        String var20 = e.getMessage();
                        RobotLog.setGlobalErrorMsg("User code threw an uncaught exception: " + var20);
                        EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
                        throw new RobotCoreException("EventLoop Exception in loop()");
                    } finally {
                        if (inUserEventLoop) {
                            Iterator iterator = EventLoopManager.this.syncdDevices.iterator();

                            while(iterator.hasNext()) {
                                SyncdDevice syncdDevice = (SyncdDevice)iterator.next();
                                syncdDevice.startBlockingWork();
                            }

                        }
                    }

                    iterator1 = EventLoopManager.this.syncdDevices.iterator();

                    while(iterator1.hasNext()) {
                        syncdDevice1 = (SyncdDevice)iterator1.next();
                        syncdDevice1.startBlockingWork();
                    }
                }
            } catch (InterruptedException var18) {
                RobotLog.v("EventLoopRunnable interrupted");
            } catch (RobotCoreException var19) {
                RobotLog.v("RobotCoreException in EventLoopManager: " + var19.getMessage());
                EventLoopManager.this.onStateChange(EventLoopManager.State.EMERGENCY_STOP);
                EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
            }

            EventLoopManager.this.c();
            RobotLog.v("EventLoopRunnable has exited");
        }
    }

    private class ReceiveDriverStationMessageLoop implements Runnable {
        ElapsedTime a;

        private ReceiveDriverStationMessageLoop() {
            this.a = new ElapsedTime();
        }

        public void run() {
            while(true) {
                RobocolDatagram datagram = EventLoopManager.this.socket.recv();
                if(EventLoopManager.this.e || EventLoopManager.this.socket.isClosed()) {
                    return;
                }

                if(datagram == null) {
                    Thread.yield();
                } else {
                    if(RobotLog.hasGlobalErrorMsg()) {
                        EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
                    }

                    try {
                        switch(datagram.getMsgType().ordinal()) {
                        case 1:
                            EventLoopManager.this.onUserInputDatagramReceived(datagram);
                            break;
                        case 2:
                            EventLoopManager.this.onHeartbeatDatagramReceived(datagram);
                            break;
                        case 3:
                            EventLoopManager.this.c(datagram);
                            break;
                        case 4:
                            EventLoopManager.this.onCommandReceived(datagram);
                            break;
                        case 5:
                            EventLoopManager.this.d();
                            break;
                        default:
                            EventLoopManager.this.unknownDatagramReceived(datagram);
                        }
                    } catch (RobotCoreException var3) {
                        RobotLog.w("RobotCore event loop cannot process event: " + var3.toString());
                    }
                }
            }
        }
    }

    private class SendDataLoop implements Runnable {
        private Set<Command> b;

        private SendDataLoop() {
            this.b = new HashSet();
        }

        public void run() {
            while(!Thread.interrupted()) {
                Iterator var1 = EventLoopManager.this.commandsReceivedPendingAck.iterator();

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

                EventLoopManager.this.commandsReceivedPendingAck.removeAll(this.b);
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
        void onStateChange(EventLoopManager.State var1);
    }
}
