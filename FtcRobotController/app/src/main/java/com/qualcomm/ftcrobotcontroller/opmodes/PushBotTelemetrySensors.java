package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.PushBotHardwareSensors;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;

public class PushBotTelemetrySensors extends PushBotHardwareSensors {
   public void update_telemetry() {
      super.update_telemetry();
      this.telemetry.addData("12", "Touch: " + this.is_touch_sensor_pressed());
      this.telemetry.addData("13", "IR Angle: " + this.a_ir_angle());
      this.telemetry.addData("14", "IR Strength: " + this.a_ir_strength());
      IrSeekerSensor.IrSeekerIndividualSensor[] var1 = this.a_ir_angles_and_strengths();
      this.telemetry.addData("15", "IR Sensor 1: " + var1[0].toString());
      this.telemetry.addData("16", "IR Sensor 2: " + var1[1].toString());
      this.telemetry.addData("17", "ODS: " + this.a_ods_light_detected());
   }
}
