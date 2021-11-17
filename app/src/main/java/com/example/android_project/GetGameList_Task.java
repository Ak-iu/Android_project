package com.example.android_project;

import android.content.Context;
import android.os.AsyncTask;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class GetGameList_Task extends AsyncTask {

    private final String apiKey = "C3F1DE897195B9FAAA2572D388F90D52";
    private final String urlLink = "https://api.steampowered.com/IStoreService/GetAppList/v1/?key=" + apiKey;
    private int lastID;
    private boolean more_results;
    private InternalStorage istor = new InternalStorage();

    private Context ctx;

    public GetGameList_Task(Context _ctx) {
        this.ctx = _ctx;
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            lastID = 0;
            more_results = true;
            HashMap<Integer, String> game_map = new HashMap<>();
            do {
                String game_list = getAllGames(lastID);
                game_map.putAll(getGamesMap(game_list));
            } while (more_results);

            // write the map on internal storage
            istor.writeFileOnInternalStorage(ctx,"gameMap",game_map);
            System.out.println("Map mis en mémoire interne! ");

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAllGames(int id) throws IOException {
        URL url = new URL(urlLink + "&last_appid=" + id);
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

    public HashMap<Integer, String> getGamesMap(String game_list) throws JSONException {
        HashMap<Integer, String> game_map = new HashMap<>();
        JSONObject obj = new JSONObject(game_list);
        JSONObject response = obj.getJSONObject("response");
        JSONArray game_array = response.getJSONArray("apps");

        for (int i = 0; i < game_array.length(); i++) {
            int appid = game_array.getJSONObject(i).getInt("appid");
            String name = game_array.getJSONObject(i).getString("name");
            game_map.put(appid, name);
        }
        try {
            more_results = response.getBoolean("have_more_results");
            lastID = response.getInt("last_appid");
            System.out.println(lastID);
        } catch (JSONException j) {
            more_results = false;
        }
        return game_map;
    }
}
