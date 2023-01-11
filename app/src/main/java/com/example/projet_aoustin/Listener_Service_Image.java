package com.example.projet_aoustin;


import java.util.ArrayList;

/**
 * Interface définissant un Listener assurant la communication entre un service et un fragment
 */
public interface Listener_Service_Image {
     void update(ArrayList<Image> ImageList);
     void progress(int progress);
}
