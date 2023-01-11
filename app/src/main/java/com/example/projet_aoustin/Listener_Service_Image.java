package com.example.projet_aoustin;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface définissant un Listener assurant la communication entre un service et un fragment
 */
public interface Listener_Service_Image {
     void update(ArrayList<Image> ImageList);
     void progress(int progress);
}
