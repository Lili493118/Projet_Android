package com.example.projet_aoustin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class Fragment_Preference extends Fragment {
    public Fragment_Preference(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_preference_layout, container, false);
        Log.d("Fragment Preference","onCreateView");
        return rootView;
    }
}
