package com.qualcomm.robotcore.robot;

import com.qualcomm.robotcore.eventloop.EventLoop;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.RobocolDatagram;
import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;
import com.qualcomm.robotcore.util.RobotLog;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;

public class Robot {
    public EventLoopManager eventLoopManager = null;
    public ArrayBlockingQueue<RobocolDatagram> eventQueue = null;
    public ArrayBlockingQueue<RobocolDatagram> sendQueue = null;
    public RobocolDatagramSocket socket = null;

    public void shutdown() {
        if (this.eventLoopManager != null) {
            this.eventLoopManager.shutdown();
        }

        if (this.socket != null) {
            this.socket.close();
        }

    }

    public void start(InetAddress var1, EventLoop var2) throws RobotCoreException {
        try {
            this.socket.listen(var1);
            this.eventLoopManager.start(var2);
        } catch (SocketException var4) {
            RobotLog.logStacktrace(var4);
            throw new RobotCoreException("Robot start failed: " + var4.toString());
        }
    }
}
