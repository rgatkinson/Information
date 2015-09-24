package com.qualcomm.robotcore.sensor;

import java.util.Iterator;
import java.util.List;

public abstract class SensorBase<T>
{
    protected List<SensorListener<T>> mListeners;
    
    public SensorBase(final List<SensorListener<T>> mListeners) {
        this.mListeners = mListeners;
    }
    
    public abstract boolean initialize();
    
    public abstract boolean pause();
    
    public abstract boolean resume();
    
    public abstract boolean shutdown();
    
    public final void update(final T t) {
        synchronized (this.mListeners) {
            if (this.mListeners == null) {
                return;
            }
            final Iterator<SensorListener<T>> iterator = this.mListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onUpdate(t);
            }
        }
    }
    // monitorexit(list)
}
