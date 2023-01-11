package com.example.projet_aoustin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;

/**
 * Fragment des favoris
 * Affiche les images favorites sous forme de listView dans un ordre définis
 */
public class Fragment_Favoris extends Fragment {
    /** Préference partagée de l'application  */
    public SharedPreferences prefs;

    /**
     * Méthode s'executant à la création de la vue du fragment
     * Lit les Images dans la base de données
     * Affiche les images sous forme de ListView
     * Définit les actions lors de la section d'un élement de la listView
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return rootView vue principale
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*Récupération de la vue principale à partir de l'inflater */
        View rootView = inflater.inflate(R.layout.fragment_favoris_layout, container, false);

        /*Récupération des préférérence partagées à travers le Preference Manager */
        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());

        /*Contact et récupération des Images de la base de données*/
        MyDatabase myDatabase = new MyDatabase(requireContext());
        ArrayList<Image> ImageList = myDatabase.readData((prefs.getBoolean("ordre_tri",true)));

        /*Récupération de la listView dans laquelle les Images seront affichées*/
        ListView listView = rootView.findViewById(R.id.listViewimagefavoris);

        /*Création et assignation d'un adaptateur affichant l'image et le titre d'une Image  */
        AdaptateurListImage adaptateurListImage = new AdaptateurListImage(listView.getContext(),ImageList);
        listView.post(()-> listView.setAdapter(adaptateurListImage));

        /*Action lors de la selection d'un element de la liste listView*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Ouvre le fragment favoris pour l'image sélectionnée
             * @param parent AdapterView<?>
             * @param view View
             * @param position int
             * @param id long
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Récuperation de l'Image séléctionnée*/
                Image selectedItem = (Image) parent.getItemAtPosition(position);

                /* Lancement du fragment info sur cette image*/
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,new Fragment_Info(selectedItem)).addToBackStack("favoris").commit();
            }
        });

        return rootView;
    }
}
