package com.qualcomm.robotcore.exception;

public class RobotCoreException extends Exception {
   private Exception a = null;

   public RobotCoreException(String var1) {
      super(var1);
   }

   public RobotCoreException(String var1, Exception var2) {
      super(var1);
      this.a = var2;
   }

   public Exception getChainedException() {
      return this.a;
   }

   public boolean isChainedException() {
      return this.a != null;
   }
}
