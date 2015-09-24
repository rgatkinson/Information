package com.qualcomm.ftccommon.configuration;

import android.text.Editable;
import android.widget.TextView;
import android.text.TextWatcher;
import com.qualcomm.robotcore.util.RobotLog;
import android.preference.PreferenceManager;
import android.widget.ListAdapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;
import java.io.Serializable;
import com.qualcomm.ftccommon.R;
import java.util.Iterator;
import android.os.Bundle;
import java.util.List;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemClickListener;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import java.util.ArrayList;
import android.widget.EditText;
import com.qualcomm.robotcore.hardware.configuration.DeviceInterfaceModuleConfiguration;
import android.content.Context;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import android.app.Activity;

public class EditDeviceInterfaceModuleActivity extends Activity
{
    public static final int EDIT_ANALOG_INPUT_REQUEST_CODE = 203;
    public static final int EDIT_ANALOG_OUTPUT_REQUEST_CODE = 205;
    public static final String EDIT_DEVICE_INTERFACE_MODULE_CONFIG = "EDIT_DEVICE_INTERFACE_MODULE_CONFIG";
    public static final int EDIT_DIGITAL_REQUEST_CODE = 204;
    public static final int EDIT_I2C_PORT_REQUEST_CODE = 202;
    public static final int EDIT_PWM_PORT_REQUEST_CODE = 201;
    private Utility a;
    private String b;
    private Context c;
    private DeviceInterfaceModuleConfiguration d;
    private EditText e;
    private ArrayList<DeviceConfiguration> f;
    private AdapterView$OnItemClickListener g;
    
    public EditDeviceInterfaceModuleActivity() {
        this.f = new ArrayList<DeviceConfiguration>();
        this.g = (AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                switch (n) {
                    default: {}
                    case 0: {
                        EditDeviceInterfaceModuleActivity.this.a(201, EditDeviceInterfaceModuleActivity.this.d.getPwmDevices(), EditPWMDevicesActivity.class);
                    }
                    case 1: {
                        EditDeviceInterfaceModuleActivity.this.a(202, EditDeviceInterfaceModuleActivity.this.d.getI2cDevices(), EditI2cDevicesActivity.class);
                    }
                    case 2: {
                        EditDeviceInterfaceModuleActivity.this.a(203, EditDeviceInterfaceModuleActivity.this.d.getAnalogInputDevices(), EditAnalogInputDevicesActivity.class);
                    }
                    case 3: {
                        EditDeviceInterfaceModuleActivity.this.a(204, EditDeviceInterfaceModuleActivity.this.d.getDigitalDevices(), EditDigitalDevicesActivity.class);
                    }
                    case 4: {
                        EditDeviceInterfaceModuleActivity.this.a(205, EditDeviceInterfaceModuleActivity.this.d.getAnalogOutputDevices(), EditAnalogOutputDevicesActivity.class);
                    }
                }
            }
        };
    }
    
    private ArrayList<DeviceConfiguration> a(final Bundle bundle) {
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        for (final String s : bundle.keySet()) {
            list.add(Integer.parseInt(s), (DeviceConfiguration)bundle.getSerializable(s));
        }
        return list;
    }
    
    private void a() {
        if (!this.b.toLowerCase().contains("Unsaved".toLowerCase())) {
            final String string = "Unsaved " + this.b;
            this.a.saveToPreferences(string, R.string.pref_hardware_config_filename);
            this.b = string;
        }
        this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
    }
    
    private void a(final int n, final List<DeviceConfiguration> list, final Class clazz) {
        final Bundle bundle = new Bundle();
        for (int i = 0; i < list.size(); ++i) {
            bundle.putSerializable(String.valueOf(i), (Serializable)list.get(i));
        }
        final Intent intent = new Intent(this.c, clazz);
        intent.putExtras(bundle);
        this.setResult(-1, intent);
        this.startActivityForResult(intent, n);
    }
    
    private void b() {
        final Intent intent = new Intent();
        this.d.setName(this.e.getText().toString());
        intent.putExtra("EDIT_DEVICE_INTERFACE_MODULE_CONFIG", (Serializable)this.d);
        this.setResult(-1, intent);
        this.finish();
    }
    
    public void cancelDeviceInterface(final View view) {
        this.setResult(0, new Intent());
        this.finish();
    }
    
    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        if (n2 == -1) {
            if (n == 201) {
                final Bundle extras = intent.getExtras();
                if (extras != null) {
                    this.d.setPwmDevices(this.a(extras));
                }
            }
            else if (n == 203) {
                final Bundle extras2 = intent.getExtras();
                if (extras2 != null) {
                    this.d.setAnalogInputDevices(this.a(extras2));
                }
            }
            else if (n == 204) {
                final Bundle extras3 = intent.getExtras();
                if (extras3 != null) {
                    this.d.setDigitalDevices(this.a(extras3));
                }
            }
            else if (n == 202) {
                final Bundle extras4 = intent.getExtras();
                if (extras4 != null) {
                    this.d.setI2cDevices(this.a(extras4));
                }
            }
            else if (n == 205) {
                final Bundle extras5 = intent.getExtras();
                if (extras5 != null) {
                    this.d.setAnalogOutputDevices(this.a(extras5));
                }
            }
            this.a();
        }
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.device_interface_module);
        final String[] stringArray = this.getResources().getStringArray(R.array.device_interface_module_options_array);
        final ListView listView = (ListView)this.findViewById(R.id.listView_devices);
        listView.setAdapter((ListAdapter)new ArrayAdapter((Context)this, 17367043, (Object[])stringArray));
        listView.setOnItemClickListener(this.g);
        PreferenceManager.setDefaultValues(this.c = (Context)this, R.xml.preferences, false);
        this.a = new Utility(this);
        RobotLog.writeLogcatToDisk((Context)this, 1024);
        (this.e = (EditText)this.findViewById(R.id.device_interface_module_name)).addTextChangedListener((TextWatcher)new a());
    }
    
    protected void onStart() {
        super.onStart();
        this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
        this.b = this.a.getFilenameFromPrefs(R.string.pref_hardware_config_filename, "No current file!");
        final Serializable serializableExtra = this.getIntent().getSerializableExtra("EDIT_DEVICE_INTERFACE_MODULE_CONFIG");
        if (serializableExtra != null) {
            this.d = (DeviceInterfaceModuleConfiguration)serializableExtra;
            this.f = (ArrayList<DeviceConfiguration>)(ArrayList)this.d.getDevices();
            this.e.setText((CharSequence)this.d.getName());
            ((TextView)this.findViewById(R.id.device_interface_module_serialNumber)).setText((CharSequence)this.d.getSerialNumber().toString());
        }
    }
    
    public void saveDeviceInterface(final View view) {
        this.b();
    }
    
    private class a implements TextWatcher
    {
        public void afterTextChanged(final Editable editable) {
            EditDeviceInterfaceModuleActivity.this.d.setName(editable.toString());
        }
        
        public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
        }
        
        public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
        }
    }
}
