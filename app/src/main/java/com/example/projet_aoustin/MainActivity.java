package com.example.projet_aoustin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.projet_aoustin.R.color;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs;
    public String dossierDeStockage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        GetThemeFromSharedPreference();
        dossierDeStockage = new String(prefs.getString("emplacement",""));


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
                switch (key){
                    case "theme":
                        GetThemeFromSharedPreference();
                        recreate();
                        Toast.makeText(getApplicationContext(), "Si le theme ne change pas, svp redémarrer l'application", Toast.LENGTH_LONG).show();
                        break;
                    case "telecharger":
                        if((boolean) sharedPreferences.getAll().get(key)){
                            Log.d("key",sharedPreferences.getAll().get(key).toString());
                            if (Build.VERSION.SDK_INT >= 30){
                                if (!Environment.isExternalStorageManager()){
                                    Toast.makeText(getApplicationContext(), "Une permission est requise pour cette fonctionalitée", Toast.LENGTH_LONG).show();
                                    Intent getpermission = new Intent();
                                    getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                    startActivity(getpermission);
                                }
                            }
                            //telechargement des images déja en favorite
                            MyDatabase myDatabase = new MyDatabase(getApplicationContext());
                            ArrayList<Image> ImageList = myDatabase.readData(true);
                            for (Image i : ImageList){
                                i.saveimage(getApplicationContext(),prefs.getString("emplacement",""));
                            }
                        }
                        break;
                    case "emplacement":
                        Log.d("recent","je rename");
                        String root = Environment.getExternalStorageDirectory().toString();
                        File myDir = new File(root + dossierDeStockage );
                        if (!myDir.exists()) {
                            myDir.mkdirs();
                        }
                        myDir.renameTo(new File(root + sharedPreferences.getString("emplacement","")));
                        dossierDeStockage = sharedPreferences.getString("emplacement","");
                        break;
                }
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