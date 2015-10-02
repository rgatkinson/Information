package com.qualcomm.ftcrobotcontroller;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.OnMenuVisibilityListener;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftccommon.FtcEventLoop;
import com.qualcomm.ftccommon.FtcRobotControllerService;
import com.qualcomm.ftccommon.Restarter;
import com.qualcomm.ftccommon.UpdateUI;
import com.qualcomm.ftcrobotcontroller.opmodes.FtcOpModeRegister;
import com.qualcomm.hardware.ModernRoboticsHardwareFactory;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.util.Dimmer;
import com.qualcomm.robotcore.util.ImmersiveMode;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;

public class FtcRobotControllerActivity extends Activity {
   public static final String CONFIGURE_FILENAME = "CONFIGURE_FILENAME";
   private static final int NUM_GAMEPADS = 2;
   private static final int REQUEST_CONFIG_WIFI_CHANNEL = 1;
   private static final boolean USE_DEVICE_EMULATION = false;
   protected ImageButton buttonMenu;
   protected UpdateUI.Callback callback;
   protected ServiceConnection connection = new ServiceConnection() {
      public void onServiceConnected(ComponentName var1, IBinder var2) {
         FtcRobotControllerService.FtcRobotControllerBinder var3 = (FtcRobotControllerService.FtcRobotControllerBinder)var2;
         FtcRobotControllerActivity.this.onServiceBind(var3.getService());
      }

      public void onServiceDisconnected(ComponentName var1) {
         FtcRobotControllerActivity.this.controllerService = null;
      }
   };
   protected Context context;
   protected FtcRobotControllerService controllerService;
   protected Dimmer dimmer;
   protected LinearLayout entireScreenLayout;
   protected FtcEventLoop eventLoop;
   protected ImmersiveMode immersion;
   protected SharedPreferences preferences;
   protected TextView textDeviceName;
   protected TextView textErrorMessage;
   protected TextView[] textGamepad = new TextView[2];
   protected TextView textOpMode;
   protected TextView textRobotStatus;
   protected TextView textWifiDirectStatus;
   protected UpdateUI updateUI;
   private Utility utility;

   private FileInputStream fileSetup() {
      String var1 = Utility.CONFIG_FILES_DIR + this.utility.getFilenameFromPrefs(R.string.pref_hardware_config_filename, "No current file!") + ".xml";

      FileInputStream fis;
      try {
         fis = new FileInputStream(var1);
      } catch (FileNotFoundException var5) {
         String msg = "Cannot open robot configuration file - " + var1;
         this.utility.complainToast(msg, this.context);
         DbgLog.msg(msg);
         this.utility.saveToPreferences("No current file!",R.string.pref_hardware_config_filename);
         fis = null;
      }

      this.utility.updateHeader("No current file!", R.string.pref_hardware_config_filename, 2131427434, 2131427329);
      return fis;
   }

   private void requestRobotRestart() {
      this.requestRobotShutdown();
      this.requestRobotSetup();
   }

   private void requestRobotSetup() {
      if(this.controllerService != null) {
         FileInputStream var1 = this.fileSetup();
         if(var1 != null) {
            ModernRoboticsHardwareFactory hardwareFactory = new ModernRoboticsHardwareFactory(this.context);
            hardwareFactory.setXmlInputStream(var1);
            this.eventLoop = new FtcEventLoop(hardwareFactory, new FtcOpModeRegister(), this.callback, this);
            this.controllerService.setCallback(this.callback);
            this.controllerService.setupRobot(this.eventLoop);
            return;
         }
      }

   }

   private void requestRobotShutdown() {
      if(this.controllerService != null) {
         this.controllerService.shutdownRobot();
      }
   }

   protected void hittingMenuButtonBrightensScreen() {
      ActionBar var1 = this.getActionBar();
      if(var1 != null) {
         var1.addOnMenuVisibilityListener(new OnMenuVisibilityListener() {
            public void onMenuVisibilityChanged(boolean var1) {
               if(var1) {
                  FtcRobotControllerActivity.this.dimmer.handleDimTimer();
               }

            }
         });
      }

   }

   protected void onActivityResult(int var1, int var2, Intent var3) {
      if(var1 == 1 && var2 == -1) {
         Toast var5 = Toast.makeText(this.context, "Configuration Complete", 1);
         var5.setGravity(17, 0, 0);
         this.showToast(var5);
      }

      if(var1 == 3 && var2 == -1) {
         Serializable var4 = var3.getSerializableExtra("CONFIGURE_FILENAME");
         if(var4 != null) {
            this.utility.saveToPreferences(var4.toString(), R.string.pref_hardware_config_filename);
            this.utility.updateHeader("No current file!", R.string.pref_hardware_config_filename, 2131427434, 2131427329);
         }
      }

   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(2130903044);
      this.utility = new Utility(this);
      this.context = this;
      this.entireScreenLayout = (LinearLayout)this.findViewById(2131427348);
      this.buttonMenu = (ImageButton)this.findViewById(2131427352);
      this.buttonMenu.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            FtcRobotControllerActivity.this.openOptionsMenu();
         }
      });
      this.textDeviceName = (TextView)this.findViewById(2131427351);
      this.textWifiDirectStatus = (TextView)this.findViewById(2131427354);
      this.textRobotStatus = (TextView)this.findViewById(2131427355);
      this.textOpMode = (TextView)this.findViewById(2131427356);
      this.textErrorMessage = (TextView)this.findViewById(2131427357);
      this.textGamepad[0] = (TextView)this.findViewById(2131427358);
      this.textGamepad[1] = (TextView)this.findViewById(2131427359);
      this.immersion = new ImmersiveMode(this.getWindow().getDecorView());
      this.dimmer = new Dimmer(this);
      this.dimmer.longBright();
      FtcRobotControllerActivity.RobotRestarter var2 = new FtcRobotControllerActivity.RobotRestarter();
      this.updateUI = new UpdateUI(this, this.dimmer);
      this.updateUI.setRestarter(var2);
      this.updateUI.setTextViews(this.textWifiDirectStatus, this.textRobotStatus, this.textGamepad, this.textOpMode, this.textErrorMessage, this.textDeviceName);
      UpdateUI var3 = this.updateUI;
      this.callback = var3.new Callback();
      PreferenceManager.setDefaultValues(this, 2130968577, false);
      this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
      this.hittingMenuButtonBrightensScreen();
   }

   public boolean onCreateOptionsMenu(Menu var1) {
      this.getMenuInflater().inflate(2131361792, var1);
      return true;
   }

   protected void onNewIntent(Intent var1) {
      super.onNewIntent(var1);
      if("android.hardware.usb.action.USB_ACCESSORY_ATTACHED".equals(var1.getAction())) {
         DbgLog.msg("USB Device attached; app restart may be needed");
      }

   }

   public boolean onOptionsItemSelected(MenuItem var1) {
      switch(var1.getItemId()) {
      case 2131427528:
         this.startActivityForResult(new Intent("com.qualcomm.ftccommon.FtcRobotControllerSettingsActivity.intent.action.Launch"), 3);
         return true;
      case 2131427529:
         this.dimmer.handleDimTimer();
         Toast.makeText(this.context, "Restarting Robot", 0).show();
         this.requestRobotRestart();
         return true;
      case 2131427530:
         Intent var2 = new Intent("com.qualcomm.ftccommon.ViewLogsActivity.intent.action.Launch");
         var2.putExtra("Filename", RobotLog.getLogFilename(this));
         this.startActivity(var2);
         return true;
      case 2131427531:
         this.startActivity(new Intent("com.qualcomm.ftccommon.configuration.AboutActivity.intent.action.Launch"));
         return true;
      case 2131427532:
         this.finish();
         return true;
      default:
         return super.onOptionsItemSelected(var1);
      }
   }

   public void onPause() {
      super.onPause();
   }

   protected void onResume() {
      super.onResume();
   }

   public void onServiceBind(FtcRobotControllerService var1) {
      DbgLog.msg("Bound to Ftc Controller Service");
      this.controllerService = var1;
      this.updateUI.setControllerService(this.controllerService);
      this.callback.wifiDirectUpdate(this.controllerService.getWifiDirectStatus());
      this.callback.robotUpdate(this.controllerService.getRobotStatus());
      this.requestRobotSetup();
   }

   protected void onStart() {
      super.onStart();
      RobotLog.writeLogcatToDisk(this, 4096);
      this.bindService(new Intent(this, FtcRobotControllerService.class), this.connection, 1);
      this.utility.updateHeader("No current file!", R.string.pref_hardware_config_filename, 2131427434, 2131427329);
      this.callback.wifiDirectUpdate(WifiDirectAssistant.Event.DISCONNECTED);
      this.entireScreenLayout.setOnTouchListener(new OnTouchListener() {
         public boolean onTouch(View var1, MotionEvent var2) {
            FtcRobotControllerActivity.this.dimmer.handleDimTimer();
            return false;
         }
      });
   }

   protected void onStop() {
      super.onStop();
      if(this.controllerService != null) {
         this.unbindService(this.connection);
      }

      RobotLog.cancelWriteLogcatToDisk(this);
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

   public void showToast(final Toast var1) {
      this.runOnUiThread(new Runnable() {
         public void run() {
            var1.show();
         }
      });
   }

   protected class RobotRestarter implements Restarter {
      public void requestRestart() {
         FtcRobotControllerActivity.this.requestRobotRestart();
      }
   }
}
