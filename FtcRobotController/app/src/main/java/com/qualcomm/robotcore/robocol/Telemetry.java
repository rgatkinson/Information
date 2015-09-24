package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.RobocolParsable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Telemetry implements RobocolParsable {
   public static final String DEFAULT_TAG = "TELEMETRY_DATA";
   private static final Charset a = Charset.forName("UTF-8");
   private final Map<String, String> b = new HashMap();
   private final Map<String, Float> c = new HashMap();
   private String d = "";
   private long e = 0L;

   public Telemetry() {
   }

   public Telemetry(byte[] var1) throws RobotCoreException {
      this.fromByteArray(var1);
   }

   private int a() {
      int var1 = 1 + 0 + 1 + this.d.getBytes(a).length;
      Iterator var2 = this.b.entrySet().iterator();

      int var3;
      Entry var7;
      for(var3 = var1; var2.hasNext(); var3 = var3 + 1 + ((String)var7.getKey()).getBytes(a).length + 1 + ((String)var7.getValue()).getBytes(a).length) {
         var7 = (Entry)var2.next();
      }

      int var4 = var3 + 1;
      Iterator var5 = this.c.entrySet().iterator();

      int var6;
      for(var6 = var4; var5.hasNext(); var6 = 4 + var6 + 1 + ((String)((Entry)var5.next()).getKey()).getBytes(a).length) {
         ;
      }

      return var6;
   }

   public void addData(String var1, double var2) {
      synchronized(this){}

      try {
         this.c.put(var1, Float.valueOf((float)var2));
      } finally {
         ;
      }

   }

   public void addData(String var1, float var2) {
      synchronized(this){}

      try {
         this.c.put(var1, Float.valueOf(var2));
      } finally {
         ;
      }

   }

   public void addData(String var1, Object var2) {
      synchronized(this){}

      try {
         this.b.put(var1, var2.toString());
      } finally {
         ;
      }

   }

   public void addData(String var1, String var2) {
      synchronized(this){}

      try {
         this.b.put(var1, var2);
      } finally {
         ;
      }

   }

   public void clearData() {
      synchronized(this){}

      try {
         this.e = 0L;
         this.b.clear();
         this.c.clear();
      } finally {
         ;
      }

   }

   public void fromByteArray(byte[] param1) throws RobotCoreException {
      // $FF: Couldn't be decompiled
   }

   public Map<String, Float> getDataNumbers() {
      synchronized(this){}

      Map var2;
      try {
         var2 = this.c;
      } finally {
         ;
      }

      return var2;
   }

   public Map<String, String> getDataStrings() {
      synchronized(this){}

      Map var2;
      try {
         var2 = this.b;
      } finally {
         ;
      }

      return var2;
   }

   public RobocolParsable.MsgType getRobocolMsgType() {
      return RobocolParsable.MsgType.TELEMETRY;
   }

   public String getTag() {
      synchronized(this){}
      boolean var4 = false;

      String var2;
      try {
         var4 = true;
         if(this.d.length() != 0) {
            var2 = this.d;
            var4 = false;
            return var2;
         }

         var4 = false;
      } finally {
         if(var4) {
            ;
         }
      }

      var2 = "TELEMETRY_DATA";
      return var2;
   }

   public long getTimestamp() {
      synchronized(this){}

      long var2;
      try {
         var2 = this.e;
      } finally {
         ;
      }

      return var2;
   }

   public boolean hasData() {
      synchronized(this){}
      boolean var5 = false;

      boolean var2;
      label45: {
         boolean var3;
         try {
            var5 = true;
            if(!this.b.isEmpty()) {
               var5 = false;
               break label45;
            }

            var3 = this.c.isEmpty();
            var5 = false;
         } finally {
            if(var5) {
               ;
            }
         }

         if(var3) {
            var2 = false;
            return var2;
         }
      }

      var2 = true;
      return var2;
   }

   public void setTag(String var1) {
      synchronized(this){}

      try {
         this.d = var1;
      } finally {
         ;
      }

   }

   public byte[] toByteArray() throws RobotCoreException {
      // $FF: Couldn't be decompiled
   }
}
