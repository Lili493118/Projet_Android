package com.example.projet_aoustin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Favoris extends Fragment {
    public Fragment_Favoris(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favoris_layout, container, false);
        Log.d("Fragment Favoris","onCreateView");

        MyDatabase myDatabase = new MyDatabase(getContext());

        ArrayList<Image> ImageList = myDatabase.readData();

        ListView listView = rootView.findViewById(R.id.listViewimagefavoris);
        AdaptateurListImage adaptateurListImage = new AdaptateurListImage(listView.getContext(),ImageList);
        listView.post(()-> listView.setAdapter(adaptateurListImage));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Image selectedItem = (Image) parent.getItemAtPosition(position);
                Log.d("ONCLICK",selectedItem.toString());
                MyDatabase myDatabased = new MyDatabase(getContext());

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,new Fragment_Info(selectedItem)).commit();
            }
        });
        return rootView;
    }
}
