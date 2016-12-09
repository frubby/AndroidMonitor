package com.monitor;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.util.Log;

import com.frw.monitor.R;

/**
 * 配置setting
 */
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
