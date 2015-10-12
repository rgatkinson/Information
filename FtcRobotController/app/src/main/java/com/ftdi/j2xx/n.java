package com.ftdi.j2xx;

import java.nio.ByteBuffer;

class n {
   private int a;
   private ByteBuffer b;
   private int c;
   private boolean d;

   public n(int var1) {
      this.b = ByteBuffer.allocate(var1);
      this.b(0);
   }

   ByteBuffer a() {
      return this.b;
   }

   void a(int var1) {
      this.a = var1;
   }

   int b() {
      return this.c;
   }

   void b(int var1) {
      this.c = var1;
   }

   ByteBuffer c(int param1) {
      // $FF: Couldn't be decompiled
   }

   void c() {
      synchronized(this){}

      try {
         this.b.clear();
         this.b(0);
      } finally {
         ;
      }

   }

   boolean d() {
      synchronized(this){}

      boolean var2;
      try {
         var2 = this.d;
      } finally {
         ;
      }

      return var2;
   }

   boolean d(int param1) {
      // $FF: Couldn't be decompiled
   }
}
