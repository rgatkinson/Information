package com.qualcomm.ftccommon.configuration;

import android.view.View;
import android.view.View$OnClickListener;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.hardware.ModernRoboticsDeviceManager;
import android.os.Bundle;
import java.util.Iterator;
import android.widget.LinearLayout;
import java.io.IOException;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.ftccommon.DbgLog;
import android.widget.Toast;
import com.qualcomm.robotcore.hardware.configuration.LegacyModuleControllerConfiguration;
import com.qualcomm.ftccommon.R;
import com.qualcomm.robotcore.hardware.configuration.ServoConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
import java.util.List;
import com.qualcomm.robotcore.hardware.configuration.MotorConfiguration;
import java.util.ArrayList;
import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import java.util.HashSet;
import java.util.HashMap;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import java.util.Set;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.Map;
import com.qualcomm.robotcore.hardware.DeviceManager;
import android.widget.Button;
import android.content.Context;
import android.app.Activity;

public class AutoConfigureActivity extends Activity
{
    private Context a;
    private Button b;
    private Button c;
    private DeviceManager d;
    private Map<SerialNumber, ControllerConfiguration> e;
    protected Set<Map.Entry<SerialNumber, DeviceManager.DeviceType>> entries;
    private Thread f;
    private Utility g;
    protected Map<SerialNumber, DeviceManager.DeviceType> scannedDevices;
    
    public AutoConfigureActivity() {
        this.scannedDevices = new HashMap<SerialNumber, DeviceManager.DeviceType>();
        this.entries = new HashSet<Map.Entry<SerialNumber, DeviceManager.DeviceType>>();
        this.e = new HashMap<SerialNumber, ControllerConfiguration>();
    }
    
    private DeviceConfiguration a(final DeviceConfiguration.ConfigurationType configurationType, final String s, final int n) {
        return new DeviceConfiguration(n, configurationType, s, true);
    }
    
    private MotorControllerConfiguration a(final SerialNumber serialNumber, final String s, final String s2, final String name) {
        MotorControllerConfiguration motorControllerConfiguration;
        if (!serialNumber.equals(ControllerConfiguration.NO_SERIAL_NUMBER)) {
            motorControllerConfiguration = this.e.get(serialNumber);
        }
        else {
            motorControllerConfiguration = new MotorControllerConfiguration();
        }
        motorControllerConfiguration.setName(name);
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        final MotorConfiguration motorConfiguration = new MotorConfiguration(1, s, true);
        final MotorConfiguration motorConfiguration2 = new MotorConfiguration(2, s2, true);
        list.add(motorConfiguration);
        list.add(motorConfiguration2);
        motorControllerConfiguration.addMotors(list);
        return motorControllerConfiguration;
    }
    
    private ServoControllerConfiguration a(final SerialNumber serialNumber, final ArrayList<String> list, final String name) {
        ServoControllerConfiguration servoControllerConfiguration;
        if (!serialNumber.equals(ControllerConfiguration.NO_SERIAL_NUMBER)) {
            servoControllerConfiguration = this.e.get(serialNumber);
        }
        else {
            servoControllerConfiguration = new ServoControllerConfiguration();
        }
        servoControllerConfiguration.setName(name);
        final ArrayList<DeviceConfiguration> list2 = new ArrayList<DeviceConfiguration>();
        for (int i = 1; i <= 6; ++i) {
            final String s = list.get(i - 1);
            list2.add(new ServoConfiguration(i, s, !s.equals("NO DEVICE ATTACHED")));
        }
        servoControllerConfiguration.addServos(list2);
        return servoControllerConfiguration;
    }
    
    private void a() {
        this.g.setOrangeText("No devices found!", "To configure K9LegacyBot, please: \n   1. Attach a LegacyModuleController, \n       with \n       a. MotorController in port 0, with a \n         motor in port 1 and port 2 \n       b. ServoController in port 1, with a \n         servo in port 1 and port 6 \n      c. IR seeker in port 2\n      d. Light sensor in port 3 \n   2. Press the K9LegacyBot button\n \nTo configure K9USBBot, please: \n   1. Attach a USBMotorController, with a \n       motor in port 1 and port 2 \n    2. USBServoController in port 1, with a \n      servo in port 1 and port 6 \n   3. LegacyModule, with \n      a. IR seeker in port 2\n      b. Light sensor in port 3 \n   4. Press the K9USBBot button", R.id.autoconfigure_info, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
    }
    
    private void a(final SerialNumber serialNumber, final String name) {
        final LegacyModuleControllerConfiguration legacyModuleControllerConfiguration = this.e.get(serialNumber);
        legacyModuleControllerConfiguration.setName(name);
        final DeviceConfiguration a = this.a(DeviceConfiguration.ConfigurationType.IR_SEEKER, "ir_seeker", 2);
        final DeviceConfiguration a2 = this.a(DeviceConfiguration.ConfigurationType.LIGHT_SENSOR, "light_sensor", 3);
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        for (int i = 0; i < 6; ++i) {
            if (i == 2) {
                list.add(a);
            }
            if (i == 3) {
                list.add(a2);
            }
            else {
                list.add(new DeviceConfiguration(i));
            }
        }
        legacyModuleControllerConfiguration.addDevices(list);
    }
    
    private void a(final String s) {
        this.g.writeXML(this.e);
        try {
            this.g.writeToFile(s + ".xml");
            this.g.saveToPreferences(s, R.string.pref_hardware_config_filename);
            this.g.updateHeader(s, R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
            Toast.makeText(this.a, (CharSequence)("AutoConfigure " + s + " Successful"), 0).show();
        }
        catch (RobotCoreException ex) {
            this.g.complainToast(ex.getMessage(), this.a);
            DbgLog.error(ex.getMessage());
        }
        catch (IOException ex2) {
            this.g.complainToast("Found " + ex2.getMessage() + "\n Please fix and re-save", this.a);
            DbgLog.error(ex2.getMessage());
        }
    }
    
    private void b() {
        this.g.setOrangeText("Wrong devices found!", "Found: \n" + this.scannedDevices.values() + "\n" + "Required: \n" + "   1. LegacyModuleController, with \n " + "      a. MotorController in port 0, with a \n" + "          motor in port 1 and port 2 \n " + "      b. ServoController in port 1, with a \n" + "          servo in port 1 and port 6 \n" + "       c. IR seeker in port 2\n" + "       d. Light sensor in port 3 ", R.id.autoconfigure_info, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
    }
    
    private void b(final SerialNumber serialNumber, final String name) {
        final LegacyModuleControllerConfiguration legacyModuleControllerConfiguration = this.e.get(serialNumber);
        legacyModuleControllerConfiguration.setName(name);
        final MotorControllerConfiguration a = this.a(ControllerConfiguration.NO_SERIAL_NUMBER, "motor_1", "motor_2", "wheels");
        a.setPort(0);
        final ServoControllerConfiguration a2 = this.a(ControllerConfiguration.NO_SERIAL_NUMBER, this.f(), "servos");
        a2.setPort(1);
        final DeviceConfiguration a3 = this.a(DeviceConfiguration.ConfigurationType.IR_SEEKER, "ir_seeker", 2);
        final DeviceConfiguration a4 = this.a(DeviceConfiguration.ConfigurationType.LIGHT_SENSOR, "light_sensor", 3);
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        list.add(a);
        list.add(a2);
        list.add(a3);
        list.add(a4);
        for (int i = 4; i < 6; ++i) {
            list.add(new DeviceConfiguration(i));
        }
        legacyModuleControllerConfiguration.addDevices(list);
    }
    
    private void c() {
        this.g.setOrangeText("Wrong devices found!", "Found: \n" + this.scannedDevices.values() + "\n" + "Required: \n" + "   1. USBMotorController with a \n" + "      motor in port 1 and port 2 \n " + "   2. USBServoController with a \n" + "      servo in port 1 and port 6 \n" + "   3. LegacyModuleController, with \n " + "       a. IR seeker in port 2\n" + "       b. Light sensor in port 3 ", R.id.autoconfigure_info, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
    }
    
    private void d() {
        this.g.setOrangeText("Already configured!", "", R.id.autoconfigure_info, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
    }
    
    private boolean e() {
        final Iterator<Map.Entry<SerialNumber, DeviceManager.DeviceType>> iterator = this.entries.iterator();
        int n = 1;
        int n2 = 1;
        int n3 = 1;
        while (iterator.hasNext()) {
            final Map.Entry<SerialNumber, DeviceManager.DeviceType> entry = iterator.next();
            final DeviceManager.DeviceType deviceType = entry.getValue();
            int n4;
            if (deviceType == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE && n3 != 0) {
                this.a(entry.getKey(), "sensors");
                n4 = 0;
            }
            else {
                n4 = n3;
            }
            int n5;
            if (deviceType == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER && n2 != 0) {
                this.a(entry.getKey(), "motor_1", "motor_2", "wheels");
                n5 = 0;
            }
            else {
                n5 = n2;
            }
            int n6;
            if (deviceType == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER && n != 0) {
                this.a(entry.getKey(), this.f(), "servos");
                n6 = 0;
            }
            else {
                n6 = n;
            }
            n = n6;
            n2 = n5;
            n3 = n4;
        }
        if (n3 != 0 || n2 != 0 || n != 0) {
            return false;
        }
        ((LinearLayout)this.findViewById(R.id.autoconfigure_info)).removeAllViews();
        return true;
    }
    
    private ArrayList<String> f() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("servo_1");
        list.add("NO DEVICE ATTACHED");
        list.add("NO DEVICE ATTACHED");
        list.add("NO DEVICE ATTACHED");
        list.add("NO DEVICE ATTACHED");
        list.add("servo_6");
        return list;
    }
    
    private boolean g() {
        final Iterator<Map.Entry<SerialNumber, DeviceManager.DeviceType>> iterator = this.entries.iterator();
        int n = 1;
        while (iterator.hasNext()) {
            final Map.Entry<SerialNumber, DeviceManager.DeviceType> entry = iterator.next();
            int n2;
            if ((DeviceManager.DeviceType)entry.getValue() == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE && n != 0) {
                this.b(entry.getKey(), "devices");
                n2 = 0;
            }
            else {
                n2 = n;
            }
            n = n2;
        }
        if (n != 0) {
            return false;
        }
        ((LinearLayout)this.findViewById(R.id.autoconfigure_info)).removeAllViews();
        return true;
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        ((AutoConfigureActivity)(this.a = (Context)this)).setContentView(R.layout.activity_autoconfigure);
        this.g = new Utility(this);
        this.b = (Button)this.findViewById(R.id.configureLegacy);
        this.c = (Button)this.findViewById(R.id.configureUSB);
        try {
            this.d = new ModernRoboticsDeviceManager(this.a, null);
        }
        catch (RobotCoreException ex) {
            this.g.complainToast("Failed to open the Device Manager", this.a);
            DbgLog.error("Failed to open deviceManager: " + ex.toString());
            DbgLog.logStacktrace(ex);
        }
    }
    
    protected void onStart() {
        super.onStart();
        this.g.updateHeader("K9LegacyBot", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
        final String filenameFromPrefs = this.g.getFilenameFromPrefs(R.string.pref_hardware_config_filename, "No current file!");
        if (filenameFromPrefs.equals("K9LegacyBot") || filenameFromPrefs.equals("K9USBBot")) {
            this.d();
        }
        else {
            this.a();
        }
        this.b.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                AutoConfigureActivity.this.f = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                DbgLog.msg("Scanning USB bus");
                                AutoConfigureActivity.this.scannedDevices = AutoConfigureActivity.this.d.scanForUsbDevices();
                                AutoConfigureActivity.this.runOnUiThread((Runnable)new Runnable() {
                                    @Override
                                    public void run() {
                                        AutoConfigureActivity.this.g.resetCount();
                                        if (AutoConfigureActivity.this.scannedDevices.size() == 0) {
                                            AutoConfigureActivity.this.g.saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
                                            AutoConfigureActivity.this.g.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                                            AutoConfigureActivity.this.a();
                                        }
                                        AutoConfigureActivity.this.entries = AutoConfigureActivity.this.scannedDevices.entrySet();
                                        AutoConfigureActivity.this.e = (Map<SerialNumber, ControllerConfiguration>)new HashMap();
                                        AutoConfigureActivity.this.g.createLists(AutoConfigureActivity.this.entries, AutoConfigureActivity.this.e);
                                        if (AutoConfigureActivity.this.g()) {
                                            AutoConfigureActivity.this.a("K9LegacyBot");
                                            return;
                                        }
                                        AutoConfigureActivity.this.g.saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
                                        AutoConfigureActivity.this.g.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                                        AutoConfigureActivity.this.b();
                                    }
                                });
                            }
                            catch (RobotCoreException ex) {
                                DbgLog.error("Device scan failed");
                                continue;
                            }
                            break;
                        }
                    }
                });
                AutoConfigureActivity.this.f.start();
            }
        });
        this.c.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                AutoConfigureActivity.this.f = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                DbgLog.msg("Scanning USB bus");
                                AutoConfigureActivity.this.scannedDevices = AutoConfigureActivity.this.d.scanForUsbDevices();
                                AutoConfigureActivity.this.runOnUiThread((Runnable)new Runnable() {
                                    @Override
                                    public void run() {
                                        AutoConfigureActivity.this.g.resetCount();
                                        if (AutoConfigureActivity.this.scannedDevices.size() == 0) {
                                            AutoConfigureActivity.this.g.saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
                                            AutoConfigureActivity.this.g.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                                            AutoConfigureActivity.this.a();
                                        }
                                        AutoConfigureActivity.this.entries = AutoConfigureActivity.this.scannedDevices.entrySet();
                                        AutoConfigureActivity.this.e = (Map<SerialNumber, ControllerConfiguration>)new HashMap();
                                        AutoConfigureActivity.this.g.createLists(AutoConfigureActivity.this.entries, AutoConfigureActivity.this.e);
                                        if (AutoConfigureActivity.this.e()) {
                                            AutoConfigureActivity.this.a("K9USBBot");
                                            return;
                                        }
                                        AutoConfigureActivity.this.g.saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
                                        AutoConfigureActivity.this.g.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                                        AutoConfigureActivity.this.c();
                                    }
                                });
                            }
                            catch (RobotCoreException ex) {
                                DbgLog.error("Device scan failed");
                                continue;
                            }
                            break;
                        }
                    }
                });
                AutoConfigureActivity.this.f.start();
            }
        });
    }
}
