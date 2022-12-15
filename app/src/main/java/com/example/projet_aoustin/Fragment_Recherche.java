package com.example.projet_aoustin;

import static android.content.Context.BIND_AUTO_CREATE;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Recherche extends Fragment {
    private static String TAG = "grenouille";
    //declaration du service qui utilise l'API pour prendre des images
    private Service_Image service_image;
    public View rootView;
    public Fragment_Recherche(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recherche_layout, container, false);
        Log.d("Fragment Recherche","onCreateView");
        //Log.d("Fragment Recherche",getActivity().toString());



        Button searchbutton = rootView.findViewById(R.id.searchbutton);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText InputSearchText = rootView.findViewById(R.id.textinputbar);
                String editTextValue = InputSearchText.getText().toString();
                Log.d("RECHERCHE",editTextValue);
                Intent intent = new Intent(getActivity(), Service_Image.class);
                intent.putExtra("recherche",editTextValue);
                getActivity().bindService(intent, connection, BIND_AUTO_CREATE);
                getActivity().startService(intent);
            }
        });
        return rootView;
    }

    //Service
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG,"onServiceConnected");
            Service_Image.MonBinder binder = (Service_Image.MonBinder) service;
            service_image = binder.getService();
            Log.d(TAG,service_image.test);

            //On enregistre un listener auprès de notre service
            service_image.setMonListener(new Listener_Service_Image() {

                @Override
                public void update(ArrayList<Image> ImageList) {
                    Log.d(TAG, ImageList.toString());
                    ListView listView = rootView.findViewById(R.id.listViewimage);
                    AdaptateurListImage adaptateurListImage = new AdaptateurListImage(listView.getContext(),ImageList);
                    listView.post(()-> listView.setAdapter(adaptateurListImage));
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };


}
