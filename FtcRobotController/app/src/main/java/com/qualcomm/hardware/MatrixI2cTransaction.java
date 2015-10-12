package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.RobotLog;

public class MatrixI2cTransaction {
   public byte mode;
   public byte motor;
   public MatrixI2cTransaction.a property;
   public byte servo;
   public byte speed;
   public MatrixI2cTransaction.b state;
   public int target;
   public int value;
   public boolean write;

   MatrixI2cTransaction(byte var1, byte var2, byte var3) {
      this.servo = var1;
      this.speed = var3;
      this.target = var2;
      this.property = MatrixI2cTransaction.a.g;
      this.state = MatrixI2cTransaction.b.a;
      this.write = true;
   }

   MatrixI2cTransaction(byte var1, byte var2, int var3, byte var4) {
      this.motor = var1;
      this.speed = var2;
      this.target = var3;
      this.mode = var4;
      this.property = MatrixI2cTransaction.a.f;
      this.state = MatrixI2cTransaction.b.a;
      this.write = true;
   }

   MatrixI2cTransaction(byte var1, MatrixI2cTransaction.a var2) {
      this.motor = var1;
      this.property = var2;
      this.state = MatrixI2cTransaction.b.a;
      this.write = false;
   }

   MatrixI2cTransaction(byte var1, MatrixI2cTransaction.a var2, int var3) {
      this.motor = var1;
      this.value = var3;
      this.property = var2;
      this.state = MatrixI2cTransaction.b.a;
      this.write = true;
   }

   public boolean isEqual(MatrixI2cTransaction var1) {
      boolean var2 = true;
      if(this.property != var1.property) {
         var2 = false;
      } else {
         switch(null.a[this.property.ordinal()]) {
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
            if(this.write == var1.write && this.motor == var1.motor && this.value == var1.value) {
               break;
            }

            return false;
         case 8:
            if(this.write == var1.write && this.motor == var1.motor && this.speed == var1.speed && this.target == var1.target && this.mode == var1.mode) {
               break;
            }

            return false;
         case 9:
            if(this.write == var1.write && this.servo == var1.servo && this.speed == var1.speed && this.target == var1.target) {
               break;
            }

            return false;
         case 10:
            if(this.write == var1.write && this.value == var1.value) {
               break;
            }

            return false;
         default:
            RobotLog.e("Can not compare against unknown transaction property " + var1.toString());
            return false;
         }
      }

      return var2;
   }

   public String toString() {
      return this.property == MatrixI2cTransaction.a.f?"Matrix motor transaction: " + this.property + " motor " + this.motor + " write " + this.write + " speed " + this.speed + " target " + this.target + " mode " + this.mode:(this.property == MatrixI2cTransaction.a.g?"Matrix servo transaction: " + this.property + " servo " + this.servo + " write " + this.write + " change rate " + this.speed + " target " + this.target:(this.property == MatrixI2cTransaction.a.h?"Matrix servo transaction: " + this.property + " servo " + this.servo + " write " + this.write + " value " + this.value:"Matrix motor transaction: " + this.property + " motor " + this.motor + " write " + this.write + " value " + this.value));
   }

   static enum a {
      a,
      b,
      c,
      d,
      e,
      f,
      g,
      h,
      i,
      j;

      static {
         MatrixI2cTransaction.a[] var0 = new MatrixI2cTransaction.a[]{a, b, c, d, e, f, g, h, i, j};
      }
   }

   static enum b {
      a,
      b,
      c,
      d,
      e;

      static {
         MatrixI2cTransaction.b[] var0 = new MatrixI2cTransaction.b[]{a, b, c, d, e};
      }
   }
}
