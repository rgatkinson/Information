package com.qualcomm.ftccommon.configuration;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftccommon.R;
import com.qualcomm.hardware.ModernRoboticsDeviceManager;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.LegacyModuleControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ServoConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class AutoConfigureActivity extends Activity {
   private Context a;
   private Button b;
   private Button c;
   private DeviceManager d;
   private Map<SerialNumber, ControllerConfiguration> e = new HashMap();
   protected Set<Entry<SerialNumber, DeviceManager.DeviceType>> entries = new HashSet();
   private Thread f;
   private Utility g;
   protected Map<SerialNumber, DeviceManager.DeviceType> scannedDevices = new HashMap();

   private DeviceConfiguration a(DeviceConfiguration.ConfigurationType var1, String var2, int var3) {
      return new DeviceConfiguration(var3, var1, var2, true);
   }

   private MotorControllerConfiguration a(SerialNumber var1, String var2, String var3, String var4) {
      MotorControllerConfiguration var5;
      if(!var1.equals(ControllerConfiguration.NO_SERIAL_NUMBER)) {
         var5 = (MotorControllerConfiguration)this.e.get(var1);
      } else {
         var5 = new MotorControllerConfiguration();
      }

      var5.setName(var4);
      ArrayList var6 = new ArrayList();
      MotorConfiguration var7 = new MotorConfiguration(1, var2, true);
      MotorConfiguration var8 = new MotorConfiguration(2, var3, true);
      var6.add(var7);
      var6.add(var8);
      var5.addMotors(var6);
      return var5;
   }

   private ServoControllerConfiguration a(SerialNumber var1, ArrayList<String> var2, String var3) {
      ServoControllerConfiguration var4;
      if(!var1.equals(ControllerConfiguration.NO_SERIAL_NUMBER)) {
         var4 = (ServoControllerConfiguration)this.e.get(var1);
      } else {
         var4 = new ServoControllerConfiguration();
      }

      var4.setName(var3);
      ArrayList var5 = new ArrayList();

      for(int var6 = 1; var6 <= 6; ++var6) {
         String var7 = (String)var2.get(var6 - 1);
         boolean var8;
         if(var7.equals("NO DEVICE ATTACHED")) {
            var8 = false;
         } else {
            var8 = true;
         }

         var5.add(new ServoConfiguration(var6, var7, var8));
      }

      var4.addServos(var5);
      return var4;
   }

   private void a() {
      this.g.setOrangeText("No devices found!", "To configure K9LegacyBot, please: \n   1. Attach a LegacyModuleController, \n       with \n       a. MotorController in port 0, with a \n         motor in port 1 and port 2 \n       b. ServoController in port 1, with a \n         servo in port 1 and port 6 \n      c. IR seeker in port 2\n      d. Light sensor in port 3 \n   2. Press the K9LegacyBot button\n \nTo configure K9USBBot, please: \n   1. Attach a USBMotorController, with a \n       motor in port 1 and port 2 \n    2. USBServoController in port 1, with a \n      servo in port 1 and port 6 \n   3. LegacyModule, with \n      a. IR seeker in port 2\n      b. Light sensor in port 3 \n   4. Press the K9USBBot button", R.id.autoconfigure_info, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
   }

   private void a(SerialNumber var1, String var2) {
      LegacyModuleControllerConfiguration var3 = (LegacyModuleControllerConfiguration)this.e.get(var1);
      var3.setName(var2);
      DeviceConfiguration var4 = this.a(DeviceConfiguration.ConfigurationType.IR_SEEKER, "ir_seeker", 2);
      DeviceConfiguration var5 = this.a(DeviceConfiguration.ConfigurationType.LIGHT_SENSOR, "light_sensor", 3);
      ArrayList var6 = new ArrayList();

      for(int var7 = 0; var7 < 6; ++var7) {
         if(var7 == 2) {
            var6.add(var4);
         }

         if(var7 == 3) {
            var6.add(var5);
         } else {
            var6.add(new DeviceConfiguration(var7));
         }
      }

      var3.addDevices(var6);
   }

   private void a(String var1) {
      this.g.writeXML(this.e);

      try {
         this.g.writeToFile(var1 + ".xml");
         this.g.saveToPreferences(var1, R.string.pref_hardware_config_filename);
         this.g.updateHeader(var1, R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
         Toast.makeText(this.a, "AutoConfigure " + var1 + " Successful", 0).show();
      } catch (RobotCoreException var5) {
         this.g.complainToast(var5.getMessage(), this.a);
         DbgLog.error(var5.getMessage());
      } catch (IOException var6) {
         this.g.complainToast("Found " + var6.getMessage() + "\n Please fix and re-save", this.a);
         DbgLog.error(var6.getMessage());
      }
   }

   private void b() {
      String var1 = "Found: \n" + this.scannedDevices.values() + "\n" + "Required: \n" + "   1. LegacyModuleController, with \n " + "      a. MotorController in port 0, with a \n" + "          motor in port 1 and port 2 \n " + "      b. ServoController in port 1, with a \n" + "          servo in port 1 and port 6 \n" + "       c. IR seeker in port 2\n" + "       d. Light sensor in port 3 ";
      this.g.setOrangeText("Wrong devices found!", var1, R.id.autoconfigure_info, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
   }

   private void b(SerialNumber var1, String var2) {
      LegacyModuleControllerConfiguration var3 = (LegacyModuleControllerConfiguration)this.e.get(var1);
      var3.setName(var2);
      MotorControllerConfiguration var4 = this.a(ControllerConfiguration.NO_SERIAL_NUMBER, "motor_1", "motor_2", "wheels");
      var4.setPort(0);
      ArrayList var5 = this.f();
      ServoControllerConfiguration var6 = this.a(ControllerConfiguration.NO_SERIAL_NUMBER, var5, "servos");
      var6.setPort(1);
      DeviceConfiguration var7 = this.a(DeviceConfiguration.ConfigurationType.IR_SEEKER, "ir_seeker", 2);
      DeviceConfiguration var8 = this.a(DeviceConfiguration.ConfigurationType.LIGHT_SENSOR, "light_sensor", 3);
      ArrayList var9 = new ArrayList();
      var9.add(var4);
      var9.add(var6);
      var9.add(var7);
      var9.add(var8);

      for(int var14 = 4; var14 < 6; ++var14) {
         var9.add(new DeviceConfiguration(var14));
      }

      var3.addDevices(var9);
   }

   private void c() {
      String var1 = "Found: \n" + this.scannedDevices.values() + "\n" + "Required: \n" + "   1. USBMotorController with a \n" + "      motor in port 1 and port 2 \n " + "   2. USBServoController with a \n" + "      servo in port 1 and port 6 \n" + "   3. LegacyModuleController, with \n " + "       a. IR seeker in port 2\n" + "       b. Light sensor in port 3 ";
      this.g.setOrangeText("Wrong devices found!", var1, R.id.autoconfigure_info, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
   }

   private void d() {
      this.g.setOrangeText("Already configured!", "", R.id.autoconfigure_info, R.layout.orange_warning, R.id.orangetext0, R.id.orangetext1);
   }

   private boolean e() {
      Iterator var1 = this.entries.iterator();
      boolean var2 = true;
      boolean var3 = true;

      boolean var4;
      boolean var7;
      for(var4 = true; var1.hasNext(); var4 = var7) {
         Entry var5 = (Entry)var1.next();
         DeviceManager.DeviceType var6 = (DeviceManager.DeviceType)var5.getValue();
         if(var6 == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE && var4) {
            this.a((SerialNumber)var5.getKey(), "sensors");
            var7 = false;
         } else {
            var7 = var4;
         }

         boolean var8;
         if(var6 == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER && var3) {
            this.a((SerialNumber)var5.getKey(), "motor_1", "motor_2", "wheels");
            var8 = false;
         } else {
            var8 = var3;
         }

         boolean var9;
         if(var6 == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER && var2) {
            this.a((SerialNumber)var5.getKey(), this.f(), "servos");
            var9 = false;
         } else {
            var9 = var2;
         }

         var2 = var9;
         var3 = var8;
      }

      if(!var4 && !var3 && !var2) {
         ((LinearLayout)this.findViewById(R.id.autoconfigure_info)).removeAllViews();
         return true;
      } else {
         return false;
      }
   }

   private ArrayList<String> f() {
      ArrayList var1 = new ArrayList();
      var1.add("servo_1");
      var1.add("NO DEVICE ATTACHED");
      var1.add("NO DEVICE ATTACHED");
      var1.add("NO DEVICE ATTACHED");
      var1.add("NO DEVICE ATTACHED");
      var1.add("servo_6");
      return var1;
   }

   private boolean g() {
      Iterator var1 = this.entries.iterator();

      boolean var2;
      boolean var4;
      for(var2 = true; var1.hasNext(); var2 = var4) {
         Entry var3 = (Entry)var1.next();
         if((DeviceManager.DeviceType)var3.getValue() == DeviceManager.DeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE && var2) {
            this.b((SerialNumber)var3.getKey(), "devices");
            var4 = false;
         } else {
            var4 = var2;
         }
      }

      if(var2) {
         return false;
      } else {
         ((LinearLayout)this.findViewById(R.id.autoconfigure_info)).removeAllViews();
         return true;
      }
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.a = this;
      this.setContentView(R.layout.activity_autoconfigure);
      this.g = new Utility(this);
      this.b = (Button)this.findViewById(R.id.configureLegacy);
      this.c = (Button)this.findViewById(R.id.configureUSB);

      try {
         this.d = new ModernRoboticsDeviceManager(this.a, (EventLoopManager)null);
      } catch (RobotCoreException var3) {
         this.g.complainToast("Failed to open the Device Manager", this.a);
         DbgLog.error("Failed to open deviceManager: " + var3.toString());
         DbgLog.logStacktrace(var3);
      }
   }

   protected void onStart() {
      super.onStart();
      this.g.updateHeader("K9LegacyBot", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
      String var1 = this.g.getFilenameFromPrefs(R.string.pref_hardware_config_filename, "No current file!");
      if(!var1.equals("K9LegacyBot") && !var1.equals("K9USBBot")) {
         this.a();
      } else {
         this.d();
      }

      this.b.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            AutoConfigureActivity.this.f = new Thread(new Runnable() {
               public void run() {
                  try {
                     DbgLog.msg("Scanning USB bus");
                     AutoConfigureActivity.this.scannedDevices = AutoConfigureActivity.this.d.scanForUsbDevices();
                  } catch (RobotCoreException var2) {
                     DbgLog.error("Device scan failed");
                  }

                  AutoConfigureActivity.this.runOnUiThread(new Runnable() {
                     public void run() {
                        AutoConfigureActivity.this.g.resetCount();
                        if(AutoConfigureActivity.this.scannedDevices.size() == 0) {
                           AutoConfigureActivity.this.g.saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
                           AutoConfigureActivity.this.g.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                           AutoConfigureActivity.this.a();
                        }

                        AutoConfigureActivity.this.entries = AutoConfigureActivity.this.scannedDevices.entrySet();
                        AutoConfigureActivity.this.e = new HashMap();
                        AutoConfigureActivity.this.g.createLists(AutoConfigureActivity.this.entries, AutoConfigureActivity.this.e);
                        if(AutoConfigureActivity.this.g()) {
                           AutoConfigureActivity.this.a("K9LegacyBot");
                        } else {
                           AutoConfigureActivity.this.g.saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
                           AutoConfigureActivity.this.g.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                           AutoConfigureActivity.this.b();
                        }
                     }
                  });
               }
            });
            AutoConfigureActivity.this.f.start();
         }
      });
      this.c.setOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            AutoConfigureActivity.this.f = new Thread(new Runnable() {
               public void run() {
                  try {
                     DbgLog.msg("Scanning USB bus");
                     AutoConfigureActivity.this.scannedDevices = AutoConfigureActivity.this.d.scanForUsbDevices();
                  } catch (RobotCoreException var2) {
                     DbgLog.error("Device scan failed");
                  }

                  AutoConfigureActivity.this.runOnUiThread(new Runnable() {
                     public void run() {
                        AutoConfigureActivity.this.g.resetCount();
                        if(AutoConfigureActivity.this.scannedDevices.size() == 0) {
                           AutoConfigureActivity.this.g.saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
                           AutoConfigureActivity.this.g.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                           AutoConfigureActivity.this.a();
                        }

                        AutoConfigureActivity.this.entries = AutoConfigureActivity.this.scannedDevices.entrySet();
                        AutoConfigureActivity.this.e = new HashMap();
                        AutoConfigureActivity.this.g.createLists(AutoConfigureActivity.this.entries, AutoConfigureActivity.this.e);
                        if(AutoConfigureActivity.this.e()) {
                           AutoConfigureActivity.this.a("K9USBBot");
                        } else {
                           AutoConfigureActivity.this.g.saveToPreferences("No current file!", R.string.pref_hardware_config_filename);
                           AutoConfigureActivity.this.g.updateHeader("No current file!", R.string.pref_hardware_config_filename, R.id.active_filename, R.id.included_header);
                           AutoConfigureActivity.this.c();
                        }
                     }
                  });
               }
            });
            AutoConfigureActivity.this.f.start();
         }
      });
   }
}
