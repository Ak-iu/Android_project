package com.example.android_project;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

public class GetMostPlayedGames_Task extends AsyncTask {
    private final String urlMostPlayed = "https://steamspy.com/api.php?request=top100in2weeks";
    private MostPlayedGamesActivity parent;
    private boolean internet_error = false;
    private GameMap gameMap;

    public GetMostPlayedGames_Task(MostPlayedGamesActivity parent) {
        this.parent = parent;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        ArrayList<Integer> gameList = new ArrayList<Integer>();
        try {
            gameList = getGameList();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            internet_error = true;
        }
        return gameList;
    }

    @Override
    protected void onPostExecute(Object o) {
        parent.setGameMPList((ArrayList<Integer>) o);
        parent.setMpListLoaded(true);
        gameMap = GameMap.getInstance();
        if (internet_error)
            gameMap.notifyErrorListeners();
        else{
            gameMap.notifyListeners();
        }


    }

    private ArrayList<Integer> getGameList() throws IOException, JSONException {
        URL url = new URL(urlMostPlayed);
        URLConnection c = url.openConnection();
        c.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        String result = sb.toString();
        JSONObject obj = new JSONObject(result);

        ArrayList<Integer> gameList = new ArrayList<Integer>();
        Iterator<String> keys = obj.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            if (obj.get(key) instanceof JSONObject) {
                gameList.add( Integer.parseInt(key) );
            }
        }

        return gameList;
    }
}
