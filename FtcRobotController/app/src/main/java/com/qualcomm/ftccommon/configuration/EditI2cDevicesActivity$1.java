package com.qualcomm.ftccommon.configuration;

import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemSelectedListener;

class EditI2cDevicesActivity$1 implements AdapterView$OnItemSelectedListener {
    public void onItemSelected(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
        final String string = adapterView.getItemAtPosition(n).toString();
        final LinearLayout linearLayout = (LinearLayout)view.getParent().getParent().getParent();
        if (string.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.NOTHING.toString())) {
            EditI2cDevicesActivity.a(EditI2cDevicesActivity.this, linearLayout);
            return;
        }
        EditI2cDevicesActivity.a(EditI2cDevicesActivity.this, linearLayout, string);
    }
    
    public void onNothingSelected(final AdapterView<?> adapterView) {
    }
}