package com.example.projet_aoustin;

import static android.content.Context.BIND_AUTO_CREATE;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Fragment_Info extends Fragment {

    private Image image;
    public View rootView;
    public SharedPreferences prefs;

    public Fragment_Info(Image image){
        this.image= image;
    }

    public Fragment_Info() {
        this.image = new Image();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_info, container, false);
        Log.d("Fragment Info","onCreateView");

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        //remplissage du switch
        Switch addFavorite = (Switch) rootView.findViewById(R.id.favorite);
        MyDatabase myDatabase = new MyDatabase(getContext());
        addFavorite.setChecked(myDatabase.isInDatabase(image));

        //Remplissage des champs
        ImageView imageView = rootView.findViewById(R.id.image_info);
        imageView.setImageBitmap(image.getBitmap());

        TextView titre = rootView.findViewById(R.id.titre);
        titre.setText(image.getTitre());

        TextView auteur = rootView.findViewById(R.id.auteur);
        auteur.setText(image.getAuteur());

        TextView description = rootView.findViewById(R.id.description);
        description.setText(image.getDescription());

        TextView date = rootView.findViewById(R.id.date);
        date.setText(image.getDate());

        addFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    myDatabase.insertData(image);
                    if((boolean) prefs.getAll().get("telecharger")){
                        image.saveimage(getContext(),prefs.getString("emplacement",""));
                        Log.d("enrengistrement","en cours");
                    }
                    Toast.makeText(getContext(), "added to favorite", Toast.LENGTH_SHORT).show();
                }
                else{
                    //suppression de l'image
                    myDatabase.deleteData(image);
                    Toast.makeText(getContext(), "removed from favorite", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button setWallpaper = rootView.findViewById(R.id.wallpaperbutton);
        if((boolean) prefs.getAll().get("fond_d_ecran")){
            setWallpaper.setVisibility(View.VISIBLE);
        }else{
            setWallpaper.setVisibility(View.GONE);
        }
        setWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getContext());
                Intent intent = new Intent(wallpaperManager.getCropAndSetWallpaperIntent(image.getImageUri(getContext())));
                startActivity(intent);
                //reviens sur la page précedente
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new Fragment_Favoris()).addToBackStack("recherche")
                        .commit();

            }
        });

        Button retour = rootView.findViewById(R.id.retour);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("close","closing");
                //getActivity().onBackPressed();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .remove(Fragment_Info.this)
                        .commit();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return rootView;
    }

}
