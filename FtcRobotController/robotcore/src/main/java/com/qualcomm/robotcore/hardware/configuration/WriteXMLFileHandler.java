package com.qualcomm.robotcore.hardware.configuration;

import android.content.Context;
import android.util.Xml;

import com.qualcomm.robotcore.exception.RobotCoreException;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class WriteXMLFileHandler {
    private XmlSerializer a = Xml.newSerializer();
    private HashSet<String> b = new HashSet();
    private ArrayList<String> c = new ArrayList();
    private String[] d = new String[]{"    ", "        ", "            "};
    private int e = 0;

    public WriteXMLFileHandler(Context var1) {
    }

    private void a(ControllerConfiguration var1) throws IOException {
        this.a.ignorableWhitespace(this.d[this.e]);
        this.a.startTag("", this.b(var1.getType().toString()));
        this.a(var1.getName());
        this.a.attribute("", "name", var1.getName());
        this.a.attribute("", "serialNumber", var1.getSerialNumber().toString());
        this.a.ignorableWhitespace("\n");
        ++this.e;
        DeviceInterfaceModuleConfiguration var5 = (DeviceInterfaceModuleConfiguration) var1;
        Iterator var6 = ((ArrayList) var5.getPwmDevices()).iterator();

        while (var6.hasNext()) {
            this.a((DeviceConfiguration) var6.next());
        }

        Iterator var7 = ((ArrayList) var5.getI2cDevices()).iterator();

        while (var7.hasNext()) {
            this.a((DeviceConfiguration) var7.next());
        }

        Iterator var8 = ((ArrayList) var5.getAnalogInputDevices()).iterator();

        while (var8.hasNext()) {
            this.a((DeviceConfiguration) var8.next());
        }

        Iterator var9 = ((ArrayList) var5.getDigitalDevices()).iterator();

        while (var9.hasNext()) {
            this.a((DeviceConfiguration) var9.next());
        }

        Iterator var10 = ((ArrayList) var5.getAnalogOutputDevices()).iterator();

        while (var10.hasNext()) {
            this.a((DeviceConfiguration) var10.next());
        }

        this.e += -1;
        this.a.ignorableWhitespace(this.d[this.e]);
        this.a.endTag("", this.b(var1.getType().toString()));
        this.a.ignorableWhitespace("\n");
    }

    private void a(ControllerConfiguration var1, boolean var2) throws IOException {
        this.a.ignorableWhitespace(this.d[this.e]);
        this.a.startTag("", this.b(var1.getType().toString()));
        this.a(var1.getName());
        this.a.attribute("", "name", var1.getName());
        if (var2) {
            this.a.attribute("", "serialNumber", var1.getSerialNumber().toString());
        } else {
            this.a.attribute("", "port", String.valueOf(var1.getPort()));
        }

        this.a.ignorableWhitespace("\n");
        ++this.e;
        Iterator var6 = ((ArrayList) var1.getDevices()).iterator();

        while (var6.hasNext()) {
            DeviceConfiguration var12 = (DeviceConfiguration) var6.next();
            if (var12.isEnabled()) {
                this.a(var12);
            }
        }

        if (var1.getType() == DeviceConfiguration.ConfigurationType.MATRIX_CONTROLLER) {
            Iterator var8 = ((ArrayList) ((MatrixControllerConfiguration) var1).getMotors()).iterator();

            while (var8.hasNext()) {
                DeviceConfiguration var11 = (DeviceConfiguration) var8.next();
                if (var11.isEnabled()) {
                    this.a(var11);
                }
            }

            Iterator var9 = ((ArrayList) ((MatrixControllerConfiguration) var1).getServos()).iterator();

            while (var9.hasNext()) {
                DeviceConfiguration var10 = (DeviceConfiguration) var9.next();
                if (var10.isEnabled()) {
                    this.a(var10);
                }
            }
        }

        this.e += -1;
        this.a.ignorableWhitespace(this.d[this.e]);
        this.a.endTag("", this.b(var1.getType().toString()));
        this.a.ignorableWhitespace("\n");
    }

    private void a(DeviceConfiguration var1) {
        if (var1.isEnabled()) {
            try {
                this.a.ignorableWhitespace(this.d[this.e]);
                this.a.startTag("", this.b(var1.getType().toString()));
                this.a(var1.getName());
                this.a.attribute("", "name", var1.getName());
                this.a.attribute("", "port", String.valueOf(var1.getPort()));
                this.a.endTag("", this.b(var1.getType().toString()));
                this.a.ignorableWhitespace("\n");
            } catch (Exception var3) {
                throw new RuntimeException(var3);
            }
        }
    }

    private void a(String var1) {
        if (!var1.equalsIgnoreCase("NO DEVICE ATTACHED")) {
            if (this.b.contains(var1)) {
                this.c.add(var1);
            } else {
                this.b.add(var1);
            }
        }
    }

    private String b(String var1) {
        String var2 = var1.substring(0, 1) + var1.substring(1).toLowerCase();

        for (int var3 = var1.lastIndexOf("_"); var3 > 0; var3 = var2.lastIndexOf("_")) {
            int var4 = var3 + 1;
            String var5 = var2.substring(0, var3);
            String var6 = var2.substring(var4, var4 + 1).toUpperCase();
            String var7 = var2.substring(var4 + 1);
            var2 = var5 + var6 + var7;
        }

        return var2;
    }

    private void b(ControllerConfiguration var1) throws IOException {
        this.a.ignorableWhitespace(this.d[this.e]);
        this.a.startTag("", this.b(var1.getType().toString()));
        this.a(var1.getName());
        this.a.attribute("", "name", var1.getName());
        this.a.attribute("", "serialNumber", var1.getSerialNumber().toString());
        this.a.ignorableWhitespace("\n");
        ++this.e;
        Iterator var5 = ((ArrayList) var1.getDevices()).iterator();

        while (true) {
            while (var5.hasNext()) {
                DeviceConfiguration var7 = (DeviceConfiguration) var5.next();
                String var8 = var7.getType().toString();
                if (!var8.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER.toString()) && !var8.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER.toString()) && !var8.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MATRIX_CONTROLLER.toString())) {
                    if (var7.isEnabled()) {
                        this.a(var7);
                    }
                } else {
                    this.a((ControllerConfiguration) var7, false);
                }
            }

            this.e += -1;
            this.a.ignorableWhitespace(this.d[this.e]);
            this.a.endTag("", this.b(var1.getType().toString()));
            this.a.ignorableWhitespace("\n");
            return;
        }
    }

    public void writeToFile(String data, String folderName, String filename) throws RobotCoreException, IOException {
        if (this.c.size() > 0) {
            throw new IOException("Duplicate names: " + this.c);
        } else {
            filename = filename.replaceFirst("[.][^.]+$", "");
            File var4 = new File(folderName);
            boolean var5 = true;
            if (!var4.exists()) {
                var5 = var4.mkdir();
            }

            if (var5) {
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

            while (var3.hasNext()) {
                ControllerConfiguration var4 = (ControllerConfiguration) var3.next();
                String var5 = var4.getType().toString();
                if (var5.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER.toString()) || var5.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER.toString())) {
                    this.a(var4, true);
                }

                if (var5.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.LEGACY_MODULE_CONTROLLER.toString())) {
                    this.b(var4);
                }

                if (var5.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.DEVICE_INTERFACE_MODULE.toString())) {
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
}
