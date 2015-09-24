package com.qualcomm.ftccommon.configuration;

import android.text.Editable;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import android.text.TextWatcher;

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
