package com.example.projet_aoustin;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

/**
 * Fragment de préférence
 * Affiche et gère les préférences partagées
 */
public class Fragment_Preference extends PreferenceFragmentCompat {

    /**
     * Méthode qui se réalise lors de la création de ce fragment
     * Met en place les préférences suivant les indications contenues dans le fichier mes_preferences
     * @param savedInstanceState Bundle
     * @param rootKey String
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.mes_preferences, rootKey);
    }
}
