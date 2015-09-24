package com.qualcomm.robotcore.eventloop.opmode;

import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.Gamepad;
import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.LinkedHashMap;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.Map;

public class OpModeManager
{
    public static final OpMode DEFAULT_OP_MODE;
    public static final String DEFAULT_OP_MODE_NAME = "Stop Robot";
    private Map<String, Class<?>> a;
    private Map<String, OpMode> b;
    private String c;
    private OpMode d;
    private String e;
    private HardwareMap f;
    private HardwareMap g;
    private b h;
    private boolean i;
    private boolean j;
    private boolean k;
    
    static {
        DEFAULT_OP_MODE = new a();
    }
    
    public OpModeManager(final HardwareMap f) {
        this.a = new LinkedHashMap<String, Class<?>>();
        this.b = new LinkedHashMap<String, OpMode>();
        this.c = "Stop Robot";
        this.d = OpModeManager.DEFAULT_OP_MODE;
        this.e = "Stop Robot";
        this.f = new HardwareMap();
        this.g = new HardwareMap();
        this.h = OpModeManager.b.a;
        this.i = false;
        this.j = false;
        this.k = false;
        this.f = f;
        this.register("Stop Robot", a.class);
        this.initActiveOpMode("Stop Robot");
    }
    
    private void a() {
        RobotLog.i("Attempting to switch to op mode " + this.e);
        while (true) {
            try {
                if (this.b.containsKey(this.e)) {
                    this.d = this.b.get(this.e);
                }
                else {
                    this.d = (OpMode)this.a.get(this.e).newInstance();
                }
                this.c = this.e;
                this.i = false;
            }
            catch (InstantiationException ex) {
                this.a(ex);
                continue;
            }
            catch (IllegalAccessException ex2) {
                this.a(ex2);
                continue;
            }
            break;
        }
    }
    
    private void a(final Exception ex) {
        RobotLog.e("Unable to start op mode " + this.c);
        RobotLog.logStacktrace(ex);
        this.c = "Stop Robot";
        this.d = OpModeManager.DEFAULT_OP_MODE;
    }
    
    private boolean a(final String s) {
        return this.getOpModes().contains(s);
    }
    
    public OpMode getActiveOpMode() {
        return this.d;
    }
    
    public String getActiveOpModeName() {
        return this.c;
    }
    
    public HardwareMap getHardwareMap() {
        return this.f;
    }
    
    public Set<String> getOpModes() {
        final LinkedHashSet<Object> set = (LinkedHashSet<Object>)new LinkedHashSet<String>();
        set.addAll(this.a.keySet());
        set.addAll(this.b.keySet());
        return (Set<String>)set;
    }
    
    public void initActiveOpMode(final String e) {
        this.e = e;
        this.i = true;
        this.j = true;
        this.h = OpModeManager.b.a;
    }
    
    public void logOpModes() {
        RobotLog.i("There are " + (this.a.size() + this.b.size()) + " Op Modes");
        final Iterator<Map.Entry<String, Class<?>>> iterator = this.a.entrySet().iterator();
        while (iterator.hasNext()) {
            RobotLog.i("   Op Mode: " + iterator.next().getKey());
        }
        final Iterator<Map.Entry<String, OpMode>> iterator2 = this.b.entrySet().iterator();
        while (iterator2.hasNext()) {
            RobotLog.i("   Op Mode: " + iterator2.next().getKey());
        }
    }
    
    public void register(final String s, final OpMode opMode) {
        if (this.a(s)) {
            throw new IllegalArgumentException("Cannot register the same op mode name twice");
        }
        this.b.put(s, opMode);
    }
    
    public void register(final String s, final Class clazz) {
        if (this.a(s)) {
            throw new IllegalArgumentException("Cannot register the same op mode name twice");
        }
        this.a.put(s, clazz);
    }
    
    public void registerOpModes(final OpModeRegister opModeRegister) {
        opModeRegister.register(this);
    }
    
    public void runActiveOpMode(final Gamepad[] array) {
        this.d.time = this.d.getRuntime();
        this.d.gamepad1 = array[0];
        this.d.gamepad2 = array[1];
        if (this.i) {
            this.d.stop();
            this.a();
            this.h = OpModeManager.b.a;
            this.j = true;
        }
        if (this.h == OpModeManager.b.a) {
            if (this.j) {
                this.d.hardwareMap = this.f;
                this.d.resetStartTime();
                this.d.init();
                this.j = false;
            }
            this.d.init_loop();
            return;
        }
        if (this.k) {
            this.d.start();
            this.k = false;
        }
        this.d.loop();
    }
    
    public void setHardwareMap(final HardwareMap f) {
        this.f = f;
    }
    
    public void startActiveOpMode() {
        this.h = OpModeManager.b.b;
        this.k = true;
    }
    
    public void stopActiveOpMode() {
        this.d.stop();
        this.initActiveOpMode("Stop Robot");
    }
    
    private static class a extends OpMode
    {
        private void a() {
            final Iterator<ServoController> iterator = this.hardwareMap.servoController.iterator();
            while (iterator.hasNext()) {
                iterator.next().pwmDisable();
            }
            final Iterator<DcMotorController> iterator2 = this.hardwareMap.dcMotorController.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
            }
            for (final DcMotor dcMotor : this.hardwareMap.dcMotor) {
                dcMotor.setPower(0.0);
                dcMotor.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
            }
            final Iterator<LightSensor> iterator4 = this.hardwareMap.lightSensor.iterator();
            while (iterator4.hasNext()) {
                iterator4.next().enableLed(false);
            }
        }
        
        @Override
        public void init() {
            this.a();
        }
        
        @Override
        public void init_loop() {
            this.a();
            this.telemetry.addData("Status", "Robot is stopped");
        }
        
        @Override
        public void loop() {
            this.a();
            this.telemetry.addData("Status", "Robot is stopped");
        }
        
        @Override
        public void stop() {
        }
    }
    
    private enum b
    {
        a, 
        b;
    }
}
