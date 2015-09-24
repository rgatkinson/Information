package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.PushBotTelemetrySensors;

public class PushBotAutoSensors extends PushBotTelemetrySensors {
   private int v_arm_state = 0;
   private int v_state = 0;

   public void loop() {
      switch(this.v_state) {
      case 0:
         if(this.have_drive_encoders_reset()) {
            this.drive_using_encoders(-1.0D, 1.0D, -2880.0D, 2880.0D);
            ++this.v_state;
         }
         break;
      case 1:
         if(this.drive_using_encoders(1.0D, 1.0D, 2880.0D, 2880.0D)) {
            ++this.v_state;
         }
         break;
      case 2:
         if(this.have_drive_encoders_reset()) {
            this.drive_using_encoders(-1.0D, 1.0D, -2880.0D, 2880.0D);
            this.v_arm_state = 1;
            ++this.v_state;
         }
         break;
      case 3:
         if(this.drive_using_encoders(-1.0D, 1.0D, -2880.0D, 2880.0D)) {
            ++this.v_state;
         }
         break;
      case 4:
         if(this.have_drive_encoders_reset()) {
            this.run_without_drive_encoders();
            ++this.v_state;
         }
         break;
      case 5:
         if(this.a_ods_white_tape_detected()) {
            this.set_drive_power(0.0D, 0.0D);
            this.drive_to_ir_beacon();
            ++this.v_state;
         } else {
            this.set_drive_power(1.0D, 1.0D);
         }
         break;
      case 6:
         int var1 = this.drive_to_ir_beacon();
         if(var1 == 1) {
            this.open_hand();
            ++this.v_state;
         } else if(var1 == -2) {
            this.set_error_message("IR beacon not detected!");
         }
      }

      this.update_arm_state();
      this.update_telemetry();
      this.telemetry.addData("11", "Drive State: " + this.v_state);
      this.telemetry.addData("12", "Arm State: " + this.v_arm_state);
   }

   public void start() {
      super.start();
      this.reset_drive_encoders();
   }

   public void update_arm_state() {
      switch(this.v_arm_state) {
      case 1:
         if(this.move_arm_upward_until_touch()) {
            ++this.v_arm_state;
            return;
         }
      case 0:
      default:
      }
   }
}
