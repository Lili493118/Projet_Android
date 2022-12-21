package com.example.projet_aoustin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.projet_aoustin.R.color;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        GetThemeFromSharedPreference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Creation et affichage de la toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(getResources().getColor(color.lightbeige));
        setSupportActionBar(myToolbar);

        //Creation du Fragment manager
        FragmentManager monManager = getSupportFragmentManager();
        // Réalisation de la première transaction affichant le fragment recherche : initialisation
        FragmentTransaction maTransaction = monManager.beginTransaction();
        maTransaction.add(R.id.fragment_container, new Fragment_Recherche(),null).commit();

        prefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                GetThemeFromSharedPreference();
                recreate();
            }
        });

    }

    private void GetThemeFromSharedPreference() {
        String theme_number = prefs.getString("theme","");
        switch (theme_number){
            case "1":
                this.setTheme(R.style.Theme_theme1);
                break;
            case "2":
                this.setTheme(R.style.Theme_theme2);
                break;
            case "3":
                this.setTheme(R.style.Theme_theme3);
                break;
        }

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