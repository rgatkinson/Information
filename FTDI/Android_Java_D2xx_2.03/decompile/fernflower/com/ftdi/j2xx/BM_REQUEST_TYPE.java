package com.ftdi.j2xx;

class BM_REQUEST_TYPE {
   static final byte HOST_TO_DEVICE = 0;
   static final byte DEVICE_TO_HOST = -128;
   static final byte STANDARD = 0;
   static final byte CLASS = 32;
   static final byte VENDOR = 64;
   static final byte RESERVED = 96;
   static final byte DEVICE = 0;
   static final byte INTERFACE = 1;
   static final byte ENDPOINT = 2;
   static final byte OTHER = 3;
}
