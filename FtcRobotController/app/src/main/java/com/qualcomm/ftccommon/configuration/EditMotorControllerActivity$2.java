package com.qualcomm.ftccommon.configuration;

import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import android.widget.CheckBox;
import android.view.View;
import android.view.View$OnClickListener;

class EditMotorControllerActivity$2 implements View$OnClickListener {
    public void onClick(final View view) {
        if (((CheckBox)view).isChecked()) {
            EditMotorControllerActivity.b(EditMotorControllerActivity.this, true);
            EditMotorControllerActivity.c(EditMotorControllerActivity.this).setEnabled(true);
            EditMotorControllerActivity.c(EditMotorControllerActivity.this).setText((CharSequence)"");
            EditMotorControllerActivity.d(EditMotorControllerActivity.this).setPort(2);
            EditMotorControllerActivity.b(EditMotorControllerActivity.this).setType(DeviceConfiguration.ConfigurationType.MOTOR);
            return;
        }
        EditMotorControllerActivity.b(EditMotorControllerActivity.this, false);
        EditMotorControllerActivity.c(EditMotorControllerActivity.this).setEnabled(false);
        EditMotorControllerActivity.c(EditMotorControllerActivity.this).setText((CharSequence)"NO DEVICE ATTACHED");
        EditMotorControllerActivity.b(EditMotorControllerActivity.this).setType(DeviceConfiguration.ConfigurationType.NOTHING);
    }
}