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

public class EditAnalogOutputDevicesActivity extends Activity {
   private Utility a;
   private View b;
   private View c;
   private ArrayList<DeviceConfiguration> d = new ArrayList();
   private OnItemSelectedListener e = new OnItemSelectedListener() {
      public void onItemSelected(AdapterView<?> var1, View var2, int var3, long var4) {
         String var6 = var1.getItemAtPosition(var3).toString();
         LinearLayout var7 = (LinearLayout)var2.getParent().getParent().getParent();
         if(var6.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.NOTHING.toString())) {
            EditAnalogOutputDevicesActivity.this.a(var7);
         } else {
            EditAnalogOutputDevicesActivity.this.a(var7, var6);
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
      default:
         return null;
      }
   }

   private void a() {
      Bundle var1 = new Bundle();

      for(int var2 = 0; var2 < this.d.size(); ++var2) {
         DeviceConfiguration var6 = this.d.get(var2);
         var1.putSerializable(String.valueOf(var2), var6);
      }

      Intent var3 = new Intent();
      var3.putExtras(var1);
      var3.putExtras(var1);
      this.setResult(-1, var3);
      this.finish();
   }

   private void a(View var1) {
      ((EditText)var1.findViewById(R.id.editTextResult_analogOutput)).addTextChangedListener(new a(var1, null));
   }

   private void a(View var1, DeviceConfiguration var2) {
      Spinner var3 = (Spinner)var1.findViewById(R.id.choiceSpinner_analogOutput);
      ArrayAdapter var4 = (ArrayAdapter)var3.getAdapter();
      if(var2.isEnabled()) {
         var3.setSelection(var4.getPosition(var2.getType().toString()));
      } else {
         var3.setSelection(0);
      }

      var3.setOnItemSelectedListener(this.e);
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
      int var2 = Integer.parseInt(((TextView)var1.findViewById(R.id.port_number_analogOutput)).getText().toString());
      EditText var3 = (EditText)var1.findViewById(R.id.editTextResult_analogOutput);
      var3.setEnabled(false);
      var3.setText("NO DEVICE ATTACHED");
      this.d.get(var2).setEnabled(false);
   }

   private void a(LinearLayout var1, String var2) {
      int var3 = Integer.parseInt(((TextView)var1.findViewById(R.id.port_number_analogOutput)).getText().toString());
      EditText var4 = (EditText)var1.findViewById(R.id.editTextResult_analogOutput);
      var4.setEnabled(true);
      DeviceConfiguration var5 = this.d.get(var3);
      var5.setType(var5.typeFromString(var2));
      var5.setEnabled(true);
      this.a(var4, var5);
   }

   private void b(View var1, DeviceConfiguration var2) {
      EditText var3 = (EditText)var1.findViewById(R.id.editTextResult_analogOutput);
      if(var2.isEnabled()) {
         var3.setText(var2.getName());
         var3.setEnabled(true);
      } else {
         var3.setText("NO DEVICE ATTACHED");
         var3.setEnabled(false);
      }
   }

   public void cancelanalogOutputDevices(View var1) {
      this.setResult(0, new Intent());
      this.finish();
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(R.layout.analog_outputs);
      PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
      this.a = new Utility(this);
      RobotLog.writeLogcatToDisk(this, 1024);
      LinearLayout var2 = (LinearLayout)this.findViewById(R.id.linearLayout_analogOutput0);
      this.b = this.getLayoutInflater().inflate(R.layout.analog_output_device, var2, true);
      ((TextView)this.b.findViewById(R.id.port_number_analogOutput)).setText("0");
      LinearLayout var3 = (LinearLayout)this.findViewById(R.id.linearLayout_analogOutput1);
      this.c = this.getLayoutInflater().inflate(R.layout.analog_output_device, var3, true);
      ((TextView)this.c.findViewById(R.id.port_number_analogOutput)).setText("1");
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
            this.d.add(Integer.parseInt(var6), var7);
         }

         for(int var3 = 0; var3 < this.d.size(); ++var3) {
            View var4 = this.a(var3);
            DeviceConfiguration var5 = this.d.get(var3);
            this.a(var4);
            this.b(var4, var5);
            this.a(var4, var5);
         }
      }

   }

   public void saveanalogOutputDevices(View var1) {
      this.a();
   }

   private class a implements TextWatcher {
      private int b;

      private a(View var2) {
         this.b = Integer.parseInt(((TextView)var2.findViewById(R.id.port_number_analogOutput)).getText().toString());
      }

      // $FF: synthetic method
      a(View var2, Object var3) {
         this(var2);
      }

      public void afterTextChanged(Editable var1) {
         EditAnalogOutputDevicesActivity.this.d.get(this.b).setName(var1.toString());
      }

      public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }

      public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }
   }
}
