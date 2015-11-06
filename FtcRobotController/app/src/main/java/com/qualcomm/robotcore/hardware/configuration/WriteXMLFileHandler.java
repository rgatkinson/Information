//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.qualcomm.robotcore.hardware.configuration;

import android.content.Context;
import android.util.Xml;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceInterfaceModuleConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MatrixControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration.ConfigurationType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import org.xmlpull.v1.XmlSerializer;

public class WriteXMLFileHandler {
    private XmlSerializer a = Xml.newSerializer();
    private HashSet<String> b = new HashSet();
    private ArrayList<String> c = new ArrayList();
    private String[] d = new String[]{"    ", "        ", "            "};
    private int e = 0;

    public WriteXMLFileHandler(Context context) {
    }

    public String writeXml(ArrayList<ControllerConfiguration> deviceControllerConfigurations) {
        this.c = new ArrayList();
        this.b = new HashSet();
        StringWriter var2 = new StringWriter();

        try {
            this.a.setOutput(var2);
            this.a.startDocument("UTF-8", Boolean.valueOf(true));
            this.a.ignorableWhitespace("\n");
            this.a.startTag("", "Robot");
            this.a.ignorableWhitespace("\n");
            Iterator var3 = deviceControllerConfigurations.iterator();

            while(var3.hasNext()) {
                ControllerConfiguration var4 = (ControllerConfiguration)var3.next();
                String var5 = var4.getType().toString();
                if(var5.equalsIgnoreCase(ConfigurationType.MOTOR_CONTROLLER.toString()) || var5.equalsIgnoreCase(ConfigurationType.SERVO_CONTROLLER.toString())) {
                    this.a(var4, true);
                }

                if(var5.equalsIgnoreCase(ConfigurationType.LEGACY_MODULE_CONTROLLER.toString())) {
                    this.b(var4);
                }

                if(var5.equalsIgnoreCase(ConfigurationType.DEVICE_INTERFACE_MODULE.toString())) {
                    this.a(var4);
                }
            }

            this.a.endTag("", "Robot");
            this.a.ignorableWhitespace("\n");
            this.a.endDocument();
            return var2.toString();
        } catch (Exception var6) {
            throw new RuntimeException(var6);
        }
    }

    private void a(String var1) {
        if(!var1.equalsIgnoreCase("NO DEVICE ATTACHED")) {
            if(this.b.contains(var1)) {
                this.c.add(var1);
            } else {
                this.b.add(var1);
            }

        }
    }

    private void a(ControllerConfiguration var1) throws IOException {
        this.a.ignorableWhitespace(this.d[this.e]);
        this.a.startTag("", this.b(var1.getType().toString()));
        String var2 = var1.getName();
        this.a(var2);
        this.a.attribute("", "name", var1.getName());
        this.a.attribute("", "serialNumber", var1.getSerialNumber().toString());
        this.a.ignorableWhitespace("\n");
        ++this.e;
        DeviceInterfaceModuleConfiguration var3 = (DeviceInterfaceModuleConfiguration)var1;
        ArrayList var4 = (ArrayList)var3.getPwmDevices();
        Iterator var5 = var4.iterator();

        while(var5.hasNext()) {
            DeviceConfiguration var6 = (DeviceConfiguration)var5.next();
            this.a(var6);
        }

        ArrayList var11 = (ArrayList)var3.getI2cDevices();
        Iterator var12 = var11.iterator();

        while(var12.hasNext()) {
            DeviceConfiguration var7 = (DeviceConfiguration)var12.next();
            this.a(var7);
        }

        ArrayList var13 = (ArrayList)var3.getAnalogInputDevices();
        Iterator var14 = var13.iterator();

        while(var14.hasNext()) {
            DeviceConfiguration var8 = (DeviceConfiguration)var14.next();
            this.a(var8);
        }

        ArrayList var15 = (ArrayList)var3.getDigitalDevices();
        Iterator var16 = var15.iterator();

        while(var16.hasNext()) {
            DeviceConfiguration var9 = (DeviceConfiguration)var16.next();
            this.a(var9);
        }

        ArrayList var17 = (ArrayList)var3.getAnalogOutputDevices();
        Iterator var18 = var17.iterator();

        while(var18.hasNext()) {
            DeviceConfiguration var10 = (DeviceConfiguration)var18.next();
            this.a(var10);
        }

        --this.e;
        this.a.ignorableWhitespace(this.d[this.e]);
        this.a.endTag("", this.b(var1.getType().toString()));
        this.a.ignorableWhitespace("\n");
    }

    private void b(ControllerConfiguration var1) throws IOException {
        this.a.ignorableWhitespace(this.d[this.e]);
        this.a.startTag("", this.b(var1.getType().toString()));
        String var2 = var1.getName();
        this.a(var2);
        this.a.attribute("", "name", var1.getName());
        this.a.attribute("", "serialNumber", var1.getSerialNumber().toString());
        this.a.ignorableWhitespace("\n");
        ++this.e;
        ArrayList var3 = (ArrayList)var1.getDevices();
        Iterator var4 = var3.iterator();

        while(true) {
            while(var4.hasNext()) {
                DeviceConfiguration var5 = (DeviceConfiguration)var4.next();
                String var6 = var5.getType().toString();
                if(!var6.equalsIgnoreCase(ConfigurationType.MOTOR_CONTROLLER.toString()) && !var6.equalsIgnoreCase(ConfigurationType.SERVO_CONTROLLER.toString()) && !var6.equalsIgnoreCase(ConfigurationType.MATRIX_CONTROLLER.toString())) {
                    if(var5.isEnabled()) {
                        this.a(var5);
                    }
                } else {
                    this.a((ControllerConfiguration)var5, false);
                }
            }

            --this.e;
            this.a.ignorableWhitespace(this.d[this.e]);
            this.a.endTag("", this.b(var1.getType().toString()));
            this.a.ignorableWhitespace("\n");
            return;
        }
    }

    private void a(ControllerConfiguration var1, boolean var2) throws IOException {
        this.a.ignorableWhitespace(this.d[this.e]);
        this.a.startTag("", this.b(var1.getType().toString()));
        String var3 = var1.getName();
        this.a(var3);
        this.a.attribute("", "name", var1.getName());
        if(var2) {
            this.a.attribute("", "serialNumber", var1.getSerialNumber().toString());
        } else {
            this.a.attribute("", "port", String.valueOf(var1.getPort()));
        }

        this.a.ignorableWhitespace("\n");
        ++this.e;
        ArrayList var4 = (ArrayList)var1.getDevices();
        Iterator var5 = var4.iterator();

        while(var5.hasNext()) {
            DeviceConfiguration var6 = (DeviceConfiguration)var5.next();
            if(var6.isEnabled()) {
                this.a(var6);
            }
        }

        if(var1.getType() == ConfigurationType.MATRIX_CONTROLLER) {
            ArrayList var9 = (ArrayList)((MatrixControllerConfiguration)var1).getMotors();
            Iterator var10 = var9.iterator();

            while(var10.hasNext()) {
                DeviceConfiguration var7 = (DeviceConfiguration)var10.next();
                if(var7.isEnabled()) {
                    this.a(var7);
                }
            }

            ArrayList var11 = (ArrayList)((MatrixControllerConfiguration)var1).getServos();
            Iterator var12 = var11.iterator();

            while(var12.hasNext()) {
                DeviceConfiguration var8 = (DeviceConfiguration)var12.next();
                if(var8.isEnabled()) {
                    this.a(var8);
                }
            }
        }

        --this.e;
        this.a.ignorableWhitespace(this.d[this.e]);
        this.a.endTag("", this.b(var1.getType().toString()));
        this.a.ignorableWhitespace("\n");
    }

    private void a(DeviceConfiguration var1) {
        if(var1.isEnabled()) {
            try {
                this.a.ignorableWhitespace(this.d[this.e]);
                this.a.startTag("", this.b(var1.getType().toString()));
                String var2 = var1.getName();
                this.a(var2);
                this.a.attribute("", "name", var1.getName());
                this.a.attribute("", "port", String.valueOf(var1.getPort()));
                this.a.endTag("", this.b(var1.getType().toString()));
                this.a.ignorableWhitespace("\n");
            } catch (Exception var3) {
                throw new RuntimeException(var3);
            }
        }
    }

    public void writeToFile(String data, String folderName, String filename) throws RobotCoreException, IOException {
        if(this.c.size() > 0) {
            throw new IOException("Duplicate names: " + this.c);
        } else {
            filename = filename.replaceFirst("[.][^.]+$", "");
            File var4 = new File(folderName);
            boolean var5 = true;
            if(!var4.exists()) {
                var5 = var4.mkdir();
            }

            if(var5) {
                File var6 = new File(folderName + filename + ".xml");
                FileOutputStream var7 = null;

                try {
                    var7 = new FileOutputStream(var6);
                    var7.write(data.getBytes());
                } catch (Exception var17) {
                    var17.printStackTrace();
                } finally {
                    try {
                        var7.close();
                    } catch (IOException var16) {
                        var16.printStackTrace();
                    }

                }

            } else {
                throw new RobotCoreException("Unable to create directory");
            }
        }
    }

    private String b(String var1) {
        String var2 = var1.substring(0, 1) + var1.substring(1).toLowerCase();

        for(int var3 = var1.lastIndexOf("_"); var3 > 0; var3 = var2.lastIndexOf("_")) {
            int var4 = var3 + 1;
            String var5 = var2.substring(0, var3);
            String var6 = var2.substring(var4, var4 + 1).toUpperCase();
            String var7 = var2.substring(var4 + 1);
            var2 = var5 + var6 + var7;
        }

        return var2;
    }
}
