package com.ftdi.j2xx;

import com.ftdi.j2xx.FT_EEPROM;

public class FT_EEPROM_2232H extends FT_EEPROM {
   public boolean AL_SlowSlew = false;
   public boolean AL_SchmittInput = false;
   public byte AL_DriveCurrent = 0;
   public boolean AH_SlowSlew = false;
   public boolean AH_SchmittInput = false;
   public byte AH_DriveCurrent = 0;
   public boolean BL_SlowSlew = false;
   public boolean BL_SchmittInput = false;
   public byte BL_DriveCurrent = 0;
   public boolean BH_SlowSlew = false;
   public boolean BH_SchmittInput = false;
   public byte BH_DriveCurrent = 0;
   public boolean A_UART = false;
   public boolean B_UART = false;
   public boolean A_FIFO = false;
   public boolean B_FIFO = false;
   public boolean A_FIFOTarget = false;
   public boolean B_FIFOTarget = false;
   public boolean A_FastSerial = false;
   public boolean B_FastSerial = false;
   public boolean PowerSaveEnable = false;
   public boolean A_LoadVCP = false;
   public boolean B_LoadVCP = false;
   public boolean A_LoadD2XX = false;
   public boolean B_LoadD2XX = false;
   public int TPRDRV = 0;

   public static final class DRIVE_STRENGTH {
      static final byte DRIVE_4mA = 0;
      static final byte DRIVE_8mA = 1;
      static final byte DRIVE_12mA = 2;
      static final byte DRIVE_16mA = 3;
   }
}
