package com.example.projet_aoustin;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
/**
 * Fragment info
 * Affiche une Image et ces informations
 * Permet de mettre une image en favoris
 * Permet de mettre une image en fond d'ecran
 * Permet de se rendre vers le fragment favoris
 */
public class Fragment_Info extends Fragment {
    /** image pour laquelle les infos vont etres affichées*/
    private final Image image;
    /** Vue principale */
    public View rootView;
    /** Préference partagée de l'application  */
    public SharedPreferences prefs;

    /**
     * Constructeur définissant l'image du fragment
     * @param image Image image pour laquelle les infos vont etres affichées
     */
    public Fragment_Info(Image image){
        this.image= image;
    }

    /**
     * Constructeur vide
     */
    public Fragment_Info() {
        this.image = new Image();
    }

    /**
     * Méthode s'executant à la création de la vue du fragment
     * Lit les Images dans la base de données
     * Initialise et remplit les champs informations de l'image
     * Définit les actions lors de l'activation des boutons se trouvant sur la vue
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return rootView vue principale
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*Récupération de la vue principale à partir de l'inflater */
        rootView = inflater.inflate(R.layout.fragment_info, container, false);

        /*Récupération des préférérence partagées à travers le Preference Manager */
        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());

        /*Récupération de la base de données de l'application*/
        MyDatabase myDatabase = new MyDatabase(getContext());

        /*Initialisation du switch définissant le status de l'Image (favoris ou non)*/
        Switch addFavorite = rootView.findViewById(R.id.favorite);
        addFavorite.setChecked(myDatabase.isInDatabase(image));

        /*Remplissage des champs informations */
        ImageView imageView = rootView.findViewById(R.id.image_info);
        imageView.setImageBitmap(image.getBitmap());

        TextView titre = rootView.findViewById(R.id.titre);
        titre.setText(image.getTitre());

        TextView auteur = rootView.findViewById(R.id.auteur);
        auteur.setText(image.getAuteur());

        TextView lien = rootView.findViewById(R.id.lien);
        lien.setText(image.getLien());

        TextView date = rootView.findViewById(R.id.date);
        date.setText(image.getDate());

        TextView description = rootView.findViewById(R.id.description);
        /*Conversion et affichage de la description qui est en format HTML*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            description.setText(Html.fromHtml(image.getDescription(),Html.FROM_HTML_MODE_COMPACT));
        }

        /*Action lors du changement sur le switch de favoris*/
        addFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /**
             * Fonction appélée lors du click sur le bouton
             * Ajoute et Enrengistre l'image/ Retire l'image dans la base de la base de données
             * @param buttonView CompoundButton
             * @param isChecked boolean indiquant le statut du switch
             */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*Si le switch est activé = l'image doit en favorite */
                if(isChecked){
                    /* On ajoute l'image à la base de données*/
                    myDatabase.insertData(image);

                    /*Si la préférence autorise l'enrengistrement des images */
                    if(prefs.getBoolean("telecharger", false)){
                        //enrengistrement de l'image sur le téléphone à l'emplacement se trouvant dans les préférences
                        image.saveimage(getContext(),prefs.getString("emplacement",""));
                    }
                    Toast.makeText(getContext(), "added to favorite", Toast.LENGTH_SHORT).show();
                }
                else{
                    /*Suppression de l'image de la base de données*/
                    myDatabase.deleteData(image);
                    Toast.makeText(getContext(), "removed from favorite", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* Récupération du bouton permettant de mettre l'image en fond d'ecran */
        Button setWallpaper = rootView.findViewById(R.id.wallpaperbutton);

        /* Rendre le bouton visible ou non visible selon les préférences */
        if( prefs.getBoolean("fond_d_ecran",true)){
            setWallpaper.setVisibility(View.VISIBLE);
        }else{
            setWallpaper.setVisibility(View.GONE);
        }

        /*Action lors du click sur le bouton pour définir l'Image en fond d'ecran*/
        setWallpaper.setOnClickListener(new View.OnClickListener() {
            /**
             * Fonction appelée lors du click sur le bouton permettant de définir l'Image en fond d'ecran
             * Démmarre une nouvelle activité pour changer le fond d'ecran
             * @param v View
             */
            @Override
            public void onClick(View v) {
                /* Démarre une nouvelle activité permettant de recadrer l'Image et de la mettre en fond d'ecran */
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(requireContext());
                Intent intent = new Intent(wallpaperManager.getCropAndSetWallpaperIntent(image.getImageUri(requireContext())));
                startActivity(intent);

                /*Redirection vers le fragment favoris */
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new Fragment_Favoris()).addToBackStack("recherche")
                        .commit();
            }
        });

        /*Action lors du click sur le bouton pour voir les Images favorites*/
        Button goToFavorite = rootView.findViewById(R.id.GoToFavorite);
        goToFavorite.setOnClickListener(new View.OnClickListener() {
            /**
             * Fonction appelée lors du click sur le bouton "Voir mes favoris"
             * Changement de fragment pour le fragment favoris
             * @param v View
             */
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new Fragment_Favoris())
                        .commit();
            }
        });
        return rootView;
    }

}
