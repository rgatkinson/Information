package com.qualcomm.ftccommon.configuration;

import android.text.Editable;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.qualcomm.robotcore.util.RobotLog;
import android.content.Context;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View$OnClickListener;
import android.widget.CheckBox;
import android.text.TextWatcher;
import com.qualcomm.ftccommon.R;
import java.io.Serializable;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import java.util.ArrayList;
import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import android.app.Activity;

public class EditServoControllerActivity extends Activity
{
    public static final String EDIT_SERVO_ACTIVITY = "Edit Servo ControllerConfiguration Activity";
    private Utility a;
    private ServoControllerConfiguration b;
    private ArrayList<DeviceConfiguration> c;
    private EditText d;
    private View e;
    private View f;
    private View g;
    private View h;
    private View i;
    private View j;
    
    private void a() {
        final Intent intent = new Intent();
        this.b.addServos(this.c);
        this.b.setName(this.d.getText().toString());
        intent.putExtra("Edit Servo ControllerConfiguration Activity", (Serializable)this.b);
        this.setResult(-1, intent);
        this.finish();
    }
    
    private void a(final int n) {
        final View d = this.d(n);
        ((EditText)d.findViewById(R.id.editTextResult_servo)).addTextChangedListener((TextWatcher)new a(d));
    }
    
    private void b(final int n) {
        final View d = this.d(n);
        final CheckBox checkBox = (CheckBox)d.findViewById(R.id.checkbox_port_servo);
        final DeviceConfiguration deviceConfiguration = this.c.get(n - 1);
        if (deviceConfiguration.isEnabled()) {
            checkBox.setChecked(true);
            ((EditText)d.findViewById(R.id.editTextResult_servo)).setText((CharSequence)deviceConfiguration.getName());
            return;
        }
        checkBox.setChecked(true);
        checkBox.performClick();
    }
    
    private void c(final int n) {
        final View d = this.d(n);
        ((CheckBox)d.findViewById(R.id.checkbox_port_servo)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            final /* synthetic */ EditText a = (EditText)d.findViewById(R.id.editTextResult_servo);
            final /* synthetic */ DeviceConfiguration b = EditServoControllerActivity.this.c.get(n - 1);
            
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
            case 1: {
                return this.e;
            }
            case 2: {
                return this.f;
            }
            case 3: {
                return this.g;
            }
            case 4: {
                return this.h;
            }
            case 5: {
                return this.i;
            }
            case 6: {
                return this.j;
            }
        }
    }
    
    public void cancelServoController(final View view) {
        this.setResult(0, new Intent());
        this.finish();
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.servos);
        PreferenceManager.setDefaultValues((Context)this, R.xml.preferences, false);
        this.a = new Utility(this);
        RobotLog.writeLogcatToDisk((Context)this, 1024);
        this.d = (EditText)this.findViewById(R.id.servocontroller_name);
        this.e = this.getLayoutInflater().inflate(R.layout.servo, (ViewGroup)this.findViewById(R.id.linearLayout_servo1), true);
        ((TextView)this.e.findViewById(R.id.port_number_servo)).setText((CharSequence)"1");
        this.f = this.getLayoutInflater().inflate(R.layout.servo, (ViewGroup)this.findViewById(R.id.linearLayout_servo2), true);
        ((TextView)this.f.findViewById(R.id.port_number_servo)).setText((CharSequence)"2");
        this.g = this.getLayoutInflater().inflate(R.layout.servo, (ViewGroup)this.findViewById(R.id.linearLayout_servo3), true);
        ((TextView)this.g.findViewById(R.id.port_number_servo)).setText((CharSequence)"3");
        this.h = this.getLayoutInflater().inflate(R.layout.servo, (ViewGroup)this.findViewById(R.id.linearLayout_servo4), true);
        ((TextView)this.h.findViewById(R.id.port_number_servo)).setText((CharSequence)"4");
        this.i = this.getLayoutInflater().inflate(R.layout.servo, (ViewGroup)this.findViewById(R.id.linearLayout_servo5), true);
        ((TextView)this.i.findViewById(R.id.port_number_servo)).setText((CharSequence)"5");
        this.j = this.getLayoutInflater().inflate(R.layout.servo, (ViewGroup)this.findViewById(R.id.linearLayout_servo6), true);
        ((TextView)this.j.findViewById(R.id.port_number_servo)).setText((CharSequence)"6");
    }
    
    protected void onStart() {
        super.onStart();
        this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
        final Serializable serializableExtra = this.getIntent().getSerializableExtra("Edit Servo ControllerConfiguration Activity");
        if (serializableExtra != null) {
            this.b = (ServoControllerConfiguration)serializableExtra;
            this.c = (ArrayList<DeviceConfiguration>)(ArrayList)this.b.getServos();
        }
        this.d.setText((CharSequence)this.b.getName());
        final TextView textView = (TextView)this.findViewById(R.id.servo_controller_serialNumber);
        String string = this.b.getSerialNumber().toString();
        if (string.equalsIgnoreCase(ControllerConfiguration.NO_SERIAL_NUMBER.toString())) {
            string = "No serial number";
        }
        textView.setText((CharSequence)string);
        for (int i = 0; i < this.c.size(); ++i) {
            this.c(i + 1);
            this.a(i + 1);
            this.b(i + 1);
        }
    }
    
    public void saveServoController(final View view) {
        this.a();
    }
    
    private class a implements TextWatcher
    {
        private int b;
        
        private a(final View view) {
            this.b = Integer.parseInt(((TextView)view.findViewById(R.id.port_number_servo)).getText().toString());
        }
        
        public void afterTextChanged(final Editable editable) {
            EditServoControllerActivity.this.c.get(-1 + this.b).setName(editable.toString());
        }
        
        public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
        }
        
        public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
        }
    }
}
