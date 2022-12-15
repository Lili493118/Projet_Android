package com.example.projet_aoustin;

import android.graphics.Bitmap;

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
}
