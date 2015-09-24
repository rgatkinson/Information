package com.qualcomm.ftccommon;

import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.RobotLog;
import android.content.Context;
import android.widget.Toast;

public class Callback
{
    public void restartRobot() {
        UpdateUI.this.c.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                Toast.makeText((Context)UpdateUI.this.c, (CharSequence)"Restarting Robot", 0).show();
            }
        });
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1500L);
                        UpdateUI.this.c.runOnUiThread((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                UpdateUI.a(UpdateUI.this);
                            }
                        });
                    }
                    catch (InterruptedException ex) {
                        continue;
                    }
                    break;
                }
            }
        }.start();
    }
    
    public void robotUpdate(final String s) {
        DbgLog.msg(s);
        UpdateUI.this.c.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                UpdateUI.this.textRobotStatus.setText((CharSequence)s);
                UpdateUI.this.textErrorMessage.setText((CharSequence)RobotLog.getGlobalErrorMsg());
                if (RobotLog.hasGlobalErrorMsg()) {
                    UpdateUI.this.d.longBright();
                }
            }
        });
    }
    
    public void updateUi(final String s, final Gamepad[] array) {
        UpdateUI.this.c.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                for (int n = 0; n < UpdateUI.this.textGamepad.length && n < array.length; ++n) {
                    if (array[n].id == -1) {
                        UpdateUI.this.textGamepad[n].setText((CharSequence)" ");
                    }
                    else {
                        UpdateUI.this.textGamepad[n].setText((CharSequence)array[n].toString());
                    }
                }
                UpdateUI.this.textOpMode.setText((CharSequence)("Op Mode: " + s));
                UpdateUI.this.textErrorMessage.setText((CharSequence)RobotLog.getGlobalErrorMsg());
            }
        });
    }
    
    public void wifiDirectUpdate(final WifiDirectAssistant.Event event) {
        switch (UpdateUI$3.a[event.ordinal()]) {
            default: {}
            case 1: {
                UpdateUI.a(UpdateUI.this, "Wifi Direct - disconnected");
            }
            case 2: {
                UpdateUI.a(UpdateUI.this, "Wifi Direct - enabled");
            }
            case 3: {
                UpdateUI.a(UpdateUI.this, "Wifi Direct - ERROR");
            }
            case 4: {
                UpdateUI.b(UpdateUI.this, UpdateUI.this.b.getWifiDirectAssistant().getDeviceName());
            }
        }
    }
}
