package com.qualcomm.robotcore.robot;

import java.net.SocketException;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.eventloop.EventLoop;
import java.net.InetAddress;
import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;
import com.qualcomm.robotcore.robocol.RobocolDatagram;
import java.util.concurrent.ArrayBlockingQueue;
import com.qualcomm.robotcore.eventloop.EventLoopManager;

public class Robot
{
    public EventLoopManager eventLoopManager;
    public ArrayBlockingQueue<RobocolDatagram> eventQueue;
    public ArrayBlockingQueue<RobocolDatagram> sendQueue;
    public RobocolDatagramSocket socket;
    
    public Robot() {
        this.eventLoopManager = null;
        this.socket = null;
        this.sendQueue = null;
        this.eventQueue = null;
    }
    
    public void shutdown() {
        if (this.eventLoopManager != null) {
            this.eventLoopManager.shutdown();
        }
        if (this.socket != null) {
            this.socket.close();
        }
    }
    
    public void start(final InetAddress inetAddress, final EventLoop eventLoop) throws RobotCoreException {
        try {
            this.socket.listen(inetAddress);
            this.eventLoopManager.start(eventLoop);
        }
        catch (SocketException ex) {
            RobotLog.logStacktrace(ex);
            throw new RobotCoreException("Robot start failed: " + ex.toString());
        }
    }
}
