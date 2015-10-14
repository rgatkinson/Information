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
import com.qualcomm.robotcore.hardware.configuration.MatrixControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.util.RobotLog;

import java.io.Serializable;
import java.util.ArrayList;

public class EditMatrixControllerActivity extends Activity {
   public static final String EDIT_MATRIX_ACTIVITY = "Edit Matrix ControllerConfiguration Activity";
   private Utility a;
   private MatrixControllerConfiguration b;
   private ArrayList<DeviceConfiguration> c;
   private ArrayList<DeviceConfiguration> d;
   private EditText e;
   private View f;
   private View g;
   private View h;
   private View i;
   private View j;
   private View k;
   private View l;
   private View m;

   private View a(int var1) {
      switch(var1) {
      case 1:
         return this.f;
      case 2:
         return this.g;
      case 3:
         return this.h;
      case 4:
         return this.i;
      default:
         return null;
      }
   }

   private void a() {
      Intent var1 = new Intent();
      this.b.addServos(this.d);
      this.b.addMotors(this.c);
      this.b.setName(this.e.getText().toString());
      var1.putExtra("Edit Matrix ControllerConfiguration Activity", this.b);
      this.setResult(-1, var1);
      this.finish();
   }

   private void a(int var1, View var2, ArrayList<DeviceConfiguration> var3) {
      CheckBox var4 = (CheckBox)var2.findViewById(R.id.checkbox_port_matrix);
      DeviceConfiguration var5 = var3.get(var1 - 1);
      if(var5.isEnabled()) {
         var4.setChecked(true);
         ((EditText)var2.findViewById(R.id.editTextResult_matrix)).setText(var5.getName());
      } else {
         var4.setChecked(true);
         var4.performClick();
      }
   }

   private void a(View var1, DeviceConfiguration var2) {
      ((EditText)var1.findViewById(R.id.editTextResult_matrix)).addTextChangedListener(new a(var2, null));
   }

   private View b(int var1) {
      switch(var1) {
      case 1:
         return this.j;
      case 2:
         return this.k;
      case 3:
         return this.l;
      case 4:
         return this.m;
      default:
         return null;
      }
   }

   private void b(int var1, View var2, ArrayList<DeviceConfiguration> var3) {
      final EditText var4 = (EditText)var2.findViewById(R.id.editTextResult_matrix);
      final DeviceConfiguration var5 = var3.get(var1 - 1);
      var2.findViewById(R.id.checkbox_port_matrix).setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            if (((CheckBox) var1).isChecked()) {
               var4.setEnabled(true);
               var4.setText("");
               var5.setEnabled(true);
               var5.setName("");
            } else {
               var4.setEnabled(false);
               var5.setEnabled(false);
               var5.setName("NO DEVICE ATTACHED");
               var4.setText("NO DEVICE ATTACHED");
            }
         }
      });
   }

   public void cancelMatrixController(View var1) {
      this.setResult(0, new Intent());
      this.finish();
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(R.layout.matrices);
      PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
      this.a = new Utility(this);
      RobotLog.writeLogcatToDisk(this, 1024);
      this.e = (EditText)this.findViewById(R.id.matrixcontroller_name);
      LinearLayout var2 = (LinearLayout)this.findViewById(R.id.linearLayout_matrix1);
      this.f = this.getLayoutInflater().inflate(R.layout.matrix_devices, var2, true);
      ((TextView)this.f.findViewById(R.id.port_number_matrix)).setText("1");
      LinearLayout var3 = (LinearLayout)this.findViewById(R.id.linearLayout_matrix2);
      this.g = this.getLayoutInflater().inflate(R.layout.matrix_devices, var3, true);
      ((TextView)this.g.findViewById(R.id.port_number_matrix)).setText("2");
      LinearLayout var4 = (LinearLayout)this.findViewById(R.id.linearLayout_matrix3);
      this.h = this.getLayoutInflater().inflate(R.layout.matrix_devices, var4, true);
      ((TextView)this.h.findViewById(R.id.port_number_matrix)).setText("3");
      LinearLayout var5 = (LinearLayout)this.findViewById(R.id.linearLayout_matrix4);
      this.i = this.getLayoutInflater().inflate(R.layout.matrix_devices, var5, true);
      ((TextView)this.i.findViewById(R.id.port_number_matrix)).setText("4");
      LinearLayout var6 = (LinearLayout)this.findViewById(R.id.linearLayout_matrix5);
      this.j = this.getLayoutInflater().inflate(R.layout.matrix_devices, var6, true);
      ((TextView)this.j.findViewById(R.id.port_number_matrix)).setText("1");
      LinearLayout var7 = (LinearLayout)this.findViewById(R.id.linearLayout_matrix6);
      this.k = this.getLayoutInflater().inflate(R.layout.matrix_devices, var7, true);
      ((TextView)this.k.findViewById(R.id.port_number_matrix)).setText("2");
      LinearLayout var8 = (LinearLayout)this.findViewById(R.id.linearLayout_matrix7);
      this.l = this.getLayoutInflater().inflate(R.layout.matrix_devices, var8, true);
      ((TextView)this.l.findViewById(R.id.port_number_matrix)).setText("3");
      LinearLayout var9 = (LinearLayout)this.findViewById(R.id.linearLayout_matrix8);
      this.m = this.getLayoutInflater().inflate(R.layout.matrix_devices, var9, true);
      ((TextView)this.m.findViewById(R.id.port_number_matrix)).setText("4");
   }

   protected void onStart() {
      super.onStart();
      this.a.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
      Serializable var1 = this.getIntent().getSerializableExtra("Edit Matrix ControllerConfiguration Activity");
      if(var1 != null) {
         this.b = (MatrixControllerConfiguration)var1;
         this.c = (ArrayList)this.b.getMotors();
         this.d = (ArrayList)this.b.getServos();
      }

      this.e.setText(this.b.getName());
      int var2 = 0;

      while(true) {
         int var3 = this.c.size();
         int var4 = 0;
         if(var2 >= var3) {
            while(var4 < this.d.size()) {
               View var5 = this.a(var4 + 1);
               this.b(var4 + 1, var5, this.d);
               this.a(var5, this.d.get(var4));
               this.a(var4 + 1, var5, this.d);
               ++var4;
            }

            return;
         }

         View var6 = this.b(var2 + 1);
         this.b(var2 + 1, var6, this.c);
         this.a(var6, this.c.get(var2));
         this.a(var2 + 1, var6, this.c);
         ++var2;
      }
   }

   public void saveMatrixController(View var1) {
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
