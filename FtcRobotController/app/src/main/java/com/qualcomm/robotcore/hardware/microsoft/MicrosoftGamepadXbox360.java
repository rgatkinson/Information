package com.qualcomm.robotcore.hardware.microsoft;

import com.qualcomm.robotcore.hardware.Gamepad;

public class MicrosoftGamepadXbox360 extends Gamepad {
   public MicrosoftGamepadXbox360() {
      this((Gamepad.GamepadCallback)null);
   }

   public MicrosoftGamepadXbox360(Gamepad.GamepadCallback var1) {
      super(var1);
      this.joystickDeadzone = 0.15F;
   }

   public String type() {
      return "Xbox 360";
   }
}
