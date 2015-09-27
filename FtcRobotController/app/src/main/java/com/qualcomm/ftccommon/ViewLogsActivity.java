package com.qualcomm.ftccommon;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.ScrollView;
import android.widget.TextView;

import com.qualcomm.robotcore.util.RobotLog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

public class ViewLogsActivity extends Activity {
   public static final String FILENAME = "Filename";
   TextView a;
   int b = 300;
   String c = " ";

   private Spannable a(String var1) {
      int var2 = 0;
      SpannableString var3 = new SpannableString(var1);
      String[] var4 = var1.split("\\n");
      int var5 = var4.length;

      for(int var6 = 0; var2 < var5; ++var2) {
         String var7 = var4[var2];
         if(var7.contains("E/RobotCore") || var7.contains("### ERROR: ")) {
            var3.setSpan(new ForegroundColorSpan(-65536), var6, var6 + var7.length(), 33);
         }

         var6 = 1 + var6 + var7.length();
      }

      return var3;
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.setContentView(R.layout.activity_view_logs);
      this.a = (TextView)this.findViewById(R.id.textAdbLogs);
      final ScrollView var2 = (ScrollView)this.findViewById(R.id.scrollView);
      var2.post(new Runnable() {
         public void run() {
            var2.fullScroll(130);
         }
      });
   }

   protected void onStart() {
      super.onStart();
      Serializable var1 = this.getIntent().getSerializableExtra("Filename");
      if(var1 != null) {
         this.c = (String)var1;
      }

      this.runOnUiThread(new Runnable() {
         public void run() {
            try {
               String var2 = ViewLogsActivity.this.readNLines(ViewLogsActivity.this.b);
               Spannable var3 = ViewLogsActivity.this.a(var2);
               ViewLogsActivity.this.a.setText(var3);
            } catch (IOException var4) {
               RobotLog.log(var4.toString());
               ViewLogsActivity.this.a.setText("File not found: " + ViewLogsActivity.this.c);
            }
         }
      });
   }

   public String readNLines(int var1) throws IOException {
      Environment.getExternalStorageDirectory();
      BufferedReader var3 = new BufferedReader(new FileReader(new File(this.c)));
      String[] var4 = new String[var1];
      int var5 = 0;

      while(true) {
         String var6 = var3.readLine();
         if(var6 == null) {
            int var7 = var5 - var1;
            int var8 = 0;
            if(var7 >= 0) {
               var8 = var7;
            }

            String var10 = "";

            String var14;
            for(int var11 = var8; var11 < var5; var10 = var14) {
               String var13 = var4[var11 % var4.length];
               var14 = var10 + var13 + "\n";
               ++var11;
            }

            int var12 = var10.lastIndexOf("--------- beginning");
            return var12 < 0?var10:var10.substring(var12);
         }

         var4[var5 % var4.length] = var6;
         ++var5;
      }
   }
}
