package com.example.projet_aoustin;

import static java.nio.charset.StandardCharsets.UTF_8;

import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Service_Image extends Service {
    private static String TAG = "grenouille";
    private Listener_Service_Image listener_service_image;
    private final IBinder binder = new MonBinder();
    public String test = "debut";

    public class MonBinder extends Binder {
        Service_Image getService() {
            return Service_Image.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "OnCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        this.test = "fin";
        Bundle getExtra = intent.getExtras();
        String keyword = new String(getExtra.getString("recherche"));

        new Thread(new Runnable() {
            public void run() {
                int progress = 0;

                URL url = null;
                try {
                    url = new URL("https://www.flickr.com/services/feeds/photos_public.gne?tags=" + keyword + "&format=json");
                    StringBuilder builder = new StringBuilder();

                    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream(), UTF_8))) {
                        String str;
                        builder.append(bufferedReader.readLine().substring(15));
                        while ((str = bufferedReader.readLine()) != null) {
                            builder.append(str);
                        }
                        String jsonStr = builder.toString();
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                        //Image image = new Image.Builder().auteur("test").build();
                        ArrayList<Image> ImageList = new ArrayList<>();

                        for (int i = 0; i < jsonObject.getJSONArray("items").length(); i++) {
                            Log.d("reponse", jsonArray.getJSONObject(i).get("title").toString());
                            Bitmap bitmap = null;
                            listener_service_image.progress(progress++);
                            try {
                                bitmap = BitmapFactory.decodeStream((InputStream) new URL(
                                        jsonArray.getJSONObject(i).getJSONObject("media").get("m").toString()).getContent());

                                ImageList.add(new Image.Builder()
                                        .bitmap(bitmap)
                                        .titre(jsonArray.getJSONObject(i).get("title").toString())
                                        .date(jsonArray.getJSONObject(i).get("date_taken").toString())
                                        .auteur(jsonArray.getJSONObject(i).get("author").toString())
                                        .description(jsonArray.getJSONObject(i).get("description").toString())
                                        .lien(jsonArray.getJSONObject(i).get("link").toString())
                                        .build()
                                );
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        listener_service_image.update(ImageList);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setMonListener(Listener_Service_Image monListener) {
        this.listener_service_image = monListener;
    }


}
