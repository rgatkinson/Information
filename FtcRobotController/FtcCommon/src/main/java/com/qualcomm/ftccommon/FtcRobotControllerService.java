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
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant.Event;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant.WifiDirectAssistantCallback;

import java.lang.Thread.State;
import java.net.InetAddress;

public class FtcRobotControllerService extends Service implements WifiDirectAssistantCallback {
    private final IBinder a = new FtcRobotControllerBinder();
    private final FtcRobotControllerService.a h;
    private final ElapsedTime i;
    private WifiDirectAssistant b;
    private Robot c;
    private EventLoop d;
    private Event e;
    private String f;
    private Callback g;
    private Thread j;

    public FtcRobotControllerService() {
        this.e = Event.DISCONNECTED;
        this.f = "Robot Status: null";
        this.g = null;
        this.h = new a();
        this.i = new ElapsedTime();
        this.j = null;
    }

    public WifiDirectAssistant getWifiDirectAssistant() {
        return this.b;
    }

    public Event getWifiDirectStatus() {
        return this.e;
    }

    public String getRobotStatus() {
        return this.f;
    }

    public IBinder onBind(Intent intent) {
        DbgLog.msg("Starting FTC Controller Service");
        DbgLog.msg("Android device is " + Build.MANUFACTURER + ", " + Build.MODEL);
        this.b = WifiDirectAssistant.getWifiDirectAssistant(this);
        this.b.setCallback(this);
        this.b.enable();
        if(Build.MODEL.equals("FL7007")) {
            this.b.discoverPeers();
        } else {
            this.b.createGroup();
        }

        return this.a;
    }

    public boolean onUnbind(Intent intent) {
        DbgLog.msg("Stopping FTC Controller Service");
        this.b.disable();
        this.shutdownRobot();
        return false;
    }

    public synchronized void setCallback(Callback callback) {
        this.g = callback;
    }

    public synchronized void setupRobot(EventLoop eventLoop) {
        if(this.j != null && this.j.isAlive()) {
            DbgLog.msg("FtcRobotControllerService.setupRobot() is currently running, stopping old setup");
            this.j.interrupt();

            while(this.j.isAlive()) {
                Thread.yield();
            }

            DbgLog.msg("Old setup stopped; restarting setup");
        }

        RobotLog.clearGlobalErrorMsg();
        DbgLog.msg("Processing robot setup");
        this.d = eventLoop;
        this.j = new Thread(new b(), "Robot Setup");
        this.j.start();

        while(this.j.getState() == State.NEW) {
            Thread.yield();
        }

    }

    public synchronized void shutdownRobot() {
        if(this.j != null && this.j.isAlive()) {
            this.j.interrupt();
        }

        if(this.c != null) {
            this.c.shutdown();
        }

        this.c = null;
        this.a("Robot Status: null");
    }

    public void onWifiDirectEvent(Event event) {
        switch (event.ordinal()) {
        case 1:
            DbgLog.msg("Wifi Direct - Group Owner");
            this.b.cancelDiscoverPeers();
            break;
        case 2:
            DbgLog.error("Wifi Direct - connected as peer, was expecting Group Owner");
            break;
        case 3:
            DbgLog.msg("Wifi Direct Passphrase: " + this.b.getPassphrase());
            break;
        case 4:
            DbgLog.error("Wifi Direct Error: " + this.b.getFailureReason());
        }

        this.a(event);
    }

    private void a(Event var1) {
        this.e = var1;
        if(this.g != null) {
            this.g.wifiDirectUpdate(this.e);
        }

    }

    private void a(String var1) {
        this.f = var1;
        if(this.g != null) {
            this.g.robotUpdate(var1);
        }

    }

    private class b implements Runnable {
        private b() {
        }

        public void run() {
            try {
                if(FtcRobotControllerService.this.c != null) {
                    FtcRobotControllerService.this.c.shutdown();
                    FtcRobotControllerService.this.c = null;
                }

                FtcRobotControllerService.this.a("Robot Status: scanning for USB devices");

                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException var4) {
                    FtcRobotControllerService.this.a("Robot Status: abort due to interrupt");
                    return;
                }

                FtcRobotControllerService.this.c = RobotFactory.createRobot();
                FtcRobotControllerService.this.a("Robot Status: waiting on network");
                FtcRobotControllerService.this.i.reset();

                while(!FtcRobotControllerService.this.b.isConnected()) {
                    try {
                        Thread.sleep(1000L);
                        if(FtcRobotControllerService.this.i.time() > 120.0D) {
                            FtcRobotControllerService.this.a("Robot Status: network timed out");
                            return;
                        }
                    } catch (InterruptedException var3) {
                        DbgLog.msg("interrupt waiting for network; aborting setup");
                        return;
                    }
                }

                FtcRobotControllerService.this.a("Robot Status: starting robot");

                try {
                    FtcRobotControllerService.this.c.eventLoopManager.setMonitor(FtcRobotControllerService.this.h);
                    InetAddress var1 = FtcRobotControllerService.this.b.getGroupOwnerAddress();
                    FtcRobotControllerService.this.c.start(var1, FtcRobotControllerService.this.d);
                } catch (RobotCoreException var2) {
                    FtcRobotControllerService.this.a("Robot Status: failed to start robot");
                    RobotLog.setGlobalErrorMsg(var2.getMessage());
                }
            } catch (RobotCoreException var5) {
                FtcRobotControllerService.this.a("Robot Status: Unable to create robot!");
                RobotLog.setGlobalErrorMsg(var5.getMessage());
            }

        }
    }

    private class a implements EventLoopMonitor {
        private a() {
        }

        public void onStateChange(RobotState state) {
            if(FtcRobotControllerService.this.g != null) {
                switch (state.ordinal()) {
                case 1:
                    FtcRobotControllerService.this.g.robotUpdate("Robot Status: init");
                    break;
                case 2:
                    FtcRobotControllerService.this.g.robotUpdate("Robot Status: not started");
                    break;
                case 3:
                    FtcRobotControllerService.this.g.robotUpdate("Robot Status: running");
                    break;
                case 4:
                    FtcRobotControllerService.this.g.robotUpdate("Robot Status: stopped");
                    break;
                case 5:
                    FtcRobotControllerService.this.g.robotUpdate("Robot Status: EMERGENCY STOP");
                    break;
                case 6:
                    FtcRobotControllerService.this.g.robotUpdate("Robot Status: dropped connection");
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
