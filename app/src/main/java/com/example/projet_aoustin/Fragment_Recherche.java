package com.example.projet_aoustin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class Fragment_Recherche extends Fragment {
    public Fragment_Recherche(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recherche_layout, container, false);
        Log.d("Fragment Recherche","onCreateView");

        Button searchbutton = rootView.findViewById(R.id.searchbutton);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText InputSearchText = rootView.findViewById(R.id.textinputbar);
                String editTextValue = InputSearchText.getText().toString();
                Log.d("RECHERCHE",editTextValue);
            }
        });



        return rootView;
    }


}
