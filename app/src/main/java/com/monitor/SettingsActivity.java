package com.monitor;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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
        editTextIp.setSummary(getIPAddress(this));
        editTextPort.setSummary("" + port);


    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        }
        return "0.0.0.0";

    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
}
