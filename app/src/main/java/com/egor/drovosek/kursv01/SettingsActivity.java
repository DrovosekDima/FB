package com.egor.drovosek.kursv01;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.egor.drovosek.kursv01.Misc.GrabCompletedMatchesRunnable;
import com.egor.drovosek.kursv01.Misc.GrabScheduleRunnable;
import com.egor.drovosek.kursv01.Misc.GrabTeamsRunnable;

import static com.egor.drovosek.kursv01.MainActivity.dataMinerThread;
import static com.egor.drovosek.kursv01.MainActivity.gdSeason;

public class SettingsActivity extends PreferenceActivity implements
        OnSharedPreferenceChangeListener {

    public static final String KEY_PREF_SYNC_INTERVAL =  "sync_interval";
    public static final String KEY_PREF_SEASON = "season";
    public static final String KEY_PREF_NEWSSITE ="news_site";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.preferences_toolbar, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        PreferenceManager.setDefaultValues(this, R.xml.preferences,
                false);
        initSummary(getPreferenceScreen());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        String value;
        if (key.equals(KEY_PREF_SYNC_INTERVAL)) {
            Preference connectionPref = findPreference(key);
            value = sharedPreferences.getString(key, "");
        }else if (key.equals(KEY_PREF_SEASON)) {
            Preference connectionPref = findPreference(key);
            value = sharedPreferences.getString(key, "");
            gdSeason = Integer.valueOf(value);
            Log.i("Settings", "Season was changed to " + value);

            dataMinerThread.postTask(new GrabTeamsRunnable(MainActivity.mUIHandler, getApplicationContext()));

            dataMinerThread.postTask(new GrabScheduleRunnable(MainActivity.mUIHandler, getApplicationContext()));

            dataMinerThread.postTask(new GrabCompletedMatchesRunnable(MainActivity.mUIHandler, getApplicationContext()));
        }

        updatePrefSummary(findPreference(key));
    }

    private void initSummary(Preference p) {
        if (p instanceof PreferenceGroup) {
            PreferenceGroup pGrp = (PreferenceGroup) p;
            for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
                initSummary(pGrp.getPreference(i));
            }
        } else {
            updatePrefSummary(p);
        }
    }

    private void updatePrefSummary(Preference p) {
        if (p instanceof ListPreference) {
            ListPreference listPref = (ListPreference) p;
            p.setSummary(listPref.getEntry());
        }
        if (p instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p;
            if (p.getTitle().toString().toLowerCase().contains("password"))
            {
                p.setSummary("******");
            } else {
                p.setSummary(editTextPref.getText());
            }
        }
        if (p instanceof MultiSelectListPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p;
            p.setSummary(editTextPref.getText());
        }
    }

}
