package com.qualcomm.ftccommon;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.factory.RobotFactory;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import android.os.Binder;
import com.qualcomm.robotcore.util.RobotLog;
import android.content.Context;
import android.os.Build;
import android.content.Intent;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.EventLoop;
import com.qualcomm.robotcore.robot.Robot;
import android.os.IBinder;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
import android.app.Service;

public class FtcRobotControllerService extends Service implements WifiDirectAssistantCallback
{
    private final IBinder a;
    private WifiDirectAssistant b;
    private Robot c;
    private EventLoop d;
    private Event e;
    private String f;
    private UpdateUI.Callback g;
    private final a h;
    private final ElapsedTime i;
    private Thread j;
    
    public FtcRobotControllerService() {
        this.a = (IBinder)new FtcRobotControllerBinder();
        this.e = Event.DISCONNECTED;
        this.f = "Robot Status: null";
        this.g = null;
        this.h = new a();
        this.i = new ElapsedTime();
        this.j = null;
    }
    
    private void a(final Event e) {
        this.e = e;
        if (this.g != null) {
            this.g.wifiDirectUpdate(this.e);
        }
    }
    
    private void a(final String f) {
        this.f = f;
        if (this.g != null) {
            this.g.robotUpdate(f);
        }
    }
    
    public String getRobotStatus() {
        return this.f;
    }
    
    public WifiDirectAssistant getWifiDirectAssistant() {
        return this.b;
    }
    
    public Event getWifiDirectStatus() {
        return this.e;
    }
    
    public IBinder onBind(final Intent intent) {
        DbgLog.msg("Starting FTC Controller Service");
        DbgLog.msg("Android device is " + Build.MANUFACTURER + ", " + Build.MODEL);
        (this.b = WifiDirectAssistant.getWifiDirectAssistant((Context)this)).setCallback((WifiDirectAssistant.WifiDirectAssistantCallback)this);
        this.b.enable();
        if (Build.MODEL.equals("FL7007")) {
            this.b.discoverPeers();
        }
        else {
            this.b.createGroup();
        }
        return this.a;
    }
    
    public boolean onUnbind(final Intent intent) {
        DbgLog.msg("Stopping FTC Controller Service");
        this.b.disable();
        this.shutdownRobot();
        return false;
    }
    
    public void onWifiDirectEvent(final Event event) {
        switch (FtcRobotControllerService$1.b[event.ordinal()]) {
            case 1: {
                DbgLog.msg("Wifi Direct - Group Owner");
                this.b.cancelDiscoverPeers();
                break;
            }
            case 2: {
                DbgLog.error("Wifi Direct - connected as peer, was expecting Group Owner");
                break;
            }
            case 3: {
                DbgLog.msg("Wifi Direct Passphrase: " + this.b.getPassphrase());
                break;
            }
            case 4: {
                DbgLog.error("Wifi Direct Error: " + this.b.getFailureReason());
                break;
            }
        }
        this.a(event);
    }
    
    public void setCallback(final UpdateUI.Callback g) {
        synchronized (this) {
            this.g = g;
        }
    }
    
    public void setupRobot(final EventLoop d) {
        Label_0058: {
            synchronized (this) {
                if (this.j == null || !this.j.isAlive()) {
                    break Label_0058;
                }
                DbgLog.msg("FtcRobotControllerService.setupRobot() is currently running, stopping old setup");
                this.j.interrupt();
                while (this.j.isAlive()) {
                    Thread.yield();
                }
            }
            DbgLog.msg("Old setup stopped; restarting setup");
        }
        RobotLog.clearGlobalErrorMsg();
        DbgLog.msg("Processing robot setup");
        this.d = d;
        (this.j = new Thread(new b())).start();
        while (this.j.getState() == Thread.State.NEW) {
            Thread.yield();
        }
    }
    // monitorexit(this)
    
    public void shutdownRobot() {
        synchronized (this) {
            if (this.j != null && this.j.isAlive()) {
                this.j.interrupt();
            }
            if (this.c != null) {
                this.c.shutdown();
            }
            this.c = null;
            this.a("Robot Status: null");
        }
    }
    
    public class FtcRobotControllerBinder extends Binder
    {
        public FtcRobotControllerService getService() {
            return FtcRobotControllerService.this;
        }
    }
    
    private class a implements EventLoopMonitor
    {
        @Override
        public void onStateChange(final State state) {
            if (FtcRobotControllerService.this.g == null) {
                return;
            }
            switch (FtcRobotControllerService$1.a[state.ordinal()]) {
                default: {}
                case 1: {
                    FtcRobotControllerService.this.g.robotUpdate("Robot Status: init");
                }
                case 2: {
                    FtcRobotControllerService.this.g.robotUpdate("Robot Status: not started");
                }
                case 3: {
                    FtcRobotControllerService.this.g.robotUpdate("Robot Status: running");
                }
                case 4: {
                    FtcRobotControllerService.this.g.robotUpdate("Robot Status: stopped");
                }
                case 5: {
                    FtcRobotControllerService.this.g.robotUpdate("Robot Status: EMERGENCY STOP");
                }
                case 6: {
                    FtcRobotControllerService.this.g.robotUpdate("Robot Status: dropped connection");
                }
            }
        }
    }
    
    private class b implements Runnable
    {
        final /* synthetic */ FtcRobotControllerService a;
        
        @Override
        public void run() {
        Label_0161:
            while (true) {
                try {
                    if (FtcRobotControllerService.this.c != null) {
                        FtcRobotControllerService.this.c.shutdown();
                        FtcRobotControllerService.this.c = null;
                    }
                    FtcRobotControllerService.this.a("Robot Status: scanning for USB devices");
                    try {
                        Thread.sleep(2000L);
                        FtcRobotControllerService.this.c = RobotFactory.createRobot();
                        FtcRobotControllerService.this.a("Robot Status: waiting on network");
                        FtcRobotControllerService.this.i.reset();
                        Block_8: {
                            while (!FtcRobotControllerService.this.b.isConnected()) {
                                final long n = 1000L;
                                Thread.sleep(n);
                                final b b = this;
                                final FtcRobotControllerService ftcRobotControllerService = b.a;
                                final ElapsedTime elapsedTime = ftcRobotControllerService.i;
                                final double n2 = elapsedTime.time();
                                final double n3 = 120.0;
                                final double n4 = dcmpl(n2, n3);
                                if (n4 > 0) {
                                    break Block_8;
                                }
                            }
                            break Label_0161;
                        }
                        final b b2 = this;
                        final FtcRobotControllerService ftcRobotControllerService2 = b2.a;
                        final String s = "Robot Status: network timed out";
                        ftcRobotControllerService2.a(s);
                        return;
                    }
                    catch (InterruptedException ex3) {
                        FtcRobotControllerService.this.a("Robot Status: abort due to interrupt");
                        return;
                    }
                }
                catch (RobotCoreException ex) {
                    FtcRobotControllerService.this.a("Robot Status: Unable to create robot!");
                    RobotLog.setGlobalErrorMsg(ex.getMessage());
                    return;
                }
                try {
                    final long n = 1000L;
                    Thread.sleep(n);
                    final b b = this;
                    final FtcRobotControllerService ftcRobotControllerService = b.a;
                    final ElapsedTime elapsedTime = ftcRobotControllerService.i;
                    final double n2 = elapsedTime.time();
                    final double n3 = 120.0;
                    final double n4 = dcmpl(n2, n3);
                    if (n4 > 0) {
                        final b b2 = this;
                        final FtcRobotControllerService ftcRobotControllerService2 = b2.a;
                        final String s = "Robot Status: network timed out";
                        ftcRobotControllerService2.a(s);
                        return;
                    }
                    continue;
                }
                catch (InterruptedException ex4) {
                    DbgLog.msg("interrupt waiting for network; aborting setup");
                    return;
                }
                break;
            }
            FtcRobotControllerService.this.a("Robot Status: starting robot");
            try {
                FtcRobotControllerService.this.c.eventLoopManager.setMonitor((EventLoopManager.EventLoopMonitor)FtcRobotControllerService.this.h);
                FtcRobotControllerService.this.c.start(FtcRobotControllerService.this.b.getGroupOwnerAddress(), FtcRobotControllerService.this.d);
            }
            catch (RobotCoreException ex2) {
                FtcRobotControllerService.this.a("Robot Status: failed to start robot");
                RobotLog.setGlobalErrorMsg(ex2.getMessage());
            }
        }
    }
}
