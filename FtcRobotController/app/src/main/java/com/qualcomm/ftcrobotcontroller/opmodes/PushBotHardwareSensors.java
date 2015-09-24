package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;

public class PushBotHardwareSensors extends PushBotTelemetry
{
    final int drive_to_ir_beacon_done;
    final int drive_to_ir_beacon_invalid;
    final int drive_to_ir_beacon_not_detected;
    final int drive_to_ir_beacon_running;
    private IrSeekerSensor v_sensor_ir;
    private OpticalDistanceSensor v_sensor_ods;
    private TouchSensor v_sensor_touch;
    
    public PushBotHardwareSensors() {
        this.drive_to_ir_beacon_not_detected = -2;
        this.drive_to_ir_beacon_invalid = -1;
        this.drive_to_ir_beacon_running = 0;
        this.drive_to_ir_beacon_done = 1;
    }
    
    double a_ir_angle() {
        double angle = 0.0;
        if (this.v_sensor_ir != null) {
            angle = this.v_sensor_ir.getAngle();
        }
        return angle;
    }
    
    IrSeekerSensor.IrSeekerIndividualSensor[] a_ir_angles_and_strengths() {
        Object[] individualSensors = { new IrSeekerSensor.IrSeekerIndividualSensor(), new IrSeekerSensor.IrSeekerIndividualSensor() };
        if (this.v_sensor_ir != null) {
            individualSensors = this.v_sensor_ir.getIndividualSensors();
        }
        return (IrSeekerSensor.IrSeekerIndividualSensor[])individualSensors;
    }
    
    double a_ir_strength() {
        double strength = 0.0;
        if (this.v_sensor_ir != null) {
            strength = this.v_sensor_ir.getStrength();
        }
        return strength;
    }
    
    double a_ods_light_detected() {
        if (this.v_sensor_ods != null) {
            this.v_sensor_ods.getLightDetected();
        }
        return 0.0;
    }
    
    boolean a_ods_white_tape_detected() {
        final OpticalDistanceSensor v_sensor_ods = this.v_sensor_ods;
        boolean b = false;
        if (v_sensor_ods != null) {
            final double n = dcmpl(this.v_sensor_ods.getLightDetected(), 0.8);
            b = false;
            if (n > 0) {
                b = true;
            }
        }
        return b;
    }
    
    int drive_to_ir_beacon() {
        final double a_ir_strength = this.a_ir_strength();
        if (a_ir_strength > 0.01 && a_ir_strength < 0.2) {
            final double n = this.a_ir_angle() / 240.0;
            this.set_drive_power(Range.clip(0.15 + n, -1.0, 1.0), Range.clip(0.15 - n, -1.0, 1.0));
            return 0;
        }
        if (a_ir_strength <= 0.0) {
            this.set_drive_power(0.0, 0.0);
            return -2;
        }
        this.set_drive_power(0.0, 0.0);
        return 1;
    }
    
    @Override
    public void init() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: invokespecial   com/qualcomm/ftcrobotcontroller/opmodes/PushBotTelemetry.init:()V
        //     4: aload_0        
        //     5: aload_0        
        //     6: getfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardwareSensors.hardwareMap:Lcom/qualcomm/robotcore/hardware/HardwareMap;
        //     9: getfield        com/qualcomm/robotcore/hardware/HardwareMap.touchSensor:Lcom/qualcomm/robotcore/hardware/HardwareMap$DeviceMapping;
        //    12: ldc             "sensor_touch"
        //    14: invokevirtual   com/qualcomm/robotcore/hardware/HardwareMap$DeviceMapping.get:(Ljava/lang/String;)Ljava/lang/Object;
        //    17: checkcast       Lcom/qualcomm/robotcore/hardware/TouchSensor;
        //    20: putfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardwareSensors.v_sensor_touch:Lcom/qualcomm/robotcore/hardware/TouchSensor;
        //    23: aload_0        
        //    24: aload_0        
        //    25: getfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardwareSensors.hardwareMap:Lcom/qualcomm/robotcore/hardware/HardwareMap;
        //    28: getfield        com/qualcomm/robotcore/hardware/HardwareMap.irSeekerSensor:Lcom/qualcomm/robotcore/hardware/HardwareMap$DeviceMapping;
        //    31: ldc             "sensor_ir"
        //    33: invokevirtual   com/qualcomm/robotcore/hardware/HardwareMap$DeviceMapping.get:(Ljava/lang/String;)Ljava/lang/Object;
        //    36: checkcast       Lcom/qualcomm/robotcore/hardware/IrSeekerSensor;
        //    39: putfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardwareSensors.v_sensor_ir:Lcom/qualcomm/robotcore/hardware/IrSeekerSensor;
        //    42: aload_0        
        //    43: aload_0        
        //    44: getfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardwareSensors.hardwareMap:Lcom/qualcomm/robotcore/hardware/HardwareMap;
        //    47: getfield        com/qualcomm/robotcore/hardware/HardwareMap.opticalDistanceSensor:Lcom/qualcomm/robotcore/hardware/HardwareMap$DeviceMapping;
        //    50: ldc             "sensor_ods"
        //    52: invokevirtual   com/qualcomm/robotcore/hardware/HardwareMap$DeviceMapping.get:(Ljava/lang/String;)Ljava/lang/Object;
        //    55: checkcast       Lcom/qualcomm/robotcore/hardware/OpticalDistanceSensor;
        //    58: putfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardwareSensors.v_sensor_ods:Lcom/qualcomm/robotcore/hardware/OpticalDistanceSensor;
        //    61: return         
        //    62: astore_1       
        //    63: aload_0        
        //    64: ldc             "sensor_touch"
        //    66: invokevirtual   com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardwareSensors.m_warning_message:(Ljava/lang/String;)V
        //    69: aload_1        
        //    70: invokevirtual   java/lang/Exception.getLocalizedMessage:()Ljava/lang/String;
        //    73: invokestatic    com/qualcomm/ftccommon/DbgLog.msg:(Ljava/lang/String;)V
        //    76: aload_0        
        //    77: aconst_null    
        //    78: putfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardwareSensors.v_sensor_touch:Lcom/qualcomm/robotcore/hardware/TouchSensor;
        //    81: goto            23
        //    84: astore_2       
        //    85: aload_0        
        //    86: ldc             "sensor_ir"
        //    88: invokevirtual   com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardwareSensors.m_warning_message:(Ljava/lang/String;)V
        //    91: aload_2        
        //    92: invokevirtual   java/lang/Exception.getLocalizedMessage:()Ljava/lang/String;
        //    95: invokestatic    com/qualcomm/ftccommon/DbgLog.msg:(Ljava/lang/String;)V
        //    98: aload_0        
        //    99: aconst_null    
        //   100: putfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardwareSensors.v_sensor_ir:Lcom/qualcomm/robotcore/hardware/IrSeekerSensor;
        //   103: goto            42
        //   106: astore_3       
        //   107: aload_0        
        //   108: aload_0        
        //   109: getfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardwareSensors.hardwareMap:Lcom/qualcomm/robotcore/hardware/HardwareMap;
        //   112: getfield        com/qualcomm/robotcore/hardware/HardwareMap.opticalDistanceSensor:Lcom/qualcomm/robotcore/hardware/HardwareMap$DeviceMapping;
        //   115: ldc             "sensor_eopd"
        //   117: invokevirtual   com/qualcomm/robotcore/hardware/HardwareMap$DeviceMapping.get:(Ljava/lang/String;)Ljava/lang/Object;
        //   120: checkcast       Lcom/qualcomm/robotcore/hardware/OpticalDistanceSensor;
        //   123: putfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardwareSensors.v_sensor_ods:Lcom/qualcomm/robotcore/hardware/OpticalDistanceSensor;
        //   126: return         
        //   127: astore          4
        //   129: aload_0        
        //   130: aload_0        
        //   131: getfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardwareSensors.hardwareMap:Lcom/qualcomm/robotcore/hardware/HardwareMap;
        //   134: getfield        com/qualcomm/robotcore/hardware/HardwareMap.opticalDistanceSensor:Lcom/qualcomm/robotcore/hardware/HardwareMap$DeviceMapping;
        //   137: ldc             "sensor_EOPD"
        //   139: invokevirtual   com/qualcomm/robotcore/hardware/HardwareMap$DeviceMapping.get:(Ljava/lang/String;)Ljava/lang/Object;
        //   142: checkcast       Lcom/qualcomm/robotcore/hardware/OpticalDistanceSensor;
        //   145: putfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardwareSensors.v_sensor_ods:Lcom/qualcomm/robotcore/hardware/OpticalDistanceSensor;
        //   148: return         
        //   149: astore          5
        //   151: aload_0        
        //   152: ldc             "sensor_ods/eopd/EOPD"
        //   154: invokevirtual   com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardwareSensors.m_warning_message:(Ljava/lang/String;)V
        //   157: new             Ljava/lang/StringBuilder;
        //   160: dup            
        //   161: invokespecial   java/lang/StringBuilder.<init>:()V
        //   164: ldc             "Can't map sensor_ods nor sensor_eopd, nor sensor_EOPD ("
        //   166: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   169: aload           5
        //   171: invokevirtual   java/lang/Exception.getLocalizedMessage:()Ljava/lang/String;
        //   174: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   177: ldc             ").\n"
        //   179: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   182: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   185: invokestatic    com/qualcomm/ftccommon/DbgLog.msg:(Ljava/lang/String;)V
        //   188: aload_0        
        //   189: aconst_null    
        //   190: putfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardwareSensors.v_sensor_ods:Lcom/qualcomm/robotcore/hardware/OpticalDistanceSensor;
        //   193: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  4      23     62     84     Ljava/lang/Exception;
        //  23     42     84     106    Ljava/lang/Exception;
        //  42     61     106    194    Ljava/lang/Exception;
        //  107    126    127    194    Ljava/lang/Exception;
        //  129    148    149    194    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 89, Size: 89
        //     at java.util.ArrayList.rangeCheck(Unknown Source)
        //     at java.util.ArrayList.get(Unknown Source)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3305)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:114)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at the.bytecode.club.bytecodeviewer.decompilers.ProcyonDecompiler.doSaveJarDecompiled(ProcyonDecompiler.java:194)
        //     at the.bytecode.club.bytecodeviewer.decompilers.ProcyonDecompiler.decompileToZip(ProcyonDecompiler.java:146)
        //     at the.bytecode.club.bytecodeviewer.gui.MainViewerGUI$18$1$2.run(MainViewerGUI.java:1093)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    boolean is_touch_sensor_pressed() {
        final TouchSensor v_sensor_touch = this.v_sensor_touch;
        boolean pressed = false;
        if (v_sensor_touch != null) {
            pressed = this.v_sensor_touch.isPressed();
        }
        return pressed;
    }
    
    boolean move_arm_upward_until_touch() {
        if (this.is_touch_sensor_pressed()) {
            this.m_left_arm_power(0.0);
        }
        else {
            this.m_left_arm_power(1.0);
        }
        return this.is_touch_sensor_pressed();
    }
}
