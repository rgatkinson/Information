package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.PushBotTelemetrySensors;

public class PushBotOdsDetectEvent extends PushBotTelemetrySensors {
   public void loop() {
      if(this.a_ods_white_tape_detected()) {
         this.set_drive_power(0.0D, 0.0D);
      } else {
         this.set_drive_power(1.0D, 1.0D);
      }

      this.update_telemetry();
   }
}
