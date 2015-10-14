package com.qualcomm.robotcore.sensor;

import java.util.Iterator;
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

    public final void update(T data) {
        List var2 = this.mListeners;
        synchronized (this.mListeners) {
            if (this.mListeners != null) {
                Iterator var3 = this.mListeners.iterator();

                while (var3.hasNext()) {
                    SensorListener var4 = (SensorListener) var3.next();
                    var4.onUpdate(data);
                }

            }
        }
    }
}
