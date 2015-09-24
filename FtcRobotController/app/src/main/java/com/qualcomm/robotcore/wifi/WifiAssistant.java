package com.qualcomm.robotcore.wifi;

import android.net.NetworkInfo;
import android.content.Intent;
import android.content.BroadcastReceiver;
import com.qualcomm.robotcore.util.RobotLog;
import android.content.Context;
import android.content.IntentFilter;

public class WifiAssistant
{
    private final IntentFilter a;
    private final Context b;
    private final a c;
    
    public WifiAssistant(final Context b, final WifiAssistantCallback wifiAssistantCallback) {
        this.b = b;
        if (wifiAssistantCallback == null) {
            RobotLog.v("WifiAssistantCallback is null");
        }
        this.c = new a(wifiAssistantCallback);
        (this.a = new IntentFilter()).addAction("android.net.wifi.STATE_CHANGE");
    }
    
    public void disable() {
        this.b.unregisterReceiver((BroadcastReceiver)this.c);
    }
    
    public void enable() {
        this.b.registerReceiver((BroadcastReceiver)this.c, this.a);
    }
    
    public interface WifiAssistantCallback
    {
        void wifiEventCallback(WifiState p0);
    }
    
    public enum WifiState
    {
        CONNECTED, 
        NOT_CONNECTED;
    }
    
    private static class a extends BroadcastReceiver
    {
        private WifiState a;
        private final WifiAssistantCallback b;
        
        public a(final WifiAssistantCallback b) {
            this.a = null;
            this.b = b;
        }
        
        private void a(final WifiState a) {
            if (this.a != a) {
                this.a = a;
                if (this.b != null) {
                    this.b.wifiEventCallback(this.a);
                }
            }
        }
        
        public void onReceive(final Context context, final Intent intent) {
            if (intent.getAction().equals("android.net.wifi.STATE_CHANGE")) {
                if (!((NetworkInfo)intent.getParcelableExtra("networkInfo")).isConnected()) {
                    this.a(WifiState.NOT_CONNECTED);
                    return;
                }
                this.a(WifiState.CONNECTED);
            }
        }
    }
}
