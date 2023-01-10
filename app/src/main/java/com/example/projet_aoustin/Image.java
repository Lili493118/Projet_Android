package com.example.projet_aoustin;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class Image {
    public String titre;
    public String lien;
    public String date;
    public String description;
    public String auteur;
    public Bitmap bitmap;

    @Override
    public String toString() {
        return "Image{" +
                "titre='" + titre + '\'' +
                ", lien='" + lien + '\'' +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", auteur='" + auteur + '\'' +
                ", bitmap=" + bitmap.toString() +
                '}';
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Uri getImageUri(Context inContext) {
        Bitmap uriImage = this.bitmap;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        uriImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                uriImage, "Title", null);
        return Uri.parse(path);
    }

    public static class Builder{
        private Image image;


        public Builder(){
            this.image = new Image();
        }

        public Builder titre(String titre){
            image.setTitre(titre);
            return this;
        }
        public Builder lien(String lien){
            image.setLien(lien);
            return this;
        }
        public Builder auteur(String auteur){
            image.setAuteur(auteur);
            return this;
        }
        public Builder description(String description){
            image.setDescription(description);
            return  this;
        }
        public Builder date(String date){
            image.setDate(date);
            return this;
        }

        public Builder bitmap(Bitmap bitmap){
            image.setBitmap(bitmap);
            return this;
        }

        public Image build(){
            return image;
        }
    }

    public void saveimage(Context context,String directoryName) { // File name like "image.png"
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + directoryName);
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        String fname = "Image-"+ this.titre +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            this.bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            MediaScannerConnection.scanFile(context, new String[]{file.toString()}, new String[]{file.getName()}, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
