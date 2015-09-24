package com.qualcomm.ftccommon.configuration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.qualcomm.ftccommon.R;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.Serializable;
import java.util.ArrayList;

public class EditMotorControllerActivity extends Activity {
   public static final String EDIT_MOTOR_CONTROLLER_CONFIG = "EDIT_MOTOR_CONTROLLER_CONFIG";
   private Utility a;
   private MotorControllerConfiguration b;
   private ArrayList<DeviceConfiguration> c = new ArrayList();
   private MotorConfiguration d = new MotorConfiguration(1);
   private MotorConfiguration e = new MotorConfiguration(2);
   private EditText f;
   private boolean g = true;
   private boolean h = true;
   private CheckBox i;
   private CheckBox j;
   private EditText k;
   private EditText l;

   private void a() {
      this.i.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            if(((CheckBox)var1).isChecked()) {
               EditMotorControllerActivity.this.g = true;
               EditMotorControllerActivity.this.k.setEnabled(true);
               EditMotorControllerActivity.this.k.setText("");
               EditMotorControllerActivity.this.d.setPort(1);
               EditMotorControllerActivity.this.d.setType(DeviceConfiguration.ConfigurationType.MOTOR);
            } else {
               EditMotorControllerActivity.this.g = false;
               EditMotorControllerActivity.this.k.setEnabled(false);
               EditMotorControllerActivity.this.k.setText("NO DEVICE ATTACHED");
               EditMotorControllerActivity.this.d.setType(DeviceConfiguration.ConfigurationType.NOTHING);
            }
         }
      });
   }

   private void a(MotorConfiguration var1, CheckBox var2) {
      if(!var1.getName().equals("NO DEVICE ATTACHED") && var1.getType() != DeviceConfiguration.ConfigurationType.NOTHING) {
         var2.setChecked(true);
      } else {
         var2.setChecked(true);
         var2.performClick();
      }
   }

   private void b() {
      this.j.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            if(((CheckBox)var1).isChecked()) {
               EditMotorControllerActivity.this.h = true;
               EditMotorControllerActivity.this.l.setEnabled(true);
               EditMotorControllerActivity.this.l.setText("");
               EditMotorControllerActivity.this.e.setPort(2);
               EditMotorControllerActivity.this.d.setType(DeviceConfiguration.ConfigurationType.MOTOR);
            } else {
               EditMotorControllerActivity.this.h = false;
               EditMotorControllerActivity.this.l.setEnabled(false);
               EditMotorControllerActivity.this.l.setText("NO DEVICE ATTACHED");
               EditMotorControllerActivity.this.d.setType(DeviceConfiguration.ConfigurationType.NOTHING);
            }
         }
      });
   }

   private void c() {
      Intent var1 = new Intent();
      ArrayList var2 = new ArrayList();
      if(this.g) {
         MotorConfiguration var3 = new MotorConfiguration(this.k.getText().toString());
         var3.setEnabled(true);
         var3.setPort(1);
         var2.add(var3);
      } else {
         var2.add(new MotorConfiguration(1));
      }

      if(this.h) {
         MotorConfiguration var5 = new MotorConfiguration(this.l.getText().toString());
         var5.setEnabled(true);
         var5.setPort(2);
         var2.add(var5);
      } else {
         var2.add(new MotorConfiguration(2));
      }

      this.b.addMotors(var2);
      this.b.setName(this.f.getText().toString());
      var1.putExtra("EDIT_MOTOR_CONTROLLER_CONFIG", this.b);
      this.setResult(-1, var1);
      this.finish();
   }

   public void cancelMotorController(View var1) {
      this.setResult(0, new Intent());
      this.finish();
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(R.layout.motors);
      PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
      this.a = new Utility(this);
      RobotLog.writeLogcatToDisk(this, 1024);
      this.f = (EditText)this.findViewById(R.id.controller_name);
      this.i = (CheckBox)this.findViewById(R.id.checkbox_port7);
      this.j = (CheckBox)this.findViewById(R.id.checkbox_port6);
      this.k = (EditText)this.findViewById(R.id.editTextResult_analogInput7);
      this.l = (EditText)this.findViewById(R.id.editTextResult_analogInput6);
   }

   protected void onStart() {
      super.onStart();
      this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
      Serializable var1 = this.getIntent().getSerializableExtra("EDIT_MOTOR_CONTROLLER_CONFIG");
      if(var1 != null) {
         this.b = (MotorControllerConfiguration)var1;
         this.c = (ArrayList)this.b.getMotors();
         this.d = (MotorConfiguration)this.c.get(0);
         this.e = (MotorConfiguration)this.c.get(1);
         this.f.setText(this.b.getName());
         TextView var2 = (TextView)this.findViewById(R.id.motor_controller_serialNumber);
         String var3 = this.b.getSerialNumber().toString();
         if(var3.equalsIgnoreCase(ControllerConfiguration.NO_SERIAL_NUMBER.toString())) {
            var3 = "No serial number";
         }

         var2.setText(var3);
         this.k.setText(this.d.getName());
         this.l.setText(this.e.getName());
         this.a();
         this.a(this.d, this.i);
         this.b();
         this.a(this.e, this.j);
      }

   }

   public void saveMotorController(View var1) {
      this.c();
   }
}
