package com.ftdi.j2xx.ft4222;

import com.ftdi.j2xx.ft4222.dev_ctrl;

class gpio_dev {
   dev_ctrl usb;
   byte mask;
   byte dir;
   byte[] dat;

   public gpio_dev(char fwVer) {
      this.usb = new dev_ctrl(fwVer);
      this.dat = new byte[1];
   }
}
