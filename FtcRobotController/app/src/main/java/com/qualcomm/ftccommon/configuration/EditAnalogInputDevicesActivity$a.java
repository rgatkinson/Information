package com.qualcomm.ftccommon.configuration;

import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import android.text.Editable;
import com.qualcomm.ftccommon.R;
import android.widget.TextView;
import android.view.View;
import android.text.TextWatcher;

private class a implements TextWatcher
{
    private int b;
    
    private a(final View view) {
        this.b = Integer.parseInt(((TextView)view.findViewById(R.id.port_number_analogInput)).getText().toString());
    }
    
    public void afterTextChanged(final Editable editable) {
        EditAnalogInputDevicesActivity.a(EditAnalogInputDevicesActivity.this).get(this.b).setName(editable.toString());
    }
    
    public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
    }
    
    public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
    }
}
