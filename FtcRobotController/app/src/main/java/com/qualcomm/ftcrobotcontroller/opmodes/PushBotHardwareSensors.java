package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftcrobotcontroller.opmodes.PushBotTelemetry;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

public class PushBotHardwareSensors extends PushBotTelemetry {
   final int drive_to_ir_beacon_done = 1;
   final int drive_to_ir_beacon_invalid = -1;
   final int drive_to_ir_beacon_not_detected = -2;
   final int drive_to_ir_beacon_running = 0;
   private IrSeekerSensor v_sensor_ir;
   private OpticalDistanceSensor v_sensor_ods;
   private TouchSensor v_sensor_touch;

   double a_ir_angle() {
      double var1 = 0.0D;
      if(this.v_sensor_ir != null) {
         var1 = this.v_sensor_ir.getAngle();
      }

      return var1;
   }

   IrSeekerSensor.IrSeekerIndividualSensor[] a_ir_angles_and_strengths() {
      IrSeekerSensor.IrSeekerIndividualSensor[] var1 = new IrSeekerSensor.IrSeekerIndividualSensor[]{new IrSeekerSensor.IrSeekerIndividualSensor(), new IrSeekerSensor.IrSeekerIndividualSensor()};
      if(this.v_sensor_ir != null) {
         var1 = this.v_sensor_ir.getIndividualSensors();
      }

      return var1;
   }

   double a_ir_strength() {
      double var1 = 0.0D;
      if(this.v_sensor_ir != null) {
         var1 = this.v_sensor_ir.getStrength();
      }

      return var1;
   }

   double a_ods_light_detected() {
      if(this.v_sensor_ods != null) {
         this.v_sensor_ods.getLightDetected();
      }

      return 0.0D;
   }

   boolean a_ods_white_tape_detected() {
      OpticalDistanceSensor var1 = this.v_sensor_ods;
      boolean var2 = false;
      if(var1 != null) {
         double var4;
         int var3 = (var4 = this.v_sensor_ods.getLightDetected() - 0.8D) == 0.0D?0:(var4 < 0.0D?-1:1);
         var2 = false;
         if(var3 > 0) {
            var2 = true;
         }
      }

      return var2;
   }

   int drive_to_ir_beacon() {
      double var1 = this.a_ir_strength();
      if(var1 > 0.01D && var1 < 0.2D) {
         double var3 = this.a_ir_angle() / 240.0D;
         this.set_drive_power(Range.clip(0.15D + var3, -1.0D, 1.0D), Range.clip(0.15D - var3, -1.0D, 1.0D));
         return 0;
      } else if(var1 <= 0.0D) {
         this.set_drive_power(0.0D, 0.0D);
         return -2;
      } else {
         this.set_drive_power(0.0D, 0.0D);
         return 1;
      }
   }

   public void init() {
      super.init();

      try {
         this.v_sensor_touch = (TouchSensor)this.hardwareMap.touchSensor.get("sensor_touch");
      } catch (Exception var10) {
         this.m_warning_message("sensor_touch");
         DbgLog.msg(var10.getLocalizedMessage());
         this.v_sensor_touch = null;
      }

      try {
         this.v_sensor_ir = (IrSeekerSensor)this.hardwareMap.irSeekerSensor.get("sensor_ir");
      } catch (Exception var9) {
         this.m_warning_message("sensor_ir");
         DbgLog.msg(var9.getLocalizedMessage());
         this.v_sensor_ir = null;
      }

      try {
         this.v_sensor_ods = (OpticalDistanceSensor)this.hardwareMap.opticalDistanceSensor.get("sensor_ods");
      } catch (Exception var8) {
         try {
            this.v_sensor_ods = (OpticalDistanceSensor)this.hardwareMap.opticalDistanceSensor.get("sensor_eopd");
         } catch (Exception var7) {
            try {
               this.v_sensor_ods = (OpticalDistanceSensor)this.hardwareMap.opticalDistanceSensor.get("sensor_EOPD");
            } catch (Exception var6) {
               this.m_warning_message("sensor_ods/eopd/EOPD");
               DbgLog.msg("Can\'t map sensor_ods nor sensor_eopd, nor sensor_EOPD (" + var6.getLocalizedMessage() + ").\n");
               this.v_sensor_ods = null;
            }
         }
      }
   }

   boolean is_touch_sensor_pressed() {
      TouchSensor var1 = this.v_sensor_touch;
      boolean var2 = false;
      if(var1 != null) {
         var2 = this.v_sensor_touch.isPressed();
      }

      return var2;
   }

   boolean move_arm_upward_until_touch() {
      if(this.is_touch_sensor_pressed()) {
         this.m_left_arm_power(0.0D);
      } else {
         this.m_left_arm_power(1.0D);
      }

      return this.is_touch_sensor_pressed();
   }
}
