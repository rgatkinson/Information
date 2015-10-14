package com.qualcomm.ftccommon.configuration;

import android.app.Activity;
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
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.util.RobotLog;

import java.util.ArrayList;
import java.util.Iterator;

public class EditAnalogInputDevicesActivity extends Activity {
   private Utility a;
   private View b;
   private View c;
   private View d;
   private View e;
   private View f;
   private View g;
   private View h;
   private View i;
   private ArrayList<DeviceConfiguration> j = new ArrayList();
   private OnItemSelectedListener k = new OnItemSelectedListener() {
      public void onItemSelected(AdapterView<?> var1, View var2, int var3, long var4) {
         String var6 = var1.getItemAtPosition(var3).toString();
         LinearLayout var7 = (LinearLayout)var2.getParent().getParent().getParent();
         if(var6.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.NOTHING.toString())) {
            EditAnalogInputDevicesActivity.this.a(var7);
         } else {
            EditAnalogInputDevicesActivity.this.a(var7, var6);
         }
      }

      public void onNothingSelected(AdapterView<?> var1) {
      }
   };

   private View a(int var1) {
      switch(var1) {
      case 0:
         return this.b;
      case 1:
         return this.c;
      case 2:
         return this.d;
      case 3:
         return this.e;
      case 4:
         return this.f;
      case 5:
         return this.g;
      case 6:
         return this.h;
      case 7:
         return this.i;
      default:
         return null;
      }
   }

   private void a() {
      Bundle var1 = new Bundle();

      for(int var2 = 0; var2 < this.j.size(); ++var2) {
         var1.putSerializable(String.valueOf(var2), this.j.get(var2));
      }

      Intent var3 = new Intent();
      var3.putExtras(var1);
      var3.putExtras(var1);
      this.setResult(-1, var3);
      this.finish();
   }

   private void a(View var1) {
      ((EditText)var1.findViewById(R.id.editTextResult_analogInput)).addTextChangedListener(new EditAnalogInputDevicesActivity.a(var1, null));
   }

   private void a(View var1, DeviceConfiguration var2) {
      Spinner var3 = (Spinner)var1.findViewById(R.id.choiceSpinner_analogInput);
      ArrayAdapter var4 = (ArrayAdapter)var3.getAdapter();
      if(var2.isEnabled()) {
         var3.setSelection(var4.getPosition(var2.getType().toString()));
      } else {
         var3.setSelection(0);
      }

      var3.setOnItemSelectedListener(this.k);
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
      int var2 = Integer.parseInt(((TextView)var1.findViewById(R.id.port_number_analogInput)).getText().toString());
      EditText var3 = (EditText)var1.findViewById(R.id.editTextResult_analogInput);
      var3.setEnabled(false);
      var3.setText("NO DEVICE ATTACHED");
      this.j.get(var2).setEnabled(false);
   }

   private void a(LinearLayout var1, String var2) {
      int var3 = Integer.parseInt(((TextView)var1.findViewById(R.id.port_number_analogInput)).getText().toString());
      EditText var4 = (EditText)var1.findViewById(R.id.editTextResult_analogInput);
      var4.setEnabled(true);
      DeviceConfiguration var5 = this.j.get(var3);
      var5.setType(var5.typeFromString(var2));
      var5.setEnabled(true);
      this.a(var4, var5);
   }

   private void b(View var1, DeviceConfiguration var2) {
      EditText var3 = (EditText)var1.findViewById(R.id.editTextResult_analogInput);
      if(var2.isEnabled()) {
         var3.setText(var2.getName());
         var3.setEnabled(true);
      } else {
         var3.setText("NO DEVICE ATTACHED");
         var3.setEnabled(false);
      }
   }

   public void cancelAnalogInputDevices(View var1) {
      this.setResult(0, new Intent());
      this.finish();
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(R.layout.analog_inputs);
      PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
      this.a = new Utility(this);
      RobotLog.writeLogcatToDisk(this, 1024);
      LinearLayout var2 = (LinearLayout)this.findViewById(R.id.linearLayout_analogInput0);
      this.b = this.getLayoutInflater().inflate(R.layout.analog_input_device, var2, true);
      ((TextView)this.b.findViewById(R.id.port_number_analogInput)).setText("0");
      LinearLayout var3 = (LinearLayout)this.findViewById(R.id.linearLayout_analogInput1);
      this.c = this.getLayoutInflater().inflate(R.layout.analog_input_device, var3, true);
      ((TextView)this.c.findViewById(R.id.port_number_analogInput)).setText("1");
      LinearLayout var4 = (LinearLayout)this.findViewById(R.id.linearLayout_analogInput2);
      this.d = this.getLayoutInflater().inflate(R.layout.analog_input_device, var4, true);
      ((TextView)this.d.findViewById(R.id.port_number_analogInput)).setText("2");
      LinearLayout var5 = (LinearLayout)this.findViewById(R.id.linearLayout_analogInput3);
      this.e = this.getLayoutInflater().inflate(R.layout.analog_input_device, var5, true);
      ((TextView)this.e.findViewById(R.id.port_number_analogInput)).setText("3");
      LinearLayout var6 = (LinearLayout)this.findViewById(R.id.linearLayout_analogInput4);
      this.f = this.getLayoutInflater().inflate(R.layout.analog_input_device, var6, true);
      ((TextView)this.f.findViewById(R.id.port_number_analogInput)).setText("4");
      LinearLayout var7 = (LinearLayout)this.findViewById(R.id.linearLayout_analogInput5);
      this.g = this.getLayoutInflater().inflate(R.layout.analog_input_device, var7, true);
      ((TextView)this.g.findViewById(R.id.port_number_analogInput)).setText("5");
      LinearLayout var8 = (LinearLayout)this.findViewById(R.id.linearLayout_analogInput6);
      this.h = this.getLayoutInflater().inflate(R.layout.analog_input_device, var8, true);
      ((TextView)this.h.findViewById(R.id.port_number_analogInput)).setText("6");
      LinearLayout var9 = (LinearLayout)this.findViewById(R.id.linearLayout_analogInput7);
      this.i = this.getLayoutInflater().inflate(R.layout.analog_input_device, var9, true);
      ((TextView)this.i.findViewById(R.id.port_number_analogInput)).setText("7");
   }

   protected void onStart() {
      super.onStart();
      this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
      Bundle var1 = this.getIntent().getExtras();
      if(var1 != null) {
         Iterator var2 = var1.keySet().iterator();

         while(var2.hasNext()) {
            String var6 = (String)var2.next();
            DeviceConfiguration var7 = (DeviceConfiguration)var1.getSerializable(var6);
            this.j.add(Integer.parseInt(var6), var7);
         }

         for(int var3 = 0; var3 < this.j.size(); ++var3) {
            View var4 = this.a(var3);
            DeviceConfiguration var5 = this.j.get(var3);
            this.a(var4);
            this.b(var4, var5);
            this.a(var4, var5);
         }
      }

   }

   public void saveAnalogInputDevices(View var1) {
      this.a();
   }

   private class a implements TextWatcher {
      private int b;

      private a(View var2) {
         this.b = Integer.parseInt(((TextView)var2.findViewById(R.id.port_number_analogInput)).getText().toString());
      }

      // $FF: synthetic method
      a(View var2, Object var3) {
         this(var2);
      }

      public void afterTextChanged(Editable var1) {
         EditAnalogInputDevicesActivity.this.j.get(this.b).setName(var1.toString());
      }

      public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }

      public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }
   }
}
