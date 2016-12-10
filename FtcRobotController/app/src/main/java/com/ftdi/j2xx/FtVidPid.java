package com.ftdi.j2xx;

class FtVidPid
   {
   private int mVendorId;
   private int mProductId;

   FtVidPid() {
      this.mVendorId = 0;
      this.mProductId = 0;
   }

   FtVidPid(int var1, int var2) {
      this.mVendorId = var1;
      this.mProductId = var2;
   }

   public int a() {
      return this.mVendorId;
   }

   public int b() {
      return this.mProductId;
   }

   public boolean equals(Object var1) {
      if(this != var1) {
         if(!(var1 instanceof FtVidPid)) {
            return false;
         }

         FtVidPid var2 = (FtVidPid)var1;
         if(this.mVendorId != var2.mVendorId) {
            return false;
         }

         if(this.mProductId != var2.mProductId) {
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
      Object[] var2 = new Object[]{Integer.valueOf(this.mVendorId)};
      StringBuilder var3 = var1.append(String.format("%04x", var2)).append(", Product: ");
      Object[] var4 = new Object[]{Integer.valueOf(this.mProductId)};
      return var3.append(String.format("%04x", var4)).toString();
   }
}
