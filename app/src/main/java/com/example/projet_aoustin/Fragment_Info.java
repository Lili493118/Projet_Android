package com.example.projet_aoustin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment_Info extends Fragment {

    private Image image;
    public View rootView;

    public Fragment_Info(Image image){
        this.image= image;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_info, container, false);
        Log.d("Fragment Info","onCreateView");
        TextView titre = rootView.findViewById(R.id.titre);
        titre.setText(image.titre);
        return rootView;
    }
}
