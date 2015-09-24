package com.qualcomm.robotcore.hardware.configuration;

import java.io.Writer;
import java.io.StringWriter;
import com.qualcomm.robotcore.exception.RobotCoreException;
import java.io.IOException;
import java.util.Iterator;
import android.util.Xml;
import android.content.Context;
import java.util.ArrayList;
import java.util.HashSet;
import org.xmlpull.v1.XmlSerializer;

public class WriteXMLFileHandler
{
    private XmlSerializer a;
    private HashSet<String> b;
    private ArrayList<String> c;
    private String[] d;
    private int e;
    
    public WriteXMLFileHandler(final Context context) {
        this.b = new HashSet<String>();
        this.c = new ArrayList<String>();
        this.d = new String[] { "    ", "        ", "            " };
        this.e = 0;
        this.a = Xml.newSerializer();
    }
    
    private void a(final ControllerConfiguration controllerConfiguration) throws IOException {
        this.a.ignorableWhitespace(this.d[this.e]);
        this.a.startTag("", this.b(controllerConfiguration.getType().toString()));
        this.a(controllerConfiguration.getName());
        this.a.attribute("", "name", controllerConfiguration.getName());
        this.a.attribute("", "serialNumber", controllerConfiguration.getSerialNumber().toString());
        this.a.ignorableWhitespace("\n");
        ++this.e;
        final DeviceInterfaceModuleConfiguration deviceInterfaceModuleConfiguration = (DeviceInterfaceModuleConfiguration)controllerConfiguration;
        final Iterator iterator = ((ArrayList)deviceInterfaceModuleConfiguration.getPwmDevices()).iterator();
        while (iterator.hasNext()) {
            this.a(iterator.next());
        }
        final Iterator iterator2 = ((ArrayList)deviceInterfaceModuleConfiguration.getI2cDevices()).iterator();
        while (iterator2.hasNext()) {
            this.a(iterator2.next());
        }
        final Iterator iterator3 = ((ArrayList)deviceInterfaceModuleConfiguration.getAnalogInputDevices()).iterator();
        while (iterator3.hasNext()) {
            this.a(iterator3.next());
        }
        final Iterator iterator4 = ((ArrayList)deviceInterfaceModuleConfiguration.getDigitalDevices()).iterator();
        while (iterator4.hasNext()) {
            this.a(iterator4.next());
        }
        final Iterator iterator5 = ((ArrayList)deviceInterfaceModuleConfiguration.getAnalogOutputDevices()).iterator();
        while (iterator5.hasNext()) {
            this.a(iterator5.next());
        }
        --this.e;
        this.a.ignorableWhitespace(this.d[this.e]);
        this.a.endTag("", this.b(controllerConfiguration.getType().toString()));
        this.a.ignorableWhitespace("\n");
    }
    
    private void a(final ControllerConfiguration controllerConfiguration, final boolean b) throws IOException {
        this.a.ignorableWhitespace(this.d[this.e]);
        this.a.startTag("", this.b(controllerConfiguration.getType().toString()));
        this.a(controllerConfiguration.getName());
        this.a.attribute("", "name", controllerConfiguration.getName());
        if (b) {
            this.a.attribute("", "serialNumber", controllerConfiguration.getSerialNumber().toString());
        }
        else {
            this.a.attribute("", "port", String.valueOf(controllerConfiguration.getPort()));
        }
        this.a.ignorableWhitespace("\n");
        ++this.e;
        for (final DeviceConfiguration deviceConfiguration : (ArrayList)controllerConfiguration.getDevices()) {
            if (deviceConfiguration.isEnabled()) {
                this.a(deviceConfiguration);
            }
        }
        if (controllerConfiguration.getType() == DeviceConfiguration.ConfigurationType.MATRIX_CONTROLLER) {
            for (final DeviceConfiguration deviceConfiguration2 : (ArrayList)((MatrixControllerConfiguration)controllerConfiguration).getMotors()) {
                if (deviceConfiguration2.isEnabled()) {
                    this.a(deviceConfiguration2);
                }
            }
            for (final DeviceConfiguration deviceConfiguration3 : (ArrayList)((MatrixControllerConfiguration)controllerConfiguration).getServos()) {
                if (deviceConfiguration3.isEnabled()) {
                    this.a(deviceConfiguration3);
                }
            }
        }
        --this.e;
        this.a.ignorableWhitespace(this.d[this.e]);
        this.a.endTag("", this.b(controllerConfiguration.getType().toString()));
        this.a.ignorableWhitespace("\n");
    }
    
    private void a(final DeviceConfiguration deviceConfiguration) {
        if (!deviceConfiguration.isEnabled()) {
            return;
        }
        try {
            this.a.ignorableWhitespace(this.d[this.e]);
            this.a.startTag("", this.b(deviceConfiguration.getType().toString()));
            this.a(deviceConfiguration.getName());
            this.a.attribute("", "name", deviceConfiguration.getName());
            this.a.attribute("", "port", String.valueOf(deviceConfiguration.getPort()));
            this.a.endTag("", this.b(deviceConfiguration.getType().toString()));
            this.a.ignorableWhitespace("\n");
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private void a(final String s) {
        if (s.equalsIgnoreCase("NO DEVICE ATTACHED")) {
            return;
        }
        if (this.b.contains(s)) {
            this.c.add(s);
            return;
        }
        this.b.add(s);
    }
    
    private String b(final String s) {
        String s2 = s.substring(0, 1) + s.substring(1).toLowerCase();
        for (int i = s.lastIndexOf("_"); i > 0; i = s2.lastIndexOf("_")) {
            final int n = i + 1;
            s2 = s2.substring(0, i) + s2.substring(n, n + 1).toUpperCase() + s2.substring(n + 1);
        }
        return s2;
    }
    
    private void b(final ControllerConfiguration controllerConfiguration) throws IOException {
        this.a.ignorableWhitespace(this.d[this.e]);
        this.a.startTag("", this.b(controllerConfiguration.getType().toString()));
        this.a(controllerConfiguration.getName());
        this.a.attribute("", "name", controllerConfiguration.getName());
        this.a.attribute("", "serialNumber", controllerConfiguration.getSerialNumber().toString());
        this.a.ignorableWhitespace("\n");
        ++this.e;
        for (final DeviceConfiguration deviceConfiguration : (ArrayList)controllerConfiguration.getDevices()) {
            final String string = deviceConfiguration.getType().toString();
            if (string.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER.toString()) || string.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER.toString()) || string.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MATRIX_CONTROLLER.toString())) {
                this.a((ControllerConfiguration)deviceConfiguration, false);
            }
            else {
                if (!deviceConfiguration.isEnabled()) {
                    continue;
                }
                this.a(deviceConfiguration);
            }
        }
        --this.e;
        this.a.ignorableWhitespace(this.d[this.e]);
        this.a.endTag("", this.b(controllerConfiguration.getType().toString()));
        this.a.ignorableWhitespace("\n");
    }
    
    public void writeToFile(final String p0, final String p1, final String p2) throws RobotCoreException, IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: getfield        com/qualcomm/robotcore/hardware/configuration/WriteXMLFileHandler.c:Ljava/util/ArrayList;
        //     4: invokevirtual   java/util/ArrayList.size:()I
        //     7: ifle            40
        //    10: new             Ljava/io/IOException;
        //    13: dup            
        //    14: new             Ljava/lang/StringBuilder;
        //    17: dup            
        //    18: invokespecial   java/lang/StringBuilder.<init>:()V
        //    21: ldc             "Duplicate names: "
        //    23: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    26: aload_0        
        //    27: getfield        com/qualcomm/robotcore/hardware/configuration/WriteXMLFileHandler.c:Ljava/util/ArrayList;
        //    30: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    33: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    36: invokespecial   java/io/IOException.<init>:(Ljava/lang/String;)V
        //    39: athrow         
        //    40: aload_3        
        //    41: ldc             "[.][^.]+$"
        //    43: ldc             ""
        //    45: invokevirtual   java/lang/String.replaceFirst:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //    48: astore          4
        //    50: new             Ljava/io/File;
        //    53: dup            
        //    54: aload_2        
        //    55: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //    58: astore          5
        //    60: iconst_1       
        //    61: istore          6
        //    63: aload           5
        //    65: invokevirtual   java/io/File.exists:()Z
        //    68: ifne            78
        //    71: aload           5
        //    73: invokevirtual   java/io/File.mkdir:()Z
        //    76: istore          6
        //    78: iload           6
        //    80: ifeq            198
        //    83: new             Ljava/io/File;
        //    86: dup            
        //    87: new             Ljava/lang/StringBuilder;
        //    90: dup            
        //    91: invokespecial   java/lang/StringBuilder.<init>:()V
        //    94: aload_2        
        //    95: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    98: aload           4
        //   100: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   103: ldc_w           ".xml"
        //   106: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   109: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   112: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   115: astore          7
        //   117: new             Ljava/io/FileOutputStream;
        //   120: dup            
        //   121: aload           7
        //   123: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //   126: astore          8
        //   128: aload           8
        //   130: aload_1        
        //   131: invokevirtual   java/lang/String.getBytes:()[B
        //   134: invokevirtual   java/io/FileOutputStream.write:([B)V
        //   137: aload           8
        //   139: invokevirtual   java/io/FileOutputStream.close:()V
        //   142: return         
        //   143: astore          13
        //   145: aload           13
        //   147: invokevirtual   java/io/IOException.printStackTrace:()V
        //   150: return         
        //   151: astore          9
        //   153: aconst_null    
        //   154: astore          8
        //   156: aload           9
        //   158: invokevirtual   java/lang/Exception.printStackTrace:()V
        //   161: aload           8
        //   163: invokevirtual   java/io/FileOutputStream.close:()V
        //   166: return         
        //   167: astore          12
        //   169: aload           12
        //   171: invokevirtual   java/io/IOException.printStackTrace:()V
        //   174: return         
        //   175: astore          10
        //   177: aconst_null    
        //   178: astore          8
        //   180: aload           8
        //   182: invokevirtual   java/io/FileOutputStream.close:()V
        //   185: aload           10
        //   187: athrow         
        //   188: astore          11
        //   190: aload           11
        //   192: invokevirtual   java/io/IOException.printStackTrace:()V
        //   195: goto            185
        //   198: new             Lcom/qualcomm/robotcore/exception/RobotCoreException;
        //   201: dup            
        //   202: ldc_w           "Unable to create directory"
        //   205: invokespecial   com/qualcomm/robotcore/exception/RobotCoreException.<init>:(Ljava/lang/String;)V
        //   208: athrow         
        //   209: astore          10
        //   211: goto            180
        //   214: astore          9
        //   216: goto            156
        //    Exceptions:
        //  throws com.qualcomm.robotcore.exception.RobotCoreException
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  117    128    151    156    Ljava/lang/Exception;
        //  117    128    175    180    Any
        //  128    137    214    219    Ljava/lang/Exception;
        //  128    137    209    214    Any
        //  137    142    143    151    Ljava/io/IOException;
        //  156    161    209    214    Any
        //  161    166    167    175    Ljava/io/IOException;
        //  180    185    188    198    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0156:
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
    
    public String writeXml(final ArrayList<ControllerConfiguration> list) {
        this.c = new ArrayList<String>();
        this.b = new HashSet<String>();
        final StringWriter output = new StringWriter();
        try {
            this.a.setOutput((Writer)output);
            this.a.startDocument("UTF-8", true);
            this.a.ignorableWhitespace("\n");
            this.a.startTag("", "Robot");
            this.a.ignorableWhitespace("\n");
            for (final ControllerConfiguration controllerConfiguration : list) {
                final String string = controllerConfiguration.getType().toString();
                if (string.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER.toString()) || string.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER.toString())) {
                    this.a(controllerConfiguration, true);
                }
                if (string.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.LEGACY_MODULE_CONTROLLER.toString())) {
                    this.b(controllerConfiguration);
                }
                if (string.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.DEVICE_INTERFACE_MODULE.toString())) {
                    this.a(controllerConfiguration);
                }
            }
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        this.a.endTag("", "Robot");
        this.a.ignorableWhitespace("\n");
        this.a.endDocument();
        return output.toString();
    }
}
