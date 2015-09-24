package com.qualcomm.ftccommon.configuration;

import android.preference.PreferenceManager;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.hardware.ModernRoboticsDeviceManager;
import android.os.Bundle;
import java.io.IOException;
import java.io.Serializable;
import android.content.Intent;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemClickListener;
import android.widget.ListAdapter;
import com.qualcomm.robotcore.hardware.configuration.DeviceInfoAdapter;
import android.widget.ListView;
import android.widget.LinearLayout;
import java.io.FileNotFoundException;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.InputStream;
import java.io.FileInputStream;
import com.qualcomm.robotcore.hardware.configuration.ReadXMLFileHandler;
import android.widget.EditText;
import com.qualcomm.robotcore.hardware.configuration.DeviceInterfaceModuleConfiguration;
import java.util.Iterator;
import java.util.ArrayList;
import android.app.AlertDialog;
import android.app.AlertDialog$Builder;
import android.widget.TextView;
import android.view.View;
import android.view.View$OnClickListener;
import com.qualcomm.ftccommon.R;
import android.content.DialogInterface;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import android.content.SharedPreferences;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import android.widget.Button;
import com.qualcomm.robotcore.hardware.DeviceManager;
import android.content.Context;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.Map;
import android.content.DialogInterface$OnClickListener;
import android.app.Activity;

public class FtcConfigurationActivity extends Activity
{
    DialogInterface$OnClickListener a;
    DialogInterface$OnClickListener b;
    DialogInterface$OnClickListener c;
    private Thread d;
    private Map<SerialNumber, ControllerConfiguration> e;
    private Context f;
    private DeviceManager g;
    private Button h;
    private String i;
    private Utility j;
    protected SharedPreferences preferences;
    protected Map<SerialNumber, DeviceManager.DeviceType> scannedDevices;
    protected Set<Map.Entry<SerialNumber, DeviceManager.DeviceType>> scannedEntries;
    
    public FtcConfigurationActivity() {
        this.e = new HashMap<SerialNumber, ControllerConfiguration>();
        this.i = "No current file!";
        this.scannedDevices = new HashMap<SerialNumber, DeviceManager.DeviceType>();
        this.scannedEntries = new HashSet<Map.Entry<SerialNumber, DeviceManager.DeviceType>>();
        this.a = (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
            }
        };
        this.b = (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                FtcConfigurationActivity.this.j.saveToPreferences(FtcConfigurationActivity.this.i.substring(7).trim(), R.string.pref_hardware_config_filename);
                FtcConfigurationActivity.this.finish();
            }
        };
        this.c = (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
            }
        };
    }
    
    private void a() {
        ((Button)this.findViewById(R.id.devices_holder).findViewById(R.id.info_btn)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final AlertDialog$Builder buildBuilder = FtcConfigurationActivity.this.j.buildBuilder("Devices", "These are the devices discovered by the Hardware Wizard. You can click on the name of each device to edit the information relating to that device. Make sure each device has a unique name. The names should match what is in the Op mode code. Scroll down to see more devices if there are any.");
                buildBuilder.setPositiveButton((CharSequence)"Ok", FtcConfigurationActivity.this.a);
                final AlertDialog create = buildBuilder.create();
                create.show();
                ((TextView)create.findViewById(16908299)).setTextSize(14.0f);
            }
        });
        ((Button)this.findViewById(R.id.save_holder).findViewById(R.id.info_btn)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final AlertDialog$Builder buildBuilder = FtcConfigurationActivity.this.j.buildBuilder("Save Configuration", "Clicking the save button will create an xml file in: \n      /sdcard/FIRST/  \nThis file will be used to initialize the robot.");
                buildBuilder.setPositiveButton((CharSequence)"Ok", FtcConfigurationActivity.this.a);
                final AlertDialog create = buildBuilder.create();
                create.show();
                ((TextView)create.findViewById(16908299)).setTextSize(14.0f);
            }
        });
    }
    
    private void a(final String s) {
        this.j.setOrangeText("Found " + s, "Please fix and re-save.", R.id.warning_layout, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
    }
    
    private void a(final ArrayList<ControllerConfiguration> list) {
        this.e = new HashMap<SerialNumber, ControllerConfiguration>();
        for (final ControllerConfiguration controllerConfiguration : list) {
            this.e.put(controllerConfiguration.getSerialNumber(), controllerConfiguration);
        }
    }
    
    private HashMap<SerialNumber, ControllerConfiguration> b() {
        final HashMap<SerialNumber, DeviceInterfaceModuleConfiguration> hashMap = new HashMap<SerialNumber, DeviceInterfaceModuleConfiguration>();
        for (final Map.Entry<SerialNumber, DeviceManager.DeviceType> entry : this.scannedEntries) {
            final SerialNumber serialNumber = entry.getKey();
            if (this.e.containsKey(serialNumber)) {
                hashMap.put(serialNumber, (DeviceInterfaceModuleConfiguration)this.e.get(serialNumber));
            }
            else {
                switch (FtcConfigurationActivity$3.a[entry.getValue().ordinal()]) {
                    default: {
                        continue;
                    }
                    case 1: {
                        hashMap.put(serialNumber, (DeviceInterfaceModuleConfiguration)this.j.buildMotorController(serialNumber));
                        continue;
                    }
                    case 2: {
                        hashMap.put(serialNumber, (DeviceInterfaceModuleConfiguration)this.j.buildServoController(serialNumber));
                        continue;
                    }
                    case 3: {
                        hashMap.put(serialNumber, (DeviceInterfaceModuleConfiguration)this.j.buildLegacyModule(serialNumber));
                        continue;
                    }
                    case 4: {
                        hashMap.put(serialNumber, this.j.buildDeviceInterfaceModule(serialNumber));
                        continue;
                    }
                }
            }
        }
        return (HashMap<SerialNumber, ControllerConfiguration>)hashMap;
    }
    
    private void c() {
        if (this.i.toLowerCase().contains("Unsaved".toLowerCase())) {
            final EditText view = new EditText(this.f);
            view.setEnabled(false);
            view.setText((CharSequence)"");
            final AlertDialog$Builder buildBuilder = this.j.buildBuilder("Unsaved changes", "If you scan, your current unsaved changes will be lost.");
            buildBuilder.setView((View)view);
            buildBuilder.setPositiveButton((CharSequence)"Ok", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, final int n) {
                    FtcConfigurationActivity.this.d.start();
                }
            });
            buildBuilder.setNegativeButton((CharSequence)"Cancel", this.c);
            buildBuilder.show();
            return;
        }
        this.d.start();
    }
    
    private void d() {
        final ReadXMLFileHandler readXMLFileHandler = new ReadXMLFileHandler(this.f);
        if (this.i.equalsIgnoreCase("No current file!")) {
            return;
        }
        try {
            this.a((ArrayList)readXMLFileHandler.parse(new FileInputStream(Utility.CONFIG_FILES_DIR + this.i + ".xml")));
            this.h();
            this.f();
        }
        catch (RobotCoreException ex) {
            RobotLog.e("Error parsing XML file");
            RobotLog.logStacktrace(ex);
            this.j.complainToast("Error parsing XML file: " + this.i, this.f);
        }
        catch (FileNotFoundException ex2) {
            DbgLog.error("File was not found: " + this.i);
            DbgLog.logStacktrace(ex2);
            this.j.complainToast("That file was not found: " + this.i, this.f);
        }
    }
    
    private void e() {
        if (this.e.size() == 0) {
            this.j.setOrangeText("Scan to find devices.", "In order to find devices: \n    1. Attach a robot \n    2. Press the 'Scan' button", R.id.empty_devicelist, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
            this.g();
            return;
        }
        final LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.empty_devicelist);
        linearLayout.removeAllViews();
        linearLayout.setVisibility(8);
    }
    
    private void f() {
        if (this.e.size() == 0) {
            this.j.setOrangeText("No devices found!", "In order to find devices: \n    1. Attach a robot \n    2. Press the 'Scan' button", R.id.empty_devicelist, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
            this.g();
            return;
        }
        final LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.empty_devicelist);
        linearLayout.removeAllViews();
        linearLayout.setVisibility(8);
    }
    
    private void g() {
        final LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.warning_layout);
        linearLayout.removeAllViews();
        linearLayout.setVisibility(8);
    }
    
    private void h() {
        final ListView listView = (ListView)this.findViewById(R.id.controllersList);
        listView.setAdapter((ListAdapter)new DeviceInfoAdapter(this, 17367044, this.e));
        listView.setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                final ControllerConfiguration controllerConfiguration = (ControllerConfiguration)adapterView.getItemAtPosition(n);
                final DeviceConfiguration.ConfigurationType type = controllerConfiguration.getType();
                if (type.equals(DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER)) {
                    final Intent intent = new Intent(FtcConfigurationActivity.this.f, (Class)EditMotorControllerActivity.class);
                    intent.putExtra("EDIT_MOTOR_CONTROLLER_CONFIG", (Serializable)controllerConfiguration);
                    intent.putExtra("requestCode", 1);
                    FtcConfigurationActivity.this.setResult(-1, intent);
                    FtcConfigurationActivity.this.startActivityForResult(intent, 1);
                }
                if (type.equals(DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER)) {
                    final Intent intent2 = new Intent(FtcConfigurationActivity.this.f, (Class)EditServoControllerActivity.class);
                    intent2.putExtra("Edit Servo ControllerConfiguration Activity", (Serializable)controllerConfiguration);
                    intent2.putExtra("requestCode", 2);
                    FtcConfigurationActivity.this.setResult(-1, intent2);
                    FtcConfigurationActivity.this.startActivityForResult(intent2, 2);
                }
                if (type.equals(DeviceConfiguration.ConfigurationType.LEGACY_MODULE_CONTROLLER)) {
                    final Intent intent3 = new Intent(FtcConfigurationActivity.this.f, (Class)EditLegacyModuleControllerActivity.class);
                    intent3.putExtra("EDIT_LEGACY_CONFIG", (Serializable)controllerConfiguration);
                    intent3.putExtra("requestCode", 3);
                    FtcConfigurationActivity.this.setResult(-1, intent3);
                    FtcConfigurationActivity.this.startActivityForResult(intent3, 3);
                }
                if (type.equals(DeviceConfiguration.ConfigurationType.DEVICE_INTERFACE_MODULE)) {
                    final Intent intent4 = new Intent(FtcConfigurationActivity.this.f, (Class)EditDeviceInterfaceModuleActivity.class);
                    intent4.putExtra("EDIT_DEVICE_INTERFACE_MODULE_CONFIG", (Serializable)controllerConfiguration);
                    intent4.putExtra("requestCode", 4);
                    FtcConfigurationActivity.this.setResult(-1, intent4);
                    FtcConfigurationActivity.this.startActivityForResult(intent4, 4);
                }
            }
        });
    }
    
    private void i() {
        if (!this.i.toLowerCase().contains("Unsaved".toLowerCase())) {
            final String string = "Unsaved " + this.i;
            this.j.saveToPreferences(string, R.string.pref_hardware_config_filename);
            this.i = string;
        }
    }
    
    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        if (n2 == 0) {
            return;
        }
        Serializable s;
        if (n == 1) {
            s = intent.getSerializableExtra("EDIT_MOTOR_CONTROLLER_CONFIG");
        }
        else if (n == 2) {
            s = intent.getSerializableExtra("Edit Servo ControllerConfiguration Activity");
        }
        else if (n == 3) {
            s = intent.getSerializableExtra("EDIT_LEGACY_CONFIG");
        }
        else {
            s = null;
            if (n == 4) {
                s = intent.getSerializableExtra("EDIT_DEVICE_INTERFACE_MODULE_CONFIG");
            }
        }
        if (s != null) {
            final ControllerConfiguration controllerConfiguration = (ControllerConfiguration)s;
            this.scannedDevices.put(controllerConfiguration.getSerialNumber(), controllerConfiguration.configTypeToDeviceType(controllerConfiguration.getType()));
            this.e.put(controllerConfiguration.getSerialNumber(), controllerConfiguration);
            this.h();
            this.i();
            return;
        }
        DbgLog.error("Received Result with an incorrect request code: " + String.valueOf(n));
    }
    
    public void onBackPressed() {
        if (!this.i.toLowerCase().contains("Unsaved".toLowerCase())) {
            super.onBackPressed();
            return;
        }
        if (this.j.writeXML(this.e)) {
            return;
        }
        final EditText view = new EditText((Context)this);
        view.setText((CharSequence)this.j.prepareFilename(this.i));
        final AlertDialog$Builder buildBuilder = this.j.buildBuilder("Unsaved changes", "Please save your file before exiting the Hardware Wizard! \n If you click 'Cancel' your changes will be lost.");
        buildBuilder.setView((View)view);
        buildBuilder.setPositiveButton((CharSequence)"Ok", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                final String trim = (view.getText().toString() + ".xml").trim();
                if (trim.equals(".xml")) {
                    FtcConfigurationActivity.this.j.complainToast("File not saved: Please entire a filename.", FtcConfigurationActivity.this.f);
                    return;
                }
                try {
                    FtcConfigurationActivity.this.j.writeToFile(trim);
                    FtcConfigurationActivity.this.g();
                    FtcConfigurationActivity.this.j.saveToPreferences(view.getText().toString(), R.string.pref_hardware_config_filename);
                    FtcConfigurationActivity.this.i = view.getText().toString();
                    FtcConfigurationActivity.this.j.updateHeader("robot_config", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                    FtcConfigurationActivity.this.j.confirmSave();
                    FtcConfigurationActivity.this.finish();
                }
                catch (RobotCoreException ex) {
                    FtcConfigurationActivity.this.j.complainToast(ex.getMessage(), FtcConfigurationActivity.this.f);
                    DbgLog.error(ex.getMessage());
                }
                catch (IOException ex2) {
                    FtcConfigurationActivity.this.a(ex2.getMessage());
                    DbgLog.error(ex2.getMessage());
                }
            }
        });
        buildBuilder.setNegativeButton((CharSequence)"Cancel", this.b);
        buildBuilder.show();
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_ftc_configuration);
        RobotLog.writeLogcatToDisk((Context)this, 1024);
        this.f = (Context)this;
        this.j = new Utility(this);
        this.h = (Button)this.findViewById(R.id.scanButton);
        this.a();
        while (true) {
            try {
                this.g = new ModernRoboticsDeviceManager(this.f, null);
                this.preferences = PreferenceManager.getDefaultSharedPreferences((Context)this);
            }
            catch (RobotCoreException ex) {
                this.j.complainToast("Failed to open the Device Manager", this.f);
                DbgLog.error("Failed to open deviceManager: " + ex.toString());
                DbgLog.logStacktrace(ex);
                continue;
            }
            break;
        }
    }
    
    protected void onDestroy() {
        super.onDestroy();
        this.j.resetCount();
    }
    
    protected void onStart() {
        super.onStart();
        this.i = this.j.getFilenameFromPrefs(R.string.pref_hardware_config_filename, "No current file!");
        if (this.i.equalsIgnoreCase("No current file!")) {
            this.j.createConfigFolder();
        }
        this.j.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
        this.e();
        if (!this.i.toLowerCase().contains("Unsaved".toLowerCase())) {
            this.d();
        }
        this.h.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                FtcConfigurationActivity.this.d = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                DbgLog.msg("Scanning USB bus");
                                FtcConfigurationActivity.this.scannedDevices = FtcConfigurationActivity.this.g.scanForUsbDevices();
                                FtcConfigurationActivity.this.runOnUiThread((Runnable)new Runnable() {
                                    @Override
                                    public void run() {
                                        FtcConfigurationActivity.this.j.resetCount();
                                        FtcConfigurationActivity.this.g();
                                        FtcConfigurationActivity.this.i();
                                        FtcConfigurationActivity.this.j.updateHeader(FtcConfigurationActivity.this.i, R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                                        FtcConfigurationActivity.this.scannedEntries = FtcConfigurationActivity.this.scannedDevices.entrySet();
                                        FtcConfigurationActivity.this.e = FtcConfigurationActivity.this.b();
                                        FtcConfigurationActivity.this.h();
                                        FtcConfigurationActivity.this.f();
                                    }
                                });
                            }
                            catch (RobotCoreException ex) {
                                DbgLog.error("Device scan failed: " + ex.toString());
                                continue;
                            }
                            break;
                        }
                    }
                });
                FtcConfigurationActivity.this.c();
            }
        });
    }
    
    public void saveConfiguration(final View view) {
        if (this.j.writeXML(this.e)) {
            return;
        }
        final EditText view2 = new EditText((Context)this);
        view2.setText((CharSequence)this.j.prepareFilename(this.i));
        final AlertDialog$Builder buildBuilder = this.j.buildBuilder("Enter file name", "Please enter the file name");
        buildBuilder.setView((View)view2);
        buildBuilder.setPositiveButton((CharSequence)"Ok", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                final String trim = (view2.getText().toString() + ".xml").trim();
                if (trim.equals(".xml")) {
                    FtcConfigurationActivity.this.j.complainToast("File not saved: Please entire a filename.", FtcConfigurationActivity.this.f);
                    return;
                }
                try {
                    FtcConfigurationActivity.this.j.writeToFile(trim);
                    FtcConfigurationActivity.this.g();
                    FtcConfigurationActivity.this.j.saveToPreferences(view2.getText().toString(), R.string.pref_hardware_config_filename);
                    FtcConfigurationActivity.this.i = view2.getText().toString();
                    FtcConfigurationActivity.this.j.updateHeader("robot_config", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                    FtcConfigurationActivity.this.j.confirmSave();
                }
                catch (RobotCoreException ex) {
                    FtcConfigurationActivity.this.j.complainToast(ex.getMessage(), FtcConfigurationActivity.this.f);
                    DbgLog.error(ex.getMessage());
                }
                catch (IOException ex2) {
                    FtcConfigurationActivity.this.a(ex2.getMessage());
                    DbgLog.error(ex2.getMessage());
                }
            }
        });
        buildBuilder.setNegativeButton((CharSequence)"Cancel", this.c);
        buildBuilder.show();
    }
}
