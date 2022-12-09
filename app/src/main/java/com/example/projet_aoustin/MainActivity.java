package com.example.projet_aoustin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Creation du Fragment manager
        FragmentManager monManager = getSupportFragmentManager();
        // Réalisation de la première transaction affichant le fragment recherche : initialisation
        FragmentTransaction maTransaction = monManager.beginTransaction();
        maTransaction.add(R.id.fragment_container, new Fragment_Recherche(),null).commit();

        // Gestion des fragments

        /*Button bouttonFrag1 = findViewById(R.id.buttonShowFrag1);
        Button bouttonFrag2 = findViewById(R.id.buttonShowFrag2);

        bouttonFrag1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                monManager.beginTransaction().replace(R.id.mon_fragment_container, new PremierFragment(),null).commit();
            }
        });

        bouttonFrag2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                monManager.beginTransaction().replace(R.id.mon_fragment_container, new DeuxiemeFragment(),null).commit();
            }
        });*/



    }
}