package com.ftdi.j2xx;

class USBVendorAndProduct
   {
   private int vendor;
   private int product;

   USBVendorAndProduct() {
      this.vendor = 0;
      this.product = 0;
   }

   USBVendorAndProduct(int var1, int var2) {
      this.vendor = var1;
      this.product = var2;
   }

   public int getVendor() {
      return this.vendor;
   }

   public int getProduct() {
      return this.product;
   }

   public boolean equals(Object him) {
      if(this != him) {
         if(!(him instanceof USBVendorAndProduct)) {
            return false;
         }

         USBVendorAndProduct var2 = (USBVendorAndProduct)him;
         if(this.vendor != var2.vendor) {
            return false;
         }

         if(this.product != var2.product) {
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
      Object[] var2 = new Object[]{Integer.valueOf(this.vendor)};
      StringBuilder var3 = var1.append(String.format("%04x", var2)).append(", Product: ");
      Object[] var4 = new Object[]{Integer.valueOf(this.product)};
      return var3.append(String.format("%04x", var4)).toString();
   }
}
