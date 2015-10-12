package com.qualcomm.hardware;

import com.qualcomm.hardware.MatrixDcMotorController;
import com.qualcomm.hardware.MatrixI2cTransaction;
import com.qualcomm.hardware.MatrixServoController;
import com.qualcomm.hardware.ModernRoboticsUsbLegacyModule;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MatrixMasterController implements I2cController.I2cPortReadyCallback {
   private static final byte[] a = new byte[]{(byte)0, (byte)70, (byte)72, (byte)74, (byte)76};
   private static final byte[] b = new byte[]{(byte)0, (byte)78, (byte)88, (byte)98, (byte)108};
   private static final byte[] c = new byte[]{(byte)0, (byte)82, (byte)92, (byte)102, (byte)112};
   private static final byte[] d = new byte[]{(byte)0, (byte)86, (byte)96, (byte)106, (byte)116};
   private static final byte[] e = new byte[]{(byte)0, (byte)87, (byte)97, (byte)107, (byte)117};
   private volatile boolean f = false;
   private final ElapsedTime g = new ElapsedTime(0L);
   protected ModernRoboticsUsbLegacyModule legacyModule;
   protected MatrixDcMotorController motorController;
   protected int physicalPort;
   protected MatrixServoController servoController;
   protected ConcurrentLinkedQueue<MatrixI2cTransaction> transactionQueue;

   public MatrixMasterController(ModernRoboticsUsbLegacyModule var1, int var2) {
      this.legacyModule = var1;
      this.physicalPort = var2;
      this.transactionQueue = new ConcurrentLinkedQueue();
      var1.registerForI2cPortReadyCallback(this, var2);
   }

   protected void buginf(String var1) {
   }

   public String getConnectionInfo() {
      return this.legacyModule.getConnectionInfo() + "; port " + this.physicalPort;
   }

   public int getPort() {
      return this.physicalPort;
   }

   protected void handleReadDone(MatrixI2cTransaction param1) {
      // $FF: Couldn't be decompiled
   }

   public void portIsReady(int param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean queueTransaction(MatrixI2cTransaction var1) {
      return this.queueTransaction(var1, false);
   }

   public boolean queueTransaction(MatrixI2cTransaction var1, boolean var2) {
      if(!var2) {
         Iterator var4 = this.transactionQueue.iterator();

         while(var4.hasNext()) {
            if(((MatrixI2cTransaction)var4.next()).isEqual(var1)) {
               this.buginf("NO Queue transaction " + var1.toString());
               return false;
            }
         }
      }

      this.buginf("YES Queue transaction " + var1.toString());
      this.transactionQueue.add(var1);
      return true;
   }

   public void registerMotorController(MatrixDcMotorController var1) {
      this.motorController = var1;
   }

   public void registerServoController(MatrixServoController var1) {
      this.servoController = var1;
   }

   protected void sendHeartbeat() {
      this.queueTransaction(new MatrixI2cTransaction((byte)0, MatrixI2cTransaction.a.j, 3));
   }

   public void waitOnRead() {
      // $FF: Couldn't be decompiled
   }
}
