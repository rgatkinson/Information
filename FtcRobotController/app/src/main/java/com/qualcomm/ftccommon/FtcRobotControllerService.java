//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.ftccommon;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import com.qualcomm.ftccommon.UpdateUI.Callback;
import com.qualcomm.robotcore.eventloop.EventLoop;
import com.qualcomm.robotcore.eventloop.EventLoopManager.EventLoopMonitor;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.factory.RobotFactory;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant.Event;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant.WifiDirectAssistantCallback;
import java.lang.Thread.State;
import java.net.InetAddress;

public class FtcRobotControllerService extends Service implements WifiDirectAssistantCallback {
    private final IBinder binder = new FtcRobotControllerService.FtcRobotControllerBinder();
    private WifiDirectAssistant wifiDirectAssistant;
    private Robot robot;
    private EventLoop eventLoop;
    private Event event;
    private String robotStatus;
    private Callback callback;
    private final MonitorRobotStatus monitorRobotStatus;
    private final ElapsedTime elapsed;
    private Thread runRobotThread;

    public FtcRobotControllerService() {
        this.event = Event.DISCONNECTED;
        this.robotStatus = "Robot Status: null";
        this.callback = null;
        this.monitorRobotStatus = new MonitorRobotStatus();
        this.elapsed = new ElapsedTime();
        this.runRobotThread = null;
    }

    public WifiDirectAssistant getWifiDirectAssistant() {
        return this.wifiDirectAssistant;
    }

    public Event getWifiDirectStatus() {
        return this.event;
    }

    public String getRobotStatus() {
        return this.robotStatus;
    }

    public IBinder onBind(Intent intent) {
        DbgLog.msg("Starting FTC Controller Service");
        DbgLog.msg("Android device is " + Build.MANUFACTURER + ", " + Build.MODEL);
        this.wifiDirectAssistant = WifiDirectAssistant.getWifiDirectAssistant(this);
        this.wifiDirectAssistant.setCallback(this);
        this.wifiDirectAssistant.enable();
        if(Build.MODEL.equals("FL7007")) {
            this.wifiDirectAssistant.discoverPeers();
        } else {
            this.wifiDirectAssistant.createGroup();
        }

        return this.binder;
    }

    public boolean onUnbind(Intent intent) {
        DbgLog.msg("Stopping FTC Controller Service");
        this.wifiDirectAssistant.disable();
        this.shutdownRobot();
        return false;
    }

    public synchronized void setCallback(Callback callback) {
        this.callback = callback;
    }

    public synchronized void setupRobot(EventLoop eventLoop) {
        if(this.runRobotThread != null && this.runRobotThread.isAlive()) {
            DbgLog.msg("FtcRobotControllerService.setupRobot() is currently running, stopping old setup");
            this.runRobotThread.interrupt();

            while(this.runRobotThread.isAlive()) {
                Thread.yield();
            }

            DbgLog.msg("Old setup stopped; restarting setup");
        }

        RobotLog.clearGlobalErrorMsg();
        DbgLog.msg("Processing robot setup");
        this.eventLoop = eventLoop;
        this.runRobotThread = new Thread(new RunRobotThread());
        this.runRobotThread.start();

        while(this.runRobotThread.getState() == State.NEW) {
            Thread.yield();
        }

    }

    public synchronized void shutdownRobot() {
        if(this.runRobotThread != null && this.runRobotThread.isAlive()) {
            this.runRobotThread.interrupt();
        }

        if(this.robot != null) {
            this.robot.shutdown();
        }

        this.robot = null;
        this.reportRobotStatus("Robot Status: null");
    }

    public void onWifiDirectEvent(Event event) {
        switch(event.ordinal()) {
        case 1:
            DbgLog.msg("Wifi Direct - Group Owner");
            this.wifiDirectAssistant.cancelDiscoverPeers();
            break;
        case 2:
            DbgLog.error("Wifi Direct - connected as peer, was expecting Group Owner");
            break;
        case 3:
            DbgLog.msg("Wifi Direct Passphrase: " + this.wifiDirectAssistant.getPassphrase());
            break;
        case 4:
            DbgLog.error("Wifi Direct Error: " + this.wifiDirectAssistant.getFailureReason());
        }

        this.a(event);
    }

    private void a(Event var1) {
        this.event = var1;
        if(this.callback != null) {
            this.callback.wifiDirectUpdate(this.event);
        }

    }

    private void reportRobotStatus(String var1) {
        this.robotStatus = var1;
        if(this.callback != null) {
            this.callback.robotUpdate(var1);
        }

    }

    private class RunRobotThread implements Runnable {
        private RunRobotThread() {
        }

        public void run() {
            try {
                if(FtcRobotControllerService.this.robot != null) {
                    FtcRobotControllerService.this.robot.shutdown();
                    FtcRobotControllerService.this.robot = null;
                }

                FtcRobotControllerService.this.reportRobotStatus("Robot Status: scanning for USB devices");

                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException var4) {
                    FtcRobotControllerService.this.reportRobotStatus("Robot Status: abort due to interrupt");
                    return;
                }

                FtcRobotControllerService.this.robot = RobotFactory.createRobot();
                FtcRobotControllerService.this.reportRobotStatus("Robot Status: waiting on network");
                FtcRobotControllerService.this.elapsed.reset();

                while(!FtcRobotControllerService.this.wifiDirectAssistant.isConnected()) {
                    try {
                        Thread.sleep(1000L);
                        if(FtcRobotControllerService.this.elapsed.time() > 120.0D) {
                            FtcRobotControllerService.this.reportRobotStatus("Robot Status: network timed out");
                            return;
                        }
                    } catch (InterruptedException var3) {
                        DbgLog.msg("interrupt waiting for network; aborting setup");
                        return;
                    }
                }

                FtcRobotControllerService.this.reportRobotStatus("Robot Status: starting robot");

                try {
                    FtcRobotControllerService.this.robot.eventLoopManager.setMonitor(FtcRobotControllerService.this.monitorRobotStatus);
                    InetAddress groupOwnerAddress = FtcRobotControllerService.this.wifiDirectAssistant.getGroupOwnerAddress();

                    FtcRobotControllerService.this.robot.start(groupOwnerAddress, FtcRobotControllerService.this.eventLoop);

                } catch (RobotCoreException e) {
                    FtcRobotControllerService.this.reportRobotStatus("Robot Status: failed to start robot");
                    RobotLog.setGlobalErrorMsg(e.getMessage());
                }
            } catch (RobotCoreException e) {
                FtcRobotControllerService.this.reportRobotStatus("Robot Status: Unable to create robot!");
                RobotLog.setGlobalErrorMsg(e.getMessage());
            }

        }
    }

    private class MonitorRobotStatus implements EventLoopMonitor {
        private MonitorRobotStatus() {
        }

        public void onStateChange(com.qualcomm.robotcore.eventloop.EventLoopManager.State state) {
            if(FtcRobotControllerService.this.callback != null) {
                switch(state.ordinal()) {
                case 1:
                    FtcRobotControllerService.this.callback.robotUpdate("Robot Status: init");
                    break;
                case 2:
                    FtcRobotControllerService.this.callback.robotUpdate("Robot Status: not started");
                    break;
                case 3:
                    FtcRobotControllerService.this.callback.robotUpdate("Robot Status: running");
                    break;
                case 4:
                    FtcRobotControllerService.this.callback.robotUpdate("Robot Status: stopped");
                    break;
                case 5:
                    FtcRobotControllerService.this.callback.robotUpdate("Robot Status: EMERGENCY STOP");
                    break;
                case 6:
                    FtcRobotControllerService.this.callback.robotUpdate("Robot Status: dropped connection");
                }

            }
        }
    }

    public class FtcRobotControllerBinder extends Binder {
        public FtcRobotControllerBinder() {
        }

        public FtcRobotControllerService getService() {
            return FtcRobotControllerService.this;
        }
    }
}
