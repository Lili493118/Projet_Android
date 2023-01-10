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

/**
 * Activité principale du projet
 */
public class MainActivity extends AppCompatActivity {
    /** Préference partagée de l'application  */
    SharedPreferences prefs;
    /** String contenant le nom du dossier où sont stokées les images en cas de téléchargement */
    public String dossierDeStockage;

    /**
     * Méthode qui s'execute à la céation de l'activité
     * Cette méthode :
     * <ul>
     *     <li>Assigne des valeurs aux attributs définit précédemment</li>
     *     <li></li>
     * </ul>
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /** Assignation des valeurs aux attributs
         * Definition des préférérence partagées à travers le Preference Manager
         * Définition de l'emplacement de stockage des images à partir des préférences
         * */
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        dossierDeStockage = new String(prefs.getString("emplacement",""));

        /** Définition du Themes à appliquer à partir des préférences */
        GetThemeFromSharedPreference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Récupration, Personalisation et Affichage de la toolbar*/
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(getResources().getColor(color.lightbeige));
        setSupportActionBar(myToolbar);

        //Creation du Fragment manager
        /** Création du Fragment Manager permettant la gestion des fragments */
        FragmentManager monManager = getSupportFragmentManager();

        /** Initialisation : réalisation de la première transaction affichant le fragment de recherche*/
        FragmentTransaction maTransaction = monManager.beginTransaction();
        maTransaction.add(R.id.fragment_container, new Fragment_Recherche(),null).commit();

        /** Définit les actions à réaliser au moment du changement des préférences */
        prefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                switch (key){
                    case "theme":
                        /**
                         * Change le thème
                         * Alerte l'utlisateur d'un redémrrage sera peut-etre necessaire
                         * Re-créaction de l'activité afin de changer le théme */
                        GetThemeFromSharedPreference();
                        Toast.makeText(getApplicationContext(), "Si le theme ne change pas, redémarrer l'application", Toast.LENGTH_LONG).show();
                        recreate();
                        break;
                    case "telecharger":
                        /** Si l'enrengistrement devient autorisé lors du changement */
                        /**
                         * Alerte l'utlisateur qu'une permission supplémentaire (MANAGE_ALL_FILES) est requise
                         * Commence une nouvelle activité permettant à l'utlisateur d'autorisé l'enrengistrement des images
                         * */
                        if((boolean) sharedPreferences.getAll().get(key)){
                            if (Build.VERSION.SDK_INT >= 30){
                                if (!Environment.isExternalStorageManager()){
                                    Toast.makeText(getApplicationContext(), "Une permission est requise pour cette fonctionalitée", Toast.LENGTH_LONG).show();
                                    Intent getpermission = new Intent();
                                    getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                    startActivity(getpermission);
                                }
                            }
                            /** Récupération et enrengistrement des images se trouvant déja en favorites */
                            MyDatabase myDatabase = new MyDatabase(getApplicationContext());
                            ArrayList<Image> ImageList = myDatabase.readData(true);
                            for (Image i : ImageList){
                                i.saveimage(getApplicationContext(),prefs.getString("emplacement",""));
                            }
                        }
                        break;
                    case "emplacement":
                        /** Renommage du dossier comprenant les images téléchargées
                         * dossierDeStockage = ancier nom du dossier
                         * clé emplacement des préférences = nouveau nom du dossier
                         * */
                        String root = Environment.getExternalStorageDirectory().toString();
                        File myDir = new File(root + dossierDeStockage );
                        if (!myDir.exists()) {
                            myDir.mkdirs();
                        }
                        myDir.renameTo(new File(root + sharedPreferences.getString("emplacement","")));
                        /** Actialisation de la variable aprés le changement */
                        dossierDeStockage = sharedPreferences.getString("emplacement","");
                        break;
                }
            }
        });
    }

    /**
     * Change le thème de l'application à partir des préférences
     */
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

    /**
     * Indique et Réalise les actions à faire lors de la sélection des items de la toolbar
     * Indique vers quel fragment se diriger selon l'item de la toolbar séléctioné
     * @param item MenuItem
     * @return true si l'option existe
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /** Indique vers quel fragment se diriger selon l'item de la toolbar séléctioné */
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

    /**
     * Crée le menu de la toolbar à partir du fichier menu_content.xml (contenant les items du menu)
     * @param menu Menu
     * @return toujours true
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content,menu);
        return true;
    }
}