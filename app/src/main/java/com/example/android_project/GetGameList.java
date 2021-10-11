package com.example.android_project;

import android.os.AsyncTask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GetGameList extends AsyncTask {

    @Override
    protected HashMap<Integer,String> doInBackground(Object[] objects) {
        try {
            String game_list = getAllGames();
            return (HashMap<Integer,String>) getGamesMap(game_list);
        } catch (IOException | JSONException e) {
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

    public Map<Integer,String> getGamesMap(String game_list) throws JsonProcessingException, JSONException {
        HashMap<Integer,String> game_map = new HashMap<Integer,String>();
        JSONObject obj = new JSONObject(game_list);
        JSONArray game_array = obj.getJSONObject("applist").getJSONArray("apps");


       for(int i=0; i< game_array.length(); i++){
           int appid = game_array.getJSONObject(i).getInt("appid");
           String name = game_array.getJSONObject(i).getString("name");
           game_map.put(appid,name);
       }

        return game_map;
    }

}
