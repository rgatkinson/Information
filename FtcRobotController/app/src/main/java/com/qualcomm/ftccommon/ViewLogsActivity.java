package com.qualcomm.ftccommon;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import android.os.Environment;
import java.io.Serializable;
import java.io.IOException;
import com.qualcomm.robotcore.util.RobotLog;
import android.widget.ScrollView;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.SpannableString;
import android.text.Spannable;
import android.widget.TextView;
import android.app.Activity;

public class ViewLogsActivity extends Activity
{
    public static final String FILENAME = "Filename";
    TextView a;
    int b;
    String c;
    
    public ViewLogsActivity() {
        this.b = 300;
        this.c = " ";
    }
    
    private Spannable a(final String s) {
        int i = 0;
        final SpannableString spannableString = new SpannableString((CharSequence)s);
        final String[] split = s.split("\\n");
        final int length = split.length;
        int n = 0;
        while (i < length) {
            final String s2 = split[i];
            if (s2.contains("E/RobotCore") || s2.contains("### ERROR: ")) {
                ((Spannable)spannableString).setSpan((Object)new ForegroundColorSpan(-65536), n, n + s2.length(), 33);
            }
            n = 1 + (n + s2.length());
            ++i;
        }
        return (Spannable)spannableString;
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_view_logs);
        this.a = (TextView)this.findViewById(R.id.textAdbLogs);
        final ScrollView scrollView = (ScrollView)this.findViewById(R.id.scrollView);
        scrollView.post((Runnable)new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(130);
            }
        });
    }
    
    protected void onStart() {
        super.onStart();
        final Serializable serializableExtra = this.getIntent().getSerializableExtra("Filename");
        if (serializableExtra != null) {
            this.c = (String)serializableExtra;
        }
        this.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                try {
                    ViewLogsActivity.this.a.setText((CharSequence)ViewLogsActivity.this.a(ViewLogsActivity.this.readNLines(ViewLogsActivity.this.b)));
                }
                catch (IOException ex) {
                    RobotLog.e(ex.toString());
                    ViewLogsActivity.this.a.setText((CharSequence)("File not found: " + ViewLogsActivity.this.c));
                }
            }
        });
    }
    
    public String readNLines(final int n) throws IOException {
        Environment.getExternalStorageDirectory();
        final BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(this.c)));
        final String[] array = new String[n];
        int n2 = 0;
        while (true) {
            final String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            array[n2 % array.length] = line;
            ++n2;
        }
        final int n3 = n2 - n;
        int n4 = 0;
        if (n3 >= 0) {
            n4 = n3;
        }
        final int n5 = n4;
        String s = "";
        String string;
        for (int i = n5; i < n2; ++i, s = string) {
            string = s + array[i % array.length] + "\n";
        }
        final int lastIndex = s.lastIndexOf("--------- beginning");
        if (lastIndex < 0) {
            return s;
        }
        return s.substring(lastIndex);
    }
}
