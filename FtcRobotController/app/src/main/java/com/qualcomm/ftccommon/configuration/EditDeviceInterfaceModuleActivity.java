package com.qualcomm.ftccommon.configuration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.qualcomm.ftccommon.R;
import com.qualcomm.ftccommon.configuration.EditAnalogInputDevicesActivity;
import com.qualcomm.ftccommon.configuration.EditAnalogOutputDevicesActivity;
import com.qualcomm.ftccommon.configuration.EditDigitalDevicesActivity;
import com.qualcomm.ftccommon.configuration.EditI2cDevicesActivity;
import com.qualcomm.ftccommon.configuration.EditPWMDevicesActivity;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceInterfaceModuleConfiguration;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EditDeviceInterfaceModuleActivity extends Activity {
   public static final int EDIT_ANALOG_INPUT_REQUEST_CODE = 203;
   public static final int EDIT_ANALOG_OUTPUT_REQUEST_CODE = 205;
   public static final String EDIT_DEVICE_INTERFACE_MODULE_CONFIG = "EDIT_DEVICE_INTERFACE_MODULE_CONFIG";
   public static final int EDIT_DIGITAL_REQUEST_CODE = 204;
   public static final int EDIT_I2C_PORT_REQUEST_CODE = 202;
   public static final int EDIT_PWM_PORT_REQUEST_CODE = 201;
   private Utility a;
   private String b;
   private Context c;
   private DeviceInterfaceModuleConfiguration d;
   private EditText e;
   private ArrayList<DeviceConfiguration> f = new ArrayList();
   private OnItemClickListener g = new OnItemClickListener() {
      public void onItemClick(AdapterView<?> var1, View var2, int var3, long var4) {
         switch(var3) {
         case 0:
            EditDeviceInterfaceModuleActivity.this.a(201, EditDeviceInterfaceModuleActivity.this.d.getPwmDevices(), EditPWMDevicesActivity.class);
            return;
         case 1:
            EditDeviceInterfaceModuleActivity.this.a(202, EditDeviceInterfaceModuleActivity.this.d.getI2cDevices(), EditI2cDevicesActivity.class);
            return;
         case 2:
            EditDeviceInterfaceModuleActivity.this.a(203, EditDeviceInterfaceModuleActivity.this.d.getAnalogInputDevices(), EditAnalogInputDevicesActivity.class);
            return;
         case 3:
            EditDeviceInterfaceModuleActivity.this.a(204, EditDeviceInterfaceModuleActivity.this.d.getDigitalDevices(), EditDigitalDevicesActivity.class);
            return;
         case 4:
            EditDeviceInterfaceModuleActivity.this.a(205, EditDeviceInterfaceModuleActivity.this.d.getAnalogOutputDevices(), EditAnalogOutputDevicesActivity.class);
            return;
         default:
         }
      }
   };

   private ArrayList<DeviceConfiguration> a(Bundle var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.keySet().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         DeviceConfiguration var5 = (DeviceConfiguration)var1.getSerializable(var4);
         var2.add(Integer.parseInt(var4), var5);
      }

      return var2;
   }

   private void a() {
      if(!this.b.toLowerCase().contains("Unsaved".toLowerCase())) {
         String var1 = "Unsaved " + this.b;
         this.a.saveToPreferences(var1, R.string.pref_hardware_config_filename);
         this.b = var1;
      }

      this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
   }

   private void a(int var1, List<DeviceConfiguration> var2, Class var3) {
      Bundle var4 = new Bundle();

      for(int var5 = 0; var5 < var2.size(); ++var5) {
         var4.putSerializable(String.valueOf(var5), (Serializable)var2.get(var5));
      }

      Intent var6 = new Intent(this.c, var3);
      var6.putExtras(var4);
      this.setResult(-1, var6);
      this.startActivityForResult(var6, var1);
   }

   private void b() {
      Intent var1 = new Intent();
      this.d.setName(this.e.getText().toString());
      var1.putExtra("EDIT_DEVICE_INTERFACE_MODULE_CONFIG", this.d);
      this.setResult(-1, var1);
      this.finish();
   }

   public void cancelDeviceInterface(View var1) {
      this.setResult(0, new Intent());
      this.finish();
   }

   protected void onActivityResult(int var1, int var2, Intent var3) {
      if(var2 == -1) {
         if(var1 == 201) {
            Bundle var8 = var3.getExtras();
            if(var8 != null) {
               this.d.setPwmDevices(this.a(var8));
            }
         } else if(var1 == 203) {
            Bundle var7 = var3.getExtras();
            if(var7 != null) {
               this.d.setAnalogInputDevices(this.a(var7));
            }
         } else if(var1 == 204) {
            Bundle var6 = var3.getExtras();
            if(var6 != null) {
               this.d.setDigitalDevices(this.a(var6));
            }
         } else if(var1 == 202) {
            Bundle var5 = var3.getExtras();
            if(var5 != null) {
               this.d.setI2cDevices(this.a(var5));
            }
         } else if(var1 == 205) {
            Bundle var4 = var3.getExtras();
            if(var4 != null) {
               this.d.setAnalogOutputDevices(this.a(var4));
            }
         }

         this.a();
      }

   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(R.layout.device_interface_module);
      String[] var2 = this.getResources().getStringArray(R.array.device_interface_module_options_array);
      ListView var3 = (ListView)this.findViewById(R.id.listView_devices);
      var3.setAdapter(new ArrayAdapter(this, 17367043, var2));
      var3.setOnItemClickListener(this.g);
      this.c = this;
      PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
      this.a = new Utility(this);
      RobotLog.writeLogcatToDisk(this, 1024);
      this.e = (EditText)this.findViewById(R.id.device_interface_module_name);
      this.e.addTextChangedListener(new EditDeviceInterfaceModuleActivity.a(null));
   }

   protected void onStart() {
      super.onStart();
      this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
      this.b = this.a.getFilenameFromPrefs(R.string.pref_hardware_config_filename, "No current file!");
      Serializable var1 = this.getIntent().getSerializableExtra("EDIT_DEVICE_INTERFACE_MODULE_CONFIG");
      if(var1 != null) {
         this.d = (DeviceInterfaceModuleConfiguration)var1;
         this.f = (ArrayList)this.d.getDevices();
         this.e.setText(this.d.getName());
         ((TextView)this.findViewById(R.id.device_interface_module_serialNumber)).setText(this.d.getSerialNumber().toString());
      }

   }

   public void saveDeviceInterface(View var1) {
      this.b();
   }

   private class a implements TextWatcher {
      private a() {
      }

      // $FF: synthetic method
      a(Object var2) {
         this();
      }

      public void afterTextChanged(Editable var1) {
         String var2 = var1.toString();
         EditDeviceInterfaceModuleActivity.this.d.setName(var2);
      }

      public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }

      public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }
   }
}
