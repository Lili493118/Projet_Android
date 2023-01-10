package com.example.projet_aoustin;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyDatabase extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION=2;
    private static final String DATABASE_TABLE_NAME="favorite_img";
    private static final String ID="id";
    private static final String TITRE ="image";
    private static final String AUTEUR = "auteur";
    private static final String DATE = "date";
    private static final String BITMAP = "bitmap";
    private static final String LIEN = "lien";
    private static final String DESCRIPTION = "description";




    public MyDatabase(Context context){
        super(context,DATABASE_TABLE_NAME,null,DATABASE_VERSION);
    }

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void onDestroy() {
        SQLiteDatabase db = getReadableDatabase();
        Log.i("MonLog", "Destroy des tables");
        db.execSQL("DROP TABLE IF EXISTS favorite_img;");
        onCreate(db);
    }

    public void insertData(Image image){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put(TITRE,image.getTitre());
        values.put(AUTEUR,image.getAuteur());
        values.put(DATE,image.getDate());
        values.put(BITMAP,image.getBytes());
        values.put(DESCRIPTION,image.getDescription());
        values.put(LIEN,image.getLien());

        db.insertOrThrow(DATABASE_TABLE_NAME,null,values);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public  void deleteData(Image image){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        db.delete(DATABASE_TABLE_NAME, TITRE+"=? and "+AUTEUR+"=? and "+DATE+"=?", new String[]{image.getTitre(),image.getAuteur(),image.getDate()});
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public boolean isInDatabase(Image image){
        String select = new String("SELECT * FROM "+ DATABASE_TABLE_NAME+" WHERE "+TITRE+"=? and "+AUTEUR+"=? and "+DATE+"=?");
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(select,new String[]{image.getTitre(),image.getAuteur(),image.getDate()});
        return cursor.getCount() >0;
    }

    @SuppressLint("Range")
    public ArrayList<Image> readData(boolean order_tri){
        ArrayList<Image> imageList = new ArrayList<>();
        String select = null;
        if(order_tri){
            select = new String("SELECT * FROM "+ DATABASE_TABLE_NAME +" ORDER BY "+ID+" ASC");
        }
        else {
            select = new String("SELECT * FROM "+ DATABASE_TABLE_NAME +" ORDER BY "+ID+" DESC");
        }
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(select,null);
        if(cursor.getCount() > 0 ){
            cursor.moveToFirst();
            do {
                Image image = new Image.Builder()
                        .titre(cursor.getString(cursor.getColumnIndex(TITRE)))
                        .auteur(cursor.getString(cursor.getColumnIndex(AUTEUR)))
                        .date(cursor.getString(cursor.getColumnIndex(DATE)))
                        .bitmap(cursor.getBlob(cursor.getColumnIndex(BITMAP)))
                        .lien(cursor.getString((cursor.getColumnIndex(LIEN))))
                        .description(cursor.getString(cursor.getColumnIndex(DESCRIPTION)))
                        .build();
                imageList.add(image);
            }while (cursor.moveToNext());
        }
        return imageList;
    }



}
