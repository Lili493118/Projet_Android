package com.example.projet_aoustin;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

public class Fragment_Preference extends PreferenceFragmentCompat {

    public Fragment_Preference() {
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.mes_preferences, rootKey);
    }
}
