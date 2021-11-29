package com.example.android_project;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class GetGameList_Task extends AsyncTask {

    private final String apiKey = "C3F1DE897195B9FAAA2572D388F90D52";
    private final String urlLink = "https://api.steampowered.com/IStoreService/GetAppList/v1/?key=" + apiKey;
    private Context ctx;
    private int lastID;
    private long last_modified = 0;
    private long max_modified = -1;
    private boolean more_results;
    private boolean internet_error = false;
    private Map<Integer,String> game_map;
    private GameMap gameMap;

    public GetGameList_Task(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            gameMap = GameMap.getInstance();
            gameMap.setWaiting(true);
            game_map = new HashMap<>();
            try {
                game_map = InternalStorage.readMapOnInternalStorage(ctx, "gameMap");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (FileNotFoundException ignored) { //Le fichier n'a pas encore été créé
            }
            lastID = 0;
            more_results = true;
            try {
                last_modified = InternalStorage.readLongOnInternalStorage(ctx, "last_modified");
            } catch (FileNotFoundException ignored) { //Le fichier n'a pas encore été créé
            }
            catch (EOFException e) { //Le fichier est vide
                last_modified = 0;
            }
            do {
                String game_list = getAllGames(lastID, last_modified);
                if (!internet_error) game_map.putAll(getGamesMap(game_list));
            } while (more_results && !internet_error);

            if (!internet_error) {
                System.out.println("Récupération finie");
                last_modified = max_modified;

                // write the map on internal storage
                InternalStorage.writeFileOnInternalStorage(ctx, "gameMap", game_map, Context.MODE_APPEND);
                InternalStorage.writeLongOnInternalStorage(ctx, "last_modified", last_modified, Context.MODE_PRIVATE);
                System.out.println("Map mise en mémoire interne !");
            }
            return null;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        gameMap.setWaiting(false);
        if(internet_error)
            gameMap.notifyErrorListeners();
        else {
            gameMap.setHasMap(true);
            gameMap.copyMap(game_map);
            gameMap.notifyListeners();
        }
    }

    public String getAllGames(int id, long modified_since) throws IOException {
        URL url = new URL(urlLink + "&last_appid=" + id + "&if_modified_since=" + modified_since);
        URLConnection c = url.openConnection();
        try {
            c.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
            return sb.toString();

        } catch (UnknownHostException e) { //No internet connexion
            internet_error = true;
            System.out.println("Internet error");
            //e.printStackTrace();
            return null;
        }
    }

    public Map<Integer, String> getGamesMap(String game_list) throws JSONException {
        Map<Integer, String> game_map = new HashMap<>();
        JSONObject obj = new JSONObject(game_list);
        JSONObject response = obj.getJSONObject("response");
        try {
            JSONArray game_array = response.getJSONArray("apps");

            for (int i = 0; i < game_array.length(); i++) {
                int appid = game_array.getJSONObject(i).getInt("appid");
                String name = game_array.getJSONObject(i).getString("name");
                long last_modified = game_array.getJSONObject(i).getLong("last_modified");
                if (max_modified < last_modified) {
                    max_modified = last_modified;
                }
                game_map.put(appid, name);
            }
            try {
                more_results = response.getBoolean("have_more_results");
                lastID = response.getInt("last_appid");
                System.out.println(lastID);
            } catch (JSONException j) {
                more_results = false;
            }
        } catch (JSONException ignored) {} //The response is empty because there is no new games
        return game_map;
    }

}
