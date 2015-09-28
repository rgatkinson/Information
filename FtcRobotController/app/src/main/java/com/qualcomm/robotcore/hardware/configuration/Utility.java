package com.qualcomm.robotcore.hardware.configuration;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class Utility {
   public static final String AUTOCONFIGURE_K9LEGACYBOT = "K9LegacyBot";
   public static final String AUTOCONFIGURE_K9USBBOT = "K9USBBot";
   public static final String CONFIG_FILES_DIR = Environment.getExternalStorageDirectory() + "/FIRST/";
   public static final String DEFAULT_ROBOT_CONFIG = "robot_config";
   public static final String DEFAULT_ROBOT_CONFIG_FILENAME = "robot_config.xml";
   public static final String FILE_EXT = ".xml";
   public static final String NO_FILE = "No current file!";
   public static final String UNSAVED = "Unsaved";
   private static int c = 1;
   private Activity activity;
   private SharedPreferences sharedPreferences;
   private WriteXMLFileHandler writeXMLFileHandler;
   private String e;

   public Utility(Activity activity) {
      this.activity = activity;
      this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
      this.writeXMLFileHandler = new WriteXMLFileHandler(activity);
   }

   public Builder buildBuilder(String var1, String var2) {
      Builder var3 = new Builder(this.activity);
      var3.setTitle(var1).setMessage(var2);
      return var3;
   }

   public DeviceInterfaceModuleConfiguration buildDeviceInterfaceModule(SerialNumber var1) {
      DeviceInterfaceModuleConfiguration var2 = new DeviceInterfaceModuleConfiguration("Device Interface Module " + c, var1);
      var2.setPwmDevices(this.createPWMList());
      var2.setI2cDevices(this.createI2CList());
      var2.setAnalogInputDevices(this.createAnalogInputList());
      var2.setDigitalDevices(this.createDigitalList());
      var2.setAnalogOutputDevices(this.createAnalogOutputList());
      ++c;
      return var2;
   }

   public LegacyModuleControllerConfiguration buildLegacyModule(SerialNumber var1) {
      ArrayList var2 = this.createLegacyModuleList();
      LegacyModuleControllerConfiguration var3 = new LegacyModuleControllerConfiguration("Legacy Module " + c, var2, var1);
      ++c;
      return var3;
   }

   public MotorControllerConfiguration buildMotorController(SerialNumber var1) {
      ArrayList var2 = this.createMotorList();
      MotorControllerConfiguration var3 = new MotorControllerConfiguration("Motor Controller " + c, var2, var1);
      ++c;
      return var3;
   }

   public ServoControllerConfiguration buildServoController(SerialNumber var1) {
      ArrayList var2 = this.createServoList();
      ServoControllerConfiguration var3 = new ServoControllerConfiguration("Servo Controller " + c, var2, var1);
      ++c;
      return var3;
   }

   public void changeBackground(int var1, int var2) {
      ((LinearLayout)this.activity.findViewById(var2)).setBackgroundColor(var1);
   }

   public void complainToast(String var1, Context var2) {
      Toast var3 = Toast.makeText(var2, var1, 0);
      var3.setGravity(17, 0, 0);
      TextView var4 = (TextView)var3.getView().findViewById(16908299);
      var4.setTextColor(-1);
      var4.setTextSize(18.0F);
      var3.show();
   }

   public void confirmSave() {
      Toast var1 = Toast.makeText(this.activity, "Saved", 0);
      var1.setGravity(80, 0, 50);
      var1.show();
   }

   public ArrayList<DeviceConfiguration> createAnalogInputList() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < 8; ++var2) {
         var1.add(new DeviceConfiguration(var2, DeviceConfiguration.ConfigurationType.ANALOG_INPUT));
      }

      return var1;
   }

   public ArrayList<DeviceConfiguration> createAnalogOutputList() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < 2; ++var2) {
         var1.add(new DeviceConfiguration(var2, DeviceConfiguration.ConfigurationType.ANALOG_OUTPUT));
      }

      return var1;
   }

   public void createConfigFolder() {
      File var1 = new File(CONFIG_FILES_DIR);
      boolean var2 = true;
      if(!var1.exists()) {
         var2 = var1.mkdir();
      }

      if(!var2) {
         RobotLog.log("Can\'t create the Robot Config Files directory!");
         this.complainToast("Can\'t create the Robot Config Files directory!", this.activity);
      }

   }

   public ArrayList<DeviceConfiguration> createDigitalList() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < 8; ++var2) {
         var1.add(new DeviceConfiguration(var2, DeviceConfiguration.ConfigurationType.DIGITAL_DEVICE));
      }

      return var1;
   }

   public ArrayList<DeviceConfiguration> createI2CList() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < 6; ++var2) {
         var1.add(new DeviceConfiguration(var2, DeviceConfiguration.ConfigurationType.I2C_DEVICE));
      }

      return var1;
   }

   public ArrayList<DeviceConfiguration> createLegacyModuleList() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < 6; ++var2) {
         var1.add(new DeviceConfiguration(var2, DeviceConfiguration.ConfigurationType.NOTHING));
      }

      return var1;
   }

   public void createLists(Set<Entry<SerialNumber, DeviceManager.DeviceType>> var1, Map<SerialNumber, ControllerConfiguration> var2) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         DeviceManager.DeviceType var5 = (DeviceManager.DeviceType)var4.getValue();
         switch(null.a[var5.ordinal()]) {
         case 1:
            var2.put(var4.getKey(), this.buildMotorController((SerialNumber)var4.getKey()));
            break;
         case 2:
            var2.put(var4.getKey(), this.buildServoController((SerialNumber)var4.getKey()));
            break;
         case 3:
            var2.put(var4.getKey(), this.buildLegacyModule((SerialNumber)var4.getKey()));
            break;
         case 4:
            DeviceInterfaceModuleConfiguration var6 = this.buildDeviceInterfaceModule((SerialNumber)var4.getKey());
            var2.put(var4.getKey(), var6);
         }
      }

   }

   public ArrayList<DeviceConfiguration> createMotorList() {
      ArrayList var1 = new ArrayList();
      var1.add(new MotorConfiguration(1));
      var1.add(new MotorConfiguration(2));
      return var1;
   }

   public ArrayList<DeviceConfiguration> createPWMList() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < 2; ++var2) {
         var1.add(new DeviceConfiguration(var2, DeviceConfiguration.ConfigurationType.PULSE_WIDTH_DEVICE));
      }

      return var1;
   }

   public ArrayList<DeviceConfiguration> createServoList() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 1; var2 <= 6; ++var2) {
         var1.add(new ServoConfiguration(var2));
      }

      return var1;
   }

   public String getFilenameFromPrefs(int var1, String var2) {
      return this.sharedPreferences.getString(this.activity.getString(var1), var2);
   }

   public String getOutput() {
      return this.e;
   }

   public ArrayList<String> getXMLFiles() {
      File[] var1 = (new File(CONFIG_FILES_DIR)).listFiles();
      ArrayList var2;
      if(var1 == null) {
         RobotLog.i("robotConfigFiles directory is empty");
         var2 = new ArrayList();
      } else {
         var2 = new ArrayList();
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            File var5 = var1[var4];
            if(var5.isFile()) {
               var2.add(var5.getName().replaceFirst("[.][^.]+$", ""));
            }
         }
      }

      return var2;
   }

   public String prepareFilename(String var1) {
      if(var1.toLowerCase().contains("Unsaved".toLowerCase())) {
         var1 = var1.substring(7).trim();
      }

      if(var1.equalsIgnoreCase("No current file!")) {
         var1 = "";
      }

      return var1;
   }

   public void resetCount() {
      c = 1;
   }

   public void saveToPreferences(String value, int preferenceId) {
      String cleaned = value.replaceFirst("[.][^.]+$", "");
      Editor editor = this.sharedPreferences.edit();
      editor.putString(this.activity.getString(preferenceId), cleaned);
      editor.apply();
   }

   public void setOrangeText(String var1, String var2, int var3, int var4, int var5, int var6) {
      LinearLayout var7 = (LinearLayout)this.activity.findViewById(var3);
      var7.setVisibility(0);
      var7.removeAllViews();
      this.activity.getLayoutInflater().inflate(var4, var7, true);
      TextView var9 = (TextView)var7.findViewById(var5);
      TextView var10 = (TextView)var7.findViewById(var6);
      var10.setGravity(3);
      var9.setText(var1);
      var10.setText(var2);
   }

   public void updateHeader(String var1, int var2, int var3, int var4) {
      String var5 = this.sharedPreferences.getString(this.activity.getString(var2), var1).replaceFirst("[.][^.]+$", "");
      ((TextView)this.activity.findViewById(var3)).setText(var5);
      if(var5.equalsIgnoreCase("No current file!")) {
         this.changeBackground(Color.parseColor("#bf0510"), var4);
      } else if(var5.toLowerCase().contains("Unsaved".toLowerCase())) {
         this.changeBackground(-12303292, var4);
      } else {
         this.changeBackground(Color.parseColor("#790E15"), var4);
      }
   }

   public void writeToFile(String var1) throws RobotCoreException, IOException {
      this.writeXMLFileHandler.writeToFile(this.e, CONFIG_FILES_DIR, var1);
   }

   public boolean writeXML(Map<SerialNumber, ControllerConfiguration> var1) {
      ArrayList var2 = new ArrayList();
      var2.addAll(var1.values());

      try {
         this.e = this.writeXMLFileHandler.writeXml(var2);
      } catch (RuntimeException var5) {
         if(var5.getMessage().contains("Duplicate name")) {
            this.complainToast("Found " + var5.getMessage(), this.activity);
            RobotLog.log("Found " + var5.getMessage());
            return true;
         }
      }

      return false;
   }
}
