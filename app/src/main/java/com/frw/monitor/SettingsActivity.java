package com.frw.monitor;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import java.util.List;

public class SettingsActivity extends AppCompatPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    EditTextPreference editTextIp;
    EditTextPreference editTextPort;


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        Log.i("test", s);
        if (s.equals("text_ip")) {
            editTextIp.setSummary(sharedPreferences.getString("text_ip", "192.168.0.1"));
        }
        if (s.equals("text_port")) {
            editTextPort.setSummary(sharedPreferences.getString("text_port", "1234"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        editTextIp = (EditTextPreference) findPreference("text_ip");
        editTextPort = (EditTextPreference) findPreference("text_port");

        String ip = PreferenceManager.getDefaultSharedPreferences(this).getString("text_ip", "192.168.0.1");
        int port = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("text_port", "1234"));
        editTextIp.setSummary(ip);
        editTextPort.setSummary(""+port);


    }


}
