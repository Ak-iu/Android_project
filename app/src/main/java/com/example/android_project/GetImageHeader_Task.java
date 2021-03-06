package com.example.android_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Tâche asynchrone permettant de récupérer la bannière du jeu
 */

public class GetImageHeader_Task extends AsyncTask {
    private final String urlHeader;
    private final DetailsActivity parent;

    /**
     * Constructeur de la Tâche asynchrône
     * @param parent Activité parent de la tâche
     * @param appid  ID du jeu Steam
     */
    public GetImageHeader_Task(DetailsActivity parent, int appid) {
        this.urlHeader = "https://steamcdn-a.akamaihd.net/steam/apps/" + appid + "/header.jpg";
        this.parent = parent;
    }

    /**
     * Obtention de la bannière du jeu Steam via une requête HTTP
     * @param objects null
     * @return Drawable de la bannière du jeu Steam
     */
    @Override
    protected Object doInBackground(Object[] objects) {

        Bitmap bmp = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urlHeader).openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            bmp = BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BitmapDrawable(bmp);
    }

    /**
     * Définie la variable HeaderImage de l'activitée parent
     * @param o null
     */
    @Override
    protected void onPostExecute(Object o) {
        parent.setHeaderImage((Drawable) o);
    }
}
