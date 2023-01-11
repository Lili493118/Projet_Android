package com.example.projet_aoustin;

import static java.nio.charset.StandardCharsets.UTF_8;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * Classe Service qui permet de récupérer des images et leurs informations à partir d'un mot clé
 */
public class Service_Image extends Service {
    /** TAG servant au log  */
    private static final String TAG = "Test";
    /** Listener qui permet le transfert d'information vers le fragment grace à la méthode update*/
    private Listener_Service_Image listener_service_image;
    /** Binder permttant de récupérer le service apres son démarrage dans le fragment (communication inter-processus)*/
    private final IBinder binder = new MonBinder();

    /**
     * Binder personnalisé
     * permet de récuprer le service à travers le Binder
     */
    public class MonBinder extends Binder {
        /**
         * Retourne le service
         * @return Service_Image this
         */
        Service_Image getService() {
            return Service_Image.this;
        }
    }

    /**
     * Méthode appelée lors de la création du service
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Création du service");
    }

    /**
     * Methode appelée lors du démarrage du service
     * @param intent Intent
     * @param flags int
     * @param startId int
     * @return super.onStartCommand(intent, flags, startId);
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Démarrage du service");
        /*Récupération du mot clé se trouvant dans les extras */
        Bundle getExtra = intent.getExtras();
        String keyword = getExtra.getString("recherche");

        /*Création d'un Thread récupérant les images en background*/
        new Thread(() -> {
            /*Initialisation des variables */
            int progress = 0;
            URL url ;

            try {
                /*Récupération de l'URL à partir de l'url */
                url = new URL("https://www.flickr.com/services/feeds/photos_public.gne?tags=" + keyword + "&format=json");
                StringBuilder resultStringBuilder = new StringBuilder();

                /*Récupération de la réponse dans resultStringBuilder  */
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream(), UTF_8))) {
                    /*Initialisation de variables*/
                    String str;
                    ArrayList<Image> ImageList = new ArrayList<>();

                    /*Réduction de la réponse afin de supprimer le début qui n'est pas sous la forme JSON*/
                    resultStringBuilder.append(bufferedReader.readLine().substring(15));
                    while ((str = bufferedReader.readLine()) != null) {
                        resultStringBuilder.append(str);
                    }
                    /*Convertir StringBuiler to String*/
                    String resultString = resultStringBuilder.toString();
                    /*Convertir String to JSONObject afin de rendre le parcours plus simple */
                    JSONObject resultJsonObj = new JSONObject(resultString);
                    /*Parcours retournant le tableau d'items*/
                    JSONArray resultArray = resultJsonObj.getJSONArray("items");

                    /*Pour chaque élement du résultat */
                    for (int i = 0; i < resultArray.length(); i++) {
                        Log.d(TAG, resultArray.getJSONObject(i).get("title").toString());
                        /*Initialisation */
                        Bitmap bitmap ;
                        /*Augmentation du progres et appel de la fonction mettant à jour la barre de progression*/
                        listener_service_image.progress(progress++);
                        try {
                            /*Génération du Bitmap en fonction de l'URL*/
                            bitmap = BitmapFactory.decodeStream((InputStream) new URL(
                                    resultArray.getJSONObject(i).getJSONObject("media").get("m").toString()).getContent());

                            /*Construction et ajout d'une Image */
                            ImageList.add(new Image.Builder()
                                    .bitmap(bitmap)
                                    .titre(resultArray.getJSONObject(i).get("title").toString())
                                    .date(resultArray.getJSONObject(i).get("date_taken").toString())
                                    .auteur(resultArray.getJSONObject(i).get("author").toString())
                                    .description(resultArray.getJSONObject(i).get("description").toString())
                                    .lien(resultArray.getJSONObject(i).get("link").toString())
                                    .build()
                            );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    /*Appel de la méthode update qui previent le fragment, celui-ci affiche le résultat */
                    listener_service_image.update(ImageList);

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * Méthode appelé au moment du bind
     * @param intent Intent
     * @return IBinder
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /**
     * Permet d'enrengistrer un listener à ce service
     * @param monListener Listener_Service_Image
     */
    public void setMonListener(Listener_Service_Image monListener) {
        this.listener_service_image = monListener;
    }


}
