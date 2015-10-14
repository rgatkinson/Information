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
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.util.RobotLog;

import java.util.ArrayList;
import java.util.Iterator;

public class EditPWMDevicesActivity extends Activity {
   public static final String EDIT_PWM_DEVICES = "EDIT_PWM_DEVICES";
   private Utility a;
   private View b;
   private View c;
   private ArrayList<DeviceConfiguration> d = new ArrayList();

   private void a() {
      Bundle var1 = new Bundle();

      for(int var2 = 0; var2 < this.d.size(); ++var2) {
         var1.putSerializable(String.valueOf(var2), this.d.get(var2));
      }

      Intent var3 = new Intent();
      var3.putExtras(var1);
      var3.putExtras(var1);
      this.setResult(-1, var3);
      this.finish();
   }

   private void a(int var1) {
      View var2 = this.d(var1);
      CheckBox var3 = (CheckBox)var2.findViewById(R.id.checkbox_port_pwm);
      DeviceConfiguration var4 = this.d.get(var1);
      if(var4.isEnabled()) {
         var3.setChecked(true);
         ((EditText)var2.findViewById(R.id.editTextResult_pwm)).setText(var4.getName());
      } else {
         var3.setChecked(true);
         var3.performClick();
      }
   }

   private void b(int var1) {
      View var2 = this.d(var1);
      ((EditText)var2.findViewById(R.id.editTextResult_pwm)).addTextChangedListener(new EditPWMDevicesActivity.a(var2, null));
   }

   private void c(int var1) {
      View var2 = this.d(var1);
      final EditText var3 = (EditText)var2.findViewById(R.id.editTextResult_pwm);
      final DeviceConfiguration var4 = this.d.get(var1);
      var2.findViewById(R.id.checkbox_port_pwm).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            if (((CheckBox) var1).isChecked()) {
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
      case 0:
         return this.b;
      case 1:
         return this.c;
      default:
         return null;
      }
   }

   public void cancelPWMDevices(View var1) {
      this.setResult(0, new Intent());
      this.finish();
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(R.layout.pwms);
      PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
      this.a = new Utility(this);
      RobotLog.writeLogcatToDisk(this, 1024);
      LinearLayout var2 = (LinearLayout)this.findViewById(R.id.linearLayout_pwm0);
      this.b = this.getLayoutInflater().inflate(R.layout.pwm_device, var2, true);
      ((TextView)this.b.findViewById(R.id.port_number_pwm)).setText("0");
      LinearLayout var3 = (LinearLayout)this.findViewById(R.id.linearLayout_pwm1);
      this.c = this.getLayoutInflater().inflate(R.layout.pwm_device, var3, true);
      ((TextView)this.c.findViewById(R.id.port_number_pwm)).setText("1");
   }

   protected void onStart() {
      super.onStart();
      this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
      Bundle var1 = this.getIntent().getExtras();
      if(var1 != null) {
         Iterator var2 = var1.keySet().iterator();

         while(var2.hasNext()) {
            String var4 = (String)var2.next();
            this.d.add(Integer.parseInt(var4), (DeviceConfiguration)var1.getSerializable(var4));
         }

         for(int var3 = 0; var3 < this.d.size(); ++var3) {
            this.c(var3);
            this.b(var3);
            this.a(var3);
         }
      }

   }

   public void savePWMDevices(View var1) {
      this.a();
   }

   private class a implements TextWatcher {
      private int b;

      private a(View var2) {
         this.b = Integer.parseInt(((TextView)var2.findViewById(R.id.port_number_pwm)).getText().toString());
      }

      // $FF: synthetic method
      a(View var2, Object var3) {
         this(var2);
      }

      public void afterTextChanged(Editable var1) {
         EditPWMDevicesActivity.this.d.get(this.b).setName(var1.toString());
      }

      public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }

      public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }
   }
}
