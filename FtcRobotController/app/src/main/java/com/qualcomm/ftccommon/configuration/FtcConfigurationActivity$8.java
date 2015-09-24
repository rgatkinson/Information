package com.qualcomm.ftccommon.configuration;

import java.io.Serializable;
import android.content.Intent;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemClickListener;

class FtcConfigurationActivity$8 implements AdapterView$OnItemClickListener {
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
        final ControllerConfiguration controllerConfiguration = (ControllerConfiguration)adapterView.getItemAtPosition(n);
        final DeviceConfiguration.ConfigurationType type = controllerConfiguration.getType();
        if (type.equals(DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER)) {
            final Intent intent = new Intent(FtcConfigurationActivity.k(FtcConfigurationActivity.this), (Class)EditMotorControllerActivity.class);
            intent.putExtra("EDIT_MOTOR_CONTROLLER_CONFIG", (Serializable)controllerConfiguration);
            intent.putExtra("requestCode", 1);
            FtcConfigurationActivity.this.setResult(-1, intent);
            FtcConfigurationActivity.this.startActivityForResult(intent, 1);
        }
        if (type.equals(DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER)) {
            final Intent intent2 = new Intent(FtcConfigurationActivity.k(FtcConfigurationActivity.this), (Class)EditServoControllerActivity.class);
            intent2.putExtra("Edit Servo ControllerConfiguration Activity", (Serializable)controllerConfiguration);
            intent2.putExtra("requestCode", 2);
            FtcConfigurationActivity.this.setResult(-1, intent2);
            FtcConfigurationActivity.this.startActivityForResult(intent2, 2);
        }
        if (type.equals(DeviceConfiguration.ConfigurationType.LEGACY_MODULE_CONTROLLER)) {
            final Intent intent3 = new Intent(FtcConfigurationActivity.k(FtcConfigurationActivity.this), (Class)EditLegacyModuleControllerActivity.class);
            intent3.putExtra("EDIT_LEGACY_CONFIG", (Serializable)controllerConfiguration);
            intent3.putExtra("requestCode", 3);
            FtcConfigurationActivity.this.setResult(-1, intent3);
            FtcConfigurationActivity.this.startActivityForResult(intent3, 3);
        }
        if (type.equals(DeviceConfiguration.ConfigurationType.DEVICE_INTERFACE_MODULE)) {
            final Intent intent4 = new Intent(FtcConfigurationActivity.k(FtcConfigurationActivity.this), (Class)EditDeviceInterfaceModuleActivity.class);
            intent4.putExtra("EDIT_DEVICE_INTERFACE_MODULE_CONFIG", (Serializable)controllerConfiguration);
            intent4.putExtra("requestCode", 4);
            FtcConfigurationActivity.this.setResult(-1, intent4);
            FtcConfigurationActivity.this.startActivityForResult(intent4, 4);
        }
    }
}