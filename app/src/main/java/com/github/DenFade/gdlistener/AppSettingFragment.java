package com.github.DenFade.gdlistener;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class AppSettingFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.app_setting, rootKey);
    }
}
