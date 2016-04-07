package com.ftdi.j2xx;

final class FT_BaudRate {
   private static final int FT_CLOCK_RATE = 3000000;
   private static final int FT_CLOCK_RATE_HI = 12000000;
   private static final int FT_SUB_INT_0_0 = 0;
   private static final int FT_SUB_INT_0_125 = 49152;
   private static final int FT_SUB_INT_0_25 = 32768;
   private static final int FT_SUB_INT_0_5 = 16384;
   private static final int FT_SUB_INT_MASK = 49152;
   private static final int FT_SUB_INT_0_375 = 0;
   private static final int FT_SUB_INT_0_625 = 16384;
   private static final int FT_SUB_INT_0_75 = 32768;
   private static final int FT_SUB_INT_0_875 = 49152;

   static byte FT_GetDivisor(int rate, int[] divisors, boolean bm) {
      byte rval;
      if((rval = FT_CalcDivisor(rate, divisors, bm)) == -1) {
         return (byte)-1;
      } else {
         if(rval == 0) {
            divisors[0] = (divisors[0] & -49153) + 1;
         }

         int temp_actual = FT_CalcBaudRate(divisors[0], divisors[1], bm);
         int temp_accuracy;
         int temp_mod;
         if(rate > temp_actual) {
            temp_accuracy = rate * 100 / temp_actual - 100;
            temp_mod = rate % temp_actual * 100 % temp_actual;
         } else {
            temp_accuracy = temp_actual * 100 / rate - 100;
            temp_mod = temp_actual % rate * 100 % rate;
         }

         byte rval1;
         if(temp_accuracy < 3) {
            rval1 = 1;
         } else if(temp_accuracy == 3 && temp_mod == 0) {
            rval1 = 1;
         } else {
            rval1 = 0;
         }

         return rval1;
      }
   }

   private static byte FT_CalcDivisor(int rate, int[] divisors, boolean bm) {
      byte ok = 1;
      if(rate == 0) {
         return (byte)-1;
      } else if((3000000 / rate & -16384) > 0) {
         return (byte)-1;
      } else {
         divisors[0] = 3000000 / rate;
         divisors[1] = 0;
         int t;
         if(divisors[0] == 1) {
            t = 3000000 % rate * 100 / rate;
            if(t <= 3) {
               divisors[0] = 0;
            }
         }

         if(divisors[0] == 0) {
            return ok;
         } else {
            t = 3000000 % rate * 100 / rate;
            char modifier;
            if(!bm) {
               if(t <= 6) {
                  modifier = 0;
               } else if(t <= 18) {
                  modifier = '쀀';
               } else if(t <= 37) {
                  modifier = '耀';
               } else if(t <= 75) {
                  modifier = 16384;
               } else {
                  modifier = 0;
                  ok = 0;
               }
            } else if(t <= 6) {
               modifier = 0;
            } else if(t <= 18) {
               modifier = '쀀';
            } else if(t <= 31) {
               modifier = '耀';
            } else if(t <= 43) {
               modifier = 0;
               divisors[1] = 1;
            } else if(t <= 56) {
               modifier = 16384;
            } else if(t <= 68) {
               modifier = 16384;
               divisors[1] = 1;
            } else if(t <= 81) {
               modifier = '耀';
               divisors[1] = 1;
            } else if(t <= 93) {
               modifier = '쀀';
               divisors[1] = 1;
            } else {
               modifier = 0;
               ok = 0;
            }

            divisors[0] |= modifier;
            return ok;
         }
      }
   }

   private static final int FT_CalcBaudRate(int divisor, int extdiv, boolean bm) {
      if(divisor == 0) {
         return 3000000;
      } else {
         int rate = (divisor & -49153) * 100;
         if(!bm) {
            switch(divisor & 49152) {
            case 16384:
               rate += 50;
               break;
            case 32768:
               rate += 25;
               break;
            case 49152:
               rate += 12;
            }
         } else if(extdiv == 0) {
            switch(divisor & 49152) {
            case 16384:
               rate += 50;
               break;
            case 32768:
               rate += 25;
               break;
            case 49152:
               rate += 12;
            }
         } else {
            switch(divisor & 49152) {
            case 0:
               rate += 37;
               break;
            case 16384:
               rate += 62;
               break;
            case 32768:
               rate += 75;
               break;
            case 49152:
               rate += 87;
            }
         }

         rate = 300000000 / rate;
         return rate;
      }
   }

   static final byte FT_GetDivisorHi(int rate, int[] divisors) {
      byte rval;
      if((rval = FT_CalcDivisorHi(rate, divisors)) == -1) {
         return (byte)-1;
      } else {
         if(rval == 0) {
            divisors[0] = (divisors[0] & -49153) + 1;
         }

         int temp_actual = FT_CalcBaudRateHi(divisors[0], divisors[1]);
         int temp_accuracy;
         int temp_mod;
         if(rate > temp_actual) {
            temp_accuracy = rate * 100 / temp_actual - 100;
            temp_mod = rate % temp_actual * 100 % temp_actual;
         } else {
            temp_accuracy = temp_actual * 100 / rate - 100;
            temp_mod = temp_actual % rate * 100 % rate;
         }

         byte rval1;
         if(temp_accuracy < 3) {
            rval1 = 1;
         } else if(temp_accuracy == 3 && temp_mod == 0) {
            rval1 = 1;
         } else {
            rval1 = 0;
         }

         return rval1;
      }
   }

   private static byte FT_CalcDivisorHi(int rate, int[] divisors) {
      byte ok = 1;
      if(rate == 0) {
         return (byte)-1;
      } else if((12000000 / rate & -16384) > 0) {
         return (byte)-1;
      } else {
         divisors[1] = 2;
         if(rate >= 11640000 && rate <= 12360000) {
            divisors[0] = 0;
            return ok;
         } else if(rate >= 7760000 && rate <= 8240000) {
            divisors[0] = 1;
            return ok;
         } else {
            divisors[0] = 12000000 / rate;
            divisors[1] = 2;
            int t;
            if(divisors[0] == 1) {
               t = 12000000 % rate * 100 / rate;
               if(t <= 3) {
                  divisors[0] = 0;
               }
            }

            if(divisors[0] == 0) {
               return ok;
            } else {
               t = 12000000 % rate * 100 / rate;
               char modifier;
               if(t <= 6) {
                  modifier = 0;
               } else if(t <= 18) {
                  modifier = '쀀';
               } else if(t <= 31) {
                  modifier = '耀';
               } else if(t <= 43) {
                  modifier = 0;
                  divisors[1] |= 1;
               } else if(t <= 56) {
                  modifier = 16384;
               } else if(t <= 68) {
                  modifier = 16384;
                  divisors[1] |= 1;
               } else if(t <= 81) {
                  modifier = '耀';
                  divisors[1] |= 1;
               } else if(t <= 93) {
                  modifier = '쀀';
                  divisors[1] |= 1;
               } else {
                  modifier = 0;
                  ok = 0;
               }

               divisors[0] |= modifier;
               return ok;
            }
         }
      }
   }

   private static int FT_CalcBaudRateHi(int divisor, int extdiv) {
      if(divisor == 0) {
         return 12000000;
      } else if(divisor == 1) {
         return 8000000;
      } else {
         int rate = (divisor & -49153) * 100;
         extdiv &= '�';
         if(extdiv == 0) {
            switch(divisor & 49152) {
            case 16384:
               rate += 50;
               break;
            case 32768:
               rate += 25;
               break;
            case 49152:
               rate += 12;
            }
         } else {
            switch(divisor & 49152) {
            case 0:
               rate += 37;
               break;
            case 16384:
               rate += 62;
               break;
            case 32768:
               rate += 75;
               break;
            case 49152:
               rate += 87;
            }
         }

         rate = 1200000000 / rate;
         return rate;
      }
   }
}
