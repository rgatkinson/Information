package com.qualcomm.analytics;

import android.content.SharedPreferences$Editor;
import java.util.ArrayList;
import java.net.MalformedURLException;
import java.io.File;
import com.qualcomm.robotcore.util.RobotLog;
import java.net.URL;
import android.os.AsyncTask;

private class a extends AsyncTask
{
    protected Object doInBackground(final Object[] array) {
        if (Analytics.this.isConnected()) {
            File file;
            try {
                final URL url = new URL(Analytics.a);
                if (Analytics.getDateFromTime(Analytics.b).equals(Analytics.getDateFromTime(Analytics.this.h.getLong("last_upload_date", Analytics.this.j)))) {
                    return null;
                }
                final String ping = Analytics.ping(url, Analytics.a(Analytics.this, "cmd", "=", "ping"));
                if (ping == null || !ping.contains("\"rc\": \"OK\"")) {
                    RobotLog.e("Analytics: Ping failed.");
                    return null;
                }
                RobotLog.i("Analytics ping succeeded.");
                final String string = Analytics.EXTERNAL_STORAGE_DIRECTORY_PATH + ".ftcdc";
                file = new File(string);
                if (!file.exists()) {
                    Analytics.this.handleCreateNewFile(string, Analytics.this.incrementAndSetCount(string, Analytics.getDateFromTime(Analytics.b)));
                }
                final String fromFile = Analytics.this.readFromFile(file);
                final ArrayList<DateCount> dateCountFile = Analytics.parseDateCountFile(fromFile);
                final String call = Analytics.call(url, Analytics.this.updateStats(Analytics.c.toString(), dateCountFile, Analytics.this.e));
                if (call == null || !call.contains("\"rc\": \"OK\"")) {
                    RobotLog.e("Analytics: Upload failed.");
                    if (dateCountFile.size() > Analytics.MAX_ENTRIES_SIZE) {
                        Analytics.this.handleCreateNewFile(string, Analytics.a(fromFile, Analytics.MAX_ENTRIES_SIZE - Analytics.TRIMMED_SIZE));
                        RobotLog.i("Analytics trimmed the data file.");
                        return null;
                    }
                    return null;
                }
            }
            catch (MalformedURLException ex) {
                RobotLog.e("Analytics encountered a malformed URL exception");
                return null;
            }
            RobotLog.i("Analytics: Upload succeeded.");
            final SharedPreferences$Editor edit = Analytics.this.h.edit();
            edit.putLong("last_upload_date", Analytics.b);
            edit.apply();
            file.delete();
        }
        return null;
    }
}
