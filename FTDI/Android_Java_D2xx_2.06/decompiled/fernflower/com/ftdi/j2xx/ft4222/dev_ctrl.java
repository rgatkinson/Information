package com.ftdi.j2xx.ft4222;

class dev_ctrl {
   byte ep_in;
   byte ep_out;
   byte[] proc_io;

   public dev_ctrl(char fwVer) {
      if(fwVer < 66) {
         this.proc_io = new byte[3];
      } else {
         this.proc_io = new byte[1];
      }

   }
}
