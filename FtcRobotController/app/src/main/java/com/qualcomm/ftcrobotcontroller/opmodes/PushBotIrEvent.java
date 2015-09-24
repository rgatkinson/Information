package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.PushBotTelemetrySensors;

public class PushBotIrEvent extends PushBotTelemetrySensors {
   public void loop() {
      int var1 = this.drive_to_ir_beacon();
      if(var1 == 0) {
         this.set_first_message("Driving to IR beacon.");
      } else if(var1 == 1) {
         this.set_error_message("IR beacon is close!");
      } else if(var1 == -2) {
         this.set_error_message("IR beacon not detected!");
      } else if(var1 == -1) {
         this.set_error_message("IR beacon return value is invalid!");
      }

      this.update_telemetry();
   }
}
