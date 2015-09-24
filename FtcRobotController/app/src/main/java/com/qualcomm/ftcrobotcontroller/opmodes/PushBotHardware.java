package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class PushBotHardware extends OpMode
{
    private DcMotor v_motor_left_arm;
    private DcMotor v_motor_left_drive;
    private DcMotor v_motor_right_drive;
    private Servo v_servo_left_hand;
    private Servo v_servo_right_hand;
    private boolean v_warning_generated;
    private String v_warning_message;
    
    public PushBotHardware() {
        this.v_warning_generated = false;
    }
    
    double a_hand_position() {
        double position = 0.0;
        if (this.v_servo_left_hand != null) {
            position = this.v_servo_left_hand.getPosition();
        }
        return position;
    }
    
    double a_left_arm_power() {
        double power = 0.0;
        if (this.v_motor_left_arm != null) {
            power = this.v_motor_left_arm.getPower();
        }
        return power;
    }
    
    double a_left_drive_power() {
        double power = 0.0;
        if (this.v_motor_left_drive != null) {
            power = this.v_motor_left_drive.getPower();
        }
        return power;
    }
    
    int a_left_encoder_count() {
        final DcMotor v_motor_left_drive = this.v_motor_left_drive;
        int currentPosition = 0;
        if (v_motor_left_drive != null) {
            currentPosition = this.v_motor_left_drive.getCurrentPosition();
        }
        return currentPosition;
    }
    
    double a_right_drive_power() {
        double power = 0.0;
        if (this.v_motor_right_drive != null) {
            power = this.v_motor_right_drive.getPower();
        }
        return power;
    }
    
    int a_right_encoder_count() {
        final DcMotor v_motor_right_drive = this.v_motor_right_drive;
        int currentPosition = 0;
        if (v_motor_right_drive != null) {
            currentPosition = this.v_motor_right_drive.getCurrentPosition();
        }
        return currentPosition;
    }
    
    boolean a_warning_generated() {
        return this.v_warning_generated;
    }
    
    String a_warning_message() {
        return this.v_warning_message;
    }
    
    boolean drive_using_encoders(final double n, final double n2, final double n3, final double n4) {
        this.run_using_encoders();
        this.set_drive_power(n, n2);
        final boolean have_drive_encoders_reached = this.have_drive_encoders_reached(n3, n4);
        boolean b = false;
        if (have_drive_encoders_reached) {
            this.reset_drive_encoders();
            this.set_drive_power(0.0, 0.0);
            b = true;
        }
        return b;
    }
    
    boolean has_left_drive_encoder_reached(final double n) {
        final DcMotor v_motor_left_drive = this.v_motor_left_drive;
        boolean b = false;
        if (v_motor_left_drive != null) {
            final double n2 = dcmpl((double)Math.abs(this.v_motor_left_drive.getCurrentPosition()), n);
            b = false;
            if (n2 > 0) {
                b = true;
            }
        }
        return b;
    }
    
    boolean has_left_drive_encoder_reset() {
        final int a_left_encoder_count = this.a_left_encoder_count();
        boolean b = false;
        if (a_left_encoder_count == 0) {
            b = true;
        }
        return b;
    }
    
    boolean has_right_drive_encoder_reached(final double n) {
        final DcMotor v_motor_right_drive = this.v_motor_right_drive;
        boolean b = false;
        if (v_motor_right_drive != null) {
            final double n2 = dcmpl((double)Math.abs(this.v_motor_right_drive.getCurrentPosition()), n);
            b = false;
            if (n2 > 0) {
                b = true;
            }
        }
        return b;
    }
    
    boolean has_right_drive_encoder_reset() {
        final int a_right_encoder_count = this.a_right_encoder_count();
        boolean b = false;
        if (a_right_encoder_count == 0) {
            b = true;
        }
        return b;
    }
    
    boolean have_drive_encoders_reached(final double n, final double n2) {
        final boolean has_left_drive_encoder_reached = this.has_left_drive_encoder_reached(n);
        boolean b = false;
        if (has_left_drive_encoder_reached) {
            final boolean has_right_drive_encoder_reached = this.has_right_drive_encoder_reached(n2);
            b = false;
            if (has_right_drive_encoder_reached) {
                b = true;
            }
        }
        return b;
    }
    
    boolean have_drive_encoders_reset() {
        final boolean has_left_drive_encoder_reset = this.has_left_drive_encoder_reset();
        boolean b = false;
        if (has_left_drive_encoder_reset) {
            final boolean has_right_drive_encoder_reset = this.has_right_drive_encoder_reset();
            b = false;
            if (has_right_drive_encoder_reset) {
                b = true;
            }
        }
        return b;
    }
    
    @Override
    public void init() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: iconst_0       
        //     2: putfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.v_warning_generated:Z
        //     5: aload_0        
        //     6: ldc             "Can't map; "
        //     8: putfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.v_warning_message:Ljava/lang/String;
        //    11: aload_0        
        //    12: aload_0        
        //    13: getfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.hardwareMap:Lcom/qualcomm/robotcore/hardware/HardwareMap;
        //    16: getfield        com/qualcomm/robotcore/hardware/HardwareMap.dcMotor:Lcom/qualcomm/robotcore/hardware/HardwareMap$DeviceMapping;
        //    19: ldc             "left_drive"
        //    21: invokevirtual   com/qualcomm/robotcore/hardware/HardwareMap$DeviceMapping.get:(Ljava/lang/String;)Ljava/lang/Object;
        //    24: checkcast       Lcom/qualcomm/robotcore/hardware/DcMotor;
        //    27: putfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.v_motor_left_drive:Lcom/qualcomm/robotcore/hardware/DcMotor;
        //    30: aload_0        
        //    31: aload_0        
        //    32: getfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.hardwareMap:Lcom/qualcomm/robotcore/hardware/HardwareMap;
        //    35: getfield        com/qualcomm/robotcore/hardware/HardwareMap.dcMotor:Lcom/qualcomm/robotcore/hardware/HardwareMap$DeviceMapping;
        //    38: ldc             "right_drive"
        //    40: invokevirtual   com/qualcomm/robotcore/hardware/HardwareMap$DeviceMapping.get:(Ljava/lang/String;)Ljava/lang/Object;
        //    43: checkcast       Lcom/qualcomm/robotcore/hardware/DcMotor;
        //    46: putfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.v_motor_right_drive:Lcom/qualcomm/robotcore/hardware/DcMotor;
        //    49: aload_0        
        //    50: getfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.v_motor_right_drive:Lcom/qualcomm/robotcore/hardware/DcMotor;
        //    53: getstatic       com/qualcomm/robotcore/hardware/DcMotor$Direction.REVERSE:Lcom/qualcomm/robotcore/hardware/DcMotor$Direction;
        //    56: invokevirtual   com/qualcomm/robotcore/hardware/DcMotor.setDirection:(Lcom/qualcomm/robotcore/hardware/DcMotor$Direction;)V
        //    59: aload_0        
        //    60: aload_0        
        //    61: getfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.hardwareMap:Lcom/qualcomm/robotcore/hardware/HardwareMap;
        //    64: getfield        com/qualcomm/robotcore/hardware/HardwareMap.dcMotor:Lcom/qualcomm/robotcore/hardware/HardwareMap$DeviceMapping;
        //    67: ldc             "left_arm"
        //    69: invokevirtual   com/qualcomm/robotcore/hardware/HardwareMap$DeviceMapping.get:(Ljava/lang/String;)Ljava/lang/Object;
        //    72: checkcast       Lcom/qualcomm/robotcore/hardware/DcMotor;
        //    75: putfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.v_motor_left_arm:Lcom/qualcomm/robotcore/hardware/DcMotor;
        //    78: aload_0        
        //    79: aload_0        
        //    80: getfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.hardwareMap:Lcom/qualcomm/robotcore/hardware/HardwareMap;
        //    83: getfield        com/qualcomm/robotcore/hardware/HardwareMap.servo:Lcom/qualcomm/robotcore/hardware/HardwareMap$DeviceMapping;
        //    86: ldc             "left_hand"
        //    88: invokevirtual   com/qualcomm/robotcore/hardware/HardwareMap$DeviceMapping.get:(Ljava/lang/String;)Ljava/lang/Object;
        //    91: checkcast       Lcom/qualcomm/robotcore/hardware/Servo;
        //    94: putfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.v_servo_left_hand:Lcom/qualcomm/robotcore/hardware/Servo;
        //    97: aload_0        
        //    98: getfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.v_servo_left_hand:Lcom/qualcomm/robotcore/hardware/Servo;
        //   101: ldc2_w          0.5
        //   104: invokevirtual   com/qualcomm/robotcore/hardware/Servo.setPosition:(D)V
        //   107: aload_0        
        //   108: aload_0        
        //   109: getfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.hardwareMap:Lcom/qualcomm/robotcore/hardware/HardwareMap;
        //   112: getfield        com/qualcomm/robotcore/hardware/HardwareMap.servo:Lcom/qualcomm/robotcore/hardware/HardwareMap$DeviceMapping;
        //   115: ldc             "right_hand"
        //   117: invokevirtual   com/qualcomm/robotcore/hardware/HardwareMap$DeviceMapping.get:(Ljava/lang/String;)Ljava/lang/Object;
        //   120: checkcast       Lcom/qualcomm/robotcore/hardware/Servo;
        //   123: putfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.v_servo_right_hand:Lcom/qualcomm/robotcore/hardware/Servo;
        //   126: aload_0        
        //   127: getfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.v_servo_right_hand:Lcom/qualcomm/robotcore/hardware/Servo;
        //   130: ldc2_w          0.5
        //   133: invokevirtual   com/qualcomm/robotcore/hardware/Servo.setPosition:(D)V
        //   136: return         
        //   137: astore_1       
        //   138: aload_0        
        //   139: ldc             "left_drive"
        //   141: invokevirtual   com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.m_warning_message:(Ljava/lang/String;)V
        //   144: aload_1        
        //   145: invokevirtual   java/lang/Exception.getLocalizedMessage:()Ljava/lang/String;
        //   148: invokestatic    com/qualcomm/ftccommon/DbgLog.msg:(Ljava/lang/String;)V
        //   151: aload_0        
        //   152: aconst_null    
        //   153: putfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.v_motor_left_drive:Lcom/qualcomm/robotcore/hardware/DcMotor;
        //   156: goto            30
        //   159: astore_2       
        //   160: aload_0        
        //   161: ldc             "right_drive"
        //   163: invokevirtual   com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.m_warning_message:(Ljava/lang/String;)V
        //   166: aload_2        
        //   167: invokevirtual   java/lang/Exception.getLocalizedMessage:()Ljava/lang/String;
        //   170: invokestatic    com/qualcomm/ftccommon/DbgLog.msg:(Ljava/lang/String;)V
        //   173: aload_0        
        //   174: aconst_null    
        //   175: putfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.v_motor_right_drive:Lcom/qualcomm/robotcore/hardware/DcMotor;
        //   178: goto            59
        //   181: astore_3       
        //   182: aload_0        
        //   183: ldc             "left_arm"
        //   185: invokevirtual   com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.m_warning_message:(Ljava/lang/String;)V
        //   188: aload_3        
        //   189: invokevirtual   java/lang/Exception.getLocalizedMessage:()Ljava/lang/String;
        //   192: invokestatic    com/qualcomm/ftccommon/DbgLog.msg:(Ljava/lang/String;)V
        //   195: aload_0        
        //   196: aconst_null    
        //   197: putfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.v_motor_left_arm:Lcom/qualcomm/robotcore/hardware/DcMotor;
        //   200: goto            78
        //   203: astore          4
        //   205: aload_0        
        //   206: ldc             "left_hand"
        //   208: invokevirtual   com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.m_warning_message:(Ljava/lang/String;)V
        //   211: aload           4
        //   213: invokevirtual   java/lang/Exception.getLocalizedMessage:()Ljava/lang/String;
        //   216: invokestatic    com/qualcomm/ftccommon/DbgLog.msg:(Ljava/lang/String;)V
        //   219: aload_0        
        //   220: aconst_null    
        //   221: putfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.v_servo_left_hand:Lcom/qualcomm/robotcore/hardware/Servo;
        //   224: goto            107
        //   227: astore          5
        //   229: aload_0        
        //   230: ldc             "right_hand"
        //   232: invokevirtual   com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.m_warning_message:(Ljava/lang/String;)V
        //   235: aload           5
        //   237: invokevirtual   java/lang/Exception.getLocalizedMessage:()Ljava/lang/String;
        //   240: invokestatic    com/qualcomm/ftccommon/DbgLog.msg:(Ljava/lang/String;)V
        //   243: aload_0        
        //   244: aconst_null    
        //   245: putfield        com/qualcomm/ftcrobotcontroller/opmodes/PushBotHardware.v_servo_right_hand:Lcom/qualcomm/robotcore/hardware/Servo;
        //   248: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  11     30     137    159    Ljava/lang/Exception;
        //  30     59     159    181    Ljava/lang/Exception;
        //  59     78     181    203    Ljava/lang/Exception;
        //  78     107    203    227    Ljava/lang/Exception;
        //  107    136    227    249    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 114, Size: 114
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
    
    @Override
    public void loop() {
    }
    
    void m_hand_position(final double n) {
        final double clip = Range.clip(n, 0.0, 1.0);
        if (this.v_servo_left_hand != null) {
            this.v_servo_left_hand.setPosition(clip);
        }
        if (this.v_servo_right_hand != null) {
            this.v_servo_right_hand.setPosition(1.0 - clip);
        }
    }
    
    void m_left_arm_power(final double power) {
        if (this.v_motor_left_arm != null) {
            this.v_motor_left_arm.setPower(power);
        }
    }
    
    void m_warning_message(final String s) {
        if (this.v_warning_generated) {
            this.v_warning_message += ", ";
        }
        this.v_warning_generated = true;
        this.v_warning_message += s;
    }
    
    void open_hand() {
        if (this.v_servo_left_hand != null) {
            this.v_servo_left_hand.setPosition(1.0);
        }
        if (this.v_servo_right_hand != null) {
            this.v_servo_right_hand.setPosition(0.0);
        }
    }
    
    public void reset_drive_encoders() {
        this.reset_left_drive_encoder();
        this.reset_right_drive_encoder();
    }
    
    public void reset_left_drive_encoder() {
        if (this.v_motor_left_drive != null) {
            this.v_motor_left_drive.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
    }
    
    public void reset_right_drive_encoder() {
        if (this.v_motor_right_drive != null) {
            this.v_motor_right_drive.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
    }
    
    public void run_using_encoders() {
        this.run_using_left_drive_encoder();
        this.run_using_right_drive_encoder();
    }
    
    public void run_using_left_drive_encoder() {
        if (this.v_motor_left_drive != null) {
            this.v_motor_left_drive.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        }
    }
    
    public void run_using_right_drive_encoder() {
        if (this.v_motor_right_drive != null) {
            this.v_motor_right_drive.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        }
    }
    
    public void run_without_drive_encoders() {
        this.run_without_left_drive_encoder();
        this.run_without_right_drive_encoder();
    }
    
    public void run_without_left_drive_encoder() {
        if (this.v_motor_left_drive != null && this.v_motor_left_drive.getChannelMode() == DcMotorController.RunMode.RESET_ENCODERS) {
            this.v_motor_left_drive.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        }
    }
    
    public void run_without_right_drive_encoder() {
        if (this.v_motor_right_drive != null && this.v_motor_right_drive.getChannelMode() == DcMotorController.RunMode.RESET_ENCODERS) {
            this.v_motor_right_drive.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        }
    }
    
    float scale_motor_power(final float n) {
        final float clip = Range.clip(n, -1.0f, 1.0f);
        final float[] array = { 0.0f, 0.05f, 0.09f, 0.1f, 0.12f, 0.15f, 0.18f, 0.24f, 0.3f, 0.36f, 0.43f, 0.5f, 0.6f, 0.72f, 0.85f, 1.0f, 1.0f };
        int n2 = (int)(16.0 * clip);
        if (n2 < 0) {
            n2 = -n2;
        }
        else if (n2 > 16) {
            n2 = 16;
        }
        if (clip < 0.0f) {
            return -array[n2];
        }
        return array[n2];
    }
    
    void set_drive_power(final double power, final double power2) {
        if (this.v_motor_left_drive != null) {
            this.v_motor_left_drive.setPower(power);
        }
        if (this.v_motor_right_drive != null) {
            this.v_motor_right_drive.setPower(power2);
        }
    }
    
    @Override
    public void start() {
    }
    
    @Override
    public void stop() {
    }
}
