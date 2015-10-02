package com.qualcomm.robotcore.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.GroupInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Looper;
import com.qualcomm.robotcore.util.RobotLog;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WifiDirectAssistant {
   private static WifiDirectAssistant theAssistant = null;
   private final List<WifiP2pDevice> wifiP2pDevices = new ArrayList();
   private Context context = null;
   private boolean d = false;
   private final IntentFilter intentFilter;
   private final Channel channel;
   private final WifiP2pManager wifiP2pManager;
   private MyBroadcastReceiver broadcastReceiver;
   private final MyConnectionInfoListener connectionInfoListener;
   private final MyPeerListListener peerListListener;
   private final MyGroupInfoListener groupInfoListener;
   private int failureReason = 0;
   private WifiDirectAssistant.ConnectStatus connectStatus;
   private WifiDirectAssistant.Event n;
   private String macAddress;
   private String deviceName;
   private InetAddress groupOwnerAddress;
   private String xxdeviceAddress;
   private String xxdeviceName;
   private String passphrase;
   private boolean u;
   private int v;
   private WifiDirectAssistant.WifiDirectAssistantCallback callback;

   private WifiDirectAssistant(Context context) {
      this.connectStatus = WifiDirectAssistant.ConnectStatus.NOT_CONNECTED;
      this.n = null;
      this.macAddress = "";
      this.deviceName = "";
      this.groupOwnerAddress = null;
      this.xxdeviceAddress = "";
      this.xxdeviceName = "";
      this.passphrase = "";
      this.u = false;
      this.v = 0;
      this.callback = null;
      this.context = context;
      this.intentFilter = new IntentFilter();
      this.intentFilter.addAction("android.net.wifi.p2p.STATE_CHANGED");
      this.intentFilter.addAction("android.net.wifi.p2p.PEERS_CHANGED");
      this.intentFilter.addAction("android.net.wifi.p2p.CONNECTION_STATE_CHANGE");
      this.intentFilter.addAction("android.net.wifi.p2p.THIS_DEVICE_CHANGED");
      this.wifiP2pManager = (WifiP2pManager)context.getSystemService("wifip2p");
      this.channel = this.wifiP2pManager.initialize(context, Looper.getMainLooper(), (ChannelListener)null);
      this.broadcastReceiver = new MyBroadcastReceiver(null);
      this.connectionInfoListener = new MyConnectionInfoListener(null);
      this.peerListListener = new MyPeerListListener(null);
      this.groupInfoListener = new MyGroupInfoListener(null);
   }

   private void a(WifiP2pDevice device) {
      this.deviceName = device.deviceName;
      this.macAddress = device.deviceAddress;
      RobotLog.v("Wifi Direct device information: " + this.deviceName + " " + this.macAddress);
   }

   private void a(WifiDirectAssistant.Event event) {
      if(this.n != event || this.n == WifiDirectAssistant.Event.PEERS_AVAILABLE) {
         this.n = event;
         if(this.callback != null) {
            this.callback.onWifiDirectEvent(event);
            return;
         }
      }

   }

   public static String failureReasonToString(int failureReason) {
      switch(failureReason) {
      case 0:
         return "ERROR";
      case 1:
         return "P2P_UNSUPPORTED";
      case 2:
         return "BUSY";
      default:
         return "UNKNOWN (reason " + failureReason + ")";
      }
   }

   public static WifiDirectAssistant getWifiDirectAssistant(Context context) {
      synchronized(WifiDirectAssistant.class){}

      WifiDirectAssistant assistant;
      try {
         if(theAssistant == null) {
            theAssistant = new WifiDirectAssistant(context);
         }

         assistant = theAssistant;
      } finally {
         ;
      }

      return assistant;
   }

   public void cancelDiscoverPeers() {
      RobotLog.d("Wifi Direct stop discovering peers");
      this.wifiP2pManager.stopPeerDiscovery(this.channel, (ActionListener) null);
   }

   public void connect(WifiP2pDevice var1) {
      if(this.connectStatus != WifiDirectAssistant.ConnectStatus.CONNECTING && this.connectStatus != WifiDirectAssistant.ConnectStatus.CONNECTED) {
         RobotLog.d("WifiDirect connecting to " + var1.deviceAddress);
         this.connectStatus = WifiDirectAssistant.ConnectStatus.CONNECTING;
         WifiP2pConfig var2 = new WifiP2pConfig();
         var2.deviceAddress = var1.deviceAddress;
         var2.wps.setup = 0;
         var2.groupOwnerIntent = 1;
         this.wifiP2pManager.connect(this.channel, var2, new ActionListener()
         {
         public void onFailure(int var1)
            {
            String var2 = WifiDirectAssistant.failureReasonToString(var1);
            WifiDirectAssistant.this.failureReason = var1;
            RobotLog.d("WifiDirect connect cannot start - reason: " + var2);
            WifiDirectAssistant.this.a(WifiDirectAssistant.Event.ERROR);
            }

         public void onSuccess()
            {
            RobotLog.d("WifiDirect connect started");
            WifiDirectAssistant.this.a(WifiDirectAssistant.Event.CONNECTING);
            }
         });
      } else {
         RobotLog.d("WifiDirect connection request to " + var1.deviceAddress + " ignored, already connected");
      }
   }

   public void createGroup() {
      this.wifiP2pManager.createGroup(this.channel, new ActionListener()
      {
      public void onFailure(int var1)
         {
         if (var1 == 2)
            {
            RobotLog.d("Wifi Direct cannot create group, does group already exist?");
            }
         else
            {
            String var2 = WifiDirectAssistant.failureReasonToString(var1);
            WifiDirectAssistant.this.failureReason = var1;
            RobotLog.w("Wifi Direct failure while trying to create group - reason: " + var2);
            WifiDirectAssistant.this.connectStatus = WifiDirectAssistant.ConnectStatus.ERROR;
            WifiDirectAssistant.this.a(WifiDirectAssistant.Event.ERROR);
            }
         }

      public void onSuccess()
         {
         WifiDirectAssistant.this.connectStatus = WifiDirectAssistant.ConnectStatus.GROUP_OWNER;
         WifiDirectAssistant.this.a(WifiDirectAssistant.Event.GROUP_CREATED);
         RobotLog.d("Wifi Direct created group");
         }
      });
   }

   public void disable() {
      synchronized(this){}

      try {
         this.v += -1;
         RobotLog.v("There are " + this.v + " Wifi Direct Assistant Clients (-)");
         if(this.v == 0) {
            RobotLog.v("Disabling Wifi Direct Assistant");
            this.wifiP2pManager.stopPeerDiscovery(this.channel, (ActionListener) null);
            this.wifiP2pManager.cancelConnect(this.channel, (ActionListener) null);

            try {
               this.context.unregisterReceiver(this.broadcastReceiver);
            } catch (IllegalArgumentException var5) {
               ;
            }

            this.n = null;
         }
      } finally {
         ;
      }

   }

   public void discoverPeers() {
      this.wifiP2pManager.discoverPeers(this.channel, new ActionListener()
      {
      public void onFailure(int var1)
         {
         String var2 = WifiDirectAssistant.failureReasonToString(var1);
         WifiDirectAssistant.this.failureReason = var1;
         RobotLog.w("Wifi Direct failure while trying to discover peers - reason: " + var2);
         WifiDirectAssistant.this.a(WifiDirectAssistant.Event.ERROR);
         }

      public void onSuccess()
         {
         WifiDirectAssistant.this.a(WifiDirectAssistant.Event.DISCOVERING_PEERS);
         RobotLog.d("Wifi Direct discovering peers");
         }
      });
   }

   public void enable() {
      synchronized(this){}

      try {
         ++this.v;
         RobotLog.v("There are " + this.v + " Wifi Direct Assistant Clients (+)");
         if(this.v == 1) {
            RobotLog.v("Enabling Wifi Direct Assistant");
            if(this.broadcastReceiver == null) {
               this.broadcastReceiver = new MyBroadcastReceiver(null);
            }

            this.context.registerReceiver(this.broadcastReceiver, this.intentFilter);
         }
      } finally {
         ;
      }

   }

   public WifiDirectAssistant.WifiDirectAssistantCallback getCallback() {
      return this.callback;
   }

   public WifiDirectAssistant.ConnectStatus getConnectStatus() {
      return this.connectStatus;
   }

   public String getDeviceMacAddress() {
      return this.macAddress;
   }

   public String getDeviceName() {
      return this.deviceName;
   }

   public String getFailureReason() {
      return failureReasonToString(this.failureReason);
   }

   public InetAddress getGroupOwnerAddress() {
      return this.groupOwnerAddress;
   }

   public String getGroupOwnerMacAddress() {
      return this.xxdeviceAddress;
   }

   public String getGroupOwnerName() {
      return this.xxdeviceName;
   }

   public String getPassphrase() {
      return this.passphrase;
   }

   public List<WifiP2pDevice> getPeers() {
      return new ArrayList(this.wifiP2pDevices);
   }

   public boolean isConnected() {
      return this.connectStatus == WifiDirectAssistant.ConnectStatus.CONNECTED || this.connectStatus == WifiDirectAssistant.ConnectStatus.GROUP_OWNER;
   }

   public boolean isEnabled() {
      synchronized(this){}
      boolean var5 = false;

      int var2;
      try {
         var5 = true;
         var2 = this.v;
         var5 = false;
      } finally {
         if(var5) {
            ;
         }
      }

      boolean var3;
      if(var2 > 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean isGroupOwner() {
      return this.connectStatus == WifiDirectAssistant.ConnectStatus.GROUP_OWNER;
   }

   public boolean isWifiP2pEnabled() {
      return this.d;
   }

   public void removeGroup() {
      this.wifiP2pManager.removeGroup(this.channel, (ActionListener) null);
   }

   public void setCallback(WifiDirectAssistant.WifiDirectAssistantCallback var1) {
      this.callback = var1;
   }

   public static enum ConnectStatus {
      CONNECTED,
      CONNECTING,
      ERROR,
      GROUP_OWNER,
      NOT_CONNECTED;

      static {
         WifiDirectAssistant.ConnectStatus[] var0 = new WifiDirectAssistant.ConnectStatus[]{NOT_CONNECTED, CONNECTING, CONNECTED, GROUP_OWNER, ERROR};
      }
   }

   public static enum Event {
      CONNECTED_AS_GROUP_OWNER,
      CONNECTED_AS_PEER,
      CONNECTING,
      CONNECTION_INFO_AVAILABLE,
      DISCONNECTED,
      DISCOVERING_PEERS,
      ERROR,
      GROUP_CREATED,
      PEERS_AVAILABLE;

      static {
         WifiDirectAssistant.Event[] var0 = new WifiDirectAssistant.Event[]{DISCOVERING_PEERS, PEERS_AVAILABLE, GROUP_CREATED, CONNECTING, CONNECTED_AS_PEER, CONNECTED_AS_GROUP_OWNER, DISCONNECTED, CONNECTION_INFO_AVAILABLE, ERROR};
      }
   }

   public interface WifiDirectAssistantCallback {
      void onWifiDirectEvent(WifiDirectAssistant.Event var1);
   }

   private class MyConnectionInfoListener implements ConnectionInfoListener {
      private MyConnectionInfoListener() {
      }

      // $FF: synthetic method
      MyConnectionInfoListener(Object var2) {
         this();
      }

      public void onConnectionInfoAvailable(WifiP2pInfo var1) {
         WifiDirectAssistant.this.wifiP2pManager.requestGroupInfo(WifiDirectAssistant.this.channel, WifiDirectAssistant.this.groupInfoListener);
         WifiDirectAssistant.this.groupOwnerAddress = var1.groupOwnerAddress;
         RobotLog.d("Group owners address: " + WifiDirectAssistant.this.groupOwnerAddress.toString());
         if(var1.groupFormed && var1.isGroupOwner) {
            RobotLog.d("Wifi Direct group formed, this device is the group owner (GO)");
            WifiDirectAssistant.this.connectStatus = WifiDirectAssistant.ConnectStatus.GROUP_OWNER;
            WifiDirectAssistant.this.a(WifiDirectAssistant.Event.CONNECTED_AS_GROUP_OWNER);
         } else if(var1.groupFormed) {
            RobotLog.d("Wifi Direct group formed, this device is a client");
            WifiDirectAssistant.this.connectStatus = WifiDirectAssistant.ConnectStatus.CONNECTED;
            WifiDirectAssistant.this.a(WifiDirectAssistant.Event.CONNECTED_AS_PEER);
         } else {
            RobotLog.d("Wifi Direct group NOT formed, ERROR: " + var1.toString());
            WifiDirectAssistant.this.failureReason = 0;
            WifiDirectAssistant.this.connectStatus = WifiDirectAssistant.ConnectStatus.ERROR;
            WifiDirectAssistant.this.a(WifiDirectAssistant.Event.ERROR);
         }
      }
   }

   private class MyGroupInfoListener implements GroupInfoListener {
      private MyGroupInfoListener() {
      }

      // $FF: synthetic method
      MyGroupInfoListener(Object var2) {
         this();
      }

      public void onGroupInfoAvailable(WifiP2pGroup group) {
         if(group != null) {
            if(group.isGroupOwner()) {
               WifiDirectAssistant.this.xxdeviceAddress = WifiDirectAssistant.this.macAddress;
               WifiDirectAssistant.this.xxdeviceName = WifiDirectAssistant.this.deviceName;
            } else {
               WifiP2pDevice groupOwner = group.getOwner();
               WifiDirectAssistant.this.xxdeviceAddress = groupOwner.deviceAddress;
               WifiDirectAssistant.this.xxdeviceName = groupOwner.deviceName;
            }

            WifiDirectAssistant.this.passphrase = group.getPassphrase();
            WifiDirectAssistant assistant = WifiDirectAssistant.this;
            String var7;
            if(WifiDirectAssistant.this.passphrase != null) {
               var7 = WifiDirectAssistant.this.passphrase;
            } else {
               var7 = "";
            }

            assistant.passphrase = var7;
            RobotLog.v("Wifi Direct connection information available");
            WifiDirectAssistant.this.a(WifiDirectAssistant.Event.CONNECTION_INFO_AVAILABLE);
         }
      }
   }

   private class MyPeerListListener implements PeerListListener {
      private MyPeerListListener() {
      }

      // $FF: synthetic method
      MyPeerListListener(Object var2) {
         this();
      }

      public void onPeersAvailable(WifiP2pDeviceList var1) {
         WifiDirectAssistant.this.wifiP2pDevices.clear();
         WifiDirectAssistant.this.wifiP2pDevices.addAll(var1.getDeviceList());
         RobotLog.v("Wifi Direct peers found: " + WifiDirectAssistant.this.wifiP2pDevices.size());
         Iterator var3 = WifiDirectAssistant.this.wifiP2pDevices.iterator();

         while(var3.hasNext()) {
            WifiP2pDevice var4 = (WifiP2pDevice)var3.next();
            RobotLog.v("    peer: " + var4.deviceAddress + " " + var4.deviceName);
         }

         WifiDirectAssistant.this.a(WifiDirectAssistant.Event.PEERS_AVAILABLE);
      }
   }

   private class MyBroadcastReceiver extends BroadcastReceiver {
      private MyBroadcastReceiver() {
      }

      MyBroadcastReceiver(Object var2) {
         this();
      }

      public void onReceive(Context var1, Intent var2) {
         String var3 = var2.getAction();
         if("android.net.wifi.p2p.STATE_CHANGED".equals(var3)) {
            int var8 = var2.getIntExtra("wifi_p2p_state", -1);
            WifiDirectAssistant var9 = WifiDirectAssistant.this;
            boolean var10;
            if(var8 == 2) {
               var10 = true;
            } else {
               var10 = false;
            }

            var9.d = var10;
            RobotLog.d("Wifi Direct state - enabled: " + WifiDirectAssistant.this.d);
         } else {
            if("android.net.wifi.p2p.PEERS_CHANGED".equals(var3)) {
               RobotLog.d("Wifi Direct peers changed");
               WifiDirectAssistant.this.wifiP2pManager.requestPeers(WifiDirectAssistant.this.channel, WifiDirectAssistant.this.peerListListener);
               return;
            }

            if("android.net.wifi.p2p.CONNECTION_STATE_CHANGE".equals(var3)) {
               NetworkInfo var4 = (NetworkInfo)var2.getParcelableExtra("networkInfo");
               WifiP2pInfo var5 = (WifiP2pInfo)var2.getParcelableExtra("wifiP2pInfo");
               RobotLog.d("Wifi Direct connection changed - connected: " + var4.isConnected());
               if(var4.isConnected()) {
                  WifiDirectAssistant.this.wifiP2pManager.requestConnectionInfo(WifiDirectAssistant.this.channel, WifiDirectAssistant.this.connectionInfoListener);
                  WifiDirectAssistant.this.wifiP2pManager.stopPeerDiscovery(WifiDirectAssistant.this.channel, (ActionListener) null);
                  return;
               }

               WifiDirectAssistant.this.connectStatus = WifiDirectAssistant.ConnectStatus.NOT_CONNECTED;
               if(!WifiDirectAssistant.this.u) {
                  WifiDirectAssistant.this.discoverPeers();
               }

               if(WifiDirectAssistant.this.isConnected()) {
                  WifiDirectAssistant.this.a(WifiDirectAssistant.Event.DISCONNECTED);
               }

               WifiDirectAssistant.this.u = var5.groupFormed;
               return;
            }

            if("android.net.wifi.p2p.THIS_DEVICE_CHANGED".equals(var3)) {
               RobotLog.d("Wifi Direct this device changed");
               WifiDirectAssistant.this.a((WifiP2pDevice)var2.getParcelableExtra("wifiP2pDevice"));
               return;
            }
         }

      }
   }
}
