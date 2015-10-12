//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.robotcore.eventloop.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.DcMotorController.DeviceMode;
import com.qualcomm.robotcore.hardware.DcMotorController.RunMode;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class OpModeManager {
    public static final String DEFAULT_OP_MODE_NAME = "Stop Robot";
    public static final OpMode DEFAULT_OP_MODE = new OpModeManager.a();
    private Map<String, Class<?>> a = new LinkedHashMap();
    private Map<String, OpMode> b = new LinkedHashMap();
    private String c = "Stop Robot";
    private OpMode d;
    private String e;
    private HardwareMap f;
    private HardwareMap g;
    private OpModeManager.b h;
    private boolean i;
    private boolean j;
    private boolean k;

    public OpModeManager(HardwareMap hardwareMap) {
        this.d = DEFAULT_OP_MODE;
        this.e = "Stop Robot";
        this.f = new HardwareMap();
        this.g = new HardwareMap();
        this.h = OpModeManager.b.a;
        this.i = false;
        this.j = false;
        this.k = false;
        this.f = hardwareMap;
        this.register("Stop Robot", OpModeManager.a.class);
        this.initActiveOpMode("Stop Robot");
    }

    public void registerOpModes(OpModeRegister register) {
        register.register(this);
    }

    public void setHardwareMap(HardwareMap hardwareMap) {
        this.f = hardwareMap;
    }

    public HardwareMap getHardwareMap() {
        return this.f;
    }

    public Set<String> getOpModes() {
        LinkedHashSet var1 = new LinkedHashSet();
        var1.addAll(this.a.keySet());
        var1.addAll(this.b.keySet());
        return var1;
    }

    public String getActiveOpModeName() {
        return this.c;
    }

    public OpMode getActiveOpMode() {
        return this.d;
    }

    public void initActiveOpMode(String name) {
        this.e = name;
        this.i = true;
        this.j = true;
        this.h = OpModeManager.b.a;
    }

    public void startActiveOpMode() {
        this.h = OpModeManager.b.b;
        this.k = true;
    }

    public void stopActiveOpMode() {
        this.d.stop();
        this.initActiveOpMode("Stop Robot");
    }

    public void runActiveOpMode(Gamepad[] gamepads) {
        this.d.time = this.d.getRuntime();
        this.d.gamepad1 = gamepads[0];
        this.d.gamepad2 = gamepads[1];
        if(this.i) {
            this.d.stop();
            this.a();
            this.h = OpModeManager.b.a;
            this.j = true;
        }

        if(this.h == OpModeManager.b.a) {
            if(this.j) {
                this.d.hardwareMap = this.f;
                this.d.resetStartTime();
                this.d.init();
                this.j = false;
            }

            this.d.init_loop();
        } else {
            if(this.k) {
                this.d.start();
                this.k = false;
            }

            this.d.loop();
        }

    }

    public void logOpModes() {
        int var1 = this.a.size() + this.b.size();
        RobotLog.i("There are " + var1 + " Op Modes");
        Iterator var2 = this.a.entrySet().iterator();

        Entry var3;
        while(var2.hasNext()) {
            var3 = (Entry)var2.next();
            RobotLog.i("   Op Mode: " + (String)var3.getKey());
        }

        var2 = this.b.entrySet().iterator();

        while(var2.hasNext()) {
            var3 = (Entry)var2.next();
            RobotLog.i("   Op Mode: " + (String)var3.getKey());
        }

    }

    public void register(String name, Class opMode) {
        if(this.a(name)) {
            throw new IllegalArgumentException("Cannot register the same op mode name twice");
        } else {
            this.a.put(name, opMode);
        }
    }

    public void register(String name, OpMode opMode) {
        if(this.a(name)) {
            throw new IllegalArgumentException("Cannot register the same op mode name twice");
        } else {
            this.b.put(name, opMode);
        }
    }

    private void a() {
        RobotLog.i("Attempting to switch to op mode " + this.e);

        try {
            if(this.b.containsKey(this.e)) {
                this.d = (OpMode)this.b.get(this.e);
            } else {
                this.d = (OpMode)((Class)this.a.get(this.e)).newInstance();
            }

            this.c = this.e;
        } catch (InstantiationException var2) {
            this.a((Exception)var2);
        } catch (IllegalAccessException var3) {
            this.a((Exception)var3);
        }

        this.i = false;
    }

    private boolean a(String var1) {
        return this.getOpModes().contains(var1);
    }

    private void a(Exception var1) {
        RobotLog.e("Unable to start op mode " + this.c);
        RobotLog.logStacktrace(var1);
        this.c = "Stop Robot";
        this.d = DEFAULT_OP_MODE;
    }

    private static class a extends OpMode {
        public a() {
        }

        public void init() {
            this.a();
        }

        public void init_loop() {
            this.a();
            this.telemetry.addData("Status", "Robot is stopped");
        }

        public void loop() {
            this.a();
            this.telemetry.addData("Status", "Robot is stopped");
        }

        public void stop() {
        }

        private void a() {
            Iterator var1 = this.hardwareMap.servoController.iterator();

            while(var1.hasNext()) {
                ServoController var2 = (ServoController)var1.next();
                var2.pwmDisable();
            }

            var1 = this.hardwareMap.dcMotorController.iterator();

            while(var1.hasNext()) {
                DcMotorController var3 = (DcMotorController)var1.next();
                var3.setMotorControllerDeviceMode(DeviceMode.WRITE_ONLY);
            }

            var1 = this.hardwareMap.dcMotor.iterator();

            while(var1.hasNext()) {
                DcMotor var4 = (DcMotor)var1.next();
                var4.setPower(0.0D);
                var4.setChannelMode(RunMode.RUN_WITHOUT_ENCODERS);
            }

            var1 = this.hardwareMap.lightSensor.iterator();

            while(var1.hasNext()) {
                LightSensor var5 = (LightSensor)var1.next();
                var5.enableLed(false);
            }

        }
    }

    private static enum b {
        a,
        b;

        private b() {
        }
    }
}
