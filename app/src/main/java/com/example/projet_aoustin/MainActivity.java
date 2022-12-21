package com.example.projet_aoustin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.projet_aoustin.R.color;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Creation et affichage de la toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(getResources().getColor(color.color3));
        setSupportActionBar(myToolbar);

        //Creation du Fragment manager
        FragmentManager monManager = getSupportFragmentManager();
        // Réalisation de la première transaction affichant le fragment recherche : initialisation
        FragmentTransaction maTransaction = monManager.beginTransaction();
        maTransaction.add(R.id.fragment_container, new Fragment_Recherche(),null).commit();

    }

    //Fonction qui indique selon le bouton choisit dans la toolbar l'action à faire
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragR:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,new Fragment_Recherche()).commit();
                return true;
            case R.id.fragF:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,new Fragment_Favoris()).commit();
                return true;
            case R.id.fragP:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,new Fragment_Preference()).commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Fonction qui creer les options sur la toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content,menu);
        return true;
    }
}