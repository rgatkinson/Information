package com.qualcomm.ftccommon.configuration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.qualcomm.ftccommon.R;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.Serializable;
import java.util.ArrayList;

public class EditServoControllerActivity extends Activity {
   public static final String EDIT_SERVO_ACTIVITY = "Edit Servo ControllerConfiguration Activity";
   private Utility a;
   private ServoControllerConfiguration b;
   private ArrayList<DeviceConfiguration> c;
   private EditText d;
   private View e;
   private View f;
   private View g;
   private View h;
   private View i;
   private View j;

   private void a() {
      Intent var1 = new Intent();
      this.b.addServos(this.c);
      this.b.setName(this.d.getText().toString());
      var1.putExtra("Edit Servo ControllerConfiguration Activity", this.b);
      this.setResult(-1, var1);
      this.finish();
   }

   private void a(int var1) {
      View var2 = this.d(var1);
      ((EditText)var2.findViewById(R.id.editTextResult_servo)).addTextChangedListener(new EditServoControllerActivity.a(var2, null));
   }

   private void b(int var1) {
      View var2 = this.d(var1);
      CheckBox var3 = (CheckBox)var2.findViewById(R.id.checkbox_port_servo);
      DeviceConfiguration var4 = (DeviceConfiguration)this.c.get(var1 - 1);
      if(var4.isEnabled()) {
         var3.setChecked(true);
         ((EditText)var2.findViewById(R.id.editTextResult_servo)).setText(var4.getName());
      } else {
         var3.setChecked(true);
         var3.performClick();
      }
   }

   private void c(int var1) {
      View var2 = this.d(var1);
      final EditText var3 = (EditText)var2.findViewById(R.id.editTextResult_servo);
      final DeviceConfiguration var4 = (DeviceConfiguration)this.c.get(var1 - 1);
      ((CheckBox)var2.findViewById(R.id.checkbox_port_servo)).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            if(((CheckBox)var1).isChecked()) {
               var3.setEnabled(true);
               var3.setText("");
               var4.setEnabled(true);
               var4.setName("");
            } else {
               var3.setEnabled(false);
               var4.setEnabled(false);
               var4.setName("NO DEVICE ATTACHED");
               var3.setText("NO DEVICE ATTACHED");
            }
         }
      });
   }

   private View d(int var1) {
      switch(var1) {
      case 1:
         return this.e;
      case 2:
         return this.f;
      case 3:
         return this.g;
      case 4:
         return this.h;
      case 5:
         return this.i;
      case 6:
         return this.j;
      default:
         return null;
      }
   }

   public void cancelServoController(View var1) {
      this.setResult(0, new Intent());
      this.finish();
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(R.layout.servos);
      PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
      this.a = new Utility(this);
      RobotLog.writeLogcatToDisk(this, 1024);
      this.d = (EditText)this.findViewById(R.id.servocontroller_name);
      LinearLayout var2 = (LinearLayout)this.findViewById(R.id.linearLayout_servo1);
      this.e = this.getLayoutInflater().inflate(R.layout.servo, var2, true);
      ((TextView)this.e.findViewById(R.id.port_number_servo)).setText("1");
      LinearLayout var3 = (LinearLayout)this.findViewById(R.id.linearLayout_servo2);
      this.f = this.getLayoutInflater().inflate(R.layout.servo, var3, true);
      ((TextView)this.f.findViewById(R.id.port_number_servo)).setText("2");
      LinearLayout var4 = (LinearLayout)this.findViewById(R.id.linearLayout_servo3);
      this.g = this.getLayoutInflater().inflate(R.layout.servo, var4, true);
      ((TextView)this.g.findViewById(R.id.port_number_servo)).setText("3");
      LinearLayout var5 = (LinearLayout)this.findViewById(R.id.linearLayout_servo4);
      this.h = this.getLayoutInflater().inflate(R.layout.servo, var5, true);
      ((TextView)this.h.findViewById(R.id.port_number_servo)).setText("4");
      LinearLayout var6 = (LinearLayout)this.findViewById(R.id.linearLayout_servo5);
      this.i = this.getLayoutInflater().inflate(R.layout.servo, var6, true);
      ((TextView)this.i.findViewById(R.id.port_number_servo)).setText("5");
      LinearLayout var7 = (LinearLayout)this.findViewById(R.id.linearLayout_servo6);
      this.j = this.getLayoutInflater().inflate(R.layout.servo, var7, true);
      ((TextView)this.j.findViewById(R.id.port_number_servo)).setText("6");
   }

   protected void onStart() {
      super.onStart();
      this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
      Serializable var1 = this.getIntent().getSerializableExtra("Edit Servo ControllerConfiguration Activity");
      if(var1 != null) {
         this.b = (ServoControllerConfiguration)var1;
         this.c = (ArrayList)this.b.getServos();
      }

      this.d.setText(this.b.getName());
      TextView var2 = (TextView)this.findViewById(R.id.servo_controller_serialNumber);
      String var3 = this.b.getSerialNumber().toString();
      if(var3.equalsIgnoreCase(ControllerConfiguration.NO_SERIAL_NUMBER.toString())) {
         var3 = "No serial number";
      }

      var2.setText(var3);

      for(int var4 = 0; var4 < this.c.size(); ++var4) {
         this.c(var4 + 1);
         this.a(var4 + 1);
         this.b(var4 + 1);
      }

   }

   public void saveServoController(View var1) {
      this.a();
   }

   private class a implements TextWatcher {
      private int b;

      private a(View var2) {
         this.b = Integer.parseInt(((TextView)var2.findViewById(R.id.port_number_servo)).getText().toString());
      }

      // $FF: synthetic method
      a(View var2, Object var3) {
         this();
      }

      public void afterTextChanged(Editable var1) {
         ((DeviceConfiguration)EditServoControllerActivity.this.c.get(-1 + this.b)).setName(var1.toString());
      }

      public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }

      public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }
   }
}
