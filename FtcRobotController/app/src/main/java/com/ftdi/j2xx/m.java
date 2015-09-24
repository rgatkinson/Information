package com.ftdi.j2xx;

class m {
   private int a;
   private int b;

   m() {
      this.a = 0;
      this.b = 0;
   }

   m(int var1, int var2) {
      this.a = var1;
      this.b = var2;
   }

   public int a() {
      return this.a;
   }

   public int b() {
      return this.b;
   }

   public boolean equals(Object var1) {
      if(this != var1) {
         if(!(var1 instanceof m)) {
            return false;
         }

         m var2 = (m)var1;
         if(this.a != var2.a) {
            return false;
         }

         if(this.b != var2.b) {
            return false;
         }
      }

      return true;
   }

   public int hashCode() {
      throw new UnsupportedOperationException();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("Vendor: ");
      Object[] var2 = new Object[]{Integer.valueOf(this.a)};
      StringBuilder var3 = var1.append(String.format("%04x", var2)).append(", Product: ");
      Object[] var4 = new Object[]{Integer.valueOf(this.b)};
      return var3.append(String.format("%04x", var4)).toString();
   }
}
