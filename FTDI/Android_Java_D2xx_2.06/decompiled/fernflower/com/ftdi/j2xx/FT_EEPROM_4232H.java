package com.ftdi.j2xx;

import com.ftdi.j2xx.FT_EEPROM;

public class FT_EEPROM_4232H extends FT_EEPROM {
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
   public boolean AL_RI_TXDEN = false;
   public boolean AH_RI_TXDEN = false;
   public boolean BL_RI_TXDEN = false;
   public boolean BH_RI_TXDEN = false;
   public boolean AL_LoadVCP = false;
   public boolean AL_LoadD2XX = false;
   public boolean AL_LoadRI_RS485 = false;
   public boolean AH_LoadVCP = false;
   public boolean AH_LoadD2XX = false;
   public boolean AH_LoadRI_RS485 = false;
   public boolean BL_LoadVCP = false;
   public boolean BL_LoadD2XX = false;
   public boolean BL_LoadRI_RS485 = false;
   public boolean BH_LoadVCP = false;
   public boolean BH_LoadD2XX = false;
   public boolean BH_LoadRI_RS485 = false;
   public int TPRDRV = 0;

   public static final class DRIVE_STRENGTH {
      static final byte DRIVE_4mA = 0;
      static final byte DRIVE_8mA = 1;
      static final byte DRIVE_12mA = 2;
      static final byte DRIVE_16mA = 3;
   }
}
