package com.qualcomm.ftccommon;

import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import java.util.Map;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;
import java.util.Iterator;
import java.math.RoundingMode;
import java.math.BigDecimal;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.HardwareFactory;
import com.qualcomm.robotcore.util.ElapsedTime;
import android.content.Context;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.util.BatteryChecker;

public class FtcEventLoopHandler implements BatteryWatcher
{
    public static final String NO_VOLTAGE_SENSOR = "no voltage sensor found";
    EventLoopManager a;
    BatteryChecker b;
    Context c;
    ElapsedTime d;
    double e;
    UpdateUI.Callback f;
    HardwareFactory g;
    HardwareMap h;
    
    public FtcEventLoopHandler(final HardwareFactory g, final UpdateUI.Callback f, final Context c) {
        this.d = new ElapsedTime();
        this.e = 0.25;
        this.h = new HardwareMap();
        this.g = g;
        this.f = f;
        this.c = c;
        (this.b = new BatteryChecker(c, (BatteryChecker.BatteryWatcher)this, 180000L)).startBatteryMonitoring();
    }
    
    private void a() {
        this.sendTelemetry("RobotController Battery Level", String.valueOf(this.b.getBatteryLevel()));
    }
    
    private void b() {
        final Iterator<VoltageSensor> iterator = this.h.voltageSensor.iterator();
        double n = Double.MAX_VALUE;
        while (iterator.hasNext()) {
            final VoltageSensor voltageSensor = iterator.next();
            double voltage;
            if (voltageSensor.getVoltage() < n) {
                voltage = voltageSensor.getVoltage();
            }
            else {
                voltage = n;
            }
            n = voltage;
        }
        String value;
        if (this.h.voltageSensor.size() == 0) {
            value = "no voltage sensor found";
        }
        else {
            value = String.valueOf(new BigDecimal(n).setScale(2, RoundingMode.HALF_UP).doubleValue());
        }
        this.sendTelemetry("Robot Battery Level", value);
    }
    
    public void displayGamePadInfo(final String s) {
        this.f.updateUi(s, this.a.getGamepads());
    }
    
    public Gamepad[] getGamepads() {
        return this.a.getGamepads();
    }
    
    public HardwareMap getHardwareMap() throws RobotCoreException, InterruptedException {
        return this.h = this.g.createHardwareMap(this.a);
    }
    
    public String getOpMode(String s) {
        if (this.a.state != EventLoopManager.State.RUNNING) {
            s = "Stop Robot";
        }
        return s;
    }
    
    public void init(final EventLoopManager a) {
        this.a = a;
    }
    
    public void restartRobot() {
        this.b.endBatteryMonitoring();
        this.f.restartRobot();
    }
    
    public void sendBatteryInfo() {
        this.a();
        this.b();
    }
    
    public void sendCommand(final Command command) {
        this.a.sendCommand(command);
    }
    
    public void sendTelemetry(final String tag, final String s) {
        final Telemetry telemetry = new Telemetry();
        telemetry.setTag(tag);
        telemetry.addData(tag, s);
        if (this.a != null) {
            this.a.sendTelemetryData(telemetry);
            telemetry.clearData();
        }
    }
    
    public void sendTelemetryData(final Telemetry telemetry) {
        if (this.d.time() > this.e) {
            this.d.reset();
            if (telemetry.hasData()) {
                this.a.sendTelemetryData(telemetry);
            }
            telemetry.clearData();
        }
    }
    
    public void shutdownCoreInterfaceDeviceModules() {
        for (final Map.Entry<String, DeviceInterfaceModule> entry : this.h.deviceInterfaceModule.entrySet()) {
            final String s = entry.getKey();
            final DeviceInterfaceModule deviceInterfaceModule = entry.getValue();
            DbgLog.msg("Stopping Core Interface Device Module " + s);
            deviceInterfaceModule.close();
        }
    }
    
    public void shutdownLegacyModules() {
        for (final Map.Entry<String, LegacyModule> entry : this.h.legacyModule.entrySet()) {
            final String s = entry.getKey();
            final LegacyModule legacyModule = entry.getValue();
            DbgLog.msg("Stopping Legacy Module " + s);
            legacyModule.close();
        }
    }
    
    public void shutdownMotorControllers() {
        for (final Map.Entry<String, DcMotorController> entry : this.h.dcMotorController.entrySet()) {
            final String s = entry.getKey();
            final DcMotorController dcMotorController = entry.getValue();
            DbgLog.msg("Stopping DC Motor Controller " + s);
            dcMotorController.close();
        }
    }
    
    public void shutdownServoControllers() {
        for (final Map.Entry<String, ServoController> entry : this.h.servoController.entrySet()) {
            final String s = entry.getKey();
            final ServoController servoController = entry.getValue();
            DbgLog.msg("Stopping Servo Controller " + s);
            servoController.close();
        }
    }
    
    @Override
    public void updateBatteryLevel(final float n) {
        this.sendTelemetry("RobotController Battery Level", String.valueOf(n));
    }
}
