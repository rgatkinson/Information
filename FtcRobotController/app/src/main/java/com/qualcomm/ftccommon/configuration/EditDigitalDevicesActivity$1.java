package com.qualcomm.ftccommon.configuration;

import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemSelectedListener;

class EditDigitalDevicesActivity$1 implements AdapterView$OnItemSelectedListener {
    public void onItemSelected(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
        final String string = adapterView.getItemAtPosition(n).toString();
        final LinearLayout linearLayout = (LinearLayout)view.getParent().getParent().getParent();
        if (string.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.NOTHING.toString())) {
            EditDigitalDevicesActivity.a(EditDigitalDevicesActivity.this, linearLayout);
            return;
        }
        EditDigitalDevicesActivity.a(EditDigitalDevicesActivity.this, linearLayout, string);
    }
    
    public void onNothingSelected(final AdapterView<?> adapterView) {
    }
}