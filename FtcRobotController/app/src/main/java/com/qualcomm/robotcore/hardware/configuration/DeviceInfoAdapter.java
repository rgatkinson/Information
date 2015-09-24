package com.qualcomm.robotcore.hardware.configuration;

import android.widget.TextView;
import android.view.ViewGroup;
import android.view.View;
import java.util.HashMap;
import android.app.Activity;
import android.content.Context;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.Map;
import android.widget.ListAdapter;
import android.widget.BaseAdapter;

public class DeviceInfoAdapter extends BaseAdapter implements ListAdapter
{
    private Map<SerialNumber, ControllerConfiguration> a;
    private SerialNumber[] b;
    private Context c;
    private int d;
    private int e;
    
    public DeviceInfoAdapter(final Activity c, final int d, final Map<SerialNumber, ControllerConfiguration> a) {
        this.a = new HashMap<SerialNumber, ControllerConfiguration>();
        this.c = (Context)c;
        this.a = a;
        this.b = a.keySet().toArray(new SerialNumber[a.size()]);
        this.d = d;
        this.e = this.e;
    }
    
    public int getCount() {
        return this.a.size();
    }
    
    public Object getItem(final int n) {
        return this.a.get(this.b[n]);
    }
    
    public long getItemId(final int n) {
        return 0L;
    }
    
    public View getView(final int n, View inflate, final ViewGroup viewGroup) {
        if (inflate == null) {
            inflate = ((Activity)this.c).getLayoutInflater().inflate(this.d, viewGroup, false);
        }
        ((TextView)inflate.findViewById(16908309)).setText((CharSequence)this.b[n].toString());
        ((TextView)inflate.findViewById(16908308)).setText((CharSequence)this.a.get(this.b[n]).getName());
        return inflate;
    }
}
