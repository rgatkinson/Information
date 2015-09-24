package com.qualcomm.robotcore.hardware.configuration;

import java.util.Collection;
import java.io.IOException;
import com.qualcomm.robotcore.exception.RobotCoreException;
import android.graphics.Color;
import android.view.ViewGroup;
import android.content.SharedPreferences$Editor;
import java.util.Iterator;
import com.qualcomm.robotcore.hardware.DeviceManager;
import java.util.Map;
import java.util.Set;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.File;
import java.util.ArrayList;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import java.util.List;
import com.qualcomm.robotcore.util.SerialNumber;
import android.app.AlertDialog$Builder;
import android.content.Context;
import android.preference.PreferenceManager;
import android.os.Environment;
import android.content.SharedPreferences;
import android.app.Activity;

public class Utility
{
    public static final String AUTOCONFIGURE_K9LEGACYBOT = "K9LegacyBot";
    public static final String AUTOCONFIGURE_K9USBBOT = "K9USBBot";
    public static final String CONFIG_FILES_DIR;
    public static final String DEFAULT_ROBOT_CONFIG = "robot_config";
    public static final String DEFAULT_ROBOT_CONFIG_FILENAME = "robot_config.xml";
    public static final String FILE_EXT = ".xml";
    public static final String NO_FILE = "No current file!";
    public static final String UNSAVED = "Unsaved";
    private static int c;
    private Activity a;
    private SharedPreferences b;
    private WriteXMLFileHandler d;
    private String e;
    
    static {
        CONFIG_FILES_DIR = Environment.getExternalStorageDirectory() + "/FIRST/";
        Utility.c = 1;
    }
    
    public Utility(final Activity a) {
        this.a = a;
        this.b = PreferenceManager.getDefaultSharedPreferences((Context)a);
        this.d = new WriteXMLFileHandler((Context)a);
    }
    
    public AlertDialog$Builder buildBuilder(final String title, final String message) {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this.a);
        alertDialog$Builder.setTitle((CharSequence)title).setMessage((CharSequence)message);
        return alertDialog$Builder;
    }
    
    public DeviceInterfaceModuleConfiguration buildDeviceInterfaceModule(final SerialNumber serialNumber) {
        final DeviceInterfaceModuleConfiguration deviceInterfaceModuleConfiguration = new DeviceInterfaceModuleConfiguration("Device Interface Module " + Utility.c, serialNumber);
        deviceInterfaceModuleConfiguration.setPwmDevices(this.createPWMList());
        deviceInterfaceModuleConfiguration.setI2cDevices(this.createI2CList());
        deviceInterfaceModuleConfiguration.setAnalogInputDevices(this.createAnalogInputList());
        deviceInterfaceModuleConfiguration.setDigitalDevices(this.createDigitalList());
        deviceInterfaceModuleConfiguration.setAnalogOutputDevices(this.createAnalogOutputList());
        ++Utility.c;
        return deviceInterfaceModuleConfiguration;
    }
    
    public LegacyModuleControllerConfiguration buildLegacyModule(final SerialNumber serialNumber) {
        final LegacyModuleControllerConfiguration legacyModuleControllerConfiguration = new LegacyModuleControllerConfiguration("Legacy Module " + Utility.c, this.createLegacyModuleList(), serialNumber);
        ++Utility.c;
        return legacyModuleControllerConfiguration;
    }
    
    public MotorControllerConfiguration buildMotorController(final SerialNumber serialNumber) {
        final MotorControllerConfiguration motorControllerConfiguration = new MotorControllerConfiguration("Motor Controller " + Utility.c, this.createMotorList(), serialNumber);
        ++Utility.c;
        return motorControllerConfiguration;
    }
    
    public ServoControllerConfiguration buildServoController(final SerialNumber serialNumber) {
        final ServoControllerConfiguration servoControllerConfiguration = new ServoControllerConfiguration("Servo Controller " + Utility.c, this.createServoList(), serialNumber);
        ++Utility.c;
        return servoControllerConfiguration;
    }
    
    public void changeBackground(final int backgroundColor, final int n) {
        ((LinearLayout)this.a.findViewById(n)).setBackgroundColor(backgroundColor);
    }
    
    public void complainToast(final String s, final Context context) {
        final Toast text = Toast.makeText(context, (CharSequence)s, 0);
        text.setGravity(17, 0, 0);
        final TextView textView = (TextView)text.getView().findViewById(16908299);
        textView.setTextColor(-1);
        textView.setTextSize(18.0f);
        text.show();
    }
    
    public void confirmSave() {
        final Toast text = Toast.makeText((Context)this.a, (CharSequence)"Saved", 0);
        text.setGravity(80, 0, 50);
        text.show();
    }
    
    public ArrayList<DeviceConfiguration> createAnalogInputList() {
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        for (int i = 0; i < 8; ++i) {
            list.add(new DeviceConfiguration(i, DeviceConfiguration.ConfigurationType.ANALOG_INPUT));
        }
        return list;
    }
    
    public ArrayList<DeviceConfiguration> createAnalogOutputList() {
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        for (int i = 0; i < 2; ++i) {
            list.add(new DeviceConfiguration(i, DeviceConfiguration.ConfigurationType.ANALOG_OUTPUT));
        }
        return list;
    }
    
    public void createConfigFolder() {
        final File file = new File(Utility.CONFIG_FILES_DIR);
        boolean mkdir = true;
        if (!file.exists()) {
            mkdir = file.mkdir();
        }
        if (!mkdir) {
            RobotLog.e("Can't create the Robot Config Files directory!");
            this.complainToast("Can't create the Robot Config Files directory!", (Context)this.a);
        }
    }
    
    public ArrayList<DeviceConfiguration> createDigitalList() {
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        for (int i = 0; i < 8; ++i) {
            list.add(new DeviceConfiguration(i, DeviceConfiguration.ConfigurationType.DIGITAL_DEVICE));
        }
        return list;
    }
    
    public ArrayList<DeviceConfiguration> createI2CList() {
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        for (int i = 0; i < 6; ++i) {
            list.add(new DeviceConfiguration(i, DeviceConfiguration.ConfigurationType.I2C_DEVICE));
        }
        return list;
    }
    
    public ArrayList<DeviceConfiguration> createLegacyModuleList() {
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        for (int i = 0; i < 6; ++i) {
            list.add(new DeviceConfiguration(i, DeviceConfiguration.ConfigurationType.NOTHING));
        }
        return list;
    }
    
    public void createLists(final Set<Map.Entry<SerialNumber, DeviceManager.DeviceType>> set, final Map<SerialNumber, ControllerConfiguration> map) {
        for (final Map.Entry<SerialNumber, DeviceManager.DeviceType> entry : set) {
            switch (Utility$1.a[entry.getValue().ordinal()]) {
                default: {
                    continue;
                }
                case 1: {
                    map.put(entry.getKey(), this.buildMotorController(entry.getKey()));
                    continue;
                }
                case 2: {
                    map.put(entry.getKey(), this.buildServoController(entry.getKey()));
                    continue;
                }
                case 3: {
                    map.put(entry.getKey(), this.buildLegacyModule(entry.getKey()));
                    continue;
                }
                case 4: {
                    map.put(entry.getKey(), this.buildDeviceInterfaceModule(entry.getKey()));
                    continue;
                }
            }
        }
    }
    
    public ArrayList<DeviceConfiguration> createMotorList() {
        final ArrayList<MotorConfiguration> list = (ArrayList<MotorConfiguration>)new ArrayList<DeviceConfiguration>();
        list.add(new MotorConfiguration(1));
        list.add(new MotorConfiguration(2));
        return (ArrayList<DeviceConfiguration>)list;
    }
    
    public ArrayList<DeviceConfiguration> createPWMList() {
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        for (int i = 0; i < 2; ++i) {
            list.add(new DeviceConfiguration(i, DeviceConfiguration.ConfigurationType.PULSE_WIDTH_DEVICE));
        }
        return list;
    }
    
    public ArrayList<DeviceConfiguration> createServoList() {
        final ArrayList<ServoConfiguration> list = (ArrayList<ServoConfiguration>)new ArrayList<DeviceConfiguration>();
        for (int i = 1; i <= 6; ++i) {
            list.add(new ServoConfiguration(i));
        }
        return (ArrayList<DeviceConfiguration>)list;
    }
    
    public String getFilenameFromPrefs(final int n, final String s) {
        return this.b.getString(this.a.getString(n), s);
    }
    
    public String getOutput() {
        return this.e;
    }
    
    public ArrayList<String> getXMLFiles() {
        final File[] listFiles = new File(Utility.CONFIG_FILES_DIR).listFiles();
        ArrayList<String> list;
        if (listFiles == null) {
            RobotLog.i("robotConfigFiles directory is empty");
            list = new ArrayList<String>();
        }
        else {
            list = new ArrayList<String>();
            for (final File file : listFiles) {
                if (file.isFile()) {
                    list.add(file.getName().replaceFirst("[.][^.]+$", ""));
                }
            }
        }
        return list;
    }
    
    public String prepareFilename(String trim) {
        if (trim.toLowerCase().contains("Unsaved".toLowerCase())) {
            trim = trim.substring(7).trim();
        }
        if (trim.equalsIgnoreCase("No current file!")) {
            trim = "";
        }
        return trim;
    }
    
    public void resetCount() {
        Utility.c = 1;
    }
    
    public void saveToPreferences(final String s, final int n) {
        final String replaceFirst = s.replaceFirst("[.][^.]+$", "");
        final SharedPreferences$Editor edit = this.b.edit();
        edit.putString(this.a.getString(n), replaceFirst);
        edit.apply();
    }
    
    public void setOrangeText(final String text, final String text2, final int n, final int n2, final int n3, final int n4) {
        final LinearLayout linearLayout = (LinearLayout)this.a.findViewById(n);
        linearLayout.setVisibility(0);
        linearLayout.removeAllViews();
        this.a.getLayoutInflater().inflate(n2, (ViewGroup)linearLayout, true);
        final TextView textView = (TextView)linearLayout.findViewById(n3);
        final TextView textView2 = (TextView)linearLayout.findViewById(n4);
        textView2.setGravity(3);
        textView.setText((CharSequence)text);
        textView2.setText((CharSequence)text2);
    }
    
    public void updateHeader(final String s, final int n, final int n2, final int n3) {
        final String replaceFirst = this.b.getString(this.a.getString(n), s).replaceFirst("[.][^.]+$", "");
        ((TextView)this.a.findViewById(n2)).setText((CharSequence)replaceFirst);
        if (replaceFirst.equalsIgnoreCase("No current file!")) {
            this.changeBackground(Color.parseColor("#bf0510"), n3);
            return;
        }
        if (replaceFirst.toLowerCase().contains("Unsaved".toLowerCase())) {
            this.changeBackground(-12303292, n3);
            return;
        }
        this.changeBackground(Color.parseColor("#790E15"), n3);
    }
    
    public void writeToFile(final String s) throws RobotCoreException, IOException {
        this.d.writeToFile(this.e, Utility.CONFIG_FILES_DIR, s);
    }
    
    public boolean writeXML(final Map<SerialNumber, ControllerConfiguration> map) {
        final ArrayList<ControllerConfiguration> list = new ArrayList<ControllerConfiguration>();
        list.addAll(map.values());
        try {
            this.e = this.d.writeXml(list);
            return false;
        }
        catch (RuntimeException ex) {
            if (ex.getMessage().contains("Duplicate name")) {
                this.complainToast("Found " + ex.getMessage(), (Context)this.a);
                RobotLog.e("Found " + ex.getMessage());
                return true;
            }
            return false;
        }
    }
}
