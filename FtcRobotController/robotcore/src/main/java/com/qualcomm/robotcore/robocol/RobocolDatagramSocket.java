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
    private final DatagramPacket c;
    private final RobocolDatagram d;
    private DatagramSocket b;
    private volatile State e;

    public RobocolDatagramSocket() {
        this.c = new DatagramPacket(this.a, this.a.length);
        this.d = new RobocolDatagram();
        this.e = State.CLOSED;
    }

    public void bind(InetSocketAddress var1) throws SocketException {
        if (this.e != State.CLOSED) {
            this.close();
        }

        this.e = State.LISTENING;
        RobotLog.d("RobocolDatagramSocket binding to " + var1.toString());
        this.b = new DatagramSocket(var1);
    }

    public void close() {
        this.e = State.CLOSED;
        if (this.b != null) {
            this.b.close();
        }

        RobotLog.d("RobocolDatagramSocket is closed");
    }

    public void connect(InetAddress var1) throws SocketException {
        InetSocketAddress var2 = new InetSocketAddress(var1, 20884);
        RobotLog.d("RobocolDatagramSocket connected to " + var2.toString());
        this.b.connect(var2);
    }

    public InetAddress getInetAddress() {
        return this.b == null ? null : this.b.getInetAddress();
    }

    public InetAddress getLocalAddress() {
        return this.b == null ? null : this.b.getLocalAddress();
    }

    public State getState() {
        return this.e;
    }

    public boolean isClosed() {
        return this.e == State.CLOSED;
    }

    public boolean isRunning() {
        return this.e == State.LISTENING;
    }

    public void listen(InetAddress var1) throws SocketException {
        this.bind(new InetSocketAddress(RobocolConfig.determineBindAddress(var1), 20884));
    }

    public RobocolDatagram recv() {
        try {
            this.b.receive(this.c);
        } catch (PortUnreachableException var4) {
            RobotLog.d("RobocolDatagramSocket receive error: remote port unreachable");
            return null;
        } catch (IOException var5) {
            RobotLog.d("RobocolDatagramSocket receive error: " + var5.toString());
            return null;
        } catch (NullPointerException var6) {
            RobotLog.d("RobocolDatagramSocket receive error: " + var6.toString());
        }

        this.d.setPacket(this.c);
        return this.d;
    }

    public void send(RobocolDatagram var1) {
        try {
            this.b.send(var1.getPacket());
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

    public enum State {
        CLOSED,
        ERROR,
        LISTENING;

        static {
            State[] var0 = new State[]{LISTENING, CLOSED, ERROR};
        }
    }
}
