package com.qualcomm.robotcore.wifi;

import android.net.NetworkInfo;
import android.content.Intent;
import java.util.Iterator;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager$PeerListListener;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager$GroupInfoListener;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager$ConnectionInfoListener;
import java.util.Collection;
import android.content.BroadcastReceiver;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager$ActionListener;
import com.qualcomm.robotcore.util.RobotLog;
import android.net.wifi.p2p.WifiP2pManager$ChannelListener;
import android.os.Looper;
import java.util.ArrayList;
import java.net.InetAddress;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager$Channel;
import android.content.IntentFilter;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import java.util.List;

public class WifiDirectAssistant
{
    private static WifiDirectAssistant a;
    private final List<WifiP2pDevice> b;
    private Context c;
    private boolean d;
    private final IntentFilter e;
    private final WifiP2pManager$Channel f;
    private final WifiP2pManager g;
    private d h;
    private final a i;
    private final c j;
    private final b k;
    private int l;
    private ConnectStatus m;
    private Event n;
    private String o;
    private String p;
    private InetAddress q;
    private String r;
    private String s;
    private String t;
    private boolean u;
    private int v;
    private WifiDirectAssistantCallback w;
    
    static {
        WifiDirectAssistant.a = null;
    }
    
    private WifiDirectAssistant(final Context c) {
        this.b = new ArrayList<WifiP2pDevice>();
        this.c = null;
        this.d = false;
        this.l = 0;
        this.m = ConnectStatus.NOT_CONNECTED;
        this.n = null;
        this.o = "";
        this.p = "";
        this.q = null;
        this.r = "";
        this.s = "";
        this.t = "";
        this.u = false;
        this.v = 0;
        this.w = null;
        this.c = c;
        (this.e = new IntentFilter()).addAction("android.net.wifi.p2p.STATE_CHANGED");
        this.e.addAction("android.net.wifi.p2p.PEERS_CHANGED");
        this.e.addAction("android.net.wifi.p2p.CONNECTION_STATE_CHANGE");
        this.e.addAction("android.net.wifi.p2p.THIS_DEVICE_CHANGED");
        this.g = (WifiP2pManager)c.getSystemService("wifip2p");
        this.f = this.g.initialize(c, Looper.getMainLooper(), (WifiP2pManager$ChannelListener)null);
        this.h = new d();
        this.i = new a();
        this.j = new c();
        this.k = new b();
    }
    
    private void a(final WifiP2pDevice wifiP2pDevice) {
        this.p = wifiP2pDevice.deviceName;
        this.o = wifiP2pDevice.deviceAddress;
        RobotLog.v("Wifi Direct device information: " + this.p + " " + this.o);
    }
    
    private void a(final Event n) {
        if (this.n != n || this.n == Event.PEERS_AVAILABLE) {
            this.n = n;
            if (this.w != null) {
                this.w.onWifiDirectEvent(n);
            }
        }
    }
    
    public static String failureReasonToString(final int n) {
        switch (n) {
            default: {
                return "UNKNOWN (reason " + n + ")";
            }
            case 1: {
                return "P2P_UNSUPPORTED";
            }
            case 0: {
                return "ERROR";
            }
            case 2: {
                return "BUSY";
            }
        }
    }
    
    public static WifiDirectAssistant getWifiDirectAssistant(final Context context) {
        synchronized (WifiDirectAssistant.class) {
            if (WifiDirectAssistant.a == null) {
                WifiDirectAssistant.a = new WifiDirectAssistant(context);
            }
            return WifiDirectAssistant.a;
        }
    }
    
    public void cancelDiscoverPeers() {
        RobotLog.d("Wifi Direct stop discovering peers");
        this.g.stopPeerDiscovery(this.f, (WifiP2pManager$ActionListener)null);
    }
    
    public void connect(final WifiP2pDevice wifiP2pDevice) {
        if (this.m == ConnectStatus.CONNECTING || this.m == ConnectStatus.CONNECTED) {
            RobotLog.d("WifiDirect connection request to " + wifiP2pDevice.deviceAddress + " ignored, already connected");
            return;
        }
        RobotLog.d("WifiDirect connecting to " + wifiP2pDevice.deviceAddress);
        this.m = ConnectStatus.CONNECTING;
        final WifiP2pConfig wifiP2pConfig = new WifiP2pConfig();
        wifiP2pConfig.deviceAddress = wifiP2pDevice.deviceAddress;
        wifiP2pConfig.wps.setup = 0;
        wifiP2pConfig.groupOwnerIntent = 1;
        this.g.connect(this.f, wifiP2pConfig, (WifiP2pManager$ActionListener)new WifiP2pManager$ActionListener() {
            public void onFailure(final int n) {
                final String failureReasonToString = WifiDirectAssistant.failureReasonToString(n);
                WifiDirectAssistant.this.l = n;
                RobotLog.d("WifiDirect connect cannot start - reason: " + failureReasonToString);
                WifiDirectAssistant.this.a(Event.ERROR);
            }
            
            public void onSuccess() {
                RobotLog.d("WifiDirect connect started");
                WifiDirectAssistant.this.a(Event.CONNECTING);
            }
        });
    }
    
    public void createGroup() {
        this.g.createGroup(this.f, (WifiP2pManager$ActionListener)new WifiP2pManager$ActionListener() {
            public void onFailure(final int n) {
                if (n == 2) {
                    RobotLog.d("Wifi Direct cannot create group, does group already exist?");
                    return;
                }
                final String failureReasonToString = WifiDirectAssistant.failureReasonToString(n);
                WifiDirectAssistant.this.l = n;
                RobotLog.w("Wifi Direct failure while trying to create group - reason: " + failureReasonToString);
                WifiDirectAssistant.this.m = ConnectStatus.ERROR;
                WifiDirectAssistant.this.a(Event.ERROR);
            }
            
            public void onSuccess() {
                WifiDirectAssistant.this.m = ConnectStatus.GROUP_OWNER;
                WifiDirectAssistant.this.a(Event.GROUP_CREATED);
                RobotLog.d("Wifi Direct created group");
            }
        });
    }
    
    public void disable() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: monitorenter   
        //     2: aload_0        
        //     3: iconst_m1      
        //     4: aload_0        
        //     5: getfield        com/qualcomm/robotcore/wifi/WifiDirectAssistant.v:I
        //     8: iadd           
        //     9: putfield        com/qualcomm/robotcore/wifi/WifiDirectAssistant.v:I
        //    12: new             Ljava/lang/StringBuilder;
        //    15: dup            
        //    16: invokespecial   java/lang/StringBuilder.<init>:()V
        //    19: ldc_w           "There are "
        //    22: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    25: aload_0        
        //    26: getfield        com/qualcomm/robotcore/wifi/WifiDirectAssistant.v:I
        //    29: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //    32: ldc_w           " Wifi Direct Assistant Clients (-)"
        //    35: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    38: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    41: invokestatic    com/qualcomm/robotcore/util/RobotLog.v:(Ljava/lang/String;)V
        //    44: aload_0        
        //    45: getfield        com/qualcomm/robotcore/wifi/WifiDirectAssistant.v:I
        //    48: ifne            97
        //    51: ldc_w           "Disabling Wifi Direct Assistant"
        //    54: invokestatic    com/qualcomm/robotcore/util/RobotLog.v:(Ljava/lang/String;)V
        //    57: aload_0        
        //    58: getfield        com/qualcomm/robotcore/wifi/WifiDirectAssistant.g:Landroid/net/wifi/p2p/WifiP2pManager;
        //    61: aload_0        
        //    62: getfield        com/qualcomm/robotcore/wifi/WifiDirectAssistant.f:Landroid/net/wifi/p2p/WifiP2pManager$Channel;
        //    65: aconst_null    
        //    66: invokevirtual   android/net/wifi/p2p/WifiP2pManager.stopPeerDiscovery:(Landroid/net/wifi/p2p/WifiP2pManager$Channel;Landroid/net/wifi/p2p/WifiP2pManager$ActionListener;)V
        //    69: aload_0        
        //    70: getfield        com/qualcomm/robotcore/wifi/WifiDirectAssistant.g:Landroid/net/wifi/p2p/WifiP2pManager;
        //    73: aload_0        
        //    74: getfield        com/qualcomm/robotcore/wifi/WifiDirectAssistant.f:Landroid/net/wifi/p2p/WifiP2pManager$Channel;
        //    77: aconst_null    
        //    78: invokevirtual   android/net/wifi/p2p/WifiP2pManager.cancelConnect:(Landroid/net/wifi/p2p/WifiP2pManager$Channel;Landroid/net/wifi/p2p/WifiP2pManager$ActionListener;)V
        //    81: aload_0        
        //    82: getfield        com/qualcomm/robotcore/wifi/WifiDirectAssistant.c:Landroid/content/Context;
        //    85: aload_0        
        //    86: getfield        com/qualcomm/robotcore/wifi/WifiDirectAssistant.h:Lcom/qualcomm/robotcore/wifi/WifiDirectAssistant$d;
        //    89: invokevirtual   android/content/Context.unregisterReceiver:(Landroid/content/BroadcastReceiver;)V
        //    92: aload_0        
        //    93: aconst_null    
        //    94: putfield        com/qualcomm/robotcore/wifi/WifiDirectAssistant.n:Lcom/qualcomm/robotcore/wifi/WifiDirectAssistant$Event;
        //    97: aload_0        
        //    98: monitorexit    
        //    99: return         
        //   100: astore_1       
        //   101: aload_0        
        //   102: monitorexit    
        //   103: aload_1        
        //   104: athrow         
        //   105: astore_2       
        //   106: goto            92
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                
        //  -----  -----  -----  -----  ------------------------------------
        //  2      81     100    105    Any
        //  81     92     105    109    Ljava/lang/IllegalArgumentException;
        //  81     92     100    105    Any
        //  92     97     100    105    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0092:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at the.bytecode.club.bytecodeviewer.decompilers.ProcyonDecompiler.doSaveJarDecompiled(ProcyonDecompiler.java:194)
        //     at the.bytecode.club.bytecodeviewer.decompilers.ProcyonDecompiler.decompileToZip(ProcyonDecompiler.java:146)
        //     at the.bytecode.club.bytecodeviewer.gui.MainViewerGUI$18$1$2.run(MainViewerGUI.java:1093)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public void discoverPeers() {
        this.g.discoverPeers(this.f, (WifiP2pManager$ActionListener)new WifiP2pManager$ActionListener() {
            public void onFailure(final int n) {
                final String failureReasonToString = WifiDirectAssistant.failureReasonToString(n);
                WifiDirectAssistant.this.l = n;
                RobotLog.w("Wifi Direct failure while trying to discover peers - reason: " + failureReasonToString);
                WifiDirectAssistant.this.a(Event.ERROR);
            }
            
            public void onSuccess() {
                WifiDirectAssistant.this.a(Event.DISCOVERING_PEERS);
                RobotLog.d("Wifi Direct discovering peers");
            }
        });
    }
    
    public void enable() {
        synchronized (this) {
            ++this.v;
            RobotLog.v("There are " + this.v + " Wifi Direct Assistant Clients (+)");
            if (this.v == 1) {
                RobotLog.v("Enabling Wifi Direct Assistant");
                if (this.h == null) {
                    this.h = new d();
                }
                this.c.registerReceiver((BroadcastReceiver)this.h, this.e);
            }
        }
    }
    
    public WifiDirectAssistantCallback getCallback() {
        return this.w;
    }
    
    public ConnectStatus getConnectStatus() {
        return this.m;
    }
    
    public String getDeviceMacAddress() {
        return this.o;
    }
    
    public String getDeviceName() {
        return this.p;
    }
    
    public String getFailureReason() {
        return failureReasonToString(this.l);
    }
    
    public InetAddress getGroupOwnerAddress() {
        return this.q;
    }
    
    public String getGroupOwnerMacAddress() {
        return this.r;
    }
    
    public String getGroupOwnerName() {
        return this.s;
    }
    
    public String getPassphrase() {
        return this.t;
    }
    
    public List<WifiP2pDevice> getPeers() {
        return new ArrayList<WifiP2pDevice>(this.b);
    }
    
    public boolean isConnected() {
        return this.m == ConnectStatus.CONNECTED || this.m == ConnectStatus.GROUP_OWNER;
    }
    
    public boolean isEnabled() {
        synchronized (this) {
            return this.v > 0;
        }
    }
    
    public boolean isGroupOwner() {
        return this.m == ConnectStatus.GROUP_OWNER;
    }
    
    public boolean isWifiP2pEnabled() {
        return this.d;
    }
    
    public void removeGroup() {
        this.g.removeGroup(this.f, (WifiP2pManager$ActionListener)null);
    }
    
    public void setCallback(final WifiDirectAssistantCallback w) {
        this.w = w;
    }
    
    public enum ConnectStatus
    {
        CONNECTED, 
        CONNECTING, 
        ERROR, 
        GROUP_OWNER, 
        NOT_CONNECTED;
    }
    
    public enum Event
    {
        CONNECTED_AS_GROUP_OWNER, 
        CONNECTED_AS_PEER, 
        CONNECTING, 
        CONNECTION_INFO_AVAILABLE, 
        DISCONNECTED, 
        DISCOVERING_PEERS, 
        ERROR, 
        GROUP_CREATED, 
        PEERS_AVAILABLE;
    }
    
    public interface WifiDirectAssistantCallback
    {
        void onWifiDirectEvent(Event p0);
    }
    
    private class a implements WifiP2pManager$ConnectionInfoListener
    {
        public void onConnectionInfoAvailable(final WifiP2pInfo wifiP2pInfo) {
            WifiDirectAssistant.this.g.requestGroupInfo(WifiDirectAssistant.this.f, (WifiP2pManager$GroupInfoListener)WifiDirectAssistant.this.k);
            WifiDirectAssistant.this.q = wifiP2pInfo.groupOwnerAddress;
            RobotLog.d("Group owners address: " + WifiDirectAssistant.this.q.toString());
            if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
                RobotLog.d("Wifi Direct group formed, this device is the group owner (GO)");
                WifiDirectAssistant.this.m = ConnectStatus.GROUP_OWNER;
                WifiDirectAssistant.this.a(Event.CONNECTED_AS_GROUP_OWNER);
                return;
            }
            if (wifiP2pInfo.groupFormed) {
                RobotLog.d("Wifi Direct group formed, this device is a client");
                WifiDirectAssistant.this.m = ConnectStatus.CONNECTED;
                WifiDirectAssistant.this.a(Event.CONNECTED_AS_PEER);
                return;
            }
            RobotLog.d("Wifi Direct group NOT formed, ERROR: " + wifiP2pInfo.toString());
            WifiDirectAssistant.this.l = 0;
            WifiDirectAssistant.this.m = ConnectStatus.ERROR;
            WifiDirectAssistant.this.a(Event.ERROR);
        }
    }
    
    private class b implements WifiP2pManager$GroupInfoListener
    {
        public void onGroupInfoAvailable(final WifiP2pGroup wifiP2pGroup) {
            if (wifiP2pGroup == null) {
                return;
            }
            if (wifiP2pGroup.isGroupOwner()) {
                WifiDirectAssistant.this.r = WifiDirectAssistant.this.o;
                WifiDirectAssistant.this.s = WifiDirectAssistant.this.p;
            }
            else {
                final WifiP2pDevice owner = wifiP2pGroup.getOwner();
                WifiDirectAssistant.this.r = owner.deviceAddress;
                WifiDirectAssistant.this.s = owner.deviceName;
            }
            WifiDirectAssistant.this.t = wifiP2pGroup.getPassphrase();
            final WifiDirectAssistant a = WifiDirectAssistant.this;
            String h;
            if (WifiDirectAssistant.this.t != null) {
                h = WifiDirectAssistant.this.t;
            }
            else {
                h = "";
            }
            a.t = h;
            RobotLog.v("Wifi Direct connection information available");
            WifiDirectAssistant.this.a(Event.CONNECTION_INFO_AVAILABLE);
        }
    }
    
    private class c implements WifiP2pManager$PeerListListener
    {
        public void onPeersAvailable(final WifiP2pDeviceList list) {
            WifiDirectAssistant.this.b.clear();
            WifiDirectAssistant.this.b.addAll(list.getDeviceList());
            RobotLog.v("Wifi Direct peers found: " + WifiDirectAssistant.this.b.size());
            for (final WifiP2pDevice wifiP2pDevice : WifiDirectAssistant.this.b) {
                RobotLog.v("    peer: " + wifiP2pDevice.deviceAddress + " " + wifiP2pDevice.deviceName);
            }
            WifiDirectAssistant.this.a(Event.PEERS_AVAILABLE);
        }
    }
    
    private class d extends BroadcastReceiver
    {
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if ("android.net.wifi.p2p.STATE_CHANGED".equals(action)) {
                WifiDirectAssistant.this.d = (intent.getIntExtra("wifi_p2p_state", -1) == 2);
                RobotLog.d("Wifi Direct state - enabled: " + WifiDirectAssistant.this.d);
            }
            else {
                if ("android.net.wifi.p2p.PEERS_CHANGED".equals(action)) {
                    RobotLog.d("Wifi Direct peers changed");
                    WifiDirectAssistant.this.g.requestPeers(WifiDirectAssistant.this.f, (WifiP2pManager$PeerListListener)WifiDirectAssistant.this.j);
                    return;
                }
                if ("android.net.wifi.p2p.CONNECTION_STATE_CHANGE".equals(action)) {
                    final NetworkInfo networkInfo = (NetworkInfo)intent.getParcelableExtra("networkInfo");
                    final WifiP2pInfo wifiP2pInfo = (WifiP2pInfo)intent.getParcelableExtra("wifiP2pInfo");
                    RobotLog.d("Wifi Direct connection changed - connected: " + networkInfo.isConnected());
                    if (networkInfo.isConnected()) {
                        WifiDirectAssistant.this.g.requestConnectionInfo(WifiDirectAssistant.this.f, (WifiP2pManager$ConnectionInfoListener)WifiDirectAssistant.this.i);
                        WifiDirectAssistant.this.g.stopPeerDiscovery(WifiDirectAssistant.this.f, (WifiP2pManager$ActionListener)null);
                        return;
                    }
                    WifiDirectAssistant.this.m = ConnectStatus.NOT_CONNECTED;
                    if (!WifiDirectAssistant.this.u) {
                        WifiDirectAssistant.this.discoverPeers();
                    }
                    if (WifiDirectAssistant.this.isConnected()) {
                        WifiDirectAssistant.this.a(Event.DISCONNECTED);
                    }
                    WifiDirectAssistant.this.u = wifiP2pInfo.groupFormed;
                }
                else if ("android.net.wifi.p2p.THIS_DEVICE_CHANGED".equals(action)) {
                    RobotLog.d("Wifi Direct this device changed");
                    WifiDirectAssistant.this.a((WifiP2pDevice)intent.getParcelableExtra("wifiP2pDevice"));
                }
            }
        }
    }
}
