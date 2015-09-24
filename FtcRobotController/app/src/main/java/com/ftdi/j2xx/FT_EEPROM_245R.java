package com.ftdi.j2xx;

import com.ftdi.j2xx.FT_EEPROM;

public class FT_EEPROM_245R extends FT_EEPROM {
   public byte CBus0 = 0;
   public byte CBus1 = 0;
   public byte CBus2 = 0;
   public byte CBus3 = 0;
   public byte CBus4 = 0;
   public boolean ExternalOscillator = false;
   public boolean HighIO = false;
   public boolean InvertCTS = false;
   public boolean InvertDCD = false;
   public boolean InvertDSR = false;
   public boolean InvertDTR = false;
   public boolean InvertRI = false;
   public boolean InvertRTS = false;
   public boolean InvertRXD = false;
   public boolean InvertTXD = false;
   public boolean LoadVCP = false;

   public static final class CBUS {
   }
}
