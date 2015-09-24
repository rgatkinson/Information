package com.qualcomm.ftccommon.configuration;

import java.util.List;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemClickListener;

class EditDeviceInterfaceModuleActivity$1 implements AdapterView$OnItemClickListener {
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
        switch (n) {
            default: {}
            case 0: {
                EditDeviceInterfaceModuleActivity.a(EditDeviceInterfaceModuleActivity.this, 201, EditDeviceInterfaceModuleActivity.a(EditDeviceInterfaceModuleActivity.this).getPwmDevices(), EditPWMDevicesActivity.class);
            }
            case 1: {
                EditDeviceInterfaceModuleActivity.a(EditDeviceInterfaceModuleActivity.this, 202, EditDeviceInterfaceModuleActivity.a(EditDeviceInterfaceModuleActivity.this).getI2cDevices(), EditI2cDevicesActivity.class);
            }
            case 2: {
                EditDeviceInterfaceModuleActivity.a(EditDeviceInterfaceModuleActivity.this, 203, EditDeviceInterfaceModuleActivity.a(EditDeviceInterfaceModuleActivity.this).getAnalogInputDevices(), EditAnalogInputDevicesActivity.class);
            }
            case 3: {
                EditDeviceInterfaceModuleActivity.a(EditDeviceInterfaceModuleActivity.this, 204, EditDeviceInterfaceModuleActivity.a(EditDeviceInterfaceModuleActivity.this).getDigitalDevices(), EditDigitalDevicesActivity.class);
            }
            case 4: {
                EditDeviceInterfaceModuleActivity.a(EditDeviceInterfaceModuleActivity.this, 205, EditDeviceInterfaceModuleActivity.a(EditDeviceInterfaceModuleActivity.this).getAnalogOutputDevices(), EditAnalogOutputDevicesActivity.class);
            }
        }
    }
}