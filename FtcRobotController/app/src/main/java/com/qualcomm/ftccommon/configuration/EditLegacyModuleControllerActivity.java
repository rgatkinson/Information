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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.qualcomm.ftccommon.R;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.LegacyModuleControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MatrixControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ServoConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;

import java.io.Serializable;
import java.util.ArrayList;

public class EditLegacyModuleControllerActivity extends Activity {
   public static final String EDIT_LEGACY_CONFIG = "EDIT_LEGACY_CONFIG";
   public static final int EDIT_MATRIX_CONTROLLER_REQUEST_CODE = 103;
   public static final int EDIT_MOTOR_CONTROLLER_REQUEST_CODE = 101;
   public static final int EDIT_SERVO_CONTROLLER_REQUEST_CODE = 102;
   private static boolean a = false;
   private Utility b;
   private String c;
   private Context d;
   private LegacyModuleControllerConfiguration e;
   private EditText f;
   private ArrayList<DeviceConfiguration> g = new ArrayList();
   private View h;
   private View i;
   private View j;
   private View k;
   private View l;
   private View m;
   private OnItemSelectedListener n = new OnItemSelectedListener() {
      public void onItemSelected(AdapterView<?> var1, View var2, int var3, long var4) {
         String var6 = var1.getItemAtPosition(var3).toString();
         LinearLayout var7 = (LinearLayout)var2.getParent().getParent().getParent();
         if(var6.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.NOTHING.toString())) {
            EditLegacyModuleControllerActivity.this.a(var7);
         } else {
            EditLegacyModuleControllerActivity.this.a(var7, var6);
         }
      }

      public void onNothingSelected(AdapterView<?> var1) {
      }
   };

   private View a(int var1) {
      switch(var1) {
      case 0:
         return this.h;
      case 1:
         return this.i;
      case 2:
         return this.j;
      case 3:
         return this.k;
      case 4:
         return this.l;
      case 5:
         return this.m;
      default:
         return null;
      }
   }

   private void a() {
      Intent var1 = new Intent();
      this.e.setName(this.f.getText().toString());
      var1.putExtra("EDIT_LEGACY_CONFIG", this.e);
      this.setResult(-1, var1);
      this.finish();
   }

   private void a(int var1, int var2) {
      this.a(var1).findViewById(R.id.edit_controller_btn).setVisibility(var2);
   }

   private void a(int var1, String var2) {
      DeviceConfiguration var3 = this.g.get(var1);
      String var4 = var3.getName();
      ArrayList var5 = new ArrayList();
      SerialNumber var6 = ControllerConfiguration.NO_SERIAL_NUMBER;
      if(!var3.getType().toString().equalsIgnoreCase(var2)) {
         Object var7 = new ControllerConfiguration("dummy module", var5, var6, DeviceConfiguration.ConfigurationType.NOTHING);
         if(var2.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER.toString())) {
            for(int var16 = 1; var16 <= 2; ++var16) {
               var5.add(new MotorConfiguration(var16));
            }

            var7 = new MotorControllerConfiguration(var4, var5, var6);
            ((ControllerConfiguration)var7).setPort(var1);
         } else if(var2.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER.toString())) {
            for(int var14 = 1; var14 <= 6; ++var14) {
               var5.add(new ServoConfiguration(var14));
            }

            var7 = new ServoControllerConfiguration(var4, var5, var6);
            ((ControllerConfiguration)var7).setPort(var1);
         } else if(var2.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MATRIX_CONTROLLER.toString())) {
            ArrayList var8 = new ArrayList();

            for(int var9 = 1; var9 <= 4; ++var9) {
               var8.add(new MotorConfiguration(var9));
            }

            ArrayList var10 = new ArrayList();

            for(int var11 = 1; var11 <= 4; ++var11) {
               var10.add(new ServoConfiguration(var11));
            }

            var7 = new MatrixControllerConfiguration(var4, var8, var10, var6);
            ((ControllerConfiguration)var7).setPort(var1);
         }

         ((ControllerConfiguration)var7).setEnabled(true);
         this.b((DeviceConfiguration)var7);
      }

   }

   private void a(View var1, DeviceConfiguration var2) {
      Spinner var3 = (Spinner)var1.findViewById(R.id.choiceSpinner_legacyModule);
      var3.setSelection(((ArrayAdapter)var3.getAdapter()).getPosition(var2.getType().toString()));
      var3.setOnItemSelectedListener(this.n);
      String var4 = var2.getName();
      EditText var5 = (EditText)var1.findViewById(R.id.editTextResult_name);
      int var6 = Integer.parseInt(((TextView)var1.findViewById(R.id.portNumber)).getText().toString());
      var5.addTextChangedListener(new EditLegacyModuleControllerActivity.a(var2, null));
      var5.setText(var4);
      if(a) {
         RobotLog.e("[populatePort] name: " + var4 + ", port: " + var6 + ", type: " + var2.getType());
      }

   }

   private void a(EditText var1, DeviceConfiguration var2) {
      if(var1.getText().toString().equalsIgnoreCase("NO DEVICE ATTACHED")) {
         var1.setText("");
         var2.setName("");
      } else {
         var1.setText(var2.getName());
      }
   }

   private void a(LinearLayout var1) {
      int var2 = Integer.parseInt(((TextView)var1.findViewById(R.id.portNumber)).getText().toString());
      EditText var3 = (EditText)var1.findViewById(R.id.editTextResult_name);
      var3.setEnabled(false);
      var3.setText("NO DEVICE ATTACHED");
      DeviceConfiguration var4 = new DeviceConfiguration(DeviceConfiguration.ConfigurationType.NOTHING);
      var4.setPort(var2);
      this.b(var4);
      this.a(var2, 8);
   }

   private void a(LinearLayout var1, String var2) {
      int var3 = Integer.parseInt(((TextView)var1.findViewById(R.id.portNumber)).getText().toString());
      EditText var4 = (EditText)var1.findViewById(R.id.editTextResult_name);
      DeviceConfiguration var5 = this.g.get(var3);
      var4.setEnabled(true);
      this.a(var4, var5);
      DeviceConfiguration.ConfigurationType var6 = var5.typeFromString(var2);
      if(var6 != DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER && var6 != DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER && var6 != DeviceConfiguration.ConfigurationType.MATRIX_CONTROLLER) {
         var5.setType(var6);
         if(var6 == DeviceConfiguration.ConfigurationType.NOTHING) {
            var5.setEnabled(false);
         } else {
            var5.setEnabled(true);
         }

         this.a(var3, 8);
      } else {
         this.a(var3, var2);
         this.a(var3, 0);
      }

      if(a) {
         DeviceConfiguration var7 = this.g.get(var3);
         RobotLog.e("[changeDevice] modules.get(port) name: " + var7.getName() + ", port: " + var7.getPort() + ", type: " + var7.getType());
      }

   }

   private void a(DeviceConfiguration var1) {
      var1.setName(((EditText) this.a(var1.getPort()).findViewById(R.id.editTextResult_name)).getText().toString());
      if(var1.getType() == DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER) {
         Intent var2 = new Intent(this.d, EditMotorControllerActivity.class);
         var2.putExtra("EDIT_MOTOR_CONTROLLER_CONFIG", var1);
         var2.putExtra("requestCode", 101);
         this.setResult(-1, var2);
         this.startActivityForResult(var2, 101);
      } else {
         if(var1.getType() == DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER) {
            Intent var5 = new Intent(this.d, EditServoControllerActivity.class);
            var5.putExtra("Edit Servo ControllerConfiguration Activity", var1);
            this.setResult(-1, var5);
            this.startActivityForResult(var5, 102);
            return;
         }

         if(var1.getType() == DeviceConfiguration.ConfigurationType.MATRIX_CONTROLLER) {
            Intent var7 = new Intent(this.d, EditMatrixControllerActivity.class);
            var7.putExtra("Edit Matrix ControllerConfiguration Activity", var1);
            this.setResult(-1, var7);
            this.startActivityForResult(var7, 103);
            return;
         }
      }

   }

   private void b(DeviceConfiguration var1) {
      int var2 = var1.getPort();
      this.g.set(var2, var1);
   }

   private boolean c(DeviceConfiguration var1) {
      return var1.getType() == DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER || var1.getType() == DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER;
   }

   public void cancelLegacyController(View var1) {
      this.setResult(0, new Intent());
      this.finish();
   }

   public void editController_portALL(View var1) {
      LinearLayout var2 = (LinearLayout)var1.getParent().getParent();
      int var3 = Integer.parseInt(((TextView)var2.findViewById(R.id.portNumber)).getText().toString());
      DeviceConfiguration var4 = this.g.get(var3);
      if(!this.c(var4)) {
         this.a(var3, ((Spinner)var2.findViewById(R.id.choiceSpinner_legacyModule)).getSelectedItem().toString());
      }

      this.a(var4);
   }

   protected void onActivityResult(int var1, int var2, Intent var3) {
      if(var2 == -1) {
         Serializable var4;
         if(var1 == 101) {
            var4 = var3.getSerializableExtra("EDIT_MOTOR_CONTROLLER_CONFIG");
         } else if(var1 == 102) {
            var4 = var3.getSerializableExtra("Edit Servo ControllerConfiguration Activity");
         } else {
            var4 = null;
            if(var1 == 103) {
               var4 = var3.getSerializableExtra("Edit Matrix ControllerConfiguration Activity");
            }
         }

         if(var4 != null) {
            ControllerConfiguration var5 = (ControllerConfiguration)var4;
            this.b(var5);
            this.a(this.a(var5.getPort()), this.g.get(var5.getPort()));
            if(!this.c.toLowerCase().contains("Unsaved".toLowerCase())) {
               String var6 = "Unsaved " + this.c;
               this.b.saveToPreferences(var6, R.string.pref_hardware_config_filename);
               this.c = var6;
            }

            this.b.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
         }
      }

   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(R.layout.legacy);
      LinearLayout var2 = (LinearLayout)this.findViewById(R.id.linearLayout0);
      this.h = this.getLayoutInflater().inflate(R.layout.simple_device, var2, true);
      ((TextView)this.h.findViewById(R.id.portNumber)).setText("0");
      LinearLayout var3 = (LinearLayout)this.findViewById(R.id.linearLayout1);
      this.i = this.getLayoutInflater().inflate(R.layout.simple_device, var3, true);
      ((TextView)this.i.findViewById(R.id.portNumber)).setText("1");
      LinearLayout var4 = (LinearLayout)this.findViewById(R.id.linearLayout2);
      this.j = this.getLayoutInflater().inflate(R.layout.simple_device, var4, true);
      ((TextView)this.j.findViewById(R.id.portNumber)).setText("2");
      LinearLayout var5 = (LinearLayout)this.findViewById(R.id.linearLayout3);
      this.k = this.getLayoutInflater().inflate(R.layout.simple_device, var5, true);
      ((TextView)this.k.findViewById(R.id.portNumber)).setText("3");
      LinearLayout var6 = (LinearLayout)this.findViewById(R.id.linearLayout4);
      this.l = this.getLayoutInflater().inflate(R.layout.simple_device, var6, true);
      ((TextView)this.l.findViewById(R.id.portNumber)).setText("4");
      LinearLayout var7 = (LinearLayout)this.findViewById(R.id.linearLayout5);
      this.m = this.getLayoutInflater().inflate(R.layout.simple_device, var7, true);
      ((TextView)this.m.findViewById(R.id.portNumber)).setText("5");
      this.d = this;
      PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
      this.b = new Utility(this);
      RobotLog.writeLogcatToDisk(this, 1024);
      this.f = (EditText)this.findViewById(R.id.device_interface_module_name);
   }

   protected void onStart() {
      super.onStart();
      this.b.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
      this.c = this.b.getFilenameFromPrefs(R.string.pref_hardware_config_filename, "No current file!");
      Serializable var1 = this.getIntent().getSerializableExtra("EDIT_LEGACY_CONFIG");
      if(var1 != null) {
         this.e = (LegacyModuleControllerConfiguration)var1;
         this.g = (ArrayList)this.e.getDevices();
         this.f.setText(this.e.getName());
         this.f.addTextChangedListener(new EditLegacyModuleControllerActivity.a(this.e, null));
         ((TextView)this.findViewById(R.id.legacy_serialNumber)).setText(this.e.getSerialNumber().toString());

         for(int var2 = 0; var2 < this.g.size(); ++var2) {
            DeviceConfiguration var3 = this.g.get(var2);
            if(a) {
               RobotLog.e("[onStart] module name: " + var3.getName() + ", port: " + var3.getPort() + ", type: " + var3.getType());
            }

            this.a(this.a(var2), var3);
         }
      }

   }

   public void saveLegacyController(View var1) {
      this.a();
   }

   private class a implements TextWatcher {
      private DeviceConfiguration b;

      private a(DeviceConfiguration var2) {
         this.b = var2;
      }

      // $FF: synthetic method
      a(DeviceConfiguration var2, Object var3) {
         this(var2);
      }

      public void afterTextChanged(Editable var1) {
         String var2 = var1.toString();
         this.b.setName(var2);
      }

      public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }

      public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }
   }
}
