package com.example.projet_aoustin;

import static android.content.Context.BIND_AUTO_CREATE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Fragment_Info extends Fragment {

    private Image image;
    public View rootView;

    public Fragment_Info(Image image){
        this.image= image;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_info, container, false);
        Log.d("Fragment Info","onCreateView");

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

        Button addFavorite = rootView.findViewById(R.id.favorite);
        addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ajout du lien de l'image dans la base de données
            }
        });
        return rootView;
    }
}
