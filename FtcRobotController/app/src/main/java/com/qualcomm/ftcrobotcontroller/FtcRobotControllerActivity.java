package com.qualcomm.ftcrobotcontroller;

import android.view.MotionEvent;
import android.view.View$OnTouchListener;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
import com.qualcomm.robotcore.util.RobotLog;
import android.view.MenuItem;
import android.view.Menu;
import android.preference.PreferenceManager;
import com.qualcomm.ftccommon.Restarter;
import android.view.View;
import android.view.View$OnClickListener;
import android.os.Bundle;
import android.content.res.Configuration;
import java.io.Serializable;
import android.widget.Toast;
import android.content.Intent;
import android.app.ActionBar;
import android.app.ActionBar$OnMenuVisibilityListener;
import com.qualcomm.robotcore.eventloop.EventLoop;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
import com.qualcomm.robotcore.hardware.HardwareFactory;
import com.qualcomm.ftcrobotcontroller.opmodes.FtcOpModeRegister;
import java.io.InputStream;
import com.qualcomm.hardware.ModernRoboticsHardwareFactory;
import java.io.FileNotFoundException;
import com.qualcomm.ftccommon.DbgLog;
import java.io.FileInputStream;
import android.os.IBinder;
import android.content.ComponentName;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import android.widget.TextView;
import android.content.SharedPreferences;
import com.qualcomm.robotcore.util.ImmersiveMode;
import com.qualcomm.ftccommon.FtcEventLoop;
import android.widget.LinearLayout;
import com.qualcomm.robotcore.util.Dimmer;
import com.qualcomm.ftccommon.FtcRobotControllerService;
import android.content.Context;
import android.content.ServiceConnection;
import com.qualcomm.ftccommon.UpdateUI;
import android.widget.ImageButton;
import android.app.Activity;

public class FtcRobotControllerActivity extends Activity
{
    public static final String CONFIGURE_FILENAME = "CONFIGURE_FILENAME";
    private static final int NUM_GAMEPADS = 2;
    private static final int REQUEST_CONFIG_WIFI_CHANNEL = 1;
    private static final boolean USE_DEVICE_EMULATION;
    protected ImageButton buttonMenu;
    protected UpdateUI.Callback callback;
    protected ServiceConnection connection;
    protected Context context;
    protected FtcRobotControllerService controllerService;
    protected Dimmer dimmer;
    protected LinearLayout entireScreenLayout;
    protected FtcEventLoop eventLoop;
    protected ImmersiveMode immersion;
    protected SharedPreferences preferences;
    protected TextView textDeviceName;
    protected TextView textErrorMessage;
    protected TextView[] textGamepad;
    protected TextView textOpMode;
    protected TextView textRobotStatus;
    protected TextView textWifiDirectStatus;
    protected UpdateUI updateUI;
    private Utility utility;
    
    public FtcRobotControllerActivity() {
        this.textGamepad = new TextView[2];
        this.connection = (ServiceConnection)new ServiceConnection() {
            public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
                FtcRobotControllerActivity.this.onServiceBind(((FtcRobotControllerService.FtcRobotControllerBinder)binder).getService());
            }
            
            public void onServiceDisconnected(final ComponentName componentName) {
                FtcRobotControllerActivity.this.controllerService = null;
            }
        };
    }
    
    private FileInputStream fileSetup() {
        final String string = Utility.CONFIG_FILES_DIR + this.utility.getFilenameFromPrefs(2131296333, "No current file!") + ".xml";
        while (true) {
            try {
                final FileInputStream fileInputStream = new FileInputStream(string);
                this.utility.updateHeader("No current file!", 2131296333, 2131427434, 2131427329);
                return fileInputStream;
            }
            catch (FileNotFoundException ex) {
                final String string2 = "Cannot open robot configuration file - " + string;
                this.utility.complainToast(string2, this.context);
                DbgLog.msg(string2);
                this.utility.saveToPreferences("No current file!", 2131296333);
                final FileInputStream fileInputStream = null;
                continue;
            }
            break;
        }
    }
    
    private void requestRobotRestart() {
        this.requestRobotShutdown();
        this.requestRobotSetup();
    }
    
    private void requestRobotSetup() {
        if (this.controllerService != null) {
            final FileInputStream fileSetup = this.fileSetup();
            if (fileSetup != null) {
                final ModernRoboticsHardwareFactory modernRoboticsHardwareFactory = new ModernRoboticsHardwareFactory(this.context);
                modernRoboticsHardwareFactory.setXmlInputStream(fileSetup);
                this.eventLoop = new FtcEventLoop(modernRoboticsHardwareFactory, new FtcOpModeRegister(), this.callback, (Context)this);
                this.controllerService.setCallback(this.callback);
                this.controllerService.setupRobot(this.eventLoop);
            }
        }
    }
    
    private void requestRobotShutdown() {
        if (this.controllerService == null) {
            return;
        }
        this.controllerService.shutdownRobot();
    }
    
    protected void hittingMenuButtonBrightensScreen() {
        final ActionBar actionBar = this.getActionBar();
        if (actionBar != null) {
            actionBar.addOnMenuVisibilityListener((ActionBar$OnMenuVisibilityListener)new ActionBar$OnMenuVisibilityListener() {
                public void onMenuVisibilityChanged(final boolean b) {
                    if (b) {
                        FtcRobotControllerActivity.this.dimmer.handleDimTimer();
                    }
                }
            });
        }
    }
    
    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        if (n == 1 && n2 == -1) {
            final Toast text = Toast.makeText(this.context, (CharSequence)"Configuration Complete", 1);
            text.setGravity(17, 0, 0);
            this.showToast(text);
        }
        if (n == 3 && n2 == -1) {
            final Serializable serializableExtra = intent.getSerializableExtra("CONFIGURE_FILENAME");
            if (serializableExtra != null) {
                this.utility.saveToPreferences(serializableExtra.toString(), 2131296333);
                this.utility.updateHeader("No current file!", 2131296333, 2131427434, 2131427329);
            }
        }
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130903044);
        this.utility = new Utility(this);
        this.context = (Context)this;
        this.entireScreenLayout = (LinearLayout)this.findViewById(2131427348);
        (this.buttonMenu = (ImageButton)this.findViewById(2131427352)).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
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
        (this.dimmer = new Dimmer(this)).longBright();
        (this.updateUI = new UpdateUI(this, this.dimmer)).setRestarter(new RobotRestarter());
        this.updateUI.setTextViews(this.textWifiDirectStatus, this.textRobotStatus, this.textGamepad, this.textOpMode, this.textErrorMessage, this.textDeviceName);
        final UpdateUI updateUI = this.updateUI;
        updateUI.getClass();
        this.callback = updateUI.new Callback();
        PreferenceManager.setDefaultValues((Context)this, 2130968577, false);
        this.preferences = PreferenceManager.getDefaultSharedPreferences((Context)this);
        this.hittingMenuButtonBrightensScreen();
    }
    
    public boolean onCreateOptionsMenu(final Menu menu) {
        this.getMenuInflater().inflate(2131361792, menu);
        return true;
    }
    
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        if ("android.hardware.usb.action.USB_ACCESSORY_ATTACHED".equals(intent.getAction())) {
            DbgLog.msg("USB Device attached; app restart may be needed");
        }
    }
    
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            default: {
                return super.onOptionsItemSelected(menuItem);
            }
            case 2131427529: {
                this.dimmer.handleDimTimer();
                Toast.makeText(this.context, (CharSequence)"Restarting Robot", 0).show();
                this.requestRobotRestart();
                return true;
            }
            case 2131427528: {
                this.startActivityForResult(new Intent("com.qualcomm.ftccommon.FtcRobotControllerSettingsActivity.intent.action.Launch"), 3);
                return true;
            }
            case 2131427531: {
                this.startActivity(new Intent("com.qualcomm.ftccommon.configuration.AboutActivity.intent.action.Launch"));
                return true;
            }
            case 2131427532: {
                this.finish();
                return true;
            }
            case 2131427530: {
                final Intent intent = new Intent("com.qualcomm.ftccommon.ViewLogsActivity.intent.action.Launch");
                intent.putExtra("Filename", RobotLog.getLogFilename((Context)this));
                this.startActivity(intent);
                return true;
            }
        }
    }
    
    public void onPause() {
        super.onPause();
    }
    
    protected void onResume() {
        super.onResume();
    }
    
    public void onServiceBind(final FtcRobotControllerService controllerService) {
        DbgLog.msg("Bound to Ftc Controller Service");
        this.controllerService = controllerService;
        this.updateUI.setControllerService(this.controllerService);
        this.callback.wifiDirectUpdate(this.controllerService.getWifiDirectStatus());
        this.callback.robotUpdate(this.controllerService.getRobotStatus());
        this.requestRobotSetup();
    }
    
    protected void onStart() {
        super.onStart();
        RobotLog.writeLogcatToDisk((Context)this, 4096);
        this.bindService(new Intent((Context)this, (Class)FtcRobotControllerService.class), this.connection, 1);
        this.utility.updateHeader("No current file!", 2131296333, 2131427434, 2131427329);
        this.callback.wifiDirectUpdate(WifiDirectAssistant.Event.DISCONNECTED);
        this.entireScreenLayout.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                FtcRobotControllerActivity.this.dimmer.handleDimTimer();
                return false;
            }
        });
    }
    
    protected void onStop() {
        super.onStop();
        if (this.controllerService != null) {
            this.unbindService(this.connection);
        }
        RobotLog.cancelWriteLogcatToDisk((Context)this);
    }
    
    public void onWindowFocusChanged(final boolean b) {
        super.onWindowFocusChanged(b);
        if (b) {
            if (ImmersiveMode.apiOver19()) {
                this.immersion.hideSystemUI();
            }
            return;
        }
        this.immersion.cancelSystemUIHide();
    }
    
    public void showToast(final Toast toast) {
        this.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                toast.show();
            }
        });
    }
    
    protected class RobotRestarter implements Restarter
    {
        @Override
        public void requestRestart() {
            FtcRobotControllerActivity.this.requestRobotRestart();
        }
    }
}
