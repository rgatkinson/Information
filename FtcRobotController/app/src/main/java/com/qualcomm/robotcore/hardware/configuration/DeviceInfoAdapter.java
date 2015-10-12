package com.qualcomm.robotcore.hardware.configuration;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.HashMap;
import java.util.Map;

public class DeviceInfoAdapter extends BaseAdapter implements ListAdapter {
   private Map<SerialNumber, ControllerConfiguration> a = new HashMap();
   private SerialNumber[] b;
   private Context c;
   private int d;
   private int e;

   public DeviceInfoAdapter(Activity var1, int var2, Map<SerialNumber, ControllerConfiguration> var3) {
      this.c = var1;
      this.a = var3;
      this.b = (SerialNumber[])var3.keySet().toArray(new SerialNumber[var3.size()]);
      this.d = var2;
      this.e = this.e;
   }

   public int getCount() {
      return this.a.size();
   }

   public Object getItem(int var1) {
      return this.a.get(this.b[var1]);
   }

   public long getItemId(int var1) {
      return 0L;
   }

   public View getView(int var1, View var2, ViewGroup var3) {
      if(var2 == null) {
         var2 = ((Activity)this.c).getLayoutInflater().inflate(this.d, var3, false);
      }

      String var4 = this.b[var1].toString();
      ((TextView)var2.findViewById(16908309)).setText(var4);
      String var5 = ((ControllerConfiguration)this.a.get(this.b[var1])).getName();
      ((TextView)var2.findViewById(16908308)).setText(var5);
      return var2;
   }
}
