package com.qualcomm.ftccommon.configuration;

import android.text.Editable;
import java.util.Iterator;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.qualcomm.robotcore.util.RobotLog;
import android.content.Context;
import android.preference.PreferenceManager;
import android.view.View$OnClickListener;
import android.text.TextWatcher;
import android.widget.EditText;
import com.qualcomm.ftccommon.R;
import android.widget.CheckBox;
import android.content.Intent;
import java.io.Serializable;
import android.os.Bundle;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import java.util.ArrayList;
import android.view.View;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import android.app.Activity;

public class EditPWMDevicesActivity extends Activity
{
    public static final String EDIT_PWM_DEVICES = "EDIT_PWM_DEVICES";
    private Utility a;
    private View b;
    private View c;
    private ArrayList<DeviceConfiguration> d;
    
    public EditPWMDevicesActivity() {
        this.d = new ArrayList<DeviceConfiguration>();
    }
    
    private void a() {
        final Bundle bundle = new Bundle();
        for (int i = 0; i < this.d.size(); ++i) {
            bundle.putSerializable(String.valueOf(i), (Serializable)this.d.get(i));
        }
        final Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.putExtras(bundle);
        this.setResult(-1, intent);
        this.finish();
    }
    
    private void a(final int n) {
        final View d = this.d(n);
        final CheckBox checkBox = (CheckBox)d.findViewById(R.id.checkbox_port_pwm);
        final DeviceConfiguration deviceConfiguration = this.d.get(n);
        if (deviceConfiguration.isEnabled()) {
            checkBox.setChecked(true);
            ((EditText)d.findViewById(R.id.editTextResult_pwm)).setText((CharSequence)deviceConfiguration.getName());
            return;
        }
        checkBox.setChecked(true);
        checkBox.performClick();
    }
    
    private void b(final int n) {
        final View d = this.d(n);
        ((EditText)d.findViewById(R.id.editTextResult_pwm)).addTextChangedListener((TextWatcher)new a(d));
    }
    
    private void c(final int n) {
        final View d = this.d(n);
        ((CheckBox)d.findViewById(R.id.checkbox_port_pwm)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            final /* synthetic */ EditText a = (EditText)d.findViewById(R.id.editTextResult_pwm);
            final /* synthetic */ DeviceConfiguration b = EditPWMDevicesActivity.this.d.get(n);
            
            public void onClick(final View view) {
                if (((CheckBox)view).isChecked()) {
                    this.a.setEnabled(true);
                    this.a.setText((CharSequence)"");
                    this.b.setEnabled(true);
                    this.b.setName("");
                    return;
                }
                this.a.setEnabled(false);
                this.b.setEnabled(false);
                this.b.setName("NO DEVICE ATTACHED");
                this.a.setText((CharSequence)"NO DEVICE ATTACHED");
            }
        });
    }
    
    private View d(final int n) {
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
        }
    }
    
    public void cancelPWMDevices(final View view) {
        this.setResult(0, new Intent());
        this.finish();
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.pwms);
        PreferenceManager.setDefaultValues((Context)this, R.xml.preferences, false);
        this.a = new Utility(this);
        RobotLog.writeLogcatToDisk((Context)this, 1024);
        this.b = this.getLayoutInflater().inflate(R.layout.pwm_device, (ViewGroup)this.findViewById(R.id.linearLayout_pwm0), true);
        ((TextView)this.b.findViewById(R.id.port_number_pwm)).setText((CharSequence)"0");
        this.c = this.getLayoutInflater().inflate(R.layout.pwm_device, (ViewGroup)this.findViewById(R.id.linearLayout_pwm1), true);
        ((TextView)this.c.findViewById(R.id.port_number_pwm)).setText((CharSequence)"1");
    }
    
    protected void onStart() {
        super.onStart();
        this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
        final Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            for (final String s : extras.keySet()) {
                this.d.add(Integer.parseInt(s), (DeviceConfiguration)extras.getSerializable(s));
            }
            for (int i = 0; i < this.d.size(); ++i) {
                this.c(i);
                this.b(i);
                this.a(i);
            }
        }
    }
    
    public void savePWMDevices(final View view) {
        this.a();
    }
    
    private class a implements TextWatcher
    {
        private int b;
        
        private a(final View view) {
            this.b = Integer.parseInt(((TextView)view.findViewById(R.id.port_number_pwm)).getText().toString());
        }
        
        public void afterTextChanged(final Editable editable) {
            EditPWMDevicesActivity.this.d.get(this.b).setName(editable.toString());
        }
        
        public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
        }
        
        public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
        }
    }
}
