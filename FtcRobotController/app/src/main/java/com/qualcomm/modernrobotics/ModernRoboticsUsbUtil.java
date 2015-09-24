package com.qualcomm.modernrobotics;

import android.content.Context;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.analytics.Analytics;

public class ModernRoboticsUsbUtil
{
    public static final int DEVICE_ID_DC_MOTOR_CONTROLLER = 77;
    public static final int DEVICE_ID_DEVICE_INTERFACE_MODULE = 65;
    public static final int DEVICE_ID_LEGACY_MODULE = 73;
    public static final int DEVICE_ID_SERVO_CONTROLLER = 83;
    public static final int MFG_CODE_MODERN_ROBOTICS = 77;
    private static Analytics a;
    
    private static DeviceManager.DeviceType a(final byte[] array) {
        if (array[1] != 77) {
            return DeviceManager.DeviceType.FTDI_USB_UNKNOWN_DEVICE;
        }
        switch (array[2]) {
            default: {
                return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_UNKNOWN_DEVICE;
            }
            case 77: {
                return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER;
            }
            case 83: {
                return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER;
            }
            case 73: {
                return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE;
            }
            case 65: {
                return DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE;
            }
        }
    }
    
    private static RobotUsbDevice a(final RobotUsbManager p0, final SerialNumber p1) throws RobotCoreException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: invokeinterface com/qualcomm/robotcore/hardware/usb/RobotUsbManager.scanForDevices:()I
        //     6: istore_2       
        //     7: iconst_0       
        //     8: istore_3       
        //     9: iload_3        
        //    10: iload_2        
        //    11: if_icmpge       210
        //    14: aload_1        
        //    15: aload_0        
        //    16: iload_3        
        //    17: invokeinterface com/qualcomm/robotcore/hardware/usb/RobotUsbManager.getDeviceSerialNumberByIndex:(I)Lcom/qualcomm/robotcore/util/SerialNumber;
        //    22: invokevirtual   com/qualcomm/robotcore/util/SerialNumber.equals:(Ljava/lang/Object;)Z
        //    25: ifeq            149
        //    28: iconst_1       
        //    29: istore          5
        //    31: new             Ljava/lang/StringBuilder;
        //    34: dup            
        //    35: invokespecial   java/lang/StringBuilder.<init>:()V
        //    38: aload_0        
        //    39: iload_3        
        //    40: invokeinterface com/qualcomm/robotcore/hardware/usb/RobotUsbManager.getDeviceDescriptionByIndex:(I)Ljava/lang/String;
        //    45: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    48: ldc             " ["
        //    50: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    53: aload_1        
        //    54: invokevirtual   com/qualcomm/robotcore/util/SerialNumber.getSerialNumber:()Ljava/lang/String;
        //    57: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    60: ldc             "]"
        //    62: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    65: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    68: astore          4
        //    70: iload           5
        //    72: ifne            100
        //    75: new             Ljava/lang/StringBuilder;
        //    78: dup            
        //    79: invokespecial   java/lang/StringBuilder.<init>:()V
        //    82: ldc             "unable to find USB device with serial number "
        //    84: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    87: aload_1        
        //    88: invokevirtual   com/qualcomm/robotcore/util/SerialNumber.toString:()Ljava/lang/String;
        //    91: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    94: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    97: invokestatic    com/qualcomm/modernrobotics/ModernRoboticsUsbUtil.a:(Ljava/lang/String;)V
        //   100: aconst_null    
        //   101: astore          6
        //   103: aload_0        
        //   104: aload_1        
        //   105: invokeinterface com/qualcomm/robotcore/hardware/usb/RobotUsbManager.openBySerialNumber:(Lcom/qualcomm/robotcore/util/SerialNumber;)Lcom/qualcomm/robotcore/hardware/usb/RobotUsbDevice;
        //   110: astore          6
        //   112: aload           6
        //   114: ldc             250000
        //   116: invokeinterface com/qualcomm/robotcore/hardware/usb/RobotUsbDevice.setBaudRate:(I)V
        //   121: aload           6
        //   123: bipush          8
        //   125: iconst_0       
        //   126: iconst_0       
        //   127: invokeinterface com/qualcomm/robotcore/hardware/usb/RobotUsbDevice.setDataCharacteristics:(BBB)V
        //   132: aload           6
        //   134: iconst_2       
        //   135: invokeinterface com/qualcomm/robotcore/hardware/usb/RobotUsbDevice.setLatencyTimer:(I)V
        //   140: ldc2_w          400
        //   143: invokestatic    java/lang/Thread.sleep:(J)V
        //   146: aload           6
        //   148: areturn        
        //   149: iinc            3, 1
        //   152: goto            9
        //   155: astore          7
        //   157: new             Ljava/lang/StringBuilder;
        //   160: dup            
        //   161: invokespecial   java/lang/StringBuilder.<init>:()V
        //   164: ldc             "Unable to open USB device "
        //   166: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   169: aload_1        
        //   170: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   173: ldc             " - "
        //   175: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   178: aload           4
        //   180: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   183: ldc             ": "
        //   185: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   188: aload           7
        //   190: invokevirtual   com/qualcomm/robotcore/exception/RobotCoreException.getMessage:()Ljava/lang/String;
        //   193: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   196: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   199: invokestatic    com/qualcomm/modernrobotics/ModernRoboticsUsbUtil.a:(Ljava/lang/String;)V
        //   202: goto            140
        //   205: astore          8
        //   207: aload           6
        //   209: areturn        
        //   210: ldc             ""
        //   212: astore          4
        //   214: iconst_0       
        //   215: istore          5
        //   217: goto            70
        //    Exceptions:
        //  throws com.qualcomm.robotcore.exception.RobotCoreException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                 
        //  -----  -----  -----  -----  -----------------------------------------------------
        //  103    140    155    205    Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //  140    146    205    210    Ljava/lang/InterruptedException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0140:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
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
    
    private static void a(final String s) throws RobotCoreException {
        System.err.println(s);
        throw new RobotCoreException(s);
    }
    
    private static byte[] a(final RobotUsbDevice robotUsbDevice) throws RobotCoreException {
        final byte[] array = new byte[5];
        final byte[] array2 = new byte[3];
        final byte[] array3 = { 85, -86, -128, 0, 3 };
        while (true) {
            try {
                robotUsbDevice.purge(RobotUsbDevice.Channel.RX);
                robotUsbDevice.write(array3);
                robotUsbDevice.read(array);
                if (!ModernRoboticsPacket.a(array, 3)) {
                    return array2;
                }
            }
            catch (RobotCoreException ex) {
                a("error reading USB device headers");
                continue;
            }
            break;
        }
        robotUsbDevice.read(array2);
        return array2;
    }
    
    public static DeviceManager.DeviceType getDeviceType(final byte[] array) {
        return a(array);
    }
    
    public static byte[] getUsbDeviceHeader(final RobotUsbDevice robotUsbDevice) throws RobotCoreException {
        return a(robotUsbDevice);
    }
    
    public static void init(final Context context) {
        if (ModernRoboticsUsbUtil.a != null) {
            ModernRoboticsUsbUtil.a = new Analytics(context, "update_rc");
        }
    }
    
    public static RobotUsbDevice openUsbDevice(final RobotUsbManager robotUsbManager, final SerialNumber serialNumber) throws RobotCoreException {
        return a(robotUsbManager, serialNumber);
    }
}
