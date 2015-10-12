package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.PushBotTelemetrySensors;

public class PushBotManualSensors extends PushBotTelemetrySensors {
   private boolean v_raise_arm_automatically = false;

   public void loop() {
      float var1 = this.scale_motor_power(-this.gamepad1.left_stick_y);
      float var2 = this.scale_motor_power(-this.gamepad1.right_stick_y);
      this.set_drive_power((double)var1, (double)var2);
      if(this.gamepad2.y && !this.v_raise_arm_automatically) {
         this.v_raise_arm_automatically = true;
      }

      float var3 = -this.gamepad2.left_stick_y;
      float var4;
      if(this.v_raise_arm_automatically) {
         var4 = 1.0F;
         if(this.is_touch_sensor_pressed() || (double)Math.abs(var3) > 0.8D) {
            var4 = 0.0F;
            this.v_raise_arm_automatically = false;
         }
      } else {
         this.v_raise_arm_automatically = false;
         var4 = this.scale_motor_power(var3);
      }

      this.m_left_arm_power((double)var4);
      if(this.gamepad2.x) {
         this.m_hand_position(0.05D + this.a_hand_position());
      } else if(this.gamepad2.b) {
         this.m_hand_position(this.a_hand_position() - 0.05D);
      }

      this.update_telemetry();
      this.telemetry.addData("18", "Raise Arm: " + this.v_raise_arm_automatically);
      this.telemetry.addData("19", "Left arm command: " + var4);
   }
}
