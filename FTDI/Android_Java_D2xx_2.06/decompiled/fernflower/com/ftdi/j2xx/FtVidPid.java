package com.ftdi.j2xx;

class FtVidPid {
   private int mVendorId;
   private int mProductId;

   FtVidPid(int vendor, int product) {
      this.mVendorId = vendor;
      this.mProductId = product;
   }

   FtVidPid() {
      this.mVendorId = 0;
      this.mProductId = 0;
   }

   public void setVid(int vid) {
      this.mVendorId = vid;
   }

   public void setPid(int pid) {
      this.mProductId = pid;
   }

   public int getVid() {
      return this.mVendorId;
   }

   public int getPid() {
      return this.mProductId;
   }

   public String toString() {
      return "Vendor: " + String.format("%04x", new Object[]{Integer.valueOf(this.mVendorId)}) + ", Product: " + String.format("%04x", new Object[]{Integer.valueOf(this.mProductId)});
   }

   public boolean equals(Object o) {
      if(this == o) {
         return true;
      } else if(!(o instanceof FtVidPid)) {
         return false;
      } else {
         FtVidPid testObj = (FtVidPid)o;
         return this.mVendorId != testObj.mVendorId?false:this.mProductId == testObj.mProductId;
      }
   }

   public int hashCode() {
      throw new UnsupportedOperationException();
   }
}
