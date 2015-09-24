package com.qualcomm.ftccommon;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import java.util.Iterator;
import com.qualcomm.robotcore.util.Util;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.hardware.HardwareMap;
import android.content.Context;
import com.qualcomm.robotcore.hardware.HardwareFactory;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.EventLoop;

public class FtcEventLoop implements EventLoop
{
    FtcEventLoopHandler a;
    OpModeManager b;
    OpModeRegister c;
    
    public FtcEventLoop(final HardwareFactory hardwareFactory, final OpModeRegister c, final UpdateUI.Callback callback, final Context context) {
        this.b = new OpModeManager(new HardwareMap());
        this.a = new FtcEventLoopHandler(hardwareFactory, callback, context);
        this.c = c;
    }
    
    private void a() {
        this.a.restartRobot();
    }
    
    private void a(final String s) {
        final String opMode = this.a.getOpMode(s);
        this.b.initActiveOpMode(opMode);
        this.a.sendCommand(new Command("CMD_INIT_OP_MODE_RESP", opMode));
    }
    
    private void b() {
        String s = "";
        for (final String s2 : this.b.getOpModes()) {
            if (!s2.equals("Stop Robot")) {
                if (!s.isEmpty()) {
                    s += Util.ASCII_RECORD_SEPARATOR;
                }
                s += s2;
            }
        }
        this.a.sendCommand(new Command("CMD_REQUEST_OP_MODE_LIST_RESP", s));
    }
    
    private void c() {
        this.b.startActiveOpMode();
        this.a.sendCommand(new Command("CMD_RUN_OP_MODE_RESP", this.b.getActiveOpModeName()));
    }
    
    @Override
    public OpModeManager getOpModeManager() {
        return this.b;
    }
    
    @Override
    public void init(final EventLoopManager eventLoopManager) throws RobotCoreException, InterruptedException {
        DbgLog.msg("======= INIT START =======");
        this.b.registerOpModes(this.c);
        this.a.init(eventLoopManager);
        final HardwareMap hardwareMap = this.a.getHardwareMap();
        this.b.setHardwareMap(hardwareMap);
        hardwareMap.logDevices();
        DbgLog.msg("======= INIT FINISH =======");
    }
    
    @Override
    public void loop() throws RobotCoreException {
        this.a.displayGamePadInfo(this.b.getActiveOpModeName());
        this.b.runActiveOpMode(this.a.getGamepads());
        this.a.sendTelemetryData(this.b.getActiveOpMode().telemetry);
    }
    
    @Override
    public void processCommand(final Command command) {
        DbgLog.msg("Processing Command: " + command.getName() + " " + command.getExtra());
        this.a.sendBatteryInfo();
        final String name = command.getName();
        final String extra = command.getExtra();
        if (name.equals("CMD_RESTART_ROBOT")) {
            this.a();
            return;
        }
        if (name.equals("CMD_REQUEST_OP_MODE_LIST")) {
            this.b();
            return;
        }
        if (name.equals("CMD_INIT_OP_MODE")) {
            this.a(extra);
            return;
        }
        if (name.equals("CMD_RUN_OP_MODE")) {
            this.c();
            return;
        }
        DbgLog.msg("Unknown command: " + name);
    }
    
    @Override
    public void teardown() throws RobotCoreException {
        DbgLog.msg("======= TEARDOWN =======");
        this.b.stopActiveOpMode();
        this.a.shutdownMotorControllers();
        this.a.shutdownServoControllers();
        this.a.shutdownLegacyModules();
        this.a.shutdownCoreInterfaceDeviceModules();
        DbgLog.msg("======= TEARDOWN COMPLETE =======");
    }
}
