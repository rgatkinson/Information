package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class PushBotHardware extends OpMode {
   private DcMotor v_motor_left_arm;
   private DcMotor v_motor_left_drive;
   private DcMotor v_motor_right_drive;
   private Servo v_servo_left_hand;
   private Servo v_servo_right_hand;
   private boolean v_warning_generated = false;
   private String v_warning_message;

   double a_hand_position() {
      double var1 = 0.0D;
      if(this.v_servo_left_hand != null) {
         var1 = this.v_servo_left_hand.getPosition();
      }

      return var1;
   }

   double a_left_arm_power() {
      double var1 = 0.0D;
      if(this.v_motor_left_arm != null) {
         var1 = this.v_motor_left_arm.getPower();
      }

      return var1;
   }

   double a_left_drive_power() {
      double var1 = 0.0D;
      if(this.v_motor_left_drive != null) {
         var1 = this.v_motor_left_drive.getPower();
      }

      return var1;
   }

   int a_left_encoder_count() {
      DcMotor var1 = this.v_motor_left_drive;
      int var2 = 0;
      if(var1 != null) {
         var2 = this.v_motor_left_drive.getCurrentPosition();
      }

      return var2;
   }

   double a_right_drive_power() {
      double var1 = 0.0D;
      if(this.v_motor_right_drive != null) {
         var1 = this.v_motor_right_drive.getPower();
      }

      return var1;
   }

   int a_right_encoder_count() {
      DcMotor var1 = this.v_motor_right_drive;
      int var2 = 0;
      if(var1 != null) {
         var2 = this.v_motor_right_drive.getCurrentPosition();
      }

      return var2;
   }

   boolean a_warning_generated() {
      return this.v_warning_generated;
   }

   String a_warning_message() {
      return this.v_warning_message;
   }

   boolean drive_using_encoders(double var1, double var3, double var5, double var7) {
      this.run_using_encoders();
      this.set_drive_power(var1, var3);
      boolean var9 = this.have_drive_encoders_reached(var5, var7);
      boolean var10 = false;
      if(var9) {
         this.reset_drive_encoders();
         this.set_drive_power(0.0D, 0.0D);
         var10 = true;
      }

      return var10;
   }

   boolean has_left_drive_encoder_reached(double var1) {
      DcMotor var3 = this.v_motor_left_drive;
      boolean var4 = false;
      if(var3 != null) {
         double var6;
         int var5 = (var6 = (double)Math.abs(this.v_motor_left_drive.getCurrentPosition()) - var1) == 0.0D?0:(var6 < 0.0D?-1:1);
         var4 = false;
         if(var5 > 0) {
            var4 = true;
         }
      }

      return var4;
   }

   boolean has_left_drive_encoder_reset() {
      int var1 = this.a_left_encoder_count();
      boolean var2 = false;
      if(var1 == 0) {
         var2 = true;
      }

      return var2;
   }

   boolean has_right_drive_encoder_reached(double var1) {
      DcMotor var3 = this.v_motor_right_drive;
      boolean var4 = false;
      if(var3 != null) {
         double var6;
         int var5 = (var6 = (double)Math.abs(this.v_motor_right_drive.getCurrentPosition()) - var1) == 0.0D?0:(var6 < 0.0D?-1:1);
         var4 = false;
         if(var5 > 0) {
            var4 = true;
         }
      }

      return var4;
   }

   boolean has_right_drive_encoder_reset() {
      int var1 = this.a_right_encoder_count();
      boolean var2 = false;
      if(var1 == 0) {
         var2 = true;
      }

      return var2;
   }

   boolean have_drive_encoders_reached(double var1, double var3) {
      boolean var5 = this.has_left_drive_encoder_reached(var1);
      boolean var6 = false;
      if(var5) {
         boolean var7 = this.has_right_drive_encoder_reached(var3);
         var6 = false;
         if(var7) {
            var6 = true;
         }
      }

      return var6;
   }

   boolean have_drive_encoders_reset() {
      boolean var1 = this.has_left_drive_encoder_reset();
      boolean var2 = false;
      if(var1) {
         boolean var3 = this.has_right_drive_encoder_reset();
         var2 = false;
         if(var3) {
            var2 = true;
         }
      }

      return var2;
   }

   public void init() {
      this.v_warning_generated = false;
      this.v_warning_message = "Can\'t map; ";

      try {
         this.v_motor_left_drive = (DcMotor)this.hardwareMap.dcMotor.get("left_drive");
      } catch (Exception var10) {
         this.m_warning_message("left_drive");
         DbgLog.msg(var10.getLocalizedMessage());
         this.v_motor_left_drive = null;
      }

      try {
         this.v_motor_right_drive = (DcMotor)this.hardwareMap.dcMotor.get("right_drive");
         this.v_motor_right_drive.setDirection(DcMotor.Direction.REVERSE);
      } catch (Exception var9) {
         this.m_warning_message("right_drive");
         DbgLog.msg(var9.getLocalizedMessage());
         this.v_motor_right_drive = null;
      }

      try {
         this.v_motor_left_arm = (DcMotor)this.hardwareMap.dcMotor.get("left_arm");
      } catch (Exception var8) {
         this.m_warning_message("left_arm");
         DbgLog.msg(var8.getLocalizedMessage());
         this.v_motor_left_arm = null;
      }

      try {
         this.v_servo_left_hand = (Servo)this.hardwareMap.servo.get("left_hand");
         this.v_servo_left_hand.setPosition(0.5D);
      } catch (Exception var7) {
         this.m_warning_message("left_hand");
         DbgLog.msg(var7.getLocalizedMessage());
         this.v_servo_left_hand = null;
      }

      try {
         this.v_servo_right_hand = (Servo)this.hardwareMap.servo.get("right_hand");
         this.v_servo_right_hand.setPosition(0.5D);
      } catch (Exception var6) {
         this.m_warning_message("right_hand");
         DbgLog.msg(var6.getLocalizedMessage());
         this.v_servo_right_hand = null;
      }
   }

   public void loop() {
   }

   void m_hand_position(double var1) {
      double var3 = Range.clip(var1, 0.0D, 1.0D);
      if(this.v_servo_left_hand != null) {
         this.v_servo_left_hand.setPosition(var3);
      }

      if(this.v_servo_right_hand != null) {
         this.v_servo_right_hand.setPosition(1.0D - var3);
      }

   }

   void m_left_arm_power(double var1) {
      if(this.v_motor_left_arm != null) {
         this.v_motor_left_arm.setPower(var1);
      }

   }

   void m_warning_message(String var1) {
      if(this.v_warning_generated) {
         this.v_warning_message = this.v_warning_message + ", ";
      }

      this.v_warning_generated = true;
      this.v_warning_message = this.v_warning_message + var1;
   }

   void open_hand() {
      if(this.v_servo_left_hand != null) {
         this.v_servo_left_hand.setPosition(1.0D);
      }

      if(this.v_servo_right_hand != null) {
         this.v_servo_right_hand.setPosition(0.0D);
      }

   }

   public void reset_drive_encoders() {
      this.reset_left_drive_encoder();
      this.reset_right_drive_encoder();
   }

   public void reset_left_drive_encoder() {
      if(this.v_motor_left_drive != null) {
         this.v_motor_left_drive.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
      }

   }

   public void reset_right_drive_encoder() {
      if(this.v_motor_right_drive != null) {
         this.v_motor_right_drive.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
      }

   }

   public void run_using_encoders() {
      this.run_using_left_drive_encoder();
      this.run_using_right_drive_encoder();
   }

   public void run_using_left_drive_encoder() {
      if(this.v_motor_left_drive != null) {
         this.v_motor_left_drive.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
      }

   }

   public void run_using_right_drive_encoder() {
      if(this.v_motor_right_drive != null) {
         this.v_motor_right_drive.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
      }

   }

   public void run_without_drive_encoders() {
      this.run_without_left_drive_encoder();
      this.run_without_right_drive_encoder();
   }

   public void run_without_left_drive_encoder() {
      if(this.v_motor_left_drive != null && this.v_motor_left_drive.getChannelMode() == DcMotorController.RunMode.RESET_ENCODERS) {
         this.v_motor_left_drive.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
      }

   }

   public void run_without_right_drive_encoder() {
      if(this.v_motor_right_drive != null && this.v_motor_right_drive.getChannelMode() == DcMotorController.RunMode.RESET_ENCODERS) {
         this.v_motor_right_drive.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
      }

   }

   float scale_motor_power(float var1) {
      float var2 = Range.clip(var1, -1.0F, 1.0F);
      float[] var3 = new float[]{0.0F, 0.05F, 0.09F, 0.1F, 0.12F, 0.15F, 0.18F, 0.24F, 0.3F, 0.36F, 0.43F, 0.5F, 0.6F, 0.72F, 0.85F, 1.0F, 1.0F};
      int var4 = (int)(16.0D * (double)var2);
      if(var4 < 0) {
         var4 = -var4;
      } else if(var4 > 16) {
         var4 = 16;
      }

      return var2 < 0.0F?-var3[var4]:var3[var4];
   }

   void set_drive_power(double var1, double var3) {
      if(this.v_motor_left_drive != null) {
         this.v_motor_left_drive.setPower(var1);
      }

      if(this.v_motor_right_drive != null) {
         this.v_motor_right_drive.setPower(var3);
      }

   }

   public void start() {
   }

   public void stop() {
   }
}
