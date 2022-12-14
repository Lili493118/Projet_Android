package com.example.projet_aoustin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AdaptateurListImage extends ArrayAdapter<Bitmap> {
    public AdaptateurListImage(@NonNull Context context, ArrayList<Bitmap> imgs) {
        super(context, R.layout.element_image_list,imgs);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Bitmap img = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.element_image_list,parent,false);
        }

        // Ajout de notre image à la vue de notre liste
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewElement);
        imageView.setImageBitmap(img);

        return convertView;
    }
}
