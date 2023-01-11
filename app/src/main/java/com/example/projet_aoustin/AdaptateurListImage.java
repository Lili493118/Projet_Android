package com.example.projet_aoustin;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Classe Adaptateur permettant d'afficher une listView complexe composé d'image et de son titre
 */
public class AdaptateurListImage extends ArrayAdapter<Image> {
    /**
     * Constructeur
     * @param context @NonNull Context
     * @param imgs  ArrayList<Image> List contenant les Images (classe contenant une images et un titre) qui devront se trouver dans la liste
     */
    public AdaptateurListImage(@NonNull Context context, ArrayList<Image> imgs) {
        /*L'adapteur se basera sur le layout element_image_list pour afficher chaque item de la liste imgs
        * La listView est composée de plusieurs element_image_list les uns en dessous des autres*/
        super(context, R.layout.element_image_list,imgs);
    }

    /**
     * Fonction affichant les éléments de notre listView
     * @param position int position de l'element textView
     * @param convertView View
     * @param parent ViewGroup
     * @return View convertView
     */
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        /*Récupération de l'Image se trouvant à la position "position"*/
        Image img = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.element_image_list,parent,false);
        }

        /* Mapping des élements de notre Image aux élements de R.layout.element_image_list
        * Set du bitmap de l'Image
        * Set du titre de l'Image*/
        ImageView imageView = convertView.findViewById(R.id.imageViewElement);
        imageView.setImageBitmap(img.getBitmap());

        TextView TextView = convertView.findViewById(R.id.titleimage);
        TextView.setText(img.getTitre());

        return convertView;
    }
}
