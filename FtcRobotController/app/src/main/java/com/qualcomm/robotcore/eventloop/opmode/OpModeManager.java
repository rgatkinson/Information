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
    private Map<String, Class<?>> registeredOpModeClasses = new LinkedHashMap();
    private Map<String, OpMode> registeredOpModeInstances = new LinkedHashMap();
    private String activeOpModeName = "Stop Robot";
    private OpMode activeOpMode;
    private String initOpModeName;
    private HardwareMap masterHardwareMap;
    private HardwareMap otherHardwareMap;
    private OPMODE_STATE opmodeState;
    private boolean opModeSwitchNeeded;
    private boolean opmodeInitNeeded;
    private boolean opmodeStartNeeded;

    public OpModeManager(HardwareMap hardwareMap) {
        this.activeOpMode = DEFAULT_OP_MODE;
        this.initOpModeName = "Stop Robot";
        this.masterHardwareMap = new HardwareMap();
        this.otherHardwareMap = new HardwareMap();
        this.opmodeState = OPMODE_STATE.INITING;
        this.opModeSwitchNeeded = false;
        this.opmodeInitNeeded = false;
        this.opmodeStartNeeded = false;
        this.masterHardwareMap = hardwareMap;
        this.register("Stop Robot", StopRobotOpMode.class);
        this.initActiveOpMode("Stop Robot");
    }

    public void registerOpModes(OpModeRegister register) {
        register.register(this);
    }

    public void setHardwareMap(HardwareMap hardwareMap) {
        this.masterHardwareMap = hardwareMap;
    }

    public HardwareMap getHardwareMap() {
        return this.masterHardwareMap;
    }

    public Set<String> getOpModes() {
        LinkedHashSet result = new LinkedHashSet();
        result.addAll(this.registeredOpModeClasses.keySet());
        result.addAll(this.registeredOpModeInstances.keySet());
        return result;
    }

    public String getActiveOpModeName() {
        return this.activeOpModeName;
    }

    public OpMode getActiveOpMode() {
        return this.activeOpMode;
    }

    public void initActiveOpMode(String name) {
        this.initOpModeName = name;
        this.opModeSwitchNeeded = true;
        this.opmodeInitNeeded = true;
        this.opmodeState = OPMODE_STATE.INITING;
    }

    public void startActiveOpMode() {
        this.opmodeState = OPMODE_STATE.STARTING;
        this.opmodeStartNeeded = true;
    }

    public void stopActiveOpMode() {
        this.activeOpMode.stop();
        this.initActiveOpMode("Stop Robot");
    }

    // Called repeatedly by FtcEventLoop.loop()
    public void runActiveOpMode(Gamepad[] gamepads) {
        this.activeOpMode.time = this.activeOpMode.getRuntime();
        this.activeOpMode.gamepad1 = gamepads[0];
        this.activeOpMode.gamepad2 = gamepads[1];

        if (this.opModeSwitchNeeded) {
            this.activeOpMode.stop();
            this.tryToActivateInitOpMode();
            this.opmodeState = OPMODE_STATE.INITING;
            this.opmodeInitNeeded = true;
        }

        if (this.opmodeState == OPMODE_STATE.INITING) {
            if(this.opmodeInitNeeded) {
                this.activeOpMode.hardwareMap = this.masterHardwareMap;
                this.activeOpMode.resetStartTime();
                this.activeOpMode.init();
                this.opmodeInitNeeded = false;
            }

            this.activeOpMode.init_loop();
        } else {
            if(this.opmodeStartNeeded) {
                this.activeOpMode.start();
                this.opmodeStartNeeded = false;
            }

            this.activeOpMode.loop();
        }

    }

    public void logOpModes() {
        int var1 = this.registeredOpModeClasses.size() + this.registeredOpModeInstances.size();
        RobotLog.i("There are " + var1 + " Op Modes");
        Iterator var2 = this.registeredOpModeClasses.entrySet().iterator();

        Entry var3;
        while(var2.hasNext()) {
            var3 = (Entry)var2.next();
            RobotLog.i("   Op Mode: " + (String)var3.getKey());
        }

        var2 = this.registeredOpModeInstances.entrySet().iterator();

        while(var2.hasNext()) {
            var3 = (Entry)var2.next();
            RobotLog.i("   Op Mode: " + (String)var3.getKey());
        }

    }

    public void register(String name, Class opMode) {
        if(this.isOpMode(name)) {
            throw new IllegalArgumentException("Cannot register the same op mode name twice");
        } else {
            this.registeredOpModeClasses.put(name, opMode);
        }
    }

    public void register(String name, OpMode opMode) {
        if(this.isOpMode(name)) {
            throw new IllegalArgumentException("Cannot register the same op mode name twice");
        } else {
            this.registeredOpModeInstances.put(name, opMode);
        }
    }

    private void tryToActivateInitOpMode() {
        RobotLog.i("Attempting to switch to op mode " + this.initOpModeName);

        try {
            if(this.registeredOpModeInstances.containsKey(this.initOpModeName)) {
                this.activeOpMode = (OpMode)this.registeredOpModeInstances.get(this.initOpModeName);
            } else {
                this.activeOpMode = (OpMode)((Class)this.registeredOpModeClasses.get(this.initOpModeName)).newInstance();
            }

            this.activeOpModeName = this.initOpModeName;
        } catch (InstantiationException var2) {
            this.exceptionWhileStartingOpMOde((Exception) var2);
        } catch (IllegalAccessException var3) {
            this.exceptionWhileStartingOpMOde((Exception) var3);
        }

        this.opModeSwitchNeeded = false;
    }

    private boolean isOpMode(String opModeName) {
        return this.getOpModes().contains(opModeName);
    }

    private void exceptionWhileStartingOpMOde(Exception var1) {
        RobotLog.e("Unable to start op mode " + this.activeOpModeName);
        RobotLog.logStacktrace(var1);
        this.activeOpModeName = "Stop Robot";
        this.activeOpMode = DEFAULT_OP_MODE;
    }

    //----------------------------------------------------------------------------------------------

    private static class StopRobotOpMode extends OpMode {
        public StopRobotOpMode() {
        }

        public void init() {
            this.doStop();
        }

        public void init_loop() {
            this.doStop();
            this.telemetry.addData("Status", "Robot is stopped");
        }

        public void loop() {
            this.doStop();
            this.telemetry.addData("Status", "Robot is stopped");
        }

        public void stop() {
        }

        private void doStop() {
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

    private static enum OPMODE_STATE
        {
            INITING,
            STARTING;

        private OPMODE_STATE() {
        }
    }
}
