package com.qualcomm.ftccommon.configuration;

import android.widget.CheckBox;
import android.view.View;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import android.widget.EditText;
import android.view.View$OnClickListener;

class EditMatrixControllerActivity$1 implements View$OnClickListener {
    final /* synthetic */ EditText a;
    final /* synthetic */ DeviceConfiguration b;
    
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
}