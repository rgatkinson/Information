package com.qualcomm.ftccommon.configuration;

import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import android.widget.TextView;
import com.qualcomm.robotcore.util.RobotLog;
import android.content.Context;
import android.preference.PreferenceManager;
import com.qualcomm.ftccommon.R;
import android.os.Bundle;
import java.io.Serializable;
import java.util.List;
import android.content.Intent;
import android.view.View;
import android.view.View$OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import com.qualcomm.robotcore.hardware.configuration.MotorConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import java.util.ArrayList;
import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import android.app.Activity;

public class EditMotorControllerActivity extends Activity
{
    public static final String EDIT_MOTOR_CONTROLLER_CONFIG = "EDIT_MOTOR_CONTROLLER_CONFIG";
    private Utility a;
    private MotorControllerConfiguration b;
    private ArrayList<DeviceConfiguration> c;
    private MotorConfiguration d;
    private MotorConfiguration e;
    private EditText f;
    private boolean g;
    private boolean h;
    private CheckBox i;
    private CheckBox j;
    private EditText k;
    private EditText l;
    
    public EditMotorControllerActivity() {
        this.c = new ArrayList<DeviceConfiguration>();
        this.d = new MotorConfiguration(1);
        this.e = new MotorConfiguration(2);
        this.g = true;
        this.h = true;
    }
    
    private void a() {
        this.i.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (((CheckBox)view).isChecked()) {
                    EditMotorControllerActivity.this.g = true;
                    EditMotorControllerActivity.this.k.setEnabled(true);
                    EditMotorControllerActivity.this.k.setText((CharSequence)"");
                    EditMotorControllerActivity.this.d.setPort(1);
                    EditMotorControllerActivity.this.d.setType(DeviceConfiguration.ConfigurationType.MOTOR);
                    return;
                }
                EditMotorControllerActivity.this.g = false;
                EditMotorControllerActivity.this.k.setEnabled(false);
                EditMotorControllerActivity.this.k.setText((CharSequence)"NO DEVICE ATTACHED");
                EditMotorControllerActivity.this.d.setType(DeviceConfiguration.ConfigurationType.NOTHING);
            }
        });
    }
    
    private void a(final MotorConfiguration motorConfiguration, final CheckBox checkBox) {
        if (motorConfiguration.getName().equals("NO DEVICE ATTACHED") || motorConfiguration.getType() == DeviceConfiguration.ConfigurationType.NOTHING) {
            checkBox.setChecked(true);
            checkBox.performClick();
            return;
        }
        checkBox.setChecked(true);
    }
    
    private void b() {
        this.j.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (((CheckBox)view).isChecked()) {
                    EditMotorControllerActivity.this.h = true;
                    EditMotorControllerActivity.this.l.setEnabled(true);
                    EditMotorControllerActivity.this.l.setText((CharSequence)"");
                    EditMotorControllerActivity.this.e.setPort(2);
                    EditMotorControllerActivity.this.d.setType(DeviceConfiguration.ConfigurationType.MOTOR);
                    return;
                }
                EditMotorControllerActivity.this.h = false;
                EditMotorControllerActivity.this.l.setEnabled(false);
                EditMotorControllerActivity.this.l.setText((CharSequence)"NO DEVICE ATTACHED");
                EditMotorControllerActivity.this.d.setType(DeviceConfiguration.ConfigurationType.NOTHING);
            }
        });
    }
    
    private void c() {
        final Intent intent = new Intent();
        final ArrayList<DeviceConfiguration> list = new ArrayList<DeviceConfiguration>();
        if (this.g) {
            final MotorConfiguration motorConfiguration = new MotorConfiguration(this.k.getText().toString());
            motorConfiguration.setEnabled(true);
            motorConfiguration.setPort(1);
            list.add(motorConfiguration);
        }
        else {
            list.add(new MotorConfiguration(1));
        }
        if (this.h) {
            final MotorConfiguration motorConfiguration2 = new MotorConfiguration(this.l.getText().toString());
            motorConfiguration2.setEnabled(true);
            motorConfiguration2.setPort(2);
            list.add(motorConfiguration2);
        }
        else {
            list.add(new MotorConfiguration(2));
        }
        this.b.addMotors(list);
        this.b.setName(this.f.getText().toString());
        intent.putExtra("EDIT_MOTOR_CONTROLLER_CONFIG", (Serializable)this.b);
        this.setResult(-1, intent);
        this.finish();
    }
    
    public void cancelMotorController(final View view) {
        this.setResult(0, new Intent());
        this.finish();
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.motors);
        PreferenceManager.setDefaultValues((Context)this, R.xml.preferences, false);
        this.a = new Utility(this);
        RobotLog.writeLogcatToDisk((Context)this, 1024);
        this.f = (EditText)this.findViewById(R.id.controller_name);
        this.i = (CheckBox)this.findViewById(R.id.checkbox_port7);
        this.j = (CheckBox)this.findViewById(R.id.checkbox_port6);
        this.k = (EditText)this.findViewById(R.id.editTextResult_analogInput7);
        this.l = (EditText)this.findViewById(R.id.editTextResult_analogInput6);
    }
    
    protected void onStart() {
        super.onStart();
        this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
        final Serializable serializableExtra = this.getIntent().getSerializableExtra("EDIT_MOTOR_CONTROLLER_CONFIG");
        if (serializableExtra != null) {
            this.b = (MotorControllerConfiguration)serializableExtra;
            this.c = (ArrayList<DeviceConfiguration>)(ArrayList)this.b.getMotors();
            this.d = this.c.get(0);
            this.e = this.c.get(1);
            this.f.setText((CharSequence)this.b.getName());
            final TextView textView = (TextView)this.findViewById(R.id.motor_controller_serialNumber);
            String string = this.b.getSerialNumber().toString();
            if (string.equalsIgnoreCase(ControllerConfiguration.NO_SERIAL_NUMBER.toString())) {
                string = "No serial number";
            }
            textView.setText((CharSequence)string);
            this.k.setText((CharSequence)this.d.getName());
            this.l.setText((CharSequence)this.e.getName());
            this.a();
            this.a(this.d, this.i);
            this.b();
            this.a(this.e, this.j);
        }
    }
    
    public void saveMotorController(final View view) {
        this.c();
    }
}
