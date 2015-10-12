package com.ftdi.j2xx.protocol;

public class SpiSlaveEvent {
   private int a;
   private boolean b;
   private Object c;
   private Object d;
   private Object e;

   public SpiSlaveEvent(int var1, boolean var2, Object var3, Object var4, Object var5) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
      this.e = var5;
   }

   public Object getArg0() {
      return this.c;
   }

   public Object getArg1() {
      return this.d;
   }

   public Object getArg2() {
      return this.e;
   }

   public int getEventType() {
      return this.a;
   }

   public boolean getSync() {
      return this.b;
   }

   public void setArg0(Object var1) {
      this.c = var1;
   }

   public void setArg1(Object var1) {
      this.d = var1;
   }

   public void setArg2(Object var1) {
      this.e = var1;
   }

   public void setEventType(int var1) {
      this.a = var1;
   }

   public void setSync(boolean var1) {
      this.b = var1;
   }
}
