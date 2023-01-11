package com.example.projet_aoustin;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Classe définissant une base de données SQLite
 */
public class MyDatabase extends SQLiteOpenHelper {

    /** Version de la base de donnée*/
    public static final int DATABASE_VERSION=2;
    /**Nom de la table d'image */
    private static final String DATABASE_TABLE_NAME="favorite_img";
    /**Identifiant d'une entrée*/
    private static final String ID="id";
    /** Titre d'une image */
    private static final String TITRE ="image";
    /** Auteur d'une image */
    private static final String AUTEUR = "auteur";
    /** Date de prise d'une image*/
    private static final String DATE = "date";
    /**Image en format Bitmap*/
    private static final String BITMAP = "bitmap";
    /** Lien de l'image */
    private static final String LIEN = "lien";
    /** Description de l'image fourni par l'auteur */
    private static final String DESCRIPTION = "description";


    /**
     * Constructeur
     * @param context Context de l'application
     */
    public MyDatabase(Context context){
        super(context,DATABASE_TABLE_NAME,null,DATABASE_VERSION);
    }

    /**
     * Fonction exécutée à la création de la base de données
     * Crée la table et ses champs
     * @param db SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String DATABASE_TABLE_CREATE="CREATE TABLE "+ DATABASE_TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY," +
                TITRE + " TEXT, " +
                AUTEUR + " TEXT," +
                LIEN + " TEXT," +
                DESCRIPTION + " TEXT," +
                DATE + " TEXT,"
                + BITMAP + " BLOB);";
        db.execSQL(DATABASE_TABLE_CREATE);
    }

    /**
     * Méthode appelée en cas de mise à jour
     * Supression de la base de données
     * @param db SQLiteDatabase
     * @param oldVersion oldVersion
     * @param newVersion newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME);
        onCreate(db);
    }

    /**
     * Fonction permettant d'inserer une Image (objet) dans la base de données
     * @param image Image à insérer
     */
    public void insertData(Image image){
        /*Récupération de la base de données */
        SQLiteDatabase db = getWritableDatabase();
        /*Début de la transaction*/
        db.beginTransaction();

        /*Remplissage de la ligne avec les valeurs de l'Image*/
        ContentValues values = new ContentValues();
        values.put(TITRE,image.getTitre());
        values.put(AUTEUR,image.getAuteur());
        values.put(DATE,image.getDate());
        values.put(BITMAP,image.getBytes());
        values.put(DESCRIPTION,image.getDescription());
        values.put(LIEN,image.getLien());

        /*insertion dans la table de la base de données et fin de la transaction*/
        db.insertOrThrow(DATABASE_TABLE_NAME,null,values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * Methode permettant de supprimer une Image(object) de la base de données
     * @param image Image à supprimer de la base de données
     */
    public  void deleteData(Image image){
        /*Récupération de la base de données */
        SQLiteDatabase db = getWritableDatabase();
        /*Début de la transaction*/
        db.beginTransaction();

        /*Suppression de l'Image*/
        db.delete(DATABASE_TABLE_NAME, TITRE+"=? and "+AUTEUR+"=? and "+DATE+"=?", new String[]{image.getTitre(),image.getAuteur(),image.getDate()});
        /* fin de transaction */
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * Méthode permettant de vérifier si une Image(obj) se trouve déja dans la base de données
     * @param image Image que l'on veut tester
     * @return true si l'Image est déja dans la base de données, faux sinon
     */
    public boolean isInDatabase(Image image){
        /*Perparation de la requete recuperant de la ligne dans la base de données avec 3 parametres corrélés*/
        String select = "SELECT * FROM "+ DATABASE_TABLE_NAME+" WHERE "+TITRE+"=? and "+AUTEUR+"=? and "+DATE+"=?";

        /*Execution de la requete et comptage du nombre de résultat*/
        SQLiteDatabase db = getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(select,new String[]{image.getTitre(),image.getAuteur(),image.getDate()});
        return cursor.getCount() >0;
    }

    /**
     * Méthode permettant de lire les Images dans la base de données dans un ordre défini
     * @param order_tri indique l'ordre de tri des résultats
     * @return  ArrayList<Image> liste d'Image se trouvant dans la base de données
     */
    @SuppressLint("Range")
    public ArrayList<Image> readData(boolean order_tri){
        /* Initilisation des variables*/
        ArrayList<Image> imageList = new ArrayList<>();
        String select ;

        /*Préparation des requetes de récupération du contenu de la base de données en fonction de l'ordre de tri*/
        if(order_tri){
            select = "SELECT * FROM "+ DATABASE_TABLE_NAME +" ORDER BY "+ID+" ASC";
        }
        else {
            select = "SELECT * FROM "+ DATABASE_TABLE_NAME +" ORDER BY "+ID+" DESC";
        }

        /*Execution de la requete*/
        SQLiteDatabase db = getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(select,null);
        /*Action pour chaque éléments récuperés(chaque ligne de la base de données ici */
        if(cursor.getCount() > 0 ){
            cursor.moveToFirst();
            do {
                /* Création d'une Image avec les champs récupérés de la base de données*/
                Image image = new Image.Builder()
                        .titre(cursor.getString(cursor.getColumnIndex(TITRE)))
                        .auteur(cursor.getString(cursor.getColumnIndex(AUTEUR)))
                        .date(cursor.getString(cursor.getColumnIndex(DATE)))
                        .bitmap(cursor.getBlob(cursor.getColumnIndex(BITMAP)))
                        .lien(cursor.getString((cursor.getColumnIndex(LIEN))))
                        .description(cursor.getString(cursor.getColumnIndex(DESCRIPTION)))
                        .build();
                /*Ajout de l'Image à la liste */
                imageList.add(image);
            }while (cursor.moveToNext());
        }
        return imageList;
    }
}
