package com.qualcomm.ftccommon.configuration;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftccommon.R;
import com.qualcomm.hardware.ModernRoboticsDeviceManager;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceInfoAdapter;
import com.qualcomm.robotcore.hardware.configuration.ReadXMLFileHandler;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class FtcConfigurationActivity extends Activity {
   OnClickListener a = new OnClickListener() {
      public void onClick(DialogInterface var1, int var2) {
      }
   };
   OnClickListener b = new OnClickListener() {
      public void onClick(DialogInterface var1, int var2) {
         FtcConfigurationActivity.this.utility.saveToPreferences(FtcConfigurationActivity.this.i.substring(7).trim(), R.string.pref_hardware_config_filename);
         FtcConfigurationActivity.this.finish();
      }
   };
   OnClickListener c = new OnClickListener() {
      public void onClick(DialogInterface var1, int var2) {
      }
   };
   private Thread thread;
   private Map<SerialNumber, ControllerConfiguration> map = new HashMap();
   private Context context;
   private DeviceManager g;
   private Button button;
   private String i = "No current file!";
   private Utility utility;
   protected SharedPreferences preferences;
   protected Map<SerialNumber, DeviceManager.DeviceType> scannedDevices = new HashMap();
   protected Set<Entry<SerialNumber, DeviceManager.DeviceType>> scannedEntries = new HashSet();

   private void a() {
      ((Button)this.findViewById(R.id.devices_holder).findViewById(R.id.info_btn)).setOnClickListener(new android.view.View.OnClickListener() {
         public void onClick(View var1) {
            Builder var2 = FtcConfigurationActivity.this.utility.buildBuilder("Devices", "These are the devices discovered by the Hardware Wizard. You can click on the name of each device to edit the information relating to that device. Make sure each device has a unique name. The names should match what is in the Op mode code. Scroll down to see more devices if there are any.");
            var2.setPositiveButton("Ok", FtcConfigurationActivity.this.a);
            AlertDialog var4 = var2.create();
            var4.show();
            ((TextView)var4.findViewById(16908299)).setTextSize(14.0F);
         }
      });
      ((Button)this.findViewById(R.id.save_holder).findViewById(R.id.info_btn)).setOnClickListener(new android.view.View.OnClickListener() {
         public void onClick(View var1) {
            Builder var2 = FtcConfigurationActivity.this.utility.buildBuilder("Save Configuration", "Clicking the save button will create an xml file in: \n      /sdcard/FIRST/  \nThis file will be used to initialize the robot.");
            var2.setPositiveButton("Ok", FtcConfigurationActivity.this.a);
            AlertDialog var4 = var2.create();
            var4.show();
            ((TextView)var4.findViewById(16908299)).setTextSize(14.0F);
         }
      });
   }

   private void a(String var1) {
      String var2 = "Found " + var1;
      this.utility.setOrangeText(var2, "Please fix and re-save.", R.id.warning_layout, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
   }

   private void a(ArrayList<ControllerConfiguration> var1) {
      this.map = new HashMap();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ControllerConfiguration var3 = (ControllerConfiguration)var2.next();
         this.map.put(var3.getSerialNumber(), var3);
      }

   }

   private HashMap<SerialNumber, ControllerConfiguration> b() {
      HashMap var1 = new HashMap();
      Iterator var2 = this.scannedEntries.iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         SerialNumber var4 = (SerialNumber)var3.getKey();
         if(this.map.containsKey(var4)) {
            var1.put(var4, this.map.get(var4));
         } else {
            switch(((DeviceManager.DeviceType)var3.getValue()).ordinal()) {
            case 1:
               var1.put(var4, this.utility.buildMotorController(var4));
               break;
            case 2:
               var1.put(var4, this.utility.buildServoController(var4));
               break;
            case 3:
               var1.put(var4, this.utility.buildLegacyModule(var4));
               break;
            case 4:
               var1.put(var4, this.utility.buildDeviceInterfaceModule(var4));
            }
         }
      }

      return var1;
   }

   private void c() {
      if(this.i.toLowerCase().contains("Unsaved".toLowerCase())) {
         EditText var1 = new EditText(this.context);
         var1.setEnabled(false);
         var1.setText("");
         Builder var2 = this.utility.buildBuilder("Unsaved changes", "If you scan, your current unsaved changes will be lost.");
         var2.setView(var1);
         var2.setPositiveButton("Ok", new OnClickListener() {
            public void onClick(DialogInterface var1, int var2) {
               FtcConfigurationActivity.this.thread.start();
            }
         });
         var2.setNegativeButton("Cancel", this.c);
         var2.show();
      } else {
         this.thread.start();
      }
   }

   private void d() {
      ReadXMLFileHandler var1 = new ReadXMLFileHandler(this.context);
      if(!this.i.equalsIgnoreCase("No current file!")) {
         try {
            this.a((ArrayList)var1.parse(new FileInputStream(Utility.CONFIG_FILES_DIR + this.i + ".xml")));
            this.h();
            this.f();
         } catch (RobotCoreException var4) {
            RobotLog.log("Error parsing XML file");
            RobotLog.logStacktrace(var4);
            this.utility.complainToast("Error parsing XML file: " + this.i, this.context);
         } catch (FileNotFoundException var5) {
            DbgLog.error("File was not found: " + this.i);
            DbgLog.logStacktrace(var5);
            this.utility.complainToast("That file was not found: " + this.i, this.context);
         }
      }
   }

   private void e() {
      if(this.map.size() == 0) {
         this.utility.setOrangeText("Scan to find devices.", "In order to find devices: \n    1. Attach a robot \n    2. Press the \'Scan\' button", R.id.empty_devicelist, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
         this.g();
      } else {
         LinearLayout var1 = (LinearLayout)this.findViewById(R.id.empty_devicelist);
         var1.removeAllViews();
         var1.setVisibility(8);
      }
   }

   private void f() {
      if(this.map.size() == 0) {
         this.utility.setOrangeText("No devices found!", "In order to find devices: \n    1. Attach a robot \n    2. Press the \'Scan\' button", R.id.empty_devicelist, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
         this.g();
      } else {
         LinearLayout var1 = (LinearLayout)this.findViewById(R.id.empty_devicelist);
         var1.removeAllViews();
         var1.setVisibility(8);
      }
   }

   private void g() {
      LinearLayout var1 = (LinearLayout)this.findViewById(R.id.warning_layout);
      var1.removeAllViews();
      var1.setVisibility(8);
   }

   private void h() {
      ListView var1 = (ListView)this.findViewById(R.id.controllersList);
      var1.setAdapter(new DeviceInfoAdapter(this, 17367044, this.map));
      var1.setOnItemClickListener(new OnItemClickListener() {
         public void onItemClick(AdapterView<?> var1, View var2, int var3, long var4) {
            ControllerConfiguration var6 = (ControllerConfiguration)var1.getItemAtPosition(var3);
            DeviceConfiguration.ConfigurationType var7 = var6.getType();
            if(var7.equals(DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER)) {
               Intent var8 = new Intent(FtcConfigurationActivity.this.context, EditMotorControllerActivity.class);
               var8.putExtra("EDIT_MOTOR_CONTROLLER_CONFIG", var6);
               var8.putExtra("requestCode", 1);
               FtcConfigurationActivity.this.setResult(-1, var8);
               FtcConfigurationActivity.this.startActivityForResult(var8, 1);
            }

            if(var7.equals(DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER)) {
               Intent var11 = new Intent(FtcConfigurationActivity.this.context, EditServoControllerActivity.class);
               var11.putExtra("Edit Servo ControllerConfiguration Activity", var6);
               var11.putExtra("requestCode", 2);
               FtcConfigurationActivity.this.setResult(-1, var11);
               FtcConfigurationActivity.this.startActivityForResult(var11, 2);
            }

            if(var7.equals(DeviceConfiguration.ConfigurationType.LEGACY_MODULE_CONTROLLER)) {
               Intent var14 = new Intent(FtcConfigurationActivity.this.context, EditLegacyModuleControllerActivity.class);
               var14.putExtra("EDIT_LEGACY_CONFIG", var6);
               var14.putExtra("requestCode", 3);
               FtcConfigurationActivity.this.setResult(-1, var14);
               FtcConfigurationActivity.this.startActivityForResult(var14, 3);
            }

            if(var7.equals(DeviceConfiguration.ConfigurationType.DEVICE_INTERFACE_MODULE)) {
               Intent var17 = new Intent(FtcConfigurationActivity.this.context, EditDeviceInterfaceModuleActivity.class);
               var17.putExtra("EDIT_DEVICE_INTERFACE_MODULE_CONFIG", var6);
               var17.putExtra("requestCode", 4);
               FtcConfigurationActivity.this.setResult(-1, var17);
               FtcConfigurationActivity.this.startActivityForResult(var17, 4);
            }

         }
      });
   }

   private void i() {
      if(!this.i.toLowerCase().contains("Unsaved".toLowerCase())) {
         String var1 = "Unsaved " + this.i;
         this.utility.saveToPreferences(var1, R.string.pref_hardware_config_filename);
         this.i = var1;
      }

   }

   protected void onActivityResult(int var1, int var2, Intent var3) {
      if(var2 != 0) {
         Serializable var4;
         if(var1 == 1) {
            var4 = var3.getSerializableExtra("EDIT_MOTOR_CONTROLLER_CONFIG");
         } else if(var1 == 2) {
            var4 = var3.getSerializableExtra("Edit Servo ControllerConfiguration Activity");
         } else if(var1 == 3) {
            var4 = var3.getSerializableExtra("EDIT_LEGACY_CONFIG");
         } else {
            var4 = null;
            if(var1 == 4) {
               var4 = var3.getSerializableExtra("EDIT_DEVICE_INTERFACE_MODULE_CONFIG");
            }
         }

         if(var4 != null) {
            ControllerConfiguration var5 = (ControllerConfiguration)var4;
            this.scannedDevices.put(var5.getSerialNumber(), var5.configTypeToDeviceType(var5.getType()));
            this.map.put(var5.getSerialNumber(), var5);
            this.h();
            this.i();
         } else {
            DbgLog.error("Received Result with an incorrect request code: " + String.valueOf(var1));
         }
      }
   }

   public void onBackPressed() {
      if(this.i.toLowerCase().contains("Unsaved".toLowerCase())) {
         if(!this.utility.writeXML(this.map)) {
            final EditText editText = new EditText(this);
            editText.setText(this.utility.prepareFilename(this.i));
            Builder builder = this.utility.buildBuilder("Unsaved changes", "Please save your file before exiting the Hardware Wizard! \n If you click \'Cancel\' your changes will be lost.");
            builder.setView(editText);
            builder.setPositiveButton("Ok", new OnClickListener()
            {
            public void onClick(DialogInterface var1x, int var2)
               {
               String var3 = (editText.getText().toString() + ".xml").trim();
               if (var3.equals(".xml"))
                  {
                  FtcConfigurationActivity.this.utility.complainToast("File not saved: Please entire a filename.", FtcConfigurationActivity.this.context);
                  }
               else
                  {
                  try
                     {
                     FtcConfigurationActivity.this.utility.writeToFile(var3);
                     }
                  catch (RobotCoreException var6)
                     {
                     FtcConfigurationActivity.this.utility.complainToast(var6.getMessage(), FtcConfigurationActivity.this.context);
                     DbgLog.error(var6.getMessage());
                     return;
                     }
                  catch (IOException var7)
                     {
                     FtcConfigurationActivity.this.a(var7.getMessage());
                     DbgLog.error(var7.getMessage());
                     return;
                     }

                  FtcConfigurationActivity.this.g();
                  FtcConfigurationActivity.this.utility.saveToPreferences(editText.getText().toString(), R.string.pref_hardware_config_filename);
                  FtcConfigurationActivity.this.i = editText.getText().toString();
                  FtcConfigurationActivity.this.utility.updateHeader("robot_config", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                  FtcConfigurationActivity.this.utility.confirmSave();
                  FtcConfigurationActivity.this.finish();
                  }
               }
            });
            builder.setNegativeButton("Cancel", this.b);
            builder.show();
         }
      } else {
         super.onBackPressed();
      }
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(R.layout.activity_ftc_configuration);
      RobotLog.writeLogcatToDisk(this, 1024);
      this.context = this;
      this.utility = new Utility(this);
      this.button = (Button)this.findViewById(R.id.scanButton);
      this.a();

      try {
         this.g = new ModernRoboticsDeviceManager(this.context, (EventLoopManager)null);
      } catch (RobotCoreException var3) {
         this.utility.complainToast("Failed to open the Device Manager", this.context);
         DbgLog.error("Failed to open deviceManager: " + var3.toString());
         DbgLog.logStacktrace(var3);
      }

      this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
   }

   protected void onDestroy() {
      super.onDestroy();
      this.utility.resetCount();
   }

   protected void onStart() {
      super.onStart();
      this.i = this.utility.getFilenameFromPrefs(R.string.pref_hardware_config_filename, "No current file!");
      if(this.i.equalsIgnoreCase("No current file!")) {
         this.utility.createConfigFolder();
      }

      this.utility.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
      this.e();
      if(!this.i.toLowerCase().contains("Unsaved".toLowerCase())) {
         this.d();
      }

      this.button.setOnClickListener(new android.view.View.OnClickListener()
      {
      public void onClick(View var1)
         {
         FtcConfigurationActivity.this.thread = new Thread(new Runnable()
         {
         public void run()
            {
            try
               {
               DbgLog.msg("Scanning USB bus");
               FtcConfigurationActivity.this.scannedDevices = FtcConfigurationActivity.this.g.scanForUsbDevices();
               }
            catch (RobotCoreException var2)
               {
               DbgLog.error("Device scan failed: " + var2.toString());
               }

            FtcConfigurationActivity.this.runOnUiThread(new Runnable()
            {
            public void run()
               {
               FtcConfigurationActivity.this.utility.resetCount();
               FtcConfigurationActivity.this.g();
               FtcConfigurationActivity.this.i();
               FtcConfigurationActivity.this.utility.updateHeader(FtcConfigurationActivity.this.i, R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
               FtcConfigurationActivity.this.scannedEntries = FtcConfigurationActivity.this.scannedDevices.entrySet();
               FtcConfigurationActivity.this.map = FtcConfigurationActivity.this.b();
               FtcConfigurationActivity.this.h();
               FtcConfigurationActivity.this.f();
               }
            });
            }
         });
         FtcConfigurationActivity.this.c();
         }
      });
   }

   public void saveConfiguration(View var1) {
      if(!this.utility.writeXML(this.map)) {
         final EditText var2 = new EditText(this);
         var2.setText(this.utility.prepareFilename(this.i));
         Builder var3 = this.utility.buildBuilder("Enter file name", "Please enter the file name");
         var3.setView(var2);
         var3.setPositiveButton("Ok", new OnClickListener() {
            public void onClick(DialogInterface var1, int var2x) {
               String var3 = (var2.getText().toString() + ".xml").trim();
               if(var3.equals(".xml")) {
                  FtcConfigurationActivity.this.utility.complainToast("File not saved: Please entire a filename.", FtcConfigurationActivity.this.context);
               } else {
                  try {
                     FtcConfigurationActivity.this.utility.writeToFile(var3);
                  } catch (RobotCoreException var6) {
                     FtcConfigurationActivity.this.utility.complainToast(var6.getMessage(), FtcConfigurationActivity.this.context);
                     DbgLog.error(var6.getMessage());
                     return;
                  } catch (IOException var7) {
                     FtcConfigurationActivity.this.a(var7.getMessage());
                     DbgLog.error(var7.getMessage());
                     return;
                  }

                  FtcConfigurationActivity.this.g();
                  FtcConfigurationActivity.this.utility.saveToPreferences(var2.getText().toString(), R.string.pref_hardware_config_filename);
                  FtcConfigurationActivity.this.i = var2.getText().toString();
                  FtcConfigurationActivity.this.utility.updateHeader("robot_config", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                  FtcConfigurationActivity.this.utility.confirmSave();
               }
            }
         });
         var3.setNegativeButton("Cancel", this.c);
         var3.show();
      }
   }
}
