package com.qualcomm.ftccommon.configuration;

import android.text.Editable;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.qualcomm.robotcore.util.RobotLog;
import android.content.Context;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View$OnClickListener;
import android.text.TextWatcher;
import com.qualcomm.ftccommon.R;
import android.widget.CheckBox;
import java.io.Serializable;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import java.util.ArrayList;
import com.qualcomm.robotcore.hardware.configuration.MatrixControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import android.app.Activity;

public class EditMatrixControllerActivity extends Activity
{
    public static final String EDIT_MATRIX_ACTIVITY = "Edit Matrix ControllerConfiguration Activity";
    private Utility a;
    private MatrixControllerConfiguration b;
    private ArrayList<DeviceConfiguration> c;
    private ArrayList<DeviceConfiguration> d;
    private EditText e;
    private View f;
    private View g;
    private View h;
    private View i;
    private View j;
    private View k;
    private View l;
    private View m;
    
    private View a(final int n) {
        switch (n) {
            default: {
                return null;
            }
            case 1: {
                return this.f;
            }
            case 2: {
                return this.g;
            }
            case 3: {
                return this.h;
            }
            case 4: {
                return this.i;
            }
        }
    }
    
    private void a() {
        final Intent intent = new Intent();
        this.b.addServos(this.d);
        this.b.addMotors(this.c);
        this.b.setName(this.e.getText().toString());
        intent.putExtra("Edit Matrix ControllerConfiguration Activity", (Serializable)this.b);
        this.setResult(-1, intent);
        this.finish();
    }
    
    private void a(final int n, final View view, final ArrayList<DeviceConfiguration> list) {
        final CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkbox_port_matrix);
        final DeviceConfiguration deviceConfiguration = list.get(n - 1);
        if (deviceConfiguration.isEnabled()) {
            checkBox.setChecked(true);
            ((EditText)view.findViewById(R.id.editTextResult_matrix)).setText((CharSequence)deviceConfiguration.getName());
            return;
        }
        checkBox.setChecked(true);
        checkBox.performClick();
    }
    
    private void a(final View view, final DeviceConfiguration deviceConfiguration) {
        ((EditText)view.findViewById(R.id.editTextResult_matrix)).addTextChangedListener((TextWatcher)new a(deviceConfiguration));
    }
    
    private View b(final int n) {
        switch (n) {
            default: {
                return null;
            }
            case 1: {
                return this.j;
            }
            case 2: {
                return this.k;
            }
            case 3: {
                return this.l;
            }
            case 4: {
                return this.m;
            }
        }
    }
    
    private void b(final int n, final View view, final ArrayList<DeviceConfiguration> list) {
        ((CheckBox)view.findViewById(R.id.checkbox_port_matrix)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            final /* synthetic */ EditText a = (EditText)view.findViewById(R.id.editTextResult_matrix);
            final /* synthetic */ DeviceConfiguration b = list.get(n - 1);
            
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
    
    public void cancelMatrixController(final View view) {
        this.setResult(0, new Intent());
        this.finish();
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.matrices);
        PreferenceManager.setDefaultValues((Context)this, R.xml.preferences, false);
        this.a = new Utility(this);
        RobotLog.writeLogcatToDisk((Context)this, 1024);
        this.e = (EditText)this.findViewById(R.id.matrixcontroller_name);
        this.f = this.getLayoutInflater().inflate(R.layout.matrix_devices, (ViewGroup)this.findViewById(R.id.linearLayout_matrix1), true);
        ((TextView)this.f.findViewById(R.id.port_number_matrix)).setText((CharSequence)"1");
        this.g = this.getLayoutInflater().inflate(R.layout.matrix_devices, (ViewGroup)this.findViewById(R.id.linearLayout_matrix2), true);
        ((TextView)this.g.findViewById(R.id.port_number_matrix)).setText((CharSequence)"2");
        this.h = this.getLayoutInflater().inflate(R.layout.matrix_devices, (ViewGroup)this.findViewById(R.id.linearLayout_matrix3), true);
        ((TextView)this.h.findViewById(R.id.port_number_matrix)).setText((CharSequence)"3");
        this.i = this.getLayoutInflater().inflate(R.layout.matrix_devices, (ViewGroup)this.findViewById(R.id.linearLayout_matrix4), true);
        ((TextView)this.i.findViewById(R.id.port_number_matrix)).setText((CharSequence)"4");
        this.j = this.getLayoutInflater().inflate(R.layout.matrix_devices, (ViewGroup)this.findViewById(R.id.linearLayout_matrix5), true);
        ((TextView)this.j.findViewById(R.id.port_number_matrix)).setText((CharSequence)"1");
        this.k = this.getLayoutInflater().inflate(R.layout.matrix_devices, (ViewGroup)this.findViewById(R.id.linearLayout_matrix6), true);
        ((TextView)this.k.findViewById(R.id.port_number_matrix)).setText((CharSequence)"2");
        this.l = this.getLayoutInflater().inflate(R.layout.matrix_devices, (ViewGroup)this.findViewById(R.id.linearLayout_matrix7), true);
        ((TextView)this.l.findViewById(R.id.port_number_matrix)).setText((CharSequence)"3");
        this.m = this.getLayoutInflater().inflate(R.layout.matrix_devices, (ViewGroup)this.findViewById(R.id.linearLayout_matrix8), true);
        ((TextView)this.m.findViewById(R.id.port_number_matrix)).setText((CharSequence)"4");
    }
    
    protected void onStart() {
        super.onStart();
        this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
        final Serializable serializableExtra = this.getIntent().getSerializableExtra("Edit Matrix ControllerConfiguration Activity");
        if (serializableExtra != null) {
            this.b = (MatrixControllerConfiguration)serializableExtra;
            this.c = (ArrayList<DeviceConfiguration>)(ArrayList)this.b.getMotors();
            this.d = (ArrayList<DeviceConfiguration>)(ArrayList)this.b.getServos();
        }
        this.e.setText((CharSequence)this.b.getName());
        int n = 0;
        int i;
        while (true) {
            final int size = this.c.size();
            i = 0;
            if (n >= size) {
                break;
            }
            final View b = this.b(n + 1);
            this.b(n + 1, b, this.c);
            this.a(b, this.c.get(n));
            this.a(n + 1, b, this.c);
            ++n;
        }
        while (i < this.d.size()) {
            final View a = this.a(i + 1);
            this.b(i + 1, a, this.d);
            this.a(a, this.d.get(i));
            this.a(i + 1, a, this.d);
            ++i;
        }
    }
    
    public void saveMatrixController(final View view) {
        this.a();
    }
    
    private class a implements TextWatcher
    {
        private DeviceConfiguration b;
        
        private a(final DeviceConfiguration b) {
            this.b = b;
        }
        
        public void afterTextChanged(final Editable editable) {
            this.b.setName(editable.toString());
        }
        
        public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
        }
        
        public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
        }
    }
}
