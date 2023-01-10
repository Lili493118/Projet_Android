package com.example.projet_aoustin;

import static android.content.Context.BIND_AUTO_CREATE;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * Fragment de recherche 
 * Permet de taper un mot clé 
 * Voir les images correspondantes s'afficher sous forme de listView
 * Voir l'avancé de la récupération des images grace à une barre de progression
 */
public class Fragment_Recherche extends Fragment {
    /** Vue principale */
    public View rootView;

    /**
     * Méthode s'executant à la création de la vue du fragment 
     * Définit les actions à executer lors de l'activation des boutons (démarrage du service, affichage des images)
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return rootView = la vue principale
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Récupération de la vue principale à partir de l'inflater */
        rootView = inflater.inflate(R.layout.fragment_recherche_layout, container, false);

        /* Récupération du bouton de recherche*/
        Button searchButton = rootView.findViewById(R.id.searchbutton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Appel la fonction searchAction, lancement du service de recherche d'image
             * Est appelée lors du click sur le Boutton searchButton 
             * @param v View rootView, vue principale
             */
            @Override
            public void onClick(View v) {
                searchAction();
            }
        });

        /*Récupération du champs de text*/
        EditText editText = rootView.findViewById(R.id.textinputbar);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            /**
             * Lors de l'evenement SEND( click sur la touche entrée du clavier de téléphone) = Lancement du service de recherche d'image
             * Est appelée lors d'un evenement sur le champs de texte 
             * @param v TextView sur lequel l'evenement est réalisé
             * @param actionId int type d'action réalisé sur le TextView
             * @param event KeyEvent type d'action clavier réalisé sur le TextView
             * @return true si l'action a été réalisée
             */
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    searchAction();
                    handled = true;
                }
                return handled;
            }
        });
        return rootView;
    }

    /* Retour du service Service_Image apres son démarrage */
    private final ServiceConnection connection = new ServiceConnection() {

        /**
         * Fonction appélee lorsque la connection avec le service démmaré est réussie et effective
         * @param className className
         * @param service IBinder service
         */
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            /*Récuperétion du service */
            Service_Image.MonBinder binder = (Service_Image.MonBinder) service;
            Service_Image service_image = binder.getService();

            /*Enrengistrement du listener Listener_Service_Image auprès de notre service
             * Les fonctions de ce listener sont appelé par le service et le résultat est récupéré grace à celui-ci
             */
            service_image.setMonListener(new Listener_Service_Image() {

                /**
                 * Permet d'afficher le résultat une liste d'IMAGE 
                 * Est appelée lorsque le service à fini le telechargement des images 
                 * @param ImageList Liste d'Images à afficher 
                 */
                @Override
                public void update(ArrayList<Image> ImageList) {
                    /*Récupération de la listView dans laquelle les Images seront affichées*/
                    ListView listView = rootView.findViewById(R.id.listViewimage);
                    
                    /*Création et assignation d'un adaptateur affichant l'image et le titre d'une Image  */
                    AdaptateurListImage adaptateurListImage = new AdaptateurListImage(listView.getContext(),ImageList);
                    listView.post(()-> listView.setAdapter(adaptateurListImage));
                    
                    /*Action lors de la selection d'un element de la liste listView*/
                    listView.setOnItemClickListener((parent, view, position, id) -> {
                        /*Récuperation de l'Image séléctionnée*/
                        Image selectedItem = (Image) parent.getItemAtPosition(position);

                        /* Lancement du fragment info sur cette image*/
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container,new Fragment_Info(selectedItem)).addToBackStack("recherche").commit();
                    });
                }

                /**
                 * Est appelée lors du téléchargement d'une nouvelle image
                 * Met à jour la barre de progression de téléchargement des images
                 * @param progress entier donnant la progression du téléchargement des images
                 */
                @Override
                public void progress(int progress) {
                    ProgressBar progressBar = rootView.findViewById(R.id.progressBar);
                    progressBar.setProgress(progress);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    /**
     * Récupération du mot clé à partir du champs de texte 
     * Passage du mot-clé par Extra afin de l'utliser dans le service
     * Démarrage du service
     */
    private void searchAction(){
        /* Récupération du mot clé à partir du champs de texte*/
        EditText InputSearchText = rootView.findViewById(R.id.textinputbar);
        String editTextValue = InputSearchText.getText().toString();
        
        /*Définition de l'intent avec la classe du service qui sera démarrée à partir de l'activité courante*/ 
        Intent intent = new Intent(requireActivity(), Service_Image.class);
        
        /* Passage du mot-clé par Extra afin de l'utliser dans le service*/
        intent.putExtra("recherche",editTextValue);
        
        /*Démarrage du service Service_Image */
        requireActivity().bindService(intent, connection, BIND_AUTO_CREATE);
        requireActivity().startService(intent);
    }



}
