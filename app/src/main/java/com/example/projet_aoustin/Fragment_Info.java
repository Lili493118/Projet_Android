package com.example.projet_aoustin;

import static android.content.Context.BIND_AUTO_CREATE;

import android.content.Intent;
import android.os.Bundle;
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
                if( isChecked){
                    myDatabase.insertData(image);
                    Toast.makeText(getContext(), "added to favorite", Toast.LENGTH_SHORT).show();
                }
                else{
                    //suppression de l'image
                    myDatabase.deleteData(image);
                    Toast.makeText(getContext(), "removed from favorite", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }
}
