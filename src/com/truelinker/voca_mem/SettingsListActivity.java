package com.truelinker.voca_mem;

import com.truelinker.R;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;


public class SettingsListActivity extends PreferenceActivity{
	

	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.xml.preference_setting);

   }
    
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Log.e("SettingListActivity","onConfigurationChanged");
	}
    
}