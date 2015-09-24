package com.qualcomm.ftccommon.configuration;

import android.text.Editable;
import java.util.Iterator;
import android.view.ViewGroup;
import com.qualcomm.robotcore.util.RobotLog;
import android.content.Context;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.text.TextWatcher;
import com.qualcomm.ftccommon.R;
import android.widget.EditText;
import android.content.Intent;
import java.io.Serializable;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemSelectedListener;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import java.util.ArrayList;
import android.view.View;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import android.app.Activity;

public class EditI2cDevicesActivity extends Activity
{
    private Utility a;
    private View b;
    private View c;
    private View d;
    private View e;
    private View f;
    private View g;
    private ArrayList<DeviceConfiguration> h;
    private AdapterView$OnItemSelectedListener i;
    
    public EditI2cDevicesActivity() {
        this.h = new ArrayList<DeviceConfiguration>();
        this.i = (AdapterView$OnItemSelectedListener)new AdapterView$OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                final String string = adapterView.getItemAtPosition(n).toString();
                final LinearLayout linearLayout = (LinearLayout)view.getParent().getParent().getParent();
                if (string.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.NOTHING.toString())) {
                    EditI2cDevicesActivity.this.a(linearLayout);
                    return;
                }
                EditI2cDevicesActivity.this.a(linearLayout, string);
            }
            
            public void onNothingSelected(final AdapterView<?> adapterView) {
            }
        };
    }
    
    private View a(final int n) {
        switch (n) {
            default: {
                return null;
            }
            case 0: {
                return this.b;
            }
            case 1: {
                return this.c;
            }
            case 2: {
                return this.d;
            }
            case 3: {
                return this.e;
            }
            case 4: {
                return this.f;
            }
            case 5: {
                return this.g;
            }
        }
    }
    
    private void a() {
        final Bundle bundle = new Bundle();
        for (int i = 0; i < this.h.size(); ++i) {
            bundle.putSerializable(String.valueOf(i), (Serializable)this.h.get(i));
        }
        final Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.putExtras(bundle);
        this.setResult(-1, intent);
        this.finish();
    }
    
    private void a(final View view) {
        ((EditText)view.findViewById(R.id.editTextResult_i2c)).addTextChangedListener((TextWatcher)new a(view));
    }
    
    private void a(final View view, final DeviceConfiguration deviceConfiguration) {
        final Spinner spinner = (Spinner)view.findViewById(R.id.choiceSpinner_i2c);
        final ArrayAdapter arrayAdapter = (ArrayAdapter)spinner.getAdapter();
        if (deviceConfiguration.isEnabled()) {
            spinner.setSelection(arrayAdapter.getPosition((Object)deviceConfiguration.getType().toString()));
        }
        else {
            spinner.setSelection(0);
        }
        spinner.setOnItemSelectedListener(this.i);
    }
    
    private void a(final EditText editText, final DeviceConfiguration deviceConfiguration) {
        if (editText.getText().toString().equalsIgnoreCase("NO DEVICE ATTACHED")) {
            editText.setText((CharSequence)"");
            deviceConfiguration.setName("");
            return;
        }
        editText.setText((CharSequence)deviceConfiguration.getName());
    }
    
    private void a(final LinearLayout linearLayout) {
        final int int1 = Integer.parseInt(((TextView)linearLayout.findViewById(R.id.port_number_i2c)).getText().toString());
        final EditText editText = (EditText)linearLayout.findViewById(R.id.editTextResult_i2c);
        editText.setEnabled(false);
        editText.setText((CharSequence)"NO DEVICE ATTACHED");
        this.h.get(int1).setEnabled(false);
    }
    
    private void a(final LinearLayout linearLayout, final String s) {
        final int int1 = Integer.parseInt(((TextView)linearLayout.findViewById(R.id.port_number_i2c)).getText().toString());
        final EditText editText = (EditText)linearLayout.findViewById(R.id.editTextResult_i2c);
        editText.setEnabled(true);
        final DeviceConfiguration deviceConfiguration = this.h.get(int1);
        deviceConfiguration.setType(deviceConfiguration.typeFromString(s));
        deviceConfiguration.setEnabled(true);
        this.a(editText, deviceConfiguration);
    }
    
    private void b(final View view, final DeviceConfiguration deviceConfiguration) {
        final EditText editText = (EditText)view.findViewById(R.id.editTextResult_i2c);
        if (deviceConfiguration.isEnabled()) {
            editText.setText((CharSequence)deviceConfiguration.getName());
            editText.setEnabled(true);
            return;
        }
        editText.setText((CharSequence)"NO DEVICE ATTACHED");
        editText.setEnabled(false);
    }
    
    public void cancelI2cDevices(final View view) {
        this.setResult(0, new Intent());
        this.finish();
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.i2cs);
        PreferenceManager.setDefaultValues((Context)this, R.xml.preferences, false);
        this.a = new Utility(this);
        RobotLog.writeLogcatToDisk((Context)this, 1024);
        this.b = this.getLayoutInflater().inflate(R.layout.i2c_device, (ViewGroup)this.findViewById(R.id.linearLayout_i2c0), true);
        ((TextView)this.b.findViewById(R.id.port_number_i2c)).setText((CharSequence)"0");
        this.c = this.getLayoutInflater().inflate(R.layout.i2c_device, (ViewGroup)this.findViewById(R.id.linearLayout_i2c1), true);
        ((TextView)this.c.findViewById(R.id.port_number_i2c)).setText((CharSequence)"1");
        this.d = this.getLayoutInflater().inflate(R.layout.i2c_device, (ViewGroup)this.findViewById(R.id.linearLayout_i2c2), true);
        ((TextView)this.d.findViewById(R.id.port_number_i2c)).setText((CharSequence)"2");
        this.e = this.getLayoutInflater().inflate(R.layout.i2c_device, (ViewGroup)this.findViewById(R.id.linearLayout_i2c3), true);
        ((TextView)this.e.findViewById(R.id.port_number_i2c)).setText((CharSequence)"3");
        this.f = this.getLayoutInflater().inflate(R.layout.i2c_device, (ViewGroup)this.findViewById(R.id.linearLayout_i2c4), true);
        ((TextView)this.f.findViewById(R.id.port_number_i2c)).setText((CharSequence)"4");
        this.g = this.getLayoutInflater().inflate(R.layout.i2c_device, (ViewGroup)this.findViewById(R.id.linearLayout_i2c5), true);
        ((TextView)this.g.findViewById(R.id.port_number_i2c)).setText((CharSequence)"5");
    }
    
    protected void onStart() {
        super.onStart();
        this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
        final Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            for (final String s : extras.keySet()) {
                this.h.add(Integer.parseInt(s), (DeviceConfiguration)extras.getSerializable(s));
            }
            for (int i = 0; i < this.h.size(); ++i) {
                final View a = this.a(i);
                final DeviceConfiguration deviceConfiguration = this.h.get(i);
                this.a(a);
                this.b(a, deviceConfiguration);
                this.a(a, deviceConfiguration);
            }
        }
    }
    
    public void saveI2cDevices(final View view) {
        this.a();
    }
    
    private class a implements TextWatcher
    {
        private int b;
        
        private a(final View view) {
            this.b = Integer.parseInt(((TextView)view.findViewById(R.id.port_number_i2c)).getText().toString());
        }
        
        public void afterTextChanged(final Editable editable) {
            EditI2cDevicesActivity.this.h.get(this.b).setName(editable.toString());
        }
        
        public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
        }
        
        public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
        }
    }
}
