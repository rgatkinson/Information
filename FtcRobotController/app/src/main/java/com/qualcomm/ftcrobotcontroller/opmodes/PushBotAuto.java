package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.PushBotTelemetry;

public class PushBotAuto extends PushBotTelemetry {
   private int v_state = 0;

   public void loop() {
      switch(this.v_state) {
      case 0:
         this.reset_drive_encoders();
         ++this.v_state;
         break;
      case 1:
         this.run_using_encoders();
         this.set_drive_power(1.0D, 1.0D);
         if(this.have_drive_encoders_reached(2880.0D, 2880.0D)) {
            this.reset_drive_encoders();
            this.set_drive_power(0.0D, 0.0D);
            ++this.v_state;
         }
         break;
      case 2:
         if(this.have_drive_encoders_reset()) {
            ++this.v_state;
         }
         break;
      case 3:
         this.run_using_encoders();
         this.set_drive_power(-1.0D, 1.0D);
         if(this.have_drive_encoders_reached(2880.0D, 2880.0D)) {
            this.reset_drive_encoders();
            this.set_drive_power(0.0D, 0.0D);
            ++this.v_state;
         }
         break;
      case 4:
         if(this.have_drive_encoders_reset()) {
            ++this.v_state;
         }
         break;
      case 5:
         this.run_using_encoders();
         this.set_drive_power(1.0D, -1.0D);
         if(this.have_drive_encoders_reached(2880.0D, 2880.0D)) {
            this.reset_drive_encoders();
            this.set_drive_power(0.0D, 0.0D);
            ++this.v_state;
         }
         break;
      case 6:
         if(this.have_drive_encoders_reset()) {
            ++this.v_state;
         }
      }

      this.update_telemetry();
      this.telemetry.addData("18", "State: " + this.v_state);
   }

   public void start() {
      super.start();
      this.reset_drive_encoders();
   }
}
