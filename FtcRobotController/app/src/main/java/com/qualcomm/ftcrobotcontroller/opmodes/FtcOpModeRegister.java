package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.K9TeleOp;
import com.qualcomm.ftcrobotcontroller.opmodes.NullOp;
import com.qualcomm.ftcrobotcontroller.opmodes.PushBotAuto;
import com.qualcomm.ftcrobotcontroller.opmodes.PushBotManual;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;

public class FtcOpModeRegister implements OpModeRegister {
   public void register(OpModeManager var1) {
      var1.register("NullOp", NullOp.class);
      var1.register("K9TeleOp", K9TeleOp.class);
      var1.register("PushBotAuto", PushBotAuto.class);
      var1.register("PushBotManual", PushBotManual.class);
   }
}
