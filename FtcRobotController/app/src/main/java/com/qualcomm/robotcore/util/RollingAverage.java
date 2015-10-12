package com.qualcomm.robotcore.util;

import java.util.LinkedList;
import java.util.Queue;

public class RollingAverage {
   public static final int DEFAULT_SIZE = 100;
   private final Queue<Integer> a = new LinkedList();
   private long b;
   private int c;

   public RollingAverage() {
      this.resize(100);
   }

   public RollingAverage(int var1) {
      this.resize(var1);
   }

   public void addNumber(int var1) {
      if(this.a.size() >= this.c) {
         int var3 = ((Integer)this.a.remove()).intValue();
         this.b -= (long)var3;
      }

      this.a.add(Integer.valueOf(var1));
      this.b += (long)var1;
   }

   public int getAverage() {
      return this.a.isEmpty()?0:(int)(this.b / (long)this.a.size());
   }

   public void reset() {
      this.a.clear();
   }

   public void resize(int var1) {
      this.c = var1;
      this.a.clear();
   }

   public int size() {
      return this.c;
   }
}
