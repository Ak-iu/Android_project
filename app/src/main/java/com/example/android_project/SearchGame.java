package com.example.android_project;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class SearchGame {
    private Map<Integer, String> game_map;

    public SearchGame(Map<Integer, String> _game_map) {
        this.game_map = _game_map;
    }

    public HashMap<Integer, String> search(String game_name) {
        HashMap<Integer, String> games_found = new HashMap<>();


        for (Entry<Integer, String> entry : game_map.entrySet()) {
            int appid = entry.getKey();
            String name = entry.getValue();

            if (name.trim().toLowerCase(Locale.ROOT).contains(game_name.trim().toLowerCase(Locale.ROOT)))
                games_found.put(appid, name);
        }

        return games_found;
    }

}
