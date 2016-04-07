package com.ftdi.j2xx;

import com.ftdi.j2xx.FT_EEPROM;

public class FT_EEPROM_232R extends FT_EEPROM {
   public boolean HighIO = false;
   public boolean ExternalOscillator = false;
   public boolean InvertTXD = false;
   public boolean InvertRXD = false;
   public boolean InvertRTS = false;
   public boolean InvertCTS = false;
   public boolean InvertDTR = false;
   public boolean InvertDSR = false;
   public boolean InvertDCD = false;
   public boolean InvertRI = false;
   public byte CBus0 = 0;
   public byte CBus1 = 0;
   public byte CBus2 = 0;
   public byte CBus3 = 0;
   public byte CBus4 = 0;
   public boolean LoadVCP = false;

   public static final class CBUS {
      static final int TXDEN = 0;
      static final int PWRON = 1;
      static final int RXLED = 2;
      static final int TXLED = 3;
      static final int TXRXLED = 4;
      static final int SLEEP = 5;
      static final int CLK48MHz = 6;
      static final int CLK24MHz = 7;
      static final int CLK12MHz = 8;
      static final int CLK6MHz = 9;
      static final int IO_MODE = 10;
      static final int BIT_BANG_WR = 11;
      static final int BIT_BANG_RD = 12;
   }
}
