package com.qualcomm.ftccommon;

import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.RobotLog;
import android.content.Context;
import android.widget.Toast;
import android.widget.TextView;
import com.qualcomm.robotcore.util.Dimmer;
import android.app.Activity;

public class UpdateUI
{
    Restarter a;
    FtcRobotControllerService b;
    Activity c;
    Dimmer d;
    protected TextView textDeviceName;
    protected TextView textErrorMessage;
    protected TextView[] textGamepad;
    protected TextView textOpMode;
    protected TextView textRobotStatus;
    protected TextView textWifiDirectStatus;
    
    public UpdateUI(final Activity c, final Dimmer d) {
        this.textGamepad = new TextView[2];
        this.c = c;
        this.d = d;
    }
    
    private void a() {
        this.a.requestRestart();
    }
    
    private void a(final String s) {
        DbgLog.msg(s);
        this.c.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                UpdateUI.this.textWifiDirectStatus.setText((CharSequence)s);
            }
        });
    }
    
    private void b(final String s) {
        this.c.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                UpdateUI.this.textDeviceName.setText((CharSequence)s);
            }
        });
    }
    
    public void setControllerService(final FtcRobotControllerService b) {
        this.b = b;
    }
    
    public void setRestarter(final Restarter a) {
        this.a = a;
    }
    
    public void setTextViews(final TextView textWifiDirectStatus, final TextView textRobotStatus, final TextView[] textGamepad, final TextView textOpMode, final TextView textErrorMessage, final TextView textDeviceName) {
        this.textWifiDirectStatus = textWifiDirectStatus;
        this.textRobotStatus = textRobotStatus;
        this.textGamepad = textGamepad;
        this.textOpMode = textOpMode;
        this.textErrorMessage = textErrorMessage;
        this.textDeviceName = textDeviceName;
    }
    
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
                                    UpdateUI.this.a();
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
                    UpdateUI.this.a("Wifi Direct - disconnected");
                }
                case 2: {
                    UpdateUI.this.a("Wifi Direct - enabled");
                }
                case 3: {
                    UpdateUI.this.a("Wifi Direct - ERROR");
                }
                case 4: {
                    UpdateUI.this.b(UpdateUI.this.b.getWifiDirectAssistant().getDeviceName());
                }
            }
        }
    }
}
