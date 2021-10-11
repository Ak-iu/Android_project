package com.example.android_project;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class GetGameList extends AsyncTask {

    @Override
    protected String doInBackground(Object[] objects) {
        try {
            return getAllGames();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getAllGames() throws IOException {
        URL url = new URL("https://api.steampowered.com/ISteamApps/GetAppList/v2/");
        URLConnection c = url.openConnection();
        c.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        return sb.toString();
    }
}
