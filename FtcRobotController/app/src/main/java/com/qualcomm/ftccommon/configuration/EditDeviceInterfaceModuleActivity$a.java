package com.qualcomm.ftccommon.configuration;

import android.text.Editable;
import android.text.TextWatcher;

private class a implements TextWatcher
{
    public void afterTextChanged(final Editable editable) {
        EditDeviceInterfaceModuleActivity.a(EditDeviceInterfaceModuleActivity.this).setName(editable.toString());
    }
    
    public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
    }
    
    public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
    }
}
