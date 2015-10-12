package com.ftdi.j2xx;

final class b {
   static final byte a(int var0, int[] var1) {
      byte var2 = 1;
      byte var3 = b(var0, var1);
      if(var3 == -1) {
         var2 = -1;
      } else {
         if(var3 == 0) {
            var1[0] = 1 + (-49153 & var1[0]);
         }

         int var4 = a(var1[0], var1[var2]);
         int var5;
         int var6;
         if(var0 > var4) {
            var5 = -100 + var0 * 100 / var4;
            var6 = 100 * (var0 % var4) % var4;
         } else {
            var5 = -100 + var4 * 100 / var0;
            var6 = 100 * (var4 % var0) % var0;
         }

         if(var5 >= 3 && (var5 != 3 || var6 != 0)) {
            return (byte)0;
         }
      }

      return var2;
   }

   static byte a(int var0, int[] var1, boolean var2) {
      byte var3 = 1;
      byte var4 = b(var0, var1, var2);
      if(var4 == -1) {
         var3 = -1;
      } else {
         if(var4 == 0) {
            var1[0] = 1 + (-49153 & var1[0]);
         }

         int var5 = a(var1[0], var1[var3], var2);
         int var6;
         int var7;
         if(var0 > var5) {
            var6 = -100 + var0 * 100 / var5;
            var7 = 100 * (var0 % var5) % var5;
         } else {
            var6 = -100 + var5 * 100 / var0;
            var7 = 100 * (var5 % var0) % var0;
         }

         if(var6 >= 3 && (var6 != 3 || var7 != 0)) {
            return (byte)0;
         }
      }

      return var3;
   }

   private static int a(int var0, int var1) {
      if(var0 == 0) {
         return 12000000;
      } else if(var0 == 1) {
         return 8000000;
      } else {
         int var2 = 100 * (-49153 & var0);
         if(('�' & var1) == 0) {
            switch(var0 & 49152) {
            case 16384:
               var2 += 50;
               break;
            case 32768:
               var2 += 25;
               break;
            case 49152:
               var2 += 12;
            }
         } else {
            switch(var0 & 49152) {
            case 0:
               var2 += 37;
               break;
            case 16384:
               var2 += 62;
               break;
            case 32768:
               var2 += 75;
               break;
            case 49152:
               var2 += 87;
            }
         }

         return 1200000000 / var2;
      }
   }

   private static final int a(int var0, int var1, boolean var2) {
      if(var0 == 0) {
         return 3000000;
      } else {
         int var3 = 100 * (-49153 & var0);
         if(!var2) {
            switch(49152 & var0) {
            case 16384:
               var3 += 50;
               break;
            case 32768:
               var3 += 25;
               break;
            case 49152:
               var3 += 12;
            }
         } else if(var1 == 0) {
            switch(49152 & var0) {
            case 16384:
               var3 += 50;
               break;
            case 32768:
               var3 += 25;
               break;
            case 49152:
               var3 += 12;
            }
         } else {
            switch(49152 & var0) {
            case 0:
               var3 += 37;
               break;
            case 16384:
               var3 += 62;
               break;
            case 32768:
               var3 += 75;
               break;
            case 49152:
               var3 += 87;
            }
         }

         return 300000000 / var3;
      }
   }

   private static byte b(int var0, int[] var1) {
      byte var2 = 1;
      if(var0 == 0) {
         var2 = -1;
      } else {
         if((-16384 & 12000000 / var0) > 0) {
            return (byte)-1;
         }

         var1[var2] = 2;
         if(var0 >= 11640000 && var0 <= 12360000) {
            var1[0] = 0;
            return var2;
         }

         if(var0 >= 7760000 && var0 <= 8240000) {
            var1[0] = var2;
            return var2;
         }

         var1[0] = 12000000 / var0;
         var1[var2] = 2;
         if(var1[0] == var2 && 100 * (12000000 % var0) / var0 <= 3) {
            var1[0] = 0;
         }

         if(var1[0] != 0) {
            int var3 = 100 * (12000000 % var0) / var0;
            char var4;
            if(var3 <= 6) {
               var4 = 0;
            } else if(var3 <= 18) {
               var4 = '쀀';
            } else if(var3 <= 31) {
               var4 = '耀';
            } else if(var3 <= 43) {
               var1[var2] |= 1;
               var4 = 0;
            } else if(var3 <= 56) {
               var4 = 16384;
            } else if(var3 <= 68) {
               var4 = 16384;
               var1[var2] |= 1;
            } else if(var3 <= 81) {
               var4 = '耀';
               var1[var2] |= 1;
            } else if(var3 <= 93) {
               var4 = '쀀';
               var1[var2] |= 1;
            } else {
               var2 = 0;
               var4 = 0;
            }

            var1[0] |= var4;
            return var2;
         }
      }

      return var2;
   }

   private static byte b(int var0, int[] var1, boolean var2) {
      char var3 = '耀';
      byte var4 = 1;
      if(var0 == 0) {
         var4 = -1;
      } else {
         if((-16384 & 3000000 / var0) > 0) {
            return (byte)-1;
         }

         var1[0] = 3000000 / var0;
         var1[var4] = 0;
         if(var1[0] == var4 && 100 * (3000000 % var0) / var0 <= 3) {
            var1[0] = 0;
         }

         if(var1[0] != 0) {
            int var5 = 100 * (3000000 % var0) / var0;
            if(!var2) {
               if(var5 <= 6) {
                  var3 = 0;
               } else if(var5 <= 18) {
                  var3 = '쀀';
               } else if(var5 > 37) {
                  if(var5 <= 75) {
                     var3 = 16384;
                  } else {
                     var4 = 0;
                     var3 = 0;
                  }
               }
            } else if(var5 <= 6) {
               var3 = 0;
            } else if(var5 <= 18) {
               var3 = '쀀';
            } else if(var5 > 31) {
               if(var5 <= 43) {
                  var1[var4] = var4;
                  var3 = 0;
               } else if(var5 <= 56) {
                  var3 = 16384;
               } else if(var5 <= 68) {
                  var1[var4] = var4;
                  var3 = 16384;
               } else if(var5 <= 81) {
                  var1[var4] = var4;
               } else if(var5 <= 93) {
                  var3 = '쀀';
                  var1[var4] = var4;
               } else {
                  var4 = 0;
                  var3 = 0;
               }
            }

            var1[0] |= var3;
            return var4;
         }
      }

      return var4;
   }
}
