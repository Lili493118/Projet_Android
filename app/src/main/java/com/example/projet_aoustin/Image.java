package com.example.projet_aoustin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Classe définissant l'objet Image
 * Regroupe toutes les informations sur une image
 */
public class Image {
    /** Titre de l'image*/
    public String titre;
    /** Lien vers l'image */
    public String lien;
    /** Date de prise de l'image*/
    public String date;
    /** Déscription fourni par l'auteur de l'image*/
    public String description;
    /** Auteur de l'image*/
    public String auteur;
    /**Image au format Bitmap*/
    public Bitmap bitmap;

    /** TAG servant au log  */
    private static final String TAG = "Test";

    /**
     * Fonction toString permettant un affiche propre d'une Image
     * @return String
     */
    @NonNull
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

    /*Getter et Setter des attributs */
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

    /**
     * Fonction permettant de récupérer l'URI d'une Image ou plus precisement du bitmap de l'Image
     * @param inContext Context de l'application
     * @return URI
     */
    public Uri getImageUri(Context inContext) {
        Bitmap uriImage = this.bitmap;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        uriImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                uriImage, "Title", null);
        return Uri.parse(path);
    }

    /**
     * Classe Builder permttant de construire une Image plus facilement et plus souplement
     */
    public static class Builder{
        private final Image image;


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

        public Builder bitmap(byte[] img){
            image.setBitmap(image.getImage(img));
            return this;
        }

        public Image build(){
            return image;
        }
    }

    /**
     * Fonction permttant d'enrengistrer une image sur le stockage de l'appareil
     * @param context Context
     * @param directoryName String nom du répertoire dans lequel stocké l'Image (bitmap)
     */
    public void saveimage(Context context,String directoryName) {
        /*Récupération ou création du dossier de stockage*/
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + directoryName);
        if (!myDir.exists()) {
            if(!myDir.mkdirs()){
                Log.d(TAG,"Erreur lors de la création du dossier");
            }
        }
        /*Création du fichier vide dans le dossier */
        String fname = "Image-"+ this.titre +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ())
            if(!file.delete ()){
                Log.d(TAG,"Erreur lors de la suppression du fichier");
            }
        try {
            /*Remplissage du fichier avec la bitmap de l'Image*/
            FileOutputStream out = new FileOutputStream(file);
            this.bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            /*Permet de prévenir la galerie photo de l'appareil qu'une nouvelle image a été ajouté et de remplir ses metadata*/
            MediaScannerConnection.scanFile(context, new String[]{file.toString()}, new String[]{file.getName()}, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Fonction permettant de convertir le bitmap de l'Image en tableau de byte
     * necessaire pour l'enrengistrement dans la base de données
     * @return  byte[]
     */
    public byte[] getBytes() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        this.bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    /**
     * Fonction permettant de convertir un tableau de byte en Bitmap
     * Necessaire pour la lecture dans la base de données
     * @param image byte[] tableau de byte contenant une image
     * @return Bitmap
     */
    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
