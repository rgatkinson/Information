package com.qualcomm.ftccommon.configuration;

import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import android.widget.CheckBox;
import android.view.View;
import android.view.View$OnClickListener;

class EditMotorControllerActivity$1 implements View$OnClickListener {
    public void onClick(final View view) {
        if (((CheckBox)view).isChecked()) {
            EditMotorControllerActivity.a(EditMotorControllerActivity.this, true);
            EditMotorControllerActivity.a(EditMotorControllerActivity.this).setEnabled(true);
            EditMotorControllerActivity.a(EditMotorControllerActivity.this).setText((CharSequence)"");
            EditMotorControllerActivity.b(EditMotorControllerActivity.this).setPort(1);
            EditMotorControllerActivity.b(EditMotorControllerActivity.this).setType(DeviceConfiguration.ConfigurationType.MOTOR);
            return;
        }
        EditMotorControllerActivity.a(EditMotorControllerActivity.this, false);
        EditMotorControllerActivity.a(EditMotorControllerActivity.this).setEnabled(false);
        EditMotorControllerActivity.a(EditMotorControllerActivity.this).setText((CharSequence)"NO DEVICE ATTACHED");
        EditMotorControllerActivity.b(EditMotorControllerActivity.this).setType(DeviceConfiguration.ConfigurationType.NOTHING);
    }
}