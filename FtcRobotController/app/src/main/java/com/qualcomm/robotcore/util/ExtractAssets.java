package com.qualcomm.robotcore.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ExtractAssets {
   private static final String a = ExtractAssets.class.getSimpleName();

   public static ArrayList<String> ExtractToStorage(Context var0, ArrayList<String> var1, boolean var2) throws IOException {
      if(!var2 && !"mounted".equals(Environment.getExternalStorageState())) {
         throw new IOException("External Storage not accessible");
      } else {
         ArrayList var3 = new ArrayList();
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            a(var0, (String)var4.next(), var2, var3);
            if(var3 != null) {
               Log.d(a, "got " + var3.size() + " elements");
            }
         }

         return var3;
      }
   }

   private static ArrayList<String> a(Context param0, String param1, boolean param2, ArrayList<String> param3) {
      // $FF: Couldn't be decompiled
   }
}
