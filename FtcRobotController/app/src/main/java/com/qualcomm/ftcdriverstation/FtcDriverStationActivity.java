package com.qualcomm.ftcdriverstation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.qualcomm.analytics.Analytics;
import com.qualcomm.ftccommon.ConfigWifiDirectActivity;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftcdriverstation.OpModeSelectionDialogFragment;
import com.qualcomm.ftcdriverstation.SettingsActivity;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.hardware.logitech.LogitechGamepadF310;
import com.qualcomm.robotcore.hardware.microsoft.MicrosoftGamepadXbox360;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.Heartbeat;
import com.qualcomm.robotcore.robocol.PeerDiscoveryManager;
import com.qualcomm.robotcore.robocol.RobocolDatagram;
import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.util.BatteryChecker;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.ImmersiveMode;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.RollingAverage;
import com.qualcomm.robotcore.util.Util;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FtcDriverStationActivity extends Activity implements WifiDirectAssistant.WifiDirectAssistantCallback, OnSharedPreferenceChangeListener, OpModeSelectionDialogFragment.OpModeSelectionDialogListener, BatteryChecker.BatteryWatcher {
   public static final double ASSUME_DISCONNECT_TIMER = 2.0D;
   protected static final float FULLY_OPAQUE = 1.0F;
   protected static final int MAX_COMMAND_ATTEMPTS = 10;
   protected static final int MAX_LOG_SIZE = 2048;
   protected static final float PARTLY_OPAQUE = 0.3F;
   protected Analytics analytics;
   AnimationListener animationListener_icon1 = new AnimationListener() {
      public void onAnimationEnd(Animation var1) {
         FtcDriverStationActivity.this.userIcon_1_active.setImageResource(2130837514);
      }

      public void onAnimationRepeat(Animation var1) {
         FtcDriverStationActivity.this.userIcon_1_active.setImageResource(2130837514);
      }

      public void onAnimationStart(Animation var1) {
      }
   };
   AnimationListener animationListener_icon2 = new AnimationListener() {
      public void onAnimationEnd(Animation var1) {
         FtcDriverStationActivity.this.userIcon_2_active.setImageResource(2130837514);
      }

      public void onAnimationRepeat(Animation var1) {
         FtcDriverStationActivity.this.userIcon_2_active.setImageResource(2130837514);
      }

      public void onAnimationStart(Animation var1) {
      }
   };
   protected BatteryChecker batteryChecker;
   protected View batteryInfo;
   protected ImageButton buttonInit;
   protected ImageButton buttonInitStop;
   protected ImageButton buttonMenu;
   protected Button buttonSelect;
   protected ImageButton buttonStart;
   protected ImageButton buttonStartTimed;
   protected ImageButton buttonStop;
   protected boolean clientConnected = false;
   protected Context context;
   protected View controlPanelBack;
   protected ImageView dsBatteryIcon;
   protected TextView dsBatteryInfo;
   protected boolean enableNetworkTrafficLogging = false;
   protected Map<Integer, Gamepad> gamepads = new HashMap();
   protected String groupOwnerMac;
   protected Heartbeat heartbeatRecv = new Heartbeat();
   protected Heartbeat heartbeatSend = new Heartbeat();
   protected ImmersiveMode immersion;
   protected ElapsedTime lastRecvPacket = new ElapsedTime();
   protected ElapsedTime lastUiUpdate = new ElapsedTime();
   protected FtcDriverStationActivity.OpModeCountDownTimer opModeCountDown = new FtcDriverStationActivity.OpModeCountDownTimer(null);
   protected boolean opModeUseTimer = false;
   protected Set<String> opModes = new LinkedHashSet();
   protected PeerDiscoveryManager peerDiscoveryManager;
   protected Set<Command> pendingCommands = Collections.newSetFromMap(new ConcurrentHashMap());
   protected RollingAverage pingAverage = new RollingAverage(10);
   protected SharedPreferences preferences;
   protected String queuedOpMode = "Stop Robot";
   protected ImageView rcBatteryIcon;
   protected TextView rcBatteryTelemetry;
   protected ExecutorService recvLoopService;
   protected InetAddress remoteAddr;
   protected TextView robotBatteryTelemetry;
   protected RobotState robotState;
   protected ScheduledFuture<?> sendLoopFuture;
   protected ScheduledExecutorService sendLoopService = Executors.newSingleThreadScheduledExecutor();
   protected boolean setupNeeded = true;
   protected RobocolDatagramSocket socket;
   protected TextView systemTelemetry;
   protected TextView textDeviceName;
   protected TextView textPingStatus;
   protected TextView textTelemetry;
   protected TextView textTimer;
   protected TextView textWifiDirectStatus;
   protected ImageView userIcon_1_active;
   protected ImageView userIcon_1_base;
   protected ImageView userIcon_2_active;
   protected ImageView userIcon_2_base;
   protected Map<Integer, Integer> userToGamepadMap = new HashMap();
   protected Utility utility;
   protected WifiDirectAssistant wifiDirect;
   protected View wifiInfo;

   private void setBatteryIcon(final float var1, final ImageView var2) {
      this.runOnUiThread(new Runnable() {
         public void run() {
            if(var1 <= 15.0F) {
               var2.setImageResource(2130837509);
            } else if(var1 > 15.0F && var1 <= 45.0F) {
               var2.setImageResource(2130837511);
            } else if(var1 > 45.0F && var1 <= 65.0F) {
               var2.setImageResource(2130837512);
            } else if(var1 > 65.0F && var1 <= 85.0F) {
               var2.setImageResource(2130837513);
            } else {
               var2.setImageResource(2130837510);
            }
         }
      });
   }

   protected void assignNewGamepad(int var1, int var2) {
      HashSet var3 = new HashSet();
      Iterator var4 = this.userToGamepadMap.entrySet().iterator();

      while(var4.hasNext()) {
         Entry var10 = (Entry)var4.next();
         if(((Integer)var10.getValue()).intValue() == var2) {
            var3.add(var10.getKey());
         }
      }

      Iterator var5 = var3.iterator();

      while(var5.hasNext()) {
         Integer var8 = (Integer)var5.next();
         this.userToGamepadMap.remove(var8);
      }

      this.userToGamepadMap.put(Integer.valueOf(var1), Integer.valueOf(var2));
      this.initGamepad(var1, var2);
      Object[] var7 = new Object[]{Integer.valueOf(var1), ((Gamepad)this.gamepads.get(Integer.valueOf(var2))).type(), Integer.valueOf(var2)};
      DbgLog.msg(String.format("Gamepad %d detected as %s (ID %d)", var7));
   }

   protected void assumeClientConnect() {
      DbgLog.msg("Assuming client connected");
      this.clientConnected = true;
      this.uiRobotControllerIsConnected();
      this.pendingCommands.add(new Command("CMD_REQUEST_OP_MODE_LIST"));
   }

   protected void assumeClientDisconnect() {
      DbgLog.msg("Assuming client disconnected");
      this.clientConnected = false;
      this.opModeUseTimer = false;
      this.opModeCountDown.stop();
      this.opModeCountDown.setCountdown(30L);
      this.setTextView(this.textTimer, "");
      this.setImageResource(this.buttonStartTimed, 2130837521);
      this.queuedOpMode = "Stop Robot";
      this.opModes.clear();
      this.pingStatus(" ");
      this.pendingCommands.clear();
      this.remoteAddr = null;
      RobotLog.clearGlobalErrorMsg();
      this.uiRobotControllerIsDisconnected();
   }

   protected void clearInfo() {
      this.setVisibility(this.systemTelemetry, 8);
      this.setTextView(this.textTelemetry, "");
   }

   protected void commandEvent(RobocolDatagram var1) {
      try {
         Command var2 = new Command(var1.getData());
         if(var2.isAcknowledged()) {
            this.pendingCommands.remove(var2);
         } else {
            DbgLog.msg(" processing command: " + var2.getName());
            var2.acknowledge();
            this.pendingCommands.add(var2);
            String var5 = var2.getName();
            String var6 = var2.getExtra();
            if(var5.equals("CMD_REQUEST_OP_MODE_LIST_RESP")) {
               this.handleCommandRequestOpModeListResp(var6);
            } else if(var5.equals("CMD_INIT_OP_MODE_RESP")) {
               this.handleCommandInitOpModeResp(var6);
            } else if(var5.equals("CMD_RUN_OP_MODE_RESP")) {
               this.handleCommandStartOpModeResp(var6);
            } else {
               DbgLog.msg("Unable to process command " + var5);
            }
         }
      } catch (RobotCoreException var7) {
         DbgLog.logStacktrace(var7);
      }
   }

   public boolean dispatchGenericMotionEvent(MotionEvent var1) {
      if(Gamepad.isGamepadDevice(var1.getDeviceId())) {
         this.handleGamepadEvent(var1);
         return true;
      } else {
         return super.dispatchGenericMotionEvent(var1);
      }
   }

   public boolean dispatchKeyEvent(KeyEvent var1) {
      if(Gamepad.isGamepadDevice(var1.getDeviceId())) {
         this.handleGamepadEvent(var1);
         return true;
      } else {
         return super.dispatchKeyEvent(var1);
      }
   }

   protected void displayDeviceName(final String var1) {
      this.runOnUiThread(new Runnable() {
         public void run() {
            FtcDriverStationActivity.this.textDeviceName.setText(var1);
         }
      });
   }

   protected void handleCommandInitOpModeResp(String var1) {
      DbgLog.msg("Robot Controller initializing op mode: " + var1);
      if(var1.equals("Stop Robot")) {
         if(this.queuedOpMode.equals("Stop Robot")) {
            this.uiWaitingForOpModeSelection();
         } else {
            this.uiWaitingForInitEvent();
            this.pendingCommands.add(new Command("CMD_RUN_OP_MODE", "Stop Robot"));
         }
      } else {
         this.uiWaitingForStartEvent();
      }
   }

   protected void handleCommandRequestOpModeListResp(String var1) {
      this.opModes = new LinkedHashSet();
      this.opModes.addAll(Arrays.asList(var1.split(Util.ASCII_RECORD_SEPARATOR)));
      DbgLog.msg("Received the following op modes: " + this.opModes.toString());
      this.pendingCommands.add(new Command("CMD_INIT_OP_MODE", "Stop Robot"));
      this.uiWaitingForOpModeSelection();
   }

   protected void handleCommandStartOpModeResp(String var1) {
      DbgLog.msg("Robot Controller starting op mode: " + var1);
      if(!var1.equals("Stop Robot")) {
         this.uiWaitingForStopEvent();
      }

      if(this.opModeUseTimer && !var1.equals("Stop Robot")) {
         this.opModeCountDown.start();
      }

   }

   protected void handleGamepadEvent(KeyEvent param1) {
      // $FF: Couldn't be decompiled
   }

   protected void handleGamepadEvent(MotionEvent param1) {
      // $FF: Couldn't be decompiled
   }

   protected void handleOpModeInit() {
      this.opModeCountDown.stop();
      this.uiWaitingForStartEvent();
      this.pendingCommands.add(new Command("CMD_INIT_OP_MODE", this.queuedOpMode));
      this.clearInfo();
   }

   protected void handleOpModeQueued(String var1) {
      this.queuedOpMode = var1;
      this.setButtonText(this.buttonSelect, var1);
   }

   protected void handleOpModeStart() {
      this.opModeCountDown.stop();
      this.uiWaitingForStopEvent();
      this.pendingCommands.add(new Command("CMD_RUN_OP_MODE", this.queuedOpMode));
      this.clearInfo();
   }

   protected void handleOpModeStop() {
      this.opModeCountDown.stop();
      if(!this.textTimer.getText().toString().isEmpty()) {
         this.opModeCountDown.setCountdown(Long.parseLong(this.textTimer.getText().toString()));
      }

      this.uiWaitingForInitEvent();
      this.pendingCommands.add(new Command("CMD_INIT_OP_MODE", "Stop Robot"));
   }

   protected void heartbeatEvent(RobocolDatagram var1) {
      try {
         this.heartbeatRecv.fromByteArray(var1.getData());
         double var3 = this.heartbeatRecv.getElapsedTime();
         short var5 = this.heartbeatRecv.getSequenceNumber();
         this.robotState = RobotState.fromByte(this.heartbeatRecv.getRobotState());
         this.pingAverage.addNumber((int)(1000.0D * var3));
         if(this.enableNetworkTrafficLogging) {
            Object[] var7 = new Object[]{Integer.valueOf(var5), Double.valueOf(var3)};
            DbgLog.msg(String.format("Network - num: %5d, time: %7.4f", var7));
         }

         if(this.lastUiUpdate.time() > 0.5D) {
            this.lastUiUpdate.reset();
            Object[] var6 = new Object[]{Integer.valueOf(this.pingAverage.getAverage())};
            this.pingStatus(String.format("%3dms", var6));
         }

      } catch (RobotCoreException var8) {
         DbgLog.logStacktrace(var8);
      }
   }

   protected void indicateGamepad(InputEvent var1) {
      Iterator var2 = this.userToGamepadMap.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         if(((Integer)var3.getValue()).intValue() == var1.getDeviceId()) {
            Animation var4 = AnimationUtils.loadAnimation(this.context, 2130968577);
            if(((Integer)var3.getKey()).intValue() == 1) {
               this.userIcon_1_active.setImageResource(2130837515);
               var4.setAnimationListener(this.animationListener_icon1);
               this.userIcon_1_active.startAnimation(var4);
            } else if(((Integer)var3.getKey()).intValue() == 2) {
               this.userIcon_2_active.setImageResource(2130837515);
               var4.setAnimationListener(this.animationListener_icon2);
               this.userIcon_2_active.startAnimation(var4);
            }
         }
      }

   }

   protected void initGamepad(int var1, int var2) {
      String var3 = "";
      switch(var1) {
      case 1:
         var3 = this.getString(2131361885);
         break;
      case 2:
         var3 = this.getString(2131361887);
      }

      String var4 = this.preferences.getString(var3, this.getString(2131361837));
      Object var5;
      if(var4.equals(this.getString(2131361838))) {
         var5 = new LogitechGamepadF310();
      } else if(var4.equals(this.getString(2131361839))) {
         var5 = new MicrosoftGamepadXbox360();
      } else {
         var5 = new Gamepad();
      }

      ((Gamepad)var5).id = var2;
      ((Gamepad)var5).timestamp = SystemClock.uptimeMillis();
      this.gamepads.put(Integer.valueOf(var2), var5);
   }

   public void onClickButtonInit(View var1) {
      this.handleOpModeInit();
   }

   public void onClickButtonSelect(View var1) {
      this.opModeCountDown.stop();
      String[] var2 = new String[this.opModes.size()];
      this.opModes.toArray(var2);
      OpModeSelectionDialogFragment var4 = new OpModeSelectionDialogFragment();
      var4.setOnSelectionDialogListener(this);
      var4.setOpModes(var2);
      var4.show(this.getFragmentManager(), "op_mode_selection");
      this.setTextView(this.systemTelemetry, "");
      this.setVisibility(this.systemTelemetry, 8);
      this.pendingCommands.add(new Command("CMD_INIT_OP_MODE", "Stop Robot"));
   }

   public void onClickButtonStart(View var1) {
      this.handleOpModeStart();
   }

   public void onClickButtonStartTimed(View var1) {
      if(this.opModeUseTimer) {
         this.setImageResource(this.buttonStartTimed, 2130837521);
         this.setTextView(this.textTimer, "");
      } else {
         this.setImageResource(this.buttonStartTimed, 2130837522);
         this.setTextView(this.textTimer, "30");
         this.opModeCountDown.setCountdown(30L);
      }

      boolean var2;
      if(!this.opModeUseTimer) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.opModeUseTimer = var2;
   }

   public void onClickButtonStop(View var1) {
      this.handleOpModeStop();
   }

   public void onClickDSBatteryToast(View var1) {
      this.showToast("Driver Station battery", 0);
   }

   public void onClickRCBatteryToast(View var1) {
      this.showToast("Robot Controller battery", 0);
   }

   public void onClickRobotBatteryToast(View var1) {
      this.showToast("Robot battery", 0);
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2130903044);
      this.context = this;
      this.utility = new Utility(this);
      this.textDeviceName = (TextView)this.findViewById(2131492886);
      this.textWifiDirectStatus = (TextView)this.findViewById(2131492898);
      this.textPingStatus = (TextView)this.findViewById(2131492901);
      this.textTelemetry = (TextView)this.findViewById(2131492917);
      this.systemTelemetry = (TextView)this.findViewById(2131492916);
      this.rcBatteryTelemetry = (TextView)this.findViewById(2131492904);
      this.rcBatteryIcon = (ImageView)this.findViewById(2131492903);
      this.dsBatteryInfo = (TextView)this.findViewById(2131492888);
      this.robotBatteryTelemetry = (TextView)this.findViewById(2131492906);
      this.dsBatteryIcon = (ImageView)this.findViewById(2131492887);
      this.immersion = new ImmersiveMode(this.getWindow().getDecorView());
      this.buttonInit = (ImageButton)this.findViewById(2131492911);
      this.buttonInitStop = (ImageButton)this.findViewById(2131492915);
      this.buttonStart = (ImageButton)this.findViewById(2131492910);
      this.controlPanelBack = this.findViewById(2131492908);
      this.batteryInfo = this.findViewById(2131492902);
      this.wifiInfo = this.findViewById(2131492894);
      this.userIcon_1_active = (ImageView)this.findViewById(2131492892);
      this.userIcon_2_active = (ImageView)this.findViewById(2131492890);
      this.userIcon_1_base = (ImageView)this.findViewById(2131492893);
      this.userIcon_2_base = (ImageView)this.findViewById(2131492891);
      this.buttonStartTimed = (ImageButton)this.findViewById(2131492913);
      this.textTimer = (TextView)this.findViewById(2131492914);
      this.buttonSelect = (Button)this.findViewById(2131492907);
      this.buttonStop = (ImageButton)this.findViewById(2131492912);
      this.buttonMenu = (ImageButton)this.findViewById(2131492889);
      this.buttonMenu.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            FtcDriverStationActivity.this.openOptionsMenu();
         }
      });
      PreferenceManager.setDefaultValues(this, 2131034112, false);
      this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
      this.preferences.registerOnSharedPreferenceChangeListener(this);
      this.wifiDirect = WifiDirectAssistant.getWifiDirectAssistant(this.getApplicationContext());
      this.wifiDirect.setCallback(this);
      this.analytics = new Analytics(this.context, "update_ds", new HardwareMap());
      this.batteryChecker = new BatteryChecker(this, this, (long)300000);
      this.batteryChecker.startBatteryMonitoring();
   }

   public boolean onCreateOptionsMenu(Menu var1) {
      this.getMenuInflater().inflate(2131427328, var1);
      return true;
   }

   public boolean onOptionsItemSelected(MenuItem var1) {
      switch(var1.getItemId()) {
      case 2131493090:
         this.startActivity(new Intent(this.getBaseContext(), SettingsActivity.class));
         return true;
      case 2131493091:
         this.pendingCommands.add(new Command("CMD_RESTART_ROBOT"));
         return true;
      case 2131493092:
         this.startActivity(new Intent("com.qualcomm.ftccommon.configuration.AboutActivity.intent.action.Launch"));
         return true;
      case 2131493093:
         this.finish();
         return true;
      default:
         return super.onOptionsItemSelected(var1);
      }
   }

   protected void onPause() {
      super.onPause();
      this.analytics.unregister();
   }

   protected void onResume() {
      super.onResume();
      this.analytics.register();
      this.setupNeeded = true;
      this.enableNetworkTrafficLogging = this.preferences.getBoolean(this.getString(2131361893), false);
      this.wifiDirect.setCallback(this);
      if(this.wifiDirect.isConnected()) {
         RobotLog.i("Spoofing a wifi direct event...");
         this.onWifiDirectEvent(WifiDirectAssistant.Event.CONNECTION_INFO_AVAILABLE);
      }

   }

   public void onSelectionClick(String var1) {
      this.handleOpModeQueued(var1);
      this.opModeCountDown.setCountdown(30L);
      if(this.opModeUseTimer) {
         this.setTextView(this.textTimer, String.valueOf(this.opModeCountDown.getTimeRemainingInSeconds()));
      } else {
         this.setTextView(this.textTimer, "");
      }

      this.uiWaitingForInitEvent();
   }

   public void onSharedPreferenceChanged(SharedPreferences var1, String var2) {
      if(var2.equals(this.context.getString(2131361885))) {
         this.gamepads.remove(this.userToGamepadMap.get(Integer.valueOf(1)));
         this.userToGamepadMap.remove(Integer.valueOf(1));
      } else {
         if(var2.equals(this.context.getString(2131361887))) {
            this.gamepads.remove(this.userToGamepadMap.get(Integer.valueOf(2)));
            this.userToGamepadMap.remove(Integer.valueOf(2));
            return;
         }

         if(var2.equals(this.context.getString(2131361893))) {
            this.enableNetworkTrafficLogging = this.preferences.getBoolean(this.getString(2131361893), false);
            return;
         }
      }

   }

   protected void onStart() {
      super.onStart();
      RobotLog.writeLogcatToDisk(this, 2048);
      this.wifiDirectStatus("Disconnected");
      this.groupOwnerMac = PreferenceManager.getDefaultSharedPreferences(this).getString(this.getString(2131361882), this.getString(2131361883));
      this.assumeClientDisconnect();
      this.wifiDirect.enable();
      if(!this.wifiDirect.isConnected()) {
         this.wifiDirect.discoverPeers();
      } else if(!this.groupOwnerMac.equalsIgnoreCase(this.wifiDirect.getGroupOwnerMacAddress())) {
         DbgLog.error("Wifi Direct - connected to " + this.wifiDirect.getGroupOwnerMacAddress() + ", expected " + this.groupOwnerMac);
         this.wifiDirectStatus("Error: Connected to wrong device");
         this.startActivity(new Intent(this.getBaseContext(), ConfigWifiDirectActivity.class));
         return;
      }

      this.setVisibility(this.userIcon_1_active, 4);
      this.setVisibility(this.userIcon_2_active, 4);
      this.setVisibility(this.userIcon_1_base, 4);
      this.setVisibility(this.userIcon_2_base, 4);
      DbgLog.msg("App Started");
   }

   protected void onStop() {
      super.onStop();
      this.wifiDirect.disable();
      this.shutdown();
      DbgLog.msg("App Stopped");
      RobotLog.cancelWriteLogcatToDisk(this);
   }

   public void onWifiDirectEvent(WifiDirectAssistant.Event param1) {
      // $FF: Couldn't be decompiled
   }

   public void onWindowFocusChanged(boolean var1) {
      super.onWindowFocusChanged(var1);
      if(var1) {
         if(ImmersiveMode.apiOver19()) {
            this.immersion.hideSystemUI();
         }

      } else {
         this.immersion.cancelSystemUIHide();
      }
   }

   protected void peerDiscoveryEvent(RobocolDatagram var1) {
      if(!var1.getAddress().equals(this.remoteAddr)) {
         this.remoteAddr = var1.getAddress();
         DbgLog.msg("new remote peer discovered: " + this.remoteAddr.getHostAddress());

         try {
            this.socket.connect(this.remoteAddr);
         } catch (SocketException var3) {
            DbgLog.error("Unable to connect to peer:" + var3.toString());
         }

         if(this.sendLoopFuture == null || this.sendLoopFuture.isDone()) {
            this.sendLoopFuture = this.sendLoopService.scheduleAtFixedRate(new FtcDriverStationActivity.SendLoopRunnable(null), 0L, 40L, TimeUnit.MILLISECONDS);
         }

         this.assumeClientConnect();
      }
   }

   protected void pingStatus(String var1) {
      this.setTextView(this.textPingStatus, var1);
   }

   protected void setButtonText(final Button var1, final String var2) {
      this.runOnUiThread(new Runnable() {
         public void run() {
            var1.setText(var2);
         }
      });
   }

   protected void setEnabled(final View var1, final boolean var2) {
      this.runOnUiThread(new Runnable() {
         public void run() {
            var1.setEnabled(var2);
         }
      });
   }

   protected void setImageResource(final ImageButton var1, final int var2) {
      this.runOnUiThread(new Runnable() {
         public void run() {
            var1.setImageResource(var2);
         }
      });
   }

   protected void setOpacity(final View var1, final float var2) {
      this.runOnUiThread(new Runnable() {
         public void run() {
            var1.setAlpha(var2);
         }
      });
   }

   protected void setTextView(final TextView var1, final String var2) {
      this.runOnUiThread(new Runnable() {
         public void run() {
            var1.setText(var2);
         }
      });
   }

   protected void setVisibility(final View var1, final int var2) {
      this.runOnUiThread(new Runnable() {
         public void run() {
            var1.setVisibility(var2);
         }
      });
   }

   public void showToast(final Toast var1) {
      this.runOnUiThread(new Runnable() {
         public void run() {
            var1.show();
         }
      });
   }

   public void showToast(String var1, int var2) {
      this.showToast(Toast.makeText(this.context, var1, var2));
   }

   protected void shutdown() {
      if(this.recvLoopService != null) {
         this.recvLoopService.shutdownNow();
      }

      if(this.sendLoopFuture != null && !this.sendLoopFuture.isDone()) {
         this.sendLoopFuture.cancel(true);
      }

      if(this.peerDiscoveryManager != null) {
         this.peerDiscoveryManager.stop();
      }

      if(this.socket != null) {
         this.socket.close();
      }

      this.remoteAddr = null;
      this.setupNeeded = true;
      this.pingStatus("");
   }

   protected void telemetryEvent(RobocolDatagram var1) {
      String var2 = "";

      Telemetry var3;
      try {
         var3 = new Telemetry(var1.getData());
      } catch (RobotCoreException var15) {
         DbgLog.logStacktrace(var15);
         return;
      }

      Map var4 = var3.getDataStrings();

      String var13;
      for(Iterator var5 = (new TreeSet(var4.keySet())).iterator(); var5.hasNext(); var2 = var2 + var13 + ": " + (String)var4.get(var13) + "\n") {
         var13 = (String)var5.next();
      }

      String var6 = var2 + "\n";
      Map var7 = var3.getDataNumbers();

      String var12;
      for(Iterator var8 = (new TreeSet(var7.keySet())).iterator(); var8.hasNext(); var6 = var6 + var12 + ": " + var7.get(var12) + "\n") {
         var12 = (String)var8.next();
      }

      String var9 = var3.getTag();
      if(var9.equals("SYSTEM_TELEMETRY")) {
         DbgLog.error("System Telemetry event: " + var6);
         RobotLog.setGlobalErrorMsg(var6);
         this.setVisibility(this.systemTelemetry, 0);
         this.setTextView(this.systemTelemetry, "Robot Status: " + this.robotState + "\n" + "To recover, please restart the robot." + "\n" + "Error: " + (String)var4.get(var9));
         this.uiRobotNeedsRestart();
      } else if(var9.equals("RobotController Battery Level")) {
         DbgLog.msg("RC battery Telemetry event: " + (String)var4.get(var9));
         this.setTextView(this.rcBatteryTelemetry, (String)var4.get(var9) + "%");
         this.setBatteryIcon(Float.parseFloat((String)var4.get(var9)), this.rcBatteryIcon);
      } else if(var9.equals("Robot Battery Level")) {
         String var10 = (String)var4.get(var9);
         DbgLog.msg("Robot Battery Telemetry event: " + var10);
         this.setTextView(this.robotBatteryTelemetry, var10);
         ImageView var11 = (ImageView)this.findViewById(2131492905);
         if(var10.equals("no voltage sensor found")) {
            this.setVisibility(var11, 8);
         } else {
            this.setVisibility(var11, 0);
         }
      } else {
         this.setTextView(this.textTelemetry, var6);
      }
   }

   protected void uiRobotControllerIsConnected() {
      this.setOpacity(this.wifiInfo, 1.0F);
      this.setOpacity(this.batteryInfo, 1.0F);
      this.setOpacity(this.buttonSelect, 1.0F);
      this.setTextView(this.textTelemetry, "");
      this.setTextView(this.systemTelemetry, "");
      this.setVisibility(this.systemTelemetry, 8);
      this.setTextView(this.rcBatteryTelemetry, "");
      this.setTextView(this.robotBatteryTelemetry, "");
   }

   protected void uiRobotControllerIsDisconnected() {
      this.setOpacity(this.controlPanelBack, 0.3F);
      this.setOpacity(this.wifiInfo, 0.3F);
      this.setOpacity(this.batteryInfo, 0.3F);
      this.setEnabled(this.buttonSelect, false);
      this.setOpacity(this.buttonSelect, 0.3F);
      this.setEnabled(this.buttonInit, false);
      this.setVisibility(this.buttonInit, 0);
      this.setVisibility(this.buttonStart, 4);
      this.setVisibility(this.buttonStop, 4);
      this.setVisibility(this.buttonInitStop, 4);
      this.setVisibility(this.buttonStartTimed, 4);
      this.setVisibility(this.textTimer, 4);
   }

   protected void uiRobotNeedsRestart() {
      this.setOpacity(this.buttonSelect, 0.3F);
      this.setEnabled(this.buttonSelect, false);
      this.setEnabled(this.buttonInit, false);
      this.setVisibility(this.buttonInit, 0);
      this.setVisibility(this.buttonStart, 4);
      this.setVisibility(this.buttonStop, 4);
      this.setVisibility(this.buttonInitStop, 4);
      this.setVisibility(this.buttonStartTimed, 4);
      this.setVisibility(this.textTimer, 4);
   }

   protected void uiWaitingForInitEvent() {
      this.setOpacity(this.controlPanelBack, 1.0F);
      this.setButtonText(this.buttonSelect, this.queuedOpMode);
      this.setEnabled(this.buttonSelect, true);
      this.setOpacity(this.buttonSelect, 1.0F);
      this.setEnabled(this.buttonInit, true);
      this.setVisibility(this.buttonInit, 0);
      this.setVisibility(this.buttonStart, 4);
      this.setVisibility(this.buttonStop, 4);
      this.setVisibility(this.buttonInitStop, 4);
      this.setEnabled(this.buttonStartTimed, true);
      this.setVisibility(this.buttonStartTimed, 0);
      this.setVisibility(this.textTimer, 0);
   }

   protected void uiWaitingForOpModeSelection() {
      this.setEnabled(this.buttonSelect, true);
      this.setOpacity(this.buttonSelect, 1.0F);
      this.setButtonText(this.buttonSelect, "Select Op Mode");
      this.setOpacity(this.controlPanelBack, 0.3F);
      this.setEnabled(this.buttonInit, false);
      this.setVisibility(this.buttonInit, 0);
      this.setVisibility(this.buttonStart, 4);
      this.setVisibility(this.buttonStop, 4);
      this.setVisibility(this.buttonInitStop, 4);
      this.setVisibility(this.buttonStartTimed, 4);
      this.setVisibility(this.textTimer, 4);
   }

   protected void uiWaitingForStartEvent() {
      this.setButtonText(this.buttonSelect, this.queuedOpMode);
      this.setEnabled(this.buttonSelect, true);
      this.setOpacity(this.buttonSelect, 1.0F);
      this.setVisibility(this.buttonStart, 0);
      this.setVisibility(this.buttonInit, 4);
      this.setVisibility(this.buttonStop, 4);
      this.setVisibility(this.buttonInitStop, 0);
      this.setEnabled(this.buttonStartTimed, true);
      this.setVisibility(this.buttonStartTimed, 0);
      this.setVisibility(this.textTimer, 0);
   }

   protected void uiWaitingForStopEvent() {
      this.setButtonText(this.buttonSelect, this.queuedOpMode);
      this.setEnabled(this.buttonSelect, true);
      this.setOpacity(this.buttonSelect, 1.0F);
      this.setVisibility(this.buttonStop, 0);
      this.setVisibility(this.buttonInit, 4);
      this.setVisibility(this.buttonStart, 4);
      this.setVisibility(this.buttonInitStop, 4);
      this.setEnabled(this.buttonStartTimed, false);
      this.setVisibility(this.buttonStartTimed, 0);
      this.setVisibility(this.textTimer, 0);
   }

   public void updateBatteryLevel(float var1) {
      this.setTextView(this.dsBatteryInfo, var1 + "%");
      this.setBatteryIcon(var1, this.dsBatteryIcon);
   }

   protected void wifiDirectStatus(final String var1) {
      this.runOnUiThread(new Runnable() {
         public void run() {
            FtcDriverStationActivity.this.textWifiDirectStatus.setText(var1);
         }
      });
   }

   private class OpModeCountDownTimer {
      public static final long COUNTDOWN_INTERVAL = 30L;
      public static final long MILLISECONDS_PER_SECOND = 1000L;
      public static final long TICK = 1000L;
      public static final long TICK_INTERVAL = 1L;
      private long countdown;
      boolean running;
      CountDownTimer timer;

      private OpModeCountDownTimer() {
         this.countdown = 30000L;
         this.timer = null;
         this.running = false;
      }

      // $FF: synthetic method
      OpModeCountDownTimer(Object var2) {
         this();
      }

      public long getTimeRemainingInSeconds() {
         return this.countdown / 1000L;
      }

      public boolean isRunning() {
         return this.running;
      }

      public void setCountdown(long var1) {
         this.countdown = 1000L * var1;
      }

      public void start() {
         DbgLog.msg("Running current op mode for " + this.getTimeRemainingInSeconds() + " seconds");
         this.running = true;
         FtcDriverStationActivity.this.runOnUiThread(new Runnable() {
            public void run() {
               OpModeCountDownTimer.this.timer = (new CountDownTimer(OpModeCountDownTimer.this.countdown, var4) {
                  public void onFinish() {
                     OpModeCountDownTimer.this.running = false;
                     DbgLog.msg("Stopping current op mode, timer expired");
                     OpModeCountDownTimer.this.setCountdown(30L);
                     FtcDriverStationActivity.this.setTextView(FtcDriverStationActivity.this.textTimer, "");
                     FtcDriverStationActivity.this.setImageResource(FtcDriverStationActivity.this.buttonStartTimed, 2130837521);
                     FtcDriverStationActivity.this.handleOpModeStop();
                  }

                  public void onTick(long var1) {
                     long var3 = var1 / 1000L;
                     String var5 = String.valueOf(var3);
                     FtcDriverStationActivity.this.setTextView(FtcDriverStationActivity.this.textTimer, var5);
                     DbgLog.msg("Running current op mode for " + var3 + " seconds");
                  }
               }).start();
            }
         });
      }

      public void stop() {
         if(this.running) {
            this.running = false;
            DbgLog.msg("Stopping current op mode timer");
            if(this.timer != null) {
               this.timer.cancel();
               return;
            }
         }

      }
   }

   private class RecvLoopRunnable implements Runnable {
      private RecvLoopRunnable() {
      }

      // $FF: synthetic method
      RecvLoopRunnable(Object var2) {
         this();
      }

      public void run() {
         while(true) {
            RobocolDatagram var1 = FtcDriverStationActivity.this.socket.recv();
            if(var1 == null) {
               if(FtcDriverStationActivity.this.socket.isClosed()) {
                  return;
               }

               Thread.yield();
            } else {
               FtcDriverStationActivity.this.lastRecvPacket.reset();
               switch(null.$SwitchMap$com$qualcomm$robotcore$robocol$RobocolParsable$MsgType[var1.getMsgType().ordinal()]) {
               case 1:
                  FtcDriverStationActivity.this.peerDiscoveryEvent(var1);
                  break;
               case 2:
                  FtcDriverStationActivity.this.heartbeatEvent(var1);
                  break;
               case 3:
                  FtcDriverStationActivity.this.commandEvent(var1);
                  break;
               case 4:
                  FtcDriverStationActivity.this.telemetryEvent(var1);
                  break;
               default:
                  DbgLog.msg("Unhandled message type: " + var1.getMsgType().name());
               }
            }
         }
      }
   }

   private class SendLoopRunnable implements Runnable {
      private static final long GAMEPAD_UPDATE_THRESHOLD = 1000L;

      private SendLoopRunnable() {
      }

      // $FF: synthetic method
      SendLoopRunnable(Object var2) {
         this();
      }

      public void run() {
         // $FF: Couldn't be decompiled
      }
   }

   private class SetupRunnable implements Runnable {
      private SetupRunnable() {
      }

      // $FF: synthetic method
      SetupRunnable(Object var2) {
         this();
      }

      public void run() {
         try {
            if(FtcDriverStationActivity.this.socket != null) {
               FtcDriverStationActivity.this.socket.close();
            }

            FtcDriverStationActivity.this.socket = new RobocolDatagramSocket();
            FtcDriverStationActivity.this.socket.listen(FtcDriverStationActivity.this.wifiDirect.getGroupOwnerAddress());
            FtcDriverStationActivity.this.socket.connect(FtcDriverStationActivity.this.wifiDirect.getGroupOwnerAddress());
         } catch (SocketException var2) {
            DbgLog.error("Failed to open socket: " + var2.toString());
         }

         if(FtcDriverStationActivity.this.peerDiscoveryManager != null) {
            FtcDriverStationActivity.this.peerDiscoveryManager.stop();
         }

         FtcDriverStationActivity.this.peerDiscoveryManager = new PeerDiscoveryManager(FtcDriverStationActivity.this.socket);
         FtcDriverStationActivity.this.peerDiscoveryManager.start(FtcDriverStationActivity.this.wifiDirect.getGroupOwnerAddress());
         FtcDriverStationActivity.this.recvLoopService = Executors.newSingleThreadExecutor();
         FtcDriverStationActivity.this.recvLoopService.execute(FtcDriverStationActivity.this.new RecvLoopRunnable(null));
         DbgLog.msg("Setup complete");
      }
   }
}
