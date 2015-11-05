package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.hardware.MatrixDcMotorController;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.HashSet;
import java.util.Set;

public class MatrixControllerDemo extends OpMode {
   private static final double MOTOR_OSC_FREQ = 2.0D;
   private static final double SERVO_OSC_FREQ = 1.0D;
   private static final double SPAM_PREVENTION_FREQ = 1.0D;
   private int battery;
   private boolean firstBattery = true;
   private boolean firstMotors = true;
   private boolean firstServos = true;
   private boolean loopOnce = false;
   private MatrixDcMotorController mc;
   private DcMotor motor1;
   private DcMotor motor2;
   private DcMotor motor3;
   private DcMotor motor4;
   private ElapsedTime motorOscTimer = new ElapsedTime(0L);
   private double motorPower = 1.0D;
   private Set<DcMotor> motorSet = new HashSet();
   private ServoController sc;
   private Servo servo1;
   private Servo servo2;
   private Servo servo3;
   private Servo servo4;
   private ElapsedTime servoOscTimer = new ElapsedTime(0L);
   private double servoPosition = 0.0D;
   private ElapsedTime spamPrevention = new ElapsedTime(0L);

   protected void handleBattery() {
      if(this.firstBattery || this.spamPrevention.time() > 1.0D) {
         this.battery = this.mc.getBattery();
         this.spamPrevention.reset();
         this.firstBattery = false;
      }

      this.telemetry.addData("Battery: ", (float)this.battery / 1000.0F);
   }

   protected void handleMotors() {
      if(this.firstMotors || this.motorOscTimer.time() > 2.0D) {
         this.motorPower = -this.motorPower;
         this.mc.setMotorPower(this.motorSet, this.motorPower);
         this.motorOscTimer.reset();
         this.firstMotors = false;
      }

   }

   protected void handleServos() {
      if(this.firstServos || this.servoOscTimer.time() > 1.0D) {
         if(this.servoPosition == 0.0D) {
            this.servoPosition = 1.0D;
         } else {
            this.servoPosition = 0.0D;
         }

         this.servo1.setPosition(this.servoPosition);
         this.servo2.setPosition(this.servoPosition);
         this.servo3.setPosition(this.servoPosition);
         this.servo4.setPosition(this.servoPosition);
         this.servoOscTimer.reset();
         this.firstServos = false;
      }

   }

   public void init() {
      this.motor1 = (DcMotor)this.hardwareMap.dcMotor.get("motor_1");
      this.motor2 = (DcMotor)this.hardwareMap.dcMotor.get("motor_2");
      this.motor3 = (DcMotor)this.hardwareMap.dcMotor.get("motor_3");
      this.motor4 = (DcMotor)this.hardwareMap.dcMotor.get("motor_4");
      this.motorSet.add(this.motor1);
      this.motorSet.add(this.motor2);
      this.motorSet.add(this.motor3);
      this.motorSet.add(this.motor4);
      this.servo1 = (Servo)this.hardwareMap.servo.get("servo_1");
      this.servo2 = (Servo)this.hardwareMap.servo.get("servo_2");
      this.servo3 = (Servo)this.hardwareMap.servo.get("servo_3");
      this.servo4 = (Servo)this.hardwareMap.servo.get("servo_4");
      this.mc = (MatrixDcMotorController)this.hardwareMap.dcMotorController.get("MatrixController");
      this.motor1.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
      this.motor2.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
      this.motor3.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
      this.motor4.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
      this.sc = (ServoController)this.hardwareMap.servoController.get("MatrixController");
      this.sc.pwmEnable();
   }

   public void loop() {
      this.handleMotors();
      this.handleServos();
      this.handleBattery();
   }

   public void start() {
      this.motorOscTimer.reset();
      this.servoOscTimer.reset();
      this.spamPrevention.reset();
   }

   public void stop() {
      this.motor1.setPower(0.0D);
      this.motor2.setPower(0.0D);
      this.motor3.setPower(0.0D);
      this.motor4.setPower(0.0D);
      this.sc.pwmDisable();
   }
}
