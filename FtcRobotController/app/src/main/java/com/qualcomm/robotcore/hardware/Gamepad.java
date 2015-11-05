package com.qualcomm.robotcore.hardware;

import android.annotation.TargetApi;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robocol.RobocolParsable;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;

public class Gamepad implements RobocolParsable {
   public static final int ID_UNASSOCIATED = -1;
   private static Set<Integer> d = new HashSet();
   private static Set<Gamepad.a> e = null;
   public boolean a;
   public boolean b;
   public boolean back;
   private final Gamepad.GamepadCallback c;
   protected float dpadThreshold;
   public boolean dpad_down;
   public boolean dpad_left;
   public boolean dpad_right;
   public boolean dpad_up;
   public boolean guide;
   public int id;
   protected float joystickDeadzone;
   public boolean left_bumper;
   public boolean left_stick_button;
   public float left_stick_x;
   public float left_stick_y;
   public float left_trigger;
   public boolean right_bumper;
   public boolean right_stick_button;
   public float right_stick_x;
   public float right_stick_y;
   public float right_trigger;
   public boolean start;
   public long timestamp;
   public byte user;
   public boolean x;
   public boolean y;

   public Gamepad() {
      this((Gamepad.GamepadCallback)null);
   }

   public Gamepad(Gamepad.GamepadCallback var1) {
      this.left_stick_x = 0.0F;
      this.left_stick_y = 0.0F;
      this.right_stick_x = 0.0F;
      this.right_stick_y = 0.0F;
      this.dpad_up = false;
      this.dpad_down = false;
      this.dpad_left = false;
      this.dpad_right = false;
      this.a = false;
      this.b = false;
      this.x = false;
      this.y = false;
      this.guide = false;
      this.start = false;
      this.back = false;
      this.left_bumper = false;
      this.right_bumper = false;
      this.left_stick_button = false;
      this.right_stick_button = false;
      this.left_trigger = 0.0F;
      this.right_trigger = 0.0F;
      this.user = -1;
      this.id = -1;
      this.timestamp = 0L;
      this.dpadThreshold = 0.2F;
      this.joystickDeadzone = 0.2F;
      this.c = var1;
   }

   public static void clearWhitelistFilter() {
      e = null;
   }

   public static void enableWhitelistFilter(int var0, int var1) {
      if(e == null) {
         e = new HashSet();
      }

      e.add(new Gamepad.a(var0, var1));
   }

   @TargetApi(19)
   public static boolean isGamepadDevice(int param0) {
      // $FF: Couldn't be decompiled
   }

   public boolean atRest() {
      return this.left_stick_x == 0.0F && this.left_stick_y == 0.0F && this.right_stick_x == 0.0F && this.right_stick_y == 0.0F && this.left_trigger == 0.0F && this.right_trigger == 0.0F;
   }

   protected void callCallback() {
      if(this.c != null) {
         this.c.gamepadChanged(this);
      }

   }

   protected float cleanMotionValues(float var1) {
      if(var1 < this.joystickDeadzone && var1 > -this.joystickDeadzone) {
         var1 = 0.0F;
      } else {
         if(var1 > 1.0F) {
            return 1.0F;
         }

         if(var1 < -1.0F) {
            return -1.0F;
         }

         if(var1 < 0.0F) {
            Range.scale((double)var1, (double)this.joystickDeadzone, 1.0D, 0.0D, 1.0D);
         }

         if(var1 > 0.0F) {
            Range.scale((double)var1, (double)(-this.joystickDeadzone), -1.0D, 0.0D, -1.0D);
            return var1;
         }
      }

      return var1;
   }

   public void copy(Gamepad var1) throws RobotCoreException {
      this.fromByteArray(var1.toByteArray());
   }

   public void fromByteArray(byte[] var1) throws RobotCoreException {
      byte var2 = 1;
      if(var1.length < 45) {
         throw new RobotCoreException("Expected buffer of at least 45 bytes, received " + var1.length);
      } else {
         ByteBuffer var3 = ByteBuffer.wrap(var1, 3, 42);
         byte var4 = var3.get();
         if(var4 >= var2) {
            this.id = var3.getInt();
            this.timestamp = var3.getLong();
            this.left_stick_x = var3.getFloat();
            this.left_stick_y = var3.getFloat();
            this.right_stick_x = var3.getFloat();
            this.right_stick_y = var3.getFloat();
            this.left_trigger = var3.getFloat();
            this.right_trigger = var3.getFloat();
            int var5 = var3.getInt();
            byte var6;
            if((var5 & 16384) != 0) {
               var6 = var2;
            } else {
               var6 = 0;
            }

            this.left_stick_button = (boolean)var6;
            byte var7;
            if((var5 & 8192) != 0) {
               var7 = var2;
            } else {
               var7 = 0;
            }

            this.right_stick_button = (boolean)var7;
            byte var8;
            if((var5 & 4096) != 0) {
               var8 = var2;
            } else {
               var8 = 0;
            }

            this.dpad_up = (boolean)var8;
            byte var9;
            if((var5 & 2048) != 0) {
               var9 = var2;
            } else {
               var9 = 0;
            }

            this.dpad_down = (boolean)var9;
            byte var10;
            if((var5 & 1024) != 0) {
               var10 = var2;
            } else {
               var10 = 0;
            }

            this.dpad_left = (boolean)var10;
            byte var11;
            if((var5 & 512) != 0) {
               var11 = var2;
            } else {
               var11 = 0;
            }

            this.dpad_right = (boolean)var11;
            byte var12;
            if((var5 & 256) != 0) {
               var12 = var2;
            } else {
               var12 = 0;
            }

            this.a = (boolean)var12;
            byte var13;
            if((var5 & 128) != 0) {
               var13 = var2;
            } else {
               var13 = 0;
            }

            this.b = (boolean)var13;
            byte var14;
            if((var5 & 64) != 0) {
               var14 = var2;
            } else {
               var14 = 0;
            }

            this.x = (boolean)var14;
            byte var15;
            if((var5 & 32) != 0) {
               var15 = var2;
            } else {
               var15 = 0;
            }

            this.y = (boolean)var15;
            byte var16;
            if((var5 & 16) != 0) {
               var16 = var2;
            } else {
               var16 = 0;
            }

            this.guide = (boolean)var16;
            byte var17;
            if((var5 & 8) != 0) {
               var17 = var2;
            } else {
               var17 = 0;
            }

            this.start = (boolean)var17;
            byte var18;
            if((var5 & 4) != 0) {
               var18 = var2;
            } else {
               var18 = 0;
            }

            this.back = (boolean)var18;
            byte var19;
            if((var5 & 2) != 0) {
               var19 = var2;
            } else {
               var19 = 0;
            }

            this.left_bumper = (boolean)var19;
            if((var5 & 1) == 0) {
               var2 = 0;
            }

            this.right_bumper = (boolean)var2;
         }

         if(var4 >= 2) {
            this.user = var3.get();
         }

         this.callCallback();
      }
   }

   public RobocolParsable.MsgType getRobocolMsgType() {
      return RobocolParsable.MsgType.GAMEPAD;
   }

   protected boolean pressed(KeyEvent var1) {
      return var1.getAction() == 0;
   }

   public void reset() {
      try {
         this.copy(new Gamepad());
      } catch (RobotCoreException var2) {
         RobotLog.e("Gamepad library in an invalid state");
         throw new IllegalStateException("Gamepad library in an invalid state");
      }
   }

   public void setJoystickDeadzone(float var1) {
      if(var1 >= 0.0F && var1 <= 1.0F) {
         this.joystickDeadzone = var1;
      } else {
         throw new IllegalArgumentException("deadzone cannot be greater than max joystick value");
      }
   }

   public byte[] toByteArray() throws RobotCoreException {
      // $FF: Couldn't be decompiled
   }

   public String toString() {
      String var1 = new String();
      if(this.dpad_up) {
         var1 = var1 + "dpad_up ";
      }

      if(this.dpad_down) {
         var1 = var1 + "dpad_down ";
      }

      if(this.dpad_left) {
         var1 = var1 + "dpad_left ";
      }

      if(this.dpad_right) {
         var1 = var1 + "dpad_right ";
      }

      if(this.a) {
         var1 = var1 + "a ";
      }

      if(this.b) {
         var1 = var1 + "b ";
      }

      if(this.x) {
         var1 = var1 + "x ";
      }

      if(this.y) {
         var1 = var1 + "y ";
      }

      if(this.guide) {
         var1 = var1 + "guide ";
      }

      if(this.start) {
         var1 = var1 + "start ";
      }

      if(this.back) {
         var1 = var1 + "back ";
      }

      if(this.left_bumper) {
         var1 = var1 + "left_bumper ";
      }

      if(this.right_bumper) {
         var1 = var1 + "right_bumper ";
      }

      if(this.left_stick_button) {
         var1 = var1 + "left stick button ";
      }

      if(this.right_stick_button) {
         var1 = var1 + "right stick button ";
      }

      Object[] var2 = new Object[]{Integer.valueOf(this.id), Byte.valueOf(this.user), Float.valueOf(this.left_stick_x), Float.valueOf(this.left_stick_y), Float.valueOf(this.right_stick_x), Float.valueOf(this.right_stick_y), Float.valueOf(this.left_trigger), Float.valueOf(this.right_trigger), var1};
      return String.format("ID: %2d user: %2d lx: % 1.2f ly: % 1.2f rx: % 1.2f ry: % 1.2f lt: %1.2f rt: %1.2f %s", var2);
   }

   public String type() {
      return "Standard";
   }

   public void update(KeyEvent var1) {
      this.id = var1.getDeviceId();
      this.timestamp = var1.getEventTime();
      int var2 = var1.getKeyCode();
      if(var2 == 19) {
         this.dpad_up = this.pressed(var1);
      } else if(var2 == 20) {
         this.dpad_down = this.pressed(var1);
      } else if(var2 == 22) {
         this.dpad_right = this.pressed(var1);
      } else if(var2 == 21) {
         this.dpad_left = this.pressed(var1);
      } else if(var2 == 96) {
         this.a = this.pressed(var1);
      } else if(var2 == 97) {
         this.b = this.pressed(var1);
      } else if(var2 == 99) {
         this.x = this.pressed(var1);
      } else if(var2 == 100) {
         this.y = this.pressed(var1);
      } else if(var2 == 110) {
         this.guide = this.pressed(var1);
      } else if(var2 == 108) {
         this.start = this.pressed(var1);
      } else if(var2 == 4) {
         this.back = this.pressed(var1);
      } else if(var2 == 103) {
         this.right_bumper = this.pressed(var1);
      } else if(var2 == 102) {
         this.left_bumper = this.pressed(var1);
      } else if(var2 == 106) {
         this.left_stick_button = this.pressed(var1);
      } else if(var2 == 107) {
         this.right_stick_button = this.pressed(var1);
      }

      this.callCallback();
   }

   public void update(MotionEvent var1) {
      byte var2 = 1;
      this.id = var1.getDeviceId();
      this.timestamp = var1.getEventTime();
      this.left_stick_x = this.cleanMotionValues(var1.getAxisValue(0));
      this.left_stick_y = this.cleanMotionValues(var1.getAxisValue(var2));
      this.right_stick_x = this.cleanMotionValues(var1.getAxisValue(11));
      this.right_stick_y = this.cleanMotionValues(var1.getAxisValue(14));
      this.left_trigger = var1.getAxisValue(17);
      this.right_trigger = var1.getAxisValue(18);
      byte var3;
      if(var1.getAxisValue(16) > this.dpadThreshold) {
         var3 = var2;
      } else {
         var3 = 0;
      }

      this.dpad_down = (boolean)var3;
      byte var4;
      if(var1.getAxisValue(16) < -this.dpadThreshold) {
         var4 = var2;
      } else {
         var4 = 0;
      }

      this.dpad_up = (boolean)var4;
      byte var5;
      if(var1.getAxisValue(15) > this.dpadThreshold) {
         var5 = var2;
      } else {
         var5 = 0;
      }

      this.dpad_right = (boolean)var5;
      if(var1.getAxisValue(15) >= -this.dpadThreshold) {
         var2 = 0;
      }

      this.dpad_left = (boolean)var2;
      this.callCallback();
   }

   public interface GamepadCallback {
      void gamepadChanged(Gamepad var1);
   }

   private static class a extends SimpleEntry<Integer, Integer> {
      public a(int var1, int var2) {
         super(Integer.valueOf(var1), Integer.valueOf(var2));
      }
   }
}
