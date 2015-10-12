package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NullOp extends OpMode {
   private ElapsedTime runtime = new ElapsedTime();
   private String startDate;

   public void init() {
   }

   public void init_loop() {
      this.startDate = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date());
      this.runtime.reset();
      this.telemetry.addData("Null Op Init Loop", this.runtime.toString());
   }

   public void loop() {
      this.telemetry.addData("1 Start", "NullOp started at " + this.startDate);
      this.telemetry.addData("2 Status", "running for " + this.runtime.toString());
   }
}
