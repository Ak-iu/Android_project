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

public class GetImageHeader_Task extends AsyncTask {
    private final int appid;
    private final String urlHeader;
    private final DetailsActivity parent;

    public GetImageHeader_Task(DetailsActivity parent, int appid) {
        this.appid = appid;
        this.urlHeader = "https://steamcdn-a.akamaihd.net/steam/apps/" + appid + "/header.jpg";
        this.parent = parent;
    }

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

    @Override
    protected void onPostExecute(Object o) {
        parent.setHeaderImage((Drawable) o);
    }
}
