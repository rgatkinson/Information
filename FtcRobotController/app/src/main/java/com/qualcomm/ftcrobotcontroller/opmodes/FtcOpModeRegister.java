package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;

public class FtcOpModeRegister implements OpModeRegister
{
    @Override
    public void register(final OpModeManager opModeManager) {
        opModeManager.register("NullOp", NullOp.class);
        opModeManager.register("K9TeleOp", K9TeleOp.class);
        opModeManager.register("PushBotAuto", PushBotAuto.class);
        opModeManager.register("PushBotManual", PushBotManual.class);
    }
}
