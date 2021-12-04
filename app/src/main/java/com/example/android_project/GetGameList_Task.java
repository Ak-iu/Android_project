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

/**
 * Tâche asynchrone permettant de récupérer la liste des jeux depuis l'api et la mémoire
 */

public class GetGameList_Task extends AsyncTask {

    private final String apiKey = "C3F1DE897195B9FAAA2572D388F90D52";
    private final String urlLink = "https://api.steampowered.com/IStoreService/GetAppList/v1/?key=" + apiKey;
    private final Context ctx;
    private int lastID;
    private long last_modified = 0;
    private long max_modified = -1;
    private boolean more_results;
    private boolean internet_error = false;
    private Map<Integer, String> game_map;
    private GameMap gameMap;

    public GetGameList_Task(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * Récupère la liste des jeux du store Steam
     * @param objects null
     * @return null
     */
    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            gameMap = GameMap.getInstance();
            gameMap.setWaiting(true);
            game_map = new HashMap<>();

            try { //Lecture de la map mise en mémoire
                game_map = InternalStorage.readMapOnInternalStorage(ctx, "gameMap");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (FileNotFoundException ignored) { //Le fichier n'a pas encore été créé
            }
            lastID = 0;
            more_results = true;

            try { //Lecture de la valeur de temps à demander à l'api
                last_modified = InternalStorage.readLongOnInternalStorage(ctx, "last_modified");
            } catch (FileNotFoundException ignored) { //Le fichier n'a pas encore été créé
            } catch (EOFException e) { //Le fichier est vide
                last_modified = 0;
            }
            do { //Récupération des jeux restants
                String game_list = getAllGames(lastID, last_modified);
                if (!internet_error) game_map.putAll(getGamesMap(game_list));
            } while (more_results && !internet_error);

            if (!internet_error) {
                last_modified = max_modified;
                // write the map on internal storage
                InternalStorage.writeFileOnInternalStorage(ctx, "gameMap", game_map, Context.MODE_APPEND);
                InternalStorage.writeLongOnInternalStorage(ctx, "last_modified", last_modified, Context.MODE_PRIVATE);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Met à jour la map globale et notifie ses observateurs
     * @param o null
     */
    @Override
    protected void onPostExecute(Object o) {
        gameMap.setWaiting(false);
        if (internet_error)
            gameMap.notifyErrorListeners();
        else {
            gameMap.setHasMap(true);
            gameMap.copyMap(game_map);
            gameMap.notifyListeners();
        }
    }

    /**
     * Se connecte à l'api et récupère la liste des favoris
     * @param id champ last_appid de la requête
     * @param modified_since champ if_modified_since de la requête
     * @return la réponse de l'api
     * @throws IOException en cas d'erreur de connexion avec l'api
     */
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
            return null;
        }
    }

    /**
     * Récupère la map des jeux depuis une réponse de l'api
     * @param game_list réponse de l'api
     * @return la map des jeux extraite de la réponse
     * @throws JSONException si la réponse n'est pas bien formée
     */
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
                //System.out.println(lastID);
            } catch (JSONException j) {
                more_results = false;
            }
        } catch (JSONException ignored) {
        } //The response is empty because there is no new games
        return game_map;
    }

}
