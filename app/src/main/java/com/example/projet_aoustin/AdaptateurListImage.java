package com.example.projet_aoustin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdaptateurListImage extends ArrayAdapter<Image> {
    public AdaptateurListImage(@NonNull Context context, ArrayList<Image> imgs) {
        super(context, R.layout.element_image_list,imgs);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Image img = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.element_image_list,parent,false);
        }

        // Ajout de notre image à la vue de notre liste
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewElement);
        imageView.setImageBitmap(img.getBitmap());

        TextView TextView = (android.widget.TextView) convertView.findViewById(R.id.titleimage);
        TextView.setText(img.getTitre());

        return convertView;
    }
}
