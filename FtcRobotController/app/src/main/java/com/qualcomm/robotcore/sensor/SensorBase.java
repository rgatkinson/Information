package com.qualcomm.robotcore.sensor;

import com.qualcomm.robotcore.sensor.SensorListener;
import java.util.List;

public abstract class SensorBase<T> {
   protected List<SensorListener<T>> mListeners;

   public SensorBase(List<SensorListener<T>> var1) {
      this.mListeners = var1;
   }

   public abstract boolean initialize();

   public abstract boolean pause();

   public abstract boolean resume();

   public abstract boolean shutdown();

   public final void update(T param1) {
      // $FF: Couldn't be decompiled
   }
}
