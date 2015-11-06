//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.robotcore.eventloop.opmode;

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
    public static final OpMode DEFAULT_OP_MODE = new StopRobotOpMode();
    private Map<String, Class<?>> a = new LinkedHashMap();
    private Map<String, OpMode> b = new LinkedHashMap();
    private String activeOpModeName = "Stop Robot";
    private OpMode activeOpMode;
    private String pendingOpModeName;
    private HardwareMap hardwareMap;
    private HardwareMap unusedHardwareMap;
    private ANENUM h;
    private boolean i;
    private boolean j;
    private boolean k;

    public OpModeManager(HardwareMap hardwareMap) {
        this.activeOpMode = DEFAULT_OP_MODE;
        this.pendingOpModeName = "Stop Robot";
        this.hardwareMap = new HardwareMap();
        this.unusedHardwareMap = new HardwareMap();
        this.h = OpModeManager.ANENUM.a;
        this.i = false;
        this.j = false;
        this.k = false;
        this.hardwareMap = hardwareMap;
        this.register("Stop Robot", StopRobotOpMode.class);
        this.initActiveOpMode("Stop Robot");
    }

    public void registerOpModes(OpModeRegister register) {
        register.register(this);
    }

    public void setHardwareMap(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public HardwareMap getHardwareMap() {
        return this.hardwareMap;
    }

    public Set<String> getOpModes() {
        LinkedHashSet var1 = new LinkedHashSet();
        var1.addAll(this.a.keySet());
        var1.addAll(this.b.keySet());
        return var1;
    }

    public String getActiveOpModeName() {
        return this.activeOpModeName;
    }

    public OpMode getActiveOpMode() {
        return this.activeOpMode;
    }

    public void initActiveOpMode(String name) {
        this.pendingOpModeName = name;
        this.i = true;
        this.j = true;
        this.h = OpModeManager.ANENUM.a;
    }

    public void startActiveOpMode() {
        this.h = OpModeManager.ANENUM.b;
        this.k = true;
    }

    public void stopActiveOpMode() {
        this.activeOpMode.stop();
        this.initActiveOpMode("Stop Robot");
    }

    public void runActiveOpMode(Gamepad[] gamepads) {
        this.activeOpMode.time = this.activeOpMode.getRuntime();
        this.activeOpMode.gamepad1 = gamepads[0];
        this.activeOpMode.gamepad2 = gamepads[1];
        if (this.i) {
            this.activeOpMode.stop();
            this.a();
            this.h = OpModeManager.ANENUM.a;
            this.j = true;
        }

        if(this.h == OpModeManager.ANENUM.a) {
            if(this.j) {
                this.activeOpMode.hardwareMap = this.hardwareMap;
                this.activeOpMode.resetStartTime();
                this.activeOpMode.init();
                this.j = false;
            }

            this.activeOpMode.init_loop();
        } else {
            if(this.k) {
                this.activeOpMode.start();
                this.k = false;
            }

            this.activeOpMode.loop();
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
        RobotLog.i("Attempting to switch to op mode " + this.pendingOpModeName);

        try {
            if(this.b.containsKey(this.pendingOpModeName)) {
                this.activeOpMode = (OpMode)this.b.get(this.pendingOpModeName);
            } else {
                this.activeOpMode = (OpMode)((Class)this.a.get(this.pendingOpModeName)).newInstance();
            }

            this.activeOpModeName = this.pendingOpModeName;
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
        RobotLog.e("Unable to start op mode " + this.activeOpModeName);
        RobotLog.logStacktrace(var1);
        this.activeOpModeName = "Stop Robot";
        this.activeOpMode = DEFAULT_OP_MODE;
    }

    private static class StopRobotOpMode extends OpMode {
        public StopRobotOpMode() {
        }

        public void init() {
            this.StopRobot();
        }

        public void init_loop() {
            this.StopRobot();
            this.telemetry.addData("Status", "Robot is stopped");
        }

        public void loop() {
            this.StopRobot();
            this.telemetry.addData("Status", "Robot is stopped");
        }

        public void stop() {
        }

        private void StopRobot() {
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
                var4.setMode(RunMode.RUN_WITHOUT_ENCODERS);
            }

            var1 = this.hardwareMap.lightSensor.iterator();

            while(var1.hasNext()) {
                LightSensor var5 = (LightSensor)var1.next();
                var5.enableLed(false);
            }

        }
    }

    private static enum ANENUM
        {
        a,
        b;

        private ANENUM() {
        }
    }
}
