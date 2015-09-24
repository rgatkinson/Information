package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.PushBotTelemetry;

public class PushBotManual1 extends PushBotTelemetry {
   public void loop() {
      float var1 = this.scale_motor_power(-this.gamepad1.left_stick_y);
      float var2 = this.scale_motor_power(-this.gamepad1.right_stick_y);
      this.set_drive_power((double)var1, (double)var2);
      float var3 = this.scale_motor_power(this.gamepad1.right_trigger) - this.scale_motor_power(this.gamepad1.left_trigger);
      this.m_left_arm_power((double)var3);
      if(this.gamepad1.x) {
         this.m_hand_position(0.05D + this.a_hand_position());
      } else if(this.gamepad1.b) {
         this.m_hand_position(this.a_hand_position() - 0.05D);
      }

      this.update_telemetry();
      this.update_gamepad_telemetry();
      this.telemetry.addData("12", "Left Arm: " + var3);
   }
}
