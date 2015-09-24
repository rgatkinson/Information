package com.qualcomm.ftccommon;

import android.widget.AdapterView;
import android.net.wifi.WifiManager;
import android.widget.SpinnerAdapter;
import android.widget.ArrayAdapter;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import java.io.IOException;
import android.content.Context;
import com.qualcomm.wirelessp2p.WifiDirectChannelSelection;
import android.app.ProgressDialog;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.AdapterView$OnItemSelectedListener;
import android.view.View$OnClickListener;
import android.app.Activity;

public class FtcWifiChannelSelectorActivity extends Activity implements View$OnClickListener, AdapterView$OnItemSelectedListener
{
    private static int a;
    private Button b;
    private Button c;
    private Spinner d;
    private ProgressDialog e;
    private WifiDirectChannelSelection f;
    private int g;
    private int h;
    private Context i;
    
    static {
        FtcWifiChannelSelectorActivity.a = 0;
    }
    
    public FtcWifiChannelSelectorActivity() {
        this.g = -1;
        this.h = -1;
    }
    
    private void a() {
        DbgLog.msg(String.format("configure p2p channel - class %d channel %d", this.g, this.h));
        try {
            this.e = ProgressDialog.show((Context)this, (CharSequence)"Configuring Channel", (CharSequence)"Please Wait", true);
            this.f.config(this.g, this.h);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(5000L);
                            FtcWifiChannelSelectorActivity.this.runOnUiThread((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    FtcWifiChannelSelectorActivity.this.setResult(-1);
                                    FtcWifiChannelSelectorActivity.this.e.dismiss();
                                    FtcWifiChannelSelectorActivity.this.finish();
                                }
                            });
                        }
                        catch (InterruptedException ex) {
                            continue;
                        }
                        break;
                    }
                }
            }).start();
        }
        catch (IOException ex) {
            this.a("Failed - root is required", 0);
            ex.printStackTrace();
        }
    }
    
    private void a(final String s, final int n) {
        this.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FtcWifiChannelSelectorActivity.this.i, (CharSequence)s, n).show();
            }
        });
    }
    
    public void onClick(final View view) {
        if (view.getId() == R.id.buttonConfigure) {
            FtcWifiChannelSelectorActivity.a = this.d.getSelectedItemPosition();
            this.a();
        }
        else if (view.getId() == R.id.buttonWifiSettings) {
            DbgLog.msg("launch wifi settings");
            this.startActivity(new Intent("android.net.wifi.PICK_WIFI_NETWORK"));
        }
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_ftc_wifi_channel_selector);
        this.i = (Context)this;
        this.d = (Spinner)this.findViewById(R.id.spinnerChannelSelect);
        final ArrayAdapter fromResource = ArrayAdapter.createFromResource((Context)this, R.array.wifi_direct_channels, 17367048);
        fromResource.setDropDownViewResource(17367049);
        this.d.setAdapter((SpinnerAdapter)fromResource);
        this.d.setOnItemSelectedListener((AdapterView$OnItemSelectedListener)this);
        (this.b = (Button)this.findViewById(R.id.buttonConfigure)).setOnClickListener((View$OnClickListener)this);
        (this.c = (Button)this.findViewById(R.id.buttonWifiSettings)).setOnClickListener((View$OnClickListener)this);
        this.f = new WifiDirectChannelSelection((Context)this, (WifiManager)this.getSystemService("wifi"));
    }
    
    public void onItemSelected(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
        switch (n) {
            default: {}
            case 0: {
                this.g = -1;
                this.h = -1;
            }
            case 1: {
                this.g = 81;
                this.h = 1;
            }
            case 2: {
                this.g = 81;
                this.h = 6;
            }
            case 3: {
                this.g = 81;
                this.h = 11;
            }
            case 4: {
                this.g = 124;
                this.h = 149;
            }
            case 5: {
                this.g = 124;
                this.h = 153;
            }
            case 6: {
                this.g = 124;
                this.h = 157;
            }
            case 7: {
                this.g = 124;
                this.h = 161;
            }
        }
    }
    
    public void onNothingSelected(final AdapterView<?> adapterView) {
    }
    
    protected void onStart() {
        super.onStart();
        this.d.setSelection(FtcWifiChannelSelectorActivity.a);
    }
}
